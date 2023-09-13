package com.inov8.integration.corporate.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "Cnic",
        "CustomerName",
        "AccountTitle",
        "AccountLevel",
        "MobileNumber",
        "Balance",
        "RemainingDebitLimit",
        "RemainingCreditLimit",
        "ConsumedVelocity",
        "HashData"
})
public class CorporateTitleFetchResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("CustomerName")
    private String customerName;
    @JsonProperty("AccountTitle")
    private String accountTitle;
    @JsonProperty("AccountLevel")
    private String accountLevel;
    @JsonProperty("MobileNumber")
    private String customerMobile;
    @JsonProperty("Balance")
    private String balance;
    @JsonProperty("RemainingDebitLimit")
    private String remainingDebitLimit;
    @JsonProperty("RemainingCreditLimit")
    private String remainingCreditLimit;
    @JsonProperty("ConsumedVelocity")
    private String consumedVelocity;
    @JsonProperty("HashData")
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

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRemainingDebitLimit() {
        return remainingDebitLimit;
    }

    public void setRemainingDebitLimit(String remainingDebitLimit) {
        this.remainingDebitLimit = remainingDebitLimit;
    }

    public String getRemainingCreditLimit() {
        return remainingCreditLimit;
    }

    public void setRemainingCreditLimit(String remainingCreditLimit) {
        this.remainingCreditLimit = remainingCreditLimit;
    }

    public String getConsumedVelocity() {
        return consumedVelocity;
    }

    public void setConsumedVelocity(String consumedVelocity) {
        this.consumedVelocity = consumedVelocity;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}