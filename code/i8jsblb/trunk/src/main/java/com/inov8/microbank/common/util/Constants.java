package com.inov8.microbank.common.util;

public class Constants 
{
	
	public static final String IMAGE_PATH = "";
	public static final Long ADDRESS_TYPE_PRESENT_HOME= 1L;
	public static final Long ADDRESS_TYPE_PERMANENT_HOME= 2L;
	public static final Long ADDRESS_TYPE_BUSINESS= 3L;
	
	public static final Long MOBILE_TYPE_POSTPAID= 1L;
	public static final Long MOBILE_TYPE_PREPAID= 2L;
	
	/* Managed Bean Names */
    public static final String DISTRIBUTOR_ACTION_BEAN = "distributorActionBean";
    public static final String REGION_ACTION_BEAN = "regionActionBean";
    public static final String AREA_LEVEL_ACTION_BEAN = "areaLevelActionBean";
    public static final String DISTRIBUTORLEVEL_ACTION_BEAN = "agentLevelBean";
    public static final String AREA_NAME_ACTION_BEAN = "areaNameActionBean";
    public static final String AGENT_ACTION_BEAN = "agentActionBean";
    public static final String FRANCHISE_ACTION_BEAN = "retailerBean";
    public static final String AGENT_HIERARCHY_ACTION_BEAN = "agentHierarchyActionBean";
    public static final String SEARCH_AGENT_ACTION_BEAN = "agentSearchBean";
    public static final String BULK_AGENT_ACTION_BEAN = "agentBulkActionBean";
    public static final String BULK_FRANCHISE_ACTION_BEAN = "bulkRetailerBean";
    public static final String REGIONAL_HIERARCHY_ACTION_BEAN = "regionalHierarchyActionBean";
    public static final String ASSOCIATE_REG_HIER_ACTION_BEAN = "associateRegHierActionBean";
    public static final String SALES_HIERARCHY_ACTION_BEAN = "salesHierarchyActionBean";
    public static final String VIEW_SALES_HIERARCHY_ACTION_BEAN = "viewSaleHierarchyActionBean";

    //Customer Session Objects
	public static final String BULK_UPDATE_CUSTOMER_SEGMENT_ACTION_BEAN = "bulkCustomerSegmentActionBean";

    /* Navigation Rules */
    public class ReturnCodes
    {
    	public static final String SEARCH_REG_HIER_ADD_REG_HIER = "addRegionalHierarchy";
        public static final String SEARCH_DIST_ADD_DIST = "addDistributor";
        public static final String REGIONAL_HIERARCHY_REGION = "addRegion";
        public static final String REGION_REG_HIER = "addRegionalHierarchy";
        public static final String REGION_AREA_LEVEL = "addAreaLevel";
        public static final String AREA_LEVEL_AGENT_LEVEL = "addAgentLevel";
        public static final String AREA_LEVEL_REGION = "addRegion";
        public static final String AGENT_LEVEL_AREA_LEVEL = "addAreaLevel";
        public static final String CANCEL_AGENT_NETWORK = "searchDistributors";
        public static final String CANCEL = "cancel";
        public static final String CANCEL_REG_HIER_ASSOCIATIONS = "associateRegionalHierarchy";
        public static final String CANCEL_AGENT = "searchAgent"; 
        
        // Franchise
        public static final String SEARCH_FRANCHISE_ADD_BRANCH = "addFranchise";
        public static final String CANCEL_FRANCHISE = "searchFranchise";
        public static final String AGENT_LEVEL_AREA_NAME = "addAreaName";
        public static final String AREA_NAME_AGENT_LEVEL = "addAgentLevel";
        public static final String AREA_NAME_FINISH = "searchRegionalHierarchy";
        public static final String CANCEL_REG_HIER = "searchRegionalHierarchy";
        
        // Agent
        public static final String SEARCH_AGENT_ADD_AGENT = "addAgent";
        public static final String AGENT_HIERARCHY_ADD_AGENT = "addAgent";
        
        public static final String CREATE_SALES_HIERARCHY = "createSalesHierarchy";
    }
    
    
    /* Error Messages */
    public class ErrorMessages
    {
    	// Distributor
		public static final String AH_DIST_SERVICE_OPERATOR_ERROR="Service Operator is required.";
    	public static final String AH_DIST_DISTNAME_ERROR="Agent Network Name is required.";
    	public static final String AH_REG_HIER_NAME_ERROR="Regional Hierarchy Name is required.";
    	public static final String AH_SALE_USER_NAME_ERROR="Sale User Name is required.";
    	public static final String AH_ROLE_TITLE_ERROR="Role Title is required.";
    	public static final String AH_PARENT_SALE_USER_REQ_ERROR="Parent Sale User is required.";
    	public static final String AH_SALE_USER_ALREADY_ASSOCIATED_ERROR="Sale User is already associated to hierarchy.";
    	public static final String AH_DIST_CONTNAME_ERROR="Contact Name is required.";
    	public static final String AH_DIST_ADDRESS_ERROR="Adress1 is required.";
    	public static final String AH_DIST_EMAILFORMAT_ERROR="Provide Correct Email Address."; 
    	public static final String AH_DIST_ACTIVE_ERROR="Agent Network cannot be in-active due to reference data found in the system.";
    	public static final String AH_REG_HIER_ACTIVE_ERROR="Regional Hierarchy cannot be in-active due to reference data found in the system.";
    	    	
    	//	Region
    	public static final String AH_REGION_REGION_NAME_REQ_ERROR="Region name is required.";
    	public static final String AH_REGION_REGION_NAME_DUP_PRE_ERROR="Region name ";
    	public static final String AH_REGION_REGION_NAME_DUP_POST_ERROR=" is duplicated.";
    	public static final String AH_REGION_REGION_DELETE_FAILURE_ERROR= "Region cannot be deleted due to reference data found in the system.";
    	public static final String AH_REGION_REGION_INACTIVE_ERROR= "Region cannot be in-active due to reference data found in the system.";
    	public static final String AH_REGION_SAVE_INLINE_ROW_ERROR= "Please save inline row.";
    	public static final String AH_REGION_REGION_ONE_REQ_ERROR= "Please enter at least one region to proceed.";
    	
    	
    	// Area Level
    	public static final String AH_AREALEVEL_REGION_NAME_REQ_ERROR="Region name is required.";
    	public static final String AH_AREALEVEL_AREA_LEVEL_NAME_REQ_ERROR="Area level name is required.";
    	public static final String AH_AREALEVEL_AREA_LEVEL_NAME_DUP_ERROR="Area level name is duplicated under one parent area level and region.";
    	public static final String AH_AREALEVEL_PARENT_AREA_LEVEL_REQ_ERROR="Parent Area level is required.";
    	public static final String AH_AREALEVEL_REFERENCE_FOUND_ERROR="Area Level, has child node(s), cannot be deleted.";
    	public static final String AH_AREALEVEL_SAVE_INLINE_ROW_ERROR="Please save inline row.";
    	public static final String AH_AREALEVE_AREALEVE_INACTIVE_ERROR= "Area Level cannot be in-active due to reference data found in the system.";
    	
    	
    	//  Distributor Level
    	public static final String AH_DISTLEVEL_LEVELNAME_ERROR="Agent Level Name is required.";
    	public static final String AH_DISTLEVEL_Region_ERROR="Region is required.";
    	public static final String AH_DISTLEVEL_PARENTLEVELNAME_ERROR="Parent Agent Level Name is required.";
    	public static final String AH_DISTLEVEL_ULTIMATE_PARENTLEVELNAME_ERROR="Ultimate Agent Level Name is required.";
    	public static final String AH_DISTLEVEL_LEVELNAMEEXIST_ERROR="Agent level has child node cannot create multiple childs.";
    	public static final String AH_DISTLEVEL_REF_EXIST_ERROR="Record already used in system cannot be deleted.";
    	
    //  Area Name
    	public static final String AH_AREANAME_AREANAME_ERROR="Area Name is required.";
    	public static final String AH_AREANAME_REGION_ERROR="Region is required.";
    	public static final String AH_AREANAME_AREALEVELNAME_ERROR="Area Level Name is required.";
    	public static final String AH_AREANAME_AREANAMEEXIST_ERROR="Area has child node(s) cannot be deleted.";
    	public static final String AH_AREANAME_REF_EXIST_ERROR="Record already used in system cannot be deleted.";
    	public static final String AH_AREANAME_PARENT_EXIST_ERROR="Parent Level Area have no record.";
    	
    	
    	// Franchise
    	public static final String AH_FRANCHISE_DISTNAME_ERROR="Agent Network Name is required.";
    	public static final String AH_FRANCHISE_CONTNAME_ERROR="Contact Name is required.";
    	public static final String AH_FRANCHISE_ADDRESS_ERROR="Current Address is required.";
    	public static final String AH_FRANCHISE_BRANCHNAME_ERROR="Franchise/Branch Name is required."; 
    	public static final String AH_FRANCHISE_COUNTRY_ERROR="Country is required.";
    	public static final String AH_FRANCHISE_PROVINCE_ERROR="Province is required.";
    	public static final String AH_FRANCHISE_CITY_ERROR="City is required.";
    	public static final String AH_FRANCHISE_CONTACTNO_ERROR="Contact number is required."; 
    	public static final String AH_FRANCHISE_REGION_NAME_REQ_ERROR="Region name is required.";
    	public static final String AH_FRANCHISE_INACTIVE_ERROR= "Franchise cannot be in-active due to reference data found in the system.";
    	
    	// Search Agent
    	public static final String AH_SEARCHAGENT_AGENTREFERENCEEXIST_ERROR="Agent has reference in the system cannot be deleted.";
    	
    	// Agent
    	public static final String AH_AGENT_ACTIVE_ERROR="Agent cannot be in-active due to active child agents found.";
    	
    	public static final String AH_REG_HIER_ASSOCIATION_SAVE_INLINE_ROW_ERROR= "Please save inline row.";
    	public static final String AH_REG_HIER_ASSOCIATION_REFERENCE_FOUND_ERROR="Regional Hierarchy association has referenced data, cannot be deleted.";
    	public static final String AH_REG_HIER_ASSOCIATION_SAVE_INTEGRITY_ERROR= "Regional Hierarchy association cannot be updated due to child data found.";
    }
    
    /* Info Messages */
    public class InfoMessages
    {        
    	public static final String AH_DIST_DISTNAME_INFO="Agent Network Name is already exist.";
    	public static final String AH_REG_HIER_NAME_INFO="Regional Hierarchy Name is already exist.";
    	public static final String AH_DIST_DISTSAVE_INFO="Agent Network Information has been saved/updated successfully.";
    	public static final String AH_REG_HIER_SAVE_INFO="Regional Hierarchy Information has been saved/updated successfully.";
    	public static final String AH_REGION_REGION_SAVE_SUCCESS_INFO= "Region Information has been saved/updated successfully.";
    	public static final String AH_SALES_HIER_SAVE_INFO="Sales Hierarchy Information has been saved/updated successfully.";
    	
    	//  Distributor Level
    	public static final String AH_DISTLEVEL_DELETE_FAILURE_INFO="Record has references in the System.";
    	public static final String AH_DISTLEVEL_EDIT_FAILURE_INFO="Please save inline row.";
    	public static final String AH_DISTLEVEL_SAVE_SUCCESS_INFO= "Agent Level has been saved/updated successfully.";
    	public static final String AH_DISTLEVEL_DELETE_SUCCESS_INFO= "Agent Level has been deleted successfully.";
    	
    	public static final String AH_AREALEVEL_SAVED_SUCCESS_INFO= "Area Level has been saved successfully.";
    	public static final String AH_REG_HIER_ASSOCIATION_SAVED_SUCCESS_INFO= "Regional Hierarchy has been associated with distributor and saved successfully.";
    	public static final String AH_REG_HIER_ASSOCIATION_UPDATE_SUCCESS_INFO= "Regional Hierarchy has been associated with distributor and updated successfully.";
    	public static final String AH_AREALEVEL_UPDATED_SUCCESS_INFO= "Area Level has been updated successfully.";
    	public static final String AH_AREALEVEL_DELETED_SUCCESS_INFO= "Area Level has been deleted successfully.";
    	
    	//Franchise
    	public static final String AH_FRANCHISE_SAVE_SUCCESS_INFO= "Franchise has been saved/updated successfully.";
    	
    	//Search Agent
    	public static final String AH_SEARCHAGENT_REMOVE_SUCCESS_INFO= "Agent has been deleted successfully.";
    	
    	public static final String AH_AREANAME_SAVE_SUCCESS_INFO= "Area Name has been saved/updated successfully.";
    	public static final String AH_AREANAME_DELETE_SUCCESS_INFO= "Area Name has been deleted successfully.";
    	
    	public static final String AH_REG_HIER_ASSOCIATION_DELETED_SUCCESS_INFO= "Regional Hierarchy Association has been deleted successfully.";
    }
    
}
