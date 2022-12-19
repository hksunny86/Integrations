package com.inov8.microbank.webapp.action.portal.transactiondetail;

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
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.TransactionLogModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.PhoenixTransactionSearchModel;
import com.inov8.microbank.server.service.postedtransactionmodule.PostedTransactionsManager;

public class PhoenixTransactionController extends BaseFormSearchController {

	private PostedTransactionsManager postedTransactionsManager;
	
	public PhoenixTransactionController() {
		setCommandName("phoenixTransactionSearchModel");
		setCommandClass(PhoenixTransactionSearchModel.class);
	}

	@Override
	protected Map loadReferenceData( HttpServletRequest request ) throws Exception {
		return null;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object model, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		List<TransactionLogModel> transList = new ArrayList<TransactionLogModel>();
		try{
			PhoenixTransactionSearchModel phoenixTransactionSearchModel = (PhoenixTransactionSearchModel) model;
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			TransactionLogModel transactionLogModel = new TransactionLogModel();
			transactionLogModel.setTransactionType(phoenixTransactionSearchModel.getTransactionCode());
	        searchBaseWrapper.setBasePersistableModel(transactionLogModel);
			searchBaseWrapper.setPagingHelperModel( pagingHelperModel );
			
	        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", 
	        		phoenixTransactionSearchModel.getFromDate(), phoenixTransactionSearchModel.getToDate());
	        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
	
	        if( sortingOrderMap.isEmpty()){
	            sortingOrderMap.put( "transactionDate", SortingOrder.DESC );
	            sortingOrderMap.put( "transactionLogId", SortingOrder.DESC );
	        }
	        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
	
	        searchBaseWrapper = postedTransactionsManager.searchPostedTransactions(searchBaseWrapper);
	        CustomList<TransactionLogModel> customList = searchBaseWrapper.getCustomList();
	        if( customList != null){
	        	transList = customList.getResultsetList();
	        }
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
//		pagingHelperModel.setTotalRecordsCount((transList==null)?0:transList.size());
		return new ModelAndView( getSuccessView(), "phoenixTransactionList", transList);

	}

	public void setPostedTransactionsManager(
			PostedTransactionsManager postedTransactionsManager) {
		this.postedTransactionsManager = postedTransactionsManager;
	}

}
