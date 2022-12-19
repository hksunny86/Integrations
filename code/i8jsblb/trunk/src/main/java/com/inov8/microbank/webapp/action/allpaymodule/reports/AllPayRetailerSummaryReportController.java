package com.inov8.microbank.webapp.action.allpaymodule.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.allpaymodule.RetailerSummaryViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.reports.AllPayReportsManager;

public class AllPayRetailerSummaryReportController 
extends BaseFormSearchController
{

	private ReferenceDataManager referenceDataManager;
	private AllPayReportsManager allpayReportsManager;
	public AllPayRetailerSummaryReportController() {
//		super.setFilterSearchCommandClass(RetailerSummaryViewModel.class);
				
		super.setCommandName("allpayRetTransViewModel");
		super.setCommandClass(RetailerSummaryViewModel.class);
		
	}

//	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object command, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
//		return new ModelAndView(getSuccessView());//, "transList", searchBaseWrapper.getCustomList().getResultsetList());
//	}


	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}


	public AllPayReportsManager getAllpayReportsManager() {
		return allpayReportsManager;
	}


	public void setAllpayReportsManager(AllPayReportsManager allpayReportsManager) {
		this.allpayReportsManager = allpayReportsManager;
	}


	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object model, PagingHelperModel pagingHelperModel,LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		RetailerSummaryViewModel retailerSummaryViewModel = (RetailerSummaryViewModel) model;
		
		retailerSummaryViewModel.setRetailerContactId( UserUtils.getCurrentUser().getRetailerContactId()) ;
		
		//allpayRetTransViewModel.setRetailerContactId(getRetailerContactId());

		searchBaseWrapper.setBasePersistableModel(retailerSummaryViewModel);

		//DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("txDate", allpayRetTransViewModel.getStartDate(), allpayRetTransViewModel.getEndDate());
		//searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date startDate = (Date)format.parse( "10/10/2009" );
		
//		retailerSummaryViewModel.setStartDate( format.format(startDate) );
//		retailerSummaryViewModel.setEndDate( format.format(startDate) );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("txDate", startDate, (Date)format.parse( "11/10/2009" ));
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
 
		
		/*if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("txDate", SortingOrder.ASC);
		}*/
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.allpayReportsManager.searchRetSummary(searchBaseWrapper);
		
		pagingHelperModel.setTotalRecordsCount( searchBaseWrapper.getCustomList().getResultsetList().size()) ;

		return new ModelAndView(getSuccessView(), "transList", searchBaseWrapper.getCustomList().getResultsetList());
		//pagingHelperModel.setTotalRecordsCount(0);
		//return new ModelAndView(getSearchView());//, "transList", searchBaseWrapper.getCustomList().getResultsetList());	
	
		
		}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception
	{		
		return null;
	}



}
