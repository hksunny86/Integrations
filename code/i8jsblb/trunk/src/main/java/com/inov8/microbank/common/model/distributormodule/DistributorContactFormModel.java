package com.inov8.microbank.common.model.distributormodule;

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
public class DistributorContactFormModel
 implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 4252706151671874538L;
private Long distributorContactId;
  private String contactName;
  private String distributorName;
  private String mobileNo;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String country;
  private String zip;
  private String distributorId;
  private String mnoUserId;
  private String email;
  private String fax;
  private Date updatedOn;
  private Date createdOn;
  private Integer distributorContactModelVersionNo;
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
  private String appUserId;
  private String versionNo;
  private String distributorLevelId;
  private String distributorLevelName;
  private String managingContactId;
  private String managingContactName;
  private String areaId;
  private String areaName;
  private String ssn;
  private String balance;
  private String head;
  private String active;
  private String levelName;
  private String passwordHint;
  private String motherMaidenName;
  private String nic ; 
  private Date dob ;
  private String accountExpired ;
  private String accountLocked ;
  private String accountEnabled ;
  private String credentialsExpired ;
  private String verified ;
  private Long partnerGroupId;
  // added by aali01
  private String middleName;
  private String fatherName;
  private String landlineNumber;
  private Long accountStatusId;
	private String name;
	private String allpayId;
	private String accountNick;
	private Long paymentMode;
	private Long cardType;
	private String cardNo;	
	private String accountNo;
	private Date expiryDate;	
	private Long accountType;
	private Long currencyCode;
	private Boolean commissioned;
	private Long statusId;
	private String oldNic;
  ///////////////////////////////
  public String getNic()
{
	return nic;
}


public void setNic(String nic)
{
	this.nic = nic;
}


public String getActive()
{
	return active;
}


public void setActive(String active)
{
	this.active = active;
}


public String getBalance()
{
	return balance;
}


public void setBalance(String balance)
{
	this.balance = balance;
}


public String getHead()
{
	return head;
}


public void setHead(String head)
{
	this.head = head;
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


public Integer getDistributorContactModelVersionNo()
{
	return distributorContactModelVersionNo;
}


public void setDistributorContactModelVersionNo(Integer distributorContactModelVersionNo)
{
	this.distributorContactModelVersionNo = distributorContactModelVersionNo;
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

public Long getDistributorContactId()
{
	return distributorContactId;
}

public void setDistributorContactId(Long distributorContactId)
{
	this.distributorContactId = distributorContactId;
}

public String getDistributorId()
{
	return distributorId;
}

public void setDistributorId(String distributorId)
{
	this.distributorId = distributorId;
}


public String getVersionNo()
{
	return versionNo;
}


public void setVersionNo(String versionNo)
{
	this.versionNo = versionNo;
}


public String getDistributorName()
{
	return distributorName;
}


public void setDistributorName(String distributorName)
{
	this.distributorName = distributorName;
}


public String getDistributorLevelId()
{
	return distributorLevelId;
}


public void setDistributorLevelId(String distributorLevelId)
{
	this.distributorLevelId = distributorLevelId;
}


public String getDistributorLevelName()
{
	return distributorLevelName;
}


public void setDistributorLevelName(String distributorLevelName)
{
	this.distributorLevelName = distributorLevelName;
}


public String getAreaId()
{
	return areaId;
}


public void setAreaId(String areaId)
{
	this.areaId = areaId;
}


public String getAreaName()
{
	return areaName;
}


public void setAreaName(String areaName)
{
	this.areaName = areaName;
}


public String getManagingContactId()
{
	return managingContactId;
}


public void setManagingContactId(String managingContactId)
{
	this.managingContactId = managingContactId;
}


public String getManagingContactName()
{
	return managingContactName;
}


public void setManagingContactName(String managingContactName)
{
	this.managingContactName = managingContactName;
}


public String getSsn()
{
	return ssn;
}


public void setSsn(String ssn)
{
	this.ssn = ssn;
}


public String getLevelName()
{
	return levelName;
}


public void setLevelName(String levelName)
{
	this.levelName = levelName;
}


public Date getDob()
{
	return dob;
}


public void setDob(Date dob)
{
	this.dob = dob;
}


public String getAccountEnabled()
{
	return accountEnabled;
}


public void setAccountEnabled(String accountEnabled)
{
	this.accountEnabled = accountEnabled;
}


public String getAccountExpired()
{
	return accountExpired;
}


public void setAccountExpired(String accountExpired)
{
	this.accountExpired = accountExpired;
}


public String getAccountLocked()
{
	return accountLocked;
}


public void setAccountLocked(String accountLocked)
{
	this.accountLocked = accountLocked;
}


public String getCredentialsExpired()
{
	return credentialsExpired;
}


public void setCredentialsExpired(String credentialsExpired)
{
	this.credentialsExpired = credentialsExpired;
}


public String getVerified()
{
	return verified;
}


public void setVerified(String verified)
{
	this.verified = verified;
}


public String getPasswordHint()
{
	return passwordHint;
}


public void setPasswordHint(String passwordHint)
{
	this.passwordHint = passwordHint;
}


public String getMotherMaidenName()
{
	return motherMaidenName;
}


public void setMotherMaidenName(String motherMaidenName)
{
	this.motherMaidenName = motherMaidenName;
}


public Long getPartnerGroupId() {
	return partnerGroupId;
}


public void setPartnerGroupId(Long partnerGroupId) {
	this.partnerGroupId = partnerGroupId;
}


public String getAccountNo() {
	return accountNo;
}


public void setAccountNo(String accountNo) {
	this.accountNo = accountNo;
}





public Long getAccountType() {
	return accountType;
}


public void setAccountType(Long accountType) {
	this.accountType = accountType;
}


public String getCardNo() {
	return cardNo;
}


public void setCardNo(String cardNo) {
	this.cardNo = cardNo;
}


public Long getCardType() {
	return cardType;
}


public void setCardType(Long cardType) {
	this.cardType = cardType;
}


public Long getCurrencyCode() {
	return currencyCode;
}


public void setCurrencyCode(Long currencyCode) {
	this.currencyCode = currencyCode;
}


public Date getExpiryDate() {
	return expiryDate;
}


public void setExpiryDate(Date expiryDate) {
	this.expiryDate = expiryDate;
}

public String getLandlineNumber() {
	return landlineNumber;
}


public void setLandlineNumber(String landlineNumber) {
	this.landlineNumber = landlineNumber;
}


public String getMiddleName() {
	return middleName;
}


public void setMiddleName(String middleName) {
	this.middleName = middleName;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public Long getPaymentMode() {
	return paymentMode;
}


public void setPaymentMode(Long paymentMode) {
	this.paymentMode = paymentMode;
}


public String getAllpayId() {
	return allpayId;
}


public void setAllpayId(String allpayId) {
	this.allpayId = allpayId;
}


public String getAccountNick() {
	return accountNick;
}


public void setAccountNick(String accountNick) {
	this.accountNick = accountNick;
}


public String getFatherName() {
	return fatherName;
}


public void setFatherName(String fatherName) {
	this.fatherName = fatherName;
}


public Long getAccountStatusId() {
	return accountStatusId;
}


public void setAccountStatusId(Long accountStatusId) {
	this.accountStatusId = accountStatusId;
}


public Boolean getCommissioned() {
	return commissioned;
}


public void setCommissioned(Boolean commissioned) {
	this.commissioned = commissioned;
}


public Long getStatusId() {
	return statusId;
}


public void setStatusId(Long statusId) {
	this.statusId = statusId;
}


public String getOldNic() {
	return oldNic;
}


public void setOldNic(String oldNic) {
	this.oldNic = oldNic;
}

}
