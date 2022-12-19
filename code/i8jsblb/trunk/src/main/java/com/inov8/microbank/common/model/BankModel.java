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
 * The BankModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="BankModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BANK_seq",sequenceName = "BANK_seq", allocationSize=1)
@Table(name = "BANK")
public class BankModel extends BasePersistableModel implements Serializable {
  

   private VeriflyModel veriflyIdVeriflyModel;
   private FinancialIntegrationModel financialIntegrationIdFinancialIntegrationModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<BankUserModel> bankIdBankUserModelList = new ArrayList<BankUserModel>();
   private Collection<CommissionStakeholderModel> bankIdCommissionStakeholderModelList = new ArrayList<CommissionStakeholderModel>();
   private Collection<ConcernPartnerModel> bankIdConcernPartnerModelList = new ArrayList<ConcernPartnerModel>();
   private Collection<OperatorBankInfoModel> bankIdOperatorBankInfoModelList = new ArrayList<OperatorBankInfoModel>();
   private Collection<PartnerModel> bankIdPartnerModelList = new ArrayList<PartnerModel>();
   private Collection<SmartMoneyAccountModel> bankIdSmartMoneyAccountModelList = new ArrayList<SmartMoneyAccountModel>();
   private Collection<StakeholderBankInfoModel> bankIdStakeholderBankInfoModelList = new ArrayList<StakeholderBankInfoModel>();
   private Collection<SupplierBankInfoModel> bankIdSupplierBankInfoModelList = new ArrayList<SupplierBankInfoModel>();
   private Collection<SwitchFinderModel> bankIdSwitchFinderModelList = new ArrayList<SwitchFinderModel>();
   private Collection<TransactionModel> processingBankIdTransactionModelList = new ArrayList<TransactionModel>();

   private Long bankId;
   private String name;
   private String contactName;
   private String address1;
   private String address2;
   private String city;
   private String state;
   private String country;
   private String zip;
   private String phoneNo;
   private String fax;
   private String email;
   private String description;
   private Boolean active;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private String comments;
   private String imagePath;

   /**
    * Default constructor.
    */
   public BankModel() {
   }   
   
   public BankModel(Long bankId) {
	   this.bankId = bankId;
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getBankId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setBankId(primaryKey);
    }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
      @Column(name = "BANK_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BANK_seq")
   public Long getBankId() {
      return bankId;
   }

   /**
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
    *    
		    */

   public void setBankId(Long bankId) {
      this.bankId = bankId;
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
    * Returns the value of the <code>address1</code> property.
    *
    */
      @Column(name = "ADDRESS1" , nullable = false , length=250 )
   public String getAddress1() {
      return address1;
   }

   /**
    * Sets the value of the <code>address1</code> property.
    *
    * @param address1 the value for the <code>address1</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress1(String address1) {
      this.address1 = address1;
   }

   /**
    * Returns the value of the <code>address2</code> property.
    *
    */
      @Column(name = "ADDRESS2"  , length=250 )
   public String getAddress2() {
      return address2;
   }

   /**
    * Sets the value of the <code>address2</code> property.
    *
    * @param address2 the value for the <code>address2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress2(String address2) {
      this.address2 = address2;
   }

   /**
    * Returns the value of the <code>city</code> property.
    *
    */
      @Column(name = "CITY"  , length=50 )
   public String getCity() {
      return city;
   }

   /**
    * Sets the value of the <code>city</code> property.
    *
    * @param city the value for the <code>city</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCity(String city) {
      this.city = city;
   }

   /**
    * Returns the value of the <code>state</code> property.
    *
    */
      @Column(name = "STATE"  , length=50 )
   public String getState() {
      return state;
   }

   /**
    * Sets the value of the <code>state</code> property.
    *
    * @param state the value for the <code>state</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setState(String state) {
      this.state = state;
   }

   /**
    * Returns the value of the <code>country</code> property.
    *
    */
      @Column(name = "COUNTRY"  , length=50 )
   public String getCountry() {
      return country;
   }

   /**
    * Sets the value of the <code>country</code> property.
    *
    * @param country the value for the <code>country</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCountry(String country) {
      this.country = country;
   }

   /**
    * Returns the value of the <code>zip</code> property.
    *
    */
      @Column(name = "ZIP"  , length=50 )
   public String getZip() {
      return zip;
   }

   /**
    * Sets the value of the <code>zip</code> property.
    *
    * @param zip the value for the <code>zip</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setZip(String zip) {
      this.zip = zip;
   }

   /**
    * Returns the value of the <code>phoneNo</code> property.
    *
    */
      @Column(name = "PHONE_NO"  , length=50 )
   public String getPhoneNo() {
      return phoneNo;
   }

   /**
    * Sets the value of the <code>phoneNo</code> property.
    *
    * @param phoneNo the value for the <code>phoneNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPhoneNo(String phoneNo) {
      this.phoneNo = phoneNo;
   }

   /**
    * Returns the value of the <code>fax</code> property.
    *
    */
      @Column(name = "FAX"  , length=50 )
   public String getFax() {
      return fax;
   }

   /**
    * Sets the value of the <code>fax</code> property.
    *
    * @param fax the value for the <code>fax</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFax(String fax) {
      this.fax = fax;
   }

   /**
    * Returns the value of the <code>email</code> property.
    *
    */
      @Column(name = "EMAIL"  , length=50 )
   public String getEmail() {
      return email;
   }

   /**
    * Sets the value of the <code>email</code> property.
    *
    * @param email the value for the <code>email</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setEmail(String email) {
      this.email = email;
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
    * Returns the value of the <code>imagePath</code> property.
    *
    */
      @Column(name = "IMAGE_PATH"  , length=250 )
   public String getImagePath() {
      return imagePath;
   }

   /**
    * Sets the value of the <code>imagePath</code> property.
    *
    * @param imagePath the value for the <code>imagePath</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setImagePath(String imagePath) {
      this.imagePath = imagePath;
   }

   /**
    * Returns the value of the <code>veriflyIdVeriflyModel</code> relation property.
    *
    * @return the value of the <code>veriflyIdVeriflyModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "VERIFLY_ID")    
   public VeriflyModel getRelationVeriflyIdVeriflyModel(){
      return veriflyIdVeriflyModel;
   }
    
   /**
    * Returns the value of the <code>veriflyIdVeriflyModel</code> relation property.
    *
    * @return the value of the <code>veriflyIdVeriflyModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public VeriflyModel getVeriflyIdVeriflyModel(){
      return getRelationVeriflyIdVeriflyModel();
   }

   /**
    * Sets the value of the <code>veriflyIdVeriflyModel</code> relation property.
    *
    * @param veriflyModel a value for <code>veriflyIdVeriflyModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationVeriflyIdVeriflyModel(VeriflyModel veriflyModel) {
      this.veriflyIdVeriflyModel = veriflyModel;
   }
   
   /**
    * Sets the value of the <code>veriflyIdVeriflyModel</code> relation property.
    *
    * @param veriflyModel a value for <code>veriflyIdVeriflyModel</code>.
    */
   @javax.persistence.Transient
   public void setVeriflyIdVeriflyModel(VeriflyModel veriflyModel) {
      if(null != veriflyModel)
      {
      	setRelationVeriflyIdVeriflyModel((VeriflyModel)veriflyModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>financialIntegrationIdFinancialIntegrationModel</code> relation property.
    *
    * @return the value of the <code>financialIntegrationIdFinancialIntegrationModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
   @JoinColumn(name = "FINANCIAL_INTEGRATION_ID")    
   public FinancialIntegrationModel getRelationFinancialIntegrationIdFinancialIntegrationModel(){
      return financialIntegrationIdFinancialIntegrationModel;
   }
    
   /**
    * Returns the value of the <code>financialIntegrationIdFinancialIntegrationModel</code> relation property.
    *
    * @return the value of the <code>financialIntegrationIdFinancialIntegrationModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public FinancialIntegrationModel getFinancialIntegrationIdFinancialIntegrationModel(){
      return getRelationFinancialIntegrationIdFinancialIntegrationModel();
   }

   /**
    * Sets the value of the <code>financialIntegrationIdFinancialIntegrationModel</code> relation property.
    *
    * @param financialIntegrationModel a value for <code>financialIntegrationIdFinancialIntegrationModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationFinancialIntegrationIdFinancialIntegrationModel(FinancialIntegrationModel financialIntegrationModel) {
      this.financialIntegrationIdFinancialIntegrationModel = financialIntegrationModel;
   }
   
   /**
    * Sets the value of the <code>financialIntegrationIdFinancialIntegrationModel</code> relation property.
    *
    * @param financialIntegrationModel a value for <code>financialIntegrationIdFinancialIntegrationModel</code>.
    */
   @javax.persistence.Transient
   public void setFinancialIntegrationIdFinancialIntegrationModel(FinancialIntegrationModel financialIntegrationModel) {
      if(null != financialIntegrationModel)
      {
      	setRelationFinancialIntegrationIdFinancialIntegrationModel((FinancialIntegrationModel)financialIntegrationModel.clone());
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
    * Add the related BankUserModel to this one-to-many relation.
    *
    * @param bankUserModel object to be added.
    */
    
   public void addBankIdBankUserModel(BankUserModel bankUserModel) {
      bankUserModel.setRelationBankIdBankModel(this);
      bankIdBankUserModelList.add(bankUserModel);
   }
   
   /**
    * Remove the related BankUserModel to this one-to-many relation.
    *
    * @param bankUserModel object to be removed.
    */
   
   public void removeBankIdBankUserModel(BankUserModel bankUserModel) {      
      bankUserModel.setRelationBankIdBankModel(null);
      bankIdBankUserModelList.remove(bankUserModel);      
   }

   /**
    * Get a list of related BankUserModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of BankUserModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<BankUserModel> getBankIdBankUserModelList() throws Exception {
   		return bankIdBankUserModelList;
   }


   /**
    * Set a list of BankUserModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param bankUserModelList the list of related objects.
    */
    public void setBankIdBankUserModelList(Collection<BankUserModel> bankUserModelList) throws Exception {
		this.bankIdBankUserModelList = bankUserModelList;
   }


   /**
    * Add the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be added.
    */
    
   public void addBankIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      commissionStakeholderModel.setRelationBankIdBankModel(this);
      bankIdCommissionStakeholderModelList.add(commissionStakeholderModel);
   }
   
   /**
    * Remove the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be removed.
    */
   
   public void removeBankIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {      
      commissionStakeholderModel.setRelationBankIdBankModel(null);
      bankIdCommissionStakeholderModelList.remove(commissionStakeholderModel);      
   }

   /**
    * Get a list of related CommissionStakeholderModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of CommissionStakeholderModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<CommissionStakeholderModel> getBankIdCommissionStakeholderModelList() throws Exception {
   		return bankIdCommissionStakeholderModelList;
   }


   /**
    * Set a list of CommissionStakeholderModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param commissionStakeholderModelList the list of related objects.
    */
    public void setBankIdCommissionStakeholderModelList(Collection<CommissionStakeholderModel> commissionStakeholderModelList) throws Exception {
		this.bankIdCommissionStakeholderModelList = commissionStakeholderModelList;
   }


   /**
    * Add the related ConcernPartnerModel to this one-to-many relation.
    *
    * @param concernPartnerModel object to be added.
    */
    
   public void addBankIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      concernPartnerModel.setRelationBankIdBankModel(this);
      bankIdConcernPartnerModelList.add(concernPartnerModel);
   }
   
   /**
    * Remove the related ConcernPartnerModel to this one-to-many relation.
    *
    * @param concernPartnerModel object to be removed.
    */
   
   public void removeBankIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {      
      concernPartnerModel.setRelationBankIdBankModel(null);
      bankIdConcernPartnerModelList.remove(concernPartnerModel);      
   }

   /**
    * Get a list of related ConcernPartnerModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of ConcernPartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<ConcernPartnerModel> getBankIdConcernPartnerModelList() throws Exception {
   		return bankIdConcernPartnerModelList;
   }


   /**
    * Set a list of ConcernPartnerModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param concernPartnerModelList the list of related objects.
    */
    public void setBankIdConcernPartnerModelList(Collection<ConcernPartnerModel> concernPartnerModelList) throws Exception {
		this.bankIdConcernPartnerModelList = concernPartnerModelList;
   }


   /**
    * Add the related OperatorBankInfoModel to this one-to-many relation.
    *
    * @param operatorBankInfoModel object to be added.
    */
    
   public void addBankIdOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel) {
      operatorBankInfoModel.setRelationBankIdBankModel(this);
      bankIdOperatorBankInfoModelList.add(operatorBankInfoModel);
   }
   
   /**
    * Remove the related OperatorBankInfoModel to this one-to-many relation.
    *
    * @param operatorBankInfoModel object to be removed.
    */
   
   public void removeBankIdOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel) {      
      operatorBankInfoModel.setRelationBankIdBankModel(null);
      bankIdOperatorBankInfoModelList.remove(operatorBankInfoModel);      
   }

   /**
    * Get a list of related OperatorBankInfoModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of OperatorBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<OperatorBankInfoModel> getBankIdOperatorBankInfoModelList() throws Exception {
   		return bankIdOperatorBankInfoModelList;
   }


   /**
    * Set a list of OperatorBankInfoModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param operatorBankInfoModelList the list of related objects.
    */
    public void setBankIdOperatorBankInfoModelList(Collection<OperatorBankInfoModel> operatorBankInfoModelList) throws Exception {
		this.bankIdOperatorBankInfoModelList = operatorBankInfoModelList;
   }


   /**
    * Add the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be added.
    */
    
   public void addBankIdPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationBankIdBankModel(this);
      bankIdPartnerModelList.add(partnerModel);
   }
   
   /**
    * Remove the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be removed.
    */
   
   public void removeBankIdPartnerModel(PartnerModel partnerModel) {      
      partnerModel.setRelationBankIdBankModel(null);
      bankIdPartnerModelList.remove(partnerModel);      
   }

   /**
    * Get a list of related PartnerModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of PartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<PartnerModel> getBankIdPartnerModelList() throws Exception {
   		return bankIdPartnerModelList;
   }


   /**
    * Set a list of PartnerModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param partnerModelList the list of related objects.
    */
    public void setBankIdPartnerModelList(Collection<PartnerModel> partnerModelList) throws Exception {
		this.bankIdPartnerModelList = partnerModelList;
   }


   /**
    * Add the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be added.
    */
    
   public void addBankIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {
      smartMoneyAccountModel.setRelationBankIdBankModel(this);
      bankIdSmartMoneyAccountModelList.add(smartMoneyAccountModel);
   }
   
   /**
    * Remove the related SmartMoneyAccountModel to this one-to-many relation.
    *
    * @param smartMoneyAccountModel object to be removed.
    */
   
   public void removeBankIdSmartMoneyAccountModel(SmartMoneyAccountModel smartMoneyAccountModel) {      
      smartMoneyAccountModel.setRelationBankIdBankModel(null);
      bankIdSmartMoneyAccountModelList.remove(smartMoneyAccountModel);      
   }

   /**
    * Get a list of related SmartMoneyAccountModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of SmartMoneyAccountModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<SmartMoneyAccountModel> getBankIdSmartMoneyAccountModelList() throws Exception {
   		return bankIdSmartMoneyAccountModelList;
   }


   /**
    * Set a list of SmartMoneyAccountModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param smartMoneyAccountModelList the list of related objects.
    */
    public void setBankIdSmartMoneyAccountModelList(Collection<SmartMoneyAccountModel> smartMoneyAccountModelList) throws Exception {
		this.bankIdSmartMoneyAccountModelList = smartMoneyAccountModelList;
   }


   /**
    * Add the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be added.
    */
    
   public void addBankIdStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
      stakeholderBankInfoModel.setRelationBankIdBankModel(this);
      bankIdStakeholderBankInfoModelList.add(stakeholderBankInfoModel);
   }
   
   /**
    * Remove the related StakeholderBankInfoModel to this one-to-many relation.
    *
    * @param stakeholderBankInfoModel object to be removed.
    */
   
   public void removeBankIdStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {      
      stakeholderBankInfoModel.setRelationBankIdBankModel(null);
      bankIdStakeholderBankInfoModelList.remove(stakeholderBankInfoModel);      
   }

   /**
    * Get a list of related StakeholderBankInfoModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of StakeholderBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<StakeholderBankInfoModel> getBankIdStakeholderBankInfoModelList() throws Exception {
   		return bankIdStakeholderBankInfoModelList;
   }


   /**
    * Set a list of StakeholderBankInfoModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param stakeholderBankInfoModelList the list of related objects.
    */
    public void setBankIdStakeholderBankInfoModelList(Collection<StakeholderBankInfoModel> stakeholderBankInfoModelList) throws Exception {
		this.bankIdStakeholderBankInfoModelList = stakeholderBankInfoModelList;
   }


   /**
    * Add the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be added.
    */
    
   public void addBankIdSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {
      supplierBankInfoModel.setRelationBankIdBankModel(this);
      bankIdSupplierBankInfoModelList.add(supplierBankInfoModel);
   }
   
   /**
    * Remove the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be removed.
    */
   
   public void removeBankIdSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {      
      supplierBankInfoModel.setRelationBankIdBankModel(null);
      bankIdSupplierBankInfoModelList.remove(supplierBankInfoModel);      
   }

   /**
    * Get a list of related SupplierBankInfoModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of SupplierBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<SupplierBankInfoModel> getBankIdSupplierBankInfoModelList() throws Exception {
   		return bankIdSupplierBankInfoModelList;
   }


   /**
    * Set a list of SupplierBankInfoModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param supplierBankInfoModelList the list of related objects.
    */
    public void setBankIdSupplierBankInfoModelList(Collection<SupplierBankInfoModel> supplierBankInfoModelList) throws Exception {
		this.bankIdSupplierBankInfoModelList = supplierBankInfoModelList;
   }


   /**
    * Add the related SwitchFinderModel to this one-to-many relation.
    *
    * @param switchFinderModel object to be added.
    */
    
   public void addBankIdSwitchFinderModel(SwitchFinderModel switchFinderModel) {
      switchFinderModel.setRelationBankIdBankModel(this);
      bankIdSwitchFinderModelList.add(switchFinderModel);
   }
   
   /**
    * Remove the related SwitchFinderModel to this one-to-many relation.
    *
    * @param switchFinderModel object to be removed.
    */
   
   public void removeBankIdSwitchFinderModel(SwitchFinderModel switchFinderModel) {      
      switchFinderModel.setRelationBankIdBankModel(null);
      bankIdSwitchFinderModelList.remove(switchFinderModel);      
   }

   /**
    * Get a list of related SwitchFinderModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @return Collection of SwitchFinderModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationBankIdBankModel")
   @JoinColumn(name = "BANK_ID")
   public Collection<SwitchFinderModel> getBankIdSwitchFinderModelList() throws Exception {
   		return bankIdSwitchFinderModelList;
   }


   /**
    * Set a list of SwitchFinderModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the BankId member.
    *
    * @param switchFinderModelList the list of related objects.
    */
    public void setBankIdSwitchFinderModelList(Collection<SwitchFinderModel> switchFinderModelList) throws Exception {
		this.bankIdSwitchFinderModelList = switchFinderModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addProcessingBankIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationProcessingBankIdBankModel(this);
      processingBankIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeProcessingBankIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationProcessingBankIdBankModel(null);
      processingBankIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the ProcessingBankId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProcessingBankIdBankModel")
   @JoinColumn(name = "PROCESSING_BANK_ID")
   public Collection<TransactionModel> getProcessingBankIdTransactionModelList() throws Exception {
   		return processingBankIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the BankModel object.
    * These objects are in a bidirectional one-to-many relation by the ProcessingBankId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setProcessingBankIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.processingBankIdTransactionModelList = transactionModelList;
   }



   /**
    * Returns the value of the <code>veriflyId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getVeriflyId() {
      if (veriflyIdVeriflyModel != null) {
         return veriflyIdVeriflyModel.getVeriflyId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>veriflyId</code> property.
    *
    * @param veriflyId the value for the <code>veriflyId</code> property
																																																	    */
   
   @javax.persistence.Transient
   public void setVeriflyId(Long veriflyId) {
      if(veriflyId == null)
      {      
      	veriflyIdVeriflyModel = null;
      }
      else
      {
        veriflyIdVeriflyModel = new VeriflyModel();
      	veriflyIdVeriflyModel.setVeriflyId(veriflyId);
      }      
   }

   /**
    * Returns the value of the <code>financialIntegrationId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getFinancialIntegrationId() {
      if (financialIntegrationIdFinancialIntegrationModel != null) {
         return financialIntegrationIdFinancialIntegrationModel.getFinancialIntegrationId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>financialIntegrationId</code> property.
    *
    * @param financialIntegrationId the value for the <code>financialIntegrationId</code> property
																																															    * @spring.validator type="required"
			    */
   
   @javax.persistence.Transient
   public void setFinancialIntegrationId(Long financialIntegrationId) {
      if(financialIntegrationId == null)
      {      
      	financialIntegrationIdFinancialIntegrationModel = null;
      }
      else
      {
        financialIntegrationIdFinancialIntegrationModel = new FinancialIntegrationModel();
      	financialIntegrationIdFinancialIntegrationModel.setFinancialIntegrationId(financialIntegrationId);
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
        checkBox += "_"+ getBankId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&bankId=" + getBankId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "bankId";
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
    	
    	associationModel.setClassName("VeriflyModel");
    	associationModel.setPropertyName("relationVeriflyIdVeriflyModel");   		
   		associationModel.setValue(getRelationVeriflyIdVeriflyModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("FinancialIntegrationModel");
    	associationModel.setPropertyName("relationFinancialIntegrationIdFinancialIntegrationModel");   		
   		associationModel.setValue(getRelationFinancialIntegrationIdFinancialIntegrationModel());
   		
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
