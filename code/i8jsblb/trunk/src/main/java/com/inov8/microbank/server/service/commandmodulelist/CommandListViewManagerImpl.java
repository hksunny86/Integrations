package com.inov8.microbank.server.service.commandmodulelist;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.commandmodule.CommandListViewModel;
import com.inov8.microbank.server.dao.commandmodule.CommandListViewDAO;


public class CommandListViewManagerImpl implements CommandListViewManager {

	private CommandListViewDAO commandListViewDAO;
	
	public SearchBaseWrapper searchCommandListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		
		CustomList<CommandListViewModel> list = this.commandListViewDAO
		.findByExample((CommandListViewModel) searchBaseWrapper
				.getBasePersistableModel(), searchBaseWrapper
				.getPagingHelperModel(), searchBaseWrapper
				.getSortingOrderMap());
					searchBaseWrapper.setCustomList(list);
		
		
		return searchBaseWrapper;
	}
	public void setCommandListViewDAO(CommandListViewDAO commandListViewDAO) {
		this.commandListViewDAO = commandListViewDAO;
	}



}
