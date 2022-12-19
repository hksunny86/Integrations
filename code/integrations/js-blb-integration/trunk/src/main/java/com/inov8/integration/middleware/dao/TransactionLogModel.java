package com.inov8.integration.middleware.dao;

import java.util.Date;

public class TransactionLogModel {

	private Long transactionLogId;
	private String retrievalRefNo;
	private String messageType;
	private String transactionCode;
	private Date transactionDateTime;
	private Long status;
	private String responseCode;
	private String pduRequestString;
	private String pduResponseString;
	private Long parentTransactionLogId;
	private String i8TransactionCode;
	private Long processedTime;
	private Integer retryCount;
	private String channelId;
    private String pduRequestHEX;
    private String pduResponseHEX;

	public Long getTransactionLogId() {
		return transactionLogId;
	}

	public void setTransactionLogId(Long transactionLogId) {
		this.transactionLogId = transactionLogId;
	}

	public String getRetrievalRefNo() {
		return retrievalRefNo;
	}

	public void setRetrievalRefNo(String retrievalRefNo) {
		this.retrievalRefNo = retrievalRefNo;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getPduRequestString() {
		return pduRequestString;
	}

	public void setPduRequestString(String pduRequestString) {
		this.pduRequestString = pduRequestString;
	}

	public String getPduResponseString() {
		return pduResponseString;
	}

	public void setPduResponseString(String pduResponseString) {
		this.pduResponseString = pduResponseString;
	}

	public Long getParentTransactionLogId() {
		return parentTransactionLogId;
	}

	public void setParentTransactionLogId(Long parentTransactionLogId) {
		this.parentTransactionLogId = parentTransactionLogId;
	}

	public String getI8TransactionCode() {
		return i8TransactionCode;
	}

	public void setI8TransactionCode(String i8TransactionCode) {
		this.i8TransactionCode = i8TransactionCode;
	}

	public Long getProcessedTime() {
		return processedTime;
	}

	public void setProcessedTime(Long processedTime) {
		this.processedTime = processedTime;
	}

	public Date getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

    public String getPduRequestHEX() {
        return pduRequestHEX;
    }

    public void setPduRequestHEX(String pduRequestHEX) {
        this.pduRequestHEX = pduRequestHEX;
    }

    public String getPduResponseHEX() {
        return pduResponseHEX;
    }

    public void setPduResponseHEX(String pduResponseHEX) {
        this.pduResponseHEX = pduResponseHEX;
    }
}
