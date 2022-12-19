package com.inov8.microbank.common.model.transactiondetailinfomodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransactionDetInfoListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionDetInfoListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANSACTION_DET_INFO_LIST_VIEW")
public class TransactionDetInfoListViewModel extends BasePersistableModel implements Serializable {
  



   private Long transactionCodeId;
   private String transactionCode;
   private String mfsId;
   private String saleMobileNo;
   private Long transactionTypeId;
   private String bankAccountNo;
   private String bankAccountNoLastFive;
   private String transactiontype;
   private Long bankid;
   private String bankname;
   private Long productId;
   private String productname;
   private Long supplierid;
   private Double discountAmount;
   private String suppliername;
   private Long servicetypeid;
   private String servicetypename;
   private Boolean commissioned;   
   private Date createdOn;

   private String channel;
   private String senderPaymentMode;
   private String senderAccountNumber;
   private String senderAccountNick;
   private String recipientMobileNumber;
   private String recipientAccountNick;
   private String recipientAccountNumber;
   private String depositorCnic;

   /**
    * Default constructor.
    */
   public TransactionDetInfoListViewModel() {
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
      @Column(name = "TRANSACTION_CODE_ID" , nullable = false )
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
      @Column(name = "TRANSACTION_CODE" , nullable = false , length=50 )
   public String getTransactionCode() {
      return transactionCode;
   }
      
      
      
      /**
       * Returns the value of the <code>discountAmount</code> property.
       *
       */
         @Column(name = "DISCOUNT_AMOUNT" , nullable = false )
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
    * Returns the value of the <code>commissioned</code> property.
    *
    */
      @Column(name = "IS_COMMISSIONED")
   public Boolean getCommissioned() {
      return commissioned;
   }

   /**
    * Sets the value of the <code>commissioned</code> property.
    *
    * @param commissioned the value for the <code>commissioned</code> property
    *    
		    */

   public void setCommissioned(Boolean commissioned) {
      this.commissioned = commissioned;
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
      @Column(name = "BANK_ACCOUNT_NO" , length=20, insertable=false, updatable=false )
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
   public Long getBankid() {
      return bankid;
   }

   /**
    * Sets the value of the <code>bankid</code> property.
    *
    * @param bankid the value for the <code>bankid</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBankid(Long bankid) {
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
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
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
   public Long getServicetypeid() {
      return servicetypeid;
   }

   /**
    * Sets the value of the <code>servicetypeid</code> property.
    *
    * @param servicetypeid the value for the <code>servicetypeid</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setServicetypeid(Long servicetypeid) {
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
    @Column(name = "DEVICE_TYPE_NAME")
    public String getChannel() {
    	return channel;
    }

    public void setChannel(String channel) {
    	this.channel = channel;
    }

    @Column(name = "PAYMENT_MODE")
    public String getSenderPaymentMode() {
    	return senderPaymentMode;
    }

    public void setSenderPaymentMode(String senderPaymentMode) {
    	this.senderPaymentMode = senderPaymentMode;
    }

    @Column(name = "BANK_ACCOUNT_NO")
    public String getSenderAccountNumber() {
    	return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
    	this.senderAccountNumber = senderAccountNumber;
    }

    @Column(name = "ACCOUNT_NICK")
    public String getSenderAccountNick() {
    	return senderAccountNick;
    }

    public void setSenderAccountNick(String senderAccountNick) {
    	this.senderAccountNick = senderAccountNick;
    }

    @Column(name = "NOTIFICATION_MOBILE_NO")
    public String getRecipientMobileNumber() {
    	return recipientMobileNumber;
    }

    public void setRecipientMobileNumber(String recipientMobileNumber) {
        this.recipientMobileNumber = recipientMobileNumber;
    }

    @Column(name = "RCEIPIENT_ACCOUNT_NICK")
    public String getRecipientAccountNick() {
    	return recipientAccountNick;
    }

    public void setRecipientAccountNick(String recipientAccountNick) {
        this.recipientAccountNick = recipientAccountNick;
    }

    @Column(name = "RCEIPIENT_ACCOUNT_NO")
    public String getRecipientAccountNumber() {
    	return recipientAccountNumber;
    }

    public void setRecipientAccountNumber(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    @Column(name = "DEPOSITOR_CNIC")
    public String getDepositorCnic() {
    	return depositorCnic;
    }

    public void setDepositorCnic(String depositorCnic) {
    	this.depositorCnic = depositorCnic;
    }
    
}