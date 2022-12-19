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
import com.inov8.microbank.common.model.agenthierarchy.RegionalHierarchyModel;

/**
 * The DistributorLevelModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistributorLevelModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DISTRIBUTOR_LEVEL_seq",sequenceName = "DISTRIBUTOR_LEVEL_seq", allocationSize=1)
@Table(name = "DISTRIBUTOR_LEVEL")
public class DistributorLevelModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 8366472563522921793L;
	private DistributorLevelModel managingLevelIdDistributorLevelModel;
	private DistributorLevelModel ultimateManagingLevelIdDistributorLevelModel;
	private AppUserModel createdByAppUserModel;
   	private AppUserModel updatedByAppUserModel;
   	private RegionModel regionModel;
   	private RegionalHierarchyModel regionalHierarchyModel;
   
   	private Collection<DistributorContactModel> distributorLevelIdDistributorContactModelList = new ArrayList<DistributorContactModel>();
   	private Collection<DistributorLevelModel> managingLevelIdDistributorLevelModelList = new ArrayList<DistributorLevelModel>();
   	private Collection<DistributorLevelModel> ultimateManagingLevelIdDistributorLevelModelList = new ArrayList<DistributorLevelModel>();

   	private Long distributorLevelId;
   	private String name;
   	private String description;
   	private String comments;
   	private Date updatedOn;
   	private Date createdOn;
   	private boolean active;
   	private Integer versionNo;
   	private Integer index;
   	boolean deleted;
	boolean selected;
	boolean editMode;
	boolean dirty;

   /**
    * Default constructor.
    */
   public DistributorLevelModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistributorLevelId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistributorLevelId(primaryKey);
    }

   /**
    * Returns the value of the <code>distributorLevelId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_LEVEL_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DISTRIBUTOR_LEVEL_seq")
   public Long getDistributorLevelId() {
      return distributorLevelId;
   }

   /**
    * Sets the value of the <code>distributorLevelId</code> property.
    *
    * @param distributorLevelId the value for the <code>distributorLevelId</code> property
    *    
		    */

   public void setDistributorLevelId(Long distributorLevelId) {
      this.distributorLevelId = distributorLevelId;
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
    * Returns the value of the <code>managingLevelIdDistributorLevelModel</code> relation property.
    *
    * @return the value of the <code>managingLevelIdDistributorLevelModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "MANAGING_LEVEL_ID")    
   public DistributorLevelModel getRelationManagingLevelIdDistributorLevelModel(){
      return managingLevelIdDistributorLevelModel;
   }
    
   /**
    * Returns the value of the <code>managingLevelIdDistributorLevelModel</code> relation property.
    *
    * @return the value of the <code>managingLevelIdDistributorLevelModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorLevelModel getManagingLevelIdDistributorLevelModel(){
      return getRelationManagingLevelIdDistributorLevelModel();
   }

   @javax.persistence.Transient
   public String getParentAgentLevelName()
   {
	   if(managingLevelIdDistributorLevelModel != null)
	   {
		   return managingLevelIdDistributorLevelModel.getName();
	   }
	   else
	   {
		   return null;
	   }
   }
   
   /**
    * Sets the value of the <code>managingLevelIdDistributorLevelModel</code> relation property.
    *
    * @param distributorLevelModel a value for <code>managingLevelIdDistributorLevelModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      this.managingLevelIdDistributorLevelModel = distributorLevelModel;
   }
   
   /**
    * Sets the value of the <code>managingLevelIdDistributorLevelModel</code> relation property.
    *
    * @param distributorLevelModel a value for <code>managingLevelIdDistributorLevelModel</code>.
    */
   @javax.persistence.Transient
   public void setManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      if(null != distributorLevelModel)
      {
      	setRelationManagingLevelIdDistributorLevelModel((DistributorLevelModel)distributorLevelModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>ultimateManagingLevelIdDistributorLevelModel</code> relation property.
    *
    * @return the value of the <code>ultimateManagingLevelIdDistributorLevelModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ULTIMATE_MANAGING_LEVEL_ID")    
   public DistributorLevelModel getRelationUltimateManagingLevelIdDistributorLevelModel(){
      return ultimateManagingLevelIdDistributorLevelModel;
   }
    
   /**
    * Returns the value of the <code>ultimateManagingLevelIdDistributorLevelModel</code> relation property.
    *
    * @return the value of the <code>ultimateManagingLevelIdDistributorLevelModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DistributorLevelModel getUltimateManagingLevelIdDistributorLevelModel(){
      return getRelationUltimateManagingLevelIdDistributorLevelModel();
   }

   /**
    * Sets the value of the <code>ultimateManagingLevelIdDistributorLevelModel</code> relation property.
    *
    * @param distributorLevelModel a value for <code>ultimateManagingLevelIdDistributorLevelModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUltimateManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      this.ultimateManagingLevelIdDistributorLevelModel = distributorLevelModel;
   }
   
   /**
    * Sets the value of the <code>ultimateManagingLevelIdDistributorLevelModel</code> relation property.
    *
    * @param distributorLevelModel a value for <code>ultimateManagingLevelIdDistributorLevelModel</code>.
    */
   @javax.persistence.Transient
   public void setUltimateManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      if(null != distributorLevelModel)
      {
      	setRelationUltimateManagingLevelIdDistributorLevelModel((DistributorLevelModel)distributorLevelModel.clone());
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
    * Add the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be added.
    */
    
   public void addDistributorLevelIdDistributorContactModel(DistributorContactModel distributorContactModel) {
      distributorContactModel.setRelationDistributorLevelIdDistributorLevelModel(this);
      distributorLevelIdDistributorContactModelList.add(distributorContactModel);
   }
   
   /**
    * Remove the related DistributorContactModel to this one-to-many relation.
    *
    * @param distributorContactModel object to be removed.
    */
   
   public void removeDistributorLevelIdDistributorContactModel(DistributorContactModel distributorContactModel) {      
      distributorContactModel.setRelationDistributorLevelIdDistributorLevelModel(null);
      distributorLevelIdDistributorContactModelList.remove(distributorContactModel);      
   }

   /**
    * Get a list of related DistributorContactModel objects of the DistributorLevelModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorLevelId member.
    *
    * @return Collection of DistributorContactModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistributorLevelIdDistributorLevelModel")
   @JoinColumn(name = "DISTRIBUTOR_LEVEL_ID")
   public Collection<DistributorContactModel> getDistributorLevelIdDistributorContactModelList() throws Exception {
   		return distributorLevelIdDistributorContactModelList;
   }


   /**
    * Set a list of DistributorContactModel related objects to the DistributorLevelModel object.
    * These objects are in a bidirectional one-to-many relation by the DistributorLevelId member.
    *
    * @param distributorContactModelList the list of related objects.
    */
    public void setDistributorLevelIdDistributorContactModelList(Collection<DistributorContactModel> distributorContactModelList) throws Exception {
		this.distributorLevelIdDistributorContactModelList = distributorContactModelList;
   }


   /**
    * Add the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be added.
    */
    
   public void addManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      distributorLevelModel.setRelationManagingLevelIdDistributorLevelModel(this);
      managingLevelIdDistributorLevelModelList.add(distributorLevelModel);
   }
   
   /**
    * Remove the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be removed.
    */
   
   public void removeManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {      
      distributorLevelModel.setRelationManagingLevelIdDistributorLevelModel(null);
      managingLevelIdDistributorLevelModelList.remove(distributorLevelModel);      
   }

   /**
    * Get a list of related DistributorLevelModel objects of the DistributorLevelModel object.
    * These objects are in a bidirectional one-to-many relation by the ManagingLevelId member.
    *
    * @return Collection of DistributorLevelModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationManagingLevelIdDistributorLevelModel")
   @JoinColumn(name = "MANAGING_LEVEL_ID")
   public Collection<DistributorLevelModel> getManagingLevelIdDistributorLevelModelList() throws Exception {
   		return managingLevelIdDistributorLevelModelList;
   }


   /**
    * Set a list of DistributorLevelModel related objects to the DistributorLevelModel object.
    * These objects are in a bidirectional one-to-many relation by the ManagingLevelId member.
    *
    * @param distributorLevelModelList the list of related objects.
    */
    public void setManagingLevelIdDistributorLevelModelList(Collection<DistributorLevelModel> distributorLevelModelList) throws Exception {
		this.managingLevelIdDistributorLevelModelList = distributorLevelModelList;
   }


   /**
    * Add the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be added.
    */
    
   public void addUltimateManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
      distributorLevelModel.setRelationUltimateManagingLevelIdDistributorLevelModel(this);
      ultimateManagingLevelIdDistributorLevelModelList.add(distributorLevelModel);
   }
   
   /**
    * Remove the related DistributorLevelModel to this one-to-many relation.
    *
    * @param distributorLevelModel object to be removed.
    */
   
   public void removeUltimateManagingLevelIdDistributorLevelModel(DistributorLevelModel distributorLevelModel) {      
      distributorLevelModel.setRelationUltimateManagingLevelIdDistributorLevelModel(null);
      ultimateManagingLevelIdDistributorLevelModelList.remove(distributorLevelModel);      
   }

   /**
    * Get a list of related DistributorLevelModel objects of the DistributorLevelModel object.
    * These objects are in a bidirectional one-to-many relation by the UltimateManagingLevelId member.
    *
    * @return Collection of DistributorLevelModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUltimateManagingLevelIdDistributorLevelModel")
   @JoinColumn(name = "ULTIMATE_MANAGING_LEVEL_ID")
   public Collection<DistributorLevelModel> getUltimateManagingLevelIdDistributorLevelModelList() throws Exception {
   		return ultimateManagingLevelIdDistributorLevelModelList;
   }


   /**
    * Set a list of DistributorLevelModel related objects to the DistributorLevelModel object.
    * These objects are in a bidirectional one-to-many relation by the UltimateManagingLevelId member.
    *
    * @param distributorLevelModelList the list of related objects.
    */
    public void setUltimateManagingLevelIdDistributorLevelModelList(Collection<DistributorLevelModel> distributorLevelModelList) throws Exception {
		this.ultimateManagingLevelIdDistributorLevelModelList = distributorLevelModelList;
   }



   /**
    * Returns the value of the <code>distributorLevelId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getManagingLevelId() {
      if (managingLevelIdDistributorLevelModel != null) {
         return managingLevelIdDistributorLevelModel.getDistributorLevelId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorLevelId</code> property.
    *
    * @param distributorLevelId the value for the <code>distributorLevelId</code> property
																											    */
   
   @javax.persistence.Transient
   public void setManagingLevelId(Long distributorLevelId) {
      if(distributorLevelId == null)
      {      
      	managingLevelIdDistributorLevelModel = null;
      }
      else
      {
        managingLevelIdDistributorLevelModel = new DistributorLevelModel();
      	managingLevelIdDistributorLevelModel.setDistributorLevelId(distributorLevelId);
      }      
   }

   /**
    * Returns the value of the <code>distributorLevelId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUltimateManagingLevelId() {
      if (ultimateManagingLevelIdDistributorLevelModel != null) {
         return ultimateManagingLevelIdDistributorLevelModel.getDistributorLevelId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>distributorLevelId</code> property.
    *
    * @param distributorLevelId the value for the <code>distributorLevelId</code> property
																											    */
   
   @javax.persistence.Transient
   public void setUltimateManagingLevelId(Long distributorLevelId) {
      if(distributorLevelId == null)
      {      
      	ultimateManagingLevelIdDistributorLevelModel = null;
      }
      else
      {
        ultimateManagingLevelIdDistributorLevelModel = new DistributorLevelModel();
      	ultimateManagingLevelIdDistributorLevelModel.setDistributorLevelId(distributorLevelId);
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
        checkBox += "_"+ getDistributorLevelId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&distributorLevelId=" + getDistributorLevelId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "distributorLevelId";
			return primaryKeyFieldName;				
    }
    
    /**
     * Returns the value of the <code>active</code> property.
     *
     */
       @Column(name = "IS_ACTIVE" , nullable = true )
    public boolean isActive() {
       return active;
    }

       @javax.persistence.Transient
       public boolean getActive() {
    	      return active;
    	   }
       
    /**
     * Sets the value of the <code>active</code> property.
     *
     * @param active the value for the <code>active</code> property
     *    
 		    */

    public void setActive(boolean active) {
       this.active = active;
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
    public String getRegionName() {
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
    	
    	associationModel.setClassName("DistributorLevelModel");
    	associationModel.setPropertyName("relationManagingLevelIdDistributorLevelModel");   		
   		associationModel.setValue(getRelationManagingLevelIdDistributorLevelModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DistributorLevelModel");
    	associationModel.setPropertyName("relationUltimateManagingLevelIdDistributorLevelModel");   		
   		associationModel.setValue(getRelationUltimateManagingLevelIdDistributorLevelModel());
   		
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
     
    @javax.persistence.Transient
	public boolean isDeleted() {
		return deleted;
	}

	@javax.persistence.Transient
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@javax.persistence.Transient
	public boolean isSelected() {
		return selected;
	}

	@javax.persistence.Transient
	public void setSelected(boolean selected) {
		this.selected = selected;
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
	public Integer getIndex() {
		return index;
	}
	@javax.persistence.Transient
	public void setIndex(Integer index) {
		this.index = index;
	}

	//---------------------------Regional Hierarchy FORIEGN KEY--------------------------------------------------
	   
	   
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
