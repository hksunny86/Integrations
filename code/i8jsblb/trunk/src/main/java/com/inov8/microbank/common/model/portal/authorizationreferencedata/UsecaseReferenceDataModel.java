package com.inov8.microbank.common.model.portal.authorizationreferencedata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;

public class UsecaseReferenceDataModel {	   
	   
	   private Collection<UsecaseLevelRefDataModel> usecaseIdLevelModelList = new ArrayList<UsecaseLevelRefDataModel>();
	   private Long usecaseId;
	   private String name;
	   private String description;
	   private String comments;
	   private Long escalationLevels;
	   private Boolean isAuthorizationEnable;
	   private Integer versionNo;
	   private String authorizationStatus;
	   private Date updatedOn;
	   private Date createdOn;
	   private Long createdBy;
	   private Long updatedBy;
	   
	   private String[] levelcheckers = new String[20];
		
	public UsecaseReferenceDataModel() {
	}
	
	   
	public Collection<UsecaseLevelRefDataModel> getUsecaseIdLevelModelList() {
		return usecaseIdLevelModelList;
	}

	public void setUsecaseIdLevelModelList(
			Collection<UsecaseLevelRefDataModel> usecaseIdLevelModelList) {
		this.usecaseIdLevelModelList = usecaseIdLevelModelList;
	}

	public Long getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getEscalationLevels() {
		return escalationLevels;
	}

	public void setEscalationLevels(Long escalationLevels) {
		this.escalationLevels = escalationLevels;
	}

	public Boolean getIsAuthorizationEnable() {
		return isAuthorizationEnable;
	}

	public void setIsAuthorizationEnable(Boolean isAuthorizationEnable) {
		this.isAuthorizationEnable = isAuthorizationEnable;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getAuthorizationStatus() {
		if(isAuthorizationEnable)
			return "Enable";
		else 
			return "Disable";
	}

	public void setAuthorizationStatus(String authorizationStatus) {
		this.authorizationStatus = authorizationStatus;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public Long getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}


	public Long getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String[] getLevelcheckers() {
		return levelcheckers;
	}

	public void setLevelcheckers(String[] levelcheckers) {
		this.levelcheckers = levelcheckers;
	}
}
