package com.inov8.integration.corporate.pdu.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "UserName",
        "Password",
        "MobileNo",
        "DateTime",
        "Rrn",
        "ChannelId",
        "TerminalId",
        "OldMpin",
        "NewMpin",
        "ConfirmMpin",
        "PortalId",
        "PortalPassword",
        "OTP",
        "Reserved1",
        "Reserved2",
        "Reserved3",
        "Reserved4",
        "Reserved5",
        "HashData"
})
public class MpinResetRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("MobileNo")
    private String mobileNo;
    @JsonProperty("DateTime")
    private String dateTime;
    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ChannelId")
    private String channelId;
    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("OldMpin")
    private String oldMpin;
    @JsonProperty("NewMpin")
    private String newMpin;
    @JsonProperty("ConfirmMpin")
    private String confirmMpin;
    @JsonProperty("PortalId")
    private String portalId;
    @JsonProperty("PortalPassword")
    private String portalPassword;
    @JsonProperty("OTP")
    private String otp;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("Reserved3")
    private String reserved3;
    @JsonProperty("Reserved4")
    private String reserved4;
    @JsonProperty("Reserved5")
    private String reserved5;
    @JsonProperty("HashData")
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
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

    public String getOldMpin() {
        return oldMpin;
    }

    public void setOldMpin(String oldMpin) {
        this.oldMpin = oldMpin;
    }

    public String getNewMpin() {
        return newMpin;
    }

    public void setNewMpin(String newMpin) {
        this.newMpin = newMpin;
    }

    public String getConfirmMpin() {
        return confirmMpin;
    }

    public void setConfirmMpin(String confirmMpin) {
        this.confirmMpin = confirmMpin;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public String getPortalPassword() {
        return portalPassword;
    }

    public void setPortalPassword(String portalPassword) {
        this.portalPassword = portalPassword;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
