package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.IntgTransactionTypeModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionNadraViewModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 23, 2012 12:56:09 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class PostedTransactionsNadraSearchController extends BaseFormSearchController
{
    //Autowired
    private ReferenceDataManager referenceDataManager;

    //Autowired
    private PostedTransactionReportFacade postedTransactionReportFacade;

    public PostedTransactionsNadraSearchController()
    {
        setCommandName( "postedTransactionNadraViewModel" );
        setCommandClass( PostedTransactionNadraViewModel.class );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected Map<String, Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        Map<String, Object> refDataMap = new HashMap<String, Object>( 2 );

        //BasePersistableModel
        IntgTransactionTypeModel intgTransactionTypeModel = new IntgTransactionTypeModel();
        intgTransactionTypeModel.setProductIntgModuleInfo( new ProductIntgModuleInfoModel( PortalConstants.PRODUCT_INTG_MODULE_INFO_ID_NADRA ) );

        List<IntgTransactionTypeModel> list = new ArrayList<IntgTransactionTypeModel>(0);
        list.add(intgTransactionTypeModel);        
        
        List<IntgTransactionTypeModel> intgTransactionTypeModelList = postedTransactionReportFacade.fetchIntgTransactionTypes( list, "transactionType", SortingOrder.ASC, null );

        ProductModel productModel = new ProductModel();
        productModel.setActive( true );
        productModel.setProductIntgModuleInfoId( PortalConstants.PRODUCT_INTG_MODULE_INFO_ID_NADRA );
        ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData( refDataWrapper );
        List<ProductModel> productModelList = refDataWrapper.getReferenceDataList();

        refDataMap.put( "intgTransactionTypeModelList", intgTransactionTypeModelList );
        refDataMap.put( "productModelList", productModelList );

        return refDataMap;
    }

    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object command,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        PostedTransactionNadraViewModel postedTransactionNadraViewModel = (PostedTransactionNadraViewModel) command;

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );
        searchBaseWrapper.setBasePersistableModel( postedTransactionNadraViewModel );

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", 
            postedTransactionNadraViewModel.getStartDate(), postedTransactionNadraViewModel.getEndDate() );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        //sorting order 
        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put( "createdOn", SortingOrder.DESC );
        }
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

        List<PostedTransactionNadraViewModel> postedTransactionNadraViewModelList = postedTransactionReportFacade.searchPostedTransactionNadraView( searchBaseWrapper );
        return new ModelAndView( getSuccessView(), "postedTransactionNadraViewModelList", postedTransactionNadraViewModelList );
    }

    public void setReferenceDataManager( ReferenceDataManager referenceDataManager )
    {
        this.referenceDataManager = referenceDataManager;
    }

    public void setPostedTransactionReportFacade( PostedTransactionReportFacade postedTransactionReportFacade )
    {
        this.postedTransactionReportFacade = postedTransactionReportFacade;
    }

}
