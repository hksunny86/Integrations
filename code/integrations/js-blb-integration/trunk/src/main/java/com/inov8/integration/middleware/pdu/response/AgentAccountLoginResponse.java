package com.inov8.integration.middleware.pdu.response;


import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentAccountLoginResponse")
public class AgentAccountLoginResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "AccountTitle")
    private String accountTitle;
    @XmlElement(name = "AgentNetwork")
    private String agentNetwork;
    @XmlElement(name = "AccountBalance")
    private String balance;
    @XmlElement(name = "DailyDebitLimit")
    private String dailyDebitLimit;
    @XmlElement(name = "MonthlyDebitLimit")
    private String monthlyDebitLimit;
    @XmlElement(name = "DailyCreditLimit")
    private String dailyCreditLimit;
    @XmlElement(name = "monthlyCreditLimit")
    private String monthlyCreditLimit;
    @XmlElement(name = "HashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
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

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAgentNetwork() {
        return agentNetwork;
    }

    public void setAgentNetwork(String agentNetwork) {
        this.agentNetwork = agentNetwork;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDailyDebitLimit() {
        return dailyDebitLimit;
    }

    public void setDailyDebitLimit(String dailyDebitLimit) {
        this.dailyDebitLimit = dailyDebitLimit;
    }

    public String getMonthlyDebitLimit() {
        return monthlyDebitLimit;
    }

    public void setMonthlyDebitLimit(String monthlyDebitLimit) {
        this.monthlyDebitLimit = monthlyDebitLimit;
    }

    public String getDailyCreditLimit() {
        return dailyCreditLimit;
    }

    public void setDailyCreditLimit(String dailyCreditLimit) {
        this.dailyCreditLimit = dailyCreditLimit;
    }

    public String getMonthlyCreditLimit() {
        return monthlyCreditLimit;
    }

    public void setMonthlyCreditLimit(String monthlyCreditLimit) {
        this.monthlyCreditLimit = monthlyCreditLimit;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
