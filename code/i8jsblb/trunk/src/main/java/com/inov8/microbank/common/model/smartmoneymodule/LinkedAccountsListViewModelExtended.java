package com.inov8.microbank.common.model.smartmoneymodule;

import java.util.Date;

public class LinkedAccountsListViewModelExtended extends
		LinkedAccountsListViewModel {
	private Date toDate;

	private Date fromDate;

	@javax.persistence.Transient
	public Date getFromDate() {
		return fromDate;
	}

	@javax.persistence.Transient
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@javax.persistence.Transient
	public Date getToDate() {
		return toDate;
	}

	@javax.persistence.Transient
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
