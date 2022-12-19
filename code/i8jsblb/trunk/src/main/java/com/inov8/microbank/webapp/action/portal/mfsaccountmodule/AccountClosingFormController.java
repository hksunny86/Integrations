package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountClosureFacade;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.accountholder.AccountHolderManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccountClosingFormController extends AdvanceFormController
{
	private MfsAccountClosureFacade mfsAccountClosureFacade;
	private MfsAccountManager mfsAccountManager;
	private AppUserManager	appUserManager;
	private SmsSender smsSender;
	private SmartMoneyAccountManager smartMoneyAccountManager ;
	private VeriflyManager veriflyManager;
	private AccountHolderManager accountHolderManager;
	private AccountManager accountManager;
	private ActionAuthorizationFacade actionAuthorizationFacade;
	private String appUserId;

	public AccountClosingFormController()
	{
		setCommandName("appUserModel");
		setCommandClass(AppUserModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception
	{
		appUserId = ServletRequestUtils.getStringParameter(req,"appUserId");
		String customerId = ServletRequestUtils.getStringParameter(req,"customerId");
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		AppUserModel appUserModel = new AppUserModel();

		if (null != appUserId)
		{
			appUserModel.setAppUserId(Long.valueOf(EncryptionUtil.decryptForAppUserId( appUserId)));
			baseWrapper.setBasePersistableModel(appUserModel);
			baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
			appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
		}

		if (null != customerId)
		{
			appUserModel.setCustomerId(Long.valueOf(customerId));
		}

		return appUserModel;

	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest req) throws Exception
	{
		ArrayList<String> list = new ArrayList();

		Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
		AppUserModel appUserModel1 = appUserManager.loadAppUser(Long.parseLong(appUserId));
		SmartMoneyAccountModel smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel1,paymentModeId);

		if(smartMoneyAccountModel != null && smartMoneyAccountModel.getActive())
			list.add("BLB");

		if(smartMoneyAccountModel == null)
			smartMoneyAccountModel = smartMoneyAccountManager.getInActiveSMA(appUserModel1,paymentModeId,OlaStatusConstants.ACCOUNT_STATUS_CLOSED);

		if(smartMoneyAccountModel != null && !smartMoneyAccountModel.getActive() && smartMoneyAccountModel.getAccountClosedUnsetteled() != null &&
				smartMoneyAccountModel.getAccountClosedSetteled() != null && smartMoneyAccountModel.getAccountClosedUnsetteled() == 1L
				&& smartMoneyAccountModel.getAccountClosedSetteled() == 0L)
			list.add("BLB");

		paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
		smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel1,paymentModeId);

		if(smartMoneyAccountModel != null && smartMoneyAccountModel.getActive())
			list.add("HRA");

		Map referenceDataMap = new HashMap();

		referenceDataMap.put("accountTypeList", list);

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception
	{
		ModelAndView modelAndView = null;
		try
		{
			String acType=ServletRequestUtils.getStringParameter(req, "acType");
			Long paymentModeId=null;
			Long statusId = OlaStatusConstants.ACCOUNT_STATUS_ACTIVE;
			Long customerAccountTypeId = null;
			Boolean isSetteled = false;
			if(acType.equals("HRA"))
			{
				paymentModeId= PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
				customerAccountTypeId = CustomerAccountTypeConstants.HRA;
			}
			else
			{
				paymentModeId= PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
			}

			Long isActive = 1L;
			List<ActionAuthorizationModel> existingRequest;
			ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();

			AppUserModel appUserModel = (AppUserModel) obj;
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			String mfsId = ServletRequestUtils.getStringParameter(req, "customerId");
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject("mfsId", mfsId);

			AppUserModel appUserModel1 = appUserManager.loadAppUser(appUserModel.getAppUserId());

			if(!acType.equals("HRA"))
			{
				CustomerModel customerModel = appUserModel1.getCustomerIdCustomerModel();
				customerAccountTypeId= customerModel.getCustomerAccountTypeId();
			}

			SmartMoneyAccountModel smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel1,paymentModeId);

			if(smartMoneyAccountModel != null && paymentModeId == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT)
			{
				SmartMoneyAccountModel sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel1, PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
				if(sma != null)
					throw new FrameworkCheckedException("Exists");
			}
			else if(smartMoneyAccountModel == null && paymentModeId == PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT ) {
				throw new FrameworkCheckedException("notExist");
			}

			if(!StringUtil.isNullOrEmpty(mfsId)){
				actionAuthorizationModel.setReferenceId(mfsId);
				actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
				existingRequest = actionAuthorizationFacade.checkExistingRequest(actionAuthorizationModel).getResultsetList();

				if(existingRequest != null && !existingRequest.isEmpty())
					throw new FrameworkCheckedException("requestExists");
			}

			if(smartMoneyAccountModel == null )
			{
				SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
				sma.setCustomerId(appUserModel1.getCustomerId());
				sma.setPaymentModeId(paymentModeId);
				smartMoneyAccountModel = smartMoneyAccountManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
			}

			AccountInfoModel accountInfoModel = null;
			if(smartMoneyAccountModel != null && smartMoneyAccountModel.getActive() == true)
				accountInfoModel = this.veriflyManager.getAccountInfoModel(smartMoneyAccountModel.getCustomerId(),paymentModeId);
			if(smartMoneyAccountModel != null && smartMoneyAccountModel.getActive() == true)
			{
				if(paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
					appUserModel.setAccountClosedUnsettled(true);
				smartMoneyAccountModel.setAccountClosedUnsetteled(1L);
				/*smartMoneyAccountModel.setActive(false);*/
				smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_CLOSED);
				smartMoneyAccountModel.setAccountStateId(OlaStatusConstants.ACCOUNT_STATUS_CLOSED);
			}
			else
			{
				if(paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
					appUserModel.setAccountClosedSettled(true);
				smartMoneyAccountModel.setAccountClosedSetteled(1L);
				isSetteled = true;
			}
			if(accountInfoModel != null)
			{
				accountInfoModel.setActive(Boolean.FALSE);
			}
			AccountHolderModel accountHolderModel = null;
			AccountModel accountModel = this.accountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel1.getNic(),customerAccountTypeId,statusId);
			if(accountModel != null)
			{
				if(accountModel.getAccountHolderId() != null)
				{
					BaseWrapper accountHolderWrapper = new BaseWrapperImpl();
					AccountHolderModel acHolderModel = new AccountHolderModel();
					acHolderModel.setAccountHolderId(accountModel.getAccountHolderId());
					accountHolderWrapper.setBasePersistableModel(acHolderModel);
					accountHolderWrapper = this.accountHolderManager.loadAccountHolder(accountHolderWrapper);

					accountHolderModel = (AccountHolderModel)accountHolderWrapper.getBasePersistableModel();
					accountHolderModel.setActive(Boolean.FALSE);
				}
				accountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);
			}

			baseWrapper.putObject("accountHolderModel",accountHolderModel);
			baseWrapper.putObject("accountModel",accountModel);
			baseWrapper.putObject("accountInfoModel",accountInfoModel);
			baseWrapper.putObject("paymentModeId",paymentModeId);
			baseWrapper.putObject("smartMoneyAccountModel",smartMoneyAccountModel);
			baseWrapper.putObject("isClosedSettled",isSetteled);
			baseWrapper.setBasePersistableModel(appUserModel);

			baseWrapper = mfsAccountClosureFacade.makeCustomerAccountClosed(baseWrapper);

			if(paymentModeId == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT)
				appUserModel = (AppUserModel)baseWrapper.getBasePersistableModel();
			else
				appUserModel = appUserModel1;
			if(appUserModel != null && appUserModel.getAccountClosedSettled() == false){
				//Sending SMS on Successfull Account Closing
				logger.info("[AccountClosingFormController.onUpdate] Sending SMS to customer after closing successfully. MobileNo:" + appUserModel.getMobileNo());
				String messageString = "Dear Customer, your " +acType + " account has been closed successfully.";//MessageUtil.getMessage("smsCommand.close_customer");
				SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(),messageString);

				try {

					smsSender.send(smsMessage);

				} catch (Exception e) {
					e.printStackTrace();
					logger.error("[AccountClosingFormController.onUpdate] Exception while sending SMS to Mobile No:" + appUserModel.getMobileNo());
				}
			}
			Map<String, String> map = new HashMap<String, String>();
			if(acType.equals("BLB"))
			{
				map.put("status","success");
				modelAndView = new ModelAndView(this.getSuccessView()+"?status=success&appUserId="+appUserModel.getAppUserId(),map);
				Long id = appUserModel.getAppUserId();
				appUserModel = this.mfsAccountManager.getAppUserModelByPrimaryKey(id);
				appUserModel.setAccountStateId(6L);
				baseWrapper.setBasePersistableModel(appUserModel);
				this.appUserManager.saveOrUpdateAppUser(baseWrapper);
			}
			else
			{
				map.put("status","success");
				modelAndView = new ModelAndView(this.getSuccessView()+"?status=success&appUserId="+appUserModel.getAppUserId(),map);
				smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel1,paymentModeId);
				baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			}
		}
		catch (Exception ex)
		{
			if("TrnsPend".equals(ex.getMessage()))
				req.setAttribute("message", "Transaction is pending against account, so account cannot be closed.");
			else if("Balance not Zero".equals(ex.getMessage()))
				req.setAttribute("message", "Account can not be closed due to outstanding balance.");
			else if("Exists".equals(ex.getMessage()))
				req.setAttribute("message", "HRA Account exists,so Branch Less Banking account cannot be closed.");
			else if("notExist".equals(ex.getMessage()))
				req.setAttribute("message", "HRA Account does not exist");
			else if("requestExists".equals(ex.getMessage()))
				req.setAttribute("message", "Action authorization request exists,So account cannot be closed.");
			else
				req.setAttribute("message", "Account Cannot be closed or settled, Kindly consult administrator for details.");
			req.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(req, res, errors);
		}
		finally
		{
			ThreadLocalAppUser.remove();
			ThreadLocalActionLog.remove();
		}
		return modelAndView;
	}
	
	/*private void settleAccountBalances(AppUserModel tempAppUserModel) throws Exception{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SmartMoneyAccountModel smartMoneyAccountModel = mfsAccountManager.getSmartMoneyAccountByCustomerId(tempAppUserModel.getCustomerId());
		baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setBasePersistableModel(smartMoneyAccountModel) ;
		switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
		this.recordActionLogBeforeStart();
		switchWrapper = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapper);
		
		Double balance = switchWrapper.getBalance();
		
		if(balance != null && balance > 0){
			//debit customer account
			switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setBasePersistableModel(smartMoneyAccountModel) ;
			switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
			switchWrapper.setBalance(balance);
			switchWrapper.getWorkFlowWrapper().setSmartMoneyAccountModel(smartMoneyAccountModel);
			switchWrapper.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
			switchWrapper = abstractFinancialInstitution.closeOLAAccount(switchWrapper);
			
			// Now Make a phoenix call for FT from Customer Pool Account to Closure Sundry Account.
			StakeholderBankInfoModel customerPoolBankInfo = getAccount(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
			StakeholderBankInfoModel closureSundryBankInfo = getAccount(PoolAccountConstantsInterface.ACC_CLOSURE_SUNDRY_ACCOUNT_ID);
			
			debitCreditPhoenixAccounts(customerPoolBankInfo.getAccountNo(), closureSundryBankInfo.getAccountNo(), balance);
		}
	}
	
	private void recordActionLogBeforeStart() throws Exception {
		ActionLogModel actionLogModel = new ActionLogModel();
		XStream xstream = new XStream();
		try {
			xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(""),
					XPathConstants.actionLogInputXMLLocationSteps));
			
			actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
			actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
			actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
			if (actionLogModel.getActionLogId() != null) {
				ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			}
		}finally{
		}
	}
		
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try {
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		} catch (Exception ex) {
			logger.error("Exception occurred while processing", ex);

		}
		
		return actionLogModel;
	}
	
	private boolean debitCreditPhoenixAccounts(String fromAccountNo, String toAccountNo, Double transactionAmount) throws FrameworkCheckedException {
		boolean success = true;
		
		SwitchWrapper switchWrapperMain = new SwitchWrapperImpl();
		IntegrationMessageVO integrationMessageVOMain = new PhoenixIntegrationMessageVO();
		switchWrapperMain.setIntegrationMessageVO(integrationMessageVOMain);

		switchWrapperMain.setFromAccountNo(fromAccountNo);
		switchWrapperMain.setFromAccountType("20");
		switchWrapperMain.setFromCurrencyCode("586");

		switchWrapperMain.setToAccountNo(toAccountNo);
		switchWrapperMain.setToAccountType("20");
		switchWrapperMain.setToCurrencyCode("586");

		switchWrapperMain.setTransactionAmount(transactionAmount);
		switchWrapperMain.setCurrencyCode("586");

		switchWrapperMain.setWorkFlowWrapper(new WorkFlowWrapperImpl());
		switchWrapperMain.getWorkFlowWrapper().setAccountInfoModel(new AccountInfoModel());
		AppUserModel appUserModelMain = new AppUserModel();
		appUserModelMain.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		ThreadLocalAppUser.setAppUserModel(appUserModelMain);
		switchWrapperMain.setBankId(50110L);
		switchWrapperMain.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
		logger.info("[AccountClosingFormController.debitCreditPhoenixAccounts] Going to make debitCredit tx from account " + fromAccountNo + " to Account: " + toAccountNo);
		switchWrapperMain = switchController.debitCreditAccount(switchWrapperMain);
		
		//if FT is unsuccessful, update success flag.
		if (switchWrapperMain.getIntegrationMessageVO().getResponseCode() == null || ! switchWrapperMain.getIntegrationMessageVO().getResponseCode().equals("00")){
			success = false;
		}

		return success;
	}
	
	public StakeholderBankInfoModel getAccount(Long key) {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
		stakeholderBankInfoModel.setPrimaryKey(key);
		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
		try {
			stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper)
					.getBasePersistableModel();
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		return stakeholderBankInfoModel;
	}*/


	public void setMfsAccountManager(MfsAccountManager mfsAccountManager)
	{
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setMfsAccountClosureFacade(MfsAccountClosureFacade mfsAccountClosureFacade) {
		this.mfsAccountClosureFacade = mfsAccountClosureFacade;
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setVeriflyManager(VeriflyManager veriflyManager) {
		this.veriflyManager = veriflyManager;
	}

	public void setAccountHolderManager(AccountHolderManager accountHolderManager) {
		this.accountHolderManager = accountHolderManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setActionAuthorizationFacade(ActionAuthorizationFacade actionAuthorizationFacade) {
		this.actionAuthorizationFacade = actionAuthorizationFacade;
	}
}
