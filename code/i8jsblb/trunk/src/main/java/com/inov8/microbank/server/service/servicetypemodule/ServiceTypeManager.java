package com.inov8.microbank.server.service.servicetypemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface ServiceTypeManager {


	public SearchBaseWrapper searchServiceType(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;

	 public BaseWrapper loadServiceType(BaseWrapper   baseWrapper) throws
     FrameworkCheckedException;

	 public BaseWrapper updateServiceType(BaseWrapper   baseWrapper) throws
     FrameworkCheckedException;

}
