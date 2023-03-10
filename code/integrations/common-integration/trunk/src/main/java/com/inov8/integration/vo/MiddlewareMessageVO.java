package com.inov8.integration.vo;

import com.inov8.integration.middleware.bop.avenza.MiniStatementBlk;
import com.inov8.integration.middleware.prisim.*;
import com.inov8.microbank.server.service.switchmodule.iris.model.CardVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MiddlewareMessageVO implements Serializable {

    private static final long serialVersionUID = 5824473488070381027L;
    private String userID;
    private String applicationID;
    private String rrnKey;
    private String retrievalReferenceNumber;
    private String microbankTransactionCode;
    private String transactionType;
    private Date transmissionTime;
    private Date requestTime;
    private String responseCode;
    private String responseDescription;
    private String messageAsEdi;
    private String stan;
    private String PAN;
    private String transactionAmount;
    private String subscriptionCheck;
    private String settlementAmount;
    private String transactionFee;
    private String settlementFee;
    private String settlementProcessingFee;
    private String accountTitle;
    private String accountBalance;
    private String accountType;
    private String bankIMD;
    private String accountCurrency;
    private String accountStatus;
    private String currencyCode;
    private String amountType;
    private String balanceType;
    private String fromAccountType;
    private String fromAccountCurrency;
    private String fromAccountIMD;
    private String accountNo1;
    private String accountNo2;
    private String toAccountCurrency;
    private String toAccountType;
    private String toBankName;
    private String toAccountIMD;
    private String cnicNo;
    private String consumerNo;
    private String consumerName;
    private String companyCategory;
    private String companyCode;
    private Date billDueDate;
    private String billAggregator;
    private String billStatus;
    private String billingMonth;
    private String amountDueDate;
    private String amountAfterDueDate;
    private String netCED;
    private String netWithholdingTAX;
    private String authResponseId;
    private String billCategoryId;
    private String ubpSTAN;
    private Long parentTransactionId;
    private String reversalSTAN;
    private String reversalRequestTime;
    private Date timeLocalTransaction;
    private Date dateLocalTransaction;
    private String pinBlock;
    private String newPinBlock;
    private String channelStatus;
    private String channel;
    private String text;
    private String cardType;
    private String cardNo;
    private String cardExpiry;
    private String cardStatusCode;
    private String cardName;
    private String merchantType;
    private String otpPin;
    private String charges;
    private String dateTime;
    private String birthPlace;
    private String presentAddress;
    private String cnicStatus;
    private String cnicExpiry;
    private String fatherHusbandName;
    private String motherMaiden;
    private String customerStatus;
    private String terminalId;
    private String paymentType;
    private String transactionId;
    //addedd by ahsan
    private String dateExpiration;
    private String pointOfServiceEntryMode;
    private String institutionIdentificationCode;
    private String track2Data;
    private String cardAcceptorTerminalIdentification;
    private String cardAcceptorIdentificationCode;
    private String cardAcceptorNameAndLocation;
    private String track1Data;
    private String additionalDataprivate;
    private String pinData;
    private String privateEmvData;
    private String privateData;
    private String networkIdentifier;

    private String vouchCode;
    //add below parameter on request of shehryar nawaz
    private long productId;
    private String reserved1;
    private String reserved2;
    private String transactionDateTime;
    private String senderName;
    private String senderIban;
    private String accountBankName;
    private String benificieryIban;
    private String accountBranchName;
    private String amountCardHolderBilling;

    public String getAmountCardHolderBilling() {
        return amountCardHolderBilling;
    }

    public void setAmountCardHolderBilling(String amountCardHolderBilling) {
        this.amountCardHolderBilling = amountCardHolderBilling;
    }

    public String getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(String totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public String getTotalOutstandingAmount() {
        return totalOutstandingAmount;
    }

    public void setTotalOutstandingAmount(String totalOutstandingAmount) {
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(String minLimit) {
        this.minLimit = minLimit;
    }

    private String totalLoanAmount;
    private String totalOutstandingAmount;
    private String feeAmount;
    private String maxLimit;
    private String minLimit;

    private String posEntryMode;


    public String getPosEntryMode() {
        return posEntryMode;
    }

    public void setPosEntryMode(String posEntryMode) {
        this.posEntryMode = posEntryMode;
    }


    public String getSenderIban() {
        return senderIban;
    }

    public void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getBenificieryIban() {
        return benificieryIban;
    }

    public void setBenificieryIban(String benificieryIban) {
        this.benificieryIban = benificieryIban;
    }

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getPointOfServiceEntryMode() {
        return pointOfServiceEntryMode;
    }

    public void setPointOfServiceEntryMode(String pointOfServiceEntryMode) {
        this.pointOfServiceEntryMode = pointOfServiceEntryMode;
    }

    public String getInstitutionIdentificationCode() {
        return institutionIdentificationCode;
    }

    public void setInstitutionIdentificationCode(String institutionIdentificationCode) {
        this.institutionIdentificationCode = institutionIdentificationCode;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public void setTrack2Data(String track2Data) {
        this.track2Data = track2Data;
    }

    public String getCardAcceptorTerminalIdentification() {
        return cardAcceptorTerminalIdentification;
    }

    public void setCardAcceptorTerminalIdentification(String cardAcceptorTerminalIdentification) {
        this.cardAcceptorTerminalIdentification = cardAcceptorTerminalIdentification;
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

    public String getTrack1Data() {
        return track1Data;
    }

    public void setTrack1Data(String track1Data) {
        this.track1Data = track1Data;
    }

    public String getAdditionalDataprivate() {
        return additionalDataprivate;
    }

    public void setAdditionalDataprivate(String additionalDataprivate) {
        this.additionalDataprivate = additionalDataprivate;
    }

    public String getPinData() {
        return pinData;
    }

    public void setPinData(String pinData) {
        this.pinData = pinData;
    }

    public String getPrivateEmvData() {
        return privateEmvData;
    }

    public void setPrivateEmvData(String privateEmvData) {
        this.privateEmvData = privateEmvData;
    }

    public String getPrivateData() {
        return privateData;
    }

    public void setPrivateData(String privateData) {
        this.privateData = privateData;
    }

    public String getSubscriptionCheck() {
        return subscriptionCheck;
    }

    public void setSubscriptionCheck(String subscriptionCheck) {
        this.subscriptionCheck = subscriptionCheck;
    }

    public String getPasswordPositions() {
        return passwordPositions;
    }

    public void setPasswordPositions(String passwordPositions) {
        this.passwordPositions = passwordPositions;
    }

    public String getPasswordPattern() {
        return passwordPattern;
    }

    public void setPasswordPattern(String passwordPattern) {
        this.passwordPattern = passwordPattern;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    private String withdrawalLimit;
    private String availableWithdrawalLimit;
    private String purchaseLimit;
    private String availablePurchaseLimit;


    private List<AccountInfoVO> accountList;
    private List<CardInfoVO> cardList;
    private List<StatementRowVO> statementRowList;

    private CustomerProfileVO customerProfileVO;

    // PRISM Related Fields
    private String userName;
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String city;
    private String country;
    private String nationality;
    private String beneficiaryType;
    private String email;
    private String email1;
    private String email2;
    private String defaultEmail;
    private String firstTimeLogin;

    private String mobileIpAddress;
    private String emailPin;
    private String mobilePin;
    private String mobileNo;
    private String financialPIN;
    private String pinNature;
    private String isMiniStatment;
    private String fromDate;
    private String toDate;

    private String transactionResponseCode;
    private String transactionReferenceNumber;
    private String purposeId;
    private String nickName;
    private String loginDatetime;
    private String logoutDatetime;
    private CustomerAccount customerAccount;
    private PaymentDetailDTO paymentDetailDTO;

    private AccountStatementFilter accountStatementFilter;

    private BillPayee billPayee;
    private CustomerDetail customerDetail;
    private BeneficiaryAccount beneficiaryAccount;
    private List<BeneficiaryAccount> beneficiaryAccountList;
    private List<BillingCompany> billingCompanyList;
    private List<Relationship> relationshipList;
    private List<TransactionList> transactionList;
    private List<SecretQuestion> secretQuestionList;
    private List<BillPayee> billPayeeList;
    private List<BankList> bankList;
    private List<Purpose> purposeList;
    private List<BillingCompanyType> billingCompanyTypeList;
    private String numberOfBooklets;
    private String bookletSize;
    private String bookletType;
    private String printName;
    private String methodOfCollection;
    private String statementOfDuration;
    private String identifier;
    private String loginId;
    private SecretQuestion secretQuestion;
    private String secretAnswer;
    private String newEmail;
    private SecretQuestion newSecretQuestion;
    private String newSecretAnswer;
    private String recurPattern;
    private String recurEndType;
    private String numberOfOccurrence;
    private String batchId;
    private String accountTypeValue;
    private String statementPeriodType;
    private String dateRange;
    private String transactionCount;
    private String transactionLength;
    private String currencyName;
    private String currencyMnemonic;
    private String currencyDecimalPoints;

    private String miniStatementType;
    private String passwordPositions;
    private String passwordPattern;
    private String customerPassword;

    private String behaviorFlag;
    private String reserved;
    private String accountProcCode;
    private String cycleBeginDate;
    private String cycleLength;
    private String cycleAmount;
    private String cycleAmountRemaining;
    private String accountAvailableBalance;
    private String accountWithdrawal;
    private String billingAddressFlag;
    private String accountNature;
    private String accountDefault;
    private String withdrawalFlag;
    private String purchaseFlag;
    private String transferToFlag;
    private String transferFromFlag;
    private String miniStatementFlag;
    private String balanceInquiryFlag;
    private String chequeBookReqFlag;
    private String statementReqFlag;
    private String billPaymentFlag;
    private String chequeDepositFlag;
    private String cashDepositFlag;
    private String mapCode;
    private String accountBranchCode;
    private String operationName;
    private String serviceName;
    private String statementLimit;
    private String desc;
    private String otpSubscriptionFlag;
    private String OrignalRetrievalReferenceNumber;
    private String depositSlipNumber;
    private String depositBranch;
    private String cardSecurityCode;
    private String totalAmount;

    private String conversationID;
    private String originatorConversationID;

    private String password;
    private String PIN;
    private String statusPosition;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String utilityCompanyId;
    private String billName;
    private String availableBalance;
    private String billAmount;
    private String accountNumber;
    private String billAmountAfterDueDate;

    private String purposeOfPayment;
    private String originatorName;
    private String benificieryName;
    private String benificieryAccountNumber;
    private String originatorBankName;
    private String benificieryBankName;
    private String benificieryIBAN;

    private String bankBranchName;
    private String processingCode;
    private List<Map<String, String>> transactionsMap;
    private Map<String, String> miniStatementRows;

    private List<TransactionHistoryList> transactionHistoryLists;
    private List<LoginHistoryTransactionList> loginHistoryTransactionLists;
    private List<FTScheduleTransactionList> ftScheduleTransactionList;
    private List<BPScheduleTransactionList> bpScheduleTransactionList;
    private String responseContentXML;
    private String senderIBAN;
    private String orignalStan;
    private String orignalTransactionDateTime;
    private String senderBankImd;
    private String beneficiaryBankImd;
    private String crdr;
    private String agentId;


    public String getAgentId() {

        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getSenderBankImd() {
        return senderBankImd;
    }

    public void setSenderBankImd(String senderBankImd) {
        this.senderBankImd = senderBankImd;
    }

    public String getBeneficiaryBankImd() {
        return beneficiaryBankImd;
    }

    public void setBeneficiaryBankImd(String beneficiaryBankImd) {
        this.beneficiaryBankImd = beneficiaryBankImd;
    }

    public String getCrdr() {
        return crdr;
    }

    public void setCrdr(String crdr) {
        this.crdr = crdr;
    }

    public String getOrignalStan() {
        return orignalStan;
    }

    public void setOrignalStan(String orignalStan) {
        this.orignalStan = orignalStan;
    }

    public String getOrignalTransactionDateTime() {
        return orignalTransactionDateTime;
    }

    public void setOrignalTransactionDateTime(String orignalTransactionDateTime) {
        this.orignalTransactionDateTime = orignalTransactionDateTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public String getResponseContentXML() {
        return responseContentXML;
    }

    public void setResponseContentXML(String responseContentXML) {
        this.responseContentXML = responseContentXML;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MiddlewareMessageVO{");
        sb.append("userID='").append(userID).append('\'');
        sb.append(", applicationID='").append(applicationID).append('\'');
        sb.append(", rrnKey='").append(rrnKey).append('\'');
        sb.append(", retrievalReferenceNumber='").append(retrievalReferenceNumber).append('\'');
        sb.append(", microbankTransactionCode='").append(microbankTransactionCode).append('\'');
        sb.append(", transactionType='").append(transactionType).append('\'');
        sb.append(", transmissionTime=").append(transmissionTime);
        sb.append(", requestTime=").append(requestTime);
        sb.append(", responseCode='").append(responseCode).append('\'');
        sb.append(", responseDescription='").append(responseDescription).append('\'');
        sb.append(", messageAsEdi='").append(messageAsEdi).append('\'');
        sb.append(", stan='").append(stan).append('\'');
        sb.append(", PAN='").append(PAN).append('\'');
        sb.append(", transactionAmount='").append(transactionAmount).append('\'');
        sb.append(", subscriptionCheck='").append(subscriptionCheck).append('\'');
        sb.append(", settlementAmount='").append(settlementAmount).append('\'');
        sb.append(", transactionFee='").append(transactionFee).append('\'');
        sb.append(", settlementFee='").append(settlementFee).append('\'');
        sb.append(", settlementProcessingFee='").append(settlementProcessingFee).append('\'');
        sb.append(", accountTitle='").append(accountTitle).append('\'');
        sb.append(", accountBalance='").append(accountBalance).append('\'');
        sb.append(", accountType='").append(accountType).append('\'');
        sb.append(", bankIMD='").append(bankIMD).append('\'');
        sb.append(", accountCurrency='").append(accountCurrency).append('\'');
        sb.append(", accountStatus='").append(accountStatus).append('\'');
        sb.append(", currencyCode='").append(currencyCode).append('\'');
        sb.append(", amountType='").append(amountType).append('\'');
        sb.append(", balanceType='").append(balanceType).append('\'');
        sb.append(", fromAccountType='").append(fromAccountType).append('\'');
        sb.append(", fromAccountCurrency='").append(fromAccountCurrency).append('\'');
        sb.append(", fromAccountIMD='").append(fromAccountIMD).append('\'');
        sb.append(", accountNo1='").append(accountNo1).append('\'');
        sb.append(", accountNo2='").append(accountNo2).append('\'');
        sb.append(", toAccountCurrency='").append(toAccountCurrency).append('\'');
        sb.append(", toAccountType='").append(toAccountType).append('\'');
        sb.append(", toBankName='").append(toBankName).append('\'');
        sb.append(", toAccountIMD='").append(toAccountIMD).append('\'');
        sb.append(", cnicNo='").append(cnicNo).append('\'');
        sb.append(", consumerNo='").append(consumerNo).append('\'');
        sb.append(", consumerName='").append(consumerName).append('\'');
        sb.append(", companyCategory='").append(companyCategory).append('\'');
        sb.append(", companyCode='").append(companyCode).append('\'');
        sb.append(", billDueDate=").append(billDueDate);
        sb.append(", billAggregator='").append(billAggregator).append('\'');
        sb.append(", billStatus='").append(billStatus).append('\'');
        sb.append(", billingMonth='").append(billingMonth).append('\'');
        sb.append(", amountDueDate='").append(amountDueDate).append('\'');
        sb.append(", amountAfterDueDate='").append(amountAfterDueDate).append('\'');
        sb.append(", netCED='").append(netCED).append('\'');
        sb.append(", netWithholdingTAX='").append(netWithholdingTAX).append('\'');
        sb.append(", authResponseId='").append(authResponseId).append('\'');
        sb.append(", billCategoryId='").append(billCategoryId).append('\'');
        sb.append(", ubpSTAN='").append(ubpSTAN).append('\'');
        sb.append(", parentTransactionId=").append(parentTransactionId);
        sb.append(", reversalSTAN='").append(reversalSTAN).append('\'');
        sb.append(", reversalRequestTime='").append(reversalRequestTime).append('\'');
        sb.append(", timeLocalTransaction=").append(timeLocalTransaction);
        sb.append(", dateLocalTransaction=").append(dateLocalTransaction);
        sb.append(", pinBlock='").append(pinBlock).append('\'');
        sb.append(", newPinBlock='").append(newPinBlock).append('\'');
        sb.append(", channelStatus='").append(channelStatus).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", cardType='").append(cardType).append('\'');
        sb.append(", cardNo='").append(cardNo).append('\'');
        sb.append(", cardExpiry='").append(cardExpiry).append('\'');
        sb.append(", cardStatusCode='").append(cardStatusCode).append('\'');
        sb.append(", cardName='").append(cardName).append('\'');
        sb.append(", merchantType='").append(merchantType).append('\'');
        sb.append(", withdrawalLimit='").append(withdrawalLimit).append('\'');
        sb.append(", availableWithdrawalLimit='").append(availableWithdrawalLimit).append('\'');
        sb.append(", purchaseLimit='").append(purchaseLimit).append('\'');
        sb.append(", availablePurchaseLimit='").append(availablePurchaseLimit).append('\'');
        sb.append(", accountList=").append(accountList);
        sb.append(", cardList=").append(cardList);
        sb.append(", statementRowList=").append(statementRowList);
        sb.append(", customerProfileVO=").append(customerProfileVO);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", dateOfBirth='").append(dateOfBirth).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", nationality='").append(nationality).append('\'');
        sb.append(", beneficiaryType='").append(beneficiaryType).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", email1='").append(email1).append('\'');
        sb.append(", email2='").append(email2).append('\'');
        sb.append(", defaultEmail='").append(defaultEmail).append('\'');
        sb.append(", firstTimeLogin='").append(firstTimeLogin).append('\'');
        sb.append(", mobileIpAddress='").append(mobileIpAddress).append('\'');
        sb.append(", emailPin='").append(emailPin).append('\'');
        sb.append(", mobilePin='").append(mobilePin).append('\'');
        sb.append(", mobileNo='").append(mobileNo).append('\'');
        sb.append(", financialPIN='").append(financialPIN).append('\'');
        sb.append(", pinNature='").append(pinNature).append('\'');
        sb.append(", isMiniStatment='").append(isMiniStatment).append('\'');
        sb.append(", fromDate='").append(fromDate).append('\'');
        sb.append(", toDate='").append(toDate).append('\'');
        sb.append(", transactionResponseCode='").append(transactionResponseCode).append('\'');
        sb.append(", transactionReferenceNumber='").append(transactionReferenceNumber).append('\'');
        sb.append(", purposeId='").append(purposeId).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", loginDatetime='").append(loginDatetime).append('\'');
        sb.append(", logoutDatetime='").append(logoutDatetime).append('\'');
        sb.append(", customerAccount=").append(customerAccount);
        sb.append(", paymentDetailDTO=").append(paymentDetailDTO);
        sb.append(", accountStatementFilter=").append(accountStatementFilter);
        sb.append(", billPayee=").append(billPayee);
        sb.append(", customerDetail=").append(customerDetail);
        sb.append(", beneficiaryAccount=").append(beneficiaryAccount);
        sb.append(", beneficiaryAccountList=").append(beneficiaryAccountList);
        sb.append(", billingCompanyList=").append(billingCompanyList);
        sb.append(", relationshipList=").append(relationshipList);
        sb.append(", transactionList=").append(transactionList);
        sb.append(", secretQuestionList=").append(secretQuestionList);
        sb.append(", billPayeeList=").append(billPayeeList);
        sb.append(", bankList=").append(bankList);
        sb.append(", purposeList=").append(purposeList);
        sb.append(", billingCompanyTypeList=").append(billingCompanyTypeList);
        sb.append(", numberOfBooklets='").append(numberOfBooklets).append('\'');
        sb.append(", bookletSize='").append(bookletSize).append('\'');
        sb.append(", bookletType='").append(bookletType).append('\'');
        sb.append(", printName='").append(printName).append('\'');
        sb.append(", methodOfCollection='").append(methodOfCollection).append('\'');
        sb.append(", statementOfDuration='").append(statementOfDuration).append('\'');
        sb.append(", identifier='").append(identifier).append('\'');
        sb.append(", loginId='").append(loginId).append('\'');
        sb.append(", secretQuestion=").append(secretQuestion);
        sb.append(", secretAnswer='").append(secretAnswer).append('\'');
        sb.append(", newEmail='").append(newEmail).append('\'');
        sb.append(", newSecretQuestion=").append(newSecretQuestion);
        sb.append(", newSecretAnswer='").append(newSecretAnswer).append('\'');
        sb.append(", recurPattern='").append(recurPattern).append('\'');
        sb.append(", recurEndType='").append(recurEndType).append('\'');
        sb.append(", numberOfOccurrence='").append(numberOfOccurrence).append('\'');
        sb.append(", batchId='").append(batchId).append('\'');
        sb.append(", accountTypeValue='").append(accountTypeValue).append('\'');
        sb.append(", statementPeriodType='").append(statementPeriodType).append('\'');
        sb.append(", dateRange='").append(dateRange).append('\'');
        sb.append(", transactionCount='").append(transactionCount).append('\'');
        sb.append(", transactionLength='").append(transactionLength).append('\'');
        sb.append(", currencyName='").append(currencyName).append('\'');
        sb.append(", currencyMnemonic='").append(currencyMnemonic).append('\'');
        sb.append(", currencyDecimalPoints='").append(currencyDecimalPoints).append('\'');
        sb.append(", miniStatementType='").append(miniStatementType).append('\'');
        sb.append(", passwordPositions='").append(passwordPositions).append('\'');
        sb.append(", passwordPattern='").append(passwordPattern).append('\'');
        sb.append(", customerPassword='").append(customerPassword).append('\'');
        sb.append(", behaviorFlag='").append(behaviorFlag).append('\'');
        sb.append(", reserved='").append(reserved).append('\'');
        sb.append(", accountProcCode='").append(accountProcCode).append('\'');
        sb.append(", cycleBeginDate='").append(cycleBeginDate).append('\'');
        sb.append(", cycleLength='").append(cycleLength).append('\'');
        sb.append(", cycleAmount='").append(cycleAmount).append('\'');
        sb.append(", cycleAmountRemaining='").append(cycleAmountRemaining).append('\'');
        sb.append(", accountAvailableBalance='").append(accountAvailableBalance).append('\'');
        sb.append(", accountWithdrawal='").append(accountWithdrawal).append('\'');
        sb.append(", billingAddressFlag='").append(billingAddressFlag).append('\'');
        sb.append(", accountNature='").append(accountNature).append('\'');
        sb.append(", accountDefault='").append(accountDefault).append('\'');
        sb.append(", withdrawalFlag='").append(withdrawalFlag).append('\'');
        sb.append(", purchaseFlag='").append(purchaseFlag).append('\'');
        sb.append(", transferToFlag='").append(transferToFlag).append('\'');
        sb.append(", transferFromFlag='").append(transferFromFlag).append('\'');
        sb.append(", miniStatementFlag='").append(miniStatementFlag).append('\'');
        sb.append(", balanceInquiryFlag='").append(balanceInquiryFlag).append('\'');
        sb.append(", chequeBookReqFlag='").append(chequeBookReqFlag).append('\'');
        sb.append(", statementReqFlag='").append(statementReqFlag).append('\'');
        sb.append(", billPaymentFlag='").append(billPaymentFlag).append('\'');
        sb.append(", chequeDepositFlag='").append(chequeDepositFlag).append('\'');
        sb.append(", cashDepositFlag='").append(cashDepositFlag).append('\'');
        sb.append(", mapCode='").append(mapCode).append('\'');
        sb.append(", accountBranchCode='").append(accountBranchCode).append('\'');
        sb.append(", operationName='").append(operationName).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", statementLimit='").append(statementLimit).append('\'');
        sb.append(", desc='").append(desc).append('\'');
        sb.append(", otpSubscriptionFlag='").append(otpSubscriptionFlag).append('\'');
        sb.append(", transactionHistoryLists=").append(transactionHistoryLists);
        sb.append(", loginHistoryTransactionLists=").append(loginHistoryTransactionLists);
        sb.append(", ftScheduleTransactionList=").append(ftScheduleTransactionList);
        sb.append(", bpScheduleTransactionList=").append(bpScheduleTransactionList);
        sb.append(", purposeOfPayment=").append(purposeOfPayment);
        sb.append(", originatorName=").append(originatorName);
        sb.append(", benificieryName=").append(benificieryName);
        sb.append(", benificieryAccountNumber=").append(benificieryAccountNumber);
        sb.append(", originatorBankName=").append(originatorBankName);
        sb.append(", benificieryBankName=").append(benificieryBankName);
        sb.append(", benificieryIBAN=").append(benificieryIBAN);
        sb.append(", orignalStan=").append(orignalStan);
        sb.append(", orignalTransactionDateTime=").append(orignalTransactionDateTime);
        sb.append(", ProductId=").append(productId);
        sb.append(", Reserved1=").append(reserved1);
        sb.append(", Reseved2=").append(reserved2);
        sb.append(", DateExpiration=").append(dateExpiration);
        sb.append(", PointOfServiceEntryMode=").append(pointOfServiceEntryMode);
        sb.append(", posEntryMode=").append(posEntryMode);
        sb.append(", institutionIdentificationCode=").append(institutionIdentificationCode);
        sb.append(", track2Data").append(track2Data);
        sb.append(", cardAcceptorTerminalIdentification").append(cardAcceptorTerminalIdentification);
        sb.append(", cardAcceptorIdentificationCode").append(cardAcceptorIdentificationCode);
        sb.append(", track1Data").append(track1Data);
        sb.append(", additionalDataprivate").append(additionalDataprivate);
        sb.append(", pinData").append(pinData);
        sb.append(", privateEmvData").append(privateEmvData);
        sb.append(", privateData").append(privateData);
        sb.append(", networkIdentifier").append(networkIdentifier);
        sb.append(", senderBankImd").append(senderBankImd);
        sb.append(", benificiaryBankImd").append(beneficiaryBankImd);
        sb.append(", accountBankName").append(accountBankName);
        sb.append(", accountBranchName").append(accountBranchName);
        sb.append('}');
        return sb.toString();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getAuthResponseId() {
        return authResponseId;
    }

    public void setAuthResponseId(String authResponseId) {
        this.authResponseId = authResponseId;
    }

    public String getNetWithholdingTAX() {
        return netWithholdingTAX;
    }

    public void setNetWithholdingTAX(String netWithholdingTAX) {
        this.netWithholdingTAX = netWithholdingTAX;
    }

    public String getNetCED() {
        return netCED;
    }

    public void setNetCED(String netCED) {
        this.netCED = netCED;
    }

    public String getMessageAsEdi() {
        return messageAsEdi;
    }

    public void setMessageAsEdi(String messageAsEdi) {
        this.messageAsEdi = messageAsEdi;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Date getTimeLocalTransaction() {
        return this.timeLocalTransaction;
    }

    public void setTimeLocalTransaction(Date timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public Date getDateLocalTransaction() {
        return this.dateLocalTransaction;
    }

    public void setDateLocalTransaction(Date dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getRetrievalReferenceNumber() {
        return this.retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMicrobankTransactionCode() {
        return this.microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getTransactionAmount() {
        return this.transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionFee() {
        return this.transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getAccountNo1() {
        return this.accountNo1;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getAccountNo2() {
        return this.accountNo2;
    }

    public void setAccountNo2(String accountNo2) {
        this.accountNo2 = accountNo2;
    }

    public String getCnicNo() {
        return this.cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getConsumerNo() {
        return this.consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getToAccountCurrency() {
        return toAccountCurrency;
    }

    public void setToAccountCurrency(String toAccountCurrency) {
        this.toAccountCurrency = toAccountCurrency;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getCompnayCode() {
        return companyCode;
    }

    public void setCompnayCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getConsumerName() {
        return this.consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public Date getBillDueDate() {
        return this.billDueDate;
    }

    public void setBillDueDate(Date billDueDate) {
        this.billDueDate = billDueDate;
    }

    public String getBillAggregator() {
        return this.billAggregator;
    }

    public void setBillAggregator(String billAggregator) {
        this.billAggregator = billAggregator;
    }

    public String getAccountTitle() {
        return this.accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getReversalSTAN() {
        return this.reversalSTAN;
    }

    public void setReversalSTAN(String reversalSTAN) {
        this.reversalSTAN = reversalSTAN;
    }

    public String getReversalRequestTime() {
        return this.reversalRequestTime;
    }

    public void setReversalRequestTime(String reversalRequestTime) {
        this.reversalRequestTime = reversalRequestTime;
    }

    public String getStan() {
        return this.stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public Date getTransmissionTime() {
        return this.transmissionTime;
    }

    public void setTransmissionTime(Date transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

    public String getBillStatus() {
        return this.billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBillingMonth() {
        return this.billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getAmountDueDate() {
        return this.amountDueDate;
    }

    public void setAmountDueDate(String amountDueDate) {
        this.amountDueDate = amountDueDate;
    }

    public String getAmountAfterDueDate() {
        return this.amountAfterDueDate;
    }

    public void setAmountAfterDueDate(String amountAfterDueDate) {
        this.amountAfterDueDate = amountAfterDueDate;
    }

    public String getAccountBalance() {
        return this.accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getBillCategoryId() {
        return this.billCategoryId;
    }

    public void setBillCategoryId(String billCategoryId) {
        this.billCategoryId = billCategoryId;
    }

    public Long getParentTransactionId() {
        return this.parentTransactionId;
    }

    public void setParentTransactionId(Long parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAmountType() {
        return this.amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBalanceType() {
        return this.balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public Date getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRrnKey() {
        return this.rrnKey;
    }

    public void setRrnKey(String rrnKey) {
        this.rrnKey = rrnKey;
    }

    public String getUbpSTAN() {
        return this.ubpSTAN;
    }

    public void setUbpSTAN(String ubpSTAN) {
        this.ubpSTAN = ubpSTAN;
    }

    public String getPinBlock() {
        return this.pinBlock;
    }

    public void setPinBlock(String pinBlock) {
        this.pinBlock = pinBlock;
    }

    public List<AccountInfoVO> getAccountList() {
        return this.accountList;
    }

    public void setAccountList(List<AccountInfoVO> accountList) {
        this.accountList = accountList;
    }

    public List<StatementRowVO> getStatementRowList() {
        return this.statementRowList;
    }

    public void setStatementRowList(List<StatementRowVO> statementRowList) {
        this.statementRowList = statementRowList;
    }

    public String getSettlementAmount() {
        return this.settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementFee() {
        return this.settlementFee;
    }

    public void setSettlementFee(String settlementFee) {
        this.settlementFee = settlementFee;
    }

    public String getSettlementProcessingFee() {
        return this.settlementProcessingFee;
    }

    public void setSettlementProcessingFee(String settlementProcessingFee) {
        this.settlementProcessingFee = settlementProcessingFee;
    }

    public String getFromAccountIMD() {
        return this.fromAccountIMD;
    }

    public void setFromAccountIMD(String fromAccountIMD) {
        this.fromAccountIMD = fromAccountIMD;
    }

    public String getToAccountIMD() {
        return this.toAccountIMD;
    }

    public void setToAccountIMD(String toAccountIMD) {
        this.toAccountIMD = toAccountIMD;
    }

    public String getToBankName() {
        return this.toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public List<CardInfoVO> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardInfoVO> cardList) {
        this.cardList = cardList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getMobileIpAddress() {
        return mobileIpAddress;
    }

    public void setMobileIpAddress(String mobileIpAddress) {
        this.mobileIpAddress = mobileIpAddress;
    }

    public String getEmailPin() {
        return emailPin;
    }

    public void setEmailPin(String emailPin) {
        this.emailPin = emailPin;
    }

    public String getMobilePin() {
        return mobilePin;
    }

    public void setMobilePin(String mobilePin) {
        this.mobilePin = mobilePin;
    }

    public String getFinancialPIN() {
        return financialPIN;
    }

    public void setFinancialPIN(String financialPIN) {
        this.financialPIN = financialPIN;
    }

    public AccountStatementFilter getAccountStatementFilter() {
        return accountStatementFilter;
    }

    public void setAccountStatementFilter(AccountStatementFilter accountStatementFilter) {
        this.accountStatementFilter = accountStatementFilter;
    }

    public BillPayee getBillPayee() {
        return billPayee;
    }

    public void setBillPayee(BillPayee billPayee) {
        this.billPayee = billPayee;
    }

    public CustomerDetail getCustomerDetail() {
        return customerDetail;
    }

    public void setCustomerDetail(CustomerDetail customerDetail) {
        this.customerDetail = customerDetail;
    }

    public BeneficiaryAccount getBeneficiaryAccount() {
        return beneficiaryAccount;
    }

    public void setBeneficiaryAccount(BeneficiaryAccount beneficiaryAccount) {
        this.beneficiaryAccount = beneficiaryAccount;
    }

    public List<BeneficiaryAccount> getBeneficiaryAccountList() {
        return beneficiaryAccountList;
    }

    public void setBeneficiaryAccountList(List<BeneficiaryAccount> beneficiaryAccountList) {
        this.beneficiaryAccountList = beneficiaryAccountList;
    }

    public List<BillingCompany> getBillingCompanyList() {
        return billingCompanyList;
    }

    public void setBillingCompanyList(List<BillingCompany> billingCompanyList) {
        this.billingCompanyList = billingCompanyList;
    }

    public List<Relationship> getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(List<Relationship> relationshipList) {
        this.relationshipList = relationshipList;
    }

    public List<TransactionList> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionList> transactionList) {
        this.transactionList = transactionList;
    }

    public List<SecretQuestion> getSecretQuestionList() {
        return secretQuestionList;
    }

    public void setSecretQuestionList(List<SecretQuestion> secretQuestionList) {
        this.secretQuestionList = secretQuestionList;
    }

    public List<BillPayee> getBillPayeeList() {
        return billPayeeList;
    }

    public void setBillPayeeList(List<BillPayee> billPayeeList) {
        this.billPayeeList = billPayeeList;
    }

    public List<BankList> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankList> bankList) {
        this.bankList = bankList;
    }

    public List<BillingCompanyType> getBillingCompanyTypeList() {
        return billingCompanyTypeList;
    }

    public void setBillingCompanyTypeList(List<BillingCompanyType> billingCompanyTypeList) {
        this.billingCompanyTypeList = billingCompanyTypeList;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getNewPinBlock() {
        return newPinBlock;
    }

    public void setNewPinBlock(String newPinBlock) {
        this.newPinBlock = newPinBlock;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardStatusCode() {
        return cardStatusCode;
    }

    public void setCardStatusCode(String cardStatusCode) {
        this.cardStatusCode = cardStatusCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public void setWithdrawalLimit(String withdrawalLimit) {
        this.withdrawalLimit = withdrawalLimit;
    }

    public String getAvailableWithdrawalLimit() {
        return availableWithdrawalLimit;
    }

    public void setAvailableWithdrawalLimit(String availableWithdrawalLimit) {
        this.availableWithdrawalLimit = availableWithdrawalLimit;
    }

    public String getPurchaseLimit() {
        return purchaseLimit;
    }

    public void setPurchaseLimit(String purchaseLimit) {
        this.purchaseLimit = purchaseLimit;
    }

    public String getAvailablePurchaseLimit() {
        return availablePurchaseLimit;
    }

    public void setAvailablePurchaseLimit(String availablePurchaseLimit) {
        this.availablePurchaseLimit = availablePurchaseLimit;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    public String getFirstTimeLogin() {
        return firstTimeLogin;
    }

    public void setFirstTimeLogin(String firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    public String getPinNature() {
        return pinNature;
    }

    public void setPinNature(String pinNature) {
        this.pinNature = pinNature;
    }

    public String getIsMiniStatment() {
        return isMiniStatment;
    }

    public void setIsMiniStatment(String isMiniStatment) {
        this.isMiniStatment = isMiniStatment;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(String fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public String getFromAccountCurrency() {
        return fromAccountCurrency;
    }

    public void setFromAccountCurrency(String fromAccountCurrency) {
        this.fromAccountCurrency = fromAccountCurrency;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }

    public List<Purpose> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<Purpose> purposeList) {
        this.purposeList = purposeList;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCompanyCategory() {
        return companyCategory;
    }

    public void setCompanyCategory(String companyCategory) {
        this.companyCategory = companyCategory;
    }

    public String getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLoginDatetime() {
        return loginDatetime;
    }

    public void setLoginDatetime(String loginDatetime) {
        this.loginDatetime = loginDatetime;
    }

    public String getLogoutDatetime() {
        return logoutDatetime;
    }

    public void setLogoutDatetime(String logoutDatetime) {
        this.logoutDatetime = logoutDatetime;
    }

    public String getTransactionResponseCode() {
        return transactionResponseCode;
    }

    public void setTransactionResponseCode(String transactionResponseCode) {
        this.transactionResponseCode = transactionResponseCode;
    }

    public String getTransactionReferenceNumber() {
        return transactionReferenceNumber;
    }

    public void setTransactionReferenceNumber(String transactionReferenceNumber) {
        this.transactionReferenceNumber = transactionReferenceNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CustomerProfileVO getCustomerProfileVO() {
        return customerProfileVO;
    }

    public void setCustomerProfileVO(CustomerProfileVO customerProfileVO) {
        this.customerProfileVO = customerProfileVO;
    }

    public String getNumberOfBooklets() {
        return numberOfBooklets;
    }

    public void setNumberOfBooklets(String numberOfBooklets) {
        this.numberOfBooklets = numberOfBooklets;
    }

    public String getBookletSize() {
        return bookletSize;
    }

    public void setBookletSize(String bookletSize) {
        this.bookletSize = bookletSize;
    }

    public String getBookletType() {
        return bookletType;
    }

    public void setBookletType(String bookletType) {
        this.bookletType = bookletType;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getMethodOfCollection() {
        return methodOfCollection;
    }

    public void setMethodOfCollection(String methodOfCollection) {
        this.methodOfCollection = methodOfCollection;
    }

    public String getStatementOfDuration() {
        return statementOfDuration;
    }

    public void setStatementOfDuration(String statementOfDuration) {
        this.statementOfDuration = statementOfDuration;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public SecretQuestion getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(SecretQuestion secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public PaymentDetailDTO getPaymentDetailDTO() {
        return paymentDetailDTO;
    }

    public void setPaymentDetailDTO(PaymentDetailDTO paymentDetailDTO) {
        this.paymentDetailDTO = paymentDetailDTO;
    }

    public String getRecurPattern() {
        return recurPattern;
    }

    public void setRecurPattern(String recurPattern) {
        this.recurPattern = recurPattern;
    }

    public String getRecurEndType() {
        return recurEndType;
    }

    public void setRecurEndType(String recurEndType) {
        this.recurEndType = recurEndType;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public SecretQuestion getNewSecretQuestion() {
        return newSecretQuestion;
    }

    public void setNewSecretQuestion(SecretQuestion newSecretQuestion) {
        this.newSecretQuestion = newSecretQuestion;
    }

    public String getNewSecretAnswer() {
        return newSecretAnswer;
    }

    public void setNewSecretAnswer(String newSecretAnswer) {
        this.newSecretAnswer = newSecretAnswer;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }


    public String getNumberOfOccurrence() {
        return numberOfOccurrence;
    }

    public void setNumberOfOccurrence(String numberOfOccurrence) {
        this.numberOfOccurrence = numberOfOccurrence;
    }

    public List<LoginHistoryTransactionList> getLoginHistoryTransactionLists() {
        return loginHistoryTransactionLists;
    }

    public void setLoginHistoryTransactionLists(List<LoginHistoryTransactionList> loginHistoryTransactionLists) {
        this.loginHistoryTransactionLists = loginHistoryTransactionLists;
    }

    public String getAccountTypeValue() {
        return accountTypeValue;
    }

    public void setAccountTypeValue(String accountTypeValue) {
        this.accountTypeValue = accountTypeValue;
    }

    public String getStatementPeriodType() {
        return statementPeriodType;
    }

    public void setStatementPeriodType(String statementPeriodType) {
        this.statementPeriodType = statementPeriodType;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(String transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getTransactionLength() {
        return transactionLength;
    }

    public void setTransactionLength(String transactionLength) {
        this.transactionLength = transactionLength;
    }


    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyMnemonic() {
        return currencyMnemonic;
    }

    public void setCurrencyMnemonic(String currencyMnemonic) {
        this.currencyMnemonic = currencyMnemonic;
    }

    public String getCurrencyDecimalPoints() {
        return currencyDecimalPoints;
    }

    public void setCurrencyDecimalPoints(String currencyDecimalPoints) {
        this.currencyDecimalPoints = currencyDecimalPoints;
    }

    public String getMiniStatementType() {
        return miniStatementType;
    }

    public void setMiniStatementType(String miniStatementType) {
        this.miniStatementType = miniStatementType;
    }

    public List<TransactionHistoryList> getTransactionHistoryLists() {
        return transactionHistoryLists;
    }

    public void setTransactionHistoryLists(List<TransactionHistoryList> transactionHistoryLists) {
        this.transactionHistoryLists = transactionHistoryLists;
    }

    public List<FTScheduleTransactionList> getFtScheduleTransactionList() {
        return ftScheduleTransactionList;
    }

    public void setFtScheduleTransactionList(List<FTScheduleTransactionList> ftScheduleTransactionList) {
        this.ftScheduleTransactionList = ftScheduleTransactionList;
    }

    public List<BPScheduleTransactionList> getBpScheduleTransactionList() {
        return bpScheduleTransactionList;
    }

    public void setBpScheduleTransactionList(List<BPScheduleTransactionList> bpScheduleTransactionList) {
        this.bpScheduleTransactionList = bpScheduleTransactionList;
    }

    public String getBehaviorFlag() {
        return behaviorFlag;
    }

    public void setBehaviorFlag(String behaviorFlag) {
        this.behaviorFlag = behaviorFlag;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getAccountProcCode() {
        return accountProcCode;
    }

    public void setAccountProcCode(String accountProcCode) {
        this.accountProcCode = accountProcCode;
    }

    public String getCycleBeginDate() {
        return cycleBeginDate;
    }

    public void setCycleBeginDate(String cycleBeginDate) {
        this.cycleBeginDate = cycleBeginDate;
    }

    public String getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(String cycleLength) {
        this.cycleLength = cycleLength;
    }

    public String getCycleAmount() {
        return cycleAmount;
    }

    public void setCycleAmount(String cycleAmount) {
        this.cycleAmount = cycleAmount;
    }

    public String getCycleAmountRemaining() {
        return cycleAmountRemaining;
    }

    public void setCycleAmountRemaining(String cycleAmountRemaining) {
        this.cycleAmountRemaining = cycleAmountRemaining;
    }

    public String getAccountAvailableBalance() {
        return accountAvailableBalance;
    }

    public void setAccountAvailableBalance(String accountAvailableBalance) {
        this.accountAvailableBalance = accountAvailableBalance;
    }

    public String getAccountWithdrawal() {
        return accountWithdrawal;
    }

    public void setAccountWithdrawal(String accountWithdrawal) {
        this.accountWithdrawal = accountWithdrawal;
    }

    public String getBillingAddressFlag() {
        return billingAddressFlag;
    }

    public void setBillingAddressFlag(String billingAddressFlag) {
        this.billingAddressFlag = billingAddressFlag;
    }

    public String getAccountNature() {
        return accountNature;
    }

    public void setAccountNature(String accountNature) {
        this.accountNature = accountNature;
    }

    public String getAccountDefault() {
        return accountDefault;
    }

    public void setAccountDefault(String accountDefault) {
        this.accountDefault = accountDefault;
    }

    public String getWithdrawalFlag() {
        return withdrawalFlag;
    }

    public void setWithdrawalFlag(String withdrawalFlag) {
        this.withdrawalFlag = withdrawalFlag;
    }

    public String getPurchaseFlag() {
        return purchaseFlag;
    }

    public void setPurchaseFlag(String purchaseFlag) {
        this.purchaseFlag = purchaseFlag;
    }

    public String getTransferToFlag() {
        return transferToFlag;
    }

    public void setTransferToFlag(String transferToFlag) {
        this.transferToFlag = transferToFlag;
    }

    public String getTransferFromFlag() {
        return transferFromFlag;
    }

    public void setTransferFromFlag(String transferFromFlag) {
        this.transferFromFlag = transferFromFlag;
    }

    public String getMiniStatementFlag() {
        return miniStatementFlag;
    }

    public void setMiniStatementFlag(String miniStatementFlag) {
        this.miniStatementFlag = miniStatementFlag;
    }

    public String getBalanceInquiryFlag() {
        return balanceInquiryFlag;
    }

    public void setBalanceInquiryFlag(String balanceInquiryFlag) {
        this.balanceInquiryFlag = balanceInquiryFlag;
    }

    public String getChequeBookReqFlag() {
        return chequeBookReqFlag;
    }

    public void setChequeBookReqFlag(String chequeBookReqFlag) {
        this.chequeBookReqFlag = chequeBookReqFlag;
    }

    public String getStatementReqFlag() {
        return statementReqFlag;
    }

    public void setStatementReqFlag(String statementReqFlag) {
        this.statementReqFlag = statementReqFlag;
    }

    public String getBillPaymentFlag() {
        return billPaymentFlag;
    }

    public void setBillPaymentFlag(String billPaymentFlag) {
        this.billPaymentFlag = billPaymentFlag;
    }

    public String getChequeDepositFlag() {
        return chequeDepositFlag;
    }

    public void setChequeDepositFlag(String chequeDepositFlag) {
        this.chequeDepositFlag = chequeDepositFlag;
    }

    public String getCashDepositFlag() {
        return cashDepositFlag;
    }

    public void setCashDepositFlag(String cashDepositFlag) {
        this.cashDepositFlag = cashDepositFlag;
    }


    public String getMapCode() {
        return mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public String getAccountBranchCode() {
        return accountBranchCode;
    }

    public void setAccountBranchCode(String accountBranchCode) {
        this.accountBranchCode = accountBranchCode;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatementLimit() {
        return statementLimit;
    }

    public void setStatementLimit(String statementLimit) {
        this.statementLimit = statementLimit;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOtpSubscriptionFlag() {
        return otpSubscriptionFlag;
    }

    public void setOtpSubscriptionFlag(String otpSubscriptionFlag) {
        this.otpSubscriptionFlag = otpSubscriptionFlag;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getOrignalRetrievalReferenceNumber() {
        return OrignalRetrievalReferenceNumber;
    }

    public void setOrignalRetrievalReferenceNumber(String orignalRetrievalReferenceNumber) {
        OrignalRetrievalReferenceNumber = orignalRetrievalReferenceNumber;
    }

    public String getDepositSlipNumber() {
        return depositSlipNumber;
    }

    public void setDepositSlipNumber(String depositSlipNumber) {
        this.depositSlipNumber = depositSlipNumber;
    }

    public String getDepositBranch() {
        return depositBranch;
    }

    public void setDepositBranch(String depositBranch) {
        this.depositBranch = depositBranch;
    }

    public String getCardSecurityCode() {
        return cardSecurityCode;
    }

    public void setCardSecurityCode(String cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getOriginatorConversationID() {
        return originatorConversationID;
    }

    public void setOriginatorConversationID(String originatorConversationID) {
        this.originatorConversationID = originatorConversationID;
    }

    public String getOtpPin() {
        return otpPin;
    }

    public void setOtpPin(String otpPin) {
        this.otpPin = otpPin;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getVouchCode() {
        return vouchCode;
    }

    public void setVouchCode(String vouchCode) {
        this.vouchCode = vouchCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getStatusPosition() {
        return statusPosition;
    }

    public void setStatusPosition(String statusPosition) {
        this.statusPosition = statusPosition;
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

    public String getUtilityCompanyId() {
        return utilityCompanyId;
    }

    public void setUtilityCompanyId(String utilityCompanyId) {
        this.utilityCompanyId = utilityCompanyId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBillAmountAfterDueDate() {
        return billAmountAfterDueDate;
    }

    public void setBillAmountAfterDueDate(String billAmountAfterDueDate) {
        this.billAmountAfterDueDate = billAmountAfterDueDate;
    }

    public String getBenificieryIBAN() {
        return benificieryIBAN;
    }

    public void setBenificieryIBAN(String benificieryIBAN) {
        this.benificieryIBAN = benificieryIBAN;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public List<Map<String, String>> getTransactionsMap() {
        return transactionsMap;
    }

    public void setTransactionsMap(List<Map<String, String>> transactionsMap) {
        this.transactionsMap = transactionsMap;
    }

    public Map<String, String> getMiniStatementRows() {
        return miniStatementRows;
    }

    public void setMiniStatementRows(Map<String, String> miniStatementRows) {
        this.miniStatementRows = miniStatementRows;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getBenificieryName() {
        return benificieryName;
    }

    public void setBenificieryName(String benificieryName) {
        this.benificieryName = benificieryName;
    }

    public String getBenificieryAccountNumber() {
        return benificieryAccountNumber;
    }

    public void setBenificieryAccountNumber(String benificieryAccountNumber) {
        this.benificieryAccountNumber = benificieryAccountNumber;
    }

    public String getOriginatorBankName() {
        return originatorBankName;
    }

    public void setOriginatorBankName(String originatorBankName) {
        this.originatorBankName = originatorBankName;
    }

    public String getBenificieryBankName() {
        return benificieryBankName;
    }

    public void setBenificieryBankName(String benificieryBankName) {
        this.benificieryBankName = benificieryBankName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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
}
