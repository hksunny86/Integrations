package com.inov8.integration.channel.optasia.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseCode",
        "responseDescription",
})
public class ECIBDataResponse extends Response implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private String responseDescription;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("200") && this.getResponseCode() != null) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription("Fail");
        }
        return i8SBSwitchControllerResponseVO;
    }
}
