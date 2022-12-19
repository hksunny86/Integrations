package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.springframework.web.bind.ServletRequestUtils;
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
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.model.SettlementTransactionViewModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.integration.vo.OFSettlementTransactionVO;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;

public class OfSettlementTransactionViewController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailI8Manager transactionDetailI8Manager;
	private DeviceTypeManager deviceTypeManager;

	public OfSettlementTransactionViewController()
	{
		 super.setCommandName("settlementTransactionViewModel");
		 super.setCommandClass(SettlementTransactionViewModel.class);
	}

	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();

	    if (log.isDebugEnabled())
	    {
	      log.debug("Inside reference data");
	    }

		/**
		 * code fragment to load reference data for SupplierModel
		 */
		String strSupplierId = httpServletRequest.getParameter("supplierId");

		SupplierModel supplierModel = new SupplierModel();
		supplierModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				supplierModel, "name", SortingOrder.ASC);
		try
		{
		
		referenceDataManager.getReferenceData(referenceDataWrapper);

		List<SupplierModel> supplierModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			supplierModelList = referenceDataWrapper.getReferenceDataList();
		}

		ProductModel productModel = null;
		List<ProductModel> productModelList = null;

		if( !GenericValidator.isBlankOrNull( strSupplierId ) )
		{
				Long supplierId = Long.parseLong(strSupplierId);
				productModel = new ProductModel();
				productModel.setSupplierId(supplierId);
				productModel.setActive(true);
				referenceDataWrapper = new ReferenceDataWrapperImpl(
						productModel, "name", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				if (referenceDataWrapper.getReferenceDataList() != null)
				{
					productModelList = referenceDataWrapper.getReferenceDataList();
				}
		}

		referenceDataMap.put("supplierModelList", supplierModelList);
		referenceDataMap.put("productModelList", productModelList);
		}
		catch (Exception ex)
		{
			log.error("Error getting product data in loadReferenceData", ex);
			ex.printStackTrace();
		}

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

		SettlementTransactionViewModel  settlementTransactionViewModel = (SettlementTransactionViewModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", settlementTransactionViewModel.getStartDate(),
				settlementTransactionViewModel.getEndDate());
				
		searchBaseWrapper.setBasePersistableModel(settlementTransactionViewModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		//sorting order 
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		
		searchBaseWrapper = this.transactionDetailI8Manager.searchOFSettlementTransaction(searchBaseWrapper);
		
		List<SettlementTransactionViewModel> list = null;
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView( getSuccessView(), "settlementTransactionModelList", list);
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailI8Manager(TransactionDetailI8Manager transactionDetailI8Manager)
	{
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}

	public DeviceTypeManager getDeviceTypeManager()
    {
        return deviceTypeManager;
    }

	public void setDeviceTypeManager( DeviceTypeManager deviceTypeManager )
    {
        this.deviceTypeManager = deviceTypeManager;
    }

}
