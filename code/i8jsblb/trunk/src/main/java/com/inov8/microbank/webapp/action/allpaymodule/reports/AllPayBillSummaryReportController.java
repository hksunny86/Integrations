package com.inov8.microbank.webapp.action.allpaymodule.reports;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.allpaymodule.RetailerBillSummaryViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.reports.AllPayReportsManager;

public class AllPayBillSummaryReportController extends BaseFormSearchController {

	private ReferenceDataManager referenceDataManager;
	private AllPayReportsManager allpayReportsManager;

	

	public AllPayBillSummaryReportController() {
		//super.setFilterSearchCommandClass(RetailerBillSummaryViewModel.class);
		
		
		super.setCommandName("retailerBillSummaryViewModel");
		super.setCommandClass(RetailerBillSummaryViewModel.class);
		
	}
	
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
	private Long getRetailerContactId (){
		
		if (UserUtils.getCurrentUser() != null)
			return UserUtils.getCurrentUser().getRetailerContactId() ;
		else 
			return null;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object command, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		RetailerBillSummaryViewModel retailerBillSummaryViewModel = (RetailerBillSummaryViewModel) command;
//		retailerBillSummaryViewModel.setRetailerContactId(getRetailerContactId());
		
		
		if( UserUtils.getCurrentUser().getRetailerContactId() != null )
		{
			if( UserUtils.getCurrentUser().getRetailerContactIdRetailerContactModel().getHead() )
			{
				System.out.println( "-----------------------HEAD" );
				retailerBillSummaryViewModel.setHeadId(getRetailerContactId());
			}
			else
			{
				retailerBillSummaryViewModel.setRetailerContactId(getRetailerContactId());
				System.out.println( "-----------------------NOT  HEAD" );	
			}
		}
		else
		{
			System.out.println( "-----------------------DIST" );
			retailerBillSummaryViewModel.setDistributorContactId( UserUtils.getCurrentUser().getDistributorContactId() ) ;
		}
		
		
		searchBaseWrapper.setBasePersistableModel(retailerBillSummaryViewModel);
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("name", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.allpayReportsManager.searchRetailerBillSummaries(searchBaseWrapper);

		return new ModelAndView(getSuccessView(), "transList", searchBaseWrapper.getCustomList().getResultsetList());

	}

}
