package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.OperatorUserModel;
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.ExtendedPostedTransactionViewModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.IntgTransactionTypeModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionViewModel;
import com.inov8.microbank.common.model.usergroupmodule.CustomUserPermissionViewModel;
import com.inov8.microbank.common.util.AccessLevelConstants;
import com.inov8.microbank.common.util.IntgTransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.SupplierConstants;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 23, 2012 12:56:09 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class PostedTransactionsPheonixSearchController extends BaseFormSearchController
{
    //Autowired
    private ReferenceDataManager referenceDataManager;

    //Autowired
    private PostedTransactionReportFacade postedTransactionReportFacade;

    public PostedTransactionsPheonixSearchController()
    {
        setCommandName( "extendedPostedTransactionViewModel" );
        setCommandClass( ExtendedPostedTransactionViewModel.class );
    }

    @Override
    protected Map<String, Object> loadReferenceData( HttpServletRequest request ) throws Exception
    {
        Map<String, Object> refDataMap = new HashMap<String, Object>( 2 );
        List<IntgTransactionTypeModel> intgTransactionTypeModelList = null;

        //BasePersistableModel
        IntgTransactionTypeModel intgTransactionTypeModel = new IntgTransactionTypeModel();
        intgTransactionTypeModel.setSwitchRef( new SwitchModel( SwitchConstants.CORE_BANKING_SWITCH ) );
        intgTransactionTypeModel.setProductIntgModuleInfo( new ProductIntgModuleInfoModel( 4L  ) ); //4 refers to LescoBillPaymentDispenser
        
        IntgTransactionTypeModel intgTransactionTypeModel2 = new IntgTransactionTypeModel();
        intgTransactionTypeModel2.setSwitchRef( new SwitchModel( SwitchConstants.CORE_BANKING_SWITCH ) );
        intgTransactionTypeModel2.setProductIntgModuleInfo( new ProductIntgModuleInfoModel( 38L  ) ); //38 refers to Transfer In/Out

        IntgTransactionTypeModel intgTransactionTypeModel3 = new IntgTransactionTypeModel();
        intgTransactionTypeModel3.setSwitchRef( new SwitchModel( SwitchConstants.OLA_SWITCH ) );
        intgTransactionTypeModel3.setProductIntgModuleInfo( new ProductIntgModuleInfoModel( 13L  ) ); //13 refers to Incoming IBFT Title Fetch

        IntgTransactionTypeModel intgTransactionTypeModel4 = new IntgTransactionTypeModel();
        intgTransactionTypeModel4.setSwitchRef( new SwitchModel( SwitchConstants.OLA_SWITCH ) );
        intgTransactionTypeModel4.setProductIntgModuleInfo( new ProductIntgModuleInfoModel( 16L  ) ); //16 refers to Incoming IBFT OLA Credit

        List<IntgTransactionTypeModel> list = new ArrayList<IntgTransactionTypeModel>(0);
        list.add(intgTransactionTypeModel);
        list.add(intgTransactionTypeModel2);
        list.add(intgTransactionTypeModel3);
        list.add(intgTransactionTypeModel4);
        
        AppUserModel appUserModel = UserUtils.getCurrentUser();
        OperatorUserModel operatorUser= appUserModel.getOperatorUserIdOperatorUserModel();
        Collection<CustomUserPermissionViewModel> userPermissionList = appUserModel.getUserPermissionList();
        boolean isCsr = operatorUser!=null && !UserUtils.isInPermissionList(userPermissionList,AccessLevelConstants.ADMIN_GP_USER);
      
        intgTransactionTypeModelList = postedTransactionReportFacade.fetchIntgTransactionTypes( list, "transactionType", SortingOrder.ASC, null );
       
        ProductModel productModel = new ProductModel();
   		if( isCsr ) {       
	    	productModel.setSupplierId(SupplierConstants.TransReportPhonixCSRViewSupplierID);
	    } 
        productModel.setActive( true );
        ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
        referenceDataManager.getReferenceData( refDataWrapper );
        List<ProductModel> productModelList = refDataWrapper.getReferenceDataList();

        //Remove Act to Act product
        final long productIdActToAct = 50000L;
        Iterator<ProductModel> productModelItr = productModelList.iterator();
        while( productModelItr.hasNext() )
        {
            ProductModel prodModel = productModelItr.next();
            if( productIdActToAct == prodModel.getProductId() )
            {
                productModelItr.remove();
                break;
            }
        }

        refDataMap.put( "intgTransactionTypeModelList", intgTransactionTypeModelList );
        refDataMap.put( "productModelList", productModelList );

        return refDataMap;
    }

    @Override
    protected ModelAndView onSearch( HttpServletRequest request, HttpServletResponse response, Object command,
                                     PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        ExtendedPostedTransactionViewModel extendedPostedTransactionViewModel = (ExtendedPostedTransactionViewModel) command;

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );
        searchBaseWrapper.setBasePersistableModel( (PostedTransactionViewModel) command );

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", 
            extendedPostedTransactionViewModel.getStartDate(), extendedPostedTransactionViewModel.getEndDate() );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        //sorting order 
        if( sortingOrderMap.isEmpty() )
        {
            sortingOrderMap.put( "createdOn", SortingOrder.DESC );
        }
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

        CustomList<PostedTransactionViewModel> customList = postedTransactionReportFacade.searchPostedTransactionView( searchBaseWrapper );
        searchBaseWrapper.setCustomList( customList );

        List<PostedTransactionViewModel> postedTransactionViewModelList = null;
        if( customList != null )
        {
            postedTransactionViewModelList = customList.getResultsetList();
        }
        return new ModelAndView( getSuccessView(), "postedTransactionViewModelList", postedTransactionViewModelList );
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
