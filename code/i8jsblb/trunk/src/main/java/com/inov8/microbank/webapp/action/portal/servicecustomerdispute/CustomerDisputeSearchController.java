package com.inov8.microbank.webapp.action.portal.servicecustomerdispute;

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
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.CustDispTranBlPmListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.CustDisputeServiceListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.ExtendedCustDisputeTranBlPmListViewModel;
import com.inov8.microbank.common.model.portal.servicecustomerdisputemodule.MnoSupplierListViewModel;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.servicecustomerdispute.CustomerDisputeManager;


public class CustomerDisputeSearchController extends BaseFormSearchController 
{
	
	ReferenceDataManager referenceDataManager;
	CustomerDisputeManager customerDisputeManager;

	
	public CustomerDisputeSearchController()
	{
		 super.setCommandName("extendedCustDisputeTranBlPmListViewModel");
		 super.setCommandClass(ExtendedCustDisputeTranBlPmListViewModel.class);
	}

	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		/**
		 * code fragment to load reference data for ProductModel
		 */
		Map<String,Object> referenceDataMap = new HashMap<>();
		CustDisputeServiceListViewModel custDisputeServiceListViewModel = new CustDisputeServiceListViewModel();
		List<CustDisputeServiceListViewModel> custDisputeServiceListViewModelList = null;
		AppUserModel appUserModel = UserUtils.getCurrentUser();
		
		if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.SUPPLIER.longValue())
		{
			custDisputeServiceListViewModel.setSupplierId(appUserModel.
					getSupplierUserIdSupplierUserModel().
					getSupplierId());
			custDisputeServiceListViewModel.setServiceTypeId(ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT);
						
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					custDisputeServiceListViewModel, "productName", SortingOrder.ASC);
			
			referenceDataManager.getReferenceData(referenceDataWrapper);
		
			if (referenceDataWrapper.getReferenceDataList() != null)
			{
				custDisputeServiceListViewModelList = referenceDataWrapper.getReferenceDataList();
			}
		}
		
		else if(appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.MNO.longValue())
		{
			MnoSupplierListViewModel mnoSupplierListViewModel = new MnoSupplierListViewModel();
			mnoSupplierListViewModel.setMnoUserId(appUserModel.getMnoUserId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(mnoSupplierListViewModel);
			baseWrapper = this.customerDisputeManager.searchMNOSupplier(baseWrapper);
			mnoSupplierListViewModel = (MnoSupplierListViewModel)baseWrapper.getBasePersistableModel();                               
			
			if(mnoSupplierListViewModel.getSupplierId() != null)
			{
				custDisputeServiceListViewModel.setSupplierId(mnoSupplierListViewModel.getSupplierId());
				custDisputeServiceListViewModel.setServiceTypeId(ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT);
				
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					custDisputeServiceListViewModel, "productName", SortingOrder.ASC);
			
				referenceDataManager.getReferenceData(referenceDataWrapper);

				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					custDisputeServiceListViewModelList = referenceDataWrapper.getReferenceDataList();
				}
			}
		}
	
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
		
		referenceDataMap.put("custDisputeServiceList", custDisputeServiceListViewModelList);
		return referenceDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedCustDisputeTranBlPmListViewModel extendedCustDisputeTranBlPmListViewModel = (ExtendedCustDisputeTranBlPmListViewModel) model;
		
		extendedCustDisputeTranBlPmListViewModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", extendedCustDisputeTranBlPmListViewModel.getStartDate(),
				extendedCustDisputeTranBlPmListViewModel.getEndDate());
		searchBaseWrapper.setBasePersistableModel((CustDispTranBlPmListViewModel)extendedCustDisputeTranBlPmListViewModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		
        if(sortingOrderMap.isEmpty()){
        	sortingOrderMap.put("createdOn", SortingOrder.DESC);
        }
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper = this.customerDisputeManager.searchCustomerDisputeTransactionForBillPayment(searchBaseWrapper);
		
		List<CustDispTranBlPmListViewModel> list = null;
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView(super.getSuccessView(), "customerDisputeList",
				list);
	}


	public void setCustomerDisputeManager(CustomerDisputeManager customerDisputeManager)
	{
		this.customerDisputeManager = customerDisputeManager;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

}
