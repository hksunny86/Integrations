package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CashInInquiryRequest")
public class CashInInquiryRequest implements Serializable {


    private static final long serialVersionUID = -8474611966485764781L;

    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "TerminalId")
    private String terminalId;

    @XmlElement(name = "Amount")
    private String amount;

    @XmlElement(name = "HashData")
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
