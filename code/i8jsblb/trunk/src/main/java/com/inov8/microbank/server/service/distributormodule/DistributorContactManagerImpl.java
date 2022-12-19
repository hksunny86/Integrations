package com.inov8.microbank.server.service.distributormodule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.GoldenNosModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.distributormodule.DistributorContactFormModel;
import com.inov8.microbank.common.model.distributormodule.DistributorContactListViewModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SMSConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactListViewDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorLevelDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

//import com.inov8.microbank.server.dao.usermodule.*;

/**
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Backend Application for POS terminals.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Inov8 Limited
 * </p>
 * 
 * @author Asad Hayat
 * @version 1.0
 */

public class DistributorContactManagerImpl implements DistributorContactManager {

	private DistributorContactDAO distributorContactDAO;

	private DistributorContactListViewDAO distributorContactListViewDAO;

	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	
	private DistributorDAO distributorDAO;
	
	private DistributorLevelDAO distributorLevelDAO;

	private SmsSender smsSender;

	private GoldenNosDAO goldenNosDAO;

	private OracleSequenceGeneratorJdbcDAO sequenceGenerator;

	private AppUserDAO appUserDAO;
	
	private ActionLogManager actionLogManager;
	
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;


	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public SearchBaseWrapper loadDistributorContact(
			SearchBaseWrapper searchBaseWrapper) {
		DistributorContactModel distributorContactModel = this.distributorContactDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(distributorContactModel);
		return searchBaseWrapper;
	}

	public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) {
		DistributorContactModel distributorContactModel = this.distributorContactDAO
				.findByPrimaryKey(baseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		baseWrapper.setBasePersistableModel(distributorContactModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchDistributorContact(
			SearchBaseWrapper searchBaseWrapper) {

		CustomList<DistributorContactListViewModel> list = this.distributorContactListViewDAO
				.findByExample(
						(DistributorContactListViewModel) searchBaseWrapper
								.getBasePersistableModel(), searchBaseWrapper
								.getPagingHelperModel(), searchBaseWrapper
								.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public DistributorContactModel findDistributorContactByMobileNumber(
			SearchBaseWrapper searchBaseWrapper) {
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);

		AppUserModel appUser = (AppUserModel) searchBaseWrapper
				.getBasePersistableModel();
		CustomList<AppUserModel> appUserList = this.appUserDAO.findByExample(
				appUser, null, null, exampleHolder);

		if (null != appUserList && null != appUserList.getResultsetList()
				&& appUserList.getResultsetList().size() > 0) {
			appUser = getDistributorContactFrmList(appUserList
					.getResultsetList());
			if (appUser != null) {
				return this.distributorContactDAO.findByPrimaryKey(appUser
						.getDistributorContactId());
			}
		}
		return null;
	}

	private AppUserModel getDistributorContactFrmList(
			List<AppUserModel> appUserList) {
		for (AppUserModel appUserModel : appUserList) {
			if (appUserModel.getDistributorContactId() != null
					&& appUserModel.getDistributorContactId() > 0)
				return appUserModel;
		}
		return null;
	}

	public DistributorContactModel findDistributorNationalManagerContact(
			SearchBaseWrapper searchBaseWrapper) {
		DistributorContactModel distributorContactModel = new DistributorContactModel();
		DistributorModel  distributorModel = (DistributorModel) searchBaseWrapper.getBasePersistableModel();
		distributorContactModel.setDistributorId(distributorModel.getDistributorId());
		distributorContactModel.setHead(true);
		CustomList<DistributorContactModel> list = this.distributorContactDAO
				.findByExample(distributorContactModel);
		if (null != list && null != list.getResultsetList()
				&& list.getResultsetList().size() > 0) {
			distributorContactModel = list.getResultsetList().get(0);
		}
		return distributorContactModel;
	}

	
	public BaseWrapper createDistributorContact(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		DistributorContactModel distributorContactModel = (DistributorContactModel) baseWrapper
				.getObject("distributorContactModel");
		AppUserModel appUserModel = (AppUserModel) baseWrapper
				.getObject("appUserModel");
        
		/**
		 * Checking if mobile num is unique, otherwise throwing exception.
		 */
		/*if(!isMobileNumUnique(appUserModel.getMobileNo())){
			throw new FrameworkCheckedException("MobileNumUniqueException");
		}
		*/
		
		/**
		 * Checking if username is unique
		 */
		if(!isUsernameUnique(appUserModel.getUsername())){
			throw new FrameworkCheckedException("UsernameUniqueException");
		}
		
		if (distributorContactModel.getHead()!=null && distributorContactModel.getHead()==true)
		{
			DistributorContactModel	newRetailerContactModel =new DistributorContactModel();
			newRetailerContactModel.setDistributorId(distributorContactModel.getDistributorId());
			newRetailerContactModel.setHead(distributorContactModel.getHead());
			 int recordCount=this.distributorContactDAO.countByExample(newRetailerContactModel);
			if(recordCount!=0)
			{
				throw new FrameworkCheckedException("HeadUniqueException");
			}
			/*if(!isDistributorLevelHead(distributorContactModel)){
				throw new FrameworkCheckedException("HeadLevelUniqueException");
			}*/
			
			distributorContactModel.setHead(true);
		}
		/*
		distributorContactModel.setAccountEnabled(retailerContactListViewFormModel
				.getAccountEnabled() == null ? false
				: retailerContactListViewFormModel.getAccountEnabled()); */
		if (distributorContactModel.getHead()!=null && distributorContactModel.getHead()==false)
		{
			distributorContactModel.setHead(false);
		}
		
		
		distributorContactModel.setCreatedBy(UserUtils.getCurrentUser()
				.getAppUserId());
		distributorContactModel.setUpdatedBy(UserUtils.getCurrentUser()
				.getAppUserId());
		distributorContactModel.setUpdatedOn(new Date());
		distributorContactModel.setCreatedOn(new Date());
		distributorContactModel.setBalance(0.0);
		distributorContactModel = this.distributorContactDAO
				.saveOrUpdate(distributorContactModel);

		appUserModel.setDistributorContactId(distributorContactModel
				.getDistributorContactId());

		appUserModel.setPasswordChangeRequired(false);
		baseWrapper.putObject("appUserModel", appUserModel);
		baseWrapper=createAppUserForDistributorContact(baseWrapper);

		
		 appUserModel = (AppUserModel) baseWrapper
		.getObject("savedAppUserModel");
		 
		 DistributorContactFormModel distributorContactFormModel = (DistributorContactFormModel) baseWrapper.getObject("distributorContactFormModel");
		 
		 AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
		    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
		    appUserPartnerGroupModel.setPartnerGroupId(distributorContactFormModel.getPartnerGroupId());
		    appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setCreatedOn(new Date());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		    
		baseWrapper.setBasePersistableModel(distributorContactModel);
		
		return baseWrapper;
	}

	public BaseWrapper updateDistributorContact(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId((Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject(PortalConstants.KEY_USECASE_ID) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); 
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		//actionLogModel.setCustomField2("Updating the User Information");
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		DistributorContactModel distributorContactModel = (DistributorContactModel) baseWrapper
				.getObject("distributorContactModel");
		AppUserModel appUserModel = (AppUserModel) baseWrapper
				.getObject("appUserModel");

		AppUserModel tempAppUserModel = new AppUserModel();
		DistributorContactModel tempDistributorContactModel = new DistributorContactModel();

		tempAppUserModel = this.appUserDAO.findByPrimaryKey(appUserModel
				.getAppUserId());
		
		String oldMobileNo = appUserModel.getMobileNo();
		
		String dbMobileNo = tempAppUserModel.getMobileNo(); 
        
		/**
		 * Checking if mobile num is unique, otherwise throwing exception.
		 * First checking the mobile no actually has changed on the screen.
		 * If yes then checking for mobile no uniqueness.
		 */
		/*if(!appUserModel.getMobileNo().equals(tempAppUserModel.getMobileNo())){
			if(!isMobileNumUnique(appUserModel.getMobileNo())){
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
		}
		*/
		
		/**
		 * Checking if username is unique, otherwise throwing exception.
		 * First checking the username actually has changed on the screen.
		 * If yes then checking for username uniqueness.
		 */
		if(!appUserModel.getUsername().equals(tempAppUserModel.getUsername())){
			if(!isUsernameUnique(appUserModel.getUsername())){
				throw new FrameworkCheckedException("UsernameUniqueException");
			}
		}
		
		tempAppUserModel.setFirstName(appUserModel.getFirstName());
		tempAppUserModel.setLastName(appUserModel.getLastName());
		tempAppUserModel.setAddress1(appUserModel.getAddress1());
		tempAppUserModel.setAddress2(appUserModel.getAddress2());
		tempAppUserModel.setCity(appUserModel.getCity());
		tempAppUserModel.setState(appUserModel.getState());
		tempAppUserModel.setCountry(appUserModel.getCountry());
		tempAppUserModel.setZip(appUserModel.getZip());
		tempAppUserModel.setFax(appUserModel.getFax());
		tempAppUserModel.setEmail(appUserModel.getEmail());
		tempAppUserModel.setMobileTypeId(appUserModel.getMobileTypeId());
		tempAppUserModel.setMobileNo(appUserModel.getMobileNo());
		tempAppUserModel.setNic(appUserModel.getNic());
		tempAppUserModel.setDob(appUserModel.getDob());
		tempAppUserModel.setAccountEnabled(appUserModel.getAccountEnabled());
		tempAppUserModel.setAccountExpired(appUserModel.getAccountExpired());
		tempAppUserModel.setAccountLocked(appUserModel.getAccountLocked());
		tempAppUserModel.setVerified(appUserModel.getVerified());
		tempAppUserModel.setCredentialsExpired(appUserModel
				.getCredentialsExpired());
		tempAppUserModel.setPasswordHint(appUserModel.getPasswordHint());
		tempAppUserModel
				.setMotherMaidenName(appUserModel.getMotherMaidenName());

		
		tempAppUserModel
				.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		tempAppUserModel.setUpdatedOn(new Date());

		tempAppUserModel.setVersionNo(appUserModel.getVersionNo());

		

		tempDistributorContactModel
				.setDistributorContactId(distributorContactModel
						.getDistributorContactId());
		tempDistributorContactModel = this.distributorContactDAO
				.findByPrimaryKey(distributorContactModel
						.getDistributorContactId());
		tempDistributorContactModel.setDescription(distributorContactModel
				.getDescription());
		tempDistributorContactModel.setComments(distributorContactModel
				.getComments());
		tempDistributorContactModel.setAreaId(distributorContactModel
				.getAreaId());
		tempDistributorContactModel
				.setDistributorLevelId(distributorContactModel
						.getDistributorLevelId());
		tempDistributorContactModel
				.setManagingContactId(distributorContactModel
						.getManagingContactId());
		tempDistributorContactModel.setDistributorId(distributorContactModel
				.getDistributorId());
		tempDistributorContactModel.setVersionNo(distributorContactModel
				.getVersionNo());
		tempDistributorContactModel.setUpdatedBy(UserUtils.getCurrentUser()
				.getAppUserId());
		tempDistributorContactModel.setUpdatedOn(new Date());
		tempDistributorContactModel.setBalance(distributorContactModel
				.getBalance());
		
		/*tempDistributorContactModel.setActive(tempAppUserModel
				.getAccountEnabled());		*/
	
		if (!tempDistributorContactModel.getHead().equals(distributorContactModel.getHead()))
		{
		if (distributorContactModel.getHead()!=null && distributorContactModel.getHead()==true)
		{
			DistributorContactModel	newRetailerContactModel =new DistributorContactModel();
			newRetailerContactModel.setDistributorId(distributorContactModel.getDistributorId());
			newRetailerContactModel.setHead(distributorContactModel.getHead());
			 int recordCount=this.distributorContactDAO.countByExample(newRetailerContactModel);
			if(recordCount!=0)
			{
				throw new FrameworkCheckedException("HeadUniqueException");
			}
			
		/*	if(!isDistributorLevelHead(distributorContactModel)){
				throw new FrameworkCheckedException("HeadLevelUniqueException");
			}*/
			tempDistributorContactModel.setHead(distributorContactModel.getHead());
			
		}
		else if (distributorContactModel.getHead()!=null && distributorContactModel.getHead()==false)
		{
		tempDistributorContactModel.setHead(false);
		}
		
		}
		// check appuser userDevice
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		int  recordCount = userDeviceAccountsDAO.countByExample(userDeviceAccountsModel);
		
		if(!appUserModel.getMobileNo().equals(dbMobileNo)&& recordCount==1)
		{

		
					Object[] args = {appUserModel.getUsername(),oldMobileNo,appUserModel.getMobileNo()};
					
					String messageString = MessageUtil.getMessage("ChangeMobile.mobileChanged", args);
					
//					String messageString = "Dear Customer, the mobile number attached with your MWallet account has been changed. The new mobile number is "+appUserModel.getMobileNo();
		            SmsMessage smsMessage = 
		            	 new SmsMessage(appUserModel.getMobileNo(), messageString,SMSConstants.Sender_1611);
		            smsSender.send(smsMessage);
		}
		
		
		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setCustomField1( String.valueOf(appUserModel.getAppUserId()) );
		actionLogModel = logAction(actionLogModel);

		try{
		
			this.appUserDAO.saveOrUpdate(tempAppUserModel);
	}
	catch (Exception e) {
		// TODO Auto-generated catch block
		if(e.getMessage().contains("i8-20001"))
		{
			throw new FrameworkCheckedException("MobileNumUniqueException");
		}
		else
		{
			throw new FrameworkCheckedException(e.getMessage());
		}
  }
	
		distributorContactModel = this.distributorContactDAO
				.saveOrUpdate(tempDistributorContactModel);

		
		DistributorContactFormModel distributorContactFormModel = (DistributorContactFormModel) baseWrapper.getObject("distributorContactFormModel");
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    CustomList list=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
	    if(list.getResultsetList().size()>0)
	    {
	    	
	    	appUserPartnerGroupModel=(AppUserPartnerGroupModel)list.getResultsetList().get(0);
	    	appUserPartnerGroupModel.setPartnerGroupId(distributorContactFormModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }


		
		baseWrapper.setBasePersistableModel(distributorContactModel);

		return baseWrapper;
	}

	public BaseWrapper updateDistributorContactModel(BaseWrapper baseWrapper) {
		DistributorContactModel distributorContactModel = this.distributorContactDAO
				.saveOrUpdate((DistributorContactModel) baseWrapper
						.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(distributorContactModel);
		return baseWrapper;

	}

	public void createUserDeviceAccount(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		AppUserModel appUserModel = (AppUserModel) baseWrapper.getObject("savedAppUserModel"); 
		DistributorContactFormModel distributorContactFormModel = (DistributorContactFormModel) baseWrapper.getObject("distributorContactFormModel");
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		Date nowDate = new Date();

		userDeviceAccountsModel.setAccountEnabled(distributorContactFormModel
				.getAccountEnabled() == null ? false
						: new Boolean(distributorContactFormModel.getAccountEnabled()));
		userDeviceAccountsModel.setAccountExpired(distributorContactFormModel
				.getAccountExpired() == null ? false
						: new Boolean(distributorContactFormModel.getAccountExpired()));
		userDeviceAccountsModel.setAccountLocked(distributorContactFormModel
				.getAccountLocked() == null ? false
						: new Boolean(distributorContactFormModel.getAccountLocked()));
		userDeviceAccountsModel.setCredentialsExpired(distributorContactFormModel
				.getCredentialsExpired() == null ? false
						: new Boolean(distributorContactFormModel.getCredentialsExpired()));
		
		userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils
				.getCurrentUser());
		userDeviceAccountsModel.setCreatedOn(nowDate);
		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils
				.getCurrentUser());
		userDeviceAccountsModel.setUpdatedOn(nowDate);
		userDeviceAccountsModel.setPinChangeRequired(true);
		userDeviceAccountsModel
				.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		

		String mfsId = computeMfsId();

		String randomPin = RandomUtils.generateRandom(4, false, true);

		userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
		userDeviceAccountsModel.setUserId(mfsId);
		userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		userDeviceAccountsModel = this.userDeviceAccountsDAO
				.saveOrUpdate(userDeviceAccountsModel);
		 
		Object[] args = {mfsId,randomPin};
		
		String messageString = MessageUtil.getMessage("customer.mfsAccountCreated", args);
		
//		String messageString = "Dear Customer Your New MWallet Account is created Your MFSID is:"
//				+ mfsId + " and Pin is: " + randomPin;
		SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(),
				messageString,SMSConstants.Sender_1611);
		smsSender.send(smsMessage);
		baseWrapper.putObject("distributorContactFormModel", distributorContactFormModel);

	}

	private boolean isMobileNumUnique(String mobileNo) {
		AppUserModel appUserModel = new AppUserModel();
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		appUserModel.setMobileNo(mobileNo);
		CustomList list = this.appUserDAO.findByExample(appUserModel);
		int  recordCount = appUserDAO.countByExample(appUserModel,exampleHolder);
		int size = list.getResultsetList().size();
		if (size == 0 || recordCount==0)
			return true;
		else
			return false;
	}
	
	private boolean isUsernameUnique(String username) {
		AppUserModel appUserModel = new AppUserModel();
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		appUserModel.setUsername(username);
		CustomList list = this.appUserDAO.findByExample(appUserModel);
		int  recordCount = appUserDAO.countByExample(appUserModel,exampleHolder);
		int size = list.getResultsetList().size();
		if (size == 0 || recordCount==0)
			return true;
		else
			return false;
	}

	private String computeMfsId() {
		AppUserModel appUserModel;
		GoldenNosModel goldenNosModel;
		Long nextLongValue = null;
		boolean flag = true;

		while (flag) {
			nextLongValue = this.sequenceGenerator.nextLongValue();
			appUserModel = new AppUserModel();
			goldenNosModel = new GoldenNosModel();
			appUserModel.setUsername(String.valueOf(nextLongValue));
			goldenNosModel.setGoldenNumber(String.valueOf(nextLongValue));
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			int countAppUser = this.appUserDAO.countByExample(appUserModel,exampleHolder);
			int countGoldenNos = this.goldenNosDAO
					.countByExample(goldenNosModel);
			if (countAppUser == 0 && countGoldenNos == 0) {
				flag = false;
			}
		}

		return String.valueOf(nextLongValue);
	}
	
	public boolean isDistributorContact(SearchBaseWrapper searchBaseWrapper) {
		DistributorContactModel distributorContactModel = new DistributorContactModel();
		DistributorModel  distributorModel = (DistributorModel) searchBaseWrapper.getBasePersistableModel();
		//if (distributorContactModel.getDistributorId())
		distributorContactModel.setDistributorId(distributorModel.getDistributorId());
		
		
		CustomList list = this.distributorContactDAO.findByExample(distributorContactModel);
		int size = list.getResultsetList().size();
		if (size == 0)
			return true;
		else
			return false;
	}
	
	public boolean isDistributorContactHead(SearchBaseWrapper searchBaseWrapper) {
		DistributorContactModel distributorContactModel = new DistributorContactModel();
		DistributorModel  distributorModel = (DistributorModel) searchBaseWrapper.getBasePersistableModel();
		distributorContactModel.setDistributorId(distributorModel.getDistributorId());
		distributorContactModel.setActive(true);
		distributorContactModel.setHead(true);
		CustomList list = this.distributorContactDAO.findByExample(distributorContactModel);
		int size = list.getResultsetList().size();
		if (size == 0)
			return true;
		else
			return false;
	}
	
	
	private boolean isDistributorLevelHead(DistributorContactModel  distributorContactModel) {
		
		//DistributorLevelModel distributorLevelModel =distributorContactModel.getDistributorLevelIdDistributorLevelModel();
		DistributorLevelModel distributorLevelModel= new DistributorLevelModel();
		distributorLevelModel.setDistributorLevelId(distributorContactModel.getDistributorLevelId());
		// commented by Rashid Starts
		/*distributorLevelModel.setDistributorId(distributorContactModel.getDistributorId());*/
		// commented by Rashid Ends
		distributorLevelModel.setManagingLevelId(null);
		distributorLevelModel.setUltimateManagingLevelId(null);
		int size = this.distributorLevelDAO.countByExample(distributorLevelModel);
		//int size = list.getResultsetList().size();
		if (size == 0 || size==1)
			return true;
		else
			return false;
	}
	public boolean isDistributorContactActive(SearchBaseWrapper searchBaseWrapper) {
		DistributorContactModel distributorContactModel = new DistributorContactModel();
		DistributorModel  distributorModel = (DistributorModel) searchBaseWrapper.getBasePersistableModel();
		distributorContactModel.setDistributorId(distributorModel.getDistributorId());
		distributorContactModel.setActive(true);
		
		CustomList list = this.distributorContactDAO.findByExample(distributorContactModel);
		int size = list.getResultsetList().size();
		if (size == 0)
			return true;
		else
			return false;
	}
	
	public boolean isDistributorActive(SearchBaseWrapper searchBaseWrapper) {
		
		DistributorModel  distributorModel = (DistributorModel) searchBaseWrapper.getBasePersistableModel();
		
		distributorModel.setActive(true);
		
		CustomList list = this.distributorDAO.findByExample(distributorModel);
		int size = list.getResultsetList().size();
		if (size == 0)
			return true;
		else
			return false;
	}
	
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
	
	public void setDistributorDAO(DistributorDAO distributorDAO) {
		this.distributorDAO = distributorDAO;
	}

	public void setGoldenNosDAO(GoldenNosDAO goldenNosDAO) {
		this.goldenNosDAO = goldenNosDAO;
	}

	public void setSequenceGenerator(
			OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setUserDeviceAccountsDAO(
			UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public boolean findDistributorCreditByDistributorContactId(
			BaseWrapper baseWrapper) {
		DistributorContactModel distributorContactModel = (DistributorContactModel) baseWrapper
				.getBasePersistableModel();

		return false;
	}

	public void setDistributorContactDAO(
			DistributorContactDAO distributorContactDAO) {
		this.distributorContactDAO = distributorContactDAO;

	}

	public void setDistributorContactListViewDAO(
			DistributorContactListViewDAO distributorContactListViewDAO) {
		this.distributorContactListViewDAO = distributorContactListViewDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public BaseWrapper createAppUserForDistributorContact(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		AppUserModel appUserModel = (AppUserModel) baseWrapper
				.getObject("appUserModel");

		appUserModel.setCreatedOn(new Date());
		appUserModel.setUpdatedOn(new Date());
		appUserModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
		try
		{
			this.appUserDAO.saveUser(appUserModel);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
			else
			{
				throw new FrameworkCheckedException(e.getMessage());
			}
	  }
		baseWrapper.putObject("savedAppUserModel", appUserModel);
		//createUserDeviceAccount(baseWrapper);
		baseWrapper.setBasePersistableModel(appUserModel);

		return baseWrapper;
	}
	
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
	
	public BaseWrapper getNationalDistributor()throws FrameworkCheckedException 
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setActive(true);
		distributorModel.setNational(true);
		

		CustomList list = this.distributorDAO.findByExample(distributorModel);
		if(list!=null && list.getResultsetList().size()>0)
		{
			distributorModel =(DistributorModel)list.getResultsetList().get(0);
			baseWrapper.setBasePersistableModel(distributorModel);
			
			return baseWrapper;
		}
		
		baseWrapper.setBasePersistableModel(null);
	
		return baseWrapper;
	}


	public BaseWrapper loadDistributorContactByAppUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		AppUserModel appUser = (AppUserModel) baseWrapper
				.getBasePersistableModel();
		appUser = this.appUserDAO.findByPrimaryKey(appUser.getAppUserId());

		if (appUser.getDistributorContactId() != null
				&& appUser.getDistributorContactId() > 0) {
			baseWrapper.setBasePersistableModel(this.distributorContactDAO
					.findByPrimaryKey(appUser.getDistributorContactId()));
			return baseWrapper;
		}
		baseWrapper.setBasePersistableModel(new DistributorContactModel());
		return baseWrapper;
	}

	public boolean isManagingContact(Long fromDistributor, Long toDistributor)
			throws FrameworkCheckedException {
		return this.distributorContactDAO.isManagingContact(fromDistributor,
				toDistributor);
	}

	public void setDistributorLevelDAO(DistributorLevelDAO distributorLevelDAO) {
		this.distributorLevelDAO = distributorLevelDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

}
