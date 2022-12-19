package com.inov8.microbank.server.service.customermodule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;

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
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.CustomerRemitterModel;
import com.inov8.microbank.common.model.GoldenNosModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.customermodule.CustomerListViewModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerListViewDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerRemitterDAO;
import com.inov8.microbank.server.dao.customermodule.SegmentDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class CustomerManagerImpl implements CustomerManager {

	private CustomerListViewDAO customerListViewDAO;
	
	private CustomerDAO customerDAO;
	
	private SegmentDAO segmentDAO;

	private AppUserDAO appUserDAO;

	private UserDeviceAccountsDAO userDeviceAccountsDAO;

	private SmsSender smsSender;

	private GoldenNosDAO goldenNosDAO;
	
	private OracleSequenceGeneratorJdbcDAO sequenceGenerator;

	private AppUserManager appUserManager;

	private ActionLogManager actionLogManager;
	
	private CustomerRemitterDAO customerRemitterDAO;

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public void setCustomerListViewDAO(CustomerListViewDAO customerListViewDAO) {
		this.customerListViewDAO = customerListViewDAO;
	}
	
	public BaseWrapper createAppUserForCustomer(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		AppUserModel appUserModel = new AppUserModel();

		CustomerListViewModel customerListViewModel = (CustomerListViewModel) baseWrapper
				.getBasePersistableModel();

		// Checking if mobile Number is unique

		/*if (!this.isMobileNumUnique(customerListViewModel.getMobileNo())){
			customerListViewModel.setCustomerId(null);
			throw new FrameworkCheckedException("MobileNumUniqueException");
		}
		*/

		if (!this.isUserNameUnique(customerListViewModel.getUsername())){
			customerListViewModel.setCustomerId(null);
			throw new FrameworkCheckedException("UserNameUniqueException");
		}			

		Date nowDate = new Date();

		// Populating the Customer Model

		// Populating the AppUserModel Model
		Long id;
		id = customerListViewModel.getCustomerId();
		appUserModel.setFirstName(customerListViewModel.getFirstName());
		appUserModel.setLastName(customerListViewModel.getLastName());
		appUserModel.setAddress1(customerListViewModel.getAddress1());
		appUserModel.setAddress2(customerListViewModel.getAddress2());
		appUserModel.setPasswordChangeRequired(false);

		appUserModel.setMobileNo(customerListViewModel.getMobileNo());
		appUserModel.setNic(customerListViewModel.getNic());
		appUserModel.setCustomerId(id);
		appUserModel.setCity(customerListViewModel.getCity());
		appUserModel.setCountry(customerListViewModel.getCountry());
		appUserModel.setState(customerListViewModel.getState());
		appUserModel.setDob(customerListViewModel.getDob());
		appUserModel.setMobileTypeId(customerListViewModel.getMobileTypeId());
		appUserModel.setUsername(customerListViewModel.getUsername());
		appUserModel.setPassword(EncoderUtils.encodeToSha(customerListViewModel
				.getPassword()));
		appUserModel.setPasswordHint(customerListViewModel.getPasswordHint());

		appUserModel.setFax(customerListViewModel.getFax());
		appUserModel.setEmail(customerListViewModel.getEmail());
		appUserModel.setZip(customerListViewModel.getZip());
		appUserModel.setMotherMaidenName(customerListViewModel
				.getMotherMaidenName());
		appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setCreatedOn(nowDate);
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(nowDate);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

		appUserModel
				.setVerified(customerListViewModel.getVerified() == null ? false
						: customerListViewModel.getVerified());
		appUserModel.setAccountEnabled(customerListViewModel
				.getAccountEnabled() == null ? false : customerListViewModel
				.getAccountEnabled());
		appUserModel.setAccountExpired(customerListViewModel
				.getAccountExpired() == null ? false : customerListViewModel
				.getAccountExpired());
		appUserModel
				.setAccountLocked(customerListViewModel.getAccountLocked() == null ? false
						: customerListViewModel.getAccountLocked());
		appUserModel.setCredentialsExpired(customerListViewModel
				.getCredentialsExpired() == null ? false
				: customerListViewModel.getCredentialsExpired());
		appUserModel.setPasswordChangeRequired(false);
		

		// Saving the AppUserModel

		baseWrapper = new BaseWrapperImpl();
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
		
		//TODO: following code will be replaced with partner group for customers in future
		/*AppRoleModel appRoleModel = new AppRoleModel();
		appRoleModel.setAppRoleId(AppRoleConstantsInterface.CUSTOMER);
		AccessLevelModel aLevelModel = new AccessLevelModel();
		aLevelModel.setAccessLevelId(AccessLevelConstants.ACCESS_LEVEL_SUPER_USER);
		AppUserAppRoleModel aUserAppRoleModel = new AppUserAppRoleModel(aLevelModel, appRoleModel, appUserModel);
		this.appUserAppRoleDAO.saveOrUpdate(aUserAppRoleModel);
		 */
		
		// createUserDeviceAccount(customerListViewModel,appUserModel);

		return baseWrapper;
	}

	public BaseWrapper createCustomer(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		CustomerListViewModel customerListViewModel = (CustomerListViewModel) baseWrapper
				.getBasePersistableModel();

		CustomerModel customerModel = new CustomerModel();

		customerModel.setReferringName1(customerListViewModel
				.getReferringName1());
		customerModel
				.setReferringNic1(customerListViewModel.getReferringNic1());
		customerModel.setReferringName2(customerListViewModel
				.getReferringName2());
		customerModel
				.setReferringNic2(customerListViewModel.getReferringNic2());
		customerModel.setRegister(customerListViewModel.getRegister());
		customerModel.setCreatedOn(new Date());
		customerModel.setUpdatedOn(new Date());
		customerModel.setComments(customerListViewModel.getComments());
		customerModel.setDescription(customerListViewModel.getDescription());
		customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		customerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		customerModel = customerDAO.saveOrUpdate(customerModel);
		customerListViewModel.setCustomerId(customerModel.getCustomerId());
		baseWrapper.setBasePersistableModel(customerListViewModel);

		createAppUserForCustomer(baseWrapper);

		return baseWrapper;

	}

	public SearchBaseWrapper loadCustomerListView(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		CustomerListViewModel customerListViewModel = this.customerListViewDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(customerListViewModel);
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchCustomerListView(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		CustomList<CustomerListViewModel> list = this.customerListViewDAO
				.findByExample((CustomerListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper updateCustomer(BaseWrapper baseWrapper)	throws FrameworkCheckedException 
	{
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId((Long) baseWrapper
				.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId((Long) baseWrapper
				.getObject(PortalConstants.KEY_USECASE_ID));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		// actionLogModel.setCustomField2("Updating the User Information");
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		CustomerListViewModel customerListViewModel = (CustomerListViewModel) baseWrapper
				.getBasePersistableModel();
		CustomerModel customerModel = new CustomerModel();
		customerModel.setCustomerId(customerListViewModel.getCustomerId());
		customerModel = customerDAO.findByPrimaryKey(customerModel
				.getCustomerId());

		customerModel.setReferringName1(customerListViewModel
				.getReferringName1());
		customerModel
				.setReferringNic1(customerListViewModel.getReferringNic1());
		customerModel.setReferringName2(customerListViewModel
				.getReferringName2());
		customerModel
				.setReferringNic2(customerListViewModel.getReferringNic2());
		customerModel.setRegister(customerListViewModel.getRegister());
		customerModel.setComments(customerListViewModel.getComments());
		customerModel.setDescription(customerListViewModel.getDescription());
		customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		customerModel.setUpdatedOn(new Date());
		AppUserModel appUserModel = new AppUserModel();

		appUserModel = appUserManager.getUser(String
				.valueOf(customerListViewModel.getAppUserId()));

		String dbMobileNo = appUserModel.getMobileNo() == null ? ""
				: appUserModel.getMobileNo();

		if (!appUserModel.getUsername().equals(
				customerListViewModel.getUsername())) {
			if (!this.isUserNameUnique(customerListViewModel.getUsername()))
				throw new FrameworkCheckedException("UserNameUniqueException");
		}
		String oldMobileNo = appUserModel.getMobileNo();
		appUserModel.setFirstName(customerListViewModel.getFirstName());
		appUserModel.setLastName(customerListViewModel.getLastName());

		appUserModel.setAddress1(customerListViewModel.getAddress1());
		appUserModel.setAddress2(customerListViewModel.getAddress2());
		appUserModel.setCity(customerListViewModel.getCity());
		appUserModel.setState(customerListViewModel.getState());
		appUserModel.setCountry(customerListViewModel.getCountry());
		appUserModel.setNic(customerListViewModel.getNic());
		appUserModel.setDob(customerListViewModel.getDob());
		appUserModel.setMobileNo(customerListViewModel.getMobileNo());
		appUserModel.setMobileTypeId(customerListViewModel.getMobileTypeId());
		if (customerListViewModel.getPassword() != null)
			{
			appUserModel.setPassword(EncoderUtils
					.encodeToSha(customerListViewModel.getPassword()));
			}
		appUserModel.setPasswordHint(customerListViewModel.getPasswordHint());
		appUserModel.setFax(customerListViewModel.getFax());
		appUserModel.setEmail(customerListViewModel.getEmail());
		appUserModel.setZip(customerListViewModel.getZip());
		appUserModel.setMotherMaidenName(customerListViewModel
				.getMotherMaidenName());

		appUserModel
				.setVerified(customerListViewModel.getVerified() == null ? false
						: customerListViewModel.getVerified());
		appUserModel.setAccountEnabled(customerListViewModel
				.getAccountEnabled() == null ? false : customerListViewModel
				.getAccountEnabled());
		appUserModel.setAccountExpired(customerListViewModel
				.getAccountExpired() == null ? false : customerListViewModel
				.getAccountExpired());
		appUserModel
				.setAccountLocked(customerListViewModel.getAccountLocked() == null ? false
						: customerListViewModel.getAccountLocked());
		appUserModel.setCredentialsExpired(customerListViewModel
				.getCredentialsExpired() == null ? false
				: customerListViewModel.getCredentialsExpired());

		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(new Date());

		

		// if saved(db) mobile number is not equal to user entered mobile number
		if (!dbMobileNo.equals(customerListViewModel.getMobileNo())) {

			// first check the uniqueness of the newly entered mobile number
			/*if (!this.isMobileNumUnique(customerListViewModel.getMobileNo())) {
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
			*/
		}
		// check appuser userDeviceAccount
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		int recordCount = userDeviceAccountsDAO.countByExample(userDeviceAccountsModel);
		
		if (!dbMobileNo.equals(customerListViewModel.getMobileNo())	&& recordCount == 1) 
		{

			userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
			userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
					
			CustomList <UserDeviceAccountsModel> customList = userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
			List <UserDeviceAccountsModel> list = customList.getResultsetList();
			
			boolean mobileNoChangeFlag = false;
			
			if(list != null && list.size() > 0)
			{
				userDeviceAccountsModel = list.get(0);

				userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				userDeviceAccountsModel.setUpdatedOn(new Date());
				userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
			
			String messageString = "";
			if(mobileNoChangeFlag)
			{
				Object[] args ={userDeviceAccountsModel.getUserId(),oldMobileNo,customerListViewModel.getMobileNo()};
				messageString = MessageUtil.getMessage("ChangeMobile.mobileChanged", args);
			}
			else
			{
				messageString = MessageUtil.getMessage("smsCommand.act_sms8");
			}

			SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(),messageString);
			smsSender.send(smsMessage);
		}

		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setCustomField1(String.valueOf(appUserModel
				.getAppUserId()));
		actionLogModel = logAction(actionLogModel);
				
		customerDAO.saveOrUpdate(customerModel);
		try
		{
			appUserDAO.saveUser(appUserModel);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
	  }

		return baseWrapper;

	}

	///////////////////////////CUSTOMER SEGMENT//////////////////////
	
	@Override
	public SearchBaseWrapper searchCustomerSegmentList(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		 CustomList<SegmentModel> customList = null;
		  if (((SegmentModel) searchBaseWrapper.getBasePersistableModel()).getSegmentId()==null){
			  customList = this.segmentDAO.findByExample( (SegmentModel)
					  								searchBaseWrapper.getBasePersistableModel(),
											        searchBaseWrapper.getPagingHelperModel(),
											        searchBaseWrapper.getSortingOrderMap());
		  }
		  else{
			  SegmentModel segment  = (SegmentModel) this.segmentDAO.findByPrimaryKey
					  						  (((SegmentModel) searchBaseWrapper.getBasePersistableModel()).getSegmentId());
			  List<SegmentModel> list = new ArrayList<SegmentModel>(1);
			  list.add(segment);
			  customList = new CustomList<SegmentModel>(list);
			  searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(list.size());
		  }
		  searchBaseWrapper.setCustomList(customList);
	    return searchBaseWrapper;
		
	}

	@Override
	public BaseWrapper loadCustomerSegment(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		SegmentModel segmentModel = (SegmentModel)this.
		        segmentDAO.findByPrimaryKey( (baseWrapper.getBasePersistableModel()).
		                                    getPrimaryKey());
		    baseWrapper.setBasePersistableModel(segmentModel);
		    return baseWrapper;
	}

	public SearchBaseWrapper loadCustomerSegment(SearchBaseWrapper searchBaseWrapper)
	  {
	    SegmentModel segmentModel = (SegmentModel)this.segmentDAO.findByPrimaryKey( (
	        searchBaseWrapper.getBasePersistableModel()).getPrimaryKey());
	    searchBaseWrapper.setBasePersistableModel(segmentModel);
	    return searchBaseWrapper;
	  }
	@Override
	public BaseWrapper createOrUpdateCustomerSegment(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		SegmentModel segmentModel = (SegmentModel) baseWrapper.getBasePersistableModel();
		
		
		//***Check if customers are associated to the segment in case of Deactivate
		if(!segmentModel.getIsActive() && null!=segmentModel.getPrimaryKey() )
		{
			
			
			CustomerModel customerModel = new CustomerModel();
			customerModel.setSegmentId(segmentModel.getPrimaryKey());
			
			
			ExampleConfigHolderModel exampleConfigHolder = new ExampleConfigHolderModel();
			exampleConfigHolder.setEnableLike(false);
			
			Integer customerCount = customerDAO.countByExample(customerModel, exampleConfigHolder);
			if(customerCount == null || customerCount.intValue() > 0)
			{
				throw new FrameworkCheckedException(MessageConstantsInterface.ERROR_MSG_SEG_CUST_EXISTS);		
			}		
		}
		//***Check if name already exists
		SegmentModel newSegmentModel = new SegmentModel();
		newSegmentModel.setName(segmentModel.getName());
	    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);		
	    int recordCount = segmentDAO.countByExample(newSegmentModel,exampleHolder);
	    
	    //***Checks if record with same name already exists, then is it same product to be edited 
	    if (recordCount>0 && segmentModel.getPrimaryKey() != null){	    	
	    	
	     /* SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	   	  SegmentModel segmentModelEdited = new SegmentModel();
	   	  segmentModelEdited.setPrimaryKey(segmentModel.getPrimaryKey());
	   	  searchBaseWrapper.setBasePersistableModel(segmentModelEdited);  	  
	   	  searchBaseWrapper = this.loadCustomerSegment(searchBaseWrapper);
	   	  segmentModelEdited= (SegmentModel) searchBaseWrapper.getBasePersistableModel();	 
	   	  
	    	
	   	  if (!segmentModelEdited.getName().equals(segmentModel.getName())){
		    throw new FrameworkCheckedException(MessageConstantsInterface.ERROR_MSG_DUPLICATE_SEGMENT);
		  }
		  */
	    }    
	    if (recordCount == 0 || segmentModel.getPrimaryKey() != null)
	     {
	    	 segmentModel = this.segmentDAO.saveOrUpdate( (
	    	 SegmentModel) baseWrapper.getBasePersistableModel());
	    	 baseWrapper.setBasePersistableModel(segmentModel);
	       
	    	 actionLogModel.setCustomField1(segmentModel.getSegmentId().toString());
	    	 actionLogModel.setCustomField11(segmentModel.getName());
	    	 this.actionLogManager.completeActionLog(actionLogModel);
	    	 return baseWrapper;

	     }
	     else
	     {
	       //set baseWrapper to null if record exists
	       throw new FrameworkCheckedException(MessageConstantsInterface.ERROR_MSG_DUPLICATE_SEGMENT);
	     }

	}
	private boolean isMobileNumUnique(String mobileNo) 
	{
		AppUserModel appUserModel = new AppUserModel();
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		appUserModel.setMobileNo(mobileNo);
		CustomList list = this.appUserDAO.findByExample(appUserModel);
		int recordCount = appUserDAO.countByExample(appUserModel, exampleHolder);
		int size = list.getResultsetList().size();
		if (size == 0 || recordCount == 0)
			return true;
		else
			return false;
	}

	public int getCustomerCountByCustomerAccountType(Long customerAccountTypeId) throws FrameworkCheckedException
	{
		CustomerModel customerModel = new CustomerModel();
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		customerModel.setCustomerAccountTypeId(customerAccountTypeId);
		int recordCount = customerDAO.countByExample(customerModel, exampleHolder);
		
		return recordCount;
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
			int countAppUser = this.appUserDAO.countByExample(appUserModel,
					exampleHolder);
			int countGoldenNos = this.goldenNosDAO.countByExample(
					goldenNosModel, exampleHolder);
			if (countAppUser == 0 && countGoldenNos == 0) {
				flag = false;
			}
		}

		return String.valueOf(nextLongValue);
	}

	private boolean isUserNameUnique(String userName) {
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setUsername(userName);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		CustomList list = this.appUserDAO.findByExample(appUserModel);
		int recordCount = appUserDAO
				.countByExample(appUserModel, exampleHolder);
		int size = list.getResultsetList().size();
		if (size == 0 || recordCount == 0)
			return true;
		else
			return false;
	}

	private ActionLogModel logAction(ActionLogModel actionLogModel)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if (null == actionLogModel.getActionLogId()) {
			baseWrapper = this.actionLogManager
					.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		} else {
			baseWrapper = this.actionLogManager
					.createOrUpdateActionLog(baseWrapper);
		}
		return (ActionLogModel) baseWrapper.getBasePersistableModel();
	}


	@Override
	public List<CustomerModel> getCustomerModelListByCustomerIDs(
			List<Long> customerIDsList) throws FrameworkCheckedException {
		List <CustomerModel> customerModelsList = customerDAO.getCustomerModelListByCustomerIDs(customerIDsList);
		return customerModelsList;
	}
	
	@Override
	public void updateCustomerModels(List<CustomerModel> customerModels)
			throws FrameworkCheckedException {
		customerDAO.saveOrUpdateCollection(customerModels);
	}
	
	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setUserDeviceAccountsDAO(
			UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public void setGoldenNosDAO(GoldenNosDAO goldenNosDAO) {
		this.goldenNosDAO = goldenNosDAO;
	}

	public void setSequenceGenerator(
			OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}
	
	public void setSegmentDAO(
			SegmentDAO segmentDAO) {
		this.segmentDAO = segmentDAO;
	}

	@Override
	public int countByExample (CustomerModel customerModel, ExampleConfigHolderModel exampleConfigHolder){
		return customerDAO.countByExample(customerModel, exampleConfigHolder);
	}
	
	@Override
	public CustomerModel findByPrimaryKey(Long primaryKey) throws FrameworkCheckedException {
    	return customerDAO.findByPrimaryKey(primaryKey);
    }

	@Override
	public List<CustomerRemitterModel> getRemittanceModelList(Long customerId)
			throws FrameworkCheckedException {
		CustomerRemitterModel customerRemmitterModel = new CustomerRemitterModel();
		customerRemmitterModel.setCustomerId(customerId);
		customerRemmitterModel.setIsActive(1L);
		CustomList <CustomerRemitterModel> listCustomerRemitterModels = this.customerRemitterDAO.findByExample(customerRemmitterModel, null);
		return listCustomerRemitterModels.getResultsetList();
	}

	@Override
	public List<SegmentModel> findActiveSegments(SegmentModel segmentModel) throws FrameworkCheckedException {
		CustomList<SegmentModel> customList =segmentDAO.findByExample(segmentModel,null);
		List<SegmentModel> list = null;
		if(customList != null && customList.getResultsetList() != null && !customList.getResultsetList().isEmpty())
			list = customList.getResultsetList();
		return list;
	}

	@Override
	public void updateBulkCustomerSegments(List<CustomerModel> customerModelList) throws FrameworkCheckedException {
		customerDAO.saveOrUpdateCollection(customerModelList);
	}

	public void setCustomerRemitterDAO(CustomerRemitterDAO customerRemitterDAO) {
		this.customerRemitterDAO = customerRemitterDAO;
	}


}
