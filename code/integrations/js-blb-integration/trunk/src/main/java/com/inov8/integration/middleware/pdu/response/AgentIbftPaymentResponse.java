package com.inov8.integration.middleware.pdu.response;


import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentIbftPaymentResponse")
public class AgentIbftPaymentResponse implements Serializable {

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
    @XmlElement(name = "BeneficiaryBankName")
    private String BeneficiaryBankName;
    @XmlElement(name = "BeneficiaryBranchName")
    private String beneficiaryiciaryBranchName;
    @XmlElement(name = "BeneficiaryIBAN")
    private String beneficiaryIban;
    @XmlElement(name = "CoreAccountTitle")
    private String coreAccountTitle;
    @XmlElement(name = "CoreAccountNumber")
    private String coreAccountNumber;
    @XmlElement(name = "TransactionId")
    private String transactionId;
    @XmlElement(name = "TransactionAmount")
    private String transactionAmount;
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

    public String getAgentMobileNumber() {
        return agentMobileNumber;
    }

    public void setAgentMobileNumber(String agentMobileNumber) {
        this.agentMobileNumber = agentMobileNumber;
    }

    public String getBeneficiaryBankName() {
        return BeneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        BeneficiaryBankName = beneficiaryBankName;
    }

    public String getBeneficiaryiciaryBranchName() {
        return beneficiaryiciaryBranchName;
    }

    public void setBeneficiaryiciaryBranchName(String beneficiaryiciaryBranchName) {
        this.beneficiaryiciaryBranchName = beneficiaryiciaryBranchName;
    }

    public String getBeneficiaryIban() {
        return beneficiaryIban;
    }

    public void setBeneficiaryIban(String beneficiaryIban) {
        this.beneficiaryIban = beneficiaryIban;
    }

    public String getCoreAccountTitle() {
        return coreAccountTitle;
    }

    public void setCoreAccountTitle(String coreAccountTitle) {
        this.coreAccountTitle = coreAccountTitle;
    }

    public String getCoreAccountNumber() {
        return coreAccountNumber;
    }

    public void setCoreAccountNumber(String coreAccountNumber) {
        this.coreAccountNumber = coreAccountNumber;
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
