package com.inov8.integration.channel.wasa.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "consumerNum",
})
public class WasaGetBillRequest extends Request {

    @JsonProperty("consumerNum")
    private String consumerNum;

    public String getConsumerNum() {
        return consumerNum;
    }

    public void setConsumerNum(String consumerNum) {
        this.consumerNum = consumerNum;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setConsumerNum(i8SBSwitchControllerRequestVO.getConsumerNumber());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getConsumerNum())) {
            throw new I8SBValidationException("[Failed] Consumer Number: " + this.getConsumerNum());
        }
        return true;
    }
}


