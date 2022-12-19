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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernHistoryModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernHistoryModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CONCERN_HISTORY_seq",sequenceName = "CONCERN_HISTORY_seq", allocationSize=1)
@Table(name = "CONCERN_HISTORY")
public class ConcernHistoryModel extends BasePersistableModel implements Serializable{
  

   private ConcernStatusModel recipientConcernStatusIdConcernStatusModel;
   private ConcernStatusModel initiatorConcernStatusIdConcernStatusModel;
   private ConcernModel concernIdConcernModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;


   private Long concernHistoryId;
   private String comments;
   private Date createdOn;
   private Date updatedOn;

   /**
    * Default constructor.
    */
   public ConcernHistoryModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getConcernHistoryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setConcernHistoryId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernHistoryId</code> property.
    *
    */
      @Column(name = "CONCERN_HISTORY_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CONCERN_HISTORY_seq")
   public Long getConcernHistoryId() {
      return concernHistoryId;
   }

   /**
    * Sets the value of the <code>concernHistoryId</code> property.
    *
    * @param concernHistoryId the value for the <code>concernHistoryId</code> property
    *    
		    */

   public void setConcernHistoryId(Long concernHistoryId) {
      this.concernHistoryId = concernHistoryId;
   }

   /**
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS" , nullable = false , length=1000 )
   public String getComments() {
      return comments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="1000"
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
    * Returns the value of the <code>recipientConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @return the value of the <code>recipientConcernStatusIdConcernStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "RECIPIENT_CONCERN_STATUS_ID")    
   public ConcernStatusModel getRelationRecipientConcernStatusIdConcernStatusModel(){
      return recipientConcernStatusIdConcernStatusModel;
   }
    
   /**
    * Returns the value of the <code>recipientConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @return the value of the <code>recipientConcernStatusIdConcernStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernStatusModel getRecipientConcernStatusIdConcernStatusModel(){
      return getRelationRecipientConcernStatusIdConcernStatusModel();
   }

   /**
    * Sets the value of the <code>recipientConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @param concernStatusModel a value for <code>recipientConcernStatusIdConcernStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationRecipientConcernStatusIdConcernStatusModel(ConcernStatusModel concernStatusModel) {
      this.recipientConcernStatusIdConcernStatusModel = concernStatusModel;
   }
   
   /**
    * Sets the value of the <code>recipientConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @param concernStatusModel a value for <code>recipientConcernStatusIdConcernStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRecipientConcernStatusIdConcernStatusModel(ConcernStatusModel concernStatusModel) {
      if(null != concernStatusModel)
      {
      	setRelationRecipientConcernStatusIdConcernStatusModel((ConcernStatusModel)concernStatusModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>initiatorConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @return the value of the <code>initiatorConcernStatusIdConcernStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "INITIATOR_CONCERN_STATUS_ID")    
   public ConcernStatusModel getRelationInitiatorConcernStatusIdConcernStatusModel(){
      return initiatorConcernStatusIdConcernStatusModel;
   }
    
   /**
    * Returns the value of the <code>initiatorConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @return the value of the <code>initiatorConcernStatusIdConcernStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernStatusModel getInitiatorConcernStatusIdConcernStatusModel(){
      return getRelationInitiatorConcernStatusIdConcernStatusModel();
   }

   /**
    * Sets the value of the <code>initiatorConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @param concernStatusModel a value for <code>initiatorConcernStatusIdConcernStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationInitiatorConcernStatusIdConcernStatusModel(ConcernStatusModel concernStatusModel) {
      this.initiatorConcernStatusIdConcernStatusModel = concernStatusModel;
   }
   
   /**
    * Sets the value of the <code>initiatorConcernStatusIdConcernStatusModel</code> relation property.
    *
    * @param concernStatusModel a value for <code>initiatorConcernStatusIdConcernStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setInitiatorConcernStatusIdConcernStatusModel(ConcernStatusModel concernStatusModel) {
      if(null != concernStatusModel)
      {
      	setRelationInitiatorConcernStatusIdConcernStatusModel((ConcernStatusModel)concernStatusModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>concernIdConcernModel</code> relation property.
    *
    * @return the value of the <code>concernIdConcernModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CONCERN_ID")    
   public ConcernModel getRelationConcernIdConcernModel(){
      return concernIdConcernModel;
   }
    
   /**
    * Returns the value of the <code>concernIdConcernModel</code> relation property.
    *
    * @return the value of the <code>concernIdConcernModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ConcernModel getConcernIdConcernModel(){
      return getRelationConcernIdConcernModel();
   }

   /**
    * Sets the value of the <code>concernIdConcernModel</code> relation property.
    *
    * @param concernModel a value for <code>concernIdConcernModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationConcernIdConcernModel(ConcernModel concernModel) {
      this.concernIdConcernModel = concernModel;
   }
   
   /**
    * Sets the value of the <code>concernIdConcernModel</code> relation property.
    *
    * @param concernModel a value for <code>concernIdConcernModel</code>.
    */
   @javax.persistence.Transient
   public void setConcernIdConcernModel(ConcernModel concernModel) {
      if(null != concernModel)
      {
      	setRelationConcernIdConcernModel((ConcernModel)concernModel.clone());
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
    * Returns the value of the <code>concernStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getRecipientConcernStatusId() {
      if (recipientConcernStatusIdConcernStatusModel != null) {
         return recipientConcernStatusIdConcernStatusModel.getConcernStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernStatusId</code> property.
    *
    * @param concernStatusId the value for the <code>concernStatusId</code> property
									    * @spring.validator type="required"
													    */
   
   @javax.persistence.Transient
   public void setRecipientConcernStatusId(Long concernStatusId) {
      if(concernStatusId == null)
      {      
      	recipientConcernStatusIdConcernStatusModel = null;
      }
      else
      {
        recipientConcernStatusIdConcernStatusModel = new ConcernStatusModel();
      	recipientConcernStatusIdConcernStatusModel.setConcernStatusId(concernStatusId);
      }      
   }

   /**
    * Returns the value of the <code>concernStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getInitiatorConcernStatusId() {
      if (initiatorConcernStatusIdConcernStatusModel != null) {
         return initiatorConcernStatusIdConcernStatusModel.getConcernStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernStatusId</code> property.
    *
    * @param concernStatusId the value for the <code>concernStatusId</code> property
							    * @spring.validator type="required"
															    */
   
   @javax.persistence.Transient
   public void setInitiatorConcernStatusId(Long concernStatusId) {
      if(concernStatusId == null)
      {      
      	initiatorConcernStatusIdConcernStatusModel = null;
      }
      else
      {
        initiatorConcernStatusIdConcernStatusModel = new ConcernStatusModel();
      	initiatorConcernStatusIdConcernStatusModel.setConcernStatusId(concernStatusId);
      }      
   }

   /**
    * Returns the value of the <code>concernId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getConcernId() {
      if (concernIdConcernModel != null) {
         return concernIdConcernModel.getConcernId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>concernId</code> property.
    *
    * @param concernId the value for the <code>concernId</code> property
					    * @spring.validator type="required"
																	    */
   
   @javax.persistence.Transient
   public void setConcernId(Long concernId) {
      if(concernId == null)
      {      
      	concernIdConcernModel = null;
      }
      else
      {
        concernIdConcernModel = new ConcernModel();
      	concernIdConcernModel.setConcernId(concernId);
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
        checkBox += "_"+ getConcernHistoryId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&concernHistoryId=" + getConcernHistoryId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "concernHistoryId";
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
    	
    	associationModel.setClassName("ConcernStatusModel");
    	associationModel.setPropertyName("relationRecipientConcernStatusIdConcernStatusModel");   		
   		associationModel.setValue(getRelationRecipientConcernStatusIdConcernStatusModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernStatusModel");
    	associationModel.setPropertyName("relationInitiatorConcernStatusIdConcernStatusModel");   		
   		associationModel.setValue(getRelationInitiatorConcernStatusIdConcernStatusModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ConcernModel");
    	associationModel.setPropertyName("relationConcernIdConcernModel");   		
   		associationModel.setValue(getRelationConcernIdConcernModel());
   		
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
