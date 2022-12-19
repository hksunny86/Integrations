package com.inov8.microbank.server.service.AllpayModule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.verifly.common.des.EncryptionHandler;
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
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactListViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.webapp.action.OLAAccountModule.OLAAccountFormController;
import com.inov8.microbank.webapp.action.retailermodule.RetailerContactFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;

public class AllpayRetailerAccountManagerImpl implements AllpayRetailerAccountManager {
	private RetailerContactDAO retailerContactDAO;

	FinancialInstitution olaVeriflyFinancialInstitution;

	private RetailerContactListViewDAO retailerContactListViewDAO;

	private RetailerContactFormController retailerContactFormController;

	private BankDAO bankDAO;

	private SmsSender smsSender;

	private OLAAccountFormController accountForm;

	private UserDeviceAccountsDAO userDeviceAccountsDAO;

	private ActionLogManager actionLogManager;

	private LinkPaymentModeDAO linkPaymentModeDAO;

	private UserDeviceAccountsManager userDeviceAccountsManager;

	private FinancialIntegrationManager financialIntegrationManager;

	private SmartMoneyAccountDAO smartMoneyAccountDAO;

	private AppUserDAO appUserDAO;

	private OracleSequenceGeneratorJdbcDAO sequenceGenerator;

	protected EncryptionHandler encryptionHandler;

	public ModelAndView createAccount(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
		RetailerContactListViewFormModel madel = (RetailerContactListViewFormModel) object;
		if (!"y".equals(httpServletRequest.getAttribute("isUpdate"))) {
			if (!headContactExists(madel)) {
				throw new FrameworkCheckedException(DistributorConstants.HEAD_CHK);
			}
		}
		// ---creation of retailer contact account
//		String allPayId = this.computeAllpayId();
//		madel.setAllpayId(allPayId);
//		madel.setAccountNick("ola_" + allPayId); // because allpay id would also be the nick of that account
		ModelAndView mov = retailerContactFormController.onCreate(httpServletRequest, httpServletResponse, object, bindException);
		BaseWrapper baseWrapper = (BaseWrapper) httpServletRequest.getAttribute("baseWrapper");
		baseWrapper.putObject("retailerContactModel", baseWrapper.getBasePersistableModel());
		// step 2 ----creation of user device account
		this.createUserDeviceAccount(baseWrapper, httpServletRequest);
//		// step 3 ----- smart money account
//		createLinkPaymentMode(baseWrapper, httpServletRequest, (RetailerContactListViewFormModel) object);
//		// step 4 ------ OLA Account Creation
//		baseWrapper.putObject("bankModel", getOlaBankMadal());
//		baseWrapper = createOlaAccount(httpServletRequest, httpServletResponse, object, bindException, baseWrapper);
//		if (!"y".equals(httpServletRequest.getAttribute("isUpdate"))) {
//			createVeriflyAccount(httpServletRequest, httpServletResponse, object, bindException, baseWrapper);
//		}
		return mov;
	}

	private boolean headContactExists(RetailerContactListViewFormModel madel) {
		if (madel.getHead()!=null &&  madel.getHead() == true) {
			return true;
		} else {
			RetailerContactModel rcm = new RetailerContactModel();
			rcm.setRetailerId(madel.getRetailerId());
			rcm.setHead(true);
			List<RetailerContactModel> list = this.retailerContactDAO.findByExample(rcm).getResultsetList();
			if (list.size() == 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	private void createVeriflyAccount(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException, BaseWrapper baseWrapper)
			throws Exception {
		AbstractFinancialInstitution abstractFinancialInstitution = (AbstractFinancialInstitution) baseWrapper.getObject("abstractFinancialInstitution");
		LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) baseWrapper.getObject("linkPaymentModeModel");
		UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getObject("userDeviceAccountsModel");
		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getObject("smartMoneyAccountModel");
		AccountInfoModel accountInfoModel = crAccountModel((RetailerContactListViewFormModel) baseWrapper.getObject("retailerContactListViewFormModel"), userDeviceAccountsModel,
				smartMoneyAccountModel, (OLAVO) baseWrapper.getObject("olaVo"));
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

		// VeriflyManager veriflyMgr =
		// veriflyManagerService.getVeriflyMgrByBankId(bankModel);
		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
		LogModel logmodel = new LogModel();
		logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
		veriflyBaseWrapper.setLogModel(logmodel);
		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);
		veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

		veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);

		if (veriflyBaseWrapper.isErrorStatus()) {

			Object[] args = { veriflyBaseWrapper.getAccountInfoModel().getAccountNick(), veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
					veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() };
			// messageString =
			// MessageUtil.getMessage("linkPaymentMode.successMessage",
			// args);
			/**
			 * Phoenix call if verifly Lite (bank) is required. And First
			 * Account is being created.
			 */
		} else if (!veriflyBaseWrapper.isErrorStatus()) {
			throw new FrameworkCheckedException(veriflyBaseWrapper.getErrorMessage());
		}

	}

	private AccountInfoModel crAccountModel(RetailerContactListViewFormModel retailerContactListViewFormModel, UserDeviceAccountsModel userDeviceAccountsModel,
			SmartMoneyAccountModel smartMoneyAccountModel, OLAVO olavo) throws FrameworkCheckedException {

		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setAccountNo(olavo.getPayingAccNo().toString());
		accountInfoModel.setAccountNick(retailerContactListViewFormModel.getAccountNick());
		accountInfoModel.setActive(smartMoneyAccountModel.getActive());

		if (retailerContactListViewFormModel.getExpiryDate() != null) {
			accountInfoModel.setCardExpiryDate(PortalDateUtils.formatDate(retailerContactListViewFormModel.getExpiryDate(), "MM/yy"));
		}
		accountInfoModel.setCardNo(retailerContactListViewFormModel.getCardNo());
		accountInfoModel.setCardTypeId(retailerContactListViewFormModel.getCardType());

		accountInfoModel.setCreatedOn(smartMoneyAccountModel.getCreatedOn());
		accountInfoModel.setUpdatedOn(smartMoneyAccountModel.getUpdatedOn());
		accountInfoModel.setCustomerId(userDeviceAccountsModel.getAppUserId());
		accountInfoModel.setCustomerMobileNo(retailerContactListViewFormModel.getMobileNo());
		accountInfoModel.setFirstName(retailerContactListViewFormModel.getFirstName());
		accountInfoModel.setLastName(retailerContactListViewFormModel.getLastName());
		accountInfoModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());

		accountInfoModel.setAccountTypeId(retailerContactListViewFormModel.getAccountType());
		accountInfoModel.setCurrencyCodeId(retailerContactListViewFormModel.getCurrencyCode());
		accountInfoModel.setDeleted(Boolean.FALSE);

		return accountInfoModel;
	}

	private BaseWrapper createOlaAccount(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException, BaseWrapper baseWrapper)
			throws Exception {
		RetailerContactListViewFormModel rcm = (RetailerContactListViewFormModel) object;
		OLAVO olaVo = new OLAVO();
		if ("y".equals(httpServletRequest.getAttribute("isUpdate"))) {
			olaVo.setCnic(rcm.getOldNic());
			SwitchWrapper sWrapper = new SwitchWrapperImpl();
			sWrapper.setBankId(getOlaBankMadal().getBankId());
			sWrapper.setOlavo(olaVo);
			sWrapper = olaVeriflyFinancialInstitution.getAccountInfo(sWrapper);
			olaVo = sWrapper.getOlavo();

		}

		olaVo.setFirstName(rcm.getFirstName());
		olaVo.setMiddleName(rcm.getMiddleName());
		olaVo.setLastName(rcm.getLastName());
		olaVo.setFatherName(rcm.getFatherName());
		olaVo.setCnic(rcm.getNic());
		olaVo.setAddress(rcm.getAddress1());
		olaVo.setLandlineNumber(rcm.getLandlineNumber());
		olaVo.setMobileNumber(rcm.getMobileNo());
		olaVo.setDob(rcm.getDob());
		olaVo.setStatusId(rcm.getAccountStatusId());
		// olaVo.setBalance(dcm.getBalance()); //balance is yet to be decided
		BankModel bankModel = (BankModel) baseWrapper.getObject("bankModel");
		httpServletRequest.setAttribute("bankModel", bankModel);
		if ("y".equals(httpServletRequest.getAttribute("isUpdate"))) {
			accountForm.onUpdate(httpServletRequest, httpServletResponse, olaVo, bindException);
		} else {
			accountForm.onCreate(httpServletRequest, httpServletResponse, olaVo, bindException);
		}
		olaVo = (OLAVO) httpServletRequest.getAttribute("olaVo");
		if ("07".equals(olaVo.getResponseCode())) {
			httpServletRequest.setAttribute("olaExceptionMessage", "NIC already exisits in the OLA accounts");
			throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
		}
		baseWrapper.putObject("olaVo", olaVo);
		return baseWrapper;

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
		baseWrapperActionLog = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
		return actionLogModel;
	}

	private void logAfterAction(ActionLogModel actionLogModel) throws FrameworkCheckedException {
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // the
		// process
		// is
		// starting
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager.createOrUpdateActionLog(baseWrapperActionLog);
	}

	public BaseWrapper createLinkPaymentMode(BaseWrapper baseWrapper, HttpServletRequest request, RetailerContactListViewFormModel retailerContactListViewFormModel) throws FrameworkCheckedException {

		long useCaseId = 0;
		long actionId = 0;
		long appUserId = 0;
		if (request.getParameter(PortalConstants.KEY_USECASE_ID) != null) {
			useCaseId = Long.parseLong(request.getParameter(PortalConstants.KEY_USECASE_ID));
			System.out.println("usecase id " + useCaseId);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

		}
		if (request.getParameter(PortalConstants.KEY_ACTION_ID) != null) {
			actionId = Long.parseLong(request.getParameter(PortalConstants.KEY_ACTION_ID));
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
			UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getObject("userDeviceAccountsModel");
			userDeviceAccountsModel.setUserId(retailerContactListViewFormModel.getAllpayId());
			BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();
			baseWrapperUserDevice.setBasePersistableModel(userDeviceAccountsModel);
			baseWrapperUserDevice = userDeviceAccountsManager.loadUserDeviceAccount(baseWrapperUserDevice);
			userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapperUserDevice.getBasePersistableModel();

			// check if customer exist or not
			// if (userDeviceAccountsModel.getAppUserIdAppUserModel() == null) {
			// baseWrapperUserDevice.putObject("ErrMessage", "Allpay ID does not
			// exist");
			// throw new FrameworkCheckedException("Allpay ID does not exist");
			// }

			// customer account enable or disable
			// if (!userDeviceAccountsModel.getAccountEnabled()) {
			// baseWrapperUserDevice.putObject("ErrMessage", "Account is
			// disabled.");
			// throw new FrameworkCheckedException("Account is disabled.");
			// }
			//
			// // customer account for expired
			// if (userDeviceAccountsModel.getAccountExpired()) {
			// baseWrapperUserDevice.putObject("ErrMessage", "Account is
			// expired");
			// throw new FrameworkCheckedException("Account is expired");
			// }
			//
			// // customer account for locked
			// if (userDeviceAccountsModel.getAccountLocked()) {
			// baseWrapperUserDevice.putObject("ErrMessage", "Account is
			// locked");
			// throw new FrameworkCheckedException("Account is locked");
			// }
			//
			// // customer account for credential expired
			// if (userDeviceAccountsModel.getCredentialsExpired()) {
			// baseWrapperUserDevice.putObject("ErrMessage", "Account credential
			// is expired");
			// throw new FrameworkCheckedException("Account credential is
			// expired");
			// }

			// // Check if this is the first SMA of customer
			// boolean isFirstSMA =
			// isFirstSmartMoneyAccount(userDeviceAccountsModel
			// .getAppUserIdAppUserModel().getCustomerId());

			appUserId = userDeviceAccountsModel.getAppUserId();

			if (userDeviceAccountsModel.getAppUserIdAppUserModel().getNic() == null) {
				userDeviceAccountsModel.getAppUserIdAppUserModel().setNic(retailerContactListViewFormModel.getNic());
				baseWrapperUserDevice.setBasePersistableModel(userDeviceAccountsModel);
				baseWrapperUserDevice = this.userDeviceAccountsManager.updateUserDeviceAccount(baseWrapperUserDevice);
			}

			BankModel bankModel = getOlaBankMadal();
			bankModel.setBankId(bankModel.getBankId());
			// UserUtils.getCurrentUser()
			// .getBankUserIdBankUserModel().getBankId();

			// TODO ..: Code to be changed here for bank :..
			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			baseWrapperBank.setBasePersistableModel(bankModel);
			abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
			String mobileno = userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo();

			/**
			 * Smart Money Account is created here
			 */
			// TODO: this smartmoney account creation has to go below verifly
			RetailerContactModel retailerContactModel = (RetailerContactModel) baseWrapper.getObject("retailerContactModel");
			smartMoneyAccountModel = createSmartMoneyAccountModel(retailerContactListViewFormModel, userDeviceAccountsModel, abstractFinancialInstitution.isVeriflyRequired(), bankModel,
					retailerContactModel, (String) request.getAttribute("isUpdate"), request.getParameter("retailerContactId"));
			baseWrapper.putObject("smartMoneyAccountModel", smartMoneyAccountModel);
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
			throw new FrameworkCheckedException("implementationNotSupportedException");
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException constrainViolationException = (ConstraintViolationException) ex.getCause();
				String constraintName = "UK_USER_DEVICE_TYPE";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
					request.setAttribute("UK_USER_DEVICE_TYPE", "Allpay Id already exists");
				}
				constraintName = "UK_SM_ACCOUNT";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
					request.setAttribute("UK_USER_DEVICE_TYPE", "Account nick already exists");
				}
			}
			ex.printStackTrace();
			String errMessage = ex.getMessage();
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
			if (smartMoneyAccountModel != null && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
				System.out.println("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
				this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());
				System.out.println("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
			}
			if (/* firstSmartMoneyAccount && */!phoenixSuccessful) {
				// phoenix had a problem, verifly was hit, so rollback verifly.
				// TODO: here code to delete the verifly account
				try {
					abstractFinancialInstitution.deleteAccount(veriflyBaseWrapper);
				} catch (Exception exp) {
					ex = exp;
				}
				System.out.println("Phoenix had a problem");
			}

			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}
		baseWrapper.putObject("abstractFinancialInstitution", abstractFinancialInstitution);
		return baseWrapper;

	}

	private SmartMoneyAccountModel createSmartMoneyAccountModel(RetailerContactListViewFormModel retailerContactListViewFormModel, UserDeviceAccountsModel userDeviceAccountsModel,
			boolean veriflyRequired, BankModel bankModel, RetailerContactModel retailerContactModel, String isUpdate, String retailerContactId) throws Exception {
		int recordCount;
		int defRecordCount;

		// TODO ..: Code to be changed here for bank :..
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		// BankModel bankModel = UserUtils.getCurrentUser()
		// .getBankUserIdBankUserModel().getBankIdBankModel();
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

		String bankName = bankModel.getName();

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		SmartMoneyAccountModel smartMoneyAccountModel = null;
		if ("y".equals(isUpdate)) {
			smartMoneyAccountModel = new SmartMoneyAccountModel();
			smartMoneyAccountModel.setRetailerContactId(Long.valueOf(retailerContactId));
			smartMoneyAccountModel.setBankId(getOlaBankMadal().getBankId());
			CustomList cList = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
			smartMoneyAccountModel = (SmartMoneyAccountModel) cList.getResultsetList().get(0);

		} else {
			smartMoneyAccountModel = new SmartMoneyAccountModel();
		}
		smartMoneyAccountModel.setName(retailerContactListViewFormModel.getAccountNick());

		smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		smartMoneyAccountModel.setBankId(bankModel.getBankId());
		smartMoneyAccountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		if (!"y".equals(isUpdate)) {
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setEnableLike(Boolean.FALSE);

			CustomList cList = linkPaymentModeDAO.findByExample(smartMoneyAccountModel, null, null, exampleHolder);
			List list = cList.getResultsetList();
			if (!list.isEmpty()) {
				throw new FrameworkCheckedException("This Payment Mode is already linked.");
			}

			// smartMoneyAccountModel
			// .setCardTypeId(linkPaymentModeModel.getCardType());
			smartMoneyAccountModel.setCreatedOn(new Date());
			smartMoneyAccountModel.setUpdatedOn(new Date());
			smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
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
			if (retailerContactListViewFormModel.getCurrencyCode() != null && retailerContactListViewFormModel.getAccountType() != null) {
				smartMoneyAccountModel.setCurrencyCodeId(retailerContactListViewFormModel.getCurrencyCode());
				smartMoneyAccountModel.setAccountTypeId(retailerContactListViewFormModel.getAccountType());
			}
			smartMoneyAccountModel.setDefAccount(true);
			smartMoneyAccountModel.setDeleted(false);

			SmartMoneyAccountModel defaultSmartMoneyAccountModel = new SmartMoneyAccountModel();

			defaultSmartMoneyAccountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			defaultSmartMoneyAccountModel.setDefAccount(true);
			defaultSmartMoneyAccountModel.setActive(true);

			cList = linkPaymentModeDAO.findByExample(defaultSmartMoneyAccountModel, null, null, exampleHolder);
			list = cList.getResultsetList();

			if (!list.isEmpty()) {
				smartMoneyAccountModel.setDefAccount(false);
			}

			// first check that if account nick and customer account already
			// exist
			// then throw exception
			SmartMoneyAccountModel testSmartMoneyAccountModel = new SmartMoneyAccountModel();
			testSmartMoneyAccountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			testSmartMoneyAccountModel.setName(retailerContactListViewFormModel.getName());
			CustomList<SmartMoneyAccountModel> smartMoneyAccountList = linkPaymentModeDAO.findByExample(testSmartMoneyAccountModel, null, null, exampleHolder);
			List smartMoneyAccountRList = smartMoneyAccountList.getResultsetList();
			if (smartMoneyAccountRList.size() > 0) {
				throw new FrameworkCheckedException("This Payment Mode is already linked.");
			}
			smartMoneyAccountModel.setName(retailerContactListViewFormModel.getAccountNick());
			baseWrapper.setBasePersistableModel(this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel));
		}
		return (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
	}

	public void createUserDeviceAccount(BaseWrapper baseWrapper, HttpServletRequest httpServletRequest) throws FrameworkCheckedException {

		AppUserModel appUserModel = (AppUserModel) baseWrapper.getObject("savedAppUserModel");
		RetailerContactListViewFormModel retailerContactListViewFormModel = (RetailerContactListViewFormModel) baseWrapper.getObject("retailerContactListViewFormModel");
		UserDeviceAccountsModel userDeviceAccountsModel;
		String appUserId = httpServletRequest.getParameter("appUserId");
		if ("y".equals(httpServletRequest.getAttribute("isUpdate"))) {
			userDeviceAccountsModel = getUserDeviceAccountsModelAllPay(appUserId);
			appUserModel = getAppUserModel(appUserId);
		} else {
			userDeviceAccountsModel = new UserDeviceAccountsModel();
		}

		Date nowDate = new Date();
		String randomPin = "";
		if (retailerContactListViewFormModel.getHead() != null && retailerContactListViewFormModel.getHead() == true) {
			userDeviceAccountsModel.setPasswordChangeRequired(true);
			userDeviceAccountsModel.setPinChangeRequired(false);
			
			randomPin = RandomUtils.generateRandom(8, false, true);
			if (!("y".equals(httpServletRequest.getAttribute("isUpdate")))) {
				userDeviceAccountsModel.setPassword(EncoderUtils.encodeToSha(randomPin));
			}
		}else{
			userDeviceAccountsModel.setPinChangeRequired(true);
			userDeviceAccountsModel.setPasswordChangeRequired(false);
			randomPin = RandomUtils.generateRandom(4, false, true);
			if (!("y".equals(httpServletRequest.getAttribute("isUpdate")))) {
				userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
			}

				
		}
		userDeviceAccountsModel.setAccountEnabled(true);
		userDeviceAccountsModel.setAccountExpired(retailerContactListViewFormModel.getAccountExpired() == null ? false : new Boolean(retailerContactListViewFormModel.getAccountExpired()));
		userDeviceAccountsModel.setAccountLocked(retailerContactListViewFormModel.getAccountLocked() == null ? false : new Boolean(retailerContactListViewFormModel.getAccountLocked()));
		userDeviceAccountsModel.setCredentialsExpired(retailerContactListViewFormModel.getCredentialsExpired() == null ? false : new Boolean(retailerContactListViewFormModel.getCredentialsExpired()));
		if (!("y".equals(httpServletRequest.getAttribute("isUpdate")))) {
			userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			userDeviceAccountsModel.setCreatedOn(nowDate);
		}

		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		userDeviceAccountsModel.setUpdatedOn(nowDate);
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);

		//String allPayId = retailerContactListViewFormModel.getAllpayId();
		String allPayId = this.computeAllpayId();
		retailerContactListViewFormModel.setAllpayId(allPayId);
		
		userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
		userDeviceAccountsModel.setUserId(allPayId);
		userDeviceAccountsModel.setCommissioned(retailerContactListViewFormModel.getCommissioned() == null ? false : new Boolean(retailerContactListViewFormModel.getCommissioned()));
		userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

		Object[] args = { allPayId };
		String messageString = "";
		if (retailerContactListViewFormModel.getHead() != null && retailerContactListViewFormModel.getHead() == true) {
			messageString = MessageUtil.getMessage("ussd.agentAccountCreated", args);
		}else{
			messageString = MessageUtil.getMessage("ussd.directAgentAccountCreated", args);
		}

		// String messageString = "Dear Customer Your New MWallet Account is
		// created Your MfsID is:"
		// + mfsId + " and Pin is: " + randomPin;

		if (!("y".equals(httpServletRequest.getAttribute("isUpdate")))) {
			SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString,SMSConstants.Sender_1611);
			smsSender.send(smsMessage);
		}
		baseWrapper.putObject("retailerContactFormModel", retailerContactListViewFormModel);
		baseWrapper.putObject("userDeviceAccountsModel", userDeviceAccountsModel);
	}

	public BankModel getOlaBankMadal() {
		BankModel bankModel = new BankModel();
		bankModel.setFinancialIntegrationId(4L); // 4 is for OLA bank
		CustomList bankList = this.bankDAO.findByExample(bankModel);
		List bankL = bankList.getResultsetList();
		bankModel = (BankModel) bankL.get(0);
		return bankModel;
	}

	public SearchBaseWrapper loadAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		RetailerContactModel retailerContactModel = this.retailerContactDAO.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(retailerContactModel);
		return searchBaseWrapper;
	}

	public BaseWrapper loadAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	private AppUserModel getAppUserModel(String appUserId) {
		return this.appUserDAO.findByPrimaryKey(Long.valueOf(appUserId));
	}

	public SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper) {
		RetailerContactListViewModel retailerContactListViewModel = (RetailerContactListViewModel) searchBaseWrapper.getBasePersistableModel();
		retailerContactListViewModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		CustomList<RetailerContactListViewModel> list = this.retailerContactListViewDAO.findByExample(retailerContactListViewModel, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());

		List<RetailerContactListViewModel> list1 = list.getResultsetList();

		try {
		for (RetailerContactListViewModel model : list1) {
			if(model.getAccountNo() != null) {
				model.setAccountNo(this.encryptionHandler.decrypt(model.getAccountNo()));
			}
		}
			} catch (Exception e) {
				e.printStackTrace();
			}

			searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	private UserDeviceAccountsModel getUserDeviceAccountsModelAllPay(String appUserId) {
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(Long.valueOf(appUserId));
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
		CustomList cl = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
		return (UserDeviceAccountsModel) cl.getResultsetList().get(0);
	}
	
	public UserDeviceAccountsModel getUserDeviceAccountsModel(String appUserId) {
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(Long.valueOf(appUserId));
		//userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
		CustomList cl = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
		return (UserDeviceAccountsModel) cl.getResultsetList().get(0);
	}

	public ModelAndView updateAccount(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {

		//return createAccount(httpServletRequest, httpServletResponse, object, bindException);
		
		RetailerContactListViewFormModel model = (RetailerContactListViewFormModel) object;
		// Update retailer contact account
//		String allPayId = this.computeAllpayId();
//		model.setAllpayId(allPayId);
//		model.setAccountNick("ola_" + allPayId); // because allpay id would also be the nick of that account
		ModelAndView mov = retailerContactFormController.onUpdate(httpServletRequest, httpServletResponse, object, bindException);
		BaseWrapper baseWrapper = (BaseWrapper) httpServletRequest.getAttribute("baseWrapper");
		baseWrapper.putObject("retailerContactModel", baseWrapper.getBasePersistableModel());
		//this.createUserDeviceAccount(baseWrapper, httpServletRequest);
		return mov;
	}

	public RetailerContactListViewDAO getRetailerContactListViewDAO() {
		return retailerContactListViewDAO;
	}

	public void setRetailerContactListViewDAO(RetailerContactListViewDAO retailerContactListViewDAO) {
		this.retailerContactListViewDAO = retailerContactListViewDAO;
	}

	public BankDAO getBankDAO() {
		return bankDAO;
	}

	public void setBankDAO(BankDAO bankDAO) {
		this.bankDAO = bankDAO;
	}

	public RetailerContactFormController getRetailerContactFormController() {
		return retailerContactFormController;
	}

	public void setRetailerContactFormController(RetailerContactFormController retailerContactFormController) {
		this.retailerContactFormController = retailerContactFormController;
	}

	public UserDeviceAccountsDAO getUserDeviceAccountsDAO() {
		return userDeviceAccountsDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public ActionLogManager getActionLogManager() {
		return actionLogManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public LinkPaymentModeDAO getLinkPaymentModeDAO() {
		return linkPaymentModeDAO;
	}

	public void setLinkPaymentModeDAO(LinkPaymentModeDAO linkPaymentModeDAO) {
		this.linkPaymentModeDAO = linkPaymentModeDAO;
	}

	public UserDeviceAccountsManager getUserDeviceAccountsManager() {
		return userDeviceAccountsManager;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public FinancialIntegrationManager getFinancialIntegrationManager() {
		return financialIntegrationManager;
	}

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public SmartMoneyAccountDAO getSmartMoneyAccountDAO() {
		return smartMoneyAccountDAO;
	}

	public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
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

	public void setOlaVeriflyFinancialInstitution(FinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public OLAAccountFormController getAccountForm() {
		return accountForm;
	}

	public void setAccountForm(OLAAccountFormController accountForm) {
		this.accountForm = accountForm;
	}

	public RetailerContactDAO getRetailerContactDAO() {
		return retailerContactDAO;
	}

	public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
		this.retailerContactDAO = retailerContactDAO;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setSequenceGenerator(OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}


	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}

	private String computeAllpayId(){
		Long nextLongValue = null;
		nextLongValue = this.sequenceGenerator.nextLongValue();

/*		AppUserModel appUserModel;
		GoldenNosModel goldenNosModel;
		boolean flag = true;
		while( flag ){
			nextLongValue = this.sequenceGenerator.nextLongValue();
			appUserModel = new AppUserModel();
			goldenNosModel = new GoldenNosModel();
			appUserModel.setUsername(String.valueOf(nextLongValue));
			goldenNosModel.setGoldenNumber(String.valueOf(nextLongValue));
			int countAppUser = this.appUserDAO.countByExample(appUserModel);
			int countGoldenNos = this.goldenNosDAO.countByExample(goldenNosModel);
			if( countAppUser == 0 && countGoldenNos == 0 ){
				flag = false;
			}
		}
*/		
		return String.valueOf(nextLongValue);
	}

}
