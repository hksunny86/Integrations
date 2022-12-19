package com.inov8.microbank.common.model.portal.paymentsmodule;

import java.util.Date;

public class ExtendedPaymentsListViewModel extends PaymentsListViewModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -22357571881471297L;
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
