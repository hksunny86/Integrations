package com.inov8.integration.i8sb.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.i8sb.vo.CardDetailVO;
import com.inov8.integration.vo.StatementVo;
import com.inov8.integration.webservice.optasiaVO.*;
import com.inov8.integration.webservice.raastVO.IDs;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by inov8 on 8/30/2017.
 */
@XStreamAlias("I8SBSwitchControllerResponseVO")
public class I8SBSwitchControllerResponseVO implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    private String messageType;
    private String tranCode;
    private String transmissionDateAndTime;
    private String transactionId;
    private String STAN;
    private String requestCode;
    private String responseCode;
    private String PAN;
    private String cardTitle;
    private String CNIC;
    private String email;
    private String mobilePhone;
    private String relationshipNumber;
    private String overallDefaultAccount;
    private String customerName;
    private String motherMaidenName;
    private String dateOfBirth;
    private String userId;
    private String forcePasswordChange;
    private String password;
    private String channelStatus;
    private String MPINExpiry;
    private String passwordBitmap;
    private String MPIN;
    private String FPIN;
    private String gender;
    private String homeAddress;
    private String officeAddress;
    private String homePhone;
    private String officePhone;
    private String accessToken;
    private String voucherCode;
    private String AuthToken;
    private String statusFlag;
    private String userName;
    private int vehicleAssesmentNo;
    private String registrationNumber;
    private String registrationDate;
    private String chassisNo;
    private String makerMake;
    private String catagory;
    private String bodyType;
    private int engCapacity;
    private int seats;
    private int cyllinders;
    private String ownerName;
    private String ownerCnic;
    private String filerStatus;
    private String taxPaidFrom;
    private String taxPaidUpto;
    private String vehTaxPaidLifeTime;
    private String vehicleStatus;
    private String fitnessDate;
    private String assesmentDate;
    private String vctChallanNumber;
    private String challanDate;
    private String challanStatus;
    private int paymentRecievedBy;
    private String paymentDate;
    private BigDecimal totalAmount;
    private List<AccountStatementData> data;
    private List<CardVO> cardVOList;
    private String totalSize;

    private List<?> internationalTransactionList;
    private String responseData;
    private List<StatementVo> statementVos;
    private String currency;
    private String isPartialPayment;
    private String destinationBankImd;
    private String statusDesc;
    private String hostCode;
    private String hostDesc;
    private String ticketStatus;
    private String OrderRefId;
    private String Type;

    private String MTI;
    private String processingCode;
    private String systemsTraceAuditNumber;
    private String timeLocalTransaction;
    private String dateLocalTransaction;
    private String merchantType;
    private String CIF;
    private String debitCardNumber;
    private String CardStatus;
    private String CardStatusCode;
    private String traceNo;
    private String dateTime;
    private String creationDate;
    //    private String accountNumber;
    private List<AssociatedAccounts> associatedAccounts;
    private List<HealthAndNutritionContent> content;
    private String message;
    private List<ManualTransactionVo> manualTransactionListVo;
    //    private List<MiniStatement> miniStatementList;
//    private List<UserAccounts> userAccountList;
//    private List<CheckingAccountSummary> checkingAccountSummaryList;
//    private List<LoanAccountSummary> loanAccountSummaryList;
//    private List<TDRAccountSummary> TDRAccSummaryList;
//    private List<OneLinkBank> oneLinkBankList;
//    private List<BillingCompany> billingCompanyList;
    private String filePath;
    private String accountBalance;
    //    private List<Card> cardList;
//    private List<LinkedAcc> linkedAccList;
//    private List<Transaction> transactionsList;
    private String accountId1;
    private String accountId2;
    private String accountTitle;
    private String branchName;
    //    private List<BeneficiaryAcct> beneficiaryAcctList;
//    private List<IBFTBenAcc> IBFTBenAccList;
//    private List<ConsumerData> consumerDataList;
    private String consumerTitle;
    private String billAmount;
    private String dueDate;
    private String billAmountAfterDueDate;
    private String billStatus;
    private String serviceCharges;
    private String amount;
    private String receiptNo;
    //    private List<Instrument> instrumentList;
//    private List<StopCheque> stopChequeList;
    private String requestingTranCode;
    private String transactionFees;
    //    private List<CreditCard> creditCardList;
//    private List<PrepaidCard> prepaidCardList;
    private String cardType;
    private String accountType;
    private AccountStatement accountStatement;
    private TDRAccountStatement tdrAccountStatement;
    private String CCStatusCode;
    private String outStandingAmount;
    private String minAmount;
    //    private List<CCMiniStmt> CCMiniStatementList;
    private String bankName;
    private String responseXML;
    private String status;
    private String error;
    private List<I8SBSwitchControllerResponseVO> i8SBSwitchControllerResponseVOList = new ArrayList<I8SBSwitchControllerResponseVO>();
    private String startDate;
    private String frequency;
    private String totalNumberOfExecution;
    private String SIID;
    private String SITranCode;
    private String visaTxnIdentifier;
    private String visaApprovalCode;
    private String visaActionCode;
    private String merchantVerificationValue;
    private String merchantId;
    private String merchantName;
    private String P2M_ID;
    private String billingMonth;
    private String datePaid;
    private String consumerDetail;
    private String amountWithinDueDate;
    private String amountAfterDueDate;
    private String amountPaid;
    private String tranAuthID;
    private String reserved;
    private String returnedString;
    private String IBAN;
    private String branchCode;
    private String billName;
    private String consumerNumber;
    private String companyCode;
    private String description;
    private String result;
    private String availableBalance;
    private String availableLimit;
    //    public List<CardVO> getCardVOList() {
//        return cardVOList;
//    }
//    public List<CardVO> getCardVOList() {
//        return cardVOList;
//    }
//
    private String sessionId;
    private String customerAccNumber;
    private String transactionNumber;
    private String sessionIdNadra;
    private String fingerIndex;
    private String fingerTempelete;
    private String walletId;
    private String taxpaid;
    private String uSSDResponseString;
    private String uSSDAction;
    private String codingScheme;
    private String transactionDate;
    private String transactionDecs;
    private String instrumentCode;
    private String debitAtmTrans;
    private String creditAtmTrans;
    private String remainingBalance;
    private String transactionReferenceNumber;
    private String particular;
    private String debitAmount;
    private String creditAmount;
    private String closingBalance;
    private String statementDate;
    private String statementPeriod;
    private String branchAddress;
    private String accountDesc;
    private String customerAccountNumber;
    private String customerBranchCode;
    private String withdrawlAmount;
    private String fatherName;
    private String ibanAccountNumber;
    private String address1;
    private String address2;
    private String address3;
    private String prevBalance;
    private String fromDate;
    private String toDate;
    private String companyName;
    private String billCategoryCode;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String Reserved1;
    private String Reserved2;
    private String Reserved3;
    private String Reserved4;
    private String bankIMD;
    private String temporaryLimitFee;
    private String profileFee;
    private String walletAccountId;
    private String walletBalance;
    private String sessionIDBAFL;
    private String mobileWalletNumber;
    private String terminalId;
    private String nadraSessionId;
    private String tehsil;
    private String district;
    private String dob;
    private String bvsStatus;
    private String cnicExpiry;
    private String pitb;
    private String sumAmount;
    private String avgAmount;
    private String nTransaction;
    private String toBankImd;
    private String accountNumberMasked;
    private String accquiringBin;
    private String cardAcceptorIdCode;
    private String cardAcceptorName;
    private String feeProgramIndicator;
    private String statusCode;
    private String transactionIdentifier;
    private String transactionType;
    private String rrn;
    private String transactionCurrencyCode;
    private String transactiontime;
    private String cardAcceptorAddressCity;
    private String cardAcceptorAddressCountry;
    private String orignalActionCode;
    private String merchantCategoryCode;
    private String purchaseIdentifierRefrenceNumber;
    private String purchaceIdentifierType;
    private String givePreviouslyUpdatedFlag;
    private String giveUpdatedFlag;
    private String settlementResponsibilityFlag;
    private String settlementServiceFlag;
    private String actionCode;
    private String QrResponseCode;
    private String smsText;
    private String lastFingerIndex;
    private String tempeleteType;
    private String areaName;
    private String productCode;

    private String requestId;
    private String caseId;
    private String caseStatus;
    private String importStatus;
    private String isHit;
    private String screeningStatus;
    private String totalCWL;
    private String totalGWL;
    private String totalPEPEDD;
    private String totalPrivate;
    private String messageID;
    private String shortCode;
    private List<CardDetailVO> cardDetailList;
    private String timeStamp;
    private String resultMessage;
    private String serviceType;
    private String amountSpent;
    private String resultCode;
    private String resultMsg;
    private String correlationId;
    private String loanAmount;
    private String loanPoundage;
    private String loanRecovered;
    private String finalAmount;
    private String bossid;
    private String code;
    private String retn;
    private String msisdn;
    private String country;
    private String countryId;
    private String operator;
    private String operatorId;
    private String destinationCurrency;
    private String wholeSalePrice;
    private String uniqId;
    private String telcosTransactionId;
    private String expiryDate;
    private String pushNotification;
    private String identityValue;
    private String identityType;
    private String origSource;
    private String receivedTimestamp;
    private String sourceRequestId;
    private String loanState;
    private String loanTimeStamp;
    private String loanReason;
    private String loanOffer;
    private String externalLoanId;
    private String internalLoanId;
    private String advanceOfferId;
    private String offerName;
    private String commodityType;
    private String currencyCode;
    private String principalAmount;
    private String setUpFees;
    private String loanPlanId;
    private String loanPlanName;
    private String loanProductGroup;
    private String repayment;
    private String repaymentCounts;
    private String gross;
    private String principal;
    private String interest;
    private String interestVAT;
    private String charges;
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
    private String setupFees;
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
    private String repaymentsCount;
    private String outstanding;
    private String plan;
    private String offerClass;
    private String name;

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
    private String refNo;
    private String primaryLoanId;
    private String notify;
    private List<Charge> chargeList;
    private List<Account> accountList;
    private List<ThirdPartyData> thirdPartyDataList;
    private LoanInfo loanInfoSummary;
    private List<Event> eventList;
    private IDs iDs;
    private String memberId;
    private String surName;
    private String nickName;
    private Boolean isDefault;
    private String documentType;
    private String documentNumber;
    private String value;
    private String customerStatus;
    private String aliasStatus;
    private String aliasType;
    private String aliasValue;
    private String identificationParameter;
    private String cardAcceptorTerminalId;
    private String cardAcceptorNameAndLocation;
    private String pointOfEntry;
    private String networkIdentifier;
    private String identifier;
    private String purposeOfPayment;
    private String beneficiaryIban;
    private String beneficiaryId;
    private String senderName;
    private String senderId;
    private String receiverId;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getBeneficiaryIban() {
        return beneficiaryIban;
    }

    public void setBeneficiaryIban(String beneficiaryIban) {
        this.beneficiaryIban = beneficiaryIban;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCardAcceptorTerminalId() {
        return cardAcceptorTerminalId;
    }

    public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) {
        this.cardAcceptorTerminalId = cardAcceptorTerminalId;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
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

    public String getIdentificationParameter() {
        return identificationParameter;
    }

    public void setIdentificationParameter(String identificationParameter) {
        this.identificationParameter = identificationParameter;
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

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getAliasStatus() {
        return aliasStatus;
    }

    public void setAliasStatus(String aliasStatus) {
        this.aliasStatus = aliasStatus;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public IDs getiDs() {
        return iDs;
    }

    public void setiDs(IDs iDs) {
        this.iDs = iDs;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public LoanInfo getLoanInfoSummary() {
        return loanInfoSummary;
    }

    public void setLoanInfoSummary(LoanInfo loanInfoSummary) {
        this.loanInfoSummary = loanInfoSummary;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
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

    public String getPrimaryLoanId() {
        return primaryLoanId;
    }

    public void setPrimaryLoanId(String primaryLoanId) {
        this.primaryLoanId = primaryLoanId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
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

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
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

    public String getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(String principalAmount) {
        this.principalAmount = principalAmount;
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

    public String getSetupFees() {
        return setupFees;
    }

    public void setSetupFees(String setupFees) {
        this.setupFees = setupFees;
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

    public String getLoanTimestamp() {
        return loanTimestamp;
    }

    public void setLoanTimestamp(String loanTimestamp) {
        this.loanTimestamp = loanTimestamp;
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

    public String getRepaymentsCount() {
        return repaymentsCount;
    }

    public void setRepaymentsCount(String repaymentsCount) {
        this.repaymentsCount = repaymentsCount;
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

    public String getSourceRequestId() {
        return sourceRequestId;
    }

    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanTimeStamp() {
        return loanTimeStamp;
    }

    public void setLoanTimeStamp(String loanTimeStamp) {
        this.loanTimeStamp = loanTimeStamp;
    }

    public String getLoanReason() {
        return loanReason;
    }

    public void setLoanReason(String loanReason) {
        this.loanReason = loanReason;
    }

    public String getLoanOffer() {
        return loanOffer;
    }

    public void setLoanOffer(String loanOffer) {
        this.loanOffer = loanOffer;
    }

    public String getExternalLoanId() {
        return externalLoanId;
    }

    public void setExternalLoanId(String externalLoanId) {
        this.externalLoanId = externalLoanId;
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

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSetUpFees() {
        return setUpFees;
    }

    public void setSetUpFees(String setUpFees) {
        this.setUpFees = setUpFees;
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

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
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

    public String getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(String pushNotification) {
        this.pushNotification = pushNotification;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanPoundage() {
        return loanPoundage;
    }

    public void setLoanPoundage(String loanPoundage) {
        this.loanPoundage = loanPoundage;
    }

    public String getLoanRecovered() {
        return loanRecovered;
    }

    public void setLoanRecovered(String loanRecovered) {
        this.loanRecovered = loanRecovered;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getBossid() {
        return bossid;
    }

    public void setBossid(String bossid) {
        this.bossid = bossid;
    }

    public String getRetn() {
        return retn;
    }

    public void setRetn(String retn) {
        this.retn = retn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getDestinationCurrency() {
        return destinationCurrency;
    }

    public void setDestinationCurrency(String destinationCurrency) {
        this.destinationCurrency = destinationCurrency;
    }

    public String getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(String wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getTelcosTransactionId() {
        return telcosTransactionId;
    }

    public void setTelcosTransactionId(String telcosTransactionId) {
        this.telcosTransactionId = telcosTransactionId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(String amountSpent) {
        this.amountSpent = amountSpent;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public List<CardDetailVO> getCardDetailList() {
        return cardDetailList;
    }

    public void setCardDetailList(List<CardDetailVO> cardDetailList) {
        this.cardDetailList = cardDetailList;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public String getIsHit() {
        return isHit;
    }

    public void setIsHit(String isHit) {
        this.isHit = isHit;
    }

    public String getScreeningStatus() {
        return screeningStatus;
    }

    public void setScreeningStatus(String screeningStatus) {
        this.screeningStatus = screeningStatus;
    }

    public String getTotalCWL() {
        return totalCWL;
    }

    public void setTotalCWL(String totalCWL) {
        this.totalCWL = totalCWL;
    }

    public String getTotalGWL() {
        return totalGWL;
    }

    public void setTotalGWL(String totalGWL) {
        this.totalGWL = totalGWL;
    }

    public String getTotalPEPEDD() {
        return totalPEPEDD;
    }

    public void setTotalPEPEDD(String totalPEPEDD) {
        this.totalPEPEDD = totalPEPEDD;
    }

    public String getTotalPrivate() {
        return totalPrivate;
    }

    public void setTotalPrivate(String totalPrivate) {
        this.totalPrivate = totalPrivate;
    }

    //Keys available in I8SBKeysOfCollectionEnum
    private Map<String, List<?>> collectionOfList = new HashMap<String, List<?>>();

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<AssociatedAccounts> getAssociatedAccounts() {
        return associatedAccounts;
    }

    public void setAssociatedAccounts(List<AssociatedAccounts> associatedAccounts) {
        this.associatedAccounts = associatedAccounts;
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

    public String getSystemsTraceAuditNumber() {
        return systemsTraceAuditNumber;
    }

    public void setSystemsTraceAuditNumber(String systemsTraceAuditNumber) {
        this.systemsTraceAuditNumber = systemsTraceAuditNumber;
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

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    public List<HealthAndNutritionContent> getContent() {
        return content;
    }

    public void setContent(List<HealthAndNutritionContent> content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public String getCardStatus() {
        return CardStatus;
    }

    public void setCardStatus(String cardStatus) {
        CardStatus = cardStatus;
    }

    public String getCardStatusCode() {
        return CardStatusCode;
    }

    public void setCardStatusCode(String cardStatusCode) {
        CardStatusCode = cardStatusCode;
    }

    public String getOrderRefId() {
        return OrderRefId;
    }

    public void setOrderRefId(String orderRefId) {
        OrderRefId = orderRefId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getHostCode() {
        return hostCode;
    }

    public void setHostCode(String hostCode) {
        this.hostCode = hostCode;
    }

    public String getHostDesc() {
        return hostDesc;
    }

    public void setHostDesc(String hostDesc) {
        this.hostDesc = hostDesc;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getDestinationBankImd() {
        return destinationBankImd;
    }

    public void setDestinationBankImd(String destinationBankImd) {
        this.destinationBankImd = destinationBankImd;
    }

    public String getIsPartialPayment() {
        return isPartialPayment;
    }

    public void setIsPartialPayment(String isPartialPayment) {
        this.isPartialPayment = isPartialPayment;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<StatementVo> getStatementVos() {
        return statementVos;
    }

    public void setStatementVos(List<StatementVo> statementVos) {
        this.statementVos = statementVos;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public List<?> getInternationalTransactionList() {
        return internationalTransactionList;
    }

    public void setInternationalTransactionList(List<?> internationalTransactionList) {
        this.internationalTransactionList = internationalTransactionList;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public List<ManualTransactionVo> getManualTransactionListVo() {
        return manualTransactionListVo;
    }

    public void setManualTransactionListVo(List<ManualTransactionVo> manualTransactionListVo) {
        this.manualTransactionListVo = manualTransactionListVo;
    }

    public String getLastFingerIndex() {
        return lastFingerIndex;
    }

    public void setLastFingerIndex(String lastFingerIndex) {
        this.lastFingerIndex = lastFingerIndex;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getQrResponseCode() {
        return QrResponseCode;
    }

    public void setQrResponseCode(String qrResponseCode) {
        QrResponseCode = qrResponseCode;
    }

    public String getCardAcceptorAddressCity() {
        return cardAcceptorAddressCity;
    }

    public void setCardAcceptorAddressCity(String cardAcceptorAddressCity) {
        this.cardAcceptorAddressCity = cardAcceptorAddressCity;
    }

    public String getCardAcceptorAddressCountry() {
        return cardAcceptorAddressCountry;
    }

    public void setCardAcceptorAddressCountry(String cardAcceptorAddressCountry) {
        this.cardAcceptorAddressCountry = cardAcceptorAddressCountry;
    }

    public String getOrignalActionCode() {
        return orignalActionCode;
    }

    public void setOrignalActionCode(String orignalActionCode) {
        this.orignalActionCode = orignalActionCode;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getPurchaseIdentifierRefrenceNumber() {
        return purchaseIdentifierRefrenceNumber;
    }

    public void setPurchaseIdentifierRefrenceNumber(String purchaseIdentifierRefrenceNumber) {
        this.purchaseIdentifierRefrenceNumber = purchaseIdentifierRefrenceNumber;
    }

    public String getPurchaceIdentifierType() {
        return purchaceIdentifierType;
    }

    public void setPurchaceIdentifierType(String purchaceIdentifierType) {
        this.purchaceIdentifierType = purchaceIdentifierType;
    }

    public String getGivePreviouslyUpdatedFlag() {
        return givePreviouslyUpdatedFlag;
    }

    public void setGivePreviouslyUpdatedFlag(String givePreviouslyUpdatedFlag) {
        this.givePreviouslyUpdatedFlag = givePreviouslyUpdatedFlag;
    }

    public String getGiveUpdatedFlag() {
        return giveUpdatedFlag;
    }

    public void setGiveUpdatedFlag(String giveUpdatedFlag) {
        this.giveUpdatedFlag = giveUpdatedFlag;
    }

    public String getSettlementResponsibilityFlag() {
        return settlementResponsibilityFlag;
    }

    public void setSettlementResponsibilityFlag(String settlementResponsibilityFlag) {
        this.settlementResponsibilityFlag = settlementResponsibilityFlag;
    }

    public String getSettlementServiceFlag() {
        return settlementServiceFlag;
    }

    public void setSettlementServiceFlag(String settlementServiceFlag) {
        this.settlementServiceFlag = settlementServiceFlag;
    }

    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public String getTransactiontime() {
        return transactiontime;
    }

    public void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAccountNumberMasked() {
        return accountNumberMasked;
    }

    public void setAccountNumberMasked(String accountNumberMasked) {
        this.accountNumberMasked = accountNumberMasked;
    }

    public String getAccquiringBin() {
        return accquiringBin;
    }

    public void setAccquiringBin(String accquiringBin) {
        this.accquiringBin = accquiringBin;
    }

    public String getCardAcceptorIdCode() {
        return cardAcceptorIdCode;
    }

    public void setCardAcceptorIdCode(String cardAcceptorIdCode) {
        this.cardAcceptorIdCode = cardAcceptorIdCode;
    }

    public String getCardAcceptorName() {
        return cardAcceptorName;
    }

    public void setCardAcceptorName(String cardAcceptorName) {
        this.cardAcceptorName = cardAcceptorName;
    }

    public String getFeeProgramIndicator() {
        return feeProgramIndicator;
    }

    public void setFeeProgramIndicator(String feeProgramIndicator) {
        this.feeProgramIndicator = feeProgramIndicator;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public void setTransactionIdentifier(String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getToBankImd() {
        return toBankImd;
    }

    public void setToBankImd(String toBankImd) {
        this.toBankImd = toBankImd;
    }

    public String getnTransaction() {
        return nTransaction;
    }

    public void setnTransaction(String nTransaction) {
        this.nTransaction = nTransaction;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(String avgAmount) {
        this.avgAmount = avgAmount;
    }

    public String getPitb() {
        return pitb;
    }

    public void setPitb(String pitb) {
        this.pitb = pitb;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getWalletAccountId() {
        return walletAccountId;
    }

    public void setWalletAccountId(String walletAccountId) {
        this.walletAccountId = walletAccountId;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getSessionIDBAFL() {
        return sessionIDBAFL;
    }

    public void setSessionIDBAFL(String sessionIDBAFL) {
        this.sessionIDBAFL = sessionIDBAFL;
    }

    public String getMobileWalletNumber() {
        return mobileWalletNumber;
    }

    public void setMobileWalletNumber(String mobileWalletNumber) {
        this.mobileWalletNumber = mobileWalletNumber;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDecs() {
        return transactionDecs;
    }

    public void setTransactionDecs(String transactionDecs) {
        this.transactionDecs = transactionDecs;
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode) {
        this.instrumentCode = instrumentCode;
    }

    public String getDebitAtmTrans() {
        return debitAtmTrans;
    }

    public void setDebitAtmTrans(String debitAtmTrans) {
        this.debitAtmTrans = debitAtmTrans;
    }

    public String getCreditAtmTrans() {
        return creditAtmTrans;
    }

    public void setCreditAtmTrans(String creditAtmTrans) {
        this.creditAtmTrans = creditAtmTrans;
    }

    public String getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(String remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(String statementDate) {
        this.statementDate = statementDate;
    }

    public String getStatementPeriod() {
        return statementPeriod;
    }

    public void setStatementPeriod(String statementPeriod) {
        this.statementPeriod = statementPeriod;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }

    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public String getCustomerBranchCode() {
        return customerBranchCode;
    }

    public void setCustomerBranchCode(String customerBranchCode) {
        this.customerBranchCode = customerBranchCode;
    }

    public String getWithdrawlAmount() {
        return withdrawlAmount;
    }

    public void setWithdrawlAmount(String withdrawlAmount) {
        this.withdrawlAmount = withdrawlAmount;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getIbanAccountNumber() {
        return ibanAccountNumber;
    }

    public void setIbanAccountNumber(String ibanAccountNumber) {
        this.ibanAccountNumber = ibanAccountNumber;
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

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPrevBalance() {
        return prevBalance;
    }

    public void setPrevBalance(String prevBalance) {
        this.prevBalance = prevBalance;
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

    public String getTaxpaid() {
        return taxpaid;
    }

    public void setTaxpaid(String taxpaid) {
        this.taxpaid = taxpaid;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTempeleteType() {
        return tempeleteType;
    }

    public void setTempeleteType(String tempeleteType) {
        this.tempeleteType = tempeleteType;
    }

    public String getFingerTempelete() {

        return fingerTempelete;
    }

    public void setFingerTempelete(String fingerTempelete) {
        this.fingerTempelete = fingerTempelete;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(String availableLimit) {
        this.availableLimit = availableLimit;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCustomerAccNumber() {
        return customerAccNumber;
    }

    public void setCustomerAccNumber(String customerAccNumber) {
        this.customerAccNumber = customerAccNumber;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getSessionIdNadra() {
        return sessionIdNadra;
    }

    public void setSessionIdNadra(String sessionIdNadra) {
        this.sessionIdNadra = sessionIdNadra;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getReturnedString() {
        return returnedString;
    }

    public void setReturnedString(String returnedString) {
        this.returnedString = returnedString;
    }

    public String getConsumerDetail() {
        return consumerDetail;
    }

    public void setConsumerDetail(String consumerDetail) {
        this.consumerDetail = consumerDetail;
    }

    public String getAmountWithinDueDate() {
        return amountWithinDueDate;
    }

    public void setAmountWithinDueDate(String amountWithinDueDate) {
        this.amountWithinDueDate = amountWithinDueDate;
    }

    public String getAmountAfterDueDate() {
        return amountAfterDueDate;
    }

    public void setAmountAfterDueDate(String amountAfterDueDate) {
        this.amountAfterDueDate = amountAfterDueDate;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(String datePaid) {
        this.datePaid = datePaid;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getTranAuthID() {
        return tranAuthID;
    }

    public void setTranAuthID(String tranAuthID) {
        this.tranAuthID = tranAuthID;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getP2M_ID() {
        return P2M_ID;
    }

    public void setP2M_ID(String p2M_ID) {
        this.P2M_ID = p2M_ID;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<?> getList(String key) {
        return this.collectionOfList.get(key);
    }

    public Map getCollectionOfList() {
        return this.collectionOfList;
    }

    public void setCollectionOfList(Map map) {
        this.collectionOfList = map;
    }

    public void addListToCollection(String key, List<?> list) {
        this.collectionOfList.put(key, list);
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

//    public List<MiniStatement> getMiniStatementList() {
//        return miniStatementList;
//    }
//
//    public void setMiniStatementList(List<MiniStatement> miniStatementList) {
//        this.miniStatementList = miniStatementList;
//    }

    public List<I8SBSwitchControllerResponseVO> getI8SBSwitchControllerResponseVOList() {
        return i8SBSwitchControllerResponseVOList;
    }

    public void addI8SBSwitchControllerResponseVO(I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        this.i8SBSwitchControllerResponseVOList.add(i8SBSwitchControllerResponseVO);
    }

    @XmlTransient
    public String getResponseXML() {
        return responseXML;
    }

    public void setResponseXML(String responseXML) {
        this.responseXML = responseXML;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOverallDefaultAccount() {
        return overallDefaultAccount;
    }

    public void setOverallDefaultAccount(String overallDefaultAccount) {
        this.overallDefaultAccount = overallDefaultAccount;
    }

//    public List<TDRAccountSummary> getTDRAccSummaryList() {
//        return TDRAccSummaryList;
//    }
//
//    public void setTDRAccSummaryList(List<TDRAccountSummary> TDRAccSummaryList) {
//        this.TDRAccSummaryList = TDRAccSummaryList;
//    }

//    public List<CCMiniStmt> getCCMiniStatementList() {
//        return CCMiniStatementList;
//    }
//
//    public void setCCMiniStatementList(List<CCMiniStmt> CCMiniStatementList) {
//        this.CCMiniStatementList = CCMiniStatementList;
//    }
//
//    public List<CreditCard> getCreditCardList() {
//        return creditCardList;
//    }
//
//    public void setCreditCardList(List<CreditCard> creditCardList) {
//        this.creditCardList = creditCardList;
//    }

    public String getCCStatusCode() {
        return CCStatusCode;
    }

    public void setCCStatusCode(String CCStatusCode) {
        this.CCStatusCode = CCStatusCode;
    }

    public String getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(String outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

//    public List<UserAccounts> getUserAccountList() {
//        return userAccountList;
//    }
//
//    public void setUserAccountList(List<UserAccounts> userAccountList) {
//        this.userAccountList = userAccountList;
//    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSTAN() {
        return STAN;
    }

    public void setSTAN(String STAN) {
        this.STAN = STAN;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getForcePasswordChange() {
        return forcePasswordChange;
    }

    public void setForcePasswordChange(String forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }

    public String getMPINExpiry() {
        return MPINExpiry;
    }

    public void setMPINExpiry(String MPINExpiry) {
        this.MPINExpiry = MPINExpiry;
    }

    public String getPasswordBitmap() {
        return passwordBitmap;
    }

    public void setPasswordBitmap(String passwordBitmap) {
        this.passwordBitmap = passwordBitmap;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }


//    public List<CheckingAccountSummary> getCheckingAccountSummaryList() {
//        return checkingAccountSummaryList;
//    }
//
//    public void setCheckingAccountSummaryList(List<CheckingAccountSummary> checkingAccountSummaryList) {
//        this.checkingAccountSummaryList = checkingAccountSummaryList;
//    }
//
//    public List<LoanAccountSummary> getLoanAccountSummaryList() {
//        return loanAccountSummaryList;
//    }
//
//    public void setLoanAccountSummaryList(List<LoanAccountSummary> loanAccountSummaryList) {
//        this.loanAccountSummaryList = loanAccountSummaryList;
//    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

//    public List<Card> getCardList() {
//        return cardList;
//    }
//
//    public void setCardList(List<Card> cardList) {
//        this.cardList = cardList;
//    }
//
//    public List<LinkedAcc> getLinkedAccList() {
//        return linkedAccList;
//    }
//
//    public void setLinkedAccList(List<LinkedAcc> linkedAccList) {
//        this.linkedAccList = linkedAccList;
//    }
//
//    public List<Transaction> getTransactionsList() {
//        return transactionsList;
//    }
//
//    public void setTransactionsList(List<Transaction> transactionsList) {
//        this.transactionsList = transactionsList;
//    }

    public String getAccountId1() {
        return accountId1;
    }

    public void setAccountId1(String accountId1) {
        this.accountId1 = accountId1;
    }

    public String getAccountId2() {
        return accountId2;
    }

    public void setAccountId2(String accountId2) {
        this.accountId2 = accountId2;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
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

//    public List<BeneficiaryAcct> getBeneficiaryAcctList() {
//        return beneficiaryAcctList;
//    }
//
//    public void setBeneficiaryAcctList(List<BeneficiaryAcct> beneficiaryAcctList) {
//        this.beneficiaryAcctList = beneficiaryAcctList;
//    }
//
//    public List<IBFTBenAcc> getIBFTBenAccList() {
//        return IBFTBenAccList;
//    }
//
//    public void setIBFTBenAccList(List<IBFTBenAcc> IBFTBenAccList) {
//        this.IBFTBenAccList = IBFTBenAccList;
//    }
//
//    public List<ConsumerData> getConsumerDataList() {
//        return consumerDataList;
//    }
//
//    public void setConsumerDataList(List<ConsumerData> consumerDataList) {
//        this.consumerDataList = consumerDataList;
//    }

    public String getConsumerTitle() {
        return consumerTitle;
    }

    public void setConsumerTitle(String consumerTitle) {
        this.consumerTitle = consumerTitle;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getBillAmountAfterDueDate() {
        return billAmountAfterDueDate;
    }

    public void setBillAmountAfterDueDate(String billAmountAfterDueDate) {
        this.billAmountAfterDueDate = billAmountAfterDueDate;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(String serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

//    public List<Instrument> getInstrumentList() {
//        return instrumentList;
//    }
//
//    public void setInstrumentList(List<Instrument> instrumentList) {
//        this.instrumentList = instrumentList;
//    }
//
//    public List<StopCheque> getStopChequeList() {
//        return stopChequeList;
//    }
//
//    public void setStopChequeList(List<StopCheque> stopChequeList) {
//        this.stopChequeList = stopChequeList;
//    }

    public String getRequestingTranCode() {
        return requestingTranCode;
    }

    public void setRequestingTranCode(String requestingTranCode) {
        this.requestingTranCode = requestingTranCode;
    }

    public String getTransactionFees() {
        return transactionFees;
    }

    public void setTransactionFees(String transactionFees) {
        this.transactionFees = transactionFees;
    }

//    public List<PrepaidCard> getPrepaidCardList() {
//        return prepaidCardList;
//    }
//
//    public void setPrepaidCardList(List<PrepaidCard> prepaidCardList) {
//        this.prepaidCardList = prepaidCardList;
//    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public AccountStatement getAccountStatement() {
        return accountStatement;
    }

    public void setAccountStatement(AccountStatement accountStatement) {
        this.accountStatement = accountStatement;
    }

    public TDRAccountStatement getTdrAccountStatement() {
        return tdrAccountStatement;
    }

    public void setTdrAccountStatement(TDRAccountStatement tdrAccountStatement) {
        this.tdrAccountStatement = tdrAccountStatement;
    }

    public String getVisaTxnIdentifier() {
        return visaTxnIdentifier;
    }

    public void setVisaTxnIdentifier(String visaTxnIdentifier) {
        this.visaTxnIdentifier = visaTxnIdentifier;
    }

    public String getVisaApprovalCode() {
        return visaApprovalCode;
    }

    public void setVisaApprovalCode(String visaApprovalCode) {
        this.visaApprovalCode = visaApprovalCode;
    }

    public String getVisaActionCode() {
        return visaActionCode;
    }

    public void setVisaActionCode(String visaActionCode) {
        this.visaActionCode = visaActionCode;
    }

    public String getMerchantVerificationValue() {
        return merchantVerificationValue;
    }

    public void setMerchantVerificationValue(String merchantVerificationValue) {
        this.merchantVerificationValue = merchantVerificationValue;
    }
    //    public List<OneLinkBank> getOneLinkBankList() {
//        return oneLinkBankList;
//    }
//
//    public void setOneLinkBankList(List<OneLinkBank> oneLinkBankList) {
//        this.oneLinkBankList = oneLinkBankList;
//    }
//
//    public List<BillingCompany> getBillingCompanyList() {
//        return billingCompanyList;
//    }
//
//    public void setBillingCompanyList(List<BillingCompany> billingCompanyList) {
//        this.billingCompanyList = billingCompanyList;
//    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getuSSDResponseString() {
        return uSSDResponseString;
    }

    public void setuSSDResponseString(String uSSDResponseString) {
        this.uSSDResponseString = uSSDResponseString;
    }

    public String getuSSDAction() {
        return uSSDAction;
    }

    public void setuSSDAction(String uSSDAction) {
        this.uSSDAction = uSSDAction;
    }

    public String getCodingScheme() {
        return codingScheme;
    }

    public void setCodingScheme(String codingScheme) {
        this.codingScheme = codingScheme;
    }

    public int getVehicleAssesmentNo() {
        return vehicleAssesmentNo;
    }

    public void setVehicleAssesmentNo(int vehicleAssesmentNo) {
        this.vehicleAssesmentNo = vehicleAssesmentNo;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getMakerMake() {
        return makerMake;
    }

    public void setMakerMake(String makerMake) {
        this.makerMake = makerMake;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public int getEngCapacity() {
        return engCapacity;
    }

    public void setEngCapacity(int engCapacity) {
        this.engCapacity = engCapacity;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getCyllinders() {
        return cyllinders;
    }

    public void setCyllinders(int cyllinders) {
        this.cyllinders = cyllinders;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerCnic() {
        return ownerCnic;
    }

    public void setOwnerCnic(String ownerCnic) {
        this.ownerCnic = ownerCnic;
    }

    public String getFilerStatus() {
        return filerStatus;
    }

    public void setFilerStatus(String filerStatus) {
        this.filerStatus = filerStatus;
    }

    public String getTaxPaidFrom() {
        return taxPaidFrom;
    }

    public void setTaxPaidFrom(String taxPaidFrom) {
        this.taxPaidFrom = taxPaidFrom;
    }

    public String getTaxPaidUpto() {
        return taxPaidUpto;
    }

    public void setTaxPaidUpto(String taxPaidUpto) {
        this.taxPaidUpto = taxPaidUpto;
    }

    public String getVehTaxPaidLifeTime() {
        return vehTaxPaidLifeTime;
    }

    public void setVehTaxPaidLifeTime(String vehTaxPaidLifeTime) {
        this.vehTaxPaidLifeTime = vehTaxPaidLifeTime;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getFitnessDate() {
        return fitnessDate;
    }

    public void setFitnessDate(String fitnessDate) {
        this.fitnessDate = fitnessDate;
    }


    public String getVctChallanNumber() {
        return vctChallanNumber;
    }

    public void setVctChallanNumber(String vctChallanNumber) {
        this.vctChallanNumber = vctChallanNumber;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
    }

    public String getChallanStatus() {
        return challanStatus;
    }

    public void setChallanStatus(String challanStatus) {
        this.challanStatus = challanStatus;
    }

    public int getPaymentRecievedBy() {
        return paymentRecievedBy;
    }

    public void setPaymentRecievedBy(int paymentRecievedBy) {
        this.paymentRecievedBy = paymentRecievedBy;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CardVO> getCardVOList() {
        return cardVOList;
    }

    public void setCardVOList(List<CardVO> cardVOList) {
        this.cardVOList = cardVOList;
    }

    public List<AccountStatementData> getData() {
        return data;
    }

    public void setData(List<AccountStatementData> data) {
        this.data = data;
    }

    public String getAssesmentDate() {
        return assesmentDate;
    }

    public void setAssesmentDate(String assesmentDate) {
        this.assesmentDate = assesmentDate;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBillCategoryCode() {
        return billCategoryCode;
    }

    public void setBillCategoryCode(String billCategoryCode) {
        this.billCategoryCode = billCategoryCode;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
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

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getTemporaryLimitFee() {
        return temporaryLimitFee;
    }

    public void setTemporaryLimitFee(String temporaryLimitFee) {
        this.temporaryLimitFee = temporaryLimitFee;
    }

    public String getProfileFee() {
        return profileFee;
    }

    public void setProfileFee(String profileFee) {
        this.profileFee = profileFee;
    }
}


