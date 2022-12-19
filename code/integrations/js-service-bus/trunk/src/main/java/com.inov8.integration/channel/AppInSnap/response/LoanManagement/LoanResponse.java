package com.inov8.integration.channel.AppInSnap.response.LoanManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.AppInSnap.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

public class LoanResponse extends Response {

    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("Description")
    private String description;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO=new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getStatusCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        return i8SBSwitchControllerResponseVO;
    }
}
