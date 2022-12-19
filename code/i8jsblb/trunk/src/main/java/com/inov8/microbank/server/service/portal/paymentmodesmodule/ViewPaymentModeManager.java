package com.inov8.microbank.server.service.portal.paymentmodesmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface ViewPaymentModeManager
{

	public SearchBaseWrapper viewAvailablePaymentModeList(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;


}
