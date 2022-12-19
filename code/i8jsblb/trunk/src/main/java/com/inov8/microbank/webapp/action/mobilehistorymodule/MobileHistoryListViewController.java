package com.inov8.microbank.webapp.action.mobilehistorymodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.mobilehistorymodule.MobileHistoryListViewModel;
import com.inov8.microbank.server.service.mobilehistorymodule.MobileHistoryListViewManager;

public class MobileHistoryListViewController extends BaseSearchController{

		private MobileHistoryListViewManager mobileHistoryListViewManager;
	
	public MobileHistoryListViewController()
	  {
	    super.setFilterSearchCommandClass(MobileHistoryListViewModel.class);
	    
	  }
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		MobileHistoryListViewModel mobileHistoryListViewModel=(MobileHistoryListViewModel)object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setBasePersistableModel(mobileHistoryListViewModel);
		if(linkedHashMap.isEmpty())
		    linkedHashMap.put("mfsId", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(linkedHashMap);
		searchBaseWrapper=this.mobileHistoryListViewManager.searchMobileHistory(searchBaseWrapper);
		 	
		return new ModelAndView(getSearchView(), "mobileHistoryList", searchBaseWrapper.getCustomList().getResultsetList());
	}
	public void setMobileHistoryListViewManager(
			MobileHistoryListViewManager mobileHistoryListViewManager) {
		this.mobileHistoryListViewManager = mobileHistoryListViewManager;
	}


}
