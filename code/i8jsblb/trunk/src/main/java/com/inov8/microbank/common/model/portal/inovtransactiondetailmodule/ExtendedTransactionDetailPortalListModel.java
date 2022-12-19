package com.inov8.microbank.common.model.portal.inovtransactiondetailmodule;

import java.util.Date;

public class ExtendedTransactionDetailPortalListModel extends TransactionDetailPortalListModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2436347487879090899L;
	private Date startDate;
	private Date endDate;
	private Long userType;
	private String fileURL;
	
	private Date updatedOnStartDate;
	private Date updatedOnEndDate;

	@javax.persistence.Transient
	public Long getUserType() {
		return userType;
	}

	public void setUserType(Long userType) {
		this.userType = userType;
	}

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

	@javax.persistence.Transient
	public String getFileURL() {
		return fileURL +".";
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
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