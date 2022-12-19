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
 * The SupplierModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SupplierModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SUPPLIER_seq",sequenceName = "SUPPLIER_seq", allocationSize=1)
@Table(name = "SUPPLIER")
public class SupplierModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 5505479124607729656L;
private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<ConcernPartnerModel> supplierIdConcernPartnerModelList = new ArrayList<ConcernPartnerModel>();
   private Collection<MnoModel> supplierIdMnoModelList = new ArrayList<MnoModel>();
   private Collection<PartnerModel> supplierIdPartnerModelList = new ArrayList<PartnerModel>();
   private Collection<ProductModel> supplierIdProductModelList = new ArrayList<ProductModel>();
   private Collection<ShipmentModel> supplierIdShipmentModelList = new ArrayList<ShipmentModel>();
   private Collection<SupplierBankInfoModel> supplierIdSupplierBankInfoModelList = new ArrayList<SupplierBankInfoModel>();
   private Collection<SupplierUserModel> supplierIdSupplierUserModelList = new ArrayList<SupplierUserModel>();

   private Long supplierId;
   private String name;
   private String contactName;
   private String address1;
   private String address2;
   private String city;
   private String state;
   private String zip;
   private String country;
   private String phoneNo;
   private String fax;
   private String email;
   private String description;
   private String comments;
   private Boolean active;
   private Boolean vendor;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private String imagePath;

   /**
    * Default constructor.
    */
   public SupplierModel() {
   }   

   public SupplierModel(Long pk, String name) {
	   this.supplierId = pk;
	   this.name = name;
   } 

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSupplierId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSupplierId(primaryKey);
    }

   /**
    * Returns the value of the <code>supplierId</code> property.
    *
    */
      @Column(name = "SUPPLIER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SUPPLIER_seq")
   public Long getSupplierId() {
      return supplierId;
   }

   /**
    * Sets the value of the <code>supplierId</code> property.
    *
    * @param supplierId the value for the <code>supplierId</code> property
    *    
		    */

   public void setSupplierId(Long supplierId) {
      this.supplierId = supplierId;
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
    * Returns the value of the <code>vendor</code> property.
    *
    */
      @Column(name = "IS_VENDOR" , nullable = false )
   public Boolean getVendor() {
      return vendor;
   }

   /**
    * Sets the value of the <code>vendor</code> property.
    *
    * @param vendor the value for the <code>vendor</code> property
    *    
		    */

   public void setVendor(Boolean vendor) {
      this.vendor = vendor;
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
    * Add the related ConcernPartnerModel to this one-to-many relation.
    *
    * @param concernPartnerModel object to be added.
    */
    
   public void addSupplierIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {
      concernPartnerModel.setRelationSupplierIdSupplierModel(this);
      supplierIdConcernPartnerModelList.add(concernPartnerModel);
   }
   
   /**
    * Remove the related ConcernPartnerModel to this one-to-many relation.
    *
    * @param concernPartnerModel object to be removed.
    */
   
   public void removeSupplierIdConcernPartnerModel(ConcernPartnerModel concernPartnerModel) {      
      concernPartnerModel.setRelationSupplierIdSupplierModel(null);
      supplierIdConcernPartnerModelList.remove(concernPartnerModel);      
   }

   /**
    * Get a list of related ConcernPartnerModel objects of the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @return Collection of ConcernPartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSupplierIdSupplierModel")
   @JoinColumn(name = "SUPPLIER_ID")
   public Collection<ConcernPartnerModel> getSupplierIdConcernPartnerModelList() throws Exception {
   		return supplierIdConcernPartnerModelList;
   }


   /**
    * Set a list of ConcernPartnerModel related objects to the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @param concernPartnerModelList the list of related objects.
    */
    public void setSupplierIdConcernPartnerModelList(Collection<ConcernPartnerModel> concernPartnerModelList) throws Exception {
		this.supplierIdConcernPartnerModelList = concernPartnerModelList;
   }


   /**
    * Add the related MnoModel to this one-to-many relation.
    *
    * @param mnoModel object to be added.
    */
    
   public void addSupplierIdMnoModel(MnoModel mnoModel) {
      mnoModel.setRelationSupplierIdSupplierModel(this);
      supplierIdMnoModelList.add(mnoModel);
   }
   
   /**
    * Remove the related MnoModel to this one-to-many relation.
    *
    * @param mnoModel object to be removed.
    */
   
   public void removeSupplierIdMnoModel(MnoModel mnoModel) {      
      mnoModel.setRelationSupplierIdSupplierModel(null);
      supplierIdMnoModelList.remove(mnoModel);      
   }

   /**
    * Get a list of related MnoModel objects of the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @return Collection of MnoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSupplierIdSupplierModel")
   @JoinColumn(name = "SUPPLIER_ID")
   public Collection<MnoModel> getSupplierIdMnoModelList() throws Exception {
   		return supplierIdMnoModelList;
   }


   /**
    * Set a list of MnoModel related objects to the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @param mnoModelList the list of related objects.
    */
    public void setSupplierIdMnoModelList(Collection<MnoModel> mnoModelList) throws Exception {
		this.supplierIdMnoModelList = mnoModelList;
   }


   /**
    * Add the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be added.
    */
    
   public void addSupplierIdPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationSupplierIdSupplierModel(this);
      supplierIdPartnerModelList.add(partnerModel);
   }
   
   /**
    * Remove the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be removed.
    */
   
   public void removeSupplierIdPartnerModel(PartnerModel partnerModel) {      
      partnerModel.setRelationSupplierIdSupplierModel(null);
      supplierIdPartnerModelList.remove(partnerModel);      
   }

   /**
    * Get a list of related PartnerModel objects of the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @return Collection of PartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSupplierIdSupplierModel")
   @JoinColumn(name = "SUPPLIER_ID")
   public Collection<PartnerModel> getSupplierIdPartnerModelList() throws Exception {
   		return supplierIdPartnerModelList;
   }


   /**
    * Set a list of PartnerModel related objects to the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @param partnerModelList the list of related objects.
    */
    public void setSupplierIdPartnerModelList(Collection<PartnerModel> partnerModelList) throws Exception {
		this.supplierIdPartnerModelList = partnerModelList;
   }


   /**
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */
    
   public void addSupplierIdProductModel(ProductModel productModel) {
      productModel.setRelationSupplierIdSupplierModel(this);
      supplierIdProductModelList.add(productModel);
   }
   
   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */
   
   public void removeSupplierIdProductModel(ProductModel productModel) {      
      productModel.setRelationSupplierIdSupplierModel(null);
      supplierIdProductModelList.remove(productModel);      
   }

   /**
    * Get a list of related ProductModel objects of the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @return Collection of ProductModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSupplierIdSupplierModel")
   @JoinColumn(name = "SUPPLIER_ID")
   public Collection<ProductModel> getSupplierIdProductModelList() throws Exception {
   		return supplierIdProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @param productModelList the list of related objects.
    */
    public void setSupplierIdProductModelList(Collection<ProductModel> productModelList) throws Exception {
		this.supplierIdProductModelList = productModelList;
   }


   /**
    * Add the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be added.
    */
    
   public void addSupplierIdShipmentModel(ShipmentModel shipmentModel) {
      shipmentModel.setRelationSupplierIdSupplierModel(this);
      supplierIdShipmentModelList.add(shipmentModel);
   }
   
   /**
    * Remove the related ShipmentModel to this one-to-many relation.
    *
    * @param shipmentModel object to be removed.
    */
   
   public void removeSupplierIdShipmentModel(ShipmentModel shipmentModel) {      
      shipmentModel.setRelationSupplierIdSupplierModel(null);
      supplierIdShipmentModelList.remove(shipmentModel);      
   }

   /**
    * Get a list of related ShipmentModel objects of the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @return Collection of ShipmentModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSupplierIdSupplierModel")
   @JoinColumn(name = "SUPPLIER_ID")
   public Collection<ShipmentModel> getSupplierIdShipmentModelList() throws Exception {
   		return supplierIdShipmentModelList;
   }


   /**
    * Set a list of ShipmentModel related objects to the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @param shipmentModelList the list of related objects.
    */
    public void setSupplierIdShipmentModelList(Collection<ShipmentModel> shipmentModelList) throws Exception {
		this.supplierIdShipmentModelList = shipmentModelList;
   }


   /**
    * Add the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be added.
    */
    
   public void addSupplierIdSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {
      supplierBankInfoModel.setRelationSupplierIdSupplierModel(this);
      supplierIdSupplierBankInfoModelList.add(supplierBankInfoModel);
   }
   
   /**
    * Remove the related SupplierBankInfoModel to this one-to-many relation.
    *
    * @param supplierBankInfoModel object to be removed.
    */
   
   public void removeSupplierIdSupplierBankInfoModel(SupplierBankInfoModel supplierBankInfoModel) {      
      supplierBankInfoModel.setRelationSupplierIdSupplierModel(null);
      supplierIdSupplierBankInfoModelList.remove(supplierBankInfoModel);      
   }

   /**
    * Get a list of related SupplierBankInfoModel objects of the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @return Collection of SupplierBankInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSupplierIdSupplierModel")
   @JoinColumn(name = "SUPPLIER_ID")
   public Collection<SupplierBankInfoModel> getSupplierIdSupplierBankInfoModelList() throws Exception {
   		return supplierIdSupplierBankInfoModelList;
   }


   /**
    * Set a list of SupplierBankInfoModel related objects to the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @param supplierBankInfoModelList the list of related objects.
    */
    public void setSupplierIdSupplierBankInfoModelList(Collection<SupplierBankInfoModel> supplierBankInfoModelList) throws Exception {
		this.supplierIdSupplierBankInfoModelList = supplierBankInfoModelList;
   }


   /**
    * Add the related SupplierUserModel to this one-to-many relation.
    *
    * @param supplierUserModel object to be added.
    */
    
   public void addSupplierIdSupplierUserModel(SupplierUserModel supplierUserModel) {
      supplierUserModel.setRelationSupplierIdSupplierModel(this);
      supplierIdSupplierUserModelList.add(supplierUserModel);
   }
   
   /**
    * Remove the related SupplierUserModel to this one-to-many relation.
    *
    * @param supplierUserModel object to be removed.
    */
   
   public void removeSupplierIdSupplierUserModel(SupplierUserModel supplierUserModel) {      
      supplierUserModel.setRelationSupplierIdSupplierModel(null);
      supplierIdSupplierUserModelList.remove(supplierUserModel);      
   }

   /**
    * Get a list of related SupplierUserModel objects of the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @return Collection of SupplierUserModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSupplierIdSupplierModel")
   @JoinColumn(name = "SUPPLIER_ID")
   public Collection<SupplierUserModel> getSupplierIdSupplierUserModelList() throws Exception {
   		return supplierIdSupplierUserModelList;
   }


   /**
    * Set a list of SupplierUserModel related objects to the SupplierModel object.
    * These objects are in a bidirectional one-to-many relation by the SupplierId member.
    *
    * @param supplierUserModelList the list of related objects.
    */
    public void setSupplierIdSupplierUserModelList(Collection<SupplierUserModel> supplierUserModelList) throws Exception {
		this.supplierIdSupplierUserModelList = supplierUserModelList;
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
        checkBox += "_"+ getSupplierId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&supplierId=" + getSupplierId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "supplierId";
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
