package com.inov8.microbank.common.model.productmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ProdCatalogDetailListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/11/14 19:29:08 $
 *
 *
 * @spring.bean name="ProdCatalogDetailListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PROD_CATALOG_DETAIL_LIST_VIEW")
public class ProdCatalogDetailListViewModel extends BasePersistableModel implements Serializable {
  



   private Long pk;
   private Long productCatalogId;
   private String productName;
   private Long productId;
   private transient String productIdEncrypt;
   private Long mnoId;
   private Double unitPrice;
   private Long supplierId;
   private Integer sequenceNo;
   private Integer sequenceForConsumerApp;
   private String supplierName;
   private String serviceTypeName;
   private Integer productCatalogVersionNo;
   private Long serviceId;
   private Long serviceTypeId;
   private Long deviceFlowId;
   private String billServiceLabel;
   private Long categoryId;
   private String ext;
   private String categoryName;
   private Boolean showSupplierInMenu;
   private Long deviceTypeId;
   private String consumerLabel;
   private Boolean amtRequired;
   private Double minLimit;
   private Double maxLimit;
   private Boolean doValidate;
   private String consumerInputType;
   private Long multiples;
   private Double minConsumerLength;
   private Double maxConsumerLength;
   private Boolean inquiryRequired;
   private Boolean partialPaymentAllowed;
   private String url;
   private String prodDenom;
   private Boolean denomFlag;
   private String denomString;

   /**
    * Default constructor.
    */
   public ProdCatalogDetailListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPk(primaryKey);
    }

   /**
    * Returns the value of the <code>pk</code> property.
    *
    */
      @Column(name = "PK"  )
   @Id 
   public Long getPk() {
      return pk;
   }

   /**
    * Sets the value of the <code>pk</code> property.
    *
    * @param pk the value for the <code>pk</code> property
    *    
		    */

   public void setPk(Long pk) {
      this.pk = pk;
   }

   /**
    * Returns the value of the <code>productCatalogId</code> property.
    *
    */
      @Column(name = "PRODUCT_CATALOG_ID" , nullable = false )
   public Long getProductCatalogId() {
      return productCatalogId;
   }

   /**
    * Sets the value of the <code>productCatalogId</code> property.
    *
    * @param productCatalogId the value for the <code>productCatalogId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setProductCatalogId(Long productCatalogId) {
      this.productCatalogId = productCatalogId;
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
    * Returns the value of the <code>productId</code> property.
    *
    */
      @Column(name = "PRODUCT_ID" , nullable = false )
   public Long getProductId() {
      return productId;
   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   /**
    * Returns the value of the <code>mnoId</code> property.
    *
    */
      @Column(name = "SERVICE_OP_ID"  )
   public Long getMnoId() {
      return mnoId;
   }

   /**
    * Sets the value of the <code>mnoId</code> property.
    *
    * @param mnoId the value for the <code>mnoId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMnoId(Long mnoId) {
      this.mnoId = mnoId;
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
    * Returns the value of the <code>sequenceNo</code> property.
    *
    */
      @Column(name = "SEQUENCE_NO"  )
   public Integer getSequenceNo() {
      return sequenceNo;
   }

   /**
    * Sets the value of the <code>sequenceNo</code> property.
    *
    * @param sequenceNo the value for the <code>sequenceNo</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setSequenceNo(Integer sequenceNo) {
      this.sequenceNo = sequenceNo;
   }


    @Column(name = "SEQUENCE_CUST_APP"  )
    public Integer getSequenceForConsumerApp() {
        return sequenceForConsumerApp;
    }

    public void setSequenceForConsumerApp(Integer sequenceForConsumerApp) {
        this.sequenceForConsumerApp = sequenceForConsumerApp;
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
    * Returns the value of the <code>serviceTypeName</code> property.
    *
    */
      @Column(name = "SERVICE_TYPE_NAME" , nullable = false , length=50 )
   public String getServiceTypeName() {
      return serviceTypeName;
   }

   /**
    * Sets the value of the <code>serviceTypeName</code> property.
    *
    * @param serviceTypeName the value for the <code>serviceTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setServiceTypeName(String serviceTypeName) {
      this.serviceTypeName = serviceTypeName;
   }

   /**
    * Returns the value of the <code>productCatalogVersionNo</code> property.
    *
    */
      @Column(name = "PRODUCT_CATALOG_VERSION_NO" , nullable = false )
   public Integer getProductCatalogVersionNo() {
      return productCatalogVersionNo;
   }

   /**
    * Sets the value of the <code>productCatalogVersionNo</code> property.
    *
    * @param productCatalogVersionNo the value for the <code>productCatalogVersionNo</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setProductCatalogVersionNo(Integer productCatalogVersionNo) {
      this.productCatalogVersionNo = productCatalogVersionNo;
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
    * Returns the value of the <code>serviceTypeId</code> property.
    *
    */
      @Column(name = "SERVICE_TYPE_ID" , nullable = false )
   public Long getServiceTypeId() {
      return serviceTypeId;
   }

   /**
    * Sets the value of the <code>serviceTypeId</code> property.
    *
    * @param serviceTypeId the value for the <code>serviceTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setServiceTypeId(Long serviceTypeId) {
      this.serviceTypeId = serviceTypeId;
   }

   /**
    * Returns the value of the <code>deviceFlowId</code> property.
    *
    */
      @Column(name = "DEVICE_FLOW_ID"  )
   public Long getDeviceFlowId() {
      return deviceFlowId;
   }

   /**
    * Sets the value of the <code>deviceFlowId</code> property.
    *
    * @param deviceFlowId the value for the <code>deviceFlowId</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setDeviceFlowId(Long deviceFlowId) {
      this.deviceFlowId = deviceFlowId;
   }

   /**
    * Returns the value of the <code>billServiceLabel</code> property.
    *
    */
      @Column(name = "BILL_SERVICE_LABEL"  , length=50 )
   public String getBillServiceLabel() {
      return billServiceLabel;
   }

   /**
    * Sets the value of the <code>billServiceLabel</code> property.
    *
    * @param billServiceLabel the value for the <code>billServiceLabel</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setBillServiceLabel(String billServiceLabel) {
      this.billServiceLabel = billServiceLabel;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getPk();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }
    
    @javax.persistence.Transient
    public String getProductIdEncrypt() {
    	return productIdEncrypt;
    }


    public void setProductIdEncrypt(String productIdEncrypt) {
    	this.productIdEncrypt = productIdEncrypt;
    }

    @Column(name = "CATEGORY_ID" , nullable = false )
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	
	
	@Column(name = "EXT" , nullable = false )
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Column(name = "SHOW_SUPPLIER_IN_MENU" , nullable = false )
	public Boolean getShowSupplierInMenu() {
		return showSupplierInMenu;
	}

	public void setShowSupplierInMenu(Boolean showSupplierInMenu) {
		this.showSupplierInMenu = showSupplierInMenu;
	}

	@Column(name = "CATEGORY_NAME" , nullable = false )
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Column(name = "DEVICE_TYPE_ID" , nullable = false )
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	@Column(name = "CONSUMER_LABEL")
	public String getConsumerLabel() {
		return consumerLabel;
	}

	public void setConsumerLabel(String consumerLabel) {
		this.consumerLabel = consumerLabel;
	}
	
	@Column(name = "AMT_REQUIRED")
	public Boolean getAmtRequired() {
		return amtRequired;
	}

	public void setAmtRequired(Boolean amtRequired) {
		this.amtRequired = amtRequired;
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

	@Column(name = "DO_VALIDATE")
	public Boolean getDoValidate() {
		return doValidate;
	}

	public void setDoValidate(Boolean doValidate) {
		this.doValidate = doValidate;
	}
	@Column(name = "CNSMR_INPUT_TYPE")
	public String getConsumerInputType() {
		return consumerInputType;
	}

	public void setConsumerInputType(String consumerInputType) {
		this.consumerInputType = consumerInputType;
	}
	
	@Column(name = "MULTIPLES")
	public Long getMultiples() {
		return multiples;
	}

	public void setMultiples(Long multiples) {
		this.multiples = multiples;
	}

    @Column(name = "MIN_CNSMR_NO_LENGTH")
    public Double getMinConsumerLength() {
        return minConsumerLength;
    }

    public void setMinConsumerLength(Double minConsumerLength) {
        this.minConsumerLength = minConsumerLength;
    }

    @Column(name = "MAX_CNSMR_NO_LENGTH")
    public Double getMaxConsumerLength() {
        return maxConsumerLength;
    }

    public void setMaxConsumerLength(Double maxConsumerLength) {
        this.maxConsumerLength = maxConsumerLength;
    }

    @Column(name = "INQUIRY_REQUIRED")
    public Boolean getInquiryRequired() {
        return inquiryRequired;
    }

    public void setInquiryRequired(Boolean inquiryRequired) {
        this.inquiryRequired = inquiryRequired;
    }

    @Column(name = "PARTIAL_PAYMENT_ALLOWED")
    public Boolean getPartialPaymentAllowed() {
        return partialPaymentAllowed;
    }

    public void setPartialPaymentAllowed(Boolean partialPaymentAllowed) {
        this.partialPaymentAllowed = partialPaymentAllowed;
    }

    @Column(name="URL")
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Column(name="PROD_DENOM")
    public String getProdDenom() { return prodDenom; }

    public void setProdDenom(String prodDenom) { this.prodDenom = prodDenom; }

    @Column(name="DENOM_FLAG")
    public Boolean getDenomFlag() { return denomFlag; }

    public void setDenomFlag(Boolean denomFlag) { this.denomFlag = denomFlag; }

    @Column(name="DENOM_STRING")
    public String getDenomString() { return denomString; }

    public void setDenomString(String denomString) { this.denomString = denomString; }
}
