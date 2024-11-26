package com.inov8.integration.lending.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DebitBlockRequest implements Serializable {
    private final static long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String mobileNumber;
    private String dateTime;
    private String rrn;
    private String channelId;
    private String terminalId;
    private String lienMark;
    private String lienAmount;
    private String hashData;

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getLienMark() {
        return lienMark;
    }

    public void setLienMark(String lienMark) {
        this.lienMark = lienMark;
    }

    public String getLienAmount() {
        return lienAmount;
    }

    public void setLienAmount(String lienAmount) {
        this.lienAmount = lienAmount;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}