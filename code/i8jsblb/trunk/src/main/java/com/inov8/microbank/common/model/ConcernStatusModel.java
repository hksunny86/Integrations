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
 * The ConcernStatusModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernStatusModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CONCERN_STATUS_seq",sequenceName = "CONCERN_STATUS_seq", allocationSize=1)
@Table(name = "CONCERN_STATUS")
public class ConcernStatusModel extends BasePersistableModel implements Serializable{
  

   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<ConcernModel> concernStatusIdConcernModelList = new ArrayList<ConcernModel>();
   private Collection<ConcernHistoryModel> recipientConcernStatusIdConcernHistoryModelList = new ArrayList<ConcernHistoryModel>();
   private Collection<ConcernHistoryModel> initiatorConcernStatusIdConcernHistoryModelList = new ArrayList<ConcernHistoryModel>();

   private Long concernStatusId;
   private String name;
   private String description;
   private String comments;
   private Integer versionNo;
   private Date createdOn;
   private Date updatedOn;

   /**
    * Default constructor.
    */
   public ConcernStatusModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getConcernStatusId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setConcernStatusId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernStatusId</code> property.
    *
    */
      @Column(name = "CONCERN_STATUS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONCERN_STATUS_seq")
   public Long getConcernStatusId() {
      return concernStatusId;
   }

   /**
    * Sets the value of the <code>concernStatusId</code> property.
    *
    * @param concernStatusId the value for the <code>concernStatusId</code> property
    *    
		    */

   public void setConcernStatusId(Long concernStatusId) {
      this.concernStatusId = concernStatusId;
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
    * Add the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be added.
    */
    
   public void addConcernStatusIdConcernModel(ConcernModel concernModel) {
      concernModel.setRelationConcernStatusIdConcernStatusModel(this);
      concernStatusIdConcernModelList.add(concernModel);
   }
   
   /**
    * Remove the related ConcernModel to this one-to-many relation.
    *
    * @param concernModel object to be removed.
    */
   
   public void removeConcernStatusIdConcernModel(ConcernModel concernModel) {      
      concernModel.setRelationConcernStatusIdConcernStatusModel(null);
      concernStatusIdConcernModelList.remove(concernModel);      
   }

   /**
    * Get a list of related ConcernModel objects of the ConcernStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the ConcernStatusId member.
    *
    * @return Collection of ConcernModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationConcernStatusIdConcernStatusModel")
   @JoinColumn(name = "CONCERN_STATUS_ID")
   public Collection<ConcernModel> getConcernStatusIdConcernModelList() throws Exception {
   		return concernStatusIdConcernModelList;
   }


   /**
    * Set a list of ConcernModel related objects to the ConcernStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the ConcernStatusId member.
    *
    * @param concernModelList the list of related objects.
    */
    public void setConcernStatusIdConcernModelList(Collection<ConcernModel> concernModelList) throws Exception {
		this.concernStatusIdConcernModelList = concernModelList;
   }


   /**
    * Add the related ConcernHistoryModel to this one-to-many relation.
    *
    * @param concernHistoryModel object to be added.
    */
    
   public void addRecipientConcernStatusIdConcernHistoryModel(ConcernHistoryModel concernHistoryModel) {
      concernHistoryModel.setRelationRecipientConcernStatusIdConcernStatusModel(this);
      recipientConcernStatusIdConcernHistoryModelList.add(concernHistoryModel);
   }
   
   /**
    * Remove the related ConcernHistoryModel to this one-to-many relation.
    *
    * @param concernHistoryModel object to be removed.
    */
   
   public void removeRecipientConcernStatusIdConcernHistoryModel(ConcernHistoryModel concernHistoryModel) {      
      concernHistoryModel.setRelationRecipientConcernStatusIdConcernStatusModel(null);
      recipientConcernStatusIdConcernHistoryModelList.remove(concernHistoryModel);      
   }

   /**
    * Get a list of related ConcernHistoryModel objects of the ConcernStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the RecipientConcernStatusId member.
    *
    * @return Collection of ConcernHistoryModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationRecipientConcernStatusIdConcernStatusModel")
   @JoinColumn(name = "RECIPIENT_CONCERN_STATUS_ID")
   public Collection<ConcernHistoryModel> getRecipientConcernStatusIdConcernHistoryModelList() throws Exception {
   		return recipientConcernStatusIdConcernHistoryModelList;
   }


   /**
    * Set a list of ConcernHistoryModel related objects to the ConcernStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the RecipientConcernStatusId member.
    *
    * @param concernHistoryModelList the list of related objects.
    */
    public void setRecipientConcernStatusIdConcernHistoryModelList(Collection<ConcernHistoryModel> concernHistoryModelList) throws Exception {
		this.recipientConcernStatusIdConcernHistoryModelList = concernHistoryModelList;
   }


   /**
    * Add the related ConcernHistoryModel to this one-to-many relation.
    *
    * @param concernHistoryModel object to be added.
    */
    
   public void addInitiatorConcernStatusIdConcernHistoryModel(ConcernHistoryModel concernHistoryModel) {
      concernHistoryModel.setRelationInitiatorConcernStatusIdConcernStatusModel(this);
      initiatorConcernStatusIdConcernHistoryModelList.add(concernHistoryModel);
   }
   
   /**
    * Remove the related ConcernHistoryModel to this one-to-many relation.
    *
    * @param concernHistoryModel object to be removed.
    */
   
   public void removeInitiatorConcernStatusIdConcernHistoryModel(ConcernHistoryModel concernHistoryModel) {      
      concernHistoryModel.setRelationInitiatorConcernStatusIdConcernStatusModel(null);
      initiatorConcernStatusIdConcernHistoryModelList.remove(concernHistoryModel);      
   }

   /**
    * Get a list of related ConcernHistoryModel objects of the ConcernStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the InitiatorConcernStatusId member.
    *
    * @return Collection of ConcernHistoryModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationInitiatorConcernStatusIdConcernStatusModel")
   @JoinColumn(name = "INITIATOR_CONCERN_STATUS_ID")
   public Collection<ConcernHistoryModel> getInitiatorConcernStatusIdConcernHistoryModelList() throws Exception {
   		return initiatorConcernStatusIdConcernHistoryModelList;
   }


   /**
    * Set a list of ConcernHistoryModel related objects to the ConcernStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the InitiatorConcernStatusId member.
    *
    * @param concernHistoryModelList the list of related objects.
    */
    public void setInitiatorConcernStatusIdConcernHistoryModelList(Collection<ConcernHistoryModel> concernHistoryModelList) throws Exception {
		this.initiatorConcernStatusIdConcernHistoryModelList = concernHistoryModelList;
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
        checkBox += "_"+ getConcernStatusId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&concernStatusId=" + getConcernStatusId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "concernStatusId";
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
