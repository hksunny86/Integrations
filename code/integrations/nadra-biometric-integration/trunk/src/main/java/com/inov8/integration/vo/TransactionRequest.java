package com.inov8.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by inov8 on 10/4/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest implements Serializable {
    private static final long serialVersionUID = -3145395115894566903L;
    private String transactionType;
    private String franchiseeID;
    private String username;
    private String sessionId;
    private String citizenNumber;
    private String contactNumber;
    private String secondaryCitizenNumber;
    private String secondaryContactNumber;
    private String fingerIndex;
    private String fingerTemplate;
    private String templateType;
    private String transactionId;
    private String areaName;
    private String verificationResult;
    private String mobileBankAccountNumber;
    private String accountLevel;
    private String remittanceAmount;
    private String remittanceType;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getFranchiseeID() {
        return franchiseeID;
    }

    public void setFranchiseeID(String franchiseeID) {
        this.franchiseeID = franchiseeID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(String citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getFingerTemplate() {
        return fingerTemplate;
    }

    public void setFingerTemplate(String fingerTemplate) {
        this.fingerTemplate = fingerTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }

    public String getMobileBankAccountNumber() {
        return mobileBankAccountNumber;
    }

    public void setMobileBankAccountNumber(String mobileBankAccountNumber) {
        this.mobileBankAccountNumber = mobileBankAccountNumber;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getRemittanceAmount() {
        return remittanceAmount;
    }

    public void setRemittanceAmount(String remittanceAmount) {
        this.remittanceAmount = remittanceAmount;
    }

    public String getRemittanceType() {
        return remittanceType;
    }

    public void setRemittanceType(String remittanceType) {
        this.remittanceType = remittanceType;
    }

    public String getSecondaryCitizenNumber() {
        return secondaryCitizenNumber;
    }

    public void setSecondaryCitizenNumber(String secondaryCitizenNumber) {
        this.secondaryCitizenNumber = secondaryCitizenNumber;
    }

    public String getSecondaryContactNumber() {
        return secondaryContactNumber;
    }

    public void setSecondaryContactNumber(String secondaryContactNumber) {
        this.secondaryContactNumber = secondaryContactNumber;
    }
}
