package com.inov8.microbank.common.model.productdeviceflowmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ProductDeviceFlowListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductDeviceFlowListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PRODUCT_DEVICE_FLOW_LIST_VIEW")
public class ProductDeviceFlowListViewModel extends BasePersistableModel {
  



   private Long productDeviceFlowId;
   private Long productId;
   private String deviceFlowName;
   private String productName;
   private String deviceTypeName;
   private Long deviceTypeId;
   private Long deviceFlowId;
   private String productSupplier;

   /**
    * Default constructor.
    */
   public ProductDeviceFlowListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductDeviceFlowId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductDeviceFlowId(primaryKey);
    }

   /**
    * Returns the value of the <code>productDeviceFlowId</code> property.
    *
    */
      @Column(name = "PRODUCT_DEVICE_FLOW_ID" , nullable = false )
   @Id 
   public Long getProductDeviceFlowId() {
      return productDeviceFlowId;
   }

   /**
    * Sets the value of the <code>productDeviceFlowId</code> property.
    *
    * @param productDeviceFlowId the value for the <code>productDeviceFlowId</code> property
    *    
		    */

   public void setProductDeviceFlowId(Long productDeviceFlowId) {
      this.productDeviceFlowId = productDeviceFlowId;
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
    * Returns the value of the <code>deviceFlowName</code> property.
    *
    */
      @Column(name = "DEVICE_FLOW_NAME" , nullable = false , length=50 )
   public String getDeviceFlowName() {
      return deviceFlowName;
   }

   /**
    * Sets the value of the <code>deviceFlowName</code> property.
    *
    * @param deviceFlowName the value for the <code>deviceFlowName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setDeviceFlowName(String deviceFlowName) {
      this.deviceFlowName = deviceFlowName;
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
    * Returns the value of the <code>deviceTypeName</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_NAME" , nullable = false , length=50 )
   public String getDeviceTypeName() {
      return deviceTypeName;
   }

   /**
    * Sets the value of the <code>deviceTypeName</code> property.
    *
    * @param deviceTypeName the value for the <code>deviceTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setDeviceTypeName(String deviceTypeName) {
      this.deviceTypeName = deviceTypeName;
   }

   /**
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_ID" , nullable = false )
   public Long getDeviceTypeId() {
      return deviceTypeId;
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDeviceTypeId(Long deviceTypeId) {
      this.deviceTypeId = deviceTypeId;
   }

   /**
    * Returns the value of the <code>deviceFlowId</code> property.
    *
    */
      @Column(name = "DEVICE_FLOW_ID" , nullable = false )
   public Long getDeviceFlowId() {
      return deviceFlowId;
   }

   /**
    * Sets the value of the <code>deviceFlowId</code> property.
    *
    * @param deviceFlowId the value for the <code>deviceFlowId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDeviceFlowId(Long deviceFlowId) {
      this.deviceFlowId = deviceFlowId;
   }

   /**
    * Returns the value of the <code>productSupplier</code> property.
    *
    */
      @Column(name = "PRODUCT_SUPPLIER"  , length=50 )
   public String getProductSupplier() {
      return productSupplier;
   }

   /**
    * Sets the value of the <code>productSupplier</code> property.
    *
    * @param productSupplier the value for the <code>productSupplier</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setProductSupplier(String productSupplier) {
      this.productSupplier = productSupplier;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductDeviceFlowId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productDeviceFlowId=" + getProductDeviceFlowId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productDeviceFlowId";
			return primaryKeyFieldName;				
    }       
}
