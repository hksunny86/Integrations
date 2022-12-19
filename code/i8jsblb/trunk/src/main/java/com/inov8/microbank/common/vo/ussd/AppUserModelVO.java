package com.inov8.microbank.common.vo.ussd;

import java.io.Serializable;
import java.util.Date;

public class AppUserModelVO implements Serializable{
	
	private static final long serialVersionUID = 3975934560559172812L;

	private Long appUserId;
	   
	   private Long appUserTypeId;
	   private Long supplierUserId;
	   private Long retailerContactId;
	   private Long operatorUserId;
	   private Long mobileTypeId;
	   private Long mnoUserId;
	   private Long distributorContactId;
	   private Long customerId;
	   private Long bankUserId;
	   
	   private String firstName;
	   private String lastName;
	   private String address1;
	   private String address2;
	   private String city;
	   private String state;
	   private String country;
	   private String zip;
	   private String nic;
	   private String email;
	   private String fax;
	   private String motherMaidenName;
	   private String username;
	   private String password;
	   private String mobileNo;
	   private String passwordHint;
	   private Boolean verified;
	   private Boolean accountEnabled;
	   private Boolean accountExpired;
	   private Boolean accountLocked;
	   private Boolean credentialsExpired;
	   private Boolean passwordChangeRequired;
	   private Date createdOn;
	   private Date updatedOn;
	   private Integer versionNo;
	   private Date dob;
	   private java.sql.Timestamp lastLoginAttemptTime;
	   private Integer loginAttemptCount;
	   private Date nicExpiryDate;
	   
	   private Boolean accountClosedUnsettled;
	   private Boolean accountClosedSettled;
	   private Boolean rso;
	   
	public Boolean isRso() {
		return rso;
	}
	public void setRso(Boolean rso) {
		this.rso = rso;
	}
	public Long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getNic() {
		return nic;
	}
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getMotherMaidenName() {
		return motherMaidenName;
	}
	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPasswordHint() {
		return passwordHint;
	}
	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	public Boolean getAccountEnabled() {
		return accountEnabled;
	}
	public void setAccountEnabled(Boolean accountEnabled) {
		this.accountEnabled = accountEnabled;
	}
	public Boolean getAccountExpired() {
		return accountExpired;
	}
	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
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
	public Boolean getPasswordChangeRequired() {
		return passwordChangeRequired;
	}
	public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
		this.passwordChangeRequired = passwordChangeRequired;
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
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
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
	public Date getNicExpiryDate() {
		return nicExpiryDate;
	}
	public void setNicExpiryDate(Date nicExpiryDate) {
		this.nicExpiryDate = nicExpiryDate;
	}
	public Long getAppUserTypeId() {
		return appUserTypeId;
	}
	public void setAppUserTypeId(Long appUserTypeId) {
		this.appUserTypeId = appUserTypeId;
	}
	public Long getSupplierUserId() {
		return supplierUserId;
	}
	public void setSupplierUserId(Long supplierUserId) {
		this.supplierUserId = supplierUserId;
	}
	public Long getRetailerContactId() {
		return retailerContactId;
	}
	public void setRetailerContactId(Long retailerContactId) {
		this.retailerContactId = retailerContactId;
	}
	public Long getOperatorUserId() {
		return operatorUserId;
	}
	public void setOperatorUserId(Long operatorUserId) {
		this.operatorUserId = operatorUserId;
	}
	public Long getMobileTypeId() {
		return mobileTypeId;
	}
	public void setMobileTypeId(Long mobileTypeId) {
		this.mobileTypeId = mobileTypeId;
	}
	public Long getMnoUserId() {
		return mnoUserId;
	}
	public void setMnoUserId(Long mnoUserId) {
		this.mnoUserId = mnoUserId;
	}
	public Long getDistributorContactId() {
		return distributorContactId;
	}
	public void setDistributorContactId(Long distributorContactId) {
		this.distributorContactId = distributorContactId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getBankUserId() {
		return bankUserId;
	}
	public void setBankUserId(Long bankUserId) {
		this.bankUserId = bankUserId;
	}
	public Boolean getAccountClosedUnsettled() {
		return accountClosedUnsettled;
	}
	public void setAccountClosedUnsettled(Boolean accountClosedUnsettled) {
		this.accountClosedUnsettled = accountClosedUnsettled;
	}
	public Boolean getAccountClosedSettled() {
		return accountClosedSettled;
	}
	public void setAccountClosedSettled(Boolean accountClosedSettled) {
		this.accountClosedSettled = accountClosedSettled;
	}
}
