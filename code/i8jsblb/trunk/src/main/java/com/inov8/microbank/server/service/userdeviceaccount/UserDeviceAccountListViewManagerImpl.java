package com.inov8.microbank.server.service.userdeviceaccount;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.userdeviceaccount.UserDeviceAccountListViewDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commandmodule.CustomerInitiatedAccountToCashCommand;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UserDeviceAccountListViewManagerImpl implements  UserDeviceAccountListViewManager
{

	private UserDeviceAccountListViewDAO userDeviceAccountListViewDAO;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private SmsSender smsSender;
	private MessageSource messageSource;
	
	private ActionLogManager actionLogManager;
	private AppUserDAO appUserDAO;
	private AppUserManager appUserManager;

	protected final Log logger = LogFactory.getLog(UserDeviceAccountListViewManagerImpl.class);

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public BaseWrapper loadUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		
		UserDeviceAccountsModel userDeviceAccountsModel=this.userDeviceAccountsDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
	   return baseWrapper;
	}

	public BaseWrapper updateUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId((Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject(PortalConstants.KEY_USECASE_ID) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
		//actionLogModel.setCustomField2("Creating the New MWallet Account");
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
				
		UserDeviceAccountsModel userDeviceAccountsModel=(UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
		UserDeviceAccountsModel _uDeviceAccModel = new UserDeviceAccountsModel();
		_uDeviceAccModel.setAppUserId(userDeviceAccountsModel.getAppUserId());
		_uDeviceAccModel.setDeviceTypeId(userDeviceAccountsModel.getDeviceTypeId());
		

		String randomPin = "";
		boolean prepaidSMSFlag = false;
		boolean accountEnabledByUser = userDeviceAccountsModel.getAccountEnabled();
		
		Integer count = this.userDeviceAccountsDAO.countByExample(_uDeviceAccModel);
		CustomList <UserDeviceAccountsModel>clist = this.userDeviceAccountsDAO.findByExample(_uDeviceAccModel);
		List <UserDeviceAccountsModel>list = clist.getResultsetList();
		_uDeviceAccModel = list.get(0);
		
		boolean accountEnabledByDB = _uDeviceAccModel.getAccountEnabled();
		boolean smsRequired = false;
		boolean smsForActivateAC = false;			
		boolean smsForCommissionedChanged = false;
		
		// Check whether isCommissioned flag type has been changed  
		UserDeviceAccountsModel existingUserDeviceAccModel = new UserDeviceAccountsModel();
		existingUserDeviceAccModel = this.userDeviceAccountsDAO.findByPrimaryKey(userDeviceAccountsModel.getUserDeviceAccountsId());
		if(existingUserDeviceAccModel.getCommissioned() != userDeviceAccountsModel.getCommissioned()){
			smsForCommissionedChanged = true;
		}		
		
		String acMobileNo = _uDeviceAccModel.getAppUserIdAppUserModel().getMobileNo(); 

		BeanUtils.copyProperties(userDeviceAccountsModel, _uDeviceAccModel);
		
		//if account is not already active and user activate the change new pin
		if(!accountEnabledByDB && accountEnabledByUser)
		{
			//generate new pin if user activate MWallet account
			randomPin = RandomUtils.generateRandom(4,false, true);
			_uDeviceAccModel.setPin(EncoderUtils.encodeToSha(randomPin));
			smsForActivateAC = true;

			_uDeviceAccModel.setAccountLocked(Boolean.FALSE);
			_uDeviceAccModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));
			prepaidSMSFlag = false;
			
		}
		
		Boolean isDeviceTypeChanged = (Boolean)baseWrapper.getObject("isDeviceTypeChanged");
		if(count==1 && isDeviceTypeChanged == false)
		{
			this.userDeviceAccountsDAO.saveOrUpdate(_uDeviceAccModel);
		}
		else if(count == 0 && isDeviceTypeChanged == true)
		{
		    this.userDeviceAccountsDAO.saveOrUpdate(_uDeviceAccModel);
		}
		else
		{
			throw new FrameworkCheckedException("AppUserIdDeviceTypeIdCombinationRepeatException");
		}
		
		//if device type is mobile then call provisioning service 
		if(_uDeviceAccModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.MOBILE.longValue() 
		&&	(!((accountEnabledByDB && accountEnabledByUser) || (!accountEnabledByDB && !accountEnabledByUser))))
		{
			String action = "";
			
			if(_uDeviceAccModel.getAccountEnabled())
			{
			    action = ActionConstantsInterface.ACTION_ACTIVATE;
			}
			else
			{
			    action = ActionConstantsInterface.ACTION_DEACTIVATE;
			}
			smsRequired = true;
		}
		
		//send newly created pin
		if(smsRequired)
		{
			if(smsForActivateAC)
			{
				String messageString = "";
				
				if(prepaidSMSFlag)
				{
					messageString = MessageUtil.getMessage("smsCommand.act_sms8");
				}
				else
				{
					Object[] args ={randomPin};
					messageString = MessageUtil.getMessage("smsCommand.act_sms7", args);
				}
				SmsMessage smsMessage = new SmsMessage(acMobileNo,messageString);
				if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
				{
					smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
				}
				smsSender.send(smsMessage);
				
				
//				sendSMS(_uDeviceAccModel.getUserId(),randomPin,acMobileNo);
			}
			else
			{
				boolean commissioned = false;
				if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
				{
					commissioned = true;
				}
				sendSMSForDeactivate(acMobileNo, commissioned);
			}
		}
		
		// If isCommsioned value has been changed than send an sms to the user
		if(smsForCommissionedChanged){
			String messageString = "";
			if(userDeviceAccountsModel.getCommissioned()){
			    messageString = MessageUtil.getMessage("smsCommand.act_sms9");			    
			}			    
			else{
				messageString = MessageUtil.getMessage("smsCommand.act_sms10");				
			}
			SmsMessage smsMessage = new SmsMessage(acMobileNo,messageString);
			smsSender.send(smsMessage);
			smsMessage = null;
			messageString = null;			
		}
		
		baseWrapper.setBasePersistableModel(_uDeviceAccModel);
		
		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
		//actionLogModel.setCustomField3("New MWallet Account Created MfsId: "+mfsId);
//		actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()) );
		actionLogModel = logAction(actionLogModel);
		
		return baseWrapper;
	}
	
	
	public BaseWrapper updateUserDeviceAccountStatus(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId((Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject(PortalConstants.KEY_USECASE_ID) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
		//actionLogModel.setCustomField2("Creating the New MWallet Account");
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		UserDeviceAccountsModel userDeviceAccountsModel=(UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
		
		String randomPin = "";
		boolean smsForActivatePin = false;
		boolean smsRequires = false;
		
		boolean prepaidSMSFlag = false;
		
		if(userDeviceAccountsModel.getAccountEnabled())
		{
			randomPin = RandomUtils.generateRandom(4,false, true);
			userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
			smsForActivatePin = true;
		
			userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
			userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));
			prepaidSMSFlag = false;
		}
		
		
		this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
		
		//if device type is mobile then call provisioning service 
		if(userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.MOBILE.longValue())
		{
		    
		    String action = "";
			
			if(userDeviceAccountsModel.getAccountEnabled())
			{
			    action = ActionConstantsInterface.ACTION_ACTIVATE;
			}
			else
			{
			    action = ActionConstantsInterface.ACTION_DEACTIVATE;
			}

			smsRequires = true;
		}
		
		if(smsRequires)
		{
			String mobileNo = userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo();
			if(smsForActivatePin)
			{
				String messageString = "";
				
				if(prepaidSMSFlag)
				{
					messageString = MessageUtil.getMessage("smsCommand.act_sms8");
				}
				else
				{
					Object[] args ={randomPin};
					messageString = MessageUtil.getMessage("smsCommand.act_sms7", args);
				}
				SmsMessage smsMessage = new SmsMessage(mobileNo,messageString);
				if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
				{
					smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
				}
				smsSender.send(smsMessage);
				
//				sendSMS(userDeviceAccountsModel.getUserId(),randomPin,mobileNo);			
			}
			else
			{
				boolean commissioned = false;
				if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
				{
					commissioned = true;
				}
				
				sendSMSForDeactivate(mobileNo, commissioned);
			}
		}
		
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		
		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
		//actionLogModel.setCustomField3("New MWallet Account Created MfsId: "+mfsId);
//		actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()) );
		actionLogModel = logAction(actionLogModel);
		
		return baseWrapper;
	}
	@Override
	public BaseWrapper changeUserDeviceAccountPin(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setCustomField11((String)baseWrapper.getObject("mfsId"));
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		UserDeviceAccountsModel userDeviceAccountsModel=(UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
		Long appUserTypeId = (Long) baseWrapper.getObject("appUserTypeId");
		String randomPin = "";
		if(appUserTypeId.longValue()==2){
			userDeviceAccountsModel.setAccountExpired(false);
		}
        randomPin = RandomUtils.generateRandom(4,false, true);
		logger.info("**************************");
		logger.info("Your otp is : "+ randomPin);
		logger.info("**************************");
		//userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		userDeviceAccountsModel.setPin(EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin));
		userDeviceAccountsModel.setPinChangeRequired(true);
		userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));		
		this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
		String mobileNo = userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo();
		String messageString = "";
		Object[] args ={randomPin};
		if(appUserTypeId.longValue()==12)
			messageString = MessageUtil.getMessage("smsCommand.chnage_login_pin_handler", args);
		else if(appUserTypeId.longValue()==3)
			messageString = MessageUtil.getMessage("smsCommand.chnage_login_pin_agent", args);
		else
			messageString = MessageUtil.getMessage("smsCommand.change_login_pin_customer", args);

		SmsMessage smsMessage = new SmsMessage(mobileNo,messageString);
		smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
		smsSender.send(smsMessage);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		baseWrapper.putObject("actionLogModel",actionLogModel);
		return baseWrapper;
	}

	@Override
	public BaseWrapper changeUserDeviceAccountForgotPin(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId((Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject(PortalConstants.KEY_USECASE_ID) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
		if(UserUtils.getCurrentUser() != null){
			actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
			actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		}
		else{
			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setUserName(ThreadLocalAppUser.getAppUserModel().getUsername());
		}
		actionLogModel.setCustomField11((String)baseWrapper.getObject("mfsId"));
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
		//actionLogModel.setCustomField2("Creating the New MWallet Account");
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		UserDeviceAccountsModel userDeviceAccountsModel=(UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
		Long appUserTypeId = (Long) baseWrapper.getObject("appUserTypeId");

		String randomPin = "";

		if(appUserTypeId.longValue()==2){
			userDeviceAccountsModel.setAccountExpired(false);
		}

		//userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		userDeviceAccountsModel.setPin((String)baseWrapper.getObject(CommandFieldConstants.KEY_PIN));

		userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));
		this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

		String mobileNo = userDeviceAccountsModel.getAppUserIdAppUserModel().getMobileNo();

		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);

		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
		actionLogModel = logAction(actionLogModel);
		return baseWrapper;
	}
	
	public BaseWrapper createUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		boolean prepaidMobileSMSFlag = false;
		
		
		ActionLogModel actionLogModel = new ActionLogModel();
		
		actionLogModel.setActionId((Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject(PortalConstants.KEY_USECASE_ID) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
		//actionLogModel.setCustomField2("Creating the New MWallet Account");
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		UserDeviceAccountsModel userDeviceAccountsModel=(UserDeviceAccountsModel)baseWrapper.getBasePersistableModel();
		
		if (!this.isMfsIdUnique(userDeviceAccountsModel.getUserId()))
		{
			throw new FrameworkCheckedException("UserIdUniqueException");
		}
		
		UserDeviceAccountsModel _uDeviceAccModel = new UserDeviceAccountsModel();
		_uDeviceAccModel.setAppUserId(userDeviceAccountsModel.getAppUserId());
		_uDeviceAccModel.setDeviceTypeId(userDeviceAccountsModel.getDeviceTypeId());
		Integer count = this.userDeviceAccountsDAO.countByExample(_uDeviceAccModel);
		if(count > 0)
		{
			throw new FrameworkCheckedException("AppUserIdDeviceTypeIdCombinationRepeatException");
		}
		
		
		AppUserModel tempAppUserModel = new AppUserModel();
		tempAppUserModel = this.appUserManager.getUser(userDeviceAccountsModel.getAppUserId().toString());

		
		//Commented by Rizwan ur Rehman against the implementation of CRF-20
		
		if (userDeviceAccountsModel.getDeviceTypeId().equals(DeviceTypeConstantsInterface.MOBILE)&& userDeviceAccountsModel.getAccountEnabled()==true)

		{			
		    userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
            userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));
            prepaidMobileSMSFlag = false;
		
			String randomPin=RandomUtils.generateRandom(4,false, true);
			userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		
			this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
			
			String messageString = "";
			
			if(prepaidMobileSMSFlag)
			{
				messageString = MessageUtil.getMessage("smsCommand.act_sms8");
			}
			else
			{
				Object[] args = {userDeviceAccountsModel.getUserId(),randomPin,MessageUtil.getMessage("mfsDownloadURL")};
				messageString = MessageUtil.getMessage("smsCommand.act_sms6", args);
			}	
			SmsMessage smsMessage = new SmsMessage(tempAppUserModel.getMobileNo(), messageString);
			smsSender.send(smsMessage);
		
		}

		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setCustomField1( String.valueOf(tempAppUserModel.getAppUserId()) );
		actionLogModel = logAction(actionLogModel);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		return baseWrapper;
	}
	
	private boolean isMfsIdUnique(String mfsId) 
	{
		UserDeviceAccountsModel appUserModel = new UserDeviceAccountsModel();
		appUserModel.setUserId(mfsId);
		CustomList list = this.userDeviceAccountsDAO.findByExample(appUserModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		int size = list.getResultsetList().size();
		if (size == 0)
			return true;
		else
			return false;
	}
	
	public SearchBaseWrapper searchUserDeviceAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		CustomList<UserDeviceAccountListViewModel> list = this.userDeviceAccountListViewDAO
		.findByExample((UserDeviceAccountListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
				
		return searchBaseWrapper;
	}
	public UserDeviceAccountsModel findUserDeviceByAppUserId(Long appUserId) throws FrameworkCheckedException {
		
		return userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserId);
	}
	
	public void sendSMS( String mfsId, String randomPin, String mobileNo )
	{
		Object[] args = {mfsId,randomPin,MessageUtil.getMessage("mfsDownloadURL")};
		
		String messageString = MessageUtil.getMessage("smsCommand.act_sms6", args);
		
//		String messageString = "Dear Customer, your new MWallet account is now active. Your MWallet ID is "+
//		mfsId+" and PIN is: "+randomPin+". Point browser to "+this.messageSource.getMessage("mfsDownloadURL", null, null);
		
		SmsMessage smsMessage = new SmsMessage(mobileNo, messageString);
		try
		{
			smsSender.send(smsMessage);
		}
		catch (FrameworkCheckedException e)
		{			
			e.printStackTrace();
		}		
	}

	public void sendSMSForDeactivate(String mobileNo,boolean isCommissioned)
	{
		String messageString = "Your account has been deactivated. Please call helpline to reactivate.";
		if(isCommissioned)
		{
			messageString = MessageParsingUtils.parseMessageForIpos(messageString);
		}
		SmsMessage smsMessage = new SmsMessage(mobileNo, messageString);
		try
		{
			smsSender.send(smsMessage);
		}
		catch (FrameworkCheckedException e)
		{			
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * Method Logs the action performed in the action log table
	 */
	private ActionLogModel logAction( ActionLogModel actionLogModel ) 
	                                                   throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if( null == actionLogModel.getActionLogId() ){
		baseWrapper = 
			this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		}
		else{
		baseWrapper =
			this.actionLogManager.createOrUpdateActionLog(baseWrapper);
		}
		return (ActionLogModel)baseWrapper.getBasePersistableModel();
	}
	
	
	
	
	public void setUserDeviceAccountListViewDAO(
			UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) {
		this.userDeviceAccountListViewDAO = userDeviceAccountListViewDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	/**
	 * @param actionLogManager the actionLogManager to set
	 */
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}


}
