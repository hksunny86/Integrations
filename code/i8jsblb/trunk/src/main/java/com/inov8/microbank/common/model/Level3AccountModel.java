package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.NokDetailVOModel;

public class Level3AccountModel implements Serializable,Cloneable
{
	private static final long serialVersionUID = -3805384140828441036L;

	public static final String LEVEL3_ACCOUNT_MODEL_KEY = "level3AccountModelKey";

	private String mobileNo; 
	private Long appUserId;
	private Long usecaseId;
	private Long actionId;

	private String searchFirstName;
	private String searchLastName;
	private String searchMfsId;
	private String searchNic;

	private String applicationNo;
	private Long segmentId;
	private Long customerTypeId;
	private Long fundsSourceId;
	private Long customerAccountTypeId;
	private String refferedBy;
	private String segmentName;
	private String languageName;
	private String typeOfCustomerName;
	private String fundSourceName;
	private String customerAccountName;

	private String accountNo;
	private String accountStatus;
	private Double accountBalance;
	private Long retailerContactId;
	private Boolean active;
	private String allpayId;
	private Date createdOn;
	private String createdBy;
	private Date closedOn;
	private String closedBy;
	private String closingComments;
	private Date settledOn;
	private String settledBy;
	private String settlementComments;
	private Boolean accountClosedUnsettled;
	private Boolean accountClosedSettled;
	private String location;
	private Long currencyId;
	private String occupation;
	private String typeOfAccount;
	private Long registrationStateId;
	private String modeOfTx;
	private Long businessTypeId;
	private Long accountReason;
	private String salary;
	private String buisnessIncome;
	private String otherIncome;
	private Long depositChannel;
	private ApplicantDetailModel applicant1DetailModel;
	private List<ApplicantDetailModel> applicantDetailModelList = new ArrayList<ApplicantDetailModel>(0);
	private ApplicantDetailModel applicantDetailModelEditMode;
	private NokDetailVOModel nokDetailVOModel;
	private Long transactionModeId;
	private String otherTransactionMode;
	private Long accountPurposeId;
	private String accountPurpose;
	private String comments;
	private String regStateComments;
	private String mfsId;
	private boolean screeningPerformed;
	private String initialAppFormNo;
	private Long acNature;
	private String acNatureName;
	private Date commencementDate;
	private Date secpRegDate;
	private String tradeBody;
	private Long businessNatureId;
	private Long locationTypeId;
	private Long locationSizeId;
	private Long estSince;
	private Long nokIdType;
	private String nokIdNumber;
	private String acTitle;
	private String businessName;
	private String businessAddress;
	private Long businessAddCity;
	private Long businessPostalCode;
	private String corresspondanceAddress;
	private Long corresspondanceAddCity;
	private Long corresspondancePostalCode;
	private Date incorporationDate;
	private String secpRegNo;
	private String ntn;
	private String salesTaxRegNo;
	private String membershipNoTradeBody;
	private String landLineNo;
	private List<ACOwnershipDetailModel> acOwnershipDetailModelList;
	private Boolean verisysDone;
	private Long taxRegimeId;
	private Double fed;
	private Long employeeID;
	private String employeeName;
	private String coreAccountNumber;
	private String coreAccountTitle;
	private Date accountOpeningDate;
	private String regStateName;
    private String accounttypeName;
    private String birthPlaceName;
    private String taxRegimeName;
    
    private String businessAddCityName;
    private String businessTypeName;
    private String businessNatureName;
    private String locationTypeName;
    private String locationSizeName;
    private String corresspondanceAddCityName;
    private boolean accountLinked;
    private String distributorName;
    
    private Boolean filer;
	private Boolean bvsEnable;
	
	private Double latitude;
	private Double longitude;
	//Added By Sheheryaar
	private Long accountStateId;

	private Long serviceOperatorId;
	public Level3AccountModel()
	{
	}

	public String getAccountNo()
	{
		return accountNo;
	}

	public void setAccountNo(String accountNo)
	{
		this.accountNo = accountNo;
	}

	public String getAccountStatus()
	{
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus)
	{
		this.accountStatus = accountStatus;
	}

	public String getRefferedBy()
	{
		return refferedBy;
	}

	public void setRefferedBy(String refferedBy)
	{
		this.refferedBy = refferedBy;
	}

	public String getMobileNo()
	{
		return mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public Long getAppUserId()
	{
		return appUserId;
	}

	public void setAppUserId(Long appUserId)
	{
		this.appUserId = appUserId;
	}

	public Long getActionId()
	{
		return actionId;
	}

	public void setActionId(Long actionId)
	{
		this.actionId = actionId;
	}

	public Long getUsecaseId()
	{
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId)
	{
		this.usecaseId = usecaseId;
	}

	public String getSearchFirstName()
	{
		return searchFirstName;
	}

	public String getSearchMfsId()
	{
		return searchMfsId;
	}

	public void setSearchMfsId(String searchMfsId)
	{
		this.searchMfsId = searchMfsId;
	}

	public String getSearchLastName()
	{
		return searchLastName;
	}

	public void setSearchLastName(String searchLastName)
	{
		this.searchLastName = searchLastName;
	}

	public String getSearchNic()
	{
		return searchNic;
	}

	public void setSearchNic(String searchNic)
	{
		this.searchNic = searchNic;
	}

	public void setSearchFirstName(String searchFirstName)
	{
		this.searchFirstName = searchFirstName;
	}

	public String getApplicationNo()
	{
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo)
	{
		this.applicationNo = applicationNo;
	}

	public Long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(Long segmentId)
	{
		this.segmentId = segmentId;
	}

	public Long getCustomerTypeId()
	{
		return customerTypeId;
	}

	public void setCustomerTypeId(Long customerTypeId)
	{
		this.customerTypeId = customerTypeId;
	}

	public Long getFundsSourceId()
	{
		return fundsSourceId;
	}

	public void setFundsSourceId(Long fundsSourceId)
	{
		this.fundsSourceId = fundsSourceId;
	}

	public Long getCustomerAccountTypeId()
	{
		return customerAccountTypeId;
	}

	public void setCustomerAccountTypeId(Long customerAccountTypeId)
	{
		this.customerAccountTypeId = customerAccountTypeId;
	}

	public String getSegmentName()
	{
		return segmentName;
	}

	public void setSegmentName(String segmentName)
	{
		this.segmentName = segmentName;
	}

	public String getLanguageName()
	{
		return languageName;
	}

	public void setLanguageName(String languageName)
	{
		this.languageName = languageName;
	}

	public String getTypeOfCustomerName()
	{
		return typeOfCustomerName;
	}

	public void setTypeOfCustomerName(String typeOfCustomerName)
	{
		this.typeOfCustomerName = typeOfCustomerName;
	}

	public String getFundSourceName()
	{
		return fundSourceName;
	}

	public void setFundSourceName(String fundSourceName)
	{
		this.fundSourceName = fundSourceName;
	}

	public String getCustomerAccountName()
	{
		return customerAccountName;
	}

	public void setCustomerAccountName(String customerAccountName)
	{
		this.customerAccountName = customerAccountName;
	}

	public Long getEmployeeID()
	{
		return employeeID;
	}

	public void setEmployeeID(Long long1)
	{
		this.employeeID = long1;
	}

	public Double getAccountBalance()
	{
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance)
	{
		this.accountBalance = accountBalance;
	}

	public Long getRetailerContactId()
	{
		return retailerContactId;
	}

	public void setRetailerContactId(Long retailerContactId)
	{
		this.retailerContactId = retailerContactId;
	}

	public Boolean getActive()
	{
		return active;
	}

	public void setActive(Boolean active)
	{
		this.active = active;
	}

	public String getAllpayId()
	{
		return allpayId;
	}

	public void setAllpayId(String allpayId)
	{
		this.allpayId = allpayId;
	}

	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Date getClosedOn()
	{
		return closedOn;
	}

	public void setClosedOn(Date closedOn)
	{
		this.closedOn = closedOn;
	}

	public String getClosedBy()
	{
		return closedBy;
	}

	public void setClosedBy(String closedBy)
	{
		this.closedBy = closedBy;
	}

	public Date getSettledOn()
	{
		return settledOn;
	}

	public void setSettledOn(Date settledOn)
	{
		this.settledOn = settledOn;
	}

	/**
	 * @return the settledBy
	 */
	public String getSettledBy()
	{
		return settledBy;
	}

	/**
	 * @param settledBy
	 *            the settledBy to set
	 */
	public void setSettledBy(String settledBy)
	{
		this.settledBy = settledBy;
	}

	/**
	 * @return the accountClosedUnsettled
	 */
	public Boolean getAccountClosedUnsettled()
	{
		return accountClosedUnsettled;
	}

	/**
	 * @param accountClosedUnsettled
	 *            the accountClosedUnsettled to set
	 */
	public void setAccountClosedUnsettled(Boolean accountClosedUnsettled)
	{
		this.accountClosedUnsettled = accountClosedUnsettled;
	}

	/**
	 * @return the accountClosedSettled
	 */
	public Boolean getAccountClosedSettled()
	{
		return accountClosedSettled;
	}

	/**
	 * @param accountClosedSettled
	 *            the accountClosedSettled to set
	 */
	public void setAccountClosedSettled(Boolean accountClosedSettled)
	{
		this.accountClosedSettled = accountClosedSettled;
	}

	/**
	 * @return the closingComments
	 */
	public String getClosingComments()
	{
		return closingComments;
	}

	/**
	 * @param closingComments
	 *            the closingComments to set
	 */
	public void setClosingComments(String closingComments)
	{
		this.closingComments = closingComments;
	}

	/**
	 * @return the settlementComments
	 */
	public String getSettlementComments()
	{
		return settlementComments;
	}

	/**
	 * @param settlementComments
	 *            the settlementComments to set
	 */
	public void setSettlementComments(String settlementComments)
	{
		this.settlementComments = settlementComments;
	}

	public Long getCurrencyId()
	{
		return currencyId;
	}

	public void setCurrencyId(Long currencyId)
	{
		this.currencyId = currencyId;
	}

	public String getOccupation()
	{
		return occupation;
	}

	public void setOccupation(String occupation)
	{
		this.occupation = occupation;
	}

	public String getTypeOfAccount()
	{
		return typeOfAccount;
	}

	public void setTypeOfAccount(String typeOfAccount)
	{
		this.typeOfAccount = typeOfAccount;
	}

	public Long getRegistrationStateId()
	{
		return registrationStateId;
	}

	public void setRegistrationStateId(Long registrationStateId)
	{
		this.registrationStateId = registrationStateId;
	}

	public String getModeOfTx()
	{
		return modeOfTx;
	}

	public void setModeOfTx(String modeOfTx)
	{
		this.modeOfTx = modeOfTx;
	}

	public Long getBusinessTypeId()
	{
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId)
	{
		this.businessTypeId = businessTypeId;
	}

	public Long getAccountReason()
	{
		return accountReason;
	}

	public void setAccountReason(Long accountReason)
	{
		this.accountReason = accountReason;
	}

	public String getSalary()
	{
		return salary;
	}

	public void setSalary(String salary)
	{
		this.salary = salary;
	}

	public String getBuisnessIncome()
	{
		return buisnessIncome;
	}

	public void setBuisnessIncome(String buisnessIncome)
	{
		this.buisnessIncome = buisnessIncome;
	}

	public String getOtherIncome()
	{
		return otherIncome;
	}

	public void setOtherIncome(String otherIncome)
	{
		this.otherIncome = otherIncome;
	}

	public Long getDepositChannel()
	{
		return depositChannel;
	}

	public void setDepositChannel(Long depositChannel)
	{
		this.depositChannel = depositChannel;
	}

	public NokDetailVOModel getNokDetailVOModel()
	{
		return nokDetailVOModel;
	}

	public void setNokDetailVOModel(NokDetailVOModel nokDetailVOModel)
	{
		this.nokDetailVOModel = nokDetailVOModel;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getRegStateComments()
	{
		return regStateComments;
	}

	public void setRegStateComments(String regStateComments)
	{
		this.regStateComments = regStateComments;
	}

	public ApplicantDetailModel getApplicant1DetailModel()
	{
		return applicant1DetailModel;
	}

	public void setApplicant1DetailModel(ApplicantDetailModel applicant1DetailModel)
	{
		this.applicant1DetailModel = applicant1DetailModel;
	}

	public Long getTransactionModeId()
	{
		return transactionModeId;
	}

	public void setTransactionModeId(Long transactionModeId)
	{
		this.transactionModeId = transactionModeId;
	}

	public String getOtherTransactionMode()
	{
		return otherTransactionMode;
	}

	public void setOtherTransactionMode(String otherTransactionMode)
	{
		this.otherTransactionMode = otherTransactionMode;
	}

	public Long getAccountPurposeId()
	{
		return accountPurposeId;
	}

	public void setAccountPurposeId(Long accountPurposeId)
	{
		this.accountPurposeId = accountPurposeId;
	}

	public String getMfsId()
	{
		return mfsId;
	}

	public void setMfsId(String mfsId)
	{
		this.mfsId = mfsId;
	}

	public boolean isScreeningPerformed()
	{
		return screeningPerformed;
	}

	public void setScreeningPerformed(boolean screeningPerformed)
	{
		this.screeningPerformed = screeningPerformed;
	}

	public List<ApplicantDetailModel> getApplicantDetailModelList()
	{
		return applicantDetailModelList;
	}

	public void setApplicantDetailModelList(List<ApplicantDetailModel> applicantDetailModelList)
	{
		this.applicantDetailModelList = applicantDetailModelList;
	}

	public ApplicantDetailModel getApplicantDetailModelEditMode()
	{
		return applicantDetailModelEditMode;
	}

	public void setApplicantDetailModelEditMode(ApplicantDetailModel applicantDetailModelEditMode)
	{
		this.applicantDetailModelEditMode = applicantDetailModelEditMode;
	}

	public String getInitialAppFormNo()
	{
		return initialAppFormNo;
	}

	public void setInitialAppFormNo(String initialAppFormNo)
	{
		if (initialAppFormNo != null)
		{
			this.initialAppFormNo = initialAppFormNo;
		}
	}

	public Long getAcNature()
	{
		return acNature;
	}

	public void setAcNature(Long acNature)
	{
		if (acNature != null)
		{
			this.acNature = acNature;
		}
	}

	public Date getCommencementDate()
	{
		return commencementDate;
	}

	public void setCommencementDate(Date commencementDate)
	{
		if (commencementDate != null)
		{
			this.commencementDate = commencementDate;
		}
	}

	public Date getSecpRegDate()
	{
		return secpRegDate;
	}

	public void setSecpRegDate(Date secpRegDate)
	{
		if (secpRegDate != null)
		{
			this.secpRegDate = secpRegDate;
		}
	}

	public String getTradeBody()
	{
		return tradeBody;
	}

	public void setTradeBody(String tradeBody)
	{
		if (tradeBody != null)
		{
			this.tradeBody = tradeBody;
		}
	}

	public Long getBusinessNatureId()
	{
		return businessNatureId;
	}

	public void setBusinessNatureId(Long businessNatureId)
	{
		if (businessNatureId != null)
		{
			this.businessNatureId = businessNatureId;
		}
	}

	public Long getLocationTypeId()
	{
		return locationTypeId;
	}

	public void setLocationTypeId(Long locationTypeId)
	{
		if (locationTypeId != null)
		{
			this.locationTypeId = locationTypeId;
		}
	}

	public Long getLocationSizeId()
	{
		return locationSizeId;
	}

	public void setLocationSizeId(Long locationSizeId)
	{
		if (locationSizeId != null)
		{
			this.locationSizeId = locationSizeId;
		}
	}

	public Long getEstSince()
	{
		return estSince;
	}

	public void setEstSince(Long estSince)
	{
		if (estSince != null)
		{
			this.estSince = estSince;
		}
	}

	public Long getNokIdType()
	{
		return nokIdType;
	}

	public void setNokIdType(Long nokIdType)
	{
		if (nokIdType != null)
		{
			this.nokIdType = nokIdType;
		}
	}

	public String getNokIdNumber()
	{
		return nokIdNumber;
	}

	public void setNokIdNumber(String nokIdNumber)
	{
		if (nokIdNumber != null)
		{
			this.nokIdNumber = nokIdNumber;
		}
	}

	public String getAcTitle()
	{
		return acTitle;
	}

	public void setAcTitle(String acTitle)
	{
		if (acTitle != null)
		{
			this.acTitle = acTitle;
		}
	}

	public String getBusinessName()
	{
		return businessName;
	}

	public void setBusinessName(String businessName)
	{
		if (businessName != null)
		{
			this.businessName = businessName;
		}
	}

	public String getBusinessAddress()
	{
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress)
	{
		if (businessAddress != null)
		{
			this.businessAddress = businessAddress;
		}
	}

	public String getCorresspondanceAddress()
	{
		return corresspondanceAddress;
	}

	public void setCorresspondanceAddress(String corresspondanceAddress)
	{
		if (corresspondanceAddress != null)
		{
			this.corresspondanceAddress = corresspondanceAddress;
		}
	}

	public Long getBusinessPostalCode()
	{
		return businessPostalCode;
	}

	public void setBusinessPostalCode(Long businessPostalCode)
	{
		if (businessPostalCode != null)
		{
			this.businessPostalCode = businessPostalCode;
		}
	}

	public Long getCorresspondancePostalCode()
	{
		return corresspondancePostalCode;
	}

	public void setCorresspondancePostalCode(Long corresspondancePostalCode)
	{
		if (corresspondancePostalCode != null)
		{
			this.corresspondancePostalCode = corresspondancePostalCode;
		}
	}

	public Date getIncorporationDate()
	{
		return incorporationDate;
	}

	public void setIncorporationDate(Date incorporationDate)
	{
		if (incorporationDate != null)
		{
			this.incorporationDate = incorporationDate;
		}
	}

	public String getSecpRegNo()
	{
		return secpRegNo;
	}

	public void setSecpRegNo(String secpRegNo)
	{
		if (secpRegNo != null)
		{
			this.secpRegNo = secpRegNo;
		}
	}

	public String getNtn()
	{
		return ntn;
	}

	public void setNtn(String ntn)
	{
		if (ntn != null)
		{
			this.ntn = ntn;
		}
	}

	public String getSalesTaxRegNo()
	{
		return salesTaxRegNo;
	}

	public void setSalesTaxRegNo(String salesTaxRegNo)
	{
		if (salesTaxRegNo != null)
		{
			this.salesTaxRegNo = salesTaxRegNo;
		}
	}

	public String getMembershipNoTradeBody()
	{
		return membershipNoTradeBody;
	}

	public void setMembershipNoTradeBody(String membershipNoTradeBody)
	{
		if (membershipNoTradeBody != null)
		{
			this.membershipNoTradeBody = membershipNoTradeBody;
		}
	}

	public String getLandLineNo()
	{
		return landLineNo;
	}

	public void setLandLineNo(String landLineNo)
	{
		if (landLineNo != null)
		{
			this.landLineNo = landLineNo;
		}
	}

	public List<ACOwnershipDetailModel> getAcOwnershipDetailModelList()
	{
		return acOwnershipDetailModelList;
	}

	public void setAcOwnershipDetailModelList(List<ACOwnershipDetailModel> acOwnershipDetailModelList)
	{
		if (acOwnershipDetailModelList != null)
		{
			this.acOwnershipDetailModelList = acOwnershipDetailModelList;
		}
	}

	public Boolean getVerisysDone()
	{
		return verisysDone;
	}

	public void setVerisysDone(Boolean verisysDone)
	{
		if (verisysDone != null)
		{
			this.verisysDone = verisysDone;
		}
	}

	public Long getBusinessAddCity()
	{
		return businessAddCity;
	}

	public void setBusinessAddCity(Long businessAddCity)
	{
		if (businessAddCity != null)
		{
			this.businessAddCity = businessAddCity;
		}
	}

	public Long getCorresspondanceAddCity()
	{
		return corresspondanceAddCity;
	}

	public void setCorresspondanceAddCity(Long corresspondanceAddCity)
	{
		if (corresspondanceAddCity != null)
		{
			this.corresspondanceAddCity = corresspondanceAddCity;
		}
	}

	public Long getTaxRegimeId()
	{
		return taxRegimeId;
	}

	public void setTaxRegimeId(Long taxRegimeId)
	{
		if (taxRegimeId != null)
		{
			this.taxRegimeId = taxRegimeId;
		}
	}

	public Double getFed()
	{
		return fed;
	}

	public void setFed(Double fed)
	{
		if (fed != null)
		{
			this.fed = fed;
		}
	}

	public String getEmployeeName()
	{
		return employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public String getCoreAccountNumber()
	{
		return coreAccountNumber;
	}

	public void setCoreAccountNumber(String coreAccountNumber)
	{
		this.coreAccountNumber = coreAccountNumber;
	}

	public String getCoreAccountTitle()
	{
		return coreAccountTitle;
	}

	public void setCoreAccountTitle(String coreAccountTitle)
	{
		this.coreAccountTitle = coreAccountTitle;
	}

	public Date getAccountOpeningDate()
	{
		return accountOpeningDate;
	}

	public void setAccountOpeningDate(Date accountOpeningDate)
	{
		this.accountOpeningDate = accountOpeningDate;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getRegStateName() {
		return regStateName;
	}

	public void setRegStateName(String regStateName) {
		if (regStateName != null) {
			this.regStateName = regStateName;
		}
	}

	public String getAccounttypeName() {
		return accounttypeName;
	}

	public void setAccounttypeName(String accounttypeName) {
		if (accounttypeName != null) {
			this.accounttypeName = accounttypeName;
		}
	}

	public String getBirthPlaceName() {
		return birthPlaceName;
	}

	public void setBirthPlaceName(String birthPlaceName) {
		if (birthPlaceName != null) {
			this.birthPlaceName = birthPlaceName;
		}
	}

	public String getTaxRegimeName() {
		return taxRegimeName;
	}

	public void setTaxRegimeName(String taxRegimeName) {
		if (taxRegimeName != null) {
			this.taxRegimeName = taxRegimeName;
		}
	}

	public String getAccountPurpose() {
		return accountPurpose;
	}

	public void setAccountPurpose(String accountPurpose) {
		if (accountPurpose != null) {
			this.accountPurpose = accountPurpose;
		}
	}

	public String getBusinessAddCityName() {
		return businessAddCityName;
	}

	public void setBusinessAddCityName(String businessAddCityName) {
		if (businessAddCityName != null) {
			this.businessAddCityName = businessAddCityName;
		}
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(String businessTypeName) {
		if (businessTypeName != null) {
			this.businessTypeName = businessTypeName;
		}
	}

	public String getBusinessNatureName() {
		return businessNatureName;
	}

	public void setBusinessNatureName(String businessNatureName) {
		if (businessNatureName != null) {
			this.businessNatureName = businessNatureName;
		}
	}

	public String getLocationTypeName() {
		return locationTypeName;
	}

	public void setLocationTypeName(String locationTypeName) {
		if (locationTypeName != null) {
			this.locationTypeName = locationTypeName;
		}
	}

	public String getLocationSizeName() {
		return locationSizeName;
	}

	public void setLocationSizeName(String locationSizeName) {
		if (locationSizeName != null) {
			this.locationSizeName = locationSizeName;
		}
	}

	public String getCorresspondanceAddCityName() {
		return corresspondanceAddCityName;
	}

	public void setCorresspondanceAddCityName(String corresspondanceAddCityName) {
		if (corresspondanceAddCityName != null) {
			this.corresspondanceAddCityName = corresspondanceAddCityName;
		}
	}

	public String getAcNatureName() {
		return acNatureName;
	}

	public void setAcNatureName(String acNatureName) {
		if (acNatureName != null) {
			this.acNatureName = acNatureName;
		}
	}

	public boolean isAccountLinked()
	{
		return accountLinked;
	}

	public void setAccountLinked(boolean accountLinked)
	{
		this.accountLinked = accountLinked;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		if (distributorName != null) {
			this.distributorName = distributorName;
		}
	}
	
	public Boolean getFiler() {
		return filer;
	}

	public void setFiler(Boolean filer) {
		this.filer = filer;
	}
	
	public Boolean getBvsEnable() {
		return bvsEnable;
	}

	public void setBvsEnable(Boolean bvsEnable) {
		this.bvsEnable = bvsEnable;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Long getAccountStateId() {
		return accountStateId;
	}

	public void setAccountStateId(Long accountStateId) {
		this.accountStateId = accountStateId;
	}

	public Long getServiceOperatorId() {
		return serviceOperatorId;
	}

	public void setServiceOperatorId(Long serviceOperatorId) {
		this.serviceOperatorId = serviceOperatorId;
	}
}
