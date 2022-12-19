package com.inov8.microbank.webapp.action.allpaymodule.reports;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.allpaymodule.RetailerHeadTransViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.reports.AllPayReportsManager;

public class AllPayRetailerHeadReportController extends BaseSearchController {

	private ReferenceDataManager referenceDataManager;
	private AllPayReportsManager allpayReportsManager;

	private Long getRetailerContactId (){
		if (UserUtils.getCurrentUser() != null)
			return UserUtils.getCurrentUser().getRetailerContactId() ;
		else 
			return null;
	}

	
	public AllPayRetailerHeadReportController() {
		super.setFilterSearchCommandClass(RetailerHeadTransViewModel.class);
				/*
		super.setCommandName("retailerHeadTransViewModel");
		super.setCommandClass(RetailerHeadTransViewModel.class);
		*/
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


	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		RetailerHeadTransViewModel retailerHeadTransViewModel = (RetailerHeadTransViewModel) model;
//		retailerHeadTransViewModel.setHeadContactId(getRetailerContactId());
		
		
		if( UserUtils.getCurrentUser().getRetailerContactId() != null )
		{
//			if( UserUtils.getCurrentUser().getRetailerContactIdRetailerContactModel().getHead() )
//			{
//				System.out.println( "-----------------------HEAD" );
//				retailerHeadTransViewModel.setHeadContactId(getRetailerContactId());
//			}	
//			else
				retailerHeadTransViewModel.setHeadContactId(getRetailerContactId());
		}
		else
		{
			System.out.println( "-----------------------DIST" );
			retailerHeadTransViewModel.setDistributorHeadId( UserUtils.getCurrentUser().getDistributorContactId() ) ;
		}
		

		searchBaseWrapper.setBasePersistableModel(retailerHeadTransViewModel);	
		

		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("headAppUserId", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.allpayReportsManager.searchRetailerHeadTransactions(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "transList", searchBaseWrapper.getCustomList().getResultsetList());

	}

}
