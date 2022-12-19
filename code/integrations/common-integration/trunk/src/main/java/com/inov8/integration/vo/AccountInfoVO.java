package com.inov8.integration.vo;

import java.io.Serializable;

public class AccountInfoVO implements Serializable
{
    private static final long serialVersionUID = 2000475637838176039L;
    private String accountNo;
    private String accountTitle;
    private String accountType;
    private String accountCurrency;
    private String accountBalance;
    private String accountStatus;
    private String accountBankIMD;
    private String accountBranchCode;
    private String ibanNumber;
    private String currencyMnemonic;
    private String accountBranchName;
    private String accountDefault;
    private String cardNo;
    private String cardStatus;
    private String cardExpiry;



    public String getAccountNo()
    {
        return this.accountNo;
    }

    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }

    public String getAccountTitle()
    {
        return this.accountTitle;
    }

    public void setAccountTitle(String accountTitle)
    {
        this.accountTitle = accountTitle;
    }

    public String getAccountType()
    {
        return this.accountType;
    }

    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }

    public String getAccountCurrency()
    {
        return this.accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency)
    {
        this.accountCurrency = accountCurrency;
    }

    public String getAccountBalance()
    {
        return this.accountBalance;
    }

    public void setAccountBalance(String accountBalance)
    {
        this.accountBalance = accountBalance;
    }

    public String getAccountStatus()
    {
        return this.accountStatus;
    }

    public void setAccountStatus(String accountStatus)
    {
        this.accountStatus = accountStatus;
    }

    public String getAccountBankIMD() {
        return accountBankIMD;
    }

    public void setAccountBankIMD(String accountBankIMD) {
        this.accountBankIMD = accountBankIMD;
    }

    public String getAccountBranchCode() {
        return accountBranchCode;
    }

    public void setAccountBranchCode(String accountBranchCode) {
        this.accountBranchCode = accountBranchCode;
    }

    public String getIbanNumber() {
        return ibanNumber;
    }

    public void setIbanNumber(String ibanNumber) {
        this.ibanNumber = ibanNumber;
    }

    public String getCurrencyMnemonic() {
        return currencyMnemonic;
    }

    public void setCurrencyMnemonic(String currencyMnemonic) {
        this.currencyMnemonic = currencyMnemonic;
    }

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getAccountDefault() {
        return accountDefault;
    }

    public void setAccountDefault(String accountDefault) {
        this.accountDefault = accountDefault;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    @Override
    public String toString() {
        return "AccountInfoVO{" +
                "accountNo='" + accountNo + '\'' +
                ", accountTitle='" + accountTitle + '\'' +
                ", accountType='" + accountType + '\'' +
                ", accountCurrency='" + accountCurrency + '\'' +
                ", accountBalance='" + accountBalance + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", accountBankIMD='" + accountBankIMD + '\'' +
                ", accountBranchCode='" + accountBranchCode + '\'' +
                '}';
    }
}
