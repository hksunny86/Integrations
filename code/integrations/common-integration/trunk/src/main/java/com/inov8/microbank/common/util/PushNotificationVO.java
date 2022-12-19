package com.inov8.microbank.common.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Hamza on 3/16/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushNotificationVO {
    private String deviceKey;
    private String message;
    private List<String> deviceKeyList;
    private String notificationTypeId;
    private String cpId;
    private String userType;
    private String responseCode;
    private String responseDescription;
    private String osType;

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(String notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public List<String> getDeviceKeyList() {
        return deviceKeyList;
    }

    public void setDeviceKeyList(List<String> deviceKeyList) {
        this.deviceKeyList = deviceKeyList;
    }
}
