package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Rizwan Munir
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "AGENT_MERCHANT_DETAIL_SEQ", sequenceName = "AGENT_MERCHANT_DETAIL_SEQ", allocationSize = 1)
@Table(name = "AGENT_MERCHANT_DETAIL")
public class AgentMerchantDetailModel extends BasePersistableModel {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= -8290513824546633535L;
	private Long						agentMerchantDetailId;
	private String						referenceNo;
	private String						initialAppFormNo;
	private String						businessName;
	private AppUserModel				employeeAppUserModel;
	private DistributorModel			distributorIdDistributorModel;
	private DistributorLevelModel		distributorLevelIdDistributorLevelModel;
	private ProductCatalogModel			productCatalogModel;
	private Long						regionId;
	private Long						areaLevelId;
	private Long						areaId;
	private String						town;
	private String						uc;
	private String						sector;
	private Long						acLevelQualificationId;
	private AppUserModel				createdByAppUserModel;
	private AppUserModel				updatedByAppUserModel;
	private Date						createdOn;
	private Date						updatedOn;
	private Integer						versionNo;
	private String						userName;
	private String						password;
	private String						confirmPassword;
	private PartnerGroupModel			partnerGroupIdPartnerGroupModel;
	private RetailerModel 				retailerIdRetailerModel;
	private RetailerContactModel 		parentAgentIdRetailerContactModel;
	private String 						parentAgentName;
	private RetailerContactModel 		ultimateParentAgentIdRetailerContactModel;
	private String 						ultimateParentAgentName;
	private Boolean 					isPasswordChanged;
	private String 						isHead;
	private boolean 					isError;
	private boolean 					isCreatedFranchise;
	private boolean						isRegionChangeAllowed;
	private Long actionId;
	private Long usecaseId;
	
	private Date startDate;
	private Date endDate;

	private Boolean						directChangedToSub;
	
	
	public AgentMerchantDetailModel() {

	}

	@Column(name = "AGENT_MERCHANT_DETAIL_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_MERCHANT_DETAIL_SEQ")
	public Long getAgentMerchantDetailId() {
		return this.agentMerchantDetailId;
	}

	public void setAgentMerchantDetailId(Long agentMerchantDetailId) {
		if (agentMerchantDetailId != null) {
			this.agentMerchantDetailId = agentMerchantDetailId;
		}
	}

	/**
	 * Return the primary key.
	 * @return Long with the primary key.
	 */
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return this.getAgentMerchantDetailId();
	}

	/**
	 * Set the primary key.
	 * @param primaryKey the primary key
	 */
	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		this.setAgentMerchantDetailId(primaryKey);
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
	
	@Transient
	public String getEmpName() {
		if(null!=this.employeeAppUserModel && null!=this.employeeAppUserModel.getFirstName())
			return this.employeeAppUserModel.getFirstName() +  " " + this.employeeAppUserModel.getLastName();
		else 
			return null;
	}


	@Column(name = "TOWN")
	public String getTown() {
		return this.town;
	}

	public void setTown(String town) {
		if (town != null) {
			this.town = town;
		}
	}

	@Column(name = "UC")
	public String getUc() {
		return this.uc;
	}

	public void setUc(String uc) {
		if (uc != null) {
			this.uc = uc;
		}
	}

	@Column(name = "SECTOR")
	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		if (sector != null) {
			this.sector = sector;
		}
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 */
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

	/**
	 * Helper method for Struts with displaytag
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&agentMerchantDetailId="
				+ this.getAgentMerchantDetailId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "agentMerchantDetailId";
		return primaryKeyFieldName;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "AGENT_TYPE_ID")
	public DistributorLevelModel getRelationDistributorLevelIdDistributorLevelModel() {
		return this.distributorLevelIdDistributorLevelModel;
	}

	@javax.persistence.Transient
	public void setRelationDistributorLevelIdDistributorLevelModel(
			DistributorLevelModel distributorLevelModel) {
		this.distributorLevelIdDistributorLevelModel = distributorLevelModel;
	}

	@javax.persistence.Transient
	public DistributorLevelModel getDistributorLevelIdDistributorLevelModel() {
		return this.distributorLevelIdDistributorLevelModel;
	}

	@javax.persistence.Transient
	public void setDistributorLevelIdDistributorLevelModel(
			DistributorLevelModel distributorLevelIdDistributorLevelModel) {
		if (distributorLevelIdDistributorLevelModel != null) {
			this.setRelationDistributorLevelIdDistributorLevelModel((DistributorLevelModel) distributorLevelIdDistributorLevelModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getDistributorLevelId() {
		if (this.distributorLevelIdDistributorLevelModel != null) {
			return this.distributorLevelIdDistributorLevelModel.getDistributorLevelId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setDistributorLevelId(Long agentTypeId) {
		if (agentTypeId == null) {
			this.distributorLevelIdDistributorLevelModel = null;
		} else {
			this.distributorLevelIdDistributorLevelModel = new DistributorLevelModel();
			this.distributorLevelIdDistributorLevelModel.setDistributorLevelId(agentTypeId);
		}
	}

	/**
	 * Helper method for Complex Example Queries
	 */
	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = new AssociationModel();

		associationModel.setClassName("DistributorLevelModel");
		associationModel.setPropertyName("relationDistributorLevelIdDistributorLevelModel");
		associationModel.setValue(this.getRelationDistributorLevelIdDistributorLevelModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("ProductCatalogModel");
		associationModel
				.setPropertyName("productCatalogModel");
		associationModel.setValue(this.getProductCatalogModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("DistributorModel");
		associationModel
				.setPropertyName("relationDistributorIdDistributorModel");
		associationModel.setValue(this
				.getRelationDistributorIdDistributorModel());
		
		/*associationModel = new AssociationModel();
		associationModel.setClassName("PartnerGroupModel");
		associationModel
				.setPropertyName("relationPartnerGroupIdPartnerGroupModel");
		associationModel.setValue(this
				.getRelationPartnerGroupIdPartnerGroupModel());
		associationModelList.add(associationModel);*/

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(this.getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(this.getRelationUpdatedByAppUserModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
		associationModel.setClassName("RetailerContactModel");
		associationModel.setPropertyName("parentAgentIdRetailerContactModel");
		associationModel.setValue(this.getParentAgentIdRetailerContactModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("RetailerContactModel");
		associationModel.setPropertyName("ultimateParentAgentIdRetailerContactModel");
		associationModel.setValue(this.getUltimateParentAgentIdRetailerContactModel());
		associationModelList.add(associationModel);

		return associationModelList;
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
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_ID")
	public DistributorModel getRelationDistributorIdDistributorModel() {
		return this.distributorIdDistributorModel;
	}

	@javax.persistence.Transient
	public DistributorModel getDistributorIdDistributorModel() {
		return this.getRelationDistributorIdDistributorModel();
	}

	@javax.persistence.Transient
	public void setRelationDistributorIdDistributorModel(
			DistributorModel distributorModel) {
		this.distributorIdDistributorModel = distributorModel;
	}

	@javax.persistence.Transient
	public void setDistributorIdDistributorModel(
			DistributorModel distributorModel) {
		if (null != distributorModel) {
			this.setRelationDistributorIdDistributorModel((DistributorModel) distributorModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getDistributorId() {
		if (this.distributorIdDistributorModel != null) {
			return this.distributorIdDistributorModel.getDistributorId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setDistributorId(Long distributorId) {
		if (distributorId == null) {
			this.distributorIdDistributorModel = null;
		} else {
			this.distributorIdDistributorModel = new DistributorModel();
			this.distributorIdDistributorModel.setDistributorId(distributorId);
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

	@Column(name = "PASSWORD")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		if (password != null) {
			this.password = password;
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARTNER_GROUP_ID")
	public PartnerGroupModel getRelationPartnerGroupIdPartnerGroupModel() {
		return this.partnerGroupIdPartnerGroupModel;
	}

	@javax.persistence.Transient
	public PartnerGroupModel getPartnerGroupIdPartnerGroupModel() {
		return this.getRelationPartnerGroupIdPartnerGroupModel();
	}

	@javax.persistence.Transient
	public void setRelationPartnerGroupIdPartnerGroupModel(
			PartnerGroupModel partnerGroupModel) {
		this.partnerGroupIdPartnerGroupModel = partnerGroupModel;
	}

	@javax.persistence.Transient
	public void setPartnerGroupIdPartnerGroupModel(
			PartnerGroupModel partnerGroupModel) {
		if (null != partnerGroupModel) {
			this.setRelationPartnerGroupIdPartnerGroupModel((PartnerGroupModel) partnerGroupModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getPartnerGroupId() {
		if (this.partnerGroupIdPartnerGroupModel != null) {
			return this.partnerGroupIdPartnerGroupModel.getPartnerGroupId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setPartnerGroupId(Long partnerGroupId) {
		if (partnerGroupId == null) {
			this.partnerGroupIdPartnerGroupModel = null;
		} else {
			this.partnerGroupIdPartnerGroupModel = new PartnerGroupModel();
			this.partnerGroupIdPartnerGroupModel
					.setPartnerGroupId(partnerGroupId);
		}
	}

	@javax.persistence.Transient
	public Long getActionId()
	{
		return actionId;
	}

	public void setActionId(Long actionId)
	{
		this.actionId = actionId;
	}

	@javax.persistence.Transient
	public Long getUsecaseId()
	{
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId)
	{
		this.usecaseId = usecaseId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATALOG_ID")
	public ProductCatalogModel getProductCatalogModel()
	{
		return productCatalogModel;
	}

	@javax.persistence.Transient
	public void setProductCatalogModel(ProductCatalogModel productCatalogModel)
	{
		this.productCatalogModel = productCatalogModel;
	}
	
	@javax.persistence.Transient
	public Long getProductCatalogId()
	{
		if(this.productCatalogModel!=null)
			return productCatalogModel.getProductCatalogId();
		else return null;
	}
	
	@javax.persistence.Transient
	public void setProductCatalogId(Long productCatalogId)
	{
		if(this.productCatalogModel!=null)
			this.productCatalogModel.setProductCatalogId(productCatalogId);
		else
		{
			this.productCatalogModel=new ProductCatalogModel();
			this.productCatalogModel.setProductCatalogId(productCatalogId);
		}
	}
	
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	   @JoinColumn(name = "RETAILER_ID")    
	   public RetailerModel getRelationRetailerIdRetailerModel(){
	      return retailerIdRetailerModel;
	   }

	   @javax.persistence.Transient
	   public RetailerModel getRetailerIdRetailerModel(){
	      return getRelationRetailerIdRetailerModel();
	   }

	   @javax.persistence.Transient
	   public void setRelationRetailerIdRetailerModel(RetailerModel retailerModel) {
	      this.retailerIdRetailerModel = retailerModel;
	   }

	   @javax.persistence.Transient
	   public void setRetailerIdRetailerModel(RetailerModel retailerModel) {
	      if(null != retailerModel)
	      {
	      	setRelationRetailerIdRetailerModel((RetailerModel)retailerModel.clone());
	      }      
	   }
	   
	   @javax.persistence.Transient
	   public Long getRetailerId() {
	      if (retailerIdRetailerModel != null) {
	         return retailerIdRetailerModel.getRetailerId();
	      } else {
	         return null;
	      }
	   }
	   
	   @javax.persistence.Transient
	   public void setRetailerId(Long retailerId) {
	      if(retailerId == null)
	      {      
	      	retailerIdRetailerModel = null;
	      }
	      else
	      {
	        retailerIdRetailerModel = new RetailerModel();
	      	retailerIdRetailerModel.setRetailerId(retailerId);
	      }      
	   }

	   @Transient
		public String getConfirmPassword()
		{
			return confirmPassword;
		}

	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}
	
	@Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_AGENT_ID")    
	public RetailerContactModel getParentAgentIdRetailerContactModel() {
		return parentAgentIdRetailerContactModel;
	}


	public void setParentAgentIdRetailerContactModel(
		RetailerContactModel parentAgentIdRetailerContactModel) {
		
		if(null!=parentAgentIdRetailerContactModel)
			this.parentAgentIdRetailerContactModel = (RetailerContactModel) parentAgentIdRetailerContactModel.clone();
		else
			this.parentAgentIdRetailerContactModel = null;
	}
	
	   
   @javax.persistence.Transient
   public Long getParentAgentId() {
      if (parentAgentIdRetailerContactModel != null) {
         return parentAgentIdRetailerContactModel.getRetailerContactId();
      } else {
         return null;
      }
   }
   
   @javax.persistence.Transient
   public void setParentAgentId(Long retailerConatctId) {
      if(retailerConatctId == null)
      {      
    	  parentAgentIdRetailerContactModel = null;
      }
      else
      {
    	  parentAgentIdRetailerContactModel = new RetailerContactModel();
    	  parentAgentIdRetailerContactModel.setRetailerContactId(retailerConatctId);
      }      
   }
   

	@Column(name = "PARENT_AGENT_NAME")
	public String getParentAgentName() {
		return parentAgentName;
	}

	public void setParentAgentName(String parentAgentName) {
		this.parentAgentName = parentAgentName;
	}
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ULTIMATE_PARENT_AGENT_ID")    
	public RetailerContactModel getUltimateParentAgentIdRetailerContactModel() {
		return ultimateParentAgentIdRetailerContactModel;
	}

	public void setUltimateParentAgentIdRetailerContactModel(
		RetailerContactModel ultimateParentAgentIdRetailerContactModel) {
		
		if(null!=ultimateParentAgentIdRetailerContactModel)
			this.ultimateParentAgentIdRetailerContactModel = (RetailerContactModel) ultimateParentAgentIdRetailerContactModel.clone();
		else 
			this.ultimateParentAgentIdRetailerContactModel=null;
	}
	
	   
   @javax.persistence.Transient
   public Long getUltimateParentAgentId() {
      if (ultimateParentAgentIdRetailerContactModel != null) {
         return ultimateParentAgentIdRetailerContactModel.getRetailerContactId();
      } else {
         return null;
      }
   }
   
   @javax.persistence.Transient
   public void setUltimateParentAgentId(Long retailerConatctId) {
      if(retailerConatctId == null)
      {      
    	  ultimateParentAgentIdRetailerContactModel = null;
      }
      else
      {
    	  ultimateParentAgentIdRetailerContactModel = new RetailerContactModel();
    	  ultimateParentAgentIdRetailerContactModel.setRetailerContactId(retailerConatctId);
      }      
   }

	@Column(name = "ULTIMATE_PARENT_AGENT_NAME")
	public String getUltimateParentAgentName() {
		return ultimateParentAgentName;
	}

	public void setUltimateParentAgentName(String ultimateParentAgentName) {
		this.ultimateParentAgentName = ultimateParentAgentName;
	}

	@Transient
	public Boolean getIsPasswordChanged() {
		return isPasswordChanged;
	}
	public void setIsPasswordChanged(Boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
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

	@Transient
	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	@Transient
	public boolean isCreatedFranchise()
	{
		return isCreatedFranchise;
	}

	public void setCreatedFranchise(boolean isCreatedFranchise)
	{
		this.isCreatedFranchise = isCreatedFranchise;
	}
	
	@Transient
	public boolean getIsRegionChangeAllowed() {
		return isRegionChangeAllowed;
	}

	public void setIsRegionChangeAllowed(boolean isRegionChangeAllowed) {
		this.isRegionChangeAllowed = isRegionChangeAllowed;
	}

	@Transient
	public Boolean getDirectChangedToSub()
	{
		return directChangedToSub;
	}

	public void setDirectChangedToSub(Boolean directChangedToSub)
	{
		this.directChangedToSub = directChangedToSub;
	}
}