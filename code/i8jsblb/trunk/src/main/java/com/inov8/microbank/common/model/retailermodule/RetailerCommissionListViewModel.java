package com.inov8.microbank.common.model.retailermodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The RetailerCommissionListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="RetailerCommissionListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "RETAILER_COMMISSION_LIST_VIEW")
public class RetailerCommissionListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -6948754761171338795L;
private Long commissionTransactionId;
   private String name;
   private Double commAmount;

   /**
    * Default constructor.
    */
   public RetailerCommissionListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommissionTransactionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommissionTransactionId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionTransactionId</code> property.
    *
    */
      @Column(name = "COMMISSION_TRANSACTION_ID" , nullable = false )
   @Id 
   public Long getCommissionTransactionId() {
      return commissionTransactionId;
   }

   /**
    * Sets the value of the <code>commissionTransactionId</code> property.
    *
    * @param commissionTransactionId the value for the <code>commissionTransactionId</code> property
    *    
		    */

   public void setCommissionTransactionId(Long commissionTransactionId) {
      this.commissionTransactionId = commissionTransactionId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the value of the <code>commAmount</code> property.
    *
    */
      @Column(name = "COMM_AMOUNT"  )
   public Double getCommAmount() {
      return commAmount;
   }

   /**
    * Sets the value of the <code>commAmount</code> property.
    *
    * @param commAmount the value for the <code>commAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setCommAmount(Double commAmount) {
      this.commAmount = commAmount;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCommissionTransactionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commissionTransactionId=" + getCommissionTransactionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commissionTransactionId";
			return primaryKeyFieldName;				
    }       
}
