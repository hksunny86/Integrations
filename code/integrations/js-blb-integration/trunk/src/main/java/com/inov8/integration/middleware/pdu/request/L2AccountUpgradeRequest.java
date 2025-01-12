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
        "PIN",
        "Cnic",
        "FingerIndex",
        "FingerTemplate",
        "TemplateType",
        "ConsumerName",
        "FatherHusbandName",
        "Gender",
        "CnicIssuanceDate",
        "DOB",
        "BirthPlace",
        "MotherMaiden",
        "EmailAddress",
        "MailingAddress",
        "PermanentAddress",
        "PurposeOfAccount",
        "SourceOfIncome",
        "SourceOfIncomePic",
        "ExpectedMonthlyTurnover",
        "NextOfKin",
        "CnicFrontPic",
        "CnicBackPic",
        "CustomerPic",
        "Latitude",
        "Longitude",
        "CurrencyCode",
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
        "Pmd",
        "Kyc",
        "PinValidation",
        "ForNadra",
        "ProofOfBusiness",
        "DualNationality",
        "UsCitizenship",
        "ChequeBook",
        "RequestType",
        "AccountCurrency",
        "SpendingAmount",
        "StreetNumber",
        "AddressArea",
        "ZindigiUltraPurpose",
        "CountryOfBirth",
        "CountryTaxResidence",
        "BornInUS",
        "Declaration",
        "SelectCountry",
        "BirthCountry",
        "ResidenceAddress",
        "TaxResidence",
        "ReferenceNumber",
        "USBornCity",
        "SalarySilp",
        "Reserved8",
        "Reserved9",
        "Reserved10",
        "Reserved11",
        "Reserved12",
        "Reserved13",
        "Reserved14",
        "Reserved15",
        "HashData"
})
public class L2AccountUpgradeRequest implements Serializable {
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
    @JsonProperty("PIN")
    private String mpin;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("FingerIndex")
    private String fingerIndex;
    @JsonProperty("FingerTemplate")
    private String fingerTemplate;
    @JsonProperty("TemplateType")
    private String templateType;
    @JsonProperty("ConsumerName")
    private String consumerName;
    @JsonProperty("FatherHusbandName")
    private String fatherHusbandName;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("CnicIssuanceDate")
    private String cnicIssuanceDate;
    @JsonProperty("DOB")
    private String dob;
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
    @JsonProperty("PurposeOfAccount")
    private String purposeOfAccount;
    @JsonProperty("SourceOfIncome")
    private String sourceOfIncome;
    @JsonProperty("SourceOfIncomePic")
    private String sourceOfIncomePic;
    @JsonProperty("ExpectedMonthlyTurnover")
    private String expectedMonthlyTurnover;
    @JsonProperty("NextOfKin")
    private String nextOfKin;
    @JsonProperty("CnicFrontPic")
    private String cnicFrontPic;
    @JsonProperty("CnicBackPic")
    private String cnicBackPic;
    @JsonProperty("CustomerPic")
    private String customerPic;
    @JsonProperty("Latitude")
    private String latitude;
    @JsonProperty("Longitude")
    private String longitude;
    @JsonProperty("CurrencyCode")
    private String currencyCode;
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
    @JsonProperty("Pmd")
    private String pmd;
    @JsonProperty("Kyc")
    private String kyc;
    @JsonProperty("PinValidation")
    private String pinValidation;
    @JsonProperty("ForNadra")
    private String forNadra;
    @JsonProperty("ProofOfBusiness")
    private String proofOfBusiness;
    @JsonProperty("DualNationality")
    private String dualNationality;
    @JsonProperty("UsCitizenship")
    private String usCitizenship;
    @JsonProperty("ChequeBook")
    private String chequeBook;
    @JsonProperty("RequestType")
    private String requestType;
    @JsonProperty("AccountCurrency")
    private String accountCurrency;
    @JsonProperty("SpendingAmount")
    private String spendingAmount;
    @JsonProperty("StreetNumber")
    private String streetNumber;
    @JsonProperty("AddressArea")
    private String addressArea;
    @JsonProperty("ZindigiUltraPurpose")
    private String zindigiUltraPurpose;
    @JsonProperty("CountryOfBirth")
    private String countryOfBirth;
    @JsonProperty("CountryTaxResidence")
    private String countryTaxResidence;
    @JsonProperty("BornInUS")
    private String bornInUS;
    @JsonProperty("Declaration")
    private String declaration;
    @JsonProperty("SelectCountry")
    private String selectCountry;
    @JsonProperty("BirthCountry")
    private String birthCountry;
    @JsonProperty("ResidenceAddress")
    private String residenceAddress;
    @JsonProperty("TaxResidence")
    private String taxResidence;
    @JsonProperty("ReferenceNumber")
    private String referenceNumber;
    @JsonProperty("USBornCity")
    private String usBornCity;
    @JsonProperty("SalarySilp")
    private String salarySilp;
    @JsonProperty("Reserved8")
    private String reserved8;
    @JsonProperty("Reserved9")
    private String reserved9;
    @JsonProperty("Reserved10")
    private String reserved10;
    @JsonProperty("Reserved11")
    private String reserved11;
    @JsonProperty("Reserved12")
    private String reserved12;
    @JsonProperty("Reserved13")
    private String reserved13;
    @JsonProperty("Reserved14")
    private String reserved14;
    @JsonProperty("Reserved15")
    private String reserved15;
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

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getFingerTemplate() {
        return fingerTemplate;
    }

    public void setFingerTemplate(String fingerTemplate) {
        this.fingerTemplate = fingerTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(String cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public String getPmd() {
        return pmd;
    }

    public void setPmd(String pmd) {
        this.pmd = pmd;
    }

    public String getKyc() {
        return kyc;
    }

    public void setKyc(String kyc) {
        this.kyc = kyc;
    }

    public String getPinValidation() {
        return pinValidation;
    }

    public void setPinValidation(String pinValidation) {
        this.pinValidation = pinValidation;
    }

    public String getForNadra() {
        return forNadra;
    }

    public void setForNadra(String forNadra) {
        this.forNadra = forNadra;
    }

    public String getProofOfBusiness() {
        return proofOfBusiness;
    }

    public void setProofOfBusiness(String proofOfBusiness) {
        this.proofOfBusiness = proofOfBusiness;
    }

    public String getDualNationality() {
        return dualNationality;
    }

    public void setDualNationality(String dualNationality) {
        this.dualNationality = dualNationality;
    }

    public String getUsCitizenship() {
        return usCitizenship;
    }

    public void setUsCitizenship(String usCitizenship) {
        this.usCitizenship = usCitizenship;
    }

    public String getChequeBook() {
        return chequeBook;
    }

    public void setChequeBook(String chequeBook) {
        this.chequeBook = chequeBook;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public String getSpendingAmount() {
        return spendingAmount;
    }

    public void setSpendingAmount(String spendingAmount) {
        this.spendingAmount = spendingAmount;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getAddressArea() {
        return addressArea;
    }

    public void setAddressArea(String addressArea) {
        this.addressArea = addressArea;
    }

    public String getZindigiUltraPurpose() {
        return zindigiUltraPurpose;
    }

    public void setZindigiUltraPurpose(String zindigiUltraPurpose) {
        this.zindigiUltraPurpose = zindigiUltraPurpose;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getCountryTaxResidence() {
        return countryTaxResidence;
    }

    public void setCountryTaxResidence(String countryTaxResidence) {
        this.countryTaxResidence = countryTaxResidence;
    }

    public String getBornInUS() {
        return bornInUS;
    }

    public void setBornInUS(String bornInUS) {
        this.bornInUS = bornInUS;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getSelectCountry() {
        return selectCountry;
    }

    public void setSelectCountry(String selectCountry) {
        this.selectCountry = selectCountry;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public String getTaxResidence() {
        return taxResidence;
    }

    public void setTaxResidence(String taxResidence) {
        this.taxResidence = taxResidence;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getUsBornCity() {
        return usBornCity;
    }

    public void setUsBornCity(String usBornCity) {
        this.usBornCity = usBornCity;
    }

    public String getSalarySilp() {
        return salarySilp;
    }

    public void setSalarySilp(String salarySilp) {
        this.salarySilp = salarySilp;
    }

    public String getReserved8() {
        return reserved8;
    }

    public void setReserved8(String reserved8) {
        this.reserved8 = reserved8;
    }

    public String getReserved9() {
        return reserved9;
    }

    public void setReserved9(String reserved9) {
        this.reserved9 = reserved9;
    }

    public String getReserved10() {
        return reserved10;
    }

    public void setReserved10(String reserved10) {
        this.reserved10 = reserved10;
    }

    public String getReserved11() {
        return reserved11;
    }

    public void setReserved11(String reserved11) {
        this.reserved11 = reserved11;
    }

    public String getReserved12() {
        return reserved12;
    }

    public void setReserved12(String reserved12) {
        this.reserved12 = reserved12;
    }

    public String getReserved13() {
        return reserved13;
    }

    public void setReserved13(String reserved13) {
        this.reserved13 = reserved13;
    }

    public String getReserved14() {
        return reserved14;
    }

    public void setReserved14(String reserved14) {
        this.reserved14 = reserved14;
    }

    public String getReserved15() {
        return reserved15;
    }

    public void setReserved15(String reserved15) {
        this.reserved15 = reserved15;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
