package com.inov8.microbank.server.service.tickermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface TickerManager {

	public SearchBaseWrapper searchTickerUser(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	public SearchBaseWrapper loadTickerUser(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	public BaseWrapper createTickerUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;

	public BaseWrapper updateTickerUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;
	
	public SearchBaseWrapper searchTicker(SearchBaseWrapper searchBaseWrapper)
	        throws FrameworkCheckedException;
	
	public BaseWrapper loadDefaultTicker(BaseWrapper baseWrapper)
	        throws FrameworkCheckedException;

}
