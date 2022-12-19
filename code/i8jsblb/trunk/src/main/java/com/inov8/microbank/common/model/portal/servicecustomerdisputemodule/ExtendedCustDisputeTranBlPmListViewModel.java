package com.inov8.microbank.common.model.portal.servicecustomerdisputemodule;

import java.util.Date;

public class ExtendedCustDisputeTranBlPmListViewModel extends CustDispTranBlPmListViewModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5392655654332825204L;
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
