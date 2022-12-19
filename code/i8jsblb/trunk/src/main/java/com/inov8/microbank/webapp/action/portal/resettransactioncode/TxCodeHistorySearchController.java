package com.inov8.microbank.webapp.action.portal.resettransactioncode;

import java.util.ArrayList;
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
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.minitransactionmodule.ExtendedTransactionCodeHistoryViewModel;
import com.inov8.microbank.common.model.minitransactionmodule.TransactionCodeHistoryViewModel;
import com.inov8.microbank.server.facade.portal.transactiondetail.TransactionDetailFacade;

public class TxCodeHistorySearchController extends BaseFormSearchController {
	
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailFacade transactionDetailFacade;
	
	public TxCodeHistorySearchController() {
		 super.setCommandName("extendedTransactionCodeHtrViewModel");
		 super.setCommandClass(ExtendedTransactionCodeHistoryViewModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception {
		Map<String,Object> referenceDataMap = new HashMap<String,Object>();
		
		UsecaseModel usecaseModel = new UsecaseModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				usecaseModel, "name", SortingOrder.ASC);
		List<UsecaseModel> usecaseModelList =  referenceDataManager.getReferenceData(referenceDataWrapper).getReferenceDataList();
		List<UsecaseModel> newUsecaseModelList = new ArrayList<UsecaseModel>();
		for(int i=0;i<usecaseModelList.size();i++){
			if(usecaseModelList.get(i).getUsecaseId()==1035 ||usecaseModelList.get(i).getUsecaseId()==1095 ){
				newUsecaseModelList.add(usecaseModelList.get(i));
			}
		}
		
		referenceDataMap.put("usecaseModelList", newUsecaseModelList);
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,HttpServletResponse response,Object model,
			PagingHelperModel pagingHelperModel,LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedTransactionCodeHistoryViewModel extendedTransactionCodeHtrViewModel = (ExtendedTransactionCodeHistoryViewModel) model;

		searchBaseWrapper.setBasePersistableModel( (TransactionCodeHistoryViewModel) extendedTransactionCodeHtrViewModel );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedTransactionCodeHtrViewModel.getStartDate(),
				extendedTransactionCodeHtrViewModel.getEndDate());

		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		//sorting order
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<TransactionCodeHistoryViewModel> list = this.transactionDetailFacade.searchTransactionCodeHtrViewModel( searchBaseWrapper ).getCustomList();

		return new ModelAndView( getSuccessView(), "transactionCodeHtrViewModelList", list.getResultsetList());
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	
	public void setTransactionDetailFacade(
			TransactionDetailFacade transactionDetailFacade) {
		this.transactionDetailFacade = transactionDetailFacade;
	}

}
