package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The TransactionCodeModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransactionCodeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="TRANSACTION_CODE_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="TRANSACTION_CODE_seq") } )
//@javax.persistence.SequenceGenerator(name = "TRANSACTION_CODE_seq",sequenceName = "TRANSACTION_CODE_seq")
@Table(name = "TRANSACTION_CODE")
public class TransactionCodeModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 4455592091340752577L;
private FailureReasonModel failureReasonIdFailureReasonModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   private ActionLogModel actionLogIdActionLogModel;

   private Collection<AuditLogModel> transactionCodeIdAuditLogModelList = new ArrayList<AuditLogModel>();
   private Collection<IssueModel> transactionCodeIdIssueModelList = new ArrayList<IssueModel>();
   private Collection<TransactionModel> transactionCodeIdTransactionModelList = new ArrayList<TransactionModel>();
   
   private Collection<MiniTransactionModel> transactionCodeIdMiniTransactionModelList = new ArrayList<MiniTransactionModel>();

   private Long transactionCodeId;
   private String code;
   private String reason;
   private java.sql.Timestamp startTime;
   private java.sql.Timestamp endTime;
   private Integer status;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public TransactionCodeModel() {
   }   
   public TransactionCodeModel(String code) {	   
	   this.code = code;
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getTransactionCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setTransactionCodeId(primaryKey);
    }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTION_CODE_seq")
   public Long getTransactionCodeId() {
      return transactionCodeId;
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
    *    
		    */

   public void setTransactionCodeId(Long transactionCodeId) {
      this.transactionCodeId = transactionCodeId;
   }

   /**
    * Returns the value of the <code>code</code> property.
    *
    */
      @Column(name = "CODE" , nullable = false , length=50 )
   public String getCode() {
      return code;
   }

   /**
    * Sets the value of the <code>code</code> property.
    *
    * @param code the value for the <code>code</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCode(String code) {
      this.code = code;
   }

   /**
    * Returns the value of the <code>reason</code> property.
    *
    */
      @Column(name = "REASON"  , length=250 )
   public String getReason() {
      return reason;
   }

   /**
    * Sets the value of the <code>reason</code> property.
    *
    * @param reason the value for the <code>reason</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setReason(String reason) {
      this.reason = reason;
   }

   /**
    * Returns the value of the <code>startTime</code> property.
    *
    */
      @Column(name = "START_TIME" , nullable = false )
   public java.sql.Timestamp getStartTime() {
      return startTime;
   }

   /**
    * Sets the value of the <code>startTime</code> property.
    *
    * @param startTime the value for the <code>startTime</code> property
    *    
		    * @spring.validator type="required"
    */

   public void setStartTime(java.sql.Timestamp startTime) {
      this.startTime = startTime;
   }

   /**
    * Returns the value of the <code>endTime</code> property.
    *
    */
      @Column(name = "END_TIME" , nullable = false )
   public java.sql.Timestamp getEndTime() {
      return endTime;
   }

   /**
    * Sets the value of the <code>endTime</code> property.
    *
    * @param endTime the value for the <code>endTime</code> property
    *    
		    * @spring.validator type="required"
    */

   public void setEndTime(java.sql.Timestamp endTime) {
      this.endTime = endTime;
   }

   /**
    * Returns the value of the <code>status</code> property.
    *
    */
      @Column(name = "STATUS" , nullable = false )
   public Integer getStatus() {
      return status;
   }

   /**
    * Sets the value of the <code>status</code> property.
    *
    * @param status the value for the <code>status</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="integer"
    */

   public void setStatus(Integer status) {
      this.status = status;
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
    * Returns the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @return the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "FAILURE_REASON_ID")    
   public FailureReasonModel getRelationFailureReasonIdFailureReasonModel(){
      return failureReasonIdFailureReasonModel;
   }
    
   /**
    * Returns the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @return the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public FailureReasonModel getFailureReasonIdFailureReasonModel(){
      return getRelationFailureReasonIdFailureReasonModel();
   }

   /**
    * Sets the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @param failureReasonModel a value for <code>failureReasonIdFailureReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationFailureReasonIdFailureReasonModel(FailureReasonModel failureReasonModel) {
      this.failureReasonIdFailureReasonModel = failureReasonModel;
   }
   
   /**
    * Sets the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @param failureReasonModel a value for <code>failureReasonIdFailureReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setFailureReasonIdFailureReasonModel(FailureReasonModel failureReasonModel) {
      if(null != failureReasonModel)
      {
      	setRelationFailureReasonIdFailureReasonModel((FailureReasonModel)failureReasonModel.clone());
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
    * Returns the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @return the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACTION_LOG_ID")    
   public ActionLogModel getRelationActionLogIdActionLogModel(){
      return actionLogIdActionLogModel;
   }
    
   /**
    * Returns the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @return the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ActionLogModel getActionLogIdActionLogModel(){
      return getRelationActionLogIdActionLogModel();
   }

   /**
    * Sets the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @param actionLogModel a value for <code>actionLogIdActionLogModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationActionLogIdActionLogModel(ActionLogModel actionLogModel) {
      this.actionLogIdActionLogModel = actionLogModel;
   }
   
   /**
    * Sets the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @param actionLogModel a value for <code>actionLogIdActionLogModel</code>.
    */
   @javax.persistence.Transient
   public void setActionLogIdActionLogModel(ActionLogModel actionLogModel) {
      if(null != actionLogModel)
      {
      	setRelationActionLogIdActionLogModel((ActionLogModel)actionLogModel.clone());
      }      
   }
   


   /**
    * Add the related AuditLogModel to this one-to-many relation.
    *
    * @param auditLogModel object to be added.
    */
    
   public void addTransactionCodeIdAuditLogModel(AuditLogModel auditLogModel) {
      auditLogModel.setRelationTransactionCodeIdTransactionCodeModel(this);
      transactionCodeIdAuditLogModelList.add(auditLogModel);
   }
   
   /**
    * Remove the related AuditLogModel to this one-to-many relation.
    *
    * @param auditLogModel object to be removed.
    */
   
   public void removeTransactionCodeIdAuditLogModel(AuditLogModel auditLogModel) {      
      auditLogModel.setRelationTransactionCodeIdTransactionCodeModel(null);
      transactionCodeIdAuditLogModelList.remove(auditLogModel);      
   }

   /**
    * Get a list of related AuditLogModel objects of the TransactionCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionCodeId member.
    *
    * @return Collection of AuditLogModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionCodeIdTransactionCodeModel")
   @JoinColumn(name = "TRANSACTION_CODE_ID")
   public Collection<AuditLogModel> getTransactionCodeIdAuditLogModelList() throws Exception {
   		return transactionCodeIdAuditLogModelList;
   }


   /**
    * Set a list of AuditLogModel related objects to the TransactionCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionCodeId member.
    *
    * @param auditLogModelList the list of related objects.
    */
    public void setTransactionCodeIdAuditLogModelList(Collection<AuditLogModel> auditLogModelList) throws Exception {
		this.transactionCodeIdAuditLogModelList = auditLogModelList;
   }


   /**
    * Add the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be added.
    */
    
   public void addTransactionCodeIdIssueModel(IssueModel issueModel) {
      issueModel.setRelationTransactionCodeIdTransactionCodeModel(this);
      transactionCodeIdIssueModelList.add(issueModel);
   }
   
   /**
    * Remove the related IssueModel to this one-to-many relation.
    *
    * @param issueModel object to be removed.
    */
   
   public void removeTransactionCodeIdIssueModel(IssueModel issueModel) {      
      issueModel.setRelationTransactionCodeIdTransactionCodeModel(null);
      transactionCodeIdIssueModelList.remove(issueModel);      
   }

   /**
    * Get a list of related IssueModel objects of the TransactionCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionCodeId member.
    *
    * @return Collection of IssueModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionCodeIdTransactionCodeModel")
   @JoinColumn(name = "TRANSACTION_CODE_ID")
   public Collection<IssueModel> getTransactionCodeIdIssueModelList() throws Exception {
   		return transactionCodeIdIssueModelList;
   }


   /**
    * Set a list of IssueModel related objects to the TransactionCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionCodeId member.
    *
    * @param issueModelList the list of related objects.
    */
    public void setTransactionCodeIdIssueModelList(Collection<IssueModel> issueModelList) throws Exception {
		this.transactionCodeIdIssueModelList = issueModelList;
   }


   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addTransactionCodeIdTransactionModel(TransactionModel transactionModel) {
      transactionModel.setRelationTransactionCodeIdTransactionCodeModel(this);
      transactionCodeIdTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeTransactionCodeIdTransactionModel(TransactionModel transactionModel) {      
      transactionModel.setRelationTransactionCodeIdTransactionCodeModel(null);
      transactionCodeIdTransactionModelList.remove(transactionModel);      
   }
   
   /**
    * Add the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be added.
    */
    
   public void addTransactionCodeIdMiniTransactionModel(MiniTransactionModel transactionModel) {
      transactionModel.setRelationTransactionCodeIdTransactionCodeModel(this);
      transactionCodeIdMiniTransactionModelList.add(transactionModel);
   }
   
   /**
    * Remove the related TransactionModel to this one-to-many relation.
    *
    * @param transactionModel object to be removed.
    */
   
   public void removeTransactionCodeIdMiniTransactionModel(MiniTransactionModel transactionModel) {      
      transactionModel.setRelationTransactionCodeIdTransactionCodeModel(null);
      transactionCodeIdMiniTransactionModelList.remove(transactionModel);      
   }

   
   
   

   /**
    * Get a list of related TransactionModel objects of the TransactionCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionCodeId member.
    *
    * @return Collection of TransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionCodeIdTransactionCodeModel")
   @JoinColumn(name = "TRANSACTION_CODE_ID")
   public Collection<TransactionModel> getTransactionCodeIdTransactionModelList() throws Exception {
   		return transactionCodeIdTransactionModelList;
   }

   /**
    * Set a list of TransactionModel related objects to the TransactionCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionCodeId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setTransactionCodeIdMiniTransactionModelList(Collection<MiniTransactionModel> transactionModelList) throws Exception {
		this.transactionCodeIdMiniTransactionModelList = transactionModelList;
   }

   
   
   /**
    * Set a list of TransactionModel related objects to the TransactionCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the TransactionCodeId member.
    *
    * @param transactionModelList the list of related objects.
    */
    public void setTransactionCodeIdTransactionModelList(Collection<TransactionModel> transactionModelList) throws Exception {
		this.transactionCodeIdTransactionModelList = transactionModelList;
   }
    
  
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationTransactionCodeIdTransactionCodeModel")
    @JoinColumn(name = "TRANSACTION_CODE_ID")
    public Collection<MiniTransactionModel> getTransactionCodeIdMiniTransactionModelList() throws Exception {
    		return transactionCodeIdMiniTransactionModelList;
    }


   /**
    * Returns the value of the <code>failureReasonId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getFailureReasonId() {
      if (failureReasonIdFailureReasonModel != null) {
         return failureReasonIdFailureReasonModel.getFailureReasonId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>failureReasonId</code> property.
    *
    * @param failureReasonId the value for the <code>failureReasonId</code> property
																													    */
   
   @javax.persistence.Transient
   public void setFailureReasonId(Long failureReasonId) {
      if(failureReasonId == null)
      {      
      	failureReasonIdFailureReasonModel = null;
      }
      else
      {
        failureReasonIdFailureReasonModel = new FailureReasonModel();
      	failureReasonIdFailureReasonModel.setFailureReasonId(failureReasonId);
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
    * Returns the value of the <code>actionLogId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getActionLogId() {
      if (actionLogIdActionLogModel != null) {
         return actionLogIdActionLogModel.getActionLogId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>actionLogId</code> property.
    *
    * @param actionLogId the value for the <code>actionLogId</code> property
																													    */
   
   @javax.persistence.Transient
   public void setActionLogId(Long actionLogId) {
      if(actionLogId == null)
      {      
      	actionLogIdActionLogModel = null;
      }
      else
      {
        actionLogIdActionLogModel = new ActionLogModel();
      	actionLogIdActionLogModel.setActionLogId(actionLogId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getTransactionCodeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&transactionCodeId=" + getTransactionCodeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "transactionCodeId";
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
    	
    	associationModel.setClassName("FailureReasonModel");
    	associationModel.setPropertyName("relationFailureReasonIdFailureReasonModel");   		
   		associationModel.setValue(getRelationFailureReasonIdFailureReasonModel());
   		
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
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ActionLogModel");
    	associationModel.setPropertyName("relationActionLogIdActionLogModel");   		
   		associationModel.setValue(getRelationActionLogIdActionLogModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
