package com.inov8.microbank.common.model.portal.issuemodule;

import java.io.Serializable;
import java.util.Date;

public class IssueHistoryStatusModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8973257380070115598L;
	private String comments;
	private Date issueHistoryDate;
	private String statusName;
	private Long issueTypeStatusId;

	
	public IssueHistoryStatusModel()
	{
		
	}
	
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	public Date getIssueHistoryDate()
	{
		return issueHistoryDate;
	}
	public void setIssueHistoryDate(Date issueHistoryDate)
	{
		this.issueHistoryDate = issueHistoryDate;
	}
	public String getStatusName()
	{
		return statusName;
	}
	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

	public Long getIssueTypeStatusId()
	{
		return issueTypeStatusId;
	}

	public void setIssueTypeStatusId(Long issueTypeStatusId)
	{
		this.issueTypeStatusId = issueTypeStatusId;
	}

	
}
