package com.inov8.microbank.common.model.productmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ProductListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/07/24 19:29:08 $
 *
 *
 * @spring.bean name="ProductListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PRODUCT_LIST_VIEW")
public class ProductListViewModel extends BasePersistableModel implements Serializable {
  

	private static final long serialVersionUID = 7724546221099494534L;

   private Long serviceId;
	
   private Long supplierId;
   private String productName;
   private Double costPrice;
   private Double unitPrice;
   private Long minimumStockLevel;
   private Boolean active;
   private Integer versionNo;
   private Long productId;
   private Double minLimit;
   private Double maxLimit;
   private String serviceName;
   private String supplierName;
   private Boolean batchMode;
   private Boolean checked;
   private Boolean uspProductidCheck;
   private Long categoryId;
   private Long appUserTypeId;
   private Integer sequenceNo = 999999999;
   /**
    * Default constructor.
    */
   public ProductListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductId(primaryKey);
    }

   /**
    * Returns the value of the <code>serviceId</code> property.
    *
    */
      @Column(name = "SERVICE_ID" , nullable = false )
   public Long getServiceId() {
      return serviceId;
   }

   /**
    * Sets the value of the <code>serviceId</code> property.
    *
    * @param serviceId the value for the <code>serviceId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setServiceId(Long serviceId) {
      this.serviceId = serviceId;
   }

   /**
    * Returns the value of the <code>supplierId</code> property.
    *
    */
      @Column(name = "SUPPLIER_ID" , nullable = false )
   public Long getSupplierId() {
      return supplierId;
   }

   /**
    * Sets the value of the <code>supplierId</code> property.
    *
    * @param supplierId the value for the <code>supplierId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setSupplierId(Long supplierId) {
      this.supplierId = supplierId;
   }

   /**
    * Returns the value of the <code>productName</code> property.
    *
    */
      @Column(name = "PRODUCT_NAME" , nullable = false , length=50 )
   public String getProductName() {
      return productName;
   }

   /**
    * Sets the value of the <code>productName</code> property.
    *
    * @param productName the value for the <code>productName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setProductName(String productName) {
      this.productName = productName;
   }

   /**
    * Returns the value of the <code>costPrice</code> property.
    *
    */
      @Column(name = "COST_PRICE"  )
   public Double getCostPrice() {
      return costPrice;
   }

   /**
    * Sets the value of the <code>costPrice</code> property.
    *
    * @param costPrice the value for the <code>costPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setCostPrice(Double costPrice) {
      this.costPrice = costPrice;
   }

   /**
    * Returns the value of the <code>unitPrice</code> property.
    *
    */
      @Column(name = "UNIT_PRICE"  )
   public Double getUnitPrice() {
      return unitPrice;
   }

   /**
    * Sets the value of the <code>unitPrice</code> property.
    *
    * @param unitPrice the value for the <code>unitPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setUnitPrice(Double unitPrice) {
      this.unitPrice = unitPrice;
   }

   /**
    * Returns the value of the <code>minimumStockLevel</code> property.
    *
    */
      @Column(name = "MINIMUM_STOCK_LEVEL"  )
   public Long getMinimumStockLevel() {
      return minimumStockLevel;
   }

   /**
    * Sets the value of the <code>minimumStockLevel</code> property.
    *
    * @param minimumStockLevel the value for the <code>minimumStockLevel</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMinimumStockLevel(Long minimumStockLevel) {
      this.minimumStockLevel = minimumStockLevel;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   /**
    * Sets the value of the <code>versionNo</code> property.
    *
    * @param versionNo the value for the <code>versionNo</code> property
    *    
		    */

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }

   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
      @Column(name = "PRODUCT_ID" , nullable = false )
   @Id 
   public Long getProductId() {
      return productId;
   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
    *    
		    */

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   @Column(name = "MIN_LIMIT")
   public Double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(Double minLimit) {
		this.minLimit = minLimit;
	}
	
	@Column(name = "MAX_LIMIT")
	public Double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Double maxLimit) {
		this.maxLimit = maxLimit;
	}

   /**
    * Returns the value of the <code>serviceName</code> property.
    *
    */
      @Column(name = "SERVICE_NAME" , nullable = false , length=50 )
   public String getServiceName() {
      return serviceName;
   }

   /**
    * Sets the value of the <code>serviceName</code> property.
    *
    * @param serviceName the value for the <code>serviceName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
   }

   /**
    * Returns the value of the <code>supplierName</code> property.
    *
    */
      @Column(name = "SUPPLIER_NAME" , nullable = false , length=50 )
   public String getSupplierName() {
      return supplierName;
   }

   /**
    * Sets the value of the <code>supplierName</code> property.
    *
    * @param supplierName the value for the <code>supplierName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setSupplierName(String supplierName) {
      this.supplierName = supplierName;
   }

   /**
    * Returns the value of the <code>batchMode</code> property.
    *
    */
      @Column(name = "IS_BATCH_MODE" , nullable = false )
   public Boolean getBatchMode() {
      return batchMode;
   }

   /**
    * Sets the value of the <code>batchMode</code> property.
    *
    * @param batchMode the value for the <code>batchMode</code> property
    *    
		    */

   public void setBatchMode(Boolean batchMode) {
      this.batchMode = batchMode;
   }

   /**
    * Returns the value of the <code>uspProductidCheck</code> property.
    *
    */
      @Column(name = "USP_PRODUCTID_CHECK" , nullable = false )
   public Boolean getUspProductidCheck() {
      return uspProductidCheck;
   }

   /**
    * Sets the value of the <code>uspProductidCheck</code> property.
    *
    * @param uspProductidCheck the value for the <code>uspProductidCheck</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setUspProductidCheck(Boolean uspProductidCheck) {
      this.uspProductidCheck = uspProductidCheck;
   }


	@Column(name = "CATEGORY_ID")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}  

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productId=" + getProductId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productId";
			return primaryKeyFieldName;				
    } 

		
	@javax.persistence.Transient
    public Boolean getChecked()
	{
		return checked;
	}

	public void setChecked(Boolean checked)
	{
		this.checked = checked;
	}

	@javax.persistence.Transient
	public Integer getSequenceNo() {
		if (sequenceNo == null){
			sequenceNo = 999999999;
		}
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

    @Column(name = "APP_USER_TYPE_ID")
    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }

}
