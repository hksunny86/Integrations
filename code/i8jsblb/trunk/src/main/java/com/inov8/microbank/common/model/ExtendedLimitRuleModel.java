package com.inov8.microbank.common.model;

import java.util.Date;

public class ExtendedLimitRuleModel extends LimitRuleModel {
	
	
	private static final long serialVersionUID = 174683701610429164L;
	
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
