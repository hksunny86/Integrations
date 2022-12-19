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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.portal.reportmodule.FedBreakupDetailListViewModel;
import com.inov8.microbank.server.facade.portal.taxreportmodule.TaxReportFacade;

public class FedBreakupDetailViewController extends BaseFormSearchController
{

	@Autowired
	private TaxReportFacade taxReportFacade;
	@Autowired
	private ReferenceDataManager referenceDataManager;

	public FedBreakupDetailViewController()
	{
		super.setCommandName("fedBreakupDetailListViewModel");
		super.setCommandClass(FedBreakupDetailListViewModel.class);
	}

	protected Map<String, Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String, Object>();
	/*	List<ProductModel> productModelList = null;

		ProductModel productModel = new ProductModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(productModel, "name", SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			productModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("productModelList", productModelList);	*/
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> map) throws Exception
	{

		FedBreakupDetailListViewModel model = (FedBreakupDetailListViewModel) object;

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(model);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("transactionDate", model.getStartDate(), model.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		if (map.isEmpty())
		{
			map.put("transactionDate", SortingOrder.DESC);
		}

		searchBaseWrapper.setSortingOrderMap(map);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		searchBaseWrapper = taxReportFacade.searchFedBreakupDetailReportView(searchBaseWrapper);

		List<FedBreakupDetailListViewModel> resultList = new ArrayList<>();

		if (searchBaseWrapper.getCustomList() != null)
		{
			resultList = searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView(getSuccessView(), "resultList", resultList);
	}
}
