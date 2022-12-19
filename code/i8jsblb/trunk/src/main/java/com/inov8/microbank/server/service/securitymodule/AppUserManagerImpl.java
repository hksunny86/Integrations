package com.inov8.microbank.server.service.securitymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.exception.UserExistsException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.appversionmodule.AppUserFormListViewModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.AppUserVO;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.mobilenetworks.dao.MobileNetworkDAO;
import com.inov8.microbank.mobilenetworks.model.MobileNetworkModel;
import com.inov8.microbank.server.dao.appuserhistorymodule.AppUserHistoryViewDao;
import com.inov8.microbank.server.dao.appusermobilehistorymodule.AppUserMobileHistoryDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintReportDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.dao.customermodule.ApplicantDetailDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoUserDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserFormListViewDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.ola.server.dao.accountholder.AccountHolderDAO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Timestamp;
import java.util.*;

public class AppUserManagerImpl implements AppUserManager
{
  private static Logger logger = Logger.getLogger(AppUserManagerImpl.class);

  private AppUserDAO appUserDAO;
  private AppUserManager appUserManager;
  private AppUserFormListViewDAO appUserFormListViewDao;
  private AppUserMobileHistoryDAO appUserMobileHistoryDAO;
  private ActionLogManager actionLogManager;
  private SmsSenderService smsSenderService;
  private CustomerDAO customerDAO;
  private RetailerContactDAO retailerContactDAO;
  private AppUserHistoryViewDao appUserHistoryViewDao;
  private AccountHolderDAO accountHolderDAO;
  private AccountInfoDAO accountInfoDao;
  private ApplicantDetailDAO applicantDetailDAO;
  private IvrRequestHandler		ivrRequestHandler;
  private ComplaintReportDAO complaintReportDAO;
  private MessageSource messageSource;
  private MnoUserDAO mnoUserDAO;
  private DebitCardModelDAO debitCardModelDAO;
  private MobileNetworkDAO mobileNetworkDAO;



  /**
   * @see org.appfuse.service.UserManager#getUser(java.lang.String)
   */
  
  public AppUserModel fetchL3Agent(Long retailerContactId) throws FrameworkCheckedException 
  {
	  return this.appUserDAO.verifyL3Agent(retailerContactId);
  }
  
  
  public SearchBaseWrapper findAppUsersByAppUserTypeId(long appUserTypeId) throws FrameworkCheckedException
  {
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
      List<AppUserModel> appUserModelList = appUserDAO.findAppUsersByType(appUserTypeId);
      if(appUserModelList != null)
      {
      	CustomList<AppUserModel> customList = new CustomList<AppUserModel>();
      	customList.setResultsetList(appUserModelList);
      	searchBaseWrapper.setCustomList(customList);
      }        
      return searchBaseWrapper;
  }
  
  @Override  
	public AppUserModel getAppUserWithRegistrationState(String mobileNo, String cnic, Long registrationState) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserDAO.getAppUserWithRegistrationState(mobileNo, cnic, registrationState);			
		return appUserModel;	  
	}
  
  @Override
	public AppUserModel loadAppUserByCnicAndType(String cnic) throws FrameworkCheckedException {		
		AppUserModel appUserModel = appUserDAO.loadAppUserByCnicAndType(cnic);
		return appUserModel;		
	}

	@Override
	public AppUserModel loadAppUserByEmail(String emailAddress) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserDAO.loadAppUserByEmailAddress(emailAddress);
		return appUserModel;
	}

	@Override
	public AppUserModel loadAppUserByCnicAndMobileAndAppUserType(String cnic, String mobileNo, Long appUserTypeId) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserDAO.loadAppUserByCnicAndMobileAndAppUserType(cnic, mobileNo, appUserTypeId);
		return appUserModel;
	}

	public AppUserModel loadWalkinCustomerAppUserByCnic(String cnic) throws FrameworkCheckedException{
	  AppUserModel appUserModel = appUserDAO.loadWalkinCustomerAppUserByCnic(cnic);
		return appUserModel;
  }
  
  @Override
  public AppUserModel loadAppUserByMobileAndType(String mobileNo)	throws FrameworkCheckedException {		
	AppUserModel appUserModel = appUserDAO.loadAppUserByMobileAndType(mobileNo);
	return appUserModel;		
  }

	@Override
	public AppUserModel loadAppUserByMobileAndTypeForCustomer(String mobileNo) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserDAO.loadAppUserByMobileAndTypeForCustomer(mobileNo);
		return appUserModel;
	}

	@Override
	public AppUserModel loadAppUserModelByCustomerId(Long customerId) {
		return appUserDAO.loadAppUserModelByCustomerId(customerId);
	}

	@Override
	public List<AppUserModel> loadAppUserMarkAmaAccountDebitBlock() throws FrameworkCheckedException {
		return appUserDAO.loadAppUserMarkAmaAccountDebitBlock();
	}

	@Override
	public List<AppUserModel> loadPendingAccountOpeningAppUserModel() throws FrameworkCheckedException {
		return appUserDAO.loadPendingAccountOpeningAppUser();
	}

	@Override
	public AppUserModel getAppUserWithRegistrationStates(String mobileNo, String cnic, Long ...registrationStates) throws FrameworkCheckedException {
		return appUserDAO.getAppUserWithRegistrationStates(mobileNo, cnic, registrationStates);
	}

	@Override
 public List<Object []> getAppUserCNICsToMarkDormant() throws FrameworkCheckedException{
	  
	return appUserDAO.getCNICsToMarkAccountDormant();
  }
  
  public AppUserModel getUser(String userId)
  {
	  AppUserModel appUserModel = appUserDAO.getUser(new Long(userId));
    return appUserModel;
  }
  
  public AppUserModel loadAppUserByQuery(String mobileNo, long appUserTypeId)
  {
	  AppUserModel appUserModel = appUserDAO.loadAppUserByQuery(mobileNo, appUserTypeId);
    return appUserModel;
  }
  
  public AppUserModel loadAppUserByMobileByQuery(final String mobileNo)
  {
	  AppUserModel appUserModel = appUserDAO.loadAppUserByMobileByQuery(mobileNo);
	  return appUserModel;
  }
  public void setActionLogManager(ActionLogManager actionLogManager) 
  {
		this.actionLogManager = actionLogManager;
  }

  /**
   * @see org.appfuse.service.UserManager#getUsers(org.appfuse.model.User)
   */
  public List getUsers(AppUserModel user)
  {
    return appUserDAO.getUsers(user);
  }

  /**
   * @see org.appfuse.service.UserManager#saveUser(org.appfuse.model.User)
   */
  public void saveUser(AppUserModel user) throws UserExistsException
  {
    // if new user, lowercase userId
    if (user.getVersionNo() == null)
    {
      user.setUsername(user.getUsername().toLowerCase());
    }
    try
    {
      appUserDAO.saveUser(user);
    }
    
    catch (Exception e)
    {
    	
    	
      throw new UserExistsException(e.getMessage());
    }
  }

  /**
   * @see org.appfuse.service.UserManager#removeUser(java.lang.String)
   */
  public void removeUser(String userId)
  {
    appUserDAO.removeUser(new Long(userId));
  }

  public AppUserModel getUserByUsername(String username) throws
      UsernameNotFoundException
  {
    return (AppUserModel) appUserDAO.loadUserByUsername(username);
  }
  
  
  public SearchBaseWrapper searchAppUserByMobile(SearchBaseWrapper searchBaseWrapper)
  {
	    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel() ;
	    exampleHolder.setMatchMode( MatchMode.EXACT );
	  
	    CustomList<AppUserModel> list = this.appUserDAO.findByExample( (AppUserModel)
        searchBaseWrapper.getBasePersistableModel(),
        searchBaseWrapper.getPagingHelperModel(), null, exampleHolder);
    searchBaseWrapper.setCustomList(list);
    return searchBaseWrapper;
  }

  public BaseWrapper loadAppUser(BaseWrapper baseWrapper)
  {
    AppUserModel appUserModel = (AppUserModel)this.
        appUserDAO.findByPrimaryKey( (baseWrapper.getBasePersistableModel()).getPrimaryKey());
    baseWrapper.setBasePersistableModel(appUserModel);
    return baseWrapper;
  }


  public void setAppUserDAO(AppUserDAO appUserDAO)
  {
    this.appUserDAO = appUserDAO;
  }

	public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
		this.retailerContactDAO = retailerContactDAO;
	}

public BaseWrapper searchCustomerByUser(BaseWrapper wrapper) throws FrameworkCheckedException {

	CustomList<AppUserModel> list = appUserDAO.findByExample((AppUserModel)wrapper.getBasePersistableModel());
	if(null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0)
	{
		AppUserModel appUserModel = list.getResultsetList().get(0);
		CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
		wrapper.setBasePersistableModel(customerModel);
	}
	else
	{
		wrapper.setBasePersistableModel(null); // set null if no appUser Found against the mobile number
	}
	return wrapper;
}

      public BaseWrapper searchRetailerByMobile(BaseWrapper wrapper) throws FrameworkCheckedException
      {
        CustomList<AppUserModel> appUserList = appUserDAO.findByExample((AppUserModel)wrapper.getBasePersistableModel());
        if(null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0)
        {
          wrapper.setBasePersistableModel(appUserList.getResultsetList().get(0));
        }
        else
        {
          wrapper.setBasePersistableModel(null); // set null if no appUser Found against the mobile number
        }
        return wrapper;
      }

      public BaseWrapper saveOrUpdateAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
      {
    	  
    	AppUserModel user = (AppUserModel)baseWrapper.getBasePersistableModel();
        // if new user, lowercase userId
        if (user.getVersionNo() == null)
        {
          user.setUsername(user.getUsername().toLowerCase());
        }
        try
        {
          appUserDAO.saveOrUpdate(user);
        }
       
        catch (DataIntegrityViolationException e)
        {
        	throw new FrameworkCheckedException("Exception occured while creating/updating the AppUserModel", e);
          //throw new UserExistsException("User '" + user.getUsername() +
            //                            "' already exists!");
        }
        catch(Exception e)
        {
        	throw new FrameworkCheckedException(e.getMessage());
        }
        return baseWrapper;
      }
      
      public BaseWrapper closeAgentAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException
      {
    	ActionLogModel actionLogModel = new ActionLogModel();
  		actionLogModel.setActionId((Long)baseWrapper.getObject("actionId"));
  		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject("usecaseId") );
  		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); 
  		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
  		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
  		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
  		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
  		
  		actionLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());   	
      	AppUserModel appUserModel;
      	appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
      	
      	//appUserModel = this.appUserDAO.closeAgnetAccount(appUserModel);
      	baseWrapper.setBasePersistableModel(appUserModel);  
      	
      	actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
  		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
  		actionLogModel.setCustomField1( String.valueOf(appUserModel.getAppUserId()) );
      
      	return baseWrapper;
      }
      
      public BaseWrapper closeHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
    	  
    	ActionLogModel actionLogModel = new ActionLogModel();
  		actionLogModel.setActionId((Long)baseWrapper.getObject("actionId"));
  		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject("usecaseId") );
  		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); 
  		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
  		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
  		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
  		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
  		
  		actionLogModel = logAction(actionLogModel);
  		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
      	
      	AppUserModel appUserModel;
      	appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
      	
      	appUserModel = this.appUserDAO.closeHandlerAccount(appUserModel);
      	baseWrapper.setBasePersistableModel(appUserModel);  
      	
      	actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
  		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
  		actionLogModel.setCustomField1( String.valueOf(appUserModel.getAppUserId()) );
  		actionLogModel = logAction(actionLogModel);
      
      	return baseWrapper;
      }
      public boolean isMobileNumberCNICUnique(BaseWrapper baseWrapper) throws FrameworkCheckedException
  	{
  		boolean returnValue = Boolean.TRUE;
  		String mobileNo = (String) baseWrapper.getObject("mobileNo");
  		String cnic		= (String) baseWrapper.getObject("cnic");
  		Long appUserId = (Long)baseWrapper.getObject("appUserId");
  		AppUserModel appUserModel = new AppUserModel();
  		if(mobileNo != null || !"".equals(mobileNo)){
  			appUserModel.setMobileNo(mobileNo);
  		}
  		if(cnic != null || !"".equals(cnic)){
  			appUserModel.setNic(cnic);
  		}
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

  		
  		return returnValue;
  	}


	public BaseWrapper searchAppUser(SearchBaseWrapper searchBaseWrapper)  throws
      FrameworkCheckedException 
      {
          CustomList<AppUserModel> appUserList = appUserDAO.findByExample((AppUserModel)searchBaseWrapper.getBasePersistableModel());
          if(null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0)
          {
        	  searchBaseWrapper.setBasePersistableModel(appUserList.getResultsetList().get(0));
          }
          else
          {
        	  searchBaseWrapper.setBasePersistableModel(null); // set null if no appUser Found against the mobile number
          }
          return searchBaseWrapper;

    	  
      }
      
      public SearchBaseWrapper searchAppUserForm(SearchBaseWrapper searchBaseWrapper)  throws
      FrameworkCheckedException 
      {
    	  CustomList<AppUserFormListViewModel>
          list = this.appUserFormListViewDao.findByExample( (
        		  AppUserFormListViewModel)
          searchBaseWrapper.
          getBasePersistableModel(),
          searchBaseWrapper.
          getPagingHelperModel(),
          searchBaseWrapper.
          getSortingOrderMap());
      searchBaseWrapper.setCustomList(list);
      return searchBaseWrapper;      }

      
      /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.securitymodule.AppUserManager#getAppUserModel(com.inov8.microbank.common.model.AppUserModel)
     */
    public AppUserModel getAppUserModel(AppUserModel example) {
    	  
    	AppUserModel appUserModel = null;
    	
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setEnableLike(Boolean.FALSE);
		exampleHolder.setMatchMode(MatchMode.EXACT);
		
		CustomList<AppUserModel> list = this.appUserDAO.findByExample(example, null, null, exampleHolder);
		
		if(null != list.getResultsetList() && list.getResultsetList().size() > 0 ) {
			
			appUserModel = list.getResultsetList().get(0);			
		}
		
		return appUserModel;
      }

	public AppUserModel loadAppUserByCNIC(String cnic) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserDAO.loadAppUserByCNIC(cnic);
		return appUserModel;
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
	
	@Override
	public SearchBaseWrapper loadMobileHistory(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		AppUserMobileHistoryModel model = (AppUserMobileHistoryModel) wrapper.getBasePersistableModel();
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		sortingOrderMap.put("fromDate", SortingOrder.DESC);
		wrapper.setSortingOrderMap(sortingOrderMap);
	    CustomList<AppUserMobileHistoryModel> customList = null;
	    customList= this.appUserMobileHistoryDAO.findByExample(model,wrapper.getPagingHelperModel(),wrapper.getSortingOrderMap());
	    wrapper.setCustomList( customList );
		return wrapper;
	}
	
	@Override
	public BaseWrapper updateMobileNo(BaseWrapper wrapper) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(wrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		AppUserMobileHistoryModel model = (AppUserMobileHistoryModel) wrapper.getBasePersistableModel();
		MobileNetworkModel mobileNetworkModel = null;
		if(model.getMobileNetworkId() != null)
			mobileNetworkModel = mobileNetworkDAO.findByPrimaryKey(model.getMobileNetworkId());
		AppUserModel appUserModel = this.appUserDAO.findByPrimaryKey(model.getAppUserId());
		String oldMobileNumber = appUserModel.getMobileNo();
		actionLogModel.setCustomField11(model.getMobileNo());
		smsSenderService.sendSms(appUserModel.getMobileNo(), "Dear user your mobile number is updated successfully from "+appUserModel.getMobileNo()+" to "+model.getMobileNo());
		smsSenderService.sendSms(model.getMobileNo(), "Dear user your mobile number is updated successfully from "+appUserModel.getMobileNo()+" to "+model.getMobileNo());
		appUserModel.setAppUserId(model.getAppUserId());
		appUserModel.setMobileNo(model.getMobileNo());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(new Date());
		if(mobileNetworkModel != null)
			appUserModel.setCustomerMobileNetwork(mobileNetworkModel.getNetworkName());
		appUserModel.setMobileNetworkId(model.getMobileNetworkId());
		this.appUserDAO.saveOrUpdate(appUserModel);
		//update accountInfoModel
		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setCustomerMobileNo(oldMobileNumber);
		accountInfoModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		CustomList<AccountInfoModel> customList = accountInfoDao.findByExample(accountInfoModel);
		if(CollectionUtils.isNotEmpty(customList.getResultsetList())){
			accountInfoModel = customList.getResultsetList().get(0);
			accountInfoModel.setCustomerMobileNo(model.getMobileNo());
			accountInfoModel.setUpdatedOn(new Date());
			accountInfoDao.saveOrUpdate(accountInfoModel);
		}
		AccountInfoModel hraAccountInfoModel = new AccountInfoModel();
		hraAccountInfoModel.setCustomerMobileNo(oldMobileNumber);
		hraAccountInfoModel.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
		CustomList<AccountInfoModel> hraList = accountInfoDao.findByExample(hraAccountInfoModel);
		if(CollectionUtils.isNotEmpty(hraList.getResultsetList()))
		{
			hraAccountInfoModel = hraList.getResultsetList().get(0);
			hraAccountInfoModel.setCustomerMobileNo(model.getMobileNo());
			hraAccountInfoModel.setUpdatedOn(new Date());
			accountInfoDao.saveOrUpdate(hraAccountInfoModel);
		}
		CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
		RetailerContactModel retailerContactModel = appUserModel.getRetailerContactIdRetailerContactModel();
		if(customerModel != null)
		{
			customerModel.setMobileNo(model.getMobileNo());
			customerDAO.saveOrUpdate(customerModel);
			Long[] cardStateIds = new Long[]{CardConstantsInterface.CARD_STATUS_HOT};
			DebitCardModel debitCardModel = debitCardModelDAO.getDebitCardModelByAppUserIdAndCardStateId(model.getAppUserId(),cardStateIds);
			if(debitCardModel != null)
			{
				debitCardModel.setMobileNo(model.getMobileNo());
				debitCardModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				debitCardModel.setUpdatedOn(new Date());
				debitCardModelDAO.saveOrUpdate(debitCardModel);
			}
		}
		else if(retailerContactModel != null)
		{
			retailerContactModel.setMobileNo(model.getMobileNo());
			retailerContactModel.setZongMsisdn(model.getMobileNo());
			retailerContactDAO.saveOrUpdate(retailerContactModel);
			ApplicantDetailModel applicantDetailModel=new ApplicantDetailModel();
			applicantDetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
			applicantDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			CustomList<ApplicantDetailModel>	list=	applicantDetailDAO.findByExample(applicantDetailModel, null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
			if(list!=null && list.getResultsetList().size()>0)
			{
				applicantDetailModel	=	list.getResultsetList().get(0);
				applicantDetailModel.setMobileNo(model.getMobileNo());
				applicantDetailDAO.saveOrUpdate(applicantDetailModel);
			}
		}
		//Update Mobile # in ACCOUNT_HOLDER table
//		String encryptedCnic = EncryptionUtil.encryptPin(appUserModel.getNic());
		String encryptedCnic = appUserModel.getNic();
		accountHolderDAO.updateMobileNo(encryptedCnic, appUserModel.getMobileNo());
		model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		model.setCreatedOn(new Date());
		model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		model.setUpdatedOn(new Date());
		model.setFromDate(new Date());
		appUserMobileHistoryDAO.saveOrUpdate(model);
		actionLogModel.setCustomField1(model.getAppUserId().toString());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return wrapper;
	}
	
	@Override
	public BaseWrapper updateCNIC(BaseWrapper wrapper) throws FrameworkCheckedException {
		Long actionAuthId = (Long) wrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(wrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		AppUserMobileHistoryModel model = (AppUserMobileHistoryModel) wrapper.getBasePersistableModel();
		
		AppUserModel appUserModel = this.appUserDAO.findByPrimaryKey(model.getAppUserId());
		actionLogModel.setCustomField11(model.getNic());
		smsSenderService.sendSms(appUserModel.getMobileNo(), "Dear user your CNIC number is updated successfully from "+appUserModel.getNic()+" to "+model.getNic());
		smsSenderService.sendSms(model.getMobileNo(), "Dear user your CNIC number is updated successfully from "+appUserModel.getNic()+" to "+model.getNic());
		appUserModel.setAppUserId(model.getAppUserId());
		appUserModel.setNic(model.getNic());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(new Date());
		this.appUserDAO.saveOrUpdate(appUserModel);
		CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
		RetailerContactModel retailerContactModel = appUserModel.getRetailerContactIdRetailerContactModel();
		if(customerModel != null)
		{
			ApplicantDetailModel applicantDetailModel = new ApplicantDetailModel();
			applicantDetailModel.setCustomerId(customerModel.getCustomerId());
			CustomList<ApplicantDetailModel> list = applicantDetailDAO.findByExample(applicantDetailModel);
			if(CollectionUtils.isNotEmpty(list.getResultsetList())){
				applicantDetailModel = list.getResultsetList().get(0);
				applicantDetailModel.setIdNumber(model.getNic());
				applicantDetailModel.setIdType(1L); //CNIC
				applicantDetailDAO.saveOrUpdate(applicantDetailModel);
			}
			DebitCardModel debitCardModel = debitCardModelDAO.getDebitCardModelByCustomerAppUserId(appUserModel.getAppUserId());
			if(debitCardModel != null)
			{
				debitCardModel.setCnic(model.getNic());
				debitCardModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				debitCardModel.setUpdatedOn(new Date());
				debitCardModelDAO.saveOrUpdate(debitCardModel);
			}
		}
		else if(retailerContactModel != null)
		{
			retailerContactModel.setCnic(model.getNic());
			retailerContactDAO.saveOrUpdate(retailerContactModel);
			
			ApplicantDetailModel applicantDetailModel = new ApplicantDetailModel();
			applicantDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			applicantDetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
			CustomList<ApplicantDetailModel> list = applicantDetailDAO.findByExample(applicantDetailModel);
			if(CollectionUtils.isNotEmpty(list.getResultsetList())){
				applicantDetailModel = list.getResultsetList().get(0);
				applicantDetailModel.setIdNumber(model.getNic());
				applicantDetailDAO.saveOrUpdate(applicantDetailModel);
			}
		}

		//Update CNIC # in ACCOUNT_HOLDER table
//		String encryptedCnic = EncryptionUtil.encryptPin(model.getNic());
		String encryptedCnic = model.getNic();
		accountHolderDAO.updateCnic(encryptedCnic, appUserModel.getMobileNo());
		
		model.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		model.setCreatedOn(new Date());
		model.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		model.setUpdatedOn(new Date());
		model.setFromDate(new Date());
		appUserMobileHistoryDAO.saveOrUpdate(model);

		actionLogModel.setCustomField1(model.getAppUserId().toString());
	    this.actionLogManager.completeActionLog(actionLogModel);
		return wrapper;
	}

	public void setAppUserFormListViewDao(AppUserFormListViewDAO appUserFormListViewDao)
	{
		this.appUserFormListViewDao = appUserFormListViewDao;
	}

	public void setAppUserMobileHistoryDAO(AppUserMobileHistoryDAO appUserMobileHistoryDAO) {
		this.appUserMobileHistoryDAO = appUserMobileHistoryDAO;
	}

	public void setSmsSenderService(SmsSenderService smsSenderService) {
		this.smsSenderService = smsSenderService;
	}
	
	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
	
	/**
	 * Method to retrieve the last three passwords of the appUser
	 */
	public List<String> getAppUserPreviousThreePasswordsByAppUserId ( String username ) throws FrameworkCheckedException{
		return appUserDAO.getPreviousThreePasswordsByAppUserId(username);
	}
	
	@Override
	public void saveAppUserPasswordHistory ( AppUserPasswordHistoryModel appUserPasswordHistory ) throws FrameworkCheckedException{
		appUserDAO.saveAppUserPasswordHistory(appUserPasswordHistory);
	}
	
	@Override
	public List <AppUserModel> getAllUsersForPasswordChangeRequired ( ) throws FrameworkCheckedException{
		
		return appUserDAO.getAllAppUsersForPasswordChangeRequired( );
	}
	
	
	
	
	
	@Override
	public List <AppUserModel> getAllUsersForAccountInactive ( ) throws FrameworkCheckedException{
		
		return appUserDAO.getAllAppUsersForAccountInactive( );
	}
	
	@Override
	public Long markPasswordChangeRequired ( List <AppUserModel> users ) throws FrameworkCheckedException{
		List <AppUserModel> updatedList = new ArrayList<AppUserModel>(10);
		Long listSize = 0L;
		for (AppUserModel appUserModel : users) {
			if (isPasswordChangeDue(appUserModel)){
				appUserModel.setPasswordChangeRequired(true);
				appUserModel.setUpdatedBy(3L);
				appUserModel.setUpdatedOn(new Date());

				updatedList.add(appUserModel);
			}
		}
		listSize = (long)updatedList.size();
		if ( updatedList.size() > 0 ){
			appUserDAO.saveOrUpdateCollection(updatedList);
		}
		
		return listSize;
	}
	

	@Override
	public Long markAppUserAccountInactive ( List<AppUserModel> users ) throws FrameworkCheckedException {
		Calendar calendar = GregorianCalendar.getInstance();
		Calendar expiryCalendar = GregorianCalendar.getInstance();
		Date currentDate = calendar.getTime();
		List <AppUserModel> updatedList = new ArrayList<AppUserModel>(10);
		Long listSize = 0L;
		for (AppUserModel appUserModel : users) {
			if ( appUserModel.getLastLoginTime() != null ){//check if user was inactive for 30days then mark credentials expired
			expiryCalendar.setTime(appUserModel.getLastLoginTime());
			expiryCalendar.add(Calendar.DATE, 30);
			Date expiryRequiredDate = expiryCalendar.getTime();
			if ( currentDate.after(expiryRequiredDate) ){
				appUserModel.setCredentialsExpired(true);
				appUserModel.setPasswordChangeRequired(true); //FIX for bug id 2095; by Turab
				appUserModel.setUpdatedBy(3L);
				appUserModel.setUpdatedOn(new Date());

				updatedList.add(appUserModel);
			}
		}else{
			//user did not logged in ever, hence his last_login_time field is null,
			//so we need to check its createdOn field and if he was not logged in within 30days of account creation 
			//we need to mark it inactive
			if ( appUserModel.getCreatedOn() != null ){
				expiryCalendar.setTime(appUserModel.getCreatedOn());
				expiryCalendar.add(Calendar.DATE, 30);
				Date expiryRequiredDate = expiryCalendar.getTime();
				if ( currentDate.after(expiryRequiredDate) ){
					appUserModel.setCredentialsExpired(true);
					appUserModel.setPasswordChangeRequired(true); //FIX for bug id 2095; by Turab
					appUserModel.setUpdatedBy(3L);
					appUserModel.setUpdatedOn(new Date());

					updatedList.add(appUserModel);
				}
			}
			
		}
	}
		listSize = (long)updatedList.size();
		if ( updatedList.size() > 0 ){
			appUserDAO.saveOrUpdateCollection(updatedList);
		}
		
		return listSize;
	}

	private boolean isPasswordChangeDue (AppUserModel appUserModel){
		List<AppUserPasswordHistoryModel> historyPasswords = new ArrayList<AppUserPasswordHistoryModel>(10);
		boolean result = false;
		Calendar calendar = GregorianCalendar.getInstance();
		Date currentDate = calendar.getTime();
		Calendar changeReqCalendar = GregorianCalendar.getInstance();
		try{
			historyPasswords = (List<AppUserPasswordHistoryModel>) appUserModel.getAppUserIdAppUserPasswordHistoryModelList();
			Comparator<AppUserPasswordHistoryModel> comparator = new Comparator<AppUserPasswordHistoryModel>() {
				@Override
				public int compare(AppUserPasswordHistoryModel o1,
						AppUserPasswordHistoryModel o2) {
					return o2.getCreatedOn().compareTo(o1.getCreatedOn());
				}
			};
			
			if ( CollectionUtils.isNotEmpty(historyPasswords) ){
				//Sort the history password list on basis of createdOn field.
				java.util.Collections.sort(historyPasswords, comparator);
			}else{
				//history is empty its mean new user first time we need to check from AppUserModel's cretedOn field, 
				//if that is 30 days older and no history so we need to mark this is_change_password_required=1
				if ( appUserModel.getCreatedOn() != null ){
					changeReqCalendar.setTime(appUserModel.getCreatedOn());
					changeReqCalendar.add(Calendar.DATE, 30);
					Date changeRequiredDate = changeReqCalendar.getTime();
					if(currentDate.after(changeRequiredDate)){
						return true;
					}
				}
			}
			for ( AppUserPasswordHistoryModel historyPassword : historyPasswords ){
				if (historyPassword.getCreatedOn() != null ){
					changeReqCalendar.setTime(historyPassword.getCreatedOn());
					changeReqCalendar.add(Calendar.DATE, 30);
					Date changeRequiredDate = changeReqCalendar.getTime();
					if ( currentDate.after( changeRequiredDate )){
						result = true;
						break;
					}
					break;
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
			result = false;
		}
		
		return result;
	}

	@Override
	public List<CustomerModel> findCustomersByAppUserIds(List<Long> appUserIdsInClause) throws FrameworkCheckedException{
		return customerDAO.findCustomersByAppUserIds(appUserIdsInClause);
	}
	
	@Override
	public void updateCustomersWithAttribute(String attribute, String value, List<CustomerModel> customers){
		for (CustomerModel customerModel : customers) {
			if (attribute.equalsIgnoreCase("segmentId")){
				customerModel.setSegmentId(Long.parseLong(value));
			}
			else if(attribute.equalsIgnoreCase("accountTypeId")){
				customerModel.setCustomerAccountTypeId(Long.parseLong(value));
			}
			customerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			customerModel.setUpdatedOn(new Date());
		}
		customerDAO.saveOrUpdateCollection(customers);
	}
	
	@Override
	public List<AppUserModel> findAppUsersByAppUserIds(List<Long> appUserIdsInClause) throws FrameworkCheckedException{
		return appUserDAO.findAppUsersByAppUserIds(appUserIdsInClause);
	}
	
	@Override
	public void updateAccountWithAccountTypeId(List<Long> appUserIds, Long accountTypeId) throws FrameworkCheckedException{
		List<String> cnicList = appUserDAO.findCNICsByAppUserIds(appUserIds);
		List<String> encryptedList = new ArrayList<String>(cnicList.size());
		for(String cnic : cnicList){
			try{
//			cnic = EncryptionUtil.encryptPin(cnic);	//encrypt cnic
			encryptedList.add(cnic);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		appUserDAO.updateAccountWithAccountTypeId(encryptedList, accountTypeId);
	}

	@Override
	public List<AppUserModel> getCNICExpiryAlertUsers() throws FrameworkCheckedException{
		AppUserModel appUserModel ;
		SmsMessage smsMessage ;
		List<AppUserModel> appUserModelList = new ArrayList<AppUserModel>();
		List<Object[]> appUsers = appUserDAO.getCNICExpiryAlertUsers();
		ArrayList<SmsMessage> smsMessageList = new ArrayList<SmsMessage>();
		for ( Object [ ] objectArray : appUsers ) {
			appUserModel = new AppUserModel();
			if(objectArray[0] != null){
				appUserModel.setAppUserId((Long) objectArray[0]);
			}
			if(objectArray[1] != null){
				appUserModel.setFirstName((String) objectArray[1]);
			}
			if(objectArray[2] != null){
				appUserModel.setLastName((String) objectArray[2]);
			}
			if(objectArray[3] != null){
				appUserModel.setMobileNo((String) objectArray[3]);
			}
			if(objectArray[4] != null){
				appUserModel.setNicExpiryDate((Date) objectArray[4]);
				if (isCnicExpirySmsDue(appUserModel.getNicExpiryDate())){
					smsMessage = new SmsMessage(appUserModel.getMobileNo(), "Dear " + appUserModel.getFirstName() + " " + appUserModel.getLastName() + "\n your CNIC is going to expire next month. This sms is just a reminder to renew your CNIC.");
					smsMessageList.add(smsMessage);
					appUserModelList.add(appUserModel);
				}
			}
		}
		if(smsMessageList != null && CollectionUtils.isNotEmpty(smsMessageList)){
			smsSenderService.sendSmsList(smsMessageList);
		}
		
		return appUserModelList;
	}
	
	private boolean isCnicExpirySmsDue (Date cnicExpiryDate){
		boolean result = false;
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DATE, 30);
		Date currentDate = calendar.getTime();
		
		Calendar cnicExpiryCalendar = GregorianCalendar.getInstance();
		cnicExpiryCalendar.setTime(cnicExpiryDate);
		
		Date expiryDate = cnicExpiryCalendar.getTime();
		if(currentDate.after(expiryDate)){
			result = true;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void markCnicExpiryReminderSent(List<AppUserModel> appUserModelList) throws FrameworkCheckedException{
		List<Long> inClasue = new ArrayList<Long>(0);
		List<AppUserModel> appUsers = new ArrayList<AppUserModel>(0);
		for(AppUserModel appUserModel : appUserModelList){
			inClasue.add(appUserModel.getAppUserId());
		}
		
		if(inClasue.size() <= 1000){
			appUsers = appUserDAO.findAppUsersByAppUserIds(inClasue);	
		}else{//handle oracle limit of 1000 IN CLAUSE
			List<Long>[] chunks = this.chunks(inClasue, 1000);
			for (List<Long> inClauseByThousands : chunks) {
				appUsers.addAll(appUserDAO.findAppUsersByAppUserIds(inClauseByThousands));
			}
		}
		 //appUsers = appUserDAO.findAppUsersByAppUserIds(inClasue);
		
		 for (AppUserModel appUserModel : appUsers) {
			appUserModel.setCnicExpiryMsgSent(true);
			appUserModel.setUpdatedBy(3L); //scheduler
			appUserModel.setUpdatedOn(new Date());
		}
		appUserDAO.saveOrUpdateCollection(appUsers);
	}

	@Override
	public SearchBaseWrapper searchAppUserHistoryView(SearchBaseWrapper wrapper)
	{
		AppUserHistoryViewModel appUserHistoryViewModel = (AppUserHistoryViewModel) wrapper.getBasePersistableModel();
		CustomList<AppUserHistoryViewModel> customList = appUserHistoryViewDao.findByExample(appUserHistoryViewModel, null, wrapper.getSortingOrderMap(), PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		wrapper.setCustomList(customList);
		return wrapper;
	}

	@Override
	public Long getAppUserTypeId(Long appUserId) throws FrameworkCheckedException {
		return appUserDAO.getAppUserTypeId(appUserId);
	}
	
	
	public AppUserModel findByRetailerContractId(long retailerContactId) throws FrameworkCheckedException{
		return appUserDAO.findByRetailerContractId(retailerContactId);
	}
	
	 private List[] chunks(final List<Long> pList, final int pSize)  
    {  
        if(pList == null || pList.size() == 0 || pSize == 0) return new List[] {};  
        if(pSize < 0) return new List[] { pList };  
   
        // Calculate the number of batches  
        int numBatches = (pList.size() / pSize) + 1;  
   
        // Create a new array of Lists to hold the return value  
        List[] batches = new List[numBatches];  
   
        for(int index = 0; index < numBatches; index++)  
        {  
            int count = index + 1;  
            int fromIndex = Math.max(((count - 1) * pSize), 0);  
            int toIndex = Math.min((count * pSize), pList.size());  
            batches[index] = pList.subList(fromIndex, toIndex);  
        }  
   
        return batches;  
    }  
	 
		@Override
		public List<AppUserModel> oneTimeHsmPinGenerator(List<AppUserModel> appUserModels, Long appUserTypeId) throws FrameworkCheckedException{
			List<AppUserModel> failureList = new ArrayList<AppUserModel>();
			List<AppUserModel> successList = new ArrayList<AppUserModel>();
			
			for(AppUserModel appUserModel : appUserModels){
				try{
				///////initiate IVR call to customer to generate user pin //////////
				IvrRequestDTO ivrDTO = new IvrRequestDTO();
				if(appUserTypeId.longValue() == 2L){ //customer
                    ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
                    ivrDTO.setProductId(new Long(CommandFieldConstants.CMD_CUSTOMER_CREATE_PIN));
					this.initiateUserGeneratedPinIvrCall(ivrDTO);
				}else if (appUserTypeId.longValue() == 3L){ //retailer
					ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
					ivrDTO.setAgentMobileNo(appUserModel.getMobileNo());
                    ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
					this.initiateUserGeneratedPinIvrCall(ivrDTO);
				}else if (appUserTypeId.longValue() == 12L){ //handler
					System.out.println("%%%%%%%%%%%% Handler's MPIN needs to be regenerate mannually %%%%%%%%%%%%%%");
				}
				
				appUserModel.setPasswordHint("SUCCESS");
				successList.add(appUserModel);
				
				}catch(Exception e){
					appUserModel.setPasswordHint("FAIL");
					failureList.add(appUserModel);
					successList.add(appUserModel);
				}
		
			}
			
			appUserDAO.saveOrUpdateCollection(successList);
			
			return failureList;
		}
		
		private void initiateUserGeneratedPinIvrCall(IvrRequestDTO ivrDTO) throws FrameworkCheckedException{
			ivrDTO.setRetryCount(0);
//			ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
			try {
				ivrRequestHandler.makeIvrRequest(ivrDTO);
			} catch (Exception e) {
				e.printStackTrace();
				throw new FrameworkCheckedException(e.getLocalizedMessage());
			}
			
		}
		
		public List<AppUserModel> getAppUserOneTimeByTypeForHsmCall(Long appUserTypeId, String strDate1, String strDate2, int limit) throws FrameworkCheckedException {
			return appUserDAO.getAppUsersByTypeForHsmCall(appUserTypeId, strDate1, strDate2, limit);
		}
		



	 public void setAppUserHistoryViewDao(AppUserHistoryViewDao appUserHistoryViewDao)
	 {
		this.appUserHistoryViewDao = appUserHistoryViewDao;
	 }

	 public void setAccountHolderDAO(AccountHolderDAO accountHolderDAO)
	 {
		this.accountHolderDAO = accountHolderDAO;
	 }


	@Override
	public AppUserModel checkAppUserTypeAsWalkinCustoemr(String mobileNo)throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserDAO.checkAppUserTypeAsWalkinCustoemr(mobileNo);
		return appUserModel;	
	}

	@Override
	 public List<AppUserModel> getAppUserModelByRegionId(Long regionId) {
		 
		 return appUserDAO.getAppUserModelByRegionId(regionId);
	 } 
	@Override
	  public void checkHandlerPendingTransactions(List<HandlerSearchViewModel> handlerSearchViewModelList) throws FrameworkCheckedException{
		appUserDAO.checkHandlerPendingTransactions(handlerSearchViewModelList);
	}
	
	@Override
	public List<AppUserModel> markAppUserDormant(List<AccountModel> accountModelList) throws FrameworkCheckedException{
		List<String> cnicList = new ArrayList<String>();
		Date currentDate = new Date();
		List<Long> accountModelIds = new ArrayList<Long>();
		List<AppUserModel> appUsersToBeMarkedDormant = new ArrayList<AppUserModel>();
		List <AccountHolderModel> accountHolderModelList = new ArrayList<AccountHolderModel>();
		
		for(AccountModel accountModel : accountModelList){
			accountModelIds.add(accountModel.getAccountId());
		}
		
		if(accountModelIds.size() <= 1000){
			accountHolderModelList = accountHolderDAO.getAccountHolderModelListByAccountIds(accountModelIds);	
		}else{//handle oracle limit of 1000 IN CLAUSE
			List<Long>[] chunks = this.chunks(accountModelIds, 1000);
			for (List<Long> inClauseByThousands : chunks) {
				accountHolderModelList.addAll(accountHolderDAO.getAccountHolderModelListByAccountIds(inClauseByThousands));
			}
		}
		
		
		
		for(AccountHolderModel accountHolderModel : accountHolderModelList){//decrypt the CNIC so that get the appUserId
			if(accountHolderModel.getCnic() != null && !"".equals(accountHolderModel.getCnic())){
//				cnicList.add(EncryptionUtil.decryptPin(accountHolderModel.getCnic()));
				cnicList.add(accountHolderModel.getCnic());
			}
		}
		
		if(cnicList != null && cnicList.size() > 0){
			
			if(cnicList.size() <= 1000){
				appUsersToBeMarkedDormant = appUserDAO.getAppUserModelListByCNICs(cnicList);	
			}else{//handle oracle limit of 1000 IN CLAUSE
				List<String>[] chunks = this.strChunks(cnicList, 1000);
				for (List<String> inClauseByThousands : chunks) {
					appUsersToBeMarkedDormant.addAll(appUserDAO.getAppUserModelListByCNICs(inClauseByThousands));
				}
			}
			
			
			for(AppUserModel appUserModel : appUsersToBeMarkedDormant){
				appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT);
				appUserModel.setPrevRegistrationStateId(appUserModel.getRegistrationStateId());
				appUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.DORMANT);
				appUserModel.setUpdatedOn(currentDate);
				appUserModel.setDormantMarkedOn(currentDate);
				appUserModel.setUpdatedBy(3L); //scheduler user id
			}
			
			appUserDAO.saveOrUpdateCollection(appUsersToBeMarkedDormant);
		}
		
		return appUsersToBeMarkedDormant;
	}
	
	private List[] strChunks(final List<String> pList, final int pSize)  
    {  
        if(pList == null || pList.size() == 0 || pSize == 0) return new List[] {};  
        if(pSize < 0) return new List[] { pList };  
   
        // Calculate the number of batches  
        int numBatches = (pList.size() / pSize) + 1;  
   
        // Create a new array of Lists to hold the return value  
        List[] batches = new List[numBatches];  
   
        for(int index = 0; index < numBatches; index++)  
        {  
            int count = index + 1;  
            int fromIndex = Math.max(((count - 1) * pSize), 0);  
            int toIndex = Math.min((count * pSize), pList.size());  
            batches[index] = pList.subList(fromIndex, toIndex);  
        }  
   
        return batches;  
    }
	
	  /*
	   * validates and updates customerModel.firstDebitAvailed or firstCreditAvailed after IVR call
	   */
	  public void updateCustomerFirstDebitCredit(CustomerModel customerModel,Long registerState, boolean isDebit, Boolean ivrResponse){
		  String errorMessage = "";
		  try {
			  if (null != registerState && (registerState.equals(RegistrationStateConstants.REQUEST_RECEIVED))) {
				  if (customerModel != null && customerModel.getCustomerId() != null) {
					  if (isDebit) {
						  if (!customerModel.getFirstDebitAvailed()) {
							  if (ivrResponse == null || ivrResponse.booleanValue() == true) {
								  customerModel.setFirstDebitAvailed(true);
								  customerDAO.saveOrUpdate(customerModel);
							  }
						  } else {
							  errorMessage = "Debit transaction limit exceeded. Full customer registration required.";
							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit] " + errorMessage + " customerMobileNumber:" + customerModel.getMobileNo());
						  }
					  } else {
						  if (!customerModel.getFirstCreditAvailed()) {
							  if (ivrResponse == null || ivrResponse.booleanValue() == true) {
								  customerModel.setFirstCreditAvailed(true);
								  customerDAO.saveOrUpdate(customerModel);
							  }
						  } else {
							  errorMessage = "Credit transaction limit exceeded. Full customer registration required.";
							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit] " + errorMessage + " customerMobileNumber:" + customerModel.getMobileNo());
						  }
					  }
				  }
			  }

			  if (null != registerState && (registerState.equals(RegistrationStateConstants.BLINK_PENDING))) {
				  if (customerModel != null && customerModel.getCustomerId() != null) {
					  if (isDebit) {
						  if (!customerModel.getFirstDebitAvailed()) {
							  errorMessage = "Account is in pending state. Full customer registration required.";
							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit For Blink] " + errorMessage + " customerMobileNumber:" + customerModel.getMobileNo());
						  }
					  } else {
						  if (!customerModel.getFirstCreditAvailed()) {
							  errorMessage = "Account is in pending state. Full customer registration required.";
							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit For Blink] " + errorMessage + " customerMobileNumber:" + customerModel.getMobileNo());
						  }
					  }
				  }
			  }
			  if (null != registerState && (registerState.equals(RegistrationStateConstants.CLSPENDING))) {
				  if (customerModel != null && customerModel.getCustomerId() != null) {
					  if (isDebit) {
						  if (customerModel.getClsDebitBlock().equals("1")) {
							  errorMessage = "Your account verification is still in process. We will inform you once it is completed.";
							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit For CLS] " + errorMessage + " customerMobileNumber:" +
									  customerModel.getMobileNo());
						  }
					  } else {
						  if (customerModel.getClsCreditBlock().equals("1")) {
							  errorMessage = "Receiver Account verification is still in process.";
							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit For CLS] " + errorMessage + " customerMobileNumber:" +
									  customerModel.getMobileNo());
						  }
					  }
				  }
			  }
//			  if (null != registerState && (registerState.equals(RegistrationStateConstants.VERIFIED))) {
//				  if ((customerModel != null && customerModel.getCustomerId() != null) && customerModel.getCustomerAccountTypeId().equals
//						  (CustomerAccountTypeConstants.BLINK)) {
//					  if (isDebit) {
//						  if (customerModel.getClsBVSDebitBlock().equals("1")) {
//							  errorMessage = "Your Zindigi Account has been suspended due to biometric verification. " +
//									  "Please get biometric verification complete from nearest JS Bank branch or call 021 111 55 66 77 for further assistance.";
//							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit For Blink] " + errorMessage + " customerMobileNumber:" +
//									  customerModel.getMobileNo());
//						  }
//					  } else {
//						  if (customerModel.getClsBVSCreditBlock().equals("1")) {
//							  errorMessage = "Your Zindigi Account has been suspended due to biometric verification. " +
//									  "Please get biometric verification complete from nearest JS Bank branch or call 021 111 55 66 77 for further assistance";
//							  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit For Blink] " + errorMessage + " customerMobileNumber:" +
//									  customerModel.getMobileNo());
//						  }
//					  }
//				  }
//			  }

		  }catch(Exception e){
			  logger.error("[AppUserManagerImpl.updateCustomerFirstDebitCredit] unable to update CustomerModel for first Debit/Credit. Customer ID:" + customerModel.getCustomerId());
			  logger.error(e.getMessage(),e);
		  }
		  
		  if(!StringUtil.isNullOrEmpty(errorMessage)){
			  throw new WorkFlowException(errorMessage);
		  }
	  }


	/**
	 * @param customerModel
	 */
	public void checkIsCustomerPINGenerated(CustomerModel customerModel) {
		  
		  CustomerModel _customerModel = customerModel;
		  Boolean isMPINGenerated = null;
		  
		  logger.info("checkIsCustomerPINGenerated() Customer  "+_customerModel);
		  
		  if(_customerModel != null) {

			  if(_customerModel.getCustomerId() != null && _customerModel.getCreatedOn() == null) {
				  
				  _customerModel = this.customerDAO.findByPrimaryKey(_customerModel.getCustomerId());
			  }

			  if(_customerModel != null) {

				  isMPINGenerated = _customerModel.getIsMPINGenerated();
			  }
			  
			  logger.info("checkIsCustomerPINGenerated() Customer PIN Generated "+isMPINGenerated);
			  
			  isMPINGenerated = (isMPINGenerated == null) ? Boolean.TRUE : isMPINGenerated;
			  
			  if(!isMPINGenerated) {

				  throw new WorkFlowException(messageSource.getMessage("customer.mpin.not.generated", null, null));
			  }
		  } else {

			  logger.info("checkIsCustomerPINGenerated() Customer not found "+ _customerModel);
		  }
		  
	  }
	  
	public String getStatusDetails(Long appUserId, Date updationDate, Boolean accLocked, Boolean accEnabled) throws FrameworkCheckedException {
		String statusDetails = "";
		
		ComplaintReportModel complaintReportModel = new ComplaintReportModel();
		complaintReportModel.setInitAppUserId(appUserId);
		DateRangeHolderModel dateRangeHolderModel = null;
		CustomList<ComplaintReportModel> list = null;
		
		if(accLocked!=null && accLocked){
			complaintReportModel.setComplaintCategoryId(ComplaintsModuleConstants.CATEGORY_BLOCK);
			dateRangeHolderModel = new DateRangeHolderModel( "createdOn", updationDate, updationDate);
			list = complaintReportDAO.findByExample(complaintReportModel, null, null, dateRangeHolderModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		}else if(accEnabled!=null && !accEnabled){
			complaintReportModel.setComplaintCategoryId(ComplaintsModuleConstants.CATEGORY_DEACTIVATE);
			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<>();
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
			list = complaintReportDAO.findByExample(complaintReportModel, null, sortingOrderMap, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		}else{
			return statusDetails;
		}
		
		if(null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0){
			complaintReportModel = list.getResultsetList().get(0);
			statusDetails = "<b> On </b>"+PortalDateUtils.formatDate(complaintReportModel.getCreatedOn(), "dd/MM/yyyy hh:mm aa");
			if(!StringUtil.isNullOrEmpty(complaintReportModel.getRemarks())){
				statusDetails += " <b>Comments:</b>"+complaintReportModel.getRemarks();
			}
		}
		
		return statusDetails;

	}
	
	@Override
	public AppUserModel loadAppUser(Long primaryKey) {
	  	AppUserModel appUserModel = appUserDAO.findByPrimaryKey(primaryKey);
		if(appUserModel == null)
			return null;

		RegistrationStateModel model = appUserModel.getRegistrationStateModel();
		if(model != null) {
			appUserModel.setRegistrationStateModel((RegistrationStateModel) model.clone());
		}

	  	return appUserModel;
	}
	
	public AppUserModel loadRetailerAppUserModelByAppUserId(Long appUserId) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserDAO.loadRetailerAppUserModelByAppUserId(appUserId);
		return appUserModel;
	}
	
	public AppUserModel loadAppUserModelByUserId(String userId) throws FrameworkCheckedException {
		return appUserDAO.loadAppUserModelByUserId(userId);
	}
	
	public AppUserModel loadAppUserByMobileAndType(String mobileNo, Long ...appUserTypes) throws FrameworkCheckedException {
		
		return appUserDAO.loadAppUserByMobileAndType(mobileNo, appUserTypes);
	}



	public void setApplicantDetailDAO(ApplicantDetailDAO applicantDetailDAO)
	{
		this.applicantDetailDAO = applicantDetailDAO;
	}

	public void setIvrRequestHandler(IvrRequestHandler ivrRequestHandler) {
		this.ivrRequestHandler = ivrRequestHandler;
	}
	
	public void setComplaintReportDAO(ComplaintReportDAO complaintReportDAO) {
		this.complaintReportDAO = complaintReportDAO;
	}


	public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
		this.accountInfoDao = accountInfoDao;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public CustomerModel getCustomerModelByPK(Long customerId) {

		return this.customerDAO.findByPrimaryKey(customerId);
	}


	@Override
	public boolean isAppUserFiler(Long appUserId) {

		boolean isFiler = appUserDAO.isUserFiler(appUserId);

		return isFiler;
		
	}


	@Override
	public void updateAppUserWithAuthorization(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		AppUserVO appUserVO = (AppUserVO) baseWrapper.getBasePersistableModel();
		AppUserModel appUserModel = new AppUserModel();

		appUserModel = appUserDAO.findByPrimaryKey(Long.parseLong(appUserVO.getAppUserId()));

		appUserModel.setUpdatedOn(new Date());
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserModel.setDormancyRemovedOn(new Date());
		appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
		appUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.VERIFIED);

		appUserDAO.saveOrUpdate(appUserModel);

	}


	@Override
	public String getCustomerId(String cnic)
			throws FrameworkCheckedException {
		return  appUserDAO.getCustomerId(cnic);
	}

	@Override
	public void saveOrUpdateAppUserModels(List<AppUserModel> appUserModels)
			throws FrameworkCheckedException {
		this.appUserDAO.saveOrUpdateCollection(appUserModels);
	}

	@Override
	public RetailerContactModel getRetailerContactModelById(Long retailerContactId) throws FrameworkCheckedException {
		return retailerContactDAO.findByPrimaryKey(retailerContactId);
	}

	@Override
	public RetailerModel getRetailerModelByRetailerIdForTrx(Long retailerId) throws FrameworkCheckedException {
		return retailerContactDAO.getRetailerModelByRetailerId(retailerId);
	}

	@Override
	public DistributorModel findDistributorModelByIdForTrx(Long distributorId) throws FrameworkCheckedException {
		return retailerContactDAO.findDistributorModelById(distributorId);
	}

	@Override
	public MnoUserModel findMnoUserModelByPrimaryKey(Long mnoUserId) throws FrameworkCheckedException {
		return mnoUserDAO.findByPrimaryKey(mnoUserId);
	}

	public void setMnoUserDAO(MnoUserDAO mnoUserDAO) {
		this.mnoUserDAO = mnoUserDAO;
	}

	public void setDebitCardModelDAO(DebitCardModelDAO debitCardModelDAO) {
		this.debitCardModelDAO = debitCardModelDAO;
	}

	public void setMobileNetworkDAO(MobileNetworkDAO mobileNetworkDAO) {
		this.mobileNetworkDAO = mobileNetworkDAO;
	}
}
