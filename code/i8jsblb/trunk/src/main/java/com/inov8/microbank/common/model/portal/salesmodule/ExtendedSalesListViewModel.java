package com.inov8.microbank.common.model.portal.salesmodule;



import java.util.Date;

public class ExtendedSalesListViewModel extends SalesListViewModel
{

  /**
	 * 
	 */
	private static final long serialVersionUID = -5044214553328660725L;
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
