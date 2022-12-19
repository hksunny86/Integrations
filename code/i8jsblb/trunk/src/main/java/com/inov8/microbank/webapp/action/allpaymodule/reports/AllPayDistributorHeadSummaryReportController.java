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
import com.inov8.microbank.common.model.allpaymodule.DistHeadSummaryViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.reports.AllPayReportsManager;

public class AllPayDistributorHeadSummaryReportController 
extends BaseFormSearchController
{

	private ReferenceDataManager referenceDataManager;
	private AllPayReportsManager allpayReportsManager;
	public AllPayDistributorHeadSummaryReportController() {
//		super.setFilterSearchCommandClass(DistHeadSummaryViewModel.class);
				
		super.setCommandName("distHeadSummaryViewModel");
		super.setCommandClass(DistHeadSummaryViewModel.class);
		
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




	                       
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel,LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception 
	{
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		DistHeadSummaryViewModel distHeadSummaryViewModel = (DistHeadSummaryViewModel) object;
		
		distHeadSummaryViewModel.setDistributorContactId( UserUtils.getCurrentUser().getDistributorContactId() ) ;
		
		//allpayRetTransViewModel.setRetailerContactId(getRetailerContactId());
		
//		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
//				"createdOn", distHeadReportListViewModel.getCreatedOn(), distHeadReportListViewModel.getCreatedOn());
//		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		
		searchBaseWrapper.setBasePersistableModel(distHeadSummaryViewModel);
		
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = (Date)format.parse( "10/11/2009" );
		
//		distHeadSummaryViewModel.setStartDate( format.format(startDate) );
//		distHeadSummaryViewModel.setEndDate( format.format(startDate) );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("txDate", startDate, (Date)format.parse( "10/11/2009" ));
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		/*if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("txDate", SortingOrder.ASC);
		}*/
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.allpayReportsManager.searchDistHeadSummary(searchBaseWrapper);
		
//		if( searchBaseWrapper.getCustomList().getResultsetList().size() == 0 )
			pagingHelperModel.setTotalRecordsCount(searchBaseWrapper.getCustomList().getResultsetList().size());

		return new ModelAndView(super.getSuccessView(), "transList", searchBaseWrapper.getCustomList().getResultsetList());
		//pagingHelperModel.setTotalRecordsCount(0);
		//return new ModelAndView(getSearchView());//, "transList", searchBaseWrapper.getCustomList().getResultsetList());	
		}




	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}




	



}
