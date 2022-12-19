package com.inov8.microbank.common.model;

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
import com.inov8.microbank.common.model.agenthierarchy.AreaLevelModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionalHierarchyModel;

/**
 * The AreaModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AreaModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "AREA_seq",sequenceName = "AREA_seq", allocationSize=1)
@Table(name = "AREA")
public class AreaModel extends BasePersistableModel {
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -21546535693474855L;
private AreaModel ultimateParentAreaIdAreaModel;
   private AreaModel parentAreaIdAreaModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private AreaLevelModel areaLevelModel;
   private RegionModel regionModel;
   private RegionalHierarchyModel regionalHierarchyModel;

   private Collection<AreaModel> ultimateParentAreaIdAreaModelList = new ArrayList<AreaModel>();
   private Collection<AreaModel> parentAreaIdAreaModelList = new ArrayList<AreaModel>();
   private Collection<DistributorModel> areaIdDistributorModelList = new ArrayList<DistributorModel>();
   private Collection<DistributorContactModel> areaIdDistributorContactModelList = new ArrayList<DistributorContactModel>();
   private Collection<RetailerModel> areaIdRetailerModelList = new ArrayList<RetailerModel>();
   private Collection<RetailerContactModel> areaIdRetailerContactModelList = new ArrayList<RetailerContactModel>();

   private Long areaId;
   private String name;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private boolean active;
   private Integer index;
   boolean deleted;
   boolean activeDisabled;
   boolean editMode;
   boolean dirty;
   boolean disabled;
   
   
   /**
    * Default constructor.
    */
   public AreaModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAreaId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAreaId(primaryKey);
    }

   /**
    * Returns the value of the <code>areaId</code> property.
    *
    */
      @Column(name = "AREA_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AREA_seq")
   public Long getAreaId() {
      return areaId;
   }

   /**
    * Sets the value of the <code>areaId</code> property.
    *
    * @param areaId the value for the <code>areaId</code> property
    *    
		    */

   public void setAreaId(Long areaId) {
      this.areaId = areaId;
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
    * Returns the value of the <code>ultimateParentAreaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>ultimateParentAreaIdAreaModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ULTIMATE_PARENT_AREA_ID")    
   public AreaModel getRelationUltimateParentAreaIdAreaModel(){
      return ultimateParentAreaIdAreaModel;
   }
    
   /**
    * Returns the value of the <code>ultimateParentAreaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>ultimateParentAreaIdAreaModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AreaModel getUltimateParentAreaIdAreaModel(){
      return getRelationUltimateParentAreaIdAreaModel();
   }

   /**
    * Sets the value of the <code>ultimateParentAreaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>ultimateParentAreaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUltimateParentAreaIdAreaModel(AreaModel areaModel) {
      this.ultimateParentAreaIdAreaModel = areaModel;
   }
   
   /**
    * Sets the value of the <code>ultimateParentAreaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>ultimateParentAreaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setUltimateParentAreaIdAreaModel(AreaModel areaModel) {
      if(null != areaModel)
      {
      	setRelationUltimateParentAreaIdAreaModel((AreaModel)areaModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>parentAreaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>parentAreaIdAreaModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PARENT_AREA_ID")    
   public AreaModel getRelationParentAreaIdAreaModel(){
      return parentAreaIdAreaModel;
   }
    
   /**
    * Returns the value of the <code>parentAreaIdAreaModel</code> relation property.
    *
    * @return the value of the <code>parentAreaIdAreaModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AreaModel getParentAreaIdAreaModel(){
      return getRelationParentAreaIdAreaModel();
   }

   @javax.persistence.Transient
   public String getParentAreaName()
   {
	   if(parentAreaIdAreaModel != null)
	   {
		   return this.parentAreaIdAreaModel.getName();
	   }
	   else
	   {
		   return null;
	   }
   }
   
   /**
    * Sets the value of the <code>parentAreaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>parentAreaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationParentAreaIdAreaModel(AreaModel areaModel) {
      this.parentAreaIdAreaModel = areaModel;
   }
   
   /**
    * Sets the value of the <code>parentAreaIdAreaModel</code> relation property.
    *
    * @param areaModel a value for <code>parentAreaIdAreaModel</code>.
    */
   @javax.persistence.Transient
   public void setParentAreaIdAreaModel(AreaModel areaModel) {
      if(null != areaModel)
      {
      	setRelationParentAreaIdAreaModel((AreaModel)areaModel.clone());
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
    * Add the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be added.
    */
    
   public void addUltimateParentAreaIdAreaModel(AreaModel areaModel) {
      areaModel.setRelationUltimateParentAreaIdAreaModel(this);
      ultimateParentAreaIdAreaModelList.add(areaModel);
   }
   
   /**
    * Remove the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be removed.
    */
   
   public void removeUltimateParentAreaIdAreaModel(AreaModel areaModel) {      
      areaModel.setRelationUltimateParentAreaIdAreaModel(null);
      ultimateParentAreaIdAreaModelList.remove(areaModel);      
   }

   /**
    * Get a list of related AreaModel objects of the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the UltimateParentAreaId member.
    *
    * @return Collection of AreaModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUltimateParentAreaIdAreaModel")
   @JoinColumn(name = "ULTIMATE_PARENT_AREA_ID")
   public Collection<AreaModel> getUltimateParentAreaIdAreaModelList() throws Exception {
   		return ultimateParentAreaIdAreaModelList;
   }


   /**
    * Set a list of AreaModel related objects to the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the UltimateParentAreaId member.
    *
    * @param areaModelList the list of related objects.
    */
    public void setUltimateParentAreaIdAreaModelList(Collection<AreaModel> areaModelList) throws Exception {
		this.ultimateParentAreaIdAreaModelList = areaModelList;
   }


   /**
    * Add the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be added.
    */
    
   public void addParentAreaIdAreaModel(AreaModel areaModel) {
      areaModel.setRelationParentAreaIdAreaModel(this);
      parentAreaIdAreaModelList.add(areaModel);
   }
   
   /**
    * Remove the related AreaModel to this one-to-many relation.
    *
    * @param areaModel object to be removed.
    */
   
   public void removeParentAreaIdAreaModel(AreaModel areaModel) {      
      areaModel.setRelationParentAreaIdAreaModel(null);
      parentAreaIdAreaModelList.remove(areaModel);      
   }

   /**
    * Get a list of related AreaModel objects of the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the ParentAreaId member.
    *
    * @return Collection of AreaModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationParentAreaIdAreaModel")
   @JoinColumn(name = "PARENT_AREA_ID")
   public Collection<AreaModel> getParentAreaIdAreaModelList() throws Exception {
   		return parentAreaIdAreaModelList;
   }


   /**
    * Set a list of AreaModel related objects to the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the ParentAreaId member.
    *
    * @param areaModelList the list of related objects.
    */
    public void setParentAreaIdAreaModelList(Collection<AreaModel> areaModelList) throws Exception {
		this.parentAreaIdAreaModelList = areaModelList;
   }


   /**
    * Add the related DistributorModel to this one-to-many relation.
    *
    * @param distributorModel object to be added.
    */
    
   public void addAreaIdDistributorModel(DistributorModel distributorModel) {
      distributorModel.setRelationAreaIdAreaModel(this);
      areaIdDistributorModelList.add(distributorModel);
   }
   
   /**
    * Remove the related DistributorModel to this one-to-many relation.
    *
    * @param distributorModel object to be removed.
    */
   
   public void removeAreaIdDistributorModel(DistributorModel distributorModel) {      
      distributorModel.setRelationAreaIdAreaModel(null);
      areaIdDistributorModelList.remove(distributorModel);      
   }

   /**
    * Get a list of related DistributorModel objects of the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @return Collection of DistributorModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAreaIdAreaModel")
   @JoinColumn(name = "AREA_ID")
   public Collection<DistributorModel> getAreaIdDistributorModelList() throws Exception {
   		return areaIdDistributorModelList;
   }


   /**
    * Set a list of DistributorModel related objects to the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @param distributorModelList the list of related objects.
    */
    public void setAreaIdDistributorModelList(Collection<DistributorModel> distributorModelList) throws Exception {
		this.areaIdDistributorModelList = distributorModelList;
   }


   /**
    * Add the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be added.
    */
    
   public void addAreaIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationAreaIdAreaModel(this);
      areaIdDistributorContactModelList.add(distributorContactModel);
   }
   
   /**
    * Remove the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be removed.
    */
   
   public void removeAreaIdDistributorContactModel(DistributorContactModel distributorContactModel) {      
      distributorContactModel.setRelationAreaIdAreaModel(null);
      areaIdDistributorContactModelList.remove(distributorContactModel);      
   }

   /**
    * Get a list of related DistributorContactModel objects of the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @return Collection of DistributorContactModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAreaIdAreaModel")
   @JoinColumn(name = "AREA_ID")
   public Collection<DistributorContactModel> getAreaIdDistributorContactModelList() throws Exception {
   		return areaIdDistributorContactModelList;
   }


   /**
    * Set a list of DistributorContactModel related objects to the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @param distributorContactModelList the list of related objects.
    */
    public void setAreaIdDistributorContactModelList(Collection<DistributorContactModel> distributorContactModelList) throws Exception {
		this.areaIdDistributorContactModelList = distributorContactModelList;
   }


   /**
    * Add the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be added.
    */
    
   public void addAreaIdRetailerModel(RetailerModel retailerModel) {
      retailerModel.setRelationAreaIdAreaModel(this);
      areaIdRetailerModelList.add(retailerModel);
   }
   
   /**
    * Remove the related RetailerModel to this one-to-many relation.
    *
    * @param retailerModel object to be removed.
    */
   
   public void removeAreaIdRetailerModel(RetailerModel retailerModel) {      
      retailerModel.setRelationAreaIdAreaModel(null);
      areaIdRetailerModelList.remove(retailerModel);      
   }

   /**
    * Get a list of related RetailerModel objects of the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @return Collection of RetailerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAreaIdAreaModel")
   @JoinColumn(name = "AREA_ID")
   public Collection<RetailerModel> getAreaIdRetailerModelList() throws Exception {
   		return areaIdRetailerModelList;
   }


   /**
    * Set a list of RetailerModel related objects to the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @param retailerModelList the list of related objects.
    */
    public void setAreaIdRetailerModelList(Collection<RetailerModel> retailerModelList) throws Exception {
		this.areaIdRetailerModelList = retailerModelList;
   }


   /**
    * Add the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be added.
    */
    
   public void addAreaIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      retailerContactModel.setRelationAreaIdAreaModel(this);
      areaIdRetailerContactModelList.add(retailerContactModel);
   }
   
   /**
    * Remove the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be removed.
    */
   
   public void removeAreaIdRetailerContactModel(RetailerContactModel retailerContactModel) {      
      retailerContactModel.setRelationAreaIdAreaModel(null);
      areaIdRetailerContactModelList.remove(retailerContactModel);      
   }

   /**
    * Get a list of related RetailerContactModel objects of the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @return Collection of RetailerContactModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAreaIdAreaModel")
   @JoinColumn(name = "AREA_ID")
   public Collection<RetailerContactModel> getAreaIdRetailerContactModelList() throws Exception {
   		return areaIdRetailerContactModelList;
   }


   /**
    * Set a list of RetailerContactModel related objects to the AreaModel object.
    * These objects are in a bidirectional one-to-many relation by the AreaId member.
    *
    * @param retailerContactModelList the list of related objects.
    */
    public void setAreaIdRetailerContactModelList(Collection<RetailerContactModel> retailerContactModelList) throws Exception {
		this.areaIdRetailerContactModelList = retailerContactModelList;
   }



   /**
    * Returns the value of the <code>areaId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUltimateParentAreaId() {
      if (ultimateParentAreaIdAreaModel != null) {
         return ultimateParentAreaIdAreaModel.getAreaId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>areaId</code> property.
    *
    * @param areaId the value for the <code>areaId</code> property
																									    */
   
   @javax.persistence.Transient
   public void setUltimateParentAreaId(Long areaId) {
      if(areaId == null)
      {      
      	ultimateParentAreaIdAreaModel = null;
      }
      else
      {
        ultimateParentAreaIdAreaModel = new AreaModel();
      	ultimateParentAreaIdAreaModel.setAreaId(areaId);
      }      
   }

   /**
    * Returns the value of the <code>areaId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getParentAreaId() {
      if (parentAreaIdAreaModel != null) {
         return parentAreaIdAreaModel.getAreaId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>areaId</code> property.
    *
    * @param areaId the value for the <code>areaId</code> property
																									    */
   
   @javax.persistence.Transient
   public void setParentAreaId(Long areaId) {
      if(areaId == null)
      {      
      	parentAreaIdAreaModel = null;
      }
      else
      {
        parentAreaIdAreaModel = new AreaModel();
      	parentAreaIdAreaModel.setAreaId(areaId);
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
        checkBox += "_"+ getAreaId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&areaId=" + getAreaId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "areaId";
			return primaryKeyFieldName;				
    }
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_LEVEL_ID")
    public AreaLevelModel getAreaLevelModel() {
    	return areaLevelModel;
    }
    @javax.persistence.Transient
    public void setAreaLevelModel(AreaLevelModel areaLevelModel) {
    	this.areaLevelModel = areaLevelModel;
    }
    
    @javax.persistence.Transient
    public Long getAreaLevelModelId() {
    	if(null!=areaLevelModel){
    		return areaLevelModel.getAreaLevelId();
    	}
    	return null;
    }
    @javax.persistence.Transient
    public void setAreaLevelModelId(Long areaLevelModelId) {    	
    	if(null!=areaLevelModelId){
    		this.areaLevelModel = new AreaLevelModel();
    		this.areaLevelModel.setAreaLevelId(areaLevelModelId);
    	}
    }
    
    @javax.persistence.Transient
    public String getAreaLevelName() 
    {
    	if(null!=areaLevelModel){
    		return areaLevelModel.getAreaLevelName();
    	}
    	return null;
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
    public String getRegionName() 
    {
    	if(null != regionModel)
    	{
    		return regionModel.getRegionName();
    	}
    	return null;
    }
    
    
    @Column(name = "IS_ACTIVE" , nullable = false )
    public boolean isActive() {
       return active;
    }

    /**
     * Sets the value of the <code>active</code> property.
     *
     * @param active the value for the <code>active</code> property
     *    
 		    */

    
    @javax.persistence.Transient
    public boolean getActive() 
    {
    	return active;
 	}
    
    
    
    public void setActive(boolean active) {
       this.active = active;
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
    	associationModel.setPropertyName("relationUltimateParentAreaIdAreaModel");   		
   		associationModel.setValue(getRelationUltimateParentAreaIdAreaModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AreaModel");
    	associationModel.setPropertyName("relationParentAreaIdAreaModel");   		
   		associationModel.setValue(getRelationParentAreaIdAreaModel());
   		
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
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("RegionModel");
    	associationModel.setPropertyName("regionModel");   		
   		associationModel.setValue(getRegionModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AreaLevelModel");
    	associationModel.setPropertyName("areaLevelModel");   		
   		associationModel.setValue(getAreaLevelModel());
   		
   		associationModelList.add(associationModel);  		
			    	
    	return associationModelList;
    }
    
    
    @javax.persistence.Transient
	public boolean isDeleted() {
		return deleted;
	}

    @javax.persistence.Transient
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

    @javax.persistence.Transient
	public boolean isActiveDisabled() {
		return activeDisabled;
	}

    @javax.persistence.Transient
	public void setActiveDisabled(boolean activeDisabled) {
		this.activeDisabled = activeDisabled;
	}

    @javax.persistence.Transient
	public boolean isEditMode() {
		return editMode;
	}

    @javax.persistence.Transient
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

    @javax.persistence.Transient
	public boolean isDirty() {
		return dirty;
	}

    @javax.persistence.Transient
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

    @javax.persistence.Transient
	public boolean isDisabled() {
		return disabled;
	}

    @javax.persistence.Transient
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
    @javax.persistence.Transient
	public Integer getIndex() {
		return index;
	}
    @javax.persistence.Transient
	public void setIndex(Integer index) {
		this.index = index;
	}

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn( name = "REGIONAL_HIERARCHY_ID" )
	  	public RegionalHierarchyModel getRegionalHierarchyModel() {
			return regionalHierarchyModel;
		}

		public void setRegionalHierarchyModel(
				RegionalHierarchyModel regionalHierarchyModel) {
			this.regionalHierarchyModel = regionalHierarchyModel;
		}
	  	
	  	
	   /**
	    * Returns the value of the <code>distributorId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getRegionalHierarchyId() {
	      if (regionalHierarchyModel != null) {
	         return regionalHierarchyModel.getRegionalHierarchyId();
	      } else {
	         return null;
	      }
	   }

		/**
	    * Sets the value of the <code>distributorId</code> property.
	    *
	    * @param distributorId the value for the <code>distributorId</code> property
	    * 
	   */
	    
	   @javax.persistence.Transient
		public void setRegionalHierarchyId(Long regionalHierarchyId) 
		{
			if(regionalHierarchyId == null)
			{      
				this.regionalHierarchyModel = null;
			}
			else
			{
				regionalHierarchyModel = new RegionalHierarchyModel();
				regionalHierarchyModel.setRegionalHierarchyId(regionalHierarchyId);
			}      
	  }    

}
