package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IbftAdviceRequest")
public class IbftAdviceRequest implements Serializable {

    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "TerminalId")
    private String terminalId;
    @XmlElement(name = "SenderMobileNumber")
    private String senderMobileNumber;
    @XmlElement(name = "RecieverMobileNumber")
    private String recieverMobileNumber;
    @XmlElement(name = "SourceBankIMD")
    private String sourceBankImd;
    @XmlElement(name = "DestinstionBankIMD")
    private String destinationBankIMD;
    @XmlElement(name = "DestinationAccount")
    private String destinationAccount;
    @XmlElement(name = "Amount")
    private String amount;
    @XmlElement(name = "Otp")
    private String mPIN;
    @XmlElement(name = "MPIN")
    private String otpPin;
    @XmlElement(name = "PurposeOfPayment")
    private String purposeOfPayment;
    @XmlElement(name = "senderAcountTitle")
    private String senderAccountTitle;
    @XmlElement(name = "RecieverAccountTitle")
    private String recieverAccountTitle;
    @XmlElement(name = "ToBankName")
    private String toBankName;
    @XmlElement(name = "ToBranchName")
    private String toBranchName;
    @XmlElement(name = "BenificieryIBAN")
    private String benificieryIban;
    @XmlElement(name = "Reserved1")
    private String reserved;
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

    public String getOtpPin() {
        return otpPin;
    }

    public void setOtpPin(String otpPin) {
        this.otpPin = otpPin;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getRecieverMobileNumber() {
        return recieverMobileNumber;
    }

    public void setRecieverMobileNumber(String recieverMobileNumber) {
        this.recieverMobileNumber = recieverMobileNumber;
    }

    public String getSenderAccountTitle() {
        return senderAccountTitle;
    }

    public void setSenderAccountTitle(String senderAccountTitle) {
        this.senderAccountTitle = senderAccountTitle;
    }

    public String getRecieverAccountTitle() {
        return recieverAccountTitle;
    }

    public void setRecieverAccountTitle(String recieverAccountTitle) {
        this.recieverAccountTitle = recieverAccountTitle;
    }

    public String getToBankName() {
        return toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public String getToBranchName() {
        return toBranchName;
    }

    public void setToBranchName(String toBranchName) {
        this.toBranchName = toBranchName;
    }

    public String getBenificieryIban() {
        return benificieryIban;
    }

    public void setBenificieryIban(String benificieryIban) {
        this.benificieryIban = benificieryIban;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getSenderMobileNumber() {
        return senderMobileNumber;
    }

    public void setSenderMobileNumber(String senderMobileNumber) {
        this.senderMobileNumber = senderMobileNumber;
    }

    public String getSourceBankImd() {
        return sourceBankImd;
    }

    public void setSourceBankImd(String sourceBankImd) {
        this.sourceBankImd = sourceBankImd;
    }


    public String getDestinationBankIMD() {
        return destinationBankIMD;
    }

    public void setDestinationBankIMD(String destinationBankIMD) {
        this.destinationBankIMD = destinationBankIMD;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getmPIN() {
        return mPIN;
    }

    public void setmPIN(String mPIN) {
        this.mPIN = mPIN;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
