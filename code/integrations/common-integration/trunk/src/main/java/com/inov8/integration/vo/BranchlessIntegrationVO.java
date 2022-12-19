package com.inov8.integration.vo;


import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

import java.io.Serializable;
import java.util.List;

public class BranchlessIntegrationVO implements Serializable{

    private static final long serialVersionUID = 2000475637838176060L;

    private String userID;
    private String userName;
    private String password;
    private String accountNo1;
    private String accountTitle;
    private String accountBalance;
    private String transactionAmount;
    private String transactionType;
    private String retrievalReferenceNumber;
    private String originalRetrievalReferenceNumber;
    private String transactionCode;
    private String channelID;
    private String sellerCode;
    private String cnic;
    private String projectCode;
    private String terminalID;
    private String mobileNumber;
    private String bankIMD;
    private String reserved;
    private String dataHash;
    private String cardNo;
    private String agentCommission;
    private String totalAmount;

    private String sessionID;
    private String otpPin;
    private String agentAccountNumber;

    private String dateTime;
    private String date;
    private String dateOfBirth;
    private String challanDate;

    private String longitude;
    private String latitude;

    private String cnicExpiry;

    private String customerName;
    private String motherMaidenName;
    private String placeOfBirth;
    private String mailingAddress;
    private String businessAddress;
    private String bvsVerified;
    private String fatherName;
    private String districtCode;


    private String beneficiaryName;
    private List<AccountInfoVO> accountsLIst;
    private String consumerNumber;
    private String charges;
    private String departmentCode;
    private String departmentName;
    private String fingerIndex;
    private String fingerTemplate;
    private String templateType;
    private String areaName;

    private String responseCode;
    private String responseDescription;
    private String bvsRequired;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getOriginalRetrievalReferenceNumber() {
        return originalRetrievalReferenceNumber;
    }

    public void setOriginalRetrievalReferenceNumber(String originalRetrievalReferenceNumber) {
        this.originalRetrievalReferenceNumber = originalRetrievalReferenceNumber;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getOtpPin() {
        return otpPin;
    }

    public void setOtpPin(String otpPin) {
        this.otpPin = otpPin;
    }

    public String getAgentAccountNumber() {
        return agentAccountNumber;
    }

    public void setAgentAccountNumber(String agentAccountNumber) {
        this.agentAccountNumber = agentAccountNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(String agentCommission) {
        this.agentCommission = agentCommission;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCnicExpiry() {
        return cnicExpiry;
    }

    public void setCnicExpiry(String cnicExpiry) {
        this.cnicExpiry = cnicExpiry;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBvsVerified() {
        return bvsVerified;
    }

    public void setBvsVerified(String bvsVerified) {
        this.bvsVerified = bvsVerified;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public List<AccountInfoVO> getAccountsLIst() {
        return accountsLIst;
    }

    public void setAccountsLIst(List<AccountInfoVO> accountsLIst) {
        this.accountsLIst = accountsLIst;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBvsRequired() {
        return bvsRequired;
    }

    public void setBvsRequired(String bvsRequired) {
        this.bvsRequired = bvsRequired;
    }
}
