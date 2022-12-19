package com.inov8.microbank.common.model.portal.authorizationreferencedata;

import java.util.ArrayList;
import java.util.List;

import com.inov8.microbank.common.model.portal.partnergroupmodule.UserPermissionWrapper;

public class UserGroupReferenceDataModel {
	
   private Long partnerGroupId;
   private String name;
   private String email;
   private String description;
   private Long partnerId;
   private String partnerName;
   private Boolean active;
   private Boolean editable;
   private Long appUserTypeId;
   private String appUserTypeName;
   
   private List<UserPermissionWrapper> userPermissionList;

	public UserGroupReferenceDataModel() {
		// TODO Auto-generated constructor stub
	}

	public Long getPartnerGroupId() {
		return partnerGroupId;
	}

	public void setPartnerGroupId(Long partnerGroupId) {
		this.partnerGroupId = partnerGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Long getAppUserTypeId() {
		return appUserTypeId;
	}

	public void setAppUserTypeId(Long appUserTypeId) {
		this.appUserTypeId = appUserTypeId;
	}

	public String getAppUserTypeName() {
		return appUserTypeName;
	}

	public void setAppUserTypeName(String appUserTypeName) {
		this.appUserTypeName = appUserTypeName;
	}

	public List<UserPermissionWrapper> getUserPermissionList() {
		return userPermissionList;
	}

	public void setUserPermissionList(List<UserPermissionWrapper> userPermissionList) {
		
		
		this.userPermissionList = new ArrayList<UserPermissionWrapper>(userPermissionList);
	}

}
