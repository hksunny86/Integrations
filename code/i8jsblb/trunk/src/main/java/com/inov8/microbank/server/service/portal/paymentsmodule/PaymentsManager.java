package com.inov8.microbank.server.service.portal.paymentsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface PaymentsManager {
	
	public SearchBaseWrapper searchPayments(SearchBaseWrapper searchBaseWrapper)throws
    FrameworkCheckedException;

}
