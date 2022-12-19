package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ConventionalAccountOpening")
public class ConventionalAccountOpening {
    private final static long serialVersionUID = 1L;
    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "Cnic")
    private String cnic;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "MobileNumber")
    private String mobileNumber;
    @XmlElement(name = "ConsumerName")
    private String consumerName;
    @XmlElement(name = "CnicExpiry")
    private String cnicExpiry;
    @XmlElement(name = "DOB")
    private String dob;
    @XmlElement(name = "CustomerPhoto")
    private String customerPhoto;
    @XmlElement(name = "CnicFrontPhoto")
    private String cnicFrontPhoto;
    @XmlElement(name = "CnicBackPhoto")
    private String cnicBackPhoto;
    @XmlElement(name = "SignaturePhoto")
    private String signaturePhoto;
    @XmlElement(name = "TermsPhoto")
    private String termsPhoto;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "AccountType")
    private String accountType;

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

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
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

    public String getCustomerPhoto() {
        return customerPhoto;
    }

    public void setCustomerPhoto(String customerPhoto) {
        this.customerPhoto = customerPhoto;
    }

    public String getCnicFrontPhoto() {
        return cnicFrontPhoto;
    }

    public void setCnicFrontPhoto(String cnicFrontPhoto) {
        this.cnicFrontPhoto = cnicFrontPhoto;
    }

    public String getCnicBackPhoto() {
        return cnicBackPhoto;
    }

    public void setCnicBackPhoto(String cnicBackPhoto) {
        this.cnicBackPhoto = cnicBackPhoto;
    }

    public String getSignaturePhoto() {
        return signaturePhoto;
    }

    public void setSignaturePhoto(String signaturePhoto) {
        this.signaturePhoto = signaturePhoto;
    }

    public String getTermsPhoto() {
        return termsPhoto;
    }

    public void setTermsPhoto(String termsPhoto) {
        this.termsPhoto = termsPhoto;
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

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }


}
