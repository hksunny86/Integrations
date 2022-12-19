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
 * The AllpayCommissionRateModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/12/01 19:29:08 $
 *
 *
 * @spring.bean name="AllpayCommissionRateModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ALLPAY_COMMISSION_RATE_seq",sequenceName = "ALLPAY_COMMISSION_RATE_seq", allocationSize=1)
@Table(name = "ALLPAY_COMMISSION_RATE")
public class AllpayCommissionRateModel extends BasePersistableModel implements Serializable {
  

   private RetailerModel retailerIdRetailerModel;
   private ProductModel productIdProductModel;
   private DistributorModel distributorIdDistributorModel;
   private DistributorModel nationalDistributorIdDistributorModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private AllpayCommissionReasonModel allpayCommissionReasonIdAllpayCommissionReasonModel;


   private Long allpayCommissionRateId;
   private Double nationalDistributorRate;
   private Double distributorRate;
   private Double retailerRate;
   private Date fromDate;
   private Date toDate;
   private Boolean active;
   private String comments;
   private String description;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   //--- take care of this attribute and its related functions while regenerating with jag
   private String nationalDistributorName = "";
   
   
   @javax.persistence.Transient
   public void setNationalDistributorName(String name){
	   this.nationalDistributorName = name;
   }
   
   @javax.persistence.Transient
   public String  getNationalDistributorName(){
	   return nationalDistributorName;
   }
   /**
    * Default constructor.
    */
   public AllpayCommissionRateModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAllpayCommissionRateId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAllpayCommissionRateId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionRateId</code> property.
    *
    */
      @Column(name = "ALLPAY_COMMISSION_RATE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALLPAY_COMMISSION_RATE_seq")
   public Long getAllpayCommissionRateId() {
      return allpayCommissionRateId;
   }

   /**
    * Sets the value of the <code>commissionRateId</code> property.
    *;
    * @param commissionRateId the value for the <code>commissionRateId</code> property
    *    
		    */

   public void setAllpayCommissionRateId(Long commissionRateId) {
      this.allpayCommissionRateId = commissionRateId;
   }

   /**
    * Returns the value of the <code>nationalDistributorRate</code> property.
    *
    */
      @Column(name = "NATIONAL_DISTRIBUTOR_RATE" , nullable = false )
   public Double getNationalDistributorRate() {
      return nationalDistributorRate;
   }

   /**
    * Sets the value of the <code>nationalDistributorRate</code> property.
    *
    * @param nationalDistributorRate the value for the <code>nationalDistributorRate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setNationalDistributorRate(Double nationalDistributorRate) {
      this.nationalDistributorRate = nationalDistributorRate;
   }

   /**
    * Returns the value of the <code>distributorRate</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_RATE" , nullable = false )
   public Double getDistributorRate() {
      return distributorRate;
   }

   /**
    * Sets the value of the <code>distributorRate</code> property.
    *
    * @param distributorRate the value for the <code>distributorRate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setDistributorRate(Double distributorRate) {
      this.distributorRate = distributorRate;
   }

   /**
    * Returns the value of the <code>retailerRate</code> property.
    *
    */
      @Column(name = "RETAILER_RATE" , nullable = false )
   public Double getRetailerRate() {
      return retailerRate;
   }

   /**
    * Sets the value of the <code>retailerRate</code> property.
    *
    * @param retailerRate the value for the <code>retailerRate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setRetailerRate(Double retailerRate) {
      this.retailerRate = retailerRate;
   }

   /**
    * Returns the value of the <code>fromDate</code> property.
    *
    */
      @Column(name = "FROM_DATE" , nullable = false )
   public Date getFromDate() {
      return fromDate;
   }

   /**
    * Sets the value of the <code>fromDate</code> property.
    *
    * @param fromDate the value for the <code>fromDate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setFromDate(Date fromDate) {
      this.fromDate = fromDate;
   }

   /**
    * Returns the value of the <code>toDate</code> property.
    *
    */
      @Column(name = "TO_DATE"  )
   public Date getToDate() {
      return toDate;
   }

   /**
    * Sets the value of the <code>toDate</code> property.
    *
    * @param toDate the value for the <code>toDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setToDate(Date toDate) {
      this.toDate = toDate;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS"  , length=250 )
   public String getComments() {
      return comments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments(String comments) {
      this.comments = comments;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=250 )
   public String getDescription() {
      return description;
   }

   /**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription(String description) {
      this.description = description;
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
        checkBox += "_"+ getAllpayCommissionRateId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&allpayCommissionRateId=" + getAllpayCommissionRateId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "allpayCommissionRateId";
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
    	associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributorIdDistributorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("relationNationalDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationNationalDistributorIdDistributorModel());
   		
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
