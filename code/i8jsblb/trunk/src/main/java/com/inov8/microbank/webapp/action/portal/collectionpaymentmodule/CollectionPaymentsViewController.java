package com.inov8.microbank.webapp.action.portal.collectionpaymentmodule;

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
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.collectionpaymentsmodule.CollectionPaymentsViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.facade.portal.collectionpaymentmodule.CollectionPaymentFacade;

public class CollectionPaymentsViewController extends BaseFormSearchController
{
    //Autowired
	private CommonFacade commonFacade;
	//Autowired
	private CollectionPaymentFacade collectionPaymentFacade;

    public CollectionPaymentsViewController()
    {
        super.setCommandName( "collectionPaymentsViewModel" );
        super.setCommandClass( CollectionPaymentsViewModel.class );
    }

	@SuppressWarnings( "unchecked" )
    @Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>(2);

		List<ProductModel> productModelList = null;
		List<SupplierProcessingStatusModel> supplierProcessingStatusModelList = null;

		ProductModel productModel = new ProductModel();
		productModel.setActive( Boolean.TRUE );

		ReferenceDataWrapper referenceDataWrapper = null;
		AppUserModel appUserModel = UserUtils.getCurrentUser();

		if( appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER.longValue() )
		{
		    productModel.setSupplierId( appUserModel.getSupplierUserIdSupplierUserModel().getSupplierId() );
		}

		try
        {
            referenceDataWrapper = new ReferenceDataWrapperImpl( productModel, "name", SortingOrder.ASC );
            commonFacade.getReferenceData(referenceDataWrapper);
            if( referenceDataWrapper.getReferenceDataList() != null )
            {
                productModelList = referenceDataWrapper.getReferenceDataList();
            }

            referenceDataWrapper = new ReferenceDataWrapperImpl( new SupplierProcessingStatusModel(), "name", SortingOrder.ASC );
            commonFacade.getReferenceData(referenceDataWrapper);
            if (referenceDataWrapper.getReferenceDataList() != null)
            {
                supplierProcessingStatusModelList = referenceDataWrapper.getReferenceDataList();
            }
        }
        catch( Exception e )
        {
            logger.error( e.getMessage(), e );
        }

        referenceDataMap.put("productModelList", productModelList);
		referenceDataMap.put("supplierProcessingStatusModelList", supplierProcessingStatusModelList);
		return referenceDataMap;
	}

	@SuppressWarnings( "unchecked" )
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object command, PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
	    CollectionPaymentsViewModel collectionPaymentsViewModel = (CollectionPaymentsViewModel) command;
	    AppUserModel appUserModel = UserUtils.getCurrentUser();
        if( appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER.longValue() )
        {
            collectionPaymentsViewModel.setSupplierId( appUserModel.getSupplierUserIdSupplierUserModel().getSupplierId() );
        }

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel( collectionPaymentsViewModel );
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        if(sortingOrderMap.isEmpty())
        {
        	sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		DateRangeHolderModel dateRangeHolderModel = collectionPaymentsViewModel.getDateRangeHolderModel();
		dateRangeHolderModel.setDatePropertyName( "createdOn" );
		searchBaseWrapper.setDateRangeHolderModel( collectionPaymentsViewModel.getDateRangeHolderModel() );

		searchBaseWrapper = collectionPaymentFacade.searchCollectionPaymentsView( searchBaseWrapper );

		CustomList<CollectionPaymentsViewModel> customList = searchBaseWrapper.getCustomList();
		List<CollectionPaymentsViewModel> collectionPaymentsViewModelList = null;
		if( customList != null )
		{
		    collectionPaymentsViewModelList = customList.getResultsetList();
		}
		return new ModelAndView( getFormView(), "collectionPaymentsViewModelList", collectionPaymentsViewModelList );
	}

	public void setCommonFacade( CommonFacade commonFacade )
    {
        this.commonFacade = commonFacade;
    }

	public void setCollectionPaymentFacade( CollectionPaymentFacade collectionPaymentFacade )
    {
        this.collectionPaymentFacade = collectionPaymentFacade;
    }

}
