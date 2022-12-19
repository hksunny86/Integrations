package com.inov8.microbank.common.model.portal.financereportsmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

/**
 * @author: Naseer Ullah
 */
public class ExtendedSettlementClosingBalanceViewModel extends
		SettlementClosingBalanceViewModel implements Serializable {
	/**
	 * 		
	 */
	private static final long serialVersionUID = -7475328163428806996L;

	private Date startDate;
	private Date endDate;

	public ExtendedSettlementClosingBalanceViewModel() {
	}

	@Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}