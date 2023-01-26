package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

public class CustomerDeviceVerificationModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public String id;
    public String mobileNo;
    public String unquieIdentifier;
    public String deviceName;
    public String requestType;
    public String approvalStatus;
    public String remarks;
    public String  requestedDate;
    public String requestedTime;
    public String cnic;
    public String fatherHusbandName;
    public Date dob;
    public Date cnicIssuanceDate;
    public Date cnicExpiryDate;
    public String address;
    private String fullName;
    private Long appUserId;

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUnquieIdentifier() {
        return unquieIdentifier;
    }

    public void setUnquieIdentifier(String unquieIdentifier) {
        this.unquieIdentifier = unquieIdentifier;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(Date cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }

    public Date getCnicExpiryDate() {
        return cnicExpiryDate;
    }

    public void setCnicExpiryDate(Date cnicExpiryDate) {
        this.cnicExpiryDate = cnicExpiryDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
