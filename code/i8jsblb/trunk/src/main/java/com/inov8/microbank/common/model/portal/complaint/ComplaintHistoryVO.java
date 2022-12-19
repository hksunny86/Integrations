package com.inov8.microbank.common.model.portal.complaint;

import java.util.Date;

public class ComplaintHistoryVO {

	private String title;
	private String status;
	private Date tatEndTime;
	private String assignedName;
	private Date assignedOn;
	private Long assigneeAppUserId;
	private String complaintCode;
	private String complaintCategory;
	private String remarks;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getTatEndTime() {
		return tatEndTime;
	}
	public void setTatEndTime(Date tatEndTime) {
		this.tatEndTime = tatEndTime;
	}
	public String getAssignedName() {
		return assignedName;
	}
	public void setAssignedName(String assignedName) {
		this.assignedName = assignedName;
	}
	public Long getAssigneeAppUserId() {
		return assigneeAppUserId;
	}
	public void setAssigneeAppUserId(Long assigneeAppUserId) {
		this.assigneeAppUserId = assigneeAppUserId;
	}
	public Date getAssignedOn() {
		return assignedOn;
	}
	public void setAssignedOn(Date assignedOn) {
		this.assignedOn = assignedOn;
	}
	public String getComplaintCode() {
		return complaintCode;
	}
	public void setComplaintCode(String complaintCode) {
		this.complaintCode = complaintCode;
	}
	public String getComplaintCategory() {
		return complaintCategory;
	}
	public void setComplaintCategory(String complaintCategory) {
		this.complaintCategory = complaintCategory;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}	
}
