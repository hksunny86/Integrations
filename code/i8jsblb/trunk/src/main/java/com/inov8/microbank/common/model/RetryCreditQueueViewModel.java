package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "RETRY_CREDIT_QUEUE_LIST_VIEW")
public class RetryCreditQueueViewModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long retryCreditQueueViewModelId;
	private String transactionCode;
	private Long transactionTypeId;
	private String responseCode;
	private String status;
	private Date createdOn;
	private String transactionType;
	private String accountNumber;
	private String accountType;
	private String description;
	private Double balance;
	private String accountHolderName;
	
	@Id
	@Column(name = "ACCOUNT_CR_QUEUE_ID")
	public Long getRetryCreditQueueViewModelId() {
		return retryCreditQueueViewModelId;
	}

	public void setRetryCreditQueueViewModelId(Long retryCreditQueueViewModelId) {
		this.retryCreditQueueViewModelId = retryCreditQueueViewModelId;
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getRetryCreditQueueViewModelId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "retryCreditQueueViewModelId";
		return primaryKeyFieldName;
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&retryCreditQueueViewModelId="
				+ retryCreditQueueViewModelId;
		return parameters;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		retryCreditQueueViewModelId = (arg0);
	}

	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Column(name = "TRANSACTION_TYPE")
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	@Column(name = "ACCOUNT_NUMBER")
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "CUSTOMER_ACCOUNT_TYPE")
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "BALANCE")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@Column(name = "TRANSACTION_TYPE_ID")
	public Long getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(Long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}
	@Column(name = "ACCOUNT_HOLDER_NAME")
	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
}