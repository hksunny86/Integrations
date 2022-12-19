package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.List;
@XStreamAlias("AccountStatement")
public class AccountStatement implements Serializable {
    private final static long serialVersionUID = 1L;

    private String workingBalance;
    private String openingBalance;
    private String runningBalance;
    private String address;
    private String date;
    private String accountType;
    private List<Transaction> transactions;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getWorkingBalance() {
        return workingBalance;
    }

    public void setWorkingBalance(String workingBalance) {
        this.workingBalance = workingBalance;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(String runningBalance) {
        this.runningBalance = runningBalance;
    }
}
