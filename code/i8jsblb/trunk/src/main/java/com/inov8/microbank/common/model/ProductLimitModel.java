package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_LIMIT_SEQ",sequenceName = "PRODUCT_LIMIT_SEQ", allocationSize=1)
@Table(name = "PRODUCT_LIMIT")
public class ProductLimitModel extends BasePersistableModel implements Serializable {

	private Long productLimitId;
    private Boolean active;
	private Date updatedOn;
	private Date createdOn;
	private String description;
	private Integer versionNo;
	private Double minLimit;
	private Double maxLimit;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	
	private DeviceTypeModel deviceTypeIdDeviceTypeModel;
	private DistributorModel distributorIdDistributorModel;
	private ProductModel productIdProductModel;
	private SupplierModel supplierIdSupplierModel;
	private SegmentModel segmentIdSegmentModel;

	public ProductLimitModel() {}
	
	public ProductLimitModel(Long deviceTypeId, Long productId, Long distributorId, Long supplierId, Long segmentId) {

		if(deviceTypeId != null) {

			deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
			deviceTypeIdDeviceTypeModel.setDeviceTypeId(deviceTypeId);
		}

		if(productId != null) {

			productIdProductModel = new ProductModel();
			productIdProductModel.setProductId(productId);
		}
		
		if(distributorId != null) {
			
			distributorIdDistributorModel = new DistributorModel();
			distributorIdDistributorModel.setDistributorId(distributorId);
		}

		if(supplierId != null) {

			supplierIdSupplierModel = new SupplierModel();
			supplierIdSupplierModel.setSupplierId(supplierId);
		}

		if(segmentId != null) {

			segmentIdSegmentModel = new SegmentModel();
			segmentIdSegmentModel.setSegmentId(segmentId);
		}
		
		
	}
	
	
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductLimitId();
    }

   @Column(name = "PRODUCT_LIMIT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_LIMIT_SEQ")
   public Long getProductLimitId() {
		return productLimitId;
	}

	public void setProductLimitId(Long productLimitId) {
		this.productLimitId = productLimitId;
	}


   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductLimitId(primaryKey);
    }

   @Column(name = "DESCRIPTION"  , length=250 )
   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   public void setActive(Boolean active) {
      this.active = active;
   }

   @Column(name = "UPDATED_ON" , nullable = false )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
   }

   @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   @Version 
   @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }


   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }


   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SUPPLIER_ID")    
   public SupplierModel getRelationSupplierIdSupplierModel(){
      return supplierIdSupplierModel;
   }
    

   @javax.persistence.Transient
   public SupplierModel getSupplierIdSupplierModel(){
      return getRelationSupplierIdSupplierModel();
   }


   @javax.persistence.Transient
   public void setRelationSupplierIdSupplierModel(SupplierModel supplierModel) {
      this.supplierIdSupplierModel = supplierModel;
   }
   
   @javax.persistence.Transient
   public void setSupplierIdSupplierModel(SupplierModel supplierModel) {
      if(null != supplierModel)
      {
      	setRelationSupplierIdSupplierModel((SupplierModel)supplierModel.clone());
      }      
   }
   

   @javax.persistence.Transient
   public Long getSupplierId() {
      if (supplierIdSupplierModel != null) {
         return supplierIdSupplierModel.getSupplierId();
      } else {
         return null;
      }
   }

   @javax.persistence.Transient
   public void setSupplierId(Long supplierId) {
      if(supplierId == null)
      {      
      	supplierIdSupplierModel = null;
      }
      else
      {
        supplierIdSupplierModel = new SupplierModel();
      	supplierIdSupplierModel.setSupplierId(supplierId);
      }      
   }
   
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CREATED_BY")    
   public AppUserModel getRelationCreatedByAppUserModel(){
      return createdByAppUserModel;
   }
    
   @javax.persistence.Transient
   public AppUserModel getCreatedByAppUserModel(){
      return getRelationCreatedByAppUserModel();
   }

   @javax.persistence.Transient
   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
      this.createdByAppUserModel = appUserModel;
   }
   
   @javax.persistence.Transient
   public void setCreatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   

   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "UPDATED_BY")    
   public AppUserModel getRelationUpdatedByAppUserModel(){
      return updatedByAppUserModel;
   }
    

   @javax.persistence.Transient
   public AppUserModel getUpdatedByAppUserModel(){
      return getRelationUpdatedByAppUserModel();
   }

   @javax.persistence.Transient
   public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
      this.updatedByAppUserModel = appUserModel;
   }
   

   @javax.persistence.Transient
   public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
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

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUpdatedBy() {
      if (updatedByAppUserModel != null) {
         return updatedByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																																																					    */
   
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


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductLimitId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productLimitId=" + getProductLimitId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			return "productLimitId";				
    }
    

    @Column(name = "MINIMUM_LIMIT")
	public Double getMinLimit() {
		return minLimit;
	}
    public void setMinLimit(Double minLimit) {
		this.minLimit = minLimit;
	}

	 @Column(name = "MAXIMUM_LIMIT")
	public Double getMaxLimit() {
		return maxLimit;
	}
	 public void setMaxLimit(Double maxLimit) {
			this.maxLimit = maxLimit;
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
	      if(productId == null) {      
	      	productIdProductModel = null;
	      }
	      else {
	        productIdProductModel = new ProductModel();
	      	productIdProductModel.setProductId(productId);
	      }      
	   }
	   
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "PRODUCT_ID")    
	   public ProductModel getRelationProductIdProductModel(){
	      return productIdProductModel;
	   }
	    

	   @javax.persistence.Transient
	   public ProductModel getProductIdProductModel(){
	      return getRelationProductIdProductModel();
	   }


	   @javax.persistence.Transient
	   public void setRelationProductIdProductModel(ProductModel productModel) {
	      this.productIdProductModel = productModel;
	   }
	   
	   @javax.persistence.Transient
	   public void setProductIdProductModel(ProductModel productModel) {
	      if(null != productModel)
	      {
	      	setRelationProductIdProductModel((ProductModel)productModel.clone());
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
	      if(distributorId == null) {      
	      	distributorIdDistributorModel = null;
	      }
	      else {
	        distributorIdDistributorModel = new DistributorModel();
	      	distributorIdDistributorModel.setDistributorId(distributorId);
	      }      
	   }
	   
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "AGENT_NETWORK_ID")    
	   public DistributorModel getRelationDistributorIdDistributorModel(){
	      return distributorIdDistributorModel;
	   }
	    

	   @javax.persistence.Transient
	   public DistributorModel getDistributorIdDistributorModel(){
	      return getRelationDistributorIdDistributorModel();
	   }


	   @javax.persistence.Transient
	   public void setRelationDistributorIdDistributorModel(DistributorModel distributorModel) {
	      this.distributorIdDistributorModel = distributorModel;
	   }
	   
	   @javax.persistence.Transient
	   public void setDistributorIdDistributorModel(DistributorModel distributorModel) {
	      if(null != distributorModel)
	      {
	      	setRelationDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
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
	      if(deviceTypeId == null) {      
	      	deviceTypeIdDeviceTypeModel = null;
	      }
	      else {
	        deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
	      	deviceTypeIdDeviceTypeModel.setDeviceTypeId(deviceTypeId);
	      }      
	   }
	   
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "CHANNEL_ID")    
	   public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel(){
	      return deviceTypeIdDeviceTypeModel;
	   }
	    

	   @javax.persistence.Transient
	   public DeviceTypeModel getDeviceTypeIdDeviceTypeModel(){
	      return getRelationDeviceTypeIdDeviceTypeModel();
	   }


	   @javax.persistence.Transient
	   public void setRelationDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
	      this.deviceTypeIdDeviceTypeModel = deviceTypeModel;
	   }
	   
	   @javax.persistence.Transient
	   public void setDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
	      if(null != deviceTypeModel)
	      {
	      	setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel)deviceTypeModel.clone());
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
	      if(segmentId == null) {      
	      	segmentIdSegmentModel = null;
	      }
	      else {
	        segmentIdSegmentModel = new SegmentModel();
	      	segmentIdSegmentModel.setSegmentId(segmentId);
	      }      
	   }
	   
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "SEGMENT_ID")    
	   public SegmentModel getRelationSegmentIdSegmentModel(){
	      return segmentIdSegmentModel;
	   }
	    

	   @javax.persistence.Transient
	   public SegmentModel getSegmentIdSegmentModel(){
	      return getRelationSegmentIdSegmentModel();
	   }


	   @javax.persistence.Transient
	   public void setRelationSegmentIdSegmentModel(SegmentModel segmentModel) {
	      this.segmentIdSegmentModel = segmentModel;
	   }
	   
	   @javax.persistence.Transient
	   public void setSegmentIdSegmentModel(SegmentModel segmentModel) {
	      if(null != segmentModel)
	      {
	      	setRelationSegmentIdSegmentModel((SegmentModel)segmentModel.clone());
	      }      
	   }
}
