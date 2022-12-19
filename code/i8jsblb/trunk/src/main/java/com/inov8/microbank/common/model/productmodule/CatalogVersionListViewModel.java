package com.inov8.microbank.common.model.productmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CatalogVersionListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CatalogVersionListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CATALOG_VERSION_LIST_VIEW")
public class CatalogVersionListViewModel extends BasePersistableModel {
  
	private static final long serialVersionUID = 6796843779308477445L;
	private Integer catalogVersionNo;
	private String productCatalogName;
	private Long productCatalogId;
	private Long deviceTypeId;
	private Long appUserTypeId;
	private Long appUserId;

   /**
    * Default constructor.
    */
   public CatalogVersionListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAppUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAppUserId(primaryKey);
    }

   /**
    * Returns the value of the <code>catalogVersionNo</code> property.
    *
    */
      @Column(name = "CATALOG_VERSION_NO"  )
   public Integer getCatalogVersionNo() {
      return catalogVersionNo;
   }

   /**
    * Sets the value of the <code>catalogVersionNo</code> property.
    *
    * @param catalogVersionNo the value for the <code>catalogVersionNo</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setCatalogVersionNo(Integer catalogVersionNo) {
      this.catalogVersionNo = catalogVersionNo;
   }

   /**
    * Returns the value of the <code>productCatalogName</code> property.
    *
    */
      @Column(name = "PRODUCT_CATALOG_NAME"  , length=50 )
   public String getProductCatalogName() {
      return productCatalogName;
   }

   /**
    * Sets the value of the <code>productCatalogName</code> property.
    *
    * @param productCatalogName the value for the <code>productCatalogName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setProductCatalogName(String productCatalogName) {
      this.productCatalogName = productCatalogName;
   }

   /**
    * Returns the value of the <code>productCatalogId</code> property.
    *
    */
      @Column(name = "PRODUCT_CATALOG_ID"  )
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAppUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&appUserId=" + getAppUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() { 
			return "appUserId";				
    }
    
    @Column(name = "DEVICE_TYPE_ID")
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

    @Column(name = "APP_USER_TYPE_ID")
	public Long getAppUserTypeId() {
		return appUserTypeId;
	}

	public void setAppUserTypeId(Long appUserTypeId) {
		this.appUserTypeId = appUserTypeId;
	}
	
	@Column(name = "APP_USER_ID" , nullable = false )
    @Id 
	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}
}
