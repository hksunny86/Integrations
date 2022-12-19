package com.inov8.integration.webservice.vo;

import com.inov8.integration.vo.CardType;
import com.inov8.integration.vo.CatalogList;
import com.inov8.integration.vo.SegmentList;

import java.io.Serializable;
import java.util.ArrayList;
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
