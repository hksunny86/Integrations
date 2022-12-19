package com.inov8.microbank.server.service.portal.usermanagementmodule;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.microbank.common.exception.EmailServiceSendFailureException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.AppUserPasswordHistoryModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.BankUserModel;
import com.inov8.microbank.common.model.MnoUserModel;
import com.inov8.microbank.common.model.OperatorUserModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.SupplierUserModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementListViewModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.EmailServiceConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.MobileTypeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.agenthierarchymodule.SalesHierarchyDAO;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.bankmodule.BankUserDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoUserDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorUserDAO;
import com.inov8.microbank.server.dao.portal.usermanagementmodule.UserManagementListViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.suppliermodule.SupplierUserDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class UserManagementManagerImpl implements UserManagementManager
{

	Log logger	= LogFactory.getLog(UserManagementManagerImpl.class);
	
	private UserManagementListViewDAO	userManagementListViewDAO;
	private AppUserDAO					appUserDAO;
	private BankUserDAO					bankUserDAO;
	private SupplierUserDAO				supplierUserDAO;
	private MnoUserDAO					mnoUserDAO;
	private OperatorUserDAO				operatorUserDAO;
	private ActionLogManager			actionLogManager;
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;
	private OracleSequenceGeneratorJdbcDAO sequenceGenerator;
	private String mobileNoPrefix;
	private JmsProducer jmsProducer;
	private SmsSender smsSender = null;
	private SalesHierarchyDAO salesHierarchyDAO;

	/**
	 * @return the mobileNoPrefix
	 */
	public String getMobileNoPrefix() {
		return mobileNoPrefix;
	}

	/**
	 * @param mobileNoPrefix the mobileNoPrefix to set
	 */
	public void setMobileNoPrefix(String mobileNoPrefix) {
		this.mobileNoPrefix = mobileNoPrefix;
	}

	/**
	 * @param sequenceGenerator the sequenceGenerator to set
	 */
	public void setSequenceGenerator(
			OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	/**
	 * This Method Creates the New User
	 */
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager#createNewUser(com.inov8.framework.common.wrapper.BaseWrapper)
	 */
	public BaseWrapper createNewUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		AppUserModel currentUser = UserUtils.getCurrentUser();

		UserManagementModel userManagementModel = (UserManagementModel) baseWrapper
				.getObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY);
		String serverURL = MessageUtil.getMessage("serverURL");//(String) baseWrapper.getObject("serverURL");

		AppUserModel appUserModel = new AppUserModel();
		Date nowDate = new Date();

		/**
		 * Checking the Password and Confirm Password are equal
		 */
		if (!userManagementModel.getPassword().trim().equals(userManagementModel.getConfirmPassword().trim()))
			throw new FrameworkCheckedException("PasswordConfirmPasswordMissMatchException");
		/**
		 * Checking if mobile Number is unique
		 */
		if (!this.isMobileNumUnique(userManagementModel.getMobileNo(),userManagementModel.getAppUserTypeId(),userManagementModel.getAppUserId())){
			throw new FrameworkCheckedException("MobileNumUniqueException");
		}
		/**
		 * Checking if employee ID is unique
		 */
		if (null!=userManagementModel.getEmployeeId() && !this.isEmployeeIdUnique(userManagementModel.getEmployeeId(),userManagementModel.getAppUserId())){
			throw new FrameworkCheckedException("Employee with same Employee ID already exist.");
		}
		
		

//		AppRoleModel appRoleModel = null;
		
		/**
		 * Saving a user of type bank
		 */
		if (UserTypeConstantsInterface.BANK.equals(userManagementModel.getAppUserTypeId()))
		{
			userManagementModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
			BankUserModel bankUserModel = new BankUserModel();
			bankUserModel.setBankId(userManagementModel.getPartnerId());
			bankUserModel.setCreatedByAppUserModel(currentUser);
			bankUserModel.setUpdatedByAppUserModel(currentUser);
			bankUserModel.setCreatedOn(nowDate);
			bankUserModel.setUpdatedOn(nowDate);
//			appRoleModel = new AppRoleModel();
//			appRoleModel.setAppRoleId(AppRoleConstantsInterface.PAYMENT_GATEWAY);
			bankUserModel = this.bankUserDAO.saveOrUpdate(bankUserModel);
			// entring data in appUserModel
			appUserModel.setBankUserIdBankUserModel(bankUserModel);

		}
		/**
		 * Saving a user of type operator like i8
		 */
		else if (UserTypeConstantsInterface.INOV8.equals(userManagementModel.getAppUserTypeId()))
		{
			userManagementModel.setAppUserTypeId(UserTypeConstantsInterface.INOV8);
			OperatorUserModel operatorUserModel = new OperatorUserModel();
			operatorUserModel.setOperatorId(userManagementModel.getPartnerId());
			operatorUserModel.setCreatedByAppUserModel(currentUser);
			operatorUserModel.setUpdatedByAppUserModel(currentUser);
			operatorUserModel.setCreatedOn(nowDate);
			operatorUserModel.setUpdatedOn(nowDate);

//			appRoleModel = new AppRoleModel();
//			appRoleModel.setAppRoleId(AppRoleConstantsInterface.CSR);

			operatorUserModel = this.operatorUserDAO.saveOrUpdate(operatorUserModel);
			// entring data in the appUser Model
			appUserModel.setOperatorUserIdOperatorUserModel(operatorUserModel);

		}
		/**
		 * Saving a user of type MNO
		 */
		else if (UserTypeConstantsInterface.MNO.equals(userManagementModel.getAppUserTypeId()))
		{
			userManagementModel.setAppUserTypeId(UserTypeConstantsInterface.MNO);
			MnoUserModel mnoUserModel = new MnoUserModel();
			mnoUserModel.setMnoId(userManagementModel.getPartnerId());
			mnoUserModel.setCreatedByAppUserModel(currentUser);
			mnoUserModel.setUpdatedByAppUserModel(currentUser);
			mnoUserModel.setCreatedOn(nowDate);
			mnoUserModel.setUpdatedOn(nowDate);
//			appRoleModel = new AppRoleModel();
//			appRoleModel.setAppRoleId(AppRoleConstantsInterface.MNO);

			mnoUserModel = this.mnoUserDAO.saveOrUpdate(mnoUserModel);
			// entring data in the appUser Model
			appUserModel.setMnoUserIdMnoUserModel(mnoUserModel);
			appUserModel.setMnoId(50028L);
		}
		/**
		 * Saving a user of type supplier
		 */
		else
		{
			SupplierUserModel supplierUserModel = new SupplierUserModel();
			supplierUserModel.setSupplierId(userManagementModel.getPartnerId());
			supplierUserModel.setCreatedByAppUserModel(currentUser);
			supplierUserModel.setUpdatedByAppUserModel(currentUser);
			supplierUserModel.setCreatedOn(nowDate);
			supplierUserModel.setUpdatedOn(nowDate);

			userManagementModel.setAppUserTypeId(UserTypeConstantsInterface.SUPPLIER);
	
			supplierUserModel = this.supplierUserDAO.saveOrUpdate(supplierUserModel);
			// entring data in the appUser Model
			appUserModel.setSupplierUserIdSupplierUserModel(supplierUserModel);
		}

		/**
		 * Saving the app user now
		 */

		if(StringUtil.isNullOrEmpty(userManagementModel.getMobileNo())) {

			String mobileNo = StringUtils.leftPad(String.valueOf(sequenceGenerator.nextLongValue()), 10, '0');
			userManagementModel.setMobileNo("U"+mobileNo);
			userManagementModel.setMobileTypeId(MobileTypeConstantsInterface.MOBILE_TYPE_ID_PRE_PAID);
		}

		appUserModel.setAppUserTypeId(userManagementModel.getAppUserTypeId());

		appUserModel.setFirstName(userManagementModel.getFirstName());
		appUserModel.setLastName(userManagementModel.getLastName());
		appUserModel.setMobileNo(userManagementModel.getMobileNo());
		appUserModel.setMobileTypeId(userManagementModel.getMobileTypeId());
		appUserModel.setEmail(userManagementModel.getEmail());
		appUserModel.setEmployeeId(userManagementModel.getEmployeeId());
		appUserModel.setUsername(userManagementModel.getUserId());
		appUserModel.setDob(userManagementModel.getDob());
		appUserModel.setPassword(EncoderUtils.encodeToSha(userManagementModel.getPassword()));

		appUserModel.setAccountEnabled(userManagementModel.getIsActive());
		appUserModel.setAccountExpired(false);
		appUserModel.setCredentialsExpired(false);
		appUserModel.setAccountLocked(false);
		appUserModel.setVerified(true);
		appUserModel.setPasswordChangeRequired(true);

		appUserModel.setCreatedByAppUserModel(currentUser);
		appUserModel.setUpdatedByAppUserModel(currentUser);
		appUserModel.setCreatedOn(nowDate);
		appUserModel.setUpdatedOn(nowDate);
		try
		{
			appUserModel=this.appUserDAO.saveOrUpdate(appUserModel);
			//now save password history
				this.saveAppUserPasswordHistory(appUserModel);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}else{
				throw new FrameworkCheckedException(e.getLocalizedMessage());
			}
	  }
		
		//-- save user group
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    appUserPartnerGroupModel.setPartnerGroupId(userManagementModel.getPartnerGroupId());
	    appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setCreatedOn(new Date());
	    appUserPartnerGroupModel.setUpdatedOn(new Date());
	    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		
	    /*
	     * Send sms to user,if he/she has provided mobile number
	     */
	    if(appUserModel.getMobileNo()!=null && !appUserModel.getMobileNo().trim().equals("") && !appUserModel.getMobileNo().startsWith("U"))
	    {
	    	String messageString="\nYour user has been created.\n\nUser Name: "+appUserModel.getUsername()+"\nPassword: "+userManagementModel.getPassword()
	    			+"\nVisit "+serverURL+"\nto change password\n*Password Policy: at least 8 Alphanumeric and 1 special character";
			SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);
			smsSender.send(smsMessage);
	    }
	    
	    /*
	     * Send email to user,if he/she has provided email
	     */
  		if(appUserModel.getEmail() != null  && !appUserModel.getEmail().trim().equals(""))
  		{
  	  		EmailMessage emailMessage = new EmailMessage();

  	  		emailMessage.setRecepients(appUserModel.getEmail().split(";"));
  	  		emailMessage.setSubject(EmailServiceConstants.SUBJECT);
  	  		
  	  		String messageString="\nYour user has been created.\n\nUser Name: "+appUserModel.getUsername()+"\nPassword: "+userManagementModel.getPassword()
    			+"\n\nVisit "+serverURL+" to change password\n*Password Policy: at least 8 Alphanumeric and 1 special character";
  	  	
  	  		emailMessage.setText(messageString);
  	  		
  	  		try{
  	  			jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
  	  		}catch(EmailServiceSendFailureException esx){
  	  			throw new FrameworkCheckedException(esx.getMessage());
  	  		}
  		}
		actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
		actionLogModel.setCustomField11(userManagementModel.getUserId());
		this.actionLogManager.completeActionLog(actionLogModel);

		return baseWrapper;
	}

	/**
	 * Method checks if mobile number is Unique, if 
	 * unique returns true , else return false
	 */
	private boolean isMobileNumUnique(String mobileNo,Long appUserType,Long appUserId)
	{
		
		if(StringUtil.isNullOrEmpty(mobileNo)) {
			return Boolean.TRUE;
		}
		
		boolean returnValue = Boolean.TRUE;
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		CustomList<AppUserModel> customAppUserModelList =  this.appUserDAO.findByExample(appUserModel);
		if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
		{
			if(appUserId != null && appUserId > 0)
			{
				for(AppUserModel model:customAppUserModelList.getResultsetList())
				{
					if(!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId))
					{
						returnValue = Boolean.FALSE;
						break;
					}
				}
			}
			else
			{
				//returnValue = Boolean.FALSE;
				for(AppUserModel model:customAppUserModelList.getResultsetList())
				{
					if(!model.getAccountClosedSettled())
					{
						returnValue = Boolean.FALSE;
						break;
					}
				}
			}
		}
		
		if(returnValue)
		{
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customAppUserModelList =  this.appUserDAO.findByExample(appUserModel);
			if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
			{
				if(appUserId != null && appUserId > 0)
				{
					for(AppUserModel model:customAppUserModelList.getResultsetList())
					{
						if(!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId))
						{
							returnValue = Boolean.FALSE;
							break;
						}
					}
				}
				else
				{
					//returnValue = Boolean.FALSE;
					for(AppUserModel model:customAppUserModelList.getResultsetList())
					{
						if(!model.getAccountClosedSettled())
						{
							returnValue = Boolean.FALSE;
							break;
						}
					}
				}
			}	
		}
		//adding handler case as well
  		if(returnValue)
  		{
  			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.HANDLER);
  			customAppUserModelList =  this.appUserDAO.findByExample(appUserModel);
  			if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
  			{
  				if(appUserId != null && appUserId > 0)
  				{
  					for(AppUserModel model:customAppUserModelList.getResultsetList())
  					{
  						if(!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId) )
  						{
  							returnValue = Boolean.FALSE;
  							break;
  						}
  					}
  				}
  				else
  				{
  					//returnValue = Boolean.FALSE;
  					for(AppUserModel model:customAppUserModelList.getResultsetList())
  					{
  						if(!model.getAccountClosedSettled())
  						{
  							returnValue = Boolean.FALSE;
  							break;
  						}
  					}
  				}
  			}	
  		}

  		if(returnValue && appUserType != null)
  		{
  			appUserModel.setAppUserTypeId(appUserType);
  			customAppUserModelList =  this.appUserDAO.findByExample(appUserModel);
  			if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
  			{
  				if(appUserId != null && appUserId > 0)
  				{
  					for(AppUserModel model:customAppUserModelList.getResultsetList())
  					{
  						if(!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId) )
  						{
  							returnValue = Boolean.FALSE;
  							break;
  						}
  					}
  				}
  				else
  				{
  					//returnValue = Boolean.FALSE;
  					for(AppUserModel model:customAppUserModelList.getResultsetList())
  					{
  						if(!model.getAccountClosedSettled())
  						{
  							returnValue = Boolean.FALSE;
  							break;
  						}
  					}
  				}
  			}	
  		}

		return returnValue;

	}
	private boolean isEmployeeIdUnique(Long employeeId, Long appUserId)
	{
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setEmployeeId(employeeId);
		appUserModel.setAppUserId(appUserId);	
		return this.appUserDAO.isEmployeeIdUnique(appUserModel);	
	}
	private boolean isUserIdUnique(String userId, Long appUserId)
	{
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setUsername(userId);
		appUserModel.setAppUserId(appUserId);	
		return this.appUserDAO.isUserIdUnique(appUserModel);	
	}
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager#isTellerIdUnique(java.lang.String, java.lang.Long)
	 */
	public Boolean isTellerIdUnique(String tellerId, Long appUserId) {
		
		Boolean isTellerIdUnique = Boolean.TRUE;
		
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setTellerId(tellerId);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);

		CustomList<AppUserModel> customList = appUserDAO.findByExample(appUserModel, null);

		if(customList != null && customList.getResultsetList().size() > 1) {
			
			isTellerIdUnique = Boolean.FALSE;
		
		} else if(customList != null && customList.getResultsetList().size() == 1) {
			
			appUserModel = customList.getResultsetList().get(0);
			
			if(appUserModel != null && appUserModel.getAppUserId().longValue() != appUserId.longValue()) {

				isTellerIdUnique = Boolean.FALSE;
			}			
		}
		
		return isTellerIdUnique;	
	}
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager#isSalesHierarchyUser(java.lang.Long)
	 */
	public Boolean isSalesHierarchyUser(Long appUserId) {
		
		Boolean isSalesHierarchyUser = Boolean.FALSE;

		AppUserModel appUserModel = this.appUserDAO.findByPrimaryKey(appUserId);
		
		if(appUserModel != null) {
			
			SalesHierarchyModel salesHierarchyModel = new SalesHierarchyModel();
			salesHierarchyModel.setBankUserId(appUserModel.getBankUserId());
			
			CustomList<SalesHierarchyModel> customList = this.salesHierarchyDAO.findByExample(salesHierarchyModel, null);
			
			if(customList != null && customList.getResultsetList().size() > 0) {
				
				isSalesHierarchyUser = Boolean.TRUE;
			}
		}

		return isSalesHierarchyUser;	
	}	
	
	
	public SearchBaseWrapper searchUsers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		CustomList<UserManagementListViewModel> customList = this.userManagementListViewDAO.findByExample(
				(UserManagementListViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	/**
	 * This method updates the user information
	 */
	public BaseWrapper updateUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		AppUserModel currentUser = UserUtils.getCurrentUser();

//		ActionLogModel actionLogModel = new ActionLogModel();
//		actionLogModel.setActionId((Long) baseWrapper.getObject("actionId"));
//		actionLogModel.setUsecaseId((Long) baseWrapper.getObject("usecaseId"));
//		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
//		actionLogModel.setAppUserId(currentUser.getAppUserId());
//		actionLogModel.setUserName(currentUser.getUsername());
//		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
//		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
//		actionLogModel = logAction(actionLogModel);

		UserManagementModel userManagementModel = (UserManagementModel) baseWrapper
				.getObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY);
		
		Date nowDate = new Date();
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setAppUserId(userManagementModel.getAppUserId());
		baseWrapper.setBasePersistableModel(appUserModel);
		baseWrapper = this.searchAppUserByPrimaryKey(baseWrapper);
		appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

		/**
		 * Here checking that if the user has changed the password, then checking the
		 * confirm password fields match and finally setting the appUserModel.setPassword after
		 * encoding
		 */
		if (userManagementModel.getIsPasswordChanged())
		{
			if (!userManagementModel.getPassword().trim().equals(userManagementModel.getConfirmPassword().trim()))
				throw new FrameworkCheckedException("PasswordConfirmPasswordMissMatchException");
			appUserModel.setPassword(EncoderUtils.encodeToSha(userManagementModel.getPassword().trim()));
			appUserModel.setPasswordChangeRequired(true);
		}
		
		/**
		 * Checking if mobile Number is unique
		 */
		if (!StringUtil.isNullOrEmpty(userManagementModel.getMobileNo()) && 
				!userManagementModel.getMobileNo().trim().equals(appUserModel.getMobileNo().trim())) {
			
			if (!this.isMobileNumUnique(userManagementModel.getMobileNo(),userManagementModel.getAppUserTypeId(),userManagementModel.getAppUserId())){
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
		} else if(StringUtil.isNullOrEmpty(userManagementModel.getMobileNo()) && appUserModel.getMobileNo().startsWith("U")) {
			userManagementModel.setMobileNo(appUserModel.getMobileNo());
		} else if(StringUtil.isNullOrEmpty(userManagementModel.getMobileNo()) && !appUserModel.getMobileNo().startsWith("U")) {

			String mobileNo = StringUtils.leftPad(String.valueOf(sequenceGenerator.nextLongValue()), 10, '0');
			userManagementModel.setMobileNo("U"+mobileNo);
		}
		
		/**
		 * Checking if employee ID is unique
		 */
		if (null!=userManagementModel.getEmployeeId() && !this.isEmployeeIdUnique(userManagementModel.getEmployeeId(),userManagementModel.getAppUserId())){
			throw new FrameworkCheckedException("Employee with same Employee ID already exist.");
		}

		appUserModel.setMobileNo(userManagementModel.getMobileNo());
		appUserModel.setMobileTypeId(userManagementModel.getMobileTypeId());
		appUserModel.setFirstName(userManagementModel.getFirstName());
		appUserModel.setLastName(userManagementModel.getLastName());
		appUserModel.setEmployeeId(userManagementModel.getEmployeeId());
		appUserModel.setAccountEnabled(userManagementModel.getIsActive());
		appUserModel.setDob(userManagementModel.getDob());
		appUserModel.setEmail(userManagementModel.getEmail());
		
		
			    

		
		/*AppUserAppRoleModel appUserAppRoleModel = UserUtils.determineCurrentAppUserAppRoleModel(appUserModel);
		AccessLevelModel aLevelModel = new AccessLevelModel();
		aLevelModel.setAccessLevelId(userManagementModel.getAccessLevelId());

		appUserAppRoleModel.setAccessLevelModel(aLevelModel);
		this.appUserAppRoleDAO.saveOrUpdate(appUserAppRoleModel);*/
		
		appUserModel.setUpdatedByAppUserModel(currentUser);
		appUserModel.setUpdatedOn(nowDate);

		try
		{
			this.appUserDAO.saveOrUpdate(appUserModel);
			//now save password history
			if(userManagementModel.getIsPasswordChanged()){
				this.saveAppUserPasswordHistory(appUserModel);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}else{
				throw new FrameworkCheckedException(e.getLocalizedMessage());
			}
	  }
		
		//-- save user group
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    CustomList list=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
	    if(list.getResultsetList().size()>0)
	    {
	    	
	    	appUserPartnerGroupModel=(AppUserPartnerGroupModel)list.getResultsetList().get(0);
	    	appUserPartnerGroupModel.setPartnerGroupId(userManagementModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }
		
		
		if(appUserModel.getAppUserId().longValue() == UserUtils.getCurrentUser().getAppUserId().longValue()){
			UserUtils.getCurrentUser().setFirstName(appUserModel.getFirstName());
			UserUtils.getCurrentUser().setLastName(appUserModel.getLastName());
			UserUtils.getCurrentUser().setEmail(appUserModel.getEmail());
			UserUtils.getCurrentUser().setAccountEnabled(appUserModel.getAccountEnabled());
			UserUtils.getCurrentUser().setDob(appUserModel.getDob());
		}
		
//		/**
//		 * Logging the information process ends
//		 */
//		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
//		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
//		actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
//		actionLogModel = logAction(actionLogModel);
	    actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
	    actionLogModel.setCustomField11(userManagementModel.getUserId());
		this.actionLogManager.completeActionLog(actionLogModel);

		return baseWrapper;
	}

	/**
	 * This Method Creates the New User
	 */
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager#createNewUser(com.inov8.framework.common.wrapper.BaseWrapper)
	 */
	public BaseWrapper createNewUserByAdmin(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		AppUserModel currentUser = UserUtils.getCurrentUser();

		UserManagementModel userManagementModel = (UserManagementModel) baseWrapper
				.getObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY);

		if(userManagementModel.getEmail() == null || userManagementModel.getEmail().trim().length() < 2){
			throw new FrameworkCheckedException("Email Empty Exception");
		}
		
		AppUserModel tmpappUserModel = new AppUserModel();  
		tmpappUserModel.setUsername(userManagementModel.getUserId());
		CustomList<AppUserModel> clist = appUserDAO.findByExample(tmpappUserModel);
		List<AppUserModel> list = clist.getResultsetList();
		if(!list.isEmpty()){
			throw new FrameworkCheckedException(PortalConstants.CONSTRAINT_VIOLATION_EXCEPTION);
		}
		
		
		AppUserModel appUserModel = new AppUserModel();
		Date nowDate = new Date();

		//AppRoleModel appRoleModel = null;

		userManagementModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
		BankUserModel bankUserModel = new BankUserModel();
		bankUserModel.setBankId(currentUser.getBankUserIdBankUserModel().getBankId());
		bankUserModel.setCreatedByAppUserModel(currentUser);
		bankUserModel.setUpdatedByAppUserModel(currentUser);
		bankUserModel.setCreatedOn(nowDate);
		bankUserModel.setUpdatedOn(nowDate);
		//appRoleModel = new AppRoleModel();
		//appRoleModel.setAppRoleId(AppRoleConstantsInterface.PAYMENT_GATEWAY);
		bankUserModel = this.bankUserDAO.saveOrUpdate(bankUserModel);
		// entring data in appUserModel
		appUserModel.setBankUserIdBankUserModel(bankUserModel);

		appUserModel.setAppUserTypeId(userManagementModel.getAppUserTypeId());

		appUserModel.setFirstName(userManagementModel.getFirstName());
		appUserModel.setLastName(userManagementModel.getLastName());
		appUserModel.setMobileNo(mobileNoPrefix+sequenceGenerator.nextLongValue());
		appUserModel.setMobileTypeId(MobileTypeConstantsInterface.MOBILE_TYPE_ID_PRE_PAID);
		appUserModel.setEmail(userManagementModel.getEmail());
		appUserModel.setUsername(userManagementModel.getUserId());
		String userPassword = CommonUtils.generateNumber(9);
		
		appUserModel.setPassword(EncoderUtils.encodeToSha(userPassword));
		
		appUserModel.setAccountEnabled(userManagementModel.getIsActive());
		appUserModel.setAccountExpired(false);
		appUserModel.setCredentialsExpired(false);
		appUserModel.setAccountLocked(false);
		appUserModel.setVerified(true);
		appUserModel.setPasswordChangeRequired(true);

		appUserModel.setCreatedByAppUserModel(currentUser);
		appUserModel.setUpdatedByAppUserModel(currentUser);
		appUserModel.setCreatedOn(nowDate);
		appUserModel.setUpdatedOn(nowDate);

		appUserModel=this.appUserDAO.saveOrUpdate(appUserModel);

		AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
		PartnerGroupModel partnerGroupModel =new PartnerGroupModel();
		partnerGroupModel.setPartnerGroupId(userManagementModel.getPartnerGroupId());
		appUserPartnerGroupModel.setPartnerGroupIdPartnerGroupModel(partnerGroupModel);
		appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setCreatedOn(new Date());
	    appUserPartnerGroupModel.setUpdatedOn(new Date());
	    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		
		/*AccessLevelModel aLevelModel = new AccessLevelModel();
		aLevelModel.setAccessLevelId(AccessLevelConstants.ACCESS_LEVEL_NORMAL_USER);
		AppUserAppRoleModel aUserAppRoleModel = new AppUserAppRoleModel(aLevelModel, appRoleModel, appUserModel);
		this.appUserAppRoleDAO.saveOrUpdate(aUserAppRoleModel);*/
		
		//send password through email
		EmailMessage emailMessage = new EmailMessage();
		if(appUserModel.getEmail() != null){
			emailMessage.setRecepients(appUserModel.getEmail().split(";"));
		}
		emailMessage.setSubject(EmailServiceConstants.SUBJECT);
		emailMessage.setText("\nYour Agent Web Application account has been created.\n\nUser Name: "+appUserModel.getUsername()+"\nPassword: "+userPassword);
		
		try{
			jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
		}catch(EmailServiceSendFailureException esx){
			throw new FrameworkCheckedException(esx.getMessage());
		}
				
	    baseWrapper.setBasePersistableModel(tmpappUserModel);
		
	    actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
		this.actionLogManager.completeActionLog(actionLogModel);

		return baseWrapper;
	}	
	
/*	public BaseWrapper changePasswordByAdmin(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		Long appUserId = new Long(baseWrapper.getObject("appUserId").toString());
		String mfsId = new String(baseWrapper.getObject("mfsId").toString());
		
		AppUserModel appUserModel = appUserDAO.findByPrimaryKey(appUserId);
		

		if(appUserModel.getEmail() == null){
			baseWrapper.putObject("errorMessage", "Please update your email to reset your password");
			//throw new FrameworkCheckedException("Email Empty Exception");
		}

		
		String password = CommonUtils.generateNumber(9);
		appUserModel.setPassword(EncoderUtils.encodeToSha(password));
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setUpdatedOn(new Date());
		
		//set credentials expired to false also
		appUserModel.setCredentialsExpired(false);
		//set password change required flag to true also
		appUserModel.setPasswordChangeRequired(true);
		
		
		this.appUserDAO.saveOrUpdate(appUserModel);		
		
		//send password through email
		EmailMessage emailMessage = new EmailMessage();
		
		if(appUserModel.getEmail() != null){
			emailMessage.setRecepients(appUserModel.getEmail().split(";"));
			emailMessage.setText("\nYour Branchless Banking Portal Password has been reset.\n\nPassword:"+password+"\n\n\nTo change your password please go to: http://uat.inov.com.pk");
			emailMessage.setSubject(EmailServiceConstants.SUBJECT);
			
			try{
				jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
			}catch(EmailServiceSendFailureException esx){
				baseWrapper.putObject("errorMessage", "Please update Email Address in User Info to reset Password");
				throw new FrameworkCheckedException(esx.getMessage());
			}
		}else{
			logger.info("Email not sent to "+appUserModel.getUsername()+" on reset password because email is null.");
		}
		
		
		actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
		actionLogModel.setCustomField11(mfsId);
		this.actionLogManager.completeActionLog(actionLogModel);
		return baseWrapper;
	}	
	*/
	/**
	 * This method is used to reset Agent password over SMS
	 *//*
	public BaseWrapper changePasswordBySMS(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		Long appUserId = new Long(baseWrapper.getObject("appUserId").toString());
		String mfsId = new String(baseWrapper.getObject("mfsId").toString());
		AppUserModel appUserModel = appUserDAO.findByPrimaryKey(appUserId);
		
		String password = CommonUtils.generateNumber(9);

		appUserModel.setPassword(EncoderUtils.encodeToSha(password));
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setUpdatedOn(new Date());
		appUserModel.setCredentialsExpired(false);
		appUserModel.setPasswordChangeRequired(true);
		this.appUserDAO.saveOrUpdate(appUserModel);		
		
		String messageString = null;
		String mobileno = appUserModel.getMobileNo();
		Object[] args = { password };
		
		if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
			
			messageString = MessageUtil.getMessage("forgotpin.portal.allpaypasswordchange.success", args);
			
		} else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {

			messageString = MessageUtil.getMessage("forgotpin.portal.allpaypasswordchange.success.handler", args);
		}
		
		SmsMessage smsMessage = new SmsMessage(mobileno, messageString);
		smsSender.send(smsMessage);
		
		actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
		actionLogModel.setCustomField11(mfsId);
		this.actionLogManager.completeActionLog(actionLogModel);
		
		return baseWrapper;
	}	*/
	
	/**
	 * This method updates the user information
	 */
	public BaseWrapper updateUserByAdmin(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		AppUserModel currentUser = UserUtils.getCurrentUser();

		UserManagementModel userManagementModel = (UserManagementModel) baseWrapper
				.getObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY);
		Date nowDate = new Date();
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setAppUserId(userManagementModel.getAppUserId());
		baseWrapper.setBasePersistableModel(appUserModel);
		baseWrapper = this.searchAppUserByPrimaryKey(baseWrapper);
		appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

		appUserModel.setFirstName(userManagementModel.getFirstName());
		appUserModel.setLastName(userManagementModel.getLastName());
		appUserModel.setAccountEnabled(userManagementModel.getIsActive());
		appUserModel.setEmail(userManagementModel.getEmail());
	
		appUserModel.setUpdatedByAppUserModel(currentUser);
		appUserModel.setUpdatedOn(nowDate);

		this.appUserDAO.saveOrUpdate(appUserModel);
		
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    CustomList list=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
	    if(list.getResultsetList().size()>0)
	    {
	    	
	    	appUserPartnerGroupModel=(AppUserPartnerGroupModel)list.getResultsetList().get(0);
	    	appUserPartnerGroupModel.setPartnerGroupId(userManagementModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }

		
		if(appUserModel.getAppUserId().longValue() == UserUtils.getCurrentUser().getAppUserId().longValue()){
			UserUtils.getCurrentUser().setFirstName(appUserModel.getFirstName());
			UserUtils.getCurrentUser().setLastName(appUserModel.getLastName());
			UserUtils.getCurrentUser().setEmail(appUserModel.getEmail());
			UserUtils.getCurrentUser().setAccountEnabled(appUserModel.getAccountEnabled());
			
		}
		
		/**
		 * Logging the information process ends
		 */
		actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
		this.actionLogManager.completeActionLog(actionLogModel);

		return baseWrapper;
	}
	
	
	/**
	 * Method search the App_User on the basis of the primary key
	 */
	public BaseWrapper searchAppUserByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
		appUserModel = this.appUserDAO.findByPrimaryKey(appUserModel.getAppUserId());
		baseWrapper.setBasePersistableModel(appUserModel);
		return baseWrapper;
	}

	/**
	 * Method Logs the action performed in the action log table
	 */
	private ActionLogModel logAction(ActionLogModel actionLogModel) throws FrameworkCheckedException
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if (null == actionLogModel.getActionLogId())
		{
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		}
		else
		{
			baseWrapper = this.actionLogManager.createOrUpdateActionLog(baseWrapper);
		}
		return (ActionLogModel) baseWrapper.getBasePersistableModel();
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
	@Override
	public void validateUser(UserManagementModel userManagementModel) throws FrameworkCheckedException
	{
		
		String errorMsg ="";
		boolean passwordMismatch = false;
		boolean mobileNumberNotUnique = false;
		boolean employeeIdNotUnique = false;
		boolean userIdNotUnique = false;
		boolean isTellerIdUnique = true;
		Boolean isSalesHierarchyUser = Boolean.FALSE;
		
		/**
		 * Checking the Password and Confirm Password are equal
		 */
		if (!userManagementModel.getPassword().trim().equals(userManagementModel.getConfirmPassword().trim()))
			passwordMismatch = true;	
		/**
		 * Checking if mobile Number is unique
		 */
		if (!this.isMobileNumUnique(userManagementModel.getMobileNo(),userManagementModel.getAppUserTypeId(),userManagementModel.getAppUserId())){
			mobileNumberNotUnique = true;
		}
		/**
		 * Checking if employee ID is unique
		 */
		if (null!=userManagementModel.getEmployeeId() && !this.isEmployeeIdUnique(userManagementModel.getEmployeeId(),userManagementModel.getAppUserId())){
			employeeIdNotUnique = true;
		}
		
		/**
		 * Checking if User ID is unique
		 */
		if (null!=userManagementModel.getUserId() && !this.isUserIdUnique(userManagementModel.getUserId(),userManagementModel.getAppUserId())){
			userIdNotUnique = true;
		}
		
		if (!StringUtil.isNullOrEmpty(userManagementModel.getTellerId())){
			isTellerIdUnique = this.isTellerIdUnique(userManagementModel.getTellerId(),userManagementModel.getAppUserId());
		}
		
		if (!StringUtil.isNullOrEmpty(userManagementModel.getTellerId()) && userManagementModel.getAppUserId() != null && userManagementModel.getAppUserId() > 0 &&
				(userManagementModel.getAppUserType().equalsIgnoreCase("BANK"))) {
				
			isSalesHierarchyUser = this.isSalesHierarchyUser(userManagementModel.getAppUserId());
		}		
		
		if(passwordMismatch && (!mobileNumberNotUnique) && (!employeeIdNotUnique) && (!userIdNotUnique))
			errorMsg = MessageUtil.getMessage("userManagementModule.confirmPasswordMissMatch")+"<br>";
		else if ((!passwordMismatch) && (mobileNumberNotUnique) && (!employeeIdNotUnique) && (!userIdNotUnique))
			errorMsg =MessageUtil.getMessage("userManagementModule.mobileNumNotUnique")+"<br>";
		else if ((!passwordMismatch) && (!mobileNumberNotUnique) && (employeeIdNotUnique) && (!userIdNotUnique))
			errorMsg =MessageUtil.getMessage("userManagementModule.employeeIdUnique")+"<br>";
		else if ((!passwordMismatch) && (!mobileNumberNotUnique) && (!employeeIdNotUnique) && (userIdNotUnique))
			errorMsg =MessageUtil.getMessage("userManagementModule.userIdUnique")+"<br>";
		else if ((!isTellerIdUnique))
			errorMsg =MessageUtil.getMessage("userManagementModule.tellerIdUnique")+"<br>";
		else if ((isSalesHierarchyUser))
			errorMsg =MessageUtil.getMessage("userManagementModule.isSalesHierarchyUser")+"<br>";
		else if ((mobileNumberNotUnique) && (employeeIdNotUnique) && (!userIdNotUnique))
			errorMsg ="Another user account with same mobile number and employee ID exists. Please enter some other mobile number and employee ID.";
		else if ((mobileNumberNotUnique) && (!employeeIdNotUnique) && (userIdNotUnique))
			errorMsg ="Another user account with same mobile number and user ID exists. Please enter some other mobile number and user ID.";
		else if ((!mobileNumberNotUnique) && (employeeIdNotUnique) && (userIdNotUnique))
			errorMsg ="Another user account with same employee ID and user ID exists. Please enter some other employee ID and user ID.";
		else if ((mobileNumberNotUnique) && (employeeIdNotUnique) && (userIdNotUnique))
			errorMsg ="Another user account with same mobile number, employee ID and user ID exists.<br> Please enter some other mobile number, employee ID and user ID.";
		
		/*
		   * commented by atif
		   * if(null!=errorMsg && !errorMsg.isEmpty()){
		   throw new FrameworkCheckedException(errorMsg);
		  }*/
		  
		  if(passwordMismatch || mobileNumberNotUnique || employeeIdNotUnique || userIdNotUnique || !isTellerIdUnique || isSalesHierarchyUser)
		  {
		   throw new FrameworkCheckedException(errorMsg);
		  }
	}

	@Override
	public UserManagementModel getUserManagementModel(Long appUserId) throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		UserManagementModel userManagementModel = new UserManagementModel();
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setAppUserId(appUserId);
		baseWrapper.setBasePersistableModel(appUserModel);
		baseWrapper = this.searchAppUserByPrimaryKey(baseWrapper);
		appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

		userManagementModel.setAppUserId(appUserModel.getAppUserId());
		userManagementModel.setEmail(appUserModel.getEmail());
		userManagementModel.setFirstName(appUserModel.getFirstName());
		userManagementModel.setLastName(appUserModel.getLastName());
		userManagementModel.setEmployeeId(appUserModel.getEmployeeId());
		userManagementModel.setDob(appUserModel.getDob());
		userManagementModel.setIsActive(appUserModel.getAccountEnabled());
		userManagementModel.setMobileNo(appUserModel.getMobileNo());
		userManagementModel.setMobileTypeId(appUserModel.getMobileTypeId());
		userManagementModel.setUserId(appUserModel.getUsername());
		userManagementModel.setIsPasswordChanged(false);
		userManagementModel.setPassword(appUserModel.getPassword());
		userManagementModel.setConfirmPassword(appUserModel.getPassword());
		userManagementModel.setActionId(PortalConstants.ACTION_UPDATE);
		userManagementModel.setUsecaseId(PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID);
		
		baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(appUserModel);
		baseWrapper = this.searchUserGroup(baseWrapper);
		AppUserPartnerGroupModel appUserPartnerGroupModel = (AppUserPartnerGroupModel)baseWrapper.getBasePersistableModel();
		userManagementModel.setPartnerGroupId(appUserPartnerGroupModel.getPartnerGroupId());
		userManagementModel.setPartnerGroup(appUserPartnerGroupModel.getPartnerGroupIdPartnerGroupModel().getName());
		
		userManagementModel.setAppUserTypeId(appUserModel.getAppUserTypeId());
		userManagementModel.setAppUserType(appUserModel.getAppUserTypeIdAppUserTypeModel().getName());
		
		if (UserTypeConstantsInterface.BANK.equals(userManagementModel.getAppUserTypeId())){
			userManagementModel.setPartnerId(appUserModel.getBankUserIdBankUserModel().getBankId());
			userManagementModel.setPartnerType(appUserModel.getBankUserIdBankUserModel().getBankIdBankModel().getName());
		}
		else if (UserTypeConstantsInterface.MNO.equals(userManagementModel.getAppUserTypeId())){
			userManagementModel.setPartnerId(appUserModel.getMnoUserIdMnoUserModel().getMnoId());
			userManagementModel.setPartnerType(appUserModel.getMnoUserIdMnoUserModel().getMnoIdMnoModel().getName());
		}
		else if (UserTypeConstantsInterface.INOV8.equals(userManagementModel.getAppUserTypeId())){
			userManagementModel.setPartnerId(appUserModel.getOperatorUserIdOperatorUserModel().getOperatorId());
			userManagementModel.setPartnerType(appUserModel.getOperatorUserIdOperatorUserModel().getOperatorIdOperatorModel().getName());		
		}	
		else if (UserTypeConstantsInterface.SUPPLIER.equals(userManagementModel.getAppUserTypeId())){
			userManagementModel.setPartnerId(appUserModel.getSupplierUserIdSupplierUserModel().getSupplierId());
			userManagementModel.setPartnerType(appUserModel.getSupplierUserIdSupplierUserModel().getSupplierIdSupplierModel().getName());		
		}
		return userManagementModel;
	}

	@Override
	public BaseWrapper changePasswordBySMSEmail(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		Long appUserId = new Long(baseWrapper.getObject("appUserId").toString());
//		String serverURL = new String (baseWrapper.getObject("serverURL").toString());
		AppUserModel appUserModel = appUserDAO.findByPrimaryKey(appUserId);
		
		String password = CommonUtils.generateNumber(9);

		appUserModel.setPassword(EncoderUtils.encodeToSha(password));
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setUpdatedOn(new Date());
		appUserModel.setCredentialsExpired(false);
		appUserModel.setPasswordChangeRequired(true);
		this.appUserDAO.saveOrUpdate(appUserModel);		
		
		Object[] args = { password , MessageUtil.getMessage("serverURL")};
		String message = MessageUtil.getMessage("password.reset.mobile.and.policy.text", args);
		boolean smsFlag = false;
		boolean emailFlag = false;
		
		logger.info("Sending reset Password to mobile no:"+appUserModel.getMobileNo()+" , email:"+appUserModel.getEmail());
		if(!StringUtil.isNullOrEmpty(appUserModel.getMobileNo()) && !appUserModel.getMobileNo().startsWith("U")) {
			smsFlag = true;
			logger.info("Sending new Password via SMS to User mobile : " + appUserModel.getMobileNo());
			SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), message);
			smsSender.send(smsMessage);
		}
		
		if(!StringUtil.isNullOrEmpty(appUserModel.getEmail())) {
			emailFlag = true;
			logger.info("Sending new Password via Email to email-address : " + appUserModel.getEmail());
			try {
				message = MessageUtil.getMessage("password.reset.email.and.policy.text", args);
				EmailMessage emailMessage = new EmailMessage();
				emailMessage.setRecepients(appUserModel.getEmail().split(";"));
				emailMessage.setText(message);
				emailMessage.setSubject(EmailServiceConstants.SUBJECT);
				jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
			} catch(EmailServiceSendFailureException esx){
				if(!smsFlag){
					baseWrapper.putObject("errorMessage", "Please update Email Address in User Info to reset Password");
					throw new FrameworkCheckedException(esx.getMessage());
				}else{
					logger.error("Sending new Password via email FAILED. Exception:"+esx.getMessage(),esx);
				}
			}
		}
		
		if(!smsFlag && !emailFlag){
			logger.error("Email and SMS not sent on reset password. Because both are empty");
			throw new FrameworkCheckedException("Please update Email Address/Mobile No in User Info to reset Password");
		}
		
		actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
		actionLogModel.setCustomField11(appUserModel.getUsername());
		this.actionLogManager.completeActionLog(actionLogModel);

		return baseWrapper;
	}
	
	@Override
	public BaseWrapper changeAgentPasswordBySMSEmail(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		Long appUserId = new Long(baseWrapper.getObject("appUserId").toString());
		String mfsId = new String(baseWrapper.getObject("mfsId").toString());
		AppUserModel appUserModel = appUserDAO.findByPrimaryKey(appUserId);
		
		String password = CommonUtils.generateNumber(9);
		
		appUserModel.setPassword(EncoderUtils.encodeToSha(password));
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setUpdatedOn(new Date());
		appUserModel.setCredentialsExpired(false);
		appUserModel.setPasswordChangeRequired(true);
		this.appUserDAO.saveOrUpdate(appUserModel);		
		
		Object[] args={password, MessageUtil.getMessage("serverURL")};
		
		String message=MessageUtil.getMessage("password.reset.mobile.and.policy.text", args);

		SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), message);
		smsSender.send(smsMessage);
		
		EmailMessage emailMessage = new EmailMessage();
		
		if(appUserModel.getEmail() != null){
			emailMessage.setRecepients(appUserModel.getEmail().split(";"));
		
			message=MessageUtil.getMessage("password.reset.email.and.policy.text", args);
			
			emailMessage.setText(message);
			emailMessage.setSubject(EmailServiceConstants.SUBJECT);
			
			try{
				jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
			}catch(EmailServiceSendFailureException esx){
				baseWrapper.putObject("errorMessage", "Please update Email Address in User Info to reset Password");
				throw new FrameworkCheckedException(esx.getMessage());
			}
		}
		else{
			logger.info("Email not sent on reset password. Because recepient was null");
		}
		
		actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
		actionLogModel.setCustomField11(mfsId);
		this.actionLogManager.completeActionLog(actionLogModel);
		
		return baseWrapper;
	}
	
	public void setActionLogManager(ActionLogManager actionLogManager)
	{
		this.actionLogManager = actionLogManager;
	}

	public void setUserManagementListViewDAO(UserManagementListViewDAO userManagementListViewDAO)
	{
		this.userManagementListViewDAO = userManagementListViewDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO)
	{
		this.appUserDAO = appUserDAO;
	}

	public void setBankUserDAO(BankUserDAO bankUserDAO)
	{
		this.bankUserDAO = bankUserDAO;
	}

	public void setMnoUserDAO(MnoUserDAO mnoUserDAO)
	{
		this.mnoUserDAO = mnoUserDAO;
	}

	public void setOperatorUserDAO(OperatorUserDAO operatorUserDAO)
	{
		this.operatorUserDAO = operatorUserDAO;
	}

	public void setSupplierUserDAO(SupplierUserDAO supplierUserDAO)
	{
		this.supplierUserDAO = supplierUserDAO;
	}

	/**
	 * @param jmsProducer the jmsProducer to set
	 */
	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public BaseWrapper searchUserGroup(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
		AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
		CustomList<AppUserPartnerGroupModel> list = this.appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		baseWrapper.setBasePersistableModel(list.getResultsetList().get(0));
		return baseWrapper;
	}
	
	@Override
	public void saveAppUserPasswordHistory(AppUserModel appUserModel) throws FrameworkCheckedException{
			AppUserPasswordHistoryModel passwordHistory = new AppUserPasswordHistoryModel();
	        passwordHistory.setAppUserIdAppUserModel(appUserModel);
	        passwordHistory.setCreatedByAppUserModel(UserUtils.getCurrentUser());
	        passwordHistory.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	        passwordHistory.setCreatedOn(new Date());
	        passwordHistory.setUpdatedOn(new Date());
	        passwordHistory.setPassword(appUserModel.getPassword());
	        appUserDAO.saveAppUserPasswordHistory(passwordHistory);
	}
	
	@Override
	public boolean isAppUserInPartnerGroup(Long appUserId, Long partnerGroupId)
			throws FrameworkCheckedException {
		
		AppUserPartnerGroupModel appUserPartnerGroupModel = new	AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserId);
		appUserPartnerGroupModel.setPartnerGroupId(partnerGroupId);
		
		CustomList list = this.appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		if(list!=null && list.getResultsetList().size()>0)
		{
			return true;
		}
		else
		{
			return false;	
		}
	}
	
	@Override
	public AppUserModel getAppUserByRetailer(Long retailerContactId) throws FrameworkCheckedException 
	{
	  return this.appUserDAO.findByRetailerContractId(retailerContactId);
	}

	public void setSalesHierarchyDAO(SalesHierarchyDAO salesHierarchyDAO) {
		this.salesHierarchyDAO = salesHierarchyDAO;
	}
}
