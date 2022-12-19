package com.inov8.microbank.fonepay.common;

public class FonePayMessageVO{
    private String mobileNo;
    private String cnic;
    private boolean validateMobileCnic;
    private boolean validateWebServiceEnabled;
    private boolean validateFonePayEnabled;
    private boolean validateDiscripent;

    private String responseCode;
    private String responseCodeDescription;

    private String customerId;
    private Long appUserId;
    private String cardNo;
    private String customerStatus;
    private String firstName;
    private String lastName;

    //Account Inquiry related params
    //*** Account Inquiry - Params ***
    private String accountTitle;
    private String accountStatus;

    //*** Instant Account Opening - Params ***
//    private String mobileNo;
//    private String cnic;
    private String customerAccountyType;
    private String accountType;
    private String customerName;
    private String dob;
    private String birthPlace;
    private String cnicExpiry;
    private String motherName;
    private String fatherHusbandName;
    private String presentAddress;
    private String permanentAddress;
    private String mpin;



    public String getCustomerAccountyType() {
        return customerAccountyType;
    }

    public void setCustomerAccountyType(String customerAccountyType) {
        this.customerAccountyType = customerAccountyType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCnicExpiry() {
        return cnicExpiry;
    }

    public void setCnicExpiry(String cnicExpiry) {
        this.cnicExpiry = cnicExpiry;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getResponseCodeDescription() {
        return responseCodeDescription;
    }

    public void setResponseCodeDescription(String responseCodeDescription) {
        this.responseCodeDescription = responseCodeDescription;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isValidateMobileCnic() {
        return validateMobileCnic;
    }

    public void setValidateMobileCnic(boolean validateMobileCnic) {
        this.validateMobileCnic = validateMobileCnic;
    }

    public boolean isValidateWebServiceEnabled() {
        return validateWebServiceEnabled;
    }

    public void setValidateWebServiceEnabled(boolean validateWebServiceEnabled) {
        this.validateWebServiceEnabled = validateWebServiceEnabled;
    }

    public boolean isValidateFonePayEnabled() {
        return validateFonePayEnabled;
    }

    public void setValidateFonePayEnabled(boolean validateFonePayEnabled) {
        this.validateFonePayEnabled = validateFonePayEnabled;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
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

    public boolean isValidateDiscripent() {
        return validateDiscripent;
    }

    public void setValidateDiscripent(boolean validateDiscripent) {
        this.validateDiscripent = validateDiscripent;
    }

	public String getMpin() {
		return mpin;
	}

	public void setMpin(String mpin) {
		this.mpin = mpin;
	}

    public String getFatherHusbandName() { return fatherHusbandName; }

    public void setFatherHusbandName(String fatherHusbandName) { this.fatherHusbandName = fatherHusbandName; }

}
