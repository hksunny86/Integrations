package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "AccountTitle",
        "FatherName",
        "RegistrationState",
        "AccountState",
        "DailyCreditLimit",
        "DailyDebitLimit",
        "MonthlyCreditLimit",
        "MonthlyDebitLimit",
        "YearlyCreditLimit",
        "YearlyDebitLimit",
        "HashData"
})
public class AccountStatusResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("AccountTitle")
    private String accountTitle;
    @JsonProperty("FatherName")
    private String fatherName;
    @JsonProperty("RegistrationState")
    private String registrationState;
    @JsonProperty("AccountState")
    private String accountState;
    @JsonProperty("DailyCreditLimit")
    private String dailyCreditLimit;
    @JsonProperty("DailyDebitLimit")
    private String dailyDebitLimit;
    @JsonProperty("MonthlyCreditLimit")
    private String monthlyCreditLimit;
    @JsonProperty("MonthlyDebitLimit")
    private String monthlyDebitLimit;
    @JsonProperty("YearlyCreditLimit")
    private String yearlyCreditLimit;
    @JsonProperty("YearlyDebitLimit")
    private String yearlyDebitLimit;
    @JsonProperty("HashData")
    private String hashData;

    @JsonProperty("Rrn")
    public String getRrn() {
        return rrn;
    }

    @JsonProperty("Rrn")
    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @JsonProperty("ResponseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("ResponseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("ResponseDescription")
    public String getResponseDescription() {
        return responseDescription;
    }

    @JsonProperty("ResponseDescription")
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @JsonProperty("ResponseDateTime")
    public String getResponseDateTime() {
        return responseDateTime;
    }

    @JsonProperty("ResponseDateTime")
    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    @JsonProperty("AccountTitle")
    public String getAccountTitle() {
        return accountTitle;
    }

    @JsonProperty("AccountTitle")
    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    @JsonProperty("FatherName")
    public String getFatherName() {
        return fatherName;
    }

    @JsonProperty("FatherName")
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @JsonProperty("RegistrationState")
    public String getRegistrationState() {
        return registrationState;
    }

    @JsonProperty("RegistrationState")
    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }

    @JsonProperty("AccountState")
    public String getAccountState() {
        return accountState;
    }

    @JsonProperty("AccountState")
    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    @JsonProperty("DailyCreditLimit")
    public String getDailyCreditLimit() {
        return dailyCreditLimit;
    }

    @JsonProperty("DailyCreditLimit")
    public void setDailyCreditLimit(String dailyCreditLimit) {
        this.dailyCreditLimit = dailyCreditLimit;
    }

    @JsonProperty("DailyDebitLimit")
    public String getDailyDebitLimit() {
        return dailyDebitLimit;
    }

    @JsonProperty("DailyDebitLimit")
    public void setDailyDebitLimit(String dailyDebitLimit) {
        this.dailyDebitLimit = dailyDebitLimit;
    }

    @JsonProperty("MonthlyCreditLimit")
    public String getMonthlyCreditLimit() {
        return monthlyCreditLimit;
    }

    @JsonProperty("MonthlyCreditLimit")
    public void setMonthlyCreditLimit(String monthlyCreditLimit) {
        this.monthlyCreditLimit = monthlyCreditLimit;
    }

    @JsonProperty("MonthlyDebitLimit")
    public String getMonthlyDebitLimit() {
        return monthlyDebitLimit;
    }

    @JsonProperty("MonthlyDebitLimit")
    public void setMonthlyDebitLimit(String monthlyDebitLimit) {
        this.monthlyDebitLimit = monthlyDebitLimit;
    }

    @JsonProperty("YearlyCreditLimit")
    public String getYearlyCreditLimit() {
        return yearlyCreditLimit;
    }

    @JsonProperty("YearlyCreditLimit")
    public void setYearlyCreditLimit(String yearlyCreditLimit) {
        this.yearlyCreditLimit = yearlyCreditLimit;
    }

    @JsonProperty("YearlyDebitLimit")
    public String getYearlyDebitLimit() {
        return yearlyDebitLimit;
    }

    @JsonProperty("YearlyDebitLimit")
    public void setYearlyDebitLimit(String yearlyDebitLimit) {
        this.yearlyDebitLimit = yearlyDebitLimit;
    }

    @JsonProperty("HashData")
    public String getHashData() {
        return hashData;
    }

    @JsonProperty("HashData")
    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

}