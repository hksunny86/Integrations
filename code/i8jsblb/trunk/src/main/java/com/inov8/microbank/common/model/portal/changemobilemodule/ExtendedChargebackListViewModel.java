package com.inov8.microbank.common.model.portal.changemobilemodule;

import java.util.Date;

import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;

public class ExtendedChargebackListViewModel extends ChargebackListViewModel {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
