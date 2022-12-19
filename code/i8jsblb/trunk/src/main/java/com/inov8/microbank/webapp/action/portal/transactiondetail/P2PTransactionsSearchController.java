package com.inov8.microbank.webapp.action.portal.transactiondetail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.util.PortalConstants;
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
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.server.facade.portal.transactiondetail.TransactionDetailFacade;

public class P2PTransactionsSearchController extends BaseFormSearchController
{
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailFacade transactionDetailFacade;


	public P2PTransactionsSearchController()
	{
		 super.setCommandName("extendedTransactionAllViewModel");
		 super.setCommandClass(ExtendedTransactionAllViewModel.class);
	}

	@Override
	protected Map<String, List<?>> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
	{
	    return new HashMap<String, List<?>>();
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
		extendedTransactionAllViewModel.setProductId(ProductConstantsInterface.CASH_TRANSFER);
		searchBaseWrapper.setBasePersistableModel( (TransactionAllViewModel) extendedTransactionAllViewModel );
		searchBaseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.KEY_SEARCH_P2P_TRX_USECASE_ID);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedTransactionAllViewModel.getStartDate(),extendedTransactionAllViewModel.getEndDate());

		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		if( sortingOrderMap.isEmpty() ){
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<TransactionAllViewModel> list = this.transactionDetailFacade.searchTransactionAllView( searchBaseWrapper );
		
		return new ModelAndView(getSuccessView(), "transactionAllViewModelList", list.getResultsetList());

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
