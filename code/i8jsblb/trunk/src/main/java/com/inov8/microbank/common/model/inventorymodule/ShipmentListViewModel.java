package com.inov8.microbank.common.model.inventorymodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ShipmentListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ShipmentListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SHIPMENT_LIST_VIEW")
public class ShipmentListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -1155607208723883113L;
private Long shipmentId;
   private Double price;
   private Date expiryDate;
   private String productName;
   private String supplierName;
   private Double outstandingCredit;
   private Double quantity;
   private Date purchaseDate;
   private Boolean active;
   private Long productId;

   /**
    * Default constructor.
    */
   public ShipmentListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getShipmentId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setShipmentId(primaryKey);
    }

   /**
    * Returns the value of the <code>shipmentId</code> property.
    *
    */
      @Column(name = "SHIPMENT_ID" , nullable = false )
   @Id 
   public Long getShipmentId() {
      return shipmentId;
   }

   /**
    * Sets the value of the <code>shipmentId</code> property.
    *
    * @param shipmentId the value for the <code>shipmentId</code> property
    *    
		    */

   public void setShipmentId(Long shipmentId) {
      this.shipmentId = shipmentId;
   }

   /**
    * Returns the value of the <code>price</code> property.
    *
    */
      @Column(name = "PRICE" , nullable = false )
   public Double getPrice() {
      return price;
   }

   /**
    * Sets the value of the <code>price</code> property.
    *
    * @param price the value for the <code>price</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setPrice(Double price) {
      this.price = price;
   }

   /**
    * Returns the value of the <code>expiryDate</code> property.
    *
    */
      @Column(name = "EXPIRY_DATE"  )
   public Date getExpiryDate() {
      return expiryDate;
   }

   /**
    * Sets the value of the <code>expiryDate</code> property.
    *
    * @param expiryDate the value for the <code>expiryDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setExpiryDate(Date expiryDate) {
      this.expiryDate = expiryDate;
   }

   /**
    * Returns the value of the <code>productName</code> property.
    *
    */
      @Column(name = "PRODUCT_NAME"  , length=50 )
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
    * Returns the value of the <code>supplierName</code> property.
    *
    */
      @Column(name = "SUPPLIER_NAME"  , length=50 )
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
    * Returns the value of the <code>outstandingCredit</code> property.
    *
    */
      @Column(name = "OUTSTANDING_CREDIT"  )
   public Double getOutstandingCredit() {
      return outstandingCredit;
   }

   /**
    * Sets the value of the <code>outstandingCredit</code> property.
    *
    * @param outstandingCredit the value for the <code>outstandingCredit</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setOutstandingCredit(Double outstandingCredit) {
      this.outstandingCredit = outstandingCredit;
   }

   /**
    * Returns the value of the <code>quantity</code> property.
    *
    */
      @Column(name = "QUANTITY"  )
   public Double getQuantity() {
      return quantity;
   }

   /**
    * Sets the value of the <code>quantity</code> property.
    *
    * @param quantity the value for the <code>quantity</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setQuantity(Double quantity) {
      this.quantity = quantity;
   }

   /**
    * Returns the value of the <code>purchaseDate</code> property.
    *
    */
      @Column(name = "PURCHASE_DATE" , nullable = false )
   public Date getPurchaseDate() {
      return purchaseDate;
   }

   /**
    * Sets the value of the <code>purchaseDate</code> property.
    *
    * @param purchaseDate the value for the <code>purchaseDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setPurchaseDate(Date purchaseDate) {
      this.purchaseDate = purchaseDate;
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getShipmentId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&shipmentId=" + getShipmentId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "shipmentId";
			return primaryKeyFieldName;				
    }       
}
