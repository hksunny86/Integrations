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
        "code",
        "message"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeductionIntimationResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(DeductionIntimationResponse.class.getSimpleName());

    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getCode() != null) {
            if (this.getCode().equalsIgnoreCase("00")) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
            }
            i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
        }

        return i8SBSwitchControllerResponseVO;
    }
}