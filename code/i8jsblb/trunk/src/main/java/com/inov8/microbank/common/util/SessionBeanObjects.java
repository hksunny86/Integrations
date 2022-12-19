package com.inov8.microbank.common.util;

import javax.servlet.http.HttpServletRequest;

public class SessionBeanObjects {

	public static void removeAllSessionObjects()
	{
		JSFContext.removeBean(Constants.DISTRIBUTOR_ACTION_BEAN);
		JSFContext.removeBean(Constants.REGION_ACTION_BEAN);
		JSFContext.removeBean(Constants.AREA_LEVEL_ACTION_BEAN);
		JSFContext.removeBean(Constants.DISTRIBUTORLEVEL_ACTION_BEAN);
		JSFContext.removeBean(Constants.AREA_NAME_ACTION_BEAN);
		JSFContext.removeBean(Constants.AGENT_ACTION_BEAN);
		JSFContext.removeBean(Constants.FRANCHISE_ACTION_BEAN);
		JSFContext.removeBean(Constants.AGENT_HIERARCHY_ACTION_BEAN);
		JSFContext.removeBean(Constants.SEARCH_AGENT_ACTION_BEAN);
		JSFContext.removeBean(Constants.BULK_AGENT_ACTION_BEAN);
		JSFContext.removeBean(Constants.BULK_FRANCHISE_ACTION_BEAN);
		JSFContext.removeBean(Constants.REGIONAL_HIERARCHY_ACTION_BEAN);
		JSFContext.removeBean(Constants.ASSOCIATE_REG_HIER_ACTION_BEAN);
		JSFContext.removeBean(Constants.SALES_HIERARCHY_ACTION_BEAN);
		JSFContext.removeBean(Constants.VIEW_SALES_HIERARCHY_ACTION_BEAN);
	}

	public static void removeAllCustomerSessionObjects()
	{
		JSFContext.removeBean(Constants.BULK_UPDATE_CUSTOMER_SEGMENT_ACTION_BEAN);
	}

	public static void removeAllSessionObjects(HttpServletRequest request)
	{
		
		JSFContext.removeBean(request, Constants.REGION_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.AREA_LEVEL_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.DISTRIBUTORLEVEL_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.AREA_NAME_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.AGENT_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.AGENT_HIERARCHY_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.BULK_AGENT_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.BULK_FRANCHISE_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.FRANCHISE_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.ASSOCIATE_REG_HIER_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.REGIONAL_HIERARCHY_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.SEARCH_AGENT_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.DISTRIBUTOR_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.SALES_HIERARCHY_ACTION_BEAN);
		JSFContext.removeBean(request, Constants.VIEW_SALES_HIERARCHY_ACTION_BEAN);
	}
	
}
