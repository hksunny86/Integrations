package com.inov8.microbank.webapp.action.portal.reports.marketingretailfranchise;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.portal.retailertransactionmodule.ExtendedRetailerTransactionViewModel;
import com.inov8.microbank.common.model.portal.retailertransactionmodule.RetailerTransactionViewModel;
import com.inov8.microbank.server.facade.RetailerFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

public class RegionalRetailActivitySearchController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	//Autowired
	private AgentHierarchyManager agentHierarchyManager;
	//Autowired
	private RetailerFacade retailerFacade;

	public RegionalRetailActivitySearchController()
	{
        super.setCommandName( "extendedRetailerTransactionViewModel" );
        super.setCommandClass( ExtendedRetailerTransactionViewModel.class );
	}

	@SuppressWarnings( "unchecked" )
    protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();
		List<DistributorModel> distributorModelList = null;
		List<RegionModel> senderAgentRegionModelList = null;
		List<RegionModel> receiverAgentRegionModelList = null;
		List<DistributorLevelModel> senderDistributorLevelModelList = null;
		List<DistributorLevelModel> receiverDistributorLevelModelList = null;

	    if( log.isDebugEnabled() )
	    {
	      log.debug("Inside reference data");
	    }

		ReferenceDataWrapper referenceDataWrapper = null;

		try
		{
    		//Load All Distributors
    		referenceDataWrapper = new ReferenceDataWrapperImpl( new DistributorModel(), "name", SortingOrder.ASC );
    		referenceDataManager.getReferenceData(referenceDataWrapper);
    		distributorModelList = referenceDataWrapper.getReferenceDataList();

    		//Load respective regions    		
    		String senderDistributorIdStr = request.getParameter( "senderDistributorId" );
    		senderAgentRegionModelList = findRegionsByDistributorId( senderDistributorIdStr );

    		String receiverDistributorIdStr = request.getParameter( "receiverDistributorId" );
            receiverAgentRegionModelList = findRegionsByDistributorId( receiverDistributorIdStr );
            
            //Load respective agent levels
            String senderAgentRegionIdStr = request.getParameter( "senderAgentRegionId" );
            senderDistributorLevelModelList = findAgentLevelsByRegionId( senderAgentRegionIdStr );

            String receiverAgentRegionIdStr = request.getParameter( "receiverAgentRegionId" );
            receiverDistributorLevelModelList = findAgentLevelsByRegionId( receiverAgentRegionIdStr );
            
		}
        catch( Exception ex )
        {
            log.error("Error getting product data in loadReferenceData", ex);
            ex.printStackTrace();
        }

        referenceDataMap.put("distributorModelList", distributorModelList);
        referenceDataMap.put("senderAgentRegionModelList", senderAgentRegionModelList);
        referenceDataMap.put("receiverAgentRegionModelList", receiverAgentRegionModelList);
        referenceDataMap.put("senderDistributorLevelModelList", senderDistributorLevelModelList);
        referenceDataMap.put("receiverDistributorLevelModelList", receiverDistributorLevelModelList);

	    return referenceDataMap;
	}

	@SuppressWarnings( "unchecked" )
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap ) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		ExtendedRetailerTransactionViewModel extendedRetailerTransactionViewModel = (ExtendedRetailerTransactionViewModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedRetailerTransactionViewModel.getStartDate(), extendedRetailerTransactionViewModel.getEndDate());

		searchBaseWrapper.setBasePersistableModel( (RetailerTransactionViewModel) extendedRetailerTransactionViewModel );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		//sorting order 
        if( sortingOrderMap.isEmpty() )
		{
            sortingOrderMap.put( "senderDistributorName", SortingOrder.ASC );
            sortingOrderMap.put( "senderAgentRegionName", SortingOrder.ASC );
            sortingOrderMap.put( "senderDistLevelName", SortingOrder.ASC );
            sortingOrderMap.put( "receiverDistributorName", SortingOrder.ASC );
            sortingOrderMap.put( "receiverAgentRegionName", SortingOrder.ASC );
            sortingOrderMap.put( "receiverDistLevelName", SortingOrder.ASC );
			sortingOrderMap.put( "supplierId", SortingOrder.ASC );
			sortingOrderMap.put( "productName", SortingOrder.ASC );
		}

        searchBaseWrapper.setSortingOrderMap( sortingOrderMap ); 
		searchBaseWrapper = retailerFacade.fetchRegionalRetailActivitySummary( searchBaseWrapper );
		CustomList<RetailerTransactionViewModel> customList = searchBaseWrapper.getCustomList();
		List<RetailerTransactionViewModel> retailerTransactionViewModelList = null;

		if( customList != null )
		{
		    retailerTransactionViewModelList = customList.getResultsetList();
		}

		if( retailerTransactionViewModelList != null && !retailerTransactionViewModelList.isEmpty()  )
		{
		    pagingHelperModel.setTotalRecordsCount( retailerTransactionViewModelList.size() );
		}
		else
		{
		    pagingHelperModel.setTotalRecordsCount( 0 );
		}
        return new ModelAndView( getFormView(), "retailerTransactionViewModelList", retailerTransactionViewModelList );
	}

	@SuppressWarnings( "unchecked" )
    private List<RegionModel> findRegionsByDistributorId( String distributorIdStr ) throws FrameworkCheckedException
    {
	    List<RegionModel> regionModelList = null;
        if( !GenericValidator.isBlankOrNull( distributorIdStr ) )
        {
            long distributorId = Long.parseLong( distributorIdStr );
            SearchBaseWrapper wrapper = agentHierarchyManager.findRegionsByDistributorId( distributorId );

            CustomList<RegionModel> customList = wrapper.getCustomList();
            if( customList != null )
            {
                regionModelList = customList.getResultsetList();
            }
        }
        return regionModelList;
    }

	@SuppressWarnings( "unchecked" )
    private List<DistributorLevelModel> findAgentLevelsByRegionId( String regionIdStr ) throws FrameworkCheckedException
    {
	    List<DistributorLevelModel> distributorLevelModelList = null;
	    if( !GenericValidator.isBlankOrNull( regionIdStr ) )
        {
            long regionId= Long.parseLong( regionIdStr );
            SearchBaseWrapper wrapper = agentHierarchyManager.findAgentLevelsByRegionId( regionId );

            CustomList<DistributorLevelModel> customList = wrapper.getCustomList();
            if( customList != null )
            {
                distributorLevelModelList = customList.getResultsetList();
            }
        }
	    return distributorLevelModelList;
    }
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setAgentHierarchyManager( AgentHierarchyManager agentHierarchyManager )
    {
        this.agentHierarchyManager = agentHierarchyManager;
    }

	public void setRetailerFacade( RetailerFacade retailerFacade )
    {
        this.retailerFacade = retailerFacade;
    }

}
