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
 * The ConcernPartnerModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernPartnerModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CONCERN_PARTNER_seq",sequenceName = "CONCERN_PARTNER_seq", allocationSize=1)
@Table(name = "CONCERN_PARTNER")
public class ConcernPartnerModel extends BasePersistableModel implements Serializable{
  

   private SupplierModel supplierIdSupplierModel;
   private OperatorModel operatorIdOperatorModel;
   private MnoModel mnoIdMnoModel;
   private ConcernPartnerTypeModel concernPartnerTypeIdConcernPartnerTypeModel;
   private BankModel bankIdBankModel;
   private RetailerModel retailerIdRetailerModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<ConcernModel> recipientPartnerIdConcernModelList = new ArrayList<ConcernModel>();
   private Collection<ConcernModel> initiatorPartnerIdConcernModelList = new ArrayList<ConcernModel>();
   private Collection<ConcernPartnerAsociationModel> associatedPartnerIdConcernPartnerAsociationModelList = new ArrayList<ConcernPartnerAsociationModel>();
   private Collection<ConcernPartnerAsociationModel> partnerIdConcernPartnerAsociationModelList = new ArrayList<ConcernPartnerAsociationModel>();

   private Long concernPartnerId;
   private String name;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Boolean active;

   /**
    * Default constructor.
    */
   public ConcernPartnerModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getConcernPartnerId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setConcernPartnerId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONCERN_PARTNER_seq")
   public Long getConcernPartnerId() {
      return concernPartnerId;
   }

   /**
    * Sets the value of the <code>concernPartnerId</code> property.
    *
    * @param concernPartnerId the value for the <code>concernPartnerId</code> property
    *    
		    */

   public void setConcernPartnerId(Long concernPartnerId) {
      this.concernPartnerId = concernPartnerId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
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
    * @spring.validator-var name="maxlength" value="50"
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
    * Returns the value of the <code>concernPartnerTypeIdConcernPartnerTypeModel</code> relation property.
    *
    * @return the value of the <code>concernPartnerTypeIdConcernPartnerTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CONCERN_PARTNER_TYPE_ID")    
   public ConcernPartnerTypeModel getRelationConcernPartnerTypeIdConcernPartnerTypeModel(){
      return concernPartnerTypeIdConcernPartnerTypeModel;
   }
    
   /**
    * Returns the value of the <code>concernPartnerTypeIdConcernPartnerTypeModel</code> relation property.
    *
    * @return the value of the <code>concernPartnerTypeIdConcernPartnerTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernPartnerTypeModel getConcernPartnerTypeIdConcernPartnerTypeModel(){
      return getRelationConcernPartnerTypeIdConcernPartnerTypeModel();
   }

   /**
    * Sets the value of the <code>concernPartnerTypeIdConcernPartnerTypeModel</code> relation property.
    *
    * @param concernPartnerTypeModel a value for <code>concernPartnerTypeIdConcernPartnerTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationConcernPartnerTypeIdConcernPartnerTypeModel(ConcernPartnerTypeModel concernPartnerTypeModel) {
      this.concernPartnerTypeIdConcernPartnerTypeModel = concernPartnerTypeModel;
   }
   
   /**
    * Sets the value of the <code>concernPartnerTypeIdConcernPartnerTypeModel</code> relation property.
    *
    * @param concernPartnerTypeModel a value for <code>concernPartnerTypeIdConcernPartnerTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setConcernPartnerTypeIdConcernPartnerTypeModel(ConcernPartnerTypeModel concernPartnerTypeModel) {
      if(null != concernPartnerTypeModel)
      {
      	setRelationConcernPartnerTypeIdConcernPartnerTypeModel((ConcernPartnerTypeModel)concernPartnerTypeModel.clone());
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
    * @param bankModel a value for <code>retailerIdRetailerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRetailerIdRetailerModel(RetailerModel retailerModel) {
      this.retailerIdRetailerModel = retailerModel;
   }
   
   /**
    * Sets the value of the <code>bankIdBankModel</code> relation property.
    *
    * @param bankModel a value for <code>bankIdBankModel</code>.
    */
   @javax.persistence.Transient
   public void setRetailerIdRetailerModel(RetailerModel retailerModel) {
      if(null != retailerModel)
      {
      	setRelationRetailerIdRetailerModel((RetailerModel)retailerModel.clone());
      }      
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
    * Add the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be added.
    */
    
   public void addRecipientPartnerIdConcernModel(ConcernModel concernModel) {
      concernModel.setRelationRecipientPartnerIdConcernPartnerModel(this);
      recipientPartnerIdConcernModelList.add(concernModel);
   }
   
   /**
    * Remove the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be removed.
    */
   
   public void removeRecipientPartnerIdConcernModel(ConcernModel concernModel) {      
      concernModel.setRelationRecipientPartnerIdConcernPartnerModel(null);
      recipientPartnerIdConcernModelList.remove(concernModel);      
   }

   /**
    * Get a list of related ConcernModel objects of the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the RecipientPartnerId member.
    *
    * @return Collection of ConcernModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationRecipientPartnerIdConcernPartnerModel")
   @JoinColumn(name = "RECIPIENT_PARTNER_ID")
   public Collection<ConcernModel> getRecipientPartnerIdConcernModelList() throws Exception {
   		return recipientPartnerIdConcernModelList;
   }


   /**
    * Set a list of ConcernModel related objects to the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the RecipientPartnerId member.
    *
    * @param concernModelList the list of related objects.
    */
    public void setRecipientPartnerIdConcernModelList(Collection<ConcernModel> concernModelList) throws Exception {
		this.recipientPartnerIdConcernModelList = concernModelList;
   }


   /**
    * Add the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be added.
    */
    
   public void addInitiatorPartnerIdConcernModel(ConcernModel concernModel) {
      concernModel.setRelationInitiatorPartnerIdConcernPartnerModel(this);
      initiatorPartnerIdConcernModelList.add(concernModel);
   }
   
   /**
    * Remove the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be removed.
    */
   
   public void removeInitiatorPartnerIdConcernModel(ConcernModel concernModel) {      
      concernModel.setRelationInitiatorPartnerIdConcernPartnerModel(null);
      initiatorPartnerIdConcernModelList.remove(concernModel);      
   }

   /**
    * Get a list of related ConcernModel objects of the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the InitiatorPartnerId member.
    *
    * @return Collection of ConcernModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationInitiatorPartnerIdConcernPartnerModel")
   @JoinColumn(name = "INITIATOR_PARTNER_ID")
   public Collection<ConcernModel> getInitiatorPartnerIdConcernModelList() throws Exception {
   		return initiatorPartnerIdConcernModelList;
   }


   /**
    * Set a list of ConcernModel related objects to the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the InitiatorPartnerId member.
    *
    * @param concernModelList the list of related objects.
    */
    public void setInitiatorPartnerIdConcernModelList(Collection<ConcernModel> concernModelList) throws Exception {
		this.initiatorPartnerIdConcernModelList = concernModelList;
   }


   /**
    * Add the related ConcernPartnerAsociationModel to this one-to-many relation.
    *
    * @param concernPartnerAsociationModel object to be added.
    */
    
   public void addAssociatedPartnerIdConcernPartnerAsociationModel(ConcernPartnerAsociationModel concernPartnerAsociationModel) {
      concernPartnerAsociationModel.setRelationAssociatedPartnerIdConcernPartnerModel(this);
      associatedPartnerIdConcernPartnerAsociationModelList.add(concernPartnerAsociationModel);
   }
   
   /**
    * Remove the related ConcernPartnerAsociationModel to this one-to-many relation.
    *
    * @param concernPartnerAsociationModel object to be removed.
    */
   
   public void removeAssociatedPartnerIdConcernPartnerAsociationModel(ConcernPartnerAsociationModel concernPartnerAsociationModel) {      
      concernPartnerAsociationModel.setRelationAssociatedPartnerIdConcernPartnerModel(null);
      associatedPartnerIdConcernPartnerAsociationModelList.remove(concernPartnerAsociationModel);      
   }

   /**
    * Get a list of related ConcernPartnerAsociationModel objects of the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the AssociatedPartnerId member.
    *
    * @return Collection of ConcernPartnerAsociationModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAssociatedPartnerIdConcernPartnerModel")
   @JoinColumn(name = "ASSOCIATED_PARTNER_ID")
   public Collection<ConcernPartnerAsociationModel> getAssociatedPartnerIdConcernPartnerAsociationModelList() throws Exception {
   		return associatedPartnerIdConcernPartnerAsociationModelList;
   }


   /**
    * Set a list of ConcernPartnerAsociationModel related objects to the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the AssociatedPartnerId member.
    *
    * @param concernPartnerAsociationModelList the list of related objects.
    */
    public void setAssociatedPartnerIdConcernPartnerAsociationModelList(Collection<ConcernPartnerAsociationModel> concernPartnerAsociationModelList) throws Exception {
		this.associatedPartnerIdConcernPartnerAsociationModelList = concernPartnerAsociationModelList;
   }


   /**
    * Add the related ConcernPartnerAsociationModel to this one-to-many relation.
    *
    * @param concernPartnerAsociationModel object to be added.
    */
    
   public void addPartnerIdConcernPartnerAsociationModel(ConcernPartnerAsociationModel concernPartnerAsociationModel) {
      concernPartnerAsociationModel.setRelationPartnerIdConcernPartnerModel(this);
      partnerIdConcernPartnerAsociationModelList.add(concernPartnerAsociationModel);
   }
   
   /**
    * Remove the related ConcernPartnerAsociationModel to this one-to-many relation.
    *
    * @param concernPartnerAsociationModel object to be removed.
    */
   
   public void removePartnerIdConcernPartnerAsociationModel(ConcernPartnerAsociationModel concernPartnerAsociationModel) {      
      concernPartnerAsociationModel.setRelationPartnerIdConcernPartnerModel(null);
      partnerIdConcernPartnerAsociationModelList.remove(concernPartnerAsociationModel);      
   }

   /**
    * Get a list of related ConcernPartnerAsociationModel objects of the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @return Collection of ConcernPartnerAsociationModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPartnerIdConcernPartnerModel")
   @JoinColumn(name = "PARTNER_ID")
   public Collection<ConcernPartnerAsociationModel> getPartnerIdConcernPartnerAsociationModelList() throws Exception {
   		return partnerIdConcernPartnerAsociationModelList;
   }


   /**
    * Set a list of ConcernPartnerAsociationModel related objects to the ConcernPartnerModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerId member.
    *
    * @param concernPartnerAsociationModelList the list of related objects.
    */
    public void setPartnerIdConcernPartnerAsociationModelList(Collection<ConcernPartnerAsociationModel> concernPartnerAsociationModelList) throws Exception {
		this.partnerIdConcernPartnerAsociationModelList = concernPartnerAsociationModelList;
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
    * Returns the value of the <code>concernPartnerTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getConcernPartnerTypeId() {
      if (concernPartnerTypeIdConcernPartnerTypeModel != null) {
         return concernPartnerTypeIdConcernPartnerTypeModel.getConcernPartnerTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernPartnerTypeId</code> property.
    *
    * @param concernPartnerTypeId the value for the <code>concernPartnerTypeId</code> property
					    * @spring.validator type="required"
																													    */
   
   @javax.persistence.Transient
   public void setConcernPartnerTypeId(Long concernPartnerTypeId) {
      if(concernPartnerTypeId == null)
      {      
      	concernPartnerTypeIdConcernPartnerTypeModel = null;
      }
      else
      {
        concernPartnerTypeIdConcernPartnerTypeModel = new ConcernPartnerTypeModel();
      	concernPartnerTypeIdConcernPartnerTypeModel.setConcernPartnerTypeId(concernPartnerTypeId);
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
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
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
        checkBox += "_"+ getConcernPartnerId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&concernPartnerId=" + getConcernPartnerId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "concernPartnerId";
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
    	
    	associationModel.setClassName("SupplierModel");
    	associationModel.setPropertyName("relationSupplierIdSupplierModel");   		
   		associationModel.setValue(getRelationSupplierIdSupplierModel());
   		
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
    	
    	associationModel.setClassName("ConcernPartnerTypeModel");
    	associationModel.setPropertyName("relationConcernPartnerTypeIdConcernPartnerTypeModel");   		
   		associationModel.setValue(getRelationConcernPartnerTypeIdConcernPartnerTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationBankIdBankModel");   		
   		associationModel.setValue(getRelationBankIdBankModel());
   		
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
   		
			    	
    	return associationModelList;
    }    
          
}
