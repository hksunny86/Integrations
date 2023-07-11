package com.inov8.integration.channel.raast.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Mobile"
})
public class CustomerAliasAccountRequest extends Request implements Serializable {

    @JsonProperty("Mobile")
    private String mobileNumber;

    @JsonProperty("Mobile")
    public String getMobileNumber() {
        return mobileNumber;
    }

    @JsonProperty("Mobile")
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMobileNumber())) {
            throw new I8SBValidationException("[Failed] Mobile Number:" + this.getMobileNumber());
        }

        return true;
    }
}
