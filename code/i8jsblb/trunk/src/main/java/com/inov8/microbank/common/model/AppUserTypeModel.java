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
 * The AppUserTypeModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AppUserTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_USER_TYPE_seq",sequenceName = "APP_USER_TYPE_seq", allocationSize=1)
@Table(name = "APP_USER_TYPE")
public class AppUserTypeModel extends BasePersistableModel implements Serializable {
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 3795205060904912330L;
private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<AppUserModel> appUserTypeIdAppUserModelList = new ArrayList<AppUserModel>();
   private Collection<PartnerModel> appUserTypeIdPartnerModelList = new ArrayList<PartnerModel>();
   private Collection<PermissionGroupModel> appUserTypeIdPermissionGroupModelList = new ArrayList<PermissionGroupModel>();

   private Long appUserTypeId;
   private String name;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public AppUserTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAppUserTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAppUserTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>appUserTypeId</code> property.
    *
    */
      @Column(name = "APP_USER_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_USER_TYPE_seq")
   public Long getAppUserTypeId() {
      return appUserTypeId;
   }

   /**
    * Sets the value of the <code>appUserTypeId</code> property.
    *
    * @param appUserTypeId the value for the <code>appUserTypeId</code> property
    *    
		    */

   public void setAppUserTypeId(Long appUserTypeId) {
      this.appUserTypeId = appUserTypeId;
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
    * Add the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be added.
    */
    
   public void addAppUserTypeIdAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationAppUserTypeIdAppUserTypeModel(this);
      appUserTypeIdAppUserModelList.add(appUserModel);
   }
   
   /**
    * Remove the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be removed.
    */
   
   public void removeAppUserTypeIdAppUserModel(AppUserModel appUserModel) {      
      appUserModel.setRelationAppUserTypeIdAppUserTypeModel(null);
      appUserTypeIdAppUserModelList.remove(appUserModel);      
   }

   /**
    * Get a list of related AppUserModel objects of the AppUserTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserTypeId member.
    *
    * @return Collection of AppUserModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserTypeIdAppUserTypeModel")
   @JoinColumn(name = "APP_USER_TYPE_ID")
   public Collection<AppUserModel> getAppUserTypeIdAppUserModelList() throws Exception {
   		return appUserTypeIdAppUserModelList;
   }


   /**
    * Set a list of AppUserModel related objects to the AppUserTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserTypeId member.
    *
    * @param appUserModelList the list of related objects.
    */
    public void setAppUserTypeIdAppUserModelList(Collection<AppUserModel> appUserModelList) throws Exception {
		this.appUserTypeIdAppUserModelList = appUserModelList;
   }


   /**
    * Add the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be added.
    */
    
   public void addAppUserTypeIdPartnerModel(PartnerModel partnerModel) {
      partnerModel.setRelationAppUserTypeIdAppUserTypeModel(this);
      appUserTypeIdPartnerModelList.add(partnerModel);
   }
   
   /**
    * Remove the related PartnerModel to this one-to-many relation.
    *
    * @param partnerModel object to be removed.
    */
   
   public void removeAppUserTypeIdPartnerModel(PartnerModel partnerModel) {      
      partnerModel.setRelationAppUserTypeIdAppUserTypeModel(null);
      appUserTypeIdPartnerModelList.remove(partnerModel);      
   }

   /**
    * Get a list of related PartnerModel objects of the AppUserTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserTypeId member.
    *
    * @return Collection of PartnerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserTypeIdAppUserTypeModel")
   @JoinColumn(name = "APP_USER_TYPE_ID")
   public Collection<PartnerModel> getAppUserTypeIdPartnerModelList() throws Exception {
   		return appUserTypeIdPartnerModelList;
   }


   /**
    * Set a list of PartnerModel related objects to the AppUserTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserTypeId member.
    *
    * @param partnerModelList the list of related objects.
    */
    public void setAppUserTypeIdPartnerModelList(Collection<PartnerModel> partnerModelList) throws Exception {
		this.appUserTypeIdPartnerModelList = partnerModelList;
   }


   /**
    * Add the related PermissionGroupModel to this one-to-many relation.
    *
    * @param permissionGroupModel object to be added.
    */
    
   public void addAppUserTypeIdPermissionGroupModel(PermissionGroupModel permissionGroupModel) {
      permissionGroupModel.setRelationAppUserTypeIdAppUserTypeModel(this);
      appUserTypeIdPermissionGroupModelList.add(permissionGroupModel);
   }
   
   /**
    * Remove the related PermissionGroupModel to this one-to-many relation.
    *
    * @param permissionGroupModel object to be removed.
    */
   
   public void removeAppUserTypeIdPermissionGroupModel(PermissionGroupModel permissionGroupModel) {      
      permissionGroupModel.setRelationAppUserTypeIdAppUserTypeModel(null);
      appUserTypeIdPermissionGroupModelList.remove(permissionGroupModel);      
   }

   /**
    * Get a list of related PermissionGroupModel objects of the AppUserTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserTypeId member.
    *
    * @return Collection of PermissionGroupModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAppUserTypeIdAppUserTypeModel")
   @JoinColumn(name = "APP_USER_TYPE_ID")
   public Collection<PermissionGroupModel> getAppUserTypeIdPermissionGroupModelList() throws Exception {
   		return appUserTypeIdPermissionGroupModelList;
   }


   /**
    * Set a list of PermissionGroupModel related objects to the AppUserTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AppUserTypeId member.
    *
    * @param permissionGroupModelList the list of related objects.
    */
    public void setAppUserTypeIdPermissionGroupModelList(Collection<PermissionGroupModel> permissionGroupModelList) throws Exception {
		this.appUserTypeIdPermissionGroupModelList = permissionGroupModelList;
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
        checkBox += "_"+ getAppUserTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&appUserTypeId=" + getAppUserTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "appUserTypeId";
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
