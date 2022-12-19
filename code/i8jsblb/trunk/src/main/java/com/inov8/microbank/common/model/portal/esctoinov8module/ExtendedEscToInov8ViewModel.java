package com.inov8.microbank.common.model.portal.esctoinov8module;

import java.util.Date;

public class ExtendedEscToInov8ViewModel extends EscToInov8ViewModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5079894970159673982L;
	private Date startDate;
	private Date endDate;
	private String veriflyStatus;
	
	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@javax.persistence.Transient
	public String getVeriflyStatus() {
		return veriflyStatus;
	}
	
	public void setVeriflyStatus(String veriflyStatus) {
		this.veriflyStatus = veriflyStatus;
	}
	
		
}
