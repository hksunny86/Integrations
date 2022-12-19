package com.inov8.microbank.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransactionModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionModel"
 */
@XmlRootElement(name="transactionModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="TRANSACTION_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="TRANSACTION_seq") } )
//@javax.persistence.SequenceGenerator(name = "TRANSACTION_seq",sequenceName = "TRANSACTION_seq")
@Table(name = "TRANSACTION")
public class TransactionModel extends BasePersistableModel {
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -524509700850133071L;
private TransactionTypeModel transactionTypeIdTransactionTypeModel;
   private TransactionCodeModel transactionCodeIdTransactionCodeModel;
   private SwitchModel processingSwitchIdSwitchModel;
   private SupplierProcessingStatusModel supProcessingStatusIdSupplierProcessingStatusModel;
   private SmartMoneyAccountModel smartMoneyAccountIdSmartMoneyAccountModel;
   private RetailerContactModel toRetContactIdRetailerContactModel;
   private RetailerContactModel fromRetContactIdRetailerContactModel;
   private RetailerModel retailerIdRetailerModel;
   private PaymentModeModel paymentModeIdPaymentModeModel;
   private OperatorModel operatorIdOperatorModel;
   private DistributorContactModel fromDistContactIdDistributorContactModel;
   private DistributorContactModel toDistContactIdDistributorContactModel;
   private DistributorContactModel distributorNmContactIdDistributorContactModel;
   private DistributorModel distributorIdDistributorModel;
   private DistributorModel toDistributorIdDistributorModel;
   private DeviceTypeModel deviceTypeIdDeviceTypeModel;
   private CustomerModel customerIdCustomerModel;
   private BankModel processingBankIdBankModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private RetailerModel toRetailerIdRetailerModel;

   private Collection<IssueModel> transactionIdIssueModelList = new ArrayList<IssueModel>();
   private Collection<TransactionDetailModel> transactionIdTransactionDetailModelList = new ArrayList<TransactionDetailModel>();

   private Long transactionId;
   private String mfsId;
   private String fromDistContactName;
   private String fromDistContactMobNo;
   private String fromRetContactName;
   private String fromRetContactMobNo;
   private String toRetContactMobNo;
   private String toRetContactName;
   private String toDistContactMobNo;
   private String toDistContactName;
   private String customerMobileNo;
   private String bankResponseCode;
   private Double totalAmount;
   private Double totalCommissionAmount;
   private Double transactionAmount;
   private Double discountAmount;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
   private String notificationMobileNo;
   private String confirmationMessage;
   private Boolean issue;
   private String saleMobileNo;
   private String bankAccountNo;
   private Long handlerId;
   private String handlerMfsId;
   
   private Date businessDate;


   @Column(name = "BUSINESS_DATE")
   public Date getBusinessDate() {
      return businessDate;
   }

   public void setBusinessDate(Date businessDate) {
      this.businessDate = businessDate;
   }

   /**
    * Default constructor.
    */
   public TransactionModel() {
   }   

   public TransactionModel(Double txAmount) {
	   transactionAmount = txAmount;
   }

   public TransactionModel(Long transactionId) {
	   this.transactionId = transactionId;
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
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTION_seq")
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
    * Returns the value of the <code>fromDistContactName</code> property.
    *
    */
      @Column(name = "FROM_DIST_CONTACT_NAME"  , length=50 )
   public String getFromDistContactName() {
      return fromDistContactName;
   }

   /**
    * Sets the value of the <code>fromDistContactName</code> property.
    *
    * @param fromDistContactName the value for the <code>fromDistContactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFromDistContactName(String fromDistContactName) {
      this.fromDistContactName = fromDistContactName;
   }

   /**
    * Returns the value of the <code>fromDistContactMobNo</code> property.
    *
    */
      @Column(name = "FROM_DIST_CONTACT_MOB_NO"  , length=50 )
   public String getFromDistContactMobNo() {
      return fromDistContactMobNo;
   }

   /**
    * Sets the value of the <code>fromDistContactMobNo</code> property.
    *
    * @param fromDistContactMobNo the value for the <code>fromDistContactMobNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFromDistContactMobNo(String fromDistContactMobNo) {
      this.fromDistContactMobNo = fromDistContactMobNo;
   }

   /**
    * Returns the value of the <code>fromRetContactName</code> property.
    *
    */
      @Column(name = "FROM_RET_CONTACT_NAME"  , length=50 )
   public String getFromRetContactName() {
      return fromRetContactName;
   }

   /**
    * Sets the value of the <code>fromRetContactName</code> property.
    *
    * @param fromRetContactName the value for the <code>fromRetContactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFromRetContactName(String fromRetContactName) {
      this.fromRetContactName = fromRetContactName;
   }

   /**
    * Returns the value of the <code>fromRetContactMobNo</code> property.
    *
    */
      @Column(name = "FROM_RET_CONTACT_MOB_NO"  , length=50 )
   public String getFromRetContactMobNo() {
      return fromRetContactMobNo;
   }

   /**
    * Sets the value of the <code>fromRetContactMobNo</code> property.
    *
    * @param fromRetContactMobNo the value for the <code>fromRetContactMobNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFromRetContactMobNo(String fromRetContactMobNo) {
      this.fromRetContactMobNo = fromRetContactMobNo;
   }

   /**
    * Returns the value of the <code>toRetContactMobNo</code> property.
    *
    */
      @Column(name = "TO_RET_CONTACT_MOB_NO"  , length=50 )
   public String getToRetContactMobNo() {
      return toRetContactMobNo;
   }

   /**
    * Sets the value of the <code>toRetContactMobNo</code> property.
    *
    * @param toRetContactMobNo the value for the <code>toRetContactMobNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setToRetContactMobNo(String toRetContactMobNo) {
      this.toRetContactMobNo = toRetContactMobNo;
   }

   /**
    * Returns the value of the <code>toRetContactName</code> property.
    *
    */
      @Column(name = "TO_RET_CONTACT_NAME"  , length=50 )
   public String getToRetContactName() {
      return toRetContactName;
   }

   /**
    * Sets the value of the <code>toRetContactName</code> property.
    *
    * @param toRetContactName the value for the <code>toRetContactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setToRetContactName(String toRetContactName) {
      this.toRetContactName = toRetContactName;
   }

   /**
    * Returns the value of the <code>toDistContactMobNo</code> property.
    *
    */
      @Column(name = "TO_DIST_CONTACT_MOB_NO"  , length=50 )
   public String getToDistContactMobNo() {
      return toDistContactMobNo;
   }

   /**
    * Sets the value of the <code>toDistContactMobNo</code> property.
    *
    * @param toDistContactMobNo the value for the <code>toDistContactMobNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setToDistContactMobNo(String toDistContactMobNo) {
      this.toDistContactMobNo = toDistContactMobNo;
   }

   /**
    * Returns the value of the <code>toDistContactName</code> property.
    *
    */
      @Column(name = "TO_DIST_CONTACT_NAME"  , length=50 )
   public String getToDistContactName() {
      return toDistContactName;
   }

   /**
    * Sets the value of the <code>toDistContactName</code> property.
    *
    * @param toDistContactName the value for the <code>toDistContactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setToDistContactName(String toDistContactName) {
      this.toDistContactName = toDistContactName;
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
    * @param customerMobileNo the value for the <code>customerMobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="20"
    */

   public void setBankAccountNo(String bankAccountNo) {
      this.bankAccountNo = bankAccountNo;
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
		    * @spring.validator type="required"
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
		    * @spring.validator type="required"
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
    * @param transactionAmount the value for the <code>discountAmount</code> property
    *    
		    * @spring.validator type="required"
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
    * Returns the value of the <code>discountAmount</code> property.
    *
    */
      @Column(name = "DISCOUNT_AMOUNT" , nullable = false )
   public Double getDiscountAmount() {
      return discountAmount;
   }

   /**
    * Sets the value of the <code>transactionAmount</code> property.
    *
    * @param transactionAmount the value for the <code>transactionAmount</code> property
    *    
		    * @spring.validator type="required"
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
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   /**
    * Sets the value of the <code>versionNo</code> property.
    *
    * @param versionNo the value for the <code>versionNo</code> property
    *    
		    */

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
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
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="13"
    */

   public void setNotificationMobileNo(String notificationMobileNo) {
      this.notificationMobileNo = notificationMobileNo;
   }

   /**
    * Returns the value of the <code>confirmationMessage</code> property.
    *
    */
      @Column(name = "CONFIRMATION_MESSAGE" , nullable = false , length=250 )
   public String getConfirmationMessage() {
      return confirmationMessage;
   }

   /**
    * Sets the value of the <code>confirmationMessage</code> property.
    *
    * @param confirmationMessage the value for the <code>confirmationMessage</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setConfirmationMessage(String confirmationMessage) {
      this.confirmationMessage = confirmationMessage;
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
    * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_TYPE_ID")    
   public TransactionTypeModel getRelationTransactionTypeIdTransactionTypeModel(){
      return transactionTypeIdTransactionTypeModel;
   }
    
   /**
    * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public TransactionTypeModel getTransactionTypeIdTransactionTypeModel(){
      return getRelationTransactionTypeIdTransactionTypeModel();
   }

   /**
    * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionTypeIdTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
      this.transactionTypeIdTransactionTypeModel = transactionTypeModel;
   }
   
   /**
    * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionTypeIdTransactionTypeModel(TransactionTypeModel transactionTypeModel) {
      if(null != transactionTypeModel)
      {
      	setRelationTransactionTypeIdTransactionTypeModel((TransactionTypeModel)transactionTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_CODE_ID")    
   public TransactionCodeModel getRelationTransactionCodeIdTransactionCodeModel(){
      return transactionCodeIdTransactionCodeModel;
   }
    
   /**
    * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public TransactionCodeModel getTransactionCodeIdTransactionCodeModel(){
      return getRelationTransactionCodeIdTransactionCodeModel();
   }

   /**
    * Sets the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @param transactionCodeModel a value for <code>transactionCodeIdTransactionCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionCodeIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      this.transactionCodeIdTransactionCodeModel = transactionCodeModel;
   }
   
   /**
    * Sets the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @param transactionCodeModel a value for <code>transactionCodeIdTransactionCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionCodeIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      if(null != transactionCodeModel)
      {
      	setRelationTransactionCodeIdTransactionCodeModel((TransactionCodeModel)transactionCodeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>processingSwitchIdSwitchModel</code> relation property.
    *
    * @return the value of the <code>processingSwitchIdSwitchModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PROCESSING_SWITCH_ID")    
   public SwitchModel getRelationProcessingSwitchIdSwitchModel(){
      return processingSwitchIdSwitchModel;
   }
    
   /**
    * Returns the value of the <code>processingSwitchIdSwitchModel</code> relation property.
    *
    * @return the value of the <code>processingSwitchIdSwitchModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SwitchModel getProcessingSwitchIdSwitchModel(){
      return getRelationProcessingSwitchIdSwitchModel();
   }

   /**
    * Sets the value of the <code>processingSwitchIdSwitchModel</code> relation property.
    *
    * @param switchModel a value for <code>processingSwitchIdSwitchModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProcessingSwitchIdSwitchModel(SwitchModel switchModel) {
      this.processingSwitchIdSwitchModel = switchModel;
   }
   
   /**
    * Sets the value of the <code>processingSwitchIdSwitchModel</code> relation property.
    *
    * @param switchModel a value for <code>processingSwitchIdSwitchModel</code>.
    */
   @javax.persistence.Transient
   public void setProcessingSwitchIdSwitchModel(SwitchModel switchModel) {
      if(null != switchModel)
      {
      	setRelationProcessingSwitchIdSwitchModel((SwitchModel)switchModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>supProcessingStatusIdSupplierProcessingStatusModel</code> relation property.
    *
    * @return the value of the <code>supProcessingStatusIdSupplierProcessingStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SUP_PROCESSING_STATUS_ID")    
   public SupplierProcessingStatusModel getRelationSupProcessingStatusIdSupplierProcessingStatusModel(){
      return supProcessingStatusIdSupplierProcessingStatusModel;
   }
    
   /**
    * Returns the value of the <code>supProcessingStatusIdSupplierProcessingStatusModel</code> relation property.
    *
    * @return the value of the <code>supProcessingStatusIdSupplierProcessingStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SupplierProcessingStatusModel getSupProcessingStatusIdSupplierProcessingStatusModel(){
      return getRelationSupProcessingStatusIdSupplierProcessingStatusModel();
   }

   /**
    * Sets the value of the <code>supProcessingStatusIdSupplierProcessingStatusModel</code> relation property.
    *
    * @param supplierProcessingStatusModel a value for <code>supProcessingStatusIdSupplierProcessingStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSupProcessingStatusIdSupplierProcessingStatusModel(SupplierProcessingStatusModel supplierProcessingStatusModel) {
      this.supProcessingStatusIdSupplierProcessingStatusModel = supplierProcessingStatusModel;
   }
   
   /**
    * Sets the value of the <code>supProcessingStatusIdSupplierProcessingStatusModel</code> relation property.
    *
    * @param supplierProcessingStatusModel a value for <code>supProcessingStatusIdSupplierProcessingStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setSupProcessingStatusIdSupplierProcessingStatusModel(SupplierProcessingStatusModel supplierProcessingStatusModel) {
      if(null != supplierProcessingStatusModel)
      {
      	setRelationSupProcessingStatusIdSupplierProcessingStatusModel((SupplierProcessingStatusModel)supplierProcessingStatusModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>smartMoneyAccountIdSmartMoneyAccountModel</code> relation property.
    *
    * @return the value of the <code>smartMoneyAccountIdSmartMoneyAccountModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SMART_MONEY_ACCOUNT_ID")    
   public SmartMoneyAccountModel getRelationSmartMoneyAccountIdSmartMoneyAccountModel(){
      return smartMoneyAccountIdSmartMoneyAccountModel;
   }
    
   /**
    * Returns the value of the <code>smartMoneyAccountIdSmartMoneyAccountModel</code> relation property.
    *
    * @return the value of the <code>smartMoneyAccountIdSmartMoneyAccountModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SmartMoneyAccountModel getSmartMoneyAccountIdSmartMoneyAccountModel(){
      return getRelationSmartMoneyAccountIdSmartMoneyAccountModel();
   }

   /**
    * Sets the value of the <code>smartMoneyAccountIdSmartMoneyAccountModel</code> relation property.
    *
    * @param smartMoneyAccountModel a value for <code>smartMoneyAccountIdSmartMoneyAccountModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSmartMoneyAccountIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      this.smartMoneyAccountIdSmartMoneyAccountModel = smartMoneyAccountModel;
   }
   
   /**
    * Sets the value of the <code>smartMoneyAccountIdSmartMoneyAccountModel</code> relation property.
    *
    * @param smartMoneyAccountModel a value for <code>smartMoneyAccountIdSmartMoneyAccountModel</code>.
    */
   @javax.persistence.Transient
   public void setSmartMoneyAccountIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      if(null != smartMoneyAccountModel)
      {
      	setRelationSmartMoneyAccountIdSmartMoneyAccountModel((SmartMoneyAccountModel)smartMoneyAccountModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>toRetContactIdRetailerContactModel</code> relation property.
    *
    * @return the value of the <code>toRetContactIdRetailerContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TO_RET_CONTACT_ID")    
   public RetailerContactModel getRelationToRetContactIdRetailerContactModel(){
      return toRetContactIdRetailerContactModel;
   }
    
   /**
    * Returns the value of the <code>toRetContactIdRetailerContactModel</code> relation property.
    *
    * @return the value of the <code>toRetContactIdRetailerContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerContactModel getToRetContactIdRetailerContactModel(){
      return getRelationToRetContactIdRetailerContactModel();
   }

   /**
    * Sets the value of the <code>toRetContactIdRetailerContactModel</code> relation property.
    *
    * @param retailerContactModel a value for <code>toRetContactIdRetailerContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationToRetContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      this.toRetContactIdRetailerContactModel = retailerContactModel;
   }
   
   /**
    * Sets the value of the <code>toRetContactIdRetailerContactModel</code> relation property.
    *
    * @param retailerContactModel a value for <code>toRetContactIdRetailerContactModel</code>.
    */
   @javax.persistence.Transient
   public void setToRetContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      if(null != retailerContactModel)
      {
      	setRelationToRetContactIdRetailerContactModel((RetailerContactModel)retailerContactModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>fromRetContactIdRetailerContactModel</code> relation property.
    *
    * @return the value of the <code>fromRetContactIdRetailerContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "FROM_RET_CONTACT_ID")    
   public RetailerContactModel getRelationFromRetContactIdRetailerContactModel(){
      return fromRetContactIdRetailerContactModel;
   }
    
   /**
    * Returns the value of the <code>fromRetContactIdRetailerContactModel</code> relation property.
    *
    * @return the value of the <code>fromRetContactIdRetailerContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerContactModel getFromRetContactIdRetailerContactModel(){
      return getRelationFromRetContactIdRetailerContactModel();
   }

   /**
    * Sets the value of the <code>fromRetContactIdRetailerContactModel</code> relation property.
    *
    * @param retailerContactModel a value for <code>fromRetContactIdRetailerContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationFromRetContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      this.fromRetContactIdRetailerContactModel = retailerContactModel;
   }
   
   /**
    * Sets the value of the <code>fromRetContactIdRetailerContactModel</code> relation property.
    *
    * @param retailerContactModel a value for <code>fromRetContactIdRetailerContactModel</code>.
    */
   @javax.persistence.Transient
   public void setFromRetContactIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      if(null != retailerContactModel)
      {
      	setRelationFromRetContactIdRetailerContactModel((RetailerContactModel)retailerContactModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RETAILER_ID")    
   public RetailerModel getRelationRetailerIdRetailerModel(){
      return retailerIdRetailerModel;
   }
    
   /**
    * Returns the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerModel getRetailerIdRetailerModel(){
      return getRelationRetailerIdRetailerModel();
   }

   /**
    * Sets the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>retailerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRetailerIdRetailerModel(RetailerModel retailerModel) {
      this.retailerIdRetailerModel = retailerModel;
   }
   
   /**
    * Sets the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>retailerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRetailerIdRetailerModel(RetailerModel retailerModel) {
      if(null != retailerModel)
      {
      	setRelationRetailerIdRetailerModel((RetailerModel)retailerModel.clone());
      }      
   }
   
   
   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   /**
    * Returns the value of the <code>toRetailerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>toRetailerIdRetailerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TO_RETAILER_ID")    
   public RetailerModel getRelationToRetailerIdRetailerModel(){
      return toRetailerIdRetailerModel;
   }
    
   /**
    * Returns the value of the <code>toRetailerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>toRetailerIdRetailerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerModel getToRetailerIdRetailerModel(){
      return getRelationToRetailerIdRetailerModel();
   }

   /**
    * Sets the value of the <code>toRetailerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>toRetailerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationToRetailerIdRetailerModel(RetailerModel toRetailerModel) {
      this.toRetailerIdRetailerModel = toRetailerModel;
   }
   
   /**
    * Sets the value of the <code>retailerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>retailerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setToRetailerIdRetailerModel(RetailerModel toRetailerModel) {
      if(null != toRetailerModel)
      {
      	setRelationToRetailerIdRetailerModel((RetailerModel)toRetailerModel.clone());
      }      
   }
   
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   /**
    * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PAYMENT_MODE_ID")    
   public PaymentModeModel getRelationPaymentModeIdPaymentModeModel(){
      return paymentModeIdPaymentModeModel;
   }
    
   /**
    * Returns the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @return the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public PaymentModeModel getPaymentModeIdPaymentModeModel(){
      return getRelationPaymentModeIdPaymentModeModel();
   }

   /**
    * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPaymentModeIdPaymentModeModel(PaymentModeModel paymentModeModel) {
      this.paymentModeIdPaymentModeModel = paymentModeModel;
   }
   
   /**
    * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation property.
    *
    * @param paymentModeModel a value for <code>paymentModeIdPaymentModeModel</code>.
    */
   @javax.persistence.Transient
   public void setPaymentModeIdPaymentModeModel(PaymentModeModel paymentModeModel) {
      if(null != paymentModeModel)
      {
      	setRelationPaymentModeIdPaymentModeModel((PaymentModeModel)paymentModeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @return the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "OPERATOR_ID")    
   public OperatorModel getRelationOperatorIdOperatorModel(){
      return operatorIdOperatorModel;
   }
    
   /**
    * Returns the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @return the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public OperatorModel getOperatorIdOperatorModel(){
      return getRelationOperatorIdOperatorModel();
   }

   /**
    * Sets the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @param operatorModel a value for <code>operatorIdOperatorModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationOperatorIdOperatorModel(OperatorModel operatorModel) {
      this.operatorIdOperatorModel = operatorModel;
   }
   
   /**
    * Sets the value of the <code>operatorIdOperatorModel</code> relation property.
    *
    * @param operatorModel a value for <code>operatorIdOperatorModel</code>.
    */
   @javax.persistence.Transient
   public void setOperatorIdOperatorModel(OperatorModel operatorModel) {
      if(null != operatorModel)
      {
      	setRelationOperatorIdOperatorModel((OperatorModel)operatorModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>fromDistContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>fromDistContactIdDistributorContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "FROM_DIST_CONTACT_ID")    
   public DistributorContactModel getRelationFromDistContactIdDistributorContactModel(){
      return fromDistContactIdDistributorContactModel;
   }
    
   /**
    * Returns the value of the <code>fromDistContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>fromDistContactIdDistributorContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorContactModel getFromDistContactIdDistributorContactModel(){
      return getRelationFromDistContactIdDistributorContactModel();
   }

   /**
    * Sets the value of the <code>fromDistContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>fromDistContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationFromDistContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      this.fromDistContactIdDistributorContactModel = distributorContactModel;
   }
   
   /**
    * Sets the value of the <code>fromDistContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>fromDistContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setFromDistContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      if(null != distributorContactModel)
      {
      	setRelationFromDistContactIdDistributorContactModel((DistributorContactModel)distributorContactModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>toDistContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>toDistContactIdDistributorContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TO_DIST_CONTACT_ID")    
   public DistributorContactModel getRelationToDistContactIdDistributorContactModel(){
      return toDistContactIdDistributorContactModel;
   }
    
   /**
    * Returns the value of the <code>toDistContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>toDistContactIdDistributorContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorContactModel getToDistContactIdDistributorContactModel(){
      return getRelationToDistContactIdDistributorContactModel();
   }

   /**
    * Sets the value of the <code>toDistContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>toDistContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationToDistContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      this.toDistContactIdDistributorContactModel = distributorContactModel;
   }
   
   /**
    * Sets the value of the <code>toDistContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>toDistContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setToDistContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      if(null != distributorContactModel)
      {
      	setRelationToDistContactIdDistributorContactModel((DistributorContactModel)distributorContactModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>distributorNmContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>distributorNmContactIdDistributorContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DISTRIBUTOR_NM_CONTACT_ID")    
   public DistributorContactModel getRelationDistributorNmContactIdDistributorContactModel(){
      return distributorNmContactIdDistributorContactModel;
   }
    
   /**
    * Returns the value of the <code>distributorNmContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>distributorNmContactIdDistributorContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorContactModel getDistributorNmContactIdDistributorContactModel(){
      return getRelationDistributorNmContactIdDistributorContactModel();
   }

   /**
    * Sets the value of the <code>distributorNmContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>distributorNmContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDistributorNmContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      this.distributorNmContactIdDistributorContactModel = distributorContactModel;
   }
   
   /**
    * Sets the value of the <code>distributorNmContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>distributorNmContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setDistributorNmContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      if(null != distributorContactModel)
      {
      	setRelationDistributorNmContactIdDistributorContactModel((DistributorContactModel)distributorContactModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>distributorIdDistributorModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DISTRIBUTOR_ID")    
   public DistributorModel getRelationDistributorIdDistributorModel(){
      return distributorIdDistributorModel;
   }
    
   /**
    * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>distributorIdDistributorModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorModel getDistributorIdDistributorModel(){
      return getRelationDistributorIdDistributorModel();
   }

   /**
    * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDistributorIdDistributorModel(DistributorModel distributorModel) {
      this.distributorIdDistributorModel = distributorModel;
   }
   
   /**
    * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setDistributorIdDistributorModel(DistributorModel distributorModel) {
      if(null != distributorModel)
      {
      	setRelationDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
      }      
   }
   
   
   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   /**
    * Returns the value of the <code>toDistributorIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>toDistributorIdDistributorModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TO_DISTRIBUTOR_ID")    
   public DistributorModel getRelationToDistributorIdDistributorModel(){
      return toDistributorIdDistributorModel;
   }
    
   /**
    * Returns the value of the <code>toDistributorIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>toDistributorIdDistributorModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorModel getToDistributorIdDistributorModel(){
      return getRelationToDistributorIdDistributorModel();
   }

   /**
    * Sets the value of the <code>toDistributorIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>toDistributorIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationToDistributorIdDistributorModel(DistributorModel toDistributorModel) {
      this.toDistributorIdDistributorModel = toDistributorModel;
   }
   
   /**
    * Sets the value of the <code>toDistributorIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>toDistributorIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setToDistributorIdDistributorModel(DistributorModel toDistributorModel) {
      if(null != toDistributorModel)
      {
      	setRelationToDistributorIdDistributorModel((DistributorModel)toDistributorModel.clone());
      }      
   }
   
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   /**
    * Returns the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @return the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DEVICE_TYPE_ID")    
   public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel(){
      return deviceTypeIdDeviceTypeModel;
   }
    
   /**
    * Returns the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @return the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DeviceTypeModel getDeviceTypeIdDeviceTypeModel(){
      return getRelationDeviceTypeIdDeviceTypeModel();
   }

   /**
    * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @param deviceTypeModel a value for <code>deviceTypeIdDeviceTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      this.deviceTypeIdDeviceTypeModel = deviceTypeModel;
   }
   
   /**
    * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @param deviceTypeModel a value for <code>deviceTypeIdDeviceTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      if(null != deviceTypeModel)
      {
      	setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel)deviceTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @return the value of the <code>customerIdCustomerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CUSTOMER_ID")    
   public CustomerModel getRelationCustomerIdCustomerModel(){
      return customerIdCustomerModel;
   }
    
   /**
    * Returns the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @return the value of the <code>customerIdCustomerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CustomerModel getCustomerIdCustomerModel(){
      return getRelationCustomerIdCustomerModel();
   }

   /**
    * Sets the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @param customerModel a value for <code>customerIdCustomerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCustomerIdCustomerModel(CustomerModel customerModel) {
      this.customerIdCustomerModel = customerModel;
   }
   
   /**
    * Sets the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @param customerModel a value for <code>customerIdCustomerModel</code>.
    */
   @javax.persistence.Transient
   public void setCustomerIdCustomerModel(CustomerModel customerModel) {
      if(null != customerModel)
      {
      	setRelationCustomerIdCustomerModel((CustomerModel)customerModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>processingBankIdBankModel</code> relation property.
    *
    * @return the value of the <code>processingBankIdBankModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PROCESSING_BANK_ID")    
   public BankModel getRelationProcessingBankIdBankModel(){
      return processingBankIdBankModel;
   }
    
   /**
    * Returns the value of the <code>processingBankIdBankModel</code> relation property.
    *
    * @return the value of the <code>processingBankIdBankModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public BankModel getProcessingBankIdBankModel(){
      return getRelationProcessingBankIdBankModel();
   }

   /**
    * Sets the value of the <code>processingBankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>processingBankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProcessingBankIdBankModel(BankModel bankModel) {
      this.processingBankIdBankModel = bankModel;
   }
   
   /**
    * Sets the value of the <code>processingBankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>processingBankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setProcessingBankIdBankModel(BankModel bankModel) {
      if(null != bankModel)
      {
      	setRelationProcessingBankIdBankModel((BankModel)bankModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @return the value of the <code>updatedByAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "UPDATED_BY")    
   public AppUserModel getRelationUpdatedByAppUserModel(){
      return updatedByAppUserModel;
   }
    
   /**
    * Returns the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @return the value of the <code>updatedByAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getUpdatedByAppUserModel(){
      return getRelationUpdatedByAppUserModel();
   }

   /**
    * Sets the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>updatedByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
      this.updatedByAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>updatedByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CREATED_BY")    
   public AppUserModel getRelationCreatedByAppUserModel(){
      return createdByAppUserModel;
   }
    
   /**
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getCreatedByAppUserModel(){
      return getRelationCreatedByAppUserModel();
   }

   /**
    * Sets the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>createdByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
      this.createdByAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>createdByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setCreatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   


   /**
    * Add the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be added.
    */
    
   public void addTransactionIdIssueModel(IssueModel issueModel) {
      issueModel.setRelationTransactionIdTransactionModel(this);
      transactionIdIssueModelList.add(issueModel);
   }
   
   /**
    * Remove the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be removed.
    */
   
   public void removeTransactionIdIssueModel(IssueModel issueModel) {      
      issueModel.setRelationTransactionIdTransactionModel(null);
      transactionIdIssueModelList.remove(issueModel);      
   }

   /**
    * Get a list of related IssueModel objects of the TransactionModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionId member.
    *
    * @return Collection of IssueModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionIdTransactionModel")
   @JoinColumn(name = "TRANSACTION_ID")
   public Collection<IssueModel> getTransactionIdIssueModelList() throws Exception {
   		return transactionIdIssueModelList;
   }


   /**
    * Set a list of IssueModel related objects to the TransactionModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionId member.
    *
    * @param issueModelList the list of related objects.
    */
    public void setTransactionIdIssueModelList(Collection<IssueModel> issueModelList) throws Exception {
		this.transactionIdIssueModelList = issueModelList;
   }


   /**
    * Add the related TransactionDetailModel to this one-to-many relation.
    *
    * @param transactionDetailModel object to be added.
    */
    
   public void addTransactionIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {
      transactionDetailModel.setRelationTransactionIdTransactionModel(this);
      transactionIdTransactionDetailModelList.add(transactionDetailModel);
   }
   
   /**
    * Remove the related TransactionDetailModel to this one-to-many relation.
    *
    * @param transactionDetailModel object to be removed.
    */
   
   public void removeTransactionIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {      
      transactionDetailModel.setRelationTransactionIdTransactionModel(null);
      transactionIdTransactionDetailModelList.remove(transactionDetailModel);      
   }

   /**
    * Get a list of related TransactionDetailModel objects of the TransactionModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionId member.
    *
    * @return Collection of TransactionDetailModel objects.
    *
    */
   
//   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionIdTransactionModel")
//   @JoinColumn(name = "TRANSACTION_ID")
   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
              fetch = FetchType.LAZY,mappedBy = "relationTransactionIdTransactionModel")
   @JoinColumn(name = "TRANSACTION_ID")
   @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                                      org.hibernate.annotations.CascadeType.PERSIST,
                                      org.hibernate.annotations.CascadeType.MERGE,
                                      org.hibernate.annotations.CascadeType.REFRESH})
   public Collection<TransactionDetailModel> getTransactionIdTransactionDetailModelList() throws RuntimeException {
   		return transactionIdTransactionDetailModelList;
   }


   /**
    * Set a list of TransactionDetailModel related objects to the TransactionModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionId member.
    *
    * @param transactionDetailModelList the list of related objects.
    */
    public void setTransactionIdTransactionDetailModelList(Collection<TransactionDetailModel> transactionDetailModelList) throws Exception {
		this.transactionIdTransactionDetailModelList = transactionDetailModelList;
   }



   /**
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionTypeId() {
      if (transactionTypeIdTransactionTypeModel != null) {
         return transactionTypeIdTransactionTypeModel.getTransactionTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionTypeId</code> property.
    *
    * @param transactionTypeId the value for the <code>transactionTypeId</code> property
																													    * @spring.validator type="required"
																																																									    */
   
   @javax.persistence.Transient
   public void setTransactionTypeId(Long transactionTypeId) {
      if(transactionTypeId == null)
      {      
      	transactionTypeIdTransactionTypeModel = null;
      }
      else
      {
        transactionTypeIdTransactionTypeModel = new TransactionTypeModel();
      	transactionTypeIdTransactionTypeModel.setTransactionTypeId(transactionTypeId);
      }      
   }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionCodeId() {
      if (transactionCodeIdTransactionCodeModel != null) {
         return transactionCodeIdTransactionCodeModel.getTransactionCodeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
							    * @spring.validator type="required"
																																																																															    */
   
   @javax.persistence.Transient
   public void setTransactionCodeId(Long transactionCodeId) {
      if(transactionCodeId == null)
      {      
      	transactionCodeIdTransactionCodeModel = null;
      }
      else
      {
        transactionCodeIdTransactionCodeModel = new TransactionCodeModel();
      	transactionCodeIdTransactionCodeModel.setTransactionCodeId(transactionCodeId);
      }      
   }

   /**
    * Returns the value of the <code>switchId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProcessingSwitchId() {
      if (processingSwitchIdSwitchModel != null) {
         return processingSwitchIdSwitchModel.getSwitchId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>switchId</code> property.
    *
    * @param switchId the value for the <code>switchId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setProcessingSwitchId(Long switchId) {
      if(switchId == null)
      {      
      	processingSwitchIdSwitchModel = null;
      }
      else
      {
        processingSwitchIdSwitchModel = new SwitchModel();
      	processingSwitchIdSwitchModel.setSwitchId(switchId);
      }      
   }

   /**
    * Returns the value of the <code>supProcessingStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSupProcessingStatusId() {
      if (supProcessingStatusIdSupplierProcessingStatusModel != null) {
         return supProcessingStatusIdSupplierProcessingStatusModel.getSupProcessingStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>supProcessingStatusId</code> property.
    *
    * @param supProcessingStatusId the value for the <code>supProcessingStatusId</code> property
																																																																																			    * @spring.validator type="required"
			    */
   
   @javax.persistence.Transient
   public void setSupProcessingStatusId(Long supProcessingStatusId) {
      if(supProcessingStatusId == null)
      {      
      	supProcessingStatusIdSupplierProcessingStatusModel = null;
      }
      else
      {
        supProcessingStatusIdSupplierProcessingStatusModel = new SupplierProcessingStatusModel();
      	supProcessingStatusIdSupplierProcessingStatusModel.setSupProcessingStatusId(supProcessingStatusId);
      }      
   }

   /**
    * Returns the value of the <code>smartMoneyAccountId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSmartMoneyAccountId() {
      if (smartMoneyAccountIdSmartMoneyAccountModel != null) {
         return smartMoneyAccountIdSmartMoneyAccountModel.getSmartMoneyAccountId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>smartMoneyAccountId</code> property.
    *
    * @param smartMoneyAccountId the value for the <code>smartMoneyAccountId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
      if(smartMoneyAccountId == null)
      {      
      	smartMoneyAccountIdSmartMoneyAccountModel = null;
      }
      else
      {
        smartMoneyAccountIdSmartMoneyAccountModel = new SmartMoneyAccountModel();
      	smartMoneyAccountIdSmartMoneyAccountModel.setSmartMoneyAccountId(smartMoneyAccountId);
      }      
   }

   /**
    * Returns the value of the <code>retailerContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getToRetContactId() {
      if (toRetContactIdRetailerContactModel != null) {
         return toRetContactIdRetailerContactModel.getRetailerContactId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>retailerContactId</code> property.
    *
    * @param retailerContactId the value for the <code>retailerContactId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setToRetContactId(Long retailerContactId) {
      if(retailerContactId == null)
      {      
      	toRetContactIdRetailerContactModel = null;
      }
      else
      {
        toRetContactIdRetailerContactModel = new RetailerContactModel();
      	toRetContactIdRetailerContactModel.setRetailerContactId(retailerContactId);
      }      
   }

   /**
    * Returns the value of the <code>retailerContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getFromRetContactId() {
      if (fromRetContactIdRetailerContactModel != null) {
         return fromRetContactIdRetailerContactModel.getRetailerContactId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>retailerContactId</code> property.
    *
    * @param retailerContactId the value for the <code>retailerContactId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setFromRetContactId(Long retailerContactId) {
      if(retailerContactId == null)
      {      
      	fromRetContactIdRetailerContactModel = null;
      }
      else
      {
        fromRetContactIdRetailerContactModel = new RetailerContactModel();
      	fromRetContactIdRetailerContactModel.setRetailerContactId(retailerContactId);
      }      
   }

   /**
    * Returns the value of the <code>retailerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getRetailerId() {
      if (retailerIdRetailerModel != null) {
         return retailerIdRetailerModel.getRetailerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>retailerId</code> property.
    *
    * @param retailerId the value for the <code>retailerId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setRetailerId(Long retailerId) {
      if(retailerId == null)
      {      
      	retailerIdRetailerModel = null;
      }
      else
      {
        retailerIdRetailerModel = new RetailerModel();
      	retailerIdRetailerModel.setRetailerId(retailerId);
      }      
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   /**
    * Returns the value of the <code>toRetailerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getToRetailerId() {
      if (toRetailerIdRetailerModel != null) {
         return toRetailerIdRetailerModel.getRetailerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>retailerId</code> property.
    *
    * @param retailerId the value for the <code>retailerId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setToRetailerId(Long toRetailerId) {
      if(toRetailerId == null)
      {      
    	  toRetailerIdRetailerModel = null;
      }
      else
      {
    	  toRetailerIdRetailerModel = new RetailerModel();
    	  toRetailerIdRetailerModel.setRetailerId(toRetailerId);
      }      
   }

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   /**
    * Returns the value of the <code>paymentModeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPaymentModeId() {
      if (paymentModeIdPaymentModeModel != null) {
         return paymentModeIdPaymentModeModel.getPaymentModeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>paymentModeId</code> property.
    *
    * @param paymentModeId the value for the <code>paymentModeId</code> property
																					    * @spring.validator type="required"
																																																																	    */
   
   @javax.persistence.Transient
   public void setPaymentModeId(Long paymentModeId) {
      if(paymentModeId == null)
      {      
      	paymentModeIdPaymentModeModel = null;
      }
      else
      {
        paymentModeIdPaymentModeModel = new PaymentModeModel();
      	paymentModeIdPaymentModeModel.setPaymentModeId(paymentModeId);
      }      
   }

   /**
    * Returns the value of the <code>operatorId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getOperatorId() {
      if (operatorIdOperatorModel != null) {
         return operatorIdOperatorModel.getOperatorId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>operatorId</code> property.
    *
    * @param operatorId the value for the <code>operatorId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setOperatorId(Long operatorId) {
      if(operatorId == null)
      {      
      	operatorIdOperatorModel = null;
      }
      else
      {
        operatorIdOperatorModel = new OperatorModel();
      	operatorIdOperatorModel.setOperatorId(operatorId);
      }      
   }

   /**
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getFromDistContactId() {
      if (fromDistContactIdDistributorContactModel != null) {
         return fromDistContactIdDistributorContactModel.getDistributorContactId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setFromDistContactId(Long distributorContactId) {
      if(distributorContactId == null)
      {      
      	fromDistContactIdDistributorContactModel = null;
      }
      else
      {
        fromDistContactIdDistributorContactModel = new DistributorContactModel();
      	fromDistContactIdDistributorContactModel.setDistributorContactId(distributorContactId);
      }      
   }

   /**
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getToDistContactId() {
      if (toDistContactIdDistributorContactModel != null) {
         return toDistContactIdDistributorContactModel.getDistributorContactId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setToDistContactId(Long distributorContactId) {
      if(distributorContactId == null)
      {      
      	toDistContactIdDistributorContactModel = null;
      }
      else
      {
        toDistContactIdDistributorContactModel = new DistributorContactModel();
      	toDistContactIdDistributorContactModel.setDistributorContactId(distributorContactId);
      }      
   }

   /**
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDistributorNmContactId() {
      if (distributorNmContactIdDistributorContactModel != null) {
         return distributorNmContactIdDistributorContactModel.getDistributorContactId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setDistributorNmContactId(Long distributorContactId) {
      if(distributorContactId == null)
      {      
      	distributorNmContactIdDistributorContactModel = null;
      }
      else
      {
        distributorNmContactIdDistributorContactModel = new DistributorContactModel();
      	distributorNmContactIdDistributorContactModel.setDistributorContactId(distributorContactId);
      }      
   }

   /**
    * Returns the value of the <code>distributorId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDistributorId() {
      if (distributorIdDistributorModel != null) {
         return distributorIdDistributorModel.getDistributorId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorId</code> property.
    *
    * @param distributorId the value for the <code>distributorId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setDistributorId(Long distributorId) {
      if(distributorId == null)
      {      
      	distributorIdDistributorModel = null;
      }
      else
      {
        distributorIdDistributorModel = new DistributorModel();
      	distributorIdDistributorModel.setDistributorId(distributorId);
      }      
   }

   
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   /**
    * Returns the value of the <code>toDistributorId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getToDistributorId() {
      if (toDistributorIdDistributorModel != null) {
         return toDistributorIdDistributorModel.getDistributorId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>toDistributorId</code> property.
    *
    * @param distributorId the value for the <code>toDistributorId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setToDistributorId(Long toDistributorId) {
      if(toDistributorId == null)
      {      
    	  toDistributorIdDistributorModel = null;
      }
      else
      {
    	  toDistributorIdDistributorModel = new DistributorModel();
    	  toDistributorIdDistributorModel.setDistributorId(toDistributorId);
      }      
   }

   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   /**
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDeviceTypeId() {
      if (deviceTypeIdDeviceTypeModel != null) {
         return deviceTypeIdDeviceTypeModel.getDeviceTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
					    * @spring.validator type="required"
																																																																																	    */
   
   @javax.persistence.Transient
   public void setDeviceTypeId(Long deviceTypeId) {
      if(deviceTypeId == null)
      {      
      	deviceTypeIdDeviceTypeModel = null;
      }
      else
      {
        deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
      	deviceTypeIdDeviceTypeModel.setDeviceTypeId(deviceTypeId);
      }      
   }

   /**
    * Returns the value of the <code>customerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCustomerId() {
      if (customerIdCustomerModel != null) {
         return customerIdCustomerModel.getCustomerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setCustomerId(Long customerId) {
      if(customerId == null)
      {      
      	customerIdCustomerModel = null;
      }
      else
      {
        customerIdCustomerModel = new CustomerModel();
      	customerIdCustomerModel.setCustomerId(customerId);
      }      
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProcessingBankId() {
      if (processingBankIdBankModel != null) {
         return processingBankIdBankModel.getBankId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
																																																																																					    */
   
   @javax.persistence.Transient
   public void setProcessingBankId(Long bankId) {
      if(bankId == null)
      {      
      	processingBankIdBankModel = null;
      }
      else
      {
        processingBankIdBankModel = new BankModel();
      	processingBankIdBankModel.setBankId(bankId);
      }      
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUpdatedBy() {
      if (updatedByAppUserModel != null) {
         return updatedByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																																																																																			    */
   
   @javax.persistence.Transient
   public void setUpdatedBy(Long appUserId) {
      if(appUserId == null)
      {      
      	updatedByAppUserModel = null;
      }
      else
      {
        updatedByAppUserModel = new AppUserModel();
      	updatedByAppUserModel.setAppUserId(appUserId);
      }      
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCreatedBy() {
      if (createdByAppUserModel != null) {
         return createdByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																																																																																			    */
   
   @javax.persistence.Transient
   public void setCreatedBy(Long appUserId) {
      if(appUserId == null)
      {      
      	createdByAppUserModel = null;
      }
      else
      {
        createdByAppUserModel = new AppUserModel();
      	createdByAppUserModel.setAppUserId(appUserId);
      }      
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
    
    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("TransactionTypeModel");
    	associationModel.setPropertyName("relationTransactionTypeIdTransactionTypeModel");   		
   		associationModel.setValue(getRelationTransactionTypeIdTransactionTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("TransactionCodeModel");
    	associationModel.setPropertyName("relationTransactionCodeIdTransactionCodeModel");   		
   		associationModel.setValue(getRelationTransactionCodeIdTransactionCodeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("SwitchModel");
    	associationModel.setPropertyName("relationProcessingSwitchIdSwitchModel");   		
   		associationModel.setValue(getRelationProcessingSwitchIdSwitchModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("SupplierProcessingStatusModel");
    	associationModel.setPropertyName("relationSupProcessingStatusIdSupplierProcessingStatusModel");   		
   		associationModel.setValue(getRelationSupProcessingStatusIdSupplierProcessingStatusModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("SmartMoneyAccountModel");
    	associationModel.setPropertyName("relationSmartMoneyAccountIdSmartMoneyAccountModel");   		
   		associationModel.setValue(getRelationSmartMoneyAccountIdSmartMoneyAccountModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("RetailerContactModel");
    	associationModel.setPropertyName("relationToRetContactIdRetailerContactModel");   		
   		associationModel.setValue(getRelationToRetContactIdRetailerContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("RetailerContactModel");
    	associationModel.setPropertyName("relationFromRetContactIdRetailerContactModel");   		
   		associationModel.setValue(getRelationFromRetContactIdRetailerContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("RetailerModel");
    	associationModel.setPropertyName("relationRetailerIdRetailerModel");   		
   		associationModel.setValue(getRelationRetailerIdRetailerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PaymentModeModel");
    	associationModel.setPropertyName("relationPaymentModeIdPaymentModeModel");   		
   		associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("OperatorModel");
    	associationModel.setPropertyName("relationOperatorIdOperatorModel");   		
   		associationModel.setValue(getRelationOperatorIdOperatorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorContactModel");
    	associationModel.setPropertyName("relationFromDistContactIdDistributorContactModel");   		
   		associationModel.setValue(getRelationFromDistContactIdDistributorContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorContactModel");
    	associationModel.setPropertyName("relationToDistContactIdDistributorContactModel");   		
   		associationModel.setValue(getRelationToDistContactIdDistributorContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorContactModel");
    	associationModel.setPropertyName("relationDistributorNmContactIdDistributorContactModel");   		
   		associationModel.setValue(getRelationDistributorNmContactIdDistributorContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributorIdDistributorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DeviceTypeModel");
    	associationModel.setPropertyName("relationDeviceTypeIdDeviceTypeModel");   		
   		associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CustomerModel");
    	associationModel.setPropertyName("relationCustomerIdCustomerModel");   		
   		associationModel.setValue(getRelationCustomerIdCustomerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationProcessingBankIdBankModel");   		
   		associationModel.setValue(getRelationProcessingBankIdBankModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("RetailerModel");
    	associationModel.setPropertyName("relationToRetailerIdRetailerModel");   		
   		associationModel.setValue(getRelationToRetailerIdRetailerModel());
   		
   		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();

		associationModel.setClassName("DistributorModel");
		associationModel.setPropertyName("relationToDistributorIdDistributorModel");
		associationModel.setValue(getRelationToDistributorIdDistributorModel());

		associationModelList.add(associationModel);
 		
    	return associationModelList;
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
          
}
