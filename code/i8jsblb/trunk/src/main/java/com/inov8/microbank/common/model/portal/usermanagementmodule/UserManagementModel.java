package com.inov8.microbank.common.model.portal.usermanagementmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

public class UserManagementModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3044346947137403110L;
	private Long appUserId;
    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String email;
    private Date dob;
    private String mobileNo;
    private Boolean isActive;
    private String  isActiveStatus;
    private Long appUserTypeId;
    //private Long appUserRoleId;
    private Long mobileTypeId;
    private Long partnerId;
    private Boolean isPasswordChanged;
    
    private Long usecaseId;
    private Long actionId;
    private Long accessLevelId;
    private Long partnerGroupId;
    
    private String appUserType;
    private String mobileType;
    private String partnerGroup;
    private String partnerType;
    private Long employeeId;
    private String tellerId;
    
	public static final String USER_MANAGEMENT_MODEL_KEY = "userManagementModelKey";
    
	public Long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getIsActiveStatus() {
		if(this.isActive)
			this.isActiveStatus="Active";
		else
			this.isActiveStatus="Inactive";
		return isActiveStatus;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public Long getUsecaseId() {
		return usecaseId;
	}
	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}
	public void setAppUserTypeId(Long appUserTypeId) {
		this.appUserTypeId = appUserTypeId;
	}
	public Long getAppUserTypeId() {
		return appUserTypeId;
	}
	public Long getMobileTypeId() {
		return mobileTypeId;
	}
	public void setMobileTypeId(Long mobileTypeId) {
		this.mobileTypeId = mobileTypeId;
	}
	public Long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public Boolean getIsPasswordChanged() {
		return isPasswordChanged;
	}
	public void setIsPasswordChanged(Boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}
	 public String getAppUserType() {
			return appUserType;
	}
	public void setAppUserType(String appUserType) {
		this.appUserType = appUserType;
	}
	public String getMobileType() {
		return mobileType;
	}
	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}
	public String getPartnerGroup() {
		return partnerGroup;
	}
	public void setPartnerGroup(String partnerGroup) {
		this.partnerGroup = partnerGroup;
	}
	public String getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}
	/**
	 * @return the appUserRoleId
	 */
	/*public Long getAppUserRoleId()
	{
		return appUserRoleId;
	}
	/**
	 * @param appUserRoleId the appUserRoleId to set
	 */
	/*public void setAppUserRoleId(Long appUserRoleId)
	{
		this.appUserRoleId = appUserRoleId;
	}*/
	public Long getAccessLevelId() {
		return accessLevelId;
	}
	public void setAccessLevelId(Long accessLevelId) {
		this.accessLevelId = accessLevelId;
	}
	public void setPartnerGroupId(Long partnerGroupId) {
		this.partnerGroupId = partnerGroupId;
	}
	public Long getPartnerGroupId() {
		return partnerGroupId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}		
	
	@Transient
	public String getTellerId() {
		return tellerId;
	}
	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}
}
