package com.inov8.microbank.server.service.switchmodule.iris.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inov8.integration.middleware.bop.avenza.MiniStatementBlk;
import com.inov8.integration.middleware.prisim.*;
import com.inov8.integration.vo.AccountInfoVO;
import com.inov8.integration.vo.CardInfoVO;
import com.inov8.integration.vo.StatementRowVO;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;


/**
 * Project Name: Financial-Integration
 *
 * @author Imran Sarwar Creation Date: Nov 5, 2007 Creation Time: 1:25:15 PM Description:
 */
public class PhoenixIntegrationMessageVO implements IntegrationMessageVO, Serializable {

    protected static String STAN_PADDING_CHAR = "0";
    protected static int STAN_LENGTH = 6;
    protected static boolean STAN_LEFT_PAD = true;

    protected static String RRN_PADDING_CHAR = "0";
    protected static int RRN_LENGTH = 12;
    protected static boolean RRN_LEFT_PAD = true;

    private long timeOutInterval;

    public long getTimeOutInterval() {
        return timeOutInterval;
    }

    public void setTimeOutInterval(long timeOut) {
        this.timeOutInterval = timeOut;
    }

    /**
     *
     */
    private static final long serialVersionUID = -3857122069685739201L;

    private boolean isNetworkMessage;

    private String paymentGatewayCode;
    private String microbankTransactionCode;
    private Long microbankTransactionCodeId;
    private Long actionLogId;
    private String serviceUrl;


    private String protocol;
    private String version;
    private String fieldInError;
    private String messageType;
    private String transmissionDateAndTime;
    private String deliveryChannelType = IntegrationConstants.PhoenixChannelTypes.MOBILE_BANKING.getChannelType();
    private String deliveryChannelId;
    private String customerIdentification;
    private String transactionCode;
    private String transactionDate;
    private String transactionTime;
    private String retrievalReferenceNumber;
    private String customerPINData;
    private String channelSpecificDataField;
    private String channelPrivateData;
    private String authorizationResponseId;
    private String responseCode;
    private String responseDescription;
    private String merchantType;
    private String merchantNameAndLocation;

    private String destinationChannelType;
    private String agentID;


    /**
     * Not used just introduced to implement to handle base class abstract methods
     */
    private String systemTraceAuditNumber;
    private String secureVerificationData;
    private List<CustomerAccount> accounts;

    /**
     * This field (accountDetail) is a cumulative field consist of either of below one set
     * 1) Account Name, Type and Currency
     * 2) Card Number, Card Expiry and Reserved
     */
    private String accountDetail;

    private String customerId;
    private String noOfAccounts;
    private String utilityCompanyId;
    private String consumerNumber;
    private String amountPaid;

    private String cardRequestResponse;
    private String miniStatementType;
    private String additionalData;
    private String transactionLength;
    private String transactionBlock;
    private String channelPassword;
    private String newPIN;
    private String PAN;
    private String DCType = IntegrationConstants.PhoenixChannelTypes.MOBILE_BANKING.getChannelType();
    private String reqChannelStatus;
    private String cardStatusCode;

    // Customer Account related fields
    public static final String ACC_STATUS_ACTIVE = "00";
    public static final String ACC_STATUS_INACTIVE = "01";

    private String accountNumber;
    private String accountType;
    private String accountCurrency;
    private String accountStatus;
    private String availableBalance;
    private String actualBalance;
    private String withdrawalLimit;
    private String availableWithdrawalLimit;
    private String purchaseLimit;
    private String availablePurchaseLimit;
    private String draftLimit;
    private String limitExpiryDate;
    private String currencyName;
    private String currencyMnemonic;
    private String currencyDecimalPoints;

    private String openingBalance;
    private String transactionAmount;
    private String transactionCurrency;
    private String titleOfTheAccount;
    private String accountBankIMD = "600648";
    private String accountBankName;
    private String accountBranchName;
    private String accountBranchCode;

    //IBFT FIELDS
    private String fromAccountBankIMD;
    private String toAccountBankIMD;
    private String toAccountBankName;
    private String toAccountBranchName;
    private String transactionFee;

    // Open account fund transfer related fields
    private String toAccountNumber;
    private String toAccountType;
    private String toAccountCurrency;

    // Bill Inquiry related fields
    private String subscriberName;
    private String billingMonth;
    private String dueDatePayableAmount;
    private String paymentDueDate;
    private String paymentAfterDueDate;
    private String billStatus;
    private String paymentAuthResponseId;
    private String netCED;
    private String netWithholdingTAX;

    //Customer Profile related fields
    private String NIC;
    private String newNIC;
    private String customerID;
    private String title;
    private String fullName;
    private String dateOfBirth;
    private String motherMaidenName;
    private String address;
    private String postalCode;
    private String telephoneNumber;
    private String mobileNumber;
    private String email;
    private String email1;
    private String email2;
    private String defaultEmail;
    private String company;
    private String CompanyAddress;
    private String companyPostalCode;
    private String companyTelephone;
    private String anniversaryDate;
    private String correspondenceFlag;
    private String ivrChannelStatus;
    private String mobileChannelStatus;
    private String callCenterChannelStatus;
    private String internetBankingChannelStatus;
    private String reserved;
    private String serviceName;
    private String statementLimit;
    private String desc;
    private String operationName;
    private String channel;
    private String text;
    private String cardType;
    private List<MiniStatementBlk> miniStatementblk;

    // MiniStatement related fields

    private String transactionCount;
    private List<Map<String, String>> transactionsMap = new ArrayList<Map<String, String>>();
    private List<CardInfoVO> cardRelationships = new ArrayList<CardInfoVO>();
    private List<AccountInfoVO> accountRelationships = new ArrayList<AccountInfoVO>();
    private List<StatementRowVO> statementRowList;
    private Map<String, String> miniStatementRows;

    // Credit Card related fields

    private String cardNumber;
    private String cardName;
    private String cardExpiry;
    private String reserved1;
    private String closingBalance;
    private String dueDate;
    private String minPaymentDue;
    private String currentBalance;
    private String rewardPointsEarned;
    private String reserved2;
    private String availableCreditLimit;
    private String customerName;
    private String reserved3;
    private String messageAsEdi;
    private String channelId;
    private String fromAccountNumber;
    private String fromAccountType;
    private String fromAccountCurrency;
    private String creditLimit;
    private String lastPaymentAmount;
    private String lastPaymentDate;
    private String totalCurrentSpending;
    private String billingCurrencyCode;
    private String authorizationCode;
    private String statusCode;
    private String plasticCode;
    private String feeWaiverSpending;
    private String previousBalance;
    private String totalCashAdvanceLimit;
    private String availableCashAdvanceLimit;
    private String outstandingBalance;
    private String rewardPointsRedeemed;
    private String rewardPointsAvailable;

    // Nadra Integration Related Fields

    private String kioskId;
    private String username;
    private String password;
    private String consumerNo;
    private String trackingId;
    private String companyName;
    private String status;
    private String consumerName;
    private String billAmount;
    private String lateAmount;
    private String billMonth;
    private List<CompanyVO> companiesList = new ArrayList<CompanyVO>();


    // IDP related fields
    private int fingerIndex;
    private byte[] fingerTemplate;
    private String templateType;
    private String agentName;
    private String paymentStatus;
    private String verificationId;
    private String beneficiaryName;
    private String isFlood;
    private String responseMessage;

    private String issuer;
    private String currencyCode;
    private String processingCode;
    private String stan;
    private String accountBalance;
    private String accountLimit;

    private String orignalSTAN;
    private String orignalTransactionDate;
    private String orignalTransactionTime;
    private String orignalTransmissionDateTime;
    private String reversalAmount;

    private String newPassword;
    private List<CardVO> cardList;

    private String bookletSize;
    private String numberOfBooklets;
    private String bookletType;
    private String printName;
    private String methodOfCollection;
    private String statementDuration;
    private String fromDate;
    private String toDate;
    private String accountTypeCode;
    private String statementPeriodType;
    private String dateRange;
    private String transactionType;
    private String transactionResponseCode;
    private String transactionReferencNumber;
    private String accountTitle;
    private String accountTypeValue;
    private String numberOfLeaves;
    private String numberOfMonths;
    private String behaviorFlag;
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
    private String personTitle;
    private String channelStatus;
    private String isMiniStatment;
    private Date dateLocalTrasaction;
    private String OrignalRetrievalReferenceNumber;
    private String birthPlace;
    private String nicStatus;
    private String nicExpirty;
    private String fatherHusbandName;
    private String gender;
    private String charges;
    private String customerStatus;
    private String terminalId;
    private String paymentType;
    private String transactionId;
    private String firstName;
    private String lastName;
    private String otp;
    private String departmentCode;
    private String departmentName;
    private String challanDate;
    private String receiveDate;
    private String districtCode;

    private String mobilePin;
    private String financialPin;
    //  bop related fields

    private String purposeOfPayment;
    private String originatorName;
    private String benificieryName;
    private String benificieryAccountNumber;
    private String originatorBankName;
    private String benificieryBankName;
    private String benificieryIBAN;
    private String senderName;
    private String senderIBAN;
    private String atmChargesFlag;
    private String transactionAmountFee;


    private String totalLoanAmount;
    private String totalOutstandingAmount;
    private String feeAmount;
    private String maxLimit;
    private String minLimit;


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


    public String getTransactionAmountFee() {
        return transactionAmountFee;
    }

    public void setTransactionAmountFee(String transactionAmountFee) {
        this.transactionAmountFee = transactionAmountFee;
    }

    public String getAtmChargesFlag() {
        return atmChargesFlag;
    }

    public void setAtmChargesFlag(String atmChargesFlag) {
        this.atmChargesFlag = atmChargesFlag;
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

    public PhoenixIntegrationMessageVO() {

    }

    public PhoenixIntegrationMessageVO(boolean isNetworkMessage, String paymentGatewayCode, String microbankTransactionCode) {
        super();
        this.isNetworkMessage = isNetworkMessage;
        this.paymentGatewayCode = paymentGatewayCode;
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getToAccountCurrency() {
        return toAccountCurrency;
    }

    public void setToAccountCurrency(String toAccountCurrency) {
        this.toAccountCurrency = toAccountCurrency;
    }

    /**
     * @return the isNetworkMessage
     */
    public boolean isNetworkMessage() {
        return isNetworkMessage;
    }

    /**
     * @return the secureVerificationData
     */
    public String getSecureVerificationData() {
        return secureVerificationData;
    }

    /**
     * @param secureVerificationData the secureVerificationData to set
     */
    public void setSecureVerificationData(String secureVerificationData) {
        this.secureVerificationData = secureVerificationData;
    }

    /**
     * @return the authorizationResponseId
     */
    public String getAuthorizationResponseId() {
        return authorizationResponseId;
    }

    /**
     * @param authorizationResponseId the authorizationResponseId to set
     */
    public void setAuthorizationResponseId(String authorizationResponseId) {
        this.authorizationResponseId = authorizationResponseId;
    }

    /**
     * @return the deliveryChannelId
     */
    public String getDeliveryChannelId() {
        return deliveryChannelId;
    }

    /**
     * @param deliveryChannelId the deliveryChannelId to set
     */
    public void setDeliveryChannelId(String deliveryChannelId) {
        this.deliveryChannelId = deliveryChannelId;
    }

    /**
     * @return the destinationChannelType
     */
    public String getDestinationChannelType() {
        return destinationChannelType;
    }

    /**
     * @param destinationChannelType the destinationChannelType to set
     */
    public void setDestinationChannelType(String destinationChannelType) {
        this.destinationChannelType = destinationChannelType;
    }

    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the retrievalReferenceNumber
     */
    public String getRetrievalReferenceNumber() {
//		return addPadding(RRN_PADDING_CHAR, RRN_LEFT_PAD, RRN_LENGTH, retrievalReferenceNumber);
        return retrievalReferenceNumber;
    }

    /**
     * @param retrievalReferenceNumber the retrievalReferenceNumber to set
     */
    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
//		this.retrievalReferenceNumber = addPadding(RRN_PADDING_CHAR, RRN_LEFT_PAD, RRN_LENGTH, retrievalReferenceNumber);
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    /**
     * @return the transactionCode
     */
    public String getTransactionCode() {
        return transactionCode;
    }

    /**
     * @param transactionCode the transactionCode to set
     */
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    /**
     * @return the transactionLocalDate
     */
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionLocalDate the transactionLocalDate to set
     */
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the transactionLocalTime
     */
    public String getTransactionTime() {
        return transactionTime;
    }

    /**
     * @param transactionLocalTime the transactionLocalTime to set
     */
    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    /**
     * @return the transmissionDateAndTime
     */
    public String getTransmissionDateAndTime() {
        return transmissionDateAndTime;
    }

    /**
     * @param transmissionDateAndTime the transmissionDateAndTime to set
     */
    public void setTransmissionDateAndTime(String transmissionDateAndTime) {
        this.transmissionDateAndTime = transmissionDateAndTime;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public void addAccount(CustomerAccount customerAccount) {
        this.accounts.add(customerAccount);
    }

    /**
     * @return the systemTraceAuditNumber
     */
    public String getSystemTraceAuditNumber() {
        return addPadding(STAN_PADDING_CHAR, STAN_LEFT_PAD, STAN_LENGTH, systemTraceAuditNumber);
    }

    /**
     * @param systemTraceAuditNumber the systemTraceAuditNumber to set
     */
    public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
        this.systemTraceAuditNumber = addPadding(STAN_PADDING_CHAR, STAN_LEFT_PAD, STAN_LENGTH, systemTraceAuditNumber);
    }

    /**
     * @return the amountPaid
     */
    public String getAmountPaid() {
        return amountPaid;
    }

    /**
     * @param amountPaid the amountPaid to set
     */
    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
     * @return the consumerNumber
     */
    public String getConsumerNumber() {
        return consumerNumber;
    }

    /**
     * @param consumerNumber the consumerNumber to set
     */
    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the noOfAccounts
     */
    public String getNoOfAccounts() {
        return noOfAccounts;
    }

    /**
     * @param noOfAccounts the noOfAccounts to set
     */
    public void setNoOfAccounts(String noOfAccounts) {
        this.noOfAccounts = noOfAccounts;
    }

    /**
     * @return the utilityCompanyId
     */
    public String getUtilityCompanyId() {
        return utilityCompanyId;
    }

    /**
     * @param utilityCompanyId the utilityCompanyId to set
     */
    public void setUtilityCompanyId(String utilityCompanyId) {
        this.utilityCompanyId = utilityCompanyId;
    }

    /**
     * @return the paymentGatewayCode
     */
    public String getPaymentGatewayCode() {
        return paymentGatewayCode;
    }

    /**
     * @param paymentGatewayCode the paymentGatewayCode to set
     */
    public void setPaymentGatewayCode(String paymentGatewayCode) {
        this.paymentGatewayCode = paymentGatewayCode;
    }

    /**
     * @return the microbankTransactionCode
     */
    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setCustomerAccounts(List<CustomerAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the accounts
     */
    public List<CustomerAccount> getCustomerAccounts() {
        return accounts;
    }

    /**
     * @return the messageAsEdi
     */
    public String getMessageAsEdi() {
        return messageAsEdi;
    }

    /**
     * @param messageAsEdi the messageAsEdi to set
     */
    public void setMessageAsEdi(String messageAsEdi) {
        this.messageAsEdi = messageAsEdi;
    }

    public void setNetworkMessage(boolean isNetworkMessage) {
        this.isNetworkMessage = isNetworkMessage;
    }

    public static String addPadding(String padCharacter, boolean leftPad, int requiredLength, String value) {
        String input = defaultString(value).trim();
        if (leftPad)
            return leftPad(input, requiredLength, padCharacter);
        else
            return rightPad(input, requiredLength, padCharacter);
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (null == padStr || "".equals(str)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= 8192) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > 8192) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(padding(pads, padChar));
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return padding(pads, padChar).concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (null == padStr || "".equals(str)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= 8192) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    public static String defaultString(String str) {
        return str == null ? "" : str;
    }


    public static void main(String[] args) {
        PhoenixIntegrationMessageVO vo = new PhoenixIntegrationMessageVO(false, "", "");
        //vo.setSystemTraceAuditNumber("520");
        System.out.println(">>" + vo.getRetrievalReferenceNumber());
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getFieldInError() {
        return fieldInError;
    }

    public void setFieldInError(String fieldInError) {
        this.fieldInError = fieldInError;
    }

    public String getCustomerPINData() {
        return customerPINData;
    }

    public void setCustomerPINData(String customerPINData) {
        this.customerPINData = customerPINData;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getChannelSpecificDataField() {
        return channelSpecificDataField;
    }

    public void setChannelSpecificDataField(String channelSpecificDataField) {
        this.channelSpecificDataField = channelSpecificDataField;
    }

    public String getChannelPrivateData() {
        return channelPrivateData;
    }

    public void setChannelPrivateData(String channelPrivateData) {
        this.channelPrivateData = channelPrivateData;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardRequestResponse() {
        return cardRequestResponse;
    }

    public void setCardRequestResponse(String cardRequestResponse) {
        this.cardRequestResponse = cardRequestResponse;
    }

    public String getMiniStatementType() {
        return miniStatementType;
    }

    public void setMiniStatementType(String miniStatementType) {
        this.miniStatementType = miniStatementType;
    }

    public List<StatementRowVO> getStatementRowList() {
        return statementRowList;
    }

    public void setStatementRowList(List<StatementRowVO> statementRowList) {
        this.statementRowList = statementRowList;
    }


    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getTransactionLength() {
        return transactionLength;
    }

    public void setTransactionLength(String transactionLength) {
        this.transactionLength = transactionLength;
    }

    public String getTransactionBlock() {
        return transactionBlock;
    }

    public void setTransactionBlock(String transactionBlock) {
        this.transactionBlock = transactionBlock;
    }

    public String getChannelPassword() {
        return channelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        this.channelPassword = channelPassword;
    }

    public String getNewPIN() {
        return newPIN;
    }

    public void setNewPIN(String newPIN) {
        this.newPIN = newPIN;
    }

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getReqChannelStatus() {
        return reqChannelStatus;
    }

    public void setReqChannelStatus(String reqChannelStatus) {
        this.reqChannelStatus = reqChannelStatus;
    }

    public String getCardStatusCode() {
        return cardStatusCode;
    }

    public void setCardStatusCode(String cardStatusCode) {
        this.cardStatusCode = cardStatusCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(String actualBalance) {
        this.actualBalance = actualBalance;
    }

    public String getDraftLimit() {
        return draftLimit;
    }

    public void setDraftLimit(String draftLimit) {
        this.draftLimit = draftLimit;
    }

    public String getLimitExpiryDate() {
        return limitExpiryDate;
    }

    public void setLimitExpiryDate(String limitExpiryDate) {
        this.limitExpiryDate = limitExpiryDate;
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

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTitleOfTheAccount() {
        return titleOfTheAccount;
    }

    public void setTitleOfTheAccount(String titleOfTheAccount) {
        this.titleOfTheAccount = titleOfTheAccount;
    }

    public String getAccountBankIMD() {
        return accountBankIMD;
    }

    public void setAccountBankIMD(String accountBankIMD) {
        this.accountBankIMD = accountBankIMD;
    }


    public String getNIC() {
        return NIC;
    }

    public void setNIC(String nic) {
        NIC = nic;
    }

    public String getNewNIC() {
        return newNIC;
    }

    public void setNewNIC(String newNIC) {
        this.newNIC = newNIC;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public String getCompanyPostalCode() {
        return companyPostalCode;
    }

    public void setCompanyPostalCode(String companyPostalCode) {
        this.companyPostalCode = companyPostalCode;
    }

    public String getCompanyTelephone() {
        return companyTelephone;
    }

    public void setCompanyTelephone(String companyTelephone) {
        this.companyTelephone = companyTelephone;
    }

    public String getAnniversaryDate() {
        return anniversaryDate;
    }

    public void setAnniversaryDate(String anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
    }

    public String getCorrespondenceFlag() {
        return correspondenceFlag;
    }

    public void setCorrespondenceFlag(String correspondenceFlag) {
        this.correspondenceFlag = correspondenceFlag;
    }

    public String getIvrChannelStatus() {
        return ivrChannelStatus;
    }

    public void setIvrChannelStatus(String ivrChannelStatus) {
        this.ivrChannelStatus = ivrChannelStatus;
    }

    public String getMobileChannelStatus() {
        return mobileChannelStatus;
    }

    public void setMobileChannelStatus(String mobileChannelStatus) {
        this.mobileChannelStatus = mobileChannelStatus;
    }

    public String getCallCenterChannelStatus() {
        return callCenterChannelStatus;
    }

    public void setCallCenterChannelStatus(String callCenterChannelStatus) {
        this.callCenterChannelStatus = callCenterChannelStatus;
    }

    public String getInternetBankingChannelStatus() {
        return internetBankingChannelStatus;
    }

    public void setInternetBankingChannelStatus(String internetBankingChannelStatus) {
        this.internetBankingChannelStatus = internetBankingChannelStatus;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }


    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getMinPaymentDue() {
        return minPaymentDue;
    }

    public void setMinPaymentDue(String minPaymentDue) {
        this.minPaymentDue = minPaymentDue;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getRewardPointsEarned() {
        return rewardPointsEarned;
    }

    public void setRewardPointsEarned(String rewardPointsEarned) {
        this.rewardPointsEarned = rewardPointsEarned;
    }

    public String getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    public void setAvailableCreditLimit(String availableCreditLimit) {
        this.availableCreditLimit = availableCreditLimit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(String accountDetail) {
        this.accountDetail = accountDetail;
    }

    public String getDeliveryChannelType() {
        return deliveryChannelType;
    }

    public void setDeliveryChannelType(String deliveryChannelType) {
        this.deliveryChannelType = deliveryChannelType;
    }

    public String getCustomerIdentification() {
        return customerIdentification;
    }

    public void setCustomerIdentification(String customerIdentification) {
        this.customerIdentification = customerIdentification;
    }

    public String getDCType() {
        return DCType;
    }

    public void setDCType(String type) {
        DCType = type;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
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

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getDueDatePayableAmount() {
        return dueDatePayableAmount;
    }

    public void setDueDatePayableAmount(String dueDatePayableAmount) {
        this.dueDatePayableAmount = dueDatePayableAmount;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getPaymentAfterDueDate() {
        return paymentAfterDueDate;
    }

    public void setPaymentAfterDueDate(String paymentAfterDueDate) {
        this.paymentAfterDueDate = paymentAfterDueDate;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getPaymentAuthResponseId() {
        return paymentAuthResponseId;
    }

    public void setPaymentAuthResponseId(String paymentAuthResponseId) {
        this.paymentAuthResponseId = paymentAuthResponseId;
    }

    public String getNetCED() {
        return netCED;
    }

    public void setNetCED(String netCED) {
        this.netCED = netCED;
    }

    public String getNetWithholdingTAX() {
        return netWithholdingTAX;
    }

    public void setNetWithholdingTAX(String netWithholdingTAX) {
        this.netWithholdingTAX = netWithholdingTAX;
    }

    public Long getMicrobankTransactionCodeId() {
        return microbankTransactionCodeId;
    }

    public void setMicrobankTransactionCodeId(Long microbankTransactionCodeId) {
        this.microbankTransactionCodeId = microbankTransactionCodeId;
    }

    public Long getActionLogId() {
        return actionLogId;
    }

    public void setActionLogId(Long actionLogId) {
        this.actionLogId = actionLogId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


    public String getKioskId() {
        return kioskId;
    }

    public void setKioskId(String kioskId) {
        this.kioskId = kioskId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFromAccountBankIMD() {
        return fromAccountBankIMD;
    }

    public void setFromAccountBankIMD(String fromAccountBankIMD) {
        this.fromAccountBankIMD = fromAccountBankIMD;
    }

    public String getToAccountBankIMD() {
        return toAccountBankIMD;
    }

    public void setToAccountBankIMD(String toAccountBankIMD) {
        this.toAccountBankIMD = toAccountBankIMD;
    }

    public String getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(String transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<CompanyVO> getCompaniesList() {
        return companiesList;
    }

    public void setCompaniesList(List<CompanyVO> companiesList) {
        this.companiesList = companiesList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getLateAmount() {
        return lateAmount;
    }

    public void setLateAmount(String lateAmount) {
        this.lateAmount = lateAmount;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
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

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    public void setLastPaymentAmount(String lastPaymentAmount) {
        this.lastPaymentAmount = lastPaymentAmount;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getTotalCurrentSpending() {
        return totalCurrentSpending;
    }

    public void setTotalCurrentSpending(String totalCurrentSpending) {
        this.totalCurrentSpending = totalCurrentSpending;
    }

    public String getBillingCurrencyCode() {
        return billingCurrencyCode;
    }

    public void setBillingCurrencyCode(String billingCurrencyCode) {
        this.billingCurrencyCode = billingCurrencyCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getPlasticCode() {
        return plasticCode;
    }

    public void setPlasticCode(String plasticCode) {
        this.plasticCode = plasticCode;
    }

    public String getFeeWaiverSpending() {
        return feeWaiverSpending;
    }

    public void setFeeWaiverSpending(String feeWaiverSpending) {
        this.feeWaiverSpending = feeWaiverSpending;
    }

    public String getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(String previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getTotalCashAdvanceLimit() {
        return totalCashAdvanceLimit;
    }

    public void setTotalCashAdvanceLimit(String totalCashAdvanceLimit) {
        this.totalCashAdvanceLimit = totalCashAdvanceLimit;
    }

    public String getAvailableCashAdvanceLimit() {
        return availableCashAdvanceLimit;
    }

    public void setAvailableCashAdvanceLimit(String availableCashAdvanceLimit) {
        this.availableCashAdvanceLimit = availableCashAdvanceLimit;
    }

    public String getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(String outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getRewardPointsRedeemed() {
        return rewardPointsRedeemed;
    }

    public void setRewardPointsRedeemed(String rewardPointsRedeemed) {
        this.rewardPointsRedeemed = rewardPointsRedeemed;
    }

    public String getRewardPointsAvailable() {
        return rewardPointsAvailable;
    }

    public void setRewardPointsAvailable(String rewardPointsAvailable) {
        this.rewardPointsAvailable = rewardPointsAvailable;
    }

    public String getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(String transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public List<Map<String, String>> getTransactionsMap() {
        return transactionsMap;
    }

    public void setTransactionsMap(List<Map<String, String>> transactionsMap) {
        this.transactionsMap = transactionsMap;
    }

    public int getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(int fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public byte[] getFingerTemplate() {
        return fingerTemplate;
    }

    public void setFingerTemplate(byte[] fingerTemplate) {
        this.fingerTemplate = fingerTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getIsFlood() {
        return isFlood;
    }

    public void setIsFlood(String isFlood) {
        this.isFlood = isFlood;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(String accountLimit) {
        this.accountLimit = accountLimit;
    }

    public String getOrignalSTAN() {
        return orignalSTAN;
    }

    public void setOrignalSTAN(String orignalSTAN) {
        this.orignalSTAN = orignalSTAN;
    }

    public String getOrignalTransactionDate() {
        return orignalTransactionDate;
    }

    public void setOrignalTransactionDate(String orignalTransactionDate) {
        this.orignalTransactionDate = orignalTransactionDate;
    }

    public String getOrignalTransactionTime() {
        return orignalTransactionTime;
    }

    public void setOrignalTransactionTime(String orignalTransactionTime) {
        this.orignalTransactionTime = orignalTransactionTime;
    }

    public String getBenificieryIBAN() {
        return benificieryIBAN;
    }

    public void setBenificieryIBAN(String benificieryIBAN) {
        this.benificieryIBAN = benificieryIBAN;
    }

    public String getOrignalTransmissionDateTime() {
        return orignalTransmissionDateTime;
    }

    public void setOrignalTransmissionDateTime(String orignalTransmissionDateTime) {
        this.orignalTransmissionDateTime = orignalTransmissionDateTime;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getToAccountBankName() {
        return toAccountBankName;
    }

    public void setToAccountBankName(String toAccountBankName) {
        this.toAccountBankName = toAccountBankName;
    }

    public String getToAccountBranchName() {
        return toAccountBranchName;
    }

    public void setToAccountBranchName(String toAccountBranchName) {
        this.toAccountBranchName = toAccountBranchName;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantNameAndLocation() {
        return merchantNameAndLocation;
    }

    public void setMerchantNameAndLocation(String merchantNameAndLocation) {
        this.merchantNameAndLocation = merchantNameAndLocation;
    }

    public String getReversalAmount() {
        return reversalAmount;
    }

    public void setReversalAmount(String reversalAmount) {
        this.reversalAmount = reversalAmount;
    }

    public List<CardVO> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardVO> cardList) {
        this.cardList = cardList;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<CustomerAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<CustomerAccount> accounts) {
        this.accounts = accounts;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getBookletSize() {
        return bookletSize;
    }

    public void setBookletSize(String bookletSize) {
        this.bookletSize = bookletSize;
    }

    public String getNumberOfBooklets() {
        return numberOfBooklets;
    }

    public void setNumberOfBooklets(String numberOfBooklets) {
        this.numberOfBooklets = numberOfBooklets;
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

    public String getStatementDuration() {
        return statementDuration;
    }

    public void setStatementDuration(String statementDuration) {
        this.statementDuration = statementDuration;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionResponseCode() {
        return transactionResponseCode;
    }

    public void setTransactionResponseCode(String transactionResponseCode) {
        this.transactionResponseCode = transactionResponseCode;
    }

    public String getTransactionReferencNumber() {
        return transactionReferencNumber;
    }

    public void setTransactionReferencNumber(String transactionReferencNumber) {
        this.transactionReferencNumber = transactionReferencNumber;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountTypeValue() {
        return accountTypeValue;
    }

    public void setAccountTypeValue(String accountTypeValue) {
        this.accountTypeValue = accountTypeValue;
    }

    public String getNumberOfLeaves() {
        return numberOfLeaves;
    }

    public void setNumberOfLeaves(String numberOfLeaves) {
        this.numberOfLeaves = numberOfLeaves;
    }

    public String getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(String numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public String getBehaviorFlag() {
        return behaviorFlag;
    }

    public void setBehaviorFlag(String behaviorFlag) {
        this.behaviorFlag = behaviorFlag;
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

    public String getPersonTitle() {
        return personTitle;
    }

    public void setPersonTitle(String personTitle) {
        this.personTitle = personTitle;
    }

    public String getAccountBranchCode() {
        return accountBranchCode;
    }

    public void setAccountBranchCode(String accountBranchCode) {
        this.accountBranchCode = accountBranchCode;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }

    public List<CardInfoVO> getCardRelationships() {
        return cardRelationships;
    }

    public void setCardRelationships(List<CardInfoVO> cardRelationships) {
        this.cardRelationships = cardRelationships;
    }

    public List<AccountInfoVO> getAccountRelationships() {
        return accountRelationships;
    }

    public void setAccountRelationships(List<AccountInfoVO> accountRelationships) {
        this.accountRelationships = accountRelationships;
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

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
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

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public List<MiniStatementBlk> getMiniStatementblk() {
        return miniStatementblk;
    }

    public void setMiniStatementblk(List<MiniStatementBlk> miniStatementblk) {
        this.miniStatementblk = miniStatementblk;
    }

    public String getIsMiniStatment() {
        return isMiniStatment;
    }

    public void setIsMiniStatment(String isMiniStatment) {
        this.isMiniStatment = isMiniStatment;
    }

    public Date getDateLocalTrasaction() {
        return dateLocalTrasaction;
    }

    public void setDateLocalTrasaction(Date dateLocalTrasaction) {
        this.dateLocalTrasaction = dateLocalTrasaction;
    }

    public String getOrignalRetrievalReferenceNumber() {
        return OrignalRetrievalReferenceNumber;
    }

    public void setOrignalRetrievalReferenceNumber(String orignalRetrievalReferenceNumber) {
        OrignalRetrievalReferenceNumber = orignalRetrievalReferenceNumber;
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

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getNicStatus() {
        return nicStatus;
    }

    public void setNicStatus(String nicStatus) {
        this.nicStatus = nicStatus;
    }

    public String getNicExpirty() {
        return nicExpirty;
    }

    public void setNicExpirty(String nicExpirty) {
        this.nicExpirty = nicExpirty;
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

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getMobilePin() {
        return mobilePin;
    }

    public void setMobilePin(String mobilePin) {
        this.mobilePin = mobilePin;
    }

    public String getFinancialPin() {
        return financialPin;
    }

    public void setFinancialPin(String financialPin) {
        this.financialPin = financialPin;
    }

    public Map<String, String> getMiniStatementRows() {
        return miniStatementRows;
    }

    public void setMiniStatementRows(Map<String, String> miniStatementRows) {
        this.miniStatementRows = miniStatementRows;
    }
}
