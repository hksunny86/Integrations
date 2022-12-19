package com.inov8.microbank.webapp.action.portal.transactiondetail;

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
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.ExtendedTransactionDetailListViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionDetailListViewModel;
import com.inov8.microbank.server.service.portal.transactiondetail.TransactionDetailManager;


public class CustomerTransactionDetailsSearchController extends BaseFormSearchController
{

	private TransactionDetailManager transactionDetailManager;
	private ReferenceDataManager referenceDataManager;

	private final String REQUEST_ATTRIBUTE_ID = "transactionDetailModelList";
	
	public CustomerTransactionDetailsSearchController() {
		super.setCommandName("extendedTransactionDetailListViewModel");
		super.setCommandClass(ExtendedTransactionDetailListViewModel.class);
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>(3);

	    if (log.isDebugEnabled())
	    {
	      log.debug("Inside reference data");
	    }

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = httpServletRequest.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				supplierModel, "name", SortingOrder.ASC);

		try
		{		
		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<SupplierModel> supplierModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			supplierModelList = referenceDataWrapper.getReferenceDataList();
		}

		ProductModel productModel = null;
		List<ProductModel> productModelList = null;

		if ( !GenericValidator.isBlankOrNull(strSupplierId) )
		{
				Long supplierId = Long.parseLong(strSupplierId);
				productModel = new ProductModel();
				productModel.setSupplierId(supplierId);
				productModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					productModelList = referenceDataWrapper.getReferenceDataList();
				}
		}
		referenceDataMap.put("supplierModelList", supplierModelList);
		referenceDataMap.put("productModelList", productModelList);		
		// Load Data for Transaction Status
		SupplierProcessingStatusModel supplierProcessingStatusModel = new SupplierProcessingStatusModel();
		ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( supplierProcessingStatusModel, "name", SortingOrder.ASC );
		referenceDataManager.getReferenceData( refDataWrapper );
		referenceDataMap.put( "supplierProcessingStatusList", refDataWrapper.getReferenceDataList() );
		}
		catch (Exception ex)
		{
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}
	    
	    return referenceDataMap;	
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception{

		ModelAndView modelAndView = new ModelAndView(getSuccessView());
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		
		List<TransactionDetailListViewModel> resultList = null;
		
		String mfsId = httpServletRequest.getParameter("mfsId");
		String recipientId = httpServletRequest.getParameter("mfsId");		
		
		if((mfsId != null && !mfsId.isEmpty()) && 
				(recipientId != null && !recipientId.isEmpty())) {
			
			ExtendedTransactionDetailListViewModel extendedTransactionDetailListViewModel = (ExtendedTransactionDetailListViewModel) model;
			extendedTransactionDetailListViewModel.setMfsId(null);//hidden field is automatically populated in command so exclude it explicitly 
			
			DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedTransactionDetailListViewModel.getStartDate(),
					extendedTransactionDetailListViewModel.getEndDate());
			if(sortingOrderMap.isEmpty()){
	        	sortingOrderMap.put("createdOn", SortingOrder.DESC );
	        }
						
			searchBaseWrapper.setBasePersistableModel((TransactionDetailListViewModel)extendedTransactionDetailListViewModel);
			searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );	
			searchBaseWrapper.setPagingHelperModel(pagingHelperModel);       
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

			searchBaseWrapper = transactionDetailManager.searchTransactionDetail( searchBaseWrapper, mfsId );
								
            if ( searchBaseWrapper.getCustomList() != null
                 && searchBaseWrapper.getCustomList().getResultsetList() != null )
            {
                resultList = searchBaseWrapper.getCustomList().getResultsetList();
            }
        }
        else
        {
            pagingHelperModel.setTotalRecordsCount( 0 );
        }

		modelAndView.addObject(REQUEST_ATTRIBUTE_ID, resultList);

		return modelAndView;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}
	public void setTransactionDetailManager(
			TransactionDetailManager transactionDetailManager)
	{
		this.transactionDetailManager = transactionDetailManager;
	}
	
	}