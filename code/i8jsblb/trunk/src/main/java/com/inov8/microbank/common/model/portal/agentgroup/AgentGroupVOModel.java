package com.inov8.microbank.common.model.portal.agentgroup;

import java.util.ArrayList;
import java.util.List;

import com.inov8.microbank.common.model.AppUserModel;

public class AgentGroupVOModel {

	private Long groupId;
	private Long groupIdentity;
	private String parrentId;
	private Long appUserId;
	private String agentName;
	private String businessName;
	private String mobileNumber;
	private String cnic;
	private String groupTitle;
	private Boolean status;
	private String childString;
	private Long usecaseId;
	private List<AgentGroupVOModel> children = new ArrayList<AgentGroupVOModel>(0);	

	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getParrentId() {
		return parrentId;
	}
	public void setParrentId(String parrentId) {
		this.parrentId = parrentId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCnic() {
		return cnic;
	}
	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getStatusName() {
		String statusName = "Inactive";
		if(status != null && status){
			statusName = "Active";
		}
		return statusName;
	}
	public List<AgentGroupVOModel> getChildren() {
		return children;
	}
	public void setChildren(List<AgentGroupVOModel> children) {
		this.children = children;
	}
	
	public void add(AgentGroupVOModel child) {
		children.add(child);
	}
	
	public void remove(AgentGroupVOModel child) {
		children.remove(child);
	}
	public Long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
	
	public String getChildString() {
		return childString;
	}
	public void setChildString(String childString) {
		this.childString = childString;
	}
	public Long getGroupIdentity() {
		return groupIdentity;
	}
	public void setGroupIdentity(Long groupIdentity) {
		this.groupIdentity = groupIdentity;
	}
	public Long getUsecaseId() {
		return usecaseId;
	}
	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}
}
