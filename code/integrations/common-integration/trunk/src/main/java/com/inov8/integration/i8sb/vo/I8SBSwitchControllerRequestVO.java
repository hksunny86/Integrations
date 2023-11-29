package com.inov8.integration.i8sb.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.webservice.optasiaVO.AdditionalInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by inov8 on 8/28/2017.
 */
@XStreamAlias("I8SBSwitchControllerRequestVO")
public class I8SBSwitchControllerRequestVO implements Serializable {

    private static final long serialVersionUID = 2311473488070381027L;

    private String i8sbGateway;
    //I8SB required parameters in each request
    private String i8sbClientID;
    private String i8sbClientTerminalID;
    private String i8sbChannelID;
    private String requestType;
    private String RRN;

    //Benificary
    private String totalTransaction;

    public String getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(String totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    // RDV Mobile banking common request/response parameters
    private String messageType;
    private String tranCode;
    private String transmissionDateAndTime;
    private String transactionId;
    private String transactionDate;
    private String transactionDateTime;
    private String transactionData;
    private String narrative;
    private String STAN;
    private String PAN;
    private String pinData;
    private String CNIC;
    private String dateOfBirth;
    private String cardTitle;
    private String email;
    private String emailSubject;
    private String emailText;
    private String mobilePhone;
    private String relationshipNumber;
    private String userId;
    private String password;
    private String passwordBitmap;
    private String MPINExpiry;
    private String MPINExpiryCheck;
    private String oldPassword;
    private String newPassword;
    private String MPIN;
    private String FPIN;
    private String FPINExpiry;
    private String FPINExpiryCheck;
    private String forcePasswordChange;
    private String accountId1;
    private String fromDate;
    private String toDate;
    private String fromAmmount;
    private String toAmmount;
    private String requestingTranCode;
    private String accountNumber;
    private String cardStatusFlag;
    private String accountId2;
    private String amount;
    private String transactionFees;
    private String bankImd;
    private String bankName;
    private String bankId;
    private String branchName;
    private String accountTitle;
    private String accountId1Type;
    private String merchantId;
    private String merchantName;
    private String merchantQRCode;
    private String additionalDetails;
    private String activityFlag;
    private String beneficiaryAccountTitle;
    private String categoryCode;
    private String companyCode;
    private String companyName;
    private String consumerNumber;
    private String consumerNickName;
    private String instrumentType;
    private String instrumentNumberStart;
    private String instrumentNumberEnd;
    private String chequeNoStart;
    private String chequeNoEnd;
    private String reasonCode;
    private String transactionAmount;
    private String additionalInformation;
    private String startDate;
    private String frequency;
    private String totalNumberOfExecution;
    private String SIID;
    private String SITranCode;
    private String beneficiaryCNIC;
    private String beneficiaryMobile;
    private String branchCurrency;
    private String accountType;
    private I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO;
    private String parentRequestRRN;
    private String requestXML;
    private String smsText;
    private String smsTransactionNature;
    private String smsTransactionType;
    private String encryptedContent;
    private String productCode;
    private String transferPurpose;
    private String txnCurrency;
    private String merchantCity;
    private String merchantCountry;
    private String merchantReferenceNum;
    private String merchantPurchaseType;
    private String acquirerCountryISO;
    private String acquirerBIN;
    private String visaBusinessAppId;
    private String visaIdCode;
    private String visaIdName;
    private String visaFeeProgramIndicator;
    private String txnRefNo;
    private String senderMobile;
    private String originalTxnDateTime;
    private String originalTxnDate;
    private String originalTxnID;
    private String originalSTAN;
    private String bankMnemonic;
    private String firstName;
    private String lastName;
    private String domainName;
    private String principleName;
    private String userName;
    private String channelID;
    private String terminalID;
    private String hashData;
    private String checkSum;
    private String categoryName;
    private String fromIBAN;
    private String GLAccount;
    private String instrumentNumber;
    private String instrumentDate;
    private String toIBAN;
    private String token;
    private String passcode;
    private String address;
    private String agentId;
    private String sessionId;
    private String sessionIdNadra;
    private String fingerTemplete;
    private String fingerIndex;
    private String accessToken;
    private String otp;
    private String Reserved1;
    private String Reserved2;
    private String Reserved3;
    private String Reserved4;
    private String Reserved5;
    private String recieverMobileNo;
    private String recieverCnic;
    private String recieverCity;
    private String SenderCity;
    private String SenderCnic;

    private String ResponseLanguage;
    private String productEntityId;
    private String EFormType;
    private String ProductNumber;
    private String Notes;
    private String CustCallbackPhone;
    private String CustRelationNum;
    private String DocPriority;
    private String DocMedium;
    private String CustSearchPan;
    private String PassportNumber;
    private String ConfirmEmailAddress;
    private String ConfirmPassword;
    private String EFAccountNumber;
    private String FullName15;
    private String EFDateOfBirth;
    private String MobileNumber;
    private String RelationshipPanNumber;
    private String IBUserName;
    private String IBCNIC;
    private String IBPassword;
    private String IBConfirmUserName;
    private String TelephoneNumberHome;
    private String IBEmailAddress;
    private String InstanceId;
    private String TFlag;
    private String AuthToken;
    private String Carrier;
    private String segmentId;
    private String orderRefId;
    private String status;
    private String partnerId;
    private String API_KEY;

    //Keys available in I8SBKeysOfCollectionEnum
    private Map<String, List<?>> collectionOfList = new HashMap<String, List<?>>();


    private String deviceKey;
    private String message;
    private List<String> deviceKeyList;
    private String notificationTypeId;
    private String cpId;
    private String userType;
    private String osType;
    private String uSSDServiceCode;
    private String uSSDRequestString;
    private String uSSDResponseCode;
    private String vlrNumber;
    private String location;

    private String statusFlag;
    private String bioFlag;

    private String registrationNumebr;
    private String chesisNumber;
    private String assessmentNumber;
    private String vehicleRegistrationNumber;
    private String assesmentTotalAmount;
    private String challanStatus;
    private String challanNumber;
    private String branchCode;
    private String cardId;
    private String cardNumber;
    private String transactionCodeDesc;
    private String transactionCode;
    private String description;
    private String startTime;
    private String endTime;
    private String windowTransactionId;
    private String dueDate;
    private String amount1;
    private String sIPaymentUpperLimit;
    private String sIStartDate;
    private String sIFrequency;
    private String sIEndDate;
    private String SISoonPossible;
    private String sIEndOccurance;
    private String sILatePossible;
    private String sIFrequencyDay;
    private String reminder;
    private String titleFetchStan;
    private String fromAccountTitle;
    private String fromAccountIBAN;
    private String toAccountNumber;
    private String amountTransaction;
    private String fromAccountNumber;
    private String toAccountTitle;
    private String toBankIMD;
    private String endDate;


    private String nfiq;
    private String minutiaeCount;
    private String isRegistered;
    private String isEnabled;
    private String isClose;
    private String isPinSet;

    private String cnicIssuanceDate;
    private String msisdn;
    private String dateTime;
    private String bankIMD;
    private String type;
    private String traceNo;
    private String channelName;
    private String newMPin;
    private String oldMPin;
    private String requestId;
    private String billerShortName;
    private String profileId;
    private String maxLimit;
    private String maxLimitOffline;
    private String singleTranLimit;
    private String isAllowed;
    private String imeiNumber;
    private String walletRequired;
    private String walletAccountId;
    private String transactionType;
    private String agentLocation;
    private String agenCity;
    private String pitb;
    private String nadraSessionId;
    private String tehsil;
    private String district;
    private String dob;
    private String bvsStatus;
    private String cnicExpiry;
    private String acquirerCountryCode;
    private String acquiringBin;
    private String businessApplicationId;
    private String cardAcceptor;
    private String feeProgramIndicator;
    private String merchantCategoryCode;
    private String recipientrimaryAccountNumber;
    private String secondaryId;
    private String senderAccountNumber;
    private String senderReferenceNumber;
    private String settlementServiceIndicator;
    private String systemTraceAuditNumber;
    private String transactionCurrencyCode;
    private String idCode;
    private String name;
    private String city;
    private String country;
    private String refrenceNumber;
    private String debitAccountNumber;
    private String debitCurrency;
    private String creditBranch;
    private String creditAccount;
    private String creditCurrency;
    private String chargeDebitBranch;
    private String chargedebitAccountNumber;
    private String chargedebitCurrency;
    private String toAccountBranch;

    private String orignalRRN;
    private String trnasctionIdentifier;
    private String machineName;
    private String ipAddress;
    private String macAddress;
    private String agentCity;
    private String longitude;
    private String latitude;
    private String udid;
    private String fatherName;
    private String thirdPartyChannelId;

    private String MTI;
    private String processingCode;
    private String timeLocalTransaction;
    private String dateLocalTransaction;
    private String merchantType;
    private String cardType;

    private String accountTypeCode;
    private String accountStatusCode;
    private String currencyCode;
    private String odLimitReviewDate;
    private String lastFinTranDate;
    private String interestFromDate;
    private String interestToDate;
    private String openedDate;
    private String limitProfile1;
    private String accountTitleOther;
    private String officerCode;
    private String availableBalance;
    private String ledgerBalance;
    private String holdAmount;
    private String statementFrequency;
    private String lastStatementDate;
    private String nextStatementDate;
    private String introRelationshipNum;
    private String introAddress;
    private String introAccountNum;
    private String comments;
    private String closedDate;
    private String closedRemarks;
    private String whenDeleted;
    private String jointDescription;
    private String address1;
    private String address2;
    private String countryCode;
    private String zipCode;
    private String t1;
    private String t2;
    private String t3;
    private String t4;
    private String t5;
    private String t6;
    private String t7;
    private String t8;
    private String iban;
    private String cardEmborsingName;
    private String cardTypeCode;
    private String cardProdCode;
    private String cardBranchCode;
    private String primaryCnic;
    private String issuedDate;
    private String custTypeCode;
    private String segmentCode;
    private String custStatusCode;
    private String genderCode;
    private String nationalityCode;
    private String langCode;
    private String fullNameOther;
    private String middleName;
    private String surName;
    private String suffix;
    private String title;
    private String department;
    private String businessTitle;
    private String primaryEmail;
    private String secondaryEmail;
    private String motherMaidenName;
    private String customerNin;
    private String taxNumber;
    private String placeOfBirth;
    private String qualification;
    private String profession;
    private String religionCode;
    private String martialCode;
    private String officeAddress1;
    private String officeAddress2;
    private String officeCity;
    private String officeCountry;
    private String officeZipCode;
    private String homeAddress1;
    private String homeAddress2;
    private String homeCity;
    private String homeCountry;
    private String homeZipCode;
    private String tempAddress1;
    private String tempAddress2;
    private String tempCity;
    private String tempCountry;
    private String tempZipCode;
    private String otherAddress1;
    private String otherAddress2;
    private String correspondenceFlag;
    private String officePhone1;
    private String officePhone2;
    private String officeFaxNum;
    private String residencePhone1;
    private String residencePhone2;
    private String residenceFaxNum;
    private String limitProfile;
    private String cif;
    private String nationality;
    private String mobileNetwork;
    private String operation;
    private String expiryDate;
    private String customerMsisdn;
    private String remark;
    private String mailingAddress;
    private String purposeOfAccount;
    private String sourceOfIncomePic;
    private String expectedMonthlyTurnOver;
    private String proofOfProfession;
    private String cnicFrontPic;
    private String cnicBackPic;
    private String customerPic;
    private String signaturePic;
    private String sourceOfIncome;
    private String fatherCnicPic;
    private String minorCnicPic;
    private String minorCnicBackPic;
    private String motherCnicPic;
    private String sourceRequestId;
    private String offerName;
    private String externalLoanId;
    private String loanPurpose;
    private String reason;
    private String identityType;
    private String origSource;
    private String identityValue;
    private String fed;
    private String loanEvent;
    private String loanEventStatus;
    private String upToPeriod;
    private String filterCommodityType;
    private String commodityType;
    private String filterLoanState;
    private String filterType;
    private String fullName;
    private List<AdditionalInfo> additionalInfoList;
    private String customerId;
    private String activeAccounts;
    private String outstandingAmount;
    private String dpd30;
    private String dpd60;
    private String dpd90;
    private String dpd120;
    private String dpd150;
    private String dpd180;
    private String writeOff;
    private boolean businessName;
    private boolean businessAddress;
    private boolean cnicFront;
    private boolean cnicBack;
    private boolean selfie;
    private String internalLoanId;
    private String aliasType;
    private String aliasValue;
    private String aliasId;
    private String accountId;
    private String nameApproved;
    private String streetNoApproved;
    private String houseNoApproved;
    private String cityApproved;
    private String areaApproved;
    private String deleted;
    private String pointOfEntry;
    private String networkIdentifier;
    private String cardAcceptorTerminalId;
    private String cardAcceptorIdentificationCode;
    private String cardAcceptorNameAndLocation;
    private String saf1linkIntegrationId;
    private String messageId;
    private String consumerCell;
    private String reversalId;
    private String reversalStatus;
    private String payMode;

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getReversalId() {
        return reversalId;
    }

    public void setReversalId(String reversalId) {
        this.reversalId = reversalId;
    }

    public String getReversalStatus() {
        return reversalStatus;
    }

    public void setReversalStatus(String reversalStatus) {
        this.reversalStatus = reversalStatus;
    }

    public String getConsumerCell() {
        return consumerCell;
    }

    public void setConsumerCell(String consumerCell) {
        this.consumerCell = consumerCell;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCityApproved() {
        return cityApproved;
    }

    public void setCityApproved(String cityApproved) {
        this.cityApproved = cityApproved;
    }

    public String getAreaApproved() {
        return areaApproved;
    }

    public void setAreaApproved(String areaApproved) {
        this.areaApproved = areaApproved;
    }

    public String getSaf1linkIntegrationId() {
        return saf1linkIntegrationId;
    }

    public void setSaf1linkIntegrationId(String saf1linkIntegrationId) {
        this.saf1linkIntegrationId = saf1linkIntegrationId;
    }

    public String getPointOfEntry() {
        return pointOfEntry;
    }

    public void setPointOfEntry(String pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
    }

    public String getCardAcceptorTerminalId() {
        return cardAcceptorTerminalId;
    }

    public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) {
        this.cardAcceptorTerminalId = cardAcceptorTerminalId;
    }

    public String getCardAcceptorIdentificationCode() {
        return cardAcceptorIdentificationCode;
    }

    public void setCardAcceptorIdentificationCode(String cardAcceptorIdentificationCode) {
        this.cardAcceptorIdentificationCode = cardAcceptorIdentificationCode;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getNameApproved() {
        return nameApproved;
    }

    public void setNameApproved(String nameApproved) {
        this.nameApproved = nameApproved;
    }

    public String getStreetNoApproved() {
        return streetNoApproved;
    }

    public void setStreetNoApproved(String streetNoApproved) {
        this.streetNoApproved = streetNoApproved;
    }

    public String getHouseNoApproved() {
        return houseNoApproved;
    }

    public void setHouseNoApproved(String houseNoApproved) {
        this.houseNoApproved = houseNoApproved;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAliasId() {
        return aliasId;
    }

    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    public String getAliasType() {
        return aliasType;
    }

    public void setAliasType(String aliasType) {
        this.aliasType = aliasType;
    }

    public String getAliasValue() {
        return aliasValue;
    }

    public void setAliasValue(String aliasValue) {
        this.aliasValue = aliasValue;
    }

    public String getInternalLoanId() {
        return internalLoanId;
    }

    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    public boolean isBusinessName() {
        return businessName;
    }

    public void setBusinessName(boolean businessName) {
        this.businessName = businessName;
    }

    public boolean isBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(boolean businessAddress) {
        this.businessAddress = businessAddress;
    }

    public boolean isCnicFront() {
        return cnicFront;
    }

    public void setCnicFront(boolean cnicFront) {
        this.cnicFront = cnicFront;
    }

    public boolean isCnicBack() {
        return cnicBack;
    }

    public void setCnicBack(boolean cnicBack) {
        this.cnicBack = cnicBack;
    }

    public boolean isSelfie() {
        return selfie;
    }

    public void setSelfie(boolean selfie) {
        this.selfie = selfie;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getActiveAccounts() {
        return activeAccounts;
    }

    public void setActiveAccounts(String activeAccounts) {
        this.activeAccounts = activeAccounts;
    }

    public String getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getDpd30() {
        return dpd30;
    }

    public void setDpd30(String dpd30) {
        this.dpd30 = dpd30;
    }

    public String getDpd60() {
        return dpd60;
    }

    public void setDpd60(String dpd60) {
        this.dpd60 = dpd60;
    }

    public String getDpd90() {
        return dpd90;
    }

    public void setDpd90(String dpd90) {
        this.dpd90 = dpd90;
    }

    public String getDpd120() {
        return dpd120;
    }

    public void setDpd120(String dpd120) {
        this.dpd120 = dpd120;
    }

    public String getDpd150() {
        return dpd150;
    }

    public void setDpd150(String dpd150) {
        this.dpd150 = dpd150;
    }

    public String getDpd180() {
        return dpd180;
    }

    public void setDpd180(String dpd180) {
        this.dpd180 = dpd180;
    }

    public String getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(String writeOff) {
        this.writeOff = writeOff;
    }

    public List<AdditionalInfo> getAdditionalInfoList() {
        return additionalInfoList;
    }

    public void setAdditionalInfoList(List<AdditionalInfo> additionalInfoList) {
        this.additionalInfoList = additionalInfoList;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getFilterLoanState() {
        return filterLoanState;
    }

    public void setFilterLoanState(String filterLoanState) {
        this.filterLoanState = filterLoanState;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterCommodityType() {
        return filterCommodityType;
    }

    public void setFilterCommodityType(String filterCommodityType) {
        this.filterCommodityType = filterCommodityType;
    }

    public String getUpToPeriod() {
        return upToPeriod;
    }

    public void setUpToPeriod(String upToPeriod) {
        this.upToPeriod = upToPeriod;
    }

    public String getLoanEvent() {
        return loanEvent;
    }

    public void setLoanEvent(String loanEvent) {
        this.loanEvent = loanEvent;
    }

    public String getLoanEventStatus() {
        return loanEventStatus;
    }

    public void setLoanEventStatus(String loanEventStatus) {
        this.loanEventStatus = loanEventStatus;
    }

    public String getFed() {
        return fed;
    }

    public void setFed(String fed) {
        this.fed = fed;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getOrigSource() {
        return origSource;
    }

    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    public String getIdentityValue() {
        return identityValue;
    }

    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSourceRequestId() {
        return sourceRequestId;
    }

    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getExternalLoanId() {
        return externalLoanId;
    }

    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getMotherCnicPic() {
        return motherCnicPic;
    }

    public void setMotherCnicPic(String motherCnicPic) {
        this.motherCnicPic = motherCnicPic;
    }

    public String getSignaturePic() {
        return signaturePic;
    }

    public void setSignaturePic(String signaturePic) {
        this.signaturePic = signaturePic;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getFatherCnicPic() {
        return fatherCnicPic;
    }

    public void setFatherCnicPic(String fatherCnicPic) {
        this.fatherCnicPic = fatherCnicPic;
    }

    public String getMinorCnicPic() {
        return minorCnicPic;
    }

    public void setMinorCnicPic(String minorCnicPic) {
        this.minorCnicPic = minorCnicPic;
    }

    public String getMinorCnicBackPic() {
        return minorCnicBackPic;
    }

    public void setMinorCnicBackPic(String minorCnicBackPic) {
        this.minorCnicBackPic = minorCnicBackPic;
    }

    public String getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(String customerPic) {
        this.customerPic = customerPic;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setPurposeOfAccount(String purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    public String getSourceOfIncomePic() {
        return sourceOfIncomePic;
    }

    public void setSourceOfIncomePic(String sourceOfIncomePic) {
        this.sourceOfIncomePic = sourceOfIncomePic;
    }

    public String getExpectedMonthlyTurnOver() {
        return expectedMonthlyTurnOver;
    }

    public void setExpectedMonthlyTurnOver(String expectedMonthlyTurnOver) {
        this.expectedMonthlyTurnOver = expectedMonthlyTurnOver;
    }

    public String getProofOfProfession() {
        return proofOfProfession;
    }

    public void setProofOfProfession(String proofOfProfession) {
        this.proofOfProfession = proofOfProfession;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCustomerMsisdn() {
        return customerMsisdn;
    }

    public void setCustomerMsisdn(String customerMsisdn) {
        this.customerMsisdn = customerMsisdn;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMobileNetwork() {
        return mobileNetwork;
    }

    public void setMobileNetwork(String mobileNetwork) {
        this.mobileNetwork = mobileNetwork;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getCustTypeCode() {
        return custTypeCode;
    }

    public void setCustTypeCode(String custTypeCode) {
        this.custTypeCode = custTypeCode;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getCustStatusCode() {
        return custStatusCode;
    }

    public void setCustStatusCode(String custStatusCode) {
        this.custStatusCode = custStatusCode;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getFullNameOther() {
        return fullNameOther;
    }

    public void setFullNameOther(String fullNameOther) {
        this.fullNameOther = fullNameOther;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBusinessTitle() {
        return businessTitle;
    }

    public void setBusinessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getCustomerNin() {
        return customerNin;
    }

    public void setCustomerNin(String customerNin) {
        this.customerNin = customerNin;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getReligionCode() {
        return religionCode;
    }

    public void setReligionCode(String religionCode) {
        this.religionCode = religionCode;
    }

    public String getMartialCode() {
        return martialCode;
    }

    public void setMartialCode(String martialCode) {
        this.martialCode = martialCode;
    }

    public String getOfficeAddress1() {
        return officeAddress1;
    }

    public void setOfficeAddress1(String officeAddress1) {
        this.officeAddress1 = officeAddress1;
    }

    public String getOfficeAddress2() {
        return officeAddress2;
    }

    public void setOfficeAddress2(String officeAddress2) {
        this.officeAddress2 = officeAddress2;
    }

    public String getOfficeCity() {
        return officeCity;
    }

    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }

    public String getOfficeCountry() {
        return officeCountry;
    }

    public void setOfficeCountry(String officeCountry) {
        this.officeCountry = officeCountry;
    }

    public String getOfficeZipCode() {
        return officeZipCode;
    }

    public void setOfficeZipCode(String officeZipCode) {
        this.officeZipCode = officeZipCode;
    }

    public String getHomeAddress1() {
        return homeAddress1;
    }

    public void setHomeAddress1(String homeAddress1) {
        this.homeAddress1 = homeAddress1;
    }

    public String getHomeAddress2() {
        return homeAddress2;
    }

    public void setHomeAddress2(String homeAddress2) {
        this.homeAddress2 = homeAddress2;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getHomeCountry() {
        return homeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }

    public String getHomeZipCode() {
        return homeZipCode;
    }

    public void setHomeZipCode(String homeZipCode) {
        this.homeZipCode = homeZipCode;
    }

    public String getTempAddress1() {
        return tempAddress1;
    }

    public void setTempAddress1(String tempAddress1) {
        this.tempAddress1 = tempAddress1;
    }

    public String getTempAddress2() {
        return tempAddress2;
    }

    public void setTempAddress2(String tempAddress2) {
        this.tempAddress2 = tempAddress2;
    }

    public String getTempCity() {
        return tempCity;
    }

    public void setTempCity(String tempCity) {
        this.tempCity = tempCity;
    }

    public String getTempCountry() {
        return tempCountry;
    }

    public void setTempCountry(String tempCountry) {
        this.tempCountry = tempCountry;
    }

    public String getTempZipCode() {
        return tempZipCode;
    }

    public void setTempZipCode(String tempZipCode) {
        this.tempZipCode = tempZipCode;
    }

    public String getOtherAddress1() {
        return otherAddress1;
    }

    public void setOtherAddress1(String otherAddress1) {
        this.otherAddress1 = otherAddress1;
    }

    public String getOtherAddress2() {
        return otherAddress2;
    }

    public void setOtherAddress2(String otherAddress2) {
        this.otherAddress2 = otherAddress2;
    }

    public String getCorrespondenceFlag() {
        return correspondenceFlag;
    }

    public void setCorrespondenceFlag(String correspondenceFlag) {
        this.correspondenceFlag = correspondenceFlag;
    }

    public String getOfficePhone1() {
        return officePhone1;
    }

    public void setOfficePhone1(String officePhone1) {
        this.officePhone1 = officePhone1;
    }

    public String getOfficePhone2() {
        return officePhone2;
    }

    public void setOfficePhone2(String officePhone2) {
        this.officePhone2 = officePhone2;
    }

    public String getOfficeFaxNum() {
        return officeFaxNum;
    }

    public void setOfficeFaxNum(String officeFaxNum) {
        this.officeFaxNum = officeFaxNum;
    }

    public String getResidencePhone1() {
        return residencePhone1;
    }

    public void setResidencePhone1(String residencePhone1) {
        this.residencePhone1 = residencePhone1;
    }

    public String getResidencePhone2() {
        return residencePhone2;
    }

    public void setResidencePhone2(String residencePhone2) {
        this.residencePhone2 = residencePhone2;
    }

    public String getResidenceFaxNum() {
        return residenceFaxNum;
    }

    public void setResidenceFaxNum(String residenceFaxNum) {
        this.residenceFaxNum = residenceFaxNum;
    }

    public String getLimitProfile() {
        return limitProfile;
    }

    public void setLimitProfile(String limitProfile) {
        this.limitProfile = limitProfile;
    }

    public String getCardEmborsingName() {
        return cardEmborsingName;
    }

    public void setCardEmborsingName(String cardEmborsingName) {
        this.cardEmborsingName = cardEmborsingName;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardProdCode() {
        return cardProdCode;
    }

    public void setCardProdCode(String cardProdCode) {
        this.cardProdCode = cardProdCode;
    }

    public String getCardBranchCode() {
        return cardBranchCode;
    }

    public void setCardBranchCode(String cardBranchCode) {
        this.cardBranchCode = cardBranchCode;
    }

    public String getPrimaryCnic() {
        return primaryCnic;
    }

    public void setPrimaryCnic(String primaryCnic) {
        this.primaryCnic = primaryCnic;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountStatusCode() {
        return accountStatusCode;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getOdLimitReviewDate() {
        return odLimitReviewDate;
    }

    public void setOdLimitReviewDate(String odLimitReviewDate) {
        this.odLimitReviewDate = odLimitReviewDate;
    }

    public String getLastFinTranDate() {
        return lastFinTranDate;
    }

    public void setLastFinTranDate(String lastFinTranDate) {
        this.lastFinTranDate = lastFinTranDate;
    }

    public String getInterestFromDate() {
        return interestFromDate;
    }

    public void setInterestFromDate(String interestFromDate) {
        this.interestFromDate = interestFromDate;
    }

    public String getInterestToDate() {
        return interestToDate;
    }

    public void setInterestToDate(String interestToDate) {
        this.interestToDate = interestToDate;
    }

    public String getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(String openedDate) {
        this.openedDate = openedDate;
    }

    public String getLimitProfile1() {
        return limitProfile1;
    }

    public void setLimitProfile1(String limitProfile1) {
        this.limitProfile1 = limitProfile1;
    }

    public String getAccountTitleOther() {
        return accountTitleOther;
    }

    public void setAccountTitleOther(String accountTitleOther) {
        this.accountTitleOther = accountTitleOther;
    }

    public String getOfficerCode() {
        return officerCode;
    }

    public void setOfficerCode(String officerCode) {
        this.officerCode = officerCode;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(String ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public String getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(String holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getStatementFrequency() {
        return statementFrequency;
    }

    public void setStatementFrequency(String statementFrequency) {
        this.statementFrequency = statementFrequency;
    }

    public String getLastStatementDate() {
        return lastStatementDate;
    }

    public void setLastStatementDate(String lastStatementDate) {
        this.lastStatementDate = lastStatementDate;
    }

    public String getNextStatementDate() {
        return nextStatementDate;
    }

    public void setNextStatementDate(String nextStatementDate) {
        this.nextStatementDate = nextStatementDate;
    }

    public String getIntroRelationshipNum() {
        return introRelationshipNum;
    }

    public void setIntroRelationshipNum(String introRelationshipNum) {
        this.introRelationshipNum = introRelationshipNum;
    }

    public String getIntroAddress() {
        return introAddress;
    }

    public void setIntroAddress(String introAddress) {
        this.introAddress = introAddress;
    }

    public String getIntroAccountNum() {
        return introAccountNum;
    }

    public void setIntroAccountNum(String introAccountNum) {
        this.introAccountNum = introAccountNum;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getClosedRemarks() {
        return closedRemarks;
    }

    public void setClosedRemarks(String closedRemarks) {
        this.closedRemarks = closedRemarks;
    }

    public String getWhenDeleted() {
        return whenDeleted;
    }

    public void setWhenDeleted(String whenDeleted) {
        this.whenDeleted = whenDeleted;
    }

    public String getJointDescription() {
        return jointDescription;
    }

    public void setJointDescription(String jointDescription) {
        this.jointDescription = jointDescription;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public String getT5() {
        return t5;
    }

    public void setT5(String t5) {
        this.t5 = t5;
    }

    public String getT6() {
        return t6;
    }

    public void setT6(String t6) {
        this.t6 = t6;
    }

    public String getT7() {
        return t7;
    }

    public void setT7(String t7) {
        this.t7 = t7;
    }

    public String getT8() {
        return t8;
    }

    public void setT8(String t8) {
        this.t8 = t8;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getMTI() {
        return MTI;
    }

    public void setMTI(String MTI) {
        this.MTI = MTI;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(String dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public void setAPI_KEY(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
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

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAgentCity() {
        return agentCity;
    }

    public void setAgentCity(String agentCity) {
        this.agentCity = agentCity;
    }

    public String getOrignalRRN() {
        return orignalRRN;
    }

    public void setOrignalRRN(String orignalRRN) {
        this.orignalRRN = orignalRRN;
    }

    public String getTrnasctionIdentifier() {
        return trnasctionIdentifier;
    }

    public void setTrnasctionIdentifier(String trnasctionIdentifier) {
        this.trnasctionIdentifier = trnasctionIdentifier;
    }

    public String getToAccountBranch() {
        return toAccountBranch;
    }

    public void setToAccountBranch(String toAccountBranch) {
        this.toAccountBranch = toAccountBranch;
    }

    public String getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public void setDebitAccountNumber(String debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
    }

    public String getDebitCurrency() {
        return debitCurrency;
    }

    public void setDebitCurrency(String debitCurrency) {
        this.debitCurrency = debitCurrency;
    }

    public String getCreditBranch() {
        return creditBranch;
    }

    public void setCreditBranch(String creditBranch) {
        this.creditBranch = creditBranch;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getCreditCurrency() {
        return creditCurrency;
    }

    public void setCreditCurrency(String creditCurrency) {
        this.creditCurrency = creditCurrency;
    }

    public String getChargeDebitBranch() {
        return chargeDebitBranch;
    }

    public void setChargeDebitBranch(String chargeDebitBranch) {
        this.chargeDebitBranch = chargeDebitBranch;
    }

    public String getChargedebitAccountNumber() {
        return chargedebitAccountNumber;
    }

    public void setChargedebitAccountNumber(String chargedebitAccountNumber) {
        this.chargedebitAccountNumber = chargedebitAccountNumber;
    }

    public String getChargedebitCurrency() {
        return chargedebitCurrency;
    }

    public void setChargedebitCurrency(String chargedebitCurrency) {
        this.chargedebitCurrency = chargedebitCurrency;
    }

    public String getAcquirerCountryCode() {
        return acquirerCountryCode;
    }

    public void setAcquirerCountryCode(String acquirerCountryCode) {
        this.acquirerCountryCode = acquirerCountryCode;
    }

    public String getAcquiringBin() {
        return acquiringBin;
    }

    public void setAcquiringBin(String acquiringBin) {
        this.acquiringBin = acquiringBin;
    }

    public String getBusinessApplicationId() {
        return businessApplicationId;
    }

    public void setBusinessApplicationId(String businessApplicationId) {
        this.businessApplicationId = businessApplicationId;
    }

    public String getCardAcceptor() {
        return cardAcceptor;
    }

    public void setCardAcceptor(String cardAcceptor) {
        this.cardAcceptor = cardAcceptor;
    }

    public String getFeeProgramIndicator() {
        return feeProgramIndicator;
    }

    public void setFeeProgramIndicator(String feeProgramIndicator) {
        this.feeProgramIndicator = feeProgramIndicator;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getRecipientrimaryAccountNumber() {
        return recipientrimaryAccountNumber;
    }

    public void setRecipientrimaryAccountNumber(String recipientrimaryAccountNumber) {
        this.recipientrimaryAccountNumber = recipientrimaryAccountNumber;
    }

    public String getSecondaryId() {
        return secondaryId;
    }

    public void setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getSenderReferenceNumber() {
        return senderReferenceNumber;
    }

    public void setSenderReferenceNumber(String senderReferenceNumber) {
        this.senderReferenceNumber = senderReferenceNumber;
    }

    public String getSettlementServiceIndicator() {
        return settlementServiceIndicator;
    }

    public void setSettlementServiceIndicator(String settlementServiceIndicator) {
        this.settlementServiceIndicator = settlementServiceIndicator;
    }

    public String getSystemTraceAuditNumber() {
        return systemTraceAuditNumber;
    }

    public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
        this.systemTraceAuditNumber = systemTraceAuditNumber;
    }

    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRefrenceNumber() {
        return refrenceNumber;
    }

    public void setRefrenceNumber(String refrenceNumber) {
        this.refrenceNumber = refrenceNumber;
    }

    public String getCnicExpiry() {
        return cnicExpiry;
    }

    public void setCnicExpiry(String cnicExpiry) {
        this.cnicExpiry = cnicExpiry;
    }

    public String getNadraSessionId() {
        return nadraSessionId;
    }

    public void setNadraSessionId(String nadraSessionId) {
        this.nadraSessionId = nadraSessionId;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBvsStatus() {
        return bvsStatus;
    }

    public void setBvsStatus(String bvsStatus) {
        this.bvsStatus = bvsStatus;
    }

    public String getPitb() {
        return pitb;
    }

    public void setPitb(String pitb) {
        this.pitb = pitb;
    }

    public String getAgenCity() {
        return agenCity;
    }

    public void setAgenCity(String agenCity) {
        this.agenCity = agenCity;
    }

    public String getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    public String getWalletAccountId() {
        return walletAccountId;
    }

    public void setWalletAccountId(String walletAccountId) {
        this.walletAccountId = walletAccountId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getWalletRequired() {
        return walletRequired;
    }

    public void setWalletRequired(String walletRequired) {
        this.walletRequired = walletRequired;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getNewMPin() {
        return newMPin;
    }

    public void setNewMPin(String newMPin) {
        this.newMPin = newMPin;
    }

    public String getOldMPin() {
        return oldMPin;
    }

    public void setOldMPin(String oldMPin) {
        this.oldMPin = oldMPin;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(String cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }


    public String getWindowTransactionId() {
        return windowTransactionId;
    }

    public void setWindowTransactionId(String windowTransactionId) {
        this.windowTransactionId = windowTransactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTransactionCodeDesc() {
        return transactionCodeDesc;
    }

    public void setTransactionCodeDesc(String transactionCodeDesc) {
        this.transactionCodeDesc = transactionCodeDesc;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getNfiq() {
        return nfiq;
    }

    public void setNfiq(String nfiq) {
        this.nfiq = nfiq;
    }

    public String getMinutiaeCount() {
        return minutiaeCount;
    }

    public void setMinutiaeCount(String minutiaeCount) {
        this.minutiaeCount = minutiaeCount;
    }


    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBioFlag() {
        return bioFlag;
    }

    public void setBioFlag(String bioFlag) {
        this.bioFlag = bioFlag;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDeviceKeyList() {
        return deviceKeyList;
    }

    public void setDeviceKeyList(List<String> deviceKeyList) {
        this.deviceKeyList = deviceKeyList;
    }

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(String notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }


    public String getCarrier() {
        return Carrier;
    }

    public void setCarrier(String carrier) {
        Carrier = carrier;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getTFlag() {
        return TFlag;
    }

    public void setTFlag(String TFlag) {
        this.TFlag = TFlag;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEFAccountNumber() {
        return EFAccountNumber;
    }

    public void setEFAccountNumber(String EFAccountNumber) {
        this.EFAccountNumber = EFAccountNumber;
    }

    public String getEFDateOfBirth() {
        return EFDateOfBirth;
    }

    public void setEFDateOfBirth(String EFDateOfBirth) {
        this.EFDateOfBirth = EFDateOfBirth;
    }

    public String getInstanceId() {
        return InstanceId;
    }

    public void setInstanceId(String instanceId) {
        InstanceId = instanceId;
    }

    public String getResponseLanguage() {
        return ResponseLanguage;
    }

    public void setResponseLanguage(String responseLanguage) {
        ResponseLanguage = responseLanguage;
    }

    public String getProductEntityId() {
        return productEntityId;
    }

    public void setProductEntityId(String productEntityId) {
        this.productEntityId = productEntityId;
    }

    public String getEFormType() {
        return EFormType;
    }

    public void setEFormType(String EFormType) {
        this.EFormType = EFormType;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumber(String productNumber) {
        ProductNumber = productNumber;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getCustCallbackPhone() {
        return CustCallbackPhone;
    }

    public void setCustCallbackPhone(String custCallbackPhone) {
        CustCallbackPhone = custCallbackPhone;
    }

    public String getCustRelationNum() {
        return CustRelationNum;
    }

    public void setCustRelationNum(String custRelationNum) {
        CustRelationNum = custRelationNum;
    }

    public String getDocPriority() {
        return DocPriority;
    }

    public void setDocPriority(String docPriority) {
        DocPriority = docPriority;
    }

    public String getDocMedium() {
        return DocMedium;
    }

    public void setDocMedium(String docMedium) {
        DocMedium = docMedium;
    }

    public String getCustSearchPan() {
        return CustSearchPan;
    }

    public void setCustSearchPan(String custSearchPan) {
        CustSearchPan = custSearchPan;
    }

    public String getPassportNumber() {
        return PassportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        PassportNumber = passportNumber;
    }

    public String getConfirmEmailAddress() {
        return ConfirmEmailAddress;
    }

    public void setConfirmEmailAddress(String confirmEmailAddress) {
        ConfirmEmailAddress = confirmEmailAddress;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getFullName15() {
        return FullName15;
    }

    public void setFullName15(String fullName15) {
        FullName15 = fullName15;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getRelationshipPanNumber() {
        return RelationshipPanNumber;
    }

    public void setRelationshipPanNumber(String relationshipPanNumber) {
        RelationshipPanNumber = relationshipPanNumber;
    }

    public String getIBUserName() {
        return IBUserName;
    }

    public void setIBUserName(String IBUserName) {
        this.IBUserName = IBUserName;
    }

    public String getIBCNIC() {
        return IBCNIC;
    }

    public void setIBCNIC(String IBCNIC) {
        this.IBCNIC = IBCNIC;
    }

    public String getIBPassword() {
        return IBPassword;
    }

    public void setIBPassword(String IBPassword) {
        this.IBPassword = IBPassword;
    }

    public String getIBConfirmUserName() {
        return IBConfirmUserName;
    }

    public void setIBConfirmUserName(String IBConfirmUserName) {
        this.IBConfirmUserName = IBConfirmUserName;
    }

    public String getTelephoneNumberHome() {
        return TelephoneNumberHome;
    }

    public void setTelephoneNumberHome(String telephoneNumberHome) {
        TelephoneNumberHome = telephoneNumberHome;
    }

    public String getIBEmailAddress() {
        return IBEmailAddress;
    }

    public void setIBEmailAddress(String IBEmailAddress) {
        this.IBEmailAddress = IBEmailAddress;
    }

    public String getRecieverMobileNo() {
        return recieverMobileNo;
    }

    public void setRecieverMobileNo(String recieverMobileNo) {
        this.recieverMobileNo = recieverMobileNo;
    }

    public String getRecieverCnic() {
        return recieverCnic;
    }

    public void setRecieverCnic(String recieverCnic) {
        this.recieverCnic = recieverCnic;
    }

    public String getRecieverCity() {
        return recieverCity;
    }

    public void setRecieverCity(String recieverCity) {
        this.recieverCity = recieverCity;
    }

    public String getSenderCity() {
        return SenderCity;
    }

    public void setSenderCity(String senderCity) {
        SenderCity = senderCity;
    }

    public String getSenderCnic() {
        return SenderCnic;
    }

    public void setSenderCnic(String senderCnic) {
        SenderCnic = senderCnic;
    }

    public String getReserved1() {
        return Reserved1;
    }

    public void setReserved1(String reserved1) {
        Reserved1 = reserved1;
    }

    public String getReserved2() {
        return Reserved2;
    }

    public void setReserved2(String reserved2) {
        Reserved2 = reserved2;
    }

    public String getReserved3() {
        return Reserved3;
    }

    public void setReserved3(String reserved3) {
        Reserved3 = reserved3;
    }

    public String getReserved4() {
        return Reserved4;
    }

    public void setReserved4(String reserved4) {
        Reserved4 = reserved4;
    }

    public String getReserved5() {
        return Reserved5;
    }

    public void setReserved5(String reserved5) {
        Reserved5 = reserved5;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    private String TempeleteType;
    private String AreaName;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionIdNadra() {
        return sessionIdNadra;
    }

    public void setSessionIdNadra(String sessionIdNadra) {
        this.sessionIdNadra = sessionIdNadra;
    }

    public String getFingerTemplete() {
        return fingerTemplete;
    }

    public void setFingerTemplete(String fingerTemplete) {
        this.fingerTemplete = fingerTemplete;
    }

    public String getTempeleteType() {
        return TempeleteType;
    }

    public void setTempeleteType(String tempeleteType) {
        TempeleteType = tempeleteType;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(String transactionData) {
        this.transactionData = transactionData;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getOriginalTxnDate() {
        return originalTxnDate;
    }

    public void setOriginalTxnDate(String originalTxnDate) {
        this.originalTxnDate = originalTxnDate;
    }

    public String getBankMnemonic() {
        return bankMnemonic;
    }

    public void setBankMnemonic(String bankMnemonic) {
        this.bankMnemonic = bankMnemonic;
    }

    public String getI8sbGateway() {
        return i8sbGateway;
    }

    public void setI8sbGateway(String i8sbGateway) {
        this.i8sbGateway = i8sbGateway;
    }

    public String getI8sbClientID() {
        return i8sbClientID;
    }

    public void setI8sbClientID(String i8sbClientID) {
        this.i8sbClientID = i8sbClientID;
    }

    public String getI8sbClientTerminalID() {
        return i8sbClientTerminalID;
    }

    public void setI8sbClientTerminalID(String i8sbClientTerminalID) {
        this.i8sbClientTerminalID = i8sbClientTerminalID;
    }

    public String getI8sbChannelID() {
        return i8sbChannelID;
    }

    public void setI8sbChannelID(String i8sbChannelID) {
        this.i8sbChannelID = i8sbChannelID;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getTransferPurpose() {
        return transferPurpose;
    }

    public void setTransferPurpose(String transferPurpose) {
        this.transferPurpose = transferPurpose;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSmsTransactionType() {
        return smsTransactionType;
    }

    public void setSmsTransactionType(String smsTransactionType) {
        this.smsTransactionType = smsTransactionType;
    }

    public String getSmsTransactionNature() {
        return smsTransactionNature;
    }

    public void setSmsTransactionNature(String smsTransactionNature) {
        this.smsTransactionNature = smsTransactionNature;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    @XmlTransient
    public String getRequestXML() {
        return requestXML;
    }

    public void setRequestXML(String requestXML) {
        this.requestXML = requestXML;
    }

    public String getParentRequestRRN() {
        return parentRequestRRN;
    }

    public void setParentRequestRRN(String parentRequestRRN) {
        this.parentRequestRRN = parentRequestRRN;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getBranchCurrency() {
        return branchCurrency;
    }

    public void setBranchCurrency(String branchCurrency) {
        this.branchCurrency = branchCurrency;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public I8SBSwitchControllerResponseVO getI8SBSwitchControllerResponseVO() {
        return i8SBSwitchControllerResponseVO;
    }

    public void setI8SBSwitchControllerResponseVO(I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        this.i8SBSwitchControllerResponseVO = i8SBSwitchControllerResponseVO;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordBitmap() {
        return passwordBitmap;
    }

    public void setPasswordBitmap(String passwordBitmap) {
        this.passwordBitmap = passwordBitmap;
    }

    public String getMPINExpiry() {
        return MPINExpiry;
    }

    public void setMPINExpiry(String MPINExpiry) {
        this.MPINExpiry = MPINExpiry;
    }

    public String getMPINExpiryCheck() {
        return MPINExpiryCheck;
    }

    public void setMPINExpiryCheck(String MPINExpiryCheck) {
        this.MPINExpiryCheck = MPINExpiryCheck;
    }

    public String getFPINExpiry() {
        return FPINExpiry;
    }

    public void setFPINExpiry(String FPINExpiry) {
        this.FPINExpiry = FPINExpiry;
    }

    public String getFPINExpiryCheck() {
        return FPINExpiryCheck;
    }

    public void setFPINExpiryCheck(String FPINExpiryCheck) {
        this.FPINExpiryCheck = FPINExpiryCheck;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMPIN() {
        return MPIN;
    }

    public void setMPIN(String MPIN) {
        this.MPIN = MPIN;
    }

    public String getFPIN() {
        return FPIN;
    }

    public void setFPIN(String FPIN) {
        this.FPIN = FPIN;
    }

    public String getForcePasswordChange() {
        return forcePasswordChange;
    }

    public void setForcePasswordChange(String forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }

    public String getAccountId1() {
        return accountId1;
    }

    public void setAccountId1(String accountId1) {
        this.accountId1 = accountId1;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromAmmount() {
        return fromAmmount;
    }

    public void setFromAmmount(String fromAmmount) {
        this.fromAmmount = fromAmmount;
    }

    public String getToAmmount() {
        return toAmmount;
    }

    public void setToAmmount(String toAmmount) {
        this.toAmmount = toAmmount;
    }

    public String getRequestingTranCode() {
        return requestingTranCode;
    }

    public void setRequestingTranCode(String requestingTranCode) {
        this.requestingTranCode = requestingTranCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardStatusFlag() {
        return cardStatusFlag;
    }

    public void setCardStatusFlag(String cardStatusFlag) {
        this.cardStatusFlag = cardStatusFlag;
    }

    public String getAccountId2() {
        return accountId2;
    }

    public void setAccountId2(String accountId2) {
        this.accountId2 = accountId2;
    }

    public String getTransactionFees() {
        return transactionFees;
    }

    public void setTransactionFees(String transactionFees) {
        this.transactionFees = transactionFees;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankImd() {
        return bankImd;
    }

    public void setBankImd(String bankImd) {
        this.bankImd = bankImd;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountId1Type() {
        return accountId1Type;
    }

    public void setAccountId1Type(String accountId1Type) {
        this.accountId1Type = accountId1Type;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantQRCode() {
        return merchantQRCode;
    }

    public void setMerchantQRCode(String merchantQRCode) {
        this.merchantQRCode = merchantQRCode;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public String getActivityFlag() {
        return activityFlag;
    }

    public void setActivityFlag(String activityFlag) {
        this.activityFlag = activityFlag;
    }

    public String getBeneficiaryAccountTitle() {
        return beneficiaryAccountTitle;
    }

    public void setBeneficiaryAccountTitle(String beneficiaryAccountTitle) {
        this.beneficiaryAccountTitle = beneficiaryAccountTitle;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getConsumerNickName() {
        return consumerNickName;
    }

    public void setConsumerNickName(String consumerNickName) {
        this.consumerNickName = consumerNickName;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrumentNumberStart() {
        return instrumentNumberStart;
    }

    public void setInstrumentNumberStart(String instrumentNumberStart) {
        this.instrumentNumberStart = instrumentNumberStart;
    }

    public String getInstrumentNumberEnd() {
        return instrumentNumberEnd;
    }

    public void setInstrumentNumberEnd(String instrumentNumberEnd) {
        this.instrumentNumberEnd = instrumentNumberEnd;
    }

    public String getChequeNoStart() {
        return chequeNoStart;
    }

    public void setChequeNoStart(String chequeNoStart) {
        this.chequeNoStart = chequeNoStart;
    }

    public String getChequeNoEnd() {
        return chequeNoEnd;
    }

    public void setChequeNoEnd(String chequeNoEnd) {
        this.chequeNoEnd = chequeNoEnd;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTotalNumberOfExecution() {
        return totalNumberOfExecution;
    }

    public void setTotalNumberOfExecution(String totalNumberOfExecution) {
        this.totalNumberOfExecution = totalNumberOfExecution;
    }

    public String getSIID() {
        return SIID;
    }

    public void setSIID(String SIID) {
        this.SIID = SIID;
    }

    public String getSITranCode() {
        return SITranCode;
    }

    public void setSITranCode(String SITranCode) {
        this.SITranCode = SITranCode;
    }

    public String getBeneficiaryCNIC() {
        return beneficiaryCNIC;
    }

    public void setBeneficiaryCNIC(String beneficiaryCNIC) {
        this.beneficiaryCNIC = beneficiaryCNIC;
    }

    public String getBeneficiaryMobile() {
        return beneficiaryMobile;
    }

    public void setBeneficiaryMobile(String beneficiaryMobile) {
        this.beneficiaryMobile = beneficiaryMobile;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getTransmissionDateAndTime() {
        return transmissionDateAndTime;
    }

    public void setTransmissionDateAndTime(String transmissionDateAndTime) {
        this.transmissionDateAndTime = transmissionDateAndTime;
    }

    public String getSTAN() {
        return STAN;
    }

    public void setSTAN(String STAN) {
        this.STAN = STAN;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getPinData() {
        return pinData;
    }

    public void setPinData(String pinData) {
        this.pinData = pinData;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRelationshipNumber() {
        return relationshipNumber;
    }

    public void setRelationshipNumber(String relationshipNumber) {
        this.relationshipNumber = relationshipNumber;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getTxnCurrency() {
        return txnCurrency;
    }

    public void setTxnCurrency(String txnCurrency) {
        this.txnCurrency = txnCurrency;
    }

    public String getMerchantCity() {
        return merchantCity;
    }

    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    public String getMerchantCountry() {
        return merchantCountry;
    }

    public void setMerchantCountry(String merchantCountry) {
        this.merchantCountry = merchantCountry;
    }

    public String getMerchantReferenceNum() {
        return merchantReferenceNum;
    }

    public void setMerchantReferenceNum(String merchantReferenceNum) {
        this.merchantReferenceNum = merchantReferenceNum;
    }

    public String getMerchantPurchaseType() {
        return merchantPurchaseType;
    }

    public void setMerchantPurchaseType(String merchantPurchaseType) {
        this.merchantPurchaseType = merchantPurchaseType;
    }

    public String getAcquirerCountryISO() {
        return acquirerCountryISO;
    }

    public void setAcquirerCountryISO(String acquirerCountryISO) {
        this.acquirerCountryISO = acquirerCountryISO;
    }

    public String getAcquirerBIN() {
        return acquirerBIN;
    }

    public void setAcquirerBIN(String acquirerBIN) {
        this.acquirerBIN = acquirerBIN;
    }

    public String getVisaBusinessAppId() {
        return visaBusinessAppId;
    }

    public void setVisaBusinessAppId(String visaBusinessAppId) {
        this.visaBusinessAppId = visaBusinessAppId;
    }

    public String getVisaIdCode() {
        return visaIdCode;
    }

    public void setVisaIdCode(String visaIdCode) {
        this.visaIdCode = visaIdCode;
    }

    public String getVisaIdName() {
        return visaIdName;
    }

    public void setVisaIdName(String visaIdName) {
        this.visaIdName = visaIdName;
    }

    public String getVisaFeeProgramIndicator() {
        return visaFeeProgramIndicator;
    }

    public void setVisaFeeProgramIndicator(String visaFeeProgramIndicator) {
        this.visaFeeProgramIndicator = visaFeeProgramIndicator;
    }

    public String getTxnRefNo() {
        return txnRefNo;
    }

    public void setTxnRefNo(String txnRefNo) {
        this.txnRefNo = txnRefNo;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getOriginalTxnDateTime() {
        return originalTxnDateTime;
    }

    public void setOriginalTxnDateTime(String originalTxnDateTime) {
        this.originalTxnDateTime = originalTxnDateTime;
    }

    public String getOriginalTxnID() {
        return originalTxnID;
    }

    public void setOriginalTxnID(String originalTxnID) {
        this.originalTxnID = originalTxnID;
    }

    public String getOriginalSTAN() {
        return originalSTAN;
    }

    public void setOriginalSTAN(String originalSTAN) {
        this.originalSTAN = originalSTAN;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getPrincipleName() {
        return principleName;
    }

    public void setPrincipleName(String principleName) {
        this.principleName = principleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFromIBAN() {
        return fromIBAN;
    }

    public void setFromIBAN(String fromIBAN) {
        this.fromIBAN = fromIBAN;
    }

    public String getGLAccount() {
        return GLAccount;
    }

    public void setGLAccount(String GLAccount) {
        this.GLAccount = GLAccount;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String getInstrumentDate() {
        return instrumentDate;
    }

    public void setInstrumentDate(String instrumentDate) {
        this.instrumentDate = instrumentDate;
    }

    public String getToIBAN() {
        return toIBAN;
    }

    public void setToIBAN(String toIBAN) {
        this.toIBAN = toIBAN;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public List<?> getList(String key) {
        return this.collectionOfList.get(key);
    }

    public void setCollectionOfList(Map map) {
        this.collectionOfList = map;
    }

    public Map getCollectionOfList() {
        return this.collectionOfList;
    }

    public void addListToCollection(String key, List<?> list) {
        this.collectionOfList.put(key, list);
    }

    public String getuSSDServiceCode() {
        return uSSDServiceCode;
    }

    public void setuSSDServiceCode(String uSSDServiceCode) {
        this.uSSDServiceCode = uSSDServiceCode;
    }

    public String getuSSDRequestString() {
        return uSSDRequestString;
    }

    public void setuSSDRequestString(String uSSDRequestString) {
        this.uSSDRequestString = uSSDRequestString;
    }

    public String getVlrNumber() {
        return vlrNumber;
    }

    public void setVlrNumber(String vlrNumber) {
        this.vlrNumber = vlrNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getuSSDResponseCode() {
        return uSSDResponseCode;
    }

    public void setuSSDResponseCode(String uSSDResponseCode) {
        this.uSSDResponseCode = uSSDResponseCode;
    }

    public String getRegistrationNumebr() {
        return registrationNumebr;
    }

    public void setRegistrationNumebr(String registrationNumebr) {
        this.registrationNumebr = registrationNumebr;
    }

    public String getChesisNumber() {
        return chesisNumber;
    }

    public void setChesisNumber(String chesisNumber) {
        this.chesisNumber = chesisNumber;
    }

    public String getAssessmentNumber() {
        return assessmentNumber;
    }

    public void setAssessmentNumber(String assessmentNumber) {
        this.assessmentNumber = assessmentNumber;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getAssesmentTotalAmount() {
        return assesmentTotalAmount;
    }

    public void setAssesmentTotalAmount(String assesmentTotalAmount) {
        this.assesmentTotalAmount = assesmentTotalAmount;
    }

    public String getChallanStatus() {
        return challanStatus;
    }

    public void setChallanStatus(String challanStatus) {
        this.challanStatus = challanStatus;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmount1() {
        return amount1;
    }

    public void setAmount1(String amount1) {
        this.amount1 = amount1;
    }

    public String getsIPaymentUpperLimit() {
        return sIPaymentUpperLimit;
    }

    public void setsIPaymentUpperLimit(String sIPaymentUpperLimit) {
        this.sIPaymentUpperLimit = sIPaymentUpperLimit;
    }

    public String getsIStartDate() {
        return sIStartDate;
    }

    public void setsIStartDate(String sIStartDate) {
        this.sIStartDate = sIStartDate;
    }

    public String getsIFrequency() {
        return sIFrequency;
    }

    public void setsIFrequency(String sIFrequency) {
        this.sIFrequency = sIFrequency;
    }

    public String getsIEndDate() {
        return sIEndDate;
    }

    public void setsIEndDate(String sIEndDate) {
        this.sIEndDate = sIEndDate;
    }

    public String getSISoonPossible() {
        return SISoonPossible;
    }

    public void setSISoonPossible(String SISoonPossible) {
        this.SISoonPossible = SISoonPossible;
    }

    public String getsIEndOccurance() {
        return sIEndOccurance;
    }

    public void setsIEndOccurance(String sIEndOccurance) {
        this.sIEndOccurance = sIEndOccurance;
    }

    public String getsILatePossible() {
        return sILatePossible;
    }

    public void setsILatePossible(String sILatePossible) {
        this.sILatePossible = sILatePossible;
    }

    public String getsIFrequencyDay() {
        return sIFrequencyDay;
    }

    public void setsIFrequencyDay(String sIFrequencyDay) {
        this.sIFrequencyDay = sIFrequencyDay;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getTitleFetchStan() {
        return titleFetchStan;
    }

    public void setTitleFetchStan(String titleFetchStan) {
        this.titleFetchStan = titleFetchStan;
    }

    public String getFromAccountTitle() {
        return fromAccountTitle;
    }

    public void setFromAccountTitle(String fromAccountTitle) {
        this.fromAccountTitle = fromAccountTitle;
    }

    public String getFromAccountIBAN() {
        return fromAccountIBAN;
    }

    public void setFromAccountIBAN(String fromAccountIBAN) {
        this.fromAccountIBAN = fromAccountIBAN;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getAmountTransaction() {
        return amountTransaction;
    }

    public void setAmountTransaction(String amountTransaction) {
        this.amountTransaction = amountTransaction;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountTitle() {
        return toAccountTitle;
    }

    public void setToAccountTitle(String toAccountTitle) {
        this.toAccountTitle = toAccountTitle;
    }

    public String getToBankIMD() {
        return toBankIMD;
    }

    public void setToBankIMD(String toBankIMD) {
        this.toBankIMD = toBankIMD;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getIsClose() {
        return isClose;
    }

    public void setIsClose(String isClose) {
        this.isClose = isClose;
    }

    public String getIsPinSet() {
        return isPinSet;
    }

    public void setIsPinSet(String isPinSet) {
        this.isPinSet = isPinSet;
    }

    public String getBillerShortName() {
        return billerShortName;
    }

    public void setBillerShortName(String billerShortName) {
        this.billerShortName = billerShortName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getOrderRefId() {
        return orderRefId;
    }

    public void setOrderRefId(String orderRefId) {
        this.orderRefId = orderRefId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getMaxLimitOffline() {
        return maxLimitOffline;
    }

    public void setMaxLimitOffline(String maxLimitOffline) {
        this.maxLimitOffline = maxLimitOffline;
    }

    public String getSingleTranLimit() {
        return singleTranLimit;
    }

    public void setSingleTranLimit(String singleTranLimit) {
        this.singleTranLimit = singleTranLimit;
    }

    public String getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(String isAllowed) {
        this.isAllowed = isAllowed;
    }

    public String getThirdPartyChannelId() {
        return thirdPartyChannelId;
    }

    public void setThirdPartyChannelId(String thirdPartyChannelId) {
        this.thirdPartyChannelId = thirdPartyChannelId;
    }
}
