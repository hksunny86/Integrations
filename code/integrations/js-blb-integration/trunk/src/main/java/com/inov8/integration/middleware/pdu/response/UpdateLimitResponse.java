package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UpdateCustomerLimitResponse")
public class UpdateLimitResponse implements Serializable{

    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "AccountNumber ")
    private String accountNumber;
    @XmlElement(name = "Cnic")
    private String cnic;
    @XmlElement(name = "DailyRemainingCreditLimit")
    private String dailyRemainingCreditLimit ;
    @XmlElement(name = "DailyRemainingDebitLimit")
    private String dailyRemainingDebitLimit ;
    @XmlElement(name = "DateLocalTransaction")
    private String dateLocalTransaction;
    @XmlElement(name = "MTI")
    private String mti;
    @XmlElement(name = "MonthlyRemainingCreditLimit")
    private String monthlyRemainingCreditLimit;
    @XmlElement(name = "MonthlyRemainingDebitLimit")
    private String monthlyRemainingDebitLimit;
    @XmlElement(name = "ProcessingCode")
    private String processingCode;
    @XmlElement(name = "ReceivingLimitDaily")
    private String receivingLimitDaily;
    @XmlElement(name = "ReceivingLimitMonthly")
    private String receivingLimitMonthly;
    @XmlElement(name = "ReceivingLimitYearly")
    private String receivingLimitYearly;
    @XmlElement(name = "RemainingReceivingLimitDaily")
    private String remainingReceivingLimitDaily;
    @XmlElement(name = "RemainingReceivingLimitMonthly")
    private String remainingReceivingLimitMonthly;
    @XmlElement(name = "RemainingReceivingLimitYearly")
    private String remainingReceivingLimitYearly;
    @XmlElement(name = "RemainingSendingLimitDaily")
    private String remainingSendingLimitDaily;
    @XmlElement(name = "RemainingSendingLimitMonthly")
    private String remainingSendingLimitMonthly;
    @XmlElement(name = "RemainingSendingLimitYearly")
    private String remainingSendingLimitYearly;
    @XmlElement(name = "SendingLimitDaily")
    private String sendingLimitDaily;
    @XmlElement(name = "SendingLimitMonthly")
    private String sendingLimitMonthly;
    @XmlElement(name = "SendingLimitYearly")
    private String sendingLimitYearly;
    @XmlElement(name = "SystemsTraceAuditNumber")
    private String systemsTraceAuditNumber;
    @XmlElement(name = "TimeLocalTransaction")
    private String timeLocalTransaction;
    @XmlElement(name = "TransmissionDatetime")
    private String transmissionDatetime;
    @XmlElement(name = "YearlyRemainingCreditLimit")
    private String yearlyRemainingCreditLimit;
    @XmlElement(name = "YearlyRemainingDebitLimit")
    private String yearlyRemainingDebitLimit;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "HashData")
    private String hashData;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDailyRemainingCreditLimit() {
        return dailyRemainingCreditLimit;
    }

    public void setDailyRemainingCreditLimit(String dailyRemainingCreditLimit) {
        this.dailyRemainingCreditLimit = dailyRemainingCreditLimit;
    }

    public String getDailyRemainingDebitLimit() {
        return dailyRemainingDebitLimit;
    }

    public void setDailyRemainingDebitLimit(String dailyRemainingDebitLimit) {
        this.dailyRemainingDebitLimit = dailyRemainingDebitLimit;
    }

    public String getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(String dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getMonthlyRemainingCreditLimit() {
        return monthlyRemainingCreditLimit;
    }

    public void setMonthlyRemainingCreditLimit(String monthlyRemainingCreditLimit) {
        this.monthlyRemainingCreditLimit = monthlyRemainingCreditLimit;
    }

    public String getMonthlyRemainingDebitLimit() {
        return monthlyRemainingDebitLimit;
    }

    public void setMonthlyRemainingDebitLimit(String monthlyRemainingDebitLimit) {
        this.monthlyRemainingDebitLimit = monthlyRemainingDebitLimit;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getReceivingLimitDaily() {
        return receivingLimitDaily;
    }

    public void setReceivingLimitDaily(String receivingLimitDaily) {
        this.receivingLimitDaily = receivingLimitDaily;
    }

    public String getReceivingLimitMonthly() {
        return receivingLimitMonthly;
    }

    public void setReceivingLimitMonthly(String receivingLimitMonthly) {
        this.receivingLimitMonthly = receivingLimitMonthly;
    }

    public String getReceivingLimitYearly() {
        return receivingLimitYearly;
    }

    public void setReceivingLimitYearly(String receivingLimitYearly) {
        this.receivingLimitYearly = receivingLimitYearly;
    }

    public String getRemainingReceivingLimitDaily() {
        return remainingReceivingLimitDaily;
    }

    public void setRemainingReceivingLimitDaily(String remainingReceivingLimitDaily) {
        this.remainingReceivingLimitDaily = remainingReceivingLimitDaily;
    }

    public String getRemainingReceivingLimitMonthly() {
        return remainingReceivingLimitMonthly;
    }

    public void setRemainingReceivingLimitMonthly(String remainingReceivingLimitMonthly) {
        this.remainingReceivingLimitMonthly = remainingReceivingLimitMonthly;
    }

    public String getRemainingReceivingLimitYearly() {
        return remainingReceivingLimitYearly;
    }

    public void setRemainingReceivingLimitYearly(String remainingReceivingLimitYearly) {
        this.remainingReceivingLimitYearly = remainingReceivingLimitYearly;
    }

    public String getRemainingSendingLimitDaily() {
        return remainingSendingLimitDaily;
    }

    public void setRemainingSendingLimitDaily(String remainingSendingLimitDaily) {
        this.remainingSendingLimitDaily = remainingSendingLimitDaily;
    }

    public String getRemainingSendingLimitMonthly() {
        return remainingSendingLimitMonthly;
    }

    public void setRemainingSendingLimitMonthly(String remainingSendingLimitMonthly) {
        this.remainingSendingLimitMonthly = remainingSendingLimitMonthly;
    }

    public String getRemainingSendingLimitYearly() {
        return remainingSendingLimitYearly;
    }

    public void setRemainingSendingLimitYearly(String remainingSendingLimitYearly) {
        this.remainingSendingLimitYearly = remainingSendingLimitYearly;
    }

    public String getSendingLimitDaily() {
        return sendingLimitDaily;
    }

    public void setSendingLimitDaily(String sendingLimitDaily) {
        this.sendingLimitDaily = sendingLimitDaily;
    }

    public String getSendingLimitMonthly() {
        return sendingLimitMonthly;
    }

    public void setSendingLimitMonthly(String sendingLimitMonthly) {
        this.sendingLimitMonthly = sendingLimitMonthly;
    }

    public String getSendingLimitYearly() {
        return sendingLimitYearly;
    }

    public void setSendingLimitYearly(String sendingLimitYearly) {
        this.sendingLimitYearly = sendingLimitYearly;
    }

    public String getSystemsTraceAuditNumber() {
        return systemsTraceAuditNumber;
    }

    public void setSystemsTraceAuditNumber(String systemsTraceAuditNumber) {
        this.systemsTraceAuditNumber = systemsTraceAuditNumber;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getTransmissionDatetime() {
        return transmissionDatetime;
    }

    public void setTransmissionDatetime(String transmissionDatetime) {
        this.transmissionDatetime = transmissionDatetime;
    }

    public String getYearlyRemainingCreditLimit() {
        return yearlyRemainingCreditLimit;
    }

    public void setYearlyRemainingCreditLimit(String yearlyRemainingCreditLimit) {
        this.yearlyRemainingCreditLimit = yearlyRemainingCreditLimit;
    }

    public String getYearlyRemainingDebitLimit() {
        return yearlyRemainingDebitLimit;
    }

    public void setYearlyRemainingDebitLimit(String yearlyRemainingDebitLimit) {
        this.yearlyRemainingDebitLimit = yearlyRemainingDebitLimit;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
