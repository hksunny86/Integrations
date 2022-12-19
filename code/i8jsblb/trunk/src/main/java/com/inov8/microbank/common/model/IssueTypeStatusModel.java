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
 * The IssueTypeStatusModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="IssueTypeStatusModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ISSUE_TYPE_STATUS_seq",sequenceName = "ISSUE_TYPE_STATUS_seq", allocationSize=1)
@Table(name = "ISSUE_TYPE_STATUS")
public class IssueTypeStatusModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -1855730555858376907L;
private IssueTypeModel issueTypeIdIssueTypeModel;
   private IssueStatusModel issueStatusIdIssueStatusModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;

   private Collection<IssueModel> issueTypeStatusIdIssueModelList = new ArrayList<IssueModel>();
   private Collection<IssueHistoryModel> fromIssueTypeStatusIdIssueHistoryModelList = new ArrayList<IssueHistoryModel>();
   private Collection<IssueHistoryModel> toIssueTypeStatusIdIssueHistoryModelList = new ArrayList<IssueHistoryModel>();

   private Long issueTypeStatusId;
   private String description;
   private String comments;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public IssueTypeStatusModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getIssueTypeStatusId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setIssueTypeStatusId(primaryKey);
    }

   /**
    * Returns the value of the <code>issueTypeStatusId</code> property.
    *
    */
      @Column(name = "ISSUE_TYPE_STATUS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUE_TYPE_STATUS_seq")
   public Long getIssueTypeStatusId() {
      return issueTypeStatusId;
   }

   /**
    * Sets the value of the <code>issueTypeStatusId</code> property.
    *
    * @param issueTypeStatusId the value for the <code>issueTypeStatusId</code> property
    *    
		    */

   public void setIssueTypeStatusId(Long issueTypeStatusId) {
      this.issueTypeStatusId = issueTypeStatusId;
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
    * Returns the value of the <code>issueTypeIdIssueTypeModel</code> relation property.
    *
    * @return the value of the <code>issueTypeIdIssueTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ISSUE_TYPE_ID")    
   public IssueTypeModel getRelationIssueTypeIdIssueTypeModel(){
      return issueTypeIdIssueTypeModel;
   }
    
   /**
    * Returns the value of the <code>issueTypeIdIssueTypeModel</code> relation property.
    *
    * @return the value of the <code>issueTypeIdIssueTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public IssueTypeModel getIssueTypeIdIssueTypeModel(){
      return getRelationIssueTypeIdIssueTypeModel();
   }

   /**
    * Sets the value of the <code>issueTypeIdIssueTypeModel</code> relation property.
    *
    * @param issueTypeModel a value for <code>issueTypeIdIssueTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationIssueTypeIdIssueTypeModel(IssueTypeModel issueTypeModel) {
      this.issueTypeIdIssueTypeModel = issueTypeModel;
   }
   
   /**
    * Sets the value of the <code>issueTypeIdIssueTypeModel</code> relation property.
    *
    * @param issueTypeModel a value for <code>issueTypeIdIssueTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setIssueTypeIdIssueTypeModel(IssueTypeModel issueTypeModel) {
      if(null != issueTypeModel)
      {
      	setRelationIssueTypeIdIssueTypeModel((IssueTypeModel)issueTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>issueStatusIdIssueStatusModel</code> relation property.
    *
    * @return the value of the <code>issueStatusIdIssueStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ISSUE_STATUS_ID")    
   public IssueStatusModel getRelationIssueStatusIdIssueStatusModel(){
      return issueStatusIdIssueStatusModel;
   }
    
   /**
    * Returns the value of the <code>issueStatusIdIssueStatusModel</code> relation property.
    *
    * @return the value of the <code>issueStatusIdIssueStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public IssueStatusModel getIssueStatusIdIssueStatusModel(){
      return getRelationIssueStatusIdIssueStatusModel();
   }

   /**
    * Sets the value of the <code>issueStatusIdIssueStatusModel</code> relation property.
    *
    * @param issueStatusModel a value for <code>issueStatusIdIssueStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationIssueStatusIdIssueStatusModel(IssueStatusModel issueStatusModel) {
      this.issueStatusIdIssueStatusModel = issueStatusModel;
   }
   
   /**
    * Sets the value of the <code>issueStatusIdIssueStatusModel</code> relation property.
    *
    * @param issueStatusModel a value for <code>issueStatusIdIssueStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setIssueStatusIdIssueStatusModel(IssueStatusModel issueStatusModel) {
      if(null != issueStatusModel)
      {
      	setRelationIssueStatusIdIssueStatusModel((IssueStatusModel)issueStatusModel.clone());
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
    * Add the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be added.
    */
    
   public void addIssueTypeStatusIdIssueModel(IssueModel issueModel) {
      issueModel.setRelationIssueTypeStatusIdIssueTypeStatusModel(this);
      issueTypeStatusIdIssueModelList.add(issueModel);
   }
   
   /**
    * Remove the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be removed.
    */
   
   public void removeIssueTypeStatusIdIssueModel(IssueModel issueModel) {      
      issueModel.setRelationIssueTypeStatusIdIssueTypeStatusModel(null);
      issueTypeStatusIdIssueModelList.remove(issueModel);      
   }

   /**
    * Get a list of related IssueModel objects of the IssueTypeStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the IssueTypeStatusId member.
    *
    * @return Collection of IssueModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationIssueTypeStatusIdIssueTypeStatusModel")
   @JoinColumn(name = "ISSUE_TYPE_STATUS_ID")
   public Collection<IssueModel> getIssueTypeStatusIdIssueModelList() throws Exception {
   		return issueTypeStatusIdIssueModelList;
   }


   /**
    * Set a list of IssueModel related objects to the IssueTypeStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the IssueTypeStatusId member.
    *
    * @param issueModelList the list of related objects.
    */
    public void setIssueTypeStatusIdIssueModelList(Collection<IssueModel> issueModelList) throws Exception {
		this.issueTypeStatusIdIssueModelList = issueModelList;
   }


   /**
    * Add the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be added.
    */
    
   public void addFromIssueTypeStatusIdIssueHistoryModel(IssueHistoryModel issueHistoryModel) {
      issueHistoryModel.setRelationFromIssueTypeStatusIdIssueTypeStatusModel(this);
      fromIssueTypeStatusIdIssueHistoryModelList.add(issueHistoryModel);
   }
   
   /**
    * Remove the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be removed.
    */
   
   public void removeFromIssueTypeStatusIdIssueHistoryModel(IssueHistoryModel issueHistoryModel) {      
      issueHistoryModel.setRelationFromIssueTypeStatusIdIssueTypeStatusModel(null);
      fromIssueTypeStatusIdIssueHistoryModelList.remove(issueHistoryModel);      
   }

   /**
    * Get a list of related IssueHistoryModel objects of the IssueTypeStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the FromIssueTypeStatusId member.
    *
    * @return Collection of IssueHistoryModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFromIssueTypeStatusIdIssueTypeStatusModel")
   @JoinColumn(name = "FROM_ISSUE_TYPE_STATUS_ID")
   public Collection<IssueHistoryModel> getFromIssueTypeStatusIdIssueHistoryModelList() throws Exception {
   		return fromIssueTypeStatusIdIssueHistoryModelList;
   }


   /**
    * Set a list of IssueHistoryModel related objects to the IssueTypeStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the FromIssueTypeStatusId member.
    *
    * @param issueHistoryModelList the list of related objects.
    */
    public void setFromIssueTypeStatusIdIssueHistoryModelList(Collection<IssueHistoryModel> issueHistoryModelList) throws Exception {
		this.fromIssueTypeStatusIdIssueHistoryModelList = issueHistoryModelList;
   }


   /**
    * Add the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be added.
    */
    
   public void addToIssueTypeStatusIdIssueHistoryModel(IssueHistoryModel issueHistoryModel) {
      issueHistoryModel.setRelationToIssueTypeStatusIdIssueTypeStatusModel(this);
      toIssueTypeStatusIdIssueHistoryModelList.add(issueHistoryModel);
   }
   
   /**
    * Remove the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be removed.
    */
   
   public void removeToIssueTypeStatusIdIssueHistoryModel(IssueHistoryModel issueHistoryModel) {      
      issueHistoryModel.setRelationToIssueTypeStatusIdIssueTypeStatusModel(null);
      toIssueTypeStatusIdIssueHistoryModelList.remove(issueHistoryModel);      
   }

   /**
    * Get a list of related IssueHistoryModel objects of the IssueTypeStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the ToIssueTypeStatusId member.
    *
    * @return Collection of IssueHistoryModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationToIssueTypeStatusIdIssueTypeStatusModel")
   @JoinColumn(name = "TO_ISSUE_TYPE_STATUS_ID")
   public Collection<IssueHistoryModel> getToIssueTypeStatusIdIssueHistoryModelList() throws Exception {
   		return toIssueTypeStatusIdIssueHistoryModelList;
   }


   /**
    * Set a list of IssueHistoryModel related objects to the IssueTypeStatusModel object.
    * These objects are in a bidirectional one-to-many relation by the ToIssueTypeStatusId member.
    *
    * @param issueHistoryModelList the list of related objects.
    */
    public void setToIssueTypeStatusIdIssueHistoryModelList(Collection<IssueHistoryModel> issueHistoryModelList) throws Exception {
		this.toIssueTypeStatusIdIssueHistoryModelList = issueHistoryModelList;
   }



   /**
    * Returns the value of the <code>issueTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getIssueTypeId() {
      if (issueTypeIdIssueTypeModel != null) {
         return issueTypeIdIssueTypeModel.getIssueTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>issueTypeId</code> property.
    *
    * @param issueTypeId the value for the <code>issueTypeId</code> property
							    * @spring.validator type="required"
																	    */
   
   @javax.persistence.Transient
   public void setIssueTypeId(Long issueTypeId) {
      if(issueTypeId == null)
      {      
      	issueTypeIdIssueTypeModel = null;
      }
      else
      {
        issueTypeIdIssueTypeModel = new IssueTypeModel();
      	issueTypeIdIssueTypeModel.setIssueTypeId(issueTypeId);
      }      
   }

   /**
    * Returns the value of the <code>issueStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getIssueStatusId() {
      if (issueStatusIdIssueStatusModel != null) {
         return issueStatusIdIssueStatusModel.getIssueStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>issueStatusId</code> property.
    *
    * @param issueStatusId the value for the <code>issueStatusId</code> property
					    * @spring.validator type="required"
																			    */
   
   @javax.persistence.Transient
   public void setIssueStatusId(Long issueStatusId) {
      if(issueStatusId == null)
      {      
      	issueStatusIdIssueStatusModel = null;
      }
      else
      {
        issueStatusIdIssueStatusModel = new IssueStatusModel();
      	issueStatusIdIssueStatusModel.setIssueStatusId(issueStatusId);
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
        checkBox += "_"+ getIssueTypeStatusId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&issueTypeStatusId=" + getIssueTypeStatusId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "issueTypeStatusId";
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
    	
    	associationModel.setClassName("IssueTypeModel");
    	associationModel.setPropertyName("relationIssueTypeIdIssueTypeModel");   		
   		associationModel.setValue(getRelationIssueTypeIdIssueTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("IssueStatusModel");
    	associationModel.setPropertyName("relationIssueStatusIdIssueStatusModel");   		
   		associationModel.setValue(getRelationIssueStatusIdIssueStatusModel());
   		
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
