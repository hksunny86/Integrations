package com.inov8.microbank.webapp.action.portal.reports.agents;

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
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.portal.agentreportsmodule.BbAccountsByAgentsViewModel;
import com.inov8.microbank.server.facade.portal.agentreportsmodule.AgentReportsFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:29:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BbAccountsByAgentsViewController extends BaseFormSearchController
{
    private ReferenceDataManager referenceDataManager;
    //Autowired
    private AgentHierarchyManager agentHierarchyManager;
    //Autowired
    private AgentReportsFacade agentReportsFacade;

    public BbAccountsByAgentsViewController()
    {
        setCommandClass( BbAccountsByAgentsViewModel.class );
        setCommandName( "bbAccountsByAgentsViewModel" );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        Map<String,Object> referenceDataMap = new HashMap<String,Object>();
        List<DistributorModel> distributorModelList = null;
        List<RegionModel> regionModelList = null;
        List<RetailerModel> retailerModelList = null;

        ReferenceDataWrapper referenceDataWrapper = null;
        try
        {
            //Load All Distributors
            referenceDataWrapper = new ReferenceDataWrapperImpl( new DistributorModel(), "name", SortingOrder.ASC );
            referenceDataManager.getReferenceData(referenceDataWrapper);
            distributorModelList = referenceDataWrapper.getReferenceDataList();

            //Load respective regions
            String distributorIdStr = request.getParameter( "distributorId" );
            regionModelList = findRegionsByDistributorId( distributorIdStr );

            //Load respective Franchises
            String regionIdStr = request.getParameter( "regionId" );
            retailerModelList = findRetailersByRegionId( regionIdStr );
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }
        referenceDataMap.put("distributorModelList", distributorModelList);
        referenceDataMap.put("regionModelList", regionModelList);
        referenceDataMap.put("retailerModelList", retailerModelList);
        return referenceDataMap;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        BbAccountsByAgentsViewModel bbAccountsByAgentsViewModel = ( (BbAccountsByAgentsViewModel) model );
        wrapper.setBasePersistableModel( bbAccountsByAgentsViewModel );
        wrapper.setPagingHelperModel( pagingHelperModel );

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", 
            bbAccountsByAgentsViewModel.getStartDate(), bbAccountsByAgentsViewModel.getEndDate());
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put( "createdOn", SortingOrder.DESC );
        }
        wrapper.setSortingOrderMap( sortingOrderMap );

        wrapper = agentReportsFacade.searchBbAccountsByAgentsView( wrapper );

        CustomList<BbAccountsByAgentsViewModel> customList = wrapper.getCustomList();
        List<BbAccountsByAgentsViewModel> bbAccountsByAgentsViewModelList = null;
        if( customList != null )
        {
            bbAccountsByAgentsViewModelList = customList.getResultsetList();
        }

        return new ModelAndView( getFormView(), "bbAccountsByAgentsViewModelList", bbAccountsByAgentsViewModelList );
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
    private List<RetailerModel> findRetailersByRegionId( String regionIdStr ) throws FrameworkCheckedException
    {
        List<RetailerModel> retailerModelList = null;
        if( !GenericValidator.isBlankOrNull( regionIdStr ) )
        {
            long regionId = Long.parseLong( regionIdStr );
            SearchBaseWrapper wrapper = agentHierarchyManager.findRetailersByRegionId( regionId );

            CustomList<RetailerModel> customList = wrapper.getCustomList();
            if( customList != null )
            {
                retailerModelList = customList.getResultsetList();
            }
        }
        return retailerModelList;
    }

    public void setAgentHierarchyManager( AgentHierarchyManager agentHierarchyManager )
    {
        this.agentHierarchyManager = agentHierarchyManager;
    }

    public void setReferenceDataManager( ReferenceDataManager referenceDataManager )
    {
        this.referenceDataManager = referenceDataManager;
    }

    public void setAgentReportsFacade( AgentReportsFacade agentReportsFacade )
    {
        this.agentReportsFacade = agentReportsFacade;
    }

}
