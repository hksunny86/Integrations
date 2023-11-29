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
        "reversalID",
        "reversalStatus"
})
public class WasaBillReversalRequest extends Request {

    @JsonProperty("consumerNum")
    private String consumerNum;
    @JsonProperty("TransID")
    private String TransId;
    @JsonProperty("reversalID")
    private String reversalId;
    @JsonProperty("reversalStatus")
    private String reversalStatus;

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

    public String getReversalId() {
        return reversalId;
    }

    public void setReversalId(String reversalId) {
        this.reversalId = reversalId;
    }

    public String getReversalStatus() {
        return reversalStatus;
    }

    public void setReversalStatus(String reversalStatus) {
        this.reversalStatus = reversalStatus;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setConsumerNum(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setTransId(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setReversalId(i8SBSwitchControllerRequestVO.getReversalId());
        this.setReversalStatus(i8SBSwitchControllerRequestVO.getReversalStatus());
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


