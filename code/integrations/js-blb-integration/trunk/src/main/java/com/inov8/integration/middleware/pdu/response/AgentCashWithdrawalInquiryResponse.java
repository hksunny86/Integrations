package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;


@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentCashWithdrawalInquiryResponse")
public class AgentCashWithdrawalInquiryResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "CustomerMobileNumber")
    private String customerMobileNumber;
    @XmlElement(name = "AgentMobileNumber")
    private String agentMobileNumber;
    @XmlElement(name = "CustomerName")
    private String customerName;
    @XmlElement(name = "CustomerCnic")
    private String customerCnic;
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

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
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

    public String getCustomerCnic() {
        return customerCnic;
    }

    public void setCustomerCnic(String customerCnic) {
        this.customerCnic = customerCnic;
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
