package com.inov8.microbank.server.service.portal.authorizationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface  ActionAuthorizationHistoryManager {
	SearchBaseWrapper search( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws FrameworkCheckedException;
}
