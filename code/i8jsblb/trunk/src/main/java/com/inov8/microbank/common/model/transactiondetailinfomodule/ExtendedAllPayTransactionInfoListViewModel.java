package com.inov8.microbank.common.model.transactiondetailinfomodule;

import java.util.Date;

public class ExtendedAllPayTransactionInfoListViewModel  extends AllpayTransInfoListViewModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1006664900472629030L;

	
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
