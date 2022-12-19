package com.inov8.microbank.server.service.bankmodule;

import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.BankUserModel;
import com.inov8.microbank.common.model.bankmodule.BankUserListViewFormModel;
import com.inov8.microbank.common.model.bankmodule.BankUserListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.bankmodule.BankUserDAO;
import com.inov8.microbank.server.dao.bankmodule.BankUserListViewDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class BankUserManagerImpl implements BankUserManager {

	private AppUserDAO appUserDAO;

	private BankUserDAO bankUserDAO;

	private AppUserManager appUserManager;

	private BankUserListViewDAO bankUserListViewDAO;

	private CustTransManager custTransManager;

	private UserDeviceAccountsDAO userDeviceAccountsDAO;

	private GoldenNosDAO goldenNosDAO;

	private OracleSequenceGeneratorJdbcDAO sequenceGenerator;
	

	
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;

	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}



	public void setCustTransManager(CustTransManager custTransManager) {
		this.custTransManager = custTransManager;
	}

	public void setGoldenNosDAO(GoldenNosDAO goldenNosDAO) {
		this.goldenNosDAO = goldenNosDAO;
	}

	public void setSequenceGenerator(
			OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setUserDeviceAccountsDAO(
			UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public SearchBaseWrapper loadBankUser(SearchBaseWrapper searchBaseWrapper) {
		BankUserListViewModel bankUserListViewModel = this.bankUserListViewDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		searchBaseWrapper.setBasePersistableModel(bankUserListViewModel);
		return searchBaseWrapper;
	}

	public BaseWrapper loadBankUser(BaseWrapper baseWrapper) {
		BankUserModel bankUserModel = this.bankUserDAO
				.findByPrimaryKey(baseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		baseWrapper.setBasePersistableModel(bankUserModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchBankUser(SearchBaseWrapper searchBaseWrapper) {

		CustomList<BankUserListViewModel> list = this.bankUserListViewDAO
				.findByExample((BankUserListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}

	public BaseWrapper createBankUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		BankUserListViewFormModel bankUserListViewFormModel = (BankUserListViewFormModel) baseWrapper
				.getObject("BankUserListViewFormModel");
		BankUserModel bankUserModel = new BankUserModel();
		bankUserModel.setBankId(bankUserListViewFormModel.getBankId());
		bankUserModel.setCreatedOn(new Date());
		bankUserModel.setUpdatedOn(new Date());
		
		/////////////////////Added By Maqsood Shahzad (10th Feb 2009)
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(bankUserListViewFormModel.getMobileNo());
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
		BaseWrapper wrapper = new BaseWrapperImpl();
		wrapper.setBasePersistableModel(appUserModel);
		/*if (!this.appUserManager.isMobileNumberUnique(wrapper))
			throw new FrameworkCheckedException("MobileNumUniqueException");
			*/
		
		if (!this.isUsernameUnique(bankUserListViewFormModel.getUsername()))
			throw new FrameworkCheckedException("UsernameUniqueException");
		
		bankUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		bankUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        bankUserModel.setComments(bankUserListViewFormModel.getComments());
        bankUserModel.setDescription(bankUserListViewFormModel.getDescription());
       
        	bankUserModel = this.bankUserDAO.saveOrUpdate(bankUserModel);
       
        
		bankUserListViewFormModel.setBankUserId(bankUserModel.getBankUserId());
		
		baseWrapper.putObject("BankUserListViewFormModel",
				bankUserListViewFormModel);
		createAppUserForBank(baseWrapper);
		
		return baseWrapper;
	}

	public BaseWrapper updateBankUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		BankUserListViewFormModel bankUserListViewFormModel = (BankUserListViewFormModel) baseWrapper
				.getObject("BankUserListViewFormModel");
		BankUserModel bankUserModel = bankUserDAO
				.findByPrimaryKey(bankUserListViewFormModel.getBankUserId());
		BankUserListViewModel bankUserListViewModel_1 = new BankUserListViewModel();
		bankUserListViewModel_1 = bankUserListViewDAO
				.findByPrimaryKey(bankUserListViewFormModel.getBankUserId());
		AppUserModel appUserModel = new AppUserModel();
		//appUserModel.setAppUserId(bankUserListViewModel_1.getAppUserId());
		appUserModel = appUserManager.getUser(String
				.valueOf(bankUserListViewModel_1.getAppUserId()));
		
		AppUserModel tempAppUserModel = new AppUserModel();
		tempAppUserModel.setMobileNo(bankUserListViewFormModel.getMobileNo());
		tempAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
		BaseWrapper wrapper = new BaseWrapperImpl();
		wrapper.setBasePersistableModel(tempAppUserModel);
		
		if (!appUserModel.getMobileNo().equals(
				bankUserListViewFormModel.getMobileNo())) {
			/*if (!this.appUserManager.isMobileNumberUnique(wrapper))
					throw new FrameworkCheckedException("MobileNumUniqueException");
					*/

		}
		
		if (!appUserModel.getUsername().equals(
				bankUserListViewFormModel.getUsername())) {
		    if (!this.isUsernameUnique(bankUserListViewFormModel.getUsername()))
			    throw new FrameworkCheckedException("UsernameUniqueException");
		}
		
		bankUserModel.setComments(bankUserListViewFormModel.getComments());
        bankUserModel.setDescription(bankUserListViewFormModel.getDescription());
		bankUserModel.setBankId(bankUserListViewFormModel.getBankId());
		bankUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		//bankUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		bankUserModel.setUpdatedOn(new Date());
		bankUserDAO.saveOrUpdate(bankUserModel);

		
		appUserModel.setFirstName(bankUserListViewFormModel.getFirstName());
		appUserModel.setLastName(bankUserListViewFormModel.getLastName());

		appUserModel.setAddress1(bankUserListViewFormModel.getAddress1());
		appUserModel.setAddress2(bankUserListViewFormModel.getAddress2());
		appUserModel.setCity(bankUserListViewFormModel.getCity());
		appUserModel.setState(bankUserListViewFormModel.getState());
		appUserModel.setCountry(bankUserListViewFormModel.getCountry());
		appUserModel.setNic(bankUserListViewFormModel.getNic());
		appUserModel.setDob(bankUserListViewFormModel.getDob());
		
		appUserModel.setEmail(bankUserListViewFormModel.getEmail());
		appUserModel.setZip(bankUserListViewFormModel.getZip());
		appUserModel.setFax(bankUserListViewFormModel.getFax());
		appUserModel.setMotherMaidenName(bankUserListViewFormModel.getMotherMaidenName());
		
		appUserModel.setVerified(bankUserListViewFormModel.getVerified() == null ? false : bankUserListViewFormModel.getVerified() );
		appUserModel.setAccountEnabled( bankUserListViewFormModel.getAccountEnabled() == null ? false : bankUserListViewFormModel.getAccountEnabled() );
	    appUserModel.setAccountExpired( bankUserListViewFormModel.getAccountExpired() == null ? false : bankUserListViewFormModel.getAccountExpired());
	    appUserModel.setAccountLocked( bankUserListViewFormModel.getAccountLocked() == null ? false : bankUserListViewFormModel.getAccountLocked());
	    appUserModel.setCredentialsExpired( bankUserListViewFormModel.getCredentialsExpired() == null ? false : bankUserListViewFormModel.getCredentialsExpired());
	    
	    if (bankUserListViewFormModel.getPassword()!=null)
		{
		appUserModel.setPassword(EncoderUtils
				.encodeToSha(bankUserListViewFormModel.getPassword()));
		}
		
		appUserModel.setMobileNo(bankUserListViewFormModel.getMobileNo());
		appUserModel.setMobileTypeId(bankUserListViewFormModel
				.getMobileTypeId());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(new Date());

		//bankUserListViewModel = this.bankUserListViewDAO.saveOrUpdate(bankUserListViewModel_1);
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
	    	appUserPartnerGroupModel.setPartnerGroupId(bankUserListViewFormModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }
	    
	    
	    
		
		/*AppUserAppRoleModel appUserAppRoleModel = UserUtils.determineCurrentAppUserAppRoleModel(appUserModel);
		AccessLevelModel aLevelModel = new AccessLevelModel();
		aLevelModel.setAccessLevelId(bankUserListViewFormModel.getAccessLevelId());

		appUserAppRoleModel.setAccessLevelModel(aLevelModel);
		this.appUserAppRoleDAO.saveOrUpdate(appUserAppRoleModel);		
*/		
		
		return baseWrapper;
	}

	public BaseWrapper createAppUserForBank(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		AppUserModel appUserModel = new AppUserModel();
		//CustomerModel customerModel = new CustomerModel();
		//UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		BankUserListViewFormModel bankUserListViewFormModel = (BankUserListViewFormModel) baseWrapper
				.getObject("BankUserListViewFormModel");

		// Checking if mobile Number is unique

		

		Date nowDate = new Date();

		// Populating the Customer Model

		/*customerModel.setRegister(true);
		customerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		customerModel.setCreatedOn(nowDate);
		customerModel.setUpdatedOn(nowDate);*/

		// Populating the AppUserModel Model
		Long id;
		id = bankUserListViewFormModel.getBankUserId();
		appUserModel.setFirstName(bankUserListViewFormModel.getFirstName());
		appUserModel.setLastName(bankUserListViewFormModel.getLastName());
		appUserModel.setAddress1(bankUserListViewFormModel.getAddress1());
		appUserModel.setAddress2(bankUserListViewFormModel.getAddress2());
		appUserModel.setMobileNo(bankUserListViewFormModel.getMobileNo());
		appUserModel.setNic(bankUserListViewFormModel.getNic());
		appUserModel.setBankUserId(id);
		appUserModel.setCity(bankUserListViewFormModel.getCity());
		appUserModel.setCountry(bankUserListViewFormModel.getCountry());
		appUserModel.setState(bankUserListViewFormModel.getState());
		appUserModel.setDob(bankUserListViewFormModel.getDob());
		appUserModel.setMobileTypeId(bankUserListViewFormModel
				.getMobileTypeId());
		appUserModel.setMotherMaidenName(bankUserListViewFormModel.getMotherMaidenName());
		appUserModel.setPasswordHint(bankUserListViewFormModel.getPasswordHint());
		appUserModel.setEmail(bankUserListViewFormModel.getEmail());
		appUserModel.setFax(bankUserListViewFormModel.getFax());
		appUserModel.setZip(bankUserListViewFormModel.getZip());
		appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setCreatedOn(nowDate);
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(nowDate);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
		

	    appUserModel.setVerified(bankUserListViewFormModel.getVerified() == null ? false : bankUserListViewFormModel.getVerified() );	
		appUserModel.setAccountEnabled( bankUserListViewFormModel.getAccountEnabled() == null ? false : bankUserListViewFormModel.getAccountEnabled() );
	    appUserModel.setAccountExpired( bankUserListViewFormModel.getAccountExpired() == null ? false : bankUserListViewFormModel.getAccountExpired());
	    appUserModel.setAccountLocked( bankUserListViewFormModel.getAccountLocked() == null ? false : bankUserListViewFormModel.getAccountLocked());
	    appUserModel.setCredentialsExpired( bankUserListViewFormModel.getCredentialsExpired() == null ? false : bankUserListViewFormModel.getCredentialsExpired());
	    appUserModel.setPasswordChangeRequired(false);
			
		//appUserModel.setAccountEnabled(false);
		//appUserModel.setAccountExpired(false);
		//appUserModel.setAccountLocked(false);
		//appUserModel.setCredentialsExpired(false);

		// Populating the UserDeviceAccountsModel

		/*userDeviceAccountsModel.setAccountEnabled(true);
		 userDeviceAccountsModel.setAccountExpired(false);
		 userDeviceAccountsModel.setAccountLocked(false);
		 userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		 userDeviceAccountsModel.setCreatedOn(nowDate);
		 userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		 userDeviceAccountsModel.setUpdatedOn(nowDate);
		 userDeviceAccountsModel.setPinChangeRequired(true);
		 userDeviceAccountsModel.setDeviceTypeId( DeviceTypeConstantsInterface.WEB); 
		 userDeviceAccountsModel.setCredentialsExpired(false);*/

		//Here creating the mfsId/usernaem , pin/password
//		String mfsId = computeMfsId();
//		String username = mfsId;

//		String randomPin = RandomUtils.generateRandom(4, false, true);
//		String password = randomPin;

		// Saving the CustomerModel

		/*baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(customerModel);
		baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
		customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
*/
		// Saving the AppUserModel

		baseWrapper = new BaseWrapperImpl();
		BaseWrapper tempBaseWrapper = new BaseWrapperImpl();
		//appUserModel.setCustomerId(customerModel.getCustomerId());
		appUserModel.setUsername(bankUserListViewFormModel.getUsername());
		appUserModel.setPassword(EncoderUtils.encodeToSha(bankUserListViewFormModel.getPassword()));
		tempBaseWrapper.setBasePersistableModel(appUserModel);
		try
		{
			
		 this.appUserManager.saveUser(appUserModel);
		
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

//		AppRoleModel appRoleModel = new AppRoleModel() ;
//	    appRoleModel.setAppRoleId( AppRoleConstantsInterface.PAYMENT_GATEWAY) ;
	    
	    AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    appUserPartnerGroupModel.setPartnerGroupId(bankUserListViewFormModel.getPartnerGroupId());
	    appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setCreatedOn(new Date());
	    appUserPartnerGroupModel.setUpdatedOn(new Date());
	    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		/*AccessLevelModel aLevelModel = new AccessLevelModel();
		aLevelModel.setAccessLevelId(bankUserListViewFormModel.getAccessLevelId());
		AppUserAppRoleModel aUserAppRoleModel = new AppUserAppRoleModel(aLevelModel, appRoleModel, appUserModel);
		this.appUserAppRoleDAO.saveOrUpdate(aUserAppRoleModel);
*/		
		
		// Saving the UserDeviceAccountsModel

		/*userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
		 userDeviceAccountsModel.setUserId(mfsId);
		 userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		 userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);*/

		return baseWrapper;
	}
/*
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
			goldenNosModel.setGoldenNumber(nextLongValue);
			int countAppUser = this.appUserDAO.countByExample(appUserModel);
			int countGoldenNos = this.goldenNosDAO
					.countByExample(goldenNosModel);
			if (countAppUser == 0 && countGoldenNos == 0) {
				flag = false;
			}
		}

		return String.valueOf(nextLongValue);
	}
*/
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
		appUserModel.setUsername(username);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);

		CustomList list = this.appUserDAO.findByExample(appUserModel);
		int  recordCount = appUserDAO.countByExample(appUserModel,exampleHolder);
		int size = list.getResultsetList().size();
		if (size == 0 || recordCount==0)
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
			throw new FrameworkCheckedException("User doest not belong to any partner group");	
		
		
	}
	
	
	
	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public void setBankUserDAO(BankUserDAO bankUserDAO) {
		this.bankUserDAO = bankUserDAO;
	}

	public void setBankUserListViewDAO(BankUserListViewDAO bankUserListViewDAO) {
		this.bankUserListViewDAO = bankUserListViewDAO;
	}

}
