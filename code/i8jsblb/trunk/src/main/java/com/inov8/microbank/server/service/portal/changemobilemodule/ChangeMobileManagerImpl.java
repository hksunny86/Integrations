package com.inov8.microbank.server.service.portal.changemobilemodule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.changemobilemodule.ChangemobileListViewModel;
import com.inov8.microbank.common.util.CommandConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MobileTypeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SMSConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.changemobilemodule.ChangeMobileListViewDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class ChangeMobileManagerImpl implements ChangeMobileManager {
	private ChangeMobileListViewDAO changeMobileListViewDAO;

	private AppUserManager appUserManager;
	private ActionLogManager actionLogManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private SmsSender smsSender;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private MessageSource messageSource;

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public SearchBaseWrapper loadChangeMobile(
			SearchBaseWrapper searchBaseWrapper) {

		searchBaseWrapper.setBasePersistableModel(this.changeMobileListViewDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey()));
		return searchBaseWrapper;
	}

	public BaseWrapper loadChangeMobile(BaseWrapper baseWrapper) {
		return null;
	}

	public SearchBaseWrapper searchChangeMobile(
			SearchBaseWrapper searchBaseWrapper) {
		CustomList<ChangemobileListViewModel> list = this.changeMobileListViewDAO
				.findByExample((ChangemobileListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper updateChangeMobile(BaseWrapper baseWrapper)
			throws FrameworkCheckedException

	{
		long useCaseId = 0;
		long actionId = 0;
        Date nowDate = new Date();
		actionId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);
		useCaseId = (Long) baseWrapper
				.getObject(PortalConstants.KEY_USECASE_ID);
		
		
		ChangemobileListViewModel changemobileListViewModel = (ChangemobileListViewModel) baseWrapper
				.getObject("changemobileListViewModel");
		ActionLogModel actionLogModel = logBeforeAction(actionId, useCaseId, changemobileListViewModel.getAppUserId());
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		AppUserModel appUserModel = this.appUserManager
				.getUser(String.valueOf(changemobileListViewModel.getAppUserId()));
		
		String oldMobileNo = appUserModel.getMobileNo();
		Long dbMobileType = appUserModel.getMobileTypeId();
		
		boolean CHANGE_MOBILE_NO_FLAGE = false;
		boolean CHANGE_MOBILE_TYPE_FLAGE = false;
		if(!appUserModel.getMobileNo().trim().equals(changemobileListViewModel.getMobileNo().trim()) )
		{
			CHANGE_MOBILE_NO_FLAGE =  true;
			/**
			 * As the mobile no is changed, it is checked that the new entered mobile no
			 * must not exist already in database, if it dose, an exception is thrown
			 */
			
			/*if(!isMobileNumUnique(changemobileListViewModel.getMobileNo())){
				throw new FrameworkCheckedException("MobileNumberUniqueException");
			}
			*/
			appUserModel.setMobileNo(changemobileListViewModel.getMobileNo().trim());
		}
		
		if(!appUserModel.getMobileTypeId().equals(changemobileListViewModel.getMobileTypeId()))
			CHANGE_MOBILE_TYPE_FLAGE = true;
			
		baseWrapper.setBasePersistableModel(appUserModel);
		try {
			appUserModel.setMobileTypeId(changemobileListViewModel.getMobileTypeId());
			if(CHANGE_MOBILE_TYPE_FLAGE || CHANGE_MOBILE_NO_FLAGE){
			    appUserModel.setUpdatedOn(nowDate);
			    appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			}

			
				this.appUserManager.saveUser(appUserModel);
			
			
			



			
			/**
	         * $Reference Documents 
	         * Integration Description and flows version 1.6 - section 2.1.8 - Change mobile number - point 14.
			 * $Description
			 * 1. If number change is postpaid to prepaid, then lock user and send sms for service charges. set service
			 *    expiration date to one year from current date.
			 * 2. If number change is from prepaid to postpaid then set expiration date to infinity.
			 * 3. If number change is from prepaid to prepaid and postpaid to postpaid then no change is requied.    
			 */

			if( dbMobileType.longValue() != changemobileListViewModel.getMobileTypeId().longValue() ){
				UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
				userDeviceAccountsModel.setAppUserId(changemobileListViewModel.getAppUserId());
				userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
				
				CustomList <UserDeviceAccountsModel>clist = userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
				List <UserDeviceAccountsModel>rlist = clist.getResultsetList();
				userDeviceAccountsModel = rlist.get(0);
				
				if(MobileTypeConstantsInterface.MOBILE_TYPE_ID_POST_PAID.longValue() == dbMobileType.longValue()  && 
						MobileTypeConstantsInterface.MOBILE_TYPE_ID_PRE_PAID.longValue() == changemobileListViewModel.getMobileTypeId().longValue()){
					userDeviceAccountsModel.setAccountLocked(true);
					userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), 1));

					SmsMessage smsMessage = 
		            	 new SmsMessage(appUserModel.getMobileNo(), this.messageSource.getMessage("text.sms.activationcharges", null, null),SMSConstants.Sender_1611);
		            smsSender.send(smsMessage);

				} else
					if(MobileTypeConstantsInterface.MOBILE_TYPE_ID_PRE_PAID.longValue() ==  dbMobileType.longValue() && 
							MobileTypeConstantsInterface.MOBILE_TYPE_ID_POST_PAID.longValue() == changemobileListViewModel.getMobileTypeId().longValue() ){
						userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));
				}
				
			}
			
			
			/**
			 * Now sending the SMS of change mobile no, first checking the number was changed
			 */
			if(CHANGE_MOBILE_NO_FLAGE)
			{
				
				//Updated against CRF-28.
				
				Object[] args = {appUserModel.getUsername(),oldMobileNo,appUserModel.getMobileNo()};
				String messageString = MessageUtil.getMessage("ChangeMobile.mobileChanged", args);
				
//				String messageString = "Dear Customer, the mobile number attached with your MWallet account has been changed. The new mobile number is "+appUserModel.getMobileNo();
                SmsMessage smsMessage = 
            	     new SmsMessage(appUserModel.getMobileNo(), messageString);
                smsSender.send(smsMessage);
			}
			
			actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
			logAfterAction(actionLogModel,String.valueOf(appUserModel.getAppUserId()));
		}
		catch(Exception e)
		{
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
			else
			{
				throw new FrameworkCheckedException(e.getMessage());
			}
		}

		return baseWrapper;

	}

	private ActionLogModel logBeforeAction(Long actionId, Long useCaseId, Long appUserId)
			throws FrameworkCheckedException {
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId(actionId);
		actionLogModel.setUsecaseId(useCaseId);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the
		// process
		// is
		// starting
		actionLogModel.setCustomField1(String.valueOf(appUserId) );
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
		return actionLogModel;
	}

	private void logAfterAction(ActionLogModel actionLogModel,String appUserId)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // the
		// 	process
		// is
		// 	starting
		
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLog(baseWrapperActionLog);
	}

	/**
	 * Method checks if mobile number is Unique, if 
	 * unique returns true , else return false
	 */
	private boolean isMobileNumUnique(String mobileNo) throws FrameworkCheckedException{
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(appUserModel);
		searchBaseWrapper = this.appUserManager.searchAppUserByMobile(searchBaseWrapper);
		CustomList list = searchBaseWrapper.getCustomList();
		int size = list.getResultsetList().size();
		if(size == 0)
			return true;
		else
			return false;
	}
	
	public BaseWrapper createChangeMobile(BaseWrapper baseWrapper) {
		return null;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setChangeMobileListViewDAO(
			ChangeMobileListViewDAO changeMobileListViewDAO) {
		this.changeMobileListViewDAO = changeMobileListViewDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @param userDeviceAccountsDAO the userDeviceAccountsDAO to set
	 */
	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

}
