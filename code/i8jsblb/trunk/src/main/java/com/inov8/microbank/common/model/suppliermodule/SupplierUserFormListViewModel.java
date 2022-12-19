package com.inov8.microbank.common.model.suppliermodule;

import java.io.Serializable;
import java.util.Date;

public class SupplierUserFormListViewModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7905199163386678837L;

	public static final String SUPPLIER_USER_FORM_LIST_VIEW_MODEL_KEY = "SupplierUserFormListViewModel";

	private Long supplierUserId;

	private String supplierName;

	private Long supplierId;

	private Integer versionNo;

	private Long appUserId;

	private String firstName;

	private String lastName;

	private String username;

	private String password;

	private String address1;

	private String mobileNo;

	private String name;

	private Long mobileTypeId;

	private String address2;

	private String state;

	private String city;

	private String country;

	private Date dob;

	private String nic;

	private String zip;

	private String email;

	private String fax;

	private String motherMaidenName;

	private String passwordHint;

	private String description;

	private String comments;

	private Boolean accountEnabled;

	private Boolean accountExpired;

	private Boolean accountLocked;

	private Boolean credentialsExpired;

	private Boolean verified;
	
	private Long permissionGroupId;
	
	
	
	private Long partnerGroupId; 

	/**
	 * @return the accessLevelId
	 */
	

	/**
	 * @param accessLevelId the accessLevelId to set
	 */
	

	

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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getSupplierUserId() {
		return supplierUserId;
	}

	public void setSupplierUserId(Long supplierUserId) {
		this.supplierUserId = supplierUserId;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getVersionNo() {
		return versionNo;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getMobileTypeId() {
		return mobileTypeId;
	}

	public void setMobileTypeId(Long mobileTypeId) {
		this.mobileTypeId = mobileTypeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPasswordHint() {
		return passwordHint;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}

	public Long getPartnerGroupId() {
		return partnerGroupId;
	}

	public void setPartnerGroupId(Long partnerGroupId) {
		this.partnerGroupId = partnerGroupId;
	}

	public Long getPermissionGroupId() {
		return permissionGroupId;
	}

	public void setPermissionGroupId(Long permissionGroupId) {
		this.permissionGroupId = permissionGroupId;
	}



}
