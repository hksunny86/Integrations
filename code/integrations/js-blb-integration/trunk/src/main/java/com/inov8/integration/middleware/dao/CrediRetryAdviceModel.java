package com.inov8.integration.middleware.dao;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CrediRetryAdviceModel implements Serializable, RowMapper {

    private static final long serialVersionUID = 6075904576001999598L;

    private Long ibftRetryAdviceId;

    private String mobileNo; //AccountNo2
    private String accountNo; //getAccountNo1
    private Double transactionAmount;
    private Date requestTime;
    private String stan;
    private String retrievalReferenceNumber;
    private String status;

    private String transactionCode;
    private String bankImd;

    private Long createdByAppUserModel;
    private Long updatedByAppUserModel;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;
    //ibft
    private Long productId;

    private String channelName;
    private String creditInquiryRRN;
    private String orignalTransactionRRN;


    public CrediRetryAdviceModel() {
    }

    public Long getIbftRetryAdviceId() {
        return ibftRetryAdviceId;
    }

    public void setIbftRetryAdviceId(Long ibftRetryAdviceId) {
        this.ibftRetryAdviceId = ibftRetryAdviceId;
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

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getBankImd() {
        return bankImd;
    }

    public void setBankImd(String bankImd) {
        this.bankImd = bankImd;
    }

    public Long getCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    public void setCreatedByAppUserModel(Long createdByAppUserModel) {
        this.createdByAppUserModel = createdByAppUserModel;
    }

    public Long getUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    public void setUpdatedByAppUserModel(Long updatedByAppUserModel) {
        this.updatedByAppUserModel = updatedByAppUserModel;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCreditInquiryRRN() {
        return creditInquiryRRN;
    }

    public void setCreditInquiryRRN(String creditInquiryRRN) {
        this.creditInquiryRRN = creditInquiryRRN;
    }

    public String getOrignalTransactionRRN() {
        return orignalTransactionRRN;
    }

    public void setOrignalTransactionRRN(String orignalTransactionRRN) {
        this.orignalTransactionRRN = orignalTransactionRRN;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
