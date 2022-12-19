package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.ACOwnershipDetailModel;

public class Level2AccountModel implements Serializable,Cloneable{

	private static final long serialVersionUID = 4099489810777964069L;
	
	private String initialAppFormNo;
	private String mobileNo;
    private Long appUserId;
    private Long usecaseId;
    private Long actionId;
    
    private String searchFirstName;
    private String searchLastName;
    private String searchMfsId;
    private String searchNic;
    
    /**
     * Edited By Usman Ashraf
     */
    private String applicationNo;
    private Date applicationDate;
    private Long segmentId;
    private Long customerTypeId;
    private Long fundsSourceId;
    private String fundSourceOthers;
    private String fundSourceNarration;
    private Long customerAccountTypeId;
    private String refferedBy;
    private String segmentName;
    private String languageName;
    private String typeOfCustomerName;
    private String fundSourceName;
    private String customerAccountName;
    private String employeeID;
	private String accountNo;
    private String accountStatus;
    private Double accountBalance;
    private String customerId;
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
    private String accountNature;
    private String currency;
    private String occupation;
    private String typeOfAccount;
    private Long registrationStateId;
    private Boolean publicFigure;
    private String modeOfTx;
    private String accountPurpose;
    private Long businessTypeId;
    private Long accountReason;
    private String salary;
    private String buisnessIncome;
    private String otherIncome;
    private String otherBankName;
    private String otherBankAddress;
    private String otherBankACNo;
    private BusinessDetailModel businessDetailModel;
    private AdditionalDetailVOModel additionalDetailVOModel;
    private ApplicantDetailModel applicant1DetailModel;
    private ApplicantDetailModel applicant2DetailModel;
    private ApplicantDetailModel applicant3DetailModel;
    private List<ApplicantDetailModel>	applicantDetailModelList = new ArrayList<ApplicantDetailModel>(0);
    private List<ACOwnershipDetailModel>	acOwnershipDetailModelList = new ArrayList<ACOwnershipDetailModel>(0);
    private ApplicantDetailModel applicantDetailModelEditMode;
    private NokDetailVOModel nokDetailVOModel;
    private Long transactionModeId;
    private String otherTransactionMode;
    private Long accountPurposeId;
    private String initialDeposit;
    private String comments;
    private String regStateComments;
    private String mfsId;
    /*added by atif hussain*/
    private boolean cnicSeen;
    private Boolean screeningPerformed;
    private Long acNature;
    private String acNatureName;
    private Long nokIdType;
    private String nokIdNumber;
    private Long empId;
    private String employeeName;
    private Date kycOpeningDate;
    private Long	taxRegimeId;
    private String	taxRegimeName;
    
    private Long productCatalogId;
    private String productCatalogName;
    
    private	Double	fed;
    private Boolean isVeriSysDone;
    private String regStateName;
    private String accounttypeName;
    private String birthPlaceName;
    private Boolean isBulkRequest;
    private String	accountState;
    private String	remainingDailyCreditLimit;
    private String	remainingDailyDebitLimit;
    private String	remainingMonthlyCreditLimit;
    private String	remainingMonthlyDebitLimit;
    private String	remainingYearlyCreditLimit;
    private String	remainingYearlyDebitLimit;
    
    public Level2AccountModel() {
	}

    public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getRefferedBy() {
		return refferedBy;
	}
	public void setRefferedBy(String refferedBy) {
		this.refferedBy = refferedBy;
	}
	public static final String LEVEL2_ACCOUNT_MODEL_KEY = "level2AccountModelKey";
    
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Long getAppUserId() {
		return appUserId;
	}
	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
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
	public String getSearchFirstName()
	{
		return searchFirstName;
	}
	
	public String getSearchMfsId() {
		return searchMfsId;
	}
	public void setSearchMfsId(String searchMfsId) {
		this.searchMfsId = searchMfsId;
	}
	public String getSearchLastName() {
		return searchLastName;
	}
	public void setSearchLastName(String searchLastName) {
		this.searchLastName = searchLastName;
	}
	public String getSearchNic() {
		return searchNic;
	}
	public void setSearchNic(String searchNic) {
		this.searchNic = searchNic;
	}
	public void setSearchFirstName(String searchFirstName) {
		this.searchFirstName = searchFirstName;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public Date getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}
	public Long getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}
	public Long getCustomerTypeId() {
		return customerTypeId;
	}
	public void setCustomerTypeId(Long customerTypeId) {
		this.customerTypeId = customerTypeId;
	}
	public Long getFundsSourceId() {
		return fundsSourceId;
	}
	public void setFundsSourceId(Long fundsSourceId) {
		this.fundsSourceId = fundsSourceId;
	}
	public String getFundSourceOthers() {
		return fundSourceOthers;
	}
	public void setFundSourceOthers(String fundSourceOthers) {
		this.fundSourceOthers = fundSourceOthers;
	}
	public String getFundSourceNarration() {
		return fundSourceNarration;
	}
	public void setFundSourceNarration(String fundSourceNarration) {
		this.fundSourceNarration = fundSourceNarration;
	}
	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}
	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}
	public String getSegmentName() {
		return segmentName;
	}
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	public String getTypeOfCustomerName() {
		return typeOfCustomerName;
	}
	public void setTypeOfCustomerName(String typeOfCustomerName) {
		this.typeOfCustomerName = typeOfCustomerName;
	}
	public String getFundSourceName() {
		return fundSourceName;
	}
	public void setFundSourceName(String fundSourceName) {
		this.fundSourceName = fundSourceName;
	}
	public String getCustomerAccountName() {
		return customerAccountName;
	}
	public void setCustomerAccountName(String customerAccountName) {
		this.customerAccountName = customerAccountName;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public Double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getAllpayId() {
		return allpayId;
	}
	public void setAllpayId(String allpayId) {
		this.allpayId = allpayId;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getClosedOn() {
		return closedOn;
	}
	public void setClosedOn(Date closedOn) {
		this.closedOn = closedOn;
	}
	public String getClosedBy() {
		return closedBy;
	}
	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}
	public Date getSettledOn() {
		return settledOn;
	}
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
	public String getAccountNature() {
		return accountNature;
	}
	public void setAccountNature(String accountNature) {
		this.accountNature = accountNature;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getTypeOfAccount() {
		return typeOfAccount;
	}
	public void setTypeOfAccount(String typeOfAccount) {
		this.typeOfAccount = typeOfAccount;
	}
	public Long getRegistrationStateId() {
		return registrationStateId;
	}
	public void setRegistrationStateId(Long registrationStateId) {
		this.registrationStateId = registrationStateId;
	}
	public Boolean getPublicFigure() {
		return publicFigure;
	}
	public void setPublicFigure(Boolean publicFigure) {
		this.publicFigure = publicFigure;
	}
	public String getModeOfTx() {
		return modeOfTx;
	}
	public void setModeOfTx(String modeOfTx) {
		this.modeOfTx = modeOfTx;
	}
	public String getAccountPurpose() {
		return accountPurpose;
	}
	public void setAccountPurpose(String accountPurpose) {
		this.accountPurpose = accountPurpose;
	}
	public Long getBusinessTypeId() {
		return businessTypeId;
	}
	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}
	public Long getAccountReason() {
		return accountReason;
	}
	public void setAccountReason(Long accountReason) {
		this.accountReason = accountReason;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getBuisnessIncome() {
		return buisnessIncome;
	}
	public void setBuisnessIncome(String buisnessIncome) {
		this.buisnessIncome = buisnessIncome;
	}
	public String getOtherIncome() {
		return otherIncome;
	}
	public void setOtherIncome(String otherIncome) {
		this.otherIncome = otherIncome;
	}
	public String getOtherBankName() {
		return otherBankName;
	}
	public void setOtherBankName(String otherBankName) {
		this.otherBankName = otherBankName;
	}
	public String getOtherBankAddress() {
		return otherBankAddress;
	}
	public void setOtherBankAddress(String otherBankAddress) {
		this.otherBankAddress = otherBankAddress;
	}
	public String getOtherBankACNo() {
		return otherBankACNo;
	}
	public void setOtherBankACNo(String otherBankACNo) {
		this.otherBankACNo = otherBankACNo;
	}
	public BusinessDetailModel getBusinessDetailModel() {
		return businessDetailModel;
	}
	public void setBusinessDetailModel(BusinessDetailModel businessDetailModel) {
		this.businessDetailModel = businessDetailModel;
	}
	public AdditionalDetailVOModel getAdditionalDetailVOModel() {
		return additionalDetailVOModel;
	}
	public void setAdditionalDetailVOModel(AdditionalDetailVOModel additionalDetailVOModel) {
		this.additionalDetailVOModel = additionalDetailVOModel;
	}
	public NokDetailVOModel getNokDetailVOModel() {
		return nokDetailVOModel;
	}
	public void setNokDetailVOModel(NokDetailVOModel nokDetailVOModel) {
		this.nokDetailVOModel = nokDetailVOModel;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRegStateComments() {
		return regStateComments;
	}
	public void setRegStateComments(String regStateComments) {
		this.regStateComments = regStateComments;
	}
	public ApplicantDetailModel getApplicant1DetailModel() {
		return applicant1DetailModel;
	}
	public void setApplicant1DetailModel(ApplicantDetailModel applicant1DetailModel) {
		this.applicant1DetailModel = applicant1DetailModel;
	}
	public ApplicantDetailModel getApplicant2DetailModel() {
		return applicant2DetailModel;
	}
	public void setApplicant2DetailModel(ApplicantDetailModel applicant2DetailModel) {
		this.applicant2DetailModel = applicant2DetailModel;
	}
	public ApplicantDetailModel getApplicant3DetailModel() {
		return applicant3DetailModel;
	}
	public void setApplicant3DetailModel(ApplicantDetailModel applicant3DetailModel) {
		this.applicant3DetailModel = applicant3DetailModel;
	}
	public Long getTransactionModeId() {
		return transactionModeId;
	}
	public void setTransactionModeId(Long transactionModeId) {
		this.transactionModeId = transactionModeId;
	}
	public String getOtherTransactionMode() {
		return otherTransactionMode;
	}
	public void setOtherTransactionMode(String otherTransactionMode) {
		this.otherTransactionMode = otherTransactionMode;
	}
	public Long getAccountPurposeId() {
		return accountPurposeId;
	}
	public void setAccountPurposeId(Long accountPurposeId) {
		this.accountPurposeId = accountPurposeId;
	}

	public String getMfsId() {
		return mfsId;
	}

	public void setMfsId(String mfsId) {
		this.mfsId = mfsId;
	}

	public String getInitialDeposit() {
		return initialDeposit;
	}

	public void setInitialDeposit(String initialDeposit) {
		this.initialDeposit = initialDeposit;
	}

	public boolean isCnicSeen() {
		return cnicSeen;
	}
	
	public void setCnicSeen(boolean cnicSeen) {
		this.cnicSeen = cnicSeen;
	}

	public Boolean getScreeningPerformed() {
		return screeningPerformed;
	}

	public void setScreeningPerformed(Boolean screeningPerformed) {
		this.screeningPerformed = screeningPerformed;
	}

	public List<ApplicantDetailModel> getApplicantDetailModelList() {
		return applicantDetailModelList;
	}

	public void setApplicantDetailModelList(
			List<ApplicantDetailModel> applicantDetailModelList) {
		this.applicantDetailModelList = applicantDetailModelList;
	}

	public ApplicantDetailModel getApplicantDetailModelEditMode() {
		return applicantDetailModelEditMode;
	}

	public void setApplicantDetailModelEditMode(
			ApplicantDetailModel applicantDetailModelEditMode) {
		this.applicantDetailModelEditMode = applicantDetailModelEditMode;
	}

	public Long getAcNature() {
		return acNature;
	}

	public void setAcNature(Long acNature) {
		this.acNature = acNature;
	}

	public Long getNokIdType() {
		return nokIdType;
	}

	public void setNokIdType(Long nokIdType) {
		this.nokIdType = nokIdType;
	}

	public String getNokIdNumber() {
		return nokIdNumber;
	}

	public void setNokIdNumber(String nokIdNumber) {
		this.nokIdNumber = nokIdNumber;
	}

	public String getInitialAppFormNo() {
		return initialAppFormNo;
	}

	public void setInitialAppFormNo(String initialAppFormNo) {
		this.initialAppFormNo = initialAppFormNo;
	}

	public List<ACOwnershipDetailModel> getAcOwnershipDetailModelList() {
		return acOwnershipDetailModelList;
	}

	public void setAcOwnershipDetailModelList(
			List<ACOwnershipDetailModel> acOwnershipDetailModelList) {
		this.acOwnershipDetailModelList = acOwnershipDetailModelList;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getKycOpeningDate() {
		return kycOpeningDate;
	}

	public void setKycOpeningDate(Date kycOpeningDate) {
		this.kycOpeningDate = kycOpeningDate;
	}

	public Double getFed() {
		return fed;
	}

	public void setFed(Double fed) {
		this.fed = fed;
	}

	public Long getTaxRegimeId() {
		return taxRegimeId;
	}

	public void setTaxRegimeId(Long taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
	}

	public Boolean getIsVeriSysDone() {
		return isVeriSysDone;
	}

	public void setIsVeriSysDone(Boolean isVeriSysDone) {
		this.isVeriSysDone = isVeriSysDone;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getTaxRegimeName() {
		return taxRegimeName;
	}

	public void setTaxRegimeName(String taxRegimeName) {
		if (taxRegimeName != null) {
			this.taxRegimeName = taxRegimeName;
		}
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

	public String getAcNatureName() {
		return acNatureName;
	}

	public void setAcNatureName(String acNatureName) {
		if (acNatureName != null) {
			this.acNatureName = acNatureName;
		}
	}

	public Boolean getIsBulkRequest() {
		return isBulkRequest;
	}

	public void setIsBulkRequest(Boolean isBulkRequest) {
		this.isBulkRequest = isBulkRequest;
	}

	public String getAccountState() {
		return accountState;
	}

	public void setAccountState(String accountState) {
		if (accountState != null) {
			this.accountState = accountState;
		}
	}

	public String getRemainingDailyCreditLimit() {
		return remainingDailyCreditLimit;
	}

	public void setRemainingDailyCreditLimit(String remainingDailyCreditLimit) {
		if (remainingDailyCreditLimit != null) {
			this.remainingDailyCreditLimit = remainingDailyCreditLimit;
		}
	}

	public String getRemainingDailyDebitLimit() {
		return remainingDailyDebitLimit;
	}

	public void setRemainingDailyDebitLimit(String remainingDailyDebitLimit) {
		if (remainingDailyDebitLimit != null) {
			this.remainingDailyDebitLimit = remainingDailyDebitLimit;
		}
	}

	public String getRemainingMonthlyCreditLimit() {
		return remainingMonthlyCreditLimit;
	}

	public void setRemainingMonthlyCreditLimit(String remainingMonthlyCreditLimit) {
		if (remainingMonthlyCreditLimit != null) {
			this.remainingMonthlyCreditLimit = remainingMonthlyCreditLimit;
		}
	}

	public String getRemainingMonthlyDebitLimit() {
		return remainingMonthlyDebitLimit;
	}

	public void setRemainingMonthlyDebitLimit(String remainingMonthlyDebitLimit) {
		if (remainingMonthlyDebitLimit != null) {
			this.remainingMonthlyDebitLimit = remainingMonthlyDebitLimit;
		}
	}

	public String getRemainingYearlyCreditLimit() {
		return remainingYearlyCreditLimit;
	}

	public void setRemainingYearlyCreditLimit(String remainingYearlyCreditLimit) {
		if (remainingYearlyCreditLimit != null) {
			this.remainingYearlyCreditLimit = remainingYearlyCreditLimit;
		}
	}

	public String getRemainingYearlyDebitLimit() {
		return remainingYearlyDebitLimit;
	}

	public void setRemainingYearlyDebitLimit(String remainingYearlyDebitLimit) {
		if (remainingYearlyDebitLimit != null) {
			this.remainingYearlyDebitLimit = remainingYearlyDebitLimit;
		}
	}

	public Long getProductCatalogId() {
		return productCatalogId;
	}

	public void setProductCatalogId(Long productCatalogId) {
		this.productCatalogId = productCatalogId;
	}

	public String getProductCatalogName() {
		return productCatalogName;
	}

	public void setProductCatalogName(String productCatalogName) {
		this.productCatalogName = productCatalogName;
	}
	
	
	
	
	
}
