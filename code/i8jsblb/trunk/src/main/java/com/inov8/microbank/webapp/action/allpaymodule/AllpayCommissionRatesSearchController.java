package com.inov8.microbank.webapp.action.allpaymodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.CommissionRatesListViewModel;
import com.inov8.microbank.server.service.AllpayModule.AllpayCommissionRatesManager;

public class AllpayCommissionRatesSearchController extends BaseSearchController {
	public AllpayCommissionRatesSearchController() {
		super.setFilterSearchCommandClass(CommissionRatesListViewModel.class);
	}

	private AllpayCommissionRatesManager allpayCommissionRatesManager;

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		CommissionRatesListViewModel commissionRateListViewModel = (CommissionRatesListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(commissionRateListViewModel);

		if (sortingOrderMap.isEmpty())
			sortingOrderMap.put("productName", SortingOrder.ASC);

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		
		searchBaseWrapper = this.allpayCommissionRatesManager.searchCommissionRates(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "commissionRateList", searchBaseWrapper.getCustomList()
				.getResultsetList());

	}

	public AllpayCommissionRatesManager getAllpayCommissionRatesManager() {
		return allpayCommissionRatesManager;
	}

	public void setAllpayCommissionRatesManager(AllpayCommissionRatesManager allpayCommissionRatesManager) {
		this.allpayCommissionRatesManager = allpayCommissionRatesManager;
	}

}
