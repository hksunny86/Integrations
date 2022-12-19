package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The CommissionStakeholderModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionStakeholderModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMMISSION_STAKEHOLDER_seq",sequenceName = "COMMISSION_STAKEHOLDER_seq", allocationSize=1)
@Table(name = "COMMISSION_STAKEHOLDER")
public class CommissionStakeholderModel extends BasePersistableModel implements Serializable,RowMapper {
  

   private StakeholderTypeModel stakeholderTypeIdStakeholderTypeModel;
   private RetailerModel retailerIdRetailerModel;
   private OperatorModel operatorIdOperatorModel;
   private DistributorModel distributorIdDistributorModel;
   private BankModel bankIdBankModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private CommissionShAcctsTypeModel cmshaccttypeIdCommissionShAcctsTypeModel;

   private Collection<CommissionRateModel> commissionStakeholderIdCommissionRateModelList = new ArrayList<CommissionRateModel>();
   private Collection<CommissionShAcctsModel> commissionStakeholderIdCommissionShAcctsModelList = new ArrayList<CommissionShAcctsModel>();
   private Collection<CommissionShSharesModel> commissionStakeholderIdCommissionShSharesModelList = new ArrayList<CommissionShSharesModel>();
   private Collection<CommissionTransactionModel> commissionStakeholderIdCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();
   private Collection<StakeholderBankInfoModel> commissionStakeholderIdStakeholderBankInfoModelList = new ArrayList<StakeholderBankInfoModel>();

   private Long commissionStakeholderId;
   private String name;
   private String contactName;
   private String description;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private String comments;
   private Boolean displayOnProductScreen;
   private Boolean filer;

   /**
    * Default constructor.
    */
   public CommissionStakeholderModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommissionStakeholderId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommissionStakeholderId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionStakeholderId</code> property.
    *
    */
      @Column(name = "COMMISSION_STAKEHOLDER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMISSION_STAKEHOLDER_seq")
   public Long getCommissionStakeholderId() {
      return commissionStakeholderId;
   }

   /**
    * Sets the value of the <code>commissionStakeholderId</code> property.
    *
    * @param commissionStakeholderId the value for the <code>commissionStakeholderId</code> property
    *    
		    */

   public void setCommissionStakeholderId(Long commissionStakeholderId) {
      this.commissionStakeholderId = commissionStakeholderId;
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
    * Returns the value of the <code>contactName</code> property.
    *
    */
      @Column(name = "CONTACT_NAME" , nullable = false , length=50 )
   public String getContactName() {
      return contactName;
   }

   /**
    * Sets the value of the <code>contactName</code> property.
    *
    * @param contactName the value for the <code>contactName</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setContactName(String contactName) {
      this.contactName = contactName;
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
    * Returns the value of the <code>stakeholderTypeIdStakeholderTypeModel</code> relation property.
    *
    * @return the value of the <code>stakeholderTypeIdStakeholderTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "STAKEHOLDER_TYPE_ID")    
   public StakeholderTypeModel getRelationStakeholderTypeIdStakeholderTypeModel(){
      return stakeholderTypeIdStakeholderTypeModel;
   }
    
   /**
    * Returns the value of the <code>stakeholderTypeIdStakeholderTypeModel</code> relation property.
    *
    * @return the value of the <code>stakeholderTypeIdStakeholderTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public StakeholderTypeModel getStakeholderTypeIdStakeholderTypeModel(){
      return getRelationStakeholderTypeIdStakeholderTypeModel();
   }

   /**
    * Sets the value of the <code>stakeholderTypeIdStakeholderTypeModel</code> relation property.
    *
    * @param stakeholderTypeModel a value for <code>stakeholderTypeIdStakeholderTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationStakeholderTypeIdStakeholderTypeModel(StakeholderTypeModel stakeholderTypeModel) {
      this.stakeholderTypeIdStakeholderTypeModel = stakeholderTypeModel;
   }
   
   /**
    * Sets the value of the <code>stakeholderTypeIdStakeholderTypeModel</code> relation property.
    *
    * @param stakeholderTypeModel a value for <code>stakeholderTypeIdStakeholderTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setStakeholderTypeIdStakeholderTypeModel(StakeholderTypeModel stakeholderTypeModel) {
      if(null != stakeholderTypeModel)
      {
      	setRelationStakeholderTypeIdStakeholderTypeModel((StakeholderTypeModel)stakeholderTypeModel.clone());
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
    * Add the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be added.
    */
    
   public void addCommissionStakeholderIdCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(this);
      commissionStakeholderIdCommissionRateModelList.add(commissionRateModel);
   }
   
   /**
    * Remove the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be removed.
    */
   
   public void removeCommissionStakeholderIdCommissionRateModel(CommissionRateModel commissionRateModel) {      
      commissionRateModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(null);
      commissionStakeholderIdCommissionRateModelList.remove(commissionRateModel);      
   }

   /**
    * Get a list of related CommissionRateModel objects of the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @return Collection of CommissionRateModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommissionStakeholderIdCommissionStakeholderModel")
   @JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")
   public Collection<CommissionRateModel> getCommissionStakeholderIdCommissionRateModelList() throws Exception {
   		return commissionStakeholderIdCommissionRateModelList;
   }


   /**
    * Set a list of CommissionRateModel related objects to the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @param commissionRateModelList the list of related objects.
    */
    public void setCommissionStakeholderIdCommissionRateModelList(Collection<CommissionRateModel> commissionRateModelList) throws Exception {
		this.commissionStakeholderIdCommissionRateModelList = commissionRateModelList;
   }


   /**
    * Add the related CommissionShAcctsModel to this one-to-many relation.
    *
    * @param commissionShAcctsModel object to be added.
    */
    
   public void addCommissionStakeholderIdCommissionShAcctsModel(CommissionShAcctsModel commissionShAcctsModel) {
      commissionShAcctsModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(this);
      commissionStakeholderIdCommissionShAcctsModelList.add(commissionShAcctsModel);
   }
   
   /**
    * Remove the related CommissionShAcctsModel to this one-to-many relation.
    *
    * @param commissionShAcctsModel object to be removed.
    */
   
   public void removeCommissionStakeholderIdCommissionShAcctsModel(CommissionShAcctsModel commissionShAcctsModel) {      
      commissionShAcctsModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(null);
      commissionStakeholderIdCommissionShAcctsModelList.remove(commissionShAcctsModel);      
   }

   /**
    * Get a list of related CommissionShAcctsModel objects of the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @return Collection of CommissionShAcctsModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommissionStakeholderIdCommissionStakeholderModel")
   @JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")
   public Collection<CommissionShAcctsModel> getCommissionStakeholderIdCommissionShAcctsModelList() throws Exception {
   		return commissionStakeholderIdCommissionShAcctsModelList;
   }


   /**
    * Set a list of CommissionShAcctsModel related objects to the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @param commissionShAcctsModelList the list of related objects.
    */
    public void setCommissionStakeholderIdCommissionShAcctsModelList(Collection<CommissionShAcctsModel> commissionShAcctsModelList) throws Exception {
		this.commissionStakeholderIdCommissionShAcctsModelList = commissionShAcctsModelList;
   }


   /**
    * Add the related CommissionShSharesModel to this one-to-many relation.
    *
    * @param commissionShSharesModel object to be added.
    */
    
   public void addCommissionStakeholderIdCommissionShSharesModel(CommissionShSharesModel commissionShSharesModel) {
      commissionShSharesModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(this);
      commissionStakeholderIdCommissionShSharesModelList.add(commissionShSharesModel);
   }
   
   /**
    * Remove the related CommissionShSharesModel to this one-to-many relation.
    *
    * @param commissionShSharesModel object to be removed.
    */
   
   public void removeCommissionStakeholderIdCommissionShSharesModel(CommissionShSharesModel commissionShSharesModel) {      
      commissionShSharesModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(null);
      commissionStakeholderIdCommissionShSharesModelList.remove(commissionShSharesModel);      
   }

   /**
    * Get a list of related CommissionShSharesModel objects of the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @return Collection of CommissionShSharesModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommissionStakeholderIdCommissionStakeholderModel")
   @JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")
   public Collection<CommissionShSharesModel> getCommissionStakeholderIdCommissionShSharesModelList() throws Exception {
   		return commissionStakeholderIdCommissionShSharesModelList;
   }


   /**
    * Set a list of CommissionShSharesModel related objects to the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @param commissionShSharesModelList the list of related objects.
    */
    public void setCommissionStakeholderIdCommissionShSharesModelList(Collection<CommissionShSharesModel> commissionShSharesModelList) throws Exception {
		this.commissionStakeholderIdCommissionShSharesModelList = commissionShSharesModelList;
   }


   /**
    * Add the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be added.
    */
    
   public void addCommissionStakeholderIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(this);
      commissionStakeholderIdCommissionTransactionModelList.add(commissionTransactionModel);
   }
   
   /**
    * Remove the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be removed.
    */
   
   public void removeCommissionStakeholderIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {      
      commissionTransactionModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(null);
      commissionStakeholderIdCommissionTransactionModelList.remove(commissionTransactionModel);      
   }

   /**
    * Get a list of related CommissionTransactionModel objects of the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @return Collection of CommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommissionStakeholderIdCommissionStakeholderModel")
   @JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")
   public Collection<CommissionTransactionModel> getCommissionStakeholderIdCommissionTransactionModelList() throws Exception {
   		return commissionStakeholderIdCommissionTransactionModelList;
   }


   /**
    * Set a list of CommissionTransactionModel related objects to the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @param commissionTransactionModelList the list of related objects.
    */
    public void setCommissionStakeholderIdCommissionTransactionModelList(Collection<CommissionTransactionModel> commissionTransactionModelList) throws Exception {
		this.commissionStakeholderIdCommissionTransactionModelList = commissionTransactionModelList;
   }


   /**
    * Add the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be added.
    */
    
   public void addCommissionStakeholderIdStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      stakeholderBankInfoModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(this);
      commissionStakeholderIdStakeholderBankInfoModelList.add(stakeholderBankInfoModel);
   }
   
   /**
    * Remove the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be removed.
    */
   
   public void removeCommissionStakeholderIdStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {      
      stakeholderBankInfoModel.setRelationCommissionStakeholderIdCommissionStakeholderModel(null);
      commissionStakeholderIdStakeholderBankInfoModelList.remove(stakeholderBankInfoModel);      
   }

   /**
    * Get a list of related StakeholderBankInfoModel objects of the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @return Collection of StakeholderBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommissionStakeholderIdCommissionStakeholderModel")
   @JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")
   public Collection<StakeholderBankInfoModel> getCommissionStakeholderIdStakeholderBankInfoModelList() throws Exception {
   		return commissionStakeholderIdStakeholderBankInfoModelList;
   }


   /**
    * Set a list of StakeholderBankInfoModel related objects to the CommissionStakeholderModel object.
    * These objects are in a bidirectional one-to-many relation by the CommissionStakeholderId member.
    *
    * @param stakeholderBankInfoModelList the list of related objects.
    */
    public void setCommissionStakeholderIdStakeholderBankInfoModelList(Collection<StakeholderBankInfoModel> stakeholderBankInfoModelList) throws Exception {
		this.commissionStakeholderIdStakeholderBankInfoModelList = stakeholderBankInfoModelList;
   }



   /**
    * Returns the value of the <code>stakeholderTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getStakeholderTypeId() {
      if (stakeholderTypeIdStakeholderTypeModel != null) {
         return stakeholderTypeIdStakeholderTypeModel.getStakeholderTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>stakeholderTypeId</code> property.
    *
    * @param stakeholderTypeId the value for the <code>stakeholderTypeId</code> property
											    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setStakeholderTypeId(Long stakeholderTypeId) {
      if(stakeholderTypeId == null)
      {      
      	stakeholderTypeIdStakeholderTypeModel = null;
      }
      else
      {
        stakeholderTypeIdStakeholderTypeModel = new StakeholderTypeModel();
      	stakeholderTypeIdStakeholderTypeModel.setStakeholderTypeId(stakeholderTypeId);
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
        checkBox += "_"+ getCommissionStakeholderId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commissionStakeholderId=" + getCommissionStakeholderId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commissionStakeholderId";
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
    	
    	associationModel.setClassName("StakeholderTypeModel");
    	associationModel.setPropertyName("relationStakeholderTypeIdStakeholderTypeModel");   		
   		associationModel.setValue(getRelationStakeholderTypeIdStakeholderTypeModel());
   		
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
    	
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributorIdDistributorModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("BankModel");
    	associationModel.setPropertyName("relationBankIdBankModel");   		
   		associationModel.setValue(getRelationBankIdBankModel());
   		
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
   		
   		associationModel = new AssociationModel();
   		associationModel.setClassName("CommissionShAcctsTypeModel");
    	associationModel.setPropertyName("relationCmshaccttypeIdCommissionShAcctsTypeModel");   		
   		associationModel.setValue(getRelationCmshaccttypeIdCommissionShAcctsTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }

    @Column(name = "DISPLAY_ON_PRODUCT_SCREEN")
	public Boolean getDisplayOnProductScreen() {
		return displayOnProductScreen;
	}

	public void setDisplayOnProductScreen(Boolean displayOnProductScreen) {
		this.displayOnProductScreen = displayOnProductScreen;
	}


   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CMSHACCTTYPE_ID")
   public CommissionShAcctsTypeModel getRelationCmshaccttypeIdCommissionShAcctsTypeModel(){
      return cmshaccttypeIdCommissionShAcctsTypeModel;
   }

   /**
    * Returns the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    * @return the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommissionShAcctsTypeModel getCmshaccttypeIdCommissionShAcctsTypeModel(){
      return getRelationCmshaccttypeIdCommissionShAcctsTypeModel();
   }

   /**
    * Sets the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    * @param commissionShAcctsTypeModel a value for <code>cmshaccttypeIdCommissionShAcctsTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCmshaccttypeIdCommissionShAcctsTypeModel(CommissionShAcctsTypeModel commissionShAcctsTypeModel) {
      this.cmshaccttypeIdCommissionShAcctsTypeModel = commissionShAcctsTypeModel;
   }

   /**
    * Sets the value of the <code>cmshaccttypeIdCommissionShAcctsTypeModel</code> relation property.
    *
    * @param commissionShAcctsTypeModel a value for <code>cmshaccttypeIdCommissionShAcctsTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setCmshaccttypeIdCommissionShAcctsTypeModel(CommissionShAcctsTypeModel commissionShAcctsTypeModel) {
      if(null != commissionShAcctsTypeModel)
      {
         setRelationCmshaccttypeIdCommissionShAcctsTypeModel((CommissionShAcctsTypeModel)commissionShAcctsTypeModel.clone());
      }
   }
   @javax.persistence.Transient
   public Long getCmshaccttypeId() {
      if (cmshaccttypeIdCommissionShAcctsTypeModel != null) {
         return cmshaccttypeIdCommissionShAcctsTypeModel.getCmshacctstypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>cmshacctstypeId</code> property.
    *
    * @param cmshacctstypeId the value for the <code>cmshacctstypeId</code> property
    * @spring.validator type="required"
    */

   @javax.persistence.Transient
   public void setCmshaccttypeId(Long cmshacctstypeId) {
      if(cmshacctstypeId == null)
      {
         cmshaccttypeIdCommissionShAcctsTypeModel = null;
      }
      else
      {
         cmshaccttypeIdCommissionShAcctsTypeModel = new CommissionShAcctsTypeModel();
         cmshaccttypeIdCommissionShAcctsTypeModel.setCmshacctstypeId(cmshacctstypeId);
      }
   }
   
	@Column(name = "IS_FILER")
	public Boolean getFiler() {
		return filer;
	}

	public void setFiler(Boolean filer) {
		this.filer = filer;
	}


   @Override
   public Object mapRow(ResultSet resultSet, int i) throws SQLException {
      CommissionStakeholderModel vo = new CommissionStakeholderModel();
      vo.setCommissionStakeholderId(resultSet.getLong("STAKEHOLDER_TYPE_ID"));
      vo.setRetailerId(resultSet.getLong("RETAILER_ID"));
      vo.setOperatorId(resultSet.getLong("OPERATOR_ID"));
      vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
      vo.setBankId(resultSet.getLong("BANK_ID"));
      vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
      vo.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
      //vo.setComm(resultSet.getLong("CMSHACCTTYPE_ID"));
      vo.setCommissionStakeholderId(resultSet.getLong("COMMISSION_STAKEHOLDER_ID"));
      vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
      vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
      vo.setName(resultSet.getString("NAME"));
      vo.setContactName(resultSet.getString("CONTACT_NAME"));
      vo.setDescription(resultSet.getString("DESCRIPTION"));
      vo.setVersionNo(resultSet.getInt("VERSION_NO"));
      vo.setComments(resultSet.getString("COMMENTS"));
      vo.setDisplayOnProductScreen(resultSet.getBoolean("DISPLAY_ON_PRODUCT_SCREEN"));
      vo.setFiler(resultSet.getBoolean("IS_FILER"));
      return vo;
   }
}
