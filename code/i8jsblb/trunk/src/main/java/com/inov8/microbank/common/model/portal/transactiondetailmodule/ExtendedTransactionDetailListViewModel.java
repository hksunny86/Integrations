package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import java.util.Date;

public class ExtendedTransactionDetailListViewModel extends TransactionDetailListViewModel
{

  /**
	 * 
	 */
	private static final long serialVersionUID = -8032010912872142583L;
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
