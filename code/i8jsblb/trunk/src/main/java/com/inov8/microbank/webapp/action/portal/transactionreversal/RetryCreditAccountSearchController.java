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
import com.inov8.microbank.common.model.RetryAdviceListSummaryViewModel;
import com.inov8.microbank.common.model.RetryCreditQueueViewModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.vo.transactionreversal.RetryAdviceSearchVO;
import com.inov8.microbank.common.vo.transactionreversal.RetryCreditQueueSearchVO;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;

public class RetryCreditAccountSearchController extends BaseFormSearchController {

	@Autowired
	private ReferenceDataManager referenceDataManager;

	@Autowired
	private TransactionReversalFacade transactionReversalFacade;

	public RetryCreditAccountSearchController() {
		super.setCommandName("retryCreditQueueSearchVO");
		super.setCommandClass(RetryCreditQueueSearchVO.class);
	}

	
	protected Map<String, Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {

		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		ReferenceDataWrapper referenceDataWrapper;

		List<TransactionTypeModel> transactionTypeModelList = null;

		TransactionTypeModel transactionTypeModel = new TransactionTypeModel();

		referenceDataWrapper = new ReferenceDataWrapperImpl(new TransactionTypeModel(), "name", SortingOrder.ASC);
		
		try {
			
			referenceDataWrapper = referenceDataManager.getReferenceData(referenceDataWrapper);
			
			if (referenceDataWrapper.getReferenceDataList() != null) {
				transactionTypeModelList = referenceDataWrapper.getReferenceDataList();
			}
			
		} catch (Exception ex) {
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

		referenceDataMap.put("transactionTypeModelList", transactionTypeModelList);

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, 
				PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> map) throws Exception {

		RetryCreditQueueSearchVO retryCreditQueueSearchVO = (RetryCreditQueueSearchVO) object;

		RetryCreditQueueViewModel retryCreditQueueViewModel = new RetryCreditQueueViewModel();
		retryCreditQueueViewModel.setTransactionTypeId(retryCreditQueueSearchVO.getTransactionTypeId());
		retryCreditQueueViewModel.setTransactionCode(retryCreditQueueSearchVO.getTransactionCode());
		retryCreditQueueViewModel.setStatus(retryCreditQueueSearchVO.getStatus());
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(retryCreditQueueViewModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn", retryCreditQueueSearchVO.getRequestTimeStart(),retryCreditQueueSearchVO.getRequestTimeEnd());
		
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if(map.isEmpty()) {
			
			map.put("retryCreditQueueViewModelId", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(map);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		searchBaseWrapper = transactionReversalFacade.findRetryCreditQueueViewModel(searchBaseWrapper);

		List<RetryCreditQueueViewModel> retryCreditQueueViewModelList=new ArrayList<RetryCreditQueueViewModel>();

		if(searchBaseWrapper.getCustomList() != null) {
			
			retryCreditQueueViewModelList = searchBaseWrapper.getCustomList().getResultsetList();
		}
		
		return new ModelAndView(getSuccessView(), "resultList", retryCreditQueueViewModelList);
	}
}
