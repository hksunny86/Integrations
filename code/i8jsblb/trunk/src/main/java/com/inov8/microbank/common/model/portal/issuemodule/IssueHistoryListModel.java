package com.inov8.microbank.common.model.portal.issuemodule;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class IssueHistoryListModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2076381625226501845L;
	private Date issueDate;
	private String issueCode;
	private List<IssueHistoryStatusModel> issueHistoryStatusModellist;
	
	public IssueHistoryListModel()
	{
		
	}
	
	public String getIssueCode()
	{
		return issueCode;
	}
	
	public void setIssueCode(String issueCode)
	{
		this.issueCode = issueCode;
	}
	
	public Date getIssueDate()
	{
		return issueDate;
	}
	
	public void setIssueDate(Date issueDate)
	{
		this.issueDate = issueDate;
	}
	
	public List<IssueHistoryStatusModel> getIssueHistoryStatusModellist()
	{
		return issueHistoryStatusModellist;
	}
	

	public void setIssueHistoryStatusModellist(List<IssueHistoryStatusModel> issueHistoryStatusModellist)
	{
		this.issueHistoryStatusModellist = issueHistoryStatusModellist;
	}
}
