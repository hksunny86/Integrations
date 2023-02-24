package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.exception.UserExistsException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.retailermodule.HandlerSearchViewModel;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.List;

public class SecurityFacadeImpl implements SecurityFacade
{
  private FrameworkExceptionTranslator frameworkExceptionTranslator;
  private AppUserManager appUserManager;
  

  public SecurityFacadeImpl()
  {
  }



	@Override
	public AppUserModel loadAppUserModelByUserId(String userId) throws FrameworkCheckedException {
		return appUserManager.loadAppUserModelByUserId(userId);
	}
  
  
  	@Override
	public AppUserModel fetchL3Agent(Long retailerContactId) throws FrameworkCheckedException {
  		return this.appUserManager.fetchL3Agent(retailerContactId);
	}



	@Override  
	public AppUserModel getAppUserWithRegistrationState(String mobileNo, String cnic, Long registrationState) throws FrameworkCheckedException {
		AppUserModel appUserModel = this.appUserManager.getAppUserWithRegistrationState(mobileNo, cnic, registrationState);			
		return appUserModel;	  
	}
	
	@Override
	public AppUserModel loadAppUserByMobileAndType(String mobileNo)	throws FrameworkCheckedException {		
		AppUserModel appUserModel = appUserManager.loadAppUserByMobileAndType(mobileNo);
		return appUserModel;		
	}

	@Override
	public AppUserModel loadAppUserByMobileAndTypeForCustomer(String mobileNo) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserManager.loadAppUserByMobileAndTypeForCustomer(mobileNo);
		return appUserModel;
	}

	@Override
	public AppUserModel loadAppUserByCnicAndType(String cnic) throws FrameworkCheckedException {		
		AppUserModel appUserModel = appUserManager.loadAppUserByCnicAndType(cnic);
		return appUserModel;		
	}

	@Override
	public AppUserModel loadAppUserByEmail(String emailAddress) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserManager.loadAppUserByEmail(emailAddress);
		return appUserModel;
	}

	@Override
	public AppUserModel loadAppUserByCnicAndMobileAndAppUserType(String cnic, String mobileNo, Long appUserTypeId) throws FrameworkCheckedException {
		AppUserModel appUserModel = appUserManager.loadAppUserByCnicAndMobileAndAppUserType(cnic, mobileNo, appUserTypeId);
		return appUserModel;
	}

	public AppUserModel loadWalkinCustomerAppUserByCnic(String cnic) throws FrameworkCheckedException {		
		AppUserModel appUserModel = appUserManager.loadWalkinCustomerAppUserByCnic(cnic);
		return appUserModel;		
	}
	
  public AppUserModel getUser(String userId)
  {
    return this.appUserManager.getUser(userId);
  }

  public AppUserModel getUserByUsername(String username) throws
      UsernameNotFoundException
  {
    return this.appUserManager.getUserByUsername(username);
  }

  public List getUsers(AppUserModel user)
  {
    return this.appUserManager.getUsers(user);
  }

  public void saveUser(AppUserModel user) throws UserExistsException
  {
    this.appUserManager.saveUser(user);
  }

  public void removeUser(String userId)
  {
    this.appUserManager.removeUser(userId);
  }

  public BaseWrapper saveOrUpdateAppUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
  {
    try
    {
      return this.appUserManager.saveOrUpdateAppUser( baseWrapper ) ;
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION ) ;
    }
  }

  public SearchBaseWrapper searchAppUserByMobile(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException
  {
    try
    {
      this.appUserManager.searchAppUserByMobile( searchBaseWrapper ) ;
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
    		  FrameworkExceptionTranslator.FIND_ACTION ) ;
    }
    return searchBaseWrapper;
  }

  public BaseWrapper loadAppUser(BaseWrapper baseWrapper) throws
  FrameworkCheckedException
  {
    try
    {
      this.appUserManager.loadAppUser( baseWrapper ) ;
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
    		  FrameworkExceptionTranslator.FIND_ACTION ) ;
    }
    return baseWrapper;
  }


  public void setAppUserManager(AppUserManager appUserManager)
  {
    this.appUserManager = appUserManager;
  }

  
  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

public BaseWrapper searchCustomerByUser(BaseWrapper wrapper) throws FrameworkCheckedException {
	try
    {
      this.appUserManager.searchCustomerByUser( wrapper ) ;
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
    		  FrameworkExceptionTranslator.FIND_ACTION ) ;
    }
    return wrapper;
}

  public BaseWrapper searchRetailerByMobile(BaseWrapper wrapper) throws FrameworkCheckedException {
        try
    {
      this.appUserManager.searchRetailerByMobile( wrapper ) ;
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
    		  FrameworkExceptionTranslator.FIND_ACTION ) ;
    }
    return wrapper;
  } 
  
  public SearchBaseWrapper searchAppUser(SearchBaseWrapper wrapper)
			throws FrameworkCheckedException {
		try {
			this.appUserManager.searchAppUser(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return wrapper;
	}
  
  public SearchBaseWrapper searchAppUserForm(SearchBaseWrapper wrapper)
	throws FrameworkCheckedException {
try {
	this.appUserManager.searchAppUserForm(wrapper);
} catch (Exception ex) {
	throw this.frameworkExceptionTranslator.translate(ex,
			FrameworkExceptionTranslator.FIND_ACTION);
}
return wrapper;
}


public boolean isMobileNumberCNICUnique(BaseWrapper baseWrapper) throws FrameworkCheckedException
{
	// TODO Auto-generated method stub
	try {
		return this.appUserManager.isMobileNumberCNICUnique(baseWrapper);
	} catch (Exception ex) {
		throw this.frameworkExceptionTranslator.translate(ex,
				FrameworkExceptionTranslator.FIND_ACTION);
	}
	
	
}


	public AppUserModel getAppUserModel(AppUserModel example) {
			
		return appUserManager.getAppUserModel(example);
	}


	@Override
	public AppUserModel loadAppUserByQuery(String mobileNo, long appUserTypeId) throws FrameworkCheckedException{
		
		AppUserModel appUserModel = null;
		try
	    {
	      appUserModel = this.appUserManager.loadAppUserByQuery(mobileNo, appUserTypeId) ;
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    		  FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	    return appUserModel;
	}


	@Override
	public AppUserModel loadAppUserByMobileByQuery(String mobileNo)
			throws FrameworkCheckedException {
		AppUserModel appUserModel = null;
		try
	    {
	      appUserModel = this.appUserManager.loadAppUserByMobileByQuery(mobileNo) ;
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    		  FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	    return appUserModel;
	}

	@Override
	public AppUserModel loadAppUserByCNIC(String cnic) throws FrameworkCheckedException {
		AppUserModel appUserModel = null;
		try{
	      appUserModel = this.appUserManager.loadAppUserByCNIC(cnic);
	    }catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	    return appUserModel;
	}
	@Override 
	public BaseWrapper closeAgentAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
	    try
	    {
	      return this.appUserManager.closeAgentAccount(baseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION ) ;
	    }
	  }

	@Override
	public SearchBaseWrapper loadMobileHistory(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			wrapper = this.appUserManager.loadMobileHistory(wrapper);
	    }catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	    return wrapper;
	}

	@Override
	public BaseWrapper updateMobileNo(BaseWrapper wrapper) throws FrameworkCheckedException {
		try{
			wrapper = this.appUserManager.updateMobileNo(wrapper);
	    }catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION ) ;
	    }
	    return wrapper;
	}

	@Override
	public List<String> getAppUserPreviousThreePasswordsByAppUserId(
			String username) throws FrameworkCheckedException {
		return this.appUserManager.getAppUserPreviousThreePasswordsByAppUserId(username);
	}


	@Override
	public void saveAppUserPasswordHistory(
			AppUserPasswordHistoryModel appUserPasswordHistory)
			throws FrameworkCheckedException {
		this.appUserManager.saveAppUserPasswordHistory(appUserPasswordHistory);
		
	}


	@Override
	public List<AppUserModel> getAllUsersForPasswordChangeRequired()
			throws FrameworkCheckedException {
		
		return this.appUserManager.getAllUsersForPasswordChangeRequired();
	}


	@Override
	public Long markPasswordChangeRequired(List<AppUserModel> users)
			throws FrameworkCheckedException {
		return this.appUserManager.markPasswordChangeRequired(users);
	}


	@Override
	public List<AppUserModel> getAllUsersForAccountInactive()
			throws FrameworkCheckedException {
		return this.appUserManager.getAllUsersForAccountInactive();
	}


	@Override
	public Long markAppUserAccountInactive(List<AppUserModel> users)
			throws FrameworkCheckedException {
		return this.appUserManager.markAppUserAccountInactive(users);
	}

	@Override
	public void updateCustomerFirstDebitCredit(CustomerModel customerModel,Long registerState, boolean isDebit, Boolean ivrResponse)
			throws FrameworkCheckedException {
		this.appUserManager.updateCustomerFirstDebitCredit(customerModel,registerState, isDebit, ivrResponse);
	}

	@Override
	public List<CustomerModel> findCustomersByAppUserIds(
			List<Long> appUserIdsInClause) throws FrameworkCheckedException {
		try{
			return this.appUserManager.findCustomersByAppUserIds(appUserIdsInClause);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	}

	@Override
	public void updateCustomersWithAttribute(String attribue, String value,
			List<CustomerModel> customers) throws FrameworkCheckedException {
		try{
			this.appUserManager.updateCustomersWithAttribute(attribue, value, customers);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION ) ;
	    }
	}

	@Override
	public List<AppUserModel> findAppUsersByAppUserIds(
			List<Long> appUserIdsInClause) throws FrameworkCheckedException {
		try{
			return this.appUserManager.findAppUsersByAppUserIds(appUserIdsInClause);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	}

	@Override
	public void updateAccountWithAccountTypeId(List<Long> appUserIds,
			Long accountTypeId) throws FrameworkCheckedException {
		try{
			this.appUserManager.updateAccountWithAccountTypeId(appUserIds, accountTypeId);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION ) ;
	    }
		
	}

	@Override
	public SearchBaseWrapper findAppUsersByAppUserTypeId(long appUserTypeId)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AppUserModel> getCNICExpiryAlertUsers()
			throws FrameworkCheckedException {
		try{
			return this.appUserManager.getCNICExpiryAlertUsers();
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	}

	@Override
	public void markCnicExpiryReminderSent(List<AppUserModel> appUserModelList)
			throws FrameworkCheckedException {
		try{
			this.appUserManager.markCnicExpiryReminderSent(appUserModelList);
		}catch(Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}



	@Override
	public Long getAppUserTypeId(Long appUserId) throws FrameworkCheckedException {
		try{
			return this.appUserManager.getAppUserTypeId(appUserId);
		}catch(Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	@Override
	public SearchBaseWrapper searchAppUserHistoryView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.appUserManager.searchAppUserHistoryView(searchBaseWrapper);
		}
		catch(Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}



	@Override
	public AppUserModel checkAppUserTypeAsWalkinCustoemr(String mobileNo)
			throws FrameworkCheckedException {
		try
		{
			return this.appUserManager.checkAppUserTypeAsWalkinCustoemr(mobileNo);
		}
		catch(Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	

	@Override
	public List<AppUserModel> getAppUserModelByRegionId(Long regionId) {

		return appUserManager.getAppUserModelByRegionId(regionId);
	}



	@Override
	public BaseWrapper closeHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		return appUserManager.closeHandlerAccount(baseWrapper);
	}



	@Override
	public void checkHandlerPendingTransactions(
			List<HandlerSearchViewModel> handlerSearchViewModelList)
			throws FrameworkCheckedException {
		
		appUserManager.checkHandlerPendingTransactions(handlerSearchViewModelList);
	}



	@Override
	public List<Object[]> getAppUserCNICsToMarkDormant()
			throws FrameworkCheckedException {
		try{
			return this.appUserManager.getAppUserCNICsToMarkDormant();
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	}



	@Override
	public List<AppUserModel> markAppUserDormant(List<AccountModel> accountModelList)
			throws FrameworkCheckedException {
		try{
			return this.appUserManager.markAppUserDormant(accountModelList);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION ) ;
	    }
	}



	@Override
	public AppUserModel loadAppUserByMobileAndType(String mobileNo,	Long... appUserTypes) throws FrameworkCheckedException {
		
		return appUserManager.loadAppUserByMobileAndType(mobileNo, appUserTypes);
	}

	@Override
	public List<AppUserModel> oneTimeHsmPinGenerator(
			List<AppUserModel> appUserModels, Long appUserTypeId) throws FrameworkCheckedException {
		try{
			return appUserManager.oneTimeHsmPinGenerator(appUserModels, appUserTypeId);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	}



	@Override
	public List<AppUserModel> getAppUserOneTimeByTypeForHsmCall(Long appUserTypeId, String strDate1, String strDate2, int limit) throws FrameworkCheckedException {
		try{
			return appUserManager.getAppUserOneTimeByTypeForHsmCall(appUserTypeId, strDate1, strDate2, limit);
		}catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
	    }
	}

	@Override
	public BaseWrapper updateCNIC(BaseWrapper wrapper)
			throws FrameworkCheckedException {
		try{
			wrapper = this.appUserManager.updateCNIC(wrapper);
	    }catch (Exception ex){
	    	throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.INSERT_ACTION ) ;
	    }
	    return wrapper;
	}



	@Override
	public void checkIsCustomerPINGenerated(CustomerModel customerModel) {
		
		this.appUserManager.checkIsCustomerPINGenerated(customerModel);
	}

	@Override
	public String getStatusDetails(Long appUserId, Date updationDate, Boolean accLocked, Boolean accEnabled) throws FrameworkCheckedException {
		return this.appUserManager.getStatusDetails(appUserId, updationDate, accLocked, accEnabled);
	}

	@Override
	public AppUserModel findByRetailerContractId(long retailerContactId) throws FrameworkCheckedException {
		return appUserManager.findByRetailerContractId(retailerContactId);
	}

	@Override
	public CustomerModel getCustomerModelByPK(Long customerId) {
		return appUserManager.getCustomerModelByPK(customerId);
	}
	
	@Override
	public AppUserModel loadRetailerAppUserModelByAppUserId(Long appUserId) throws FrameworkCheckedException {
		AppUserModel appUserModel = null;
		try{
			appUserModel= appUserManager.loadRetailerAppUserModelByAppUserId(appUserId);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION ) ;
		}
		return appUserModel;
	}
	
  	@Override
	public AppUserModel loadAppUser(Long primaryKey) {
		return appUserManager.loadAppUser(primaryKey);
	}



	@Override
	public boolean isAppUserFiler(Long appUserId) {
		// TODO Auto-generated method stub
		return appUserManager.isAppUserFiler(appUserId);
	}



	@Override
	public void updateAppUserWithAuthorization(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try{
			appUserManager.updateAppUserWithAuthorization(baseWrapper);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.UPDATE_ACTION ) ;
		}
		
	}



	@Override
	public String getCustomerId(String cnic)
			throws FrameworkCheckedException {
		try{
			return appUserManager.getCustomerId(cnic);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION) ;
		}
		 
	}

	@Override
	public void saveOrUpdateAppUserModels(List<AppUserModel> appUserModels)
			throws FrameworkCheckedException {
		try{
			 appUserManager.saveOrUpdateAppUserModels(appUserModels);
		}catch (Exception ex){
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION) ;
		}
	}

	@Override
	public RetailerContactModel getRetailerContactModelById(Long retailerContactId) throws FrameworkCheckedException {
		return appUserManager.getRetailerContactModelById(retailerContactId);
	}

	@Override
	public RetailerModel getRetailerModelByRetailerIdForTrx(Long retailerId) throws FrameworkCheckedException {
		return appUserManager.getRetailerModelByRetailerIdForTrx(retailerId);
	}

	@Override
	public DistributorModel findDistributorModelByIdForTrx(Long distributorId) throws FrameworkCheckedException {
		return appUserManager.findDistributorModelByIdForTrx(distributorId);
	}

	@Override
	public MnoUserModel findMnoUserModelByPrimaryKey(Long mnoUserId) throws FrameworkCheckedException {
		return appUserManager.findMnoUserModelByPrimaryKey(mnoUserId);
	}
	@Override
	public AppUserModel loadAppUserModelByCustomerId(Long customerId) {
		return appUserManager.loadAppUserModelByCustomerId(customerId);
	}

	@Override
	public List<AppUserModel> loadAppUserMarkAmaAccountDebitBlock() throws FrameworkCheckedException {
		return  appUserManager.loadAppUserMarkAmaAccountDebitBlock();
	}

	@Override
	public List<AppUserModel> loadPendingAccountOpeningAppUserModel() throws FrameworkCheckedException {
		return appUserManager.loadPendingAccountOpeningAppUserModel();
	}

	@Override
	public AppUserModel getAppUserWithRegistrationStates(String mobileNo, String cnic, Long ...registrationStates) throws FrameworkCheckedException {
		return appUserManager.getAppUserWithRegistrationStates(mobileNo, cnic, registrationStates);
	}

	@Override
	public AppUserModel loadAppUserByCnic256(String shaCnic) throws FrameworkCheckedException {
		return appUserManager.loadAppUserByCnic256(shaCnic);
	}
}
