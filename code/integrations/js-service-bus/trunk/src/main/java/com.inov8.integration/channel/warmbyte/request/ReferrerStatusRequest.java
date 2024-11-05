package com.inov8.integration.channel.warmbyte.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "msisdn",
        "cnic"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferrerStatusRequest extends Request {

    @JsonProperty("msisdn")
    private String msisdn;
    @JsonProperty("cnic")
    private String cnic;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMsisdn(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


