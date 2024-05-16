package com.inov8.integration.channel.sbp.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "processingCode",
        "merchantType",
        "OrgnlmessageId",
        "OrgnlTransactionDate"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnPaymentRequest extends Request {

    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("OrgnlmessageId")
    private String OrgnlmessageId;
    @JsonProperty("OrgnlTransactionDate")
    private String OrgnlTransactionDate;

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getOrgnlmessageId() {
        return OrgnlmessageId;
    }

    public void setOrgnlmessageId(String orgnlmessageId) {
        OrgnlmessageId = orgnlmessageId;
    }

    public String getOrgnlTransactionDate() {
        return OrgnlTransactionDate;
    }

    public void setOrgnlTransactionDate(String orgnlTransactionDate) {
        OrgnlTransactionDate = orgnlTransactionDate;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setProcessingCode(i8SBSwitchControllerRequestVO.getProcessingCode());
        this.setMerchantType(i8SBSwitchControllerRequestVO.getMerchantType());
        this.setOrgnlmessageId(i8SBSwitchControllerRequestVO.getMessageId());
        this.setOrgnlTransactionDate(i8SBSwitchControllerRequestVO.getTransactionDate());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


