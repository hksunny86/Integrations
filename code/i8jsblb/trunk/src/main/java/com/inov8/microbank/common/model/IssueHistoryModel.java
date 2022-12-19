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
 * The IssueHistoryModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="IssueHistoryModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ISSUE_HISTORY_seq",sequenceName = "ISSUE_HISTORY_seq", allocationSize=1)
@Table(name = "ISSUE_HISTORY")
public class IssueHistoryModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -183638064167310603L;
private IssueTypeStatusModel fromIssueTypeStatusIdIssueTypeStatusModel;
   private IssueTypeStatusModel toIssueTypeStatusIdIssueTypeStatusModel;
   private IssueModel issueIdIssueModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;


   private Long issueHistoryId;
   private Date createdOn;
   private String comments;
   private Date updatedOn;
   private String description;

   /**
    * Default constructor.
    */
   public IssueHistoryModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getIssueHistoryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setIssueHistoryId(primaryKey);
    }

   /**
    * Returns the value of the <code>issueHistoryId</code> property.
    *
    */
      @Column(name = "ISSUE_HISTORY_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUE_HISTORY_seq")
   public Long getIssueHistoryId() {
      return issueHistoryId;
   }

   /**
    * Sets the value of the <code>issueHistoryId</code> property.
    *
    * @param issueHistoryId the value for the <code>issueHistoryId</code> property
    *    
		    */

   public void setIssueHistoryId(Long issueHistoryId) {
      this.issueHistoryId = issueHistoryId;
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
    * Returns the value of the <code>fromIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @return the value of the <code>fromIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "FROM_ISSUE_TYPE_STATUS_ID")    
   public IssueTypeStatusModel getRelationFromIssueTypeStatusIdIssueTypeStatusModel(){
      return fromIssueTypeStatusIdIssueTypeStatusModel;
   }
    
   /**
    * Returns the value of the <code>fromIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @return the value of the <code>fromIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public IssueTypeStatusModel getFromIssueTypeStatusIdIssueTypeStatusModel(){
      return getRelationFromIssueTypeStatusIdIssueTypeStatusModel();
   }

   /**
    * Sets the value of the <code>fromIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @param issueTypeStatusModel a value for <code>fromIssueTypeStatusIdIssueTypeStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationFromIssueTypeStatusIdIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      this.fromIssueTypeStatusIdIssueTypeStatusModel = issueTypeStatusModel;
   }
   
   /**
    * Sets the value of the <code>fromIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @param issueTypeStatusModel a value for <code>fromIssueTypeStatusIdIssueTypeStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setFromIssueTypeStatusIdIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      if(null != issueTypeStatusModel)
      {
      	setRelationFromIssueTypeStatusIdIssueTypeStatusModel((IssueTypeStatusModel)issueTypeStatusModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>toIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @return the value of the <code>toIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TO_ISSUE_TYPE_STATUS_ID")    
   public IssueTypeStatusModel getRelationToIssueTypeStatusIdIssueTypeStatusModel(){
      return toIssueTypeStatusIdIssueTypeStatusModel;
   }
    
   /**
    * Returns the value of the <code>toIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @return the value of the <code>toIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public IssueTypeStatusModel getToIssueTypeStatusIdIssueTypeStatusModel(){
      return getRelationToIssueTypeStatusIdIssueTypeStatusModel();
   }

   /**
    * Sets the value of the <code>toIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @param issueTypeStatusModel a value for <code>toIssueTypeStatusIdIssueTypeStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationToIssueTypeStatusIdIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      this.toIssueTypeStatusIdIssueTypeStatusModel = issueTypeStatusModel;
   }
   
   /**
    * Sets the value of the <code>toIssueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @param issueTypeStatusModel a value for <code>toIssueTypeStatusIdIssueTypeStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setToIssueTypeStatusIdIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      if(null != issueTypeStatusModel)
      {
      	setRelationToIssueTypeStatusIdIssueTypeStatusModel((IssueTypeStatusModel)issueTypeStatusModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>issueIdIssueModel</code> relation property.
    *
    * @return the value of the <code>issueIdIssueModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ISSUE_ID")    
   public IssueModel getRelationIssueIdIssueModel(){
      return issueIdIssueModel;
   }
    
   /**
    * Returns the value of the <code>issueIdIssueModel</code> relation property.
    *
    * @return the value of the <code>issueIdIssueModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public IssueModel getIssueIdIssueModel(){
      return getRelationIssueIdIssueModel();
   }

   /**
    * Sets the value of the <code>issueIdIssueModel</code> relation property.
    *
    * @param issueModel a value for <code>issueIdIssueModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationIssueIdIssueModel(IssueModel issueModel) {
      this.issueIdIssueModel = issueModel;
   }
   
   /**
    * Sets the value of the <code>issueIdIssueModel</code> relation property.
    *
    * @param issueModel a value for <code>issueIdIssueModel</code>.
    */
   @javax.persistence.Transient
   public void setIssueIdIssueModel(IssueModel issueModel) {
      if(null != issueModel)
      {
      	setRelationIssueIdIssueModel((IssueModel)issueModel.clone());
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
    * Returns the value of the <code>issueTypeStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getFromIssueTypeStatusId() {
      if (fromIssueTypeStatusIdIssueTypeStatusModel != null) {
         return fromIssueTypeStatusIdIssueTypeStatusModel.getIssueTypeStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>issueTypeStatusId</code> property.
    *
    * @param issueTypeStatusId the value for the <code>issueTypeStatusId</code> property
																							    */
   
   @javax.persistence.Transient
   public void setFromIssueTypeStatusId(Long issueTypeStatusId) {
      if(issueTypeStatusId == null)
      {      
      	fromIssueTypeStatusIdIssueTypeStatusModel = null;
      }
      else
      {
        fromIssueTypeStatusIdIssueTypeStatusModel = new IssueTypeStatusModel();
      	fromIssueTypeStatusIdIssueTypeStatusModel.setIssueTypeStatusId(issueTypeStatusId);
      }      
   }

   /**
    * Returns the value of the <code>issueTypeStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getToIssueTypeStatusId() {
      if (toIssueTypeStatusIdIssueTypeStatusModel != null) {
         return toIssueTypeStatusIdIssueTypeStatusModel.getIssueTypeStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>issueTypeStatusId</code> property.
    *
    * @param issueTypeStatusId the value for the <code>issueTypeStatusId</code> property
																							    */
   
   @javax.persistence.Transient
   public void setToIssueTypeStatusId(Long issueTypeStatusId) {
      if(issueTypeStatusId == null)
      {      
      	toIssueTypeStatusIdIssueTypeStatusModel = null;
      }
      else
      {
        toIssueTypeStatusIdIssueTypeStatusModel = new IssueTypeStatusModel();
      	toIssueTypeStatusIdIssueTypeStatusModel.setIssueTypeStatusId(issueTypeStatusId);
      }      
   }

   /**
    * Returns the value of the <code>issueId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getIssueId() {
      if (issueIdIssueModel != null) {
         return issueIdIssueModel.getIssueId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>issueId</code> property.
    *
    * @param issueId the value for the <code>issueId</code> property
					    * @spring.validator type="required"
																			    */
   
   @javax.persistence.Transient
   public void setIssueId(Long issueId) {
      if(issueId == null)
      {      
      	issueIdIssueModel = null;
      }
      else
      {
        issueIdIssueModel = new IssueModel();
      	issueIdIssueModel.setIssueId(issueId);
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
        checkBox += "_"+ getIssueHistoryId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&issueHistoryId=" + getIssueHistoryId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "issueHistoryId";
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
    	
    	associationModel.setClassName("IssueTypeStatusModel");
    	associationModel.setPropertyName("relationFromIssueTypeStatusIdIssueTypeStatusModel");   		
   		associationModel.setValue(getRelationFromIssueTypeStatusIdIssueTypeStatusModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("IssueTypeStatusModel");
    	associationModel.setPropertyName("relationToIssueTypeStatusIdIssueTypeStatusModel");   		
   		associationModel.setValue(getRelationToIssueTypeStatusIdIssueTypeStatusModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("IssueModel");
    	associationModel.setPropertyName("relationIssueIdIssueModel");   		
   		associationModel.setValue(getRelationIssueIdIssueModel());
   		
   		associationModelList.add(associationModel);
   		
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
