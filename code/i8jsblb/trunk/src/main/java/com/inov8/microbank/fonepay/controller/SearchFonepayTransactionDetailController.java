package com.inov8.microbank.fonepay.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.fonepay.model.ExtendedFonePayTransactionDetailModel;
import com.inov8.microbank.fonepay.model.FonePayTransactionDetailViewModel;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.webapp.action.portal.bigreports.ReportCriteriaSessionObject;
import com.inov8.microbank.webapp.action.portal.transactiondetaili8module.ReportTypeEnum;

public class SearchFonepayTransactionDetailController extends BaseFormSearchController {

	@Autowired
	private FonePayManager fonePayManager;
	
	public SearchFonepayTransactionDetailController()
	{
		 super.setCommandName("extendedFonePayTransactionDetailModel");
		 super.setCommandClass(ExtendedFonePayTransactionDetailModel.class);
	}
	
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {

		Map<String,Object> referenceDataMap = new HashMap<String,Object>();
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse arg1, Object model, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedFonePayTransactionDetailModel extendedFonePayTransactionDetailModel = (ExtendedFonePayTransactionDetailModel) model;
		//String reportType = ServletRequestUtils.getStringParameter( httpServletRequest, "reportType" );
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedFonePayTransactionDetailModel.getStartDate(),
				extendedFonePayTransactionDetailModel.getEndDate());
		DateRangeHolderModel dateRangeHolderModelOnUpdateDate = new DateRangeHolderModel("updatedOn", extendedFonePayTransactionDetailModel.getUpdatedOnStartDate(),extendedFonePayTransactionDetailModel.getUpdatedOnEndDate());

		searchBaseWrapper.setBasePersistableModel((FonePayTransactionDetailViewModel)extendedFonePayTransactionDetailModel);
		
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        dateRangeHolderModels.add(dateRangeHolderModelOnUpdateDate);
        searchBaseWrapper.setDateRangeHolderModelList(dateRangeHolderModels);
		
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = fonePayManager.searchFonePayTransactionDetail(searchBaseWrapper);
		
		List<FonePayTransactionDetailViewModel> list = null;
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		
		
		Integer totalRecordsCount = 0;
		
		if(pagingHelperModel != null) {
			
			totalRecordsCount = pagingHelperModel.getTotalRecordsCount();
		}		
		
		httpServletRequest.setAttribute("totalRecordsCount", totalRecordsCount);
		
		return new ModelAndView( getSuccessView(), "fonePayTransactionDetailList",list);
	
	}

	public void setFonePayManager(FonePayManager fonePayManager) {
		this.fonePayManager = fonePayManager;
	}

}
