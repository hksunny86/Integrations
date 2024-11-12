package com.inov8.integration.channel.warmbyte.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseCode",
        "responseMessage"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferrerStatusResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(ReferrerStatusResponse.class.getSimpleName());

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseMessage")
    private String responseMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode() != null) {
            if (this.getResponseCode().equalsIgnoreCase("0020")) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            }
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseMessage());
        }

        return i8SBSwitchControllerResponseVO;
    }
}