package com.inov8.microbank.webapp.action.transactiondetailinfomodule;

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
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.ExtendedTransactionDetInfoListViewModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.TransactionDetInfoListViewModel;
import com.inov8.microbank.server.service.transactiondetailinfomodule.TransactionDetInfoListViewManager;

public class TransactionDetailInfoSearchController extends BaseFormSearchController 

{
	private ReferenceDataManager referenceDataManager;
	private TransactionDetInfoListViewManager transactionDetInfoListViewManager;
	private Long id;
	
	public TransactionDetailInfoSearchController() {
		setCommandName("extendedTransactionDetInfoListViewModel");
		setCommandClass(ExtendedTransactionDetInfoListViewModel.class);
	}
	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		
		Long supplierId = request.getParameter("supplierid") == null || request.getParameter("supplierid").equals("")?0:new Long(request.getParameter("supplierid"));
		
		SupplierModel supplierModel = new SupplierModel();
	    supplierModel.setActive(true);
	    ReferenceDataWrapper  referenceDataWrapper = new ReferenceDataWrapperImpl(
	        supplierModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(supplierModel);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<SupplierModel> supplierModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	      supplierModelList = referenceDataWrapper.
	          getReferenceDataList();
	    }
	    Map referenceDataMap = new HashMap();
	    referenceDataMap.put("supplierModelList",
	                         supplierModelList);
	    
	    
	    if (supplierId.longValue() != 0)
		{
		    ProductModel productModel = new ProductModel();
		    productModel.setSupplierId(supplierId) ;
		    productModel.setActive(true);
		    
		    referenceDataWrapper = new ReferenceDataWrapperImpl(
		        productModel, "name", SortingOrder.ASC);
		    referenceDataWrapper.setBasePersistableModel(productModel);
	
		    referenceDataManager.getReferenceData(referenceDataWrapper);
		    List<ProductModel> productModelList = null;
		    if (referenceDataWrapper.getReferenceDataList() != null)
		    {
		      productModelList = referenceDataWrapper.getReferenceDataList();
		    }
	
		    referenceDataMap.put("productModelList", productModelList);
		}

	    
	    BankModel bankModel = new BankModel();
	    bankModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(
				bankModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(bankModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<BankModel> bankModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			bankModelList = referenceDataWrapper.getReferenceDataList();
		}

		
		referenceDataMap.put("bankModelList", bankModelList);


		ServiceTypeModel serviceTypeModel = new ServiceTypeModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(serviceTypeModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);

	    List<ServiceTypeModel> serviceTypeModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	serviceTypeModelList = referenceDataWrapper.getReferenceDataList();
	    }	    
	    referenceDataMap.put("serviceTypeModelList", serviceTypeModelList);
	    // ********************************************************************************

	    
	    TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(transactionTypeModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);

	    List<TransactionTypeModel> transactionTypeModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	transactionTypeModelList = referenceDataWrapper.getReferenceDataList();
	    }	    
	    referenceDataMap.put("transactionTypeModelList", transactionTypeModelList);
	    // ********************************************************************************

	    return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse arg1, Object object, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		// TODO Auto-generated method stub

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		//TransactionDetInfoListViewModel transactionDetInfoListViewModel = (TransactionDetInfoListViewModel) object;
		ExtendedTransactionDetInfoListViewModel extendedTransactionDetInfoListViewModel = (ExtendedTransactionDetInfoListViewModel) object;
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedTransactionDetInfoListViewModel.getStartDate(),
				extendedTransactionDetInfoListViewModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setBasePersistableModel((TransactionDetInfoListViewModel)extendedTransactionDetInfoListViewModel);
		searchBaseWrapper = this.transactionDetInfoListViewManager.searchAllTransaction(searchBaseWrapper);

		
		return new ModelAndView(super.getSuccessView(), "transactionDetInfoListViewModelList", searchBaseWrapper.getCustomList()
				.getResultsetList());



	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetInfoListViewManager(
			TransactionDetInfoListViewManager transactionDetInfoListViewManager) {
		this.transactionDetInfoListViewManager = transactionDetInfoListViewManager;
	}

}
