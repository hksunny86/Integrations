package com.inov8.microbank.webapp.action.portal.transactionreversal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetryAdviceListSummaryViewModel;
import com.inov8.microbank.common.vo.transactionreversal.RetryAdviceSearchVO;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;

public class RetryAdviceListViewSearchController extends
		BaseFormSearchController {

	@Autowired
	private ReferenceDataManager referenceDataManager;

	@Autowired
	private TransactionReversalFacade transactionReversalFacade;

	public RetryAdviceListViewSearchController() {
		super.setCommandName("retryAdviceSearchVO");
		super.setCommandClass(RetryAdviceSearchVO.class);
	}

	protected Map<String, Object> loadReferenceData(
			HttpServletRequest httpServletRequest) throws Exception {

		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		ReferenceDataWrapper referenceDataWrapper;

		ProductModel productModel = null;
		List<ProductModel> productModelList = null;
		productModel = new ProductModel();
		productModel.setActive(true);
		referenceDataWrapper = new ReferenceDataWrapperImpl(productModel,
				"name", SortingOrder.ASC);
		try {
			referenceDataWrapper = referenceDataManager
					.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null) {
				productModelList = referenceDataWrapper.getReferenceDataList();
			}
		} catch (Exception ex) {
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

		referenceDataMap.put("productModelList", productModelList);

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object object,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> map) throws Exception {

		RetryAdviceSearchVO retryAdviceSearchVO = (RetryAdviceSearchVO) object;

		RetryAdviceListSummaryViewModel retryAdviceListSummaryViewModel = new RetryAdviceListSummaryViewModel();

		retryAdviceListSummaryViewModel.setAdviceType(retryAdviceSearchVO.getAdviceType());
		retryAdviceListSummaryViewModel.setFromAccount(retryAdviceSearchVO.getFromAccount());
		retryAdviceListSummaryViewModel.setProductId(retryAdviceSearchVO.getProductId());
		retryAdviceListSummaryViewModel.setToAccount(retryAdviceSearchVO.getToAccount());
		retryAdviceListSummaryViewModel.setTransactionCode(retryAdviceSearchVO.getTransactionCode());
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(retryAdviceListSummaryViewModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime", 
				retryAdviceSearchVO.getRequestTimeStart(),retryAdviceSearchVO.getRequestTimeEnd());
		
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if(map.isEmpty()){
			map.put("middlewareRetryAdvRprtId", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(map);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		searchBaseWrapper	=	transactionReversalFacade.findRetryAdviceSummaryListView(searchBaseWrapper);

		List<RetryAdviceListSummaryViewModel> retryAdviceListViewModelList=new ArrayList<>();

		if(searchBaseWrapper.getCustomList()!=null)
		{
			retryAdviceListViewModelList	=	searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView(getSuccessView(), "resultList", retryAdviceListViewModelList);
	}
}
