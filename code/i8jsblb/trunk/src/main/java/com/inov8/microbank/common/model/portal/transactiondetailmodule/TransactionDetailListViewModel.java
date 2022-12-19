package com.inov8.microbank.common.model.portal.transactiondetailmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ProductUtils;
import com.inov8.microbank.common.util.ResendSmsButtonLabelConstants;
import com.inov8.microbank.common.util.ResendSmsButtonLabelEnum;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;

/**
 * The TransactionDetailListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionDetailListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANSACTION_DETAIL_LIST_VIEW")
public class TransactionDetailListViewModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = -7493344636214587001L;

    private Long transactionId;
   private Long transactionCodeId;
   private String mfsId;
   private String authorizationCode;
   private Double amount;
   private Double totalAmount;
   private Double totalCommissionAmount;
   private Double serviceChargesExclusive;
   private Double serviceChargesInclusive;
   private Boolean issue;
   private Date createdOn;
   private Long transactionTypeId;
   private String notificationMobileNo;
   private String saleMobileNo;
   private String bankAccountNo;
   private Long deviceTypeId;
   private String deviceTypeName;
   private String bankAccountNoLastFive;
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
   private Long bankId;
   private String veriflyStatus;
   private Double bankCommission;
   private Long supProcessingStatusId;
   private String processingStatusName;
   private Double commSharing;
   private Double serviceFeeSharing;
   private String recipientAccountNick;
   private String recipientAccountNumber;
   private String recipientId;
   private String agent1Id;
   private String agent2Id;
   private String senderCnic;
   private String recipientCnic;
   private String cashDepositorCnic;
/**
    * Default constructor.
    */
   public TransactionDetailListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionId(primaryKey);
    }

   /**
    * Returns the value of the <code>transactionId</code> property.
    *
    */
      @Column(name = "TRANSACTION_ID" , nullable = false )
   @Id 
   public Long getTransactionId() {
      return transactionId;
   }

   /**
    * Sets the value of the <code>transactionId</code> property.
    *
    * @param transactionId the value for the <code>transactionId</code> property
    *    
		    */

   public void setTransactionId(Long transactionId) {
      this.transactionId = transactionId;
   }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE_ID" , nullable = false )
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
      @Column(name = "AMOUNT" , nullable = false )
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
      @Column(name = "TOTAL_AMOUNT" , nullable = false )
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
      @Column(name = "TOTAL_COMMISSION_AMOUNT" , nullable = false )
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
    * Returns the value of the <code>serviceChargesExclusive</code> property.
    *
    */
      @Column(name = "SERVICE_CHARGES_EXCLUSIVE"  )
   public Double getServiceChargesExclusive() {
      return serviceChargesExclusive;
   }

   /**
    * Sets the value of the <code>serviceChargesExclusive</code> property.
    *
    * @param serviceCharges the value for the <code>serviceChargesExclusive</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setServiceChargesExclusive(Double serviceChargesExclusive) {
      this.serviceChargesExclusive = serviceChargesExclusive;
   }

   /**
    * Returns the value of the <code>serviceChargesInclusive</code> property.
    *
    */
      @Column(name = "SERVICE_CHARGES_INCLUSIVE"  )
   public Double getServiceChargesInclusive() {
      return serviceChargesInclusive;
   }

   /**
    * Sets the value of the <code>serviceChargesInclusive</code> property.
    *
    * @param serviceCharges the value for the <code>serviceChargesInclusive</code> property
    *    
    */

   public void setServiceChargesInclusive(Double serviceChargesInclusive) {
      this.serviceChargesInclusive = serviceChargesInclusive;
   }

   /**
    * Returns the value of the <code>issue</code> property.
    *
    */
      @Column(name = "IS_ISSUE"  )
   public Boolean getIssue() {
      return issue;
   }

   /**
    * Sets the value of the <code>issue</code> property.
    *
    * @param issue the value for the <code>issue</code> property
    *    
		    */

   public void setIssue(Boolean issue) {
      this.issue = issue;
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
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_TYPE_ID" , nullable = false )
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
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_ID" , nullable = false )
   public Long getDeviceTypeId() {
      return deviceTypeId;
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDeviceTypeId(Long deviceTypeId) {
      this.deviceTypeId = deviceTypeId;
   }

   /**
    * Returns the value of the <code>deviceTypeName</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_NAME" , nullable = false , length=50 )
   public String getDeviceTypeName() {
      return deviceTypeName;
   }

   /**
    * Sets the value of the <code>deviceTypeName</code> property.
    *
    * @param deviceTypeName the value for the <code>deviceTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setDeviceTypeName(String deviceTypeName) {
      this.deviceTypeName = deviceTypeName;
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
    * Returns the value of the <code>transactionDetailId</code> property.
    *
    */
      @Column(name = "TRANSACTION_DETAIL_ID" , nullable = false )
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
      @Column(name = "TRANSACTION_CODE" , nullable = false , length=50 )
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
    * Returns the value of the <code>productName</code> property.
    *
    */
      @Column(name = "PRODUCT_NAME" , nullable = false , length=50 )
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
      @Column(name = "SUPPLIER_ID" , nullable = false )
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
      @Column(name = "SUPPLIER_NAME" , nullable = false , length=50 )
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
    * Returns the value of the <code>paymentMode</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE" , nullable = false , length=50 )
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
    * Returns the value of the <code>veriflyStatus</code> property.
    *
    */
      @Column(name = "VERIFLY_STATUS"  , length=2 )
   public String getVeriflyStatus() {
      return veriflyStatus;
   }

   /**
    * Sets the value of the <code>veriflyStatus</code> property.
    *
    * @param veriflyStatus the value for the <code>veriflyStatus</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="2"
    */

   public void setVeriflyStatus(String veriflyStatus) {
      this.veriflyStatus = veriflyStatus;
   }

   /**
    * Returns the value of the <code>bankCommission</code> property.
    *
    */
      @Column(name = "BANK_COMMISSION"  )
   public Double getBankCommission() {
      return bankCommission;
   }

   /**
    * Sets the value of the <code>bankCommission</code> property.
    *
    * @param bankCommission the value for the <code>bankCommission</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBankCommission(Double bankCommission) {
      this.bankCommission = bankCommission;
   }

   @Column(name="SUP_PROCESSING_STATUS_ID")
   public Long getSupProcessingStatusId() {
	return supProcessingStatusId;
   }

   public void setSupProcessingStatusId(Long supProcessingStatusId) {
	   this.supProcessingStatusId = supProcessingStatusId;
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
    * Returns the value of the <code>commSharing</code> property.
    *
    */
      @Column(name = "COMM_SHARING"  )
   public Double getCommSharing() {
      return commSharing;
   }

   /**
    * Sets the value of the <code>commSharing</code> property.
    *
    * @param commSharing the value for the <code>commSharing</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setCommSharing(Double commSharing) {
      this.commSharing = commSharing;
   }

   /**
    * Returns the value of the <code>serviceFeeSharing</code> property.
    *
    */
      @Column(name = "SERVICE_FEE_SHARING"  )
   public Double getServiceFeeSharing() {
      return serviceFeeSharing;
   }

   /**
    * Sets the value of the <code>serviceFeeSharing</code> property.
    *
    * @param serviceFeeSharing the value for the <code>serviceFeeSharing</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setServiceFeeSharing(Double serviceFeeSharing) {
      this.serviceFeeSharing = serviceFeeSharing;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionId=" + getTransactionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionId";
			return primaryKeyFieldName;				
    }       


    @Column(name = "RECIPIENT_ACCOUNT_NICK")
    public String getRecipientAccountNick() {
 	return recipientAccountNick;
 	}
 	
 	public void setRecipientAccountNick(String recipientAccountNick) {
 		this.recipientAccountNick = recipientAccountNick;
 	}
 	
 	@Column(name = "RECIPIENT_ACCOUNT_NO")	
 	public String getRecipientAccountNumber() {
 		return recipientAccountNumber;
 	}
 	
 	public void setRecipientAccountNumber(String recipientAccountNumber) {
 		this.recipientAccountNumber = recipientAccountNumber;
 	}

    
 	@Column(name = "RECIPIENT_MFS_ID")	
    public String getRecipientId() {
 	return recipientId;
	 }
	
	 public void setRecipientId(String recipientId) {
	 	this.recipientId = recipientId;
	 }

	 @Column(name = "AGENT1_ID", length = 50)
     public String getAgent1Id() {
         return this.agent1Id;
     }

     public void setAgent1Id(String agent1Id) {
         this.agent1Id = agent1Id;
     }

     @Column(name = "AGENT2_ID", length = 50)
     public String getAgent2Id() {
         return this.agent2Id;
     }

     public void setAgent2Id(String agent2Id) {
         this.agent2Id = agent2Id;
     }

	    @Column( name = "SENDER_CNIC" )
	    public String getSenderCnic()
	    {
	        return senderCnic;
	    }

	    public void setSenderCnic( String senderCnic )
	    {
	        this.senderCnic = senderCnic;
	    }

	    @Column( name = "RECIPIENT_CNIC" )
	    public String getRecipientCnic()
	    {
	        return recipientCnic;
	    }

	    public void setRecipientCnic( String recipientCnic )
	    {
	        this.recipientCnic = recipientCnic;
	    }

	    @Column( name = "DEPOSITOR_CNIC" )
	    public String getCashDepositorCnic()
	    {
	        return cashDepositorCnic;
	    }

	    public void setCashDepositorCnic( String cashDepositorCnic )
	    {
	        this.cashDepositorCnic = cashDepositorCnic;
	    }

	    @Transient
	    public boolean isInitiatorResendSmsAvailable()
	    {
	        boolean available = false;
	        if( productId != null )
            {
	            long productId = this.productId.longValue();
	            if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName ) )
	            {
	                if( ProductConstantsInterface.CASH_DEPOSIT == productId || ProductConstantsInterface.CASH_WITHDRAWAL == productId
	                        || ProductConstantsInterface.ACT_TO_ACT == productId || ProductConstantsInterface.ACCOUNT_TO_CASH == productId
	                        || ProductConstantsInterface.CASH_TRANSFER == productId || ProductConstantsInterface.CUSTOMER_RETAIL_PAYMENT == productId )
	                {
	                    available = true;
	                }
	            }
            }
	        return available;
	    }

	    @Transient
	    public String getInitiatorResendSmsBtnLabel()
	    {
	        String label = null;
	        ResendSmsButtonLabelEnum resendSmsButtonLabelEnum = ResendSmsButtonLabelEnum.getEnumByProductId( productId );
	        if( resendSmsButtonLabelEnum != null )
	        {
	            label = resendSmsButtonLabelEnum.getInitiatorLabel();
	        }
	        return label;
	    }

	    @Transient
        public boolean isRecipientResendSmsAvailable()
        {
            boolean available = false;
            if( productId != null )
            {
                long productId = this.productId.longValue();
                if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName ) )
                {
                    if( ProductConstantsInterface.CASH_DEPOSIT == productId || ProductConstantsInterface.CASH_WITHDRAWAL == productId
                            || ProductConstantsInterface.ACT_TO_ACT == productId || ProductConstantsInterface.ACCOUNT_TO_CASH == productId
                            || ProductConstantsInterface.BULK_DISBURSEMENT == productId || ProductConstantsInterface.ZONG_TOPUP == productId
                            || ProductConstantsInterface.CUSTOMER_RETAIL_PAYMENT == productId
                            || ProductConstantsInterface.APOTHECARE_PAYMENT == productId
                            || ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA_PAYMENT == productId ||ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT_PAYMENT == productId
                            || ProductUtils.isBillPaymentByCustomer( supplierId, agent1Id ) )
                    {
                        available = true;
                    }
                }
            }
            return available;
        }

	    @Transient
        public String getRecipientResendSmsBtnLabel()
        {
            String label = null;
            ResendSmsButtonLabelEnum resendSmsButtonLabelEnum = ResendSmsButtonLabelEnum.getEnumByProductId( productId );
            if( resendSmsButtonLabelEnum != null )
            {
                label = resendSmsButtonLabelEnum.getRecipientLabel();
            }
            else if( ProductUtils.isBillPaymentByCustomer( supplierId, agent1Id ) )
            {
                label = ResendSmsButtonLabelConstants.LABEL_CUSTOMER;
            }
            return label;
        }

	    @Transient
        public boolean isWalkinDepositorSmsAvailable()
        {
            boolean available = false;
            if( productId != null )
            {
                long productId = this.productId.longValue();
                if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName ) )
                {
                    if( (ProductConstantsInterface.CASH_DEPOSIT == productId && !GenericValidator.isBlankOrNull( cashDepositorCnic ) )
                            || ProductConstantsInterface.CASH_TRANSFER == productId || ProductConstantsInterface.APOTHECARE == productId
                            || ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA == productId ||ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT == productId
                            || ProductUtils.isBillPaymentByAgent( supplierId, agent1Id ) )
                    {
                        available = true;
                    }
                }
            }
            return available;
        }

	    @Transient
        public boolean isWalkinBeneficiarySmsAvailable()
        {
            boolean available = false;
            if( productId != null )
            {
                long productId = this.productId.longValue();
                if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName ) )
                {
                    if( ProductConstantsInterface.ACCOUNT_TO_CASH == productId || ProductConstantsInterface.CASH_TRANSFER == productId )
                    {
                        available = true;
                    }
                }
            }
            return available;
        }
}
