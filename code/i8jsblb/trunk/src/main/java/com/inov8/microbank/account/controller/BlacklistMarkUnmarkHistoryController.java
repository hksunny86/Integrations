package com.inov8.microbank.account.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.account.model.BlacklistMarkUnmarkHistoryModel;
import com.inov8.microbank.account.service.AccountControlManager;

public class BlacklistMarkUnmarkHistoryController extends BaseSearchController  {

	 private AccountControlManager accountControlManager;
	
	public BlacklistMarkUnmarkHistoryController(){
		
		super.setFilterSearchCommandClass(BlacklistMarkUnmarkHistoryModel.class);
      
	}
	 	
	public void setAccountControlManager(AccountControlManager accountControlManager) {
		this.accountControlManager = accountControlManager;
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model,
			HttpServletRequest req, LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        BlacklistMarkUnmarkHistoryModel blacklistMarkUnmarkHistoryModel = (BlacklistMarkUnmarkHistoryModel) model;
        Map<String,Object> detailMap = new HashMap<>();
        String cnicNo = req.getParameter("cnicNo");
        blacklistMarkUnmarkHistoryModel.setCnicNo(cnicNo);
        searchBaseWrapper.setBasePersistableModel(blacklistMarkUnmarkHistoryModel);
                
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "updatedOn", blacklistMarkUnmarkHistoryModel.getStartDate(), blacklistMarkUnmarkHistoryModel.getEndDate());
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
          
        if( sortingOrderMap.isEmpty() ){
            sortingOrderMap.put( "updatedOn", SortingOrder.DESC );
        }        
        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
         
        searchBaseWrapper = this.accountControlManager.loadBlacklistMarkUnmarkHistory(searchBaseWrapper);
        List<BlacklistMarkUnmarkHistoryModel> list = searchBaseWrapper.getCustomList().getResultsetList();
        return new ModelAndView(getSearchView(),"bulkManualAdjustmentModelList",list );
	}
	
	
	
	
	
	

}
