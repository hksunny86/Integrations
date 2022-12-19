package com.inov8.microbank.webapp.action.commissionmodule;

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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.server.facade.portal.commissionmodule.CommissionTransactionViewFacade;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Apr 26, 2013 4:05:33 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class CommissionTransactionViewController extends BaseFormSearchController
{
    private ReferenceDataManager referenceDataManager;
    //Autowired
    private CommissionTransactionViewFacade commissionTransactionViewFacade;

    public CommissionTransactionViewController()
    {
        setCommandClass( CommissionTransactionViewModel.class );
        setCommandName( "commissionTransactionViewModel" );
    }

    @Override
    protected Map<String, Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        Map<String, Object> referenceDataMap = new HashMap<>( 3 );
        List<CommissionStakeholderModel> commissionStakeholderModelList = null;
        List<SupplierModel> supplierModelList = null;
        List<ProductModel> productModelList = null;

        try
        {
            //Load All Stakeholders
            commissionStakeholderModelList = findAllStakeholders();

            //Load All Suppliers
            supplierModelList = findAllSuppliers();

            //Load products against Supplier
            String supplierIdStr = request.getParameter("supplierId");
            productModelList = findProductsBySupplierId( supplierIdStr );
        }
        catch( Exception e )
        {
            logger.error( e.getMessage(), e );
        }
        referenceDataMap.put( "commissionStakeholderModelList", commissionStakeholderModelList );
        referenceDataMap.put("supplierModelList", supplierModelList);
        referenceDataMap.put("productModelList", productModelList);
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object command,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        List<CommissionTransactionViewModel> commissionTransactionViewModelList = null;

        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        CommissionTransactionViewModel commissionTransactionViewModel = ( (CommissionTransactionViewModel) command );
        wrapper.setBasePersistableModel( commissionTransactionViewModel );
        wrapper.setPagingHelperModel( pagingHelperModel );

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", 
            commissionTransactionViewModel.getStartDate(), commissionTransactionViewModel.getEndDate());
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put("createdOn", SortingOrder.ASC);
        }
        wrapper.setSortingOrderMap( sortingOrderMap );

        commissionTransactionViewModelList = commissionTransactionViewFacade.searchCommissionTransactionView( wrapper );

        return new ModelAndView( getFormView(), "commissionTransactionViewModelList", commissionTransactionViewModelList );
    }

    @SuppressWarnings( "unchecked" )
    private List<CommissionStakeholderModel> findAllStakeholders() throws FrameworkCheckedException
    {
        List<CommissionStakeholderModel> commissionStakeholderModelList = null;

        ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl( new CommissionStakeholderModel(), "name", SortingOrder.ASC );
        referenceDataWrapper = referenceDataManager.getReferenceData( referenceDataWrapper );
        commissionStakeholderModelList = referenceDataWrapper.getReferenceDataList();

        return commissionStakeholderModelList;
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

    public void setReferenceDataManager( ReferenceDataManager referenceDataManager )
    {
        this.referenceDataManager = referenceDataManager;
    }

    public void setCommissionTransactionViewFacade( CommissionTransactionViewFacade commissionTransactionViewFacade )
    {
        this.commissionTransactionViewFacade = commissionTransactionViewFacade;
    }

}
