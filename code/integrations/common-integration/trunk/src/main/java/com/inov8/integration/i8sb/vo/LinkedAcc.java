package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("LinkedAcc")
public class LinkedAcc implements Serializable {

    private final static long serialVersionUID = 1L;

    private String linkedAccNumber;
    private String linkedAccTitle;
    private String transactionsList;
    private String transactionAccNumber;
    private String transactionDateTime;
    private String transactionDesc;
    private String transactionAmount;
    private String transactionAmountPKR;

    public String getLinkedAccNumber() {
        return linkedAccNumber;
    }

    public void setLinkedAccNumber(String linkedAccNumber) {
        this.linkedAccNumber = linkedAccNumber;
    }

    public String getLinkedAccTitle() {
        return linkedAccTitle;
    }

    public void setLinkedAccTitle(String linkedAccTitle) {
        this.linkedAccTitle = linkedAccTitle;
    }

    public String getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(String transactionsList) {
        this.transactionsList = transactionsList;
    }

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
}
