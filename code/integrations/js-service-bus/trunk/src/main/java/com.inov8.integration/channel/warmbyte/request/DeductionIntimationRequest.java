package com.inov8.integration.channel.warmbyte.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mobileNo"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeductionIntimationRequest extends Request {

    @JsonProperty("mobileNo")
    private String mobileNo;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


