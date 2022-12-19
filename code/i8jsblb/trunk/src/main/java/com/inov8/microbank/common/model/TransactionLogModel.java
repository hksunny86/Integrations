package com.inov8.microbank.common.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
//@org.hibernate.annotations.Entity( dynamicInsert = true)
@Table(name = "PHX_TRANSACTION_LOG")
public class TransactionLogModel extends BasePersistableModel {

	private Long transactionLogId;
	private String rrn;
	private String messageCode;
	private String transactionType;
	private Date transactionDate;
	private Long statusId;
	private String responseCode;
	private String requestPDU;
	private String responsePDU;
	private Long parentTransactionId;
	private String i8TransactionCode;
	private Long processedTime;
	private Date responseTime;
	private Date createdOn;
	private Date updatedOn;
	private Integer retryCount;
	private String channelId;
  
	private String displayTransactionType;
	private String displayRRN;
	private String fromAccount;
	private String toAccount;
	private String amount;
	private String displayStatus;
	private List<TransactionLogModel> reversalTransactionList;
   
   public TransactionLogModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionLogId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setTransactionLogId(primaryKey);
    }

   	@Id
  	@Column(name = "TRANSACTION_LOG_ID")
	public Long getTransactionLogId() {
		return transactionLogId;
	}

	public void setTransactionLogId(Long transactionLogId) {
		this.transactionLogId = transactionLogId;
	}

  	@Column(name = "RRN")
	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

  	@Column(name = "MESSAGE_CODE")
	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

  	@Column(name = "TRANSACTION_TYPE")
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

  	@Column(name = "TRANSACTION_DATE")
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

  	@Column(name = "STATUS_ID")
	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

  	@Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

  	@Column(name = "PDU_REQUEST_STRING")
	public String getRequestPDU() {
		return requestPDU;
	}

	public void setRequestPDU(String requestPDU) {
		this.requestPDU = requestPDU;
	}

  	@Column(name = "PDU_RESPONSE_STRING")
	public String getResponsePDU() {
		return responsePDU;
	}

	public void setResponsePDU(String responsePDU) {
		this.responsePDU = responsePDU;
	}

  	@Column(name = "PARENT_TRANSACTION_ID")
	public Long getParentTransactionId() {
		return parentTransactionId;
	}

	public void setParentTransactionId(Long parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}

  	@Column(name = "I8_TRANSACTION_CODE")
	public String getI8TransactionCode() {
		return i8TransactionCode;
	}

	public void setI8TransactionCode(String i8TransactionCode) {
		this.i8TransactionCode = i8TransactionCode;
	}

  	@Column(name = "PROCESSED_TIME")
	public Long getProcessedTime() {
		return processedTime;
	}

	public void setProcessedTime(Long processedTime) {
		this.processedTime = processedTime;
	}

  	@Column(name = "RESPONSE_TIME")
	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

  	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

  	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

  	@Column(name = "RETRY_COUNT")
	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

  	@Column(name = "CHANNEL_ID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		return "transactionLogId";
	}
	
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		return String.valueOf(transactionLogId);
	}

	@Transient
	public String getDisplayTransactionType() {
		return displayTransactionType;
	}

	public void setDisplayTransactionType(String displayTransactionType) {
		this.displayTransactionType = displayTransactionType;
	}

	@Transient
	public String getDisplayRRN() {
		return displayRRN;
	}

	public void setDisplayRRN(String displayRRN) {
		this.displayRRN = displayRRN;
	}

	@Transient
	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	@Transient
	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	
	@Transient
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Transient
	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	@Transient
	public List<TransactionLogModel> getReversalTransactionList() {
		return reversalTransactionList;
	}

	public void setReversalTransactionList(
			List<TransactionLogModel> reversalTransactionList) {
		this.reversalTransactionList = reversalTransactionList;
	}

}
