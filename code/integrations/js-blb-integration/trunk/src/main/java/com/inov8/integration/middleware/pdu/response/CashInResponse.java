package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CashInResponse")
public class CashInResponse implements Serializable {


    private static final long serialVersionUID = -1156481809344525232L;

    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "Rrn")
    private String rrn;

    @XmlElement(name = "CustomerMobile")
    private String customerMobile;
    @XmlElement(name = "Cnic")
    private String cnic;
    @XmlElement(name = "TransactionDateTime")
    private String transactionDateTime;
    @XmlElement(name = "TransactionProcessingCharges")
    private String transactionProcessingCharges;
    @XmlElement(name = "CommissionAmount")
    private String commissionAmount;
    @XmlElement(name = "TotalAmount")
    private String totalAmount;
    @XmlElement(name = "TransactionId")
    private String transactionId;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
    @XmlElement(name = "RemainingBalance")
    private String remainingBalance;

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

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTransactionProcessingCharges() {
        return transactionProcessingCharges;
    }

    public void setTransactionProcessingCharges(String transactionProcessingCharges) {
        this.transactionProcessingCharges = transactionProcessingCharges;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

}
