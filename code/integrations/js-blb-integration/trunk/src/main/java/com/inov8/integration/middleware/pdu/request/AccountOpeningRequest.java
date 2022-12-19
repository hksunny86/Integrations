package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AccountOpeningRequest")
public class AccountOpeningRequest {
    private final static long serialVersionUID = 1L;
    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "Cnic")
    private String cnic;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ConsumerName")
    private String consumerName;
    @XmlElement(name = "AccountTitle")
    private String accountTitle;
    @XmlElement(name = "BirthPlace")
    private String birthPlace;
    @XmlElement(name = "PresentAddress")
    private String presentAddress;
    @XmlElement(name = "CnicStatus")
    private String cnicStatus;
    @XmlElement(name = "CnicExpiry")
    private String cnicExpiry;
    @XmlElement(name = "DOB")
    private String dob;
    @XmlElement(name = "FatherHusbandName")
    private String fatherHusbandName;
    @XmlElement(name = "MotherMaiden")
    private String motherMaiden;
    @XmlElement(name = "Gender")
    private String gender;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "AccountType")
    private String accountType;
    @XmlElement(name = "TrackingId")
    private String trackingId;
    @XmlElement(name = "Cnic_Issuance_Date")
    private String cnicIssuanceDate;
    @XmlElement(name = "email_Address")
    private String emailAddress;
    @XmlElement(name="Mobile_Network")
    private String mobileNetwork;
    @XmlElement(name = "Reserved")
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

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
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

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getCnicStatus() {
        return cnicStatus;
    }

    public void setCnicStatus(String cnicStatus) {
        this.cnicStatus = cnicStatus;
    }

    public String getCnicExpiry() {
        return cnicExpiry;
    }

    public void setCnicExpiry(String cnicExpiry) {
        this.cnicExpiry = cnicExpiry;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    public String getMotherMaiden() {
        return motherMaiden;
    }

    public void setMotherMaiden(String motherMaiden) {
        this.motherMaiden = motherMaiden;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(String cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNetwork() {
        return mobileNetwork;
    }

    public void setMobileNetwork(String mobileNetwork) {
        this.mobileNetwork = mobileNetwork;
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
