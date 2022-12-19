package com.inov8.microbank.common.model.stakeholdermodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The StakehldBankInfoListViewModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="StakehldBankInfoListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "STAKEHLD_BANK_INFO_LIST_VIEW")
public class StakehldBankInfoListViewModel extends BasePersistableModel {
  



   private Long stakeholderBankInfoId;
   private Long bankId;
   private Long commissionStakeholderId;
   private String name;
   private String accountNo;
   private Long createdBy;
   private Long updatedBy;
   private Date createdOn;
   private Date updatedOn;
   private String bankName;
   private String commissionStakeholderName;
   private String stakeholderTypeName;
   private Boolean active;
   private Long customerAccountTypeId;

   /*added by atif hussain*/
   private Long productId;
   
   
   /**
    * Default constructor.
    */
   public StakehldBankInfoListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getStakeholderBankInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setStakeholderBankInfoId(primaryKey);
    }

   /**
    * Returns the value of the <code>stakeholderBankInfoId</code> property.
    *
    */
      @Column(name = "STAKEHOLDER_BANK_INFO_ID" , nullable = false )
   @Id 
   public Long getStakeholderBankInfoId() {
      return stakeholderBankInfoId;
   }

   /**
    * Sets the value of the <code>stakeholderBankInfoId</code> property.
    *
    * @param stakeholderBankInfoId the value for the <code>stakeholderBankInfoId</code> property
    *    
		    */

   public void setStakeholderBankInfoId(Long stakeholderBankInfoId) {
      this.stakeholderBankInfoId = stakeholderBankInfoId;
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
      @Column(name = "BANK_ID" , nullable = false )
   public Long getBankId() {
      return bankId;
   }

   /**
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBankId(Long bankId) {
      this.bankId = bankId;
   }

   /**
    * Returns the value of the <code>commissionStakeholderId</code> property.
    *
    */
      @Column(name = "COMMISSION_STAKEHOLDER_ID" , nullable = false )
   public Long getCommissionStakeholderId() {
      return commissionStakeholderId;
   }

   /**
    * Sets the value of the <code>commissionStakeholderId</code> property.
    *
    * @param commissionStakeholderId the value for the <code>commissionStakeholderId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCommissionStakeholderId(Long commissionStakeholderId) {
      this.commissionStakeholderId = commissionStakeholderId;
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
    * Returns the value of the <code>accountNo</code> property.
    *
    */
      @Column(name = "ACCOUNT_NO" , nullable = false , length=50 )
   public String getAccountNo() {
      return accountNo;
   }

   /**
    * Sets the value of the <code>accountNo</code> property.
    *
    * @param accountNo the value for the <code>accountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountNo(String accountNo) {
      this.accountNo = accountNo;
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
    * Returns the value of the <code>bankName</code> property.
    *
    */
      @Column(name = "BANK_NAME" , nullable = false , length=50 )
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
        checkBox += "_"+ getStakeholderBankInfoId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&stakeholderBankInfoId=" + getStakeholderBankInfoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "stakeholderBankInfoId";
			return primaryKeyFieldName;				
    }

	/**
	 * @return the accountTypeId
	 */
    @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID" , nullable = false , length=10)
	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}

	/**
	 * @param accountTypeId the accountTypeId to set
	 */
	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}

    @Column(name = "PRODUCT_ID" , nullable = false , length=10)
    public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
}
