package com.inov8.microbank.common.model;

import java.io.Serializable;
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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The PartnerModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PartnerModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PARTNER_seq",sequenceName = "PARTNER_seq", allocationSize=1)
@Table(name = "PARTNER")
public class PartnerModel extends BasePersistableModel implements Serializable {
  

   private SupplierModel supplierIdSupplierModel;
   private RetailerModel retailerIdRetailerModel;
   private OperatorModel operatorIdOperatorModel;
   private MnoModel mnoIdMnoModel;
   private DistributorModel distributerIdDistributorModel;
   private BankModel bankIdBankModel;
   private AppUserTypeModel appUserTypeIdAppUserTypeModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<PartnerIpAddressModel> partnerIdPartnerIpAddressModelList = new ArrayList<PartnerIpAddressModel>();
   private Collection<PartnerGroupModel> partnerIdPartnerGroupModelList = new ArrayList<PartnerGroupModel>();
   private Collection<PartnerPermissionGroupModel> partnerIdPartnerPermissionGroupModelList = new ArrayList<PartnerPermissionGroupModel>();

   private Long partnerId;
   private String name;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Boolean active;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public PartnerModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPartnerId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPartnerId(primaryKey);
    }

   /**
    * Returns the value of the <code>partnerId</code> property.
    *
    */
      @Column(name = "PARTNER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARTNER_seq")
   public Long getPartnerId() {
      return partnerId;
   }

   /**
    * Sets the value of the <code>partnerId</code> property.
    *
    * @param partnerId the value for the <code>partnerId</code> property
    *    
		    */

   public void setPartnerId(Long partnerId) {
      this.partnerId = partnerId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=100 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="100"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
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
    * Returns the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @return the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SUPPLIER_ID")    
   public SupplierModel getRelationSupplierIdSupplierModel(){
      return supplierIdSupplierModel;
   }
    
   /**
    * Returns the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @return the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SupplierModel getSupplierIdSupplierModel(){
      return getRelationSupplierIdSupplierModel();
   }

   /**
    * Sets the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @param supplierModel a value for <code>supplierIdSupplierModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSupplierIdSupplierModel(SupplierModel supplierModel) {
      this.supplierIdSupplierModel = supplierModel;
   }
   
   /**
    * Sets the value of the <code>supplierIdSupplierModel</code> relation property.
    *
    * @param supplierModel a value for <code>supplierIdSupplierModel</code>.
    */
   @javax.persistence.Transient
   public void setSupplierIdSupplierModel(SupplierModel supplierModel) {
      if(null != supplierModel)
      {
      	setRelationSupplierIdSupplierModel((SupplierModel)supplierModel.clone());
      }      
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
    * Returns the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @return the value of the <code>mnoIdMnoModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SERVICE_OP_ID")    
   public MnoModel getRelationMnoIdMnoModel(){
      return mnoIdMnoModel;
   }
    
   /**
    * Returns the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @return the value of the <code>mnoIdMnoModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public MnoModel getMnoIdMnoModel(){
      return getRelationMnoIdMnoModel();
   }

   /**
    * Sets the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @param mnoModel a value for <code>mnoIdMnoModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationMnoIdMnoModel(MnoModel mnoModel) {
      this.mnoIdMnoModel = mnoModel;
   }
   
   /**
    * Sets the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @param mnoModel a value for <code>mnoIdMnoModel</code>.
    */
   @javax.persistence.Transient
   public void setMnoIdMnoModel(MnoModel mnoModel) {
      if(null != mnoModel)
      {
      	setRelationMnoIdMnoModel((MnoModel)mnoModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>distributerIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>distributerIdDistributorModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DISTRIBUTER_ID")    
   public DistributorModel getRelationDistributerIdDistributorModel(){
      return distributerIdDistributorModel;
   }
    
   /**
    * Returns the value of the <code>distributerIdDistributorModel</code> relation property.
    *
    * @return the value of the <code>distributerIdDistributorModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorModel getDistributerIdDistributorModel(){
      return getRelationDistributerIdDistributorModel();
   }

   /**
    * Sets the value of the <code>distributerIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>distributerIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDistributerIdDistributorModel(DistributorModel distributorModel) {
      this.distributerIdDistributorModel = distributorModel;
   }
   
   /**
    * Sets the value of the <code>distributerIdDistributorModel</code> relation property.
    *
    * @param distributorModel a value for <code>distributerIdDistributorModel</code>.
    */
   @javax.persistence.Transient
   public void setDistributerIdDistributorModel(DistributorModel distributorModel) {
      if(null != distributorModel)
      {
      	setRelationDistributerIdDistributorModel((DistributorModel)distributorModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>bankIdBankModel</code> relation property.
    *
    * @return the value of the <code>bankIdBankModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "BANK_ID")    
   public BankModel getRelationBankIdBankModel(){
      return bankIdBankModel;
   }
    
   /**
    * Returns the value of the <code>bankIdBankModel</code> relation property.
    *
    * @return the value of the <code>bankIdBankModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public BankModel getBankIdBankModel(){
      return getRelationBankIdBankModel();
   }

   /**
    * Sets the value of the <code>bankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>bankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationBankIdBankModel(BankModel bankModel) {
      this.bankIdBankModel = bankModel;
   }
   
   /**
    * Sets the value of the <code>bankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>bankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setBankIdBankModel(BankModel bankModel) {
      if(null != bankModel)
      {
      	setRelationBankIdBankModel((BankModel)bankModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_USER_TYPE_ID")    
   public AppUserTypeModel getRelationAppUserTypeIdAppUserTypeModel(){
      return appUserTypeIdAppUserTypeModel;
   }
    
   /**
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserTypeModel getAppUserTypeIdAppUserTypeModel(){
      return getRelationAppUserTypeIdAppUserTypeModel();
   }

   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      this.appUserTypeIdAppUserTypeModel = appUserTypeModel;
   }
   
   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      if(null != appUserTypeModel)
      {
      	setRelationAppUserTypeIdAppUserTypeModel((AppUserTypeModel)appUserTypeModel.clone());
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
    * Add the related PartnerIpAddressModel to this one-to-many relation.
    *
    * @param partnerIpAddressModel object to be added.
    */
    
   public void addPartnerIdPartnerIpAddressModel(PartnerIpAddressModel partnerIpAddressModel) {
      partnerIpAddressModel.setRelationPartnerIdPartnerModel(this);
      partnerIdPartnerIpAddressModelList.add(partnerIpAddressModel);
   }
   
   /**
    * Remove the related PartnerIpAddressModel to this one-to-many relation.
    *
    * @param partnerIpAddressModel object to be removed.
    */
   
   public void removePartnerIdPartnerIpAddressModel(PartnerIpAddressModel partnerIpAddressModel) {      
      partnerIpAddressModel.setRelationPartnerIdPartnerModel(null);
      partnerIdPartnerIpAddressModelList.remove(partnerIpAddressModel);      
   }

   /**
    * Get a list of related PartnerIpAddressModel objects of the PartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @return Collection of PartnerIpAddressModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPartnerIdPartnerModel")
   @JoinColumn(name = "PARTNER_ID")
   public Collection<PartnerIpAddressModel> getPartnerIdPartnerIpAddressModelList() throws Exception {
   		return partnerIdPartnerIpAddressModelList;
   }


   /**
    * Set a list of PartnerIpAddressModel related objects to the PartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @param partnerIpAddressModelList the list of related objects.
    */
    public void setPartnerIdPartnerIpAddressModelList(Collection<PartnerIpAddressModel> partnerIpAddressModelList) throws Exception {
		this.partnerIdPartnerIpAddressModelList = partnerIpAddressModelList;
   }



   /**
    * Add the related PartnerGroupModel to this one-to-many relation.
    *
    * @param partnerGroupModel object to be added.
    */
    
   public void addPartnerIdPartnerGroupModel(PartnerGroupModel partnerGroupModel) {
      partnerGroupModel.setRelationPartnerIdPartnerModel(this);
      partnerIdPartnerGroupModelList.add(partnerGroupModel);
   }
   
   /**
    * Remove the related PartnerGroupModel to this one-to-many relation.
    *
    * @param partnerGroupModel object to be removed.
    */
   
   public void removePartnerIdPartnerGroupModel(PartnerGroupModel partnerGroupModel) {      
      partnerGroupModel.setRelationPartnerIdPartnerModel(null);
      partnerIdPartnerGroupModelList.remove(partnerGroupModel);      
   }

   /**
    * Get a list of related PartnerGroupModel objects of the PartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @return Collection of PartnerGroupModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPartnerIdPartnerModel")
   @JoinColumn(name = "PARTNER_ID")
   public Collection<PartnerGroupModel> getPartnerIdPartnerGroupModelList() throws Exception {
   		return partnerIdPartnerGroupModelList;
   }


   /**
    * Set a list of PartnerGroupModel related objects to the PartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @param partnerGroupModelList the list of related objects.
    */
    public void setPartnerIdPartnerGroupModelList(Collection<PartnerGroupModel> partnerGroupModelList) throws Exception {
		this.partnerIdPartnerGroupModelList = partnerGroupModelList;
   }


   /**
    * Add the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be added.
    */
    
   public void addPartnerIdPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {
      partnerPermissionGroupModel.setRelationPartnerIdPartnerModel(this);
      partnerIdPartnerPermissionGroupModelList.add(partnerPermissionGroupModel);
   }
   
   /**
    * Remove the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be removed.
    */
   
   public void removePartnerIdPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {      
      partnerPermissionGroupModel.setRelationPartnerIdPartnerModel(null);
      partnerIdPartnerPermissionGroupModelList.remove(partnerPermissionGroupModel);      
   }

   /**
    * Get a list of related PartnerPermissionGroupModel objects of the PartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @return Collection of PartnerPermissionGroupModel objects.
    *
    */
   
   //@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPartnerIdPartnerModel")
   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
           fetch = FetchType.LAZY,mappedBy = "relationPartnerIdPartnerModel")
   @JoinColumn(name = "PARTNER_ID")
   @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                                      org.hibernate.annotations.CascadeType.PERSIST,
                                      org.hibernate.annotations.CascadeType.MERGE,
                                      org.hibernate.annotations.CascadeType.REFRESH})
   public Collection<PartnerPermissionGroupModel> getPartnerIdPartnerPermissionGroupModelList() throws Exception {
   		return partnerIdPartnerPermissionGroupModelList;
   }


   /**
    * Set a list of PartnerPermissionGroupModel related objects to the PartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @param partnerPermissionGroupModelList the list of related objects.
    */
    public void setPartnerIdPartnerPermissionGroupModelList(Collection<PartnerPermissionGroupModel> partnerPermissionGroupModelList) throws Exception {
		this.partnerIdPartnerPermissionGroupModelList = partnerPermissionGroupModelList;
   }



   /**
    * Returns the value of the <code>supplierId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSupplierId() {
      if (supplierIdSupplierModel != null) {
         return supplierIdSupplierModel.getSupplierId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>supplierId</code> property.
    *
    * @param supplierId the value for the <code>supplierId</code> property
																																			    */
   
   @javax.persistence.Transient
   public void setSupplierId(Long supplierId) {
      if(supplierId == null)
      {      
      	supplierIdSupplierModel = null;
      }
      else
      {
        supplierIdSupplierModel = new SupplierModel();
      	supplierIdSupplierModel.setSupplierId(supplierId);
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
    * Returns the value of the <code>mnoId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getMnoId() {
      if (mnoIdMnoModel != null) {
         return mnoIdMnoModel.getMnoId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>mnoId</code> property.
    *
    * @param mnoId the value for the <code>mnoId</code> property
																																			    */
   
   @javax.persistence.Transient
   public void setMnoId(Long mnoId) {
      if(mnoId == null)
      {      
      	mnoIdMnoModel = null;
      }
      else
      {
        mnoIdMnoModel = new MnoModel();
      	mnoIdMnoModel.setMnoId(mnoId);
      }      
   }

   /**
    * Returns the value of the <code>distributorId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDistributerId() {
      if (distributerIdDistributorModel != null) {
         return distributerIdDistributorModel.getDistributorId();
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
   public void setDistributerId(Long distributorId) {
      if(distributorId == null)
      {      
      	distributerIdDistributorModel = null;
      }
      else
      {
        distributerIdDistributorModel = new DistributorModel();
      	distributerIdDistributorModel.setDistributorId(distributorId);
      }      
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getBankId() {
      if (bankIdBankModel != null) {
         return bankIdBankModel.getBankId();
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
   public void setBankId(Long bankId) {
      if(bankId == null)
      {      
      	bankIdBankModel = null;
      }
      else
      {
        bankIdBankModel = new BankModel();
      	bankIdBankModel.setBankId(bankId);
      }      
   }

   /**
    * Returns the value of the <code>appUserTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAppUserTypeId() {
      if (appUserTypeIdAppUserTypeModel != null) {
         return appUserTypeIdAppUserTypeModel.getAppUserTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserTypeId</code> property.
    *
    * @param appUserTypeId the value for the <code>appUserTypeId</code> property
					    * @spring.validator type="required"
																															    */
   
   @javax.persistence.Transient
   public void setAppUserTypeId(Long appUserTypeId) {
      if(appUserTypeId == null)
      {      
      	appUserTypeIdAppUserTypeModel = null;
      }
      else
      {
        appUserTypeIdAppUserTypeModel = new AppUserTypeModel();
      	appUserTypeIdAppUserTypeModel.setAppUserTypeId(appUserTypeId);
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPartnerId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&partnerId=" + getPartnerId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "partnerId";
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
    	
    	associationModel.setClassName("SupplierModel");
    	associationModel.setPropertyName("relationSupplierIdSupplierModel");   		
   		associationModel.setValue(getRelationSupplierIdSupplierModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("RetailerModel");
    	associationModel.setPropertyName("relationRetailerIdRetailerModel");   		
   		associationModel.setValue(getRelationRetailerIdRetailerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("OperatorModel");
    	associationModel.setPropertyName("relationOperatorIdOperatorModel");   		
   		associationModel.setValue(getRelationOperatorIdOperatorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("MnoModel");
    	associationModel.setPropertyName("relationMnoIdMnoModel");   		
   		associationModel.setValue(getRelationMnoIdMnoModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("relationDistributerIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributerIdDistributorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationBankIdBankModel");   		
   		associationModel.setValue(getRelationBankIdBankModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserTypeModel");
    	associationModel.setPropertyName("relationAppUserTypeIdAppUserTypeModel");   		
   		associationModel.setValue(getRelationAppUserTypeIdAppUserTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
