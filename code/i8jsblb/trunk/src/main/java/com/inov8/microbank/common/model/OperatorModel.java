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
 * The OperatorModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="OperatorModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "OPERATOR_seq",sequenceName = "OPERATOR_seq", allocationSize=1)
@Table(name = "OPERATOR")
public class OperatorModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 1990979845970083759L;
private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<CommissionStakeholderModel> operatorIdCommissionStakeholderModelList = new ArrayList<CommissionStakeholderModel>();
   private Collection<ConcernPartnerModel> operatorIdConcernPartnerModelList = new ArrayList<ConcernPartnerModel>();
   private Collection<OperatorBankInfoModel> operatorIdOperatorBankInfoModelList = new ArrayList<OperatorBankInfoModel>();
   private Collection<OperatorUserModel> operatorIdOperatorUserModelList = new ArrayList<OperatorUserModel>();
   private Collection<PartnerModel> operatorIdPartnerModelList = new ArrayList<PartnerModel>();
   private Collection<TransactionModel> operatorIdTransactionModelList = new ArrayList<TransactionModel>();

   private Long operatorId;
   private String name;
   private String contactName;
   private String address1;
   private String address2;
   private String city;
   private String state;
   private String country;
   private String zip;
   private String email;
   private String phoneNo;
   private String fax;
   private Double balance;
   private String description;
   private Date createdOn;
   private Date updatedOn;
   private String comments;
   private Integer versionNo;
   private String imagePath;
   private String key;
   private String aes_key;
   
   /**
    * Default constructor.
    */
   public OperatorModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getOperatorId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setOperatorId(primaryKey);
    }

   /**
    * Returns the value of the <code>operatorId</code> property.
    *
    */
      @Column(name = "OPERATOR_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OPERATOR_seq")
   public Long getOperatorId() {
      return operatorId;
   }

   /**
    * Sets the value of the <code>operatorId</code> property.
    *
    * @param operatorId the value for the <code>operatorId</code> property
    *    
		    */

   public void setOperatorId(Long operatorId) {
      this.operatorId = operatorId;
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
    * Returns the value of the <code>balance</code> property.
    *
    */
      @Column(name = "BALANCE"  )
   public Double getBalance() {
      return balance;
   }

   /**
    * Sets the value of the <code>balance</code> property.
    *
    * @param balance the value for the <code>balance</code> property
    *    
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
    * Returns the value of the <code>key</code> property.
    *
    */
      @Column(name = "KEY"  , length=4000 )
   public String getKey() {
      return key;
   }

   /**
    * Sets the value of the <code>key</code> property.
    *
    * @param key the value for the <code>key</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="4000"
    */

   public void setKey(String key) {
      this.key = key;
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
    * Add the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be added.
    */
    
   public void addOperatorIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      commissionStakeholderModel.setRelationOperatorIdOperatorModel(this);
      operatorIdCommissionStakeholderModelList.add(commissionStakeholderModel);
   }
   
   /**
    * Remove the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be removed.
    */
   
   public void removeOperatorIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {      
      commissionStakeholderModel.setRelationOperatorIdOperatorModel(null);
      operatorIdCommissionStakeholderModelList.remove(commissionStakeholderModel);      
   }

   /**
    * Get a list of related CommissionStakeholderModel objects of the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @return Collection of CommissionStakeholderModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationOperatorIdOperatorModel")
   @JoinColumn(name = "OPERATOR_ID")
   public Collection<CommissionStakeholderModel> getOperatorIdCommissionStakeholderModelList() throws Exception {
   		return operatorIdCommissionStakeholderModelList;
   }


   /**
    * Set a list of CommissionStakeholderModel related objects to the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @param commissionStakeholderModelList the list of related objects.
    */
    public void setOperatorIdCommissionStakeholderModelList(Collection<CommissionStakeholderModel> commissionStakeholderModelList) throws Exception {
		this.operatorIdCommissionStakeholderModelList = commissionStakeholderModelList;
   }


   /**
    * Add the related ConcernPartnerModel to this one-to-many relation.
    *
    * @param concernPartnerModel object to be added.
    */
    
   public void addOperatorIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      concernPartnerModel.setRelationOperatorIdOperatorModel(this);
      operatorIdConcernPartnerModelList.add(concernPartnerModel);
   }
   
   /**
    * Remove the related ConcernPartnerModel to this one-to-many relation.
    *
    * @param concernPartnerModel object to be removed.
    */
   
   public void removeOperatorIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {      
      concernPartnerModel.setRelationOperatorIdOperatorModel(null);
      operatorIdConcernPartnerModelList.remove(concernPartnerModel);      
   }

   /**
    * Get a list of related ConcernPartnerModel objects of the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @return Collection of ConcernPartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationOperatorIdOperatorModel")
   @JoinColumn(name = "OPERATOR_ID")
   public Collection<ConcernPartnerModel> getOperatorIdConcernPartnerModelList() throws Exception {
   		return operatorIdConcernPartnerModelList;
   }


   /**
    * Set a list of ConcernPartnerModel related objects to the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @param concernPartnerModelList the list of related objects.
    */
    public void setOperatorIdConcernPartnerModelList(Collection<ConcernPartnerModel> concernPartnerModelList) throws Exception {
		this.operatorIdConcernPartnerModelList = concernPartnerModelList;
   }


   /**
    * Add the related OperatorBankInfoModel to this one-to-many relation.
    *
    * @param operatorBankInfoModel object to be added.
    */
    
   public void addOperatorIdOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel) {
      operatorBankInfoModel.setRelationOperatorIdOperatorModel(this);
      operatorIdOperatorBankInfoModelList.add(operatorBankInfoModel);
   }
   
   /**
    * Remove the related OperatorBankInfoModel to this one-to-many relation.
    *
    * @param operatorBankInfoModel object to be removed.
    */
   
   public void removeOperatorIdOperatorBankInfoModel(OperatorBankInfoModel operatorBankInfoModel) {      
      operatorBankInfoModel.setRelationOperatorIdOperatorModel(null);
      operatorIdOperatorBankInfoModelList.remove(operatorBankInfoModel);      
   }

   /**
    * Get a list of related OperatorBankInfoModel objects of the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @return Collection of OperatorBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationOperatorIdOperatorModel")
   @JoinColumn(name = "OPERATOR_ID")
   public Collection<OperatorBankInfoModel> getOperatorIdOperatorBankInfoModelList() throws Exception {
   		return operatorIdOperatorBankInfoModelList;
   }


   /**
    * Set a list of OperatorBankInfoModel related objects to the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @param operatorBankInfoModelList the list of related objects.
    */
    public void setOperatorIdOperatorBankInfoModelList(Collection<OperatorBankInfoModel> operatorBankInfoModelList) throws Exception {
		this.operatorIdOperatorBankInfoModelList = operatorBankInfoModelList;
   }


   /**
    * Add the related OperatorUserModel to this one-to-many relation.
    *
    * @param operatorUserModel object to be added.
    */
    
   public void addOperatorIdOperatorUserModel(OperatorUserModel operatorUserModel) {
      operatorUserModel.setRelationOperatorIdOperatorModel(this);
      operatorIdOperatorUserModelList.add(operatorUserModel);
   }
   
   /**
    * Remove the related OperatorUserModel to this one-to-many relation.
    *
    * @param operatorUserModel object to be removed.
    */
   
   public void removeOperatorIdOperatorUserModel(OperatorUserModel operatorUserModel) {      
      operatorUserModel.setRelationOperatorIdOperatorModel(null);
      operatorIdOperatorUserModelList.remove(operatorUserModel);      
   }

   /**
    * Get a list of related OperatorUserModel objects of the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @return Collection of OperatorUserModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationOperatorIdOperatorModel")
   @JoinColumn(name = "OPERATOR_ID")
   public Collection<OperatorUserModel> getOperatorIdOperatorUserModelList() throws Exception {
   		return operatorIdOperatorUserModelList;
   }


   /**
    * Set a list of OperatorUserModel related objects to the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @param operatorUserModelList the list of related objects.
    */
    public void setOperatorIdOperatorUserModelList(Collection<OperatorUserModel> operatorUserModelList) throws Exception {
		this.operatorIdOperatorUserModelList = operatorUserModelList;
   }


   /**
    * Add the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be added.
    */
    
   public void addOperatorIdPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationOperatorIdOperatorModel(this);
      operatorIdPartnerModelList.add(partnerModel);
   }
   
   /**
    * Remove the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be removed.
    */
   
   public void removeOperatorIdPartnerModel(PartnerModel partnerModel) {      
      partnerModel.setRelationOperatorIdOperatorModel(null);
      operatorIdPartnerModelList.remove(partnerModel);      
   }

   /**
    * Get a list of related PartnerModel objects of the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @return Collection of PartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationOperatorIdOperatorModel")
   @JoinColumn(name = "OPERATOR_ID")
   public Collection<PartnerModel> getOperatorIdPartnerModelList() throws Exception {
   		return operatorIdPartnerModelList;
   }


   /**
    * Set a list of PartnerModel related objects to the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @param partnerModelList the list of related objects.
    */
    public void setOperatorIdPartnerModelList(Collection<PartnerModel> partnerModelList) throws Exception {
		this.operatorIdPartnerModelList = partnerModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addOperatorIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationOperatorIdOperatorModel(this);
      operatorIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeOperatorIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationOperatorIdOperatorModel(null);
      operatorIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationOperatorIdOperatorModel")
   @JoinColumn(name = "OPERATOR_ID")
   public Collection<TransactionModel> getOperatorIdTransactionModelList() throws Exception {
   		return operatorIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the OperatorModel object.
    * These objects are in a bidirectional one-to-many relation by the OperatorId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setOperatorIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.operatorIdTransactionModelList = transactionModelList;
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
        checkBox += "_"+ getOperatorId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&operatorId=" + getOperatorId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "operatorId";
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
    @Column(name = "AES_KEY"  , length=1000 )
    public String getAESKey() {
       return aes_key;
    }
    
    public void setAESKey(String aes_key) {
       this.aes_key = aes_key;
    }          
}
