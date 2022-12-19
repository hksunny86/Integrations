package com.inov8.microbank.server.service.retailertypemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface RetailerTypeManager {

	public SearchBaseWrapper searchRetailerType(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;

	 public BaseWrapper loadRetailerType(BaseWrapper   baseWrapper) throws
     FrameworkCheckedException;

	 public BaseWrapper updateRetailerType(BaseWrapper   baseWrapper) throws
     FrameworkCheckedException;


}
