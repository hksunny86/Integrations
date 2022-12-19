package com.inov8.microbank.server.service.suppliermodule;

import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.SupplierUserModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierUserFormListViewModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierUserListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.suppliermodule.SupplierUserDAO;
import com.inov8.microbank.server.dao.suppliermodule.SupplierUserListViewDAO;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class SupplierUserManagerImpl implements SupplierUserManager
{

	private SupplierUserDAO supplierUserDAO;

	private SupplierUserListViewDAO supplierUserListViewDAO;

	private AppUserDAO appUserDAO;

	private CustTransManager custTransManager;

	private AppUserManager appUserManager;
	
  
    private AppUserPartnerGroupDAO appUserPartnerGroupDAO;

	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public SearchBaseWrapper loadSupplierUser(SearchBaseWrapper searchBaseWrapper)
	{

		SupplierUserListViewModel supplierUserListViewModel = this.supplierUserListViewDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(supplierUserListViewModel);
		return searchBaseWrapper;

	}

	public BaseWrapper loadSupplierUser(BaseWrapper baseWrapper)
	{
		SupplierUserModel supplierUserModel = this.supplierUserDAO.findByPrimaryKey(baseWrapper
				.getBasePersistableModel().getPrimaryKey());
		baseWrapper.setBasePersistableModel(supplierUserModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchSupplierUser(SearchBaseWrapper searchBaseWrapper)
	{

		CustomList<SupplierUserListViewModel> list = this.supplierUserListViewDAO.findByExample(
				(SupplierUserListViewModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper createSupplierUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		SupplierUserFormListViewModel supplierUserFormListViewModel = (SupplierUserFormListViewModel) baseWrapper
				.getObject("SupplierUserFormListViewModel");
		SupplierUserModel supplierUserModel = new SupplierUserModel();
		supplierUserModel.setSupplierId(supplierUserFormListViewModel.getSupplierId());
		supplierUserModel.setCreatedOn(new Date());
		supplierUserModel.setUpdatedOn(new Date());
		supplierUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		supplierUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());

		/*if (!this.isMobileNumUnique(supplierUserFormListViewModel.getMobileNo()))
			throw new FrameworkCheckedException("MobileNumUniqueException");
		*/

		if (!this.isUserNameUnique(supplierUserFormListViewModel.getUsername()))
			throw new FrameworkCheckedException("UserNameUniqueException");

		supplierUserModel.setComments(supplierUserFormListViewModel.getComments());
		supplierUserModel.setDescription(supplierUserFormListViewModel.getDescription());
		supplierUserModel = this.supplierUserDAO.saveOrUpdate(supplierUserModel);
		supplierUserFormListViewModel.setSupplierUserId(supplierUserModel.getSupplierUserId());

		baseWrapper.putObject("SupplierUserFormListViewModel", supplierUserFormListViewModel);

		createAppUserForSupplier(baseWrapper);

		return baseWrapper;

	}

	public BaseWrapper updateSupplierUser(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		SupplierUserFormListViewModel supplierUserFormListViewModel = (SupplierUserFormListViewModel) baseWrapper
				.getObject("SupplierUserFormListViewModel");
		SupplierUserModel supplierUserModel = supplierUserDAO.findByPrimaryKey(supplierUserFormListViewModel
				.getSupplierUserId());
		SupplierUserListViewModel supplierUserListViewModel_1 = new SupplierUserListViewModel();
		supplierUserListViewModel_1 = supplierUserListViewDAO.findByPrimaryKey(supplierUserFormListViewModel
				.getSupplierUserId());
		AppUserModel appUserModel = new AppUserModel();

		appUserModel = appUserManager.getUser(String.valueOf(supplierUserListViewModel_1.getAppUserId()));
		supplierUserModel.setSupplierId(supplierUserFormListViewModel.getSupplierId());

		if (!appUserModel.getMobileNo().equals(supplierUserFormListViewModel.getMobileNo()))
		{

			/*if (!this.isMobileNumUnique(supplierUserFormListViewModel.getMobileNo()))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");

			}
			*/
		}
		
		if (!appUserModel.getUsername().equals(
				supplierUserFormListViewModel.getUsername())) {
		    if (!this.isUserNameUnique(supplierUserFormListViewModel.getUsername()))
			    throw new FrameworkCheckedException("UserNameUniqueException");
		}
		supplierUserModel.setComments(supplierUserFormListViewModel.getComments());
		supplierUserModel.setUpdatedOn(new Date());
		supplierUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		supplierUserModel.setDescription(supplierUserFormListViewModel.getDescription());
		supplierUserDAO.saveOrUpdate(supplierUserModel);

		appUserModel.setFirstName(supplierUserFormListViewModel.getFirstName());
		appUserModel.setLastName(supplierUserFormListViewModel.getLastName());

		appUserModel.setAddress1(supplierUserFormListViewModel.getAddress1());
		appUserModel.setAddress2(supplierUserFormListViewModel.getAddress2());
		appUserModel.setCity(supplierUserFormListViewModel.getCity());
		appUserModel.setState(supplierUserFormListViewModel.getState());
		appUserModel.setCountry(supplierUserFormListViewModel.getCountry());
		appUserModel.setNic(supplierUserFormListViewModel.getNic());
		appUserModel.setDob(supplierUserFormListViewModel.getDob());
		appUserModel.setMobileNo(supplierUserFormListViewModel.getMobileNo());
		appUserModel.setMotherMaidenName(supplierUserFormListViewModel.getMotherMaidenName());

		if (supplierUserFormListViewModel.getPassword() != null)
		{
			appUserModel.setPassword(EncoderUtils.encodeToSha(supplierUserFormListViewModel.getPassword()));
		}
		appUserModel.setZip(supplierUserFormListViewModel.getZip());
		appUserModel.setVerified(supplierUserFormListViewModel.getVerified() == null ? false
				: supplierUserFormListViewModel.getVerified());
		appUserModel.setAccountEnabled(supplierUserFormListViewModel.getAccountEnabled() == null ? false
				: supplierUserFormListViewModel.getAccountEnabled());
		appUserModel.setAccountExpired(supplierUserFormListViewModel.getAccountExpired() == null ? false
				: supplierUserFormListViewModel.getAccountExpired());
		appUserModel.setAccountLocked(supplierUserFormListViewModel.getAccountLocked() == null ? false
				: supplierUserFormListViewModel.getAccountLocked());
		appUserModel
				.setCredentialsExpired(supplierUserFormListViewModel.getCredentialsExpired() == null ? false
						: supplierUserFormListViewModel.getCredentialsExpired());

		appUserModel.setEmail(supplierUserFormListViewModel.getEmail());
		appUserModel.setFax(supplierUserFormListViewModel.getFax());

		appUserModel.setMobileTypeId(supplierUserFormListViewModel.getMobileTypeId());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(new Date());

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
		
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    CustomList list=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
	    if(list.getResultsetList().size()>0)
	    {
	    	
	    	appUserPartnerGroupModel=(AppUserPartnerGroupModel)list.getResultsetList().get(0);
	    	appUserPartnerGroupModel.setPartnerGroupId(supplierUserFormListViewModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }
		
		
	 	 /*AppUserAppRoleModel appUserAppRoleModel = UserUtils.determineCurrentAppUserAppRoleModel(appUserModel);
		 AccessLevelModel aLevelModel = new AccessLevelModel();
		 aLevelModel.setAccessLevelId(supplierUserFormListViewModel.getAccessLevelId());

		 appUserAppRoleModel.setAccessLevelModel(aLevelModel);
		 this.appUserAppRoleDAO.saveOrUpdate(appUserAppRoleModel);			*/

		return baseWrapper;

	}

	public BaseWrapper createAppUserForSupplier(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{

		AppUserModel appUserModel = new AppUserModel();

		SupplierUserFormListViewModel supplierUserFormListViewModel = (SupplierUserFormListViewModel) baseWrapper
				.getObject("SupplierUserFormListViewModel");

		// Checking if mobile Number is unique

		Date nowDate = new Date();

		// Populating the Customer Model

		// Populating the AppUserModel Model
		Long id;
		id = supplierUserFormListViewModel.getSupplierUserId();
		appUserModel.setFirstName(supplierUserFormListViewModel.getFirstName());
		appUserModel.setLastName(supplierUserFormListViewModel.getLastName());
		appUserModel.setAddress1(supplierUserFormListViewModel.getAddress1());
		appUserModel.setAddress2(supplierUserFormListViewModel.getAddress2());
		appUserModel.setMobileNo(supplierUserFormListViewModel.getMobileNo());
		appUserModel.setNic(supplierUserFormListViewModel.getNic());
		appUserModel.setSupplierUserId(id);
		appUserModel.setCity(supplierUserFormListViewModel.getCity());
		appUserModel.setCountry(supplierUserFormListViewModel.getCountry());
		appUserModel.setState(supplierUserFormListViewModel.getState());
		appUserModel.setDob(supplierUserFormListViewModel.getDob());
		appUserModel.setMobileTypeId(supplierUserFormListViewModel.getMobileTypeId());
		appUserModel.setUsername(supplierUserFormListViewModel.getUsername());
		appUserModel.setPassword(EncoderUtils.encodeToSha(supplierUserFormListViewModel.getPassword()));
		appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setMotherMaidenName(supplierUserFormListViewModel.getMotherMaidenName());
		appUserModel.setPasswordHint(supplierUserFormListViewModel.getPasswordHint());
		appUserModel.setZip(supplierUserFormListViewModel.getZip());
		appUserModel.setCreatedOn(nowDate);
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		appUserModel.setEmail(supplierUserFormListViewModel.getEmail());
		appUserModel.setFax(supplierUserFormListViewModel.getFax());

		appUserModel.setUpdatedOn(nowDate);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.SUPPLIER);

		appUserModel.setVerified(supplierUserFormListViewModel.getVerified() == null ? false
				: supplierUserFormListViewModel.getVerified());
		appUserModel.setAccountEnabled(supplierUserFormListViewModel.getAccountEnabled() == null ? false
				: supplierUserFormListViewModel.getAccountEnabled());
		appUserModel.setAccountExpired(supplierUserFormListViewModel.getAccountExpired() == null ? false
				: supplierUserFormListViewModel.getAccountExpired());
		appUserModel.setAccountLocked(supplierUserFormListViewModel.getAccountLocked() == null ? false
				: supplierUserFormListViewModel.getAccountLocked());
		appUserModel
				.setCredentialsExpired(supplierUserFormListViewModel.getCredentialsExpired() == null ? false
						: supplierUserFormListViewModel.getCredentialsExpired());
		appUserModel.setPasswordChangeRequired(false);
		//		appUserModel.setAccountEnabled(false);
		//		appUserModel.setAccountExpired(false);
		//		appUserModel.setAccountLocked(false);
		//		appUserModel.setCredentialsExpired(false);

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
		
		
		 AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
		    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
		    appUserPartnerGroupModel.setPartnerGroupId(supplierUserFormListViewModel.getPartnerGroupId());
		    appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setCreatedOn(new Date());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		
		/*AppRoleModel appRoleModel = new AppRoleModel() ;
	    //appRoleModel.setAppRoleId( AppRoleConstantsInterface.PRODUCT_SUPPLIER ) ;
		appRoleModel.setAppRoleId( supplierUserFormListViewModel.getAppRoleId()) ;

		
	 	 AccessLevelModel aLevelModel = new AccessLevelModel();
		 aLevelModel.setAccessLevelId(supplierUserFormListViewModel.getAccessLevelId());
		 AppUserAppRoleModel aUserAppRoleModel = new AppUserAppRoleModel(aLevelModel, appRoleModel, appUserModel);
		 this.appUserAppRoleDAO.saveOrUpdate(aUserAppRoleModel);*/
		

		return baseWrapper;
	}

	private boolean isMobileNumUnique(String mobileNo)
	{
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

	private boolean isUserNameUnique(String userName)
	{
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
			throw new FrameworkCheckedException("Userdoestnotbelongtoanypartnergroup");
		
		
	
		
		
	}
	public void setSupplierUserDAO(SupplierUserDAO supplierUserDAO)
	{
		this.supplierUserDAO = supplierUserDAO;
	}

	public void setSupplierUserListViewDAO(SupplierUserListViewDAO supplierUserListViewDAO)
	{
		this.supplierUserListViewDAO = supplierUserListViewDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO)
	{
		this.appUserDAO = appUserDAO;
	}

	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}

	public void setCustTransManager(CustTransManager custTransManager)
	{
		this.custTransManager = custTransManager;
	}

}
