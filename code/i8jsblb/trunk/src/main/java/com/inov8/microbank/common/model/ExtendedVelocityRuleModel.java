package com.inov8.microbank.common.model;

import java.util.Date;

public class ExtendedVelocityRuleModel extends VelocityRuleModel {

	private static final long serialVersionUID = -4425763947840173765L;
	
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
