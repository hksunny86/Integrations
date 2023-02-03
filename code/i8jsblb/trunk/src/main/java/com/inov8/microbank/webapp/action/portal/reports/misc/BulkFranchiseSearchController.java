package com.inov8.microbank.webapp.action.portal.reports.misc;

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
import com.inov8.microbank.common.model.BulkFranchiseModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.util.StatusEnum;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Feb 1, 2013 7:29:27 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BulkFranchiseSearchController extends BaseFormSearchController
{
    private ReferenceDataManager referenceDataManager;
    //Autowired
    private AgentHierarchyManager agentHierarchyManager;

    public BulkFranchiseSearchController()
    {
        setCommandClass( BulkFranchiseModel.class );
        setCommandName( "bulkFranchiseModel" );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        Map<String,Object> referenceDataMap = new HashMap<String,Object>();
        List<DistributorModel> distributorModelList = null;
        List<RegionModel> regionModelList = null;

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
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }
        referenceDataMap.put("distributorModelList", distributorModelList);
        referenceDataMap.put("regionModelList", regionModelList);
        referenceDataMap.put("statuses", StatusEnum.values());
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        BulkFranchiseModel bulkFranchiseModel = ( (BulkFranchiseModel) model );
        wrapper.setBasePersistableModel( bulkFranchiseModel );
        wrapper.setPagingHelperModel( pagingHelperModel );

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", 
            bulkFranchiseModel.getStartDate(), bulkFranchiseModel.getEndDate() );
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put( "createdOn", SortingOrder.DESC );
        }
        wrapper.setSortingOrderMap( sortingOrderMap );

        List<BulkFranchiseModel> bulkFranchiseModelList = agentHierarchyManager.searchBulkFranchise( wrapper );
        return new ModelAndView( getFormView(), "bulkFranchiseModelList", bulkFranchiseModelList );
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

    public void setAgentHierarchyManager( AgentHierarchyManager agentHierarchyManager )
    {
        this.agentHierarchyManager = agentHierarchyManager;
    }

    public void setReferenceDataManager( ReferenceDataManager referenceDataManager )
    {
        this.referenceDataManager = referenceDataManager;
    }

}