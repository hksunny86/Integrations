package com.inov8.microbank.webapp.action.auditlogmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.auditlogmodule.AuditLogListViewModel;
import com.inov8.microbank.server.service.auditlogmodule.AuditLogListViewManager;


public class AuditLogListViewController extends BaseSearchController {

	private AuditLogListViewManager auditLogListViewManager; 
	
	
	public AuditLogListViewController()
	  {
	    super.setFilterSearchCommandClass(AuditLogListViewModel.class);
	    
	  }
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		AuditLogListViewModel auditLogListViewModel=(AuditLogListViewModel)object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setBasePersistableModel(auditLogListViewModel);
		if(linkedHashMap.isEmpty())	
			linkedHashMap.put("auditLogId", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(linkedHashMap);
		searchBaseWrapper=this.auditLogListViewManager.searchAuditLogListView(searchBaseWrapper);
		
		 	
		return new ModelAndView(getSearchView(), "auditLogList", searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setAuditLogListViewManager(
			AuditLogListViewManager auditLogListViewManager) {
		this.auditLogListViewManager = auditLogListViewManager;
	}



}
