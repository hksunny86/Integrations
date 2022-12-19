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
 * The PartnerGroupModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PartnerGroupModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PARTNER_GROUP_seq",sequenceName = "PARTNER_GROUP_seq", allocationSize=1)
@Table(name = "PARTNER_GROUP")
public class PartnerGroupModel extends BasePersistableModel implements Serializable {
  

   private PartnerModel partnerIdPartnerModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<AppUserPartnerGroupModel> partnerGroupIdAppUserPartnerGroupModelList = new ArrayList<AppUserPartnerGroupModel>();
   private Collection<PartnerGroupDetailModel> partnerGroupIdPartnerGroupDetailModelList = new ArrayList<PartnerGroupDetailModel>();

   private Long partnerGroupId;
   private String name;
   private String description;
   private Boolean active;
   private Boolean editable;
   private String email;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public PartnerGroupModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPartnerGroupId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPartnerGroupId(primaryKey);
    }

   /**
    * Returns the value of the <code>partnerGroupId</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARTNER_GROUP_seq")
   public Long getPartnerGroupId() {
      return partnerGroupId;
   }

   /**
    * Sets the value of the <code>partnerGroupId</code> property.
    *
    * @param partnerGroupId the value for the <code>partnerGroupId</code> property
    *    
		    */

   public void setPartnerGroupId(Long partnerGroupId) {
      this.partnerGroupId = partnerGroupId;
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
    * Returns the value of the <code>editable</code> property.
    *
    */
      @Column(name = "IS_EDITABLE" , nullable = false )
   public Boolean getEditable() {
      return editable;
   }

   /**
    * Sets the value of the <code>editable</code> property.
    *
    * @param editable the value for the <code>editable</code> property
    *    
		    */

   public void setEditable(Boolean editable) {
      this.editable = editable;
   }

   /**
    * Returns the value of the <code>email</code> property.
    *
    */
      @Column(name = "EMAIL"  , length=100 )
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
    * @spring.validator-var name="maxlength" value="100"
    */

   public void setEmail(String email) {
      this.email = email;
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
    * Add the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be added.
    */
    
   public void addPartnerGroupIdAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {
      appUserPartnerGroupModel.setRelationPartnerGroupIdPartnerGroupModel(this);
      partnerGroupIdAppUserPartnerGroupModelList.add(appUserPartnerGroupModel);
   }
   
   /**
    * Remove the related AppUserPartnerGroupModel to this one-to-many relation.
    *
    * @param appUserPartnerGroupModel object to be removed.
    */
   
   public void removePartnerGroupIdAppUserPartnerGroupModel(AppUserPartnerGroupModel appUserPartnerGroupModel) {      
      appUserPartnerGroupModel.setRelationPartnerGroupIdPartnerGroupModel(null);
      partnerGroupIdAppUserPartnerGroupModelList.remove(appUserPartnerGroupModel);      
   }

   /**
    * Get a list of related AppUserPartnerGroupModel objects of the PartnerGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerGroupId member.
    *
    * @return Collection of AppUserPartnerGroupModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPartnerGroupIdPartnerGroupModel")
   @JoinColumn(name = "PARTNER_GROUP_ID")
   public Collection<AppUserPartnerGroupModel> getPartnerGroupIdAppUserPartnerGroupModelList() throws Exception {
   		return partnerGroupIdAppUserPartnerGroupModelList;
   }


   /**
    * Set a list of AppUserPartnerGroupModel related objects to the PartnerGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerGroupId member.
    *
    * @param appUserPartnerGroupModelList the list of related objects.
    */
    public void setPartnerGroupIdAppUserPartnerGroupModelList(Collection<AppUserPartnerGroupModel> appUserPartnerGroupModelList) throws Exception {
		this.partnerGroupIdAppUserPartnerGroupModelList = appUserPartnerGroupModelList;
   }


   /**
    * Add the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be added.
    */
    
   public void addPartnerGroupIdPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {
      partnerGroupDetailModel.setRelationPartnerGroupIdPartnerGroupModel(this);
      partnerGroupIdPartnerGroupDetailModelList.add(partnerGroupDetailModel);
   }
   
   /**
    * Remove the related PartnerGroupDetailModel to this one-to-many relation.
    *
    * @param partnerGroupDetailModel object to be removed.
    */
   
   public void removePartnerGroupIdPartnerGroupDetailModel(PartnerGroupDetailModel partnerGroupDetailModel) {      
      partnerGroupDetailModel.setRelationPartnerGroupIdPartnerGroupModel(null);
      partnerGroupIdPartnerGroupDetailModelList.remove(partnerGroupDetailModel);      
   }

   /**
    * Get a list of related PartnerGroupDetailModel objects of the PartnerGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerGroupId member.
    *
    * @return Collection of PartnerGroupDetailModel objects.
    *
    */
   
   //@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPartnerGroupIdPartnerGroupModel")
   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
              fetch = FetchType.EAGER,mappedBy = "relationPartnerGroupIdPartnerGroupModel")
   @JoinColumn(name = "PARTNER_GROUP_ID")
   @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                                      org.hibernate.annotations.CascadeType.PERSIST,
                                      org.hibernate.annotations.CascadeType.MERGE,
                                      org.hibernate.annotations.CascadeType.REFRESH})
   public Collection<PartnerGroupDetailModel> getPartnerGroupIdPartnerGroupDetailModelList() throws Exception {
   		return partnerGroupIdPartnerGroupDetailModelList;
   }


   /**
    * Set a list of PartnerGroupDetailModel related objects to the PartnerGroupModel object.
    * These objects are in a bidirectional one-to-many relation by the PartnerGroupId member.
    *
    * @param partnerGroupDetailModelList the list of related objects.
    */
    public void setPartnerGroupIdPartnerGroupDetailModelList(Collection<PartnerGroupDetailModel> partnerGroupDetailModelList) throws Exception {
		this.partnerGroupIdPartnerGroupDetailModelList = partnerGroupDetailModelList;
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
        checkBox += "_"+ getPartnerGroupId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&partnerGroupId=" + getPartnerGroupId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "partnerGroupId";
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
