package com.inov8.microbank.common.model.portal.complaint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.common.model.ComplaintModel;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.util.LabelValueBean;

public class ComplaintDetailVO implements Serializable {

	public ComplaintDetailVO() {}
	
	private static final long serialVersionUID = 3610096515008770731L;
	
	private AppUserModel appUserModel;
	private ComplaintModel complaintModel = new ComplaintModel();
	private ComplaintSubcategoryModel complaintSubcategoryModel;
	private ComplaintCategoryModel complaintCategoryModel;
	private List<ComplaintHistoryVO> complaintHistoryList = new ArrayList<ComplaintHistoryVO>(0);
	private Map complaintParamValueMap;
	private Boolean isSameAssigneeUser = Boolean.FALSE;
	private Boolean isCustomer = Boolean.FALSE;
	private Boolean isAgent = Boolean.FALSE;
	private Boolean isWalkin = Boolean.FALSE;
	private List<LabelValueBean> escalationList = new ArrayList<LabelValueBean>();
	private Long escalateTo;
	private String loggedByName;

	public Boolean getIsSameAssigneeUser() {
		return isSameAssigneeUser;
	}
	public void setIsSameAssigneeUser(Boolean isSameAssigneeUser) {
		this.isSameAssigneeUser = isSameAssigneeUser;
	}
	public Map getComplaintParamValueMap() {
		return complaintParamValueMap;
	}
	public void setComplaintParamValueMap(Map complaintParamValueMap) {
		this.complaintParamValueMap = complaintParamValueMap;
	}
	public AppUserModel getAppUserModel() {
		return appUserModel;
	}
	public void setAppUserModel(AppUserModel appUserModel) {
		this.appUserModel = appUserModel;
	}
	public ComplaintModel getComplaintModel() {
		return complaintModel;
	}
	public void setComplaintModel(ComplaintModel complaintModel) {
		this.complaintModel = complaintModel;
	}
	public ComplaintSubcategoryModel getComplaintSubcategoryModel() {
		return complaintSubcategoryModel;
	}
	public void setComplaintSubcategoryModel(ComplaintSubcategoryModel complaintSubcategoryModel) {
		this.complaintSubcategoryModel = complaintSubcategoryModel;
	}
	public ComplaintCategoryModel getComplaintCategoryModel() {
		return complaintCategoryModel;
	}
	public void setComplaintCategoryModel(ComplaintCategoryModel complaintCategoryModel) {
		this.complaintCategoryModel = complaintCategoryModel;
	}
	public List<ComplaintHistoryVO> getComplaintHistoryList() {
		return complaintHistoryList;
	}
	public void setComplaintHistoryList(List<ComplaintHistoryVO> complaintHistoryList) {
		this.complaintHistoryList = complaintHistoryList;
	}
	public Boolean getIsCustomer() {
		return isCustomer;
	}
	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}
	public Boolean getIsAgent() {
		return isAgent;
	}
	public void setIsAgent(Boolean isAgent) {
		this.isAgent = isAgent;
	}
	public List<LabelValueBean> getEscalationList() {
		return escalationList;
	}
	public void setEscalationList(List<LabelValueBean> escalationList) {
		this.escalationList = escalationList;
	}
	public Long getEscalateTo() {
		return escalateTo;
	}
	public void setEscalateTo(Long escalateTo) {
		this.escalateTo = escalateTo;
	}
	public String getLoggedByName() {
		return loggedByName;
	}
	public void setLoggedByName(String loggedByName) {
		this.loggedByName = loggedByName;
	}
	public Boolean getIsWalkin() {
		return isWalkin;
	}
	public void setIsWalkin(Boolean isWalkin) {
		this.isWalkin = isWalkin;
	}
	
}