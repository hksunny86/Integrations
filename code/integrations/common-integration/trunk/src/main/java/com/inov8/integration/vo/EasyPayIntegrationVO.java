package com.inov8.integration.vo;

import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EasyPayIntegrationVO implements IntegrationMessageVO, Serializable {


    private static final long serialVersionUID = -4156867531272113046L;
    private String microbankTransactionCode;
    private String responseCode;
    private String responseDescription;
    private String messageAsEdi;

    private String username;
    private String password;

    private String storeId;
    private String storeName;
    private String orderId;
    private String amount;
    private String transactionType;
    private String emailAddress;
    private String msisdn;
    private String mobileNo;
    private String accountNo;

    private String merchantAccountNo;
    private String paymentToken;
    private String transactionId;
    private String transactionStatus;
    private String transactionRefNumber;

    private String merchantID;
    private String merchantName;
    private String merchantLocation;

    private Date transactionDate;
    private Date paymentTokenExpiryDate;
    private Date transactionPaidDate;
    private Date fromDate;
    private Date toDate;

    private String nfcTagId;

    private PaymentNotificationVO paymentNotificationVO;

    private List<EasyPayTransactionDetailVO> easyPayTransactionDetailVOList;

    private String cardType;
    private String securityCode;
    private String cardNo;
    private String expiryMonth;
    private String expiryYear;
    private String authorizationId;
    private String batchNumber;

    private String conversationID;
    private String originalConversationID;

    private String qrCode;
    private String firstName;
    private String lastName;
    private String CNIC;
    private String gender;
    private String dateOfBirth;
    private String retrievalReferenceNumber;
    private String originalRetrievalReferenceNumber;
    private String transactionDateTime;
    private String originalTransactionDateTime;

    private String bankID;
    private String paymentModeId;

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getOriginalTransactionDateTime() {
        return originalTransactionDateTime;
    }

    public void setOriginalTransactionDateTime(String originalTransactionDateTime) {
        this.originalTransactionDateTime = originalTransactionDateTime;
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

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
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

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getOriginalRetrievalReferenceNumber() {
        return originalRetrievalReferenceNumber;
    }

    public void setOriginalRetrievalReferenceNumber(String originalRetrievalReferenceNumber) {
        this.originalRetrievalReferenceNumber = originalRetrievalReferenceNumber;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getMessageAsEdi() {
        return messageAsEdi;
    }

    public void setMessageAsEdi(String messageAsEdi) {
        this.messageAsEdi = messageAsEdi;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public PaymentNotificationVO getPaymentNotificationVO() {
        return paymentNotificationVO;
    }

    public void setPaymentNotificationVO(PaymentNotificationVO paymentNotificationVO) {
        this.paymentNotificationVO = paymentNotificationVO;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionRefNumber() {
        return transactionRefNumber;
    }

    public void setTransactionRefNumber(String transactionRefNumber) {
        this.transactionRefNumber = transactionRefNumber;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getPaymentTokenExpiryDate() {
        return paymentTokenExpiryDate;
    }

    public void setPaymentTokenExpiryDate(Date paymentTokenExpiryDate) {
        this.paymentTokenExpiryDate = paymentTokenExpiryDate;
    }

    public Date getTransactionPaidDate() {
        return transactionPaidDate;
    }

    public void setTransactionPaidDate(Date transactionPaidDate) {
        this.transactionPaidDate = transactionPaidDate;
    }

    public String getMerchantAccountNo() {
        return merchantAccountNo;
    }

    public void setMerchantAccountNo(String merchantAccountNo) {
        this.merchantAccountNo = merchantAccountNo;
    }

    public String getNfcTagId() {
        return nfcTagId;
    }

    public void setNfcTagId(String nfcTagId) {
        this.nfcTagId = nfcTagId;
    }

    @Override
    public String toString() {
        return "EasyPayIntegrationVO{" +
                "microbankTransactionCode='" + microbankTransactionCode + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDescription='" + responseDescription + '\'' +
                ", messageAsEdi='" + messageAsEdi + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", storeId='" + storeId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount='" + amount + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", merchantAccountNo='" + merchantAccountNo + '\'' +
                ", paymentToken='" + paymentToken + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", transactionRefNumber='" + transactionRefNumber + '\'' +
                ", transactionDate=" + transactionDate +
                ", paymentTokenExpiryDate=" + paymentTokenExpiryDate +
                ", transactionPaidDate=" + transactionPaidDate +
                ", paymentNotificationVO=" + paymentNotificationVO +
                '}';
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

    @Override
    public String getPaymentGatewayCode() {
        return null;
    }

    @Override
    public void setPaymentGatewayCode(String code) {

    }

    @Override
    public String getSystemTraceAuditNumber() {
        return null;
    }

    @Override
    public String getTransmissionDateAndTime() {
        return null;
    }

    @Override
    public String getRetrievalReferenceNumber() {
        return null;
    }

    @Override
    public List<CustomerAccount> getCustomerAccounts() {
        return null;
    }

    @Override
    public String getSecureVerificationData() {
        return null;
    }

    @Override
    public void setSecureVerificationData(String secureVerificationData) {

    }

    @Override
    public String getIvrChannelStatus() {
        return null;
    }

    @Override
    public String getMobileChannelStatus() {
        return null;
    }

    @Override
    public long getTimeOutInterval() {
        return 0;
    }

    @Override
    public void setTimeOutInterval(long timeOut) {

    }

    public List<EasyPayTransactionDetailVO> getEasyPayTransactionDetailVOList() {
        return easyPayTransactionDetailVOList;
    }

    public void setEasyPayTransactionDetailVOList(List<EasyPayTransactionDetailVO> easyPayTransactionDetailVOList) {
        this.easyPayTransactionDetailVOList = easyPayTransactionDetailVOList;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(String authorizationId) {
        this.authorizationId = authorizationId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantLocation() {
        return merchantLocation;
    }

    public void setMerchantLocation(String merchantLocation) {
        this.merchantLocation = merchantLocation;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getOriginalConversationID() {
        return originalConversationID;
    }

    public void setOriginalConversationID(String originalConversationID) {
        this.originalConversationID = originalConversationID;
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }
}
