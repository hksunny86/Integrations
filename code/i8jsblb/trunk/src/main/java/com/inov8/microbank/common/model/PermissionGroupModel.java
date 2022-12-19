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
 * The PermissionGroupModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PermissionGroupModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PERMISSION_GROUP_seq",sequenceName = "PERMISSION_GROUP_seq", allocationSize=1)
@Table(name = "PERMISSION_GROUP")
public class PermissionGroupModel extends BasePersistableModel implements Serializable {
  

   private AppUserTypeModel appUserTypeIdAppUserTypeModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<PartnerPermissionGroupModel> permissionGroupIdPartnerPermissionGroupModelList = new ArrayList<PartnerPermissionGroupModel>();
   private Collection<PermissionGroupDetailModel> permissionGroupIdPermissionGroupDetailModelList = new ArrayList<PermissionGroupDetailModel>();

   private Long permissionGroupId;
   private String name;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public PermissionGroupModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPermissionGroupId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPermissionGroupId(primaryKey);
    }

   /**
    * Returns the value of the <code>permissionGroupId</code> property.
    *
    */
      @Column(name = "PERMISSION_GROUP_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PERMISSION_GROUP_seq")
   public Long getPermissionGroupId() {
      return permissionGroupId;
   }

   /**
    * Sets the value of the <code>permissionGroupId</code> property.
    *
    * @param permissionGroupId the value for the <code>permissionGroupId</code> property
    *    
		    */

   public void setPermissionGroupId(Long permissionGroupId) {
      this.permissionGroupId = permissionGroupId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=100 )
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
    * @spring.validator-var name="maxlength" value="100"
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
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_USER_TYPE_ID")    
   public AppUserTypeModel getRelationAppUserTypeIdAppUserTypeModel(){
      return appUserTypeIdAppUserTypeModel;
   }
    
   /**
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserTypeModel getAppUserTypeIdAppUserTypeModel(){
      return getRelationAppUserTypeIdAppUserTypeModel();
   }

   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      this.appUserTypeIdAppUserTypeModel = appUserTypeModel;
   }
   
   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      if(null != appUserTypeModel)
      {
      	setRelationAppUserTypeIdAppUserTypeModel((AppUserTypeModel)appUserTypeModel.clone());
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
    * Add the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be added.
    */
    
   public void addPermissionGroupIdPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {
      partnerPermissionGroupModel.setRelationPermissionGroupIdPermissionGroupModel(this);
      permissionGroupIdPartnerPermissionGroupModelList.add(partnerPermissionGroupModel);
   }
   
   /**
    * Remove the related PartnerPermissionGroupModel to this one-to-many relation.
    *
    * @param partnerPermissionGroupModel object to be removed.
    */
   
   public void removePermissionGroupIdPartnerPermissionGroupModel(PartnerPermissionGroupModel partnerPermissionGroupModel) {      
      partnerPermissionGroupModel.setRelationPermissionGroupIdPermissionGroupModel(null);
      permissionGroupIdPartnerPermissionGroupModelList.remove(partnerPermissionGroupModel);      
   }

   /**
    * Get a list of related PartnerPermissionGroupModel objects of the PermissionGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PermissionGroupId member.
    *
    * @return Collection of PartnerPermissionGroupModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPermissionGroupIdPermissionGroupModel")
   @JoinColumn(name = "PERMISSION_GROUP_ID")
   public Collection<PartnerPermissionGroupModel> getPermissionGroupIdPartnerPermissionGroupModelList() throws Exception {
   		return permissionGroupIdPartnerPermissionGroupModelList;
   }


   /**
    * Set a list of PartnerPermissionGroupModel related objects to the PermissionGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PermissionGroupId member.
    *
    * @param partnerPermissionGroupModelList the list of related objects.
    */
    public void setPermissionGroupIdPartnerPermissionGroupModelList(Collection<PartnerPermissionGroupModel> partnerPermissionGroupModelList) throws Exception {
		this.permissionGroupIdPartnerPermissionGroupModelList = partnerPermissionGroupModelList;
   }


   /**
    * Add the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be added.
    */
    
   public void addPermissionGroupIdPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {
      permissionGroupDetailModel.setRelationPermissionGroupIdPermissionGroupModel(this);
      permissionGroupIdPermissionGroupDetailModelList.add(permissionGroupDetailModel);
   }
   
   /**
    * Remove the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be removed.
    */
   
   public void removePermissionGroupIdPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {      
      permissionGroupDetailModel.setRelationPermissionGroupIdPermissionGroupModel(null);
      permissionGroupIdPermissionGroupDetailModelList.remove(permissionGroupDetailModel);      
   }

   /**
    * Get a list of related PermissionGroupDetailModel objects of the PermissionGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PermissionGroupId member.
    *
    * @return Collection of PermissionGroupDetailModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPermissionGroupIdPermissionGroupModel")
   @JoinColumn(name = "PERMISSION_GROUP_ID")
   public Collection<PermissionGroupDetailModel> getPermissionGroupIdPermissionGroupDetailModelList() throws Exception {
   		return permissionGroupIdPermissionGroupDetailModelList;
   }


   /**
    * Set a list of PermissionGroupDetailModel related objects to the PermissionGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PermissionGroupId member.
    *
    * @param permissionGroupDetailModelList the list of related objects.
    */
    public void setPermissionGroupIdPermissionGroupDetailModelList(Collection<PermissionGroupDetailModel> permissionGroupDetailModelList) throws Exception {
		this.permissionGroupIdPermissionGroupDetailModelList = permissionGroupDetailModelList;
   }



   /**
    * Returns the value of the <code>appUserTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAppUserTypeId() {
      if (appUserTypeIdAppUserTypeModel != null) {
         return appUserTypeIdAppUserTypeModel.getAppUserTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserTypeId</code> property.
    *
    * @param appUserTypeId the value for the <code>appUserTypeId</code> property
					    * @spring.validator type="required"
																			    */
   
   @javax.persistence.Transient
   public void setAppUserTypeId(Long appUserTypeId) {
      if(appUserTypeId == null)
      {      
      	appUserTypeIdAppUserTypeModel = null;
      }
      else
      {
        appUserTypeIdAppUserTypeModel = new AppUserTypeModel();
      	appUserTypeIdAppUserTypeModel.setAppUserTypeId(appUserTypeId);
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
        checkBox += "_"+ getPermissionGroupId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&permissionGroupId=" + getPermissionGroupId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "permissionGroupId";
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
    	
    	associationModel.setClassName("AppUserTypeModel");
    	associationModel.setPropertyName("relationAppUserTypeIdAppUserTypeModel");   		
   		associationModel.setValue(getRelationAppUserTypeIdAppUserTypeModel());
   		
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
