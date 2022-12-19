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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.ExtendedSenderRedeemViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.SenderRedeemViewModel;
import com.inov8.microbank.server.facade.portal.transactiondetail.TransactionDetailFacade;

public class SenderRedeemSearchController extends BaseFormSearchController
{
	private TransactionDetailFacade transactionDetailFacade;

	
	public SenderRedeemSearchController()
	{
		 super.setCommandName("extendedSenderRedeemViewModel");
		 super.setCommandClass(ExtendedSenderRedeemViewModel.class);
	}

	@Override
	protected Map<String, List<SupplierProcessingStatusModel>> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception{
		Map<String, List<SupplierProcessingStatusModel>> refDataMap = new HashMap<String, List<SupplierProcessingStatusModel>>();
		Long[] ids = { 4L, 5L, 6L };
		refDataMap.put( "supplierProcessingStatusList", transactionDetailFacade.loadSupplierProcessingStatuses( ids ) );
	    return refDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model,
				PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedSenderRedeemViewModel extendedSenderRedeemViewModel = (ExtendedSenderRedeemViewModel) model;
		searchBaseWrapper.setBasePersistableModel( (SenderRedeemViewModel) extendedSenderRedeemViewModel );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedSenderRedeemViewModel.getStartDate(),
																			extendedSenderRedeemViewModel.getEndDate());

		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		if(sortingOrderMap.isEmpty()){
			sortingOrderMap.put( "createdOn", SortingOrder.DESC );
		}		
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
		
		CustomList<SenderRedeemViewModel> list = this.transactionDetailFacade.searchSenderRedeemView( searchBaseWrapper );

		return new ModelAndView( getSuccessView(), "senderRedeemViewModelList", list.getResultsetList());
	}

	public void setTransactionDetailFacade( TransactionDetailFacade transactionDetailFacade ) {
		this.transactionDetailFacade = transactionDetailFacade;
	}

}
