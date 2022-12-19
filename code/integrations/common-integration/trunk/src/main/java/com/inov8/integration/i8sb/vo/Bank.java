package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("Bank")
public class Bank implements Serializable {
    private String bankIMD;
    private String bankName;
    private String accountNumberFormat;
    private String accountSize;
    private String accountMinSize;
    private String accountMaxSize;

    public String getBankIMD() { return bankIMD; }

    public void setBankIMD(String bankIMD) { this.bankIMD = bankIMD; }

    public String getBankName() { return bankName; }

    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountNumberFormat() { return accountNumberFormat; }

    public void setAccountNumberFormat(String accountNumberFormat) { this.accountNumberFormat = accountNumberFormat; }

    public String getAccountSize() { return accountSize; }

    public void setAccountSize(String accountSize) { this.accountSize = accountSize; }

    public String getAccountMinSize() {
        return accountMinSize;
    }

    public void setAccountMinSize(String accountMinSize) {
        this.accountMinSize = accountMinSize;
    }

    public String getAccountMaxSize() {
        return accountMaxSize;
    }

    public void setAccountMaxSize(String accountMaxSize) {
        this.accountMaxSize = accountMaxSize;
    }
}
