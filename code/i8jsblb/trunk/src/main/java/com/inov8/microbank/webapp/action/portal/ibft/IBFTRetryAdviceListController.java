package com.inov8.microbank.webapp.action.portal.ibft;

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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.IBFTRetryAdviceListViewModel;
import com.inov8.microbank.common.model.IBFTRetryAdviceModel;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;

public class IBFTRetryAdviceListController extends
		BaseFormSearchController {

	@Autowired
	private TransactionReversalFacade transactionReversalFacade;

	public IBFTRetryAdviceListController() {
		super.setCommandName("iBFTRetryAdviceListViewModel");
		super.setCommandClass(IBFTRetryAdviceListViewModel.class);
	}

	protected Map<String, Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		return new HashMap<String, Object>();
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, 
			PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> map) throws Exception {

		IBFTRetryAdviceListViewModel model = (IBFTRetryAdviceListViewModel) object;

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(model);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime",  model.getRequestTimeStart(), model.getRequestTimeEnd());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if(map.isEmpty()){
			map.put("ibftRetryAdviceId", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(map);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		searchBaseWrapper	=	transactionReversalFacade.findIBFTRetryAdviceListView(searchBaseWrapper);

		List<IBFTRetryAdviceListViewModel> resultList = new ArrayList<>();

		if(searchBaseWrapper.getCustomList()!=null)
		{
			resultList	=	searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView(getSuccessView(), "resultList", resultList);
	}
}
