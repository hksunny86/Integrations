package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by Administrator on 10/29/2019.
 */
@XStreamAlias("AccountStatementData")
public class AccountStatementData implements Serializable {
    private final static long serialVersionUID = 1L;
    private String transactionDate;
    private String transactionDecs;
    private String instrumentCode;
    private String debitAtmTrans;
    private String creditAtmTrans;
    private String remainingBalance;
    private String transactionReferenceNumber;
    private String particular;
    private String debitAmount;
    private String creditAmount;
    private String closingBalance;
    private String statementDate;
    private String statementPeriod;
    private String branchName;
    private String branchAddress;
    private String accountDesc;
    private String customerAccountNumber;
    private String customerBranchCode;
    private String withdrawlAmount;
    private String fatherName;
    private String ibanAccountNumber;
    private String address1;
    private String address2;
    private String address3;
    private String prevBalance;
    private String fromDate;
    private String toDate;


    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDecs() {
        return transactionDecs;
    }

    public void setTransactionDecs(String transactionDecs) {
        this.transactionDecs = transactionDecs;
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode) {
        this.instrumentCode = instrumentCode;
    }

    public String getDebitAtmTrans() {
        return debitAtmTrans;
    }

    public void setDebitAtmTrans(String debitAtmTrans) {
        this.debitAtmTrans = debitAtmTrans;
    }

    public String getCreditAtmTrans() {
        return creditAtmTrans;
    }

    public void setCreditAtmTrans(String creditAtmTrans) {
        this.creditAtmTrans = creditAtmTrans;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(String statementDate) {
        this.statementDate = statementDate;
    }

    public String getStatementPeriod() {
        return statementPeriod;
    }

    public void setStatementPeriod(String statementPeriod) {
        this.statementPeriod = statementPeriod;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }

    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public String getCustomerBranchCode() {
        return customerBranchCode;
    }

    public void setCustomerBranchCode(String customerBranchCode) {
        this.customerBranchCode = customerBranchCode;
    }

    public String getWithdrawlAmount() {
        return withdrawlAmount;
    }

    public void setWithdrawlAmount(String withdrawlAmount) {
        this.withdrawlAmount = withdrawlAmount;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getIbanAccountNumber() {
        return ibanAccountNumber;
    }

    public void setIbanAccountNumber(String ibanAccountNumber) {
        this.ibanAccountNumber = ibanAccountNumber;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPrevBalance() {
        return prevBalance;
    }

    public void setPrevBalance(String prevBalance) {
        this.prevBalance = prevBalance;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
