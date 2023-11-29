package com.inov8.integration.channel.wasa.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "consumerNum",
        "TransID",
        "payMode"
})
public class WasaBillClearanceRequest extends Request {

    @JsonProperty("consumerNum")
    private String consumerNum;
    @JsonProperty("TransID")
    private String TransId;
    @JsonProperty("payMode")
    private String payMode;

    public String getConsumerNum() {
        return consumerNum;
    }

    public void setConsumerNum(String consumerNum) {
        this.consumerNum = consumerNum;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setConsumerNum(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setTransId(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setPayMode(i8SBSwitchControllerRequestVO.getPayMode());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getConsumerNum())) {
            throw new I8SBValidationException("[Failed] Consumer Number: " + this.getConsumerNum());
        }
        if (StringUtils.isEmpty(this.getTransId())) {
            throw new I8SBValidationException("[Failed] Transaction Id: " + this.getTransId());
        }
        return true;
    }
}


