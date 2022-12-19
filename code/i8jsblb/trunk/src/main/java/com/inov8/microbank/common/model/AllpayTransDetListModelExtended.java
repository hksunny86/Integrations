package com.inov8.microbank.common.model;

import java.util.Date;

public class AllpayTransDetListModelExtended extends AllpayTransDetListModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2436347487879090899L;
	private Date startDate;
	private Date endDate;

	@javax.persistence.Transient
	public Date getEndDate()
	{
		return endDate;
	}
	
	@javax.persistence.Transient
	public Date getStartDate()
	{
		return startDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}


}
