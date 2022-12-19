package com.inov8.microbank.common.model.productmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The DiscreteProdUnitListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DiscreteProdUnitListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DISCRETE_PROD_UNIT_LIST_VIEW")
public class DiscreteProdUnitListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 5692076068967117396L;
private Long productUnitId;
   private Long productId;
   private Long shipmentId;
   private String serialNo;
   private String pin;
   private Boolean sold;
   private Boolean active;
   private String userName;
   private String additionalField1;
   private String additionalField2;
   private Long createdBy;
   private Long updatedBy;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public DiscreteProdUnitListViewModel() {
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
    * Returns the value of the <code>serialNo</code> property.
    *
    */
      @Column(name = "SERIAL_NO" , nullable = false , length=30 )
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
    * @spring.validator-var name="maxlength" value="30"
    */

   public void setSerialNo(String serialNo) {
      this.serialNo = serialNo;
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
    * Returns the value of the <code>sold</code> property.
    *
    */
      @Column(name = "IS_SOLD" , nullable = false )
   public Boolean getSold() {
      return sold;
   }

   /**
    * Sets the value of the <code>sold</code> property.
    *
    * @param sold the value for the <code>sold</code> property
    *    
		    */

   public void setSold(Boolean sold) {
      this.sold = sold;
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
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY" , nullable = false )
   public Long getCreatedBy() {
      return createdBy;
   }

   /**
    * Sets the value of the <code>createdBy</code> property.
    *
    * @param createdBy the value for the <code>createdBy</code> property
    *    
		    */

   public void setCreatedBy(Long createdBy) {
      this.createdBy = createdBy;
   }

   /**
    * Returns the value of the <code>updatedBy</code> property.
    *
    */
      @Column(name = "UPDATED_BY" , nullable = false )
   public Long getUpdatedBy() {
      return updatedBy;
   }

   /**
    * Sets the value of the <code>updatedBy</code> property.
    *
    * @param updatedBy the value for the <code>updatedBy</code> property
    *    
		    */

   public void setUpdatedBy(Long updatedBy) {
      this.updatedBy = updatedBy;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   /**
    * Returns the value of the <code>updatedOn</code> property.
    *
    */
      @Column(name = "UPDATED_ON" , nullable = false )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   /**
    * Sets the value of the <code>updatedOn</code> property.
    *
    * @param updatedOn the value for the <code>updatedOn</code> property
    *    
		    */

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
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
