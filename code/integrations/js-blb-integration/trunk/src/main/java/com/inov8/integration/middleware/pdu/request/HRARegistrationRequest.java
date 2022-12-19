package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HRARegistrationRequest")
public class HRARegistrationRequest implements Serializable {

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
    @XmlElement(name = "PIN")
    String pin;
    @XmlElement(name = "PinType")
    String pinType;
    @XmlElement(name = "Name")
    String name;
    @XmlElement(name = "FatherName")
    String fatherName;
    @XmlElement(name = "DateOfBirth")
    String dateOfBirth;
    @XmlElement(name = "CNIC")
    private String cnic;
    @XmlElement(name = "SourceOfIncome")
    private String sourceOfIncome;
    @XmlElement(name = "Occupation")
    private String occupation;
    @XmlElement(name = "PurposeOfAccount")
    private String purposeOfAccount;
    @XmlElement(name = "KINName")
    private String kinName;
    @XmlElement(name = "KINMobileNumber")
    private String kinMobileNumber;
    @XmlElement(name = "KINCNIC")
    private String kinCnic;
    @XmlElement(name = "KINRelation")
    private String kinRelation;
    @XmlElement(name = "InternationalRemittanceLocation")
    private String internationalRemittanceLocation;
    @XmlElement(name = "OriginatorRelation")
    private String originatorRelation;
    @XmlElement(name = "Reserved1")
    private String reserved1;
    @XmlElement(name = "Reserved2")
    private String reserved2;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPinType() {
        return pinType;
    }

    public void setPinType(String pinType) {
        this.pinType = pinType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setPurposeOfAccount(String purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    public String getKinName() {
        return kinName;
    }

    public void setKinName(String kinName) {
        this.kinName = kinName;
    }

    public String getKinMobileNumber() {
        return kinMobileNumber;
    }

    public void setKinMobileNumber(String kinMobileNumber) {
        this.kinMobileNumber = kinMobileNumber;
    }

    public String getKinCnic() {
        return kinCnic;
    }

    public void setKinCnic(String kinCnic) {
        this.kinCnic = kinCnic;
    }

    public String getKinRelation() {
        return kinRelation;
    }

    public void setKinRelation(String kinRelation) {
        this.kinRelation = kinRelation;
    }

    public String getInternationalRemittanceLocation() {
        return internationalRemittanceLocation;
    }

    public void setInternationalRemittanceLocation(String internationalRemittanceLocation) {
        this.internationalRemittanceLocation = internationalRemittanceLocation;
    }

    public String getOriginatorRelation() {
        return originatorRelation;
    }

    public void setOriginatorRelation(String originatorRelation) {
        this.originatorRelation = originatorRelation;
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

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
