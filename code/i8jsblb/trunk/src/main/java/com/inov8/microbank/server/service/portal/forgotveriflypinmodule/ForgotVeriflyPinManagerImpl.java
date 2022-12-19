package com.inov8.microbank.server.service.portal.forgotveriflypinmodule;

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
import com.inov8.microbank.common.model.portal.forgotveriflypinmodule.ForgotVeriflyPinViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.server.dao.portal.allpaymodule.AllpayForgotVeriflyPinViewDAO;
import com.inov8.microbank.server.dao.portal.forgotveriflypinmodule.ForgotVeriflyPinViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.util.Date;

public class ForgotVeriflyPinManagerImpl implements ForgotVeriflyPinManager
{
	private final static Log logger = LogFactory.getLog(ForgotVeriflyPinManagerImpl.class);
	
	public AppUserDAO appUserDAO;
	private AllpayForgotVeriflyPinViewDAO allpayForgotVeriflyPinViewDAO;
	private ForgotVeriflyPinViewDAO	forgotVeriflyPinViewDAO;
	private VeriflyManagerService	veriflyManagerService;
	private SmartMoneyAccountDAO	smartMoneyAccountDAO;
	private ActionLogManager		actionLogManager;
	private SmsSender				smsSender;
	private FinancialIntegrationManager financialIntegrationManager;
	private IvrRequestHandler		ivrRequestHandler;

	public AppUserModel isRetailerOrDistributor(String appUserId) {
		
		AppUserModel appUserModel = this.appUserDAO.getUser(Long.valueOf(appUserId));
		
		return appUserModel;
	}
	public SearchBaseWrapper searchForgotVeriflyPin(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		//populate list
		CustomList<ForgotVeriflyPinViewModel> list = this.forgotVeriflyPinViewDAO.findByExample(
				(ForgotVeriflyPinViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
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
			logger.info("YEs");

			ActionLogModel actionLogModel = new ActionLogModel();
			actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
			actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the process is starting
			actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
			actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
			actionLogModel.setCustomField1(baseWrapper.getObject("appUserId").toString());
			actionLogModel.setCustomField11(baseWrapper.getObject("mfsId").toString());
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
			ForgotVeriflyPinViewModel forgotVeriflyPinViewModel = new ForgotVeriflyPinViewModel();
			forgotVeriflyPinViewModel.setAppUserId(appUserId);
			forgotVeriflyPinViewModel.setSmartMoneyAccountId(smartMoneyAccountId);

			CustomList customList = forgotVeriflyPinViewDAO.findByExample(forgotVeriflyPinViewModel);
			forgotVeriflyPinViewModel = (ForgotVeriflyPinViewModel) customList.getResultsetList().get(0);

			//create accountInfoModel and initialize with data
			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setAccountNick(forgotVeriflyPinViewModel.getAccountNick());
			accountInfoModel.setCustomerId(forgotVeriflyPinViewModel.getCustomerId());
			veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

			//create logModel and initialize with values
			LogModel logModel = new LogModel();

			logModel.setCreatdByUserId(appUserModel.getAppUserId());
			logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());

			veriflyBaseWrapper.setLogModel(logModel);

			Long samrtMoneyAccountId = new Long(baseWrapper.getObject("smartMoneyAccountId").toString());
			SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO
					.findByPrimaryKey(forgotVeriflyPinViewModel.getSmartMoneyAccountId());
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
                	veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_CNIC, forgotVeriflyPinViewModel.getNic() ) ;
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
                	IvrRequestDTO ivrDTO = new IvrRequestDTO();
            		ivrDTO.setCustomerMobileNo(baseWrapper.getObject("mobileNo").toString());
            		ivrDTO.setPin("1234"); //dummy pin to bypass validate pin check
                	ivrDTO.setRetryCount(0);
            		ivrDTO.setProductId(new Long(CommandFieldConstants.REGENERATE_PIN_IVR));
            		try {
            			ivrRequestHandler.makeIvrRequest(ivrDTO);
            		} catch (Exception e) {
            			e.printStackTrace();
            			throw new FrameworkCheckedException(e.getLocalizedMessage());
            		}
                	
                	/* 	
	                veriflyBaseWrapper = abstractFinancialInstitution.resetPin(veriflyBaseWrapper);
	                
					if (veriflyBaseWrapper.isErrorStatus())
					{
						smartMoneyAccountModel.setChangePinRequired(true);
	
						smartMoneyAccountModel.setUpdatedBy(appUserModel.getAppUserId());
						smartMoneyAccountModel.setUpdatedOn(new Date());
						this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
						
						Object[] args = {"Branchless Banking Account",veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin()};
						if(smartMoneyAccountModel.getDistributorContactId() != null || smartMoneyAccountModel.getRetailerContactId() != null)
						{
							String messageString = MessageUtil.getMessage("forgotVeriflyPin.successMessage", args);
							// Its an allPay user so change the sender in the SMS
							//smsSender.send(new SmsMessage(forgotVeriflyPinViewModel.getMobileNo(), messageString, SMSConstants.Sender_1611));
							smsSender.send(new SmsMessage(forgotVeriflyPinViewModel.getMobileNo(), messageString));
							
						}
						else
						{
							String messageString = MessageUtil.getMessage("forgotVeriflyPin.successMessage.customer", args);
							smsSender.send(new SmsMessage(forgotVeriflyPinViewModel.getMobileNo(), messageString));
						}
						
						
	
	//					smsSender.send(new SmsMessage(forgotVeriflyPinViewModel.getMobileNo(), "Dear Customer, your new Bank PIN is "
	//							+ veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin()
	//							+ ". Please change this PIN immediately on first use."));
						//smsSender.send(new SmsMessage("03044116780","Your pin has been changed new pin is "+veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin() +" for account nick "+forgotVeriflyPinViewModel.getAccountNick() ));
					}
					else
					{
						baseWrapper.putObject("errorMessage", veriflyBaseWrapper.getErrorMessage());
					}
                */}

			}
			catch (Exception e)
			{
				
				baseWrapper.putObject("errorMessage", e.getMessage());
				throw new FrameworkCheckedException(e.getMessage(), e);
			}

			/**
			 * Logging Information, Ending Status
			 */
			AppUserModel appUser = appUserDAO.getUser(appUserId);
			actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
			actionLogModel.setCustomField11(baseWrapper.getObject("mfsId").toString());
			actionLogModel = logAction(actionLogModel, false);
		}
		finally
		{
			ThreadLocalActionLog.remove();
		}
		return baseWrapper;
	}
	public BaseWrapper changeAllPayVeriflyPin(BaseWrapper baseWrapper) throws FrameworkCheckedException
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
			actionLogModel.setCustomField11(baseWrapper.getObject("mfsId").toString());
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
			AllpayForgotVeriflyPinViewModel allPayforgotVeriflyPinViewModel = new AllpayForgotVeriflyPinViewModel();
			allPayforgotVeriflyPinViewModel.setAppUserId(appUserId);
			allPayforgotVeriflyPinViewModel.setSmartMoneyAccountId(smartMoneyAccountId);

			CustomList customList = allpayForgotVeriflyPinViewDAO.findByExample(allPayforgotVeriflyPinViewModel);
			allPayforgotVeriflyPinViewModel = (AllpayForgotVeriflyPinViewModel) customList.getResultsetList().get(0);

			//create accountInfoModel and initialize with data
			AccountInfoModel accountInfoModel = new AccountInfoModel();
			accountInfoModel.setAccountNick(allPayforgotVeriflyPinViewModel.getAccountNick());
			if (allPayforgotVeriflyPinViewModel.getCustomerId() != null){
				accountInfoModel.setCustomerId(allPayforgotVeriflyPinViewModel.getCustomerId());	
			}else {
				accountInfoModel.setCustomerId(appUserId);
			}
				
			
			veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

			//create logModel and initialize with values
			LogModel logModel = new LogModel();

			logModel.setCreatdByUserId(appUserModel.getAppUserId());
			logModel.setCreatedBy(appUserModel.getFirstName() + " " + appUserModel.getLastName());

			veriflyBaseWrapper.setLogModel(logModel);

			Long samrtMoneyAccountId = new Long(baseWrapper.getObject("smartMoneyAccountId").toString());
			SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountDAO
					.findByPrimaryKey(allPayforgotVeriflyPinViewModel.getSmartMoneyAccountId());
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
                	veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_CNIC, allPayforgotVeriflyPinViewModel.getNic() ) ;
                	if (smartMoneyAccountModel.getAccountTypeId() != null){
                	veriflyBaseWrapper.putObject( CommandFieldConstants.KEY_ACCOUNT_TYPE, smartMoneyAccountModel.getAccountTypeId().toString() ) ;
                	}
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
                else if(baseWrapper.getObject("mobileNo") != null && !"".equals(baseWrapper.getObject("mobileNo").toString()))
                { 	
                	IvrRequestDTO ivrDTO = new IvrRequestDTO();
                	//to dial number we need to set it in customerMobileNo as well as it is IVR limitations
            		ivrDTO.setCustomerMobileNo(baseWrapper.getObject("mobileNo").toString());
            		ivrDTO.setAgentMobileNo(baseWrapper.getObject("mobileNo").toString());
            		ivrDTO.setPin("1234"); //dummy pin to pass validate pin check
                	ivrDTO.setRetryCount(0);
            		ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
            		try {
            			ivrRequestHandler.makeIvrRequest(ivrDTO);
            		} catch (Exception e) {
            			e.printStackTrace();
            			throw new FrameworkCheckedException(e.getLocalizedMessage());
            		}
                }
                else if(baseWrapper.getObject("handlerMobileNo") != null && !"".equals(baseWrapper.getObject("handlerMobileNo").toString()))
                { 	
                	IvrRequestDTO ivrDTO = new IvrRequestDTO();
            		ivrDTO.setCustomerMobileNo(baseWrapper.getObject("handlerMobileNo").toString());
            		ivrDTO.setHandlerMobileNo(baseWrapper.getObject("handlerMobileNo").toString());
            		ivrDTO.setPin("1234"); //dummy pin to pass validate pin check
                	ivrDTO.setRetryCount(0);
            		ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
            		try {
            			ivrRequestHandler.makeIvrRequest(ivrDTO);
            		} catch (Exception e) {
            			e.printStackTrace();
            			throw new FrameworkCheckedException(e.getLocalizedMessage());
            		}
                }
                //handler case without IVR
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
						
						String messageString = null;
						
						long userType = appUserDAO.findByPrimaryKey(appUserId).getAppUserTypeId().longValue();
						if(UserTypeConstantsInterface.HANDLER == userType)
							messageString = MessageUtil.getMessage("forgotVeriflyPin.successMessage.handler", args);

						else if(UserTypeConstantsInterface.RETAILER == userType)
							messageString = MessageUtil.getMessage("forgotVeriflyPin.successMessage", args);
						
						if(null != messageString)
							smsSender.send(new SmsMessage(allPayforgotVeriflyPinViewModel.getMobileNo(), messageString));
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
			AppUserModel appUser = appUserDAO.getUser(appUserId);
			actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
			actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
			actionLogModel.setCustomField11(baseWrapper.getObject("mfsId").toString());
			actionLogModel = logAction(actionLogModel, false);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error(e);
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

	public void setForgotVeriflyPinViewDAO(ForgotVeriflyPinViewDAO forgotVeriflyPinViewDAO)
	{
		this.forgotVeriflyPinViewDAO = forgotVeriflyPinViewDAO;
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
	public AppUserDAO getAppUserDAO() {
		return appUserDAO;
	}
	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}
	public void setIvrRequestHandler(IvrRequestHandler ivrRequestHandler) {
		this.ivrRequestHandler = ivrRequestHandler;
	}

}
