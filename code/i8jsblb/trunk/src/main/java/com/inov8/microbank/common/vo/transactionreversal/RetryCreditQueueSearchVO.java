package com.inov8.microbank.common.vo.transactionreversal;

import java.io.Serializable;
import java.util.Date;

public class RetryCreditQueueSearchVO implements Serializable {

	private String transactionCode;
	private String status;
	private Long transactionTypeId;
	private Date requestTimeStart;
	private Date requestTimeEnd;

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public Long getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(Long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public Date getRequestTimeStart() {
		return requestTimeStart;
	}

	public void setRequestTimeStart(Date requestTimeStart) {
		this.requestTimeStart = requestTimeStart;
	}

	public Date getRequestTimeEnd() {
		return requestTimeEnd;
	}

	public void setRequestTimeEnd(Date requestTimeEnd) {
		this.requestTimeEnd = requestTimeEnd;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}