package com.inov8.microbank.server.service.agenthierarchy;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.model.portal.salesmodule.SalesTeamComissionViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.RetailerContactAddressesDAO;
import com.inov8.microbank.server.dao.agenthierarchymodule.*;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.common.AreaDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorLevelDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoDAO;
import com.inov8.microbank.server.dao.portal.concernmodule.ConcernPartnerDAO;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.PartnerGroupDAO;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.PartnerPermissionViewDAO;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogDAO;
import com.inov8.microbank.server.dao.retailermodule.BulkFranchiseDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;
import com.inov8.microbank.server.dao.salesmodule.SalesTeamCommissionDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.util.ConfigurationContainer;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.hibernate.criterion.MatchMode;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.Timestamp;
import java.util.*;

public class AgentHierarchyManagerImpl implements AgentHierarchyManager {

	private final static Log logger= LogFactory.getLog(AgentHierarchyManagerImpl.class); 
	private RegionDAO regionDAO;
	private DistributorDAO distributorDAO;
	private RegionalHierarchyDAO regionalHierarchyDAO;
	private SalesHierarchyDAO salesHierarchyDAO;
	private DistRegHierAssociationDAO distRegHierAssociationDAO;
	private BankDAO bankDAO;
	private MnoDAO mnoDAO;
	private AreaLevelDAO areaLevelDAO; 
	private DistributorLevelDAO distributorLevelDAO;
	private RetailerDAO retailerDAO;
	private AreaDAO areaDAO;
	private ProductCatalogDAO productCatalogDAO;
	private PartnerGroupDAO partnerGroupDAO;
	private RetailerContactDAO retailerContactDAO;
	private OracleSequenceGeneratorJdbcDAO retailerSequenceGenerator;
	private OracleSequenceGeneratorJdbcDAO allpaySequenceGenerator;
	private AddressDAO addressDAO;
	private RetailerContactAddressesDAO retailerContactAddressesDAO;
	private AppUserDAO appUserDAO;
	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;
	private RetailerContactSearchViewDAO retailerContactSearchViewDAO;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private AccountInfoDAO accountInfoDao;
	private EncryptionHandler encryptionHandler;
	private PartnerDAO partnerDAO;
	private ConcernPartnerDAO concernPartnerDAO;
	private SmsSender smsSender;
	private ActionLogManager actionLogManager;
	private LinkPaymentModeManager	linkPaymentModeManager;
	private BulkAgentUploadDAO bulkAgentUploadDAO;
	private PartnerPermissionViewDAO partnerPermissionViewDAO;
	private FranchiseCreator franchiseCreator;
	private BulkFranchiseDAO bulkFranchiseDAO;
	private FinancialIntegrationManager financialIntegrationManager;
	private LinkPaymentModeDAO linkPaymentModeDAO;
	private FinancialInstitution olaVeriflyFinancialInstitution;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private SalesTeamCommissionDAO salesTeamCommissionDAO;
	protected ConfigurationContainer keysObj;
	
	/*private SalesHierarchyHistoryDAO	salesHierarchyHistoryDAO;*/
	
	public SearchBaseWrapper findSaleUsersByUltimateParentSaleUserId(Long ultimateParentSaleUserId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.findSaleUsersByUltimateParentSaleUserId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<SalesHierarchyModel> salesHierarchyModelList = this.salesHierarchyDAO.findSaleUsersByUltimateParentSaleUserId(ultimateParentSaleUserId);
		CustomList<SalesHierarchyModel> customList = new CustomList<SalesHierarchyModel>();		
		customList.setResultsetList(salesHierarchyModelList);
		searchBaseWrapper.setCustomList(customList);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.findSaleUsersByUltimateParentSaleUserId Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findUltimateSaleUsers() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.findUltimateSaleUsers Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<SalesHierarchyModel> salesHierarchyModelList = this.salesHierarchyDAO.findUltimateSaleUsers();
		CustomList<SalesHierarchyModel> customList = new CustomList<SalesHierarchyModel>();		
		customList.setResultsetList(salesHierarchyModelList);
		searchBaseWrapper.setCustomList(customList);
		
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.findUltimateSaleUsers Ends");
		}
		return searchBaseWrapper;
	}
	
	public void saveOrUpdateBulkAgentReport(BulkAgentUploadReportModel bulkAgentUploadReportModel) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateBulkAgentReport Starts");
		}
		try
		{
			bulkAgentUploadDAO.saveOrUpdate(bulkAgentUploadReportModel);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateBulkAgentReport Starts");
		}
	}
	
	public Boolean isExistByAccountNo(AccountInfoModel model) throws Exception 
	{
    	
    	AccountInfoModel accountInfoModel = new AccountInfoModel();
    	accountInfoModel.setAccountNo(model.getAccountNo());
        encryptionHandler.encrypt(accountInfoModel);
        
    	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
    	exampleHolder.setMatchMode(MatchMode.EXACT);
    	
        CustomList<AccountInfoModel> customList = accountInfoDao.findByExample(accountInfoModel, null, null, exampleHolder);

        return !(customList.getResultsetList().isEmpty());
    }
	
	public SearchBaseWrapper findAllRetailers() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRetailers Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		RetailerModel retailerModel = new RetailerModel();
		retailerModel.setActive(null);
		CustomList<RetailerModel> retailersModelList = this.retailerDAO.findByExample(retailerModel);
		
		searchBaseWrapper.setCustomList(retailersModelList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRetailers Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findRegHierAssociatedData() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegHierAssociatedData Starts");
		}
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<DistRegHierAssociationModel> distRegHierAssociationModelList = this.distRegHierAssociationDAO.findAll();
		
		searchBaseWrapper.setCustomList(distRegHierAssociationModelList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegHierAssociatedData Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findAllRegions() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRegions Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<RegionModel> regionModelModelList = this.regionDAO.findAll();
		
		searchBaseWrapper.setCustomList(regionModelModelList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRegions Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findAllSaleUsers() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllSaleUsers Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<SalesHierarchyModel> saleUserModelList = this.salesHierarchyDAO.findAll();
		
		searchBaseWrapper.setCustomList(saleUserModelList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllSaleUsers Ends");
		}
		return searchBaseWrapper;
	}
	
	public PartnerGroupModel findPartnerGroupByAppUserId(long appUserId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAppUserByRetailerContactId Starts");
		}
		AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();  
		appUserPartnerGroupModel.setAppUserId(appUserId);
		PartnerGroupModel partnerGroupModel = null;
		CustomList<AppUserPartnerGroupModel> customAppUserPartnerGroupModelList = appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		if(customAppUserPartnerGroupModelList.getResultsetList() != null && customAppUserPartnerGroupModelList.getResultsetList().size() > 0)
		{
			appUserPartnerGroupModel = customAppUserPartnerGroupModelList.getResultsetList().get(0);
			partnerGroupModel = new PartnerGroupModel();
			partnerGroupModel = partnerGroupDAO.findByPrimaryKey(appUserPartnerGroupModel.getPartnerGroupId());
			
			
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAppUserByRetailerContactId Ends");
		}
		return partnerGroupModel;
	}
	
	public AppUserModel findAppUserByRetailerContactId(long retailerContactId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAppUserByRetailerContactId Starts");
		}
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setRetailerContactId(retailerContactId);
		CustomList<AppUserModel> customAppUserModelList = appUserDAO.findByExample(appUserModel);
		if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
		{
			appUserModel = customAppUserModelList.getResultsetList().get(0);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAppUserByRetailerContactId Ends");
		}
		return appUserModel;
	}
	
	
	public DistributorLevelModel findDistributorLevelById(long distriubtorLevelId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaById Starts");
		}
		
		DistributorLevelModel distributorLevelModel = this.distributorLevelDAO.findByPrimaryKey(distriubtorLevelId);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaById Ends");
		}
		return distributorLevelModel;
	}
	
	public SearchBaseWrapper findAllRetailerContacts() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRetailerContacts Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		CustomList<RetailerContactModel> retailerContactModelList = this.retailerContactDAO.findByExample(retailerContactModel);
		
		searchBaseWrapper.setCustomList(retailerContactModelList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRetailerContacts Ends");
		}
		return searchBaseWrapper;
	}	
	
	public SearchBaseWrapper findAllRegionalHierarchyAssociations() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRegionalHierarchyAssociations Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<DistRegHierAssociationModel> distRegHierAssociationCustomList = this.distRegHierAssociationDAO.findAll();
		
		searchBaseWrapper.setCustomList(distRegHierAssociationCustomList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllRegionalHierarchyAssociations Ends");
		}
		return searchBaseWrapper;
	}
	
	
	
	public AreaModel findAreaById(long areaId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaById Starts");
		}
		
		AreaModel areaModel = areaDAO.findByPrimaryKey(areaId);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaById Ends");
		}
		return areaModel;
	}
	
	public RetailerContactModel findRetailerContactById(long retailerContactId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRetailerContactById Starts");
		}
		
		RetailerContactModel retailerContactModel = retailerContactDAO.findByPrimaryKey(retailerContactId);
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRetailerContactById Ends");
		}
		return retailerContactModel;
	}
	
	public List<RetailerContactModel> findChildRetailerContactsById(long retailerContactId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.findChildRetailerContactsById Starts");
		}
		List<RetailerContactModel> list = null;
		list = retailerContactDAO.findChildRetailerContactsById(retailerContactId);
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findChildRetailerContactsById Ends");
		}
		return list;
	}
	
	public RetailerContactListViewFormModel populateRetailerContactModel(RetailerContactModel retailerContactModel , RetailerContactListViewFormModel agentFormModel)
	{
		
		agentFormModel.setVersionNo(retailerContactModel.getVersionNo());
		agentFormModel.setRetailerContactCreatedOn(retailerContactModel.getCreatedOn());
		agentFormModel.setRetailerContactCreatedBy(retailerContactModel.getCreatedBy());
		agentFormModel.setRetailerContactUpdatedOn(retailerContactModel.getUpdatedOn());
		agentFormModel.setRetailerContactUpdatedBy(retailerContactModel.getUpdatedBy());
		
		agentFormModel.setRetailerId(retailerContactModel.getRetailerId());
		agentFormModel.setDistributorLevelId(String.valueOf(retailerContactModel.getDistributorLevelId()));
		agentFormModel.setAreaId(retailerContactModel.getAreaId());
		
		AreaModel areaModel = areaDAO.findByPrimaryKey(retailerContactModel.getAreaId());
		agentFormModel.setAreaName(areaModel.getName());
		
		agentFormModel.setNatureOfBusiness(retailerContactModel.getNatureOfBusinessId());
		
		if(retailerContactModel.getProductCatalogId() != null)
		{
			agentFormModel.setProductCatalogId(String.valueOf(retailerContactModel.getProductCatalogId()));
		}
		
		agentFormModel.setApplicationNo(retailerContactModel.getApplicationNo());
		agentFormModel.setActive(retailerContactModel.getActive());
		agentFormModel.setSmsToAgent(retailerContactModel.getSmsToAgent());
		agentFormModel.setSmsToHandler(retailerContactModel.getSmsToHandler());
		agentFormModel.setHead(retailerContactModel.getHead());
		agentFormModel.setRso(retailerContactModel.getRso());
		agentFormModel.setDescription(retailerContactModel.getDescription());
		agentFormModel.setComments(retailerContactModel.getComments());
		agentFormModel.setZongMsisdn(retailerContactModel.getMobileNo());// 1258
		agentFormModel.setAccountOpeningDate(retailerContactModel.getAccountOpeningDate());
		agentFormModel.setNtnNo(retailerContactModel.getNtn());
		agentFormModel.setContactNo(retailerContactModel.getContactNo());
		agentFormModel.setLandLineNo(retailerContactModel.getLandLineNo());
		//agentFormModel.setMobileNo(retailerContactModel.getMobileNo());
		agentFormModel.setBusinessName(retailerContactModel.getBusinessName());
		agentFormModel.setName(retailerContactModel.getName());
		if(retailerContactModel.getParentRetailerContactModelId() == null)
		{
			agentFormModel.setParentRetailerContactId("-1");
		}
		else
		{
			agentFormModel.setParentRetailerContactId(String.valueOf(retailerContactModel.getParentRetailerContactModelId()));
		}
		
		return agentFormModel;
	}
	
	public RetailerContactListViewFormModel populateAddressModel(AddressModel addressModel , RetailerContactListViewFormModel agentFormModel)
	{
		agentFormModel.setShopNo(addressModel.getHouseNo());
		agentFormModel.setMarketPlaza(addressModel.getStreetNo());
		agentFormModel.setDistrictTehsilTown(addressModel.getDistrictId());
		agentFormModel.setCityVillage(addressModel.getCityId());
		agentFormModel.setPostOffice(addressModel.getPostalOfficeId());
		agentFormModel.setNtnNumber(addressModel.getNtn());
		agentFormModel.setNearestLandmark(addressModel.getNearestLandMark());
		return agentFormModel;
	}
	
	public RetailerContactListViewFormModel populateRetailerContactAddressModel(RetailerContactAddressesModel retailerContactAddressesModel , RetailerContactListViewFormModel agentFormModel)
	{
		agentFormModel.setRetailerContactAddressId(retailerContactAddressesModel.getRetailerContactAddressesId());
		agentFormModel.setAddressId(retailerContactAddressesModel.getAddressId());
		return agentFormModel;
	}
	
	public RetailerContactListViewFormModel populateAppUserForRetailerContact(AppUserModel appUserModel , RetailerContactListViewFormModel agentFormModel)
	{
		agentFormModel.setAppUserId(appUserModel.getAppUserId());
		agentFormModel.setFirstName(appUserModel.getFirstName());
		agentFormModel.setLastName(appUserModel.getLastName());
		agentFormModel.setUsername(appUserModel.getUsername());
		agentFormModel.setPassword(appUserModel.getPassword());
		agentFormModel.setConfirmPassword(appUserModel.getPassword());
		agentFormModel.setMobileNo(appUserModel.getMobileNo());
		agentFormModel.setEmail(appUserModel.getEmail());
		agentFormModel.setCnicNo(appUserModel.getNic());
		agentFormModel.setCnicExpiryDate(appUserModel.getNicExpiryDate());
		
		agentFormModel.setVerified(true);		// TODO why these following fields
		agentFormModel.setAccountEnabled(true);
		agentFormModel.setAccountExpired(appUserModel.getAccountExpired() == null ? false: appUserModel.getAccountExpired());
		agentFormModel.setAccountLocked(appUserModel.getAccountLocked() == null ? false: appUserModel.getAccountLocked());
		agentFormModel.setCredentialsExpired(appUserModel.getCredentialsExpired() == null ? false: appUserModel.getCredentialsExpired());
		agentFormModel.setPasswordChangeRequired( appUserModel.getPasswordChangeRequired() == null ? false: appUserModel.getPasswordChangeRequired());
		
		agentFormModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
		if(appUserModel.getClosedByAppUserModel()!=null){
			agentFormModel.setClosedBy(appUserModel.getClosedByAppUserModel().getAppUserId().toString());
		 }
		agentFormModel.setClosedOn(appUserModel.getClosedOn());
		agentFormModel.setClosingComments(appUserModel.getClosingComments()); 
   	 
		agentFormModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled()); 
	   	 if(appUserModel.getSettledByAppUserModel()!=null){
	   		agentFormModel.setSettledBy(appUserModel.getSettledByAppUserModel().getAppUserId().toString());
	   	  }
	   	agentFormModel.setSettledOn(appUserModel.getSettledOn());
	   	agentFormModel.setSettlementComments(appUserModel.getSettlementComments());
		
		agentFormModel.setMobileTypeId(Constants.MOBILE_TYPE_POSTPAID);
		
		agentFormModel.setAppUserVersionNo(appUserModel.getVersionNo());
		agentFormModel.setAppUserCreatedBy(appUserModel.getCreatedBy());
		agentFormModel.setAppUserCreatedOn(appUserModel.getCreatedOn());
		agentFormModel.setAppUserUpdatedBy(appUserModel.getUpdatedBy());
		agentFormModel.setAppUserUpdatedOn(appUserModel.getUpdatedOn());
		
		return agentFormModel;
	}
	
	public RetailerContactListViewFormModel populateAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel , RetailerContactListViewFormModel agentFormModel)
	{
		agentFormModel.setAppUserPartnerGroupId(appUserPartnerGroupModel.getAppUserPartnerGroupId());
		agentFormModel.setPartnerGroupId(appUserPartnerGroupModel.getPartnerGroupId());
		
		agentFormModel.setAppUserPartnerGroupVersionNo(appUserPartnerGroupModel.getVersionNo());
		agentFormModel.setAppUserPartnerGroupCreatedBy(appUserPartnerGroupModel.getCreatedBy());
		agentFormModel.setAppUserPartnerGroupCreatedOn(appUserPartnerGroupModel.getCreatedOn());
		agentFormModel.setAppUserPartnerGroupUpdatedBy(appUserPartnerGroupModel.getUpdatedBy());
		agentFormModel.setAppUserPartnerGroupUpdatedOn(appUserPartnerGroupModel.getUpdatedOn());
		
		return agentFormModel;
	}

	public void deleteAgent(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.deleteAgent Starts");
		}
		try
		{
		
			RetailerContactListViewFormModel agentFormModel = (RetailerContactListViewFormModel) searchBaseWrapper.getObject("agentFormModel");
		
		RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
		retailerContactAddressesModel.setRetailerContactId(agentFormModel.getRetailerContactId());
		
		CustomList<RetailerContactAddressesModel> customRetailerContactAddressesModelList = this.retailerContactAddressesDAO.findByExample(retailerContactAddressesModel);
		if(customRetailerContactAddressesModelList.getResultsetList() != null && customRetailerContactAddressesModelList.getResultsetList().size() > 0)
		{
			retailerContactAddressesModel = customRetailerContactAddressesModelList.getResultsetList().get(0);
			this.retailerContactAddressesDAO.delete(retailerContactAddressesModel);
			addressDAO.deleteByPrimaryKey(retailerContactAddressesModel.getAddressId());
		}
		
		AppUserModel appUserModel = new AppUserModel();  
		appUserModel.setRetailerContactId(agentFormModel.getRetailerContactId());
		
		CustomList<AppUserModel> customAppUserModelList = appUserDAO.findByExample(appUserModel);
		if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
		{
			appUserModel = customAppUserModelList.getResultsetList().get(0);
			
			AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();  
			appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
					
			CustomList<AppUserPartnerGroupModel> customAppUserPartnerGroupModelList = appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
			if(customAppUserPartnerGroupModelList.getResultsetList() != null && customAppUserPartnerGroupModelList.getResultsetList().size() > 0)
			{
				appUserPartnerGroupModel = customAppUserPartnerGroupModelList.getResultsetList().get(0);
				appUserPartnerGroupDAO.delete(appUserPartnerGroupModel);
			}
			appUserDAO.delete(appUserModel);
		}
		
		retailerContactDAO.deleteByPrimaryKey(agentFormModel.getRetailerContactId());
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.deleteAgent Ends");
		}
	}
	
	
	
	public SearchBaseWrapper findAgent(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAgent Starts");
		}
		
		RetailerContactListViewFormModel agentFormModel = (RetailerContactListViewFormModel) searchBaseWrapper.getObject("agentFormModel");
		
		RetailerContactModel retailerContactModel = retailerContactDAO.findByPrimaryKey(agentFormModel.getRetailerContactId());
		agentFormModel =  populateRetailerContactModel(retailerContactModel, agentFormModel);
		
		SalesHierarchyModel salesHierarchyModel = this.salesHierarchyDAO.findByPrimaryKey(retailerContactModel.getSalesHierarchyId());
		agentFormModel.setSaleUserId(salesHierarchyModel.getBankUserId());
		
		AppUserModel saleUserModel = this.appUserDAO.findByPrimaryKey(salesHierarchyModel.getBankUserId());
		String name = saleUserModel.getLastName()!=null?(" " + saleUserModel.getLastName()):"";
		name = saleUserModel.getFirstName() + name;
		String empId = (saleUserModel.getEmployeeId() == null) ? "" : " - " + saleUserModel.getEmployeeId();
		agentFormModel.setSaleUserName(name  + " [ " + salesHierarchyModel.getRoleTitle() + empId + " ]");
		
		RetailerContactAddressesModel retailerContactAddressesModel  = this.retailerContactAddressesDAO.findBusinessTypeRetailerContactAddress(agentFormModel.getRetailerContactId());
		agentFormModel = populateRetailerContactAddressModel(retailerContactAddressesModel, agentFormModel);
		
		AddressModel addressModel = addressDAO.getAddress(agentFormModel.getAddressId());
		
		/*AddressModel addressModel = new AddressModel();
		addressModel.setAddressId(retailerContactAddressesModel.getAddressId());
		CustomList<AddressModel> customAddressModelList = addressDAO.findByExample(addressModel);
		addressModel = customAddressModelList.getResultsetList().get(0);*/
		
		agentFormModel = populateAddressModel(addressModel, agentFormModel);
		
		AppUserModel appUserModel = new AppUserModel();  
		appUserModel.setRetailerContactId(agentFormModel.getRetailerContactId());
		
		CustomList<AppUserModel> customAppUserModelList = appUserDAO.findByExample(appUserModel);
		appUserModel = customAppUserModelList.getResultsetList().get(0);
		agentFormModel = populateAppUserForRetailerContact(appUserModel, agentFormModel);
		
		AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();  
		appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
				
		CustomList<AppUserPartnerGroupModel> customAppUserPartnerGroupModelList = appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		
		if(customAppUserPartnerGroupModelList.getResultsetList() != null && customAppUserPartnerGroupModelList.getResultsetList().size() > 0)
		{
			appUserPartnerGroupModel = customAppUserPartnerGroupModelList.getResultsetList().get(0);
			agentFormModel = populateAppUserPartnerGroupModel(appUserPartnerGroupModel, agentFormModel);
		}
		
		
		searchBaseWrapper.putObject("agentFormModel", agentFormModel);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAgent Ends");
		}
		return searchBaseWrapper;
	}

	@Override
	public AppUserModel loadHeadRetailerContactAppUser( Long retailerId ) throws FrameworkCheckedException
	{
	    AppUserModel appUserModel = appUserDAO.loadHeadRetailerContactAppUser( retailerId );
	    return appUserModel;
	}

	private RetailerContactModel extractRetailerContactModel(RetailerContactModel retailerContactModel, RetailerContactListViewFormModel agentFormModel)
	{
		if(!agentFormModel.isEditMode())
		{
			retailerContactModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			retailerContactModel.setCreatedOn(new Date());
		}
		else
		{
			retailerContactModel.setCreatedOn(agentFormModel.getRetailerContactCreatedOn());
			retailerContactModel.setCreatedBy(agentFormModel.getRetailerContactCreatedBy());
			retailerContactModel.setVersionNo(agentFormModel.getVersionNo());
		}
		
		retailerContactModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		retailerContactModel.setUpdatedOn(new Date());
		retailerContactModel.setRetailerId(agentFormModel.getRetailerId());
		retailerContactModel.setDistributorLevelId(Long.valueOf(agentFormModel.getDistributorLevelId()));
		retailerContactModel.setAreaId(agentFormModel.getAreaId());
		retailerContactModel.setNatureOfBusinessId(agentFormModel.getNatureOfBusiness());
		
		/*if ( agentFormModel.getAccountType() != null ){
			OlaCustomerAccountTypeModel accountTypeModel = new OlaCustomerAccountTypeModel();
			retailerContactModel.setOlaCustomerAccountTypeModelId(agentFormModel.getAccountType());
			accountTypeModel.setCustomerAccountTypeId(agentFormModel.getAccountType());
			retailerContactModel.setOlaCustomerAccountTypeModel(accountTypeModel);
		}*/
		
		if(agentFormModel.getProductCatalogId() != null)
		{
			retailerContactModel.setProductCatalogId(Long.valueOf(agentFormModel.getProductCatalogId()));
		}
		retailerContactModel.setActive(agentFormModel.getActive());
		retailerContactModel.setSmsToAgent(agentFormModel.getSmsToAgent());
		retailerContactModel.setSmsToHandler(agentFormModel.getSmsToHandler());
		retailerContactModel.setRso(false); // Temporarily commented as RSO value is not provided from screen  
		retailerContactModel.setHead(agentFormModel.getHead());
		if(agentFormModel.getDescription() != null)
		{
			retailerContactModel.setDescription(agentFormModel.getDescription().trim());
		}
		if(agentFormModel.getComments() != null)
		{
			retailerContactModel.setComments(agentFormModel.getComments().trim());
		}
		retailerContactModel.setZongMsisdn(agentFormModel.getMobileNo());
		retailerContactModel.setAccountOpeningDate(agentFormModel.getAccountOpeningDate());
		retailerContactModel.setNtn(agentFormModel.getNtnNo());
		retailerContactModel.setContactNo(agentFormModel.getContactNo());
		String name = agentFormModel.getLastName()!=null?(" " + agentFormModel.getLastName()):"";
		name = agentFormModel.getFirstName() + name;
		retailerContactModel.setName(name);
		if(agentFormModel.getParentRetailerContactId() != null)
		{
			retailerContactModel.setParentRetailerContactModelId(Long.valueOf(agentFormModel.getParentRetailerContactId()));
		}
		retailerContactModel.setBalance(Double.valueOf(0));		 //  TODO 	This field need to be remove
		retailerContactModel.setActive(true);
		return retailerContactModel;
	}
	
	private AddressModel extractAddressModel(AddressModel addressModel , RetailerContactListViewFormModel agentFormModel) 
	{
		
		addressModel.setHouseNo(agentFormModel.getShopNo().trim());
		addressModel.setStreetNo(agentFormModel.getMarketPlaza().trim());
		addressModel.setDistrictId(agentFormModel.getDistrictTehsilTown());
		addressModel.setCityId(agentFormModel.getCityVillage());
		addressModel.setPostalOfficeId(agentFormModel.getPostOffice());
		addressModel.setNtn(agentFormModel.getNtnNumber());
		addressModel.setNearestLandMark(agentFormModel.getNearestLandmark().trim());
		return addressModel;
	}
	
	private RetailerContactAddressesModel extractRetailerContactAddressModel(RetailerContactListViewFormModel agentFormModel) 
	{
		RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
		retailerContactAddressesModel.setRetailerContactAddressesId(agentFormModel.getRetailerContactAddressId());
		retailerContactAddressesModel.setAddressTypeId(Constants.ADDRESS_TYPE_BUSINESS);
		retailerContactAddressesModel.setRetailerContactId(agentFormModel.getRetailerContactId());
		return retailerContactAddressesModel;
	}
	
	public boolean isRetailerNameUnique(String retialerName) throws FrameworkCheckedException
	{
		RetailerModel retailerModel = new RetailerModel();
		retailerModel.setName(retialerName);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.retailerDAO.countByExample(retailerModel, exampleHolder);
		if (count == 0)
			return true;
		else
			return false;
	}
	
	public boolean isMobileNumUnique(String mobileNo, Long appUserId) throws FrameworkCheckedException
	{
		boolean returnValue = Boolean.TRUE;
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		
		CustomList<AppUserModel> customAppUserModelList =  this.appUserDAO.findByExample(appUserModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
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
			customAppUserModelList =  this.appUserDAO.findByExample(appUserModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
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
  			customAppUserModelList =  this.appUserDAO.findByExample(appUserModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
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

	public boolean isMSISDNUnique(String msisdn, Long retailerContactId)
	{
		boolean returnValue = Boolean.TRUE;
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		retailerContactModel.setZongMsisdn(msisdn);
		CustomList<RetailerContactModel> customRetailerContactModelList =  this.retailerContactDAO.findByExample(retailerContactModel);
		if(customRetailerContactModelList.getResultsetList() != null && customRetailerContactModelList.getResultsetList().size() > 0)
		{
			if(retailerContactId != null && retailerContactId > 0)
			{
				for(RetailerContactModel model:customRetailerContactModelList.getResultsetList())
				{
					if(!model.getRetailerContactId().equals(retailerContactId))
					{
						returnValue = Boolean.FALSE;
						break;
					}
				}
			}
			else
			{
				returnValue = Boolean.FALSE;
			}
		}
		return returnValue;
	}
	
	public boolean isUserNameUnique(String userName, Long appUserId) 
	{
		
		boolean returnValue = Boolean.TRUE;
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setUsername(userName);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		CustomList<AppUserModel> customAppUserModelList = this.appUserDAO.findByExample(appUserModel, null, null, exampleHolder);
		if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
		{
			if(appUserId != null && appUserId > 0)
			{
				for(AppUserModel model:customAppUserModelList.getResultsetList())
				{
					if(!model.getAppUserId().equals(appUserId))
					{
						returnValue = Boolean.FALSE;
						break;
					}
				}
			}
			else
			{
				returnValue = Boolean.FALSE;
			}
		}
		
		if(returnValue)
		{
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customAppUserModelList = this.appUserDAO.findByExample(appUserModel, null, null, exampleHolder);
			if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
			{
				if(appUserId != null && appUserId > 0)
				{
					for(AppUserModel model:customAppUserModelList.getResultsetList())
					{
						if(!model.getAppUserId().equals(appUserId))
						{
							returnValue = Boolean.FALSE;
							break;
						}
					}
				}
				else
				{
					returnValue = Boolean.FALSE;
				}
			}	
		}
		return returnValue;	
		
	}
	
	public AppUserModel extractAppUserForRetailerContact(AppUserModel appUserModel, RetailerContactListViewFormModel retailerContactListViewFormModel) throws FrameworkCheckedException 
	{

		appUserModel.setUsername(retailerContactListViewFormModel.getUsername());
		appUserModel.setMobileNo(retailerContactListViewFormModel.getMobileNo());
		
		appUserModel.setNic(retailerContactListViewFormModel.getCnicNo());
		if(!appUserModel.getNicExpiryDate().equals(retailerContactListViewFormModel.getCnicExpiryDate())){
			//CNIC expiry date is changed, set is_cnic_expirty_msg_sent=0
			appUserModel.setCnicExpiryMsgSent(Boolean.FALSE);
		}
		appUserModel.setNicExpiryDate(retailerContactListViewFormModel.getCnicExpiryDate());
		if(retailerContactListViewFormModel.getEmail() != null)
		{
			appUserModel.setEmail(retailerContactListViewFormModel.getEmail().trim());
		}
		
		if(retailerContactListViewFormModel.isEditMode())
		{
			appUserModel.setPassword(retailerContactListViewFormModel.getPassword());
		}
		else
		{
			appUserModel.setPassword(EncoderUtils.encodeToSha(retailerContactListViewFormModel.getPassword()));
		}
		
		appUserModel.setUpdatedOn(new Date());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setPasswordChangeRequired(Boolean.TRUE);
		
		return appUserModel;
	}
	
	private AppUserPartnerGroupModel extractAppUserPartnerGroupModel(RetailerContactListViewFormModel retailerContactListViewFormModel)
	{
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserPartnerGroupId(retailerContactListViewFormModel.getAppUserPartnerGroupId());
	    appUserPartnerGroupModel.setPartnerGroupId(retailerContactListViewFormModel.getPartnerGroupId());
	    
	    if(!retailerContactListViewFormModel.isEditMode())
		{
	    	appUserPartnerGroupModel.setUpdatedOn(new Date());
	    	appUserPartnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
	    	appUserPartnerGroupModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
	    	appUserPartnerGroupModel.setCreatedOn(new Date());
		}
		else
		{
			appUserPartnerGroupModel.setUpdatedOn(new Date());
			appUserPartnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			appUserPartnerGroupModel.setCreatedOn(retailerContactListViewFormModel.getAppUserPartnerGroupCreatedOn());
			appUserPartnerGroupModel.setCreatedBy(retailerContactListViewFormModel.getAppUserPartnerGroupCreatedBy());
			appUserPartnerGroupModel.setVersionNo(retailerContactListViewFormModel.getAppUserPartnerGroupVersionNo());
		}
	    
		return appUserPartnerGroupModel;
	}
	
	private UserDeviceAccountsModel extractUserDeviceAccount(RetailerContactListViewFormModel agentFormModel)
	{
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		
		if(agentFormModel.isEditMode())
		{
			userDeviceAccountsModel.setAppUserId(agentFormModel.getAppUserId());
			CustomList<UserDeviceAccountsModel> userDeviceAccountsModelCustomList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
			if(userDeviceAccountsModelCustomList.getResultsetList() != null && userDeviceAccountsModelCustomList.getResultsetList().size() > 0)
			{
				userDeviceAccountsModel = userDeviceAccountsModelCustomList.getResultsetList().get(0);
			}
			userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			userDeviceAccountsModel.setUpdatedOn(new Date());
		}
		else
		{
			userDeviceAccountsModel.setAccountEnabled(true);
			userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			userDeviceAccountsModel.setCreatedOn(new Date());
			userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			userDeviceAccountsModel.setUpdatedOn(new Date());
			userDeviceAccountsModel.setAppUserId(agentFormModel.getAppUserId());
			String userId = String.valueOf(this.allpaySequenceGenerator.nextLongValue());
			userDeviceAccountsModel.setUserId(userId);
		}
		
		/*if (agentFormModel.getHead().equals(Boolean.TRUE)) 
		{
			userDeviceAccountsModel.setPasswordChangeRequired(true);
			userDeviceAccountsModel.setPinChangeRequired(false);
			if(!agentFormModel.isEditMode())
			{
				String randomPin = RandomUtils.generateRandom(8, false, true);
				userDeviceAccountsModel.setPassword(EncoderUtils.encodeToSha(randomPin));
			}
		}
		else
		{*/
			userDeviceAccountsModel.setPinChangeRequired(true);
			userDeviceAccountsModel.setPasswordChangeRequired(false);
			if(!agentFormModel.isEditMode())
			{
				String randomPin = RandomUtils.generateRandom(4, false, true);
				userDeviceAccountsModel.setPin(EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin));
			}
		/*}*/
		
		if( !GenericValidator.isBlankOrNull(agentFormModel.getProductCatalogId()) )
		{
			userDeviceAccountsModel.setProdCatalogId(Long.valueOf(agentFormModel.getProductCatalogId())); 
		}
		userDeviceAccountsModel.setAccountExpired(agentFormModel.getAccountExpired() == null ? false : new Boolean(agentFormModel.getAccountExpired()));
		userDeviceAccountsModel.setAccountLocked(agentFormModel.getAccountLocked() == null ? false : new Boolean(agentFormModel.getAccountLocked()));
		userDeviceAccountsModel.setCredentialsExpired(agentFormModel.getCredentialsExpired() == null ? false : new Boolean(agentFormModel.getCredentialsExpired()));
		userDeviceAccountsModel.setCommissioned(agentFormModel.getCommissioned() == null ? false : new Boolean(agentFormModel.getCommissioned()));
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		return userDeviceAccountsModel;
	}
	
	
	
	public BaseWrapper saveOrUpdateAgent(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateAgent Starts");
		}
		
		try
		{
		
		RetailerContactListViewFormModel agentFormModel = (RetailerContactListViewFormModel) baseWrapper.getObject("agentFormModel");
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		RetailerContactModel retailerContactModel = this.retailerContactDAO.findByPrimaryKey(agentFormModel.getRetailerContactId());
		
		/*		retailerContactModel = extractRetailerContactModel(retailerContactModel, agentFormModel);
		
 atif		RetailerModel retailerModel = retailerDAO.findByPrimaryKey(agentFormModel.getRetailerId());
		if(retailerModel != null){
			retailerModel.setDistributorId(Long.parseLong(agentFormModel.getDistributorId()));
			retailerDAO.saveOrUpdate(retailerModel);
		}

		SalesHierarchyModel salesHierarchyModel = this.salesHierarchyDAO.findSaleUserByBankUserId(73454LagentFormModel.getSaleUserId());
		
		retailerContactModel.setSalesHierarchyId(salesHierarchyModel.getSalesHierarchyId());
		
		retailerContactDAO.saveOrUpdate(retailerContactModel);
*/		
/*	by atif	RetailerContactAddressesModel retailerContactAddressesModel = retailerContactAddressesDAO.findBusinessTypeRetailerContactAddress(agentFormModel.getRetailerContactId());
		AddressModel addressModel = new AddressModel();
		addressModel.setAddressId(retailerContactAddressesModel.getAddressId());
		CustomList<AddressModel> customAddressModelList = addressDAO.findByExample(addressModel);
		addressModel = customAddressModelList.getResultsetList().get(0);
		
		if(retailerContactAddressesModel != null){ //this check added by turab
			AddressModel addressModel = addressDAO.findAddressById(retailerContactAddressesModel.getAddressId());
			addressModel = extractAddressModel(addressModel, agentFormModel);
			addressModel.setAddressId(retailerContactAddressesModel.getAddressId());
			addressDAO.saveOrUpdate(addressModel);
			agentFormModel.setAddressId(retailerContactAddressesModel.getAddressId());
		}		
		AppUserModel appUserModel = new AppUserModel();  
		appUserModel.setRetailerContactId(agentFormModel.getRetailerContactId());
		CustomList<AppUserModel> customAppUserModelList = appUserDAO.findByExample(appUserModel);
		appUserModel = customAppUserModelList.getResultsetList().get(0);
		
		appUserModel = extractAppUserForRetailerContact(appUserModel, agentFormModel);	
		
		appUserDAO.saveOrUpdate(appUserModel);
*/		
		AppUserPartnerGroupModel appUserPartnerGroupModel = extractAppUserPartnerGroupModel(agentFormModel);
		appUserPartnerGroupModel.setAppUserId(agentFormModel.getAppUserId());
		appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		agentFormModel.setAppUserPartnerGroupId(appUserPartnerGroupModel.getAppUserPartnerGroupId());
		agentFormModel.setAppUserPartnerGroupVersionNo(appUserPartnerGroupModel.getVersionNo());
		agentFormModel.setAppUserPartnerGroupCreatedBy(appUserPartnerGroupModel.getCreatedBy());
		agentFormModel.setAppUserPartnerGroupCreatedOn(appUserPartnerGroupModel.getCreatedOn());
		agentFormModel.setAppUserPartnerGroupUpdatedBy(appUserPartnerGroupModel.getUpdatedBy());
		agentFormModel.setAppUserPartnerGroupUpdatedOn(appUserPartnerGroupModel.getUpdatedOn());
		
		UserDeviceAccountsModel userDeviceAccountsModel = userDeviceAccountsModel = this.userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(agentFormModel.getAppUserId());
	/*	atif		agentFormModel.setUserDeviceAccountUserId(userDeviceAccountsModel.getUserId());
		
		userDeviceAccountsModel.setProdCatalogId(Long.parseLong(agentFormModel.getProductCatalogId()));
		this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
*/		
		SmartMoneyAccountModel smaModel = this.smartMoneyAccountManager.getSMALinkedWithCoreAccount(agentFormModel.getRetailerContactId());
		agentFormModel.setCoreAccountLinked(smaModel != null);
		
		
		if(!agentFormModel.isEditMode()) 
		{
	        BankModel bankModel = getOlaBankModel();
	        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
	        baseWrapperBank.setBasePersistableModel(bankModel);
	        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

	        smaModel = new SmartMoneyAccountModel();
			smaModel.setRetailerContactId(retailerContactModel.getRetailerContactId()) ;
			
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(smaModel);
			bWrapper = this.smartMoneyAccountManager.loadOLASmartMoneyAccount(bWrapper);
			smaModel = (SmartMoneyAccountModel) bWrapper.getBasePersistableModel();
			
	        AccountInfoModel accountInfo = abstractFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(smaModel, agentFormModel.getAppUserId(), null);
	        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
	        veriflyBaseWrapper.setAccountInfoModel(accountInfo);
	        LogModel logmodel = new LogModel();
	        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
	        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
	        veriflyBaseWrapper.setLogModel(logmodel);
	        veriflyBaseWrapper.setBasePersistableModel(smaModel);
	        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.WEB);
	
	        try{
	            veriflyBaseWrapper = abstractFinancialInstitution.resetPin(veriflyBaseWrapper);
	        }catch(Exception e){            
	            e.printStackTrace();
	        }
	        
	        Object[] args = {userDeviceAccountsModel.getUserId(),EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, userDeviceAccountsModel.getPin()), veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin(),MessageUtil.getMessage("agentmate.download.url")};
			String messageString = "";
			String franchiseMessageString = null;
			if (agentFormModel.getHead().equals(Boolean.TRUE)) 
			{
				messageString = MessageUtil.getMessage("ussd.agentAccountCreated", args);
			}
			else
			{
				messageString = MessageUtil.getMessage("ussd.directAgentAccountCreated", args);
			}
			SmsMessage smsMessage = new SmsMessage(agentFormModel.getMobileNo(), messageString,SMSConstants.Sender_1611);
			smsSender.send(smsMessage);
		}
        
        
		baseWrapper.putObject("agentFormModel", agentFormModel);
		
		actionLogModel.setCustomField1(retailerContactModel.getRetailerContactId().toString());
		actionLogModel.setCustomField11(userDeviceAccountsModel.getUserId());
		this.actionLogManager.completeActionLog(actionLogModel);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateAgent Ends");
		}
		}
		catch(FrameworkCheckedException fce)
		{
			fce.printStackTrace();
			throw new FrameworkCheckedException(fce.getMessage());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage(),e);
		}
		return baseWrapper;
	}	
	
	private UserDeviceAccountsModel extractUserDeviceAccountsModel(Collection<UserDeviceAccountsModel> userDeviceAccountsModels)
    {
	    UserDeviceAccountsModel userDeviceAccountsModel = null;
	    List<UserDeviceAccountsModel> userDeviceAccountsModelList = (List<UserDeviceAccountsModel>) userDeviceAccountsModels;

        if( userDeviceAccountsModelList != null && !userDeviceAccountsModelList.isEmpty() )
        {
            userDeviceAccountsModel = userDeviceAccountsModelList.get( 0 );
        }
        return userDeviceAccountsModel;
    }

	public void saveAgentForBulkProcess(RetailerContactListViewFormModel agentFormModel) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveAgentForBulkProcess Starts");
		}
		
		try
		{
			ActionLogModel logModel = new ActionLogModel();
			logModel.setUsecaseId(PortalConstants.RETAILER_CONTACT_FORM_USECASE_ID);
			logModel.setActionId(agentFormModel.isEditMode()?PortalConstants.ACTION_CREATE:PortalConstants.ACTION_UPDATE);
			actionLogStart(logModel);
			
			RetailerContactModel retailerContactModel = new RetailerContactModel();   
			retailerContactModel =  extractRetailerContactModel(retailerContactModel, agentFormModel);
			retailerContactModel.setCreatedBy(agentFormModel.getAppUserCreatedBy());
			retailerContactModel.setUpdatedBy(agentFormModel.getAppUserUpdatedBy());
			retailerContactDAO.saveOrUpdate(retailerContactModel);
			agentFormModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			agentFormModel.setApplicationNo(retailerContactModel.getApplicationNo());
			agentFormModel.setVersionNo(retailerContactModel.getVersionNo());
			agentFormModel.setRetailerContactCreatedOn(retailerContactModel.getCreatedOn());
			agentFormModel.setRetailerContactCreatedBy(retailerContactModel.getCreatedBy());
			agentFormModel.setRetailerContactUpdatedOn(retailerContactModel.getUpdatedOn());
			agentFormModel.setRetailerContactUpdatedBy(retailerContactModel.getUpdatedBy());
			
			
			AddressModel addressModel = new AddressModel(); 
			addressModel = extractAddressModel(addressModel, agentFormModel);
			addressDAO.saveOrUpdate(addressModel);
			agentFormModel.setAddressId(addressModel.getAddressId());
			
			
			RetailerContactAddressesModel retailerContactAddressesModel = extractRetailerContactAddressModel(agentFormModel);
			retailerContactAddressesModel.setAddressId(addressModel.getAddressId());
			retailerContactAddressesDAO.saveOrUpdate(retailerContactAddressesModel);
			agentFormModel.setRetailerContactAddressId(retailerContactAddressesModel.getRetailerContactAddressesId());
			
			AppUserModel appUserModel = new AppUserModel();
			appUserModel = extractAppUserForRetailerContact(appUserModel, agentFormModel);
			appUserModel.setCreatedBy(agentFormModel.getAppUserCreatedBy());
			appUserModel.setUpdatedBy(agentFormModel.getAppUserUpdatedBy());
			appUserDAO.saveOrUpdate(appUserModel);
			
			agentFormModel.setAppUserId(appUserModel.getAppUserId());
			agentFormModel.setAppUserVersionNo(appUserModel.getVersionNo());
			agentFormModel.setAppUserCreatedBy(appUserModel.getCreatedBy());
			agentFormModel.setAppUserCreatedOn(appUserModel.getCreatedOn());
			agentFormModel.setAppUserUpdatedBy(appUserModel.getUpdatedBy());
			agentFormModel.setAppUserUpdatedOn(appUserModel.getUpdatedOn());
			
			
			AppUserPartnerGroupModel appUserPartnerGroupModel = extractAppUserPartnerGroupModel(agentFormModel);
			appUserPartnerGroupModel.setCreatedBy(agentFormModel.getAppUserCreatedBy());
			appUserPartnerGroupModel.setUpdatedBy(agentFormModel.getAppUserUpdatedBy());
			appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
			agentFormModel.setAppUserPartnerGroupId(appUserPartnerGroupModel.getAppUserPartnerGroupId());
			agentFormModel.setAppUserPartnerGroupVersionNo(appUserPartnerGroupModel.getVersionNo());
			agentFormModel.setAppUserPartnerGroupCreatedBy(appUserPartnerGroupModel.getCreatedBy());
			agentFormModel.setAppUserPartnerGroupCreatedOn(appUserPartnerGroupModel.getCreatedOn());
			agentFormModel.setAppUserPartnerGroupUpdatedBy(appUserPartnerGroupModel.getUpdatedBy());
			agentFormModel.setAppUserPartnerGroupUpdatedOn(appUserPartnerGroupModel.getUpdatedOn());
			
			
			UserDeviceAccountsModel userDeviceAccountsModel = extractUserDeviceAccount(agentFormModel);
			userDeviceAccountsModel.setCreatedBy(agentFormModel.getAppUserCreatedBy());
			userDeviceAccountsModel.setUpdatedBy(agentFormModel.getAppUserUpdatedBy());
			userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
			

			
			//Agents BB account(OLA) - change start
	        BankModel bankModel = getOlaBankModel();
	        OLAVO olaVo = new OLAVO();
	        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
	        SwitchWrapper sWrapper = new SwitchWrapperImpl();
	        
	        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
	        baseWrapperBank.setBasePersistableModel(bankModel);
	        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
	        
			//Create New Agent Account in OLA

	        SmartMoneyAccountModel smaModel = extractSmartMoneyModel(agentFormModel.getRetailerContactId(),userDeviceAccountsModel.getUserId(),bankModel,abstractFinancialInstitution);
	        this.linkPaymentModeDAO.saveOrUpdate(smaModel);

	        olaVo.setFirstName(appUserModel.getFirstName());
	        olaVo.setLastName(appUserModel.getLastName());
	        olaVo.setMiddleName("-");
	        olaVo.setFatherName("-");
	        olaVo.setCnic(appUserModel.getNic());
	        olaVo.setLandlineNumber(appUserModel.getMobileNo());
	        olaVo.setMobileNumber(appUserModel.getMobileNo());
	        olaVo.setStatusId(1l);
	        olaVo.setCustomerAccountTypeId(CustomerAccountTypeConstants.RETAILER);
	        
	        if( appUserModel.getAddress1() == null ){
	            olaVo.setAddress("-");
	        }else{
	            olaVo.setAddress(appUserModel.getAddress1());
	        }
	        
	        if( appUserModel.getDob() == null ){
	            olaVo.setDob( new Date() );
	        }else{
	            olaVo.setDob(appUserModel.getDob());
	        }   
	        
	        sWrapper.setOlavo(olaVo);   
	        sWrapper.setBankId(bankModel.getBankId());
	        
	        try{
	            sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
	        }catch(Exception e){
	            e.printStackTrace();
	            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
	        }
	        
	        if ("07".equals(olaVo.getResponseCode())){
	            throw new FrameworkCheckedException("CNIC already exisits in the OLA accounts");
	        }
	        
	        AccountInfoModel accountInfoModel = new AccountInfoModel();
	        accountInfoModel.setAccountNo(olaVo.getPayingAccNo().toString());
	        accountInfoModel.setAccountNick(smaModel.getName());
	        accountInfoModel.setActive(smaModel.getActive());
	        accountInfoModel.setCreatedOn(smaModel.getCreatedOn());
	        accountInfoModel.setUpdatedOn(smaModel.getUpdatedOn());
	        accountInfoModel.setCustomerId(appUserModel.getAppUserId());
	        accountInfoModel.setCustomerMobileNo(appUserModel.getMobileNo());
	        accountInfoModel.setFirstName(appUserModel.getFirstName());
	        accountInfoModel.setLastName(appUserModel.getLastName());
	        accountInfoModel.setPaymentModeId(smaModel.getPaymentModeId());
	        accountInfoModel.setDeleted(Boolean.FALSE);

	        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
	        LogModel logmodel = new LogModel();
	        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
	        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
	        veriflyBaseWrapper.setLogModel(logmodel);
	        veriflyBaseWrapper.setBasePersistableModel(smaModel);
	        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.WEB);

	        try{
	            veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);
	        }catch(Exception e){            
	            e.printStackTrace();
	            throw new FrameworkCheckedException("Unable to generate PIN");
	        }
	        
	        //Agents BB account(OLA) - change End
			
			
			
			
	        Object[] args = {userDeviceAccountsModel.getUserId(),veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin()};
			String messageString = "";
			if (agentFormModel.getHead().equals(Boolean.TRUE)) 
			{
				messageString = MessageUtil.getMessage("ussd.agentAccountCreated", args);
			}
			else
			{
				messageString = MessageUtil.getMessage("ussd.directAgentAccountCreated", args);
			}
	
			// Sending message on agent creation
			SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString,SMSConstants.Sender_1611);
			smsSender.send(smsMessage);
	
			if(agentFormModel.getAccountNick() != null && agentFormModel.getAccountNo() != null)
			{
				LinkPaymentModeModel linkPaymentModeModel = new LinkPaymentModeModel();

				linkPaymentModeModel.setMfsId(userDeviceAccountsModel.getUserId());
				linkPaymentModeModel.setName(agentFormModel.getAccountNick());
				linkPaymentModeModel.setAccountNo(agentFormModel.getAccountNo());
				linkPaymentModeModel.setAppUserId(appUserModel.getAppUserId());
				linkPaymentModeModel.setNic(appUserModel.getNic());
				linkPaymentModeModel.setPaymentMode(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);

				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.LINK_PAYMENT_MODE_USECASE_ID);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				baseWrapper.putObject("linkPaymentModeModel", linkPaymentModeModel);
				baseWrapper.putObject("userDeviceAccountsModel", userDeviceAccountsModel);
				baseWrapper.putObject("appUserModel", appUserModel);
				baseWrapper.putObject("bankModel", agentFormModel.getBankModel());
				baseWrapper = linkPaymentModeManager.createLinkPaymentModeForBulk(baseWrapper);
			}
			logModel.setCustomField1(retailerContactModel.getRetailerContactId().toString());
			logModel.setCustomField11(retailerContactModel.getRetailerContactId().toString());
			actionLogEnd(logModel);
			
			if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveAgentForBulkProcess Ends");
			}
		}
		catch(FrameworkCheckedException fce)
		{
			throw fce;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage());
		}
	}
	
	public SearchBaseWrapper findPartnerGroupsByRetailer(Long retailerId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("AgentHierarchyManagerImpl.findAllPartnerGroups Starts");
		}
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		
		PartnerModel partnerModel = new PartnerModel();
		partnerModel.setRetailerId(retailerId);
		CustomList<PartnerModel> partnerModelList = this.partnerDAO.findByExample(partnerModel);
		partnerModel = partnerModelList.getResultsetList().get(0);
		
		PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		partnerGroupModel.setPartnerId(partnerModel.getPartnerId());
		CustomList<PartnerGroupModel> partnerGroupModelList = (new CustomList<PartnerGroupModel>(this.partnerGroupDAO.findPartnerGroupsByPartnerId(partnerModel.getPartnerId())));
		
		if(partnerGroupModelList != null && partnerGroupModelList.getResultsetList() != null && partnerGroupModelList.getResultsetList().size() > 0)
		{
			searchBaseWrapper.setCustomList(partnerGroupModelList );
		}

		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllPartnerGroups Ends");
		}
		return searchBaseWrapper;
	}
	
	
	public SearchBaseWrapper findAllPartnerGroups() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllPartnerGroups Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<PartnerGroupModel> partnerGroupModelList = this.partnerGroupDAO.findAll();
		
		searchBaseWrapper.setCustomList(partnerGroupModelList );
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllPartnerGroups Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findAllProductCatalogs() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllProductCatalogs Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CustomList<ProductCatalogModel> productCatalogModelList = this.productCatalogDAO.findAll();
		
		searchBaseWrapper.setCustomList(productCatalogModelList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllProductCatalogs Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findParentAgents(long distributorLevelId, long retailerId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findParentAgents Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				
	    List<RetailerContactModel> retailerContactModelList = this.retailerContactDAO.findRetailerContactByDistributorLevelId(distributorLevelId, retailerId);
	    
	    CustomList<RetailerContactModel> RetailerContactModelCustomList = new CustomList<RetailerContactModel>();
	    
	    RetailerContactModelCustomList.setResultsetList(retailerContactModelList);
	    
		searchBaseWrapper.setCustomList(RetailerContactModelCustomList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findParentAgents Ends");
		}
		return searchBaseWrapper;
	}

	public RetailerContactModel findHeadAgent(long distributorLevelId, long retailerId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.isHeadAgentExist Starts");
		}
		RetailerContactModel retailerContactModel = null;
	    List<RetailerContactModel> retailerContactModelList = this.retailerContactDAO.findHeadAgents(distributorLevelId, retailerId);
	    
	    if(retailerContactModelList.size() > 0)
	    {
	    	retailerContactModel = retailerContactModelList.get(0);
	    }
	    	
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.isHeadAgentExist Ends");
		}
		return retailerContactModel;
	}
	
	public SearchBaseWrapper findAreaNamesByRegionId(long regionId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaNamesByRegionId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    List<AreaModel> areaModelList = areaDAO.findAreaNamesByRegionId(regionId);
	    CustomList<AreaModel> customList = new CustomList<AreaModel>();
		customList.setResultsetList(areaModelList);
		searchBaseWrapper.setCustomList(customList);
	    if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaNamesByRegionId Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findAgentAreaByRegionId(long regionId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAgentAreaByRegionId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    List<AreaModel> areaModelList = areaDAO.findAgentAreaByRegionId(regionId);
	    CustomList<AreaModel> customList = new CustomList<AreaModel>();
		customList.setResultsetList(areaModelList);
		searchBaseWrapper.setCustomList(customList);
	    if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAgentAreaByRegionId Ends");
		}
		return searchBaseWrapper;
	}

	public SearchBaseWrapper findAreaNamesByAreaLevelId(long areaLevelId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaNamesByAreaLevelId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		AreaModel areaModel = new AreaModel();
		areaModel.setActive(Boolean.TRUE);
		areaModel.setAreaLevelModelId(areaLevelId);
	    CustomList<AreaModel> customList = areaDAO.findByExample(areaModel);
		searchBaseWrapper.setCustomList(customList);
	    if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaNamesByAreaLevelId Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findAreaLevelsByRegionId(long regionId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaLevelsByRegionId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    List<AreaLevelModel> areaLevelModelList = areaLevelDAO.findAreaLevelsByRegionId(regionId);
	    CustomList<AreaLevelModel> customList = new CustomList<AreaLevelModel>();
		customList.setResultsetList(areaLevelModelList);
		searchBaseWrapper.setCustomList(customList);
	    if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaLevelsByRegionId Ends");
		}
		return searchBaseWrapper;
	}
	

		

	public SearchBaseWrapper findAgentLevelsByRegionId(long regionId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAgentLevelsByRegionId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    List<DistributorLevelModel> agentLevelModelList = distributorLevelDAO.findAgentLevelsByRegionId(regionId);
	    CustomList<DistributorLevelModel> customList = new CustomList<DistributorLevelModel>();
		customList.setResultsetList(agentLevelModelList);
		searchBaseWrapper.setCustomList(customList);
	    if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAgentLevelsByRegionId Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findRetailersByRegionId(long regionId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRetailersByRegionId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        List<RetailerModel> retailerModelList = retailerDAO.findRetailersByRegionId(regionId);
        CustomList<RetailerModel> customList = new CustomList<RetailerModel>();
    	customList.setResultsetList(retailerModelList);
    	searchBaseWrapper.setCustomList(customList);
        if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRetailersByRegionId Ends");
		}
		return searchBaseWrapper;
	}

	public SearchBaseWrapper findRetailersByDistributorId(long distributorId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRetailersByRegionId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        List<RetailerModel> retailerModelList = retailerDAO.findRetailersByDistributorId(distributorId);
        CustomList<RetailerModel> customList = new CustomList<RetailerModel>();
    	customList.setResultsetList(retailerModelList);
    	searchBaseWrapper.setCustomList(customList);
        if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRetailersByRegionId Ends");
		}
		return searchBaseWrapper;
	}
	
	public boolean areaLevelIntegrityCheck(long areaLevelId) throws FrameworkCheckedException
	{
		boolean recordFoundFlag = Boolean.FALSE;
		AreaLevelModel model = new AreaLevelModel();
		model.setParentAreaLevelId(areaLevelId);
		model.setActive(Boolean.TRUE);
		List<AreaLevelModel> list = this.areaLevelDAO.areaLevelIntegrityCheck(model);
		if(list.size() > 0)
		{
			recordFoundFlag = Boolean.TRUE;
		}
		else
		{
			AreaModel areaModel = new AreaModel();			// TODO This code needs to be verify.
			areaModel.setAreaLevelModelId(areaLevelId);
			areaModel.setActive(Boolean.TRUE);
			CustomList<AreaModel> customList = this.areaDAO.findByExample(areaModel);
			if(customList.getResultsetList() != null && customList.getResultsetList().size() > 0)
			{
				recordFoundFlag = Boolean.TRUE;
			}
		}
		return recordFoundFlag;
	}
	
	public boolean regionIntegrityCheck(long regionId) throws FrameworkCheckedException
	{
		boolean recordFoundFlag = Boolean.FALSE;
		AreaLevelModel model = new AreaLevelModel();
		model.setRegionId(regionId);
		model.setActive(Boolean.TRUE);
		CustomList<AreaLevelModel> customList = this.areaLevelDAO.findByExample(model);
		if(customList.getResultsetList().size() > 0)
		{
			recordFoundFlag = Boolean.TRUE;
		}
		return recordFoundFlag;
	}
	
	public BaseWrapper deleteAreaLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.deleteAreaLevel Starts");
		}
		
		AreaLevelModel areaLevelModel = (AreaLevelModel) baseWrapper.getBasePersistableModel();
		areaLevelDAO.delete(areaLevelModel);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.deleteAreaLevel Ends");
		}
		return baseWrapper;
	}
	
	public BaseWrapper deleteRegionalHierarchyAssociation(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.deleteRegionalHierarchyAssociation Starts");
		}
		
		DistRegHierAssociationModel distRegHierAssociationModel = (DistRegHierAssociationModel) baseWrapper.getBasePersistableModel();
		distRegHierAssociationDAO.delete(distRegHierAssociationModel);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.deleteRegionalHierarchyAssociation Ends");
		}
		return baseWrapper;
	}
	
	public SearchBaseWrapper findAreaLevelsByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaLevelsByRegionalHierarchyId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        List<AreaLevelModel> areaLevelModelList = areaLevelDAO.findAreaLevelsByRegionalHierarchyId(regionalHierarchyId);
        if(areaLevelModelList != null)
        {
        	CustomList<AreaLevelModel> customList = new CustomList<AreaLevelModel>();
        	customList.setResultsetList(areaLevelModelList);
        	searchBaseWrapper.setCustomList(customList);
        }        
        if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAreaLevelsByRegionalHierarchyId Ends");
		}
		return searchBaseWrapper;
	}
	
	
	public BaseWrapper saveOrUpdateRegionalHierarchyAssociation(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateRegionalHierarchyAssociation Starts");
		}
		DistRegHierAssociationModel distRegHierAssociationModel = (DistRegHierAssociationModel) baseWrapper.getBasePersistableModel();
		/*ActionLogModel logModel = new ActionLogModel();
		logModel.setUsecaseId(PortalConstants.AREALEVEL_USECASE_ID);
		logModel.setActionId(areaLevelModel.isEditMode()?PortalConstants.ACTION_CREATE:PortalConstants.ACTION_UPDATE);
		actionLogStart(logModel);*/	
		distRegHierAssociationDAO.saveOrUpdate(distRegHierAssociationModel);
		/*logModel.setCustomField1(areaLevelModel.getAreaLevelId().toString());
		actionLogEnd(logModel);*/
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateRegionalHierarchyAssociation Ends");
		}
		return baseWrapper;
	}
	
	public BaseWrapper saveOrUpdateSalesHierarchy(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateSalesHierarchy Starts");
		}
		SalesHierarchyModel salesHierarchyModel = (SalesHierarchyModel) baseWrapper.getBasePersistableModel();
		this.salesHierarchyDAO.saveOrUpdate(salesHierarchyModel);
		
		SalesHierarchyModel oldSalesHierarchyModel = (SalesHierarchyModel) baseWrapper.getObject("oldSalesHierarchyModel");
/*		if(oldSalesHierarchyModel != null){
			SalesHierarchyHistoryModel salesHierarchyHistoryModel = this.populateSalesHierarchyHistoryModel(oldSalesHierarchyModel, salesHierarchyModel);
			Collection<SalesHierarchyHistoryModel> salesHierarchyIdSalesHierarchyHistoryModelList = new ArrayList<SalesHierarchyHistoryModel>();
			salesHierarchyIdSalesHierarchyHistoryModelList.add(salesHierarchyHistoryModel);
			this.salesHierarchyHistoryDAO.saveOrUpdate(salesHierarchyHistoryModel);
		}else{
			SalesHierarchyHistoryModel salesHierarchyHistoryModel = this.populateSalesHierarchyHistoryModel(salesHierarchyModel, salesHierarchyModel);
			Collection<SalesHierarchyHistoryModel> salesHierarchyIdSalesHierarchyHistoryModelList = new ArrayList<SalesHierarchyHistoryModel>();
			salesHierarchyIdSalesHierarchyHistoryModelList.add(salesHierarchyHistoryModel);
			this.salesHierarchyHistoryDAO.saveOrUpdate(salesHierarchyHistoryModel);
		}*/
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateSalesHierarchy Ends");
		}
		return baseWrapper;
	}
	
	public BaseWrapper saveOrUpdateAreaLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateAreaLevel Starts");
		}
		AreaLevelModel areaLevelModel = (AreaLevelModel) baseWrapper.getBasePersistableModel();
		ActionLogModel logModel = new ActionLogModel();
		logModel.setUsecaseId(PortalConstants.AREALEVEL_USECASE_ID);
		logModel.setActionId(areaLevelModel.isEditMode()?PortalConstants.ACTION_CREATE
				  :PortalConstants.ACTION_UPDATE);
		actionLogStart(logModel);	
		
		areaLevelDAO.saveOrUpdate(areaLevelModel);
		logModel.setCustomField1(areaLevelModel.getAreaLevelId().toString());
		logModel.setCustomField11(areaLevelModel.getRegionalHierarchyModel().getRegionalHierarchyName());
		actionLogEnd(logModel);
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateAreaLevel Ends");
		}
		return baseWrapper;
	}

	public Boolean doesRegionsExistOfRegionalHierarchy(Long regionalHierarchyId) throws FrameworkCheckedException
	{
		List<RegionModel> regionList=regionDAO.findRegionsByRegionalHierarchyId(regionalHierarchyId);
		 if(null!=regionList && regionList.size()>0)
		 {
        	return Boolean.TRUE;
         }
		 return Boolean.FALSE;
	}
	
	public Boolean doesRegionsExistOfDistributor(Long distributorId) throws FrameworkCheckedException
	{
		List<DistRegHierAssociationModel> distRegHieAssociationModelList=distRegHierAssociationDAO.findRegionalHierarchyByDistributorId(distributorId);
		DistRegHierAssociationModel distRegHieAssociationModel = distRegHieAssociationModelList.get(0);
		return doesRegionsExistOfRegionalHierarchy(distRegHieAssociationModel.getRegionalHierarchyId());
	}
	
	public SearchBaseWrapper findRegionsByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionsByRegionalHierarchyId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        List<RegionModel> regionModelList = regionDAO.findRegionsByRegionalHierarchyId(regionalHierarchyId);
        if(regionModelList != null)
        {
        	CustomList<RegionModel> customList = new CustomList<RegionModel>();
        	customList.setResultsetList(regionModelList);
        	searchBaseWrapper.setCustomList(customList);
        }        
        if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionsByRegionalHierarchyId Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findRegionsByDistributorId(long distributorId) throws FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("AgentHierarchyManagerImpl.findRegionsByDistributorId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<DistRegHierAssociationModel> distRegHieAssociationModelList = distRegHierAssociationDAO.findRegionalHierarchyByDistributorId(distributorId);
		if (CollectionUtils.isNotEmpty(distRegHieAssociationModelList)) {
			DistRegHierAssociationModel distRegHieAssociationModel = distRegHieAssociationModelList.get(0);
			List<RegionModel> regionList = regionDAO.findRegionsByRegionalHierarchyId(distRegHieAssociationModel.getRegionalHierarchyId());
			if (regionList != null) {
				CustomList<RegionModel> customList = new CustomList<RegionModel>();
				customList.setResultsetList(regionList);
				searchBaseWrapper.setCustomList(customList);
			}
		}
		return searchBaseWrapper;
	}

	public SearchBaseWrapper findDistributorByserviceOperatorId(long soId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionsByDistributorId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<DistributorModel> distributorModelList=distributorDAO.findDistributorByserviceOperatorId(soId);
		if( CollectionUtils.isNotEmpty(distributorModelList))
		{
				CustomList<DistributorModel> customList = new CustomList<DistributorModel>();
				customList.setResultsetList(distributorModelList);
				searchBaseWrapper.setCustomList(customList);
		}
		
        if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionsByDistributorId Ends");
		}
		return searchBaseWrapper;
	}
	public SearchBaseWrapper findDistributorsById(long distributorId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findDistributorsById Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		try
		{			
			DistributorModel model = (DistributorModel)this.distributorDAO.findByPrimaryKey(distributorId);	
			if(model != null)
			{
				searchBaseWrapper.setBasePersistableModel(model.giveValueObject());
			}
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findDistributorsById Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findSaleUserByBankUserId(long bankUserId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findDistributorsById Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		try
		{			
			SalesHierarchyModel model = (SalesHierarchyModel)this.salesHierarchyDAO.findSaleUserByBankUserId(bankUserId);	
			if(model != null)
			{
				searchBaseWrapper.setBasePersistableModel(model);
			}
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findDistributorsById Ends");
		}
		return searchBaseWrapper;
	}
	
	@Override
	public SearchBaseWrapper findSaleUserHistoryByBankUserId(long bankUserId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findSaleUserHistoryByBankUserId Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		try
		{			
/*			List<SalesHierarchyHistoryModel> models = this.salesHierarchyHistoryDAO.findSaleUserHistoryByBankUserId(bankUserId);	
			if(models != null)
			{
				CustomList customList = new CustomList<SalesHierarchyHistoryModel>();
				customList.setResultsetList(models);
				searchBaseWrapper.setCustomList(customList);
			}*/
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findSaleUserHistoryByBankUserId Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findRegionalHierarchyById(long regionalHierarchyId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionalHierarchyById Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		try
		{			
			RegionalHierarchyModel model = (RegionalHierarchyModel)this.regionalHierarchyDAO.findByPrimaryKey(regionalHierarchyId);	
			if(model != null)
			{
				searchBaseWrapper.setBasePersistableModel(model);
			}
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionalHierarchyById Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper findAllDistributors() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllDistributors Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<DistributorModel> distributorModels = this.distributorDAO.findAllDistributor();
		CustomList<DistributorModel> distributorModelList= new CustomList<DistributorModel>();
		distributorModelList.setResultsetList(distributorModels);
		
		searchBaseWrapper.setCustomList(distributorModelList);
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findAllDistributors Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchRegionalHierarchyByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.searchRegionalHierarchyByCriteria Starts");
		}
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.ANYWHERE); 
		exampleHolder.setIgnoreCase(false);
		RegionalHierarchyModel regionalHierarchyModel = (RegionalHierarchyModel)searchBaseWrapper.getBasePersistableModel(); 
		CustomList<RegionalHierarchyModel> regionalHierarchyModelList = this.regionalHierarchyDAO.findByExample(regionalHierarchyModel, null, null, exampleHolder);
		
		if (regionalHierarchyModelList != null && regionalHierarchyModelList.getResultsetList().size() > 0)
		{
			searchBaseWrapper.setCustomList(regionalHierarchyModelList);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.searchRegionalHierarchyByCriteria Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper searchDistributorsByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.searchDistributorsByCriteria Starts");
		}
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.ANYWHERE); 
		exampleHolder.setIgnoreCase(false);
		DistributorModel distributorModel = (DistributorModel)searchBaseWrapper.getBasePersistableModel(); 
		CustomList<DistributorModel> distributorModelList = this.distributorDAO.findByExample(distributorModel, null, null, exampleHolder);
		
		if (distributorModelList != null && distributorModelList.getResultsetList().size() > 0)
		{
			searchBaseWrapper.setCustomList(distributorModelList);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.searchDistributorsByCriteria Ends");
		}
		return searchBaseWrapper;
	}
	
	public DistributorModel findDistributorsByName(String distributorName) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findDistributorsByName Starts");
		}
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT); 
		exampleHolder.setIgnoreCase(false);
		DistributorModel distributorModel = new DistributorModel();
		distributorModel.setName(distributorName);
		CustomList<DistributorModel> distributorModelList = this.distributorDAO.findByExample(distributorModel, null, null, exampleHolder);
		
		if (distributorModelList != null && distributorModelList.getResultsetList().size() > 0)
		{
			distributorModel = distributorModelList.getResultsetList().get(0);
		}
		else
		{
			distributorModel = null;
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findDistributorsByName Ends");
		}
		return distributorModel;
	}
	
	
	
	public RetailerModel findFranchiseByName(String franchiseName) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findFranchiseByName Starts");
		}
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT); 
		exampleHolder.setIgnoreCase(false);
		RetailerModel retailerModel = new RetailerModel();
		retailerModel.setName(franchiseName);
		CustomList<RetailerModel> retailerModelList = this.retailerDAO.findByExample(retailerModel, null, null, exampleHolder);
		
		if (retailerModelList != null && retailerModelList.getResultsetList().size() > 0)
		{
			retailerModel = retailerModelList.getResultsetList().get(0);
		}
		else
		{
			retailerModel = null;
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findFranchiseByName Ends");
		}
		return retailerModel;
	}
	
	@Override
	public RetailerModel loadFranchise(Long retailerId) throws FrameworkCheckedException
	  { 
			return this.retailerDAO.findByPrimaryKey(retailerId); 	  
	  }
	
	public RegionModel findRegionByName(String regionName) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionByName Starts");
		}
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT); 
		exampleHolder.setIgnoreCase(false);
		RegionModel regionModel = new RegionModel();
		regionModel.setRegionName(regionName);
		CustomList<RegionModel> regionModelList = this.regionDAO.findByExample(regionModel, null, null, exampleHolder);
		
		if (regionModelList != null && regionModelList.getResultsetList().size() > 0)
		{
			regionModel = regionModelList.getResultsetList().get(0);
		}
		else
		{
			regionModel = null;
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.findRegionByName Ends");
		}
		return regionModel;
	}
	
	public SearchBaseWrapper loadRefrenceData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadRefrenceData Starts");
		  }
		  CustomList<Object> refdataList= new CustomList<>();
		  try
		  {
			  ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
	          exampleHolder.setMatchMode(MatchMode.EXACT); 
	          exampleHolder.setIgnoreCase(false);
			  CustomList<BankModel> bankList = this.bankDAO.findByExample(new BankModel(), null, null, exampleHolder);			  
			  CustomList<MnoModel> mnoList = this.mnoDAO.findByExample(new MnoModel(), null, null, exampleHolder);			  
			  if (bankList != null && bankList.getResultsetList().size() > 0)
			  {
				  List<Object> result= new ArrayList<Object>();
				  result.add(bankList);
				  result.add(mnoList);
				  refdataList.setResultsetList(result);				  
				  searchBaseWrapper.setCustomList(refdataList);
			  }			  
		  }
		  catch(Exception ex)
		  {
			  logger.error(ex);
		  }
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.loadRefrenceData Ends");
			  }
		  return searchBaseWrapper;
	  }
	
	 public BaseWrapper saveOrUpdateDistributor(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateDistributor Starts");
		  }
		  DistributorModel newDistributorModel = (DistributorModel) baseWrapper.getBasePersistableModel();
		  ActionLogModel logModel = new ActionLogModel();
		  logModel.setUsecaseId(PortalConstants.AGENT_NETWORK_USECASE_ID);
		  logModel.setActionId(newDistributorModel.getDistributorId()==null?PortalConstants.ACTION_CREATE
				  :PortalConstants.ACTION_UPDATE);
		  actionLogStart(logModel);		  		  	  
		  distributorDAO.saveOrUpdate(newDistributorModel);	
		  logModel.setCustomField1(newDistributorModel.getDistributorId().toString());
		  logModel.setCustomField11(newDistributorModel.getName());
		  actionLogEnd(logModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateDistributor Ends");
		  }		  
		  return baseWrapper;
	  }

	 public BaseWrapper saveOrUpdateRegionalHierarchy(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateRegionalHierarchy Starts");
		  }
		  RegionalHierarchyModel regionalHierarchyModel = (RegionalHierarchyModel) baseWrapper.getBasePersistableModel();
		  ActionLogModel logModel = new ActionLogModel();
		  logModel.setUsecaseId(PortalConstants.REGIONAL_HIERARCHY_USECASE_ID);
		  logModel.setActionId(regionalHierarchyModel.getRegionalHierarchyId()==null?PortalConstants.ACTION_CREATE:PortalConstants.ACTION_UPDATE);
		  actionLogStart(logModel);
		  this.regionalHierarchyDAO.saveOrUpdate(regionalHierarchyModel);
		  logModel.setCustomField1(regionalHierarchyModel.getRegionalHierarchyId().toString());
		  actionLogEnd(logModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateRegionalHierarchy Ends");
		  }		  
		  return baseWrapper;
	  }

	public SearchBaseWrapper saveOrUpdateRegion(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateRegion Starts");
		}
		ActionLogModel logModel = new ActionLogModel();
		logModel.setUsecaseId(PortalConstants.REGION_USECASE_ID);
		
		actionLogStart(logModel);
		try
		{
			List<RegionModel> regionModelList = (ArrayList<RegionModel>)searchBaseWrapper.getCustomList().getResultsetList();
			for(RegionModel regionModel:regionModelList)
			{
				if(regionModel.isDeleted())
				{
					logModel.setActionId(PortalConstants.ACTION_DELETE);
					regionDAO.delete(regionModel);	
					logModel.setCustomField1(regionModel.getRegionId().toString());
					logModel.setCustomField11(regionModel.getRegionalHierarchyModel().getRegionalHierarchyName());
					actionLogEnd(logModel);
				}
				else
				{
					logModel.setActionId(regionModel.getRegionId()==null?PortalConstants.ACTION_CREATE
							  :PortalConstants.ACTION_UPDATE);
					regionDAO.saveOrUpdate(regionModel);
					searchBaseWrapper.setBasePersistableModel(regionModel);
					logModel.setCustomField1(regionModel.getRegionId().toString());
					logModel.setCustomField11(regionModel.getRegionalHierarchyModel().getRegionalHierarchyName());
					actionLogEnd(logModel);
				}
				
			}
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveOrUpdateRegion Ends");
		}
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper loadDistributorLevelRefData(Long regionalHierarchyId) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadDistributorLevelRefData Starts");
		  }
		  SearchBaseWrapper searchBaseWrapper= new SearchBaseWrapperImpl();
		  try
		  {			  
			    List<RegionModel> regionModelList = regionDAO.findRegionsByRegionalHierarchyId(regionalHierarchyId);
			    List<DistributorLevelModel> levelModels= distributorLevelDAO.findDistributorLevelByRegionalHierarchyId(regionalHierarchyId);
			    List<Object> result = new ArrayList<Object>();
			    result.add(regionModelList);
			    result.add(levelModels);
			    if(regionModelList != null)
			    {
			    	CustomList<Object> customList = new CustomList<Object>();
			    	customList.setResultsetList(result);
			    	searchBaseWrapper.setCustomList(customList);
			    }
		  }
		  catch(Exception ex)
		  {
			  logger.error(ex);
		  }
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.loadDistributorLevelRefData Ends");
			  }
		  return searchBaseWrapper;
	  }
	
	@Override
	public List<DistributorLevelModel> loadDistributorLevelByRegionIdAndRegionHierarchyId(Long regionId, Long regionHierarchyId) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadDistributorLevelRefData Starts");
		  }
		  List<DistributorLevelModel> ultimateAgentLevel = null;
		  try
		  {			  
			    ultimateAgentLevel = this.distributorLevelDAO.loadDistributorLevelByRegionAndRegionHierarchy(regionId, regionHierarchyId);
		  }
		  catch(Exception ex)
		  {
			  logger.error(ex);
		  }
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.loadDistributorLevelRefData Ends");
			  }
		  return ultimateAgentLevel;
	  }
	
	public BaseWrapper saveOrUpdateDistributorLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateDistributorLevel Starts");
		  }
		  DistributorLevelModel levelModel = (DistributorLevelModel) baseWrapper.getBasePersistableModel();	
		  ActionLogModel logModel = new ActionLogModel();
		  logModel.setUsecaseId(PortalConstants.AGENTLEVEL_USECASE_ID);
		  logModel.setActionId(levelModel.isEditMode()?PortalConstants.ACTION_CREATE
				  :PortalConstants.ACTION_UPDATE);
		  actionLogStart(logModel);
		  
		  levelModel=distributorLevelDAO.saveOrUpdate(levelModel);
		  baseWrapper.setBasePersistableModel(levelModel);
		  logModel.setCustomField1(levelModel.getDistributorLevelId().toString());
		  logModel.setCustomField11(levelModel.getRegionalHierarchyModel().getRegionalHierarchyName());
		  actionLogEnd(logModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateDistributorLevel Ends");
		  }
		  return baseWrapper;
	  }
	
	public BaseWrapper deleteDistributorLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.deleteDistributorLevel Starts");
		  }
		  DistributorLevelModel levelModel = (DistributorLevelModel) baseWrapper.getBasePersistableModel();		 
		  distributorLevelDAO.deleteDistributorLevel(levelModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.deleteDistributorLevel Ends");
		  }
		  return baseWrapper;
	  }
	
	public SearchBaseWrapper loadFranchiseSearchRefData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadFranchiseRefData Starts");
		  }
		  
		  	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT); 
			exampleHolder.setIgnoreCase(false);
			List<DistributorModel> distributorModels = this.distributorDAO.findActiveDistributor();
			CustomList<DistributorModel> distributorModelList= new CustomList<DistributorModel>();
			distributorModelList.setResultsetList(distributorModels);
			
			if (distributorModelList != null && distributorModelList.getResultsetList().size() > 0)
			{
				searchBaseWrapper.setCustomList(distributorModelList);
			}
			
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.loadFranchiseRefData End");
		  }		  
		  return searchBaseWrapper;
	  }
	
	public SearchBaseWrapper searchFranchiseByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.searchFranchiseByCriteria Starts");
		  }
		  
		  	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.ANYWHERE); 
			exampleHolder.setIgnoreCase(false);
			RetailerModel retailerModel = (RetailerModel)searchBaseWrapper.getBasePersistableModel(); 
			CustomList<RetailerModel> retailerList = this.retailerDAO.findByExample(retailerModel, null, null, exampleHolder);
			
			if (retailerList != null && retailerList.getResultsetList().size() > 0)
			{
				searchBaseWrapper.setCustomList(retailerList);
			}
			
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.searchFranchiseByCriteria End");
		  }		  
		  return searchBaseWrapper;
	  }
	public SearchBaseWrapper loadCountry(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadCountry Starts");
		  }
		  CustomList<CountryModel> countryList= new CustomList<CountryModel>();
		  countryList.setResultsetList(retailerDAO.loadCountry());
		  searchBaseWrapper.setCustomList(countryList);
			
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.loadCountry End");
		  }		  
		  return searchBaseWrapper;
	  }
	
	public List<RetailerModel> saveFranchiseBulk(List<RetailerModel> validatedRetailerModels) throws FrameworkCheckedException{
		List<RetailerModel> failedModels = new ArrayList<RetailerModel>();
		for (RetailerModel retailerModel : validatedRetailerModels) {
			try {
				franchiseCreator.send(retailerModel);
				this.createOrUpdateBulkFranchiseRequiresNewTransaction(retailerModel,true);
			} catch (FrameworkCheckedException e1) {
				e1.printStackTrace();
			}
        }
		return failedModels;
	}
	
	public boolean isAlreadyExistsFranchiseName(String name){
		boolean result = false;
		try{
			RetailerModel retailerModel = new RetailerModel();
			retailerModel.setName(name);
		  	
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			exampleHolder.setIgnoreCase(false);
			if(retailerDAO.countByExample(retailerModel, exampleHolder) > 0){
				result = true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	public boolean saveFranchiseFrmQueue(RetailerModel retailerModel){
        List<UserPermissionModel> permissionList = new ArrayList<UserPermissionModel>(0);
		try {
			permissionList = this.partnerPermissionViewDAO.getFranchisePermissions();
			createOrUpdateBulkFranchiseRequiresNewTransaction(retailerModel,false);
		} catch (FrameworkCheckedException e) {
			e.printStackTrace();
		}
		return this.saveFranchise(retailerModel, permissionList);
	}
	
	private boolean saveFranchise(RetailerModel retailerModel, List<UserPermissionModel> permissionList){
		boolean result = false;
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.saveFranchiseBulk Starts");
		}
		  
		  try{
			  retailerModel=retailerDAO.saveOrUpdate(retailerModel);
			  
			  Long currentUserId = retailerModel.getCreatedBy();
			  ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel(); 
		      concernPartnerModel.setActive(true);
		      concernPartnerModel.setRetailerId(retailerModel.getRetailerId());
		      concernPartnerModel.setDescription(retailerModel.getDescription());
		      concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.RETAILER);
		      concernPartnerModel.setCreatedBy(currentUserId);
		      concernPartnerModel.setUpdatedBy(currentUserId);
		      concernPartnerModel.setUpdatedOn(new Date());
		      concernPartnerModel.setCreatedOn(new Date());
		      concernPartnerModel.setName(retailerModel.getName());
		      concernPartnerDAO.saveOrUpdate(concernPartnerModel);
	
	          PartnerModel partnerModel = new PartnerModel();
	          partnerModel.setName(retailerModel.getName());
	       	  partnerModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
	          partnerModel.setRetailerId(retailerModel.getRetailerId());
	          partnerModel.setActive(true);
	          partnerModel.setComments(retailerModel.getComments());
	          partnerModel.setDescription(retailerModel.getDescription());
	          partnerModel.setCreatedBy(currentUserId);
	          partnerModel.setUpdatedBy(currentUserId);
	          partnerModel.setUpdatedOn(new Date());
	          partnerModel.setCreatedOn(new Date());
	       
	          PartnerPermissionGroupModel partnerPermissionGroupModel = new PartnerPermissionGroupModel();
	          partnerPermissionGroupModel.setPermissionGroupId(PermissionGroupConstants.RETAILOR);
	          partnerPermissionGroupModel.setCreatedBy(currentUserId);
	          partnerPermissionGroupModel.setUpdatedBy(currentUserId);
	          partnerPermissionGroupModel.setUpdatedOn(new Date());
	          partnerPermissionGroupModel.setCreatedOn(new Date());
	
	          partnerModel.addPartnerIdPartnerPermissionGroupModel(partnerPermissionGroupModel);
	          partnerDAO.saveOrUpdate(partnerModel);

	          PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
	          partnerGroupModel.setPartnerId(partnerModel.getPartnerId());
	          partnerGroupModel.setName(retailerModel.getName());
	          partnerGroupModel.setDescription(retailerModel.getGroupDesc());
	          partnerGroupModel.setActive(retailerModel.getGroupActive());
	          partnerGroupModel.setEditable(true);
	          partnerGroupModel.setEmail(retailerModel.getGroupEmail());
	          partnerGroupModel.setCreatedBy(currentUserId);
	          partnerGroupModel.setUpdatedBy(currentUserId);
	          partnerGroupModel.setCreatedOn(new Date());
	          partnerGroupModel.setUpdatedOn(new Date());
	          
	          Iterator<UserPermissionModel> itrDP = permissionList.iterator();
	          while(itrDP.hasNext()){
	        	  UserPermissionModel permModel = itrDP.next();
	        	  
	        	  if(permModel.getIsDefault()){
	    	          PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
		        	  partnerGroupDetailModel = new PartnerGroupDetailModel();
		        	  partnerGroupDetailModel.setUserPermissionId(permModel.getUserPermissionId());
		        	  partnerGroupDetailModel.setReadAllowed(permModel.getReadAvailable());
		        	  partnerGroupDetailModel.setUpdateAllowed(permModel.getUpdateAvailable());
		        	  partnerGroupDetailModel.setDeleteAllowed(permModel.getDeleteAvailable());
		        	  partnerGroupDetailModel.setCreateAllowed(permModel.getCreateAvailable());
		        	  partnerGroupDetailModel.setCreatedBy(currentUserId);
		        	  partnerGroupDetailModel.setUpdatedBy(currentUserId);
		        	  partnerGroupDetailModel.setCreatedOn(new Date());
		        	  partnerGroupDetailModel.setUpdatedOn(new Date());
	        		  partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);
	        	  }else{
	    	          PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
	    	          partnerGroupDetailModel.setUserPermissionId(permModel.getUserPermissionId());
		        	  partnerGroupDetailModel.setReadAllowed(retailerModel.getPermissionRead());
	    	          partnerGroupDetailModel.setUpdateAllowed(retailerModel.getPermissionUpdate());
	    	          partnerGroupDetailModel.setDeleteAllowed(retailerModel.getPermissionDelete());
	    	          partnerGroupDetailModel.setCreateAllowed(retailerModel.getPermissionCreate());
	    	          partnerGroupDetailModel.setCreatedBy(currentUserId);
	    	          partnerGroupDetailModel.setUpdatedBy(currentUserId);
	    	          partnerGroupDetailModel.setCreatedOn(new Date());
	    	          partnerGroupDetailModel.setUpdatedOn(new Date());
	    	          partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);
	        	  }
	          }
	          
	          this.partnerGroupDAO.saveOrUpdate(partnerGroupModel);

        	  result = true;
		  }catch(Exception ex){
			  ex.printStackTrace();
			  retailerModel.setFailureReason("Unknown Error");
		  }

		  return result;
	  } 
	
	public void createOrUpdateBulkFranchiseRequiresNewTransaction(RetailerModel retailerModel, boolean isNew)  throws FrameworkCheckedException{
		try{
			BulkFranchiseModel bulkFranchiseModel = new BulkFranchiseModel();

			if(isNew){
				BeanUtils.copyProperties(bulkFranchiseModel, retailerModel);
				bulkFranchiseModel.setBulkFranchiseId(null);
				bulkFranchiseModel.setVersionNo(null);
				bulkFranchiseModel.setResult(StatusEnum.FAILURE.toString());
				bulkFranchiseModel.setCreatedOn(new Date());
				this.bulkFranchiseDAO.saveOrUpdate(bulkFranchiseModel);
			}else{
				ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
				exampleHolder.setMatchMode(MatchMode.EXACT);
				exampleHolder.setIgnoreCase(false);
				bulkFranchiseModel.setName(retailerModel.getName());
				bulkFranchiseModel.setResult(StatusEnum.FAILURE.toString());
				CustomList<BulkFranchiseModel> modelList = this.bulkFranchiseDAO.findByExample(bulkFranchiseModel, null, null, exampleHolder);
				if(modelList.getResultsetList().size() > 0){
					bulkFranchiseModel = modelList.getResultsetList().get(0);
					bulkFranchiseModel.setResult(StatusEnum.SUCCESS.toString());
					bulkFranchiseModel.setUpdatedBy(bulkFranchiseModel.getCreatedBy());
					bulkFranchiseModel.setUpdatedOn(new Date());
					this.bulkFranchiseDAO.saveOrUpdate(bulkFranchiseModel);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public List<BulkFranchiseModel> searchBulkFranchise( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
	{
	    List<BulkFranchiseModel> bulkFranchiseModelList = null;
	    BulkFranchiseModel model = (BulkFranchiseModel) wrapper.getBasePersistableModel();
        CustomList<BulkFranchiseModel> customList = bulkFranchiseDAO.findByExample( model, 
            wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );

	    if( customList != null )
	    {
	        bulkFranchiseModelList = customList.getResultsetList();
	        wrapper.setCustomList( customList );
	    }

	    return bulkFranchiseModelList;
	}

	@Override
	public List<BulkAgentUploadReportModel> searchBulkAgentUploadReport( SearchBaseWrapper wrapper ) throws FrameworkCheckedException
	{
	    List<BulkAgentUploadReportModel> bulkAgentUploadReportModelList = null;
	    BulkAgentUploadReportModel model = (BulkAgentUploadReportModel) wrapper.getBasePersistableModel();

	    CustomList<BulkAgentUploadReportModel> customList = bulkAgentUploadDAO.findByExample( model, 
            wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
	    if( customList != null )
        {
	        bulkAgentUploadReportModelList = customList.getResultsetList();
            wrapper.setCustomList( customList );
        }

	    return bulkAgentUploadReportModelList;
	}

	public BaseWrapper saveOrUpdateFranchise(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateFranchise Starts");
		  }
		  Long currentUserId = UserUtils.getCurrentUser().getAppUserId();
		  RetailerModel retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();		  
		  boolean editMode = retailerModel.getEditMode();
		  
		  ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		  ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		 
		  
		  if(editMode)
		  {
			  PartnerModel partnerModel = new PartnerModel();
		      List<PartnerModel> partnerModelList = partnerDAO.findPartnerByRetailerId(retailerModel.getRetailerId());
		      partnerModel = partnerModelList.get(0);
		      partnerModel.setName(retailerModel.getName());
		      partnerModel.setComments(retailerModel.getComments());		
	          partnerModel.setDescription(retailerModel.getDescription());	
	          partnerModel.setUpdatedBy(currentUserId);
	          partnerModel.setVersionNo(partnerModel.getVersionNo()-1);
	          partnerModel.setUpdatedOn(new Date());
	          
	          partnerDAO.saveOrUpdate(partnerModel);
		  }
		  
		  retailerModel=retailerDAO.saveOrUpdate(retailerModel);
		  
		  if(!editMode)
		  {
			  
			  
			  ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel(); 
		      concernPartnerModel.setActive(retailerModel.getActive());
			  //concernPartnerModel.setActive(Boolean.TRUE);
		      concernPartnerModel.setRetailerId(retailerModel.getRetailerId());
		      concernPartnerModel.setDescription(retailerModel.getDescription());	
		      concernPartnerModel.setConcernPartnerTypeId(ConcernPartnerTypeConstants.RETAILER);
		      concernPartnerModel.setCreatedBy(currentUserId);
		      concernPartnerModel.setCreatedOn(new Date());
		      concernPartnerModel.setName(retailerModel.getName());		
		      concernPartnerModel.setUpdatedBy(currentUserId);			
		      concernPartnerModel.setUpdatedOn(new Date());				
		      concernPartnerDAO.saveOrUpdate(concernPartnerModel);
	
	          PartnerModel partnerModel = new PartnerModel();
	          partnerModel.setName(retailerModel.getName());
	       	  partnerModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
	          partnerModel.setRetailerId(retailerModel.getRetailerId());
	          partnerModel.setActive(retailerModel.getActive());
	          //partnerModel.setActive(Boolean.TRUE);
	          partnerModel.setComments(retailerModel.getComments());
	          partnerModel.setDescription(retailerModel.getDescription());
	          partnerModel.setCreatedBy(currentUserId);
	          partnerModel.setUpdatedBy(currentUserId);				
	          partnerModel.setUpdatedOn(new Date());				
	          partnerModel.setCreatedOn(new Date());
	       
	          Long permissionGroupId = (Long)baseWrapper.getObject("permissionGroupId");
	       
	          PartnerPermissionGroupModel partnerPermissionGroupModel = new PartnerPermissionGroupModel();
	          partnerPermissionGroupModel.setPermissionGroupId(permissionGroupId);		       
	          partnerPermissionGroupModel.setCreatedBy(currentUserId);
	          partnerPermissionGroupModel.setUpdatedBy(currentUserId);
	          partnerPermissionGroupModel.setUpdatedOn(new Date());
	          partnerPermissionGroupModel.setCreatedOn(new Date());
	
	          partnerModel.addPartnerIdPartnerPermissionGroupModel(partnerPermissionGroupModel);
	          partnerDAO.saveOrUpdate(partnerModel);
		  }
		  else
		  {
			  //Long currentUserId = UserUtils.getCurrentUser().getAppUserId();
			  
			  ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			  exampleHolder.setMatchMode(MatchMode.EXACT);
			  
			  ConcernPartnerModel concernPartnerModel = new ConcernPartnerModel();
			  List<ConcernPartnerModel> listConcernPartnerModel = concernPartnerDAO.findConcernPartnerByRetailerId(retailerModel.getRetailerId());
			  concernPartnerModel = listConcernPartnerModel.get(0);
			  concernPartnerModel.setDescription(retailerModel.getDescription());
			  
			  concernPartnerModel.setName(retailerModel.getName());
			  concernPartnerModel.setUpdatedBy(currentUserId);			
		      concernPartnerModel.setUpdatedOn(new Date());
		      concernPartnerDAO.saveOrUpdate(concernPartnerModel);
		      
		      /*PartnerModel partnerModel = new PartnerModel();
		      List<PartnerModel> partnerModelList = partnerDAO.findPartnerByRetailerId(retailerModel.getRetailerId());
		      partnerModel = partnerModelList.get(0);
		      partnerModel.setName(retailerModel.getName());
		      partnerModel.setComments(retailerModel.getComments());		
	          partnerModel.setDescription(retailerModel.getDescription());	
	          partnerModel.setUpdatedBy(currentUserId);
	          partnerModel.setVersionNo(partnerModel.getVersionNo()-1);
	          partnerModel.setUpdatedOn(new Date());
	          
	          partnerDAO.saveOrUpdate(partnerModel);*/
		  }
		  
		  baseWrapper.setBasePersistableModel(retailerModel);
		  actionLogModel.setCustomField1(retailerModel.getRetailerId().toString());
		  actionLogModel.setCustomField11(retailerModel.getName());
		  this.actionLogManager.completeActionLog(actionLogModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateFranchise Ends");
		  }
		  return baseWrapper;
	  } 
	public BaseWrapper deleteFranchise(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.deleteFranchise Starts");
		  }
		  RetailerModel retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();		 
		  retailerDAO.delete(retailerModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.deleteFranchise Ends");
		  }
		  return baseWrapper;
	  }
	public Boolean isFranchiseRefExist(Long retailerId) throws FrameworkCheckedException{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.isFranchiseRefExist Starts");
		}
		List<RetailerContactModel> retailerContactList=retailerDAO.franchiseReference(retailerId);
		 if(null!=retailerContactList && retailerContactList.size()>0){
        	return Boolean.TRUE;
        }
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.isFranchiseRefExist End");
			}
		 return Boolean.FALSE;
	}
	public Boolean isAgentLevelRefExist(Long levelId) throws FrameworkCheckedException{
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.isAgentLevelRefExist Start");
			}
		List<RetailerContactModel> retailerContactList=null;
				try {
					retailerContactList=retailerDAO.findDistributorLevelReference(levelId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 if(null!=retailerContactList && retailerContactList.size()>0){
        	return Boolean.TRUE;
        }
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.isAgentLevelRefExist End");
			}
		 return Boolean.FALSE;
	}
	public Boolean isAreaNameRefExist(Long areaId) throws FrameworkCheckedException{
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.isAreaNameRefExist Start");
			}
		List<RetailerContactModel> retailerContactList=null;
		try {
			retailerContactList=areaDAO.findAreaReference(areaId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if(null!=retailerContactList && retailerContactList.size()>0){
       	return Boolean.TRUE;
       }
		/* if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.isAreaNameRefExist End");
			}*/
		 return Boolean.FALSE;
	}
	
	
	public SearchBaseWrapper loadRegHierAssociationData() throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadRegHierAssociationData Starts");
		}
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl(); 
		CustomList<DistributorModel> distributorCustomList = this.distributorDAO.findAll();
		CustomList<RegionalHierarchyModel> regionalHierarchyCustomList = this.regionalHierarchyDAO.findAll();
		List<Object> result = new ArrayList<Object>();
		if(null!=distributorCustomList)
			result.add(distributorCustomList.getResultsetList());
		 if(null!=regionalHierarchyCustomList)
			 result.add(regionalHierarchyCustomList.getResultsetList());
		 CustomList<Object> customList = new CustomList<Object>();
		 customList.setResultsetList(result);
		 searchBaseWrapper.setCustomList(customList);
		 if(logger.isDebugEnabled())
		 {
			logger.debug("AgentHierarchyManagerImpl.loadRegHierAssociationData End");
		 }
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper loadAreaNamesRefData(long regionalHierarchyId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadAreaNamesRefData Starts");
		}
		SearchBaseWrapper searchBaseWrapper= this.findRegionsByRegionalHierarchyId(regionalHierarchyId);
		List<RegionModel> regionModelList = searchBaseWrapper.getCustomList().getResultsetList();
		List<AreaModel> areaModels= areaDAO.findAreaByRegionalHierarchyId(regionalHierarchyId);
		List<Object> result = new ArrayList<Object>();
		if(null!=regionModelList)
			result.add(regionModelList);
		 if(null!=areaModels)
			 result.add(areaModels);
		 CustomList<Object> customList = new CustomList<Object>();
		 customList.setResultsetList(result);
		 searchBaseWrapper.setCustomList(customList);
		 if(logger.isDebugEnabled())
		 {
			logger.debug("AgentHierarchyManagerImpl.loadAreaNamesRefData End");
		 }
		return searchBaseWrapper;
	}
	
	public SearchBaseWrapper loadAreaLevelByRegion(long regionId) throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadAreaLevelByRegion Starts");
		}
		SearchBaseWrapper searchBaseWrapper= new SearchBaseWrapperImpl();
		AreaLevelModel model = new AreaLevelModel();
		model.setRegionId(regionId);
		List<AreaLevelModel> levelList = this.areaLevelDAO.findAreaLevelsByRegionId(regionId);
		CustomList<AreaLevelModel> customList= new CustomList<AreaLevelModel>();
		customList.setResultsetList(levelList);
		if(customList.getResultsetList().size() > 0)
		{
			searchBaseWrapper.setCustomList(customList);
		}
		if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.loadAreaLevelByRegion End");
		}
		return searchBaseWrapper;
	}
	
	public BaseWrapper saveOrUpdateAreaName(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateAreaName Starts");
		  }
		  AreaModel areaModel = (AreaModel) baseWrapper.getBasePersistableModel();
		  ActionLogModel logModel = new ActionLogModel();
		  logModel.setUsecaseId(PortalConstants.AREA_USECASE_ID);
		  logModel.setActionId(areaModel.isEditMode()?PortalConstants.ACTION_CREATE
				  :PortalConstants.ACTION_UPDATE);
		  actionLogStart(logModel);
		  	
		  areaModel=areaDAO.saveOrUpdate(areaModel);
		  baseWrapper.setBasePersistableModel(areaModel);
		  logModel.setCustomField1(areaModel.getAreaId().toString());
		  logModel.setCustomField11(areaModel.getRegionalHierarchyModel().getRegionalHierarchyName());
		  actionLogEnd(logModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.saveOrUpdateAreaName Ends");
		  }
		  return baseWrapper;
	  }
	public BaseWrapper deleteAreaName(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		 if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.deleteDistributorLevel Starts");
		  }
		  AreaModel areaModel = (AreaModel) baseWrapper.getBasePersistableModel();		 
		  areaDAO.delete(areaModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.deleteDistributorLevel Ends");
		  }
		  return baseWrapper;
	  }
	public SearchBaseWrapper searchAgentByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.searchAgentByCriteria Starts");
		  }
		  searchBaseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.KEY_SEARCH_AGENT_USECASE_ID);
		  ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,null);
		  	ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.ANYWHERE); 
			exampleHolder.setIgnoreCase(false);
			RetailerContactSearchViewModel retContactModel = (RetailerContactSearchViewModel)searchBaseWrapper.getBasePersistableModel(); 
			CustomList<RetailerContactSearchViewModel> retailerContactList = this.retailerContactSearchViewDAO.findByExample(retContactModel, null, null, exampleHolder);
			
			if (retailerContactList != null && retailerContactList.getResultsetList().size() > 0)
			{
				searchBaseWrapper.setCustomList(retailerContactList);
			}
			actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,actionLogModel);
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.searchAgentByCriteria End");
		  }		  
		  return searchBaseWrapper;
	  }
	public BaseWrapper deleteAgent(BaseWrapper baseWrapper) throws FrameworkCheckedException
	  {
		  if(logger.isDebugEnabled()){
			logger.debug("AgentHierarchyManagerImpl.deleteAgent Starts");
		  }		  
		  	RetailerContactSearchViewModel retContactModel = (RetailerContactSearchViewModel)baseWrapper.getBasePersistableModel(); 
			this.retailerContactSearchViewDAO.deleteAgent(retContactModel.getRetailerContactId());			
		  if(logger.isDebugEnabled()){
				logger.debug("AgentHierarchyManagerImpl.deleteAgent End");
		  }		  
		  return baseWrapper;
	  }

	@Override
	public boolean isHeadAgent(String mobileNo) throws FrameworkCheckedException
	{
		return retailerContactDAO.isHeadAgent(mobileNo);
	}

	@Override
	public SearchBaseWrapper searchSalesTeamComissionView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		
		SalesTeamComissionViewModel model = (SalesTeamComissionViewModel) wrapper.getBasePersistableModel();
		CustomList<SalesTeamComissionViewModel> customList;
		if(model.getTransactionId() != null){
			customList = salesTeamCommissionDAO.loadSalesTeamCommissionViewByCriteria(model.getTransactionId());
			wrapper.getPagingHelperModel().setTotalRecordsCount(customList.getResultsetList().size());
		}else{
			customList = salesTeamCommissionDAO.findByExample( model, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel() );
		}

		for (int i=0 ; i< customList.getResultsetList().size(); i++){
			if(customList.getResultsetList().get(i).getSalesTeamCommission() == null){
				customList.getResultsetList().get(i).setSalesTeamCommission(0.0);
			}
		}
        wrapper.setCustomList( customList );
        return wrapper;
	}

	@Override
	public SearchBaseWrapper findRetailContactAddresses(SearchBaseWrapper wrapper) throws FrameworkCheckedException
	{
		RetailerContactAddressesModel model = (RetailerContactAddressesModel) wrapper.getBasePersistableModel();
		CustomList<RetailerContactAddressesModel> customList = retailerContactAddressesDAO.findByExample(model);
		wrapper.setCustomList(customList); 
		return wrapper;
	}
	
	/**
	 * @author AtifHu
	 */
	@Override
	public SearchBaseWrapper findDistributorLevelsByDistributorId(
			SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		DistributorModel	distributorModel=	(DistributorModel)searchBaseWrapper.getBasePersistableModel();
		
		List<DistRegHierAssociationModel> list = distRegHierAssociationDAO.findRegionalHierarchyByDistributorId(distributorModel.getDistributorId());
		
		Collection<Long> idList	=	new ArrayList<Long>();
		
		for (DistRegHierAssociationModel item : list) {
			idList.add(item.getRegionalHierarchyId());
		}
		
		CustomList<DistributorLevelModel> customList = distributorLevelDAO.findDistributorLevelByRegionalHierarchyIdList(idList);
		
		searchBaseWrapper.setCustomList(customList);
		
		return searchBaseWrapper;
	}
	
	private void actionLogStart(ActionLogModel actionLogModel) throws FrameworkCheckedException{
		
		//ActionLogModel actionLogModel = new ActionLogModel();
		/*actionLogModel.setActionId((Long)baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId( (Long)baseWrapper.getObject(PortalConstants.KEY_USECASE_ID) );*/
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()) );
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
	}
	private void actionLogEnd(ActionLogModel actionLogModel) throws FrameworkCheckedException{
		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); 
		//actionLogModel.setCustomField1(String.valueOf(UserUtils.getCurrentUser().getAppUserId()) );
		actionLogModel = logAction(actionLogModel);
	}
	
	private ActionLogModel logAction( ActionLogModel actionLogModel ) throws FrameworkCheckedException{
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
	public void setDistributorLevelDAO(DistributorLevelDAO distributorLevelDAO) {
		this.distributorLevelDAO = distributorLevelDAO;
	}
	public void setRegionDAO(RegionDAO regionDAO) {
		this.regionDAO = regionDAO;
	}
	public void setDistributorDAO(DistributorDAO distributorDAO) {
		this.distributorDAO = distributorDAO;
	}
	
	public void setBankDAO(BankDAO bankDAO) {
		this.bankDAO = bankDAO;
	}

	public void setMnoDAO(MnoDAO mnoDAO) {
		this.mnoDAO = mnoDAO;
	}

	public void setAreaLevelDAO(AreaLevelDAO areaLevelDAO) {
		this.areaLevelDAO = areaLevelDAO;
	}	
	public void setRetailerDAO(RetailerDAO retailerDAO) {
		this.retailerDAO = retailerDAO;
	}
	public void setAreaDAO(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
	}

	public void setProductCatalogDAO(ProductCatalogDAO productCatalogDAO) {
		this.productCatalogDAO = productCatalogDAO;
	}

	public void setPartnerGroupDAO(PartnerGroupDAO partnerGroupDAO) {
		this.partnerGroupDAO = partnerGroupDAO;
	}

	public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
		this.retailerContactDAO = retailerContactDAO;
	}

	public void setAddressDAO(AddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	public void setRetailerContactAddressesDAO(
			RetailerContactAddressesDAO retailerContactAddressesDAO) {
		this.retailerContactAddressesDAO = retailerContactAddressesDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public void setRetailerSequenceGenerator(
			OracleSequenceGeneratorJdbcDAO retailerSequenceGenerator) {
		this.retailerSequenceGenerator = retailerSequenceGenerator;
	}
	public void setRetailerContactSearchViewDAO(
			RetailerContactSearchViewDAO retailerContactSearchViewDAO) {
		this.retailerContactSearchViewDAO = retailerContactSearchViewDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public SmsSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setPartnerDAO(PartnerDAO partnerDAO) {
		this.partnerDAO = partnerDAO;
	}

	public void setConcernPartnerDAO(ConcernPartnerDAO concernPartnerDAO) {
		this.concernPartnerDAO = concernPartnerDAO;
	}

	public void setAllpaySequenceGenerator(
			OracleSequenceGeneratorJdbcDAO allpaySequenceGenerator) {
		this.allpaySequenceGenerator = allpaySequenceGenerator;
	}
	
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
	public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		this.encryptionHandler = encryptionHandler;
	}

	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}
	public void setBulkAgentUploadDAO(BulkAgentUploadDAO bulkAgentUploadDAO) {
		this.bulkAgentUploadDAO = bulkAgentUploadDAO;
	}

	public void setPartnerPermissionViewDAO(
			PartnerPermissionViewDAO partnerPermissionViewDAO) {
		this.partnerPermissionViewDAO = partnerPermissionViewDAO;
	}
	
	public void setFranchiseCreator(FranchiseCreator franchiseCreator) {
		this.franchiseCreator = franchiseCreator;
	}

	public void setBulkFranchiseDAO(BulkFranchiseDAO bulkFranchiseDAO) {
		this.bulkFranchiseDAO = bulkFranchiseDAO;
	}
	
	private SmartMoneyAccountModel extractSmartMoneyModel(Long retailerContactId, String userIdUDA, BankModel bankModel,AbstractFinancialInstitution abstractFinancialInstitution) throws FrameworkCheckedException{
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		try {

			smartMoneyAccountModel = new SmartMoneyAccountModel (); 
			smartMoneyAccountModel.setRetailerContactId(retailerContactId);
			smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			smartMoneyAccountModel.setName( "i8_ret_" + userIdUDA);
			smartMoneyAccountModel.setBankId(bankModel.getBankId());
			smartMoneyAccountModel.setCreatedOn(new Date());
			smartMoneyAccountModel.setUpdatedOn(new Date());
			smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			smartMoneyAccountModel.setActive(true);
			smartMoneyAccountModel.setDefAccount(true);
			smartMoneyAccountModel.setDeleted(false);
			
			if (abstractFinancialInstitution.isVeriflyRequired()) {
				if (abstractFinancialInstitution.isVeriflyLite()) {
					smartMoneyAccountModel.setChangePinRequired(false);
				} else {
					smartMoneyAccountModel.setChangePinRequired(true);
				}
			} else {
				smartMoneyAccountModel.setChangePinRequired(false);
			}
			
//			baseWrapper.setBasePersistableModel();
			
		}catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException(
					"implementationNotSupportedException");
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException constrainViolationException = (ConstraintViolationException)ex.getCause();
				String constraintName = "UK_USER_DEVICE_TYPE";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1){
					throw new FrameworkCheckedException("Allpay Id already exists");
				}
				constraintName = "UK_SM_ACCOUNT";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1){
					throw new FrameworkCheckedException("Account nick already exists");
				}
			}
			ex.printStackTrace();
			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}
		return smartMoneyAccountModel;
	}
	
	public BankModel getOlaBankModel() {
		BankModel bankModel = new BankModel();
		bankModel.setFinancialIntegrationId(4L); // 4 is for OLA bank
		CustomList bankList = this.bankDAO.findByExample(bankModel);
		List bankL = bankList.getResultsetList();
		bankModel = (BankModel) bankL.get(0);
		return bankModel;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}
	public void setLinkPaymentModeDAO(LinkPaymentModeDAO linkPaymentModeDAO)
	{
		this.linkPaymentModeDAO = linkPaymentModeDAO;
	}
	public void setOlaVeriflyFinancialInstitution(FinancialInstitution olaVeriflyFinancialInstitution)
	{
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setRegionalHierarchyDAO(RegionalHierarchyDAO regionalHierarchyDAO) {
		this.regionalHierarchyDAO = regionalHierarchyDAO;
	}

	public void setDistRegHierAssociationDAO(
			DistRegHierAssociationDAO distRegHierAssociationDAO) {
		this.distRegHierAssociationDAO = distRegHierAssociationDAO;
	}

	public void setSalesHierarchyDAO(SalesHierarchyDAO salesHierarchyDAO) {
		this.salesHierarchyDAO = salesHierarchyDAO;
	}

	public SalesTeamCommissionDAO getSalesTeamCommissionDAO() {
		return salesTeamCommissionDAO;
	}

	public void setSalesTeamCommissionDAO(
			SalesTeamCommissionDAO salesTeamCommissionDAO) {
		this.salesTeamCommissionDAO = salesTeamCommissionDAO;
	}

	public void setKeysObj(ConfigurationContainer keysObj) {
		this.keysObj = keysObj;
	}

	@Override
	public boolean checkAgentExistsForDistributor(Long distributorId) throws FrameworkCheckedException {
		return retailerContactDAO.checkAgentExistsForDistributor(distributorId);
	}
	
	@Override
	public PartnerModel findPartnerByRetailerId(Long retailerId) throws FrameworkCheckedException{
		PartnerModel partnerModel = new PartnerModel();
		List<PartnerModel> partnerModelList = partnerDAO.findPartnerByRetailerId(retailerId);
		if(partnerModelList != null && partnerModelList.size()>0){
			partnerModel = partnerModelList.get(0);
		}
		return partnerModel;
	}
	
	@Override
	public List<SalesHierarchyModel> findSalesHierarchyByBankUser(Long bankUserId) throws FrameworkCheckedException{
		List<SalesHierarchyModel> salesHierarchyModelList = null;
		SalesHierarchyModel exampleModel = new SalesHierarchyModel();
		/*AppUserModel bankUserModel = new AppUserModel();
		bankUserModel.setAppUserId(bankUserId);
		exampleModel.setBankUserAppUserModel(bankUserModel);*/
		exampleModel.setBankUserId(bankUserId);
		CustomList<SalesHierarchyModel> customList = salesHierarchyDAO.findByExample(exampleModel);
		if(customList != null){
			salesHierarchyModelList = customList.getResultsetList();
			for(SalesHierarchyModel model : salesHierarchyModelList){
				model.setParentBankUserName(model.getParentBankUserAppUserModel().getFirstName() + " " + model.getParentBankUserAppUserModel().getLastName());
				model.setParentBankUserId(model.getParentBankUserAppUserModel().getAppUserId());
			}
		}
		return salesHierarchyModelList;
	}
	
	private SalesHierarchyHistoryModel populateSalesHierarchyHistoryModel(SalesHierarchyModel oldSalesHierarchyModel, SalesHierarchyModel newSalesHierarchyModel){
		SalesHierarchyHistoryModel salesHierarchyHistoryModel = new SalesHierarchyHistoryModel();
		
		salesHierarchyHistoryModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		salesHierarchyHistoryModel.setCreatedOn(new Date());
		salesHierarchyHistoryModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		salesHierarchyHistoryModel.setUpdatedOn(new Date());
		//salesHierarchyHistoryModel.setNewActive(newSalesHierarchyModel.getActive() == null ? false : newSalesHierarchyModel.getActive());
		salesHierarchyHistoryModel.setNewBankUserId(newSalesHierarchyModel.getBankUserId());
		salesHierarchyHistoryModel.setNewComments(newSalesHierarchyModel.getComments());
		salesHierarchyHistoryModel.setNewDescription(newSalesHierarchyModel.getDescription());
		salesHierarchyHistoryModel.setNewParentBankUserId(newSalesHierarchyModel.getParentBankUserId());
		salesHierarchyHistoryModel.setNewRoleTitle(newSalesHierarchyModel.getRoleTitle());
		salesHierarchyHistoryModel.setNewUltimateParentBankUserId(newSalesHierarchyModel.getUltimateParentBankUserId());
		
		salesHierarchyHistoryModel.setSalesHierarchyId(oldSalesHierarchyModel.getSalesHierarchyId());
		
		//salesHierarchyHistoryModel.setOldActive(oldSalesHierarchyModel.getActive() == null ? false : oldSalesHierarchyModel.getActive());
		salesHierarchyHistoryModel.setOldBankUserId(oldSalesHierarchyModel.getBankUserId());
		salesHierarchyHistoryModel.setOldComments(oldSalesHierarchyModel.getComments());
		salesHierarchyHistoryModel.setOldDescription(oldSalesHierarchyModel.getDescription());
		salesHierarchyHistoryModel.setOldParentBankUserId(oldSalesHierarchyModel.getParentBankUserId());
		salesHierarchyHistoryModel.setOldRoleTitle(oldSalesHierarchyModel.getRoleTitle());
		salesHierarchyHistoryModel.setOldUltimateParentBankUserId(oldSalesHierarchyModel.getUltimateParentBankUserId());
		//salesHierarchyHistoryModel.setVersionNo(0);
		
		return salesHierarchyHistoryModel;
	}
	
	public RetailerModel saveOrUpdateRetailerModel(RetailerModel retailerModel) {
	
		return retailerDAO.saveOrUpdate(retailerModel);
	}
	

//	public void setSalesHierarchyHistoryDAO(
//			SalesHierarchyHistoryDAO salesHierarchyHistoryDAO) {
//		this.salesHierarchyHistoryDAO = salesHierarchyHistoryDAO;
//	}
}
