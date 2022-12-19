package com.inov8.microbank.webapp.action.portal.transactionreversal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.RetryAdviceListHistoryViewModel;
import com.inov8.microbank.common.model.RetryAdviceListSummaryViewModel;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;

public class RetryAdviceHistoryController extends BaseSearchController {

	@Autowired
	private ReferenceDataManager referenceDataManager;

	@Autowired
	private TransactionReversalFacade transactionReversalFacade;

	public RetryAdviceHistoryController() {
		setFilterSearchCommandClass(RetryAdviceListHistoryViewModel.class);
	}
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object model, HttpServletRequest request,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {

		String param = request.getParameter("transactionCode");
		String stan = request.getParameter("stan");

		List<RetryAdviceListSummaryViewModel> resultList = new ArrayList<RetryAdviceListSummaryViewModel>();
		if (param != null && !param.trim().equals("")) 
		{
			RetryAdviceListHistoryViewModel retryAdviceListHistoryViewModel= new RetryAdviceListHistoryViewModel();
			retryAdviceListHistoryViewModel.setTransactionCode(param);

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(retryAdviceListHistoryViewModel);
			searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
			sortingOrderMap.put("middlewareRetryAdvRprtId", SortingOrder.DESC);
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
			
			searchBaseWrapper = transactionReversalFacade.findRetryAdviceHistoryListView(searchBaseWrapper);

			if (searchBaseWrapper.getCustomList() != null) 
			{
				resultList = searchBaseWrapper.getCustomList().getResultsetList();
			}
		}

		if (stan != null && !stan.trim().equals("")){
			RetryAdviceListHistoryViewModel retryAdviceListHistoryViewModel= new RetryAdviceListHistoryViewModel();
			retryAdviceListHistoryViewModel.setStan(stan);

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(retryAdviceListHistoryViewModel);
			searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
			sortingOrderMap.put("middlewareRetryAdvRprtId", SortingOrder.DESC);
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

			searchBaseWrapper = transactionReversalFacade.findRetryAdviceHistoryListView(searchBaseWrapper);

			if (searchBaseWrapper.getCustomList() != null)
			{
				resultList = searchBaseWrapper.getCustomList().getResultsetList();
			}
		}


			return new ModelAndView( getSearchView(), "RetryAdviceHistoryList", resultList );
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionReversalFacade(
			TransactionReversalFacade transactionReversalFacade) {
		this.transactionReversalFacade = transactionReversalFacade;
	}
}