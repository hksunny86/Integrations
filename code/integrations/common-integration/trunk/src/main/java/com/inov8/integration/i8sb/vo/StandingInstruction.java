package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("StandingInstruction")
public class StandingInstruction implements Serializable {


    private static final long serialVersionUID = -4556220836683183687L;

    private String accountId1;
    private String startDate;
    private String frequency;
    private String totalNumberOfExecution;
    private String SIID;
    private String SITranCode;
    private String SITranType;
    private String accountId2;
    private String amount;
    private String categoryCode;
    private String companyCode;
    private String companyName;
    private String consumerNo;
    private String SIType;
    private String createdOn;
    private String updatedOn;
    private String SIFrequency;
    private String SIStartDate;
    private String SIEndDate;
    private String SIStatus;
    private String SIEndOccurance;
    private String reminder;
    private String receivedDateAndTime;
    private String fromAccount;
    private String toAccount;
    private String statusDesc;
    private String IBAN;
    private String accountTitle;
    private String bankIMD;


    public String getAccountId1() {
        return accountId1;
    }

    public void setAccountId1(String accountId1) {
        this.accountId1 = accountId1;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTotalNumberOfExecution() {
        return totalNumberOfExecution;
    }

    public void setTotalNumberOfExecution(String totalNumberOfExecution) {
        this.totalNumberOfExecution = totalNumberOfExecution;
    }

    public String getSIID() {
        return SIID;
    }

    public void setSIID(String SIID) {
        this.SIID = SIID;
    }

    public String getSITranCode() {
        return SITranCode;
    }

    public void setSITranCode(String SITranCode) {
        this.SITranCode = SITranCode;
    }

    public String getAccountId2() {
        return accountId2;
    }

    public void setAccountId2(String accountId2) {
        this.accountId2 = accountId2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getSITranType() {
        return SITranType;
    }

    public void setSITranType(String SITranType) {
        this.SITranType = SITranType;
    }
    public String getSIType() {
        return SIType;
    }

    public void setSIType(String SIType) {
        this.SIType = SIType;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getSIFrequency() {
        return SIFrequency;
    }

    public void setSIFrequency(String SIFrequency) {
        this.SIFrequency = SIFrequency;
    }

    public String getSIStartDate() {
        return SIStartDate;
    }

    public void setSIStartDate(String SIStartDate) {
        this.SIStartDate = SIStartDate;
    }

    public String getSIEndDate() {
        return SIEndDate;
    }

    public void setSIEndDate(String SIEndDate) {
        this.SIEndDate = SIEndDate;
    }

    public String getSIStatus() {
        return SIStatus;
    }

    public void setSIStatus(String SIStatus) {
        this.SIStatus = SIStatus;
    }

    public String getSIEndOccurance() {
        return SIEndOccurance;
    }

    public void setSIEndOccurance(String SIEndOccurance) {
        this.SIEndOccurance = SIEndOccurance;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getReceivedDateAndTime() {
        return receivedDateAndTime;
    }

    public void setReceivedDateAndTime(String receivedDateAndTime) {
        this.receivedDateAndTime = receivedDateAndTime;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }
}
