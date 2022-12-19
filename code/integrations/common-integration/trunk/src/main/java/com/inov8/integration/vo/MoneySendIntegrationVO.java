package com.inov8.integration.vo;


import java.io.Serializable;

public class MoneySendIntegrationVO implements Serializable{

    private static final long serialVersionUID = -6546545646545648971L;

    private String subscriberId;
    private String localDate;
    private String localTime;
    private String transactionReference;

    private String senderFirstName;
    private String senderMiddleName;
    private String senderLastName;
    private String senderLine1;
    private String senderCity;
    private String senderCountrySubdivision;
    private String senderCountry;
    private String  senderPostalCode;
    private String senderPhone;
    private String senderDateOfBirth;

    private String fundCardAccountNumber;
    private String fundingSource;
    private String additionalMessage;
    private String languageIdentification;
    private String languageData;
    private String ICA;
    private String processorId;
    private String routingAndTransitNumber;
    private String transactionDesc;
    private String merchantId;
    private String channel;

    private String receiverFirstName;
    private String receiverMiddleName;
    private String receiverLastName;

    private String receiverLine1;
    private String receiverCity;
    private String receiverCountrySubdivision;
    private String receiverCountry;
    private String  receiverPostalCode;
    private String receiverDateOfBirth;
    private String   receivingCardAccountNumber;
    private String  receivingAmountValue;
    private String  receivingAmountCurreny;

    private String cardAcceptorName;
    private String cardAcceptorCity;
    private String cardAcceptorState;
    private String cardAcceptorPostalCode;
    private String cardAcceptorCountry;
    private String reversalReason;
    //response fields;
    private String requestID;
    private String transactionType;
    private String systemTraceAuditNumber;
    private String networkReferenceNumber;
    private String settlementDate;
    private String submitDateTime;
    private String responseCode;
    private String responseDescription;
    private String messageAsEdi;

    private String bankCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderMiddleName() {
        return senderMiddleName;
    }

    public void setSenderMiddleName(String senderMiddleName) {
        this.senderMiddleName = senderMiddleName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderLine1() {
        return senderLine1;
    }

    public void setSenderLine1(String senderLine1) {
        this.senderLine1 = senderLine1;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderCountrySubdivision() {
        return senderCountrySubdivision;
    }

    public void setSenderCountrySubdivision(String senderCountrySubdivision) {
        this.senderCountrySubdivision = senderCountrySubdivision;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderPostalCode() {
        return senderPostalCode;
    }

    public void setSenderPostalCode(String senderPostalCode) {
        this.senderPostalCode = senderPostalCode;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderDateOfBirth() {
        return senderDateOfBirth;
    }

    public void setSenderDateOfBirth(String senderDateOfBirth) {
        this.senderDateOfBirth = senderDateOfBirth;
    }

    public String getFundCardAccountNumber() {
        return fundCardAccountNumber;
    }

    public void setFundCardAccountNumber(String fundCardAccountNumber) {
        this.fundCardAccountNumber = fundCardAccountNumber;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }

    public String getLanguageIdentification() {
        return languageIdentification;
    }

    public void setLanguageIdentification(String languageIdentification) {
        this.languageIdentification = languageIdentification;
    }

    public String getLanguageData() {
        return languageData;
    }

    public void setLanguageData(String languageData) {
        this.languageData = languageData;
    }

    public String getICA() {
        return ICA;
    }

    public void setICA(String ICA) {
        this.ICA = ICA;
    }

    public String getProcessorId() {
        return processorId;
    }

    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }

    public String getRoutingAndTransitNumber() {
        return routingAndTransitNumber;
    }

    public void setRoutingAndTransitNumber(String routingAndTransitNumber) {
        this.routingAndTransitNumber = routingAndTransitNumber;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getReceiverFirstName() {
        return receiverFirstName;
    }

    public void setReceiverFirstName(String receiverFirstName) {
        this.receiverFirstName = receiverFirstName;
    }

    public String getReceiverMiddleName() {
        return receiverMiddleName;
    }

    public void setReceiverMiddleName(String receiverMiddleName) {
        this.receiverMiddleName = receiverMiddleName;
    }

    public String getReceiverLastName() {
        return receiverLastName;
    }

    public void setReceiverLastName(String receiverLastName) {
        this.receiverLastName = receiverLastName;
    }

    public String getReceiverLine1() {
        return receiverLine1;
    }

    public void setReceiverLine1(String receiverLine1) {
        this.receiverLine1 = receiverLine1;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverCountrySubdivision() {
        return receiverCountrySubdivision;
    }

    public void setReceiverCountrySubdivision(String receiverCountrySubdivision) {
        this.receiverCountrySubdivision = receiverCountrySubdivision;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverPostalCode() {
        return receiverPostalCode;
    }

    public void setReceiverPostalCode(String receiverPostalCode) {
        this.receiverPostalCode = receiverPostalCode;
    }

    public String getReceiverDateOfBirth() {
        return receiverDateOfBirth;
    }

    public void setReceiverDateOfBirth(String receiverDateOfBirth) {
        this.receiverDateOfBirth = receiverDateOfBirth;
    }

    public String getReceivingCardAccountNumber() {
        return receivingCardAccountNumber;
    }

    public void setReceivingCardAccountNumber(String receivingCardAccountNumber) {
        this.receivingCardAccountNumber = receivingCardAccountNumber;
    }

    public String getReceivingAmountValue() {
        return receivingAmountValue;
    }

    public void setReceivingAmountValue(String receivingAmountValue) {
        this.receivingAmountValue = receivingAmountValue;
    }

    public String getReceivingAmountCurreny() {
        return receivingAmountCurreny;
    }

    public void setReceivingAmountCurreny(String receivingAmountCurreny) {
        this.receivingAmountCurreny = receivingAmountCurreny;
    }

    public String getCardAcceptorName() {
        return cardAcceptorName;
    }

    public void setCardAcceptorName(String cardAcceptorName) {
        this.cardAcceptorName = cardAcceptorName;
    }

    public String getCardAcceptorCity() {
        return cardAcceptorCity;
    }

    public void setCardAcceptorCity(String cardAcceptorCity) {
        this.cardAcceptorCity = cardAcceptorCity;
    }

    public String getCardAcceptorState() {
        return cardAcceptorState;
    }

    public void setCardAcceptorState(String cardAcceptorState) {
        this.cardAcceptorState = cardAcceptorState;
    }

    public String getCardAcceptorPostalCode() {
        return cardAcceptorPostalCode;
    }

    public void setCardAcceptorPostalCode(String cardAcceptorPostalCode) {
        this.cardAcceptorPostalCode = cardAcceptorPostalCode;
    }

    public String getCardAcceptorCountry() {
        return cardAcceptorCountry;
    }

    public void setCardAcceptorCountry(String cardAcceptorCountry) {
        this.cardAcceptorCountry = cardAcceptorCountry;
    }

    public String getReversalReason() {
        return reversalReason;
    }

    public void setReversalReason(String reversalReason) {
        this.reversalReason = reversalReason;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getSystemTraceAuditNumber() {
        return systemTraceAuditNumber;
    }

    public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
        this.systemTraceAuditNumber = systemTraceAuditNumber;
    }

    public String getNetworkReferenceNumber() {
        return networkReferenceNumber;
    }

    public void setNetworkReferenceNumber(String networkReferenceNumber) {
        this.networkReferenceNumber = networkReferenceNumber;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getSubmitDateTime() {
        return submitDateTime;
    }

    public void setSubmitDateTime(String submitDateTime) {
        this.submitDateTime = submitDateTime;
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
}

