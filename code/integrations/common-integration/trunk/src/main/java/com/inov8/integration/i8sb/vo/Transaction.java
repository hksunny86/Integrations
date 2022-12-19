package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("Transaction")
public class Transaction implements Serializable {

    private final static long serialVersionUID = 1L;

    private String transactionAccNumber;
    private String transactionDateTime;
    private String transactionDate;
    private String transactionCompleteDate;
    private String transactionDesc;
    private String transactionAmount;
    private String transactionNature;
    private String transactionAmountPKR;
    private String closingBalance;

    public String getTransactionAccNumber() {
        return transactionAccNumber;
    }

    public void setTransactionAccNumber(String transactionAccNumber) {
        this.transactionAccNumber = transactionAccNumber;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionAmountPKR() {
        return transactionAmountPKR;
    }

    public void setTransactionAmountPKR(String transactionAmountPKR) {
        this.transactionAmountPKR = transactionAmountPKR;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionCompleteDate() {
        return transactionCompleteDate;
    }

    public void setTransactionCompleteDate(String transactionCompleteDate) {
        this.transactionCompleteDate = transactionCompleteDate;
    }

    public String getTransactionNature() {
        return transactionNature;
    }

    public void setTransactionNature(String transactionNature) {
        this.transactionNature = transactionNature;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }


}
