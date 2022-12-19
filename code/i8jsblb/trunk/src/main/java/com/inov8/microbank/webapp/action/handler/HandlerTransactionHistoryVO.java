package com.inov8.microbank.webapp.action.handler;

import java.util.Date;

import javax.persistence.Transient;

import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;

/**
 * @author Kashif Bashir
 * 
 */

public class HandlerTransactionHistoryVO extends AllpayTransInfoListViewModel {

	private String handlerPk;
	private Date startDate;
	private Date endDate;

	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public String getHandlerPk() {
		return handlerPk;
	}

	public void setHandlerPk(String handlerPk) {
		this.handlerPk = handlerPk;
	}
}