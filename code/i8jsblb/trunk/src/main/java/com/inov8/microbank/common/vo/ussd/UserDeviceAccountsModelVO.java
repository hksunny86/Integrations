package com.inov8.microbank.common.vo.ussd;

import java.io.Serializable;
import java.util.Date;

public class UserDeviceAccountsModelVO implements Serializable{
	 
	private static final long serialVersionUID = -111657116186719334L;

	private Long userDeviceAccountsId;
	 
	 private Long deviceTypeId;
	 private Long appUserId;
	 
	   private String userId;
	   private String pin;
	   private Boolean accountExpired;
	   private Boolean accountEnabled;
	   private Boolean accountLocked;
	   private Boolean credentialsExpired;
	   private Date createdOn;
	   private Date updatedOn;
	   private Integer versionNo;
	   private Boolean pinChangeRequired;
	   private java.sql.Timestamp lastLoginAttemptTime;
	   private Integer loginAttemptCount;
	   private Date expiryDate;
	   private Boolean commissioned;
	   private String password;
	   private Boolean passwordChangeRequired;
	public Long getUserDeviceAccountsId() {
		return userDeviceAccountsId;
	}
	public void setUserDeviceAccountsId(Long userDeviceAccountsId) {
		this.userDeviceAccountsId = userDeviceAccountsId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public Boolean getAccountExpired() {
		return accountExpired;
	}
	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
	}
	public Boolean getAccountEnabled() {
		return accountEnabled;
	}
	public void setAccountEnabled(Boolean accountEnabled) {
		this.accountEnabled = accountEnabled;
	}
	public Boolean getAccountLocked() {
		return accountLocked;
	}
	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}
	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}
	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
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
	public Integer getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	public Boolean getPinChangeRequired() {
		return pinChangeRequired;
	}
	public void setPinChangeRequired(Boolean pinChangeRequired) {
		this.pinChangeRequired = pinChangeRequired;
	}
	public java.sql.Timestamp getLastLoginAttemptTime() {
		return lastLoginAttemptTime;
	}
	public void setLastLoginAttemptTime(java.sql.Timestamp lastLoginAttemptTime) {
		this.lastLoginAttemptTime = lastLoginAttemptTime;
	}
	public Integer getLoginAttemptCount() {
		return loginAttemptCount;
	}
	public void setLoginAttemptCount(Integer loginAttemptCount) {
		this.loginAttemptCount = loginAttemptCount;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Boolean getCommissioned() {
		return commissioned;
	}
	public void setCommissioned(Boolean commissioned) {
		this.commissioned = commissioned;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getPasswordChangeRequired() {
		return passwordChangeRequired;
	}
	public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
		this.passwordChangeRequired = passwordChangeRequired;
	}
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public Long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
}
