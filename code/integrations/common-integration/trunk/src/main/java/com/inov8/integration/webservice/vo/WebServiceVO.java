package com.inov8.integration.webservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.vo.CardType;
import com.inov8.integration.vo.CatalogList;
import com.inov8.integration.vo.SegmentList;
import com.inov8.integration.webservice.corporateVO.AccountStatement;
import com.inov8.integration.webservice.corporateVO.CustomerDeviceVerification;
import com.inov8.integration.webservice.debitCardVO.CardTypeFee;
import com.inov8.integration.webservice.digiStatmentVO.DigiWalletStatementVo;
import com.inov8.integration.webservice.l2Account.L2Account;
import com.inov8.integration.webservice.l2Account.L2AccountFields;
import com.inov8.integration.webservice.optasiaVO.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WebServiceVO implements Serializable {
    private final static long serialVersionUID = 1L;
    private String userName;
    private String customerPassword;
    private String cnicNo;
    private String mobileNo;
    private String dateTime;
    private String retrievalReferenceNumber;
    private String transactionType;
    private String consumerName;
    private String accountTitle;
    private String birthPlace;
    private String presentAddress;
    private String cnicStatus;
    private String cnicExpiry;
    private String dateOfBirth;
    private String fatherHusbandName;
    private String motherMaiden;
    private String gender;
    private String cardNo;
    private String transactionAmount;
    private String settlementType;

    private String accountNo1;
    private String charges;
    private String mobilePin;
    private String loginPin;
    private String oldLoginPin;
    private String newLoginPin;
    private String confirmLoginPin;
    private String oldMpin;
    private String newMpin;
    private String confirmMpin;
    private String terminalId;
    private String paymentType;
    private String microbankTransactionCode;
    private String otpPin;
    private String otpPurpose;
    private String balance;
    private String cardExpiry;
    private String firstName;
    private String lastName;
    private String transactionId;
    private String customerStatus;
    private String channelId;
    private String accountType;

    private String customerPhoto;
    private String cnicFrontPhoto;
    private String cnicBackPhoto;
    private String signaturePhoto;
    private String termsPhoto;

    private String responseContentXML;
    private List<Transaction> transactions;
    private String transactionProcessingAmount;
    private String commissionAmount;
    private String totalAmount;
    private String remainingBalance;
    private String billAmount;
    private String productID;
    private String productName;
    private String consumerNo;
    private String consumerMobileNo;
    private String lateBillAmount;
    private String billPaid;
    private String dueDate;
    private String overDue;
    private String responseCode;
    private String responseCodeDescription;
    private String trackingId;
    private String PaymentMode;
    private String segmentCode;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String reserved5;
    private String reserved6;
    private String reserved7;
    private String reserved8;
    private String reserved9;
    private String reserved10;
    private String reserved11;
    private String reserved12;
    private String reserved13;
    private String reserved14;
    private String reserved15;
    private String remainingDebitLimit;
    private String remainingCreditLimit;
    private String consumedVelocity;
    private String transactionDateTime;
    private String transactionLocalDate;
    private String transactionLocalTime;
    private List<Map<String, String>> transactionsMap = new ArrayList<Map<String, String>>();
    private String agentMobileNumber;
    private String agentMpin;
    private String cardTypeId;
    private List<CardType> cardTypes;


    public List<CardType> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(List<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    private String fingerIndex;
    private String fingerTemplate;
    private String templateType;
    private String receiverMobileNumber;
    private String accountStatus;
    private String cnicIssuanceDate;
    private String emailAddress;
    private String customerMobileNetwork;
    private String SenderMobileNumber;
    private String DestinationAccountNumber;
    private String sourceBankImd;
    private String DestinationBankImd;
    private String purposeOfPayment;
    private String bankName;
    private String recieverAccountTilte;
    private String senderAccountTitle;
    private String recieverAccountNumber;
    private String branchName;
    private String BenificieryIban;
    private String challanAmount;
    private String pinType;
    private String cardDescription;
    private String mailingAddress;
    private String challanNumber;
    private String challanType;
    private String receiverCNIC;
    private String productCode;
    private String status;
    private String SourceOfIncome;
    private String Occupation;
    private String PurposeOfAccount;
    private String KINName;
    private String KINMobileNumber;
    private String KINCNIC;
    private String KINRelation;
    private String InternationalRemittanceLocation;
    private String OriginatorLocation;
    private String GLAccountNo1;
    private String GLAccountNo2;
    private String agentId;
    private String customerRegState;
    private String customerRegStateId;
    private String presentCity;
    private String permanentAddress;
    private String permanentCity;
    private String isCnicSeen;
    private String depositAmount;
    private String isBVSAccount;
    private String isHRA;
    private String transactionPurpose;
    private String orgLocation1;
    private String orgLocation2;
    private String orgLocation3;
    private String orgLocation4;
    private String orgLocation5;
    private String orgRelation1;
    private String orgRelation2;
    private String orgRelation3;
    private String orgRelation4;
    private String orgRelation5;
    private String isReceiveCash;
    private String isUpgrade;
    private String toAccount;
    private String fromAccount;
    private String branchlessAccountId;
    private String coreAccountId;
    private String bankId;
    private String loanAmount;
    private String numberOfInstallments;
    private String installmentAmount;
    private String gracePeriod;
    private String earlyPaymentCharges;
    private String latePaymentCharges;
    private String isOtpRequest;
    private String hraLinkedRequest;
    private String senderCity;
    private String receiverCity;
    private String bvsRequest;
    private String receiverAccountNumber;
    private String message;
    private List<SegmentList> segmentNames;
    private List<CatalogList> catalogNames;
    private String dailyCreditLimit;
    private String monthlyCreditLimit;
    private String dailyDebitLimit;
    private String monthlyDebitLimit;
    private String yearlyDebitLimit;
    private String yearlyCreditLimit;
    private String agentNetwork;
    private String expectedMonthlyTurnover;
    private String nextOfKin;
    private String latitude;
    private String longitude;
    private String stockTrading;
    private String mutulaFunds;
    private String sourceOfIncomePic;
    private String caseId;
    private String caseStatus;
    private String clsComment;
    private String currencyCode;
    private String minorCustomerPic;
    private String bFormPic;
    private String parentCnicPic;
    private String snicPic;
    private String sNicBackPic;
    private String ParentNicBackPic;
    private String fatherMotherMobileNumber;
    private String fatherCnic;
    private String fatherCnicIssuanceDate;
    private String fatherCnicExpiryDate;
    private String motherCnic;
    private String shaCnic;
    private String walletType;
    private String walletStatus;
    private String taxRegime;
    private String lienStatus;
    private String sourceRequestId;
    private String offerName;
    private String amount;
    private String externalLoanId;
    private String merchantId;
    private String fed;
    private String loanPurpose;
    private String code;
    private String identityValue;
    private String identityType;
    private String origSource;
    private String receivedTimestamp;
    private String internalLoanId;
    private String advanceOfferId;
    private String loanState;
    private String LoanTimestamp;
    private String loanReason;
    private String commodityType;
    private String principalAmount;
    private String setupFees;
    private String loanPlanId;
    private String loanPlanName;
    private String loanProductGroup;
    private String repaymentsCount;
    private String gross;
    private String principal;
    private String interest;
    private String interestVAT;
    private String chargesVAT;
    private String totalGross;
    private String totalPrincipal;
    private String totalSetupFees;
    private String totalInterest;
    private String totalInterestVAT;
    private String totalCharges;
    private String totalChargesVAT;
    private String totalPendingRecoveries;
    private String currentPeriod;
    private String daysLeftInPeriod;
    private String nextPeriod;
    private String loanOffer;
    private String setUpFees;
    private String repayment;
    private String repaymentCounts;
    private String totalExpenses;
    private String periodIndex;
    private String periodType;
    private String periodStartTimestamp;
    private String periodEndTimestamp;
    private String periodStartDayOfLoanIndex;
    private String periodEndDayOfLoanIndex;
    private String totalOneOffCharges;
    private String chargeName;
    private String chargeAmount;
    private String chargeVAT;
    private String principalFrom;
    private String principalTo;
    private String maturityDetails;
    private String maturityDuration;
    private String interestName;
    private String interestType;
    private String interestValue;
    private String daysOffset;
    private String interval;
    private String oneOffCharges;
    private String chargeType;
    private String chargeValue;
    private String periodsProjections;
    private String periodStartTimemp;
    private String milestones;
    private String dayOfLoan;
    private String date;
    private String interestAdjustment;
    private String net;
    private String vat;
    private String outstandingPerCurrency;
    private String numOutstandingLoans;
    private String totalPendingLoans;
    private String events;
    private String eventType;
    private String eventTypeStatus;
    private String eventTransactionId;
    private String thirdPartyTransactionId;
    private String eventReason;
    private String eventReasonDetails;
    private String period;
    private String periodExpirationTimestamp;
    private String principalAdjustment;
    private String principalBefore;
    private String principalAfter;
    private String setupFeesAdjustment;
    private String setupFeesBefore;
    private String setupFeesAfter;
    private String interestAdjustmentVAT;
    private String interestBefore;
    private String interestAfter;
    private String totalChargesAfter;
    private String totalChargesAdjustment;
    private String totalChargesAdjustmentVAT;
    private String totalChargesBefore;
    private String eventTimestamp;
    private String receptionTimestamp;
    private String processingTimestamp;
    private String loanReasonDetails;
    private String loanTimestamp;
    private String loanInfo;
    private String loan;
    private String report;
    private String outstanding;
    private String plan;
    private String reason;
    private String loanEvent;
    private String loanEventStatus;
    private String upToPeriod;
    private String offerClass;
    private String name;
    private String loanTimeStamp;
    private String shortCode;
    private String filterType;

    private String reportDate;
    private String reportTime;
    private String city;
    private String noOfActiveAccounts;
    private String totalOutstandingBalance;
    private String plus3024m;
    private String plus6024m;
    private String plus9024m;
    private String plus12024m;
    private String plus15024m;
    private String plus18024m;
    private String writeOff;
    private String disclaimerText;
    private String remarks;
    private String messageCode;
    private Boolean isEligible;
    private String eligibilityStatus;
    private String fromDate;
    private String toDate;
    private Boolean isStatusFlag;
    private String processingFee;
    private String refNo;
    private List<DueDatePlan> dueDatePlanList;
    private List<History> historyList;
    private List<Interest> interestList;
    private List<LoanAmount> loanAmountList;
    private List<LoanOffers> loanOffersList;
    private List<OneOffCharges> oneOffChargesList;
    private List<OutstandingStatus> outstandingStatusList;
    private List<RecurringCharges> recurringChargesList;
    private List<PeriodsProjection> periodsProjectionList;
    private List<LoansPerState> loansPerStates;
    private List<EligibilityStatus> eligibilityStatusList;
    private List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroupList;
    private List<MaturityDetails> maturityDetailsList;
    private List<LoansPerState> loansPerStateList;
    private List<Loans> loansList;
    private List<Loan> loanList;
    private List<Report> reportList;
    private List<Repayment> repaymentList;
    private List<Plan> planList;
    private List<TotalOneOffCharges> totalOneOffChargesList;
    private List<TotalRecurringCharge> totalRecurringChargeList;
    private List<InterestAdjustment> interestAdjustmentsList;
    private List<ChargeAdjustments> chargeAdjustmentsList;
    private List<Milestones> milestonesList;
    private List<Events> eventsList;
    private List<Charge> chargeList;
    private List<Account> accountList;
    private List<ThirdPartyData> thirdPartyDataList;
    private String outStandingAmount;
    private String serviceFee;
    private String disbursementDate;
    private String maturityDate;
    private String duration;
    private String primaryLoanId;
    private String startDate;
    private String businessName;
    private String businessAddress;
    private String estimatedMonthlySales;
    private String typeOfBusiness;
    private String profilePic;
    private String iDType;
    private String idN;
    private String tillID;
    private String partialRecoveryAmount;
    private String proofOfProfession;
    private String portalId;
    private String portalPassword;
    private List <AccountStatement> accountStatementList;
    private List <CustomerDeviceVerification> customerDeviceVerificationList;
    private String id;
    private String deviceName;
    private String approvalStatus;
    private List<L2AccountFields> l2AccountFieldsList;
    private List<L2Account> l2AccountList;
    private List<CardTypeFee> cardTypeFeeList;
    private String area;
    private String streetNumber;
    private String houseNumber;
    private String segmentName;
    private String emailOtp;
    private String qrCode;
    private String usCitizenship;
    private String usMobileNumber;
    private String signatoryAuthority;
    private String usLinks;
    private String federalTaxClassification;
    private String dualCitizenAddress;
    private String taxIdNumber;
    private String foreignTaxIdNumber;
    private String usaAccountNumber;
    private String billPicture;
    private String pmd;
    private String accountId;
    private String currencyId;
    private String currencyValue;
    private String debitCardRegistrationStatus;
    private String kyc;
    private Data data;
    private String accountLevel;
    private String freelanceAccountBalance;
    private String valueField;
    private CustomerValidationVO response;
    private List<MicrobankProwideVO> microbankProwideVOList;
    private String accountCurrency;
    private String spendingAmount;
    private String addressArea;
    private String zindigiUltraPurpose;
    private String countryOfBirth;
    private String countryTaxResidence;
    private String bornInUS;
    private String declaration;
    private String selectCountry;
    private String birthCountry;
    private String residenceAddress;
    private String taxResidence;
    private String referenceNumber;
    private String usBornCity;
    public List<DigiWalletStatementVo> digiWalletStatementVoList;
    public List<EndDayStatementVo> endDayStatementVoList;
    private String mti;
    private String processingCode;
    private String beneficialOwnership;
    private String sellerCode;
    private String terminalName;
    private String terminalPoiCnic;
    private String terminalPoiMobileNumber;
    private String terminalAddress;
    private String terminalCity;
    private String businessOwnerName;
    private String cnicOfOwner;
    private String ntn;
    private String fatherName;
    private String dateOfCorporation;
    private String primaryPhoneNo;
    private String secondaryPhoneNo;
    private String businessType;
    private String businessMode;
    private String merchantCategory;
    private String establishSince;
    private String accountPurpose;
    private String currentIncome;
    private String dailyTransactions;
    private String detailOfOtherSourceOfIncome;
    private String monthlyTransaction;
    private String otherDetails;
    private String expectedNoOfTransPerMonth;
    private String expectedSalesVolumePerMonth;
    private String mcnic;
    private String storeSellerCode;
    private String storeName;
    private String storeAddress;
    private String storeCity;
    private String registrationTypeCode;
    private String accountLevelCode;
    private String accountStatusCode;
    private String accountTypeCode;
    private String accountNatureCode;
    private Boolean amountRadio;
    private Boolean durationRadio;
    private String merchantName;
    private String merchantSellerCode;
    private String merchantCnic;
    private String merchantMobileNo;
    private String storeMobileNo;
    private Date createdDate;
    private Date fDate;
    private Date tDate;
    private String proofOfIncomePic;
    private String country;

    public String getProofOfIncomePic() {
        return proofOfIncomePic;
    }

    public void setProofOfIncomePic(String proofOfIncomePic) {
        this.proofOfIncomePic = proofOfIncomePic;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStoreMobileNo() {
        return storeMobileNo;
    }

    public void setStoreMobileNo(String storeMobileNo) {
        this.storeMobileNo = storeMobileNo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getfDate() {
        return fDate;
    }

    public void setfDate(Date fDate) {
        this.fDate = fDate;
    }

    public Date gettDate() {
        return tDate;
    }

    public void settDate(Date tDate) {
        this.tDate = tDate;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getStoreSellerCode() {
        return storeSellerCode;
    }

    public void setStoreSellerCode(String storeSellerCode) {
        this.storeSellerCode = storeSellerCode;
    }

    public String getMerchantSellerCode() {
        return merchantSellerCode;
    }

    public void setMerchantSellerCode(String merchantSellerCode) {
        this.merchantSellerCode = merchantSellerCode;
    }

    public String getMerchantCnic() {
        return merchantCnic;
    }

    public void setMerchantCnic(String merchantCnic) {
        this.merchantCnic = merchantCnic;
    }

    public String getMerchantMobileNo() {
        return merchantMobileNo;
    }

    public void setMerchantMobileNo(String merchantMobileNo) {
        this.merchantMobileNo = merchantMobileNo;
    }

    public Boolean getAmountRadio() {
        return amountRadio;
    }

    public void setAmountRadio(Boolean amountRadio) {
        this.amountRadio = amountRadio;
    }

    public Boolean getDurationRadio() {
        return durationRadio;
    }

    public void setDurationRadio(Boolean durationRadio) {
        this.durationRadio = durationRadio;
    }

    public String getRegistrationTypeCode() {
        return registrationTypeCode;
    }

    public void setRegistrationTypeCode(String registrationTypeCode) {
        this.registrationTypeCode = registrationTypeCode;
    }

    public String getAccountLevelCode() {
        return accountLevelCode;
    }

    public void setAccountLevelCode(String accountLevelCode) {
        this.accountLevelCode = accountLevelCode;
    }

    public String getAccountStatusCode() {
        return accountStatusCode;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountNatureCode() {
        return accountNatureCode;
    }

    public void setAccountNatureCode(String accountNatureCode) {
        this.accountNatureCode = accountNatureCode;
    }

    public String getMcnic() {
        return mcnic;
    }

    public void setMcnic(String mcnic) {
        this.mcnic = mcnic;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getBeneficialOwnership() {
        return beneficialOwnership;
    }

    public void setBeneficialOwnership(String beneficialOwnership) {
        this.beneficialOwnership = beneficialOwnership;
    }

    public String getBusinessOwnerName() {
        return businessOwnerName;
    }

    public void setBusinessOwnerName(String businessOwnerName) {
        this.businessOwnerName = businessOwnerName;
    }

    public String getCnicOfOwner() {
        return cnicOfOwner;
    }

    public void setCnicOfOwner(String cnicOfOwner) {
        this.cnicOfOwner = cnicOfOwner;
    }

    public String getNtn() {
        return ntn;
    }

    public void setNtn(String ntn) {
        this.ntn = ntn;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDateOfCorporation() {
        return dateOfCorporation;
    }

    public void setDateOfCorporation(String dateOfCorporation) {
        this.dateOfCorporation = dateOfCorporation;
    }

    public String getPrimaryPhoneNo() {
        return primaryPhoneNo;
    }

    public void setPrimaryPhoneNo(String primaryPhoneNo) {
        this.primaryPhoneNo = primaryPhoneNo;
    }

    public String getSecondaryPhoneNo() {
        return secondaryPhoneNo;
    }

    public void setSecondaryPhoneNo(String secondaryPhoneNo) {
        this.secondaryPhoneNo = secondaryPhoneNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    public String getEstablishSince() {
        return establishSince;
    }

    public void setEstablishSince(String establishSince) {
        this.establishSince = establishSince;
    }

    public String getAccountPurpose() {
        return accountPurpose;
    }

    public void setAccountPurpose(String accountPurpose) {
        this.accountPurpose = accountPurpose;
    }

    public String getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(String currentIncome) {
        this.currentIncome = currentIncome;
    }

    public String getDailyTransactions() {
        return dailyTransactions;
    }

    public void setDailyTransactions(String dailyTransactions) {
        this.dailyTransactions = dailyTransactions;
    }

    public String getDetailOfOtherSourceOfIncome() {
        return detailOfOtherSourceOfIncome;
    }

    public void setDetailOfOtherSourceOfIncome(String detailOfOtherSourceOfIncome) {
        this.detailOfOtherSourceOfIncome = detailOfOtherSourceOfIncome;
    }

    public String getMonthlyTransaction() {
        return monthlyTransaction;
    }

    public void setMonthlyTransaction(String monthlyTransaction) {
        this.monthlyTransaction = monthlyTransaction;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public String getExpectedNoOfTransPerMonth() {
        return expectedNoOfTransPerMonth;
    }

    public void setExpectedNoOfTransPerMonth(String expectedNoOfTransPerMonth) {
        this.expectedNoOfTransPerMonth = expectedNoOfTransPerMonth;
    }

    public String getExpectedSalesVolumePerMonth() {
        return expectedSalesVolumePerMonth;
    }

    public void setExpectedSalesVolumePerMonth(String expectedSalesVolumePerMonth) {
        this.expectedSalesVolumePerMonth = expectedSalesVolumePerMonth;
    }

    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalPoiCnic() {
        return terminalPoiCnic;
    }

    public void setTerminalPoiCnic(String terminalPoiCnic) {
        this.terminalPoiCnic = terminalPoiCnic;
    }

    public String getTerminalPoiMobileNumber() {
        return terminalPoiMobileNumber;
    }

    public void setTerminalPoiMobileNumber(String terminalPoiMobileNumber) {
        this.terminalPoiMobileNumber = terminalPoiMobileNumber;
    }

    public String getTerminalAddress() {
        return terminalAddress;
    }

    public void setTerminalAddress(String terminalAddress) {
        this.terminalAddress = terminalAddress;
    }

    public String getTerminalCity() {
        return terminalCity;
    }

    public void setTerminalCity(String terminalCity) {
        this.terminalCity = terminalCity;
    }

    public List<EndDayStatementVo> getEndDayStatementVoList() {
        return endDayStatementVoList;
    }

    public void setEndDayStatementVoList(List<EndDayStatementVo> endDayStatementVoList) {
        this.endDayStatementVoList = endDayStatementVoList;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public List<DigiWalletStatementVo> getDigiWalletStatementVoList() {
        return digiWalletStatementVoList;
    }

    public void setDigiWalletStatementVoList(List<DigiWalletStatementVo> digiWalletStatementVoList) {
        this.digiWalletStatementVoList = digiWalletStatementVoList;
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

    public List<MicrobankProwideVO> getMicrobankProwideVOList() {
        return microbankProwideVOList;
    }

    public void setMicrobankProwideVOList(List<MicrobankProwideVO> microbankProwideVOList) {
        this.microbankProwideVOList = microbankProwideVOList;
    }

    public CustomerValidationVO getResponse() {
        return response;
    }

    public void setResponse(CustomerValidationVO response) {
        this.response = response;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getFreelanceAccountBalance() {
        return freelanceAccountBalance;
    }

    public void setFreelanceAccountBalance(String freelanceAccountBalance) {
        this.freelanceAccountBalance = freelanceAccountBalance;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getKyc() {
        return kyc;
    }

    public void setKyc(String kyc) {
        this.kyc = kyc;
    }

    public String getDebitCardRegistrationStatus() {
        return debitCardRegistrationStatus;
    }

    public void setDebitCardRegistrationStatus(String debitCardRegistrationStatus) {
        this.debitCardRegistrationStatus = debitCardRegistrationStatus;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPmd() {
        return pmd;
    }

    public void setPmd(String pmd) {
        this.pmd = pmd;
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

    public String getUsaAccountNumber() {
        return usaAccountNumber;
    }

    public void setUsaAccountNumber(String usaAccountNumber) {
        this.usaAccountNumber = usaAccountNumber;
    }

    public String getBillPicture() {
        return billPicture;
    }

    public void setBillPicture(String billPicture) {
        this.billPicture = billPicture;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public String getEmailOtp() {
        return emailOtp;
    }

    public void setEmailOtp(String emailOtp) {
        this.emailOtp = emailOtp;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public List<CardTypeFee> getCardTypeFeeList() {
        return cardTypeFeeList;
    }

    public void setCardTypeFeeList(List<CardTypeFee> cardTypeFeeList) {
        this.cardTypeFeeList = cardTypeFeeList;
    }

    public List<L2AccountFields> getL2AccountFieldsList() {
        return l2AccountFieldsList;
    }

    public void setL2AccountFieldsList(List<L2AccountFields> l2AccountFieldsList) {
        this.l2AccountFieldsList = l2AccountFieldsList;
    }

    public List<L2Account> getL2AccountList() {
        return l2AccountList;
    }

    public void setL2AccountList(List<L2Account> l2AccountList) {
        this.l2AccountList = l2AccountList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<CustomerDeviceVerification> getCustomerDeviceVerificationList() {
        return customerDeviceVerificationList;
    }

    public void setCustomerDeviceVerificationList(List<CustomerDeviceVerification> customerDeviceVerificationList) {
        this.customerDeviceVerificationList = customerDeviceVerificationList;
    }

    public List<AccountStatement> getAccountStatementList() {
        return accountStatementList;
    }

    public void setAccountStatementList(List<AccountStatement> accountStatementList) {
        this.accountStatementList = accountStatementList;
    }

    public String getPortalId() {
        return portalId;
    }

    public void setPortalId(String portalId) {
        this.portalId = portalId;
    }

    public String getPortalPassword() {
        return portalPassword;
    }

    public void setPortalPassword(String portalPassword) {
        this.portalPassword = portalPassword;
    }

    public String getProofOfProfession() {
        return proofOfProfession;
    }

    public void setProofOfProfession(String proofOfProfession) {
        this.proofOfProfession = proofOfProfession;
    }

    public String getPartialRecoveryAmount() {
        return partialRecoveryAmount;
    }

    public void setPartialRecoveryAmount(String partialRecoveryAmount) {
        this.partialRecoveryAmount = partialRecoveryAmount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getEstimatedMonthlySales() {
        return estimatedMonthlySales;
    }

    public void setEstimatedMonthlySales(String estimatedMonthlySales) {
        this.estimatedMonthlySales = estimatedMonthlySales;
    }

    public String getTypeOfBusiness() {
        return typeOfBusiness;
    }

    public void setTypeOfBusiness(String typeOfBusiness) {
        this.typeOfBusiness = typeOfBusiness;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getiDType() {
        return iDType;
    }

    public void setiDType(String iDType) {
        this.iDType = iDType;
    }

    public String getIdN() {
        return idN;
    }

    public void setIdN(String idN) {
        this.idN = idN;
    }

    public String getTillID() {
        return tillID;
    }

    public void setTillID(String tillID) {
        this.tillID = tillID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public List<ThirdPartyData> getThirdPartyDataList() {
        return thirdPartyDataList;
    }

    public void setThirdPartyDataList(List<ThirdPartyData> thirdPartyDataList) {
        this.thirdPartyDataList = thirdPartyDataList;
    }

    public List<Charge> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<Charge> chargeList) {
        this.chargeList = chargeList;
    }

    public List<Events> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<Events> eventsList) {
        this.eventsList = eventsList;
    }

    public String getPrimaryLoanId() {
        return primaryLoanId;
    }

    public void setPrimaryLoanId(String primaryLoanId) {
        this.primaryLoanId = primaryLoanId;
    }

    public String getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(String outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(String disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<TotalRecurringCharge> getTotalRecurringChargeList() {
        return totalRecurringChargeList;
    }

    public void setTotalRecurringChargeList(List<TotalRecurringCharge> totalRecurringChargeList) {
        this.totalRecurringChargeList = totalRecurringChargeList;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public List<Milestones> getMilestonesList() {
        return milestonesList;
    }

    public void setMilestonesList(List<Milestones> milestonesList) {
        this.milestonesList = milestonesList;
    }

    public List<InterestAdjustment> getInterestAdjustmentsList() {
        return interestAdjustmentsList;
    }

    public void setInterestAdjustmentsList(List<InterestAdjustment> interestAdjustmentsList) {
        this.interestAdjustmentsList = interestAdjustmentsList;
    }

    public List<ChargeAdjustments> getChargeAdjustmentsList() {
        return chargeAdjustmentsList;
    }

    public void setChargeAdjustmentsList(List<ChargeAdjustments> chargeAdjustmentsList) {
        this.chargeAdjustmentsList = chargeAdjustmentsList;
    }

    public List<TotalOneOffCharges> getTotalOneOffChargesList() {
        return totalOneOffChargesList;
    }

    public void setTotalOneOffChargesList(List<TotalOneOffCharges> totalOneOffChargesList) {
        this.totalOneOffChargesList = totalOneOffChargesList;
    }

    public List<LoansPerState> getLoansPerStateList() {
        return loansPerStateList;
    }

    public void setLoansPerStateList(List<LoansPerState> loansPerStateList) {
        this.loansPerStateList = loansPerStateList;
    }

    public List<Loans> getLoansList() {
        return loansList;
    }

    public void setLoansList(List<Loans> loansList) {
        this.loansList = loansList;
    }

    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }

    public List<Repayment> getRepaymentList() {
        return repaymentList;
    }

    public void setRepaymentList(List<Repayment> repaymentList) {
        this.repaymentList = repaymentList;
    }

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }

    public List<MaturityDetails> getMaturityDetailsList() {
        return maturityDetailsList;
    }

    public void setMaturityDetailsList(List<MaturityDetails> maturityDetailsList) {
        this.maturityDetailsList = maturityDetailsList;
    }

    public List<LoanOffersByLoanProductGroup> getLoanOffersByLoanProductGroupList() {
        return loanOffersByLoanProductGroupList;
    }

    public void setLoanOffersByLoanProductGroupList(List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroupList) {
        this.loanOffersByLoanProductGroupList = loanOffersByLoanProductGroupList;
    }

    public List<EligibilityStatus> getEligibilityStatusList() {
        return eligibilityStatusList;
    }

    public void setEligibilityStatusList(List<EligibilityStatus> eligibilityStatusList) {
        this.eligibilityStatusList = eligibilityStatusList;
    }

    public List<LoansPerState> getLoansPerStates() {
        return loansPerStates;
    }

    public void setLoansPerStates(List<LoansPerState> loansPerStates) {
        this.loansPerStates = loansPerStates;
    }

    public String getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(String processingFee) {
        this.processingFee = processingFee;
    }

    public List<PeriodsProjection> getPeriodsProjectionList() {
        return periodsProjectionList;
    }

    public void setPeriodsProjectionList(List<PeriodsProjection> periodsProjectionList) {
        this.periodsProjectionList = periodsProjectionList;
    }

    public List<Interest> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<Interest> interestList) {
        this.interestList = interestList;
    }

    public List<LoanOffers> getLoanOffersList() {
        return loanOffersList;
    }

    public void setLoanOffersList(List<LoanOffers> loanOffersList) {
        this.loanOffersList = loanOffersList;
    }

    public List<OneOffCharges> getOneOffChargesList() {
        return oneOffChargesList;
    }

    public void setOneOffChargesList(List<OneOffCharges> oneOffChargesList) {
        this.oneOffChargesList = oneOffChargesList;
    }

    public List<OutstandingStatus> getOutstandingStatusList() {
        return outstandingStatusList;
    }

    public void setOutstandingStatusList(List<OutstandingStatus> outstandingStatusList) {
        this.outstandingStatusList = outstandingStatusList;
    }

    public List<RecurringCharges> getRecurringChargesList() {
        return recurringChargesList;
    }

    public void setRecurringChargesList(List<RecurringCharges> recurringChargesList) {
        this.recurringChargesList = recurringChargesList;
    }

    public Boolean getStatusFlag() {
        return isStatusFlag;
    }

    public void setStatusFlag(Boolean statusFlag) {
        isStatusFlag = statusFlag;
    }

    public List<DueDatePlan> getDueDatePlanList() {
        return dueDatePlanList;
    }

    public void setDueDatePlanList(List<DueDatePlan> dueDatePlanList) {
        this.dueDatePlanList = dueDatePlanList;
    }

    public List<LoanAmount> getLoanAmountList() {
        return loanAmountList;
    }

    public void setLoanAmountList(List<LoanAmount> loanAmountList) {
        this.loanAmountList = loanAmountList;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
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

    public Boolean getEligible() {
        return isEligible;
    }

    public void setEligible(Boolean eligible) {
        isEligible = eligible;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNoOfActiveAccounts() {
        return noOfActiveAccounts;
    }

    public void setNoOfActiveAccounts(String noOfActiveAccounts) {
        this.noOfActiveAccounts = noOfActiveAccounts;
    }

    public String getTotalOutstandingBalance() {
        return totalOutstandingBalance;
    }

    public void setTotalOutstandingBalance(String totalOutstandingBalance) {
        this.totalOutstandingBalance = totalOutstandingBalance;
    }

    public String getPlus3024m() {
        return plus3024m;
    }

    public void setPlus3024m(String plus3024m) {
        this.plus3024m = plus3024m;
    }

    public String getPlus6024m() {
        return plus6024m;
    }

    public void setPlus6024m(String plus6024m) {
        this.plus6024m = plus6024m;
    }

    public String getPlus9024m() {
        return plus9024m;
    }

    public void setPlus9024m(String plus9024m) {
        this.plus9024m = plus9024m;
    }

    public String getPlus12024m() {
        return plus12024m;
    }

    public void setPlus12024m(String plus12024m) {
        this.plus12024m = plus12024m;
    }

    public String getPlus15024m() {
        return plus15024m;
    }

    public void setPlus15024m(String plus15024m) {
        this.plus15024m = plus15024m;
    }

    public String getPlus18024m() {
        return plus18024m;
    }

    public void setPlus18024m(String plus18024m) {
        this.plus18024m = plus18024m;
    }

    public String getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(String writeOff) {
        this.writeOff = writeOff;
    }

    public String getDisclaimerText() {
        return disclaimerText;
    }

    public void setDisclaimerText(String disclaimerText) {
        this.disclaimerText = disclaimerText;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getLoanTimeStamp() {
        return loanTimeStamp;
    }

    public void setLoanTimeStamp(String loanTimeStamp) {
        this.loanTimeStamp = loanTimeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferClass() {
        return offerClass;
    }

    public void setOfferClass(String offerClass) {
        this.offerClass = offerClass;
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

    public String getLoanOffer() {
        return loanOffer;
    }

    public void setLoanOffer(String loanOffer) {
        this.loanOffer = loanOffer;
    }

    public String getSetUpFees() {
        return setUpFees;
    }

    public void setSetUpFees(String setUpFees) {
        this.setUpFees = setUpFees;
    }

    public String getRepayment() {
        return repayment;
    }

    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public String getRepaymentCounts() {
        return repaymentCounts;
    }

    public void setRepaymentCounts(String repaymentCounts) {
        this.repaymentCounts = repaymentCounts;
    }

    public String getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(String totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public String getPeriodIndex() {
        return periodIndex;
    }

    public void setPeriodIndex(String periodIndex) {
        this.periodIndex = periodIndex;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getPeriodStartTimestamp() {
        return periodStartTimestamp;
    }

    public void setPeriodStartTimestamp(String periodStartTimestamp) {
        this.periodStartTimestamp = periodStartTimestamp;
    }

    public String getPeriodEndTimestamp() {
        return periodEndTimestamp;
    }

    public void setPeriodEndTimestamp(String periodEndTimestamp) {
        this.periodEndTimestamp = periodEndTimestamp;
    }

    public String getPeriodStartDayOfLoanIndex() {
        return periodStartDayOfLoanIndex;
    }

    public void setPeriodStartDayOfLoanIndex(String periodStartDayOfLoanIndex) {
        this.periodStartDayOfLoanIndex = periodStartDayOfLoanIndex;
    }

    public String getPeriodEndDayOfLoanIndex() {
        return periodEndDayOfLoanIndex;
    }

    public void setPeriodEndDayOfLoanIndex(String periodEndDayOfLoanIndex) {
        this.periodEndDayOfLoanIndex = periodEndDayOfLoanIndex;
    }

    public String getTotalOneOffCharges() {
        return totalOneOffCharges;
    }

    public void setTotalOneOffCharges(String totalOneOffCharges) {
        this.totalOneOffCharges = totalOneOffCharges;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getChargeVAT() {
        return chargeVAT;
    }

    public void setChargeVAT(String chargeVAT) {
        this.chargeVAT = chargeVAT;
    }

    public String getPrincipalFrom() {
        return principalFrom;
    }

    public void setPrincipalFrom(String principalFrom) {
        this.principalFrom = principalFrom;
    }

    public String getPrincipalTo() {
        return principalTo;
    }

    public void setPrincipalTo(String principalTo) {
        this.principalTo = principalTo;
    }

    public String getMaturityDetails() {
        return maturityDetails;
    }

    public void setMaturityDetails(String maturityDetails) {
        this.maturityDetails = maturityDetails;
    }

    public String getMaturityDuration() {
        return maturityDuration;
    }

    public void setMaturityDuration(String maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getInterestValue() {
        return interestValue;
    }

    public void setInterestValue(String interestValue) {
        this.interestValue = interestValue;
    }

    public String getDaysOffset() {
        return daysOffset;
    }

    public void setDaysOffset(String daysOffset) {
        this.daysOffset = daysOffset;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getOneOffCharges() {
        return oneOffCharges;
    }

    public void setOneOffCharges(String oneOffCharges) {
        this.oneOffCharges = oneOffCharges;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    public String getPeriodsProjections() {
        return periodsProjections;
    }

    public void setPeriodsProjections(String periodsProjections) {
        this.periodsProjections = periodsProjections;
    }

    public String getPeriodStartTimemp() {
        return periodStartTimemp;
    }

    public void setPeriodStartTimemp(String periodStartTimemp) {
        this.periodStartTimemp = periodStartTimemp;
    }

    public String getMilestones() {
        return milestones;
    }

    public void setMilestones(String milestones) {
        this.milestones = milestones;
    }

    public String getDayOfLoan() {
        return dayOfLoan;
    }

    public void setDayOfLoan(String dayOfLoan) {
        this.dayOfLoan = dayOfLoan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInterestAdjustment() {
        return interestAdjustment;
    }

    public void setInterestAdjustment(String interestAdjustment) {
        this.interestAdjustment = interestAdjustment;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getOutstandingPerCurrency() {
        return outstandingPerCurrency;
    }

    public void setOutstandingPerCurrency(String outstandingPerCurrency) {
        this.outstandingPerCurrency = outstandingPerCurrency;
    }

    public String getNumOutstandingLoans() {
        return numOutstandingLoans;
    }

    public void setNumOutstandingLoans(String numOutstandingLoans) {
        this.numOutstandingLoans = numOutstandingLoans;
    }

    public String getTotalPendingLoans() {
        return totalPendingLoans;
    }

    public void setTotalPendingLoans(String totalPendingLoans) {
        this.totalPendingLoans = totalPendingLoans;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTypeStatus() {
        return eventTypeStatus;
    }

    public void setEventTypeStatus(String eventTypeStatus) {
        this.eventTypeStatus = eventTypeStatus;
    }

    public String getEventTransactionId() {
        return eventTransactionId;
    }

    public void setEventTransactionId(String eventTransactionId) {
        this.eventTransactionId = eventTransactionId;
    }

    public String getThirdPartyTransactionId() {
        return thirdPartyTransactionId;
    }

    public void setThirdPartyTransactionId(String thirdPartyTransactionId) {
        this.thirdPartyTransactionId = thirdPartyTransactionId;
    }

    public String getEventReason() {
        return eventReason;
    }

    public void setEventReason(String eventReason) {
        this.eventReason = eventReason;
    }

    public String getEventReasonDetails() {
        return eventReasonDetails;
    }

    public void setEventReasonDetails(String eventReasonDetails) {
        this.eventReasonDetails = eventReasonDetails;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPeriodExpirationTimestamp() {
        return periodExpirationTimestamp;
    }

    public void setPeriodExpirationTimestamp(String periodExpirationTimestamp) {
        this.periodExpirationTimestamp = periodExpirationTimestamp;
    }

    public String getPrincipalAdjustment() {
        return principalAdjustment;
    }

    public void setPrincipalAdjustment(String principalAdjustment) {
        this.principalAdjustment = principalAdjustment;
    }

    public String getPrincipalBefore() {
        return principalBefore;
    }

    public void setPrincipalBefore(String principalBefore) {
        this.principalBefore = principalBefore;
    }

    public String getPrincipalAfter() {
        return principalAfter;
    }

    public void setPrincipalAfter(String principalAfter) {
        this.principalAfter = principalAfter;
    }

    public String getSetupFeesAdjustment() {
        return setupFeesAdjustment;
    }

    public void setSetupFeesAdjustment(String setupFeesAdjustment) {
        this.setupFeesAdjustment = setupFeesAdjustment;
    }

    public String getSetupFeesBefore() {
        return setupFeesBefore;
    }

    public void setSetupFeesBefore(String setupFeesBefore) {
        this.setupFeesBefore = setupFeesBefore;
    }

    public String getSetupFeesAfter() {
        return setupFeesAfter;
    }

    public void setSetupFeesAfter(String setupFeesAfter) {
        this.setupFeesAfter = setupFeesAfter;
    }

    public String getInterestAdjustmentVAT() {
        return interestAdjustmentVAT;
    }

    public void setInterestAdjustmentVAT(String interestAdjustmentVAT) {
        this.interestAdjustmentVAT = interestAdjustmentVAT;
    }

    public String getInterestBefore() {
        return interestBefore;
    }

    public void setInterestBefore(String interestBefore) {
        this.interestBefore = interestBefore;
    }

    public String getInterestAfter() {
        return interestAfter;
    }

    public void setInterestAfter(String interestAfter) {
        this.interestAfter = interestAfter;
    }

    public String getTotalChargesAfter() {
        return totalChargesAfter;
    }

    public void setTotalChargesAfter(String totalChargesAfter) {
        this.totalChargesAfter = totalChargesAfter;
    }

    public String getTotalChargesAdjustment() {
        return totalChargesAdjustment;
    }

    public void setTotalChargesAdjustment(String totalChargesAdjustment) {
        this.totalChargesAdjustment = totalChargesAdjustment;
    }

    public String getTotalChargesAdjustmentVAT() {
        return totalChargesAdjustmentVAT;
    }

    public void setTotalChargesAdjustmentVAT(String totalChargesAdjustmentVAT) {
        this.totalChargesAdjustmentVAT = totalChargesAdjustmentVAT;
    }

    public String getTotalChargesBefore() {
        return totalChargesBefore;
    }

    public void setTotalChargesBefore(String totalChargesBefore) {
        this.totalChargesBefore = totalChargesBefore;
    }

    public String getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(String eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getReceptionTimestamp() {
        return receptionTimestamp;
    }

    public void setReceptionTimestamp(String receptionTimestamp) {
        this.receptionTimestamp = receptionTimestamp;
    }

    public String getProcessingTimestamp() {
        return processingTimestamp;
    }

    public void setProcessingTimestamp(String processingTimestamp) {
        this.processingTimestamp = processingTimestamp;
    }

    public String getLoanReasonDetails() {
        return loanReasonDetails;
    }

    public void setLoanReasonDetails(String loanReasonDetails) {
        this.loanReasonDetails = loanReasonDetails;
    }

    public String getLoanInfo() {
        return loanInfo;
    }

    public void setLoanInfo(String loanInfo) {
        this.loanInfo = loanInfo;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(String outstanding) {
        this.outstanding = outstanding;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanTimestamp() {
        return LoanTimestamp;
    }

    public void setLoanTimestamp(String loanTimestamp) {
        LoanTimestamp = loanTimestamp;
    }

    public String getLoanReason() {
        return loanReason;
    }

    public void setLoanReason(String loanReason) {
        this.loanReason = loanReason;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
    }

    public String getSetupFees() {
        return setupFees;
    }

    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
    }

    public String getLoanPlanId() {
        return loanPlanId;
    }

    public void setLoanPlanId(String loanPlanId) {
        this.loanPlanId = loanPlanId;
    }

    public String getLoanPlanName() {
        return loanPlanName;
    }

    public void setLoanPlanName(String loanPlanName) {
        this.loanPlanName = loanPlanName;
    }

    public String getLoanProductGroup() {
        return loanProductGroup;
    }

    public void setLoanProductGroup(String loanProductGroup) {
        this.loanProductGroup = loanProductGroup;
    }

    public String getRepaymentsCount() {
        return repaymentsCount;
    }

    public void setRepaymentsCount(String repaymentsCount) {
        this.repaymentsCount = repaymentsCount;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestVAT() {
        return interestVAT;
    }

    public void setInterestVAT(String interestVAT) {
        this.interestVAT = interestVAT;
    }

    public String getChargesVAT() {
        return chargesVAT;
    }

    public void setChargesVAT(String chargesVAT) {
        this.chargesVAT = chargesVAT;
    }

    public String getTotalGross() {
        return totalGross;
    }

    public void setTotalGross(String totalGross) {
        this.totalGross = totalGross;
    }

    public String getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(String totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public String getTotalSetupFees() {
        return totalSetupFees;
    }

    public void setTotalSetupFees(String totalSetupFees) {
        this.totalSetupFees = totalSetupFees;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getTotalInterestVAT() {
        return totalInterestVAT;
    }

    public void setTotalInterestVAT(String totalInterestVAT) {
        this.totalInterestVAT = totalInterestVAT;
    }

    public String getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    public String getTotalChargesVAT() {
        return totalChargesVAT;
    }

    public void setTotalChargesVAT(String totalChargesVAT) {
        this.totalChargesVAT = totalChargesVAT;
    }

    public String getTotalPendingRecoveries() {
        return totalPendingRecoveries;
    }

    public void setTotalPendingRecoveries(String totalPendingRecoveries) {
        this.totalPendingRecoveries = totalPendingRecoveries;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getDaysLeftInPeriod() {
        return daysLeftInPeriod;
    }

    public void setDaysLeftInPeriod(String daysLeftInPeriod) {
        this.daysLeftInPeriod = daysLeftInPeriod;
    }

    public String getNextPeriod() {
        return nextPeriod;
    }

    public void setNextPeriod(String nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdentityValue() {
        return identityValue;
    }

    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
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

    public String getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public void setReceivedTimestamp(String receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    public String getInternalLoanId() {
        return internalLoanId;
    }

    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    public String getAdvanceOfferId() {
        return advanceOfferId;
    }

    public void setAdvanceOfferId(String advanceOfferId) {
        this.advanceOfferId = advanceOfferId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExternalLoanId() {
        return externalLoanId;
    }

    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getFed() {
        return fed;
    }

    public void setFed(String fed) {
        this.fed = fed;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(String walletStatus) {
        this.walletStatus = walletStatus;
    }

    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(String taxRegime) {
        this.taxRegime = taxRegime;
    }

    public String getLienStatus() {
        return lienStatus;
    }

    public void setLienStatus(String lienStatus) {
        this.lienStatus = lienStatus;
    }

    public String getShaCnic() {
        return shaCnic;
    }

    public void setShaCnic(String shaCnic) {
        this.shaCnic = shaCnic;
    }

    public String getFatherCnicIssuanceDate() {
        return fatherCnicIssuanceDate;
    }

    public void setFatherCnicIssuanceDate(String fatherCnicIssuanceDate) {
        this.fatherCnicIssuanceDate = fatherCnicIssuanceDate;
    }

    public String getFatherCnicExpiryDate() {
        return fatherCnicExpiryDate;
    }

    public void setFatherCnicExpiryDate(String fatherCnicExpiryDate) {
        this.fatherCnicExpiryDate = fatherCnicExpiryDate;
    }

    public String getMotherCnic() {
        return motherCnic;
    }

    public void setMotherCnic(String motherCnic) {
        this.motherCnic = motherCnic;
    }

    public String getFatherMotherMobileNumber() {
        return fatherMotherMobileNumber;
    }

    public void setFatherMotherMobileNumber(String fatherMotherMobileNumber) {
        this.fatherMotherMobileNumber = fatherMotherMobileNumber;
    }

    public String getFatherCnic() {
        return fatherCnic;
    }

    public void setFatherCnic(String fatherCnic) {
        this.fatherCnic = fatherCnic;
    }

    public String getMinorCustomerPic() {
        return minorCustomerPic;
    }

    public void setMinorCustomerPic(String minorCustomerPic) {
        this.minorCustomerPic = minorCustomerPic;
    }

    public String getbFormPic() {
        return bFormPic;
    }

    public void setbFormPic(String bFormPic) {
        this.bFormPic = bFormPic;
    }

    public String getParentCnicPic() {
        return parentCnicPic;
    }

    public void setParentCnicPic(String parentCnicPic) {
        this.parentCnicPic = parentCnicPic;
    }

    public String getSnicPic() {
        return snicPic;
    }

    public void setSnicPic(String snicPic) {
        this.snicPic = snicPic;
    }

    public String getsNicBackPic() {
        return sNicBackPic;
    }

    public void setsNicBackPic(String sNicBackPic) {
        this.sNicBackPic = sNicBackPic;
    }

    public String getParentNicBackPic() {
        return ParentNicBackPic;
    }

    public void setParentNicBackPic(String parentNicBackPic) {
        ParentNicBackPic = parentNicBackPic;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getClsComment() {
        return clsComment;
    }

    public void setClsComment(String clsComment) {
        this.clsComment = clsComment;
    }

    public String getSourceOfIncomePic() {
        return sourceOfIncomePic;
    }

    public void setSourceOfIncomePic(String sourceOfIncomePic) {
        this.sourceOfIncomePic = sourceOfIncomePic;
    }

    public String getStockTrading() {
        return stockTrading;
    }

    public void setStockTrading(String stockTrading) {
        this.stockTrading = stockTrading;
    }

    public String getMutulaFunds() {
        return mutulaFunds;
    }

    public void setMutulaFunds(String mutulaFunds) {
        this.mutulaFunds = mutulaFunds;
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

    public String getAgentNetwork() {
        return agentNetwork;
    }

    public void setAgentNetwork(String agentNetwork) {
        this.agentNetwork = agentNetwork;
    }

    public String getDailyCreditLimit() {
        return dailyCreditLimit;
    }

    public void setDailyCreditLimit(String dailyCreditLimit) {
        this.dailyCreditLimit = dailyCreditLimit;
    }

    public String getMonthlyCreditLimit() {
        return monthlyCreditLimit;
    }

    public void setMonthlyCreditLimit(String monthlyCreditLimit) {
        this.monthlyCreditLimit = monthlyCreditLimit;
    }

    public String getDailyDebitLimit() {
        return dailyDebitLimit;
    }

    public void setDailyDebitLimit(String dailyDebitLimit) {
        this.dailyDebitLimit = dailyDebitLimit;
    }

    public String getMonthlyDebitLimit() {
        return monthlyDebitLimit;
    }

    public void setMonthlyDebitLimit(String monthlyDebitLimit) {
        this.monthlyDebitLimit = monthlyDebitLimit;
    }

    public String getYearlyDebitLimit() {
        return yearlyDebitLimit;
    }

    public void setYearlyDebitLimit(String yearlyDebitLimit) {
        this.yearlyDebitLimit = yearlyDebitLimit;
    }

    public String getYearlyCreditLimit() {
        return yearlyCreditLimit;
    }

    public void setYearlyCreditLimit(String yearlyCreditLimit) {
        this.yearlyCreditLimit = yearlyCreditLimit;
    }

    public List<CatalogList> getCatalogNames() {
        return catalogNames;
    }

    public void setCatalogNames(List<CatalogList> catalogNames) {
        this.catalogNames = catalogNames;
    }

    public List<SegmentList> getSegmentNames() {
        return segmentNames;
    }

    public void setSegmentNames(List<SegmentList> segmentNames) {
        this.segmentNames = segmentNames;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getBvsRequest() {
        return bvsRequest;
    }

    public void setBvsRequest(String bvsRequest) {
        this.bvsRequest = bvsRequest;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getIsOtpRequest() {
        return isOtpRequest;
    }

    public void setIsOtpRequest(String isOtpRequest) {
        this.isOtpRequest = isOtpRequest;
    }

    public String getHraLinkedRequest() {
        return hraLinkedRequest;
    }

    public void setHraLinkedRequest(String hraLinkedRequest) {
        this.hraLinkedRequest = hraLinkedRequest;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(String numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getEarlyPaymentCharges() {
        return earlyPaymentCharges;
    }

    public void setEarlyPaymentCharges(String earlyPaymentCharges) {
        this.earlyPaymentCharges = earlyPaymentCharges;
    }

    public String getLatePaymentCharges() {
        return latePaymentCharges;
    }

    public void setLatePaymentCharges(String latePaymentCharges) {
        this.latePaymentCharges = latePaymentCharges;
    }

    public String getBranchlessAccountId() {
        return branchlessAccountId;
    }

    public void setBranchlessAccountId(String branchlessAccountId) {
        this.branchlessAccountId = branchlessAccountId;
    }

    public String getCoreAccountId() {
        return coreAccountId;
    }

    public void setCoreAccountId(String coreAccountId) {
        this.coreAccountId = coreAccountId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getIsReceiveCash() {
        return isReceiveCash;
    }

    public void setIsReceiveCash(String isReceiveCash) {
        this.isReceiveCash = isReceiveCash;
    }

    public String getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(String isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public String getCustomerRegState() {
        return customerRegState;
    }

    public void setCustomerRegState(String customerRegState) {
        this.customerRegState = customerRegState;
    }

    public String getCustomerRegStateId() {
        return customerRegStateId;
    }

    public void setCustomerRegStateId(String customerRegStateId) {
        this.customerRegStateId = customerRegStateId;
    }

    public String getPresentCity() {
        return presentCity;
    }

    public void setPresentCity(String presentCity) {
        this.presentCity = presentCity;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPermanentCity() {
        return permanentCity;
    }

    public void setPermanentCity(String permanentCity) {
        this.permanentCity = permanentCity;
    }

    public String getIsCnicSeen() {
        return isCnicSeen;
    }

    public void setIsCnicSeen(String isCnicSeen) {
        this.isCnicSeen = isCnicSeen;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getIsBVSAccount() {
        return isBVSAccount;
    }

    public void setIsBVSAccount(String isBVSAccount) {
        this.isBVSAccount = isBVSAccount;
    }

    public String getIsHRA() {
        return isHRA;
    }

    public void setIsHRA(String isHRA) {
        this.isHRA = isHRA;
    }

    public String getTransactionPurpose() {
        return transactionPurpose;
    }

    public void setTransactionPurpose(String transactionPurpose) {
        this.transactionPurpose = transactionPurpose;
    }

    public String getOrgLocation1() {
        return orgLocation1;
    }

    public void setOrgLocation1(String orgLocation1) {
        this.orgLocation1 = orgLocation1;
    }

    public String getOrgLocation2() {
        return orgLocation2;
    }

    public void setOrgLocation2(String orgLocation2) {
        this.orgLocation2 = orgLocation2;
    }

    public String getOrgLocation3() {
        return orgLocation3;
    }

    public void setOrgLocation3(String orgLocation3) {
        this.orgLocation3 = orgLocation3;
    }

    public String getOrgLocation4() {
        return orgLocation4;
    }

    public void setOrgLocation4(String orgLocation4) {
        this.orgLocation4 = orgLocation4;
    }

    public String getOrgLocation5() {
        return orgLocation5;
    }

    public void setOrgLocation5(String orgLocation5) {
        this.orgLocation5 = orgLocation5;
    }

    public String getOrgRelation1() {
        return orgRelation1;
    }

    public void setOrgRelation1(String orgRelation1) {
        this.orgRelation1 = orgRelation1;
    }

    public String getOrgRelation2() {
        return orgRelation2;
    }

    public void setOrgRelation2(String orgRelation2) {
        this.orgRelation2 = orgRelation2;
    }

    public String getOrgRelation3() {
        return orgRelation3;
    }

    public void setOrgRelation3(String orgRelation3) {
        this.orgRelation3 = orgRelation3;
    }

    public String getOrgRelation4() {
        return orgRelation4;
    }

    public void setOrgRelation4(String orgRelation4) {
        this.orgRelation4 = orgRelation4;
    }

    public String getOrgRelation5() {
        return orgRelation5;
    }

    public void setOrgRelation5(String orgRelation5) {
        this.orgRelation5 = orgRelation5;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getGLAccountNo1() {
        return GLAccountNo1;
    }

    public void setGLAccountNo1(String GLAccountNo1) {
        this.GLAccountNo1 = GLAccountNo1;
    }

    public String getGLAccountNo2() {
        return GLAccountNo2;
    }

    public void setGLAccountNo2(String GLAccountNo2) {
        this.GLAccountNo2 = GLAccountNo2;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getPurposeOfAccount() {
        return PurposeOfAccount;
    }

    public void setPurposeOfAccount(String purposeOfAccount) {
        PurposeOfAccount = purposeOfAccount;
    }

    public String getKINName() {
        return KINName;
    }

    public void setKINName(String KINName) {
        this.KINName = KINName;
    }

    public String getKINMobileNumber() {
        return KINMobileNumber;
    }

    public void setKINMobileNumber(String KINMobileNumber) {
        this.KINMobileNumber = KINMobileNumber;
    }

    public String getKINCNIC() {
        return KINCNIC;
    }

    public void setKINCNIC(String KINCNIC) {
        this.KINCNIC = KINCNIC;
    }

    public String getKINRelation() {
        return KINRelation;
    }

    public void setKINRelation(String KINRelation) {
        this.KINRelation = KINRelation;
    }

    public String getInternationalRemittanceLocation() {
        return InternationalRemittanceLocation;
    }

    public void setInternationalRemittanceLocation(String internationalRemittanceLocation) {
        InternationalRemittanceLocation = internationalRemittanceLocation;
    }

    public String getOriginatorLocation() {
        return OriginatorLocation;
    }

    public void setOriginatorLocation(String originatorLocation) {
        OriginatorLocation = originatorLocation;
    }

    public String getSourceOfIncome() {
        return SourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        SourceOfIncome = sourceOfIncome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getReceiverCNIC() {
        return receiverCNIC;
    }

    public void setReceiverCNIC(String receiverCNIC) {
        this.receiverCNIC = receiverCNIC;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getChallanType() {
        return challanType;
    }

    public void setChallanType(String challanType) {
        this.challanType = challanType;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getPinType() {
        return pinType;
    }

    public void setPinType(String pinType) {
        this.pinType = pinType;
    }

    public String getChallanAmount() {
        return challanAmount;
    }

    public void setChallanAmount(String challanAmount) {
        this.challanAmount = challanAmount;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBenificieryIban() {
        return BenificieryIban;
    }

    public void setBenificieryIban(String benificieryIban) {
        BenificieryIban = benificieryIban;
    }

    public String getSenderMobileNumber() {
        return SenderMobileNumber;
    }

    public void setSenderMobileNumber(String senderMobileNumber) {
        SenderMobileNumber = senderMobileNumber;
    }

    public String getDestinationAccountNumber() {
        return DestinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        DestinationAccountNumber = destinationAccountNumber;
    }

    public String getSourceBankImd() {
        return sourceBankImd;
    }

    public void setSourceBankImd(String sourceBankImd) {
        this.sourceBankImd = sourceBankImd;
    }

    public String getDestinationBankImd() {
        return DestinationBankImd;
    }

    public void setDestinationBankImd(String destinationBankImd) {
        DestinationBankImd = destinationBankImd;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRecieverAccountTilte() {
        return recieverAccountTilte;
    }

    public void setRecieverAccountTilte(String recieverAccountTilte) {
        this.recieverAccountTilte = recieverAccountTilte;
    }

    public String getSenderAccountTitle() {
        return senderAccountTitle;
    }

    public void setSenderAccountTitle(String senderAccountTitle) {
        this.senderAccountTitle = senderAccountTitle;
    }

    public String getRecieverAccountNumber() {
        return recieverAccountNumber;
    }

    public void setRecieverAccountNumber(String recieverAccountNumber) {
        this.recieverAccountNumber = recieverAccountNumber;
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

    public String getCustomerMobileNetwork() {
        return customerMobileNetwork;
    }

    public void setCustomerMobileNetwork(String customerMobileNetwork) {
        this.customerMobileNetwork = customerMobileNetwork;
    }

    public String getAgentMobileNumber() {
        return agentMobileNumber;
    }

    public void setAgentMobileNumber(String agentMobileNumber) {
        this.agentMobileNumber = agentMobileNumber;
    }

    public String getAgentMpin() {
        return agentMpin;
    }

    public void setAgentMpin(String agentMpin) {
        this.agentMpin = agentMpin;
    }

    public List<Map<String, String>> getTransactionsMap() {
        return transactionsMap;
    }

    public void setTransactionsMap(List<Map<String, String>> transactionsMap) {
        this.transactionsMap = transactionsMap;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getTransactionLocalDate() {
        return transactionLocalDate;
    }

    public void setTransactionLocalDate(String transactionLocalDate) {
        this.transactionLocalDate = transactionLocalDate;
    }

    public String getTransactionLocalTime() {
        return transactionLocalTime;
    }

    public void setTransactionLocalTime(String transactionLocalTime) {
        this.transactionLocalTime = transactionLocalTime;
    }

    public String getRemainingDebitLimit() {
        return remainingDebitLimit;
    }

    public void setRemainingDebitLimit(String remainingDebitLimit) {
        this.remainingDebitLimit = remainingDebitLimit;
    }

    public String getRemainingCreditLimit() {
        return remainingCreditLimit;
    }

    public void setRemainingCreditLimit(String remainingCreditLimit) {
        this.remainingCreditLimit = remainingCreditLimit;
    }

    public String getConsumedVelocity() {
        return consumedVelocity;
    }

    public void setConsumedVelocity(String consumedVelocity) {
        this.consumedVelocity = consumedVelocity;
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

    public String getReserved6() {
        return reserved6;
    }

    public void setReserved6(String reserved6) {
        this.reserved6 = reserved6;
    }

    public String getReserved7() {
        return reserved7;
    }

    public void setReserved7(String reserved7) {
        this.reserved7 = reserved7;
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

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.PaymentMode = paymentMode;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
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

    public String getResponseCodeDescription() {
        return responseCodeDescription;
    }

    public void setResponseCodeDescription(String responseCodeDescription) {
        this.responseCodeDescription = responseCodeDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getMobilePin() {
        return mobilePin;
    }

    public void setMobilePin(String mobilePin) {
        this.mobilePin = mobilePin;
    }

    public String getLoginPin() {
        return loginPin;
    }

    public void setLoginPin(String loginPin) {
        this.loginPin = loginPin;
    }

    public String getOldLoginPin() {
        return oldLoginPin;
    }

    public void setOldLoginPin(String oldLoginPin) {
        this.oldLoginPin = oldLoginPin;
    }

    public String getNewLoginPin() {
        return newLoginPin;
    }

    public void setNewLoginPin(String newLoginPin) {
        this.newLoginPin = newLoginPin;
    }

    public String getConfirmLoginPin() {
        return confirmLoginPin;
    }

    public void setConfirmLoginPin(String confirmLoginPin) {
        this.confirmLoginPin = confirmLoginPin;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getOtpPin() {
        return otpPin;
    }

    public void setOtpPin(String otpPin) {
        this.otpPin = otpPin;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public String getOtpPurpose() {
        return otpPurpose;
    }

    public void setOtpPurpose(String otpPurpose) {
        this.otpPurpose = otpPurpose;
    }

    public String getResponseContentXML() {
        return responseContentXML;
    }

    public void setResponseContentXML(String responseContentXML) {
        this.responseContentXML = responseContentXML;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransactionProcessingAmount() {
        return transactionProcessingAmount;
    }

    public void setTransactionProcessingAmount(String transactionProcessingAmount) {
        this.transactionProcessingAmount = transactionProcessingAmount;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getConsumerMobileNo() {
        return consumerMobileNo;
    }

    public void setConsumerMobileNo(String consumerMobileNo) {
        this.consumerMobileNo = consumerMobileNo;
    }

    public String getLateBillAmount() {
        return lateBillAmount;
    }

    public void setLateBillAmount(String lateBillAmount) {
        this.lateBillAmount = lateBillAmount;
    }

    public String getBillPaid() {
        return billPaid;
    }

    public void setBillPaid(String billPaid) {
        this.billPaid = billPaid;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getOverDue() {
        return overDue;
    }

    public void setOverDue(String overDue) {
        this.overDue = overDue;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getOldMpin() {
        return oldMpin;
    }

    public void setOldMpin(String oldMpin) {
        this.oldMpin = oldMpin;
    }

    public String getNewMpin() {
        return newMpin;
    }

    public void setNewMpin(String newMpin) {
        this.newMpin = newMpin;
    }

    public String getConfirmMpin() {
        return confirmMpin;
    }

    public void setConfirmMpin(String confirmMpin) {
        this.confirmMpin = confirmMpin;
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

    public String getReceiverMobileNumber() {
        return receiverMobileNumber;
    }

    public void setReceiverMobileNumber(String receiverMobileNumber) {
        this.receiverMobileNumber = receiverMobileNumber;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
