package com.inov8.microbank.webapp.action.commandmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.commandmodule.CommandListViewModel;
import com.inov8.microbank.server.service.commandmodulelist.CommandListViewManager;

public class CommandListViewController extends BaseSearchController {

		private CommandListViewManager commandListViewManager; 
		
	
		public CommandListViewController()
		  {
		    super.setFilterSearchCommandClass(CommandListViewModel.class);
		    
		  }
		
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		CommandListViewModel commandListViewModel=(CommandListViewModel)object;
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		searchBaseWrapper.setBasePersistableModel(commandListViewModel);
		if(linkedHashMap.isEmpty())
		    linkedHashMap.put("name", SortingOrder.ASC);
		searchBaseWrapper.setSortingOrderMap(linkedHashMap);
		searchBaseWrapper=this.commandListViewManager.searchCommandListView(searchBaseWrapper);
		
		 	
		return new ModelAndView(getSearchView(), "commandList", searchBaseWrapper.getCustomList().getResultsetList());
	}

	public void setCommandListViewManager(
			CommandListViewManager commandListViewManager) {
		this.commandListViewManager = commandListViewManager;
	}

	


}
