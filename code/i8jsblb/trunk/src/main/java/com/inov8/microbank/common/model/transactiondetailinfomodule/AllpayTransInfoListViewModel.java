package com.inov8.microbank.common.model.transactiondetailinfomodule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.validator.GenericValidator;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ProductUtils;
import com.inov8.microbank.common.util.ResendSmsButtonLabelConstants;
import com.inov8.microbank.common.util.ResendSmsButtonLabelEnum;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;

/**
 * The AllpayTransInfoListViewModel entity bean.
 *
 * @author  Fahad Tariq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AllpayTransInfoListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "ALLPAY_TRANS_INFO_LIST_VIEW")
public class AllpayTransInfoListViewModel extends BasePersistableModel implements Serializable {
   private static final long serialVersionUID = -1787326413870873424L;

   private Long transactionCodeId;
   private String transactionCode;
   private Long transactionId;
   private String mfsId;
   //added by atif hussain
   private String handlerId;
   private String handlerMfsId;
   
   private String saleMobileNo;
   private Long transactionTypeId;
   private String bankAccountNo;
   private Double isCommissioned;
   private String bankAccountNoLastFive;
   private String transactiontype;
   private Double bankid;
   private String bankname;
   private Long productId;
   private String productname;
   private Long supplierid;
   private String suppliername;
   private Double servicetypeid;
   private String servicetypename;
   private Double discountAmount;
   private Date createdOn;
   private String recipientAccountNick;
   private String recipientAccountNo;
   private String recipientMobileNo;
   private String recipientMfsId;
   private String senderAccountNick;
   private String deviceType;
   private String paymentMode;
   private Double amount;
   private Double serviceChargesExclusive;
   private Double serviceChargesInclusive;
   private Double totalChargedFromCustomer;
   private String processingStatusName;
   private String agent1Id;
   private String agent2Id;
   private String senderCnic;
   private String recipientCnic;
   private String cashDepositorCnic;

   /**
    * Default constructor.
    */
   public AllpayTransInfoListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionCodeId(primaryKey);
    }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE_ID"  )
   @Id
   public Long getTransactionCodeId() {
      return transactionCodeId;
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
    *    
		    */

   public void setTransactionCodeId(Long transactionCodeId) {
      this.transactionCodeId = transactionCodeId;
   }

   /**
    * Returns the value of the <code>transactionCode</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE"  , length=50 )
   public String getTransactionCode() {
      return transactionCode;
   }

      @Column(name = "TRANSACTION_ID" , nullable = false )
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
    * Returns the value of the <code>isCommissioned</code> property.
    *
    */
      @Column(name = "IS_COMMISSIONED"  )
   public Double getIsCommissioned() {
      return isCommissioned;
   }

   /**
    * Sets the value of the <code>isCommissioned</code> property.
    *
    * @param isCommissioned the value for the <code>isCommissioned</code> property
    *    
		    */

   public void setIsCommissioned(Double isCommissioned) {
      this.isCommissioned = isCommissioned;
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
    * Returns the value of the <code>transactiontype</code> property.
    *
    */
      @Column(name = "TRANSACTIONTYPE"  , length=50 )
   public String getTransactiontype() {
      return transactiontype;
   }

   /**
    * Sets the value of the <code>transactiontype</code> property.
    *
    * @param transactiontype the value for the <code>transactiontype</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setTransactiontype(String transactiontype) {
      this.transactiontype = transactiontype;
   }

   /**
    * Returns the value of the <code>bankid</code> property.
    *
    */
      @Column(name = "BANKID"  )
   public Double getBankid() {
      return bankid;
   }

   /**
    * Sets the value of the <code>bankid</code> property.
    *
    * @param bankid the value for the <code>bankid</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBankid(Double bankid) {
      this.bankid = bankid;
   }

   /**
    * Returns the value of the <code>bankname</code> property.
    *
    */
      @Column(name = "BANKNAME"  , length=50 )
   public String getBankname() {
      return bankname;
   }

   /**
    * Sets the value of the <code>bankname</code> property.
    *
    * @param bankname the value for the <code>bankname</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setBankname(String bankname) {
      this.bankname = bankname;
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
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   /**
    * Returns the value of the <code>productname</code> property.
    *
    */
      @Column(name = "PRODUCTNAME"  , length=50 )
   public String getProductname() {
      return productname;
   }

   /**
    * Sets the value of the <code>productname</code> property.
    *
    * @param productname the value for the <code>productname</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setProductname(String productname) {
      this.productname = productname;
   }

   /**
    * Returns the value of the <code>supplierid</code> property.
    *
    */
      @Column(name = "SUPPLIERID"  )
   public Long getSupplierid() {
      return supplierid;
   }

   /**
    * Sets the value of the <code>supplierid</code> property.
    *
    * @param supplierid the value for the <code>supplierid</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setSupplierid(Long supplierid) {
      this.supplierid = supplierid;
   }

   /**
    * Returns the value of the <code>suppliername</code> property.
    *
    */
      @Column(name = "SUPPLIERNAME"  , length=50 )
   public String getSuppliername() {
      return suppliername;
   }

   /**
    * Sets the value of the <code>suppliername</code> property.
    *
    * @param suppliername the value for the <code>suppliername</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setSuppliername(String suppliername) {
      this.suppliername = suppliername;
   }

   /**
    * Returns the value of the <code>servicetypeid</code> property.
    *
    */
      @Column(name = "SERVICETYPEID"  )
   public Double getServicetypeid() {
      return servicetypeid;
   }

   /**
    * Sets the value of the <code>servicetypeid</code> property.
    *
    * @param servicetypeid the value for the <code>servicetypeid</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setServicetypeid(Double servicetypeid) {
      this.servicetypeid = servicetypeid;
   }

   /**
    * Returns the value of the <code>servicetypename</code> property.
    *
    */
      @Column(name = "SERVICETYPENAME"  , length=50 )
   public String getServicetypename() {
      return servicetypename;
   }

   /**
    * Sets the value of the <code>servicetypename</code> property.
    *
    * @param servicetypename the value for the <code>servicetypename</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setServicetypename(String servicetypename) {
      this.servicetypename = servicetypename;
   }

   /**
    * Returns the value of the <code>discountAmount</code> property.
    *
    */
      @Column(name = "DISCOUNT_AMOUNT"  )
   public Double getDiscountAmount() {
      return discountAmount;
   }

   /**
    * Sets the value of the <code>discountAmount</code> property.
    *
    * @param discountAmount the value for the <code>discountAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setDiscountAmount(Double discountAmount) {
      this.discountAmount = discountAmount;
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
    * Returns the value of the <code>recipientAccountNick</code> property.
    *
    */
      @Column(name = "RECIPIENT_ACCOUNT_NICK"  , length=50 )
   public String getRecipientAccountNick() {
      return recipientAccountNick;
   }

   /**
    * Sets the value of the <code>recipientAccountNick</code> property.
    *
    * @param recipientAccountNick the value for the <code>recipientAccountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setRecipientAccountNick(String recipientAccountNick) {
      this.recipientAccountNick = recipientAccountNick;
   }

   /**
    * Returns the value of the <code>recipientAccountNo</code> property.
    *
    */
      @Column(name = "RECIPIENT_ACCOUNT_NO"  , length=250 )
   public String getRecipientAccountNo() {
      return recipientAccountNo;
   }

   /**
    * Sets the value of the <code>recipientAccountNo</code> property.
    *
    * @param recipientAccountNo the value for the <code>recipientAccountNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setRecipientAccountNo(String recipientAccountNo) {
      this.recipientAccountNo = recipientAccountNo;
   }

   /**
    * Returns the value of the <code>recipientMobileNo</code> property.
    *
    */
      @Column(name = "RECIPIENT_MOBILE_NO"  , length=13 )
   public String getRecipientMobileNo() {
      return recipientMobileNo;
   }

   /**
    * Sets the value of the <code>recipientMobileNo</code> property.
    *
    * @param recipientMobileNo the value for the <code>recipientMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="13"
    */

   public void setRecipientMobileNo(String recipientMobileNo) {
      this.recipientMobileNo = recipientMobileNo;
   }

   /**
    * Returns the value of the <code>recipientMfsId</code> property.
    *
    */
      @Column(name = "RECIPIENT_MFS_ID"  , length=50 )
   public String getRecipientMfsId() {
      return recipientMfsId;
   }

   /**
    * Sets the value of the <code>recipientMfsId</code> property.
    *
    * @param recipientMfsId the value for the <code>recipientMfsId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setRecipientMfsId(String recipientMfsId) {
      this.recipientMfsId = recipientMfsId;
   }

   /**
    * Returns the value of the <code>senderAccountNick</code> property.
    *
    */
      @Column(name = "SENDER_ACCOUNT_NICK"  , length=50 )
   public String getSenderAccountNick() {
      return senderAccountNick;
   }

   /**
    * Sets the value of the <code>senderAccountNick</code> property.
    *
    * @param senderAccountNick the value for the <code>senderAccountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setSenderAccountNick(String senderAccountNick) {
      this.senderAccountNick = senderAccountNick;
   }

   /**
    * Returns the value of the <code>deviceType</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE"  , length=50 )
   public String getDeviceType() {
      return deviceType;
   }

   /**
    * Sets the value of the <code>deviceType</code> property.
    *
    * @param deviceType the value for the <code>deviceType</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
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
    * @param serviceChargesExclusive the value for the <code>serviceChargesExclusive</code> property
    *    
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
    * @param serviceChargesInclusive the value for the <code>serviceChargesInclusive</code> property
    *    
    */

   public void setServiceChargesInclusive(Double serviceChargesInclusive) {
      this.serviceChargesInclusive = serviceChargesInclusive;
   }

   /**
    * Returns the value of the <code>totalChargedFromCustomer</code> property.
    *
    */
      @Column(name = "TOTAL_CHARGED_FROM_CUSTOMER"  )
   public Double getTotalChargedFromCustomer() {
      return totalChargedFromCustomer;
   }

   /**
    * Sets the value of the <code>totalChargedFromCustomer</code> property.
    *
    * @param totalChargedFromCustomer the value for the <code>totalChargedFromCustomer</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTotalChargedFromCustomer(Double totalChargedFromCustomer) {
      this.totalChargedFromCustomer = totalChargedFromCustomer;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionCodeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionCodeId=" + getTransactionCodeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionCodeId";
			return primaryKeyFieldName;				
    }
    
    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	    	
    	return associationModelList;
    }    

    /**
     * Sets the value of the <code>processingStatusName</code> property.
     *
     * @param processingStatusName the value for the <code>processingStatusName</code> property
     *    
 		    * @spring.validator type="maxlength"     
     * @spring.validator-args arg1value="${var:maxlength}"
     * @spring.validator-var name="maxlength" value="50"
     */

    public void setProcessingStatusName(String processingStatusName) {
       this.processingStatusName = processingStatusName;
    }

    /**
     * Returns the value of the <code>processingStatusName</code> property.
     *
     */
       @Column(name = "PROCESSING_STATUS_NAME"  )
    public String getProcessingStatusName() {
       return this.processingStatusName;
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
       
       
       @Column( name = "HANDLER_ID" )
       public String getHandlerId() {
			return handlerId;
		}

		public void setHandlerId(String handlerId) {
			this.handlerId = handlerId;
		}
		
		@Column( name = "HANDLER_MFS_ID" )
		public String getHandlerMfsId() {
			return handlerMfsId;
		}

		public void setHandlerMfsId(String handlerMfsId) {
			this.handlerMfsId = handlerMfsId;
		}

	@Transient
       public boolean isInitiatorResendSmsAvailable()
       {
           boolean available = false;
           if( productId != null )
           {
               long productId = this.productId.longValue();
               if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName ) ||SupplierProcessingStatusConstants.IN_PROCESS.equalsIgnoreCase( processingStatusName ) )
               {
                   if( ProductConstantsInterface.CASH_DEPOSIT == productId || ProductConstantsInterface.CASH_WITHDRAWAL == productId
                           || ProductConstantsInterface.ACCOUNT_TO_CASH == productId
                           || ProductConstantsInterface.CASH_TRANSFER == productId || ProductConstantsInterface.CUSTOMER_RETAIL_PAYMENT == productId
                           || ProductConstantsInterface.APOTHECARE == productId || ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER == productId
                           || ProductConstantsInterface.RSO_TO_AGENT_TRANSFER == productId
                           || ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA == productId ||ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT == productId
                           || ProductConstantsInterface.ML_TRANSFER_TO_RETAILER == productId ||ProductConstantsInterface.ML_TRANSFER_TO_CUSTOMER == productId
                           || ProductConstantsInterface.BULK_PAYMENT == productId
                           || ProductUtils.isBillPaymentByAgent( supplierid, agent1Id ) )
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
           else if ( ProductUtils.isBillPaymentByAgent( supplierid, agent1Id ) )
           {
               label = ResendSmsButtonLabelConstants.LABEL_AGENT;
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
               if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName )||SupplierProcessingStatusConstants.IN_PROCESS.equalsIgnoreCase( processingStatusName ) )
               {
                   if( ProductConstantsInterface.CASH_DEPOSIT == productId || ProductConstantsInterface.CASH_WITHDRAWAL == productId
                           || ProductConstantsInterface.ACCOUNT_TO_CASH == productId || ProductConstantsInterface.CUSTOMER_RETAIL_PAYMENT == productId
                           || ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER == productId || ProductConstantsInterface.RSO_TO_AGENT_TRANSFER == productId
                	   	   || ProductConstantsInterface.ML_TRANSFER_TO_RETAILER == productId ||ProductConstantsInterface.ML_TRANSFER_TO_CUSTOMER == productId)
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
           return label;
       }

       @Transient
       public boolean isWalkinDepositorSmsAvailable()
       {
           boolean available = false;
           if( productId != null )
           {
               long productId = this.productId.longValue();
               if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName ) || SupplierProcessingStatusConstants.IN_PROCESS.equalsIgnoreCase( processingStatusName ))
               {
                   if( (ProductConstantsInterface.CASH_DEPOSIT == productId && !GenericValidator.isBlankOrNull( cashDepositorCnic ) )
                           || ProductConstantsInterface.CASH_TRANSFER == productId || ProductConstantsInterface.APOTHECARE == productId 
                           || ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA == productId ||ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT == productId
                           || ProductUtils.isBillPaymentByAgent( supplierid, agent1Id ) )
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
               if( SupplierProcessingStatusConstants.COMPLETE_NAME.equalsIgnoreCase( processingStatusName )|| SupplierProcessingStatusConstants.IN_PROCESS.equalsIgnoreCase( processingStatusName ))
               {
                   if( ProductConstantsInterface.ACCOUNT_TO_CASH == productId || ProductConstantsInterface.CASH_TRANSFER == productId 
                		   || ProductConstantsInterface.BULK_PAYMENT == productId)
                   {
                       available = true;
                   }
               }
           }
           return available;
       }
}
