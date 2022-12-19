package com.inov8.microbank.webapp.action.portal.reports.salesteam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.salesmodule.ExtendedSalesTeamComissionViewModel;
import com.inov8.microbank.common.model.portal.salesmodule.SalesTeamComissionViewModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

public class SalesTeamComissionController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;

	private AgentHierarchyManager agentHierarchyManager;

	public SalesTeamComissionController()
	{
		 super.setCommandName("extendedSalesTeamComissionViewModel");
		 super.setCommandClass(ExtendedSalesTeamComissionViewModel.class);
	}

	@SuppressWarnings( "unchecked" )
    protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = request.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( supplierModel, "name", SortingOrder.ASC );

        try
        {
            referenceDataManager.getReferenceData( referenceDataWrapper );

            List<SupplierModel> supplierModelList = null;
            if( referenceDataWrapper.getReferenceDataList() != null )
            {
                supplierModelList = referenceDataWrapper.getReferenceDataList();
            }

            ProductModel productModel = null;
            List<ProductModel> productModelList = null;

            if( !GenericValidator.isBlankOrNull( strSupplierId ) )
            {
                Long supplierId = Long.parseLong( strSupplierId );
                productModel = new ProductModel();
                productModel.setSupplierId( supplierId );
                productModel.setActive( true );
                referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
                referenceDataManager.getReferenceData( referenceDataWrapper );
                if ( referenceDataWrapper.getReferenceDataList() != null )
                {
                    productModelList = referenceDataWrapper.getReferenceDataList();
                }
            }            
            referenceDataMap.put( "supplierModelList", supplierModelList );
            referenceDataMap.put( "productModelList", productModelList );
        }
        catch( Exception ex )
        {
            log.error( ex.getMessage(), ex );
        }
	    return referenceDataMap;
	}

    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
	{
    	ExtendedSalesTeamComissionViewModel salesTeamComissionViewModel = (ExtendedSalesTeamComissionViewModel) model;
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel( (SalesTeamComissionViewModel)salesTeamComissionViewModel );
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn",
				salesTeamComissionViewModel.getStartDate(), salesTeamComissionViewModel.getEndDate() );
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		//sorting order 
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		searchBaseWrapper = agentHierarchyManager.searchSalesTeamComissionView( searchBaseWrapper );

		List<SalesTeamComissionViewModel> list = null;
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		String successView = StringUtil.trimExtension( request.getServletPath() );
		return new ModelAndView( successView, "salesTeamComissionList", list );
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public AgentHierarchyManager getAgentHierarchyManager() {
		return agentHierarchyManager;
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

	

}
