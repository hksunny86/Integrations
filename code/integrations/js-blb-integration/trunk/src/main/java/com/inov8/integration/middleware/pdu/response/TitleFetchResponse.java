package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TitleFetchResponse")
public class TitleFetchResponse {

    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "Cnic")
    private String cnic;
    @XmlElement(name = "CustomerName")
    private String customerName;
    @XmlElement(name = "AccountTitle")
    private String accountTitle;

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }



    @XmlElement(name = "MobileNumber")
    private String customerMobile;

    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "Balance")
    private String balance;

    public String getRemainingDebitLimit() {
        return remainingDebitLimit;
    }

    public void setRemainingDebitLimit(String remainingDebitLimit) {
        this.remainingDebitLimit = remainingDebitLimit;
    }

    @XmlElement(name = "RemainingDebitLimit")
    private String remainingDebitLimit;
    @XmlElement(name = "RemainingCreditLimit")
    private  String remainigCreditLimit;

    public String getRemainigCreditLimit() {
        return remainigCreditLimit;
    }

    public void setRemainigCreditLimit(String remainigCreditLimit) {
        this.remainigCreditLimit = remainigCreditLimit;
    }

    @XmlElement(name = "ConsumedVelocity")
private String consumedVilocity;

    public String getConsumedVilocity() {
        return consumedVilocity;
    }

    public void setConsumedVilocity(String consumedVilocity) {
        this.consumedVilocity = consumedVilocity;
    }

    @XmlElement(name="DateHash")
    private String dataHash;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }



    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
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


}
