package com.inov8.integration.channel.wasa.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public abstract class Request {

    public abstract void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO);
    public abstract boolean validateRequest() throws I8SBValidationException;
}
