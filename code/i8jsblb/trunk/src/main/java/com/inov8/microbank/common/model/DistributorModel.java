package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.vo.agenthierarchy.DistributorVO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;



/**
 * The DistributorModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistributorModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DISTRIBUTOR_seq",sequenceName = "DISTRIBUTOR_seq", allocationSize=1)
@Table(name = "DISTRIBUTOR")
public class DistributorModel extends BasePersistableModel implements Serializable {
  

   /**
	 * 
	 */
   private static final long serialVersionUID = -3571110109965363149L;
   private AreaModel areaIdAreaModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private MnoModel mnoModel;
   private BankModel bankModel;

   

   private Collection<CommissionStakeholderModel> distributorIdCommissionStakeholderModelList = new ArrayList<CommissionStakeholderModel>();
   private Collection<DistributorContactModel> distributorIdDistributorContactModelList = new ArrayList<DistributorContactModel>();
/*   private Collection<DistributorLevelModel> distributorIdDistributorLevelModelList = new ArrayList<DistributorLevelModel>();*/
   private Collection<PartnerModel> distributerIdPartnerModelList = new ArrayList<PartnerModel>();
   private Collection<RetailerModel> distributorIdRetailerModelList = new ArrayList<RetailerModel>();
   private Collection<TransactionModel> distributorIdTransactionModelList = new ArrayList<TransactionModel>();

   private Long distributorId;
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
   private Boolean active;
   private Boolean national;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Integer index;
   transient private StakeholderBankInfoModel stakeholderBankInfoModel;
   /**
    * Default constructor.
    */
   public DistributorModel() {
   }   

   public DistributorModel(Long distributorId) {
	   super();
	   this.distributorId = distributorId;
   }

public DistributorVO giveValueObject()
   {
	   DistributorVO vo = new DistributorVO();
	   vo.setDistributorId(distributorId);
	   vo.setName(name);
	   vo.setContactName(contactName);
	   vo.setAddress1(address1);
	   vo.setAddress2(address2);
	   vo.setCity(city);
	   vo.setState(state);
	   vo.setCountry(country);
	   vo.setZip(zip);
	   vo.setPhoneNo(phoneNo);
	   vo.setFax(fax);
	   vo.setEmail(email);
	   vo.setActive(active);
	   vo.setNational(national);
	   vo.setDescription(description);
	   vo.setComments(comments);
	   if(mnoModel != null)
	   {
		   vo.setMnoId(mnoModel.getMnoId());
	   }
	   if(bankModel != null)
	   {
		   vo.setBankId(bankModel.getBankId());
	   }
	   vo.setCreatedOn(createdOn);
	   vo.setUpdatedOn(updatedOn);
	   vo.setCreatedByAppUserModel(createdByAppUserModel);
	   vo.setUpdatedByAppUserModel(updatedByAppUserModel);
	   vo.setVersionNo(versionNo);
	   return vo;
   }
   
   public void setNationalString (String set){
	   
   }
   @javax.persistence.Transient
   public String getNationalString(){
	   if (national == null || national == false){
		   return "No";
	   }else if (national == true){
		   return "Yes";
	   }
	   return null;
   }
   
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistributorId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistributorId(primaryKey);
    }

   /**
    * Returns the value of the <code>distributorId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DISTRIBUTOR_seq")
   public Long getDistributorId() {
      return distributorId;
   }

   /**
    * Sets the value of the <code>distributorId</code> property.
    *
    * @param distributorId the value for the <code>distributorId</code> property
    *    
		    */

   public void setDistributorId(Long distributorId) {
      this.distributorId = distributorId;
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

   
   @Column(name = "IS_NATIONAL" , nullable = false )
   public Boolean getNational() {
      return national;
   }


   public void setNational(Boolean national) {
      this.national = national;
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
    * Add the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be added.
    */
    
   public void addDistributorIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      commissionStakeholderModel.setRelationDistributorIdDistributorModel(this);
      distributorIdCommissionStakeholderModelList.add(commissionStakeholderModel);
   }
   
   /**
    * Remove the related CommissionStakeholderModel to this one-to-many relation.
    *
    * @param commissionStakeholderModel object to be removed.
    */
   
   public void removeDistributorIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {      
      commissionStakeholderModel.setRelationDistributorIdDistributorModel(null);
      distributorIdCommissionStakeholderModelList.remove(commissionStakeholderModel);      
   }

   /**
    * Get a list of related CommissionStakeholderModel objects of the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @return Collection of CommissionStakeholderModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorIdDistributorModel")
   @JoinColumn(name = "DISTRIBUTOR_ID")
   public Collection<CommissionStakeholderModel> getDistributorIdCommissionStakeholderModelList() throws Exception {
   		return distributorIdCommissionStakeholderModelList;
   }


   /**
    * Set a list of CommissionStakeholderModel related objects to the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @param commissionStakeholderModelList the list of related objects.
    */
    public void setDistributorIdCommissionStakeholderModelList(Collection<CommissionStakeholderModel> commissionStakeholderModelList) throws Exception {
		this.distributorIdCommissionStakeholderModelList = commissionStakeholderModelList;
   }


   /**
    * Add the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be added.
    */
    
   public void addDistributorIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationDistributorIdDistributorModel(this);
      distributorIdDistributorContactModelList.add(distributorContactModel);
   }
   
   /**
    * Remove the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be removed.
    */
   
   public void removeDistributorIdDistributorContactModel(DistributorContactModel distributorContactModel) {      
      distributorContactModel.setRelationDistributorIdDistributorModel(null);
      distributorIdDistributorContactModelList.remove(distributorContactModel);      
   }

   /**
    * Get a list of related DistributorContactModel objects of the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @return Collection of DistributorContactModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorIdDistributorModel")
   @JoinColumn(name = "DISTRIBUTOR_ID")
   public Collection<DistributorContactModel> getDistributorIdDistributorContactModelList() throws Exception {
   		return distributorIdDistributorContactModelList;
   }


   /**
    * Set a list of DistributorContactModel related objects to the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @param distributorContactModelList the list of related objects.
    */
    public void setDistributorIdDistributorContactModelList(Collection<DistributorContactModel> distributorContactModelList) throws Exception {
		this.distributorIdDistributorContactModelList = distributorContactModelList;
   }


   /**
    * Add the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be added.
    */
	// commented by Rashid Starts
   /*public void addDistributorIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      distributorLevelModel.setRelationDistributorIdDistributorModel(this);
      distributorIdDistributorLevelModelList.add(distributorLevelModel);
   }*/
    
   
   /**
    * Remove the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be removed.
    */
   
   /*public void removeDistributorIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {      
      distributorLevelModel.setRelationDistributorIdDistributorModel(null);
      distributorIdDistributorLevelModelList.remove(distributorLevelModel);      
   }*/
	// commented by Rashid Ends
   
   /**
    * Get a list of related DistributorLevelModel objects of the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @return Collection of DistributorLevelModel objects.
    *
    */
   
   /*@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorIdDistributorModel")
   @JoinColumn(name = "DISTRIBUTOR_ID")
   public Collection<DistributorLevelModel> getDistributorIdDistributorLevelModelList() throws Exception {
   		return distributorIdDistributorLevelModelList;
   }


   *//**
    * Set a list of DistributorLevelModel related objects to the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @param distributorLevelModelList the list of related objects.
    *//*
    public void setDistributorIdDistributorLevelModelList(Collection<DistributorLevelModel> distributorLevelModelList) throws Exception {
		this.distributorIdDistributorLevelModelList = distributorLevelModelList;
   }

*/
   /**
    * Add the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be added.
    */
    
   public void addDistributerIdPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationDistributerIdDistributorModel(this);
      distributerIdPartnerModelList.add(partnerModel);
   }
   
   /**
    * Remove the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be removed.
    */
   
   public void removeDistributerIdPartnerModel(PartnerModel partnerModel) {      
      partnerModel.setRelationDistributerIdDistributorModel(null);
      distributerIdPartnerModelList.remove(partnerModel);      
   }

   /**
    * Get a list of related PartnerModel objects of the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributerId member.
    *
    * @return Collection of PartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributerIdDistributorModel")
   @JoinColumn(name = "DISTRIBUTER_ID")
   public Collection<PartnerModel> getDistributerIdPartnerModelList() throws Exception {
   		return distributerIdPartnerModelList;
   }


   /**
    * Set a list of PartnerModel related objects to the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributerId member.
    *
    * @param partnerModelList the list of related objects.
    */
    public void setDistributerIdPartnerModelList(Collection<PartnerModel> partnerModelList) throws Exception {
		this.distributerIdPartnerModelList = partnerModelList;
   }


   /**
    * Add the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be added.
    */
    
   public void addDistributorIdRetailerModel(RetailerModel retailerModel) {
      retailerModel.setRelationDistributorIdDistributorModel(this);
      distributorIdRetailerModelList.add(retailerModel);
   }
   
   /**
    * Remove the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be removed.
    */
   
   public void removeDistributorIdRetailerModel(RetailerModel retailerModel) {      
      retailerModel.setRelationDistributorIdDistributorModel(null);
      distributorIdRetailerModelList.remove(retailerModel);      
   }

   /**
    * Get a list of related RetailerModel objects of the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @return Collection of RetailerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorIdDistributorModel")
   @JoinColumn(name = "DISTRIBUTOR_ID")
   public Collection<RetailerModel> getDistributorIdRetailerModelList() throws Exception {
   		return distributorIdRetailerModelList;
   }


   /**
    * Set a list of RetailerModel related objects to the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @param retailerModelList the list of related objects.
    */
    public void setDistributorIdRetailerModelList(Collection<RetailerModel> retailerModelList) throws Exception {
		this.distributorIdRetailerModelList = retailerModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addDistributorIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationDistributorIdDistributorModel(this);
      distributorIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeDistributorIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationDistributorIdDistributorModel(null);
      distributorIdTransactionModelList.remove(transactionModel);      
   }

   /**
    * Get a list of related TransactionModel objects of the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorIdDistributorModel")
   @JoinColumn(name = "DISTRIBUTOR_ID")
   public Collection<TransactionModel> getDistributorIdTransactionModelList() throws Exception {
   		return distributorIdTransactionModelList;
   }


   /**
    * Set a list of TransactionModel related objects to the DistributorModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setDistributorIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.distributorIdTransactionModelList = transactionModelList;
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
        checkBox += "_"+ getDistributorId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&distributorId=" + getDistributorId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "distributorId";
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
    	
    	associationModel.setClassName("AreaModel");
    	associationModel.setPropertyName("relationAreaIdAreaModel");   		
   		associationModel.setValue(getRelationAreaIdAreaModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);

       associationModel.setClassName("MnoModel");
       associationModel.setPropertyName("mnoModel");
       associationModel.setValue(getMnoModel());

       associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_OP_ID") 
    public MnoModel getMnoModel() {
    	return mnoModel;
    }
    
    public void setMnoModel(MnoModel mnoModel) {
    	this.mnoModel = mnoModel;
    }
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "BANK_ID") 
    public BankModel getBankModel() {
    	return bankModel;
    }
    
    public void setBankModel(BankModel bankModel) {
    	this.bankModel = bankModel;
    }
    @javax.persistence.Transient
    public Long getMnoId(){    	   	
    	return mnoModel!=null?mnoModel.getMnoId():null;
    }
    @javax.persistence.Transient
    public Long getbankId(){    	   	
    	return bankModel!=null?bankModel.getBankId():null;
    }
    
    @javax.persistence.Transient
    public void setMnoId(Long mnoId){    	   	
    	if(mnoId!=null && mnoId>0){
    		mnoModel= new MnoModel();
    		mnoModel.setMnoId(mnoId);
    	}
    }
    @javax.persistence.Transient
    public void setbankId(Long bankId){    	   	
    	if(bankId!=null && bankId>0){
    		bankModel= new BankModel();
    		bankModel.setBankId(bankId);
    	}
    }

    @javax.persistence.Transient
	public Integer getIndex() {
		return index;
	}

    @javax.persistence.Transient
	public void setIndex(Integer index) {
		this.index = index;
	}
    

    @javax.persistence.Transient
    public StakeholderBankInfoModel getStakeholderBankInfoModel() {
        return stakeholderBankInfoModel;
     }
     public void setStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
        this.stakeholderBankInfoModel = stakeholderBankInfoModel;
     }
          
}
