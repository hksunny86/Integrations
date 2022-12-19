package com.inov8.microbank.common.model.portal.agentcashmodule;

import java.util.Date;

/**
 * @author Omar
 */
public class ExtendedAgentCashViewModel extends AgentCashViewModel {

	private static final long serialVersionUID = -1107430786755902357L;

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

}
