package com.inov8.microbank.server.service.applicationversionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public interface ApplicationVersionManager
{
	SearchBaseWrapper loadApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper loadApplicationVersion(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

	SearchBaseWrapper searchApplicationVersion(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper updateApplicationVersion(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper createApplicationVersion(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
}
