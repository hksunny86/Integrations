package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentCashDepositPaymentResponse")
public class AgentCashDepositPaymentResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "CustomerMobilenNumber")
    private String customerMobilenNumber;
    @XmlElement(name = "CustomerCnic")
    private String customerCnic;
    @XmlElement(name = "Balance")
    private String balance;
    @XmlElement(name = "TransactionId")
    private String transactionId;
    @XmlElement(name = "TransactionAmount")
    private String amount;
    @XmlElement(name = "ComissionAmount")
    private String comissionAmount;
    @XmlElement(name = "TransactionProcessingAmount")
    private String transactionProcessingAmount;
    @XmlElement(name = "TotalAmount")
    private String totalAmount;
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

    public String getCustomerMobilenNumber() {
        return customerMobilenNumber;
    }

    public void setCustomerMobilenNumber(String customerMobilenNumber) {
        this.customerMobilenNumber = customerMobilenNumber;
    }

    public String getCustomerCnic() {
        return customerCnic;
    }

    public void setCustomerCnic(String customerCnic) {
        this.customerCnic = customerCnic;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComissionAmount() {
        return comissionAmount;
    }

    public void setComissionAmount(String comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public String getTransactionProcessingAmount() {
        return transactionProcessingAmount;
    }

    public void setTransactionProcessingAmount(String transactionProcessingAmount) {
        this.transactionProcessingAmount = transactionProcessingAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
