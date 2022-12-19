package com.inov8.microbank.webapp.action.portal.transactiondetaili8module.allpay;

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
import com.inov8.microbank.common.model.AllpayTransDetListModel;
import com.inov8.microbank.common.model.AllpayTransDetListModelExtended;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.allpay.AllPayTransactionDetailI8Manager;

public class TransactionDetailI8SearchController extends BaseFormSearchController
{
	ReferenceDataManager referenceDataManager;
	AllPayTransactionDetailI8Manager allPayTransactionDetailI8Manager;

	
	public TransactionDetailI8SearchController()
	{
		 super.setCommandName("extendedTransactionDetailPortalListModel");
		 super.setCommandClass(AllpayTransDetListModelExtended.class);
	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		Map referenceDataMap = new HashMap();

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

		if (null != strSupplierId && !"".equals(strSupplierId))
		{
				Long supplierId = Long.parseLong(strSupplierId);
				productModel = new ProductModel();
				productModel.setSupplierId(supplierId);
				productModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					productModelList = referenceDataWrapper.getReferenceDataList();
				}
		}
		referenceDataMap.put("supplierModelList", supplierModelList);
		referenceDataMap.put("productModelList", productModelList);
	    
	    
	    //Load Reference Data For Bank....
	    
	    BankModel bankModel = new BankModel();
	    bankModel.setActive(true);
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	        bankModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(bankModel);

	    referenceDataManager.getReferenceData(referenceDataWrapper);

	    List<BankModel> bankModelList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	      bankModelList = referenceDataWrapper.
	          getReferenceDataList();
	    }
	    referenceDataMap.put("bankModelList", bankModelList);
	    
	    //Load Reference Data for Payment Mode
	    
	    PaymentModeModel paymentModeModel = new PaymentModeModel();
	    referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		paymentModeModel, "name", SortingOrder.ASC);
	    referenceDataWrapper.setBasePersistableModel(paymentModeModel);

	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    List<PaymentModeModel> paymentModeList = null;
	    if (referenceDataWrapper.getReferenceDataList() != null)
	    {
	    	paymentModeList = referenceDataWrapper.
	          getReferenceDataList();
	    }
	    referenceDataMap.put("paymentModeList", paymentModeList);
		}
		catch (Exception ex)
		{
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}
	    
	    return referenceDataMap;	
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		AllpayTransDetListModelExtended    extendedTransactionDetailPortalListModel = (AllpayTransDetListModelExtended) model;
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedTransactionDetailPortalListModel.getStartDate(),
				extendedTransactionDetailPortalListModel.getEndDate());
		searchBaseWrapper.setBasePersistableModel((AllpayTransDetListModel)extendedTransactionDetailPortalListModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		//sorting order 
		if(sortingOrderMap.isEmpty()){
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper = this.allPayTransactionDetailI8Manager.searchTransactionDetailForI8(searchBaseWrapper);
		
		List<AllpayTransDetListModel> list = null;
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView(super.getSuccessView(), "transactionDetailPortalList",list);
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public AllPayTransactionDetailI8Manager getAllPayTransactionDetailI8Manager() {
		return allPayTransactionDetailI8Manager;
	}

	public void setAllPayTransactionDetailI8Manager(
			AllPayTransactionDetailI8Manager allPayTransactionDetailI8Manager) {
		this.allPayTransactionDetailI8Manager = allPayTransactionDetailI8Manager;
	}

	

	
}
