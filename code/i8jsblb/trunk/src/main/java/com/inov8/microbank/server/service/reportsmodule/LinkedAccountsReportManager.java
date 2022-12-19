package com.inov8.microbank.server.service.reportsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface LinkedAccountsReportManager {

	SearchBaseWrapper searchAccounts(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;
}
