package com.inov8.microbank.common.model.portal.escalateinov8module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The EscalateInov8ListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="EscalateInov8ListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ESCALATE_INOV8_LIST_VIEW")
public class EscalateInov8ListViewModel extends BasePersistableModel {
  



   private Long transactionId;
   private Long transactionCodeId;
   private String mfsId;
   private String authorizationCode;
   private Double amount;
   private Double totalAmount;
   private Double totalCommissionAmount;
   private Double serviceCharges;
   private Double isIssue;
   private Date createdOn;
   private Long transactionTypeId;
   private String notificationMobileNo;
   private String saleMobileNo;
   private String customerMobileNo;
   private Long transactionDetailId;
   private String transactionCode;
   private Long productId;
   private String productName;
   private Long supplierId;
   private Long serviceId;
   private Long paymentModeId;
   private Long smartMoneyAccountId;
   private Long issueTypeStatusId;
   private Long issueId;
   private String issueCode;
   private Long createdBy;
   private String issueStatusName;
   private Long issueTypeId;
   private String processingStatusName;

   /**
    * Default constructor.
    */
   public EscalateInov8ListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getIssueId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setIssueId(primaryKey);
    }

   /**
    * Returns the value of the <code>transactionId</code> property.
    *
    */
      @Column(name = "TRANSACTION_ID"  )
   public Long getTransactionId() {
      return transactionId;
   }

   /**
    * Sets the value of the <code>transactionId</code> property.
    *
    * @param transactionId the value for the <code>transactionId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setTransactionId(Long transactionId) {
      this.transactionId = transactionId;
   }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE_ID"  )
   public Long getTransactionCodeId() {
      return transactionCodeId;
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setTransactionCodeId(Long transactionCodeId) {
      this.transactionCodeId = transactionCodeId;
   }

   /**
    * Returns the value of the <code>mfsId</code> property.
    *
    */
      @Column(name = "MFS_ID"  , length=50 )
   public String getMfsId() {
      return mfsId;
   }

   /**
    * Sets the value of the <code>mfsId</code> property.
    *
    * @param mfsId the value for the <code>mfsId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMfsId(String mfsId) {
      this.mfsId = mfsId;
   }

   /**
    * Returns the value of the <code>authorizationCode</code> property.
    *
    */
      @Column(name = "AUTHORIZATION_CODE"  , length=50 )
   public String getAuthorizationCode() {
      return authorizationCode;
   }

   /**
    * Sets the value of the <code>authorizationCode</code> property.
    *
    * @param authorizationCode the value for the <code>authorizationCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAuthorizationCode(String authorizationCode) {
      this.authorizationCode = authorizationCode;
   }

   /**
    * Returns the value of the <code>amount</code> property.
    *
    */
      @Column(name = "AMOUNT"  )
   public Double getAmount() {
      return amount;
   }

   /**
    * Sets the value of the <code>amount</code> property.
    *
    * @param amount the value for the <code>amount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setAmount(Double amount) {
      this.amount = amount;
   }

   /**
    * Returns the value of the <code>totalAmount</code> property.
    *
    */
      @Column(name = "TOTAL_AMOUNT"  )
   public Double getTotalAmount() {
      return totalAmount;
   }

   /**
    * Sets the value of the <code>totalAmount</code> property.
    *
    * @param totalAmount the value for the <code>totalAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTotalAmount(Double totalAmount) {
      this.totalAmount = totalAmount;
   }

   /**
    * Returns the value of the <code>totalCommissionAmount</code> property.
    *
    */
      @Column(name = "TOTAL_COMMISSION_AMOUNT"  )
   public Double getTotalCommissionAmount() {
      return totalCommissionAmount;
   }

   /**
    * Sets the value of the <code>totalCommissionAmount</code> property.
    *
    * @param totalCommissionAmount the value for the <code>totalCommissionAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTotalCommissionAmount(Double totalCommissionAmount) {
      this.totalCommissionAmount = totalCommissionAmount;
   }

   /**
    * Returns the value of the <code>serviceCharges</code> property.
    *
    */
      @Column(name = "SERVICE_CHARGES"  )
   public Double getServiceCharges() {
      return serviceCharges;
   }

   /**
    * Sets the value of the <code>serviceCharges</code> property.
    *
    * @param serviceCharges the value for the <code>serviceCharges</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setServiceCharges(Double serviceCharges) {
      this.serviceCharges = serviceCharges;
   }

   /**
    * Returns the value of the <code>isIssue</code> property.
    *
    */
      @Column(name = "IS_ISSUE"  )
   public Double getIsIssue() {
      return isIssue;
   }

   /**
    * Sets the value of the <code>isIssue</code> property.
    *
    * @param isIssue the value for the <code>isIssue</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setIsIssue(Double isIssue) {
      this.isIssue = isIssue;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON"  )
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
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_TYPE_ID"  )
   public Long getTransactionTypeId() {
      return transactionTypeId;
   }

   /**
    * Sets the value of the <code>transactionTypeId</code> property.
    *
    * @param transactionTypeId the value for the <code>transactionTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setTransactionTypeId(Long transactionTypeId) {
      this.transactionTypeId = transactionTypeId;
   }

   /**
    * Returns the value of the <code>notificationMobileNo</code> property.
    *
    */
      @Column(name = "NOTIFICATION_MOBILE_NO"  , length=13 )
   public String getNotificationMobileNo() {
      return notificationMobileNo;
   }

   /**
    * Sets the value of the <code>notificationMobileNo</code> property.
    *
    * @param notificationMobileNo the value for the <code>notificationMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="13"
    */

   public void setNotificationMobileNo(String notificationMobileNo) {
      this.notificationMobileNo = notificationMobileNo;
   }

   /**
    * Returns the value of the <code>saleMobileNo</code> property.
    *
    */
      @Column(name = "SALE_MOBILE_NO"  , length=50 )
   public String getSaleMobileNo() {
      return saleMobileNo;
   }

   /**
    * Sets the value of the <code>saleMobileNo</code> property.
    *
    * @param saleMobileNo the value for the <code>saleMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setSaleMobileNo(String saleMobileNo) {
      this.saleMobileNo = saleMobileNo;
   }

   /**
    * Returns the value of the <code>customerMobileNo</code> property.
    *
    */
      @Column(name = "CUSTOMER_MOBILE_NO"  , length=50 )
   public String getCustomerMobileNo() {
      return customerMobileNo;
   }

   /**
    * Sets the value of the <code>customerMobileNo</code> property.
    *
    * @param customerMobileNo the value for the <code>customerMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustomerMobileNo(String customerMobileNo) {
      this.customerMobileNo = customerMobileNo;
   }

   /**
    * Returns the value of the <code>transactionDetailId</code> property.
    *
    */
      @Column(name = "TRANSACTION_DETAIL_ID"  )
   public Long getTransactionDetailId() {
      return transactionDetailId;
   }

   /**
    * Sets the value of the <code>transactionDetailId</code> property.
    *
    * @param transactionDetailId the value for the <code>transactionDetailId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setTransactionDetailId(Long transactionDetailId) {
      this.transactionDetailId = transactionDetailId;
   }

   /**
    * Returns the value of the <code>transactionCode</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE"  , length=50 )
   public String getTransactionCode() {
      return transactionCode;
   }

   /**
    * Sets the value of the <code>transactionCode</code> property.
    *
    * @param transactionCode the value for the <code>transactionCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setTransactionCode(String transactionCode) {
      this.transactionCode = transactionCode;
   }

   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
      @Column(name = "PRODUCT_ID"  )
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
    * Returns the value of the <code>supplierId</code> property.
    *
    */
      @Column(name = "SUPPLIER_ID"  )
   public Long getSupplierId() {
      return supplierId;
   }

   /**
    * Sets the value of the <code>supplierId</code> property.
    *
    * @param supplierId the value for the <code>supplierId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setSupplierId(Long supplierId) {
      this.supplierId = supplierId;
   }

   /**
    * Returns the value of the <code>serviceId</code> property.
    *
    */
      @Column(name = "SERVICE_ID"  )
   public Long getServiceId() {
      return serviceId;
   }

   /**
    * Sets the value of the <code>serviceId</code> property.
    *
    * @param serviceId the value for the <code>serviceId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setServiceId(Long serviceId) {
      this.serviceId = serviceId;
   }

   /**
    * Returns the value of the <code>paymentModeId</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE_ID"  )
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
    * Returns the value of the <code>smartMoneyAccountId</code> property.
    *
    */
      @Column(name = "SMART_MONEY_ACCOUNT_ID"  )
   public Long getSmartMoneyAccountId() {
      return smartMoneyAccountId;
   }

   /**
    * Sets the value of the <code>smartMoneyAccountId</code> property.
    *
    * @param smartMoneyAccountId the value for the <code>smartMoneyAccountId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
      this.smartMoneyAccountId = smartMoneyAccountId;
   }

   /**
    * Returns the value of the <code>issueTypeStatusId</code> property.
    *
    */
      @Column(name = "ISSUE_TYPE_STATUS_ID"  )
   public Long getIssueTypeStatusId() {
      return issueTypeStatusId;
   }

   /**
    * Sets the value of the <code>issueTypeStatusId</code> property.
    *
    * @param issueTypeStatusId the value for the <code>issueTypeStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setIssueTypeStatusId(Long issueTypeStatusId) {
      this.issueTypeStatusId = issueTypeStatusId;
   }

   /**
    * Returns the value of the <code>issueId</code> property.
    *
    */
      @Column(name = "ISSUE_ID"  )
   @Id 
   public Long getIssueId() {
      return issueId;
   }

   /**
    * Sets the value of the <code>issueId</code> property.
    *
    * @param issueId the value for the <code>issueId</code> property
    *    
		    */

   public void setIssueId(Long issueId) {
      this.issueId = issueId;
   }

   /**
    * Returns the value of the <code>issueCode</code> property.
    *
    */
      @Column(name = "ISSUE_CODE"  , length=50 )
   public String getIssueCode() {
      return issueCode;
   }

   /**
    * Sets the value of the <code>issueCode</code> property.
    *
    * @param issueCode the value for the <code>issueCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setIssueCode(String issueCode) {
      this.issueCode = issueCode;
   }

   /**
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY"  )
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
    * Returns the value of the <code>issueStatusName</code> property.
    *
    */
      @Column(name = "ISSUE_STATUS_NAME"  , length=50 )
   public String getIssueStatusName() {
      return issueStatusName;
   }

   /**
    * Sets the value of the <code>issueStatusName</code> property.
    *
    * @param issueStatusName the value for the <code>issueStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setIssueStatusName(String issueStatusName) {
      this.issueStatusName = issueStatusName;
   }

   /**
    * Returns the value of the <code>issueTypeId</code> property.
    *
    */
      @Column(name = "ISSUE_TYPE_ID"  )
   public Long getIssueTypeId() {
      return issueTypeId;
   }

   /**
    * Sets the value of the <code>issueTypeId</code> property.
    *
    * @param issueTypeId the value for the <code>issueTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setIssueTypeId(Long issueTypeId) {
      this.issueTypeId = issueTypeId;
   }

   /**
    * Returns the value of the <code>processingStatusName</code> property.
    *
    */
      @Column(name = "PROCESSING_STATUS_NAME"  , length=50 )
   public String getProcessingStatusName() {
      return processingStatusName;
   }

   /**
    * Sets the value of the <code>processingStatusName</code> property.
    *
    * @param processingStatusName the value for the <code>processingStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setProcessingStatusName(String processingStatusName) {
      this.processingStatusName = processingStatusName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getIssueId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&issueId=" + getIssueId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "issueId";
			return primaryKeyFieldName;				
    }       
}
