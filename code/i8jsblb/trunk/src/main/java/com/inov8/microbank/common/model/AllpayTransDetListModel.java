package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AllpayTransDetListModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AllpayTransDetListModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ALLPAY_TRANS_DET_LIST")
public class AllpayTransDetListModel extends BasePersistableModel implements Serializable {
  



   private Long pk;
   private Long transactionId;
   private String allpayId;
   private String saleMobileNo;
   private String authorizationCode;
   private String bankAccountNo;
   private String bankAccountNoLastFive;
   private String transactionCode;
   private Date createdOn;
   private Long productId;
   private String productName;
   private Long supplierId;
   private String supplierName;
   private Long paymentModeId;
   private String paymentMode;
   private Long bankId;
   private String bankName;
   private Long transactionCodeId;
   private Long transactionDetailId;
   private Double transactionAmount;
   private String veriflyStatus;
   private String processingStatusName;
   private String consumerNo;
   private Double supplierCommission;
   private Double serviceCharges;
   private Double totalAmount;
   private Double toSupplier;
   private Double toNationalDistributor;
   private Double toDistributor;
   private Double toRetailer;
   private Double mcpl;

   /**
    * Default constructor.
    */
   public AllpayTransDetListModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPk(primaryKey);
    }

   /**
    * Returns the value of the <code>pk</code> property.
    *
    */
      @Column(name = "PK"  )
   @Id 
   public Long getPk() {
      return pk;
   }

   /**
    * Sets the value of the <code>pk</code> property.
    *
    * @param pk the value for the <code>pk</code> property
    *    
		    */

   public void setPk(Long pk) {
      this.pk = pk;
   }

   /**
    * Returns the value of the <code>transactionId</code> property.
    *
    */
      @Column(name = "TRANSACTION_ID" , nullable = false )
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
    * Returns the value of the <code>allpayId</code> property.
    *
    */
      @Column(name = "ALLPAY_ID"  , length=50 )
   public String getAllpayId() {
      return allpayId;
   }

   /**
    * Sets the value of the <code>allpayId</code> property.
    *
    * @param allpayId the value for the <code>allpayId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAllpayId(String allpayId) {
      this.allpayId = allpayId;
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
    * Returns the value of the <code>transactionAmount</code> property.
    *
    */
      @Column(name = "TRANSACTION_AMOUNT" , nullable = false )
   public Double getTransactionAmount() {
      return transactionAmount;
   }

   /**
    * Sets the value of the <code>transactionAmount</code> property.
    *
    * @param transactionAmount the value for the <code>transactionAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTransactionAmount(Double transactionAmount) {
      this.transactionAmount = transactionAmount;
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
    * Returns the value of the <code>consumerNo</code> property.
    *
    */
      @Column(name = "CONSUMER_NO"  , length=50 )
   public String getConsumerNo() {
      return consumerNo;
   }

   /**
    * Sets the value of the <code>consumerNo</code> property.
    *
    * @param consumerNo the value for the <code>consumerNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setConsumerNo(String consumerNo) {
      this.consumerNo = consumerNo;
   }

   /**
    * Returns the value of the <code>supplierCommission</code> property.
    *
    */
      @Column(name = "SUPPLIER_COMMISSION"  )
   public Double getSupplierCommission() {
      return supplierCommission;
   }

   /**
    * Sets the value of the <code>supplierCommission</code> property.
    *
    * @param supplierCommission the value for the <code>supplierCommission</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setSupplierCommission(Double supplierCommission) {
      this.supplierCommission = supplierCommission;
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
    * Returns the value of the <code>toSupplier</code> property.
    *
    */
      @Column(name = "TO_SUPPLIER"  )
   public Double getToSupplier() {
      return toSupplier;
   }

   /**
    * Sets the value of the <code>toSupplier</code> property.
    *
    * @param toSupplier the value for the <code>toSupplier</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setToSupplier(Double toSupplier) {
      this.toSupplier = toSupplier;
   }

   /**
    * Returns the value of the <code>toNationalDistributor</code> property.
    *
    */
      @Column(name = "TO_NATIONAL_DISTRIBUTOR"  )
   public Double getToNationalDistributor() {
      return toNationalDistributor;
   }

   /**
    * Sets the value of the <code>toNationalDistributor</code> property.
    *
    * @param toNationalDistributor the value for the <code>toNationalDistributor</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setToNationalDistributor(Double toNationalDistributor) {
      this.toNationalDistributor = toNationalDistributor;
   }

   /**
    * Returns the value of the <code>toDistributor</code> property.
    *
    */
      @Column(name = "TO_DISTRIBUTOR"  )
   public Double getToDistributor() {
      return toDistributor;
   }

   /**
    * Sets the value of the <code>toDistributor</code> property.
    *
    * @param toDistributor the value for the <code>toDistributor</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setToDistributor(Double toDistributor) {
      this.toDistributor = toDistributor;
   }

   /**
    * Returns the value of the <code>toRetailer</code> property.
    *
    */
      @Column(name = "TO_RETAILER"  )
   public Double getToRetailer() {
      return toRetailer;
   }

   /**
    * Sets the value of the <code>toRetailer</code> property.
    *
    * @param toRetailer the value for the <code>toRetailer</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setToRetailer(Double toRetailer) {
      this.toRetailer = toRetailer;
   }

   /**
    * Returns the value of the <code>mcpl</code> property.
    *
    */
      @Column(name = "MCPL"  )
   public Double getMcpl() {
      return mcpl;
   }

   /**
    * Sets the value of the <code>mcpl</code> property.
    *
    * @param mcpl the value for the <code>mcpl</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setMcpl(Double mcpl) {
      this.mcpl = mcpl;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getPk();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }       
}
