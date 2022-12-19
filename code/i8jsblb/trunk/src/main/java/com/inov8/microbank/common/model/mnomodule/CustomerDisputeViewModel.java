package com.inov8.microbank.common.model.mnomodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CustomerDisputeViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CustomerDisputeViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CUSTOMER_DISPUTE_VIEW")
public class CustomerDisputeViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 8067127410205773800L;
private Long pk;
   private String code;
   private Date createdOn;
   private Integer status;
   private Long productId;
   private Long transactionDetailId;
   private String consumerNo;
   private Boolean settled;
   private String customerMobileNo;
   private String notificationMobileNo;
   private Boolean issue;
   private Long transactionId;
   private Long transactionCodeId;
   private Long customerId;
   private String mfsId;
   private Double totalAmount;
   private Long appUserId;
   private String mobileNo;

   /**
    * Default constructor.
    */
   public CustomerDisputeViewModel() {
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
    * Returns the value of the <code>code</code> property.
    *
    */
      @Column(name = "CODE" , nullable = false , length=50 )
   public String getCode() {
      return code;
   }

   /**
    * Sets the value of the <code>code</code> property.
    *
    * @param code the value for the <code>code</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCode(String code) {
      this.code = code;
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
    * Returns the value of the <code>status</code> property.
    *
    */
      @Column(name = "STATUS" , nullable = false )
   public Integer getStatus() {
      return status;
   }

   /**
    * Sets the value of the <code>status</code> property.
    *
    * @param status the value for the <code>status</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setStatus(Integer status) {
      this.status = status;
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
    * Returns the value of the <code>settled</code> property.
    *
    */
      @Column(name = "IS_SETTLED" , nullable = false )
   public Boolean getSettled() {
      return settled;
   }

   /**
    * Sets the value of the <code>settled</code> property.
    *
    * @param settled the value for the <code>settled</code> property
    *    
		    */

   public void setSettled(Boolean settled) {
      this.settled = settled;
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
    * Returns the value of the <code>notificationMobileNo</code> property.
    *
    */
      @Column(name = "NOTIFICATION_MOBILE_NO" , nullable = false , length=13 )
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
    * Returns the value of the <code>customerId</code> property.
    *
    */
      @Column(name = "CUSTOMER_ID"  )
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
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID" , nullable = false )
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
   }

   /**
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
      @Column(name = "MOBILE_NO" , nullable = false , length=50 )
   public String getMobileNo() {
      return mobileNo;
   }

   /**
    * Sets the value of the <code>mobileNo</code> property.
    *
    * @param mobileNo the value for the <code>mobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
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
