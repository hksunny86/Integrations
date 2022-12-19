package com.inov8.integration.i8sb.vo;

import com.inov8.integration.i8sb.vo.CardDetailVO;
import com.inov8.integration.vo.StatementVo;
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
    private List <CardDetailVO> cardDetailList;
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


