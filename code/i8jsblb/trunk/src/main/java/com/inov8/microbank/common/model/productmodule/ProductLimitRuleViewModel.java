package com.inov8.microbank.common.model.productmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PRODUCT_LIMIT_RULE_DETAIL_VIEW")
public class ProductLimitRuleViewModel extends BasePersistableModel implements Serializable {

   private static final long serialVersionUID = -4350194068949610720L;
   private Long productLimitRuleViewId;
   private Long productId;
   private Long deviceTypeId;
   private Long segmentId;
   private Long distributorId;
   private Long handlerAccountTypeId;
   private Boolean isActive;
   private Double minLimit;
   private Double maxLimit;
   private Long mnoId;

   

   public ProductLimitRuleViewModel() {}
   
   public ProductLimitRuleViewModel(Long productId, Long deviceTypeId, Long segmentId, Long distributorId, Long handlerAccountTypeId,Long mnoId) {

	   this.productId = productId;
	   this.deviceTypeId = deviceTypeId;
	   this.segmentId = segmentId;
	   this.distributorId = distributorId;
	   this.handlerAccountTypeId = handlerAccountTypeId;
	   this.mnoId = mnoId;
   }
   
   
	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductLimitRuleViewId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setProductLimitRuleViewId(primaryKey);
    }



   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getProductLimitRuleViewId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() { 
			String primaryKeyFieldName = "productLimitRuleViewId";
			return primaryKeyFieldName;				
    }

	public void setProductLimitRuleViewId(Long productLimitRuleViewId) {
		this.productLimitRuleViewId = productLimitRuleViewId;
	}
	
	@Id
	@Column(name = "PK")
	public Long getProductLimitRuleViewId() {
		return productLimitRuleViewId;
	}

	@Column(name = "PRODUCT_ID")
	 public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "DEVICE_TYPE_ID")
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	@Column(name = "SEGMENT_ID")
	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}

	@Column(name = "DISTRIBUTOR_ID")
	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
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

	@Column(name = "IS_ACTIVE")	
	public Boolean getIsActive() {
		return isActive;
	}
		
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "HANDLER_ACCOUNT_TYPE_ID")	
	public Long getHandlerAccountTypeId() {
		return handlerAccountTypeId;
	}

	public void setHandlerAccountTypeId(Long handlerAccountTypeId) {
		this.handlerAccountTypeId = handlerAccountTypeId;
	}

	@Column(name = "SERVICE_OP_ID")
	public Long getMnoId() {
		return mnoId;
	}

	public void setMnoId(Long mnoId) {
		this.mnoId = mnoId;
	}
}