package com.inov8.microbank.server.service.currencycodemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface CurrencyCodeManager {
	
	SearchBaseWrapper loadCurrencyCode(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;

}
