package com.inov8.microbank.webapp.action.portal.transactiondetail;

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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.ExtendedTransactionAllViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.transactiondetail.TransactionDetailFacade;

public class TellerTransactionSearchController extends BaseFormSearchController {
	
	private ReferenceDataManager referenceDataManager;
	private TransactionDetailFacade transactionDetailFacade;

	public TellerTransactionSearchController() {
		 super.setCommandName("extendedTransactionAllViewModel");
		 super.setCommandClass(ExtendedTransactionAllViewModel.class);
	}

	@Override
	protected Map<String, List<?>> loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		
		Map<String, List<?>> refDataMap = new HashMap<String, List<?>>();

		ProductModel productModelWalkInCashIn = new ProductModel(ProductConstantsInterface.TELLER_WALK_IN_CASH_IN);
		productModelWalkInCashIn.setName("Teller Cash In WalkIn");
		ProductModel productModelACHCashIn = new ProductModel(ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN);
		productModelACHCashIn.setName("Teller Cash In Account Holder");
		ProductModel productModelCashOut = new ProductModel(ProductConstantsInterface.TELLER_CASH_OUT);
		productModelCashOut.setName("Teller Cash Out");
		
		List<ProductModel> productList = new ArrayList<ProductModel>(0);
		productList.add(productModelWalkInCashIn);
		productList.add(productModelACHCashIn);
		productList.add(productModelCashOut);
		
		httpServletRequest.setAttribute("productList", productList);
		
//		refDataMap.put( "productList", productList);
	    
	    return refDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel,	LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedTransactionAllViewModel extendedTransactionAllViewModel = (ExtendedTransactionAllViewModel) model;
		
		extendedTransactionAllViewModel.setTellerAppUserId(UserUtils.getCurrentUser().getAppUserId());
		searchBaseWrapper.setBasePersistableModel( (TransactionAllViewModel) extendedTransactionAllViewModel );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedTransactionAllViewModel.getStartDate(),
																					extendedTransactionAllViewModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		if(sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );

		CustomList<TransactionAllViewModel> list = this.transactionDetailFacade.searchTransactionAllView( searchBaseWrapper );
		String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
		return new ModelAndView( successView, "transactionAllViewModelList", list.getResultsetList());
	}


	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setTransactionDetailFacade( TransactionDetailFacade transactionDetailFacade ) {
		this.transactionDetailFacade = transactionDetailFacade;
	}
}