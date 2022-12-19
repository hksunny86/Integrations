package com.inov8.microbank.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.integration.common.model.LimitTypeModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;

/**
 * VelocityRuleModel entity.
 * @author Muhammad Omar Butt
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "VELOCITY_RULE_SEQ",sequenceName = "VELOCITY_RULE_SEQ", allocationSize=1)
@Table(name = "VELOCITY_RULE")
public class VelocityRuleModel  extends BasePersistableModel{
	 
	private static final long serialVersionUID = 5292376944364332315L;
	
	private Long velocityRuleId;
	private ProductModel productIdProductModel;
	private OlaCustomerAccountTypeModel customerAccountTypeIdOlaCustomerAccountTypeModel;
	private DeviceTypeModel deviceTypeIdDeviceTypeModel;
	private SegmentModel segmentIdSegmentModel;
	private DistributorModel distributorIdDistributorModel;
	private DistributorLevelModel distributorLevelIdDistributorLevelModel;
	private LimitTypeModel limitTypeIdLimitTypeModel;
	private Long maxNoOfTransaction;
	private Long maxAmountOfTransaction;
	private String errorMsg;

	private Boolean isActive;
	private Date createdOn;
	private Date updatedOn;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Integer versionNo;

    public VelocityRuleModel() {
    }

    @Column(name = "VELOCITY_RULE_ID")
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VELOCITY_RULE_SEQ")
    public Long getVelocityRuleId() {
        return this.velocityRuleId;
    }
    
    public void setVelocityRuleId(Long velocityRuleId) {
        this.velocityRuleId = velocityRuleId;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")    
    public AppUserModel getRelationUpdatedByAppUserModel(){
       return updatedByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
       return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
       this.updatedByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }
    

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")    
    public AppUserModel getRelationCreatedByAppUserModel(){
       return createdByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel(){
       return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
       this.createdByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	updatedByAppUserModel = null;
       }
       else
       {
         updatedByAppUserModel = new AppUserModel();
       	updatedByAppUserModel.setAppUserId(appUserId);
       }      
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (createdByAppUserModel != null) {
          return createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	createdByAppUserModel = null;
       }
       else
       {
         createdByAppUserModel = new AppUserModel();
       	createdByAppUserModel.setAppUserId(appUserId);
       }      
    }

    
    @Column(name="IS_ACTIVE")
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Column(name="CREATED_ON", length=7)
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Version
    @Column(name="VERSION_NO")
    public Integer getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getVelocityRuleId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setVelocityRuleId(primaryKey);
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&velocityRuleId=" + getVelocityRuleId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName(){ 
			String primaryKeyFieldName = "velocityRuleId";
			return primaryKeyFieldName;				
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")    
    public ProductModel getRelationProductIdProductModel(){
       return productIdProductModel;
    }
    
    @javax.persistence.Transient
    public void setRelationProductIdProductModel(ProductModel productModel) {
       this.productIdProductModel = productModel;
    }
    
    @javax.persistence.Transient
    public ProductModel getProductIdProductModel(){
    	return getRelationProductIdProductModel();
    }
    
    @javax.persistence.Transient
    public void setProductIdProductModel(ProductModel productModel) {
    	 if(null != productModel)
         {
    		 setRelationProductIdProductModel((ProductModel)productModel.clone());
         }
    }
    
    @javax.persistence.Transient
    public Long getProductId() {
       if (productIdProductModel != null) {
          return productIdProductModel.getProductId();
       } else {
          return null;
       }
    }
    
    @javax.persistence.Transient
    public void setProductId(Long productId) {
       if(productId == null)
       {      
       	productIdProductModel = null;
       }
       else
       {
    	   productIdProductModel = new ProductModel();
    	   productIdProductModel.setProductId(productId);;
       }      
    }

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "OLA_CUSTOMER_ACCOUNT_TYPE_ID")
	public OlaCustomerAccountTypeModel getRelationCustomerAccountTypeIdOlaCustomerAccountTypeModel(){
		return customerAccountTypeIdOlaCustomerAccountTypeModel;
	}

	@javax.persistence.Transient
	public void setRelationCustomerAccountTypeIdOlaCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
		this.customerAccountTypeIdOlaCustomerAccountTypeModel = olaCustomerAccountTypeModel;
	}

	@javax.persistence.Transient
	public OlaCustomerAccountTypeModel getCustomerAccountTypeIdOlaCustomerAccountTypeModel(){
		return getRelationCustomerAccountTypeIdOlaCustomerAccountTypeModel();
	}

	@javax.persistence.Transient
	public void setCustomerAccountTypeIdOlaCustomerAccountTypeModel(OlaCustomerAccountTypeModel olaCustomerAccountTypeModel) {
		if(null != olaCustomerAccountTypeModel)
		{
			setRelationCustomerAccountTypeIdOlaCustomerAccountTypeModel((OlaCustomerAccountTypeModel)olaCustomerAccountTypeModel.clone());
		}
	}

	@javax.persistence.Transient
	public Long getCustomerAccountTypeId() {
		if (customerAccountTypeIdOlaCustomerAccountTypeModel != null) {
			return customerAccountTypeIdOlaCustomerAccountTypeModel.getCustomerAccountTypeId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		if(customerAccountTypeId == null)
		{
			customerAccountTypeIdOlaCustomerAccountTypeModel = null;
		}
		else
		{
			customerAccountTypeIdOlaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
			customerAccountTypeIdOlaCustomerAccountTypeModel.setCustomerAccountTypeId(customerAccountTypeId);;
		}
	}

	 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	    @JoinColumn(name = "DEVICE_TYPE_ID")    
	    public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel(){
	       return deviceTypeIdDeviceTypeModel;
	    }
	    
	    @javax.persistence.Transient
	    public void setRelationDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeIdDeviceTypeModel) {
	       this.deviceTypeIdDeviceTypeModel = deviceTypeIdDeviceTypeModel;
	    }
	    
	    @javax.persistence.Transient
	    public DeviceTypeModel getDeviceTypeIdDeviceTypeModel(){
	    	return getRelationDeviceTypeIdDeviceTypeModel();
	    }
	    
	    @javax.persistence.Transient
	    public void setDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeIdDeviceTypeModel) {
	    	 if(null != deviceTypeIdDeviceTypeModel)
	         {
	    		 setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel)deviceTypeIdDeviceTypeModel.clone());
	         }
	    }
	    
	    @javax.persistence.Transient
	    public Long getDeviceTypeId() {
	       if (deviceTypeIdDeviceTypeModel != null) {
	          return deviceTypeIdDeviceTypeModel.getDeviceTypeId();
	       } else {
	          return null;
	       }
	    }
	    
	    @javax.persistence.Transient
	    public void setDeviceTypeId(Long deviceTypeId) {
	       if(deviceTypeId == null)
	       {      
	    	   deviceTypeIdDeviceTypeModel = null;
	       }
	       else
	       {
	    	   deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
	    	   deviceTypeIdDeviceTypeModel.setDeviceTypeId(deviceTypeId);;
	       }      
	    }
	
	

	 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	    @JoinColumn(name = "SEGMENT_ID")    
	    public SegmentModel getRelationSegmentIdSegmentModel(){
	       return segmentIdSegmentModel;
	    }
	    
	    @javax.persistence.Transient
	    public void setRelationSegmentIdSegmentModel(SegmentModel segmentModel) {
	       this.segmentIdSegmentModel = segmentModel;
	    }
	    
	    @javax.persistence.Transient
	    public SegmentModel getSegmentIdSegmentModel(){
	    	return getRelationSegmentIdSegmentModel();
	    }
	    
	    @javax.persistence.Transient
	    public void setSegmentIdSegmentModel(SegmentModel segmentModel) {
	    	 if(null != segmentModel)
	         {
	    		 setRelationSegmentIdSegmentModel((SegmentModel)segmentModel.clone());
	         }
	    }
	    @javax.persistence.Transient
	    public Long getSegmentId() {
	       if (segmentIdSegmentModel != null) {
	          return segmentIdSegmentModel.getSegmentId();
	       } else {
	          return null;
	       }
	    }
	    
	    @javax.persistence.Transient
	    public void setSegmentId(Long segmentId) {
	       if(segmentId == null)
	       {      
	    	   segmentIdSegmentModel = null;
	       }
	       else
	       {
	    	   segmentIdSegmentModel = new SegmentModel();
	    	   segmentIdSegmentModel.setSegmentId(segmentId);
	       }      
	    }

	 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	    @JoinColumn(name = "DISTRIBUTOR_ID")    
	    public DistributorModel getRelationDistributorIdDistributorModel(){
	       return distributorIdDistributorModel;
	    }
	    
	    @javax.persistence.Transient
	    public void setRelationDistributorIdDistributorModel(DistributorModel distributorModel) {
	       this.distributorIdDistributorModel = distributorModel;
	    }
	    
	    @javax.persistence.Transient
	    public DistributorModel getDistributorIdDistributorModel(){
	    	return getRelationDistributorIdDistributorModel();
	    }
	    
	    @javax.persistence.Transient
	    public void setDistributorIdDistributorModel(DistributorModel distributorModel) {
	    	 if(null != distributorModel)
	         {
	    		 setRelationDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
	         }
	    }
	    @javax.persistence.Transient
	    public Long getDistributorId() {
	       if (distributorIdDistributorModel != null) {
	          return distributorIdDistributorModel.getDistributorId();
	       } else {
	          return null;
	       }
	    }
	    
	    @javax.persistence.Transient
	    public void setDistributorId(Long distributorId) {
	       if(null == distributorId)
	       {      
	    	   distributorIdDistributorModel = null;
	       }
	       else
	       {
	    	   distributorIdDistributorModel = new DistributorModel();
	    	   distributorIdDistributorModel.setDistributorId(distributorId);;
	       }      
	    }
	  
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "AGENT_TYPE")    
    public DistributorLevelModel getRelationDistributorLevelIdDistributorLevelModel(){
       return distributorLevelIdDistributorLevelModel;
    }
    
    @javax.persistence.Transient
    public void setRelationDistributorLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
       this.distributorLevelIdDistributorLevelModel = distributorLevelModel;
    }
    
    @javax.persistence.Transient
    public DistributorLevelModel getDistributorLevelIdDistributorLevelModel(){
    	return getRelationDistributorLevelIdDistributorLevelModel();
    }
    
    @javax.persistence.Transient
    public void setDistributorLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
    	 if(null != distributorLevelModel)
         {
    		 setRelationDistributorLevelIdDistributorLevelModel((DistributorLevelModel)distributorLevelModel.clone());
         }
    }
    @javax.persistence.Transient
    public Long getDistributorLevelId() {
       if (distributorLevelIdDistributorLevelModel != null) {
          return distributorLevelIdDistributorLevelModel.getDistributorLevelId();
       } else {
          return null;
       }
    }
    
    @javax.persistence.Transient
    public void setDistributorLevelId(Long distributorLevelId) {
       if(distributorLevelId == null)
       {      
    	   distributorLevelIdDistributorLevelModel = null;
       }
       else
       {
    	   distributorLevelIdDistributorLevelModel = new DistributorLevelModel();
    	   distributorLevelIdDistributorLevelModel.setDistributorLevelId(distributorLevelId);
       }      
    }

	 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	    @JoinColumn(name = "LIMIT_TYPE_ID")    
	    public LimitTypeModel getRelationLimitTypeIdLimitTypeModel(){
	       return limitTypeIdLimitTypeModel;
	    }
	    
	    @javax.persistence.Transient
	    public void setRelationLimitTypeIdLimitTypeModel(LimitTypeModel limitTypeIdLimitTypeModel) {
	       this.limitTypeIdLimitTypeModel = limitTypeIdLimitTypeModel;
	    }
	    
	    @javax.persistence.Transient
	    public LimitTypeModel getLimitTypeIdLimitTypeModel(){
	    	return getRelationLimitTypeIdLimitTypeModel();
	    }
	    
	    @javax.persistence.Transient
	    public void setLimitTypeIdLimitTypeModel(LimitTypeModel limitTypeIdLimitTypeModel) {
	    	 if(null != limitTypeIdLimitTypeModel)
	         {
	    		 setRelationLimitTypeIdLimitTypeModel((LimitTypeModel)limitTypeIdLimitTypeModel.clone());
	         }
	    }
	    @javax.persistence.Transient
	    public Long getLimitTypeId() {
	       if (limitTypeIdLimitTypeModel != null) {
	          return limitTypeIdLimitTypeModel.getLimitTypeId();
	       } else {
	          return null;
	       }
	    }
	    
	    @javax.persistence.Transient
	    public void setLimitTypeId(Long limitTypeId) {
	       if(limitTypeId == null)
	       {      
	    	   limitTypeIdLimitTypeModel = null;
	       }
	       else
	       {
	    	   limitTypeIdLimitTypeModel = new LimitTypeModel();
	    	   limitTypeIdLimitTypeModel.setLimitTypeId(limitTypeId);
	       }      
	    }
    @Column(name="MAX_NO_OF_TRX")
	public Long getMaxNoOfTransaction() {
		return maxNoOfTransaction;
	}

	public void setMaxNoOfTransaction(Long maxNoOfTransaction) {
		this.maxNoOfTransaction = maxNoOfTransaction;
	}

    @Column(name="MAX_AMOUNT_OF_TRX")
	public Long getMaxAmountOfTransaction() {
		return maxAmountOfTransaction;
	}

	public void setMaxAmountOfTransaction(Long maxAmountOfTransaction) {
		this.maxAmountOfTransaction = maxAmountOfTransaction;
	}
	
	 @Column(name="ERROR_MESSAGE")
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			  associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
		   	 associationModel = new AssociationModel();
		 	
		associationModel.setClassName("ProductModel");
		associationModel.setPropertyName("relationProductIdProductModel");   		
		associationModel.setValue(getRelationProductIdProductModel());
		
		associationModelList.add(associationModel);

		//Account Type Limit
		associationModel = new AssociationModel();

		associationModel.setClassName("OlaCustomerAccountType");
		associationModel.setPropertyName("relationCustomerAccountTypeIdOlaCustomerAccountTypeModel");
		associationModel.setValue(getRelationCustomerAccountTypeIdOlaCustomerAccountTypeModel());

		associationModelList.add(associationModel);


		associationModel = new AssociationModel();
		 	
		associationModel.setClassName("SegmentModel");
		associationModel.setPropertyName("relationSegmentIdSegmentModel");   		
		associationModel.setValue(getRelationSegmentIdSegmentModel());
		
		associationModelList.add(associationModel);
		
			associationModel = new AssociationModel();
	 	
		associationModel.setClassName("DeviceTypeModel");
		associationModel.setPropertyName("relationDeviceTypeIdDeviceTypeModel");   		
		associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());
		
		associationModelList.add(associationModel);
		
			associationModel = new AssociationModel();
	 	
		associationModel.setClassName("DistributorModel");
		associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
		associationModel.setValue(getRelationDistributorIdDistributorModel());
		
		associationModelList.add(associationModel);
		 
			associationModel = new AssociationModel();
	 	
		associationModel.setClassName("DistributorLevelModel");
		associationModel.setPropertyName("relationDistributorLevelIdDistributorLevelModel");   		
		associationModel.setValue(getRelationDistributorLevelIdDistributorLevelModel());
		
		associationModelList.add(associationModel);
		
			associationModel = new AssociationModel();
	 	
		associationModel.setClassName("LimitTypeModel");
		associationModel.setPropertyName("relationLimitTypeIdLimitTypeModel");   		
		associationModel.setValue(getRelationLimitTypeIdLimitTypeModel());
		
		associationModelList.add(associationModel);
		
		
		return associationModelList;
    }
}