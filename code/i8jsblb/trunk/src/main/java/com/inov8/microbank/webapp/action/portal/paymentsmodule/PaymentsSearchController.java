package com.inov8.microbank.webapp.action.portal.paymentsmodule;

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
import com.inov8.microbank.common.model.MnoModel;
import com.inov8.microbank.common.model.portal.paymentsmodule.ExtendedPaymentsListViewModel;
import com.inov8.microbank.common.model.portal.paymentsmodule.PaymentsListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.CustDisputeServiceListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.MnoSupplierListViewModel;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.mnomodule.MnoManager;
import com.inov8.microbank.server.service.portal.paymentsmodule.PaymentsManager;
import com.inov8.microbank.server.service.portal.servicecustomerdispute.CustomerDisputeManager;

public class PaymentsSearchController extends BaseFormSearchController
{
	private PaymentsManager			paymentsManager;

	private ReferenceDataManager	referenceDataManager;

	private SmsSender				smsSender;

	private MnoManager				mnoManager;

	private CustomerDisputeManager	customerDisputeManager;

	public PaymentsSearchController()
	{
		super.setCommandName("extendedPaymentsListViewModel");
		super.setCommandClass(ExtendedPaymentsListViewModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		/**
		 * code fragment to load reference data for ProductModel
		 */
		Map<String,Object> referenceDataMap = new HashMap<>();
		CustDisputeServiceListViewModel custDisputeServiceListViewModel = new CustDisputeServiceListViewModel();
		custDisputeServiceListViewModel.setServiceTypeId(ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT);
		List<CustDisputeServiceListViewModel> custDisputeServiceListViewModelList = null;
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER.longValue())
		{
			custDisputeServiceListViewModel.setSupplierId(appUserModel.getSupplierUserIdSupplierUserModel()
					.getSupplierId());
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(custDisputeServiceListViewModel,
					"productName", SortingOrder.ASC);
			this.referenceDataManager.getReferenceData(referenceDataWrapper);
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				custDisputeServiceListViewModelList = referenceDataWrapper.getReferenceDataList();
			}
		}

		else if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.MNO.longValue())
		{
			MnoSupplierListViewModel mnoSupplierListViewModel = new MnoSupplierListViewModel();
			mnoSupplierListViewModel.setMnoUserId(appUserModel.getMnoUserId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(mnoSupplierListViewModel);
			baseWrapper = this.customerDisputeManager.searchMNOSupplier(baseWrapper);
			mnoSupplierListViewModel = (MnoSupplierListViewModel) baseWrapper.getBasePersistableModel();

			if (mnoSupplierListViewModel.getSupplierId() != null)
			{
				custDisputeServiceListViewModel.setSupplierId(mnoSupplierListViewModel.getSupplierId());
				custDisputeServiceListViewModel
						.setServiceTypeId(ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT);

				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
						custDisputeServiceListViewModel, "productName", SortingOrder.ASC);

				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					custDisputeServiceListViewModelList = referenceDataWrapper.getReferenceDataList();
				}
			}
		}

		referenceDataMap.put("custDisputeServiceListViewModelList", custDisputeServiceListViewModelList);
		
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

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		AppUserModel appUserModel = UserUtils.getCurrentUser();

		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		
        if(sortingOrderMap.isEmpty()){
        	sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER.longValue())
		{

			ExtendedPaymentsListViewModel extendedPaymentsListViewModel = (ExtendedPaymentsListViewModel) model;

			extendedPaymentsListViewModel.setSupplierId(appUserModel.getSupplierUserIdSupplierUserModel()
					.getSupplierId());

			DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn",
					extendedPaymentsListViewModel.getStartDate(), extendedPaymentsListViewModel.getEndDate());

			searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
			searchBaseWrapper.setBasePersistableModel((PaymentsListViewModel) extendedPaymentsListViewModel);
			searchBaseWrapper = this.paymentsManager.searchPayments(searchBaseWrapper);
			return new ModelAndView(super.getSuccessView(), "paymentsModelList", searchBaseWrapper.getCustomList()
					.getResultsetList());
		}

		else if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.MNO.longValue())

		{
			ExtendedPaymentsListViewModel extendedPaymentsListViewModel = (ExtendedPaymentsListViewModel) model;
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
					extendedPaymentsListViewModel.setSupplierId(mnoModel.getSupplierId());
					DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn",
							extendedPaymentsListViewModel.getStartDate(), extendedPaymentsListViewModel.getEndDate());

					searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
					searchBaseWrapper.setBasePersistableModel((PaymentsListViewModel) extendedPaymentsListViewModel);
					searchBaseWrapper = this.paymentsManager.searchPayments(searchBaseWrapper);
					return new ModelAndView(super.getSuccessView(), "paymentsModelList", searchBaseWrapper
							.getCustomList().getResultsetList());

				}

			}

		}

		searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
		searchBaseWrapper.getPagingHelperModel().setRowCountRequired(false);
		searchBaseWrapper.getPagingHelperModel().setPageNo(0);
		searchBaseWrapper.getPagingHelperModel().setPageSize(0);
		return new ModelAndView(super.getSuccessView());

		//ProductModel newProductModel = new ProductModel();

	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public void setPaymentsManager(PaymentsManager paymentsManager)
	{
		this.paymentsManager = paymentsManager;
	}

	public void setMnoManager(MnoManager mnoManager)
	{
		this.mnoManager = mnoManager;
	}

	public void setCustomerDisputeManager(CustomerDisputeManager customerDisputeManager)
	{
		this.customerDisputeManager = customerDisputeManager;
	}
}
