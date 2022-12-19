package com.inov8.integration.boss.controller;

import java.io.Serializable;
import java.util.Date;

import com.inov8.integration.pdu.response.BossResponse;

public class LimitEnhancementVO implements Serializable {
	
	private static final long serialVersionUID = 5240082238441156361L;
	
	private String paymentType;
	private String bankID;
	private String franchiseID;
	private String amount;
	private String transactionID;
	private String depositDate;
	private String payment;
	private String rrn;
	private String microbankTransactionId;
	private String fileContent;
	
	private Exception exception;
	
	private LimitEnhancementResponse response = new LimitEnhancementResponse();
	
	
	public Exception getException() {
		return exception;
	}


	public void setException(Exception exception) {
		this.exception = exception;
	}


	public LimitEnhancementResponse getResponse() {
		return response;
	}


	public void setResponse(LimitEnhancementResponse response) {
		this.response = response;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public String getBankID() {
		return bankID;
	}


	public void setBankID(String bankID) {
		this.bankID = bankID;
	}


	public String getFranchiseID() {
		return franchiseID;
	}


	public void setFranchiseID(String franchiseID) {
		this.franchiseID = franchiseID;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getTransactionID() {
		return transactionID;
	}


	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}


	public String getDepositDate() {
		return depositDate;
	}


	public void setDepositDate(String depositDate) {
		this.depositDate = depositDate;
	}


	public String getPayment() {
		return payment;
	}


	public void setPayment(String payment) {
		this.payment = payment;
	}


	public String getRrn() {
		return rrn;
	}


	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getMicrobankTransactionId() {
		return microbankTransactionId;
	}

	public void setMicrobankTransactionId(String microbankTransactionId) {
		this.microbankTransactionId = microbankTransactionId;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	@Override
	public String toString() {
		return "LimitEnhancementVO [paymentType=" + paymentType + ", bankID=" + bankID + ", franchiseID=" + franchiseID + ", amount=" + amount
				+ ", transactionID=" + transactionID + ", depositDate=" + depositDate + ", payment=" + payment + "]";
	}

}
