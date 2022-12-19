package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.SettlementTransactionDetailViewModel;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeManager;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;

public class OfSettlementTransactionDetailViewController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailI8Manager transactionDetailI8Manager;
	private DeviceTypeManager deviceTypeManager;

	public OfSettlementTransactionDetailViewController()
	{
		 super.setCommandName("settlementTransactionDetailViewModel");
		 super.setCommandClass(SettlementTransactionDetailViewModel.class);
	}

	protected Map<String,Object> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		
	    return null;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		SettlementTransactionDetailViewModel  settlementTransactionDetailViewModel = (SettlementTransactionDetailViewModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"settlementDate", settlementTransactionDetailViewModel.getStartDate(),
				settlementTransactionDetailViewModel.getEndDate());
				
		searchBaseWrapper.setBasePersistableModel(settlementTransactionDetailViewModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		//sorting order 
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("settlementDate", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		
		searchBaseWrapper = this.transactionDetailI8Manager.searchOFSettlementTransactionSummary(searchBaseWrapper);
		
		List<SettlementTransactionDetailViewModel> list = new ArrayList<SettlementTransactionDetailViewModel>(0);
		if(searchBaseWrapper.getCustomList() != null)
		{
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}
		return new ModelAndView( getSuccessView(), "settlementTransactionDetailModelList", list);
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
