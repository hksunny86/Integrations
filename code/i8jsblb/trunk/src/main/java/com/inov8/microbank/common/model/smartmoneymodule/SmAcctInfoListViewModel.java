package com.inov8.microbank.common.model.smartmoneymodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The SmAcctInfoListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2007/11/20 19:29:08 $
 *
 *
 * @spring.bean name="SmAcctInfoListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SM_ACCT_INFO_LIST_VIEW")
public class SmAcctInfoListViewModel extends BasePersistableModel implements Serializable {
  



   private String key;
   private String name;
   private Boolean defAccount;
   private Boolean changePinRequired;
   private Long customerId;
   private Long retailerContactId;
   private Long distributorContactId;
   private Long smartMoneyAccountId;
   private String mod;
   private Long veriflyId;
   private Long financialIntegrationId;
   private Boolean isCvvRequired;
   private Boolean isMpinRequired;
   private Boolean isTpinRequired;
   private Long bankId;
   private String bankName;
   private Boolean pinLevel;
   private Boolean isBank;
   private String accountId;

   /**
    * Default constructor.
    */
   public SmAcctInfoListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSmartMoneyAccountId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSmartMoneyAccountId(primaryKey);
    }

   /**
    * Returns the value of the <code>key</code> property.
    *
    */
      @Column(name = "KEY"  , length=4000 )
   public String getKey() {
      return key;
   }

   /**
    * Sets the value of the <code>key</code> property.
    *
    * @param key the value for the <code>key</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="4000"
    */

   public void setKey(String key) {
      this.key = key;
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
    * Returns the value of the <code>defAccount</code> property.
    *
    */
      @Column(name = "IS_DEF_ACCOUNT" , nullable = false )
   public Boolean getDefAccount() {
      return defAccount;
   }

   /**
    * Sets the value of the <code>defAccount</code> property.
    *
    * @param defAccount the value for the <code>defAccount</code> property
    *    
		    */

   public void setDefAccount(Boolean defAccount) {
      this.defAccount = defAccount;
   }

   /**
    * Returns the value of the <code>changePinRequired</code> property.
    *
    */
      @Column(name = "IS_CHANGE_PIN_REQUIRED" , nullable = false )
   public Boolean getChangePinRequired() {
      return changePinRequired;
   }

   /**
    * Sets the value of the <code>changePinRequired</code> property.
    *
    * @param changePinRequired the value for the <code>changePinRequired</code> property
    *    
		    */

   public void setChangePinRequired(Boolean changePinRequired) {
      this.changePinRequired = changePinRequired;
   }

   /**
    * Returns the value of the <code>customerId</code> property.
    *
    */
      @Column(name = "CUSTOMER_ID" , nullable = false )
   public Long getCustomerId() {
      return customerId;
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
   }
   
   /**
    * Returns the value of the <code>customerId</code> property.
    *
    */
      @Column(name = "RETAILER_CONTACT_ID" , nullable = false )
   public Long getRetailerContactId() {
      return retailerContactId;
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setRetailerContactId(Long retailerContId) {
      this.retailerContactId = retailerContId;
   }
   
   
   /**
    * Returns the value of the <code>customerId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_CONTACT_ID" , nullable = false )
   public Long getDistributorContactId() {
      return distributorContactId;
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDistributorContactId(Long distributorContactId) {
      this.distributorContactId = distributorContactId;
   }

   /**
    * Returns the value of the <code>smartMoneyAccountId</code> property.
    *
    */
      @Column(name = "SMART_MONEY_ACCOUNT_ID" , nullable = false )
   @Id 
   public Long getSmartMoneyAccountId() {
      return smartMoneyAccountId;
   }

   /**
    * Sets the value of the <code>smartMoneyAccountId</code> property.
    *
    * @param smartMoneyAccountId the value for the <code>smartMoneyAccountId</code> property
    *    
		    */

   public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
      this.smartMoneyAccountId = smartMoneyAccountId;
   }

   /**
    * Returns the value of the <code>mod</code> property.
    *
    */
      @Column(name = "MOD"  , length=50 )
   public String getMod() {
      return mod;
   }

   /**
    * Sets the value of the <code>mod</code> property.
    *
    * @param mod the value for the <code>mod</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMod(String mod) {
      this.mod = mod;
   }

   /**
    * Returns the value of the <code>veriflyId</code> property.
    *
    */
      @Column(name = "VERIFLY_ID"  )
   public Long getVeriflyId() {
      return veriflyId;
   }

   /**
    * Sets the value of the <code>veriflyId</code> property.
    *
    * @param veriflyId the value for the <code>veriflyId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setVeriflyId(Long veriflyId) {
      this.veriflyId = veriflyId;
   }

   /**
    * Returns the value of the <code>financialIntegrationId</code> property.
    *
    */
      @Column(name = "FINANCIAL_INTEGRATION_ID" , nullable = false )
   public Long getFinancialIntegrationId() {
      return financialIntegrationId;
   }

   /**
    * Sets the value of the <code>financialIntegrationId</code> property.
    *
    * @param financialIntegrationId the value for the <code>financialIntegrationId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFinancialIntegrationId(Long financialIntegrationId) {
      this.financialIntegrationId = financialIntegrationId;
   }

   /**
    * Returns the value of the <code>isCvvRequired</code> property.
    *
    */
      @Column(name = "IS_CVV_REQUIRED"  )
   public Boolean getIsCvvRequired() {
      return isCvvRequired;
   }

   /**
    * Sets the value of the <code>isCvvRequired</code> property.
    *
    * @param isCvvRequired the value for the <code>isCvvRequired</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setIsCvvRequired(Boolean isCvvRequired) {
      this.isCvvRequired = isCvvRequired;
   }

   /**
    * Returns the value of the <code>isMpinRequired</code> property.
    *
    */
      @Column(name = "IS_MPIN_REQUIRED"  )
   public Boolean getIsMpinRequired() {
      return isMpinRequired;
   }

   /**
    * Sets the value of the <code>isMpinRequired</code> property.
    *
    * @param isMpinRequired the value for the <code>isMpinRequired</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setIsMpinRequired(Boolean isMpinRequired) {
      this.isMpinRequired = isMpinRequired;
   }

   /**
    * Returns the value of the <code>isTpinRequired</code> property.
    *
    */
      @Column(name = "IS_TPIN_REQUIRED"  )
   public Boolean getIsTpinRequired() {
      return isTpinRequired;
   }

   /**
    * Sets the value of the <code>isTpinRequired</code> property.
    *
    * @param isTpinRequired the value for the <code>isTpinRequired</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setIsTpinRequired(Boolean isTpinRequired) {
      this.isTpinRequired = isTpinRequired;
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
    * Returns the value of the <code>pinLevel</code> property.
    *
    */
      @Column(name = "PIN_LEVEL"  )
   public Boolean getPinLevel() {
      return pinLevel;
   }

   /**
    * Sets the value of the <code>pinLevel</code> property.
    *
    * @param pinLevel the value for the <code>pinLevel</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setPinLevel(Boolean pinLevel) {
      this.pinLevel = pinLevel;
   }

   /**
    * Returns the value of the <code>isBank</code> property.
    *
    */
      @Column(name = "IS_BANK"  )
   public Boolean getIsBank() {
      return isBank;
   }

   /**
    * Sets the value of the <code>isBank</code> property.
    *
    * @param isBank the value for the <code>isBank</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setIsBank(Boolean isBank) {
      this.isBank = isBank;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSmartMoneyAccountId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&smartMoneyAccountId=" + getSmartMoneyAccountId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "smartMoneyAccountId";
			return primaryKeyFieldName;				
    }

    @javax.persistence.Transient
	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}       
}
