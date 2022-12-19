package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.CoreAdviceInterface;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "TRANSACTION_REVERSAL_SEQ",sequenceName = "TRANSACTION_REVERSAL_SEQ")
@Table(name = "TRANSACTION_REVERSAL_QUEUE")
public class TransactionReversalModel extends BasePersistableModel implements Serializable,CoreAdviceInterface{

    private Long transactionReversalId;
    private Long productId;
    private String cardPan;
    private String originalStan;
    private String reversalStan;
    private String rrn;
    private Double reversalAmount;
    private Date transactionDate;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private String status;
    private Long retryCount;
    private String transactionCode;

    private String reversalRequestTime;

    //IBFT
    private String senderBankImd;
    private String crDr;
    private String senderIBAN;
    private String beneIBAN;
    private String beneBankName;
    private String beneBranchName;
    private String senderName;
    private String cardAcceptorNameAndLocation;
    private String agentId;
    private String purposeOfPayment;
    private String beneAccountTitle;
    private String beneBankImd;

    @Column(name = "TRANSACTION_REVERSAL_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTION_REVERSAL_SEQ")
    public Long getTransactionReversalId() {
        return transactionReversalId;
    }

    public void setTransactionReversalId(Long transactionReversalId) {
        this.transactionReversalId = transactionReversalId;
    }

    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public Long getIntgTransactionTypeId() {
        return null;
    }

    @Override
    public void setIntgTransactionTypeId(Long intgTransactionTypeId) {

    }

    @Override
    public Long getTransactionCodeId() {
        return null;
    }

    @Override
    public void setTransactionCodeId(Long transactionCodeId) {

    }

    @Column(name = "PAN")
    public String getCardPan() {
        return cardPan;
    }

    public void setCardPan(String cardPan) {
        this.cardPan = cardPan;
    }

    @Column(name = "ORG_STAN")
    public String getOriginalStan() {
        return originalStan;
    }

    public void setOriginalStan(String originalStan) {
        this.originalStan = originalStan;
    }

    @Column(name = "REQ_STAN")
    public String getReversalStan() {
        return reversalStan;
    }

    public void setReversalStan(String reversalStan) {
        this.reversalStan = reversalStan;
    }

    @Column(name = "RRN")
    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @Column(name = "AMOUNT")
    public Double getReversalAmount() {
        return reversalAmount;
    }

    public void setReversalAmount(Double reversalAmount) {
        this.reversalAmount = reversalAmount;
    }

    @Column(name = "TRANSACTION_DATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public Date getRequestTime() {
        return null;
    }

    @Override
    public void setRequestTime(Date requestTime) {

    }

    @Override
    public String getAdviceType() {
        return null;
    }

    @Override
    public void setAdviceType(String adviceType) {

    }

    @Override
    public String getBillAggregator() {
        return null;
    }

    @Override
    public void setBillAggregator(String billAggregator) {

    }

    @Override
    public String getCnicNo() {
        return null;
    }

    @Override
    public void setCnicNo(String cnicNo) {

    }

    @Override
    public String getConsumerNo() {
        return null;
    }

    @Override
    public void setConsumerNo(String consumerNo) {

    }

    @Override
    public String getBillCategoryCode() {
        return null;
    }

    @Override
    public void setBillCategoryCode(String billCategoryCode) {

    }

    @Override
    public String getCompnayCode() {
        return null;
    }

    @Override
    public void setCompnayCode(String compnayCode) {

    }

    @Override
    public Long getTransactionId() {
        return null;
    }

    @Override
    public void setTransactionId(Long transactionId) {

    }

    @Override
    public Long getActionLogId() {
        return null;
    }

    @Override
    public void setActionLogId(Long actionLogId) {

    }

    @Override
    public String getRetrievalReferenceNumber() {
        return null;
    }

    @Override
    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {

    }

    @Override
    public String getUbpBBStan() {
        return null;
    }

    @Override
    public void setUbpBBStan(String ubpBBStan) {

    }

    @Column(name = "UPDATE_ON")
    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getResponseCode() {
        return null;
    }

    @Override
    public void setResponseCode(String responseCode) {

    }

    @Override
    public Date getTransmissionTime() {
        return null;
    }

    @Override
    public void setTransmissionTime(Date transmissionTime) {

    }

    @Override
    public String getReversalRequestTime() {
        return reversalRequestTime;
    }

    @Override
    public void setReversalRequestTime(String reversalRequestTime) {
        this.reversalRequestTime = reversalRequestTime;
    }

    @Column(name = "RETRY_COUNT")
    public Long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Long retryCount) {
        this.retryCount = retryCount;
    }

    @Column(name = "MICROBANK_TRANSACTION_CODE")
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Override
    public String getFromAccount() {
        return null;
    }

    @Override
    public void setFromAccount(String fromAccount) {

    }

    @Override
    public String getToAccount() {
        return null;
    }

    @Override
    public void setToAccount(String toAccount) {

    }

    @Override
    public Double getTransactionAmount() {
        return null;
    }

    @Override
    public void setTransactionAmount(Double transactionAmount) {

    }

    @Override
    public String getStan() {
        return null;
    }

    @Override
    public void setStan(String stan) {

    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getTransactionReversalId();
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setTransactionReversalId(primaryKey);
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&transactionReversalId=" + transactionReversalId;
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "transactionReversalId";
        return primaryKeyFieldName;
    }

    @Column(name = "CREATED_ON")
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "SENDER_BANK_IMD")
    public String getSenderBankImd() {
        return senderBankImd;
    }

    public void setSenderBankImd(String senderBankImd) {
        this.senderBankImd = senderBankImd;
    }

    @Column(name = "CR_DR")
    public String getCrDr() {
        return crDr;
    }

    public void setCrDr(String crDr) {
        this.crDr = crDr;
    }

    @Column(name = "BENE_IBAN")
    public String getBeneIBAN() {
        return beneIBAN;
    }

    public void setBeneIBAN(String beneIBAN) {
        this.beneIBAN = beneIBAN;
    }

    @Column(name = "BENE_BANK_NAME")
    public String getBeneBankName() {
        return beneBankName;
    }

    public void setBeneBankName(String beneBankName) {
        this.beneBankName = beneBankName;
    }

    @Column(name = "BENE_BRANCH_NAME")
    public String getBeneBranchName() {
        return beneBranchName;
    }

    public void setBeneBranchName(String beneBranchName) {
        this.beneBranchName = beneBranchName;
    }

    @Column(name = "SENDER_NAME")
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Column(name = "ACCEPTOR_DETAILS")
    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    @Column(name = "AGENT_ID")
    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Column(name = "PAYMENT_PURPOSE")
    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    @Column(name = "SENDER_IBAN")
    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    @Column(name = "BENE_NAME")
    public String getBeneAccountTitle() {
        return beneAccountTitle;
    }

    public void setBeneAccountTitle(String beneAccountTitle) {
        this.beneAccountTitle = beneAccountTitle;
    }

    @Column(name = "BENE_BANK_IMD")
    public String getBeneBankImd() {
        return beneBankImd;
    }

    public void setBeneBankImd(String beneBankImd) {
        this.beneBankImd = beneBankImd;
    }
}
