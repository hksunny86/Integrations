package com.inov8.integration.boss.controller;

import java.io.Serializable;
import java.util.Date;

import com.inov8.integration.pdu.response.BossResponse;

public class BOSSVO implements Serializable {
	private static final long serialVersionUID = 5240082238441156361L;

	private String msidn;
	private String amount;
	private String subNumber;
	private String microbankTransactionId;
	private String subType;
	private Date transmissionDateTime;

	private Long businessSerial;
	private String transactionRefNumber;

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	private Exception exception;
	
	private BossResponse response = new BossResponse();

	public String getMsidn() {
		return msidn;
	}

	public void setMsidn(String msidn) {
		this.msidn = msidn;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMicrobankTransactionId() {
		return microbankTransactionId;
	}

	public void setMicrobankTransactionId(String microbankTransactionId) {
		this.microbankTransactionId = microbankTransactionId;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Date getTransmissionDateTime() {
		return transmissionDateTime;
	}

	public void setTransmissionDateTime(Date transmissionDateTime) {
		this.transmissionDateTime = transmissionDateTime;
	}

	public String getSubNumber() {
		return subNumber;
	}

	public void setSubNumber(String subNumber) {
		this.subNumber = subNumber;
	}

	public BossResponse getResponse() {
		return response;
	}

	public void setResponse(BossResponse response) {
		this.response = response;
	}

	public Long getBusinessSerial() {
		return businessSerial;
	}

	public void setBusinessSerial(Long businessSerial) {
		this.businessSerial = businessSerial;
	}

	public String getTransactionRefNumber() {
		return transactionRefNumber;
	}

	public void setTransactionRefNumber(String transactionRefNumber) {
		this.transactionRefNumber = transactionRefNumber;
	}

	@Override
	public String toString() {
		return "BOSSVO [msidn=" + msidn + ", amount=" + amount + ", subNumber=" + subNumber + ", microbankTransactionId=" + microbankTransactionId
				+ ", transmissionDateTime=" + transmissionDateTime + ", businessSerial=" + businessSerial + ", transactionRefNumber=" + transactionRefNumber
				+ ", exception=" + exception + ", response=" + response + "]";
	}

}
