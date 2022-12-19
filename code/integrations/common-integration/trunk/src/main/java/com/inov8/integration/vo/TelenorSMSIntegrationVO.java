package com.inov8.integration.vo;

import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

import java.util.List;

public class TelenorSMSIntegrationVO implements IntegrationMessageVO {

    private static final long serialVersionUID = 7095786199346683167L;

    private String responseCode;
    private String responseDescription;
    private String transactionCode;
    private String retrievalReferenceNumber;
    private String messageAsEdi;


    private String sessionId;
    private String mobileNo;
    private String text;
    private String mask;
    private String messageId;

    @Override
    public String getPaymentGatewayCode() {
        return null;
    }

    @Override
    public void setPaymentGatewayCode(String code) {

    }

    @Override
    public String getMicrobankTransactionCode() {
        return transactionCode;
    }

    @Override
    public void setMicrobankTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
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
    public String getMessageAsEdi() {
        return messageAsEdi;
    }

    public void setMessageAsEdi(String messageAsEdi) {
        this.messageAsEdi = messageAsEdi;
    }

    @Override
    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
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
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
