package com.inov8.microbank.server.service.agenthierarchy;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.BulkAgentUploadReportModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.verifly.common.model.AccountInfoModel;

import java.util.List;


public interface AgentHierarchyManager 
{
	public SearchBaseWrapper findSaleUserByBankUserId(long bankUserId) throws FrameworkCheckedException;
	public SearchBaseWrapper findAllSaleUsers() throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateSalesHierarchy(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findSaleUsersByUltimateParentSaleUserId(Long ultimateParentSaleUserId) throws FrameworkCheckedException;
	public SearchBaseWrapper findUltimateSaleUsers() throws FrameworkCheckedException;
	public SearchBaseWrapper findAllRegionalHierarchyAssociations() throws FrameworkCheckedException;
	public BaseWrapper deleteRegionalHierarchyAssociation(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findRetailersByDistributorId(long distributorId) throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateRegionalHierarchyAssociation(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findRegHierAssociatedData() throws FrameworkCheckedException;
	public SearchBaseWrapper loadRegHierAssociationData() throws FrameworkCheckedException;
	public SearchBaseWrapper findRegionalHierarchyById(long regionalHierarchyId) throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateRegionalHierarchy(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findRegionsByDistributorId(long distributorId) throws FrameworkCheckedException;
	public SearchBaseWrapper findDistributorByserviceOperatorId(long soId) throws FrameworkCheckedException;
	public Boolean doesRegionsExistOfRegionalHierarchy(Long regionalHierarchyId) throws FrameworkCheckedException;
	public Boolean doesRegionsExistOfDistributor(Long distributorId) throws FrameworkCheckedException;
	public SearchBaseWrapper searchDistributorsByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchRegionalHierarchyByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateDistributor(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper loadRefrenceData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper saveOrUpdateRegion(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findDistributorsById(long distributorId) throws FrameworkCheckedException;
	public SearchBaseWrapper findRegionsByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException;
	public SearchBaseWrapper loadDistributorLevelRefData(Long regionalHierarchyId) throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateDistributorLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper deleteDistributorLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateAreaLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findAreaLevelsByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException;
	public BaseWrapper deleteAreaLevel(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public boolean regionIntegrityCheck(long regionId) throws FrameworkCheckedException;
	public boolean areaLevelIntegrityCheck(long areaLevelId) throws FrameworkCheckedException;
	public SearchBaseWrapper loadFranchiseSearchRefData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchFranchiseByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findAllDistributors() throws FrameworkCheckedException;
	public SearchBaseWrapper findRetailersByRegionId(long regionId) throws FrameworkCheckedException;
	public SearchBaseWrapper findAgentLevelsByRegionId(long regionId) throws FrameworkCheckedException;
	public SearchBaseWrapper findAreaLevelsByRegionId(long regionId) throws FrameworkCheckedException;
	public SearchBaseWrapper loadCountry(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateFranchise(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper loadAreaLevelByRegion(long regionId) throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateAreaName(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper loadAreaNamesRefData(long distributorId) throws FrameworkCheckedException;
	public BaseWrapper deleteAreaName(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper deleteFranchise(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findAreaNamesByRegionId(long regionId) throws FrameworkCheckedException;
	public SearchBaseWrapper findAgentAreaByRegionId(long regionId) throws FrameworkCheckedException;
	public SearchBaseWrapper findAllProductCatalogs() throws FrameworkCheckedException;
	public SearchBaseWrapper findAllPartnerGroups() throws FrameworkCheckedException;
	public BaseWrapper saveOrUpdateAgent(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findAgent(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public AppUserModel loadHeadRetailerContactAppUser( Long retailerId ) throws FrameworkCheckedException;
	public void deleteAgent(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper searchAgentByCriteria(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public BaseWrapper deleteAgent(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findParentAgents(long distributorLevelId, long retailerId) throws FrameworkCheckedException;
	public RetailerContactModel findHeadAgent(long distributorLevelId, long retailerId) throws FrameworkCheckedException;
	public RetailerContactModel findRetailerContactById(long retailerContactId) throws FrameworkCheckedException;
	public AreaModel findAreaById(long areaId) throws FrameworkCheckedException;
	public boolean isMobileNumUnique(String mobileNo, Long appUserId) throws FrameworkCheckedException;
	public SearchBaseWrapper findAllRetailerContacts() throws FrameworkCheckedException;
	public DistributorLevelModel findDistributorLevelById(long distriubtorLevelId) throws FrameworkCheckedException;
	public AppUserModel findAppUserByRetailerContactId(long retailerContactId) throws FrameworkCheckedException;
	public PartnerGroupModel findPartnerGroupByAppUserId(long appUserId) throws FrameworkCheckedException;
	public SearchBaseWrapper findAllRetailers() throws FrameworkCheckedException;
	public SearchBaseWrapper findAllRegions() throws FrameworkCheckedException;
	public SearchBaseWrapper findPartnerGroupsByRetailer(Long retailerId) throws FrameworkCheckedException;
	public boolean isRetailerNameUnique(String retialerName) throws FrameworkCheckedException;
	public boolean isUserNameUnique(String userName, Long appUserId);
	public boolean isMSISDNUnique(String userName, Long retailerContactId);
	public List<RetailerContactModel> findChildRetailerContactsById(long retailerContactId) throws FrameworkCheckedException;
	public DistributorModel findDistributorsByName(String distributorName) throws FrameworkCheckedException;
	public RegionModel findRegionByName(String regionName) throws FrameworkCheckedException;
	public RetailerModel findFranchiseByName(String franchiseName) throws FrameworkCheckedException;
	public Boolean isFranchiseRefExist(Long retailerId) throws FrameworkCheckedException;
	public Boolean isAgentLevelRefExist(Long levelId) throws FrameworkCheckedException;
	public Boolean isAreaNameRefExist(Long areaId) throws FrameworkCheckedException;
	public Boolean isExistByAccountNo(AccountInfoModel model) throws Exception;
	public void saveAgentForBulkProcess(RetailerContactListViewFormModel agentFormModel) throws FrameworkCheckedException;
	public void saveOrUpdateBulkAgentReport(BulkAgentUploadReportModel bulkAgentUploadReportModel) throws FrameworkCheckedException;
	public List<RetailerModel> saveFranchiseBulk(List<RetailerModel> validatedRetailerModels) throws FrameworkCheckedException;
	public boolean saveFranchiseFrmQueue(RetailerModel retailerModel);
	public boolean isAlreadyExistsFranchiseName(String name);
	public void createOrUpdateBulkFranchiseRequiresNewTransaction(RetailerModel retailerModel, boolean result) throws FrameworkCheckedException;
	public List<BulkFranchiseModel> searchBulkFranchise(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public List<BulkAgentUploadReportModel> searchBulkAgentUploadReport(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public SearchBaseWrapper findAreaNamesByAreaLevelId(long areaLevelId) throws FrameworkCheckedException;
	public boolean isHeadAgent(String mobileNo) throws FrameworkCheckedException;
    public SearchBaseWrapper searchSalesTeamComissionView( SearchBaseWrapper wrapper ) throws FrameworkCheckedException;
    public boolean checkAgentExistsForDistributor(Long distributorId) throws FrameworkCheckedException;
    public SearchBaseWrapper findRetailContactAddresses(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
    /*added by atif hussain*/
    public SearchBaseWrapper findDistributorLevelsByDistributorId(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public PartnerModel findPartnerByRetailerId(Long retailerId)
			throws FrameworkCheckedException;
	public List<DistributorLevelModel> loadDistributorLevelByRegionIdAndRegionHierarchyId(
			Long regionId, Long regionHierarchyId) throws FrameworkCheckedException;
	public List<SalesHierarchyModel> findSalesHierarchyByBankUser(Long bankUserId)
			throws FrameworkCheckedException;
	public SearchBaseWrapper findSaleUserHistoryByBankUserId(long bankUserId)
			throws FrameworkCheckedException;
	RetailerModel loadFranchise(Long retailerId) throws FrameworkCheckedException;
	public RetailerModel saveOrUpdateRetailerModel(RetailerModel retailerModel);
}
