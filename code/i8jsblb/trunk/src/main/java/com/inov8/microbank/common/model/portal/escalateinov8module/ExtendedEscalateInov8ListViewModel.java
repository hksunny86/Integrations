package com.inov8.microbank.common.model.portal.escalateinov8module;

import java.util.Date;

public class ExtendedEscalateInov8ListViewModel extends EscalateInov8ListViewModel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -5276970423552063896L;
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
