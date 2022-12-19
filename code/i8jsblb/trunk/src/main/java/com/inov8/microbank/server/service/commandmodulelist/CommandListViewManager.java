package com.inov8.microbank.server.service.commandmodulelist;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface CommandListViewManager {

	public SearchBaseWrapper searchCommandListView(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;


}
