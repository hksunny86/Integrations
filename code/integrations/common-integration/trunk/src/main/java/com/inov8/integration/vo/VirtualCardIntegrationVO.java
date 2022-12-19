package com.inov8.integration.vo;

import com.inov8.integration.middleware.controller.MiddlewareSwitchController;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by inov8 on 8/23/2016.
 */
public class VirtualCardIntegrationVO implements Serializable, IntegrationMessageVO {
    private static final long serialVersionUID = -3145395115894566903L;
    //local fields
    private String terminalId;
    private String reference;
    private String cardIdentifier;
    private String firstName;
    private String lastName;
    private String idOrPassport;
    private String cellphoneNumber;
    private String transactionId;
    private Date transactionDate;
    private Date expiryDate;
    private String cardNumber;
    private String validDate;
    private String cvv2;
    private String trackingNumber;
    private String title;
    private String initials;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String additionalData;
    private String newPIN;
    private String oldCardIdentifier;
    private String newCardIdentifier;
    private String reasonID;
    private String note;
    private boolean valid;
    private boolean pinBlocked;
    private boolean activated;
    private boolean retired;
    private boolean loaded;
    private boolean redeemed;
    private boolean empty;
    private boolean cancelled;
    private boolean lost;
    private boolean stolen;
    private boolean expired;
    private boolean stopped;
    private String code;
    private String narrative;
    private String requestAmount;
    private String transactionData;
    private Date referenceDate;
    private String PINBlock;
    private String referenceId;
    private String voucherNumber;
    private String stopReason;
    private String paymentGatewayCode;
    private String microbankTransactionCode;
    private String systemTraceAuditNumber;
    private String messageAsEdi;
    private String retrievalReferenceNumber;
    private String secureVerificationData;
    private String responseCode;
    private String responseDescription;
    private String ivrChannelStatus;
    private String mobileChannelStatus;
    private long timeOutInterval;
    private String transactionType;
    private String checkSum;

    @Override
    public String toString() {
        return "VirtualCardIntegrationVO{" +
                "terminalId='" + terminalId + '\'' +
                ", reference='" + reference + '\'' +
                ", cardIdentifier='" + cardIdentifier + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", idOrPassport='" + idOrPassport + '\'' +
                ", cellphoneNumber='" + cellphoneNumber + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", transactionDate=" + transactionDate +
                ", expiryDate=" + expiryDate +
                ", cardNumber='" + cardNumber + '\'' +
                ", validDate='" + validDate + '\'' +
                ", cvv2='" + cvv2 + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", title='" + title + '\'' +
                ", initials='" + initials + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", address3='" + address3 + '\'' +
                ", address4='" + address4 + '\'' +
                ", address5='" + address5 + '\'' +
                ", additionalData='" + additionalData + '\'' +
                ", newPIN='" + newPIN + '\'' +
                ", oldCardIdentifier='" + oldCardIdentifier + '\'' +
                ", newCardIdentifier='" + newCardIdentifier + '\'' +
                ", reasonID='" + reasonID + '\'' +
                ", note='" + note + '\'' +
                ", valid=" + valid +
                ", pinBlocked=" + pinBlocked +
                ", activated=" + activated +
                ", retired=" + retired +
                ", loaded=" + loaded +
                ", redeemed=" + redeemed +
                ", empty=" + empty +
                ", cancelled=" + cancelled +
                ", lost=" + lost +
                ", stolen=" + stolen +
                ", expired=" + expired +
                ", stopped=" + stopped +
                ", code='" + code + '\'' +
                ", narrative='" + narrative + '\'' +
                ", requestAmount='" + requestAmount + '\'' +
                ", transactionData='" + transactionData + '\'' +
                ", referenceDate=" + referenceDate +
                ", PINBlock='" + PINBlock + '\'' +
                ", referenceId='" + referenceId + '\'' +
                ", voucherNumber='" + voucherNumber + '\'' +
                ", stopReason='" + stopReason + '\'' +
                ", paymentGatewayCode='" + paymentGatewayCode + '\'' +
                ", microbankTransactionCode='" + microbankTransactionCode + '\'' +
                ", systemTraceAuditNumber='" + systemTraceAuditNumber + '\'' +
                ", messageAsEdi='" + messageAsEdi + '\'' +
                ", retrievalReferenceNumber='" + retrievalReferenceNumber + '\'' +
                ", secureVerificationData='" + secureVerificationData + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDescription='" + responseDescription + '\'' +
                ", ivrChannelStatus='" + ivrChannelStatus + '\'' +
                ", mobileChannelStatus='" + mobileChannelStatus + '\'' +
                ", timeOutInterval=" + timeOutInterval +
                '}';
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCardIdentifier() {
        return cardIdentifier;
    }

    public void setCardIdentifier(String cardIdentifier) {
        this.cardIdentifier = cardIdentifier;
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

    public String getIdOrPassport() {
        return idOrPassport;
    }

    public void setIdOrPassport(String idOrPassport) {
        this.idOrPassport = idOrPassport;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }


    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
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

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getNewPIN() {
        return newPIN;
    }

    public void setNewPIN(String newPIN) {
        this.newPIN = newPIN;
    }

    public String getOldCardIdentifier() {
        return oldCardIdentifier;
    }

    public void setOldCardIdentifier(String oldCardIdentifier) {
        this.oldCardIdentifier = oldCardIdentifier;
    }

    public String getNewCardIdentifier() {
        return newCardIdentifier;
    }

    public void setNewCardIdentifier(String newCardIdentifier) {
        this.newCardIdentifier = newCardIdentifier;
    }

    public String getReasonID() {
        return reasonID;
    }

    public void setReasonID(String reasonID) {
        this.reasonID = reasonID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isPinBlocked() {
        return pinBlocked;
    }

    public void setPinBlocked(boolean pinBlocked) {
        this.pinBlocked = pinBlocked;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean isStolen() {
        return stolen;
    }

    public void setStolen(boolean stolen) {
        this.stolen = stolen;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(String requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(String transactionData) {
        this.transactionData = transactionData;
    }

    public Date getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public String getPINBlock() {
        return PINBlock;
    }

    public void setPINBlock(String PINBlock) {
        this.PINBlock = PINBlock;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    @Override
    public String getPaymentGatewayCode() {
        return paymentGatewayCode;
    }

    @Override
    public void setPaymentGatewayCode(String paymentGatewayCode) {
        this.paymentGatewayCode = paymentGatewayCode;
    }

    @Override
    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    @Override
    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    @Override
    public String getSystemTraceAuditNumber() {
        return systemTraceAuditNumber;
    }

    @Override
    public String getTransmissionDateAndTime() {
        return null;
    }

    public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
        this.systemTraceAuditNumber = systemTraceAuditNumber;
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

    @Override
    public List<CustomerAccount> getCustomerAccounts() {
        return null;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    @Override
    public String getSecureVerificationData() {
        return secureVerificationData;
    }

    @Override
    public void setSecureVerificationData(String secureVerificationData) {
        this.secureVerificationData = secureVerificationData;
    }

    @Override
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

    @Override
    public String getIvrChannelStatus() {
        return ivrChannelStatus;
    }

    public void setIvrChannelStatus(String ivrChannelStatus) {
        this.ivrChannelStatus = ivrChannelStatus;
    }

    @Override
    public String getMobileChannelStatus() {
        return mobileChannelStatus;
    }

    public void setMobileChannelStatus(String mobileChannelStatus) {
        this.mobileChannelStatus = mobileChannelStatus;
    }

    @Override
    public long getTimeOutInterval() {
        return timeOutInterval;
    }

    @Override
    public void setTimeOutInterval(long timeOutInterval) {
        this.timeOutInterval = timeOutInterval;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
