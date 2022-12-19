package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransactionDetailPortalListModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionDetailPortalListModel"
 */
@XmlRootElement(name="transactionDetailMasterModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="TRANSACTION_DETAIL_MASTER_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="TRANSACTION_DETAIL_MASTER_seq") } )
//@javax.persistence.SequenceGenerator(name = "TRANSACTION_DETAIL_MASTER_seq",sequenceName = "TRANSACTION_DETAIL_MASTER_seq")
@Table(name = "TRANSACTION_DETAIL_MASTER")
public class TransactionDetailMasterModel extends BasePersistableModel implements Serializable {
   private static final long serialVersionUID = -4350194068949610720L;

   private Long transactionId;
   private String mfsId;
   private String senderAgentAccountNo;
   private Long pk;
   private String recipientMfsId;
   private String recipientAccountNo;
   private String saleMobileNo;
   private String senderCnic;
   private String recipientMobileNo;
   private String recipientCnic;
   private String authorizationCode;
   private String bankAccountNo;
   private String bankAccountNoLastFive;
   private String transactionCode;
   private Date createdOn;
   private Date updatedOn;
   private Long productId;
   private String productName;
   private String productCode;
   private String billType;
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
   private String cashDepositorCnic;
   private String processingStatusName;
   private Long supProcessingStatusId;
   private String agent1Id;
   private String agent2Id;
   private String agent1MobileNo;
   private String consumerNo;
   private Double totalAmount;
   private Double productThresholdCharges;
   private Long senderDeviceTypeId;
   private String deviceType;
   private Long recipientDeviceTypeId;
   private String recipientDeviceType;
   private Boolean isManualOTPin;

   private Double fed;
   private Double wht;
   private Double taxDeducted;

   private Double akblCommission;
   private Double agentCommission;
   private Double agent2Commission;
   private Double franchise1Commission;
   private Double franchise2Commission;
   private Double retailerShare;
   private Double salesTeamCommission;
   private Double blbSettlementCommission;
   private Double othersCommission;
   
   private Double inclusiveCharges;
   private Double exclusiveCharges;

   private String titleFetchRrn;
   private String billInquiryRrn;
   private String fundTransferRrn;
   private String billPaymentRrn;
   private String checkBalanceRrn;
   private Date billDueDate;
   private Long commissionReasonId;
//   private Double toSupplier;
//   private Double totalBankComm;
//   private Double mcpl;
   private String senderAccountNick;
   private Long franchise1Id;
   private Long franchise2Id;
   private Boolean isIssue;
   private String recipientBankAccountNo;
   private String recipientAccountNick;
   private Integer paymentTypeId;
   private String paymentTypeName;
   
   private transient String transactionNo;
   private Long segmentId;
   private Long handlerId;
   private String handlerMfsId;
   private String billAggregator;
   private Long tellerAppUserId;
   private Long failureReasonId;
   private String failureReason;
   private Boolean thirdPartyCheck;
   private Double sundryAmount;
   private Boolean updateP2PFlag;
   private Boolean reversalFlag;
   private Integer redemptionType;
   private Boolean fullReversalAllowed;

   
   private String recipientAgentAccountNo;
   private Double billAmount;
   private Double lateBillAmount;
   private Long reversedByAppUserId;
   private String reversedByName;
   private Date reversedDate;
   private String reversedComments;
   private Long sendingRegion;
   private Long receivingRegion;
   private String sendingRegionName;
   private String receivingRegionName;
   
   private Long serviceId;
   private String serviceName;
   private Boolean senderBVS;
   private Boolean receiverBVS;

   private String cDate;
   private String uDate;
   private Date startDate;
   private Date endDate;

   private String fonepayTransactionCode;
   private Long fonepaysettelmentType;

   
   private Date businessDate;
    private String terminalId;
    private String channelId;
    private String externalProductName;

   private Long senderDistributorId;
   private Long senderServiceOPId;

   private Long receiverDistributorId;
   private Long receiverServiceOPId;

   private String senderDistributorName;
   private String senderServiceOPName;

   private String receiverDistributorName;
   private String receiverServiceOPName;

   private Long receiverAreaId;
   private Long senderAreaId;

   private String senderAreaName;
   private String receiverAreaName;

   private Double scoCommission;

   //Agent Network Migration
   private Long senderRetailerContactId;
   private String senderRetailerContactName;
   private Long senderRetailerId;
   private String senderRetailerName;
   private Long senderDistributorLevelId;
   private String senderDistributorLevelName;
   private Long recipientRetailerContactId;
   private String recipientRetailerContactName;
   private Long recipientRetailerId;
   private String recipientRetailerName;
   private Long recipientDistributorLevelId;
   private String recipientDistributorLevelName;
   private Long senderParentRetailerContactId;
   private Long receiverParentRetailerContactId;

   //Excise & Taxation Transaction
   private String vehicleRegNo;
   private String vehicleChesisNo;
   private String exciseAssessmentNumber;
   private Double exciseAssessmentAmount;
   private String exciseChallanNo;

   private String nIfq;
   private String mintaieCount;

   private Long transactionPurposeId;

   private String debitCardNumber;

   private String toBankImd;

   private String recipientAccountTitle;

   private String macAddress;

   private String ipAddress;

    private String latitude;
    private String longitude;
    private String imeiNumber;
    private String stan;
    private String reserved3;
    private String reserved4;
   private String reserved5;
   private String reserved1;
   private String reserved2;
   private String reserved6;
   private String reserved7;
   private String reserved8;
   private String reserved9;
   private String reserved10;

   @Column(name = "STAN" )
   public String getStan() {
      return stan;
   }

   public void setStan(String stan) {
      this.stan = stan;
   }

   @javax.persistence.Transient
   public String getcDate() {
	return cDate;
}

@javax.persistence.Transient
public void setcDate(String cDate) {
	this.cDate = cDate;
}

@javax.persistence.Transient
public String getuDate() {
	return uDate;
}

@javax.persistence.Transient
public void setuDate(String uDate) {
	this.uDate = uDate;
}




@Column(name = "HANDLER_ID" )
public Long getHandlerId() {
	return handlerId;
}

public void setHandlerId(Long handlerId) {
	this.handlerId = handlerId;
}

@Column(name = "HANDLER_MFS_ID" )
public String getHandlerMfsId() {
	return handlerMfsId;
}

public void setHandlerMfsId(String handlerMfsId) {
	this.handlerMfsId = handlerMfsId;
}

/**
    * Default constructor.
    */
   public TransactionDetailMasterModel(){}
   
   public TransactionDetailMasterModel(boolean initialize) {
	   if(initialize){  
		   this.setAkblCommission(0d);
		   this.setAgentCommission(0d);
		   this.setAgent2Commission(0d);
		   this.setWht(0d);
		   this.setFed(0d);
		   this.setInclusiveCharges(0d);
		   this.setExclusiveCharges(0d);
		   this.setCommissionReasonId(0l);
		   this.setFranchise1Commission(0d);
		   this.setFranchise2Commission(0d);
		   this.setRetailerShare(0d);
		   this.setTaxDeducted(0d);
		   this.setSalesTeamCommission(0d);
		   this.setBlbSettlementCommission(0d);
	   }
   }

   @Transient
   public String getTransactionNo() {
	return transactionNo;
	}
	
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
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
      @Column(name = "PK" , nullable = false  )
      @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTION_DETAIL_MASTER_seq") 
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
   @Column(name = "TRANSACTION_ID" )
   public Long getTransactionId() {
      return transactionId;
   }
   public void setTransactionId(Long transactionId) {
      this.transactionId = transactionId;
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

   @Column(name = "RECIPIENT_ACCOUNT_NO"  , length=30 )
   public String getRecipientAccountNo()
   {
       return recipientAccountNo;
   }

   public void setRecipientAccountNo( String recipientAccountNo )
   {
       this.recipientAccountNo = recipientAccountNo;
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
    * Returns the value of the <code>recipientMobileNo</code> property.
    *
    */
      @Column(name = "RECIPIENT_MOBILE_NO"  , length=50 )
   public String getRecipientMobileNo() {
      return recipientMobileNo;
   }

   /**
    * Sets the value of the <code>recipientMobileNo</code> property.
    *
    * @param saleMobileNo the value for the <code>recipientMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="13"
    */

   public void setRecipientMobileNo(String recipientMobileNo) {
      this.recipientMobileNo = recipientMobileNo;
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
      @Column(name = "CREATED_ON" )
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

   @Column(name = "UPDATED_ON")
   public Date getUpdatedOn()
   {
       return updatedOn;
   }

   public void setUpdatedOn( Date updatedOn )
   {
       this.updatedOn = updatedOn;
   }
   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
      @Column(name = "PRODUCT_ID" )
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
      @Column(name = "PRODUCT_NAME" , length=50 )
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
    * Returns the value of the <code>productCode</code> property.
    *
    */
   @Column(name = "PRODUCT_CODE"  , length=50 )
   public String getProductCode()
   {
      return productCode;
   }

   public void setProductCode( String productCode )
   {
      this.productCode = productCode;
   }

   @Column( name = "BILL_TYPE" , length=50 )
   public String getBillType()
   {
       return billType;
   }

   public void setBillType( String productType )
   {
       this.billType = productType;
   }
   /**
    * Returns the value of the <code>supplierId</code> property.
    *
    */
      @Column(name = "SUPPLIER_ID" )
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
      @Column(name = "SUPPLIER_NAME" , length=50 )
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
      @Column(name = "TRANSACTION_DETAIL_ID" )
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
      @Column(name = "TRANSACTION_AMOUNT" )
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

   @Column( name="AGENT1_MOBILE_NO" )
   public String getAgent1MobileNo()
   {
       return agent1MobileNo;
   }
    
   public void setAgent1MobileNo( String agent1MobileNo )
   {
       this.agent1MobileNo = agent1MobileNo;
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
    * Returns the value of the <code>inclusiveCharges</code> property.
    *
    */
      @Column(name = "SERVICE_CHARGES_INCLUSIVE"  )
   public Double getInclusiveCharges() {
      return inclusiveCharges;
   }

   /**
    * Sets the value of the <code>inclusiveCharges</code> property.
    */

   public void setInclusiveCharges(Double inclusiveCharges) {
      this.inclusiveCharges = inclusiveCharges;
   }

   /**
    * Returns the value of the <code>exclusiveCharges</code> property.
    *
    */
      @Column(name = "SERVICE_CHARGES_EXCLUSIVE"  )
   public Double getExclusiveCharges() {
      return exclusiveCharges;
   }

   /**
    * Sets the value of the <code>exclusiveCharges</code> property.
    */
   public void setExclusiveCharges(Double exclusiveCharges) {
      this.exclusiveCharges = exclusiveCharges;
   }

   /**
    * Returns the value of the <code>totalAmount</code> property.
    *
    */
      @Column(name = "TOTAL_AMOUNT" )
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
//      @Column(name = "TO_SUPPLIER"  )
//   public Double getToSupplier() {
//      return toSupplier;
//   }

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

//   public void setToSupplier(Double toSupplier) {
//      this.toSupplier = toSupplier;
//   }

   /**
    * Returns the value of the <code>totalBankComm</code> property.
    *
    */
//      @Column(name = "TOTAL_BANK_COMM"  )
//   public Double getTotalBankComm() {
//      return totalBankComm;
//   }

   /**
    * Sets the value of the <code>totalBankComm</code> property.
    *
    * @param totalBankComm the value for the <code>totalBankComm</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

//   public void setTotalBankComm(Double totalBankComm) {
//      this.totalBankComm = totalBankComm;
//   }

   /**
    * Returns the value of the <code>mcpl</code> property.
    *
    */
//      @Column(name = "MCPL"  )
//   public Double getMcpl() {
//      return mcpl;
//   }

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

//   public void setMcpl(Double mcpl) {
//      this.mcpl = mcpl;
//   }



    @Column(name="SENDER_DEVICE_TYPE_ID")
    public Long getSenderDeviceTypeId()
    {
        return senderDeviceTypeId;
    }

    public void setSenderDeviceTypeId( Long senderDeviceTypeId )
    {
        this.senderDeviceTypeId = senderDeviceTypeId;
    }

    @Column(name="DEVICE_TYPE")
    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType( String deviceType )
    {
        this.deviceType = deviceType;
    }

    @Column(name="RECIPIENT_DEVICE_TYPE_ID")
    public Long getRecipientDeviceTypeId()
    {
        return recipientDeviceTypeId;
    }

    public void setRecipientDeviceTypeId( Long recipientDeviceTypeId )
    {
        this.recipientDeviceTypeId = recipientDeviceTypeId;
    }

    @Column(name="RECIPIENT_DEVICE_TYPE")
    public String getRecipientDeviceType()
    {
        return recipientDeviceType;
    }

    public void setRecipientDeviceType( String recipientDeviceType )
    {
        this.recipientDeviceType = recipientDeviceType;
    }

    @Column(name="IS_MANUAL_OT_PIN")
    public Boolean getIsManualOTPin()
    {
        return isManualOTPin;
    }

    public void setIsManualOTPin( Boolean isManualOTPin )
    {
        this.isManualOTPin = isManualOTPin;
    }

    @Transient
    public String getIsManualOTPinString()
    {
        String manualOTPinString = "No";
        if(this.isManualOTPin != null && this.isManualOTPin == true)
        {
            manualOTPinString = "Yes";
        }
        return manualOTPinString;
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

    @Column(name = "FED"  )
    public Double getFed() {
		return fed;
	}

    public void setFed(Double fed) {
		this.fed = fed;
	}

    @Column(name = "WHT"  )
	public Double getWht() {
		return wht;
	}

	public void setWht(Double wht) {
		this.wht = wht;
	}

	@Column( name = "TAX_DEDUCTED" )
    public Double getTaxDeducted()
    {
        return taxDeducted;
    }

    public void setTaxDeducted( Double taxDeducted )
    {
        this.taxDeducted = taxDeducted;
    }    

    @Column(name = "TO_BANK"  )
	public Double getAkblCommission() {
		return akblCommission;
	}

	public void setAkblCommission(Double akblCommission) {
		this.akblCommission = akblCommission;
	}

    @Column(name = "TO_AGENT1"  )
	public Double getAgentCommission() {
		return agentCommission;
	}

	public void setAgentCommission(Double agentCommission) {
		this.agentCommission = agentCommission;
	}

	@Column( name = "TO_AGENT2" )
	public Double getAgent2Commission()
    {
        return agent2Commission;
    }
	
	public void setAgent2Commission( Double agent2Commission )
    {
        this.agent2Commission = agent2Commission;
    }

	@Column(name = "TO_FRANCHISE1"  )
	public Double getFranchise1Commission()
    {
        return franchise1Commission;
    }

    public void setFranchise1Commission( Double franchise1Commission )
    {
        this.franchise1Commission = franchise1Commission;
    }

    @Column( name = "TO_FRANCHISE2" )
    public Double getFranchise2Commission()
    {
        return franchise2Commission;
    }

    public void setFranchise2Commission( Double franchise2Commission )
    {
        this.franchise2Commission = franchise2Commission;
    }

    @Column( name = "RETAILER_SHARE" )
    public Double getRetailerShare()
    {
        return retailerShare;
    }

    public void setRetailerShare( Double retailerShare )
    {
        this.retailerShare = retailerShare;
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

    @Column( name = "TITLE_FETCH_RRN" )
    public String getTitleFetchRrn() {
		return titleFetchRrn;
	}

	public void setTitleFetchRrn(String titleFetchRrn) {
		this.titleFetchRrn = titleFetchRrn;
	}

	@Column( name = "BILL_INQUIRY_RRN" )
	public String getBillInquiryRrn() {
		return billInquiryRrn;
	}

	public void setBillInquiryRrn(String billInquiryRrn) {
		this.billInquiryRrn = billInquiryRrn;
	}

	@Column( name = "FUND_TRANSFER_RRN" )
	public String getFundTransferRrn() {
		return fundTransferRrn;
	}

	public void setFundTransferRrn(String fundTransferRrn) {
		this.fundTransferRrn = fundTransferRrn;
	}

	@Column( name = "BILL_PAYMENT_RRN" )
	public String getBillPaymentRrn() {
		return billPaymentRrn;
	}

	public void setBillPaymentRrn(String billPaymentRrn) {
		this.billPaymentRrn = billPaymentRrn;
	}

	@Column( name = "CHECK_BALANCE_RRN" )
	public String getCheckBalanceRrn() {
		return checkBalanceRrn;
	}

	public void setCheckBalanceRrn(String checkBalanceRrn) {
		this.checkBalanceRrn = checkBalanceRrn;
	}

	@Column( name = "BILL_DUE_DATE" )
	public Date getBillDueDate() {
		return billDueDate;
	}

	public void setBillDueDate(Date billDueDate) {
		this.billDueDate = billDueDate;
	}

	@Column(name = "SUP_PROCESSING_STATUS_ID" )
	public Long getSupProcessingStatusId() {
		return supProcessingStatusId;
	}

	public void setSupProcessingStatusId(Long supProcessingStatusId) {
		this.supProcessingStatusId = supProcessingStatusId;
	}
	
	@Column(name = "COMMISSION_REASON_ID" )
	public Long getCommissionReasonId() {
		return commissionReasonId;
	}

	public void setCommissionReasonId(Long commissionReasonId) {
		this.commissionReasonId = commissionReasonId;
	}

	@Column( name = "SENDER_ACCOUNT_NICK" )
	public String getSenderAccountNick() {
		return senderAccountNick;
	}

	public void setSenderAccountNick(String senderAccountNick) {
		this.senderAccountNick = senderAccountNick;
	}
	
	@Column( name = "FRANCHISE1_ID" )
	public Long getFranchise1Id() {
		return franchise1Id;
	}

	@Column( name = "FRANCHISE2_ID" )
	public Long getFranchise2Id() {
		return franchise2Id;
	}

	public void setFranchise1Id(Long franchise1Id) {
		this.franchise1Id = franchise1Id;
	}

	public void setFranchise2Id(Long franchise2Id) {
		this.franchise2Id = franchise2Id;
	}

	@Column( name = "IS_ISSUE" )
	public Boolean getIsIssue() {
		return isIssue;
	}

	public void setIsIssue(Boolean isIssue) {
		this.isIssue = isIssue;
	}

	@Column( name = "RECIPIENT_BANK_ACCOUNT_NO" )
	public String getRecipientBankAccountNo() {
		return recipientBankAccountNo;
	}

	public void setRecipientBankAccountNo(String recipientBankAccountNo) {
		this.recipientBankAccountNo = recipientBankAccountNo;
	}
	@Column( name = "RECIPIENT_ACCOUNT_NICK" )
	public String getRecipientAccountNick() {
		return recipientAccountNick;
	}

	public void setRecipientAccountNick(String recipientAccountNick) {
		this.recipientAccountNick = recipientAccountNick;
	}

	@Column( name = "PAYMENT_TYPE_ID" )
	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}

	@Column( name = "PAYMENT_TYPE_NAME" )
	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	@Column( name = "TO_SALES_TEAM" )
	public Double getSalesTeamCommission() {
		return salesTeamCommission;
	}

	public void setSalesTeamCommission(Double salesTeamCommission) {
		this.salesTeamCommission = salesTeamCommission;
	}

	@Column( name = "SEGMENT_ID" )
	public Long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}

	@Column( name = "OTHERS" )
	public Double getOthersCommission() {
		return othersCommission;
	}

	public void setOthersCommission(Double othersCommission) {
		this.othersCommission = othersCommission;
	}

	@Column(name = "BILL_AGGREGATOR")
	public String getBillAggregator() {
		return billAggregator;
	}

	public void setBillAggregator(String billAggregator) {
		this.billAggregator = billAggregator;
	}

	@Column( name = "TELLER_APP_USER_ID" )
	public Long getTellerAppUserId() {
		return tellerAppUserId;
	}

	public void setTellerAppUserId(Long tellerAppUserId) {
		this.tellerAppUserId = tellerAppUserId;
	}
	
	@Column(name = "FAILURE_REASON_ID")
	public Long getFailureReasonId() {
		return failureReasonId;
	}

	public void setFailureReasonId(Long failureReasonId) {
		this.failureReasonId = failureReasonId;
	}

	@Column(name = "FAILURE_REASON")
	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	@Column(name = "THIRD_PARTY_CHECK")
	public Boolean getThirdPartyCheck() {
		return thirdPartyCheck;
	}

	public void setThirdPartyCheck(Boolean thirdPartyCheck) {
		this.thirdPartyCheck = thirdPartyCheck;
	}

	@Column(name = "SUNDRY_AMOUNT")
	public Double getSundryAmount() {
		return sundryAmount;
	}

	public void setSundryAmount(Double sundryAmount) {
		this.sundryAmount = sundryAmount;
	}

	@Column(name = "UPDATE_P2P_FLAG")
	public Boolean getUpdateP2PFlag() {
		return updateP2PFlag;
	}

	public void setUpdateP2PFlag(Boolean updateP2PFlag) {
		this.updateP2PFlag = updateP2PFlag;
	}
	
	@Column(name = "SENDER_AGENT_ACCOUNT_NO")
	public String getSenderAgentAccountNo() {
	   return senderAgentAccountNo;
	}
		
	public void setSenderAgentAccountNo(String senderAgentAccountNo) {
		this.senderAgentAccountNo = senderAgentAccountNo;
	}

	@Column(name = "RECIPIENT_AGENT_ACCOUNT_NO")
	public String getRecipientAgentAccountNo() {
	   return recipientAgentAccountNo;
	}
		
	public void setRecipientAgentAccountNo(String recipientAgentAccountNo) {
	   this.recipientAgentAccountNo = recipientAgentAccountNo;
	}	
   
   @Column(name = "BILL_AMOUNT")
   public Double getBillAmount() {
	   return billAmount;
   }
	
   public void setBillAmount(Double billAmount) {
	   this.billAmount = billAmount;
   }

   @Column(name = "LATE_BILL_AMOUNT")
   public Double getLateBillAmount() {
	   return lateBillAmount;
   }
	
   public void setLateBillAmount(Double lateBillAmount) {
	   this.lateBillAmount = lateBillAmount;
   }

   @Column(name = "REVERSAL_FLAG")
   public Boolean getReversalFlag() {
	   return reversalFlag;
   }

   public void setReversalFlag(Boolean reversalFlag) {
	   this.reversalFlag = reversalFlag;
   }

   @Column(name = "REDEMPTION_TYPE")
   public Integer getRedemptionType() {
	   return redemptionType;
   }

   public void setRedemptionType(Integer redemptionType) {
	   this.redemptionType = redemptionType;
   }

	@Column(name = "REVERSED_BY_APP_USER_ID")
	public Long getReversedByAppUserId() {
		return reversedByAppUserId;
	}

	public void setReversedByAppUserId(Long reversedByAppUserId) {
		this.reversedByAppUserId = reversedByAppUserId;
	}

	@Column(name = "REVERSED_BY_NAME")
	public String getReversedByName() {
		return reversedByName;
	}

	public void setReversedByName(String reversedByName) {
		this.reversedByName = reversedByName;
	}

	@Column(name = "REVERSED_ON")
	public Date getReversedDate() {
		return reversedDate;
	}

	public void setReversedDate(Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	@Column(name = "REVERSED_COMMENTS")
	public String getReversedComments() {
		return reversedComments;
	}

	public void setReversedComments(String reversedComments) {
		this.reversedComments = reversedComments;
	}

	@Column(name = "FULL_REVERSAL_ALLOWED")
	public Boolean getFullReversalAllowed() {
		return fullReversalAllowed;
	}

	public void setFullReversalAllowed(Boolean fullReversalAllowed) {
		this.fullReversalAllowed = fullReversalAllowed;
	}
	
	@Column(name ="SERVICE_ID")
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name ="SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	@Column(name = "SENDER_BVS")
	public Boolean getSenderBVS() {
		return senderBVS;
	}

	public void setSenderBVS(Boolean senderBVS) {
		this.senderBVS = senderBVS;
	}

	@Column(name = "RECEIVER_BVS")
		public Boolean getReceiverBVS() {
		return receiverBVS;
	}

	   public void setReceiverBVS(Boolean receiverBVS) {
	      this.receiverBVS = receiverBVS;
	   }
	
	@javax.persistence.Transient
	   public Date getstartDate() {
		return startDate;
	}

	@javax.persistence.Transient
	public void setstartDate(Date sDate) {
		this.startDate = sDate;
	}

	@javax.persistence.Transient
	public Date getendDate() {
		return endDate;
	}

	@javax.persistence.Transient
	public void setendDate(Date uDate) {
		this.endDate = uDate;
	}
	
	   @Column(name = "BUSINESS_DATE")
	   public Date getBusinessDate() {
	      return businessDate;
	   }

	   public void setBusinessDate(Date businessDate) {
	      this.businessDate = businessDate;
	   }

	@Column(name = "BLB_SETTLE_COMM")
	public Double getBlbSettlementCommission() {
		return blbSettlementCommission;
	}

	public void setBlbSettlementCommission(Double blbSettlementCommission) {
		this.blbSettlementCommission = blbSettlementCommission;
	}
   @Column(name = "FONEPAY_TRANSACTION_CODE")
   public String getFonepayTransactionCode() {
      return fonepayTransactionCode;
   }

   public void setFonepayTransactionCode(String fonepayTransactionCode) {
      this.fonepayTransactionCode = fonepayTransactionCode;
   }
   @Column(name = "FONEPAY_TRANSACTION_TYPE")
   public Long getFonepaysettelmentType() {
      return fonepaysettelmentType;
   }

   public void setFonepaysettelmentType(Long fonepaysettelmentType) {
      this.fonepaysettelmentType = fonepaysettelmentType;
   }
   @Column(name = "TERMINAL_ID")
   public String getTerminalId() {
      return terminalId;
   }

   public void setTerminalId(String terminalId) {
      this.terminalId = terminalId;
   }

    @Column(name = "EXTERNAL_PRODUCT_NAME")
    public String getExternalProductName() {
        return externalProductName;
    }

    public void setExternalProductName(String externalProductName) {
        this.externalProductName = externalProductName;
    }

   @Column(name = "SENDING_REGION")
   public Long getSendingRegion() {
      return sendingRegion;
   }

   public void setSendingRegion(Long sendingRegion) {
      this.sendingRegion = sendingRegion;
   }

   @Column(name = "SENDER_DISTRIBUTOR_ID")
   public Long getSenderDistributorId() {
      return senderDistributorId;
   }

   public void setSenderDistributorId(Long senderDistributorId) {
      this.senderDistributorId = senderDistributorId;
   }


   @Column(name = "SENDER_SERVICE_OP_ID")
   public Long getSenderServiceOPId() {
      return senderServiceOPId;
   }

   public void setSenderServiceOPId(Long senderServiceOPId) {
      this.senderServiceOPId = senderServiceOPId;
   }

   @Column(name = "RECEIVING_REGION")
   public Long getReceivingRegion() {
      return receivingRegion;
   }

   public void setReceivingRegion(Long receivingRegion) {
      this.receivingRegion = receivingRegion;
   }

   @Column(name = "RECEIVER_DISTRIBUTOR_ID")
   public Long getReceiverDistributorId() {
      return receiverDistributorId;
   }

   public void setReceiverDistributorId(Long receiverDistributorId) {
      this.receiverDistributorId = receiverDistributorId;
   }

   @Column(name = "RECEIVER_SERVICE_OP_ID")
   public Long getReceiverServiceOPId() {
      return receiverServiceOPId;
   }

   public void setReceiverServiceOPId(Long receiverServiceOPId) {
      this.receiverServiceOPId = receiverServiceOPId;
   }

   @Column(name = "SENDER_REGION_NAME")
   public String getSendingRegionName() {
      return sendingRegionName;
   }

   public void setSendingRegionName(String sendingRegionName) {
      this.sendingRegionName = sendingRegionName;
   }

   @Column(name = "RECEIVER_REGION_NAME")
   public String getReceivingRegionName() {
      return receivingRegionName;
   }

   public void setReceivingRegionName(String receivingRegionName) {
      this.receivingRegionName = receivingRegionName;
   }

   @Column(name = "SENDER_DISTRIBUTOR_NAME")
   public String getSenderDistributorName() {
      return senderDistributorName;
   }

   public void setSenderDistributorName(String senderDistributorName) {
      this.senderDistributorName = senderDistributorName;
   }

   @Column(name = "SENDER_SERVICE_OP_NAME")
   public String getSenderServiceOPName() {
      return senderServiceOPName;
   }

   public void setSenderServiceOPName(String senderServiceOPName) {
      this.senderServiceOPName = senderServiceOPName;
   }

   @Column(name = "RECEIVER_DISTRIBUTOR_NAME")
   public String getReceiverDistributorName() {
      return receiverDistributorName;
   }

   public void setReceiverDistributorName(String receiverDistributorName) {
      this.receiverDistributorName = receiverDistributorName;
   }

   @Column(name = "RECEIVER_SERVICE_OP_NAME")
   public String getReceiverServiceOPName() {
      return receiverServiceOPName;
   }

   public void setReceiverServiceOPName(String receiverServiceOPName) {
      this.receiverServiceOPName = receiverServiceOPName;
   }

   @Column(name = "RECEIVER_AREA_ID")
   public Long getReceiverAreaId() {
      return receiverAreaId;
   }

   public void setReceiverAreaId(Long receiverAreaId) {
      this.receiverAreaId = receiverAreaId;
   }

   @Column(name = "SENDER_AREA_ID")
   public Long getSenderAreaId() {
      return senderAreaId;
   }

   public void setSenderAreaId(Long senderAreaId) {
      this.senderAreaId = senderAreaId;
   }

   @Column(name = "SENDER_AREA_NAME")
   public String getSenderAreaName() {
      return senderAreaName;
   }

   public void setSenderAreaName(String senderAreaName) {
      this.senderAreaName = senderAreaName;
   }

   @Column(name = "RECEIVER_AREA_NAME")
   public String getReceiverAreaName() {
      return receiverAreaName;
   }

   public void setReceiverAreaName(String receiverAreaName) {
      this.receiverAreaName = receiverAreaName;
   }

   @Column(name = "SCO_COMMISSION")
   public Double getScoCommission() {
      return scoCommission;
   }

   public void setScoCommission(Double scoCommission) {
      this.scoCommission = scoCommission;
   }

   @Column(name = "SENDER_RET_CONTACT_ID")
   public Long getSenderRetailerContactId() {
      return senderRetailerContactId;
   }

   public void setSenderRetailerContactId(Long senderRetailerContactId) {
      this.senderRetailerContactId = senderRetailerContactId;
   }

   @Column(name = "SENDER_RETAILER_ID")
   public Long getSenderRetailerId() {
      return senderRetailerId;
   }

   public void setSenderRetailerId(Long senderRetailerId) {
      this.senderRetailerId = senderRetailerId;
   }

   @Column(name = "RECEIVER_RET_CONTACT_ID")
   public Long getRecipientRetailerContactId() {
      return recipientRetailerContactId;
   }

   public void setRecipientRetailerContactId(Long recipientRetailerContactId) {
      this.recipientRetailerContactId = recipientRetailerContactId;
   }

   @Column(name = "RECEIVER_RETAILER_ID")
   public Long getRecipientRetailerId() {
      return recipientRetailerId;
   }

   public void setRecipientRetailerId(Long recipientRetailerId) {
      this.recipientRetailerId = recipientRetailerId;
   }

   @Column(name = "SENDER_DISTRIBUTOR_LEVEL_ID")
   public Long getSenderDistributorLevelId() {
      return senderDistributorLevelId;
   }

   public void setSenderDistributorLevelId(Long senderDistributorLevelId) {
      this.senderDistributorLevelId = senderDistributorLevelId;
   }

   @Column(name = "SENDER_DIST_LEVEL_NAME")
   public String getSenderDistributorLevelName() {
      return senderDistributorLevelName;
   }

   public void setSenderDistributorLevelName(String senderDistributorLevelName) {
      this.senderDistributorLevelName = senderDistributorLevelName;
   }

   @Column(name = "RECEIVER_DISTRIBUTOR_LEVEL_ID")
   public Long getRecipientDistributorLevelId() {
      return recipientDistributorLevelId;
   }

   public void setRecipientDistributorLevelId(Long recipientDistributorLevelId) {
      this.recipientDistributorLevelId = recipientDistributorLevelId;
   }

   @Column(name = "RECIPIENT_DIST_LEVEL_NAME")
   public String getRecipientDistributorLevelName() {
      return recipientDistributorLevelName;
   }

   public void setRecipientDistributorLevelName(String recipientDistributorLevelName) {
      this.recipientDistributorLevelName = recipientDistributorLevelName;
   }

   @Column(name = "SENDER_RET_CONTACT_NAME")
   public String getSenderRetailerContactName() {
      return senderRetailerContactName;
   }

   public void setSenderRetailerContactName(String senderRetailerContactName) {
      this.senderRetailerContactName = senderRetailerContactName;
   }

   @Column(name = "SENDER_RETAILER_NAME")
   public String getSenderRetailerName() {
      return senderRetailerName;
   }

   public void setSenderRetailerName(String senderRetailerName) {
      this.senderRetailerName = senderRetailerName;
   }

   @Column(name = "RECEIVER_RET_CONTACT_NAME")
   public String getRecipientRetailerContactName() {
      return recipientRetailerContactName;
   }

   public void setRecipientRetailerContactName(String recipientRetailerContactName) {
      this.recipientRetailerContactName = recipientRetailerContactName;
   }

   @Column(name = "RECEIVER_RETAILER_NAME")
   public String getRecipientRetailerName() {
      return recipientRetailerName;
   }

   public void setRecipientRetailerName(String recipientRetailerName) {
      this.recipientRetailerName = recipientRetailerName;
   }

   @Column(name = "SENDER_PARENT_RET_CONTACT_ID")
   public Long getSenderParentRetailerContactId() {
      return senderParentRetailerContactId;
   }

   public void setSenderParentRetailerContactId(Long senderParentRetailerContactId) {
      this.senderParentRetailerContactId = senderParentRetailerContactId;
   }

   @Column(name = "RECEIVER_PARENT_RET_CONTACT_ID")
   public Long getReceiverParentRetailerContactId() {
      return receiverParentRetailerContactId;
   }

   public void setReceiverParentRetailerContactId(Long receiverParentRetailerContactId) {
      this.receiverParentRetailerContactId = receiverParentRetailerContactId;
   }

   @Column(name = "VEHICLE_REG_NO")
   public String getVehicleRegNo() {
      return vehicleRegNo;
   }

   public void setVehicleRegNo(String vehicleRegNo) {
      this.vehicleRegNo = vehicleRegNo;
   }

   @Column(name = "VEHICLE_CHESIS_NO")
   public String getVehicleChesisNo() {
      return vehicleChesisNo;
   }

   public void setVehicleChesisNo(String vehicleChesisNo) {
      this.vehicleChesisNo = vehicleChesisNo;
   }

   @Column(name = "EXCISE_ASSESSMENT_NUMBER")
   public String getExciseAssessmentNumber() {
      return exciseAssessmentNumber;
   }

   public void setExciseAssessmentNumber(String exciseAssessmentNumber) {
      this.exciseAssessmentNumber = exciseAssessmentNumber;
   }

   @Column(name = "EXCISE_ASSESSMENT_AMOUNT")
   public Double getExciseAssessmentAmount() {
      return exciseAssessmentAmount;
   }

   public void setExciseAssessmentAmount(Double exciseAssessmentAmount) {
      this.exciseAssessmentAmount = exciseAssessmentAmount;
   }

   @Column(name = "EXCISE_CHALLAN_NO")
   public String getExciseChallanNo() {
      return exciseChallanNo;
   }

   public void setExciseChallanNo(String exciseChallanNo) {
      this.exciseChallanNo = exciseChallanNo;
   }

   @Column(name = "CHANNEL_ID")
   public String getChannelId() {
      return channelId;
   }

   public void setChannelId(String channelId) {
      this.channelId = channelId;
   }

   @Column(name = "NIFQ")
   public String getnIfq() {
      return nIfq;
   }

   public void setnIfq(String nIfq) {
      this.nIfq = nIfq;
   }

   @Column(name = "MINUTAIE_COUNT")
   public String getMintaieCount() {
      return mintaieCount;
   }

   public void setMintaieCount(String mintaieCount) {
      this.mintaieCount = mintaieCount;
   }

   @Column(name = "TRANS_PURPOSE_ID")
   public Long getTransactionPurposeId() {
      return transactionPurposeId;
   }

   public void setTransactionPurposeId(Long transactionPurposeId) {
      this.transactionPurposeId = transactionPurposeId;
   }

   @Column(name = "CARD_NO")
   public String getDebitCardNumber() {
      return debitCardNumber;
   }

   public void setDebitCardNumber(String debitCardNumber) {
      this.debitCardNumber = debitCardNumber;
   }

   @Column(name = "TO_BANK_IMD")
   public String getToBankImd() { return toBankImd; }

   public void setToBankImd(String toBankImd) { this.toBankImd = toBankImd; }


   @Column(name = "RECIPIENT_ACCOUNT_TITLE")
   public String getRecipientAccountTitle() { return recipientAccountTitle; }

   public void setRecipientAccountTitle(String recipientAccountTitle) { this.recipientAccountTitle = recipientAccountTitle; }


   @Column(name = "MAC_ADDRESS")
   public String getMacAddress() { return macAddress; }

   public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

   @Column(name = "IP_ADDRESS")
   public String getIpAddress() { return ipAddress; }

   public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

   @Column(name = "LATITUDE")
   public String getLatitude() { return latitude; }

   public void setLatitude(String latitude) { this.latitude = latitude; }

   @Column(name = "LONGITUDE")
   public String getLongitude() { return longitude; }

   public void setLongitude(String longitude) { this.longitude = longitude; }

   @Column(name = "IMEI_NUMBER")
   public String getImeiNumber() { return imeiNumber; }

   public void setImeiNumber(String imeiNumber) { this.imeiNumber = imeiNumber; }

   @Column(name = "PRODUCT_THRESHOLD_CHARGES")
   public Double getProductThresholdCharges() {
      return productThresholdCharges;
   }


   @Column(name = "RESERVED3")
   public String getReserved3() {
      return reserved3;
   }

   public void setReserved3(String reserved3) {
      this.reserved3 = reserved3;
   }

   @Column(name = "RESERVED4")
   public String getReserved4() {
      return reserved4;
   }

   public void setReserved4(String reserved4) {
      this.reserved4 = reserved4;
   }
   @Column(name = "RESERVED5")
   public String getReserved5() {
      return reserved5;
   }

   public void setReserved5(String reserved5) {
      this.reserved5 = reserved5;
   }

   public void setProductThresholdCharges(Double productThresholdCharges) {
      this.productThresholdCharges = productThresholdCharges;
   }

   @Column(name = "RESERVED1")
   public String getReserved1() {
      return reserved1;
   }

   public void setReserved1(String reserved1) {
      this.reserved1 = reserved1;
   }

   @Column(name = "RESERVED2")
   public String getReserved2() {
      return reserved2;
   }

   public void setReserved2(String reserved2) {
      this.reserved2 = reserved2;
   }

   @Column(name = "RESERVED6")
   public String getReserved6() {
      return reserved6;
   }

   public void setReserved6(String reserved6) {
      this.reserved6 = reserved6;
   }

   @Column(name = "RESERVED7")
   public String getReserved7() {
      return reserved7;
   }

   public void setReserved7(String reserved7) {
      this.reserved7 = reserved7;
   }

   @Column(name = "RESERVED8")
   public String getReserved8() {
      return reserved8;
   }

   public void setReserved8(String reserved8) {
      this.reserved8 = reserved8;
   }

   @Column(name = "RESERVED9")
   public String getReserved9() {
      return reserved9;
   }

   public void setReserved9(String reserved9) {
      this.reserved9 = reserved9;
   }

   @Column(name = "RESERVED10")
   public String getReserved10() {
      return reserved10;
   }

   public void setReserved10(String reserved10) {
      this.reserved10 = reserved10;
   }
}