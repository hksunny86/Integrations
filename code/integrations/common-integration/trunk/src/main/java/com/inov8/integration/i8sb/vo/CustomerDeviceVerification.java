package com.inov8.integration.i8sb.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CustomerDeviceVerification implements Serializable {
    @JsonProperty("ID")
    public String id;
    @JsonProperty("MobileNo")
    public String mobileNo;
    @JsonProperty("UniqueIdentifier")
    public String uniqueIdentifier;
    @JsonProperty("DeviceName")
    public String deviceName;
    @JsonProperty("RequestType")
    public String requestType;
    @JsonProperty("ApprovalStatus")
    public String approvalStatus;
    @JsonProperty("Remarks")
    public String remarks;
    @JsonProperty("RequestedDate")
    public String requestedDate;
    @JsonProperty("RequestedTime")
    public String requestedTime;


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

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
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
}
