package com.inov8.microbank.common.model.portal.bookmemodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BOOKME_TRANSACTION_DTL_I8_VIEW")
public class BookMeTransactionDetaili8ViewModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 7059945306873740626L;

    private Long pk;
    private Long transactionId;
    private Double fed;
    private Double wht;
    private String consumerNo;
    private String failureReason;
    private Long segmentId;
    private String segment;

    private Double transactionAmount;
    private Double inclusiveCharges;
    private Double exclusiveCharges;
    private Double totalAmount;

    private String customerId;
    private String customerMobileNumber;
    private String customerCnic;
    private String bookMeTransactionId;
    private String stan;
    private String bookMeStatus;
    private String transactionStatus;
    private String processingStatusId;
    private Date createdOn;
    private Date updatedOn;
    private Date createdOnToDate;
    private String transactionCode;
    private Long productId;
    private String productName;
    private String bookMeCustomerName;
    private String bookMeCustomerEmail;
    private String bookMeCustomerMobileNo;
    private String bookMeCustomerCnic;

    private String channel;
    private String customerWalletNumber;
    private String senderTaxStatus;
    private String senderTaxRegime;
    private String senderDebitCardNumber;
    private String senderCustomerChannel;
    private String transactionCompletedOn;
    private String transactionType;
    private String receiverCustomerId;
    private String receiverCustomerMobileNo;
    private String receiverCustomerCnic;
    private String receiverCustomerWalletNumber;
    private String receiverCustomerTaxStatus;
    private String receiverCustomerTaxRegmie;
    private String receiverDebitCardNo;
    private String receiverMobileNo;
    private String receiverCnic;
    private String recieverCustomerChannel;
    private String senderParentAgentId;
    private String senderParentAgentMobileNo;
    private String senderParentAgentCnic;
    private String senderParentAgentAccountNumber;
    private String senderParentAgentTaxStatus;
    private String senderParentAgentTaxRegime;
    private String receiverParentAgentId;
    private String receiverParentAgentMobileNumber;
    private String receiverParentAgentCnic;
    private String receiverParentAgentAccount;
    private String receiverParentAgentTaxStatus;
    private String receiverParentAgentTaxRegime;
    private String totalNetFeeAfterFed;
    private String bankCommission;
    private String fedOnBankCommission;
    private String grossSenderAgentCommission;
    private String fedShareSenderAgentCommission;
    private String whtSenderAgentCommission;
    private String netSenderAgentCommission;
    private String grossReceiverAgentCommission;
    private String fedShareReceiverAgentCommission;
    private String whtShareReceiverAgentCommission;
    private String netReceiverAgentCommission;
    private String grossSenderParentAgentCommission;
    private String fedShareSenderParentAgentCommission;
    private String whtShareSenderParentAgentCommission;
    private String netSenderParentAgentCommission;
    private String grossReceiverParentCommission;
    private String fedShareOnReceiverParentCommission;
    private String whtReceiverParentCommission;
    private String netReceiverParentCommission;
    private String billDueDate;
    private String purposeOfPayment;
    private String reversedBy;
    private String reversedOn;
    private String comments;
    private String terminalId;
    private String channelId;
    private String nadraNfiq;
    private String nadraMinutiae;
    private String transactionPurpose;

    private Long senderDeviceTypeId;

    @Column(name = "BOOKME_CUSTOMER_NAME")
    public String getBookMeCustomerName() {
        return bookMeCustomerName;
    }

    public void setBookMeCustomerName(String bookMeCustomerName) {
        this.bookMeCustomerName = bookMeCustomerName;
    }
    @Column(name = "BOOKME_CUSTOMER_EMAIL")
    public String getBookMeCustomerEmail() {
        return bookMeCustomerEmail;
    }

    public void setBookMeCustomerEmail(String bookMeCustomerEmail) {
        this.bookMeCustomerEmail = bookMeCustomerEmail;
    }
    @Column(name = "BOOKME_CUSTOMER_MOBILENO")
    public String getBookMeCustomerMobileNo() {
        return bookMeCustomerMobileNo;
    }

    public void setBookMeCustomerMobileNo(String bookMeCustomerMobileNo) {
        this.bookMeCustomerMobileNo = bookMeCustomerMobileNo;
    }
    @Column(name = "BOOKME_CUSTOMER_CNIC")
    public String getBookMeCustomerCnic() {
        return bookMeCustomerCnic;
    }

    public void setBookMeCustomerCnic(String bookMeCustomerCnic) {
        this.bookMeCustomerCnic = bookMeCustomerCnic;
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "PK"  )
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @javax.persistence.Transient
    public Date getCreatedOnToDate() { return createdOnToDate; }

    public void setCreatedOnToDate(Date createdOnToDate) { this.createdOnToDate = createdOnToDate; }


    @Column(name = "TRANSACTION_ID", nullable = false)
    public Long getTransactionId() { return transactionId; }

    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    @Column(name = "MFS_ID")
    public String getCustomerId() { return customerId; }

    public void setCustomerId(String customerId) { this.customerId = customerId; }

    @Column(name = "WALLET_CUST_MOBILE_NO")
    public String getCustomerMobileNumber() { return customerMobileNumber; }

    public void setCustomerMobileNumber(String customerMobileNumber) { this.customerMobileNumber = customerMobileNumber; }

    @Column(name = "SENDER_CNIC")
    public String getCustomerCnic() { return customerCnic; }

    public void setCustomerCnic(String customerCnic) { this.customerCnic = customerCnic; }

    @Column(name = "BOOK_ME_TRANSACTION_ID")
    public String getBookMeTransactionId() { return bookMeTransactionId; }

    public void setBookMeTransactionId(String bookMeTransactionId) { this.bookMeTransactionId = bookMeTransactionId; }

    @Column(name = "STAN")
    public String getStan() { return stan; }

    public void setStan(String stan) { this.stan = stan; }

    @Column(name = "BOOKME_STATUS")
    public String getBookMeStatus() { return bookMeStatus; }

    public void setBookMeStatus(String bookMeStatus) { this.bookMeStatus = bookMeStatus; }

    @Column(name = "PROCESSING_STATUS_NAME")
    public String getTransactionStatus() { return transactionStatus; }

    public void setTransactionStatus(String transactionStatus) { this.transactionStatus = transactionStatus; }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() { return createdOn; }

    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

//    @Column(name = "UPDATED_ON")
//    public Date getUpdatedOn() { return updatedOn; }
//
//    public void setUpdatedOn(Date updatedOn) { this.updatedOn = updatedOn; }

    @Column(name = "FED")
    public Double getFed() { return fed; }

    public void setFed(Double fed) { this.fed = fed; }

    @Column(name = "WHT")
    public Double getWht() { return wht; }

    public void setWht(Double wht) { this.wht = wht; }

    @Column(name = "CONSUMER_NO")
    public String getConsumerNo() { return consumerNo; }

    public void setConsumerNo(String consumerNo) { this.consumerNo = consumerNo; }

    @Column(name = "FAILURE_REASON")
    public String getFailureReason() { return failureReason; }

    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    @Column(name = "SEGMENT_ID")
    public Long getSegmentId() { return segmentId; }

    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }

    @Column(name = "SEGMENT")
    public String getSegment() { return segment; }

    public void setSegment(String segment) { this.segment = segment; }

    @Column(name = "TRANSACTION_AMOUNT")
    public Double getTransactionAmount() { return transactionAmount; }

    public void setTransactionAmount(Double transactionAmount) { this.transactionAmount = transactionAmount; }

    @Column(name = "INCLUSIVE_CHARGES")
    public Double getInclusiveCharges() { return inclusiveCharges; }

    public void setInclusiveCharges(Double inclusiveCharges) { this.inclusiveCharges = inclusiveCharges; }

    @Column(name = "EXCLUSIVE_CHARGES")
    public Double getExclusiveCharges() { return exclusiveCharges; }

    public void setExclusiveCharges(Double exclusiveCharges) { this.exclusiveCharges = exclusiveCharges; }

    @Column(name = "TOTAL_AMOUNT")
    public Double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    @Column(name = "TRANSACTION_CODE", nullable = false, length = 50)
    public String getTransactionCode() { return transactionCode; }

    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

    @Column(name = "PRODUCT_ID", nullable = false)
    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    @Column(name = "PRODUCT_NAME", nullable = false)
    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    @Column(name = "SUP_PROCESSING_STATUS_ID")
    public String getProcessingStatusId() {
        return processingStatusId;
    }

    public void setProcessingStatusId(String processingStatusId) {
        this.processingStatusId = processingStatusId;
    }


    @Column(name = "SENDER_CHANNEL")
    public String getChannel() { return channel; }

    public void setChannel(String channel) { this.channel = channel; }

//    @Column(name = "WALLET_CUST_MOBILE_NO")
//    public String getCustomerWalletNumber() { return customerWalletNumber; }
//
//    public void setCustomerWalletNumber(String customerWalletNumber) { this.customerWalletNumber = customerWalletNumber; }

    @Column(name = "SENDER_TAX_STATUS")
    public String getSenderTaxStatus() { return senderTaxStatus; }

    public void setSenderTaxStatus(String senderTaxStatus) { this.senderTaxStatus = senderTaxStatus; }

    @Column(name = "TAX_REGIME")
    public String getSenderTaxRegime() { return senderTaxRegime; }

    public void setSenderTaxRegime(String senderTaxRegime) { this.senderTaxRegime = senderTaxRegime; }

    @Column(name = "DEBIT_CARD_NUMBER")
    public String getSenderDebitCardNumber() { return senderDebitCardNumber; }

    public void setSenderDebitCardNumber(String senderDebitCardNumber) { this.senderDebitCardNumber = senderDebitCardNumber; }

//    @Column(name = "SENDER_CHANNEL")
//    public String getSenderCustomerChannel() { return senderCustomerChannel; }
//
//    public void setSenderCustomerChannel(String senderCustomerChannel) { this.senderCustomerChannel = senderCustomerChannel; }

    @Column(name = "TRANSACTION_COMPLETED_ON")
    public String getTransactionCompletedOn() { return transactionCompletedOn; }

    public void setTransactionCompletedOn(String transactionCompletedOn) { this.transactionCompletedOn = transactionCompletedOn; }

    @Column(name = "TRANSACTION_TYPE")
    public String getTransactionType() { return transactionType; }

    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    @Column(name = "RECIPIENT_MFS_ID")
    public String getReceiverCustomerId() { return receiverCustomerId; }

    public void setReceiverCustomerId(String receiverCustomerId) { this.receiverCustomerId = receiverCustomerId; }

//    @Column(name = "RECEIVER_MOBILE_NUMBER")
//    public String getReceiverCustomerMobileNo() { return receiverCustomerMobileNo; }
//
//    public void setReceiverCustomerMobileNo(String receiverCustomerMobileNo) { this.receiverCustomerMobileNo = receiverCustomerMobileNo; }

//    @Column(name = "RECEIVER_CNIC")
//    public String getReceiverCustomerCnic() { return receiverCustomerCnic; }
//
//    public void setReceiverCustomerCnic(String receiverCustomerCnic) { this.receiverCustomerCnic = receiverCustomerCnic; }

//    public String getReceiverCustomerWalletNumber() { return receiverCustomerWalletNumber; }
//
//    public void setReceiverCustomerWalletNumber(String receiverCustomerWalletNumber) { this.receiverCustomerWalletNumber = receiverCustomerWalletNumber; }

    @Column(name = "RECEIVER_TAX_STATUS")
    public String getReceiverCustomerTaxStatus() { return receiverCustomerTaxStatus; }

    public void setReceiverCustomerTaxStatus(String receiverCustomerTaxStatus) { this.receiverCustomerTaxStatus = receiverCustomerTaxStatus; }

    @Column(name = "RECEIVER_TAX_REGEIME")
    public String getReceiverCustomerTaxRegmie() { return receiverCustomerTaxRegmie; }

    public void setReceiverCustomerTaxRegmie(String receiverCustomerTaxRegmie) { this.receiverCustomerTaxRegmie = receiverCustomerTaxRegmie; }

    @Column(name = "RECEIVER_DEBIT_CARD_NO")
    public String getReceiverDebitCardNo() { return receiverDebitCardNo; }

    public void setReceiverDebitCardNo(String receiverDebitCardNo) { this.receiverDebitCardNo = receiverDebitCardNo; }

    @Column(name = "RECEIVER_MOBILE_NO")
    public String getReceiverMobileNo() { return receiverMobileNo; }

    public void setReceiverMobileNo(String receiverMobileNo) { this.receiverMobileNo = receiverMobileNo; }

    @Column(name = "RECEIVER_CNIC")
    public String getReceiverCnic() { return receiverCnic; }

    public void setReceiverCnic(String receiverCnic) { this.receiverCnic = receiverCnic; }

    @Column(name = "RECEIVER_CHANNEL")
    public String getRecieverCustomerChannel() { return recieverCustomerChannel; }

    public void setRecieverCustomerChannel(String recieverCustomerChannel) { this.recieverCustomerChannel = recieverCustomerChannel; }

    @Column(name = "SPA_ID")
    public String getSenderParentAgentId() { return senderParentAgentId; }

    public void setSenderParentAgentId(String senderParentAgentId) { this.senderParentAgentId = senderParentAgentId; }

    @Column(name = "SPA_MOBILE_NO")
    public String getSenderParentAgentMobileNo() { return senderParentAgentMobileNo; }

    public void setSenderParentAgentMobileNo(String senderParentAgentMobileNo) { this.senderParentAgentMobileNo = senderParentAgentMobileNo; }

    @Column(name = "SPA_CNIC")
    public String getSenderParentAgentCnic() { return senderParentAgentCnic; }

    public void setSenderParentAgentCnic(String senderParentAgentCnic) { this.senderParentAgentCnic = senderParentAgentCnic; }

    @Column(name = "SPA_ACCOUNT_NO")
    public String getSenderParentAgentAccountNumber() { return senderParentAgentAccountNumber; }

    public void setSenderParentAgentAccountNumber(String senderParentAgentAccountNumber) { this.senderParentAgentAccountNumber = senderParentAgentAccountNumber; }

    @Column(name = "SPA_TAX_STATUS")
    public String getSenderParentAgentTaxStatus() { return senderParentAgentTaxStatus; }

    public void setSenderParentAgentTaxStatus(String senderParentAgentTaxStatus) { this.senderParentAgentTaxStatus = senderParentAgentTaxStatus; }

    @Column(name = "SPA_TAX_REGEIME")
    public String getSenderParentAgentTaxRegime() { return senderParentAgentTaxRegime; }

    public void setSenderParentAgentTaxRegime(String senderParentAgentTaxRegime) { this.senderParentAgentTaxRegime = senderParentAgentTaxRegime; }

    @Column(name = "RPA_ID")
    public String getReceiverParentAgentId() { return receiverParentAgentId; }

    public void setReceiverParentAgentId(String receiverParentAgentId) { this.receiverParentAgentId = receiverParentAgentId; }

    @Column(name = "RPA_MOBILE_NO")
    public String getReceiverParentAgentMobileNumber() { return receiverParentAgentMobileNumber; }

    public void setReceiverParentAgentMobileNumber(String receiverParentAgentMobileNumber) { this.receiverParentAgentMobileNumber = receiverParentAgentMobileNumber; }

    @Column(name = "RPA_CNIC")
    public String getReceiverParentAgentCnic() { return receiverParentAgentCnic; }

    public void setReceiverParentAgentCnic(String receiverParentAgentCnic) { this.receiverParentAgentCnic = receiverParentAgentCnic; }

    @Column(name = "RPA_ACCOUNT_NO")
    public String getReceiverParentAgentAccount() { return receiverParentAgentAccount; }

    public void setReceiverParentAgentAccount(String receiverParentAgentAccount) { this.receiverParentAgentAccount = receiverParentAgentAccount; }

    @Column(name = "RPA_TAX_STATUS")
    public String getReceiverParentAgentTaxStatus() { return receiverParentAgentTaxStatus; }

    public void setReceiverParentAgentTaxStatus(String receiverParentAgentTaxStatus) { this.receiverParentAgentTaxStatus = receiverParentAgentTaxStatus; }

    @Column(name = "RPA_TAX_REGEIME")
    public String getReceiverParentAgentTaxRegime() { return receiverParentAgentTaxRegime; }

    public void setReceiverParentAgentTaxRegime(String receiverParentAgentTaxRegime) { this.receiverParentAgentTaxRegime = receiverParentAgentTaxRegime; }

    @Column(name = "TOTAL_NET_FEE")
    public String getTotalNetFeeAfterFed() { return totalNetFeeAfterFed; }

    public void setTotalNetFeeAfterFed(String totalNetFeeAfterFed) { this.totalNetFeeAfterFed = totalNetFeeAfterFed; }

    @Column(name = "TO_BANK")
    public String getBankCommission() { return bankCommission; }

    public void setBankCommission(String bankCommission) { this.bankCommission = bankCommission; }

    @Column(name = "FED_BANK_COMMISSION")
    public String getFedOnBankCommission() { return fedOnBankCommission; }

    public void setFedOnBankCommission(String fedOnBankCommission) { this.fedOnBankCommission = fedOnBankCommission; }

    @Column(name = "GSA_COMMISSION")
    public String getGrossSenderAgentCommission() { return grossSenderAgentCommission; }

    public void setGrossSenderAgentCommission(String grossSenderAgentCommission) { this.grossSenderAgentCommission = grossSenderAgentCommission; }

    @Column(name = "FED_SA_COMMISSION")
    public String getFedShareSenderAgentCommission() { return fedShareSenderAgentCommission; }

    public void setFedShareSenderAgentCommission(String fedShareSenderAgentCommission) { this.fedShareSenderAgentCommission = fedShareSenderAgentCommission;}

    @Column(name = "WHT_SA_COMMISSION")
    public String getWhtSenderAgentCommission() { return whtSenderAgentCommission; }

    public void setWhtSenderAgentCommission(String whtSenderAgentCommission) { this.whtSenderAgentCommission = whtSenderAgentCommission; }

    @Column(name = "NET_SA_COMMISSION")
    public String getNetSenderAgentCommission() { return netSenderAgentCommission; }

    public void setNetSenderAgentCommission(String netSenderAgentCommission) { this.netSenderAgentCommission = netSenderAgentCommission; }

    @Column(name = "GRA_COMMISSION")
    public String getGrossReceiverAgentCommission() { return grossReceiverAgentCommission; }

    public void setGrossReceiverAgentCommission(String grossReceiverAgentCommission) { this.grossReceiverAgentCommission = grossReceiverAgentCommission; }

    @Column(name = "FED_RA_COMMISSION")
    public String getFedShareReceiverAgentCommission() { return fedShareReceiverAgentCommission; }

    public void setFedShareReceiverAgentCommission(String fedShareReceiverAgentCommission) { this.fedShareReceiverAgentCommission = fedShareReceiverAgentCommission; }

    @Column(name = "WHT_RA_COMMISSION")
    public String getWhtShareReceiverAgentCommission() { return whtShareReceiverAgentCommission; }

    public void setWhtShareReceiverAgentCommission(String whtShareReceiverAgentCommission) { this.whtShareReceiverAgentCommission = whtShareReceiverAgentCommission; }

    @Column(name = "NET_RA_COMMISSION")
    public String getNetReceiverAgentCommission() { return netReceiverAgentCommission; }

    public void setNetReceiverAgentCommission(String netReceiverAgentCommission) { this.netReceiverAgentCommission = netReceiverAgentCommission; }

    @Column(name = "GSPA_COMMISSION")
    public String getGrossSenderParentAgentCommission() { return grossSenderParentAgentCommission; }

    public void setGrossSenderParentAgentCommission(String grossSenderParentAgentCommission) { this.grossSenderParentAgentCommission = grossSenderParentAgentCommission; }

    @Column(name = "FED_SPA_COMMISSION")
    public String getFedShareSenderParentAgentCommission() { return fedShareSenderParentAgentCommission; }

    public void setFedShareSenderParentAgentCommission(String fedShareSenderParentAgentCommission) { this.fedShareSenderParentAgentCommission = fedShareSenderParentAgentCommission; }

    @Column(name = "WHT_SPA_COMMISSION")
    public String getWhtShareSenderParentAgentCommission() { return whtShareSenderParentAgentCommission; }

    public void setWhtShareSenderParentAgentCommission(String whtShareSenderParentAgentCommission) { this.whtShareSenderParentAgentCommission = whtShareSenderParentAgentCommission; }

    @Column(name = "NET_SPA_COMMISSION")
    public String getNetSenderParentAgentCommission() { return netSenderParentAgentCommission; }

    public void setNetSenderParentAgentCommission(String netSenderParentAgentCommission) { this.netSenderParentAgentCommission = netSenderParentAgentCommission; }

    @Column(name = "GRPA_COMMISSION")
    public String getGrossReceiverParentCommission() { return grossReceiverParentCommission; }

    public void setGrossReceiverParentCommission(String grossReceiverParentCommission) { this.grossReceiverParentCommission = grossReceiverParentCommission; }

    @Column(name = "FED_RPA_COMMISSION")
    public String getFedShareOnReceiverParentCommission() { return fedShareOnReceiverParentCommission; }

    public void setFedShareOnReceiverParentCommission(String fedShareOnReceiverParentCommission) { this.fedShareOnReceiverParentCommission = fedShareOnReceiverParentCommission; }

    @Column(name = "WHT_RPA_COMMISSION")
    public String getWhtReceiverParentCommission() { return whtReceiverParentCommission; }

    public void setWhtReceiverParentCommission(String whtReceiverParentCommission) { this.whtReceiverParentCommission = whtReceiverParentCommission; }

    @Column(name = "NET_RPA_COMMISSION")
    public String getNetReceiverParentCommission() { return netReceiverParentCommission; }

    public void setNetReceiverParentCommission(String netReceiverParentCommission) { this.netReceiverParentCommission = netReceiverParentCommission; }

    @Column(name = "BILL_DUE_DATE")
    public String getBillDueDate() { return billDueDate; }

    public void setBillDueDate(String billDueDate) { this.billDueDate = billDueDate; }

    @Column(name = "TRANS_PURPOSE_ID")
    public String getPurposeOfPayment() { return purposeOfPayment; }

    public void setPurposeOfPayment(String purposeOfPayment) { this.purposeOfPayment = purposeOfPayment; }

    @Column(name = "REVERSED_BY")
    public String getReversedBy() { return reversedBy; }

    public void setReversedBy(String reversedBy) { this.reversedBy = reversedBy; }

    @Column(name = "REVERSED_ON")
    public String getReversedOn() { return reversedOn; }

    public void setReversedOn(String reversedOn) { this.reversedOn = reversedOn; }

    @Column(name = "COMMENTS")
    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    @Column(name = "TERMINAL_ID")
    public String getTerminalId() { return terminalId; }

    public void setTerminalId(String terminalId) { this.terminalId = terminalId; }

    @Column(name = "CHANNEL_ID")
    public String getChannelId() { return channelId; }

    public void setChannelId(String channelId) { this.channelId = channelId; }

    @Column(name = "NADRA_NFIQ")
    public String getNadraNfiq() { return nadraNfiq; }

    public void setNadraNfiq(String nadraNfiq) { this.nadraNfiq = nadraNfiq; }

    @Column(name = "NADRA_MINUTIAE")
    public String getNadraMinutiae() { return nadraMinutiae; }

    public void setNadraMinutiae(String nadraMinutiae) { this.nadraMinutiae = nadraMinutiae; }

    @Column(name = "TRANSACTION_PURPOSE")
    public String getTransactionPurpose() { return transactionPurpose; }

    public void setTransactionPurpose(String transactionPurpose) { this.transactionPurpose = transactionPurpose; }

    @Column(name = "SENDER_DEVICE_TYPE_ID")
    public Long getSenderDeviceTypeId() { return senderDeviceTypeId; }

    public void setSenderDeviceTypeId(Long senderDeviceTypeId) { this.senderDeviceTypeId = senderDeviceTypeId; }
}
