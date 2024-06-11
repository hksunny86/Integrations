package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IbftTitleFetchResponse")
public class LoginAuthenticationResponse {
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "IBAN")
    private String iban;
    @XmlElement(name = "AccountTitle")
    private String accountTitle;
    @XmlElement(name = "AccountLevel")
    private String accountLevel;
    @XmlElement(name = "AccountBalance")
    private String balance;
    @XmlElement(name = "DailyCreditLimit")
    private String dailyCreditLimit;
    @XmlElement(name = "DailyDebitLimit")
    private String dailyDebitLimit;
    @XmlElement(name = "MonthlyCreditLimit")
    private String monthlyCreditLimit;
    @XmlElement(name = "MonthlyDebitLimit")
    private String monthlyDebitLimit;
    @XmlElement(name = "YearlyCreditLimit")
    private String yearlyCreditLimit;
    @XmlElement(name = "YearlyDebitLimit")
    private String yearlyDebitLimit;
    @XmlElement(name = "Segment")
    private String segment;
    @XmlElement(name = "BVS")
    private String bvs;
    @XmlElement(name = "BlinkBvs")
    private String BlinlBvs;
    @XmlElement(name = "FreelanceAccountBalance")
    private String freelanceAccountBalance;
    @XmlElement(name = "RemaingCoolOffTime")
    private String remaingCoolOffTime;
    @XmlElement(name = "HashData")
    private String hashData;


    public String getBvs() {
        return bvs;
    }

    public void setBvs(String bvs) {
        this.bvs = bvs;
    }

    public String getBlinlBvs() {
        return BlinlBvs;
    }

    public void setBlinlBvs(String blinlBvs) {
        BlinlBvs = blinlBvs;
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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getDailyCreditLimit() {
        return dailyCreditLimit;
    }

    public void setDailyCreditLimit(String dailyCreditLimit) {
        this.dailyCreditLimit = dailyCreditLimit;
    }

    public String getDailyDebitLimit() {
        return dailyDebitLimit;
    }

    public void setDailyDebitLimit(String dailyDebitLimit) {
        this.dailyDebitLimit = dailyDebitLimit;
    }

    public String getMonthlyCreditLimit() {
        return monthlyCreditLimit;
    }

    public void setMonthlyCreditLimit(String monthlyCreditLimit) {
        this.monthlyCreditLimit = monthlyCreditLimit;
    }

    public String getMonthlyDebitLimit() {
        return monthlyDebitLimit;
    }

    public void setMonthlyDebitLimit(String monthlyDebitLimit) {
        this.monthlyDebitLimit = monthlyDebitLimit;
    }

    public String getYearlyCreditLimit() {
        return yearlyCreditLimit;
    }

    public void setYearlyCreditLimit(String yearlyCreditLimit) {
        this.yearlyCreditLimit = yearlyCreditLimit;
    }

    public String getYearlyDebitLimit() {
        return yearlyDebitLimit;
    }

    public void setYearlyDebitLimit(String yearlyDebitLimit) {
        this.yearlyDebitLimit = yearlyDebitLimit;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getFreelanceAccountBalance() {
        return freelanceAccountBalance;
    }

    public void setFreelanceAccountBalance(String freelanceAccountBalance) {
        this.freelanceAccountBalance = freelanceAccountBalance;
    }

    public String getRemaingCoolOffTime() {
        return remaingCoolOffTime;
    }

    public void setRemaingCoolOffTime(String remaingCoolOffTime) {
        this.remaingCoolOffTime = remaingCoolOffTime;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
