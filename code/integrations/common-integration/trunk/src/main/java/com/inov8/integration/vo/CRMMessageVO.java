package com.inov8.integration.vo;

import java.io.Serializable;

public class CRMMessageVO implements Serializable {
    private static final long serialVersionUID = -7443862375180198849L;
    public String userName;
    public String password;
    public String msisdn;
    private String retrivalReferenceNumber;
    private String regionID;
    private String regionType;
    private String bvsStatus;
    private String subscriberType;
    private String name;
    private String cnic;
    private String address;
    private String simType;
    private String ActionTime;
    private String responseCode;
    private String responseDescription;

    public CRMMessageVO() {
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

    public String getMsisdn() {
        return this.msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRetrivalReferenceNumber() {
        return this.retrivalReferenceNumber;
    }

    public void setRetrivalReferenceNumber(String retrivalReferenceNumber) {
        this.retrivalReferenceNumber = retrivalReferenceNumber;
    }

    public String getRegionID() {
        return this.regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getRegionType() {
        return this.regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public String getBvsStatus() {
        return this.bvsStatus;
    }

    public void setBvsStatus(String bvsStatus) {
        this.bvsStatus = bvsStatus;
    }

    public String getSubscriberType() {
        return this.subscriberType;
    }

    public void setSubscriberType(String subscriberType) {
        this.subscriberType = subscriberType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return this.cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSimType() {
        return this.simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
    }

    public String getActionTime() {
        return this.ActionTime;
    }

    public void setActionTime(String actionTime) {
        this.ActionTime = actionTime;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return this.responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
}

