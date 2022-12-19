package com.inov8.microbank.server.service.portal.mnosearchusermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface MnoSearchMfsUserManager {

	SearchBaseWrapper searchMfsUser(SearchBaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

}
