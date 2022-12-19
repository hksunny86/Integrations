package com.inov8.microbank.server.service.portal.bookmemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface BookMeTransactionDetailManager {

    public SearchBaseWrapper searchBookMeTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
