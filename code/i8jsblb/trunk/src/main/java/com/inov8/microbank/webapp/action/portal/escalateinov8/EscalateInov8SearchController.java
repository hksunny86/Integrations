package com.inov8.microbank.webapp.action.portal.escalateinov8;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.inov8.microbank.common.model.portal.escalateinov8module.EscalateInov8ListViewModel;
import com.inov8.microbank.common.model.portal.escalateinov8module.ExtendedEscalateInov8ListViewModel;
import com.inov8.microbank.common.model.portal.escalateinov8module.IssueStatRefDataListViewModel;
import com.inov8.microbank.common.model.portal.productcustomerdisputemodule.CustDisputeProductListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.CustDisputeServiceListViewModel;
import com.inov8.microbank.common.util.IssueTypeConstantsInterface;
import com.inov8.microbank.server.service.portal.escalateinov8module.EscalateInov8Manager;

public class EscalateInov8SearchController extends BaseFormSearchController {
	private EscalateInov8Manager escalateInov8Manager;

	private ReferenceDataManager referenceDataManager;

	public EscalateInov8SearchController() {
		super.setCommandName("extendedEscalateInov8ListViewModel");
		super.setCommandClass(ExtendedEscalateInov8ListViewModel.class);
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> loadReferenceData(HttpServletRequest request)
			throws Exception

	{
		/**
		 * code fragment to load reference data for ProductModel
		 */
		//populate service list drop down
		CustDisputeServiceListViewModel custDisputeServiceListViewModel = new CustDisputeServiceListViewModel();
		List<CustDisputeServiceListViewModel> custDisputeServiceListViewModelList = null;

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				custDisputeServiceListViewModel, "productName",
				SortingOrder.ASC);

		referenceDataManager.getReferenceData(referenceDataWrapper);

		if (referenceDataWrapper.getReferenceDataList() != null) {
			custDisputeServiceListViewModelList = referenceDataWrapper
					.getReferenceDataList();
		}

		//loading product data

		CustDisputeProductListViewModel custDisputeProductListViewModel = new CustDisputeProductListViewModel();
		List<CustDisputeProductListViewModel> custDisputeProductListViewModelList = null;

		referenceDataWrapper = new ReferenceDataWrapperImpl(
				custDisputeProductListViewModel, "productName",
				SortingOrder.ASC);
		this.referenceDataManager.getReferenceData(referenceDataWrapper);
		if (referenceDataWrapper.getReferenceDataList() != null) {
			custDisputeProductListViewModelList = referenceDataWrapper
					.getReferenceDataList();
		}

		Map<String,Object> referenceDataMap = new HashMap<>();
		referenceDataMap.put("serviceModelList",
				custDisputeServiceListViewModelList);
		referenceDataMap.put("productModelList",
				custDisputeProductListViewModelList);

		IssueStatRefDataListViewModel issueStatRefDataListViewModel = new IssueStatRefDataListViewModel();
		issueStatRefDataListViewModel
				.setIssueTypeId(IssueTypeConstantsInterface.CHARGEBACK);

		ReferenceDataWrapper referenceDataWrapper1 = new ReferenceDataWrapperImpl(
				issueStatRefDataListViewModel, "issueStatusName",
				SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper1);

		List<IssueStatRefDataListViewModel> issueTypeStatusModelList = null;
		if (referenceDataWrapper1.getReferenceDataList() != null) {
			issueTypeStatusModelList = referenceDataWrapper1
					.getReferenceDataList();

			referenceDataMap.put("issueTypeStatusModelList",
					issueTypeStatusModelList);

		}

		issueStatRefDataListViewModel
				.setIssueTypeId(IssueTypeConstantsInterface.DISPUTE);

		referenceDataWrapper = new ReferenceDataWrapperImpl(
				issueStatRefDataListViewModel, "issueStatusName",
				SortingOrder.ASC);
		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<IssueStatRefDataListViewModel> disputeIssueTypeStatusModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			disputeIssueTypeStatusModelList = referenceDataWrapper
					.getReferenceDataList();

			referenceDataMap.put("disputeIssueTypeStatusModelList",
					disputeIssueTypeStatusModelList);

		}

		
		
		
		
		
		return referenceDataMap;

	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap)
			throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		Long tempServiceId = null;
		ExtendedEscalateInov8ListViewModel extendedEscalateInov8ListViewModel = (ExtendedEscalateInov8ListViewModel) model;
		
		if (null != extendedEscalateInov8ListViewModel.getServiceId())
			{
				extendedEscalateInov8ListViewModel.setProductId(extendedEscalateInov8ListViewModel.getServiceId());
				tempServiceId = extendedEscalateInov8ListViewModel.getServiceId();
				extendedEscalateInov8ListViewModel.setServiceId(null);
			}

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedEscalateInov8ListViewModel.getStartDate(),
				extendedEscalateInov8ListViewModel.getEndDate());

		searchBaseWrapper
				.setBasePersistableModel((EscalateInov8ListViewModel) extendedEscalateInov8ListViewModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		//setting order map
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper = this.escalateInov8Manager
				.searchEscalateInov8Status(searchBaseWrapper);
		extendedEscalateInov8ListViewModel.setServiceId(tempServiceId);	
		return new ModelAndView(super.getSuccessView(),
				"escalationStatusModelList", searchBaseWrapper.getCustomList()
						.getResultsetList());

	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setEscalateInov8Manager(
			EscalateInov8Manager escalateInov8Manager) {
		this.escalateInov8Manager = escalateInov8Manager;
	}

}
