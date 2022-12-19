package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HRACashWithdrawalResponse")
public class HRACashWithdrawalResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "AgentMobileNumber")
    private String agentMobileNumber;
    @XmlElement(name = "CustomerName")
    private String customerName;
    @XmlElement(name = "CustomerMobileNumber")
    private String customerMobileNumber;
    @XmlElement(name = "CNIC")
    private String cnic;
    @XmlElement(name = "TransactionId")
    private String transactionId;
    @XmlElement(name = "CommissionAmount")
    private String comissionAmount;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
    @XmlElement(name = "TotalTransactionAmount")
    private String totalTransactionAmount;
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

    public String getAgentMobileNumber() {
        return agentMobileNumber;
    }

    public void setAgentMobileNumber(String agentMobileNumber) {
        this.agentMobileNumber = agentMobileNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getComissionAmount() {
        return comissionAmount;
    }

    public void setComissionAmount(String comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(String totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
