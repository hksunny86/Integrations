package com.inov8.integration.channel.LoanIntimation.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanIntimationResponse extends Response {

    @JsonProperty("Errors")
    List< String > Errors = null;
    private String responseCode;

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO=new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode().equalsIgnoreCase(I8SBResponseCodeEnum.PROCESSED.getValue())){
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());

        }else {
            i8SBSwitchControllerResponseVO.setDescription(String.valueOf(this.getErrors().get(0)));
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        }
        return i8SBSwitchControllerResponseVO;
    }

    public List<String> getErrors() {
        return Errors;
    }

    public void setErrors(List<String> errors) {
        Errors = errors;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
