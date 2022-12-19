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
 * The UserPermissionModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UserPermissionModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "USER_PERMISSION_seq",sequenceName = "USER_PERMISSION_seq", allocationSize=1)
@Table(name = "USER_PERMISSION")
public class UserPermissionModel extends BasePersistableModel implements Serializable {
  

   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<PartnerGroupDetailModel> userPermissionIdPartnerGroupDetailModelList = new ArrayList<PartnerGroupDetailModel>();
   private Collection<PermissionGroupDetailModel> userPermissionIdPermissionGroupDetailModelList = new ArrayList<PermissionGroupDetailModel>();

   private Long userPermissionId;
   private String name;
   private Boolean readAvailable;
   private Boolean updateAvailable;
   private Boolean deleteAvailable;
   private Boolean createAvailable;
   private String description;
   private String comments;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Boolean isDefault;
   private String shortName;

   /**
    * Default constructor.
    */
   public UserPermissionModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUserPermissionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUserPermissionId(primaryKey);
    }

   /**
    * Returns the value of the <code>userPermissionId</code> property.
    *
    */
      @Column(name = "USER_PERMISSION_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_PERMISSION_seq")
   public Long getUserPermissionId() {
      return userPermissionId;
   }

   /**
    * Sets the value of the <code>userPermissionId</code> property.
    *
    * @param userPermissionId the value for the <code>userPermissionId</code> property
    *    
		    */

   public void setUserPermissionId(Long userPermissionId) {
      this.userPermissionId = userPermissionId;
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
    * Returns the value of the <code>readAvailable</code> property.
    *
    */
      @Column(name = "IS_READ_AVAILABLE" , nullable = false )
   public Boolean getReadAvailable() {
      return readAvailable;
   }

   /**
    * Sets the value of the <code>readAvailable</code> property.
    *
    * @param readAvailable the value for the <code>readAvailable</code> property
    *    
		    */

   public void setReadAvailable(Boolean readAvailable) {
      this.readAvailable = readAvailable;
   }

   /**
    * Returns the value of the <code>updateAvailable</code> property.
    *
    */
      @Column(name = "IS_UPDATE_AVAILABLE" , nullable = false )
   public Boolean getUpdateAvailable() {
      return updateAvailable;
   }

   /**
    * Sets the value of the <code>updateAvailable</code> property.
    *
    * @param updateAvailable the value for the <code>updateAvailable</code> property
    *    
		    */

   public void setUpdateAvailable(Boolean updateAvailable) {
      this.updateAvailable = updateAvailable;
   }

   /**
    * Returns the value of the <code>deleteAvailable</code> property.
    *
    */
      @Column(name = "IS_DELETE_AVAILABLE" , nullable = false )
   public Boolean getDeleteAvailable() {
      return deleteAvailable;
   }

   /**
    * Sets the value of the <code>deleteAvailable</code> property.
    *
    * @param deleteAvailable the value for the <code>deleteAvailable</code> property
    *    
		    */

   public void setDeleteAvailable(Boolean deleteAvailable) {
      this.deleteAvailable = deleteAvailable;
   }

   /**
    * Returns the value of the <code>createAvailable</code> property.
    *
    */
      @Column(name = "IS_CREATE_AVAILABLE" , nullable = false )
   public Boolean getCreateAvailable() {
      return createAvailable;
   }

   /**
    * Sets the value of the <code>createAvailable</code> property.
    *
    * @param createAvailable the value for the <code>createAvailable</code> property
    *    
		    */

   public void setCreateAvailable(Boolean createAvailable) {
      this.createAvailable = createAvailable;
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
    * Add the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be added.
    */
    
   public void addUserPermissionIdPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {
      partnerGroupDetailModel.setRelationUserPermissionIdUserPermissionModel(this);
      userPermissionIdPartnerGroupDetailModelList.add(partnerGroupDetailModel);
   }
   
   /**
    * Remove the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be removed.
    */
   
   public void removeUserPermissionIdPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {      
      partnerGroupDetailModel.setRelationUserPermissionIdUserPermissionModel(null);
      userPermissionIdPartnerGroupDetailModelList.remove(partnerGroupDetailModel);      
   }

   /**
    * Get a list of related PartnerGroupDetailModel objects of the UserPermissionModel object.
    * These objects are in a bidirectional one-to-many relation by the UserPermissionId member.
    *
    * @return Collection of PartnerGroupDetailModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUserPermissionIdUserPermissionModel")
   @JoinColumn(name = "USER_PERMISSION_ID")
   public Collection<PartnerGroupDetailModel> getUserPermissionIdPartnerGroupDetailModelList() throws Exception {
   		return userPermissionIdPartnerGroupDetailModelList;
   }


   /**
    * Set a list of PartnerGroupDetailModel related objects to the UserPermissionModel object.
    * These objects are in a bidirectional one-to-many relation by the UserPermissionId member.
    *
    * @param partnerGroupDetailModelList the list of related objects.
    */
    public void setUserPermissionIdPartnerGroupDetailModelList(Collection<PartnerGroupDetailModel> partnerGroupDetailModelList) throws Exception {
		this.userPermissionIdPartnerGroupDetailModelList = partnerGroupDetailModelList;
   }


   /**
    * Add the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be added.
    */
    
   public void addUserPermissionIdPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {
      permissionGroupDetailModel.setRelationUserPermissionIdUserPermissionModel(this);
      userPermissionIdPermissionGroupDetailModelList.add(permissionGroupDetailModel);
   }
   
   /**
    * Remove the related PermissionGroupDetailModel to this one-to-many relation.
    *
    * @param permissionGroupDetailModel object to be removed.
    */
   
   public void removeUserPermissionIdPermissionGroupDetailModel(PermissionGroupDetailModel permissionGroupDetailModel) {      
      permissionGroupDetailModel.setRelationUserPermissionIdUserPermissionModel(null);
      userPermissionIdPermissionGroupDetailModelList.remove(permissionGroupDetailModel);      
   }

   /**
    * Get a list of related PermissionGroupDetailModel objects of the UserPermissionModel object.
    * These objects are in a bidirectional one-to-many relation by the UserPermissionId member.
    *
    * @return Collection of PermissionGroupDetailModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUserPermissionIdUserPermissionModel")
   @JoinColumn(name = "USER_PERMISSION_ID")
   public Collection<PermissionGroupDetailModel> getUserPermissionIdPermissionGroupDetailModelList() throws Exception {
   		return userPermissionIdPermissionGroupDetailModelList;
   }


   /**
    * Set a list of PermissionGroupDetailModel related objects to the UserPermissionModel object.
    * These objects are in a bidirectional one-to-many relation by the UserPermissionId member.
    *
    * @param permissionGroupDetailModelList the list of related objects.
    */
    public void setUserPermissionIdPermissionGroupDetailModelList(Collection<PermissionGroupDetailModel> permissionGroupDetailModelList) throws Exception {
		this.userPermissionIdPermissionGroupDetailModelList = permissionGroupDetailModelList;
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
        checkBox += "_"+ getUserPermissionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&userPermissionId=" + getUserPermissionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "userPermissionId";
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

    @Column(name = "IS_DEFAULT")
    public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

    @Column(name = "SHORT_NAME")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}    
          
}
