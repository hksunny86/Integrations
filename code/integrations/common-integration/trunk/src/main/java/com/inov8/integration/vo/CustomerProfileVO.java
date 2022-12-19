package com.inov8.integration.vo;

import java.io.Serializable;

public class CustomerProfileVO implements Serializable{

    private static final long serialVersionUID = -5140312955659912548L;

    private String cnic;
    private String newCnic;
    private String customerId;

    private String title;
    private String fullName;
    private String dateOfBirth;
    private String motherMaidenName;
    private String address;
    private String postalCode;
    private String telephoneNumber;
    private String mobileNumber;
    private String email;
    private String company;
    private String companyAddress;
    private String companyPostalCode;
    private String companyTelephone;
    private String anniversaryDate;
    private String correspondenceFlag;
    private String IVRChannelStatus;
    private String mobileChannelStatus;
    private String callCenterChannelStatus;
    private String internetBankingChannelStatus;

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getNewCnic() {
        return newCnic;
    }

    public void setNewCnic(String newCnic) {
        this.newCnic = newCnic;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPostalCode() {
        return companyPostalCode;
    }

    public void setCompanyPostalCode(String companyPostalCode) {
        this.companyPostalCode = companyPostalCode;
    }

    public String getCompanyTelephone() {
        return companyTelephone;
    }

    public void setCompanyTelephone(String companyTelephone) {
        this.companyTelephone = companyTelephone;
    }

    public String getAnniversaryDate() {
        return anniversaryDate;
    }

    public void setAnniversaryDate(String anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
    }

    public String getCorrespondenceFlag() {
        return correspondenceFlag;
    }

    public void setCorrespondenceFlag(String correspondenceFlag) {
        this.correspondenceFlag = correspondenceFlag;
    }

    public String getIVRChannelStatus() {
        return IVRChannelStatus;
    }

    public void setIVRChannelStatus(String IVRChannelStatus) {
        this.IVRChannelStatus = IVRChannelStatus;
    }

    public String getMobileChannelStatus() {
        return mobileChannelStatus;
    }

    public void setMobileChannelStatus(String mobileChannelStatus) {
        this.mobileChannelStatus = mobileChannelStatus;
    }

    public String getCallCenterChannelStatus() {
        return callCenterChannelStatus;
    }

    public void setCallCenterChannelStatus(String callCenterChannelStatus) {
        this.callCenterChannelStatus = callCenterChannelStatus;
    }

    public String getInternetBankingChannelStatus() {
        return internetBankingChannelStatus;
    }

    public void setInternetBankingChannelStatus(String internetBankingChannelStatus) {
        this.internetBankingChannelStatus = internetBankingChannelStatus;
    }
}
