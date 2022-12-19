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
 * The DistributorContactModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistributorContactModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DISTRIBUTOR_CONTACT_seq",sequenceName = "DISTRIBUTOR_CONTACT_seq", allocationSize=1)
@Table(name = "DISTRIBUTOR_CONTACT")
public class DistributorContactModel extends BasePersistableModel implements Serializable {
  

   private DistributorLevelModel distributorLevelIdDistributorLevelModel;
   private DistributorContactModel managingContactIdDistributorContactModel;
   private DistributorModel distributorIdDistributorModel;
   private AreaModel areaIdAreaModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<AppUserModel> distributorContactIdAppUserModelList = new ArrayList<AppUserModel>();
   private Collection<DistributorContactModel> managingContactIdDistributorContactModelList = new ArrayList<DistributorContactModel>();
   private Collection<SmartMoneyAccountModel> distributorContactIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
   private Collection<TransactionModel> distributorNmContactIdTransactionModelList = new ArrayList<TransactionModel>();
   private Collection<TransactionModel> toDistContactIdTransactionModelList = new ArrayList<TransactionModel>();
   private Collection<TransactionModel> fromDistContactIdTransactionModelList = new ArrayList<TransactionModel>();

   private Long distributorContactId;
   private Double balance;
   private Boolean head;
   private Boolean active;
   private String description;
   private String comments;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public DistributorContactModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistributorContactId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistributorContactId(primaryKey);
    }

   /**
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_CONTACT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DISTRIBUTOR_CONTACT_seq")
   public Long getDistributorContactId() {
      return distributorContactId;
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
    *    
		    */

   public void setDistributorContactId(Long distributorContactId) {
      this.distributorContactId = distributorContactId;
   }

   /**
    * Returns the value of the <code>balance</code> property.
    *
    */
      @Column(name = "BALANCE" , nullable = false )
   public Double getBalance() {
      return balance;
   }

   /**
    * Sets the value of the <code>balance</code> property.
    *
    * @param balance the value for the <code>balance</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBalance(Double balance) {
      this.balance = balance;
   }

   /**
    * Returns the value of the <code>head</code> property.
    *
    */
      @Column(name = "IS_HEAD" , nullable = false )
   public Boolean getHead() {
      return head;
   }

   /**
    * Sets the value of the <code>head</code> property.
    *
    * @param head the value for the <code>head</code> property
    *    
		    */

   public void setHead(Boolean head) {
      this.head = head;
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
    * Returns the value of the <code>distributorLevelIdDistributorLevelModel</code> relation property.
    *
    * @return the value of the <code>distributorLevelIdDistributorLevelModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DISTRIBUTOR_LEVEL_ID")    
   public DistributorLevelModel getRelationDistributorLevelIdDistributorLevelModel(){
      return distributorLevelIdDistributorLevelModel;
   }
    
   /**
    * Returns the value of the <code>distributorLevelIdDistributorLevelModel</code> relation property.
    *
    * @return the value of the <code>distributorLevelIdDistributorLevelModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorLevelModel getDistributorLevelIdDistributorLevelModel(){
      return getRelationDistributorLevelIdDistributorLevelModel();
   }

   /**
    * Sets the value of the <code>distributorLevelIdDistributorLevelModel</code> relation property.
    *
    * @param distributorLevelModel a value for <code>distributorLevelIdDistributorLevelModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDistributorLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      this.distributorLevelIdDistributorLevelModel = distributorLevelModel;
   }
   
   /**
    * Sets the value of the <code>distributorLevelIdDistributorLevelModel</code> relation property.
    *
    * @param distributorLevelModel a value for <code>distributorLevelIdDistributorLevelModel</code>.
    */
   @javax.persistence.Transient
   public void setDistributorLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      if(null != distributorLevelModel)
      {
      	setRelationDistributorLevelIdDistributorLevelModel((DistributorLevelModel)distributorLevelModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>managingContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>managingContactIdDistributorContactModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "MANAGING_CONTACT_ID")    
   public DistributorContactModel getRelationManagingContactIdDistributorContactModel(){
      return managingContactIdDistributorContactModel;
   }
    
   /**
    * Returns the value of the <code>managingContactIdDistributorContactModel</code> relation property.
    *
    * @return the value of the <code>managingContactIdDistributorContactModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorContactModel getManagingContactIdDistributorContactModel(){
      return getRelationManagingContactIdDistributorContactModel();
   }

   /**
    * Sets the value of the <code>managingContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>managingContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationManagingContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      this.managingContactIdDistributorContactModel = distributorContactModel;
   }
   
   /**
    * Sets the value of the <code>managingContactIdDistributorContactModel</code> relation property.
    *
    * @param distributorContactModel a value for <code>managingContactIdDistributorContactModel</code>.
    */
   @javax.persistence.Transient
   public void setManagingContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      if(null != distributorContactModel)
      {
      	setRelationManagingContactIdDistributorContactModel((DistributorContactModel)distributorContactModel.clone());
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
    * Returns the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>areaIdAreaModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "AREA_ID")    
   public AreaModel getRelationAreaIdAreaModel(){
      return areaIdAreaModel;
   }
    
   /**
    * Returns the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>areaIdAreaModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AreaModel getAreaIdAreaModel(){
      return getRelationAreaIdAreaModel();
   }

   /**
    * Sets the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>areaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAreaIdAreaModel(AreaModel areaModel) {
      this.areaIdAreaModel = areaModel;
   }
   
   /**
    * Sets the value of the <code>areaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>areaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setAreaIdAreaModel(AreaModel areaModel) {
      if(null != areaModel)
      {
      	setRelationAreaIdAreaModel((AreaModel)areaModel.clone());
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
    * Add the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be added.
    */
    
   public void addDistributorContactIdAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationDistributorContactIdDistributorContactModel(this);
      distributorContactIdAppUserModelList.add(appUserModel);
   }
   
   /**
    * Remove the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be removed.
    */
   
   public void removeDistributorContactIdAppUserModel(AppUserModel appUserModel) {      
      appUserModel.setRelationDistributorContactIdDistributorContactModel(null);
      distributorContactIdAppUserModelList.remove(appUserModel);      
   }

   /**
    * Get a list of related AppUserModel objects of the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorContactId member.
    *
    * @return Collection of AppUserModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorContactIdDistributorContactModel")
   @JoinColumn(name = "DISTRIBUTOR_CONTACT_ID")
   public Collection<AppUserModel> getDistributorContactIdAppUserModelList() throws Exception {
   		return distributorContactIdAppUserModelList;
   }


   /**
    * Set a list of AppUserModel related objects to the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorContactId member.
    *
    * @param appUserModelList the list of related objects.
    */
    public void setDistributorContactIdAppUserModelList(Collection<AppUserModel> appUserModelList) throws Exception {
		this.distributorContactIdAppUserModelList = appUserModelList;
   }


   /**
    * Add the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be added.
    */
    
   public void addManagingContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationManagingContactIdDistributorContactModel(this);
      managingContactIdDistributorContactModelList.add(distributorContactModel);
   }
   
   /**
    * Remove the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be removed.
    */
   
   public void removeManagingContactIdDistributorContactModel(DistributorContactModel distributorContactModel) {      
      distributorContactModel.setRelationManagingContactIdDistributorContactModel(null);
      managingContactIdDistributorContactModelList.remove(distributorContactModel);      
   }

   /**
    * Get a list of related DistributorContactModel objects of the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the ManagingContactId member.
    *
    * @return Collection of DistributorContactModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationManagingContactIdDistributorContactModel")
   @JoinColumn(name = "MANAGING_CONTACT_ID")
   public Collection<DistributorContactModel> getManagingContactIdDistributorContactModelList() throws Exception {
   		return managingContactIdDistributorContactModelList;
   }


   /**
    * Set a list of DistributorContactModel related objects to the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the ManagingContactId member.
    *
    * @param distributorContactModelList the list of related objects.
    */
    public void setManagingContactIdDistributorContactModelList(Collection<DistributorContactModel> distributorContactModelList) throws Exception {
		this.managingContactIdDistributorContactModelList = distributorContactModelList;
   }


   /**
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */
    
   public void addDistributorContactIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationDistributorContactIdDistributorContactModel(this);
      distributorContactIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }
   
   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */
   
   public void removeDistributorContactIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {      
      smartMoneyAccountModel.setRelationDistributorContactIdDistributorContactModel(null);
      distributorContactIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);      
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorContactId member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorContactIdDistributorContactModel")
   @JoinColumn(name = "DISTRIBUTOR_CONTACT_ID")
   public Collection<SmartMoneyAccountModel> getDistributorContactIdSmartMoneyAccountModelList() throws Exception {
   		return distributorContactIdSmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorContactId member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
    public void setDistributorContactIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
		this.distributorContactIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addDistributorNmContactIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationDistributorNmContactIdDistributorContactModel(this);
      distributorNmContactIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeDistributorNmContactIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationDistributorNmContactIdDistributorContactModel(null);
      distributorNmContactIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorNmContactId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorNmContactIdDistributorContactModel")
   @JoinColumn(name = "DISTRIBUTOR_NM_CONTACT_ID")
   public Collection<TransactionModel> getDistributorNmContactIdTransactionModelList() throws Exception {
   		return distributorNmContactIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorNmContactId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setDistributorNmContactIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.distributorNmContactIdTransactionModelList = transactionModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addToDistContactIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationToDistContactIdDistributorContactModel(this);
      toDistContactIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeToDistContactIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationToDistContactIdDistributorContactModel(null);
      toDistContactIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the ToDistContactId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationToDistContactIdDistributorContactModel")
   @JoinColumn(name = "TO_DIST_CONTACT_ID")
   public Collection<TransactionModel> getToDistContactIdTransactionModelList() throws Exception {
   		return toDistContactIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the ToDistContactId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setToDistContactIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.toDistContactIdTransactionModelList = transactionModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addFromDistContactIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationFromDistContactIdDistributorContactModel(this);
      fromDistContactIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeFromDistContactIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationFromDistContactIdDistributorContactModel(null);
      fromDistContactIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the FromDistContactId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFromDistContactIdDistributorContactModel")
   @JoinColumn(name = "FROM_DIST_CONTACT_ID")
   public Collection<TransactionModel> getFromDistContactIdTransactionModelList() throws Exception {
   		return fromDistContactIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the DistributorContactModel object.
    * These objects are in a bidirectional one-to-many relation by the FromDistContactId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setFromDistContactIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.fromDistContactIdTransactionModelList = transactionModelList;
   }



   /**
    * Returns the value of the <code>distributorLevelId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDistributorLevelId() {
      if (distributorLevelIdDistributorLevelModel != null) {
         return distributorLevelIdDistributorLevelModel.getDistributorLevelId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorLevelId</code> property.
    *
    * @param distributorLevelId the value for the <code>distributorLevelId</code> property
									    * @spring.validator type="required"
																									    */
   
   @javax.persistence.Transient
   public void setDistributorLevelId(Long distributorLevelId) {
      if(distributorLevelId == null)
      {      
      	distributorLevelIdDistributorLevelModel = null;
      }
      else
      {
        distributorLevelIdDistributorLevelModel = new DistributorLevelModel();
      	distributorLevelIdDistributorLevelModel.setDistributorLevelId(distributorLevelId);
      }      
   }

   /**
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getManagingContactId() {
      if (managingContactIdDistributorContactModel != null) {
         return managingContactIdDistributorContactModel.getDistributorContactId();
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
   public void setManagingContactId(Long distributorContactId) {
      if(distributorContactId == null)
      {      
      	managingContactIdDistributorContactModel = null;
      }
      else
      {
        managingContactIdDistributorContactModel = new DistributorContactModel();
      	managingContactIdDistributorContactModel.setDistributorContactId(distributorContactId);
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
					    * @spring.validator type="required"
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
    * Returns the value of the <code>areaId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAreaId() {
      if (areaIdAreaModel != null) {
         return areaIdAreaModel.getAreaId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>areaId</code> property.
    *
    * @param areaId the value for the <code>areaId</code> property
											    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setAreaId(Long areaId) {
      if(areaId == null)
      {      
      	areaIdAreaModel = null;
      }
      else
      {
        areaIdAreaModel = new AreaModel();
      	areaIdAreaModel.setAreaId(areaId);
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
        checkBox += "_"+ getDistributorContactId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&distributorContactId=" + getDistributorContactId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "distributorContactId";
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
    	
    	associationModel.setClassName("DistributorLevelModel");
    	associationModel.setPropertyName("relationDistributorLevelIdDistributorLevelModel");   		
   		associationModel.setValue(getRelationDistributorLevelIdDistributorLevelModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorContactModel");
    	associationModel.setPropertyName("relationManagingContactIdDistributorContactModel");   		
   		associationModel.setValue(getRelationManagingContactIdDistributorContactModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributorIdDistributorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AreaModel");
    	associationModel.setPropertyName("relationAreaIdAreaModel");   		
   		associationModel.setValue(getRelationAreaIdAreaModel());
   		
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
