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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The PaymentModeModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PaymentModeModel"
 */
@XmlRootElement(name="paymentModeModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PAYMENT_MODE_seq",sequenceName = "PAYMENT_MODE_seq", allocationSize=1)
@Table(name = "PAYMENT_MODE")
public class PaymentModeModel extends BasePersistableModel implements Serializable {
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -6642340285253942569L;

   @XmlElement
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<CommissionRateModel> paymentModeIdCommissionRateModelList = new ArrayList<CommissionRateModel>();
   private Collection<CommissionTransactionModel> paymentModeIdCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();
   private Collection<OperatorBankInfoModel> paymentModeIdOperatorBankInfoModelList = new ArrayList<OperatorBankInfoModel>();
   private Collection<ShipmentModel> paymentModeIdShipmentModelList = new ArrayList<ShipmentModel>();
   private Collection<SmartMoneyAccountModel> paymentModeIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
   private Collection<SupplierBankInfoModel> paymentModeIdSupplierBankInfoModelList = new ArrayList<SupplierBankInfoModel>();
   private Collection<SwitchFinderModel> paymentModeIdSwitchFinderModelList = new ArrayList<SwitchFinderModel>();
   private Collection<TransactionModel> paymentModeIdTransactionModelList = new ArrayList<TransactionModel>();
   private Collection<AccountTypeModel> paymentModeIdAccountTypeModelList = new ArrayList<AccountTypeModel>();

   @XmlElement
   private Long paymentModeId;
   @XmlElement
   private String name;
   @XmlElement
   private String description;
   @XmlElement
   private String comments;
   @XmlElement
   private Date createdOn;
   @XmlElement
   private Date updatedOn;
   @XmlElement
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public PaymentModeModel() {
   }   

   public PaymentModeModel(Long paymentModeId) {
	   setPrimaryKey(paymentModeId);
   } 
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPaymentModeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPaymentModeId(primaryKey);
    }

   /**
    * Returns the value of the <code>paymentModeId</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PAYMENT_MODE_seq")
   public Long getPaymentModeId() {
      return paymentModeId;
   }

   /**
    * Sets the value of the <code>paymentModeId</code> property.
    *
    * @param paymentModeId the value for the <code>paymentModeId</code> property
    *    
		    */

   public void setPaymentModeId(Long paymentModeId) {
      this.paymentModeId = paymentModeId;
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
    
   public void addPaymentModeIdCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdCommissionRateModelList.add(commissionRateModel);
   }
   
   /**
    * Remove the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be removed.
    */
   
   public void removePaymentModeIdCommissionRateModel(CommissionRateModel commissionRateModel) {      
      commissionRateModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdCommissionRateModelList.remove(commissionRateModel);      
   }

   /**
    * Get a list of related CommissionRateModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of CommissionRateModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<CommissionRateModel> getPaymentModeIdCommissionRateModelList() throws Exception {
   		return paymentModeIdCommissionRateModelList;
   }


   /**
    * Set a list of CommissionRateModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param commissionRateModelList the list of related objects.
    */
    public void setPaymentModeIdCommissionRateModelList(Collection<CommissionRateModel> commissionRateModelList) throws Exception {
		this.paymentModeIdCommissionRateModelList = commissionRateModelList;
   }


   /**
    * Add the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be added.
    */
    
   public void addPaymentModeIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {
      commissionTransactionModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdCommissionTransactionModelList.add(commissionTransactionModel);
   }
   
   /**
    * Remove the related CommissionTransactionModel to this one-to-many relation.
    *
    * @param commissionTransactionModel object to be removed.
    */
   
   public void removePaymentModeIdCommissionTransactionModel(CommissionTransactionModel commissionTransactionModel) {      
      commissionTransactionModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdCommissionTransactionModelList.remove(commissionTransactionModel);      
   }

   /**
    * Get a list of related CommissionTransactionModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of CommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<CommissionTransactionModel> getPaymentModeIdCommissionTransactionModelList() throws Exception {
   		return paymentModeIdCommissionTransactionModelList;
   }


   /**
    * Set a list of CommissionTransactionModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param commissionTransactionModelList the list of related objects.
    */
    public void setPaymentModeIdCommissionTransactionModelList(Collection<CommissionTransactionModel> commissionTransactionModelList) throws Exception {
		this.paymentModeIdCommissionTransactionModelList = commissionTransactionModelList;
   }


   /**
    * Add the related OperatorBankInfoModel to this one-to-many relation.
    *
    * @param operatorBankInfoModel object to be added.
    */
    
   public void addPaymentModeIdOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel) {
      operatorBankInfoModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdOperatorBankInfoModelList.add(operatorBankInfoModel);
   }
   
   /**
    * Remove the related OperatorBankInfoModel to this one-to-many relation.
    *
    * @param operatorBankInfoModel object to be removed.
    */
   
   public void removePaymentModeIdOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel) {      
      operatorBankInfoModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdOperatorBankInfoModelList.remove(operatorBankInfoModel);      
   }

   /**
    * Get a list of related OperatorBankInfoModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of OperatorBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<OperatorBankInfoModel> getPaymentModeIdOperatorBankInfoModelList() throws Exception {
   		return paymentModeIdOperatorBankInfoModelList;
   }


   /**
    * Set a list of OperatorBankInfoModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param operatorBankInfoModelList the list of related objects.
    */
    public void setPaymentModeIdOperatorBankInfoModelList(Collection<OperatorBankInfoModel> operatorBankInfoModelList) throws Exception {
		this.paymentModeIdOperatorBankInfoModelList = operatorBankInfoModelList;
   }


   /**
    * Add the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be added.
    */
    
   public void addPaymentModeIdShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdShipmentModelList.add(shipmentModel);
   }
   
   /**
    * Remove the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be removed.
    */
   
   public void removePaymentModeIdShipmentModel(ShipmentModel shipmentModel) {      
      shipmentModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdShipmentModelList.remove(shipmentModel);      
   }

   /**
    * Get a list of related ShipmentModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of ShipmentModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<ShipmentModel> getPaymentModeIdShipmentModelList() throws Exception {
   		return paymentModeIdShipmentModelList;
   }


   /**
    * Set a list of ShipmentModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param shipmentModelList the list of related objects.
    */
    public void setPaymentModeIdShipmentModelList(Collection<ShipmentModel> shipmentModelList) throws Exception {
		this.paymentModeIdShipmentModelList = shipmentModelList;
   }


   /**
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */
    
   public void addPaymentModeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }
   
   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */
   
   public void removePaymentModeIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {      
      smartMoneyAccountModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);      
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<SmartMoneyAccountModel> getPaymentModeIdSmartMoneyAccountModelList() throws Exception {
   		return paymentModeIdSmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
    public void setPaymentModeIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
		this.paymentModeIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
   }


   /**
    * Add the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be added.
    */
    
   public void addPaymentModeIdSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {
      supplierBankInfoModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdSupplierBankInfoModelList.add(supplierBankInfoModel);
   }
   
   /**
    * Remove the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be removed.
    */
   
   public void removePaymentModeIdSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {      
      supplierBankInfoModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdSupplierBankInfoModelList.remove(supplierBankInfoModel);      
   }

   /**
    * Get a list of related SupplierBankInfoModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of SupplierBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<SupplierBankInfoModel> getPaymentModeIdSupplierBankInfoModelList() throws Exception {
   		return paymentModeIdSupplierBankInfoModelList;
   }


   /**
    * Set a list of SupplierBankInfoModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param supplierBankInfoModelList the list of related objects.
    */
    public void setPaymentModeIdSupplierBankInfoModelList(Collection<SupplierBankInfoModel> supplierBankInfoModelList) throws Exception {
		this.paymentModeIdSupplierBankInfoModelList = supplierBankInfoModelList;
   }


   /**
    * Add the related SwitchFinderModel to this one-to-many relation.
    *
    * @param switchFinderModel object to be added.
    */
    
   public void addPaymentModeIdSwitchFinderModel(SwitchFinderModel switchFinderModel) {
      switchFinderModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdSwitchFinderModelList.add(switchFinderModel);
   }
   
   /**
    * Remove the related SwitchFinderModel to this one-to-many relation.
    *
    * @param switchFinderModel object to be removed.
    */
   
   public void removePaymentModeIdSwitchFinderModel(SwitchFinderModel switchFinderModel) {      
      switchFinderModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdSwitchFinderModelList.remove(switchFinderModel);      
   }

   /**
    * Get a list of related SwitchFinderModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of SwitchFinderModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<SwitchFinderModel> getPaymentModeIdSwitchFinderModelList() throws Exception {
   		return paymentModeIdSwitchFinderModelList;
   }


   /**
    * Set a list of SwitchFinderModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param switchFinderModelList the list of related objects.
    */
    public void setPaymentModeIdSwitchFinderModelList(Collection<SwitchFinderModel> switchFinderModelList) throws Exception {
		this.paymentModeIdSwitchFinderModelList = switchFinderModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addPaymentModeIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removePaymentModeIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<TransactionModel> getPaymentModeIdTransactionModelList() throws Exception {
   		return paymentModeIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setPaymentModeIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.paymentModeIdTransactionModelList = transactionModelList;
   }


   /**
    * Add the related AccountTypeModel to this one-to-many relation.
    *
    * @param accountTypeModel object to be added.
    */
    
   public void addPaymentModeIdAccountTypeModel(AccountTypeModel accountTypeModel) {
      accountTypeModel.setRelationPaymentModeIdPaymentModeModel(this);
      paymentModeIdAccountTypeModelList.add(accountTypeModel);
   }
   
   /**
    * Remove the related AccountTypeModel to this one-to-many relation.
    *
    * @param accountTypeModel object to be removed.
    */
   
   public void removePaymentModeIdAccountTypeModel(AccountTypeModel accountTypeModel) {      
      accountTypeModel.setRelationPaymentModeIdPaymentModeModel(null);
      paymentModeIdAccountTypeModelList.remove(accountTypeModel);      
   }

   /**
    * Get a list of related AccountTypeModel objects of the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @return Collection of AccountTypeModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPaymentModeIdPaymentModeModel")
   @JoinColumn(name = "PAYMENT_MODE_ID")
   public Collection<AccountTypeModel> getPaymentModeIdAccountTypeModelList() throws Exception {
   		return paymentModeIdAccountTypeModelList;
   }


   /**
    * Set a list of AccountTypeModel related objects to the PaymentModeModel object.
    * These objects are in a bidirectional one-to-many relation by the PaymentModeId member.
    *
    * @param accountTypeModelList the list of related objects.
    */
    public void setPaymentModeIdAccountTypeModelList(Collection<AccountTypeModel> accountTypeModelList) throws Exception {
		this.paymentModeIdAccountTypeModelList = accountTypeModelList;
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
        checkBox += "_"+ getPaymentModeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&paymentModeId=" + getPaymentModeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "paymentModeId";
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
