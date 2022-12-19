package com.inov8.microbank.debitcard.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class DebitCardReversalVO implements Serializable {
    private static final long serialVersionUID = -1121652379255326975L;

    private Long productId;
    private String cardPan;
    private String originalStan;
    private String reversalStan;
    private String rrn;
    private Double reversalAmount;
    private Date transactionDate;
    private String reversalRequestTime;
    private Timestamp updatedOn;
    private String status;
    private Long retryCount;
    private String transactionCode;
    private String adviceType;
    private Long transactionCodeId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCardPan() {
        return cardPan;
    }

    public void setCardPan(String cardPan) {
        this.cardPan = cardPan;
    }

    public String getOriginalStan() {
        return originalStan;
    }

    public void setOriginalStan(String originalStan) {
        this.originalStan = originalStan;
    }

    public String getReversalStan() {
        return reversalStan;
    }

    public void setReversalStan(String reversalStan) {
        this.reversalStan = reversalStan;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public Double getReversalAmount() {
        return reversalAmount;
    }

    public void setReversalAmount(Double reversalAmount) {
        this.reversalAmount = reversalAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReversalRequestTime() {
        return reversalRequestTime;
    }

    public void setReversalRequestTime(String reversalRequestTime) {
        this.reversalRequestTime = reversalRequestTime;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Long retryCount) {
        this.retryCount = retryCount;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getAdviceType() {
        return adviceType;
    }

    public void setAdviceType(String adviceType) {
        this.adviceType = adviceType;
    }

    public Long getTransactionCodeId() {
        return transactionCodeId;
    }

    public void setTransactionCodeId(Long transactionCodeId) {
        this.transactionCodeId = transactionCodeId;
    }
}
