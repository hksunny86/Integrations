package com.inov8.microbank.server.service.AllpayModule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.distributormodule.DistributorContactFormModel;
import com.inov8.microbank.common.model.distributormodule.DistributorContactListViewModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.DistributorConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SMSConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactListViewDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.webapp.action.OLAAccountModule.OLAAccountFormController;
import com.inov8.microbank.webapp.action.distributormodule.DistributorContactFormController;
import com.inov8.microbank.webapp.action.portal.linkpaymentmodemodule.LinkPaymentModeFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;

public class AllpayDistributorAccountManagerImpl implements
		AllpayDistributorAccountManager {
	private SmsSender smsSender;
	FinancialInstitution olaVeriflyFinancialInstitution;
	private DistributorContactListViewDAO distributorContactListViewDAO;
private DistributorContactDAO distributorContactDAO;
private AppUserDAO appUserDAO ;

	private ActionLogManager actionLogManager;

	private UserDeviceAccountsManager userDeviceAccountsManager;
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;
	private LinkPaymentModeDAO linkPaymentModeDAO;

	private DistributorContactFormController distributorContactFormController;

	private OLAAccountFormController accountForm;

	private LinkPaymentModeFormController linkPaymentModeFormController;

	private UserDeviceAccountsDAO userDeviceAccountsDAO;

	private SmartMoneyAccountDAO smartMoneyAccountDAO;

	private BankDAO bankDAO;

	private FinancialIntegrationManager financialIntegrationManager;
	
	public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException 
	{
		AppUserPartnerGroupModel appUserPartnerGroupModel = new	AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserId);
		
		

		CustomList list = this.appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		if(list!=null && list.getResultsetList().size()>0)
		{
		appUserPartnerGroupModel =(AppUserPartnerGroupModel)list.getResultsetList().get(0);
		return appUserPartnerGroupModel.getPartnerGroupId();
		}
		else
			throw new FrameworkCheckedException("User doest not belong to any partner group");	
		
		
	}


	public ModelAndView createAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		DistributorContactFormModel madel = (DistributorContactFormModel)object;
		madel.setAccountNick("ola_"+madel.getAllpayId());//because allpay id would also be the nick of that account
		
		String isUpdate=(String)httpServletRequest.getAttribute("isUpdate");
		if (!"y".equals(httpServletRequest.getAttribute("isUpdate"))) {
			if (!headContactExists(madel)) {
				throw new FrameworkCheckedException(DistributorConstants.HEAD_CHK);
			}
		}
		ModelAndView mov = distributorContactFormController.onCreate(
				httpServletRequest, httpServletResponse, object, bindException);
		// fillup the baseWrapper
		BaseWrapper baseWrapper = (BaseWrapper) httpServletRequest
				.getAttribute("baseWrapper");

		baseWrapper.putObject("distributorContactModel", baseWrapper
				.getBasePersistableModel());
		// step 2 ----creation of user device account
		this.createUserDeviceAccount(baseWrapper,httpServletRequest);
		// step 3 ----- smart money account
		createLinkPaymentMode(baseWrapper, httpServletRequest,
				(DistributorContactFormModel) object);
		// step 4 ------ OLA Account Creation
		baseWrapper.putObject("bankModel", getOlaBankMadal());
		baseWrapper=createOlaAccount(httpServletRequest, httpServletResponse, object,
				bindException, baseWrapper);
		if (!"y".equals(httpServletRequest.getAttribute("isUpdate"))){
		createVeriflyAccount(httpServletRequest, httpServletResponse, object,
				bindException, baseWrapper);
		}
		return mov;
	}
	private boolean headContactExists(DistributorContactFormModel madel) {
		if (madel.getHead()!=null &&  madel.getHead().equals ("true")) {
			return true;
		} else {
			DistributorContactModel dcm = new DistributorContactModel();
			dcm.setDistributorId(Long.valueOf(madel.getDistributorId()));
			dcm.setHead(true);
			List<DistributorContactModel> list = this.distributorContactDAO.findByExample(dcm).getResultsetList();
			if (list.size() == 0) {
				return false;
			} else {
				return true;
			}
		}
	}
	private void createVeriflyAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException, BaseWrapper baseWrapper)
			throws Exception {
		AbstractFinancialInstitution abstractFinancialInstitution = (AbstractFinancialInstitution) baseWrapper
				.getObject("abstractFinancialInstitution");
		LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) baseWrapper
				.getObject("linkPaymentModeModel");
		UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper
				.getObject("userDeviceAccountsModel");
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper
				.getObject("smartMoneyAccountModel");
		AccountInfoModel accountInfoModel = crAccountModel(
				(DistributorContactFormModel) baseWrapper
						.getObject("distributorContactFormModel"),
				userDeviceAccountsModel, smartMoneyAccountModel,(OLAVO)baseWrapper.getObject("olaVo"));
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

		// VeriflyManager veriflyMgr =
		// veriflyManagerService.getVeriflyMgrByBankId(bankModel);
		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
		LogModel logmodel = new LogModel();
		logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
		veriflyBaseWrapper.setLogModel(logmodel);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
				DeviceTypeConstantsInterface.WEB);
		veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
				DeviceTypeConstantsInterface.WEB);

		veriflyBaseWrapper = abstractFinancialInstitution
				.generatePin(veriflyBaseWrapper);

		if (veriflyBaseWrapper.isErrorStatus()) {

			Object[] args = {
					veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
					veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
					veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() };
			// messageString =
			// MessageUtil.getMessage("linkPaymentMode.successMessage",
			// args);
			/**
			 * Phoenix call if verifly Lite (bank) is required. And First
			 * Account is being created.
			 */
		}

	}

	private BaseWrapper createOlaAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException, BaseWrapper baseWrapper)
			throws Exception {
		DistributorContactFormModel dcm = (DistributorContactFormModel) object;
		OLAVO olaVo = new OLAVO();
		if ("y".equals(httpServletRequest.getAttribute("isUpdate"))){
			olaVo.setCnic( dcm.getOldNic());
			SwitchWrapper sWrapper = new SwitchWrapperImpl ();			
			sWrapper.setBankId(getOlaBankMadal().getBankId());
			sWrapper.setOlavo(olaVo);			
			 sWrapper= olaVeriflyFinancialInstitution.getAccountInfo(sWrapper);
			 olaVo = sWrapper.getOlavo();
			
		}
		
		
		olaVo.setFirstName(dcm.getFirstName());
		olaVo.setMiddleName(dcm.getMiddleName());
		olaVo.setLastName(dcm.getLastName());
		olaVo.setFatherName(dcm.getFatherName());
		olaVo.setCnic(dcm.getNic());
		olaVo.setAddress(dcm.getAddress1());
		olaVo.setLandlineNumber(dcm.getLandlineNumber());
		olaVo.setMobileNumber(dcm.getMobileNo());
		olaVo.setDob(dcm.getDob());
		olaVo.setStatusId(dcm.getAccountStatusId());
		// olaVo.setBalance(dcm.getBalance()); //balance is yet to be decided
		BankModel bankModel = (BankModel) baseWrapper.getObject("bankModel");
		httpServletRequest.setAttribute("bankModel", bankModel);
		if ("y".equals(httpServletRequest.getAttribute("isUpdate"))){
			accountForm.onUpdate(httpServletRequest, httpServletResponse, olaVo,
					bindException);
		}else{
		accountForm.onCreate(httpServletRequest, httpServletResponse, olaVo,
				bindException);
		}
		olaVo = (OLAVO) httpServletRequest.getAttribute("olaVo");
		if ("07".equals(olaVo.getResponseCode())){
			httpServletRequest.setAttribute("olaExceptionMessage", "NIC already exisits in the OLA accounts");
			throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
		}
			
		baseWrapper.putObject("olaVo", olaVo);
		return baseWrapper;
		
		

	}

	private AccountInfoModel crAccountModel(
			DistributorContactFormModel distributorContactFormModel,
			UserDeviceAccountsModel userDeviceAccountsModel,
			SmartMoneyAccountModel smartMoneyAccountModel, OLAVO olavo)
			throws FrameworkCheckedException {

		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setAccountNo(olavo.getPayingAccNo().toString());
		accountInfoModel.setAccountNick(distributorContactFormModel.getAccountNick());
		accountInfoModel.setActive(smartMoneyAccountModel.getActive());

		if (distributorContactFormModel.getExpiryDate() != null) {
			accountInfoModel.setCardExpiryDate(PortalDateUtils.formatDate(
					distributorContactFormModel.getExpiryDate(), "MM/yy"));
		}
		accountInfoModel.setCardNo(distributorContactFormModel.getCardNo());
		accountInfoModel.setCardTypeId(distributorContactFormModel
				.getCardType());

		accountInfoModel.setCreatedOn(smartMoneyAccountModel.getCreatedOn());
		accountInfoModel.setUpdatedOn(smartMoneyAccountModel.getUpdatedOn());
		accountInfoModel.setCustomerId(userDeviceAccountsModel.getAppUserId());
		accountInfoModel.setCustomerMobileNo(distributorContactFormModel
				.getMobileNo());
		accountInfoModel.setFirstName(distributorContactFormModel
				.getFirstName());
		accountInfoModel.setLastName(distributorContactFormModel
				.getLastName());
		accountInfoModel.setPaymentModeId(smartMoneyAccountModel
				.getPaymentModeId());

		accountInfoModel.setAccountTypeId(distributorContactFormModel
				.getAccountType());
		accountInfoModel.setCurrencyCodeId(distributorContactFormModel
				.getCurrencyCode());
		accountInfoModel.setDeleted(Boolean.FALSE);

		return accountInfoModel;
	}

	public void createUserDeviceAccount(BaseWrapper baseWrapper,HttpServletRequest httpServletRequest)
			throws FrameworkCheckedException {

		AppUserModel appUserModel = (AppUserModel) baseWrapper
				.getObject("savedAppUserModel");
		DistributorContactFormModel distributorContactFormModel = (DistributorContactFormModel) baseWrapper
				.getObject("distributorContactFormModel");
		UserDeviceAccountsModel userDeviceAccountsModel ;
		String appUserId = httpServletRequest.getParameter("appUserId");
		if ("y".equals(httpServletRequest.getAttribute("isUpdate"))){
			userDeviceAccountsModel = getUserDeviceAccountsModel (appUserId);
			appUserModel = getAppUserModel (appUserId);
		}else{
			userDeviceAccountsModel= new UserDeviceAccountsModel();	
		}
		
		Date nowDate = new Date();
		
		userDeviceAccountsModel.setAccountEnabled(distributorContactFormModel
				.getAccountEnabled() == null ? false : new Boolean(
				distributorContactFormModel.getAccountEnabled()));
		userDeviceAccountsModel.setAccountExpired(distributorContactFormModel
				.getAccountExpired() == null ? false : new Boolean(
				distributorContactFormModel.getAccountExpired()));
		userDeviceAccountsModel.setAccountLocked(distributorContactFormModel
				.getAccountLocked() == null ? false : new Boolean(
				distributorContactFormModel.getAccountLocked()));
		userDeviceAccountsModel
				.setCredentialsExpired(distributorContactFormModel
						.getCredentialsExpired() == null ? false : new Boolean(
						distributorContactFormModel.getCredentialsExpired()));
		if (!("y".equals(httpServletRequest.getAttribute("isUpdate")))){
		userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils
				.getCurrentUser());
		userDeviceAccountsModel.setCreatedOn(nowDate);
		}
		
		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils
				.getCurrentUser());
		userDeviceAccountsModel.setUpdatedOn(nowDate);
		userDeviceAccountsModel.setPinChangeRequired(true);
		userDeviceAccountsModel
				.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);

		String allPayId = distributorContactFormModel.getAllpayId();

		String randomPin = RandomUtils.generateRandom(4, false, true);

		userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
		userDeviceAccountsModel.setUserId(allPayId);
		if (!("y".equals(httpServletRequest.getAttribute("isUpdate")))){
		userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		}
		userDeviceAccountsModel.setCommissioned(distributorContactFormModel
				.getCommissioned() == null ? false : new Boolean(
				distributorContactFormModel.getCommissioned()));
		userDeviceAccountsModel.setPasswordChangeRequired(false);
		userDeviceAccountsModel = this.userDeviceAccountsDAO
				.saveOrUpdate(userDeviceAccountsModel);

		Object[] args = { allPayId, randomPin };

		String messageString = MessageUtil.getMessage(
				"customer.allpayAccountCreated", args);

		// String messageString = "Dear Customer Your New MWallet Account is
		// created Your MfsID is:"
		// + mfsId + " and Pin is: " + randomPin;
		if (!("y".equals(httpServletRequest.getAttribute("isUpdate")))){
		SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(),
				messageString, SMSConstants.Sender_1611);
		 smsSender.send(smsMessage);
		}
		baseWrapper.putObject("distributorContactFormModel",
				distributorContactFormModel);
		baseWrapper.putObject("userDeviceAccountsModel",
				userDeviceAccountsModel);

	}

	private AppUserModel getAppUserModel(String appUserId) {		
		return this.appUserDAO.findByPrimaryKey(Long.valueOf(appUserId));
	}

	private UserDeviceAccountsModel getUserDeviceAccountsModel(String appUserId) {
		UserDeviceAccountsModel userDeviceAccountsModel =new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(Long.valueOf(appUserId));
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		CustomList cl=   this.userDeviceAccountsDAO.findByExample(  userDeviceAccountsModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		return (UserDeviceAccountsModel)cl.getResultsetList().get(0);
	}

	public SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		DistributorContactModel distributorContactModel = this.distributorContactDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(distributorContactModel);
		return searchBaseWrapper;
	}

	public BaseWrapper loadAccount(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		DistributorContactListViewModel madel = (DistributorContactListViewModel) searchBaseWrapper
		.getBasePersistableModel();
		madel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		CustomList<DistributorContactListViewModel> list = this.distributorContactListViewDAO
				.findByExample(madel
						, searchBaseWrapper
								.getPagingHelperModel(), searchBaseWrapper
								.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public ModelAndView updateAccount(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return createAccount(httpServletRequest, httpServletResponse, object,
				bindException);
	}

	public DistributorContactFormController getDistributorContactFormController() {
		return distributorContactFormController;
	}

	public void setDistributorContactFormController(
			DistributorContactFormController distributorContactFormController) {
		this.distributorContactFormController = distributorContactFormController;
	}

	public UserDeviceAccountsDAO getUserDeviceAccountsDAO() {
		return userDeviceAccountsDAO;
	}

	public void setUserDeviceAccountsDAO(
			UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public LinkPaymentModeFormController getLinkPaymentModeFormController() {
		return linkPaymentModeFormController;
	}

	public void setLinkPaymentModeFormController(
			LinkPaymentModeFormController linkPaymentModeFormController) {
		this.linkPaymentModeFormController = linkPaymentModeFormController;
	}

	public BaseWrapper createLinkPaymentMode(BaseWrapper baseWrapper,
			HttpServletRequest request,
			DistributorContactFormModel distributorContactFormModel)
			throws FrameworkCheckedException {

		long useCaseId = 0;
		long actionId = 0;
		long appUserId = 0;
		if (request.getParameter(PortalConstants.KEY_USECASE_ID) != null) {
			useCaseId = Long.parseLong(request
					.getParameter(PortalConstants.KEY_USECASE_ID));
			System.out.println("usecase id " + useCaseId);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

		}
		if (request.getParameter(PortalConstants.KEY_ACTION_ID) != null) {
			actionId = Long.parseLong(request
					.getParameter(PortalConstants.KEY_ACTION_ID));
			System.out.println("action id " + actionId);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
		}

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel = logBeforeAction(actionId, useCaseId/* ,appUserId */);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		System.out.println("after theread local");

		AbstractFinancialInstitution abstractFinancialInstitution = null;
		VeriflyBaseWrapper veriflyBaseWrapper = null;
		SmartMoneyAccountModel smartMoneyAccountModel = null;
		boolean phoenixSuccessful = false;
		boolean firstSmartMoneyAccount = false;
		try { // Start TRY block
			String errorMessage;
			String messageString;
			// LinkPaymentModeModel linkPaymentModeModel =
			// (LinkPaymentModeModel) baseWrapper
			// .getObject("linkPaymentModeModel");
			UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper
					.getObject("userDeviceAccountsModel");
			userDeviceAccountsModel.setUserId(distributorContactFormModel
					.getAllpayId());
			BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();
			baseWrapperUserDevice
					.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapperUserDevice = userDeviceAccountsManager
					.loadUserDeviceAccount(baseWrapperUserDevice);
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapperUserDevice
					.getBasePersistableModel();

//			// check if customer exist or not
//			if (userDeviceAccountsModel.getAppUserIdAppUserModel() == null) {
//				baseWrapperUserDevice.putObject("ErrMessage",
//						"Allpay ID does not exist");
//				throw new FrameworkCheckedException("Allpay ID does not exist");
//			}
//
//			// customer account enable or disable
//			if (!userDeviceAccountsModel.getAccountEnabled()) {
//				baseWrapperUserDevice.putObject("ErrMessage",
//						"Account is disabled.");
//				throw new FrameworkCheckedException("Account is disabled.");
//			}
//
//			// customer account for expired
//			if (userDeviceAccountsModel.getAccountExpired()) {
//				baseWrapperUserDevice.putObject("ErrMessage",
//						"Account is expired");
//				throw new FrameworkCheckedException("Account is expired");
//			}
//
//			// customer account for locked
//			if (userDeviceAccountsModel.getAccountLocked()) {
//				baseWrapperUserDevice.putObject("ErrMessage",
//						"Account is locked");
//				throw new FrameworkCheckedException("Account is locked");
//			}
//
//			// customer account for credential expired
//			if (userDeviceAccountsModel.getCredentialsExpired()) {
//				baseWrapperUserDevice.putObject("ErrMessage",
//						"Account credential is expired");
//				throw new FrameworkCheckedException(
//						"Account credential is expired");
//			}

			// // Check if this is the first SMA of customer
			// boolean isFirstSMA =
			// isFirstSmartMoneyAccount(userDeviceAccountsModel
			// .getAppUserIdAppUserModel().getCustomerId());

			appUserId = userDeviceAccountsModel.getAppUserId();

			if (userDeviceAccountsModel.getAppUserIdAppUserModel().getNic() == null) {
				userDeviceAccountsModel.getAppUserIdAppUserModel().setNic(
						distributorContactFormModel.getNic());
				baseWrapperUserDevice
						.setBasePersistableModel(userDeviceAccountsModel);
				baseWrapperUserDevice = this.userDeviceAccountsManager
						.updateUserDeviceAccount(baseWrapperUserDevice);
			}

			BankModel bankModel = getOlaBankMadal();
			bankModel.setBankId(bankModel.getBankId());
			// UserUtils.getCurrentUser()
			// .getBankUserIdBankUserModel().getBankId();

			// TODO ..: Code to be changed here for bank :..
			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			baseWrapperBank.setBasePersistableModel(bankModel);
			abstractFinancialInstitution = this.financialIntegrationManager
					.loadFinancialInstitution(baseWrapperBank);
//			String mobileno = userDeviceAccountsModel
//					.getAppUserIdAppUserModel().getMobileNo();

			/**
			 * Smart Money Account is created here
			 */
			// TODO: this smartmoney account creation has to go below verifly
			DistributorContactModel distributorContactModel = (DistributorContactModel) baseWrapper
					.getObject("distributorContactModel");
			smartMoneyAccountModel = createSmartMoneyAccountModel(
					distributorContactFormModel, userDeviceAccountsModel,
					abstractFinancialInstitution.isVeriflyRequired(),
					bankModel, distributorContactModel,(String)request.getAttribute("isUpdate"),request.getParameter("distributorContactId"));
			baseWrapper.putObject("smartMoneyAccountModel",
					smartMoneyAccountModel);
			/**
			 * Everything related the verifly is done in this IF block
			 */
			// if (abstractFinancialInstitution.isVeriflyRequired()
			// && !(PaymentModeConstantsInterface.CASH
			// .equals(linkPaymentModeModel.getPaymentMode()) ||
			// PaymentModeConstantsInterface.CREDIT_REGISTER
			// .equals(linkPaymentModeModel.getPaymentMode()))) {
			//
			// -----------------------------------------------
			// if (abstractFinancialInstitution.isVeriflyLite()) {
			// if (isFirstSMA) {
			// firstSmartMoneyAccount = true;
			// //
			// if(abstractFinancialInstitution.isIVRChannelActive(linkPaymentModeModel.getAccountNo(),
			// // linkPaymentModeModel.getAccountType().toString(),
			// //
			// linkPaymentModeModel.getCurrencyCode().toString(),linkPaymentModeModel.getNic()))
			// // {
			// // String
			// //
			// title=abstractFinancialInstitution.getAccountTitle(linkPaymentModeModel.getAccountNo(),
			// // linkPaymentModeModel.getAccountType().toString(),
			// // linkPaymentModeModel.getCurrencyCode().toString(),
			// // linkPaymentModeModel.getNic());
			//
			// if (abstractFinancialInstitution
			// .activateDeliveryChannel(
			// linkPaymentModeModel
			// .getAccountType()
			// .toString(),
			// linkPaymentModeModel.getNic())) {
			// // messageString =
			// //
			// MessageUtil.getMessage("linkPaymentMode.bankFirstaccount.successMessage",
			// // args);
			// phoenixSuccessful = true;
			//
			// } else {
			// phoenixSuccessful = false;
			// throw new FrameworkCheckedException(
			// WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED);
			//
			// }
			// // }
			// // else
			// // {
			// //
			// // throw new FrameworkCheckedException
			// // ("linkPaymentMode.customerprofiledoesnotexist");
			// // }
			//
			// }
			//
			// /*
			// * else { firstSmartMoneyAccount = false; String
			// *
			// title=abstractFinancialInstitution.getAccountTitle(linkPaymentModeModel.getAccountNo(),
			// * linkPaymentModeModel.getAccountType().toString(),
			// * linkPaymentModeModel.getCurrencyCode().toString(),
			// * linkPaymentModeModel.getNic()); if(title!= null) {
			// * //messageString =
			// *
			// MessageUtil.getMessage("linkPaymentMode.bankaccount.successMessage",
			// * args); phoenixSuccessful = true; } else {
			// * phoenixSuccessful = false; throw new
			// * FrameworkCheckedException
			// * ("linkPaymentMode.customerprofiledoesnotexist");
			// * }
			// *
			// *
			// * }
			// */
			//
			// if (firstSmartMoneyAccount) {
			// if (smartMoneyAccountModel.getPaymentModeId()
			// .longValue() == PaymentModeConstantsInterface.CREDIT_CARD
			// .longValue())
			// messageString = MessageUtil
			// .getMessage(
			// "linkPaymentModeCC.bankFirstaccount.successMessage",
			// args);
			// else
			// messageString = MessageUtil
			// .getMessage(
			// "linkPaymentModeBA.bankFirstaccount.successMessage",
			// args);
			// } else {
			// if (smartMoneyAccountModel.getPaymentModeId()
			// .longValue() == PaymentModeConstantsInterface.CREDIT_CARD
			// .longValue())
			// messageString = MessageUtil
			// .getMessage(
			// "linkPaymentModeCC.bankaccount.successMessage",
			// args);
			// else
			// messageString = MessageUtil
			// .getMessage(
			// "linkPaymentModeBA.bankaccount.successMessage",
			// args);
			//
			// // messageString =
			// //
			// MessageUtil.getMessage("linkPaymentMode.bankaccount.successMessage",
			// // args);
			// }
			//
			// } else {
			// messageString = MessageUtil.getMessage(
			// "linkPaymentMode.successMessage", args);
			//
			// }
			//
			// SmsMessage smsMessage = new SmsMessage(mobileno,
			// messageString);
			//
			// // For ALLPAY users
			// if (null != userDeviceAccountsModel
			// && userDeviceAccountsModel.getCommissioned()) {
			// smsMessage.setMessageText(MessageParsingUtils
			// .parseMessageForIpos(smsMessage
			// .getMessageText()));
			// }
			// smsSender.send(smsMessage);
			//
			// actionLogModel.setCustomField1(String.valueOf(appUserId));
			// logAfterAction(actionLogModel);
			// ThreadLocalActionLog.remove();
			//
			// } else {
			//
			// errorMessage = veriflyBaseWrapper.getErrorMessage();
			// baseWrapper.putObject("ErrMessage", errorMessage);
			//
			// linkPaymentModeDAO.delete(smartMoneyAccountModel);
			//
			// System.out
			// .println(">>>>>>>>>>>>>>> Record not saved in verifly ");
			// throw new FrameworkCheckedException(errorMessage);
			// }
			//
			// }
			//
			// else {
			// // Object[] args =
			// //
			// {UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankIdBankModel().getName()};
			//
			// SmsMessage smsMessage = new SmsMessage(
			// mobileno,
			// MessageUtil
			// .getMessage("linkPaymentMode.nonverifly.successMessage"));
			// if (null != userDeviceAccountsModel
			// && userDeviceAccountsModel.getCommissioned()) {
			// smsMessage.setMessageText(MessageParsingUtils
			// .parseMessageForIpos(smsMessage.getMessageText()));
			// }
			// smsSender.send(smsMessage);
			// }
		} // End TRY Block
		catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException(
					"implementationNotSupportedException");
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException constrainViolationException = (ConstraintViolationException)ex.getCause();
				String constraintName = "UK_USER_DEVICE_TYPE";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1){
					request.setAttribute("UK_USER_DEVICE_TYPE", "Allpay Id already exists");
				}
				constraintName = "UK_SM_ACCOUNT";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1){
					request.setAttribute("UK_USER_DEVICE_TYPE", "Account nick already exists");
				}
			}
			ex.printStackTrace();
			String errMessage = ex.getMessage();
			request.setAttribute("exceptionMessage", errMessage);
			baseWrapper.putObject("ErrMessage", errMessage);
			// if(ex instanceof FrameworkCheckedException){
			// if(ex.getMessage().equals("Error Message. Phoenix activation
			// failed")){
			// // Phoenix activation has failed
			// }
			// else{
			// // Phoenix activation has not failed, somehow other exception has
			// occured,
			// // any way have to deactivate Phoenix.
			// }
			// }

			// This is a tuch to handle the smartmoney account created in case
			// of verifly exception.
			// this is because the transaction is not getting rolled back
			if (smartMoneyAccountModel != null
					&& smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
				System.out
						.println("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
				this.linkPaymentModeDAO
						.deleteByPrimaryKey(smartMoneyAccountModel
								.getSmartMoneyAccountId());
				System.out
						.println("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
			}
			if (/* firstSmartMoneyAccount && */!phoenixSuccessful) {
				// phoenix had a problem, verifly was hit, so rollback verifly.
				// TODO: here code to delete the verifly account
				try {
					abstractFinancialInstitution
							.deleteAccount(veriflyBaseWrapper);
				} catch (Exception exp) {
					ex = exp;
				}
				System.out.println("Phoenix had a problem");
			}

			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}
		baseWrapper.putObject("abstractFinancialInstitution",
				abstractFinancialInstitution);
		return baseWrapper;

	}

	public BankModel getOlaBankMadal() {
		BankModel bankModel = new BankModel();
		bankModel.setFinancialIntegrationId(4L); // 4 is for OLA bank
		CustomList bankList = this.bankDAO.findByExample(bankModel);
		List bankL = bankList.getResultsetList();
		bankModel = (BankModel) bankL.get(0);
		return bankModel;
	}

	public boolean isFirstSmartMoneyAccount(Long customerId) {
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		smartMoneyAccountModel.setCustomerId(customerId);
		smartMoneyAccountModel.setBankId(UserUtils.getCurrentUser()
				.getBankUserIdBankUserModel().getBankId());
		smartMoneyAccountModel.setActive(true);
		CustomList list = this.smartMoneyAccountDAO
				.findByExample(smartMoneyAccountModel);
		if (list.getResultsetList().size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private ActionLogModel logBeforeAction(Long actionId, Long useCaseId/*
																		 * ,Long
																		 * appUserId
																		 */) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId(actionId);
		actionLogModel.setUsecaseId(useCaseId);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the
		// process
		// is
		// starting
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		// actionLogModel.setCustomField1(appUserId.toString());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
		return actionLogModel;
	}

	private void logAfterAction(ActionLogModel actionLogModel)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // the
		// process
		// is
		// starting
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLog(baseWrapperActionLog);
	}

	private SmartMoneyAccountModel createSmartMoneyAccountModel(
			DistributorContactFormModel distributorContactFormModel,
			UserDeviceAccountsModel userDeviceAccountsModel,
			boolean veriflyRequired, BankModel bankModel,
			DistributorContactModel distributorContactModel,String isUpdate,String distributorContactId) throws Exception {
		int recordCount;
		int defRecordCount;

		// TODO ..: Code to be changed here for bank :..
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		// BankModel bankModel = UserUtils.getCurrentUser()
		// .getBankUserIdBankUserModel().getBankIdBankModel();
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager
				.loadFinancialInstitution(baseWrapperBank);

		String bankName = bankModel.getName();

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		if ("y".equals(isUpdate)){
			smartMoneyAccountModel = new SmartMoneyAccountModel ();
			smartMoneyAccountModel.setDistributorContactId(Long.valueOf(distributorContactId));
			smartMoneyAccountModel.setBankId(getOlaBankMadal().getBankId());
			CustomList cList =   this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
			smartMoneyAccountModel = (SmartMoneyAccountModel)cList.getResultsetList().get(0);
			
		}else{
			smartMoneyAccountModel = new SmartMoneyAccountModel();
		}
				smartMoneyAccountModel.setName(distributorContactFormModel
						.getAccountNick());
			

		

		smartMoneyAccountModel
				.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		smartMoneyAccountModel.setBankId(bankModel.getBankId());
		smartMoneyAccountModel.setDistributorContactId(distributorContactModel
				.getDistributorContactId());
		if (!"y".equals(isUpdate)){
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		
		CustomList cList = linkPaymentModeDAO.findByExample(
				smartMoneyAccountModel, null, null, exampleHolder);
		List list = cList.getResultsetList();
		if (!list.isEmpty()) {
			throw new FrameworkCheckedException(
					"This Payment Mode is already linked.");
		}

		// smartMoneyAccountModel
		// .setCardTypeId(linkPaymentModeModel.getCardType());
		smartMoneyAccountModel.setCreatedOn(new Date());
		smartMoneyAccountModel.setUpdatedOn(new Date());
		smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils
				.getCurrentUser());
		smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils
				.getCurrentUser());
		smartMoneyAccountModel.setActive(true);
		if (veriflyRequired) {

			if (abstractFinancialInstitution.isVeriflyLite()) {

				smartMoneyAccountModel.setChangePinRequired(false);
			} else {
				smartMoneyAccountModel.setChangePinRequired(true);
			}
		} else {
			smartMoneyAccountModel.setChangePinRequired(false);
		}
		if (distributorContactFormModel.getCurrencyCode() != null
				&& distributorContactFormModel.getAccountType() != null) {
			smartMoneyAccountModel
					.setCurrencyCodeId(distributorContactFormModel
							.getCurrencyCode());
			smartMoneyAccountModel.setAccountTypeId(distributorContactFormModel
					.getAccountType());
		}
		smartMoneyAccountModel.setDefAccount(true);
		smartMoneyAccountModel.setDeleted(false);

		SmartMoneyAccountModel defaultSmartMoneyAccountModel = new SmartMoneyAccountModel();

		defaultSmartMoneyAccountModel
				.setDistributorContactId(distributorContactModel
						.getDistributorContactId());
		defaultSmartMoneyAccountModel.setDefAccount(true);
		defaultSmartMoneyAccountModel.setActive(true);

		cList = linkPaymentModeDAO.findByExample(defaultSmartMoneyAccountModel,
				null, null, exampleHolder);
		list = cList.getResultsetList();

		if (!list.isEmpty()) {
			smartMoneyAccountModel.setDefAccount(false);
		}

		// first check that if account nick and customer account already exist
		// then throw exception
		SmartMoneyAccountModel testSmartMoneyAccountModel = new SmartMoneyAccountModel();
		testSmartMoneyAccountModel
				.setDistributorContactId(distributorContactModel
						.getDistributorContactId());		
			testSmartMoneyAccountModel.setName(distributorContactFormModel
					.getName());		
		CustomList<SmartMoneyAccountModel> smartMoneyAccountList = linkPaymentModeDAO
				.findByExample(testSmartMoneyAccountModel, null, null,
						exampleHolder);
		List smartMoneyAccountRList = smartMoneyAccountList.getResultsetList();
		if (smartMoneyAccountRList.size() > 0) {
			throw new FrameworkCheckedException(
					"This Payment Mode is already linked.");
		}
		smartMoneyAccountModel.setName(distributorContactFormModel.getAccountNick());
		baseWrapper.setBasePersistableModel(this.linkPaymentModeDAO
				.saveOrUpdate(smartMoneyAccountModel));
	}
		return (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
	}

	public UserDeviceAccountsManager getUserDeviceAccountsManager() {
		return userDeviceAccountsManager;
	}

	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public SmartMoneyAccountDAO getSmartMoneyAccountDAO() {
		return smartMoneyAccountDAO;
	}

	public void setSmartMoneyAccountDAO(
			SmartMoneyAccountDAO smartMoneyAccountDAO) {
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
	}

	public FinancialIntegrationManager getFinancialIntegrationManager() {
		return financialIntegrationManager;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public LinkPaymentModeDAO getLinkPaymentModeDAO() {
		return linkPaymentModeDAO;
	}

	public void setLinkPaymentModeDAO(LinkPaymentModeDAO linkPaymentModeDAO) {
		this.linkPaymentModeDAO = linkPaymentModeDAO;
	}

	public ActionLogManager getActionLogManager() {
		return actionLogManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setAccountForm(OLAAccountFormController accountForm) {
		this.accountForm = accountForm;
	}

	public BankDAO getBankDAO() {
		return bankDAO;
	}

	public void setBankDAO(BankDAO bankDAO) {
		this.bankDAO = bankDAO;
	}

	public OLAAccountFormController getAccountForm() {
		return accountForm;
	}

	public DistributorContactListViewDAO getDistributorContactListViewDAO() {
		return distributorContactListViewDAO;
	}

	public void setDistributorContactListViewDAO(
			DistributorContactListViewDAO distributorContactListViewDAO) {
		this.distributorContactListViewDAO = distributorContactListViewDAO;
	}

	public DistributorContactDAO getDistributorContactDAO() {
		return distributorContactDAO;
	}

	public void setDistributorContactDAO(DistributorContactDAO distributorContactDAO) {
		this.distributorContactDAO = distributorContactDAO;
	}

	public AppUserDAO getAppUserDAO() {
		return appUserDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public FinancialInstitution getOlaVeriflyFinancialInstitution() {
		return olaVeriflyFinancialInstitution;
	}

	public void setOlaVeriflyFinancialInstitution(
			FinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}


	public AppUserPartnerGroupDAO getAppUserPartnerGroupDAO() {
		return appUserPartnerGroupDAO;
	}


	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}
}
