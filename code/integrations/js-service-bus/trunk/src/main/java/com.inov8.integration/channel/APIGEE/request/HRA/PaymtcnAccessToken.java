package com.inov8.integration.channel.APIGEE.request.HRA;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

/**
 * Created by Inov8 on 8/23/2019.
 */
public class PaymtcnAccessToken extends Request {

    @JsonProperty("grant_type")
    private String grantType;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setGrantType("client_credentials");

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
