package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AccountStatusChangeRequest")
public class AccountStatusChangeRequest implements Serializable {

    private static final long serialVersionUID = 6848934910669383177L;

    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "Cnic")
    private String cnic;
    @XmlElement(name = "AccountStatus")
    private String accountStatus;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "Mpin")
    private String mpin;
    @XmlElement(name = "TerminalId")
    private String terminalId;
    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "Reserved1")
    private String reserved1;
    @XmlElement(name = "Reserved2")
    private String reserved2;
    @XmlElement(name = "Reserved3")
    private String reserved3;
    @XmlElement(name = "Reserved4")
    private String reserved4;
    @XmlElement(name = "Reserved5")
    private String reserved5;
    @XmlElement(name = "HashData")
    private String hashData;


    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
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

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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
}
