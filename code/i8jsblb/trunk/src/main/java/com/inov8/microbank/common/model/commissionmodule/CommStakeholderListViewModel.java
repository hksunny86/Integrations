package com.inov8.microbank.common.model.commissionmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommStakeholderListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommStakeholderListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMM_STAKEHOLDER_LIST_VIEW")
public class CommStakeholderListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -2980953386475986212L;
private Long commissionStakeholderId;
   private String commissionStakeholderName;
   private String commSthContactName;
   private String stakeholderTypeName;
   private String operatorName;
   private String bankName;
   private String distributorName;
   private String retailerName;
   private Boolean displayOnProductScreen;

   /**
    * Default constructor.
    */
   public CommStakeholderListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommissionStakeholderId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommissionStakeholderId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionStakeholderId</code> property.
    *
    */
      @Column(name = "COMMISSION_STAKEHOLDER_ID" , nullable = false )
   @Id 
   public Long getCommissionStakeholderId() {
      return commissionStakeholderId;
   }

   /**
    * Sets the value of the <code>commissionStakeholderId</code> property.
    *
    * @param commissionStakeholderId the value for the <code>commissionStakeholderId</code> property
    *    
		    */

   public void setCommissionStakeholderId(Long commissionStakeholderId) {
      this.commissionStakeholderId = commissionStakeholderId;
   }

   /**
    * Returns the value of the <code>commissionStakeholderName</code> property.
    *
    */
      @Column(name = "COMMISSION_STAKEHOLDER_NAME" , nullable = false , length=50 )
   public String getCommissionStakeholderName() {
      return commissionStakeholderName;
   }

   /**
    * Sets the value of the <code>commissionStakeholderName</code> property.
    *
    * @param commissionStakeholderName the value for the <code>commissionStakeholderName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setCommissionStakeholderName(String commissionStakeholderName) {
      this.commissionStakeholderName = commissionStakeholderName;
   }

   /**
    * Returns the value of the <code>commSthContactName</code> property.
    *
    */
      @Column(name = "COMM_STH_CONTACT_NAME" , nullable = false , length=50 )
   public String getCommSthContactName() {
      return commSthContactName;
   }

   /**
    * Sets the value of the <code>commSthContactName</code> property.
    *
    * @param commSthContactName the value for the <code>commSthContactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setCommSthContactName(String commSthContactName) {
      this.commSthContactName = commSthContactName;
   }

   /**
    * Returns the value of the <code>stakeholderTypeName</code> property.
    *
    */
      @Column(name = "STAKEHOLDER_TYPE_NAME" , nullable = false , length=50 )
   public String getStakeholderTypeName() {
      return stakeholderTypeName;
   }

   /**
    * Sets the value of the <code>stakeholderTypeName</code> property.
    *
    * @param stakeholderTypeName the value for the <code>stakeholderTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setStakeholderTypeName(String stakeholderTypeName) {
      this.stakeholderTypeName = stakeholderTypeName;
   }

   /**
    * Returns the value of the <code>operatorName</code> property.
    *
    */
      @Column(name = "OPERATOR_NAME"  , length=50 )
   public String getOperatorName() {
      return operatorName;
   }

   /**
    * Sets the value of the <code>operatorName</code> property.
    *
    * @param operatorName the value for the <code>operatorName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setOperatorName(String operatorName) {
      this.operatorName = operatorName;
   }

   /**
    * Returns the value of the <code>bankName</code> property.
    *
    */
      @Column(name = "BANK_NAME"  , length=50 )
   public String getBankName() {
      return bankName;
   }

   /**
    * Sets the value of the <code>bankName</code> property.
    *
    * @param bankName the value for the <code>bankName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setBankName(String bankName) {
      this.bankName = bankName;
   }

   /**
    * Returns the value of the <code>distributorName</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_NAME"  , length=50 )
   public String getDistributorName() {
      return distributorName;
   }

   /**
    * Sets the value of the <code>distributorName</code> property.
    *
    * @param distributorName the value for the <code>distributorName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setDistributorName(String distributorName) {
      this.distributorName = distributorName;
   }

   /**
    * Returns the value of the <code>retailerName</code> property.
    *
    */
      @Column(name = "RETAILER_NAME"  , length=50 )
   public String getRetailerName() {
      return retailerName;
   }

   /**
    * Sets the value of the <code>retailerName</code> property.
    *
    * @param retailerName the value for the <code>retailerName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setRetailerName(String retailerName) {
      this.retailerName = retailerName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCommissionStakeholderId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commissionStakeholderId=" + getCommissionStakeholderId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commissionStakeholderId";
			return primaryKeyFieldName;				
    }

   @Column(name = "DISPLAY_ON_PRODUCT_SCREEN")
   public Boolean getDisplayOnProductScreen() {
      return displayOnProductScreen;
   }

   public void setDisplayOnProductScreen(Boolean displayOnProductScreen) {
      this.displayOnProductScreen = displayOnProductScreen;
   }

}
