package com.inov8.microbank.server.service.switchmodule;

import java.io.Serializable;
import java.util.Date;

public class PhoenixTransactionVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -36249679785697697L;

//	private String transactionId;
//	private String messageType;
//	private String transactionCode;
//	private Date creationTime;
//	private Date creationDate;
	private String transactionDate;
	private String transactionTime;
//	private Integer status;
//	private String responseCode;
//	private String authorizationResponseId;
//	private String pduRequstString;
//	private String parentTransactionId;
//	private String microbankTransactionCode;
	
	//Oracle version
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
	
	// search criteria fields
	private Date fromDate;
	private Date toDate;
	private Boolean isOrphan;
	
	//Extra fields
	private String amount;
	private String fromAccount;
	private String toAccount;
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
	public Boolean getIsOrphan() {
		return isOrphan;
	}
	public void setIsOrphan(Boolean isOrphan) {
		this.isOrphan = isOrphan;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public Date getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	
	
}
