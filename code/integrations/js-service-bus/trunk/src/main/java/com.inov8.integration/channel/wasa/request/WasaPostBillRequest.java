package com.inov8.integration.channel.wasa.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "consumerNum",
        "amount",
        "consumer_cell",
        "transactionID"
})
public class WasaPostBillRequest extends Request {

    @JsonProperty("consumerNum")
    private String consumerNum;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("consumer_cell")
    private String consumerCell;
    @JsonProperty("transactionID")
    private String transactionId;

    public String getConsumerNum() {
        return consumerNum;
    }

    public void setConsumerNum(String consumerNum) {
        this.consumerNum = consumerNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getConsumerCell() {
        return consumerCell;
    }

    public void setConsumerCell(String consumerCell) {
        this.consumerCell = consumerCell;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setConsumerNum(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setConsumerCell(i8SBSwitchControllerRequestVO.getConsumerCell());
        this.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getConsumerNum())) {
            throw new I8SBValidationException("[Failed] Consumer Number: " + this.getConsumerNum());
        }
        if (StringUtils.isEmpty(this.getTransactionId())) {
            throw new I8SBValidationException("[Failed] Transaction Id: " + this.getTransactionId());
        }
        return true;
    }
}


