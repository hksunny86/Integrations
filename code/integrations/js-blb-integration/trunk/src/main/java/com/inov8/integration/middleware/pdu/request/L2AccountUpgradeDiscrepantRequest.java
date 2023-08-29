package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "UserName",
        "Password",
        "MobileNumber",
        "DateTime",
        "Rrn",
        "ChannelId",
        "TerminalId",
        "Cnic",
        "CnicFrontPic",
        "CnicBackPic",
        "CustomerPic",
        "ConsumerName",
        "FatherHusbandName",
        "PurposeOfAccount",
        "SourceOfIncome",
        "SourceOfIncomePic",
        "ExpectedMonthlyTurnover",
        "BirthPlace",
        "MotherMaiden",
        "EmailAddress",
        "MailingAddress",
        "PermanentAddress",
        "SignaturePic",
        "CurrencyCode",
        "UsCitizenship",
        "UsMobileNumber",
        "SignatoryAuthority",
        "USLinks",
        "FederalTaxClassification",
        "DualCitizenAddress",
        "TaxIdNumber",
        "ForeignTaxIdNumber",
        "UsAccountNumber",
        "UtilityBillPicture",
        "City",
        "Area",
        "HouseNumber",
        "Reserved1",
        "Reserved2",
        "Reserved3",
        "Reserved4",
        "Reserved5",
        "HashData"
})
public class L2AccountUpgradeDiscrepantRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("MobileNumber")
    private String mobileNumber;
    @JsonProperty("DateTime")
    private String dateTime;
    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ChannelId")
    private String channelId;
    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("CnicFrontPic")
    private String cnicFrontPic;
    @JsonProperty("CnicBackPic")
    private String cnicBackPic;
    @JsonProperty("CustomerPic")
    private String customerPic;
    @JsonProperty("ConsumerName")
    private String consumerName;
    @JsonProperty("FatherHusbandName")
    private String fatherHusbandName;
    @JsonProperty("PurposeOfAccount")
    private String purposeOfAccount;
    @JsonProperty("SourceOfIncome")
    private String sourceOfIncome;
    @JsonProperty("SourceOfIncomePic")
    private String sourceOfIncomePic;
    @JsonProperty("ExpectedMonthlyTurnover")
    private String expectedMonthlyTurnover;
    @JsonProperty("BirthPlace")
    private String birthPlace;
    @JsonProperty("MotherMaiden")
    private String motherMaiden;
    @JsonProperty("EmailAddress")
    private String emailAddress;
    @JsonProperty("MailingAddress")
    private String mailingAddress;
    @JsonProperty("PermanentAddress")
    private String permanentAddress;
    @JsonProperty("SignaturePic")
    private String signaturePic;
    @JsonProperty("CurrencyCode")
    private String currencyCode;
    @JsonProperty("UsCitizenship")
    private String usCitizenship;
    @JsonProperty("UsMobileNumber")
    private String usMobileNumber;
    @JsonProperty("SignatoryAuthority")
    private String signatoryAuthority;
    @JsonProperty("USLinks")
    private String usLinks;
    @JsonProperty("FederalTaxClassification")
    private String federalTaxClassification;
    @JsonProperty("DualCitizenAddress")
    private String dualCitizenAddress;
    @JsonProperty("TaxIdNumber")
    private String taxIdNumber;
    @JsonProperty("ForeignTaxIdNumber")
    private String foreignTaxIdNumber;
    @JsonProperty("UsAccountNumber")
    private String usAccountNumber;
    @JsonProperty("UtilityBillPicture")
    private String utilityBillPicture;
    @JsonProperty("City")
    private String city;
    @JsonProperty("Area")
    private String area;
    @JsonProperty("HouseNumber")
    private String houseNumber;
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

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCnicFrontPic() {
        return cnicFrontPic;
    }

    public void setCnicFrontPic(String cnicFrontPic) {
        this.cnicFrontPic = cnicFrontPic;
    }

    public String getCnicBackPic() {
        return cnicBackPic;
    }

    public void setCnicBackPic(String cnicBackPic) {
        this.cnicBackPic = cnicBackPic;
    }

    public String getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(String customerPic) {
        this.customerPic = customerPic;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    public String getPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setPurposeOfAccount(String purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getSourceOfIncomePic() {
        return sourceOfIncomePic;
    }

    public void setSourceOfIncomePic(String sourceOfIncomePic) {
        this.sourceOfIncomePic = sourceOfIncomePic;
    }

    public String getExpectedMonthlyTurnover() {
        return expectedMonthlyTurnover;
    }

    public void setExpectedMonthlyTurnover(String expectedMonthlyTurnover) {
        this.expectedMonthlyTurnover = expectedMonthlyTurnover;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getMotherMaiden() {
        return motherMaiden;
    }

    public void setMotherMaiden(String motherMaiden) {
        this.motherMaiden = motherMaiden;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getSignaturePic() {
        return signaturePic;
    }

    public void setSignaturePic(String signaturePic) {
        this.signaturePic = signaturePic;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getUsCitizenship() {
        return usCitizenship;
    }

    public void setUsCitizenship(String usCitizenship) {
        this.usCitizenship = usCitizenship;
    }

    public String getUsMobileNumber() {
        return usMobileNumber;
    }

    public void setUsMobileNumber(String usMobileNumber) {
        this.usMobileNumber = usMobileNumber;
    }

    public String getSignatoryAuthority() {
        return signatoryAuthority;
    }

    public void setSignatoryAuthority(String signatoryAuthority) {
        this.signatoryAuthority = signatoryAuthority;
    }

    public String getUsLinks() {
        return usLinks;
    }

    public void setUsLinks(String usLinks) {
        this.usLinks = usLinks;
    }

    public String getFederalTaxClassification() {
        return federalTaxClassification;
    }

    public void setFederalTaxClassification(String federalTaxClassification) {
        this.federalTaxClassification = federalTaxClassification;
    }

    public String getDualCitizenAddress() {
        return dualCitizenAddress;
    }

    public void setDualCitizenAddress(String dualCitizenAddress) {
        this.dualCitizenAddress = dualCitizenAddress;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public String getForeignTaxIdNumber() {
        return foreignTaxIdNumber;
    }

    public void setForeignTaxIdNumber(String foreignTaxIdNumber) {
        this.foreignTaxIdNumber = foreignTaxIdNumber;
    }

    public String getUsAccountNumber() {
        return usAccountNumber;
    }

    public void setUsAccountNumber(String usAccountNumber) {
        this.usAccountNumber = usAccountNumber;
    }

    public String getUtilityBillPicture() {
        return utilityBillPicture;
    }

    public void setUtilityBillPicture(String utilityBillPicture) {
        this.utilityBillPicture = utilityBillPicture;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
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