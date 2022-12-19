package com.inov8.microbank.common.model.mnomodule;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class MnoUserFormModel
 implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8225326475052148869L;
private Long bankId;
  private String contactName;
  private String mobileNo;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String country;
  private String zip;
  private String mnoId;
  private String mnoUserId;
  private String email;
  private String fax;
  private Date updatedOn;
  private Date createdOn;
  private Integer mnoModelVersionNo;
  private Integer appUserModelVersionNo;
  private Long createdBy;
  private Long updatedBy;
  private String mnoName;
  private String comments;
  private String description;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String mobileTypeId;
  private String oldMobileNo;
  private String motherMaidenName;
  private String nic;  
  private String appUserId;
  private Date dob;
  private String passwordHint;
  private String referringName1;
  private String referringNic1;
  private String referringName2;
  private String referringNic2;  
  private Boolean accountEnabled;
  private Boolean accountExpired;
  private Boolean accountLocked;
  private Boolean credentialsExpired;
  private Boolean verified;
  private Long partnerGroupId;

  /**
 * @return the accessLevelId
 */


/**
 * @param accessLevelId the accessLevelId to set
 */


public Boolean getVerified()
{
	return verified;
}

public void setVerified(Boolean verified)
{
	this.verified = verified;
}

public Long getBankId()
  {
    return bankId;
  }

  public String getContactName()
  {
    return contactName;
  }


  public String getAddress1()
  {
    return address1;
  }

  public String getAddress2()
  {
    return address2;
  }

  public String getCity()
  {
    return city;
  }

  public String getState()
  {
    return state;
  }

  public String getCountry()
  {
    return country;
  }

  public String getZip()
  {
    return zip;
  }


  public String getEmail()
  {
    return email;
  }

  public String getFax()
  {
    return fax;
  }

  public String getMnoId()
  {
    return mnoId;
  }

  public Date getCreatedOn()
  {
    return createdOn;
  }

  public Long getCreatedBy()
  {
    return createdBy;
  }

  public Long getUpdatedBy()
  {
    return updatedBy;
  }

  public Date getUpdatedOn()
  {
    return updatedOn;
  }

  

  public String getMnoName()
  {
    return mnoName;
  }

  public String getComments()
  {
    return comments;
  }

  public String getDescription()
  {
    return description;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getMobileNo()
  {
    return mobileNo;
  }

  public void setBankId(Long bankId)
  {
    this.bankId = bankId;
  }

  public void setContactName(String contactName)
  {
    this.contactName = contactName;
  }

  public void setAddress1(String address1)
  {
    this.address1 = address1;
  }

  public void setAddress2(String address2)
  {
    this.address2 = address2;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public void setZip(String zip)
  {
    this.zip = zip;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setFax(String fax)
  {
    this.fax = fax;
  }

  public void setMnoId(String mnoId)
  {
    this.mnoId = mnoId;
  }

  public void setCreatedBy(Long createdBy)
  {
    this.createdBy = createdBy;
  }

  public void setCreatedOn(Date createdOn)
  {
    this.createdOn = createdOn;
  }

  public Integer getAppUserModelVersionNo() {
	return appUserModelVersionNo;
}

public void setAppUserModelVersionNo(Integer appUserModelVersionNo) {
	this.appUserModelVersionNo = appUserModelVersionNo;
}

public Integer getMnoModelVersionNo() {
	return mnoModelVersionNo;
}

public void setMnoModelVersionNo(Integer mnoModelVersionNo) {
	this.mnoModelVersionNo = mnoModelVersionNo;
}

public void setUpdatedOn(Date updatedOn)
  {
    this.updatedOn = updatedOn;
  }

  public void setUpdatedBy(Long updatedBy)
  {
    this.updatedBy = updatedBy;
  }

  public void setMnoName(String mnoName)
  {
    this.mnoName = mnoName;
  }

  public void setComments(String comments)
  {
    this.comments = comments;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public void setMobileNo(String mobileNo)
  {
    this.mobileNo = mobileNo;
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

public String getMobileTypeId() {
	return mobileTypeId;
}

public void setMobileTypeId(String mobileTypeId) {
	this.mobileTypeId = mobileTypeId;
}

public String getOldMobileNo() {
	return oldMobileNo;
}

public void setOldMobileNo(String oldMobileNo) {
	this.oldMobileNo = oldMobileNo;
}

public String getMnoUserId() {
	return mnoUserId;
}

public void setMnoUserId(String mnoUserId) {
	this.mnoUserId = mnoUserId;
}

public String getAppUserId() {
	return appUserId;
}

public void setAppUserId(String appUserId) {
	this.appUserId = appUserId;
}

public void setAccountEnabled(Boolean accountEnabled)
{
	this.accountEnabled = accountEnabled;
}

public void setAccountExpired(Boolean accountExpired)
{
	this.accountExpired = accountExpired;
}

public void setAccountLocked(Boolean accountLocked)
{
	this.accountLocked = accountLocked;
}

public void setCredentialsExpired(Boolean credentialsExpired)
{
	this.credentialsExpired = credentialsExpired;
}

public Boolean getAccountEnabled()
{
	return accountEnabled;
}

public Boolean getAccountExpired()
{
	return accountExpired;
}

public Boolean getAccountLocked()
{
	return accountLocked;
}

public Boolean getCredentialsExpired()
{
	return credentialsExpired;
}

public Date getDob()
{
	return dob;
}

public void setDob(Date dob)
{
	this.dob = dob;
}

public String getMotherMaidenName()
{
	return motherMaidenName;
}

public void setMotherMaidenName(String motherMaidenName)
{
	this.motherMaidenName = motherMaidenName;
}

public String getNic()
{
	return nic;
}

public void setNic(String nic)
{
	this.nic = nic;
}

public String getReferringName1()
{
	return referringName1;
}

public void setReferringName1(String referringName1)
{
	this.referringName1 = referringName1;
}

public String getReferringName2()
{
	return referringName2;
}

public void setReferringName2(String referringName2)
{
	this.referringName2 = referringName2;
}

public String getReferringNic1()
{
	return referringNic1;
}

public void setReferringNic1(String referringNic1)
{
	this.referringNic1 = referringNic1;
}

public String getReferringNic2()
{
	return referringNic2;
}

public void setReferringNic2(String referringNic2)
{
	this.referringNic2 = referringNic2;
}

public String getPasswordHint()
{
	return passwordHint;
}

public void setPasswordHint(String passwordHint)
{
	this.passwordHint = passwordHint;
}

public Long getPartnerGroupId() {
	return partnerGroupId;
}

public void setPartnerGroupId(Long partnerGroupId) {
	this.partnerGroupId = partnerGroupId;
}

}
