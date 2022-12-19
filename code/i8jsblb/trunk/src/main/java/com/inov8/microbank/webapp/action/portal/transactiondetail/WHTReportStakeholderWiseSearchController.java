package com.inov8.microbank.webapp.action.portal.transactiondetail;

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
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.reportmodule.WHTStakeholderViewModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.portal.taxreportmodule.TaxReportFacade;
import com.inov8.microbank.server.facade.portal.transactiondetail.TransactionDetailFacade;

public class WHTReportStakeholderWiseSearchController extends BaseFormSearchController {
	
	@Autowired
	private TaxReportFacade taxReportFacade;
	private ReferenceDataManager referenceDataManager;

	public void setTaxReportFacade(TaxReportFacade taxReportFacade) {
		this.taxReportFacade = taxReportFacade;
	}

	public WHTReportStakeholderWiseSearchController() {
		 super.setCommandName("whtStakeholderViewModel");
		 super.setCommandClass(WHTStakeholderViewModel.class);
	}

	@Override
	protected Map<String, List<?>> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		
		Map<String, List<?>> refDataMap = new HashMap<String, List<?>>();
		
	    return refDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel,	LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		WHTStakeholderViewModel whtStakeholderViewModel = (WHTStakeholderViewModel) model;
		searchBaseWrapper.setBasePersistableModel(whtStakeholderViewModel);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn", whtStakeholderViewModel.getStartDate(), whtStakeholderViewModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		if(sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("taxPayer", SortingOrder.ASC);
		}
		
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<WHTStakeholderViewModel> list = this.taxReportFacade.searchWHTStakeholderView(searchBaseWrapper);
		String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
		return new ModelAndView( super.getSuccessView(), "whtStakeholderViewModelList", list.getResultsetList());
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}