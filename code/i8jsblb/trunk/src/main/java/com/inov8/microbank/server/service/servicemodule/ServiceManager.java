package com.inov8.microbank.server.service.servicemodule;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.util.LabelValueBean;

public interface ServiceManager 
{
	
	public BaseWrapper loadService(BaseWrapper searchBaseWrapper) throws
    	FrameworkCheckedException;

	public BaseWrapper createOrUpdateService(BaseWrapper baseWrapper) throws
    	FrameworkCheckedException;
	
	public SearchBaseWrapper searchService(SearchBaseWrapper searchBaseWrapper)throws
	FrameworkCheckedException;

	List<LabelValueBean> getServiceLabels(Long... pk);

}
