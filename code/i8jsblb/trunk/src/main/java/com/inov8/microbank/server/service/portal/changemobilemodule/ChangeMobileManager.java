package com.inov8.microbank.server.service.portal.changemobilemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;


public interface ChangeMobileManager 
{
	SearchBaseWrapper loadChangeMobile(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper loadChangeMobile(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	SearchBaseWrapper searchChangeMobile(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper updateChangeMobile(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	BaseWrapper createChangeMobile(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;
}
