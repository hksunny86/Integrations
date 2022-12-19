package com.inov8.microbank.server.service.mobilehistorymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface MobileHistoryListViewManager {

	public SearchBaseWrapper searchMobileHistory(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;


}
