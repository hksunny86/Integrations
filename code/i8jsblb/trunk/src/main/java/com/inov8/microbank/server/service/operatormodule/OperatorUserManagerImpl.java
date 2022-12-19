package com.inov8.microbank.server.service.operatormodule;

import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.OperatorUserModel;
import com.inov8.microbank.common.model.operatormodule.OperatorUserListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorUserDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorUserListViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class OperatorUserManagerImpl implements OperatorUserManager {

	private OperatorUserListViewDAO operatorUserListViewDAO;

	private OperatorUserDAO operatorUserDAO;

	private AppUserManager appUserManager;

	private AppUserDAO appUserDAO;
	
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;

	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public BaseWrapper createAppUserForOperator(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		AppUserModel appUserModel = new AppUserModel();

		OperatorUserListViewModel operatorUserListViewModel = (OperatorUserListViewModel) baseWrapper
				.getBasePersistableModel();

		// Checking if mobile Number is unique

		/*if (!this.isMobileNumUnique(operatorUserListViewModel.getMobileNo()))
			throw new FrameworkCheckedException("MobileNumUniqueException");
			*/

		if (!this
				.isUserNameUnique(operatorUserListViewModel.getUsername()))
			throw new FrameworkCheckedException("UserNameUniqueException");

		Date nowDate = new Date();

		// Populating the AppUserModel Model
		Long id;
		id = operatorUserListViewModel.getOperatorUserId();
		appUserModel.setFirstName(operatorUserListViewModel.getFirstName());
		appUserModel.setLastName(operatorUserListViewModel.getLastName());
		appUserModel.setAddress1(operatorUserListViewModel.getAddress1());
		appUserModel.setAddress2(operatorUserListViewModel.getAddress2());
		appUserModel.setMobileNo(operatorUserListViewModel.getMobileNo());
		appUserModel.setNic(operatorUserListViewModel.getNic());
		appUserModel.setUsername(operatorUserListViewModel.getUsername());
		appUserModel.setPassword(EncoderUtils
				.encodeToSha(operatorUserListViewModel.getPassword()));
		appUserModel.setOperatorUserId(id);
		appUserModel.setCity(operatorUserListViewModel.getCity());
		appUserModel.setCountry(operatorUserListViewModel.getCountry());
		appUserModel.setState(operatorUserListViewModel.getState());
		appUserModel.setDob(operatorUserListViewModel.getDob());
		appUserModel.setMobileTypeId(operatorUserListViewModel
				.getMobileTypeId());
		
		appUserModel.setPasswordHint(operatorUserListViewModel.getPasswordHint());
		appUserModel.setEmail(operatorUserListViewModel.getEmail());
		appUserModel.setFax(operatorUserListViewModel.getFax());
		appUserModel.setMotherMaidenName(operatorUserListViewModel.getMotherMaidenName());
		appUserModel.setZip(operatorUserListViewModel.getZip());

		appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setCreatedOn(nowDate);		
		appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId()) ;
		appUserModel.setUpdatedOn(nowDate);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.INOV8);
		
		appUserModel.setVerified(operatorUserListViewModel.getVerified() == null ? false : operatorUserListViewModel.getVerified() );
		appUserModel.setAccountEnabled( operatorUserListViewModel.getAccountEnabled() == null ? false : operatorUserListViewModel.getAccountEnabled() );
	    appUserModel.setAccountExpired( operatorUserListViewModel.getAccountExpired() == null ? false : operatorUserListViewModel.getAccountExpired());
	    appUserModel.setAccountLocked( operatorUserListViewModel.getAccountLocked() == null ? false : operatorUserListViewModel.getAccountLocked());
	    appUserModel.setCredentialsExpired( operatorUserListViewModel.getCredentialsExpired() == null ? false : operatorUserListViewModel.getCredentialsExpired());
	    appUserModel.setPasswordChangeRequired(false);
		
//		appUserModel.setAccountEnabled(false);
//		appUserModel.setAccountExpired(false);
//		appUserModel.setAccountLocked(false);
//		appUserModel.setCredentialsExpired(false);
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

		 AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
		    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
		    appUserPartnerGroupModel.setPartnerGroupId(operatorUserListViewModel.getPartnerGroupId());
		    appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setCreatedOn(new Date());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		    
		/*AppRoleModel appRoleModel = new AppRoleModel() ;
	    appRoleModel.setAppRoleId( AppRoleConstantsInterface.CSR ) ;

		AccessLevelModel aLevelModel = new AccessLevelModel();
		aLevelModel.setAccessLevelId(operatorUserListViewModel.getAccessLevelId());
		AppUserAppRoleModel aUserAppRoleModel = new AppUserAppRoleModel(aLevelModel, appRoleModel, appUserModel);
		this.appUserAppRoleDAO.saveOrUpdate(aUserAppRoleModel);*/
		
		return baseWrapper;
	}

	public BaseWrapper createOperatorUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		OperatorUserListViewModel operatorUserListViewModel = (OperatorUserListViewModel) baseWrapper
				.getBasePersistableModel();
        
		if(!this.isUserNameUnique(operatorUserListViewModel.getUsername())){
			throw new FrameworkCheckedException("UserNameUniqueException");
		}
		/*if(!this.isMobileNumUnique(operatorUserListViewModel.getMobileNo())){
			throw new FrameworkCheckedException("MobileNumUniqueException");
		}
		*/
		
		OperatorUserModel operatorUserModel = new OperatorUserModel();
		operatorUserModel.setOperatorId(operatorUserListViewModel.getOperatorId());
		
		operatorUserModel.setCreatedOn(new Date());
		operatorUserModel.setUpdatedOn(new Date());
		operatorUserModel.setComments(operatorUserListViewModel.getComments());
		operatorUserModel.setDescription(operatorUserListViewModel.getDescription());
		operatorUserModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		operatorUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		operatorUserModel = this.operatorUserDAO
				.saveOrUpdate(operatorUserModel);
		operatorUserListViewModel.setOperatorUserId(operatorUserModel
				.getOperatorUserId());

		baseWrapper.setBasePersistableModel(operatorUserListViewModel);
		
		createAppUserForOperator(baseWrapper);
		

		return baseWrapper;
	}

	public SearchBaseWrapper loadOperatorUser(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {

		OperatorUserListViewModel operatorUserListViewModel = this.operatorUserListViewDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(operatorUserListViewModel);
		return searchBaseWrapper;
	}

	public SearchBaseWrapper searchOperatorUser(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {

		CustomList<OperatorUserListViewModel> list = this.operatorUserListViewDAO
				.findByExample((OperatorUserListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper updateOperatorUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		OperatorUserListViewModel operatorUserListViewModel = (OperatorUserListViewModel) baseWrapper
				.getBasePersistableModel();

		OperatorUserModel operatorUserModel = operatorUserDAO
				.findByPrimaryKey(operatorUserListViewModel.getOperatorUserId());
		/*
		 * OperatorUserListViewModel bankUserListViewModel_1 = new
		 * OperatorUserListViewModel(); bankUserListViewModel_1 =
		 * operatorUserListViewDAO
		 * .findByPrimaryKey(operatorUserListViewModel.getOperatorUserId());
		 */
		AppUserModel appUserModel = new AppUserModel();

		operatorUserModel.setOperatorId(operatorUserListViewModel
				.getOperatorId());
		operatorUserModel.setComments(operatorUserListViewModel.getComments());
		operatorUserModel.setDescription(operatorUserListViewModel.getDescription());
		operatorUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		operatorUserModel.setUpdatedOn(new Date());
		operatorUserDAO.saveOrUpdate(operatorUserModel);

		appUserModel = appUserManager.getUser(String
				.valueOf(operatorUserListViewModel.getAppUserId()));
		if (!appUserModel.getMobileNo().equals(
				operatorUserListViewModel.getMobileNo())) {

			/*if (!this
					.isMobileNumUnique(operatorUserListViewModel.getMobileNo()))
				
					throw new FrameworkCheckedException(
							"MobileNumUniqueException");
							*/
				
		}
		
		if (!appUserModel.getUsername().equals(
				operatorUserListViewModel.getUsername())) {
		if (!this
				.isUserNameUnique(operatorUserListViewModel.getUsername()))
			throw new FrameworkCheckedException("UserNameUniqueException");
		}

		appUserModel.setFirstName(operatorUserListViewModel.getFirstName());
		appUserModel.setLastName(operatorUserListViewModel.getLastName());

		appUserModel.setAddress1(operatorUserListViewModel.getAddress1());
		appUserModel.setAddress2(operatorUserListViewModel.getAddress2());
		appUserModel.setCity(operatorUserListViewModel.getCity());
		appUserModel.setState(operatorUserListViewModel.getState());
		appUserModel.setCountry(operatorUserListViewModel.getCountry());
		appUserModel.setNic(operatorUserListViewModel.getNic());
		appUserModel.setDob(operatorUserListViewModel.getDob());
		appUserModel.setMobileNo(operatorUserListViewModel.getMobileNo());
		appUserModel.setMobileTypeId(operatorUserListViewModel
				.getMobileTypeId());
		appUserModel.setVerified(operatorUserListViewModel.getVerified() == null ? false : operatorUserListViewModel.getVerified() );
		appUserModel.setAccountEnabled( operatorUserListViewModel.getAccountEnabled() == null ? false : operatorUserListViewModel.getAccountEnabled() );
	    appUserModel.setAccountExpired( operatorUserListViewModel.getAccountExpired() == null ? false : operatorUserListViewModel.getAccountExpired());
	    appUserModel.setAccountLocked( operatorUserListViewModel.getAccountLocked() == null ? false : operatorUserListViewModel.getAccountLocked());
	    appUserModel.setCredentialsExpired( operatorUserListViewModel.getCredentialsExpired() == null ? false : operatorUserListViewModel.getCredentialsExpired());
	    
	    if (operatorUserListViewModel.getPassword()!=null)
		{
		appUserModel.setPassword(EncoderUtils
				.encodeToSha(operatorUserListViewModel.getPassword()));
		}
		
	    appUserModel.setPasswordHint(operatorUserListViewModel.getPasswordHint());
		appUserModel.setMotherMaidenName(operatorUserListViewModel.getMotherMaidenName());
		appUserModel.setZip(operatorUserListViewModel.getZip());
		appUserModel.setEmail(operatorUserListViewModel.getEmail());
		appUserModel.setFax(operatorUserListViewModel.getFax());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(new Date());

		// bankUserListViewModel =
		// this.bankUserListViewDAO.saveOrUpdate(bankUserListViewModel_1);
		try {
			appUserManager.saveUser(appUserModel);
		} catch (Exception e) {
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
		
		
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    CustomList list=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
	    if(list.getResultsetList().size()>0)
	    {
	    	
	    	appUserPartnerGroupModel=(AppUserPartnerGroupModel)list.getResultsetList().get(0);
	    	appUserPartnerGroupModel.setPartnerGroupId(operatorUserListViewModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }
	    
	    
	    
	    
		/*AppUserAppRoleModel appUserAppRoleModel = UserUtils.determineCurrentAppUserAppRoleModel(appUserModel);
		AccessLevelModel aLevelModel = new AccessLevelModel();
		aLevelModel.setAccessLevelId(operatorUserListViewModel.getAccessLevelId());

		appUserAppRoleModel.setAccessLevelModel(aLevelModel);
		this.appUserAppRoleDAO.saveOrUpdate(appUserAppRoleModel);		*/
		
		return baseWrapper;

	}

	private boolean isMobileNumUnique(String mobileNo) {
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.appUserDAO.countByExample(appUserModel,exampleHolder);
		if (count == 0)
			return true;
		else
			return false;
	}
	private boolean isUserNameUnique(String userName) {
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setUsername(userName);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.appUserDAO.countByExample(appUserModel,exampleHolder);
		if (count == 0)
			return true;
		else
			return false;
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
			throw new FrameworkCheckedException("User does not belong toany partner group");	
		
		
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public void setOperatorUserDAO(OperatorUserDAO operatorUserDAO) {
		this.operatorUserDAO = operatorUserDAO;
	}

	public void setOperatorUserListViewDAO(
			OperatorUserListViewDAO operatorUserListViewDAO) {
		this.operatorUserListViewDAO = operatorUserListViewDAO;
	}
}
