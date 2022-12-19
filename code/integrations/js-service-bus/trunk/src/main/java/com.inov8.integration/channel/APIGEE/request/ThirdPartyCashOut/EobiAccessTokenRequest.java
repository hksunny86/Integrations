package com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut;

import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import java.util.List;

public class EobiAccessTokenRequest extends Request {
    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
