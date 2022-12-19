package com.inov8.microbank.server.service.portal.forgotveriflypinmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;

public interface ForgotVeriflyPinManager {
    public SearchBaseWrapper searchForgotVeriflyPin(SearchBaseWrapper
            searchBaseWrapper) throws FrameworkCheckedException;
    
    public BaseWrapper changeVeriflyPin(BaseWrapper
    		baseWrapper) throws FrameworkCheckedException;
    public BaseWrapper changeAllPayVeriflyPin(BaseWrapper
    		baseWrapper) throws FrameworkCheckedException;
    public AppUserModel isRetailerOrDistributor (String appUserId);
}
