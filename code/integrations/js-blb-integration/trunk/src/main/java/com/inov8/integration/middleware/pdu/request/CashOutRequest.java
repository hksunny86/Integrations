package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;

/**
 * Created by Inov8 on 8/29/2019.
 */
@XmlRootElement(name = "CashOutRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class CashOutRequest {
    private static final long serialVersionUID = -1121652379255326975L;
    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "CustomerMobileNumber")
    private String mobileNumber;
    @XmlElement(name = "TransactionDateTime")
    private String transactiondateTime;
    @XmlElement(name = "RRN")
    private String rrn;
    @XmlElement(name = "ChannelID")
    private String channelId;
    @XmlElement(name = "TerminalID")
    private String terminalID;
    @XmlElement(name = "Amount")
    private String amount;
    @XmlElement(name = "Cahrges")
    private String charges;
    @XmlElement(name = "Mpin")
    private String mpin;
    @XmlElement(name = "OTP")
    private String otp;
    @XmlElement(name = "PaymentType")
    private String paymentType;
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
    @XmlElement(name = "Data_Hash")
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



    public String getTransactiondateTime() {
        return transactiondateTime;
    }

    public void setTransactiondateTime(String transactiondateTime) {
        this.transactiondateTime = transactiondateTime;
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

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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
