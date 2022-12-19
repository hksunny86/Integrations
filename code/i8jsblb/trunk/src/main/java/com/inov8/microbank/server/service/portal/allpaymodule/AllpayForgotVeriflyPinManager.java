package com.inov8.microbank.server.service.portal.allpaymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface AllpayForgotVeriflyPinManager {
    public SearchBaseWrapper searchForgotVeriflyPin(SearchBaseWrapper
            searchBaseWrapper) throws FrameworkCheckedException;
    
    public BaseWrapper changeVeriflyPin(BaseWrapper
    		baseWrapper) throws FrameworkCheckedException;
}
