package com.inov8.microbank.server.service.auditlogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface AuditLogListViewManager {

	public SearchBaseWrapper searchAuditLogListView(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException; 
	
	public BaseWrapper loadAuditLog(BaseWrapper  baseWrapper) 
	throws FrameworkCheckedException;

}
