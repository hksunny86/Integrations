package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonPropertyOrder({
        "rrn",
        "responseCode",
        "responseDescription",
        "dateTime",
        "transactionId",
        "commission",
        "amount",
        "totalAmount",
        "hashData",
})
public class OptasiaCreditInquiryResponse implements Serializable {

    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseDescription")
    private String responseDescription;
    @JsonProperty("dateTime")
    private String responseDateTime;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("commission")
    private String comissionAmount;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("totalAmount")
    private String totalAmount;
    @JsonProperty("hashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getComissionAmount() {
        return comissionAmount;
    }

    public void setComissionAmount(String comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
