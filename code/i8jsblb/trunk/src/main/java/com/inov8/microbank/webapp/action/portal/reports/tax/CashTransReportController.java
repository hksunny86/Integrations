package com.inov8.microbank.webapp.action.portal.reports.tax;

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
import com.inov8.microbank.common.model.portal.reportmodule.CashTransReportViewModel;
import com.inov8.microbank.server.facade.portal.taxreportmodule.TaxReportFacade;

public class CashTransReportController extends BaseFormSearchController {

	@Autowired
	private TaxReportFacade taxReportFacade;

	public CashTransReportController() {
		super.setCommandName("cashTransReportViewModel");
		super.setCommandClass(CashTransReportViewModel.class);
	}

	protected Map<String, Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		return new HashMap<String, Object>();
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, 
			PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> map) throws Exception {

		CashTransReportViewModel model = (CashTransReportViewModel) object;

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(model);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("initiatedOn",  model.getStartDate(), model.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if(map.isEmpty()){
			map.put("transactionCodeId", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(map);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
		searchBaseWrapper	=	taxReportFacade.searchCashTransReportView(searchBaseWrapper);

		List<CashTransReportViewModel> resultList = new ArrayList<>();

		if(searchBaseWrapper.getCustomList()!=null)
		{
			resultList	=	searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView(getSuccessView(), "resultList", resultList);
	}
}