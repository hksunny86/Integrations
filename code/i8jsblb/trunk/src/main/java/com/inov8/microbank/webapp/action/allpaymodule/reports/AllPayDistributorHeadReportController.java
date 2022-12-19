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
import com.inov8.microbank.common.model.DistHeadReportListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.reports.AllPayReportsManager;

public class AllPayDistributorHeadReportController extends BaseFormSearchController
{

	private ReferenceDataManager referenceDataManager;
	private AllPayReportsManager allpayReportsManager;

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception
	{		
		return null;
	}

	public AllPayDistributorHeadReportController()
	{
		super.setCommandName("distHeadReportListViewModel");
		super.setCommandClass(DistHeadReportListViewModel.class);
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{
		System.out.println("###################################################################");
		
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		//TransactionDetInfoListViewModel transactionDetInfoListViewModel = (TransactionDetInfoListViewModel) object;
		DistHeadReportListViewModel distHeadReportListViewModel = (DistHeadReportListViewModel) object;
//		distHeadReportListViewModel.setAppUserId( UserUtils.getCurrentUser().getAppUserId() );
		distHeadReportListViewModel.setDistributorContactId( UserUtils.getCurrentUser().getDistributorContactId() ) ;
//		distHeadReportListViewModel.setCreatedOn("10/11/2009");
		
//		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
//				"createdOn", distHeadReportListViewModel.getCreatedOn(), distHeadReportListViewModel.getCreatedOn());
//		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
		if (sortingOrderMap.isEmpty()) 
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setBasePersistableModel((DistHeadReportListViewModel)distHeadReportListViewModel);
		searchBaseWrapper = this.allpayReportsManager.searchDistributorHeadTransactions(searchBaseWrapper);

		
		return new ModelAndView(super.getSuccessView(), "distHeadReportListViewModelList", searchBaseWrapper.getCustomList().getResultsetList());
	}

	public ReferenceDataManager getReferenceDataManager()
	{
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public AllPayReportsManager getAllpayReportsManager()
	{
		return allpayReportsManager;
	}

	public void setAllpayReportsManager(AllPayReportsManager allpayReportsManager)
	{
		this.allpayReportsManager = allpayReportsManager;
	}

	

}
