package com.inov8.microbank.common.model.agenthierarchy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * BulkAgentUploadReport entity. @author Rashid Mahmood
 */
  
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "bulk_agent_upload_report_seq",sequenceName = "bulk_agent_upload_report_seq", allocationSize=1)
@Table(name = "BULK_AGENT_UPLOAD_REPORT")
public class BulkAgentUploadReportModel extends BasePersistableModel implements Serializable {

	// Fields
	private static final long serialVersionUID = -3571110109965363149L;
	private Long bulkAgentReportId;
	private String agentNetwork;
	private Long agentNetworkId;
	private String region;
	private Long regionId;
	private String franchiseBranch;
	private Long franchiseBranchId;
	private String partnerGroup;
	private String productCatalog;
	private String agentLevel;
	private String parentAgent;
	private String areaLevel;
	private String areaName;
	private String description;
	private String comments;
	private String agentName;
	private Boolean active;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private Long zongMsisdn;
	private Date accountOpenDate;
	private Long agentNtnNo;
	private Long cnicNo;
	private Date cnicExpiryDate;
	private Long contactNo;
	private Long landlineNo;
	private Long mobileNo;
	private String email;
	private String businessName;
	private String natureOfBusiness;
	private String shopNo;
	private String marketPlaza;
	private String districtTehsilTown;
	private String cityVillage;
	private String postOffice;
	private Long businessNtnNo;
	private String nearestLandmark;
	private String accountNo;
	private String accountTitle;
	private String recordStatus;
	private Date createdOn;
   	private Date updatedOn;
   	private Integer versionNo;
   	private Long createdBy;
   	private String createdByName;
   	private Long updatedBy;

   	private Date startDate;
    private Date endDate;

	// Constructors

	/** default constructor */
	public BulkAgentUploadReportModel() {
	}

	/** minimal constructor */
	public BulkAgentUploadReportModel(Long bulkAgentReportId, String recordStatus) 
	{
		this.bulkAgentReportId = bulkAgentReportId;
		this.recordStatus = recordStatus;
	}

	// Property accessors
	@Column(name = "BULK_AGENT_REPORT_ID" , nullable = false )
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bulk_agent_upload_report_seq")
	public Long getBulkAgentReportId() {
		return this.bulkAgentReportId;
	}

	public void setBulkAgentReportId(Long bulkAgentReportId) {
		this.bulkAgentReportId = bulkAgentReportId;
	}

	@javax.persistence.Transient
	public Long getPrimaryKey() 
	{
		return getBulkAgentReportId();
	}


	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) 
	{
		setBulkAgentReportId(primaryKey);
	}
	
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName()
	{ 
		String primaryKeyFieldName = "bulkAgentReportId";
		return primaryKeyFieldName;				
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() 
	{
		String parameters = "";
		parameters += "&bulkAgentReportId=" + getBulkAgentReportId();
		return parameters;
	}
	
	
	
	
	@Column(name = "AGENT_NETWORK", length = 50)
	public String getAgentNetwork() {
		return this.agentNetwork;
	}

	public void setAgentNetwork(String agentNetwork) {
		this.agentNetwork = agentNetwork;
	}

	@Column(name = "REGION", length = 250)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "FRANCHISE_BRANCH", length = 250)
	public String getFranchiseBranch() {
		return this.franchiseBranch;
	}

	public void setFranchiseBranch(String franchiseBranch) {
		this.franchiseBranch = franchiseBranch;
	}

	@Column(name = "PARTNER_GROUP", length = 50)
	public String getPartnerGroup() {
		return this.partnerGroup;
	}

	public void setPartnerGroup(String partnerGroup) {
		this.partnerGroup = partnerGroup;
	}

	@Column(name = "PRODUCT_CATALOG", length = 50)
	public String getProductCatalog() {
		return this.productCatalog;
	}

	public void setProductCatalog(String productCatalog) {
		this.productCatalog = productCatalog;
	}

	@Column(name = "AGENT_LEVEL", length = 50)
	public String getAgentLevel() {
		return this.agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	@Column(name = "PARENT_AGENT", length = 50)
	public String getParentAgent() {
		return this.parentAgent;
	}

	public void setParentAgent(String parentAgent) {
		this.parentAgent = parentAgent;
	}

	@Column(name = "AREA_LEVEL", length = 50)
	public String getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}

	@Column(name = "AREA_NAME", length = 250)
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@Column(name = "DESCRIPTION", length = 250)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "COMMENTS", length = 250)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "AGENT_NAME", length = 50)
	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "FIRST_NAME", length = 50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME", length = 50)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "USER_NAME", length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD", length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "MSISDN")
	public Long getZongMsisdn() {
		return this.zongMsisdn;
	}

	public void setZongMsisdn(Long zongMsisdn) {
		this.zongMsisdn = zongMsisdn;
	}

	@Column(name = "ACCOUNT_OPEN_DATE")
	public Date getAccountOpenDate() {
		return this.accountOpenDate;
	}

	public void setAccountOpenDate(Date accountOpenDate) {
		this.accountOpenDate = accountOpenDate;
	}

	@Column(name = "AGENT_NTN_NO")
	public Long getAgentNtnNo() {
		return this.agentNtnNo;
	}

	public void setAgentNtnNo(Long agentNtnNo) {
		this.agentNtnNo = agentNtnNo;
	}

	@Column(name = "CNIC_NO")
	public Long getCnicNo() {
		return this.cnicNo;
	}

	public void setCnicNo(Long cnicNo) {
		this.cnicNo = cnicNo;
	}

	@Column(name = "CNIC_EXPIRY_DATE")
	public Date getCnicExpiryDate() {
		return this.cnicExpiryDate;
	}

	public void setCnicExpiryDate(Date cnicExpiryDate) {
		this.cnicExpiryDate = cnicExpiryDate;
	}

	@Column(name = "CONTACT_NO")
	public Long getContactNo() {
		return this.contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	@Column(name = "LANDLINE_NO")
	public Long getLandlineNo() {
		return this.landlineNo;
	}

	public void setLandlineNo(Long landlineNo) {
		this.landlineNo = landlineNo;
	}

	@Column(name = "MOBILE_NO")
	public Long getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name = "EMAIL", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "BUSINESS_NAME", length = 50)
	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Column(name = "NATURE_OF_BUSINESS", length = 50)
	public String getNatureOfBusiness() {
		return this.natureOfBusiness;
	}

	public void setNatureOfBusiness(String natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	@Column(name = "SHOP_NO", length = 50)
	public String getShopNo() {
		return this.shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	@Column(name = "MARKET_PLAZA", length = 50)
	public String getMarketPlaza() {
		return this.marketPlaza;
	}

	public void setMarketPlaza(String marketPlaza) {
		this.marketPlaza = marketPlaza;
	}

	@Column(name = "DISTRICT_TEHSIL_TOWN", length = 50)
	public String getDistrictTehsilTown() {
		return this.districtTehsilTown;
	}

	public void setDistrictTehsilTown(String districtTehsilTown) {
		this.districtTehsilTown = districtTehsilTown;
	}

	@Column(name = "CITY_VILLAGE", length = 50)
	public String getCityVillage() {
		return this.cityVillage;
	}

	public void setCityVillage(String cityVillage) {
		this.cityVillage = cityVillage;
	}

	@Column(name = "POST_OFFICE", length = 50)
	public String getPostOffice() {
		return this.postOffice;
	}

	public void setPostOffice(String postOffice) {
		this.postOffice = postOffice;
	}

	@Column(name = "BUSINESS_NTN_NO")
	public Long getBusinessNtnNo() {
		return this.businessNtnNo;
	}

	public void setBusinessNtnNo(Long businessNtnNo) {
		this.businessNtnNo = businessNtnNo;
	}

	@Column(name = "NEAREST_LANDMARK", length = 50)
	public String getNearestLandmark() {
		return this.nearestLandmark;
	}

	public void setNearestLandmark(String nearestLandmark) {
		this.nearestLandmark = nearestLandmark;
	}

	@Column(name = "ACCOUNT_NO" , length = 50)
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "ACCOUNT_TITLE", length = 50)
	public String getAccountTitle() {
		return this.accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	@Column(name = "CREATED_ON" , nullable = false )
    public Date getCreatedOn() {
    	return createdOn;
    }

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   @Column(name = "UPDATED_ON" , nullable = false )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
   }

   @Version 
   @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }

   @Column(name = "CREATED_BY" , nullable = false)
   public Long getCreatedBy() {
	   return createdBy;
   }

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY" , nullable = false)
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "DISTRIBUTOR_ID")
	public Long getAgentNetworkId() {
		return agentNetworkId;
	}

	public void setAgentNetworkId(Long agentNetworkId) {
		this.agentNetworkId = agentNetworkId;
	}

	@Column(name = "REGION_ID")
	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	@Column(name = "RETAILER_ID")
	public Long getFranchiseBranchId() {
		return franchiseBranchId;
	}

	public void setFranchiseBranchId(Long franchiseBranchId) {
		this.franchiseBranchId = franchiseBranchId;
	}

	@Column(name = "CREATED_BY_NAME"  , length = 250)	
	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	@Column(name = "RECORD_STATUS", nullable = false)
	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	@Transient
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    @Transient
    public String getActiveAsString()
    {
        return active? "Yes" : "No";
    }
}