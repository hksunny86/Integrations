package com.inov8.microbank.common.model.financialintegrationmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The FinancialIntgListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2007/11/06 19:29:08 $
 *
 *
 * @spring.bean name="FinancialIntgListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "FINANCIAL_INTG_LIST_VIEW")
public class FinancialIntgListViewModel extends BasePersistableModel implements Serializable {
  



   private Long smartMoneyAccountId;
   private String className;
   private Long financialIntegrationId;
   private Long bankId;
   private Long customerId;
   private String name;

   /**
    * Default constructor.
    */
   public FinancialIntgListViewModel() {
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
    * Returns the value of the <code>className</code> property.
    *
    */
      @Column(name = "CLASS_NAME" , nullable = false , length=250 )
   public String getClassName() {
      return className;
   }

   /**
    * Sets the value of the <code>className</code> property.
    *
    * @param className the value for the <code>className</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setClassName(String className) {
      this.className = className;
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
}
