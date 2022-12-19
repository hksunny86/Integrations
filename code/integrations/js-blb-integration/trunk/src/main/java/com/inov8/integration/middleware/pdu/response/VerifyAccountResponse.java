package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "VerifyAccountResponse")
public class VerifyAccountResponse {
    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Rrn")
    private String rrn;

    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "AccountTitle")
    private String accountTitle;
    @XmlElement(name = "firstName")
    private String firstName;
    @XmlElement(name = "LastName")
    private String lastName;
    @XmlElement(name = "AccountType")
    private String accountType;
    @XmlElement(name = "CnicExpiry")
    private String cnicExpiry;
    @XmlElement(name = "DateOfBirth")
    private String dateOfBirth;
    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "Cnic")
    private String cnic;
    @XmlElement(name = "AccountStatus")
    private String accountStatus;

    @XmlElement(name = "IsPinSet")
    private String isPinSet;
    @XmlElement(name = "HashData")
    private String hashData;


    public String getIsPinSet() {
        return isPinSet;
    }

    public void setIsPinSet(String isPinSet) {
        this.isPinSet = isPinSet;
    }

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

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
}
