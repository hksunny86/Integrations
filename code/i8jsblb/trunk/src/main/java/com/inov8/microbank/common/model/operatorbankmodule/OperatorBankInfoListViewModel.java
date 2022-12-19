package com.inov8.microbank.common.model.operatorbankmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The OperatorBankInfoListViewModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="OperatorBankInfoListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "OPERATOR_BANK_INFO_LIST_VIEW")
public class OperatorBankInfoListViewModel extends BasePersistableModel {
  



   private Long operatorBankInfoId;
   private String name;
   private String receivingAccountNo;
   private String payingAccountNo;
   private Long createdBy;
   private Long updatedBy;
   private Date createdOn;
   private Date updatedOn;
   private Long paymentModeId;
   private String merchantCategory;
   private Long operatorId;
   private Long bankId;
   private String bankName;
   private String operatorName;
   private String paymentModeName;

   /**
    * Default constructor.
    */
   public OperatorBankInfoListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getOperatorBankInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setOperatorBankInfoId(primaryKey);
    }

   /**
    * Returns the value of the <code>operatorBankInfoId</code> property.
    *
    */
      @Column(name = "OPERATOR_BANK_INFO_ID" , nullable = false )
   @Id 
   public Long getOperatorBankInfoId() {
      return operatorBankInfoId;
   }

   /**
    * Sets the value of the <code>operatorBankInfoId</code> property.
    *
    * @param operatorBankInfoId the value for the <code>operatorBankInfoId</code> property
    *    
		    */

   public void setOperatorBankInfoId(Long operatorBankInfoId) {
      this.operatorBankInfoId = operatorBankInfoId;
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
    * Returns the value of the <code>receivingAccountNo</code> property.
    *
    */
      @Column(name = "RECEIVING_ACCOUNT_NO" , nullable = false , length=50 )
   public String getReceivingAccountNo() {
      return receivingAccountNo;
   }

   /**
    * Sets the value of the <code>receivingAccountNo</code> property.
    *
    * @param receivingAccountNo the value for the <code>receivingAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setReceivingAccountNo(String receivingAccountNo) {
      this.receivingAccountNo = receivingAccountNo;
   }

   /**
    * Returns the value of the <code>payingAccountNo</code> property.
    *
    */
      @Column(name = "PAYING_ACCOUNT_NO" , nullable = false , length=50 )
   public String getPayingAccountNo() {
      return payingAccountNo;
   }

   /**
    * Sets the value of the <code>payingAccountNo</code> property.
    *
    * @param payingAccountNo the value for the <code>payingAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPayingAccountNo(String payingAccountNo) {
      this.payingAccountNo = payingAccountNo;
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
    * Returns the value of the <code>paymentModeId</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE_ID" , nullable = false )
   public Long getPaymentModeId() {
      return paymentModeId;
   }

   /**
    * Sets the value of the <code>paymentModeId</code> property.
    *
    * @param paymentModeId the value for the <code>paymentModeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setPaymentModeId(Long paymentModeId) {
      this.paymentModeId = paymentModeId;
   }

   /**
    * Returns the value of the <code>merchantCategory</code> property.
    *
    */
      @Column(name = "MERCHANT_CATEGORY" , nullable = false , length=50 )
   public String getMerchantCategory() {
      return merchantCategory;
   }

   /**
    * Sets the value of the <code>merchantCategory</code> property.
    *
    * @param merchantCategory the value for the <code>merchantCategory</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMerchantCategory(String merchantCategory) {
      this.merchantCategory = merchantCategory;
   }

   /**
    * Returns the value of the <code>operatorId</code> property.
    *
    */
      @Column(name = "OPERATOR_ID" , nullable = false )
   public Long getOperatorId() {
      return operatorId;
   }

   /**
    * Sets the value of the <code>operatorId</code> property.
    *
    * @param operatorId the value for the <code>operatorId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setOperatorId(Long operatorId) {
      this.operatorId = operatorId;
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
    * Returns the value of the <code>operatorName</code> property.
    *
    */
      @Column(name = "OPERATOR_NAME" , nullable = false , length=50 )
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
    * Returns the value of the <code>paymentModeName</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE_NAME" , nullable = false , length=50 )
   public String getPaymentModeName() {
      return paymentModeName;
   }

   /**
    * Sets the value of the <code>paymentModeName</code> property.
    *
    * @param paymentModeName the value for the <code>paymentModeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setPaymentModeName(String paymentModeName) {
      this.paymentModeName = paymentModeName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getOperatorBankInfoId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&operatorBankInfoId=" + getOperatorBankInfoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "operatorBankInfoId";
			return primaryKeyFieldName;				
    }       
}
