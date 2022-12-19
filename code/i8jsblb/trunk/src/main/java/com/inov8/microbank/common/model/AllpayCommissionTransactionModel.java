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
 * The AllpayCommissionTransactionModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/12/01 19:29:08 $
 *
 *
 * @spring.bean name="AllpayCommissionTransactionModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ALLPAY_COMMISSION_TRANS_seq",sequenceName = "ALLPAY_COMMISSION_TRANS_seq", allocationSize=1)
@Table(name = "ALLPAY_COMMISSION_TRANSACTION")
public class AllpayCommissionTransactionModel extends BasePersistableModel implements Serializable {
  

   private TransactionDetailModel transactionDetailIdTransactionDetailModel;
   private RetailerModel retailerIdRetailerModel;
   private ProductModel productIdProductModel;
   private DistributorModel nationalDistributorIdDistributorModel;
   private DistributorModel distributorIdDistributorModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private AllpayCommissionReasonModel allpayCommissionReasonIdAllpayCommissionReasonModel;


   private Long allpayCommissionTransId;
   private Double transactionAmount;
   private Boolean nationalDistCommSettled;
   private Double nationalDistShare;
   private Double distributorShare;
   private Double retailerShare;
   private Double nationalDistCalculatedComm;
   private Double distributorCalculatedComm;
   private Double retailerCalculatedComm;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Boolean retailerCommSettled;
   private Boolean distributorCommSettled;

   /**
    * Default constructor.
    */
   public AllpayCommissionTransactionModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAllpayCommissionTransId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAllpayCommissionTransId(primaryKey);
    }

   /**
    * Returns the value of the <code>allpayCommissionTransId</code> property.
    *
    */
      @Column(name = "ALLPAY_COMMISSION_TRANS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALLPAY_COMMISSION_TRANS_seq")
   public Long getAllpayCommissionTransId() {
      return allpayCommissionTransId;
   }

   /**
    * Sets the value of the <code>allpayCommissionTransId</code> property.
    *
    * @param allpayCommissionTransId the value for the <code>allpayCommissionTransId</code> property
    *    
		    */

   public void setAllpayCommissionTransId(Long commissionTransactionId) {
      this.allpayCommissionTransId = commissionTransactionId;
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
    * Returns the value of the <code>nationalDistCommSettled</code> property.
    *
    */
      @Column(name = "IS_NATIONAL_DIST_COMM_SETTLED" , nullable = false )
   public Boolean getNationalDistCommSettled() {
      return nationalDistCommSettled;
   }

   /**
    * Sets the value of the <code>nationalDistCommSettled</code> property.
    *
    * @param nationalDistCommSettled the value for the <code>nationalDistCommSettled</code> property
    *    
		    */

   public void setNationalDistCommSettled(Boolean nationalDistCommSettled) {
      this.nationalDistCommSettled = nationalDistCommSettled;
   }

   /**
    * Returns the value of the <code>nationalDistShare</code> property.
    *
    */
      @Column(name = "NATIONAL_DIST_SHARE" , nullable = false )
   public Double getNationalDistShare() {
      return nationalDistShare;
   }

   /**
    * Sets the value of the <code>nationalDistShare</code> property.
    *
    * @param nationalDistShare the value for the <code>nationalDistShare</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setNationalDistShare(Double nationalDistShare) {
      this.nationalDistShare = nationalDistShare;
   }

   /**
    * Returns the value of the <code>distributorShare</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_SHARE" , nullable = false )
   public Double getDistributorShare() {
      return distributorShare;
   }

   /**
    * Sets the value of the <code>distributorShare</code> property.
    *
    * @param distributorShare the value for the <code>distributorShare</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setDistributorShare(Double distributorShare) {
      this.distributorShare = distributorShare;
   }

   /**
    * Returns the value of the <code>retailerShare</code> property.
    *
    */
      @Column(name = "RETAILER_SHARE" , nullable = false )
   public Double getRetailerShare() {
      return retailerShare;
   }

   /**
    * Sets the value of the <code>retailerShare</code> property.
    *
    * @param retailerShare the value for the <code>retailerShare</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setRetailerShare(Double retailerShare) {
      this.retailerShare = retailerShare;
   }

   /**
    * Returns the value of the <code>nationalDistCalculatedComm</code> property.
    *
    */
      @Column(name = "NATIONAL_DIST_CALCULATED_COMM" , nullable = false )
   public Double getNationalDistCalculatedComm() {
      return nationalDistCalculatedComm;
   }

   /**
    * Sets the value of the <code>nationalDistCalculatedComm</code> property.
    *
    * @param nationalDistCalculatedComm the value for the <code>nationalDistCalculatedComm</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setNationalDistCalculatedComm(Double nationalDistCalculatedComm) {
      this.nationalDistCalculatedComm = nationalDistCalculatedComm;
   }

   /**
    * Returns the value of the <code>distributorCalculatedComm</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_CALCULATED_COMM" , nullable = false )
   public Double getDistributorCalculatedComm() {
      return distributorCalculatedComm;
   }

   /**
    * Sets the value of the <code>distributorCalculatedComm</code> property.
    *
    * @param distributorCalculatedComm the value for the <code>distributorCalculatedComm</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setDistributorCalculatedComm(Double distributorCalculatedComm) {
      this.distributorCalculatedComm = distributorCalculatedComm;
   }

   /**
    * Returns the value of the <code>retailerCalculatedComm</code> property.
    *
    */
      @Column(name = "RETAILER_CALCULATED_COMM" , nullable = false )
   public Double getRetailerCalculatedComm() {
      return retailerCalculatedComm;
   }

   /**
    * Sets the value of the <code>retailerCalculatedComm</code> property.
    *
    * @param retailerCalculatedComm the value for the <code>retailerCalculatedComm</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setRetailerCalculatedComm(Double retailerCalculatedComm) {
      this.retailerCalculatedComm = retailerCalculatedComm;
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
    * Returns the value of the <code>versionOn</code> property.
    *
    */
   @Version
      @Column(name = "VERSION_ON" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   /**
    * Sets the value of the <code>versionOn</code> property.
    *
    * @param versionOn the value for the <code>versionOn</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="integer"
    */

   public void setVersionNo(Integer versionOn) {
      this.versionNo = versionOn;
   }

   /**
    * Returns the value of the <code>retailerCommSettled</code> property.
    *
    */
      @Column(name = "IS_RETAILER_COMM_SETTLED" , nullable = false )
   public Boolean getRetailerCommSettled() {
      return retailerCommSettled;
   }

   /**
    * Sets the value of the <code>retailerCommSettled</code> property.
    *
    * @param retailerCommSettled the value for the <code>retailerCommSettled</code> property
    *    
		    */

   public void setRetailerCommSettled(Boolean retailerCommSettled) {
      this.retailerCommSettled = retailerCommSettled;
   }

   /**
    * Returns the value of the <code>distributorCommSettled</code> property.
    *
    */
      @Column(name = "IS_DISTRIBUTOR_COMM_SETTLED" , nullable = false )
   public Boolean getDistributorCommSettled() {
      return distributorCommSettled;
   }

   /**
    * Sets the value of the <code>distributorCommSettled</code> property.
    *
    * @param distributorCommSettled the value for the <code>distributorCommSettled</code> property
    *    
		    */

   public void setDistributorCommSettled(Boolean distributorCommSettled) {
      this.distributorCommSettled = distributorCommSettled;
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
    * Returns the value of the <code>retialerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>retialerIdRetailerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RETIALER_ID")    
   public RetailerModel getRelationRetailerIdRetailerModel(){
      return retailerIdRetailerModel;
   }
    
   /**
    * Returns the value of the <code>retialerIdRetailerModel</code> relation property.
    *
    * @return the value of the <code>retialerIdRetailerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerModel getRetailerIdRetailerModel(){
      return getRelationRetailerIdRetailerModel();
   }

   /**
    * Sets the value of the <code>retialerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>retialerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRetailerIdRetailerModel(RetailerModel retailerModel) {
      this.retailerIdRetailerModel = retailerModel;
   }
   
   /**
    * Sets the value of the <code>retialerIdRetailerModel</code> relation property.
    *
    * @param retailerModel a value for <code>retialerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRetailerIdRetailerModel(RetailerModel retailerModel) {
      if(null != retailerModel)
      {
      	setRelationRetailerIdRetailerModel((RetailerModel)retailerModel.clone());
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
    * Returns the value of the <code>nationalDistributorIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>nationalDistributorIdDistributorModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "NATIONAL_DISTRIBUTOR_ID")    
   public DistributorModel getRelationNationalDistributorIdDistributorModel(){
      return nationalDistributorIdDistributorModel;
   }
    
   /**
    * Returns the value of the <code>nationalDistributorIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>nationalDistributorIdDistributorModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorModel getNationalDistributorIdDistributorModel(){
      return getRelationNationalDistributorIdDistributorModel();
   }

   /**
    * Sets the value of the <code>nationalDistributorIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>nationalDistributorIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationNationalDistributorIdDistributorModel(DistributorModel distributorModel) {
      this.nationalDistributorIdDistributorModel = distributorModel;
   }
   
   /**
    * Sets the value of the <code>nationalDistributorIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>nationalDistributorIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setNationalDistributorIdDistributorModel(DistributorModel distributorModel) {
      if(null != distributorModel)
      {
      	setRelationNationalDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
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
    * Returns the value of the <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code> relation property.
    *
    * @return the value of the <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ALLPAY_COMMISSION_REASON_ID")    
   public AllpayCommissionReasonModel getRelationAllpayCommissionReasonIdAllpayCommissionReasonModel(){
      return allpayCommissionReasonIdAllpayCommissionReasonModel;
   }
    
   /**
    * Returns the value of the <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code> relation property.
    *
    * @return the value of the <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AllpayCommissionReasonModel getAllpayCommissionReasonIdAllpayCommissionReasonModel(){
      return getRelationAllpayCommissionReasonIdAllpayCommissionReasonModel();
   }

   /**
    * Sets the value of the <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code> relation property.
    *
    * @param allpayCommissionReasonModel a value for <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAllpayCommissionReasonIdAllpayCommissionReasonModel(AllpayCommissionReasonModel allpayCommissionReasonModel) {
      this.allpayCommissionReasonIdAllpayCommissionReasonModel = allpayCommissionReasonModel;
   }
   
   /**
    * Sets the value of the <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code> relation property.
    *
    * @param allpayCommissionReasonModel a value for <code>allpayCommissionReasonIdAllpayCommissionReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setAllpayCommissionReasonIdAllpayCommissionReasonModel(AllpayCommissionReasonModel allpayCommissionReasonModel) {
      if(null != allpayCommissionReasonModel)
      {
      	setRelationAllpayCommissionReasonIdAllpayCommissionReasonModel((AllpayCommissionReasonModel)allpayCommissionReasonModel.clone());
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
    * Returns the value of the <code>distributorId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getNationalDistributorId() {
      if (nationalDistributorIdDistributorModel != null) {
         return nationalDistributorIdDistributorModel.getDistributorId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorId</code> property.
    *
    * @param distributorId the value for the <code>distributorId</code> property
																																									    * @spring.validator type="required"
							    */
   
   @javax.persistence.Transient
   public void setNationalDistributorId(Long distributorId) {
      if(distributorId == null)
      {      
      	nationalDistributorIdDistributorModel = null;
      }
      else
      {
        nationalDistributorIdDistributorModel = new DistributorModel();
      	nationalDistributorIdDistributorModel.setDistributorId(distributorId);
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
    * Returns the value of the <code>commissionReasonId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAllpayCommissionReasonId() {
      if (allpayCommissionReasonIdAllpayCommissionReasonModel != null) {
         return allpayCommissionReasonIdAllpayCommissionReasonModel.getAllpayCommissionReasonId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commissionReasonId</code> property.
    *
    * @param commissionReasonId the value for the <code>commissionReasonId</code> property
							    * @spring.validator type="required"
																																									    */
   
   @javax.persistence.Transient
   public void setAllpayCommissionReasonId(Long commissionReasonId) {
      if(commissionReasonId == null)
      {      
      	allpayCommissionReasonIdAllpayCommissionReasonModel = null;
      }
      else
      {
        allpayCommissionReasonIdAllpayCommissionReasonModel = new AllpayCommissionReasonModel();
      	allpayCommissionReasonIdAllpayCommissionReasonModel.setAllpayCommissionReasonId(commissionReasonId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAllpayCommissionTransId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&allpayCommissionTransId=" + getAllpayCommissionTransId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "allpayCommissionTransId";
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
    	
    	associationModel.setClassName("RetailerModel");
    	associationModel.setPropertyName("relationRetailerIdRetailerModel");   		
   		associationModel.setValue(getRelationRetailerIdRetailerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("relationNationalDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationNationalDistributorIdDistributorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributorIdDistributorModel());
   		
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
    	
    	associationModel.setClassName("AllpayCommissionReasonModel");
    	associationModel.setPropertyName("relationAllpayCommissionReasonIdAllpayCommissionReasonModel");   		
   		associationModel.setValue(getRelationAllpayCommissionReasonIdAllpayCommissionReasonModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
