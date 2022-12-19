package com.inov8.microbank.webapp.action.portal.sales;

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
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.portal.productcustomerdisputemodule.CustDisputeProductListViewModel;
import com.inov8.microbank.common.model.portal.salesmodule.ExtendedSalesListViewModel;
import com.inov8.microbank.common.model.portal.salesmodule.SalesListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.MnoSupplierListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.mnomodule.MnoManager;
import com.inov8.microbank.server.service.portal.productcustomerdispute.CustomerDisputeProductManager;
import com.inov8.microbank.server.service.portal.sales.SalesManager;

public class SalesSearchController extends BaseFormSearchController
{
	private SalesManager					salesManager;
	private ReferenceDataManager			referenceDataManager;
	private MnoManager						mnoManager;
	private CustomerDisputeProductManager	customerDisputeProductManager;

	public SalesSearchController()
	{
		super.setCommandName("extendedSalesListViewModel");
		super.setCommandClass(ExtendedSalesListViewModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		/**
		 * code fragment to load reference data for ProductModel
		 */
		Map<String,Object> referenceDataMap = new HashMap<>();
		CustDisputeProductListViewModel custDisputeProductListViewModel = new CustDisputeProductListViewModel();
		List<CustDisputeProductListViewModel> custDisputeProductListViewModelList = null;
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER.longValue())
		{
			custDisputeProductListViewModel.setSupplierId(appUserModel.getSupplierUserIdSupplierUserModel()
					.getSupplierId());
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(custDisputeProductListViewModel,
					"productName", SortingOrder.ASC);
			this.referenceDataManager.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				custDisputeProductListViewModelList = referenceDataWrapper.getReferenceDataList();
			}
		}

		else if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.MNO.longValue())
		{
			MnoSupplierListViewModel mnoSupplierListViewModel = new MnoSupplierListViewModel();
			mnoSupplierListViewModel.setMnoUserId(appUserModel.getMnoUserId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(mnoSupplierListViewModel);
			baseWrapper = this.customerDisputeProductManager.searchMNOSupplier(baseWrapper);
			mnoSupplierListViewModel = (MnoSupplierListViewModel) baseWrapper.getBasePersistableModel();

			if (mnoSupplierListViewModel.getSupplierId() != null)
			{
				custDisputeProductListViewModel.setSupplierId(mnoSupplierListViewModel.getSupplierId());

				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
						custDisputeProductListViewModel, "productName", SortingOrder.ASC);

				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					custDisputeProductListViewModelList = referenceDataWrapper.getReferenceDataList();
				}
			}
		}
		SearchBaseWrapper baseWrapper = new SearchBaseWrapperImpl();
		salesManager.getSupplierProcessingStatus(baseWrapper);
		CustomList<CustomList> supCustomlist = baseWrapper.getCustomList();		
		
		List list = supCustomlist.getResultsetList();
		
		referenceDataMap.put("supplierProcessingStatusModelList", list);

		referenceDataMap.put("custDisputeProductListViewModelList", custDisputeProductListViewModelList);
		
//		//loading bank data
//		List<BankModel> bankModelList = null;
//		try
//		{
//			BankModel bankModel = new BankModel();
//			bankModel.setActive(true);
//			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
//					bankModel, "name", SortingOrder.ASC);
//			referenceDataManager.getReferenceData(referenceDataWrapper);
//			if (referenceDataWrapper.getReferenceDataList() != null)
//			{
//				bankModelList = referenceDataWrapper.getReferenceDataList();
//			}
//		}
//		catch (Exception ex)
//		{
//			log.error("Error getting product data in loadReferenceData", ex);
//			ex.printStackTrace();
//		}
//		referenceDataMap.put("bankModelList", bankModelList);
		
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		AppUserModel appUserModel = UserUtils.getCurrentUser();

		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        if(sortingOrderMap.isEmpty()){
        	sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER.longValue())
		{
			ExtendedSalesListViewModel extendedSalesListViewModel = (ExtendedSalesListViewModel) model;

			extendedSalesListViewModel.setSupplierId(appUserModel.getSupplierUserIdSupplierUserModel().getSupplierId());

			DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn",
					extendedSalesListViewModel.getStartDate(), extendedSalesListViewModel.getEndDate());

			searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
			searchBaseWrapper.setBasePersistableModel((SalesListViewModel) extendedSalesListViewModel);
			searchBaseWrapper = this.salesManager.searchSales(searchBaseWrapper);
			return new ModelAndView(super.getSuccessView(), "salesModelList", searchBaseWrapper.getCustomList()
					.getResultsetList());
		}

		else if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.MNO.longValue())

		{
			ExtendedSalesListViewModel extendedSalesListViewModel = (ExtendedSalesListViewModel) model;
			MnoModel mnoModel = new MnoModel();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			mnoModel.setPrimaryKey(appUserModel.getMnoUserIdMnoUserModel().getMnoId());
			baseWrapper.setBasePersistableModel(mnoModel);
			baseWrapper = mnoManager.loadMno(baseWrapper);
			if (null != baseWrapper.getBasePersistableModel())
			{
				mnoModel = (MnoModel) baseWrapper.getBasePersistableModel();
				if (null != mnoModel.getSupplierIdSupplierModel())
				{
					extendedSalesListViewModel.setSupplierId(mnoModel.getSupplierId());
					DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn",
							extendedSalesListViewModel.getStartDate(), extendedSalesListViewModel.getEndDate());

					searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
					searchBaseWrapper.setBasePersistableModel((SalesListViewModel) extendedSalesListViewModel);
					searchBaseWrapper = this.salesManager.searchSales(searchBaseWrapper);
					return new ModelAndView(super.getSuccessView(), "salesModelList", searchBaseWrapper.getCustomList()
							.getResultsetList());

				}

			}

		}

		searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
		searchBaseWrapper.getPagingHelperModel().setRowCountRequired(false);
		searchBaseWrapper.getPagingHelperModel().setPageNo(0);
		searchBaseWrapper.getPagingHelperModel().setPageSize(0);
		return new ModelAndView(super.getSuccessView());

		//    ProductModel newProductModel = new ProductModel();

	}

	public void setSalesManager(SalesManager salesManager)
	{
		this.salesManager = salesManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setCustomerDisputeProductManager(CustomerDisputeProductManager customerDisputeProductManager)
	{
		this.customerDisputeProductManager = customerDisputeProductManager;
	}

	public void setMnoManager(MnoManager mnoManager)
	{
		this.mnoManager = mnoManager;
	}
}
