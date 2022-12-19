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
 * The MiniTransactionModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="MiniTransactionModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="MINI_TRANSACTION_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="MINI_TRANSACTION_seq") } )
//@javax.persistence.SequenceGenerator(name = "MINI_TRANSACTION_seq",sequenceName = "MINI_TRANSACTION_seq")
@Table(name = "MINI_TRANSACTION")
public class MiniTransactionModel extends BasePersistableModel implements Serializable {
  
	
	private TransactionCodeModel transactionCodeIdTransactionCodeModel;
   private MiniTransactionStateModel miniTransactionStateIdMiniTransactionStateModel;
   private CommandModel commandIdCommandModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel appUserIdAppUserModel;
   private ActionLogModel actionLogIdActionLogModel;


   private Long miniTransactionId;
   private Date timeDate;
   private String mobileNo;
   private String smsText;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Double CAMT;
   private Double BAMT;
   private Double TPAM;
   private Double TAMT;
   private String comments;
   private String oneTimePin;
   private Boolean isManualOTPin;

   private Long otRetryCount;
   private String plainOTP;
   private String channelId;

   /**
    * Default constructor.
    */
   public MiniTransactionModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getMiniTransactionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setMiniTransactionId(primaryKey);
    }

   /**
    * Returns the value of the <code>miniTransactionId</code> property.
    *
    */
      @Column(name = "MINI_TRANSACTION_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MINI_TRANSACTION_seq")
   public Long getMiniTransactionId() {
      return miniTransactionId;
   }

   /**
    * Sets the value of the <code>miniTransactionId</code> property.
    *
    * @param miniTransactionId the value for the <code>miniTransactionId</code> property
    *    
		    */

   public void setMiniTransactionId(Long miniTransactionId) {
      this.miniTransactionId = miniTransactionId;
   }

   /**
    * Returns the value of the <code>timeDate</code> property.
    *
    */
      @Column(name = "TIME_DATE" , nullable = false )
   public Date getTimeDate() {
      return timeDate;
   }

   /**
    * Sets the value of the <code>timeDate</code> property.
    *
    * @param timeDate the value for the <code>timeDate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setTimeDate(Date timeDate) {
      this.timeDate = timeDate;
   }

   /**
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
      @Column(name = "MOBILE_NO" , nullable = false , length=50 )
   public String getMobileNo() {
      return mobileNo;
   }

   /**
    * Sets the value of the <code>mobileNo</code> property.
    *
    * @param mobileNo the value for the <code>mobileNo</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>oneTimePin</code> property.
    *
    */
   @Column(name = "ONE_TIME_PIN" , nullable = true , length=50 )
   public String getOneTimePin() {
		return oneTimePin;
	}

	public void setOneTimePin(String oneTimePin) {
		this.oneTimePin = oneTimePin;
	}
	
	
	/**
	    * Returns the value of the <code>isManualOTPin</code> property.
	    *
	    */
	      @Column(name = "IS_MANUAL_OT_PIN"  )
	   public Boolean getIsManualOTPin() {
	      return isManualOTPin;
	   }

	   /**
	    * Sets the value of the <code>isManualOTPin</code> property.
	    *
	    * @param issue the value for the <code>isManualOTPin</code> property
	    *    
			    */

	   public void setIsManualOTPin(Boolean isManualOTPin) {
	      this.isManualOTPin = isManualOTPin;
	   }
	   
   /**
    * Returns the value of the <code>smsText</code> property.
    *
    */
      @Column(name = "SMS_TEXT" , length=250 )
   public String getSmsText() {
      return smsText;
   }

   /**
    * Sets the value of the <code>smsText</code> property.
    *
    * @param smsText the value for the <code>smsText</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setSmsText(String smsText) {
      this.smsText = smsText;
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
    * Returns the value of the <code>miniTransactionStateIdMiniTransactionStateModel</code> relation property.
    *
    * @return the value of the <code>miniTransactionStateIdMiniTransactionStateModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "MINI_TRANSACTION_STATE_ID")    
   public MiniTransactionStateModel getRelationMiniTransactionStateIdMiniTransactionStateModel(){
      return miniTransactionStateIdMiniTransactionStateModel;
   }
    
   /**
    * Returns the value of the <code>miniTransactionStateIdMiniTransactionStateModel</code> relation property.
    *
    * @return the value of the <code>miniTransactionStateIdMiniTransactionStateModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public MiniTransactionStateModel getMiniTransactionStateIdMiniTransactionStateModel(){
      return getRelationMiniTransactionStateIdMiniTransactionStateModel();
   }

   /**
    * Sets the value of the <code>miniTransactionStateIdMiniTransactionStateModel</code> relation property.
    *
    * @param miniTransactionStateModel a value for <code>miniTransactionStateIdMiniTransactionStateModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationMiniTransactionStateIdMiniTransactionStateModel(MiniTransactionStateModel miniTransactionStateModel) {
      this.miniTransactionStateIdMiniTransactionStateModel = miniTransactionStateModel;
   }
   
   /**
    * Sets the value of the <code>miniTransactionStateIdMiniTransactionStateModel</code> relation property.
    *
    * @param miniTransactionStateModel a value for <code>miniTransactionStateIdMiniTransactionStateModel</code>.
    */
   @javax.persistence.Transient
   public void setMiniTransactionStateIdMiniTransactionStateModel(MiniTransactionStateModel miniTransactionStateModel) {
      if(null != miniTransactionStateModel)
      {
      	setRelationMiniTransactionStateIdMiniTransactionStateModel((MiniTransactionStateModel)miniTransactionStateModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @return the value of the <code>commandIdCommandModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COMMAND_ID")    
   public CommandModel getRelationCommandIdCommandModel(){
      return commandIdCommandModel;
   }
    
   /**
    * Returns the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @return the value of the <code>commandIdCommandModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommandModel getCommandIdCommandModel(){
      return getRelationCommandIdCommandModel();
   }

   /**
    * Sets the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @param commandModel a value for <code>commandIdCommandModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommandIdCommandModel(CommandModel commandModel) {
      this.commandIdCommandModel = commandModel;
   }
   
   /**
    * Sets the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @param commandModel a value for <code>commandIdCommandModel</code>.
    */
   @javax.persistence.Transient
   public void setCommandIdCommandModel(CommandModel commandModel) {
      if(null != commandModel)
      {
      	setRelationCommandIdCommandModel((CommandModel)commandModel.clone());
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
    * Returns the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @return the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_USER_ID")    
   public AppUserModel getRelationAppUserIdAppUserModel(){
      return appUserIdAppUserModel;
   }
    
   /**
    * Returns the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @return the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getAppUserIdAppUserModel(){
      return getRelationAppUserIdAppUserModel();
   }

   /**
    * Sets the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>appUserIdAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAppUserIdAppUserModel(AppUserModel appUserModel) {
      this.appUserIdAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>appUserIdAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setAppUserIdAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationAppUserIdAppUserModel((AppUserModel)appUserModel.clone());
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
    * Returns the value of the <code>miniTransactionStateId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getMiniTransactionStateId() {
      if (miniTransactionStateIdMiniTransactionStateModel != null) {
         return miniTransactionStateIdMiniTransactionStateModel.getMiniTransactionStateId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>miniTransactionStateId</code> property.
    *
    * @param miniTransactionStateId the value for the <code>miniTransactionStateId</code> property
					    * @spring.validator type="required"
																									    */
   
   @javax.persistence.Transient
   public void setMiniTransactionStateId(Long miniTransactionStateId) {
      if(miniTransactionStateId == null)
      {      
      	miniTransactionStateIdMiniTransactionStateModel = null;
      }
      else
      {
        miniTransactionStateIdMiniTransactionStateModel = new MiniTransactionStateModel();
      	miniTransactionStateIdMiniTransactionStateModel.setMiniTransactionStateId(miniTransactionStateId);
      }      
   }

   /**
    * Returns the value of the <code>commandId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommandId() {
      if (commandIdCommandModel != null) {
         return commandIdCommandModel.getCommandId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commandId</code> property.
    *
    * @param commandId the value for the <code>commandId</code> property
							    * @spring.validator type="required"
																							    */
   
   @javax.persistence.Transient
   public void setCommandId(Long commandId) {
      if(commandId == null)
      {      
      	commandIdCommandModel = null;
      }
      else
      {
        commandIdCommandModel = new CommandModel();
      	commandIdCommandModel.setCommandId(commandId);
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
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAppUserId() {
      if (appUserIdAppUserModel != null) {
         return appUserIdAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
									    * @spring.validator type="required"
																					    */
   
   @javax.persistence.Transient
   public void setAppUserId(Long appUserId) {
      if(appUserId == null)
      {      
      	appUserIdAppUserModel = null;
      }
      else
      {
        appUserIdAppUserModel = new AppUserModel();
      	appUserIdAppUserModel.setAppUserId(appUserId);
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
																											    * @spring.validator type="required"
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
        checkBox += "_"+ getMiniTransactionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&miniTransactionId=" + getMiniTransactionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "miniTransactionId";
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
    	
    	associationModel.setClassName("MiniTransactionStateModel");
    	associationModel.setPropertyName("relationMiniTransactionStateIdMiniTransactionStateModel");   		
   		associationModel.setValue(getRelationMiniTransactionStateIdMiniTransactionStateModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommandModel");
    	associationModel.setPropertyName("relationCommandIdCommandModel");   		
   		associationModel.setValue(getRelationCommandIdCommandModel());
   		
   		associationModelList.add(associationModel);
   		
   		
   				 associationModel = new AssociationModel();
    	
    	associationModel.setClassName("TransactionCodeModel");
    	associationModel.setPropertyName("relationTransactionCodeIdTransactionCodeModel");   		
   		associationModel.setValue(getRelationTransactionCodeIdTransactionCodeModel());
   		
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
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationAppUserIdAppUserModel");   		
   		associationModel.setValue(getRelationAppUserIdAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ActionLogModel");
    	associationModel.setPropertyName("relationActionLogIdActionLogModel");   		
   		associationModel.setValue(getRelationActionLogIdActionLogModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }
    
    /**
     * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
     *
     * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
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

    @Column(name = "CAMT")
	public Double getCAMT()
	{
		return CAMT;
	}

	public void setCAMT(Double cAMT)
	{
		CAMT = cAMT;
	}

	@Column(name = "BAMT")
	public Double getBAMT()
	{
		return BAMT;
	}

	public void setBAMT(Double bAMT)
	{
		BAMT = bAMT;
	}

	@Column(name = "TPAM")
	public Double getTPAM()
	{
		return TPAM;
	}

	public void setTPAM(Double tPAM)
	{
		TPAM = tPAM;
	}

	@Column(name = "TAMT")
	public Double getTAMT()
	{
		return TAMT;
	}

	public void setTAMT(Double tAMT)
	{
		TAMT = tAMT;
	}

	@Column(name = "comments")
	public String getComments()
    {
        return comments;
    }

	public void setComments( String comments )
    {
        this.comments = comments;
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
								    * @spring.validator type="required"
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

	   
	   
	   
	   
	   @Column(name = "OT_RETRY_COUNT")
	   public Long getOtRetryCount() {
	      return otRetryCount;
	   }

	   public void setOtRetryCount(Long otRetryCount) {
	     this.otRetryCount = otRetryCount;
	   }

	   @Transient
	   public String getPlainOTP() {
	      return plainOTP;
	   }

	   public void setPlainOTP(String plainOTP) {
	      this.plainOTP = plainOTP;
	   }
   @Column(name = "CHANNEL_ID")
   public String getChannelId() {
      return channelId;
   }

   public void setChannelId(String channelId) {
      this.channelId = channelId;
   }
}