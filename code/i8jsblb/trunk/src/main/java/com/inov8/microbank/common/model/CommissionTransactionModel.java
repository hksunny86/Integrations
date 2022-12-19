package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommissionTransactionModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionTransactionModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="COMMISSION_TRANSACTION_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="COMMISSION_TRANSACTION_seq") } )
//@javax.persistence.SequenceGenerator(name = "COMMISSION_TRANSACTION_seq",sequenceName = "COMMISSION_TRANSACTION_seq")
@Table(name = "COMMISSION_TRANSACTION")
public class CommissionTransactionModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
   private static final long serialVersionUID = -3183432472310526996L;
   private TransactionDetailModel transactionDetailIdTransactionDetailModel;
   private StakeholderBankInfoModel stakeholderBankIdStakeholderBankInfoModel;
   private ProductUnitModel productUnitIdProductUnitModel;
   private ProductModel productIdProductModel;
   private PaymentModeModel paymentModeIdPaymentModeModel;
   private DispenseTypeModel dispenseTypeIdDispenseTypeModel;
   private CommissionTypeModel commissionTypeIdCommissionTypeModel;
   private CommissionStakeholderModel commissionStakeholderIdCommissionStakeholderModel;
   private CommissionRateModel commissionRateIdCommissionRateModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel hierarchyAppUserModel;
   private AppUserModel retentionAppUserModel;
   private Double fedRate;
   private Double fedAmount;
   private Double whtRate;
   private Double whtAmount;
   private Boolean whtApplicable;
   private Integer hierarchyOrderNo;

   private Long commissionTransactionId;
   private Double commissionAmount;
   private Double productCostPrice;
   private Double productUnitPrice;
   private Double productTopupAmount;
   private Boolean settled;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Boolean posted;
   /**
    * Default constructor.
    */
   public CommissionTransactionModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommissionTransactionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommissionTransactionId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionTransactionId</code> property.
    *
    */
      @Column(name = "COMMISSION_TRANSACTION_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMISSION_TRANSACTION_seq")
   public Long getCommissionTransactionId() {
      return commissionTransactionId;
   }

   /**
    * Sets the value of the <code>commissionTransactionId</code> property.
    *
    * @param commissionTransactionId the value for the <code>commissionTransactionId</code> property
    *    
		    */

   public void setCommissionTransactionId(Long commissionTransactionId) {
      this.commissionTransactionId = commissionTransactionId;
   }

   /**
    * Returns the value of the <code>commissionAmount</code> property.
    *
    */
      @Column(name = "COMMISSION_AMOUNT" , nullable = false )
   public Double getCommissionAmount() {
      return commissionAmount;
   }

   /**
    * Sets the value of the <code>commissionAmount</code> property.
    *
    * @param commissionAmount the value for the <code>commissionAmount</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setCommissionAmount(Double commissionAmount) {
      this.commissionAmount = commissionAmount;
   }

   /**
    * Returns the value of the <code>productCostPrice</code> property.
    *
    */
      @Column(name = "PRODUCT_COST_PRICE"  )
   public Double getProductCostPrice() {
      return productCostPrice;
   }

   /**
    * Sets the value of the <code>productCostPrice</code> property.
    *
    * @param productCostPrice the value for the <code>productCostPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setProductCostPrice(Double productCostPrice) {
      this.productCostPrice = productCostPrice;
   }

   /**
    * Returns the value of the <code>productUnitPrice</code> property.
    *
    */
      @Column(name = "PRODUCT_UNIT_PRICE"  )
   public Double getProductUnitPrice() {
      return productUnitPrice;
   }

   /**
    * Sets the value of the <code>productUnitPrice</code> property.
    *
    * @param productUnitPrice the value for the <code>productUnitPrice</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setProductUnitPrice(Double productUnitPrice) {
      this.productUnitPrice = productUnitPrice;
   }

   /**
    * Returns the value of the <code>productTopupAmount</code> property.
    *
    */
      @Column(name = "PRODUCT_TOPUP_AMOUNT"  )
   public Double getProductTopupAmount() {
      return productTopupAmount;
   }

   /**
    * Sets the value of the <code>productTopupAmount</code> property.
    *
    * @param productTopupAmount the value for the <code>productTopupAmount</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setProductTopupAmount(Double productTopupAmount) {
      this.productTopupAmount = productTopupAmount;
   }

   /**
    * Returns the value of the <code>settled</code> property.
    *
    */
      @Column(name = "IS_SETTLED" , nullable = true )
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
    * Returns the value of the <code>transactionDetailIdTransactionDetailModel</code> relation property.
    *
    * @return the value of the <code>transactionDetailIdTransactionDetailModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_DETAIL_ID")    
   public TransactionDetailModel getRelationTransactionDetailIdTransactionDetailModel(){
      return transactionDetailIdTransactionDetailModel;
   }
    
   /**
    * Returns the value of the <code>transactionDetailIdTransactionDetailModel</code> relation property.
    *
    * @return the value of the <code>transactionDetailIdTransactionDetailModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public TransactionDetailModel getTransactionDetailIdTransactionDetailModel(){
      return getRelationTransactionDetailIdTransactionDetailModel();
   }

   /**
    * Sets the value of the <code>transactionDetailIdTransactionDetailModel</code> relation property.
    *
    * @param transactionDetailModel a value for <code>transactionDetailIdTransactionDetailModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionDetailIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {
      this.transactionDetailIdTransactionDetailModel = transactionDetailModel;
   }
   
   /**
    * Sets the value of the <code>transactionDetailIdTransactionDetailModel</code> relation property.
    *
    * @param transactionDetailModel a value for <code>transactionDetailIdTransactionDetailModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionDetailIdTransactionDetailModel(TransactionDetailModel transactionDetailModel) {
      if(null != transactionDetailModel)
      {
      	setRelationTransactionDetailIdTransactionDetailModel((TransactionDetailModel)transactionDetailModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>stakeholderBankIdStakeholderBankInfoModel</code> relation property.
    *
    * @return the value of the <code>stakeholderBankIdStakeholderBankInfoModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "STAKEHOLDER_BANK_ID")    
   public StakeholderBankInfoModel getRelationStakeholderBankIdStakeholderBankInfoModel(){
      return stakeholderBankIdStakeholderBankInfoModel;
   }
    
   /**
    * Returns the value of the <code>stakeholderBankIdStakeholderBankInfoModel</code> relation property.
    *
    * @return the value of the <code>stakeholderBankIdStakeholderBankInfoModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public StakeholderBankInfoModel getStakeholderBankIdStakeholderBankInfoModel(){
      return getRelationStakeholderBankIdStakeholderBankInfoModel();
   }

   /**
    * Sets the value of the <code>stakeholderBankIdStakeholderBankInfoModel</code> relation property.
    *
    * @param stakeholderBankInfoModel a value for <code>stakeholderBankIdStakeholderBankInfoModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationStakeholderBankIdStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      this.stakeholderBankIdStakeholderBankInfoModel = stakeholderBankInfoModel;
   }
   
   /**
    * Sets the value of the <code>stakeholderBankIdStakeholderBankInfoModel</code> relation property.
    *
    * @param stakeholderBankInfoModel a value for <code>stakeholderBankIdStakeholderBankInfoModel</code>.
    */
   @javax.persistence.Transient
   public void setStakeholderBankIdStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      if(null != stakeholderBankInfoModel)
      {
      	setRelationStakeholderBankIdStakeholderBankInfoModel((StakeholderBankInfoModel)stakeholderBankInfoModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @return the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_UNIT_ID")    
   public ProductUnitModel getRelationProductUnitIdProductUnitModel(){
      return productUnitIdProductUnitModel;
   }
    
   /**
    * Returns the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @return the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductUnitModel getProductUnitIdProductUnitModel(){
      return getRelationProductUnitIdProductUnitModel();
   }

   /**
    * Sets the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @param productUnitModel a value for <code>productUnitIdProductUnitModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductUnitIdProductUnitModel(ProductUnitModel productUnitModel) {
      this.productUnitIdProductUnitModel = productUnitModel;
   }
   
   /**
    * Sets the value of the <code>productUnitIdProductUnitModel</code> relation property.
    *
    * @param productUnitModel a value for <code>productUnitIdProductUnitModel</code>.
    */
   @javax.persistence.Transient
   public void setProductUnitIdProductUnitModel(ProductUnitModel productUnitModel) {
      if(null != productUnitModel)
      {
      	setRelationProductUnitIdProductUnitModel((ProductUnitModel)productUnitModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>productIdProductModel</code> relation property.
    *
    * @return the value of the <code>productIdProductModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_ID")    
   public ProductModel getRelationProductIdProductModel(){
      return productIdProductModel;
   }
    
   /**
    * Returns the value of the <code>productIdProductModel</code> relation property.
    *
    * @return the value of the <code>productIdProductModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductModel getProductIdProductModel(){
      return getRelationProductIdProductModel();
   }

   /**
    * Sets the value of the <code>productIdProductModel</code> relation property.
    *
    * @param productModel a value for <code>productIdProductModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductIdProductModel(ProductModel productModel) {
      this.productIdProductModel = productModel;
   }
   
   /**
    * Sets the value of the <code>productIdProductModel</code> relation property.
    *
    * @param productModel a value for <code>productIdProductModel</code>.
    */
   @javax.persistence.Transient
   public void setProductIdProductModel(ProductModel productModel) {
      if(null != productModel)
      {
      	setRelationProductIdProductModel((ProductModel)productModel.clone());
      }      
   }
   

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
    * Returns the value of the <code>dispenseTypeIdDispenseTypeModel</code> relation property.
    *
    * @return the value of the <code>dispenseTypeIdDispenseTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DISPENSE_TYPE_ID")    
   public DispenseTypeModel getRelationDispenseTypeIdDispenseTypeModel(){
      return dispenseTypeIdDispenseTypeModel;
   }
    
   /**
    * Returns the value of the <code>dispenseTypeIdDispenseTypeModel</code> relation property.
    *
    * @return the value of the <code>dispenseTypeIdDispenseTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DispenseTypeModel getDispenseTypeIdDispenseTypeModel(){
      return getRelationDispenseTypeIdDispenseTypeModel();
   }

   /**
    * Sets the value of the <code>dispenseTypeIdDispenseTypeModel</code> relation property.
    *
    * @param dispenseTypeModel a value for <code>dispenseTypeIdDispenseTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDispenseTypeIdDispenseTypeModel(DispenseTypeModel dispenseTypeModel) {
      this.dispenseTypeIdDispenseTypeModel = dispenseTypeModel;
   }
   
   /**
    * Sets the value of the <code>dispenseTypeIdDispenseTypeModel</code> relation property.
    *
    * @param dispenseTypeModel a value for <code>dispenseTypeIdDispenseTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setDispenseTypeIdDispenseTypeModel(DispenseTypeModel dispenseTypeModel) {
      if(null != dispenseTypeModel)
      {
      	setRelationDispenseTypeIdDispenseTypeModel((DispenseTypeModel)dispenseTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>commissionTypeIdCommissionTypeModel</code> relation property.
    *
    * @return the value of the <code>commissionTypeIdCommissionTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COMMISSION_TYPE_ID")    
   public CommissionTypeModel getRelationCommissionTypeIdCommissionTypeModel(){
      return commissionTypeIdCommissionTypeModel;
   }
    
   /**
    * Returns the value of the <code>commissionTypeIdCommissionTypeModel</code> relation property.
    *
    * @return the value of the <code>commissionTypeIdCommissionTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommissionTypeModel getCommissionTypeIdCommissionTypeModel(){
      return getRelationCommissionTypeIdCommissionTypeModel();
   }

   /**
    * Sets the value of the <code>commissionTypeIdCommissionTypeModel</code> relation property.
    *
    * @param commissionTypeModel a value for <code>commissionTypeIdCommissionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommissionTypeIdCommissionTypeModel(CommissionTypeModel commissionTypeModel) {
      this.commissionTypeIdCommissionTypeModel = commissionTypeModel;
   }
   
   /**
    * Sets the value of the <code>commissionTypeIdCommissionTypeModel</code> relation property.
    *
    * @param commissionTypeModel a value for <code>commissionTypeIdCommissionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setCommissionTypeIdCommissionTypeModel(CommissionTypeModel commissionTypeModel) {
      if(null != commissionTypeModel)
      {
      	setRelationCommissionTypeIdCommissionTypeModel((CommissionTypeModel)commissionTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @return the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")    
   public CommissionStakeholderModel getRelationCommissionStakeholderIdCommissionStakeholderModel(){
      return commissionStakeholderIdCommissionStakeholderModel;
   }
    
   /**
    * Returns the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @return the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommissionStakeholderModel getCommissionStakeholderIdCommissionStakeholderModel(){
      return getRelationCommissionStakeholderIdCommissionStakeholderModel();
   }

   /**
    * Sets the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @param commissionStakeholderModel a value for <code>commissionStakeholderIdCommissionStakeholderModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommissionStakeholderIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      this.commissionStakeholderIdCommissionStakeholderModel = commissionStakeholderModel;
   }
   
   /**
    * Sets the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @param commissionStakeholderModel a value for <code>commissionStakeholderIdCommissionStakeholderModel</code>.
    */
   @javax.persistence.Transient
   public void setCommissionStakeholderIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      if(null != commissionStakeholderModel)
      {
      	setRelationCommissionStakeholderIdCommissionStakeholderModel((CommissionStakeholderModel)commissionStakeholderModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>commissionRateIdCommissionRateModel</code> relation property.
    *
    * @return the value of the <code>commissionRateIdCommissionRateModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COMMISSION_RATE_ID")    
   public CommissionRateModel getRelationCommissionRateIdCommissionRateModel(){
      return commissionRateIdCommissionRateModel;
   }
    
   /**
    * Returns the value of the <code>commissionRateIdCommissionRateModel</code> relation property.
    *
    * @return the value of the <code>commissionRateIdCommissionRateModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommissionRateModel getCommissionRateIdCommissionRateModel(){
      return getRelationCommissionRateIdCommissionRateModel();
   }

   /**
    * Sets the value of the <code>commissionRateIdCommissionRateModel</code> relation property.
    *
    * @param commissionRateModel a value for <code>commissionRateIdCommissionRateModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommissionRateIdCommissionRateModel(CommissionRateModel commissionRateModel) {
      this.commissionRateIdCommissionRateModel = commissionRateModel;
   }
   
   /**
    * Sets the value of the <code>commissionRateIdCommissionRateModel</code> relation property.
    *
    * @param commissionRateModel a value for <code>commissionRateIdCommissionRateModel</code>.
    */
   @javax.persistence.Transient
   public void setCommissionRateIdCommissionRateModel(CommissionRateModel commissionRateModel) {
      if(null != commissionRateModel)
      {
      	setRelationCommissionRateIdCommissionRateModel((CommissionRateModel)commissionRateModel.clone());
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
    * Returns the value of the <code>transactionDetailId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionDetailId() {
      if (transactionDetailIdTransactionDetailModel != null) {
         return transactionDetailIdTransactionDetailModel.getTransactionDetailId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionDetailId</code> property.
    *
    * @param transactionDetailId the value for the <code>transactionDetailId</code> property
					    * @spring.validator type="required"
																																							    */
   
   @javax.persistence.Transient
   public void setTransactionDetailId(Long transactionDetailId) {
      if(transactionDetailId == null)
      {      
      	transactionDetailIdTransactionDetailModel = null;
      }
      else
      {
        transactionDetailIdTransactionDetailModel = new TransactionDetailModel();
      	transactionDetailIdTransactionDetailModel.setTransactionDetailId(transactionDetailId);
      }      
   }

   /**
    * Returns the value of the <code>stakeholderBankInfoId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getStakeholderBankId() {
      if (stakeholderBankIdStakeholderBankInfoModel != null) {
         return stakeholderBankIdStakeholderBankInfoModel.getStakeholderBankInfoId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>stakeholderBankInfoId</code> property.
    *
    * @param stakeholderBankInfoId the value for the <code>stakeholderBankInfoId</code> property
																																											    */
   
   @javax.persistence.Transient
   public void setStakeholderBankId(Long stakeholderBankInfoId) {
      if(stakeholderBankInfoId == null)
      {      
      	stakeholderBankIdStakeholderBankInfoModel = null;
      }
      else
      {
        stakeholderBankIdStakeholderBankInfoModel = new StakeholderBankInfoModel();
      	stakeholderBankIdStakeholderBankInfoModel.setStakeholderBankInfoId(stakeholderBankInfoId);
      }      
   }

   /**
    * Returns the value of the <code>productUnitId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductUnitId() {
      if (productUnitIdProductUnitModel != null) {
         return productUnitIdProductUnitModel.getProductUnitId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productUnitId</code> property.
    *
    * @param productUnitId the value for the <code>productUnitId</code> property
																																											    */
   
   @javax.persistence.Transient
   public void setProductUnitId(Long productUnitId) {
      if(productUnitId == null)
      {      
      	productUnitIdProductUnitModel = null;
      }
      else
      {
        productUnitIdProductUnitModel = new ProductUnitModel();
      	productUnitIdProductUnitModel.setProductUnitId(productUnitId);
      }      
   }

   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductId() {
      if (productIdProductModel != null) {
         return productIdProductModel.getProductId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
									    * @spring.validator type="required"
																																			    */
   
   @javax.persistence.Transient
   public void setProductId(Long productId) {
      if(productId == null)
      {      
      	productIdProductModel = null;
      }
      else
      {
        productIdProductModel = new ProductModel();
      	productIdProductModel.setProductId(productId);
      }      
   }

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
    * Returns the value of the <code>dispenseTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDispenseTypeId() {
      if (dispenseTypeIdDispenseTypeModel != null) {
         return dispenseTypeIdDispenseTypeModel.getDispenseTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>dispenseTypeId</code> property.
    *
    * @param dispenseTypeId the value for the <code>dispenseTypeId</code> property
																	    * @spring.validator type="required"
																											    */
   
   @javax.persistence.Transient
   public void setDispenseTypeId(Long dispenseTypeId) {
      if(dispenseTypeId == null)
      {      
      	dispenseTypeIdDispenseTypeModel = null;
      }
      else
      {
        dispenseTypeIdDispenseTypeModel = new DispenseTypeModel();
      	dispenseTypeIdDispenseTypeModel.setDispenseTypeId(dispenseTypeId);
      }      
   }

   /**
    * Returns the value of the <code>commissionTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommissionTypeId() {
      if (commissionTypeIdCommissionTypeModel != null) {
         return commissionTypeIdCommissionTypeModel.getCommissionTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commissionTypeId</code> property.
    *
    * @param commissionTypeId the value for the <code>commissionTypeId</code> property
											    * @spring.validator type="required"
																																	    */
   
   @javax.persistence.Transient
   public void setCommissionTypeId(Long commissionTypeId) {
      if(commissionTypeId == null)
      {      
      	commissionTypeIdCommissionTypeModel = null;
      }
      else
      {
        commissionTypeIdCommissionTypeModel = new CommissionTypeModel();
      	commissionTypeIdCommissionTypeModel.setCommissionTypeId(commissionTypeId);
      }      
   }

   /**
    * Returns the value of the <code>commissionStakeholderId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommissionStakeholderId() {
      if (commissionStakeholderIdCommissionStakeholderModel != null) {
         return commissionStakeholderIdCommissionStakeholderModel.getCommissionStakeholderId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commissionStakeholderId</code> property.
    *
    * @param commissionStakeholderId the value for the <code>commissionStakeholderId</code> property
													    * @spring.validator type="required"
																															    */
   
   @javax.persistence.Transient
   public void setCommissionStakeholderId(Long commissionStakeholderId) {
      if(commissionStakeholderId == null)
      {      
      	commissionStakeholderIdCommissionStakeholderModel = null;
      }
      else
      {
        commissionStakeholderIdCommissionStakeholderModel = new CommissionStakeholderModel();
      	commissionStakeholderIdCommissionStakeholderModel.setCommissionStakeholderId(commissionStakeholderId);
      }      
   }

   /**
    * Returns the value of the <code>commissionRateId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommissionRateId() {
      if (commissionRateIdCommissionRateModel != null) {
         return commissionRateIdCommissionRateModel.getCommissionRateId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commissionRateId</code> property.
    *
    * @param commissionRateId the value for the <code>commissionRateId</code> property
							    * @spring.validator type="required"
																																					    */
   
   @javax.persistence.Transient
   public void setCommissionRateId(Long commissionRateId) {
      if(commissionRateId == null)
      {      
      	commissionRateIdCommissionRateModel = null;
      }
      else
      {
        commissionRateIdCommissionRateModel = new CommissionRateModel();
      	commissionRateIdCommissionRateModel.setCommissionRateId(commissionRateId);
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
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "HIERARCHY_APP_USER_ID")    
   public AppUserModel getRelationHierarchyAppUserModel(){
      return hierarchyAppUserModel;
   }
    
   /**
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getHierarchyAppUserModel(){
      return getRelationHierarchyAppUserModel();
   }

   /**
    * Sets the value of the <code>HierarchyAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>HierarchyAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationHierarchyAppUserModel(AppUserModel appUserModel) {
      this.hierarchyAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>HierarchyAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>HierarchyAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setHierarchyAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationHierarchyAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   
   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getHierarchyAppUserId() {
      if (hierarchyAppUserModel != null) {
         return hierarchyAppUserModel.getAppUserId();
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
   public void setHierarchyAppUserId(Long appUserId) {
      if(appUserId == null)
      {      
      	hierarchyAppUserModel = null;
      }
      else
      {
        hierarchyAppUserModel = new AppUserModel();
      	hierarchyAppUserModel.setAppUserId(appUserId);
      }      
   }


   
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RETENTION_APP_USER_ID")    
   public AppUserModel getRelationRetentionAppUserModel(){
      return retentionAppUserModel;
   }
    

   @javax.persistence.Transient
   public AppUserModel getRetentionAppUserModel(){
      return getRelationRetentionAppUserModel();
   }


   @javax.persistence.Transient
   public void setRelationRetentionAppUserModel(AppUserModel retentionAppUserModel) {
      this.retentionAppUserModel = retentionAppUserModel;
   }
   

   @javax.persistence.Transient
   public void setRetentionAppUserModel(AppUserModel retentionAppUserModel) {
      if(null != retentionAppUserModel)
      {
      	setRelationRetentionAppUserModel((AppUserModel)retentionAppUserModel.clone());
      }      
   }
   

   @javax.persistence.Transient
   public Long getRetentionAppUserId() {
      if (retentionAppUserModel != null) {
         return retentionAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   @javax.persistence.Transient
   public void setRetentionAppUserId(Long retentionAppUserId) {
      if(retentionAppUserId == null)
      {      
    	  retentionAppUserModel = null;
      }
      else
      {
    	  retentionAppUserModel = new AppUserModel();
    	  retentionAppUserModel.setAppUserId(retentionAppUserId);
      }      
   }

   /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCommissionTransactionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commissionTransactionId=" + getCommissionTransactionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commissionTransactionId";
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
    	
    	associationModel.setClassName("TransactionDetailModel");
    	associationModel.setPropertyName("relationTransactionDetailIdTransactionDetailModel");   		
   		associationModel.setValue(getRelationTransactionDetailIdTransactionDetailModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("StakeholderBankInfoModel");
    	associationModel.setPropertyName("relationStakeholderBankIdStakeholderBankInfoModel");   		
   		associationModel.setValue(getRelationStakeholderBankIdStakeholderBankInfoModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductUnitModel");
    	associationModel.setPropertyName("relationProductUnitIdProductUnitModel");   		
   		associationModel.setValue(getRelationProductUnitIdProductUnitModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PaymentModeModel");
    	associationModel.setPropertyName("relationPaymentModeIdPaymentModeModel");   		
   		associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DispenseTypeModel");
    	associationModel.setPropertyName("relationDispenseTypeIdDispenseTypeModel");   		
   		associationModel.setValue(getRelationDispenseTypeIdDispenseTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommissionTypeModel");
    	associationModel.setPropertyName("relationCommissionTypeIdCommissionTypeModel");   		
   		associationModel.setValue(getRelationCommissionTypeIdCommissionTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommissionStakeholderModel");
    	associationModel.setPropertyName("relationCommissionStakeholderIdCommissionStakeholderModel");   		
   		associationModel.setValue(getRelationCommissionStakeholderIdCommissionStakeholderModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommissionRateModel");
    	associationModel.setPropertyName("relationCommissionRateIdCommissionRateModel");   		
   		associationModel.setValue(getRelationCommissionRateIdCommissionRateModel());
   		
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

   		associationModel.setClassName("AppUserModel");
   		associationModel.setPropertyName("relationHierarchyAppUserModel");   		
   		associationModel.setValue(getRelationHierarchyAppUserModel());

   		associationModelList.add(associationModel);

   		associationModel = new AssociationModel();
   		associationModel.setClassName("AppUserModel");
   		associationModel.setPropertyName("relationRetentionAppUserModel");   		
   		associationModel.setValue(getRelationRetentionAppUserModel());
   		associationModelList.add(associationModel);

    	return associationModelList;
    }

    @Column(name = "POSTED" , nullable = true )
	public Boolean getPosted() {
		return posted;
	}

	public void setPosted(Boolean posted) {
		this.posted = posted;
	}

    @Column(name = "WHT_RATE")
	public Double getWhtRate() {
		return whtRate;
	}

	public void setWhtRate(Double whtRate) {
		this.whtRate = whtRate;
	}    
    
	@Column(name = "WHT_AMOUNT")
	public Double getWhtAmount() {
		return whtAmount;
	}

	public void setWhtAmount(Double whtAmount) {
		this.whtAmount = whtAmount;
	}

    @Column(name = "FED_RATE")
	public Double getFedRate() {
		return fedRate;
	}

	public void setFedRate(Double fedRate) {
		this.fedRate = fedRate;
	}

    @Column(name = "FED_AMOUNT")
	public Double getFedAmount() {
		return fedAmount;
	}

	public void setFedAmount(Double fedAmount) {
		this.fedAmount = fedAmount;
	}

    @Column(name = "HIERARCHY_ORDER_NO")
	public Integer getHierarchyOrderNo() {
		return hierarchyOrderNo;
	}

	public void setHierarchyOrderNo(Integer hierarchyOrderNo) {
		this.hierarchyOrderNo = hierarchyOrderNo;
	}
    
	@Column(name = "IS_WHT_APPLICABLE")
	public Boolean getWhtApplicable() {
		return whtApplicable;
	}

	public void setWhtApplicable(Boolean whtApplicable) {
		this.whtApplicable = whtApplicable;
	}

}
