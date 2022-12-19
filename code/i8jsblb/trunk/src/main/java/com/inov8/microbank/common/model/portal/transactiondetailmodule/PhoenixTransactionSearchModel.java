package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import java.io.Serializable;
import java.util.Date;

public class PhoenixTransactionSearchModel implements Serializable
{
	private Date fromDate;
	private Date toDate;
	private String transactionCode;
	private Boolean isOrphan;

	public PhoenixTransactionSearchModel() {
	}

	public Date getFromDate()
	{
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public Boolean getIsOrphan() {
		return isOrphan;
	}

	public void setIsOrphan(Boolean isOrphan) {
		this.isOrphan = isOrphan;
	}

}
