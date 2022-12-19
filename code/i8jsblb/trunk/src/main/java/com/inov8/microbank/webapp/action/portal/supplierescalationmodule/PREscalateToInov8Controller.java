package com.inov8.microbank.webapp.action.portal.supplierescalationmodule;

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
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.portal.productcustomerdisputemodule.CustDisputeProductListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.MnoSupplierListViewModel;
import com.inov8.microbank.common.model.portal.supplierescalationmodule.EscToInov8ProductListViewModel;
import com.inov8.microbank.common.model.portal.supplierescalationmodule.ExtendedEscToInov8ProductListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.productcustomerdispute.CustomerDisputeProductManager;
import com.inov8.microbank.server.service.portal.supplierescalationmodule.SupplierEscalationManager;

public class PREscalateToInov8Controller extends BaseFormSearchController {

	private ReferenceDataManager referenceDataManager;

	private SupplierEscalationManager supplierEscalationManager;
	private CustomerDisputeProductManager customerDisputeProductManager;

	public void setSupplierEscalationManager(
			SupplierEscalationManager supplierEscalationManager) {
		this.supplierEscalationManager = supplierEscalationManager;
	}

	public PREscalateToInov8Controller() {
		setCommandName("extendedEscToInov8ProductListViewModel");
		setCommandClass(ExtendedEscToInov8ProductListViewModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		/**
		 * code fragment to load reference data for ProductModel
		 */
		Map referenceDataMap = new HashMap();
		CustDisputeProductListViewModel custDisputeProductListViewModel = new CustDisputeProductListViewModel();
		List<CustDisputeProductListViewModel> custDisputeProductListViewModelList = null;
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER
				.longValue()) {
			custDisputeProductListViewModel.setSupplierId(appUserModel
					.getSupplierUserIdSupplierUserModel().getSupplierId());
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					custDisputeProductListViewModel, "productName",
					SortingOrder.ASC);
			this.referenceDataManager.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null) {
				custDisputeProductListViewModelList = referenceDataWrapper
						.getReferenceDataList();
			}
		}
		
		else if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.MNO
				.longValue()) {
			MnoSupplierListViewModel mnoSupplierListViewModel = new MnoSupplierListViewModel();
			mnoSupplierListViewModel.setMnoUserId(appUserModel.getMnoUserId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(mnoSupplierListViewModel);
			baseWrapper = this.customerDisputeProductManager
					.searchMNOSupplier(baseWrapper);
			mnoSupplierListViewModel = (MnoSupplierListViewModel) baseWrapper
					.getBasePersistableModel();

			if (mnoSupplierListViewModel.getSupplierId() != null) {
				custDisputeProductListViewModel
						.setSupplierId(mnoSupplierListViewModel.getSupplierId());

				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
						custDisputeProductListViewModel, "productName",
						SortingOrder.ASC);

				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null) {
					custDisputeProductListViewModelList = referenceDataWrapper
							.getReferenceDataList();
				}
			}
		}

		referenceDataMap.put("custDisputeProductListViewModelList",
				custDisputeProductListViewModelList);
		
		//loading bank data
		List<BankModel> bankModelList = null;
		try
		{
			BankModel bankModel = new BankModel();
			bankModel.setActive(true);
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					bankModel, "name", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				bankModelList = referenceDataWrapper.getReferenceDataList();
			}
		}
		catch (Exception ex)
		{
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}
		referenceDataMap.put("bankModelList", bankModelList);
		
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
   	
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		ExtendedEscToInov8ProductListViewModel extendedEscToInov8ProductListViewModel = (ExtendedEscToInov8ProductListViewModel) model;
		
		extendedEscToInov8ProductListViewModel.setCreatedBy(UserUtils
				.getCurrentUser().getAppUserId());
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedEscToInov8ProductListViewModel
						.getStartDate(), extendedEscToInov8ProductListViewModel
						.getEndDate());

		searchBaseWrapper
				.setBasePersistableModel((EscToInov8ProductListViewModel) extendedEscToInov8ProductListViewModel);
		
		//setting order map
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
        
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		searchBaseWrapper = this.supplierEscalationManager
				.searchEscalateToInov8Product(searchBaseWrapper);
		return new ModelAndView(getSuccessView(),
				"escToInov8ProductListViewModelList", searchBaseWrapper
						.getCustomList().getResultsetList());
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setCustomerDisputeProductManager(
			CustomerDisputeProductManager customerDisputeProductManager) {
		this.customerDisputeProductManager = customerDisputeProductManager;
	}

}
