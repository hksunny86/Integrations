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
 * The PartnerPermissionGroupModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PartnerPermissionGroupModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PARTNER_PERMISSION_GROUP_seq",sequenceName = "PARTNER_PERMISSION_GROUP_seq", allocationSize=1)
@Table(name = "PARTNER_PERMISSION_GROUP")
public class PartnerPermissionGroupModel extends BasePersistableModel implements Serializable {
  

   private PermissionGroupModel permissionGroupIdPermissionGroupModel;
   private PartnerModel partnerIdPartnerModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;


   private Long partnerPermissionGroupId;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public PartnerPermissionGroupModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPartnerPermissionGroupId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPartnerPermissionGroupId(primaryKey);
    }

   /**
    * Returns the value of the <code>partnerPermissionGroupId</code> property.
    *
    */
      @Column(name = "PARTNER_PERMISSION_GROUP_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARTNER_PERMISSION_GROUP_seq")
   public Long getPartnerPermissionGroupId() {
      return partnerPermissionGroupId;
   }

   /**
    * Sets the value of the <code>partnerPermissionGroupId</code> property.
    *
    * @param partnerPermissionGroupId the value for the <code>partnerPermissionGroupId</code> property
    *    
		    */

   public void setPartnerPermissionGroupId(Long partnerPermissionGroupId) {
      this.partnerPermissionGroupId = partnerPermissionGroupId;
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
    * Returns the value of the <code>permissionGroupIdPermissionGroupModel</code> relation property.
    *
    * @return the value of the <code>permissionGroupIdPermissionGroupModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PERMISSION_GROUP_ID")    
   public PermissionGroupModel getRelationPermissionGroupIdPermissionGroupModel(){
      return permissionGroupIdPermissionGroupModel;
   }
    
   /**
    * Returns the value of the <code>permissionGroupIdPermissionGroupModel</code> relation property.
    *
    * @return the value of the <code>permissionGroupIdPermissionGroupModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public PermissionGroupModel getPermissionGroupIdPermissionGroupModel(){
      return getRelationPermissionGroupIdPermissionGroupModel();
   }

   /**
    * Sets the value of the <code>permissionGroupIdPermissionGroupModel</code> relation property.
    *
    * @param permissionGroupModel a value for <code>permissionGroupIdPermissionGroupModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPermissionGroupIdPermissionGroupModel(PermissionGroupModel permissionGroupModel) {
      this.permissionGroupIdPermissionGroupModel = permissionGroupModel;
   }
   
   /**
    * Sets the value of the <code>permissionGroupIdPermissionGroupModel</code> relation property.
    *
    * @param permissionGroupModel a value for <code>permissionGroupIdPermissionGroupModel</code>.
    */
   @javax.persistence.Transient
   public void setPermissionGroupIdPermissionGroupModel(PermissionGroupModel permissionGroupModel) {
      if(null != permissionGroupModel)
      {
      	setRelationPermissionGroupIdPermissionGroupModel((PermissionGroupModel)permissionGroupModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>partnerIdPartnerModel</code> relation property.
    *
    * @return the value of the <code>partnerIdPartnerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PARTNER_ID")    
   public PartnerModel getRelationPartnerIdPartnerModel(){
      return partnerIdPartnerModel;
   }
    
   /**
    * Returns the value of the <code>partnerIdPartnerModel</code> relation property.
    *
    * @return the value of the <code>partnerIdPartnerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public PartnerModel getPartnerIdPartnerModel(){
      return getRelationPartnerIdPartnerModel();
   }

   /**
    * Sets the value of the <code>partnerIdPartnerModel</code> relation property.
    *
    * @param partnerModel a value for <code>partnerIdPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationPartnerIdPartnerModel(PartnerModel partnerModel) {
      this.partnerIdPartnerModel = partnerModel;
   }
   
   /**
    * Sets the value of the <code>partnerIdPartnerModel</code> relation property.
    *
    * @param partnerModel a value for <code>partnerIdPartnerModel</code>.
    */
   @javax.persistence.Transient
   public void setPartnerIdPartnerModel(PartnerModel partnerModel) {
      if(null != partnerModel)
      {
      	setRelationPartnerIdPartnerModel((PartnerModel)partnerModel.clone());
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
    * Returns the value of the <code>permissionGroupId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPermissionGroupId() {
      if (permissionGroupIdPermissionGroupModel != null) {
         return permissionGroupIdPermissionGroupModel.getPermissionGroupId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>permissionGroupId</code> property.
    *
    * @param permissionGroupId the value for the <code>permissionGroupId</code> property
							    * @spring.validator type="required"
													    */
   
   @javax.persistence.Transient
   public void setPermissionGroupId(Long permissionGroupId) {
      if(permissionGroupId == null)
      {      
      	permissionGroupIdPermissionGroupModel = null;
      }
      else
      {
        permissionGroupIdPermissionGroupModel = new PermissionGroupModel();
      	permissionGroupIdPermissionGroupModel.setPermissionGroupId(permissionGroupId);
      }      
   }

   /**
    * Returns the value of the <code>partnerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getPartnerId() {
      if (partnerIdPartnerModel != null) {
         return partnerIdPartnerModel.getPartnerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>partnerId</code> property.
    *
    * @param partnerId the value for the <code>partnerId</code> property
					    * @spring.validator type="required"
															    */
   
   @javax.persistence.Transient
   public void setPartnerId(Long partnerId) {
      if(partnerId == null)
      {      
      	partnerIdPartnerModel = null;
      }
      else
      {
        partnerIdPartnerModel = new PartnerModel();
      	partnerIdPartnerModel.setPartnerId(partnerId);
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
        checkBox += "_"+ getPartnerPermissionGroupId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&partnerPermissionGroupId=" + getPartnerPermissionGroupId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "partnerPermissionGroupId";
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
    	
    	associationModel.setClassName("PermissionGroupModel");
    	associationModel.setPropertyName("relationPermissionGroupIdPermissionGroupModel");   		
   		associationModel.setValue(getRelationPermissionGroupIdPermissionGroupModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("PartnerModel");
    	associationModel.setPropertyName("relationPartnerIdPartnerModel");   		
   		associationModel.setValue(getRelationPartnerIdPartnerModel());
   		
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
