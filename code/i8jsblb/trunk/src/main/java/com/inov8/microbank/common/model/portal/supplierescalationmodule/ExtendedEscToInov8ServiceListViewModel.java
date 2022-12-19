package com.inov8.microbank.common.model.portal.supplierescalationmodule;

import java.util.Date;

public class ExtendedEscToInov8ServiceListViewModel extends EscToInov8ServiceListViewModel{

	  /**
	 * 
	 */
	private static final long serialVersionUID = 8474766495865233361L;
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
