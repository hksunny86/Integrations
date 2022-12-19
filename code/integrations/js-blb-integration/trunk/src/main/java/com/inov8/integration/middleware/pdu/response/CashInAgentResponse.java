package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;

/**
 * Created by Inov8 on 8/28/2019.
 */
@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CashInAgentResponse")
public class CashInAgentResponse {
    private static final long serialVersionUID = -1156481809344525232L;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "RRN")
    private String rrn;
    @XmlElement(name = "AgentMobileNumber")
    private String agentMobileNumber;
    @XmlElement(name = "CNIC")
    private String cnic;
    @XmlElement(name = "TransactionDateTime")
    private String transactionDateTime;
    @XmlElement(name = "TransactionProcessingAmount")
    private String transactionProcessingAmount;
    @XmlElement(name = "CommissionAmount")
    private String commisionAmount;
    @XmlElement(name = "TotalAmount")
    private String totalAmount;
    @XmlElement(name = "TransactionID")
    private String transactionId;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
    @XmlElement(name = "RemainingBalance")
    private String remainingBalance;
    @XmlElement(name = "Data_Hash")
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

    public String getAgentMobileNumber() {
        return agentMobileNumber;
    }

    public void setAgentMobileNumber(String agentMobileNumber) {
        this.agentMobileNumber = agentMobileNumber;
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

    public String getTransactionProcessingAmount() {
        return transactionProcessingAmount;
    }

    public void setTransactionProcessingAmount(String transactionProcessingAmount) {
        this.transactionProcessingAmount = transactionProcessingAmount;
    }

    public String getCommisionAmount() {
        return commisionAmount;
    }

    public void setCommisionAmount(String commisionAmount) {
        this.commisionAmount = commisionAmount;
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

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
