package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by bkr on 8/9/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MiniStatement {

    private String deviceTypeId;
    private String pin;
    private String encryptionType;
    private String bankId;
    private String ip;
    private String accountNo;
    private String retailerId;
    private String balance;
    private String balanceFormatted;
    private List<Transaction> transactions;
    private String sessionId;
    private String loyaltyPoints;
    private String sharedSession;

    public String getSharedSession() {
        return sharedSession;
    }

    public void setSharedSession(String sharedSession) {
        this.sharedSession = sharedSession;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceFormatted() {
        return balanceFormatted;
    }

    public void setBalanceFormatted(String balanceFormatted) {
        this.balanceFormatted = balanceFormatted;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
