package com.inov8.integration.channel.esbT24.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(TransactionResponse.class.getSimpleName());

    private String responseCode;
    private String description;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode() != null) {
            if (this.getResponseCode().equalsIgnoreCase("00")) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            }
            i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        }

        return i8SBSwitchControllerResponseVO;
    }
}