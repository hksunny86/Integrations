package com.inov8.microbank.common.model.portal.productcustomerdisputemodule;

import java.util.Date;

@SuppressWarnings("serial")
public class ExtendedCustDispTranVrDcListViewModel extends CustDispTranVrDcListViewModel
{
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
