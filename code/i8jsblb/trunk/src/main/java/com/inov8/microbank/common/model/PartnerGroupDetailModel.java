package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The PartnerGroupDetailModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PartnerGroupDetailModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PARTNER_GROUP_DETAIL_SEQ",sequenceName = "PARTNER_GROUP_DETAIL_SEQ", allocationSize=1)
@Table(name = "PARTNER_GROUP_DETAIL")
public class PartnerGroupDetailModel extends BasePersistableModel implements Serializable {
  

   private UserPermissionModel userPermissionIdUserPermissionModel;
   private PartnerGroupModel partnerGroupIdPartnerGroupModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;


   private Long partnerGroupDetailId;
   private Boolean readAllowed;
   private Boolean updateAllowed;
   private Boolean deleteAllowed;
   private Boolean createAllowed;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public PartnerGroupDetailModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPartnerGroupDetailId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPartnerGroupDetailId(primaryKey);
    }

   /**
    * Returns the value of the <code>partnerGroupDetailId</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_DETAIL_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARTNER_GROUP_DETAIL_SEQ")
   public Long getPartnerGroupDetailId() {
      return partnerGroupDetailId;
   }

   /**
    * Sets the value of the <code>partnerGroupDetailId</code> property.
    *
    * @param partnerGroupDetailId the value for the <code>partnerGroupDetailId</code> property
    *    
		    */

   public void setPartnerGroupDetailId(Long partnerGroupDetailId) {
      this.partnerGroupDetailId = partnerGroupDetailId;
   }

   /**
    * Returns the value of the <code>readAllowed</code> property.
    *
    */
      @Column(name = "IS_READ_ALLOWED" , nullable = false )
   public Boolean getReadAllowed() {
      return readAllowed;
   }

   /**
    * Sets the value of the <code>readAllowed</code> property.
    *
    * @param readAllowed the value for the <code>readAllowed</code> property
    *    
		    */

   public void setReadAllowed(Boolean readAllowed) {
      this.readAllowed = readAllowed;
   }

   /**
    * Returns the value of the <code>updateAllowed</code> property.
    *
    */
      @Column(name = "IS_UPDATE_ALLOWED" , nullable = false )
   public Boolean getUpdateAllowed() {
      return updateAllowed;
   }

   /**
    * Sets the value of the <code>updateAllowed</code> property.
    *
    * @param updateAllowed the value for the <code>updateAllowed</code> property
    *    
		    */

   public void setUpdateAllowed(Boolean updateAllowed) {
      this.updateAllowed = updateAllowed;
   }

   /**
    * Returns the value of the <code>deleteAllowed</code> property.
    *
    */
      @Column(name = "IS_DELETE_ALLOWED" , nullable = false )
   public Boolean getDeleteAllowed() {
      return deleteAllowed;
   }

   /**
    * Sets the value of the <code>deleteAllowed</code> property.
    *
    * @param deleteAllowed the value for the <code>deleteAllowed</code> property
    *    
		    */

   public void setDeleteAllowed(Boolean deleteAllowed) {
      this.deleteAllowed = deleteAllowed;
   }

   /**
    * Returns the value of the <code>createAllowed</code> property.
    *
    */
      @Column(name = "IS_CREATE_ALLOWED" , nullable = false )
   public Boolean getCreateAllowed() {
      return createAllowed;
   }

   /**
    * Sets the value of the <code>createAllowed</code> property.
    *
    * @param createAllowed the value for the <code>createAllowed</code> property
    *    
		    */

   public void setCreateAllowed(Boolean createAllowed) {
      this.createAllowed = createAllowed;
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
    * Returns the value of the <code>userPermissionIdUserPermissionModel</code> relation property.
    *
    * @return the value of the <code>userPermissionIdUserPermissionModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "USER_PERMISSION_ID")    
   public UserPermissionModel getRelationUserPermissionIdUserPermissionModel(){
      return userPermissionIdUserPermissionModel;
   }
    
   /**
    * Returns the value of the <code>userPermissionIdUserPermissionModel</code> relation property.
    *
    * @return the value of the <code>userPermissionIdUserPermissionModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public UserPermissionModel getUserPermissionIdUserPermissionModel(){
      return getRelationUserPermissionIdUserPermissionModel();
   }

   /**
    * Sets the value of the <code>userPermissionIdUserPermissionModel</code> relation property.
    *
    * @param userPermissionModel a value for <code>userPermissionIdUserPermissionModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUserPermissionIdUserPermissionModel(UserPermissionModel userPermissionModel) {
      this.userPermissionIdUserPermissionModel = userPermissionModel;
   }
   
   /**
    * Sets the value of the <code>userPermissionIdUserPermissionModel</code> relation property.
    *
    * @param userPermissionModel a value for <code>userPermissionIdUserPermissionModel</code>.
    */
   @javax.persistence.Transient
   public void setUserPermissionIdUserPermissionModel(UserPermissionModel userPermissionModel) {
      if(null != userPermissionModel)
      {
      	setRelationUserPermissionIdUserPermissionModel((UserPermissionModel)userPermissionModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>partnerGroupIdPartnerGroupModel</code> relation property.
    *
    * @return the value of the <code>partnerGroupIdPartnerGroupModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PARTNER_GROUP_ID")    
   public PartnerGroupModel getRelationPartnerGroupIdPartnerGroupModel(){
      return partnerGroupIdPartnerGroupModel;
   }
    
   /**
    * Returns the value of the <code>partnerGroupIdPartnerGroupModel</code> relation property.
    *
    * @return the value of the <code>partnerGroupIdPartnerGroupModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public PartnerGroupModel getPartnerGroupIdPartnerGroupModel(){
      return getRelationPartnerGroupIdPartnerGroupModel();
   }

   /**
    * Sets the value of the <code>partnerGroupIdPartnerGroupModel</code> relation property.
    *
    * @param partnerGroupModel a value for <code>partnerGroupIdPartnerGroupModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPartnerGroupIdPartnerGroupModel(PartnerGroupModel partnerGroupModel) {
      this.partnerGroupIdPartnerGroupModel = partnerGroupModel;
   }
   
   /**
    * Sets the value of the <code>partnerGroupIdPartnerGroupModel</code> relation property.
    *
    * @param partnerGroupModel a value for <code>partnerGroupIdPartnerGroupModel</code>.
    */
   @javax.persistence.Transient
   public void setPartnerGroupIdPartnerGroupModel(PartnerGroupModel partnerGroupModel) {
      if(null != partnerGroupModel)
      {
      	setRelationPartnerGroupIdPartnerGroupModel((PartnerGroupModel)partnerGroupModel.clone());
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
    * Returns the value of the <code>userPermissionId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUserPermissionId() {
      if (userPermissionIdUserPermissionModel != null) {
         return userPermissionIdUserPermissionModel.getUserPermissionId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>userPermissionId</code> property.
    *
    * @param userPermissionId the value for the <code>userPermissionId</code> property
					    * @spring.validator type="required"
																									    */
   
   @javax.persistence.Transient
   public void setUserPermissionId(Long userPermissionId) {
      if(userPermissionId == null)
      {      
      	userPermissionIdUserPermissionModel = null;
      }
      else
      {
        userPermissionIdUserPermissionModel = new UserPermissionModel();
      	userPermissionIdUserPermissionModel.setUserPermissionId(userPermissionId);
      }      
   }

   /**
    * Returns the value of the <code>partnerGroupId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPartnerGroupId() {
      if (partnerGroupIdPartnerGroupModel != null) {
         return partnerGroupIdPartnerGroupModel.getPartnerGroupId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>partnerGroupId</code> property.
    *
    * @param partnerGroupId the value for the <code>partnerGroupId</code> property
							    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setPartnerGroupId(Long partnerGroupId) {
      if(partnerGroupId == null)
      {      
      	partnerGroupIdPartnerGroupModel = null;
      }
      else
      {
        partnerGroupIdPartnerGroupModel = new PartnerGroupModel();
      	partnerGroupIdPartnerGroupModel.setPartnerGroupId(partnerGroupId);
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
        checkBox += "_"+ getPartnerGroupDetailId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&partnerGroupDetailId=" + getPartnerGroupDetailId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "partnerGroupDetailId";
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
    	
    	associationModel.setClassName("UserPermissionModel");
    	associationModel.setPropertyName("relationUserPermissionIdUserPermissionModel");   		
   		associationModel.setValue(getRelationUserPermissionIdUserPermissionModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PartnerGroupModel");
    	associationModel.setPropertyName("relationPartnerGroupIdPartnerGroupModel");   		
   		associationModel.setValue(getRelationPartnerGroupIdPartnerGroupModel());
   		
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
