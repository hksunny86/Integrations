package com.inov8.microbank.fonepay.model;

import java.util.Date;


public class ExtendedFonePayTransactionDetailModel extends FonePayTransactionDetailViewModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2367128747401987865L;
	private Date startDate;
	private Date endDate;
	
	private Date updatedOnStartDate;
	private Date updatedOnEndDate;
	
	
	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@javax.persistence.Transient
	public Date getUpdatedOnStartDate() {
		return updatedOnStartDate;
	}
	public void setUpdatedOnStartDate(Date updatedOnStartDate) {
		this.updatedOnStartDate = updatedOnStartDate;
	}
	
	@javax.persistence.Transient
	public Date getUpdatedOnEndDate() {
		return updatedOnEndDate;
	}
	public void setUpdatedOnEndDate(Date updatedOnEndDate) {
		this.updatedOnEndDate = updatedOnEndDate;
	}


}
