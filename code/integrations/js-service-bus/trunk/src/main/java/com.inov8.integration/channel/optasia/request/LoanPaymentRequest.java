package com.inov8.integration.channel.optasia.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sourceRequestId",
        "amount",
        "currencyCode",
        "reason"
})
public class LoanPaymentRequest extends Request{

    @JsonProperty("sourceRequestId")
    private String sourceRequestId;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("reason")
    private String reason;

    @JsonProperty("sourceRequestId")
    public String getSourceRequestId() {
        return sourceRequestId;
    }

    @JsonProperty("sourceRequestId")
    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setSourceRequestId(i8SBSwitchControllerRequestVO.getSourceRequestId());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setCurrencyCode(i8SBSwitchControllerRequestVO.getCurrencyCode());
        this.setReason(i8SBSwitchControllerRequestVO.getReason());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getSourceRequestId())) {
            throw new I8SBValidationException("[Failed] Source Request Id:" + this.getSourceRequestId());
        }
        if (StringUtils.isEmpty(this.getAmount())) {
            throw new I8SBValidationException("[Failed] Transaction Amount:" + this.getAmount());
        }
        if (StringUtils.isEmpty(this.getCurrencyCode())) {
            throw new I8SBValidationException("[Failed] Message Title:" + this.getCurrencyCode());
        }
        if (StringUtils.isEmpty(this.getReason())) {
            throw new I8SBValidationException("[Failed] Message Loan Reason:" + this.getReason());
        }

        return true;
    }
}
