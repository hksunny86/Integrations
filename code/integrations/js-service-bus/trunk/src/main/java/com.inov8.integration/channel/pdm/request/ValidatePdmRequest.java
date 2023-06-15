package com.inov8.integration.channel.pdm.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.request.Request;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cnic",
        "msisdn"
})
public class ValidatePdmRequest extends Request {

    @JsonProperty("cnic")
    private String cnic;
    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("cnic")
    public String getCnic() {
        return cnic;
    }

    @JsonProperty("cnic")
    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @JsonProperty("msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    @JsonProperty("msisdn")
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setMsisdn(i8SBSwitchControllerRequestVO.getMobileNumber());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed] CNIC:" + this.getCnic());
        }
        if (StringUtils.isEmpty(this.getMsisdn())) {
            throw new I8SBValidationException("[Failed] Mobile Number:" + this.getMsisdn());
        }
        return true;
    }
}
