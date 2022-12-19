package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

public class MfsAccountModel implements Serializable, Cloneable {


    public static final String MFS_ACCOUNT_MODEL_KEY = "mfsAccountModelKey";
    /**
     *
     */
    private static final long serialVersionUID = -2780818066040920852L;
    /**
     * Edited By Muhammad Aqeel
     */
    private String expectedMonthlyTurnOver;
    private String clsResponseCode;
    private String longitude;
    private String latitude;
    private String dualNationality;
    private String usCitizen;
    private String income;
    private Boolean cnicApp;
    private Boolean cnicRej;
    private Boolean mobApp;
    private Boolean mobRej;
    private Boolean fhApp;
    private Boolean fhRej;
    private Boolean emailApp;
    private Boolean emailRej;
    private Boolean mailingApp;
    private Boolean mailingRej;
    private Boolean turnOverApp;
    private Boolean turnOverRej;
    private Boolean popApp;
    private Boolean popRej;
    private Boolean cnicFrontApp;
    private Boolean cnicFrontRej;
    private Boolean cnicBackApp;
    private Boolean cnicBackRej;
    private Boolean sourcePicApp;
    private Boolean sourcePicRej;
    private Boolean poaApp;
    private Boolean poaRej;
    private Boolean customerPicApp;
    private Boolean customerPicRej;
    private Boolean pAddressApp;
    private Boolean pAddressRej;
    private Boolean incomeApp;
    private Boolean incomeRej;
    private Boolean riskApp;
    private Boolean riskRej;
    private Boolean signApp;
    private Boolean signReg;
    private String permanentAddress;
    private String sourceOfIncome;
    private String riskLevel;
    private String mailingAddress;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dialingCode;
    private String mobileNo;
    private Long connectionType;
    private String address1;
    private String address2;
    private String state;
    private String city;
    private String country;
    private String province;
    private Date dob;
    private Long hraRegistrationStateId;
    private String nic;
    private Long appUserId;
    private Long usecaseId;
    private Long actionId;
    private Long productCatalogId;
    private String productCatalogName;
    private String searchFirstName;
    private String searchLastName;
    private String searchMfsId;
    private String searchNic;
    //Added by Sheheryaar
    private String hraAccounttypeName;
    private String remainingHRADailyCreditLimit;
    private String remainingHRADailyDebitLimit;
    private String remainingHRAMonthlyCreditLimit;
    private String remainingHRAMonthlyDebitLimit;
    private String remainingHRAYearlyCreditLimit;
    private String remainingHRAYearlyDebitLimit;
    private String hraAccountState;
    /**
     * Edited By Usman Ashraf
     */
    private String applicationNo;
    private Date applicationDate;
    private Boolean zongRelation;
    private Boolean askariRelation;
    private String zongNo;
    private String name;
    private Boolean nameApp;
    private Boolean nameRej;
    private String fatherHusbandName;
    private String presentAddHouseNo;
    private String presentAddStreetNo;
    private Long presentAddDistrictId;
    private Long presentAddCityId;
    private Long presentAddPostalOfficeId;
    private String presentAddress;
    private String permanentAddHouseNo;
    private String permanentAddStreetNo;
    private Long permanentAddDistrictId;
    private Long permanentAddCityId;
    private Long permanentAddPostalOfficeId;
    private Long permanentCountry;
    private String permanentProvince;
    private String gender;
    private Long segmentId;
    private String email;
    private Long languageId;
    private String contactNo;
    private String landLineNo;
    private Long customerTypeId;
    private String[] fundsSourceId;
    private String fundSourceOthers;
    private String fundSourceNarration;
    private String presentDistOthers;
    private String permanentDistOthers;
    private String presentPostalOfficeOthers;
    private String permanentPostalOfficeOthers;
    private String presentCityOthers;
    private String permanentAddCityOthers;
    private Date nicExpiryDate;
    private Long customerAccountTypeId;
    private String refferedBy;
    private String presentHomeAddCityName;
    private String presentHomeAddDistrictName;
    private String presentHomeAddPostalOfficeName;
    private String permanentHomeAddCityName;
    private String permanentHomeAddDistrictName;
    private String permanentHomeAddPostalOfficeName;
    private String segmentName;
    private String languageName;
    private String typeOfCustomerName;
    private String[] fundSourceName;
    private String customerAccountName;
    private MultipartFile file;
    private String employeeID;
    private String accountNo;
    private String accountStatus;
    private Double accountBalance;
    private String customerId;
    private Boolean active;
    private String allpayId;
    private Date createdOn;
    private String createdBy;
    private Date closedOn;
    private String closedBy;
    private String closingComments;
    private Date settledOn;
    private String settledBy;
    private String settlementComments;
    private Boolean accountClosedUnsettled;
    private Boolean accountClosedSettled;
    private String location;
    private String accountNature;
    private String residanceStatus;
    private String currency;
    private String initialDeposit;
    private String agentPIN;
    private String occupation;
    private String ntn;
    private String isMinor;
    private String guardianName;
    private String nokName;
    private String nokMailingAdd;
    private Long nokCountry;
    private String nokCountryName;
    private String nokProvince;
    private Long nokCity;
    private String nokCityName;
    private Long nokPostalCode;
    private String nokContactNo;
    private String nokMobile;
    private String nokRelationship;
    private String nokNic;
    private String fatherCnic;
    private String motherCnic;
    private String isBeneficialOwnerDiffrent;
    private String beneficialOwnerName;
    private String beneficiaryRelationship;
    private String beneficiaryNationality;
    private String beneficiaryContactNo;
    private String typeOfAccount;
    private Long registrationStateId;
    private MultipartFile customerPic;
    private MultipartFile parentCnicPic;
    private MultipartFile bFormPic;
    private MultipartFile parentCnicBackPic;
    private MultipartFile tncPic;
    private MultipartFile signPic;
    private MultipartFile cnicFrontPic;
    private MultipartFile cnicBackPic;
    private MultipartFile proofOfProfessionPic;
    private MultipartFile level1FormPic;
    private MultipartFile sourceOfIncomePic;
    private byte[] customerPicByte;
    private byte[] parentCnicPicByte;
    private byte[] bFormPicByte;
    private byte[] parentCnicBackPicByte;
    private byte[] tncPicByte;
    private byte[] signPicByte;
    private byte[] cnicFrontPicByte;
    private byte[] cnicBackPicByte;
    private byte[] proofOfProfessionPicByte;
    private byte[] level1FormPicByte;
    private byte[] sourceOfIncomeByte;
    private String customerPicExt;
    private String parentCnicPicExt;
    private String parentCnicBackPicExt;
    private String bFormPicExt;
    private String tncPicExt;
    private String signPicExt;
    private String cnicFrontPicExt;
    private String cnicBackPicExt;
    private String proofOfProfessionExt;
    private String sourceOfIncomePicExt;
    private String level1FormPicExt;
    private Boolean customerPicDiscrepant;
    private Boolean parentCnicPicDiscrepant;
    private Boolean bFormPicDiscrepant;
    private Boolean parentCnicBackPicDiscrepant;
    private Boolean tncPicDiscrepant;
    private Boolean signPicDiscrepant;
    private Boolean cnicFrontPicDiscrepant;
    private Boolean cnicBackPicDiscrepant;
    private Boolean proofOfProfessionPicDiscrepant;
    private Boolean sourceOfIncomePicDiscrepant;
    private Boolean level1FormPicDiscrepant;
    private String fax;
    private String buisnessAddress;
    private Long buisnessPostalOfficeId;
    private Boolean publicFigure;
    private Long transactionModeId;
    private String otherTransactionMode;
    private Long accountPurposeId;
    private String accountPurposeName;
    private String comments;
    private String birthPlace;
    private String birthPlaceName;
    private String regStateComments;
    private String mfsId;
    private String accounttypeName;
    private String customerTypeName;
    private String segmentNameStr;
    private String fundSourceStr;
    private String usualModeOfTrans;
    private String regStateName;
    private Long appUserTypeId;
    /*added by atif hussain*/
    private boolean cnicSeen;
    private boolean fatherBvs;
    private Boolean screeningPerformed;
    //added by Turab
    private String remainingDailyCreditLimit;
    private String remainingDailyDebitLimit;
    private String remainingMonthlyCreditLimit;
    private String remainingMonthlyDebitLimit;
    private String remainingYearlyCreditLimit;
    private String remainingYearlyDebitLimit;
    private Long accountReasonId;
    private String accountReasonName;
    private Boolean verisysDone;
    private String accountState;
    private Long taxRegimeId;
    private String taxRegimeName;
    private Double fed;
    private String motherMaidenName;
    private Boolean filer;
    private Long nadraNegativeReasonId;
    private String thumbImpressionPic;
    private String nadraResponseCode;
    private String MotherName;
    private String fingerIndex;
    private String areaName;
    private Boolean isBvsAccount;
    private Long accountOpeningMethodId;
    private String hraTransactionPurpose;
    private String hraOccupation;
    private String hraNokMobile;
    private Date cnicIssuanceDate;
    private Boolean isDebitBlocked;
    private Double debitBlockAmount;
    // added by ahsan
    private String debitLimitDaily;
    private String creditLimitDaily;
    private String debitLimitMonthly;
    private String creditLimitMonthly;
    private String debitLimitYearly;
    private String creditLimitYearly;
    private String maximumCreditLimit;
    private String daily;
    private String monthly;
    private String yearly;
    private String maximumBalance;
    private Boolean dailyCheck;
    private Boolean monthlyCheck;
    private Boolean yearlyCheck;
    private Boolean maximumBalanceCheck;
    private String nadraMotherMedianName;
    private Boolean motherNameApp;
    private Boolean motheNameRej;
    private String nadraPlaceOfBirth;
    private Boolean placeOfBirthApp;
    private Boolean placeOfBirthRej;
    private String custPicMakerComments;
    private String custPicCheckerComments;
    private String pNicPicMakerComments;
    private String pNicPicCheckerComments;
    private String bFormPicMakerComments;
    private String bFormPicCheckerComments;
    private String nicFrontPicMakerComments;
    private String nicFrontPicCheckerComments;
    private String nicBackPicMakerComments;
    private String nicBackPicCheckerComments;
    private String pNicBackPicMakerComments;
    private String pNicBackPicCheckerComments;

    public Boolean getNameApp() {
        return nameApp;
    }

    public void setNameApp(Boolean nameApp) {
        this.nameApp = nameApp;
    }

    public Boolean getNameRej() {
        return nameRej;
    }

    public void setNameRej(Boolean nameRej) {
        this.nameRej = nameRej;
    }

    public String getNadraPlaceOfBirth() {
        return nadraPlaceOfBirth;
    }

    public void setNadraPlaceOfBirth(String nadraPlaceOfBirth) {
        this.nadraPlaceOfBirth = nadraPlaceOfBirth;
    }

    public Boolean getPlaceOfBirthApp() {
        return placeOfBirthApp;
    }

    public void setPlaceOfBirthApp(Boolean placeOfBirthApp) {
        this.placeOfBirthApp = placeOfBirthApp;
    }

    public Boolean getPlaceOfBirthRej() {
        return placeOfBirthRej;
    }

    public void setPlaceOfBirthRej(Boolean placeOfBirthRej) {
        this.placeOfBirthRej = placeOfBirthRej;
    }

    public Boolean getMotherNameApp() {
        return motherNameApp;
    }

    public void setMotherNameApp(Boolean motherNameApp) {
        this.motherNameApp = motherNameApp;
    }

    public Boolean getMotheNameRej() {
        return motheNameRej;
    }

    public void setMotheNameRej(Boolean motheNameRej) {
        this.motheNameRej = motheNameRej;
    }

    public String getNadraMotherMedianName() {
        return nadraMotherMedianName;
    }

    public void setNadraMotherMedianName(String nadraMotherMedianName) {
        this.nadraMotherMedianName = nadraMotherMedianName;
    }

    public Boolean getSignApp() {
        return signApp;
    }

    public void setSignApp(Boolean signApp) {
        this.signApp = signApp;
    }

    public Boolean getSignReg() {
        return signReg;
    }

    public void setSignReg(Boolean signReg) {
        this.signReg = signReg;
    }

    public String getPermanentDistOthers() {
        return permanentDistOthers;
    }

    public void setPermanentDistOthers(String permanentDistOthers) {
        this.permanentDistOthers = permanentDistOthers;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Boolean getRiskApp() {
        return riskApp;
    }

    public void setRiskApp(Boolean riskApp) {
        this.riskApp = riskApp;
    }

    public Boolean getRiskRej() {
        return riskRej;
    }

    public void setRiskRej(Boolean riskRej) {
        this.riskRej = riskRej;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Boolean getPoaApp() {
        return poaApp;
    }

    public void setPoaApp(Boolean poaApp) {
        this.poaApp = poaApp;
    }

    public Boolean getPoaRej() {
        return poaRej;
    }

    public void setPoaRej(Boolean poaRej) {
        this.poaRej = poaRej;
    }

    public Boolean getCustomerPicApp() {
        return customerPicApp;
    }

    public void setCustomerPicApp(Boolean customerPicApp) {
        this.customerPicApp = customerPicApp;
    }

    public Boolean getCustomerPicRej() {
        return customerPicRej;
    }

    public void setCustomerPicRej(Boolean customerPicRej) {
        this.customerPicRej = customerPicRej;
    }

    public Boolean getpAddressApp() {
        return pAddressApp;
    }

    public void setpAddressApp(Boolean pAddressApp) {
        this.pAddressApp = pAddressApp;
    }

    public Boolean getpAddressRej() {
        return pAddressRej;
    }

    public void setpAddressRej(Boolean pAddressRej) {
        this.pAddressRej = pAddressRej;
    }

    public Boolean getCnicApp() {
        return cnicApp;
    }

    public void setCnicApp(Boolean cnicApp) {
        this.cnicApp = cnicApp;
    }

    public Boolean getCnicRej() {
        return cnicRej;
    }

    public void setCnicRej(Boolean cnicRej) {
        this.cnicRej = cnicRej;
    }

    public Boolean getMobApp() {
        return mobApp;
    }

    public void setMobApp(Boolean mobApp) {
        this.mobApp = mobApp;
    }

    public Boolean getMobRej() {
        return mobRej;
    }

    public void setMobRej(Boolean mobRej) {
        this.mobRej = mobRej;
    }

    public Boolean getFhApp() {
        return fhApp;
    }

    public void setFhApp(Boolean fhApp) {
        this.fhApp = fhApp;
    }

    public Boolean getFhRej() {
        return fhRej;
    }

    public void setFhRej(Boolean fhRej) {
        this.fhRej = fhRej;
    }

    public Boolean getEmailApp() {
        return emailApp;
    }

    public void setEmailApp(Boolean emailApp) {
        this.emailApp = emailApp;
    }

    public Boolean getEmailRej() {
        return emailRej;
    }

    public void setEmailRej(Boolean emailRej) {
        this.emailRej = emailRej;
    }

    public Boolean getMailingApp() {
        return mailingApp;
    }

    public void setMailingApp(Boolean mailingApp) {
        this.mailingApp = mailingApp;
    }

    public Boolean getMailingRej() {
        return mailingRej;
    }

    public void setMailingRej(Boolean mailingRej) {
        this.mailingRej = mailingRej;
    }

    public Boolean getTurnOverApp() {
        return turnOverApp;
    }

    public void setTurnOverApp(Boolean turnOverApp) {
        this.turnOverApp = turnOverApp;
    }

    public Boolean getTurnOverRej() {
        return turnOverRej;
    }

    public void setTurnOverRej(Boolean turnOverRej) {
        this.turnOverRej = turnOverRej;
    }

    public Boolean getPopApp() {
        return popApp;
    }

    public void setPopApp(Boolean popApp) {
        this.popApp = popApp;
    }

    public Boolean getPopRej() {
        return popRej;
    }

    public void setPopRej(Boolean popRej) {
        this.popRej = popRej;
    }

    public Boolean getCnicFrontApp() {
        return cnicFrontApp;
    }

    public void setCnicFrontApp(Boolean cnicFrontApp) {
        this.cnicFrontApp = cnicFrontApp;
    }

    public Boolean getCnicFrontRej() {
        return cnicFrontRej;
    }

    public void setCnicFrontRej(Boolean cnicFrontRej) {
        this.cnicFrontRej = cnicFrontRej;
    }

    public Boolean getCnicBackApp() {
        return cnicBackApp;
    }

    public void setCnicBackApp(Boolean cnicBackApp) {
        this.cnicBackApp = cnicBackApp;
    }

    public Boolean getCnicBackRej() {
        return cnicBackRej;
    }

    public void setCnicBackRej(Boolean cnicBackRej) {
        this.cnicBackRej = cnicBackRej;
    }

    public Boolean getSourcePicApp() {
        return sourcePicApp;
    }

    public void setSourcePicApp(Boolean sourcePicApp) {
        this.sourcePicApp = sourcePicApp;
    }

    public Boolean getSourcePicRej() {
        return sourcePicRej;
    }

    public void setSourcePicRej(Boolean sourcePicRej) {
        this.sourcePicRej = sourcePicRej;
    }

    public Boolean getIncomeApp() {
        return incomeApp;
    }

    public void setIncomeApp(Boolean incomeApp) {
        this.incomeApp = incomeApp;
    }

    public Boolean getIncomeRej() {
        return incomeRej;
    }

    public void setIncomeRej(Boolean incomeRej) {
        this.incomeRej = incomeRej;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDualNationality() {
        return dualNationality;
    }

    public void setDualNationality(String dualNationality) {
        this.dualNationality = dualNationality;
    }

    public String getUsCitizen() {
        return usCitizen;
    }

    public void setUsCitizen(String usCitizen) {
        this.usCitizen = usCitizen;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getExpectedMonthlyTurnOver() {
        return expectedMonthlyTurnOver;
    }

    public void setExpectedMonthlyTurnOver(String expectedMonthlyTurnOver) {
        this.expectedMonthlyTurnOver = expectedMonthlyTurnOver;
    }

    public String getClsResponseCode() {
        return clsResponseCode;
    }

    public void setClsResponseCode(String clsResponseCode) {
        this.clsResponseCode = clsResponseCode;
    }

    public String getSourceOfIncomePicExt() {
        return sourceOfIncomePicExt;
    }

    public void setSourceOfIncomePicExt(String sourceOfIncomePicExt) {
        this.sourceOfIncomePicExt = sourceOfIncomePicExt;
    }

    public MultipartFile getSourceOfIncomePic() {
        return sourceOfIncomePic;
    }

    public void setSourceOfIncomePic(MultipartFile sourceOfIncomePic) {
        this.sourceOfIncomePic = sourceOfIncomePic;
    }

    public byte[] getSourceOfIncomeByte() {
        return sourceOfIncomeByte;
    }

    public void setSourceOfIncomeByte(byte[] sourceOfIncomeByte) {
        this.sourceOfIncomeByte = sourceOfIncomeByte;
    }

    public Boolean getSourceOfIncomePicDiscrepant() {
        return sourceOfIncomePicDiscrepant;
    }

    public void setSourceOfIncomePicDiscrepant(Boolean sourceOfIncomePicDiscrepant) {
        this.sourceOfIncomePicDiscrepant = sourceOfIncomePicDiscrepant;
    }

    public Boolean getMaximumBalanceCheck() {
        return maximumBalanceCheck;
    }

    public void setMaximumBalanceCheck(Boolean maximumBalanceCheck) {
        this.maximumBalanceCheck = maximumBalanceCheck;
    }

    public MultipartFile getProofOfProfessionPic() {
        return proofOfProfessionPic;
    }

    public void setProofOfProfessionPic(MultipartFile proofOfProfessionPic) {
        this.proofOfProfessionPic = proofOfProfessionPic;
    }

    public String getProofOfProfessionExt() {
        return proofOfProfessionExt;
    }

    public void setProofOfProfessionExt(String proofOfProfessionExt) {
        this.proofOfProfessionExt = proofOfProfessionExt;
    }

    public Boolean getProofOfProfessionPicDiscrepant() {
        return proofOfProfessionPicDiscrepant;
    }

    public void setProofOfProfessionPicDiscrepant(Boolean proofOfProfessionPicDiscrepant) {
        this.proofOfProfessionPicDiscrepant = proofOfProfessionPicDiscrepant;
    }

    public byte[] getProofOfProfessionPicByte() {
        return proofOfProfessionPicByte;
    }

    public void setProofOfProfessionPicByte(byte[] proofOfProfessionPicByte) {
        this.proofOfProfessionPicByte = proofOfProfessionPicByte;
    }

    public Boolean getDailyCheck() {
        return dailyCheck;
    }

    public void setDailyCheck(Boolean dailyCheck) {
        this.dailyCheck = dailyCheck;
    }

    public Boolean getMonthlyCheck() {
        return monthlyCheck;
    }

    public void setMonthlyCheck(Boolean monthlyCheck) {
        this.monthlyCheck = monthlyCheck;
    }

    public Boolean getYearlyCheck() {
        return yearlyCheck;
    }

    public void setYearlyCheck(Boolean yearlyCheck) {
        this.yearlyCheck = yearlyCheck;
    }

    public String getDebitLimitMonthly() {
        return debitLimitMonthly;
    }

    public void setDebitLimitMonthly(String debitLimitMonthly) {
        this.debitLimitMonthly = debitLimitMonthly;
    }

    public String getCreditLimitMonthly() {
        return creditLimitMonthly;
    }

    public void setCreditLimitMonthly(String creditLimitMonthly) {
        this.creditLimitMonthly = creditLimitMonthly;
    }

    public String getDebitLimitYearly() {
        return debitLimitYearly;
    }

    public void setDebitLimitYearly(String debitLimitYearly) {
        this.debitLimitYearly = debitLimitYearly;
    }

    public String getCreditLimitYearly() {
        return creditLimitYearly;
    }

    public void setCreditLimitYearly(String creditLimitYearly) {
        this.creditLimitYearly = creditLimitYearly;
    }

    public String getMonthly() {
        return "Monthly";
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getYearly() {
        return "Yearly";
    }

    public void setYearly(String yearly) {
        this.yearly = yearly;
    }

    public String getDaily() {
        return "Daily";
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getMaximumBalance() {
        return "Maximum Balance";
    }

    public void setMaximumBalance(String maximumBalance) {
        this.maximumBalance = maximumBalance;
    }

    public String getMaximumCreditLimit() {
        return maximumCreditLimit;
    }

    public void setMaximumCreditLimit(String maximumCreditLimit) {
        this.maximumCreditLimit = maximumCreditLimit;
    }

    public String getCreditLimitDaily() {
        return creditLimitDaily;
    }

    public void setCreditLimitDaily(String creditLimitDaily) {
        this.creditLimitDaily = creditLimitDaily;
    }

    public String getDebitLimitDaily() {
        return debitLimitDaily;
    }

    public void setDebitLimitDaily(String debitLimitDaily) {
        this.debitLimitDaily = debitLimitDaily;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPresentHomeAddCityName() {
        return presentHomeAddCityName;
    }

    public void setPresentHomeAddCityName(String presentHomeAddCityName) {
        this.presentHomeAddCityName = presentHomeAddCityName;
    }

    public Date getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(Date cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }

    public String getRefferedBy() {
        return refferedBy;
    }

    public void setRefferedBy(String refferedBy) {
        this.refferedBy = refferedBy;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(Long connectionType) {
        this.connectionType = connectionType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCustomerTypeName() {
        if (this.getCustomerTypeId() == null)
            return "";
        else
            return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccounttypeName() {
        return accounttypeName;
    }

    public void setAccounttypeName(String accounttypeName) {
        this.accounttypeName = accounttypeName;
    }

    public String getSegmentNameStr() {
        return segmentNameStr;
    }

    public void setSegmentNameStr(String segmentNameStr) {
        this.segmentNameStr = segmentNameStr;
    }

    public String getFundSourceStr() {
        if (null == this.getFundsSourceId())
            return "";
        else
            return fundSourceStr;
    }

    public void setFundSourceStr(String fundSourceStr) {
        this.fundSourceStr = fundSourceStr;
    }

    public String getUsualModeOfTrans() {
        if (null == this.getTransactionModeId())
            return "";
        else
            return usualModeOfTrans;
    }

    public void setUsualModeOfTrans(String usualModeOfTrans) {
        this.usualModeOfTrans = usualModeOfTrans;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getUsecaseId() {
        return usecaseId;
    }

    public void setUsecaseId(Long usecaseId) {
        this.usecaseId = usecaseId;
    }

    public String getSearchFirstName() {
        return searchFirstName;
    }

    public void setSearchFirstName(String searchFirstName) {
        this.searchFirstName = searchFirstName;
    }

    public String getSearchMfsId() {
        return searchMfsId;
    }

    public void setSearchMfsId(String searchMfsId) {
        this.searchMfsId = searchMfsId;
    }

    public String getSearchLastName() {
        return searchLastName;
    }

    public void setSearchLastName(String searchLastName) {
        this.searchLastName = searchLastName;
    }

    public String getSearchNic() {
        return searchNic;
    }

    public void setSearchNic(String searchNic) {
        this.searchNic = searchNic;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Boolean getZongRelation() {
        return zongRelation;
    }

    public void setZongRelation(Boolean zongRelation) {
        this.zongRelation = zongRelation;
    }

    public Boolean getAskariRelation() {
        return askariRelation;
    }

    public void setAskariRelation(Boolean askariRelation) {
        this.askariRelation = askariRelation;
    }

    public String getZongNo() {
        return zongNo;
    }

    public void setZongNo(String zongNo) {
        this.zongNo = zongNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    public String getPresentAddHouseNo() {
        return presentAddHouseNo;
    }

    public void setPresentAddHouseNo(String presentAddHouseNo) {
        this.presentAddHouseNo = presentAddHouseNo;
    }

    public String getPresentAddStreetNo() {
        return presentAddStreetNo;
    }

    public void setPresentAddStreetNo(String presentAddStreetNo) {
        this.presentAddStreetNo = presentAddStreetNo;
    }

    public Long getPresentAddDistrictId() {
        return presentAddDistrictId;
    }

    public void setPresentAddDistrictId(Long presentAddDistrictId) {
        this.presentAddDistrictId = presentAddDistrictId;
    }

    public Long getPresentAddCityId() {
        return presentAddCityId;
    }

    public void setPresentAddCityId(Long presentAddCityId) {
        this.presentAddCityId = presentAddCityId;
    }

    public Long getPresentAddPostalOfficeId() {
        return presentAddPostalOfficeId;
    }

    public void setPresentAddPostalOfficeId(Long presentAddPostalOfficeId) {
        this.presentAddPostalOfficeId = presentAddPostalOfficeId;
    }

    public String getPermanentAddHouseNo() {
        return permanentAddHouseNo;
    }

    public void setPermanentAddHouseNo(String permanentAddHouseNo) {
        this.permanentAddHouseNo = permanentAddHouseNo;
    }

    public String getPermanentAddStreetNo() {
        return permanentAddStreetNo;
    }

    public void setPermanentAddStreetNo(String permanentAddStreetNo) {
        this.permanentAddStreetNo = permanentAddStreetNo;
    }

    public void setpermanentAddStreetNo(String permanentAddStreetNo) {
        this.permanentAddStreetNo = permanentAddStreetNo;
    }

    public Long getPermanentAddDistrictId() {
        return permanentAddDistrictId;
    }

    public void setPermanentAddDistrictId(Long permanentAddDistrictId) {
        this.permanentAddDistrictId = permanentAddDistrictId;
    }

    public Long getPermanentAddCityId() {
        return permanentAddCityId;
    }

    public void setPermanentAddCityId(Long permanentAddCityId) {
        this.permanentAddCityId = permanentAddCityId;
    }

    public Long getPermanentAddPostalOfficeId() {
        return permanentAddPostalOfficeId;
    }

    public void setPermanentAddPostalOfficeId(Long permanentAddPostalOfficeId) {
        this.permanentAddPostalOfficeId = permanentAddPostalOfficeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getLandLineNo() {
        return landLineNo;
    }

    public void setLandLineNo(String landLineNo) {
        this.landLineNo = landLineNo;
    }

    public Long getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(Long customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String[] getFundsSourceId() {
        return fundsSourceId;
    }

    public void setFundsSourceId(String[] fundsSourceId) {
        this.fundsSourceId = fundsSourceId;
    }

    public String getFundSourceOthers() {
        return fundSourceOthers;
    }

    public void setFundSourceOthers(String fundSourceOthers) {
        this.fundSourceOthers = fundSourceOthers;
    }

    public String getPresentDistOthers() {
        return presentDistOthers;
    }

    public void setPresentDistOthers(String presentDistOthers) {
        this.presentDistOthers = presentDistOthers;
    }

    public String getPresentPostalOfficeOthers() {
        return presentPostalOfficeOthers;
    }

    public void setPresentPostalOfficeOthers(String presentPostalOfficeOthers) {
        this.presentPostalOfficeOthers = presentPostalOfficeOthers;
    }

    public String getPermanentPostalOfficeOthers() {
        return permanentPostalOfficeOthers;
    }

    public void setPermanentPostalOfficeOthers(String permanentPostalOfficeOthers) {
        this.permanentPostalOfficeOthers = permanentPostalOfficeOthers;
    }

    public String getFundSourceNarration() {
        return fundSourceNarration;
    }

    public void setFundSourceNarration(String fundSourceNarration) {
        this.fundSourceNarration = fundSourceNarration;
    }

    public String getPresentCityOthers() {
        return presentCityOthers;
    }

    public void setPresentCityOthers(String presentCityOthers) {
        this.presentCityOthers = presentCityOthers;
    }

    public String getPermanentAddCityOthers() {
        return permanentAddCityOthers;
    }

    public void setPermanentAddCityOthers(String permanentAddCityOthers) {
        this.permanentAddCityOthers = permanentAddCityOthers;
    }

    public Date getNicExpiryDate() {
        return nicExpiryDate;
    }

    public void setNicExpiryDate(Date nicExpiryDate) {
        this.nicExpiryDate = nicExpiryDate;
    }

    public Long getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    public String getPresentHomeAddDistrictName() {
        return presentHomeAddDistrictName;
    }

    public void setPresentHomeAddDistrictName(String presentHomeAddDistrictName) {
        this.presentHomeAddDistrictName = presentHomeAddDistrictName;
    }

    public String getPresentHomeAddPostalOfficeName() {
        return presentHomeAddPostalOfficeName;
    }

    public void setPresentHomeAddPostalOfficeName(
            String presentHomeAddPostalOfficeName) {
        this.presentHomeAddPostalOfficeName = presentHomeAddPostalOfficeName;
    }

    public String getPermanentHomeAddCityName() {
        return permanentHomeAddCityName;
    }

    public void setPermanentHomeAddCityName(String permanentHomeAddCityName) {
        this.permanentHomeAddCityName = permanentHomeAddCityName;
    }

    public String getPermanentHomeAddDistrictName() {
        return permanentHomeAddDistrictName;
    }

    public void setPermanentHomeAddDistrictName(String permanentHomeAddDistrictName) {
        this.permanentHomeAddDistrictName = permanentHomeAddDistrictName;
    }

    public String getPermanentHomeAddPostalOfficeName() {
        return permanentHomeAddPostalOfficeName;
    }

    public void setPermanentHomeAddPostalOfficeName(
            String permanentHomeAddPostalOfficeName) {
        this.permanentHomeAddPostalOfficeName = permanentHomeAddPostalOfficeName;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getTypeOfCustomerName() {
        return typeOfCustomerName;
    }

    public void setTypeOfCustomerName(String typeOfCustomerName) {
        this.typeOfCustomerName = typeOfCustomerName;
    }

    public String[] getFundSourceName() {
        return fundSourceName;
    }

    public void setFundSourceName(String[] fundSourceName) {
        this.fundSourceName = fundSourceName;
    }

    public String getCustomerAccountName() {
        return customerAccountName;
    }

    public void setCustomerAccountName(String customerAccountName) {
        this.customerAccountName = customerAccountName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAllpayId() {
        return allpayId;
    }

    public void setAllpayId(String allpayId) {
        this.allpayId = allpayId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public Date getSettledOn() {
        return settledOn;
    }

    public void setSettledOn(Date settledOn) {
        this.settledOn = settledOn;
    }

    /**
     * @return the settledBy
     */
    public String getSettledBy() {
        return settledBy;
    }

    /**
     * @param settledBy the settledBy to set
     */
    public void setSettledBy(String settledBy) {
        this.settledBy = settledBy;
    }

    /**
     * @return the accountClosedUnsettled
     */
    public Boolean getAccountClosedUnsettled() {
        return accountClosedUnsettled;
    }

    /**
     * @param accountClosedUnsettled the accountClosedUnsettled to set
     */
    public void setAccountClosedUnsettled(Boolean accountClosedUnsettled) {
        this.accountClosedUnsettled = accountClosedUnsettled;
    }

    /**
     * @return the accountClosedSettled
     */
    public Boolean getAccountClosedSettled() {
        return accountClosedSettled;
    }

    /**
     * @param accountClosedSettled the accountClosedSettled to set
     */
    public void setAccountClosedSettled(Boolean accountClosedSettled) {
        this.accountClosedSettled = accountClosedSettled;
    }

    /**
     * @return the closingComments
     */
    public String getClosingComments() {
        return closingComments;
    }

    /**
     * @param closingComments the closingComments to set
     */
    public void setClosingComments(String closingComments) {
        this.closingComments = closingComments;
    }

    /**
     * @return the settlementComments
     */
    public String getSettlementComments() {
        return settlementComments;
    }

    /**
     * @param settlementComments the settlementComments to set
     */
    public void setSettlementComments(String settlementComments) {
        this.settlementComments = settlementComments;
    }

    public String getAccountNature() {
        return accountNature;
    }

    public void setAccountNature(String accountNature) {
        this.accountNature = accountNature;
    }

    public String getResidanceStatus() {
        return residanceStatus;
    }

    public void setResidanceStatus(String residanceStatus) {
        this.residanceStatus = residanceStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(String initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNtn() {
        return ntn;
    }

    public void setNtn(String ntn) {
        this.ntn = ntn;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getNokName() {
        return nokName;
    }

    public void setNokName(String nokName) {
        this.nokName = nokName;
    }

    public String getNokMailingAdd() {
        return nokMailingAdd;
    }

    public void setNokMailingAdd(String nokMailingAdd) {
        this.nokMailingAdd = nokMailingAdd;
    }

    public Long getNokCountry() {
        return nokCountry;
    }

    public void setNokCountry(Long nokCountry) {
        this.nokCountry = nokCountry;
    }

    public String getNokProvince() {
        return nokProvince;
    }

    public void setNokProvince(String nokProvince) {
        this.nokProvince = nokProvince;
    }

    public Long getNokCity() {
        return nokCity;
    }

    public void setNokCity(Long nokCity) {
        this.nokCity = nokCity;
    }

    public Long getNokPostalCode() {
        return nokPostalCode;
    }

    public void setNokPostalCode(Long nokPostalCode) {
        this.nokPostalCode = nokPostalCode;
    }

    public String getNokContactNo() {
        return nokContactNo;
    }

    public void setNokContactNo(String nokContactNo) {
        this.nokContactNo = nokContactNo;
    }

    public String getNokRelationship() {
        return nokRelationship;
    }

    public void setNokRelationship(String nokRelationship) {
        this.nokRelationship = nokRelationship;
    }

    public String getIsBeneficialOwnerDiffrent() {
        return isBeneficialOwnerDiffrent;
    }

    public void setIsBeneficialOwnerDiffrent(String isBeneficialOwnerDiffrent) {
        this.isBeneficialOwnerDiffrent = isBeneficialOwnerDiffrent;
    }

    public String getBeneficialOwnerName() {
        return beneficialOwnerName;
    }

    public void setBeneficialOwnerName(String beneficialOwnerName) {
        this.beneficialOwnerName = beneficialOwnerName;
    }

    public String getBeneficiaryRelationship() {
        return beneficiaryRelationship;
    }

    public void setBeneficiaryRelationship(String beneficiaryRelationship) {
        this.beneficiaryRelationship = beneficiaryRelationship;
    }

    public String getBeneficiaryNationality() {
        return beneficiaryNationality;
    }

    public void setBeneficiaryNationality(String beneficiaryNationality) {
        this.beneficiaryNationality = beneficiaryNationality;
    }

    public String getBeneficiaryContactNo() {
        return beneficiaryContactNo;
    }

    public void setBeneficiaryContactNo(String beneficiaryContactNo) {
        this.beneficiaryContactNo = beneficiaryContactNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getPermanentCountry() {
        return permanentCountry;
    }

    public void setPermanentCountry(Long permanentCountry) {
        this.permanentCountry = permanentCountry;
    }

    public String getPermanentProvince() {
        return permanentProvince;
    }

    public void setPermanentProvince(String permanentProvince) {
        this.permanentProvince = permanentProvince;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public String getIsMinor() {
        return isMinor;
    }

    public void setIsMinor(String isMinor) {
        this.isMinor = isMinor;
    }

    public Long getRegistrationStateId() {
        return registrationStateId;
    }

    public void setRegistrationStateId(Long registrationStateId) {
        this.registrationStateId = registrationStateId;
    }

    public MultipartFile getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(MultipartFile customerPic) {
        this.customerPic = customerPic;
    }

    public MultipartFile getTncPic() {
        return tncPic;
    }

    public void setTncPic(MultipartFile tncPic) {
        this.tncPic = tncPic;
    }

    public MultipartFile getSignPic() {
        return signPic;
    }

    public void setSignPic(MultipartFile signPic) {
        this.signPic = signPic;
    }

    public MultipartFile getCnicFrontPic() {
        return cnicFrontPic;
    }

    public void setCnicFrontPic(MultipartFile cnicFrontPic) {
        this.cnicFrontPic = cnicFrontPic;
    }

    public MultipartFile getCnicBackPic() {
        return cnicBackPic;
    }

    public void setCnicBackPic(MultipartFile cnicBackPic) {
        this.cnicBackPic = cnicBackPic;
    }

    public String getNokMobile() {
        return nokMobile;
    }

    public void setNokMobile(String nokMobile) {
        this.nokMobile = nokMobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getBuisnessAddress() {
        return buisnessAddress;
    }

    public void setBuisnessAddress(String buisnessAddress) {
        this.buisnessAddress = buisnessAddress;
    }

    public Long getBuisnessPostalOfficeId() {
        return buisnessPostalOfficeId;
    }

    public void setBuisnessPostalOfficeId(Long buisnessPostalOfficeId) {
        this.buisnessPostalOfficeId = buisnessPostalOfficeId;
    }

    public Boolean getPublicFigure() {
        return publicFigure;
    }

    public void setPublicFigure(Boolean publicFigure) {
        this.publicFigure = publicFigure;
    }

    public Long getTransactionModeId() {
        return transactionModeId;
    }

    public void setTransactionModeId(Long transactionModeId) {
        this.transactionModeId = transactionModeId;
    }

    public Long getAccountPurposeId() {
        return accountPurposeId;
    }

    public void setAccountPurposeId(Long accountPurposeId) {
        this.accountPurposeId = accountPurposeId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getRegStateComments() {
        return regStateComments;
    }

    public void setRegStateComments(String regStateComments) {
        this.regStateComments = regStateComments;
    }

    public String getOtherTransactionMode() {
        return otherTransactionMode;
    }

    public void setOtherTransactionMode(String otherTransactionMode) {
        this.otherTransactionMode = otherTransactionMode;
    }

    public String getMfsId() {
        return mfsId;
    }

    public void setMfsId(String mfsId) {
        this.mfsId = mfsId;
    }

    public String getRegStateName() {
        if ((null != this.getRegistrationStateId()) && (2 == this.getRegistrationStateId().longValue()))
            return "Request Recieved";
        else
            return regStateName;
    }

    public void setRegStateName(String regStateName) {
        this.regStateName = regStateName;
    }

    public byte[] getCustomerPicByte() {
        return customerPicByte;
    }

    public void setCustomerPicByte(byte[] customerPicByte) {
        this.customerPicByte = customerPicByte;
    }

    public byte[] getTncPicByte() {
        return tncPicByte;
    }

    public void setTncPicByte(byte[] tncPicByte) {
        this.tncPicByte = tncPicByte;
    }

    public byte[] getSignPicByte() {
        return signPicByte;
    }

    public void setSignPicByte(byte[] signPicByte) {
        this.signPicByte = signPicByte;
    }

    public byte[] getCnicFrontPicByte() {
        return cnicFrontPicByte;
    }

    public void setCnicFrontPicByte(byte[] cnicFrontPicByte) {
        this.cnicFrontPicByte = cnicFrontPicByte;
    }

    public byte[] getCnicBackPicByte() {
        return cnicBackPicByte;
    }

    public void setCnicBackPicByte(byte[] cnicBackPicByte) {
        this.cnicBackPicByte = cnicBackPicByte;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public MultipartFile getLevel1FormPic() {
        return level1FormPic;
    }

    public void setLevel1FormPic(MultipartFile level1FormPic) {
        this.level1FormPic = level1FormPic;
    }

    public byte[] getLevel1FormPicByte() {
        return level1FormPicByte;
    }

    public void setLevel1FormPicByte(byte[] level1FormPicByte) {
        this.level1FormPicByte = level1FormPicByte;
    }

    public Boolean getCustomerPicDiscrepant() {
        return customerPicDiscrepant;
    }

    public void setCustomerPicDiscrepant(Boolean customerPicDiscrepant) {
        this.customerPicDiscrepant = customerPicDiscrepant;
    }

    public Boolean getTncPicDiscrepant() {
        return tncPicDiscrepant;
    }

    public void setTncPicDiscrepant(Boolean tncPicDiscrepant) {
        this.tncPicDiscrepant = tncPicDiscrepant;
    }

    public Boolean getSignPicDiscrepant() {
        return signPicDiscrepant;
    }

    public void setSignPicDiscrepant(Boolean signPicDiscrepant) {
        this.signPicDiscrepant = signPicDiscrepant;
    }

    public Boolean getCnicFrontPicDiscrepant() {
        return cnicFrontPicDiscrepant;
    }

    public void setCnicFrontPicDiscrepant(Boolean cnicFrontPicDiscrepant) {
        this.cnicFrontPicDiscrepant = cnicFrontPicDiscrepant;
    }

    public Boolean getCnicBackPicDiscrepant() {
        return cnicBackPicDiscrepant;
    }

    public void setCnicBackPicDiscrepant(Boolean cnicBackPicDiscrepant) {
        this.cnicBackPicDiscrepant = cnicBackPicDiscrepant;
    }

    public Boolean getLevel1FormPicDiscrepant() {
        return level1FormPicDiscrepant;
    }

    public void setLevel1FormPicDiscrepant(Boolean level1FormPicDiscrepant) {
        this.level1FormPicDiscrepant = level1FormPicDiscrepant;
    }

    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }

    public boolean isCnicSeen() {
        return cnicSeen;
    }

    public void setCnicSeen(boolean cnicSeen) {
        this.cnicSeen = cnicSeen;
    }

    public Boolean getScreeningPerformed() {
        return screeningPerformed;
    }

    public void setScreeningPerformed(Boolean screeningPerformed) {
        this.screeningPerformed = screeningPerformed;
    }

    public String getRemainingDailyCreditLimit() {
        return remainingDailyCreditLimit;
    }

    public void setRemainingDailyCreditLimit(String remainingDailyCreditLimit) {
        this.remainingDailyCreditLimit = remainingDailyCreditLimit;
    }

    public String getRemainingDailyDebitLimit() {
        return remainingDailyDebitLimit;
    }

    public void setRemainingDailyDebitLimit(String remainingDailyDebitLimit) {
        this.remainingDailyDebitLimit = remainingDailyDebitLimit;
    }

    public String getRemainingMonthlyCreditLimit() {
        return remainingMonthlyCreditLimit;
    }

    public void setRemainingMonthlyCreditLimit(String remainingMonthlyCreditLimit) {
        this.remainingMonthlyCreditLimit = remainingMonthlyCreditLimit;
    }

    public String getRemainingMonthlyDebitLimit() {
        return remainingMonthlyDebitLimit;
    }

    public void setRemainingMonthlyDebitLimit(String remainingMonthlyDebitLimit) {
        this.remainingMonthlyDebitLimit = remainingMonthlyDebitLimit;
    }

    public String getRemainingYearlyCreditLimit() {
        return remainingYearlyCreditLimit;
    }

    public void setRemainingYearlyCreditLimit(String remainingYearlyCreditLimit) {
        this.remainingYearlyCreditLimit = remainingYearlyCreditLimit;
    }

    public String getRemainingYearlyDebitLimit() {
        return remainingYearlyDebitLimit;
    }

    public void setRemainingYearlyDebitLimit(String remainingYearlyDebitLimit) {
        this.remainingYearlyDebitLimit = remainingYearlyDebitLimit;
    }

    public String getNokCountryName() {
        return nokCountryName;
    }

    public void setNokCountryName(String nokCountryName) {
        this.nokCountryName = nokCountryName;
    }

    public String getNokCityName() {
        return nokCityName;
    }

    public void setNokCityName(String nokCityName) {
        this.nokCityName = nokCityName;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getAccountPurposeName() {
        return accountPurposeName;
    }

    public void setAccountPurposeName(String accountPurposeName) {
        this.accountPurposeName = accountPurposeName;
    }

    public Long getAccountReasonId() {
        return accountReasonId;
    }

    public void setAccountReasonId(Long accountReasonId) {
        this.accountReasonId = accountReasonId;
    }

    public String getAccountReasonName() {
        return accountReasonName;
    }

    public void setAccountReasonName(String accountReasonName) {
        this.accountReasonName = accountReasonName;
    }

    public String getNokNic() {
        return nokNic;
    }

    public void setNokNic(String nokNic) {
        this.nokNic = nokNic;
    }

    public String getAgentPIN() {
        return agentPIN;
    }

    public void setAgentPIN(String agentPIN) {
        this.agentPIN = agentPIN;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        if (presentAddress != null) {
            this.presentAddress = presentAddress;
        }
    }

    public Boolean getVerisysDone() {
        return verisysDone;
    }

    public void setVerisysDone(Boolean verisysDone) {
        if (verisysDone != null) {
            this.verisysDone = verisysDone;
        }
    }

    public Long getTaxRegimeId() {
        return taxRegimeId;
    }

    public void setTaxRegimeId(Long taxRegimeId) {
        if (taxRegimeId != null) {
            this.taxRegimeId = taxRegimeId;
        }
    }

    public Double getFed() {
        return fed;
    }

    public void setFed(Double fed) {
        if (fed != null) {
            this.fed = fed;
        }
    }

    public String getTaxRegimeName() {
        return taxRegimeName;
    }

    public void setTaxRegimeName(String taxRegimeName) {
        if (taxRegimeName != null) {
            this.taxRegimeName = taxRegimeName;
        }
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        if (motherMaidenName != null) {
            this.motherMaidenName = motherMaidenName;
        }
    }

    public String getBirthPlaceName() {
        return birthPlaceName;
    }

    public void setBirthPlaceName(String birthPlaceName) {
        if (birthPlaceName != null) {
            this.birthPlaceName = birthPlaceName;
        }
    }

    public Boolean getFiler() {
        return filer;
    }

    public void setFiler(Boolean filer) {
        this.filer = filer;
    }

    public Long getNadraNegativeReasonId() {
        return nadraNegativeReasonId;
    }

    public void setNadraNegativeReasonId(Long nadraNegativeReasonId) {
        this.nadraNegativeReasonId = nadraNegativeReasonId;
    }

    public String getThumbImpressionPic() {
        return thumbImpressionPic;
    }

    public void setThumbImpressionPic(String thumbImpressionPic) {
        this.thumbImpressionPic = thumbImpressionPic;
    }

    public String getNadraResponseCode() {
        return nadraResponseCode;
    }

    public void setNadraResponseCode(String nadraResponseCode) {
        this.nadraResponseCode = nadraResponseCode;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Boolean getBvsAccount() {
        if (isBvsAccount == null) {
            isBvsAccount = false;
        }
        return isBvsAccount;
    }

    public void setIsBvsAccount(Boolean isBvsAccount) {
        this.isBvsAccount = isBvsAccount;
    }

    public Long getAccountOpeningMethodId() {
        return accountOpeningMethodId;
    }

    public void setAccountOpeningMethodId(Long accountOpeningMethodId) {
        this.accountOpeningMethodId = accountOpeningMethodId;
    }

    public Long getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(Long productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public String getProductCatalogName() {
        return productCatalogName;
    }

    public void setProductCatalogName(String productCatalogName) {
        this.productCatalogName = productCatalogName;
    }


    public String getCustomerPicExt() {
        return customerPicExt;
    }

    public void setCustomerPicExt(String customerPicExt) {
        this.customerPicExt = customerPicExt;
    }

    public String getTncPicExt() {
        return tncPicExt;
    }

    public void setTncPicExt(String tncPicExt) {
        this.tncPicExt = tncPicExt;
    }

    public String getSignPicExt() {
        return signPicExt;
    }

    public void setSignPicExt(String signPicExt) {
        this.signPicExt = signPicExt;
    }

    public String getCnicFrontPicExt() {
        return cnicFrontPicExt;
    }

    public void setCnicFrontPicExt(String cnicFrontPicExt) {
        this.cnicFrontPicExt = cnicFrontPicExt;
    }

    public String getCnicBackPicExt() {
        return cnicBackPicExt;
    }

    public void setCnicBackPicExt(String cnicBackPicExt) {
        this.cnicBackPicExt = cnicBackPicExt;
    }

    public String getLevel1FormPicExt() {
        return level1FormPicExt;
    }

    public void setLevel1FormPicExt(String level1FormPicExt) {
        this.level1FormPicExt = level1FormPicExt;
    }

    public String getHraTransactionPurpose() {
        return hraTransactionPurpose;
    }

    public void setHraTransactionPurpose(String hraTransactionPurpose) {
        this.hraTransactionPurpose = hraTransactionPurpose;
    }

    public String getHraOccupation() {
        return hraOccupation;
    }

    public void setHraOccupation(String hraOccupation) {
        this.hraOccupation = hraOccupation;
    }

    public String getHraNokMobile() {
        return hraNokMobile;
    }

    public void setHraNokMobile(String hraNokMobile) {
        this.hraNokMobile = hraNokMobile;
    }


    public String getHraAccounttypeName() {
        return hraAccounttypeName;
    }

    public void setHraAccounttypeName(String hraAccounttypeName) {
        this.hraAccounttypeName = hraAccounttypeName;
    }

    public String getRemainingHRADailyCreditLimit() {
        return remainingHRADailyCreditLimit;
    }

    public void setRemainingHRADailyCreditLimit(String remainingHRADailyCreditLimit) {
        this.remainingHRADailyCreditLimit = remainingHRADailyCreditLimit;
    }

    public String getRemainingHRADailyDebitLimit() {
        return remainingHRADailyDebitLimit;
    }

    public void setRemainingHRADailyDebitLimit(String remainingHRADailyDebitLimit) {
        this.remainingHRADailyDebitLimit = remainingHRADailyDebitLimit;
    }

    public String getRemainingHRAMonthlyCreditLimit() {
        return remainingHRAMonthlyCreditLimit;
    }

    public void setRemainingHRAMonthlyCreditLimit(String remainingHRAMonthlyCreditLimit) {
        this.remainingHRAMonthlyCreditLimit = remainingHRAMonthlyCreditLimit;
    }

    public String getRemainingHRAMonthlyDebitLimit() {
        return remainingHRAMonthlyDebitLimit;
    }

    public void setRemainingHRAMonthlyDebitLimit(String remainingHRAMonthlyDebitLimit) {
        this.remainingHRAMonthlyDebitLimit = remainingHRAMonthlyDebitLimit;
    }

    public String getRemainingHRAYearlyCreditLimit() {
        return remainingHRAYearlyCreditLimit;
    }

    public void setRemainingHRAYearlyCreditLimit(String remainingHRAYearlyCreditLimit) {
        this.remainingHRAYearlyCreditLimit = remainingHRAYearlyCreditLimit;
    }

    public String getRemainingHRAYearlyDebitLimit() {
        return remainingHRAYearlyDebitLimit;
    }

    public void setRemainingHRAYearlyDebitLimit(String remainingHRAYearlyDebitLimit) {
        this.remainingHRAYearlyDebitLimit = remainingHRAYearlyDebitLimit;
    }

    public String getHraAccountState() {
        return hraAccountState;
    }

    public void setHraAccountState(String hraAccountState) {
        this.hraAccountState = hraAccountState;
    }

    public Long getHraRegistrationStateId() {
        return hraRegistrationStateId;
    }

    public void setHraRegistrationStateId(Long hraRegistrationStateid) {
        this.hraRegistrationStateId = hraRegistrationStateid;
    }

    public Boolean getIsDebitBlocked() {
        return isDebitBlocked;
    }

    public void setIsDebitBlocked(Boolean isDebitBlocked) {
        this.isDebitBlocked = isDebitBlocked;
    }

    public Double getDebitBlockAmount() {
        return debitBlockAmount;
    }

    public void setDebitBlockAmount(Double debitBlockAmount) {
        this.debitBlockAmount = debitBlockAmount;
    }

    public String getParentCnicPicExt() {
        return parentCnicPicExt;
    }

    public void setParentCnicPicExt(String parentCnicPicExt) {
        this.parentCnicPicExt = parentCnicPicExt;
    }

    public String getbFormPicExt() {
        return bFormPicExt;
    }

    public void setbFormPicExt(String bFormPicExt) {
        this.bFormPicExt = bFormPicExt;
    }

    public Boolean getParentCnicPicDiscrepant() {
        return parentCnicPicDiscrepant;
    }

    public void setParentCnicPicDiscrepant(Boolean parentCnicPicDiscrepant) {
        this.parentCnicPicDiscrepant = parentCnicPicDiscrepant;
    }

    public Boolean getbFormPicDiscrepant() {
        return bFormPicDiscrepant;
    }

    public void setbFormPicDiscrepant(Boolean bFormPicDiscrepant) {
        this.bFormPicDiscrepant = bFormPicDiscrepant;
    }

    public MultipartFile getParentCnicPic() {
        return parentCnicPic;
    }

    public void setParentCnicPic(MultipartFile parentCnicPic) {
        this.parentCnicPic = parentCnicPic;
    }

    public MultipartFile getbFormPic() {
        return bFormPic;
    }

    public void setbFormPic(MultipartFile bFormPic) {
        this.bFormPic = bFormPic;
    }

    public byte[] getParentCnicPicByte() {
        return parentCnicPicByte;
    }

    public void setParentCnicPicByte(byte[] parentCnicPicByte) {
        this.parentCnicPicByte = parentCnicPicByte;
    }

    public byte[] getbFormPicByte() {
        return bFormPicByte;
    }

    public void setbFormPicByte(byte[] bFormPicByte) {
        this.bFormPicByte = bFormPicByte;
    }

    public String getFatherCnic() {
        return fatherCnic;
    }

    public void setFatherCnic(String fatherCnic) {
        this.fatherCnic = fatherCnic;
    }

    public String getMotherCnic() {
        return motherCnic;
    }

    public void setMotherCnic(String motherCnic) {
        this.motherCnic = motherCnic;
    }

    public MultipartFile getParentCnicBackPic() {
        return parentCnicBackPic;
    }

    public void setParentCnicBackPic(MultipartFile parentCnicBackPic) {
        this.parentCnicBackPic = parentCnicBackPic;
    }

    public byte[] getParentCnicBackPicByte() {
        return parentCnicBackPicByte;
    }

    public void setParentCnicBackPicByte(byte[] parentCnicBackPicByte) {
        this.parentCnicBackPicByte = parentCnicBackPicByte;
    }

    public Boolean getParentCnicBackPicDiscrepant() {
        return parentCnicBackPicDiscrepant;
    }

    public void setParentCnicBackPicDiscrepant(Boolean parentCnicBackPicDiscrepant) {
        this.parentCnicBackPicDiscrepant = parentCnicBackPicDiscrepant;
    }

    public String getParentCnicBackPicExt() {
        return parentCnicBackPicExt;
    }

    public void setParentCnicBackPicExt(String parentCnicBackPicExt) {
        this.parentCnicBackPicExt = parentCnicBackPicExt;
    }
    public String getCustPicMakerComments() {
        return custPicMakerComments;
    }

    public void setCustPicMakerComments(String custPicMakerComments) {
        this.custPicMakerComments = custPicMakerComments;
    }

    public String getCustPicCheckerComments() {
        return custPicCheckerComments;
    }

    public void setCustPicCheckerComments(String custPicCheckerComments) {
        this.custPicCheckerComments = custPicCheckerComments;
    }

    public String getpNicPicMakerComments() {
        return pNicPicMakerComments;
    }

    public void setpNicPicMakerComments(String pNicPicMakerComments) {
        this.pNicPicMakerComments = pNicPicMakerComments;
    }

    public String getpNicPicCheckerComments() {
        return pNicPicCheckerComments;
    }

    public void setpNicPicCheckerComments(String pNicPicCheckerComments) {
        this.pNicPicCheckerComments = pNicPicCheckerComments;
    }

    public String getbFormPicMakerComments() {
        return bFormPicMakerComments;
    }

    public void setbFormPicMakerComments(String bFormPicMakerComments) {
        this.bFormPicMakerComments = bFormPicMakerComments;
    }

    public String getbFormPicCheckerComments() {
        return bFormPicCheckerComments;
    }

    public void setbFormPicCheckerComments(String bFormPicCheckerComments) {
        this.bFormPicCheckerComments = bFormPicCheckerComments;
    }

    public String getNicFrontPicMakerComments() {
        return nicFrontPicMakerComments;
    }

    public void setNicFrontPicMakerComments(String nicFrontPicMakerComments) {
        this.nicFrontPicMakerComments = nicFrontPicMakerComments;
    }

    public String getNicFrontPicCheckerComments() {
        return nicFrontPicCheckerComments;
    }

    public void setNicFrontPicCheckerComments(String nicFrontPicCheckerComments) {
        this.nicFrontPicCheckerComments = nicFrontPicCheckerComments;
    }

    public String getNicBackPicMakerComments() {
        return nicBackPicMakerComments;
    }

    public void setNicBackPicMakerComments(String nicBackPicMakerComments) {
        this.nicBackPicMakerComments = nicBackPicMakerComments;
    }

    public String getNicBackPicCheckerComments() {
        return nicBackPicCheckerComments;
    }

    public void setNicBackPicCheckerComments(String nicBackPicCheckerComments) {
        this.nicBackPicCheckerComments = nicBackPicCheckerComments;
    }

    public String getpNicBackPicMakerComments() {
        return pNicBackPicMakerComments;
    }

    public void setpNicBackPicMakerComments(String pNicBackPicMakerComments) {
        this.pNicBackPicMakerComments = pNicBackPicMakerComments;
    }

    public String getpNicBackPicCheckerComments() {
        return pNicBackPicCheckerComments;
    }

    public void setpNicBackPicCheckerComments(String pNicBackPicCheckerComments) {
        this.pNicBackPicCheckerComments = pNicBackPicCheckerComments;
    }

    public boolean getFatherBvs() {
        return fatherBvs;
    }

    public void setFatherBvs(boolean fatherBvs) {
        this.fatherBvs = fatherBvs;
    }
}
