package com.inov8.microbank.common.model.productmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The VarProdShipmentListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="VarProdShipmentListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "VAR_PROD_SHIPMENT_LIST_VIEW")
public class VarProdShipmentListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -5807101556975372772L;
private Long shipmentId;
   private Double unitPrice;
   private Long productId;

   /**
    * Default constructor.
    */
   public VarProdShipmentListViewModel() {
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
