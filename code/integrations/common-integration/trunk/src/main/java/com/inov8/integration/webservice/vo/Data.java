package com.inov8.integration.webservice.vo;

import java.io.Serializable;

public class Data implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String rrn;
    private String responseCode;
    private String responseDescription;
    private String responseDateTime;
//    private String transactionId;
    private String commissionAmount;
    private String totalAmount;
//    private String totalTransactionAmount;
    private String productMaxLimit;
    private String productMinLimit;
    private String dailyCreditRemainingLimit;
    private String monthlyCreditRemainingLimit;
    private String accountTitle;
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

    public String getProductMaxLimit() {
        return productMaxLimit;
    }

    public void setProductMaxLimit(String productMaxLimit) {
        this.productMaxLimit = productMaxLimit;
    }

    public String getProductMinLimit() {
        return productMinLimit;
    }

    public void setProductMinLimit(String productMinLimit) {
        this.productMinLimit = productMinLimit;
    }

    public String getDailyCreditRemainingLimit() {
        return dailyCreditRemainingLimit;
    }

    public void setDailyCreditRemainingLimit(String dailyCreditRemainingLimit) {
        this.dailyCreditRemainingLimit = dailyCreditRemainingLimit;
    }

    public String getMonthlyCreditRemainingLimit() {
        return monthlyCreditRemainingLimit;
    }

    public void setMonthlyCreditRemainingLimit(String monthlyCreditRemainingLimit) {
        this.monthlyCreditRemainingLimit = monthlyCreditRemainingLimit;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
