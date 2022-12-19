package com.inov8.microbank.server.service.portal.allpaymodule;

import java.sql.Timestamp;
import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AllpayForgotVeriflyPinViewModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SMSConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.allpaymodule.AllpayForgotVeriflyPinViewDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;

public class AllpayForgotVeriflyPinManagerImpl implements AllpayForgotVeriflyPinManager
{
	private AllpayForgotVeriflyPinViewDAO	allpayForgotVeriflyPinViewDAO;
	private VeriflyManagerService	veriflyManagerService;
	private SmartMoneyAccountDAO	smartMoneyAccountDAO;
	private ActionLogManager		actionLogManager;
	private SmsSender				smsSender;
	private FinancialIntegrationManager financialIntegrationManager;

	public SearchBaseWrapper searchForgotVeriflyPin(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		//populate list
		CustomList<AllpayForgotVeriflyPinViewModel> list = this.allpayForgotVeriflyPinViewDAO.findByExample(
				(AllpayForgotVeriflyPinViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());

		//setting list into wrapper
		searchBaseWrapper.setCustomList(list);

		return searchBaseWrapper;
	}

	public BaseWrapper changeVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		try
		{
			/**
			 * Loging the data
			 */

			AppUserModel appUserModel = UserUtils.getCurrentUser();

			ActionLogModel actionLogModel = new ActionLogModel();
			actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
			actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the process is starting
			actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
			actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
			actionLogModel.setCustomField1(baseWrapper.getObject("appUserId").toString());
			actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
			actionLogModel = logAction(actionLogModel, true);

			//setting actionLogId into thread local
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

			//populating AccountInfoModel and LogModel into VeriflyBaseWrapper

			//initialize VeriflyBaseWrapper
			VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

			//getting required data from wrapper for getting latest data for model
			Long appUserId = new Long(baseWrapper.getObject("appUserId").toString());
			Long smartMoneyAccountId = new Long(baseWrapper.getObject("smartMoneyAccountId").toString());

			//getting latest data from db
			AllpayForgotVeriflyPinViewModel allpayForgotVeriflyPinViewModel = new AllpayForgotVeriflyPinViewModel();
			allpayForgotVeriflyPinViewModel.setAppUserId(appUserId);
			allpayForgotVeriflyPinViewModel.setSmartMoneyAccountId(smartMoneyAccountId);

			CustomList customList = allpayForgotVeriflyPinViewDAO.findByExample(allpayForgotVeriflyPinViewModel);
			allpayForgotVeriflyPinViewModel = (AllpayForgotVeriflyPinViewModel) customList.getResultsetList().get(0);

			//create accountInfoModel and initialize with data
			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setAccountNick(allpayForgotVeriflyPinViewModel.getAccountNick());
			accountInfoModel.setCustomerId(allpayForgotVeriflyPinViewModel.getCustomerId());
			veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

			//create logModel and initialize with values
			LogModel logModel = new LogModel();

			logModel.setCreatdByUserId(appUserModel.getAppUserId());
			logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());

			veriflyBaseWrapper.setLogModel(logModel);

			Long samrtMoneyAccountId = new Long(baseWrapper.getObject("smartMoneyAccountId").toString());
			SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO
					.findByPrimaryKey(allpayForgotVeriflyPinViewModel.getSmartMoneyAccountId());
			VeriflyManager veriflyManager = null;

			try
			{
				veriflyManager = veriflyManagerService.getVeriflyMgrByAccountId(smartMoneyAccountModel);
                // TODO: changed here
				//veriflyBaseWrapper = veriflyManager.resetPIN(veriflyBaseWrapper);
				BaseWrapper baseWrapperSmartMoney = new BaseWrapperImpl();
				baseWrapperSmartMoney.setBasePersistableModel(smartMoneyAccountModel);
				veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
                AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperSmartMoney);
                if(abstractFinancialInstitution.isVeriflyLite())
                {
                	smartMoneyAccountModel.setChangePinRequired(false);
    				smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
					smartMoneyAccountModel.setUpdatedOn(new Date());
					smartMoneyAccountModel = this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);

                	veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB ) ;
                	veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_CNIC, allpayForgotVeriflyPinViewModel.getNic() ) ;
                	veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_ACCOUNT_TYPE, smartMoneyAccountModel.getAccountTypeId().toString() ) ;
                	veriflyBaseWrapper=abstractFinancialInstitution.changePin(veriflyBaseWrapper);
                	Boolean response = (Boolean)veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_RESPONSE);
                	if(response.booleanValue()==Boolean.FALSE)
                	{
                		
                		baseWrapper.putObject("errorMessage", "Your request can not be processed at the moment. Please try again later");
                		//throw new FrameworkCheckedException("Forgot pin Request could not be processed.Please try again");
                		
                	}
                	// sms is not going
                	baseWrapper.putObject("errorMessage", "VeriflyLite");
                }
                else
                { 	
	                veriflyBaseWrapper = abstractFinancialInstitution.resetPin(veriflyBaseWrapper);
	                
					if (veriflyBaseWrapper.isErrorStatus())
					{
						smartMoneyAccountModel.setChangePinRequired(true);
	
						smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
						smartMoneyAccountModel.setUpdatedOn(new Date());
						this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
						
						Object[] args = {"Branchless Banking Account",veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin()};
	
						String messageString = MessageUtil.getMessage("forgotVeriflyPin.successMessage", args);
						
						smsSender.send(new SmsMessage(allpayForgotVeriflyPinViewModel.getMobileNo(), messageString,SMSConstants.Sender_1611));
	
	//					smsSender.send(new SmsMessage(forgotVeriflyPinViewModel.getMobileNo(), "Dear Customer, your new Bank PIN is "
	//							+ veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin()
	//							+ ". Please change this PIN immediately on first use."));
						//smsSender.send(new SmsMessage("03044116780","Your pin has been changed new pin is "+veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() +" for account nick "+forgotVeriflyPinViewModel.getAccountNick() ));
					}
					else
					{
						baseWrapper.putObject("errorMessage", veriflyBaseWrapper.getErrorMessage());
					}
                }

			}
			catch (Exception e)
			{
				
				baseWrapper.putObject("errorMessage", e.getMessage());
				throw new FrameworkCheckedException(e.getMessage(), e);
			}

			/**
			 * Logging Information, Ending Status
			 */
			actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
			//actionLogModel.setCustomField2("Pin is reset successfully");
			actionLogModel = logAction(actionLogModel, false);
		}
		finally
		{
			ThreadLocalActionLog.remove();
		}
		return baseWrapper;
	}

	private ActionLogModel logAction(ActionLogModel actionLogModel, boolean isNewTrans)
			throws FrameworkCheckedException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if (isNewTrans)
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		else
			baseWrapper = this.actionLogManager.createOrUpdateActionLog(baseWrapper);
		return (ActionLogModel) baseWrapper.getBasePersistableModel();
	}

	public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO)
	{
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
	}

	public void setVeriflyManagerService(VeriflyManagerService veriflyManagerService)
	{
		this.veriflyManagerService = veriflyManagerService;
	}


	public void setActionLogManager(ActionLogManager actionLogManager)
	{
		this.actionLogManager = actionLogManager;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public AllpayForgotVeriflyPinViewDAO getAllpayForgotVeriflyPinViewDAO() {
		return allpayForgotVeriflyPinViewDAO;
	}

	public void setAllpayForgotVeriflyPinViewDAO(AllpayForgotVeriflyPinViewDAO allpayForgotVeriflyPinViewDAO) {
		this.allpayForgotVeriflyPinViewDAO = allpayForgotVeriflyPinViewDAO;
	}

}
