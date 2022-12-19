package com.inov8.microbank.common.model.minitransactionmodule;

import java.util.Date;

public class ExtendedTransactionCodeHistoryViewModel extends
		TransactionCodeHistoryViewModel {
	  
	
	private static final long serialVersionUID = -1686210967362788965L;
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
