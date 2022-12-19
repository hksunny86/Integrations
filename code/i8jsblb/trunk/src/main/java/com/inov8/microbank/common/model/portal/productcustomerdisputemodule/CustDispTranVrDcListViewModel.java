package com.inov8.microbank.common.model.portal.productcustomerdisputemodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CustDispTranVrDcListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CustDispTranVrDcListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CUST_DISP_TRAN_VR_DC_LIST_VIEW")
public class CustDispTranVrDcListViewModel extends BasePersistableModel implements Serializable {
  



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
   private Long transactionDetailId;
   private String transactionCode;
   private Long productId;
   private String productName;
   private Long supplierId;
   private String supplierName;
   private Long paymentModeId;
   private String paymentMode;
   private Long smartMoneyAccountId;
   private String accountNick;
   private Long issueTypeStatusId;
   private Long issueId;
   private String issueCode;
   private Long createdBy;
   private String issueStatusName;
   private String rifc;
   private String invalid;
   private String i8;
   private String currentMobileNo;
   private String processingStatusName;
   private Long bankId;
   private String bankName;
   private String bankAccountNo;
   private String bankAccountNoLastFive;

   /**
    * Default constructor.
    */
   public CustDispTranVrDcListViewModel() {
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
    * Returns the value of the <code>paymentMode</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE"  , length=50 )
   public String getPaymentMode() {
      return paymentMode;
   }

   /**
    * Sets the value of the <code>paymentMode</code> property.
    *
    * @param paymentMode the value for the <code>paymentMode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPaymentMode(String paymentMode) {
      this.paymentMode = paymentMode;
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
    * Returns the value of the <code>accountNick</code> property.
    *
    */
      @Column(name = "ACCOUNT_NICK"  , length=50 )
   public String getAccountNick() {
      return accountNick;
   }

   /**
    * Sets the value of the <code>accountNick</code> property.
    *
    * @param accountNick the value for the <code>accountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountNick(String accountNick) {
      this.accountNick = accountNick;
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
    * Returns the value of the <code>rifc</code> property.
    *
    */
      @Column(name = "RIFC"  , length=3 )
   public String getRifc() {
      return rifc;
   }

   /**
    * Sets the value of the <code>rifc</code> property.
    *
    * @param rifc the value for the <code>rifc</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="3"
    */

   public void setRifc(String rifc) {
      this.rifc = rifc;
   }

   /**
    * Returns the value of the <code>invalid</code> property.
    *
    */
      @Column(name = "INVALID"  , length=3 )
   public String getInvalid() {
      return invalid;
   }

   /**
    * Sets the value of the <code>invalid</code> property.
    *
    * @param invalid the value for the <code>invalid</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="3"
    */

   public void setInvalid(String invalid) {
      this.invalid = invalid;
   }

   /**
    * Returns the value of the <code>i8</code> property.
    *
    */
      @Column(name = "I8"  , length=3 )
   public String getI8() {
      return i8;
   }

   /**
    * Sets the value of the <code>i8</code> property.
    *
    * @param i8 the value for the <code>i8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="3"
    */

   public void setI8(String i8) {
      this.i8 = i8;
   }

   /**
    * Returns the value of the <code>currentMobileNo</code> property.
    *
    */
      @Column(name = "CURRENT_MOBILE_NO"  , length=50 )
   public String getCurrentMobileNo() {
      return currentMobileNo;
   }

   /**
    * Sets the value of the <code>currentMobileNo</code> property.
    *
    * @param currentMobileNo the value for the <code>currentMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCurrentMobileNo(String currentMobileNo) {
      this.currentMobileNo = currentMobileNo;
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
    * Returns the value of the <code>bankId</code> property.
    *
    */
      @Column(name = "BANK_ID"  )
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
    * Returns the value of the <code>bankAccountNo</code> property.
    *
    */
      @Column(name = "BANK_ACCOUNT_NO"  , length=20 )
   public String getBankAccountNo() {
      return bankAccountNo;
   }

   /**
    * Sets the value of the <code>bankAccountNo</code> property.
    *
    * @param bankAccountNo the value for the <code>bankAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="20"
    */

   public void setBankAccountNo(String bankAccountNo) {
      this.bankAccountNo = bankAccountNo;
   }

   /**
    * Returns the value of the <code>bankAccountNoLastFive</code> property.
    *
    */
      @Column(name = "BANK_ACCOUNT_NO_LAST_FIVE"  , length=5 )
   public String getBankAccountNoLastFive() {
      return bankAccountNoLastFive;
   }

   /**
    * Sets the value of the <code>bankAccountNoLastFive</code> property.
    *
    * @param bankAccountNoLastFive the value for the <code>bankAccountNoLastFive</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="5"
    */

   public void setBankAccountNoLastFive(String bankAccountNoLastFive) {
      this.bankAccountNoLastFive = bankAccountNoLastFive;
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
