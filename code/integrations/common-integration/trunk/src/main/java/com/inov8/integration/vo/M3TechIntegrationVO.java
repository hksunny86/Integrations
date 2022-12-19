package com.inov8.integration.vo;

import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

import java.util.List;

/**
 * Created by Zeeshan Ahmad on 6/21/2016.
 */
public class M3TechIntegrationVO implements IntegrationMessageVO {
    private static final long serialVersionUID = 2726231528327955365L;

    private String responseCode;
    private String responseDescription;
    private String transactionCode;
    private String retrievalReferenceNumber;
    private String messageAsEdi;


    private String mobileNo;
    private String accountNumber;
    private String channel;
    private String text;
    private String messageHeader;

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

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "M3TechIntegrationVO{" +
                "responseCode='" + responseCode + '\'' +
                ", responseDescription='" + responseDescription + '\'' +
                ", transactionCode='" + transactionCode + '\'' +
                ", retrievalReferenceNumber='" + retrievalReferenceNumber + '\'' +
                ", messageAsEdi='" + messageAsEdi + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", text='" + text + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", channel='" + channel + '\'' +
                ", messageHeader='" + messageHeader + '\'' +
                '}';
    }
}
