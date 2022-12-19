package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("CheckingAccountSummary")
public class CheckingAccountSummary implements Serializable {

    private final static long serialVersionUID = 1L;

    private String branchCode;
    private String branchName;
    private String acctNumber;
    private String acctTitle;
    private String acctTypeCode;
    private String acctCurrency;
    private String balance;
    private String balanceNature;
    private String acctStatus;
    private String acctTypeDesc;
    private String isDefault;
    private String accountIBAN;

    public String getAccountIBAN() {
        return accountIBAN;
    }

    public void setAccountIBAN(String accountIBAN) {
        this.accountIBAN = accountIBAN;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAcctNumber() {
        return acctNumber;
    }

    public void setAcctNumber(String acctNumber) {
        this.acctNumber = acctNumber;
    }

    public String getAcctTitle() {
        return acctTitle;
    }

    public void setAcctTitle(String acctTitle) {
        this.acctTitle = acctTitle;
    }

    public String getAcctTypeCode() {
        return acctTypeCode;
    }

    public void setAcctTypeCode(String acctTypeCode) {
        this.acctTypeCode = acctTypeCode;
    }

    public String getAcctCurrency() {
        return acctCurrency;
    }

    public void setAcctCurrency(String acctCurrency) {
        this.acctCurrency = acctCurrency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceNature() {
        return balanceNature;
    }

    public void setBalanceNature(String balanceNature) {
        this.balanceNature = balanceNature;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getAcctTypeDesc() {
        return acctTypeDesc;
    }

    public void setAcctTypeDesc(String acctTypeDesc) {
        this.acctTypeDesc = acctTypeDesc;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
