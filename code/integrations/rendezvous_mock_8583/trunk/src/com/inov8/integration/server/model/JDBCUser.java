package com.inov8.integration.server.model;

/**
 * Created by ZeeshanAh1 on 12/21/2015.
 */
public class JDBCUser {

    private String username;
    private String CNIC;
    private String DOB;
    private String firstName;
    private String lastName;
    private String gender;
    private String PAN;
    private String firstTimeLogin;
    private String email;
    private String mobilePin;
    private String secretQuestionId;
    private String secretAnswer;
    private String responseCode;
    private long ttlRequest;
    private String channelStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getSecretQuestionId() {
        return secretQuestionId;
    }

    public void setSecretQuestionId(String secretQuestionId) {
        this.secretQuestionId = secretQuestionId;
    }

    public long getTtlRequest() {
        return ttlRequest;
    }

    public void setTtlRequest(long ttlRequest) {
        this.ttlRequest = ttlRequest;
    }

    public String getMobilePin() {
        return mobilePin;
    }

    public void setMobilePin(String mobilePin) {
        this.mobilePin = mobilePin;
    }

    public String getFirstTimeLogin() {
        return firstTimeLogin;
    }

    public void setFirstTimeLogin(String firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }
}
