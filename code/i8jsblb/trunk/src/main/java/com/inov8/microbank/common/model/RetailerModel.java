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
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;

/**
 * The RetailerModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="RetailerModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "RETAILER_seq",sequenceName = "RETAILER_seq", allocationSize=1)
@Table(name = "RETAILER")
public class RetailerModel extends BasePersistableModel implements Serializable {
  

   private RetailerTypeModel retailerTypeIdRetailerTypeModel;
   private ProductCatalogModel productCatalogueIdProductCatalogModel;
   private DistributorModel distributorIdDistributorModel;
   private AreaModel areaIdAreaModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private RegionModel regionModel;

   private Collection<RetailerContactModel> retailerIdRetailerContactModelList = new ArrayList<RetailerContactModel>();

   private Long retailerId;
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
   private Boolean active=Boolean.TRUE;
   private String description;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
   private Integer index;
   private String comments;
   private Boolean editMode;

   //Fields for bulk upload
   private String serialNo;
   private Boolean validRecord;
   private String groupEmail;
   private String groupDesc;
   private Boolean groupActive;
   private Boolean permissionCreate;
   private Boolean permissionRead;
   private Boolean permissionUpdate;
   private Boolean permissionDelete;
   private String failureReason;
   private String createdByName;
   private String distributorName;
   private String regionName;
   
   /**
    * Default constructor.
    */
   public RetailerModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getRetailerId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setRetailerId(primaryKey);
    }

   /**
    * Returns the value of the <code>retailerId</code> property.
    *
    */
      @Column(name = "RETAILER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RETAILER_seq")
   public Long getRetailerId() {
      return retailerId;
   }

   /**
    * Sets the value of the <code>retailerId</code> property.
    *
    * @param retailerId the value for the <code>retailerId</code> property
    *    
		    */

   public void setRetailerId(Long retailerId) {
      this.retailerId = retailerId;
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
    * Returns the value of the <code>retailerTypeIdRetailerTypeModel</code> relation property.
    *
    * @return the value of the <code>retailerTypeIdRetailerTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RETAILER_TYPE_ID")    
   public RetailerTypeModel getRelationRetailerTypeIdRetailerTypeModel(){
      return retailerTypeIdRetailerTypeModel;
   }
    
   /**
    * Returns the value of the <code>retailerTypeIdRetailerTypeModel</code> relation property.
    *
    * @return the value of the <code>retailerTypeIdRetailerTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public RetailerTypeModel getRetailerTypeIdRetailerTypeModel(){
      return getRelationRetailerTypeIdRetailerTypeModel();
   }

   /**
    * Sets the value of the <code>retailerTypeIdRetailerTypeModel</code> relation property.
    *
    * @param retailerTypeModel a value for <code>retailerTypeIdRetailerTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRetailerTypeIdRetailerTypeModel(RetailerTypeModel retailerTypeModel) {
      this.retailerTypeIdRetailerTypeModel = retailerTypeModel;
   }
   
   /**
    * Sets the value of the <code>retailerTypeIdRetailerTypeModel</code> relation property.
    *
    * @param retailerTypeModel a value for <code>retailerTypeIdRetailerTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRetailerTypeIdRetailerTypeModel(RetailerTypeModel retailerTypeModel) {
      if(null != retailerTypeModel)
      {
      	setRelationRetailerTypeIdRetailerTypeModel((RetailerTypeModel)retailerTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>productCatalogueIdProductCatalogModel</code> relation property.
    *
    * @return the value of the <code>productCatalogueIdProductCatalogModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_CATALOGUE_ID")    
   public ProductCatalogModel getRelationProductCatalogueIdProductCatalogModel(){
      return productCatalogueIdProductCatalogModel;
   }
    
   /**
    * Returns the value of the <code>productCatalogueIdProductCatalogModel</code> relation property.
    *
    * @return the value of the <code>productCatalogueIdProductCatalogModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductCatalogModel getProductCatalogueIdProductCatalogModel(){
      return getRelationProductCatalogueIdProductCatalogModel();
   }

   /**
    * Sets the value of the <code>productCatalogueIdProductCatalogModel</code> relation property.
    *
    * @param productCatalogModel a value for <code>productCatalogueIdProductCatalogModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductCatalogueIdProductCatalogModel(ProductCatalogModel productCatalogModel) {
      this.productCatalogueIdProductCatalogModel = productCatalogModel;
   }
   
   /**
    * Sets the value of the <code>productCatalogueIdProductCatalogModel</code> relation property.
    *
    * @param productCatalogModel a value for <code>productCatalogueIdProductCatalogModel</code>.
    */
   @javax.persistence.Transient
   public void setProductCatalogueIdProductCatalogModel(ProductCatalogModel productCatalogModel) {
      if(null != productCatalogModel)
      {
      	setRelationProductCatalogueIdProductCatalogModel((ProductCatalogModel)productCatalogModel.clone());
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
   
   
   @javax.persistence.Transient
   public String getAgentNetworkName()
   {
	   if(distributorIdDistributorModel != null)
	   {
		   return distributorIdDistributorModel.getName();
	   }
	   else
	   {
		   return null;
	   }	   
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
    * Add the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be added.
    */
    
   public void addRetailerIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      retailerContactModel.setRelationRetailerIdRetailerModel(this);
      retailerIdRetailerContactModelList.add(retailerContactModel);
   }
   
   /**
    * Remove the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be removed.
    */
   
   public void removeRetailerIdRetailerContactModel(RetailerContactModel retailerContactModel) {      
      retailerContactModel.setRelationRetailerIdRetailerModel(null);
      retailerIdRetailerContactModelList.remove(retailerContactModel);      
   }

   /**
    * Get a list of related RetailerContactModel objects of the RetailerModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerId member.
    *
    * @return Collection of RetailerContactModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationRetailerIdRetailerModel")
   @JoinColumn(name = "RETAILER_ID")
   public Collection<RetailerContactModel> getRetailerIdRetailerContactModelList() throws Exception {
   		return retailerIdRetailerContactModelList;
   }


   /**
    * Set a list of RetailerContactModel related objects to the RetailerModel object.
    * These objects are in a bidirectional one-to-many relation by the RetailerId member.
    *
    * @param retailerContactModelList the list of related objects.
    */
    public void setRetailerIdRetailerContactModelList(Collection<RetailerContactModel> retailerContactModelList) throws Exception {
		this.retailerIdRetailerContactModelList = retailerContactModelList;
   }



   /**
    * Returns the value of the <code>retailerTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getRetailerTypeId() {
      if (retailerTypeIdRetailerTypeModel != null) {
         return retailerTypeIdRetailerTypeModel.getRetailerTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>retailerTypeId</code> property.
    *
    * @param retailerTypeId the value for the <code>retailerTypeId</code> property
					    * @spring.validator type="required"
																																															    */
   
   @javax.persistence.Transient
   public void setRetailerTypeId(Long retailerTypeId) {
      if(retailerTypeId == null)
      {      
      	retailerTypeIdRetailerTypeModel = null;
      }
      else
      {
        retailerTypeIdRetailerTypeModel = new RetailerTypeModel();
      	retailerTypeIdRetailerTypeModel.setRetailerTypeId(retailerTypeId);
      }      
   }

   /**
    * Returns the value of the <code>productCatalogId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductCatalogueId() {
      if (productCatalogueIdProductCatalogModel != null) {
         return productCatalogueIdProductCatalogModel.getProductCatalogId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productCatalogId</code> property.
    *
    * @param productCatalogId the value for the <code>productCatalogId</code> property
											    * @spring.validator type="required"
																																									    */
   
   @javax.persistence.Transient
   public void setProductCatalogueId(Long productCatalogId) {
      if(productCatalogId == null)
      {      
      	productCatalogueIdProductCatalogModel = null;
      }
      else
      {
        productCatalogueIdProductCatalogModel = new ProductCatalogModel();
      	productCatalogueIdProductCatalogModel.setProductCatalogId(productCatalogId);
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
        checkBox += "_"+ getRetailerId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&retailerId=" + getRetailerId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "retailerId";
			return primaryKeyFieldName;				
    }
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID")
    public RegionModel getRegionModel() {
    	return regionModel;
    }
    @javax.persistence.Transient
    public void setRegionModel(RegionModel regionModel) {
    	this.regionModel = regionModel;
    }
    
    @javax.persistence.Transient
    public Long getRegionModelId() {
    	if(null!=regionModel){
    		return regionModel.getRegionId();
    	}
    	return null;
    }
    @javax.persistence.Transient
    public void setRegionModelId(Long regionModel) {    	
    	if(null!=regionModel){
    		this.regionModel = new RegionModel();
    		this.regionModel.setRegionId(regionModel);
    	}
    }
    
    @javax.persistence.Transient
    public String getRegionModelName() {
    	if(null!=regionModel){
    		return regionModel.getRegionName();
    	}
    	return null;
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
    	
    	associationModel.setClassName("RetailerTypeModel");
    	associationModel.setPropertyName("relationRetailerTypeIdRetailerTypeModel");   		
   		associationModel.setValue(getRelationRetailerTypeIdRetailerTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductCatalogModel");
    	associationModel.setPropertyName("relationProductCatalogueIdProductCatalogModel");   		
   		associationModel.setValue(getRelationProductCatalogueIdProductCatalogModel());
   		
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
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("RegionModel");
    	associationModel.setPropertyName("regionModel");   		
   		associationModel.setValue(getRegionModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }

    @javax.persistence.Transient
	public Boolean getEditMode() {
		return editMode;
	}

    @javax.persistence.Transient
	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}
    
    @Transient
	public Boolean getValidRecord() {
		return validRecord;
	}

	public void setValidRecord(Boolean validRecord) {
		this.validRecord = validRecord;
	}

    @Transient
	public String getGroupEmail() {
		return groupEmail;
	}

	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}

    @Transient
	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

    @Transient
	public Boolean getGroupActive() {
		return groupActive;
	}

	public void setGroupActive(Boolean groupActive) {
		this.groupActive = groupActive;
	}

    @Transient
	public Boolean getPermissionCreate() {
		return permissionCreate;
	}

	public void setPermissionCreate(Boolean permissionCreate) {
		this.permissionCreate = permissionCreate;
	}

    @Transient
	public Boolean getPermissionRead() {
		return permissionRead;
	}

	public void setPermissionRead(Boolean permissionRead) {
		this.permissionRead = permissionRead;
	}

    @Transient
	public Boolean getPermissionUpdate() {
		return permissionUpdate;
	}

	public void setPermissionUpdate(Boolean permissionUpdate) {
		this.permissionUpdate = permissionUpdate;
	}

    @Transient
	public Boolean getPermissionDelete() {
		return permissionDelete;
	}

	public void setPermissionDelete(Boolean permissionDelete) {
		this.permissionDelete = permissionDelete;
	}

    @Transient
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

    @Transient
	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

    @Transient
	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

    @Transient
	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

    @Transient
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	@Transient
	public Integer getIndex() {
		return index;
	}
	@Transient
	public void setIndex(Integer index) {
		this.index = index;
	}    
    
}
