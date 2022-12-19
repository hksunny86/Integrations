package com.inov8.microbank.common.model.transactionmodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The FetchTransactionListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="FetchTransactionListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "FETCH_TRANSACTION_LIST_VIEW")
public class FetchTransactionListViewModel extends BasePersistableModel implements Serializable {
  



   private String tranCode;
   private Long transactionId;
   private Double tranAmount;
   private String notificationMobileNo;
   private String productName;
   private Date tranDate;
   private Long fromRetContactId;
   private Long fromDistContactId;
   private Long customerId;
   private Long transactionTypeId;
   private String bankResponseCode;
   private String suppResponseCode;
   private Long deviceTypeId;
   private String deviceTypeName;
   private String name;
   private String supplierName;
   private String helpLine;
   private String transactionDateTime;
   private String serviceCharges="";
   private String totalTransactionAmount="";

   /**
    * Default constructor.
    */
   public FetchTransactionListViewModel() {
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
    * Returns the value of the <code>tranCode</code> property.
    *
    */
      @Column(name = "TRAN_CODE" , nullable = false , length=50 )
   public String getTranCode() {
      return tranCode;
   }

   /**
    * Sets the value of the <code>tranCode</code> property.
    *
    * @param tranCode the value for the <code>tranCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setTranCode(String tranCode) {
      this.tranCode = tranCode;
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
    * Returns the value of the <code>tranAmount</code> property.
    *
    */
      @Column(name = "TRAN_AMOUNT" , nullable = false )
   public Double getTranAmount() {
      return tranAmount;
   }

   /**
    * Sets the value of the <code>tranAmount</code> property.
    *
    * @param tranAmount the value for the <code>tranAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTranAmount(Double tranAmount) {
      this.tranAmount = tranAmount;
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
    * Returns the value of the <code>tranDate</code> property.
    *
    */
      @Column(name = "TRAN_DATE" , nullable = false )
   public Date getTranDate() {
      return tranDate;
   }

   /**
    * Sets the value of the <code>tranDate</code> property.
    *
    * @param tranDate the value for the <code>tranDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setTranDate(Date tranDate) {
      this.tranDate = tranDate;
   }

   /**
    * Returns the value of the <code>fromRetContactId</code> property.
    *
    */
      @Column(name = "FROM_RET_CONTACT_ID"  )
   public Long getFromRetContactId() {
      return fromRetContactId;
   }

   /**
    * Sets the value of the <code>fromRetContactId</code> property.
    *
    * @param fromRetContactId the value for the <code>fromRetContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFromRetContactId(Long fromRetContactId) {
      this.fromRetContactId = fromRetContactId;
   }

   /**
    * Returns the value of the <code>fromDistContactId</code> property.
    *
    */
      @Column(name = "FROM_DIST_CONTACT_ID"  )
   public Long getFromDistContactId() {
      return fromDistContactId;
   }

   /**
    * Sets the value of the <code>fromDistContactId</code> property.
    *
    * @param fromDistContactId the value for the <code>fromDistContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFromDistContactId(Long fromDistContactId) {
      this.fromDistContactId = fromDistContactId;
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
    * Returns the value of the <code>bankResponseCode</code> property.
    *
    */
      @Column(name = "BANK_RESPONSE_CODE"  , length=50 )
   public String getBankResponseCode() {
      return bankResponseCode;
   }

   /**
    * Sets the value of the <code>bankResponseCode</code> property.
    *
    * @param bankResponseCode the value for the <code>bankResponseCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setBankResponseCode(String bankResponseCode) {
      this.bankResponseCode = bankResponseCode;
   }

   /**
    * Returns the value of the <code>suppResponseCode</code> property.
    *
    */
      @Column(name = "SUPP_RESPONSE_CODE"  , length=50 )
   public String getSuppResponseCode() {
      return suppResponseCode;
   }

   /**
    * Sets the value of the <code>suppResponseCode</code> property.
    *
    * @param suppResponseCode the value for the <code>suppResponseCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setSuppResponseCode(String suppResponseCode) {
      this.suppResponseCode = suppResponseCode;
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
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME"  , length=50 )
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
    * Returns the value of the <code>helpLine</code> property.
    *
    */
      @Column(name = "HELP_LINE"  , length=250 )
   public String getHelpLine() {
      return helpLine;
   }

   /**
    * Sets the value of the <code>helpLine</code> property.
    *
    * @param helpLine the value for the <code>helpLine</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setHelpLine(String helpLine) {
      this.helpLine = helpLine;
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

    @javax.persistence.Transient
	public String getTransactionDateTime()
	{
		return transactionDateTime;
	}

	public void setTransactionDateTime(String transactionDateTime)
	{
		this.transactionDateTime = transactionDateTime;
	}

	 @javax.persistence.Transient
	public String getServiceCharges() {
		return serviceCharges;
	}

	public void setServiceCharges(String serviceCharges) {
		this.serviceCharges = serviceCharges;
	}

	@javax.persistence.Transient
	public String getTotalTransactionAmount() {
		return totalTransactionAmount;
	}

	public void setTotalTransactionAmount(String totalTransactionAmount) {
		this.totalTransactionAmount = totalTransactionAmount;
	}       
}
