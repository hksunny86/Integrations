package com.inov8.microbank.webapp.action.handler;

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
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;
import com.inov8.microbank.server.service.transactiondetailinfomodule.TransactionDetInfoListViewManager;

public class HandlerTransactionHistorySearchController extends
		BaseFormSearchController

{

	private TransactionDetInfoListViewManager transactionDetInfoListViewManager;
	private ReferenceDataManager referenceDataManager;

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		HandlerTransactionHistoryVO transactionHistoryVO=new HandlerTransactionHistoryVO();
		
		
		String handlerId	=	request.getParameter("handlerId");
		String handlerPk	=	request.getParameter("hId");
		
		transactionHistoryVO.setHandlerMfsId(handlerId);
		transactionHistoryVO.setHandlerPk(handlerPk);
		
		return transactionHistoryVO;
	}
	
	public HandlerTransactionHistorySearchController() {
		super.setCommandName("handlerTransactionHistoryVO");
		super.setCommandClass(HandlerTransactionHistoryVO.class);
	}

	protected Map<String, Object> loadReferenceData(
			HttpServletRequest httpServletRequest) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();

		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = httpServletRequest.getParameter("supplierid");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				supplierModel, "name", SortingOrder.ASC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);

			List<SupplierModel> supplierModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				supplierModelList = referenceDataWrapper.getReferenceDataList();
			}

			ProductModel productModel = null;
			List<ProductModel> productModelList = null;

			if (null != strSupplierId && !strSupplierId.isEmpty()) {
				Long supplierId = Long.parseLong(strSupplierId);
				productModel = new ProductModel();
				productModel.setSupplierId(supplierId);
				productModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null) {
					productModelList = referenceDataWrapper
							.getReferenceDataList();
				}
			}
			referenceDataMap.put("supplierModelList", supplierModelList);
			referenceDataMap.put("productModelList", productModelList);
			// Load Data for Transaction Status
			SupplierProcessingStatusModel supplierProcessingStatusModel = new SupplierProcessingStatusModel();
			ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl(
					supplierProcessingStatusModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(refDataWrapper);
			referenceDataMap.put("supplierProcessingStatusList",
					refDataWrapper.getReferenceDataList());
		} catch (Exception ex) {
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		List<AllpayTransInfoListViewModel> resultList = null;

		String handlerId = httpServletRequest.getParameter("handlerMfsId");

		if (handlerId!=null && !handlerId.isEmpty()) {

			HandlerTransactionHistoryVO handlerTransactionHistoryVO = (HandlerTransactionHistoryVO) model;
			DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
					"createdOn",
					handlerTransactionHistoryVO.getStartDate(),
					handlerTransactionHistoryVO.getEndDate());
			if (sortingOrderMap.isEmpty()) {
				sortingOrderMap.put("createdOn", SortingOrder.DESC);
			}

			searchBaseWrapper.setBasePersistableModel((AllpayTransInfoListViewModel) handlerTransactionHistoryVO);
			searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
			searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

			searchBaseWrapper = transactionDetInfoListViewManager.searchHandlerTransactionHistory(searchBaseWrapper);

			CustomList<AllpayTransInfoListViewModel> customList = searchBaseWrapper.getCustomList();
			if (customList != null) {
				resultList = customList.getResultsetList();
			}
		} else {
			pagingHelperModel.setTotalRecordsCount(0);
		}
		
		ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		modelAndView.addObject("handlerTransactionModelList",resultList);
		return modelAndView;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetInfoListViewManager(
			TransactionDetInfoListViewManager transactionDetInfoListViewManager) {
		this.transactionDetInfoListViewManager = transactionDetInfoListViewManager;
	}

}
