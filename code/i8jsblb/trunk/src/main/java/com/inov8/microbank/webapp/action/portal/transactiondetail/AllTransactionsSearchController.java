package com.inov8.microbank.webapp.action.portal.transactiondetail;

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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.ExtendedTransactionAllViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.portal.transactiondetail.TransactionDetailFacade;

public class AllTransactionsSearchController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailFacade transactionDetailFacade;


	public AllTransactionsSearchController()
	{
		 super.setCommandName("extendedTransactionAllViewModel");
		 super.setCommandClass(ExtendedTransactionAllViewModel.class);
	}

	@Override
	protected Map<String, List<?>> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
		Map<String, List<?>> refDataMap = new HashMap<String, List<?>>();

		SupplierProcessingStatusModel supplierProcessingStatusModel = new SupplierProcessingStatusModel();
		ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( supplierProcessingStatusModel, "name", SortingOrder.ASC );
		referenceDataManager.getReferenceData( refDataWrapper );
		refDataMap.put( "supplierProcessingStatusList", refDataWrapper.getReferenceDataList() );
	    return refDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			Object model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedTransactionAllViewModel extendedTransactionAllViewModel = (ExtendedTransactionAllViewModel) model;

		searchBaseWrapper.setBasePersistableModel( (TransactionAllViewModel) extendedTransactionAllViewModel );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedTransactionAllViewModel.getStartDate(),
																					extendedTransactionAllViewModel.getEndDate());

		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		//sorting order
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<TransactionAllViewModel> list = this.transactionDetailFacade.searchTransactionAllView( searchBaseWrapper );
		String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
		return new ModelAndView( successView, "transactionAllViewModelList", list.getResultsetList());
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
	{
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailFacade( TransactionDetailFacade transactionDetailFacade )
	{
		this.transactionDetailFacade = transactionDetailFacade;
	}

}
