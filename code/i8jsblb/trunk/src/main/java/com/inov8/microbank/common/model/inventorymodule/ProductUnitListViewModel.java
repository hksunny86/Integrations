package com.inov8.microbank.common.model.inventorymodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ProductUnitListViewModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductUnitListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PRODUCT_UNIT_LIST_VIEW")
public class ProductUnitListViewModel extends BasePersistableModel {
  



   private Long productUnitId;
   private String userName;
   private String serialNo;
   private String isSold;
   private String additionalField1;
   private String additionalField2;
   private Long shipmentId;
   private String pin;
   private Boolean active;

   /**
    * Default constructor.
    */
   public ProductUnitListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductUnitId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductUnitId(primaryKey);
    }

   /**
    * Returns the value of the <code>productUnitId</code> property.
    *
    */
      @Column(name = "PRODUCT_UNIT_ID" , nullable = false )
   @Id 
   public Long getProductUnitId() {
      return productUnitId;
   }

   /**
    * Sets the value of the <code>productUnitId</code> property.
    *
    * @param productUnitId the value for the <code>productUnitId</code> property
    *    
		    */

   public void setProductUnitId(Long productUnitId) {
      this.productUnitId = productUnitId;
   }

   /**
    * Returns the value of the <code>userName</code> property.
    *
    */
      @Column(name = "USER_NAME"  , length=50 )
   public String getUserName() {
      return userName;
   }

   /**
    * Sets the value of the <code>userName</code> property.
    *
    * @param userName the value for the <code>userName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUserName(String userName) {
      this.userName = userName;
   }

   /**
    * Returns the value of the <code>serialNo</code> property.
    *
    */
      @Column(name = "SERIAL_NO" , nullable = false , length=50 )
   public String getSerialNo() {
      return serialNo;
   }

   /**
    * Sets the value of the <code>serialNo</code> property.
    *
    * @param serialNo the value for the <code>serialNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setSerialNo(String serialNo) {
      this.serialNo = serialNo;
   }

   /**
    * Returns the value of the <code>isSold</code> property.
    *
    */
      @Column(name = "IS_SOLD"  , length=6 )
   public String getIsSold() {
      return isSold;
   }

   /**
    * Sets the value of the <code>isSold</code> property.
    *
    * @param isSold the value for the <code>isSold</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="6"
    */

   public void setIsSold(String isSold) {
      this.isSold = isSold;
   }

   /**
    * Returns the value of the <code>additionalField1</code> property.
    *
    */
      @Column(name = "ADDITIONAL_FIELD1"  , length=50 )
   public String getAdditionalField1() {
      return additionalField1;
   }

   /**
    * Sets the value of the <code>additionalField1</code> property.
    *
    * @param additionalField1 the value for the <code>additionalField1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAdditionalField1(String additionalField1) {
      this.additionalField1 = additionalField1;
   }

   /**
    * Returns the value of the <code>additionalField2</code> property.
    *
    */
      @Column(name = "ADDITIONAL_FIELD2"  , length=50 )
   public String getAdditionalField2() {
      return additionalField2;
   }

   /**
    * Sets the value of the <code>additionalField2</code> property.
    *
    * @param additionalField2 the value for the <code>additionalField2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAdditionalField2(String additionalField2) {
      this.additionalField2 = additionalField2;
   }

   /**
    * Returns the value of the <code>shipmentId</code> property.
    *
    */
      @Column(name = "SHIPMENT_ID" , nullable = false )
   public Long getShipmentId() {
      return shipmentId;
   }

   /**
    * Sets the value of the <code>shipmentId</code> property.
    *
    * @param shipmentId the value for the <code>shipmentId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setShipmentId(Long shipmentId) {
      this.shipmentId = shipmentId;
   }

   /**
    * Returns the value of the <code>pin</code> property.
    *
    */
      @Column(name = "PIN" , nullable = false , length=50 )
   public String getPin() {
      return pin;
   }

   /**
    * Sets the value of the <code>pin</code> property.
    *
    * @param pin the value for the <code>pin</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPin(String pin) {
      this.pin = pin;
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductUnitId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productUnitId=" + getProductUnitId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productUnitId";
			return primaryKeyFieldName;				
    }       
}
