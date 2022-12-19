package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AgentIbftInquiryResponse")
public class AgentIbftInquiryResponse implements Serializable {

    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "ResponseDateTime")
    private String responseDateTime;
    @XmlElement(name = "CoreAccountId")
    private String coreAccountId;
    @XmlElement(name = "CoreAccountTitle")
    private String coreAccountTitle;
    @XmlElement(name = "BankAccountId")
    private String bankAccountId;
    @XmlElement(name = "BeneficiaryBankName")
    private String beneficiaryBankName;
    @XmlElement(name = "BeneficiaryBranchName")
    private String beneficiaryBranchName;
    @XmlElement(name = "BeneficiaryIBAN")
    private String beneficiaryIban;
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

    public String getCoreAccountId() {
        return coreAccountId;
    }

    public void setCoreAccountId(String coreAccountId) {
        this.coreAccountId = coreAccountId;
    }

    public String getCoreAccountTitle() {
        return coreAccountTitle;
    }

    public void setCoreAccountTitle(String coreAccountTitle) {
        this.coreAccountTitle = coreAccountTitle;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    public String getBeneficiaryBranchName() {
        return beneficiaryBranchName;
    }

    public void setBeneficiaryBranchName(String beneficiaryBranchName) {
        this.beneficiaryBranchName = beneficiaryBranchName;
    }

    public String getBeneficiaryIban() {
        return beneficiaryIban;
    }

    public void setBeneficiaryIban(String beneficiaryIban) {
        this.beneficiaryIban = beneficiaryIban;
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
