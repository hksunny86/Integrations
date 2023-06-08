package com.inov8.integration.channel.brandverse.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "merchantXid",
        "transactionId",
        "amount",
        "timestamp"
})
public class BrandverseNotifyRequest extends Request {

    @JsonProperty("merchantXid")
    private String merchantXid;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("merchantXid")
    public String getMerchantXid() {
        return merchantXid;
    }

    @JsonProperty("merchantXid")
    public void setMerchantXid(String merchantXid) {
        this.merchantXid = merchantXid;
    }

    @JsonProperty("transactionId")
    public String getTransactionId() {
        return transactionId;
    }

    @JsonProperty("transactionId")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMerchantXid(i8SBSwitchControllerRequestVO.getMerchantId());
        this.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setTimestamp(i8SBSwitchControllerRequestVO.getTransactionDateTime());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMerchantXid())) {
            throw new I8SBValidationException("[Failed] Merchant Id:" + this.getMerchantXid());
        }
        if (StringUtils.isEmpty(this.getTransactionId())) {
            throw new I8SBValidationException("[Failed] Transaction Id:" + this.getTransactionId());
        }
        if (StringUtils.isEmpty(this.getAmount())) {
            throw new I8SBValidationException("[Failed] Amount :" + this.getAmount());
        }
        return true;
    }
}
