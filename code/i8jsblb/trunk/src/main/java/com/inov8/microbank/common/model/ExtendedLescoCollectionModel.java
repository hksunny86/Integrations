package com.inov8.microbank.common.model;

import java.util.Date;

public class ExtendedLescoCollectionModel extends LescoCollectionModel
{



	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1001173011061653948L;
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
