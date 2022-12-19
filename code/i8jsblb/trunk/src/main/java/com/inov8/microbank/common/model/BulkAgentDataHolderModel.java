package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BULK_AGENT_CREATION_SEQ", sequenceName = "BULK_AGENT_CREATION_SEQ", allocationSize = 1)
@Table(name = "BULK_AGENT_MERCHANT_CREATION")
public class BulkAgentDataHolderModel extends BasePersistableModel {

    private static final long serialVersionUID	= -8290513824546633535L;

    private Long bulkAgentId;
    private String referenceNo;
    private String initialAppFormNo;
    private String businessName;
    private Long regionId;
    private Long areaLevelId;
    private Long areaId;
    private Long acLevelQualificationId;
    private AppUserModel employeeAppUserModel;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Integer	versionNo;
    private String	userName;
    private String 	isHead;

    private Long distributorId;
    private Long distributorLevelId;
    private Long productCatalogId;
    private String password;
    private Long parentAgentId;
    private String parentAgentName;
    private Long ultimateParentAgentId;
    private Long isProcessedByScheduler;

    private Long purposeOfAccountId;
    private Long accountTypeId;
    private Long currencyId;
    private String accountTitle;
    private Long taxRegimeId;
    private Double fed;
    private String businessAddress;
    private Long businessCityId;
    private Date businessCommencementDate;
    private Long businessTypeId;
    private Long businessNatureId;
    private Long locationTypeId;
    private Long locationSizeId;
    private String establishedDate;
    private String corresspondenceAdrees;
    private Long corresspondenceCityId;
    private String applicantName;
    private Long docTypeId;
    private String docValue;
    private Date docExpiryDate;
    private Date applicantDOB;
    private String applicantMobileNo;
    private Long isScreened;
    private Long isNadraVrified;
    private String ultimateParentAgentName;
    private Long retailerId;

    private Boolean isSubAgent;

    public BulkAgentDataHolderModel() {

    }

    @Column(name = "BULK_AGENT_CREATION_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BULK_AGENT_CREATION_SEQ")
    public Long getBulkAgentId() {
        return this.bulkAgentId;
    }

    public void setBulkAgentId(Long bulkAgentId) {
        this.bulkAgentId = bulkAgentId;
    }

    /**
     * Return the primary key.
     * @return Long with the primary key.
     */
    @Override
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return this.getBulkAgentId();
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&bulkAgentId="
                + this.getBulkAgentId();
        return parameters;
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "bulkAgentId";
        return primaryKeyFieldName;
    }

    /**
     * Set the primary key.
     * @param primaryKey the primary key
     */
    @Override
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        this.setBulkAgentId(primaryKey);
    }

    @Column(name = "INITIAL_APP_FORM_NUMBER")
    public String getInitialAppFormNo() {
        return this.initialAppFormNo;
    }

    public void setInitialAppFormNo(String initialAppFormNo) {
        if (initialAppFormNo != null) {
            this.initialAppFormNo = initialAppFormNo;
        }
    }

    @Column(name = "REFERENCE_NO")
    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    @Column(name = "BUSINESS_NAME")
    public String getBusinessName() {
        return this.businessName;
    }

    public void setBusinessName(String businessName) {
        if (businessName != null) {
            this.businessName = businessName;
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_ID")
    public AppUserModel getEmployeeAppUserModel() {
        return this.employeeAppUserModel;
    }

    @javax.persistence.Transient
    public void setEmployeeAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.employeeAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    @javax.persistence.Transient
    public Long getEmpId() {
        if (this.employeeAppUserModel != null) {
            return this.employeeAppUserModel.getEmployeeId();
        } else {
            return null;
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getRelationUpdatedByAppUserModel() {
        return this.updatedByAppUserModel;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation
     * property.
     * @return the value of the <code>updatedByAppUserModel</code> relation
     *         property.
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel() {
        return this.getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation
     * property.
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation
     * property.
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.setRelationUpdatedByAppUserModel((AppUserModel) appUserModel
                    .clone());
        }
    }

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation
     * property.
     * @return the value of the <code>createdByAppUserModel</code> relation
     *         property.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getRelationCreatedByAppUserModel() {
        return this.createdByAppUserModel;
    }

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation
     * property.
     * @return the value of the <code>createdByAppUserModel</code> relation
     *         property.
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel() {
        return this.getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation
     * property.
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation
     * property.
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.setRelationCreatedByAppUserModel((AppUserModel) appUserModel
                    .clone());
        }
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
        if (this.updatedByAppUserModel != null) {
            return this.updatedByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
        if (appUserId == null) {
            this.updatedByAppUserModel = null;
        } else {
            this.updatedByAppUserModel = new AppUserModel();
            this.updatedByAppUserModel.setAppUserId(appUserId);
        }
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
        if (this.createdByAppUserModel != null) {
            return this.createdByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
        if (appUserId == null) {
            this.createdByAppUserModel = null;
        } else {
            this.createdByAppUserModel = new AppUserModel();
            this.createdByAppUserModel.setAppUserId(appUserId);
        }
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Version
    @Column(name = "VERSION_NO")
    public Integer getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @Column(name = "AC_LEVEL_QUALIFICATION_ID")
    public Long getAcLevelQualificationId() {
        return this.acLevelQualificationId;
    }

    public void setAcLevelQualificationId(Long acLevelQualificationId) {
        if (acLevelQualificationId != null) {
            this.acLevelQualificationId = acLevelQualificationId;
        }
    }

    @Column(name = "REGION_ID")
    public Long getRegionId() {
        return this.regionId;
    }

    public void setRegionId(Long regionId) {
        if (regionId != null) {
            this.regionId = regionId;
        }
    }

    @Column(name = "AREA_LEVEL_ID")
    public Long getAreaLevelId() {
        return areaLevelId;
    }

    public void setAreaLevelId(Long areaLevelId) {
        if (areaLevelId != null) {
            this.areaLevelId = areaLevelId;
        }
    }

    @Column(name = "AREA_ID")
    public Long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(Long areaId) {
        if (areaId != null) {
            this.areaId = areaId;
        }
    }

    @Column(name = "USERNAME")
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        if (userName != null) {
            this.userName = userName;
        }
    }

    @Transient
    public String getIsHead() {
        return isHead;
    }

    public void setIsHead(String isHead) {
        if (isHead != null) {
            this.isHead = isHead;
        }
    }

    @Column(name = "DISTRIBUTOR_ID")
    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    @Column(name = "PRODUCT_CATALOG_ID")
    public Long getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(Long productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "PARENT_AGENT_ID")
    public Long getParentAgentId() {
        return parentAgentId;
    }

    public void setParentAgentId(Long parentAgentId) {
        this.parentAgentId = parentAgentId;
    }

    @Column(name = "ULTIMATE_PARENT_AGENT_ID")
    public Long getUltimateParentAgentId() {
        return ultimateParentAgentId;
    }

    public void setUltimateParentAgentId(Long ultimateParentAgentId) {
        this.ultimateParentAgentId = ultimateParentAgentId;
    }

    @Column(name = "DISTRIBUTOR_LEVEL_ID")
    public Long getDistributorLevelId() {
        return distributorLevelId;
    }

    public void setDistributorLevelId(Long distributorLevelId) {
        this.distributorLevelId = distributorLevelId;
    }

    @Column(name = "IS_PROCESSED_BY_SCHEDULER")
    public Long getIsProcessedByScheduler() {
        return isProcessedByScheduler;
    }

    public void setIsProcessedByScheduler(Long isProcessedByScheduler) {
        this.isProcessedByScheduler = isProcessedByScheduler;
    }

    @Column(name = "PURPOSE_OF_ACCOUNT_ID")
    public Long getPurposeOfAccountId() {
        return purposeOfAccountId;
    }

    public void setPurposeOfAccountId(Long purposeOfAccountId) {
        this.purposeOfAccountId = purposeOfAccountId;
    }

    @Column(name = "TYPE_OF_ACCOUNT_ID")
    public Long getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Long accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    @Column(name = "CURRENCY_ID")
    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    @Column(name = "ACCOUNT_TITLE")
    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    @Column(name = "TAX_REGIME_ID")
    public Long getTaxRegimeId() {
        return taxRegimeId;
    }

    public void setTaxRegimeId(Long taxRegimeId) {
        this.taxRegimeId = taxRegimeId;
    }

    @Column(name = "FED")
    public Double getFed() {
        return fed;
    }

    public void setFed(Double fed) {
        this.fed = fed;
    }

    @Column(name = "BUSINESS_ADDRESS")
    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    @Column(name = "BUSINESS_CITY_ID")
    public Long getBusinessCityId() {
        return businessCityId;
    }

    public void setBusinessCityId(Long businessCityId) {
        this.businessCityId = businessCityId;
    }

    @Column(name = "BUSINESS_COMM_DATE")
    public Date getBusinessCommencementDate() {
        return businessCommencementDate;
    }

    public void setBusinessCommencementDate(Date businessCommencementDate) {
        this.businessCommencementDate = businessCommencementDate;
    }

    @Column(name = "BUSINESS_TYPE_ID")
    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    @Column(name = "BUSINESS_NATURE_ID")
    public Long getBusinessNatureId() {
        return businessNatureId;
    }

    public void setBusinessNatureId(Long businessNatureId) {
        this.businessNatureId = businessNatureId;
    }

    @Column(name = "LOCATION_TYPE_ID")
    public Long getLocationTypeId() {
        return locationTypeId;
    }

    public void setLocationTypeId(Long locationTypeId) {
        this.locationTypeId = locationTypeId;
    }

    @Column(name = "LOCATION_SIZE_ID")
    public Long getLocationSizeId() {
        return locationSizeId;
    }

    public void setLocationSizeId(Long locationSizeId) {
        this.locationSizeId = locationSizeId;
    }

    @Column(name = "ESTABLISHED_SINCE_DATE")
    public String getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(String establishedDate) {
        this.establishedDate = establishedDate;
    }

    @Column(name = "CORRES_ADDRESS")
    public String getCorresspondenceAdrees() {
        return corresspondenceAdrees;
    }

    public void setCorresspondenceAdrees(String corresspondenceAdrees) {
        this.corresspondenceAdrees = corresspondenceAdrees;
    }

    @Column(name = "CORRES_CITY_ID")
    public Long getCorresspondenceCityId() {
        return corresspondenceCityId;
    }

    public void setCorresspondenceCityId(Long corresspondenceCityId) {
        this.corresspondenceCityId = corresspondenceCityId;
    }

    @Column(name = "APPLICANT_NAME")
    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    @Column(name = "ID_DOC_TYPE_ID")
    public Long getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Long docTypeId) {
        this.docTypeId = docTypeId;
    }

    @Column(name = "ID_DOC_VALUE")
    public String getDocValue() {
        return docValue;
    }

    public void setDocValue(String docValue) {
        this.docValue = docValue;
    }

    @Column(name = "ID_EXPIRY_DATE")
    public Date getDocExpiryDate() {
        return docExpiryDate;
    }

    public void setDocExpiryDate(Date docExpiryDate) {
        this.docExpiryDate = docExpiryDate;
    }

    @Column(name = "APPLICANT_DOB")
    public Date getApplicantDOB() {
        return applicantDOB;
    }

    public void setApplicantDOB(Date applicantDOB) {
        this.applicantDOB = applicantDOB;
    }

    @Column(name = "MOBILE_NO")
    public String getApplicantMobileNo() {
        return applicantMobileNo;
    }

    public void setApplicantMobileNo(String applicantMobileNo) {
        this.applicantMobileNo = applicantMobileNo;
    }

    @Column(name = "IS_SCREENEED")
    public Long getIsScreened() {
        return isScreened;
    }

    public void setIsScreened(Long isScreened) {
        this.isScreened = isScreened;
    }

    @Column(name = "IS_NADRA_VERIFIED")
    public Long getIsNadraVerified() {
        return isNadraVrified;
    }

    public void setIsNadraVerified(Long isNadraVrified) {
        this.isNadraVrified = isNadraVrified;
    }

    @Column(name = "PARENT_AGENT_NAME")
    public String getParentAgentName() {
        return parentAgentName;
    }

    public void setParentAgentName(String parentAgentName) {
        this.parentAgentName = parentAgentName;
    }

    @Column(name = "ULTIMATE_PARENT_AGENT_NAME")
    public String getUltimateParentAgentName() {
        return ultimateParentAgentName;
    }

    public void setUltimateParentAgentName(String ultimateParentAgentName) {
        this.ultimateParentAgentName = ultimateParentAgentName;
    }

    @Column(name = "RETAILER_ID")
    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    @Transient
    public Boolean getSubAgent() {
        return isSubAgent;
    }

    public void setSubAgent(Boolean subAgent) {
        isSubAgent = subAgent;
    }
}
