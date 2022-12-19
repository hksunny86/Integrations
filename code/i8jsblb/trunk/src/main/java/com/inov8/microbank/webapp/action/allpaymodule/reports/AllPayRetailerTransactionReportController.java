package com.inov8.microbank.webapp.action.allpaymodule.reports;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.allpaymodule.AllpayRetTransViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.AllpayModule.reports.AllPayReportsManager;

public class AllPayRetailerTransactionReportController extends BaseFormSearchController {

	private ReferenceDataManager referenceDataManager;
	AllPayReportsManager allpayReportsManager;

	public AllPayRetailerTransactionReportController() {
		super.setCommandName("allpayRetTransViewModel");
		super.setCommandClass(AllpayRetTransViewModel.class);

	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request, HttpServletResponse response, Boolean activate) throws Exception {
		ModelAndView modelAndView = new ModelAndView(new RedirectView("allpayretailerreport.html"));
		return modelAndView;

	}

	public AllPayReportsManager getAllpayReportsManager() {
		return allpayReportsManager;
	}

	public void setAllpayReportsManager(AllPayReportsManager allpayReportsManager) {
		this.allpayReportsManager = allpayReportsManager;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object command, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		AllpayRetTransViewModel allpayRetTransViewModel = (AllpayRetTransViewModel) command;
		
		
		if( UserUtils.getCurrentUser().getRetailerContactId() != null )
		{
			if( UserUtils.getCurrentUser().getRetailerContactIdRetailerContactModel().getHead() )
			{
				System.out.println( "-----------------------HEAD" );
				allpayRetTransViewModel.setRetailerHeadId(getRetailerContactId());
			}
			else
			{
				allpayRetTransViewModel.setRetailerContactId(getRetailerContactId());
				System.out.println( "-----------------------NOT  HEAD" );	
			}
		}
		else
		{
			System.out.println( "-----------------------DIST" );
			allpayRetTransViewModel.setDistributorHeadId( UserUtils.getCurrentUser().getDistributorContactId() ) ;
		}
		

		searchBaseWrapper.setBasePersistableModel(allpayRetTransViewModel);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("txDate", allpayRetTransViewModel.getStartDate(), allpayRetTransViewModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("retailerLocation", SortingOrder.ASC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.allpayReportsManager.searchRetailerTransactions(searchBaseWrapper);

		return new ModelAndView(getSuccessView(), "transList", searchBaseWrapper.getCustomList().getResultsetList());

	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		Map referenceDataMap = new HashMap();
		ReferenceDataWrapper referenceDataWrapper = null;
		{

			// Load Reference Data For Distributor
			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);
			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<DistributorModel> distributorModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("distributorModelList", distributorModelList);
		}
		{
			RetailerModel retailerModel = new RetailerModel();
			BaseWrapper bw = new BaseWrapperImpl();
			retailerModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(retailerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(retailerModel);
			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (Exception e) {

			}
			List<RetailerModel> retailerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				retailerModelList = referenceDataWrapper.getReferenceDataList();
			}
			referenceDataMap.put("retailerModelList", retailerModelList);

		}
		return referenceDataMap;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	private Long getRetailerContactId() {
		if (UserUtils.getCurrentUser() != null)
			return UserUtils.getCurrentUser().getRetailerContactId() ;
		else 
			return null;
	}

}
