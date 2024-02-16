package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rrn",
        "responseCode",
        "responseDescription",
        "responseDateTime",
        "glAccountNo",
        "transactionDateTime",
        "transactionProcessingAmount",
        "commissionAmount",
        "totalAmount",
        "transactionId",
        "transactionAmount",
        "receiverGlAccountNo",
        "remainingBalance",
        "hashData",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlToGlPaymentResponse implements Serializable {

    private static final long serialVersionUID = 6009165415929861808L;

    @XmlElement(name = "rrn")
    private String rrn;
    @XmlElement(name = "responseCode")
    private String responseCode;
    @XmlElement(name = "responseDescription")
    private String responseDescription;
    @XmlElement(name = "responseDateTime")
    private String responseDateTime;
    @XmlElement(name = "glAccountNo")
    private String glAccountNo;
    @XmlElement(name = "transactionDateTime")
    private String transactionDateTime;
    @XmlElement(name = "transactionProcessingAmount")
    private String transactionProcessingAmount;
    @XmlElement(name = "commissionAmount")
    private String commissionAmount;
    @XmlElement(name = "totalAmount")
    private String totalAmount;
    @XmlElement(name = "transactionId")
    private String transactionId;
    @XmlElement(name = "transactionAmount")
    private String transactionAmount;
    @XmlElement(name = "receiverGlAccountNo")
    private String receiverGlAccountNo;
    @XmlElement(name = "remainingBalance")
    private String remainingBalance;
    @XmlElement(name = "hashData")
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

    public String getGlAccountNo() {
        return glAccountNo;
    }

    public void setGlAccountNo(String glAccountNo) {
        this.glAccountNo = glAccountNo;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTransactionProcessingAmount() {
        return transactionProcessingAmount;
    }

    public void setTransactionProcessingAmount(String transactionProcessingAmount) {
        this.transactionProcessingAmount = transactionProcessingAmount;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getReceiverGlAccountNo() {
        return receiverGlAccountNo;
    }

    public void setReceiverGlAccountNo(String receiverGlAccountNo) {
        this.receiverGlAccountNo = receiverGlAccountNo;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
