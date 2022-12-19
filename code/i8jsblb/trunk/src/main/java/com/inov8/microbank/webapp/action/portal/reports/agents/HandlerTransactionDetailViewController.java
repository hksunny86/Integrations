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
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.portal.agentreportsmodule.HandlerTransactionDetailViewModel;
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
public class HandlerTransactionDetailViewController extends BaseFormSearchController
{
    private ReferenceDataManager referenceDataManager;
    //Autowired
    private AgentHierarchyManager agentHierarchyManager;
    //Autowired
    private AgentReportsFacade agentReportsFacade;

    public HandlerTransactionDetailViewController()
    {
        setCommandClass( HandlerTransactionDetailViewModel.class );
        setCommandName( "handlerTransactionDetailViewModel" );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected Map<String,Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        Map<String,Object> referenceDataMap = new HashMap<String,Object>();
        List<DistributorModel> distributorModelList = null;
        List<RegionModel> regionModelList = null;
        List<RetailerModel> retailerModelList = null;
        List<SupplierModel> supplierModelList = null;
        List<ProductModel> productModelList = null;

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

            //Load All Suppliers
            supplierModelList = findAllSuppliers();

            String supplierIdStr = request.getParameter("supplierId");
            productModelList = findProductsBySupplierId( supplierIdStr );
        }
        catch( Exception e )
        {
            log.error( e.getMessage(), e );
        }
        referenceDataMap.put("distributorModelList", distributorModelList);
        referenceDataMap.put("regionModelList", regionModelList);
        referenceDataMap.put("retailerModelList", retailerModelList);
        referenceDataMap.put("supplierModelList", supplierModelList);
        referenceDataMap.put("productModelList", productModelList);
        return referenceDataMap;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object model,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        HandlerTransactionDetailViewModel handlerTransactionDetailViewModel = ( (HandlerTransactionDetailViewModel) model );
        wrapper.setBasePersistableModel( handlerTransactionDetailViewModel );
        wrapper.setPagingHelperModel( pagingHelperModel );

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", 
        		handlerTransactionDetailViewModel.getStartDate(), handlerTransactionDetailViewModel.getEndDate());
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }
        wrapper.setSortingOrderMap( sortingOrderMap );

        wrapper = agentReportsFacade.searchHandlerTransactionDetailView( wrapper );

        CustomList<HandlerTransactionDetailViewModel> customList = wrapper.getCustomList();
        List<HandlerTransactionDetailViewModel> handlerTransactionDetailViewModelList = null;
        if( customList != null )
        {
            handlerTransactionDetailViewModelList = customList.getResultsetList();
        }

        return new ModelAndView( getFormView(), "handlerTransactionDetailViewModelList", handlerTransactionDetailViewModelList );
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

    @SuppressWarnings( "unchecked" )
    private List<SupplierModel> findAllSuppliers() throws FrameworkCheckedException
    {
        List<SupplierModel> supplierModelList = null;

        SupplierModel supplierModel = new SupplierModel();
        supplierModel.setActive(true);
        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( supplierModel, "name", SortingOrder.ASC );
        referenceDataWrapper = referenceDataManager.getReferenceData( referenceDataWrapper );
        supplierModelList = referenceDataWrapper.getReferenceDataList();
        return supplierModelList;
    }

    @SuppressWarnings( "unchecked" )
    private List<ProductModel> findProductsBySupplierId( String supplierIdStr ) throws FrameworkCheckedException
    {
        List<ProductModel> productModelList = null;
        if( !GenericValidator.isBlankOrNull( supplierIdStr ) )
        {
            long supplierId = Long.parseLong( supplierIdStr );

            ProductModel productModel = new ProductModel();
            productModel.setSupplierId( supplierId );
            productModel.setActive( true );
            ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
            referenceDataManager.getReferenceData( referenceDataWrapper );
            if ( referenceDataWrapper.getReferenceDataList() != null )
            {
                productModelList = referenceDataWrapper.getReferenceDataList();
            }
        }
        return productModelList;
    }

    public void setAgentReportsFacade( AgentReportsFacade agentReportsFacade )
    {
        this.agentReportsFacade = agentReportsFacade;
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
