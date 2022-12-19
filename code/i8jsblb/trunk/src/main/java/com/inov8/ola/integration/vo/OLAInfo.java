package com.inov8.ola.integration.vo;

import java.io.Serializable;
import java.util.Date;

public class OLAInfo implements Serializable{
	
	private String microbankTransactionCode;
 	private String transactionTypeId;
 	private Long customerAccountTypeId;
	private Long reasonId;
	private Double balance;
	private String payingAccNo;
	private String receivingAccNo;
	private Long coreStakeholderBankInfoId;

	private Date transactionDateTime;
	private String responseCode;
	private Double toBalanceAfterTransaction;
	private Double fromBalanceAfterTransaction;
	private Boolean balanceAfterTrxRequired;
	// for agent assisted transactions
	private Boolean agentBalanceAfterTrxRequired;
	private Double agentBalanceAfterTransaction;
	private Boolean isAgent;
	//Handler related Changes
	private Long handlerId;
	private Long handlerAccountTypeId;
	//For Agent BB Statement
	private Long commissionType;
	private Long category;
	//For Challan Collection
	private Boolean processThroughQueue;
	
	
	/**
	 * converts current object to OLAVO object
	 */
	public OLAVO getOlaVoObject(){
		OLAVO olaVO = new OLAVO();
		olaVO.setMicrobankTransactionCode(this.microbankTransactionCode);
		olaVO.setTransactionTypeId(this.transactionTypeId);
		olaVO.setCustomerAccountTypeId(this.customerAccountTypeId);
		olaVO.setReasonId(this.reasonId);
		olaVO.setBalance(this.balance);
		olaVO.setPayingAccNo(this.payingAccNo);
		olaVO.setReceivingAccNo(this.receivingAccNo);
		
		olaVO.setTransactionDateTime(this.transactionDateTime);
		olaVO.setResponseCode(this.responseCode);
		olaVO.setToBalanceAfterTransaction(this.toBalanceAfterTransaction);
		olaVO.setFromBalanceAfterTransaction(this.fromBalanceAfterTransaction);
		olaVO.setAgentBalanceAfterTransaction(this.agentBalanceAfterTransaction);
		olaVO.setHandlerId(this.handlerId);
		olaVO.setHandlerAccountTypeId(this.handlerAccountTypeId);
		olaVO.setCommissionType(this.commissionType);
		olaVO.setCategory(this.category);
		olaVO.setIsViaQueue(this.processThroughQueue);
		
		
		return olaVO;
	}
	
	public String getMicrobankTransactionCode() {
		return microbankTransactionCode;
	}
	public void setMicrobankTransactionCode(String microbankTransactionCode) {
		this.microbankTransactionCode = microbankTransactionCode;
	}
	public String getTransactionTypeId() {
		return transactionTypeId;
	}
	public void setTransactionTypeId(String transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}
	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}
	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}
	public Long getReasonId() {
		return reasonId;
	}
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getPayingAccNo() {
		return payingAccNo;
	}
	public void setPayingAccNo(String payingAccNo) {
		this.payingAccNo = payingAccNo;
	}
	public String getReceivingAccNo() {
		return receivingAccNo;
	}
	public void setReceivingAccNo(String receivingAccNo) {
		this.receivingAccNo = receivingAccNo;
	}
	public Date getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(Date transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public Double getToBalanceAfterTransaction() {
		return toBalanceAfterTransaction;
	}
	public void setToBalanceAfterTransaction(Double toBalanceAfterTransaction) {
		this.toBalanceAfterTransaction = toBalanceAfterTransaction;
	}
	public Double getFromBalanceAfterTransaction() {
		return fromBalanceAfterTransaction;
	}
	public void setFromBalanceAfterTransaction(Double fromBalanceAfterTransaction) {
		this.fromBalanceAfterTransaction = fromBalanceAfterTransaction;
	}

	public Boolean getBalanceAfterTrxRequired() {
		return balanceAfterTrxRequired;
	}

	public void setBalanceAfterTrxRequired(Boolean balanceAfterTrxRequired) {
		this.balanceAfterTrxRequired = balanceAfterTrxRequired;
	}
	
	public Boolean getAgentBalanceAfterTrxRequired() {
		return agentBalanceAfterTrxRequired;
	}

	public void setAgentBalanceAfterTrxRequired(Boolean agentBalanceAfterTrxRequired) {
		this.agentBalanceAfterTrxRequired = agentBalanceAfterTrxRequired;
	}

	public Double getAgentBalanceAfterTransaction() {
		return agentBalanceAfterTransaction;
	}

	public void setAgentBalanceAfterTransaction(Double agentBalanceAfterTransaction) {
		this.agentBalanceAfterTransaction = agentBalanceAfterTransaction;
	}
	
	public Long getCoreStakeholderBankInfoId() {
		return coreStakeholderBankInfoId;
	}

	public void setCoreStakeholderBankInfoId(Long coreStakeholderBankInfoId) {
		this.coreStakeholderBankInfoId = coreStakeholderBankInfoId;
	}
	
	public Boolean getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(Boolean isAgent) {
		this.isAgent = isAgent;
	}

	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}
	
	public Long getHandlerAccountTypeId() {
		return handlerAccountTypeId;
	}

	public void setHandlerAccountTypeId(Long handlerAccountTypeId) {
		this.handlerAccountTypeId = handlerAccountTypeId;
	}

	public Long getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(Long commissionType) {
		this.commissionType = commissionType;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public Boolean getProcessThroughQueue() {
		return processThroughQueue;
	}

	public void setProcessThroughQueue(Boolean processThroughQueue) {
		this.processThroughQueue = processThroughQueue;
	}
}
