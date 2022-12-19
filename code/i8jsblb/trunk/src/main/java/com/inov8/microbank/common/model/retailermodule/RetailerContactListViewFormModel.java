package com.inov8.microbank.common.model.retailermodule;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.BankModel;

public class RetailerContactListViewFormModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4931055340194256069L;

	public static final String RETAILER_CONTACT_LIST_VIEW_FORM_MODEL_KEY = "RetailerContactListViewFormModel";

	private Long retailerContactId;

	private Long areaId;

	private Long retailerId;

	private Integer versionNo;

	private Double balance;

	private Boolean head;

	private Boolean rso;

	private Boolean active;
	
	private Boolean smsToAgent;
	
	private Boolean smsToHandler;

	private Long appUserId;

	private String firstName;

	private String lastName;

	private String username;

	private String password;

	private String address1;

	private String mobileNo;

	private Long mobileTypeId;

	private String address2;

	private String state;

	private String city;

	private String country;

	private Date dob;

	private String nic;

	private String areaName;

	private String retailerName;

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

	private Boolean passwordChangeRequired;

	private Boolean verified;

	private Long partnerGroupId;

	// ---added by aali01
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
	private String oldNic;
	private Long statusId;
	
	// Added by Fahad
	private String applicationNo;
	private String zongMsisdn;
	private Date accountOpeningDate;
	private String ntnNo;
	private String cnicNo;
	private Date cnicExpiryDate; 
	private String contactNo;
	private String landLineNo;
	private String mobileNumber;
//	private String operatorNo;
	private String businessName;
	private String shopNo;
	private String marketPlaza;
	private Long districtTehsilTown;
	private Long cityVillage;
	private Long postOffice;
	private String ntnNumber;
	private String nearestLandmark;
	private Long natureOfBusiness;
	
	// drop down name fields
	private String printAreaName;
	private String printRetailerName;
	private String printPartnerGroupName;
	private String printDistrictTehsilTownName;
	private String printCityVillageName;
	private String printPostOfficeName;
	private String printNatureOfBusinessName;
	
	
	// added by Rashid Mahmood
	
	private String distributorLevelId;
	private String distributorLevelName;
	private String productCatalogId;
	private String distributorId;
	private String regionId;
	private String distributorName;
	private String regionName;
	private boolean activeDisabled;	
	private boolean accountTypeDisabled;
	private boolean rsoDisabled;
	private String confirmPassword;
	private boolean editMode;
	private Long addressId;
	private Long retailerContactAddressId;
	private String parentRetailerContactId;
	private String partnerGroupName;
	private boolean disabled;				//		disable property can be false in edit mode but edit must be true in edit mode.
	
	private Date retailerContactUpdatedOn;
	private Date retailerContactCreatedOn;
	private Long retailerContactCreatedBy;
	private Long retailerContactUpdatedBy;
	
	private Date appUserUpdatedOn;
	private Date appUserCreatedOn;
	private Long appUserCreatedBy;
	private Long appUserUpdatedBy;
	private Integer appUserVersionNo;
	
	private Long appUserPartnerGroupId;
	private Date appUserPartnerGroupUpdatedOn;
	private Date appUserPartnerGroupCreatedOn;
	private Long appUserPartnerGroupCreatedBy;
	private Long appUserPartnerGroupUpdatedBy;
	private Long appUserPartnerGroupVersionNo;
	
	private String userDeviceAccountUserId;
	private BankModel bankModel;
	private String ProductCatalogName;
	private String parentRetailerContactName;
	private String areaLevelName;
	private String natureOfBusinessName;
	private String districtTehsilTownName;
	private String cityVillageName;
	private String postOfficeName;
	
	private boolean validated;
	private List<String> errors;

	private Long bulkAgentReportId;
	private Integer bulkAgentReportVersionNo;
	private String createdByName;
	private Date bulkAgentUploadReportCreatedOn;
	private Long bulkAgentUploadReportCreatedBy;

	private Date closedOn;
	private String closedBy;
	private String closingComments; 
	private Date settledOn;
	private String settledBy;
	private String settlementComments;
	private Boolean accountClosedUnsettled;
	private Boolean accountClosedSettled;
	private Boolean updateAccountInfo;

	private Long saleUserId;
	private String saleUserName;
	
	private Boolean coreAccountLinked;
	
	public Long getNatureOfBusiness() {
		return natureOfBusiness;
	}

	public void setNatureOfBusiness(Long natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getZongMsisdn() {
		return zongMsisdn;
	}

	public void setZongMsisdn(String zongMsisdn) {
		this.zongMsisdn = zongMsisdn;
	}

	public Date getAccountOpeningDate() {
		return accountOpeningDate;
	}

	public void setAccountOpeningDate(Date accountOpeningDate) {
		this.accountOpeningDate = accountOpeningDate;
	}

	public String getNtnNo() {
		return ntnNo;
	}

	public void setNtnNo(String ntnNo) {
		this.ntnNo = ntnNo;
	}

	public String getCnicNo() {
		return cnicNo;
	}

	public void setCnicNo(String cnicNo) {
		this.cnicNo = cnicNo;
	}

	public Date getCnicExpiryDate() {
		return cnicExpiryDate;
	}

	public void setCnicExpiryDate(Date cnicExpiryDate) {
		this.cnicExpiryDate = cnicExpiryDate;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getLandLineNo() {
		return landLineNo;
	}

	public void setLandLineNo(String landLineNo) {
		this.landLineNo = landLineNo;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

//	public String getOperatorNo() {
//		return operatorNo;
//	}
//
//	public void setOperatorNo(String operatorNo) {
//		this.operatorNo = operatorNo;
//	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getMarketPlaza() {
		return marketPlaza;
	}

	public void setMarketPlaza(String marketPlaza) {
		this.marketPlaza = marketPlaza;
	}

	public Long getDistrictTehsilTown() {
		return districtTehsilTown;
	}

	public void setDistrictTehsilTown(Long districtTehsilTown) {
		this.districtTehsilTown = districtTehsilTown;
	}

	public Long getCityVillage() {
		return cityVillage;
	}

	public void setCityVillage(Long cityVillage) {
		this.cityVillage = cityVillage;
	}

	public Long getPostOffice() {
		return postOffice;
	}

	public void setPostOffice(Long postOffice) {
		this.postOffice = postOffice;
	}

	public String getNtnNumber() {
		return ntnNumber;
	}

	public void setNtnNumber(String ntnNumber) {
		this.ntnNumber = ntnNumber;
	}

	public String getNearestLandmark() {
		return nearestLandmark;
	}

	public void setNearestLandmark(String nearestLandmark) {
		this.nearestLandmark = nearestLandmark;
	}

	public Long getPartnerGroupId() {
		return partnerGroupId;
	}

	public void setPartnerGroupId(Long partnerGroupId) {
		this.partnerGroupId = partnerGroupId;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getSmsToAgent() {
		return smsToAgent;
	}

	public void setSmsToAgent(Boolean smsToAgent) {
		this.smsToAgent = smsToAgent;
	}

	public Boolean getSmsToHandler() {
		return smsToHandler;
	}

	public void setSmsToHandler(Boolean smsToHandler) {
		this.smsToHandler = smsToHandler;
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

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getHead() {
		return head;
	}

	public void setHead(Boolean head) {
		this.head = head;
	}

	public Boolean getRso() {
        return rso;
    }

	public void setRso( Boolean rso ) {
        this.rso = rso;
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

	public Long getMobileTypeId() {
		return mobileTypeId;
	}

	public void setMobileTypeId(Long mobileTypeId) {
		this.mobileTypeId = mobileTypeId;
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getRetailerContactId() {
		return retailerContactId;
	}

	public void setRetailerContactId(Long retailerContactId) {
		this.retailerContactId = retailerContactId;
	}

	public Long getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static String getRETAILER_CONTACT_LIST_VIEW_FORM_MODEL_KEY() {
		return RETAILER_CONTACT_LIST_VIEW_FORM_MODEL_KEY;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
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

	public String getPasswordHint() {
		return passwordHint;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
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

	public Boolean getPasswordChangeRequired()
    {
        return passwordChangeRequired;
    }

	public void setPasswordChangeRequired( Boolean passwordChangeRequired )
    {
        this.passwordChangeRequired = passwordChangeRequired;
    }

	public String getAccountNick() {
		return accountNick;
	}

	public void setAccountNick(String accountNick) {
		this.accountNick = accountNick;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Long getAccountStatusId() {
		return accountStatusId;
	}

	public void setAccountStatusId(Long accountStatusId) {
		this.accountStatusId = accountStatusId;
	}

	public Long getAccountType() {
		return accountType;
	}

	public void setAccountType(Long accountType) {
		this.accountType = accountType;
	}

	public String getAllpayId() {
		return allpayId;
	}

	public void setAllpayId(String allpayId) {
		this.allpayId = allpayId;
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

	public Boolean getCommissioned() {
		return commissioned;
	}

	public void setCommissioned(Boolean commissioned) {
		this.commissioned = commissioned;
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

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
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

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getOldNic() {
		return oldNic;
	}

	public void setOldNic(String oldNic) {
		this.oldNic = oldNic;
	}

	public String getPrintAreaName() {
		return printAreaName;
	}

	public void setPrintAreaName(String printAreaName) {
		this.printAreaName = printAreaName;
	}

	public String getPrintRetailerName() {
		return printRetailerName;
	}

	public void setPrintRetailerName(String printRetailerName) {
		this.printRetailerName = printRetailerName;
	}

	public String getPrintPartnerGroupName() {
		return printPartnerGroupName;
	}

	public void setPrintPartnerGroupName(String printPartnerGroupName) {
		this.printPartnerGroupName = printPartnerGroupName;
	}

	public String getPrintDistrictTehsilTownName() {
		return printDistrictTehsilTownName;
	}

	public void setPrintDistrictTehsilTownName(String printDistrictTehsilTownName) {
		this.printDistrictTehsilTownName = printDistrictTehsilTownName;
	}

	public String getPrintCityVillageName() {
		return printCityVillageName;
	}

	public void setPrintCityVillageName(String printCityVillageName) {
		this.printCityVillageName = printCityVillageName;
	}

	public String getPrintPostOfficeName() {
		return printPostOfficeName;
	}

	public void setPrintPostOfficeName(String printPostOfficeName) {
		this.printPostOfficeName = printPostOfficeName;
	}

	public String getPrintNatureOfBusinessName() {
		return printNatureOfBusinessName;
	}

	public void setPrintNatureOfBusinessName(String printNatureOfBusinessName) {
		this.printNatureOfBusinessName = printNatureOfBusinessName;
	}

	public String getDistributorLevelId() {
		return distributorLevelId;
	}

	public void setDistributorLevelId(String distributorLevelId) {
		this.distributorLevelId = distributorLevelId;
	}

	public String getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(String productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public boolean isActiveDisabled() {
		return activeDisabled;
	}

	public void setActiveDisabled(boolean activeDisabled) {
		this.activeDisabled = activeDisabled;
	}

	public boolean isRsoDisabled() {
        return rsoDisabled;
    }

	public void setRsoDisabled( boolean rsoDisabled ) {
        this.rsoDisabled = rsoDisabled;
    }

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
		if( editMode )
        {
            setRsoDisabled( true );
            setAccountTypeDisabled(false);
        }
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getRetailerContactAddressId() {
		return retailerContactAddressId;
	}

	public void setRetailerContactAddressId(Long retailerContactAddressId) {
		this.retailerContactAddressId = retailerContactAddressId;
	}

	public Long getAppUserPartnerGroupId() {
		return appUserPartnerGroupId;
	}

	public void setAppUserPartnerGroupId(Long appUserPartnerGroupId) {
		this.appUserPartnerGroupId = appUserPartnerGroupId;
	}

	public Integer getAppUserVersionNo() {
		return appUserVersionNo;
	}

	public void setAppUserVersionNo(Integer appUserVersionNo) {
		this.appUserVersionNo = appUserVersionNo;
	}

	public Long getAppUserPartnerGroupVersionNo() {
		return appUserPartnerGroupVersionNo;
	}

	public void setAppUserPartnerGroupVersionNo(Long appUserPartnerGroupVersionNo) {
		this.appUserPartnerGroupVersionNo = appUserPartnerGroupVersionNo;
	}

	public Date getRetailerContactUpdatedOn() {
		return retailerContactUpdatedOn;
	}

	public void setRetailerContactUpdatedOn(Date retailerContactUpdatedOn) {
		this.retailerContactUpdatedOn = retailerContactUpdatedOn;
	}

	public Date getRetailerContactCreatedOn() {
		return retailerContactCreatedOn;
	}

	public void setRetailerContactCreatedOn(Date retailerContactCreatedOn) {
		this.retailerContactCreatedOn = retailerContactCreatedOn;
	}

	public Long getRetailerContactCreatedBy() {
		return retailerContactCreatedBy;
	}

	public void setRetailerContactCreatedBy(Long retailerContactCreatedBy) {
		this.retailerContactCreatedBy = retailerContactCreatedBy;
	}

	public Long getRetailerContactUpdatedBy() {
		return retailerContactUpdatedBy;
	}

	public void setRetailerContactUpdatedBy(Long retailerContactUpdatedBy) {
		this.retailerContactUpdatedBy = retailerContactUpdatedBy;
	}

	public Date getAppUserUpdatedOn() {
		return appUserUpdatedOn;
	}

	public void setAppUserUpdatedOn(Date appUserUpdatedOn) {
		this.appUserUpdatedOn = appUserUpdatedOn;
	}

	public Date getAppUserCreatedOn() {
		return appUserCreatedOn;
	}

	public void setAppUserCreatedOn(Date appUserCreatedOn) {
		this.appUserCreatedOn = appUserCreatedOn;
	}

	public Long getAppUserCreatedBy() {
		return appUserCreatedBy;
	}

	public void setAppUserCreatedBy(Long appUserCreatedBy) {
		this.appUserCreatedBy = appUserCreatedBy;
	}

	public Long getAppUserUpdatedBy() {
		return appUserUpdatedBy;
	}

	public void setAppUserUpdatedBy(Long appUserUpdatedBy) {
		this.appUserUpdatedBy = appUserUpdatedBy;
	}

	public Date getAppUserPartnerGroupUpdatedOn() {
		return appUserPartnerGroupUpdatedOn;
	}

	public void setAppUserPartnerGroupUpdatedOn(Date appUserPartnerGroupUpdatedOn) {
		this.appUserPartnerGroupUpdatedOn = appUserPartnerGroupUpdatedOn;
	}

	public Date getAppUserPartnerGroupCreatedOn() {
		return appUserPartnerGroupCreatedOn;
	}

	public void setAppUserPartnerGroupCreatedOn(Date appUserPartnerGroupCreatedOn) {
		this.appUserPartnerGroupCreatedOn = appUserPartnerGroupCreatedOn;
	}

	public Long getAppUserPartnerGroupCreatedBy() {
		return appUserPartnerGroupCreatedBy;
	}

	public void setAppUserPartnerGroupCreatedBy(Long appUserPartnerGroupCreatedBy) {
		this.appUserPartnerGroupCreatedBy = appUserPartnerGroupCreatedBy;
	}

	public Long getAppUserPartnerGroupUpdatedBy() {
		return appUserPartnerGroupUpdatedBy;
	}

	public void setAppUserPartnerGroupUpdatedBy(Long appUserPartnerGroupUpdatedBy) {
		this.appUserPartnerGroupUpdatedBy = appUserPartnerGroupUpdatedBy;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getParentRetailerContactId() {
		return parentRetailerContactId;
	}

	public void setParentRetailerContactId(String parentRetailerContactId) {
		this.parentRetailerContactId = parentRetailerContactId;
	}

	public String getDistributorLevelName() {
		return distributorLevelName;
	}

	public void setDistributorLevelName(String distributorLevelName) {
		this.distributorLevelName = distributorLevelName;
	}

	public String getPartnerGroupName() {
		return partnerGroupName;
	}

	public void setPartnerGroupName(String partnerGroupName) {
		this.partnerGroupName = partnerGroupName;
	}

	public String getUserDeviceAccountUserId() {
		return userDeviceAccountUserId;
	}

	public void setUserDeviceAccountUserId(String userDeviceAccountUserId) {
		this.userDeviceAccountUserId = userDeviceAccountUserId;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public BankModel getBankModel() {
		return bankModel;
	}

	public void setBankModel(BankModel bankModel) {
		this.bankModel = bankModel;
	}

	public String getProductCatalogName() {
		return ProductCatalogName;
	}

	public void setProductCatalogName(String productCatalogName) {
		ProductCatalogName = productCatalogName;
	}

	public String getParentRetailerContactName() {
		return parentRetailerContactName;
	}

	public void setParentRetailerContactName(String parentRetailerContactName) {
		this.parentRetailerContactName = parentRetailerContactName;
	}

	public String getAreaLevelName() {
		return areaLevelName;
	}

	public void setAreaLevelName(String areaLevelName) {
		this.areaLevelName = areaLevelName;
	}

	public String getNatureOfBusinessName() {
		return natureOfBusinessName;
	}

	public void setNatureOfBusinessName(String natureOfBusinessName) {
		this.natureOfBusinessName = natureOfBusinessName;
	}

	public String getDistrictTehsilTownName() {
		return districtTehsilTownName;
	}

	public void setDistrictTehsilTownName(String districtTehsilTownName) {
		this.districtTehsilTownName = districtTehsilTownName;
	}

	public String getCityVillageName() {
		return cityVillageName;
	}

	public void setCityVillageName(String cityVillageName) {
		this.cityVillageName = cityVillageName;
	}

	public String getPostOfficeName() {
		return postOfficeName;
	}

	public void setPostOfficeName(String postOfficeName) {
		this.postOfficeName = postOfficeName;
	}

	public Long getBulkAgentReportId() {
		return bulkAgentReportId;
	}

	public void setBulkAgentReportId(Long bulkAgentReportId) {
		this.bulkAgentReportId = bulkAgentReportId;
	}

	public Integer getBulkAgentReportVersionNo() {
		return bulkAgentReportVersionNo;
	}

	public void setBulkAgentReportVersionNo(Integer bulkAgentReportVersionNo) {
		this.bulkAgentReportVersionNo = bulkAgentReportVersionNo;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Date getBulkAgentUploadReportCreatedOn() {
		return bulkAgentUploadReportCreatedOn;
	}

	public void setBulkAgentUploadReportCreatedOn(
			Date bulkAgentUploadReportCreatedOn) {
		this.bulkAgentUploadReportCreatedOn = bulkAgentUploadReportCreatedOn;
	}

	public Long getBulkAgentUploadReportCreatedBy() {
		return bulkAgentUploadReportCreatedBy;
	}

	public void setBulkAgentUploadReportCreatedBy(
			Long bulkAgentUploadReportCreatedBy) {
		this.bulkAgentUploadReportCreatedBy = bulkAgentUploadReportCreatedBy;
	}
	
	/**
	 * @return the closedOn
	 */
	public Date getClosedOn() {
		return closedOn;
	}

	/**
	 * @param closedOn the closedOn to set
	 */
	public void setClosedOn(Date closedOn) {
		this.closedOn = closedOn;
	}

	/**
	 * @return the closedBy
	 */
	public String getClosedBy() {
		return closedBy;
	}

	/**
	 * @param closedBy the closedBy to set
	 */
	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	/**
	 * @return the closingComments
	 */
	public String getClosingComments() {
		return closingComments;
	}

	/**
	 * @param closingComments the closingComments to set
	 */
	public void setClosingComments(String closingComments) {
		this.closingComments = closingComments;
	}

	/**
	 * @return the settledOn
	 */
	public Date getSettledOn() {
		return settledOn;
	}

	/**
	 * @param settledOn the settledOn to set
	 */
	public void setSettledOn(Date settledOn) {
		this.settledOn = settledOn;
	}

	/**
	 * @return the settledBy
	 */
	public String getSettledBy() {
		return settledBy;
	}

	/**
	 * @param settledBy the settledBy to set
	 */
	public void setSettledBy(String settledBy) {
		this.settledBy = settledBy;
	}

	/**
	 * @return the settlementComments
	 */
	public String getSettlementComments() {
		return settlementComments;
	}

	/**
	 * @param settlementComments the settlementComments to set
	 */
	public void setSettlementComments(String settlementComments) {
		this.settlementComments = settlementComments;
	}

	/**
	 * @return the accountClosedUnsettled
	 */
	public Boolean getAccountClosedUnsettled() {
		return accountClosedUnsettled;
	}

	/**
	 * @param accountClosedUnsettled the accountClosedUnsettled to set
	 */
	public void setAccountClosedUnsettled(Boolean accountClosedUnsettled) {
		this.accountClosedUnsettled = accountClosedUnsettled;
	}

	/**
	 * @return the accountClosedSettled
	 */
	public Boolean getAccountClosedSettled() {
		return accountClosedSettled;
	}

	/**
	 * @param accountClosedSettled the accountClosedSettled to set
	 */
	public void setAccountClosedSettled(Boolean accountClosedSettled) {
		this.accountClosedSettled = accountClosedSettled;
	}

	public Boolean getUpdateAccountInfo() {
		return updateAccountInfo;
	}

	public void setUpdateAccountInfo(Boolean updateAccountInfo) {
		this.updateAccountInfo = updateAccountInfo;
	}

	public boolean isAccountTypeDisabled() {
		return accountTypeDisabled;
	}

	public void setAccountTypeDisabled(boolean accountTypeDisabled) {
		
			this.accountTypeDisabled = accountTypeDisabled;
		
	}

	public Long getSaleUserId() {
		return saleUserId;
	}

	public void setSaleUserId(Long saleUserId) {
		this.saleUserId = saleUserId;
	}

	public String getSaleUserName() {
		return saleUserName;
	}

	public void setSaleUserName(String saleUserName) {
		this.saleUserName = saleUserName;
	}

	public Boolean getCoreAccountLinked() {
		return coreAccountLinked;
	}

	public void setCoreAccountLinked(Boolean coreAccountLinked) {
		this.coreAccountLinked = coreAccountLinked;
	}

}
