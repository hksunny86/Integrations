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
 * The IssueModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="IssueModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ISSUE_seq",sequenceName = "ISSUE_seq", allocationSize=1)
@Table(name = "ISSUE")
public class IssueModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -5403125582850866912L;
private TransactionCodeModel transactionCodeIdTransactionCodeModel;
   private TransactionModel transactionIdTransactionModel;
   private IssueTypeStatusModel issueTypeStatusIdIssueTypeStatusModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<IssueHistoryModel> issueIdIssueHistoryModelList = new ArrayList<IssueHistoryModel>();

   private Long issueId;
   private String issueCode;
   private Date createdOn;
   private Date updatedOn;
   private String comments;
   private String custTransCode;
   private Integer versionNo;
   private String description;
   private String mfsId;

   /**
    * Default constructor.
    */
   public IssueModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getIssueId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setIssueId(primaryKey);
    }

   /**
    * Returns the value of the <code>issueId</code> property.
    *
    */
      @Column(name = "ISSUE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ISSUE_seq")
   public Long getIssueId() {
      return issueId;
   }

   /**
    * Sets the value of the <code>issueId</code> property.
    *
    * @param issueId the value for the <code>issueId</code> property
    *    
		    */

   public void setIssueId(Long issueId) {
      this.issueId = issueId;
   }

   /**
    * Returns the value of the <code>issueCode</code> property.
    *
    */
      @Column(name = "ISSUE_CODE" , nullable = false , length=50 )
   public String getIssueCode() {
      return issueCode;
   }

   /**
    * Sets the value of the <code>issueCode</code> property.
    *
    * @param issueCode the value for the <code>issueCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setIssueCode(String issueCode) {
      this.issueCode = issueCode;
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
    * Returns the value of the <code>custTransCode</code> property.
    *
    */
      @Column(name = "CUST_TRANS_CODE"  , length=50 )
   public String getCustTransCode() {
      return custTransCode;
   }

   /**
    * Sets the value of the <code>custTransCode</code> property.
    *
    * @param custTransCode the value for the <code>custTransCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustTransCode(String custTransCode) {
      this.custTransCode = custTransCode;
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
    * Returns the value of the <code>mfsId</code> property.
    *
    */
      @Column(name = "MFS_ID"  , length=50 )
   public String getMfsId() {
      return mfsId;
   }

   /**
    * Sets the value of the <code>mfsId</code> property.
    *
    * @param mfsId the value for the <code>mfsId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMfsId(String mfsId) {
      this.mfsId = mfsId;
   }

   /**
    * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_CODE_ID")    
   public TransactionCodeModel getRelationTransactionCodeIdTransactionCodeModel(){
      return transactionCodeIdTransactionCodeModel;
   }
    
   /**
    * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public TransactionCodeModel getTransactionCodeIdTransactionCodeModel(){
      return getRelationTransactionCodeIdTransactionCodeModel();
   }

   /**
    * Sets the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @param transactionCodeModel a value for <code>transactionCodeIdTransactionCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionCodeIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      this.transactionCodeIdTransactionCodeModel = transactionCodeModel;
   }
   
   /**
    * Sets the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @param transactionCodeModel a value for <code>transactionCodeIdTransactionCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionCodeIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      if(null != transactionCodeModel)
      {
      	setRelationTransactionCodeIdTransactionCodeModel((TransactionCodeModel)transactionCodeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @return the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_ID")    
   public TransactionModel getRelationTransactionIdTransactionModel(){
      return transactionIdTransactionModel;
   }
    
   /**
    * Returns the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @return the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public TransactionModel getTransactionIdTransactionModel(){
      return getRelationTransactionIdTransactionModel();
   }

   /**
    * Sets the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @param transactionModel a value for <code>transactionIdTransactionModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionIdTransactionModel(TransactionModel transactionModel) {
      this.transactionIdTransactionModel = transactionModel;
   }
   
   /**
    * Sets the value of the <code>transactionIdTransactionModel</code> relation property.
    *
    * @param transactionModel a value for <code>transactionIdTransactionModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionIdTransactionModel(TransactionModel transactionModel) {
      if(null != transactionModel)
      {
      	setRelationTransactionIdTransactionModel((TransactionModel)transactionModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>issueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @return the value of the <code>issueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ISSUE_TYPE_STATUS_ID")    
   public IssueTypeStatusModel getRelationIssueTypeStatusIdIssueTypeStatusModel(){
      return issueTypeStatusIdIssueTypeStatusModel;
   }
    
   /**
    * Returns the value of the <code>issueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @return the value of the <code>issueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public IssueTypeStatusModel getIssueTypeStatusIdIssueTypeStatusModel(){
      return getRelationIssueTypeStatusIdIssueTypeStatusModel();
   }

   /**
    * Sets the value of the <code>issueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @param issueTypeStatusModel a value for <code>issueTypeStatusIdIssueTypeStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationIssueTypeStatusIdIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      this.issueTypeStatusIdIssueTypeStatusModel = issueTypeStatusModel;
   }
   
   /**
    * Sets the value of the <code>issueTypeStatusIdIssueTypeStatusModel</code> relation property.
    *
    * @param issueTypeStatusModel a value for <code>issueTypeStatusIdIssueTypeStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setIssueTypeStatusIdIssueTypeStatusModel(IssueTypeStatusModel issueTypeStatusModel) {
      if(null != issueTypeStatusModel)
      {
      	setRelationIssueTypeStatusIdIssueTypeStatusModel((IssueTypeStatusModel)issueTypeStatusModel.clone());
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
    * Add the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be added.
    */
    
   public void addIssueIdIssueHistoryModel(IssueHistoryModel issueHistoryModel) {
      issueHistoryModel.setRelationIssueIdIssueModel(this);
      issueIdIssueHistoryModelList.add(issueHistoryModel);
   }
   
   /**
    * Remove the related IssueHistoryModel to this one-to-many relation.
    *
    * @param issueHistoryModel object to be removed.
    */
   
   public void removeIssueIdIssueHistoryModel(IssueHistoryModel issueHistoryModel) {      
      issueHistoryModel.setRelationIssueIdIssueModel(null);
      issueIdIssueHistoryModelList.remove(issueHistoryModel);      
   }

   /**
    * Get a list of related IssueHistoryModel objects of the IssueModel object.
    * These objects are in a bidirectional one-to-many relation by the IssueId member.
    *
    * @return Collection of IssueHistoryModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationIssueIdIssueModel")
   @JoinColumn(name = "ISSUE_ID")
   public Collection<IssueHistoryModel> getIssueIdIssueHistoryModelList() throws Exception {
   		return issueIdIssueHistoryModelList;
   }


   /**
    * Set a list of IssueHistoryModel related objects to the IssueModel object.
    * These objects are in a bidirectional one-to-many relation by the IssueId member.
    *
    * @param issueHistoryModelList the list of related objects.
    */
    public void setIssueIdIssueHistoryModelList(Collection<IssueHistoryModel> issueHistoryModelList) throws Exception {
		this.issueIdIssueHistoryModelList = issueHistoryModelList;
   }



   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionCodeId() {
      if (transactionCodeIdTransactionCodeModel != null) {
         return transactionCodeIdTransactionCodeModel.getTransactionCodeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
																															    */
   
   @javax.persistence.Transient
   public void setTransactionCodeId(Long transactionCodeId) {
      if(transactionCodeId == null)
      {      
      	transactionCodeIdTransactionCodeModel = null;
      }
      else
      {
        transactionCodeIdTransactionCodeModel = new TransactionCodeModel();
      	transactionCodeIdTransactionCodeModel.setTransactionCodeId(transactionCodeId);
      }      
   }

   /**
    * Returns the value of the <code>transactionId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionId() {
      if (transactionIdTransactionModel != null) {
         return transactionIdTransactionModel.getTransactionId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionId</code> property.
    *
    * @param transactionId the value for the <code>transactionId</code> property
																															    */
   
   @javax.persistence.Transient
   public void setTransactionId(Long transactionId) {
      if(transactionId == null)
      {      
      	transactionIdTransactionModel = null;
      }
      else
      {
        transactionIdTransactionModel = new TransactionModel();
      	transactionIdTransactionModel.setTransactionId(transactionId);
      }      
   }

   /**
    * Returns the value of the <code>issueTypeStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getIssueTypeStatusId() {
      if (issueTypeStatusIdIssueTypeStatusModel != null) {
         return issueTypeStatusIdIssueTypeStatusModel.getIssueTypeStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>issueTypeStatusId</code> property.
    *
    * @param issueTypeStatusId the value for the <code>issueTypeStatusId</code> property
									    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setIssueTypeStatusId(Long issueTypeStatusId) {
      if(issueTypeStatusId == null)
      {      
      	issueTypeStatusIdIssueTypeStatusModel = null;
      }
      else
      {
        issueTypeStatusIdIssueTypeStatusModel = new IssueTypeStatusModel();
      	issueTypeStatusIdIssueTypeStatusModel.setIssueTypeStatusId(issueTypeStatusId);
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
        checkBox += "_"+ getIssueId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&issueId=" + getIssueId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "issueId";
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
    	
    	associationModel.setClassName("TransactionCodeModel");
    	associationModel.setPropertyName("relationTransactionCodeIdTransactionCodeModel");   		
   		associationModel.setValue(getRelationTransactionCodeIdTransactionCodeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("TransactionModel");
    	associationModel.setPropertyName("relationTransactionIdTransactionModel");   		
   		associationModel.setValue(getRelationTransactionIdTransactionModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("IssueTypeStatusModel");
    	associationModel.setPropertyName("relationIssueTypeStatusIdIssueTypeStatusModel");   		
   		associationModel.setValue(getRelationIssueTypeStatusIdIssueTypeStatusModel());
   		
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
