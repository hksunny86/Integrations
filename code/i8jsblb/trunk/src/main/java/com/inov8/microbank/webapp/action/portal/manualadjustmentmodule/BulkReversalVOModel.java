package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import java.io.Serializable;
import java.util.Date;

public class BulkReversalVOModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long batchId;
	private Date createdOn;
	private Date updatedOn;
	
	
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	

}
