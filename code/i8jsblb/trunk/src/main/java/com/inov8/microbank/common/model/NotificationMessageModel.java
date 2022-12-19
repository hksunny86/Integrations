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
 * The NotificationMessageModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="NotificationMessageModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "NOTIFICATION_MESSAGE_seq",sequenceName = "NOTIFICATION_MESSAGE_seq", allocationSize=1)
@Table(name = "NOTIFICATION_MESSAGE")
public class NotificationMessageModel extends BasePersistableModel implements Serializable{
  

   private MessageTypeModel messageTypeIdMessageTypeModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private Collection<ProductModel> instructionIdProductModelList = new ArrayList<ProductModel>();
   private Collection<ProductModel> successMessageIdProductModelList = new ArrayList<ProductModel>();
   private Collection<ProductModel> helpLineProductModelList = new ArrayList<ProductModel>();
   private Collection<ProductModel> failureMessageIdProductModelList = new ArrayList<ProductModel>();

   private Long notificationMessageId;
   private String smsMessageText;
   private String emailMessageText;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public NotificationMessageModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getNotificationMessageId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setNotificationMessageId(primaryKey);
    }

   /**
    * Returns the value of the <code>notificationMessageId</code> property.
    *
    */
      @Column(name = "NOTIFICATION_MESSAGE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NOTIFICATION_MESSAGE_seq")
   public Long getNotificationMessageId() {
      return notificationMessageId;
   }

   /**
    * Sets the value of the <code>notificationMessageId</code> property.
    *
    * @param notificationMessageId the value for the <code>notificationMessageId</code> property
    *    
		    */

   public void setNotificationMessageId(Long notificationMessageId) {
      this.notificationMessageId = notificationMessageId;
   }

   /**
    * Returns the value of the <code>smsMessageText</code> property.
    *
    */
      @Column(name = "SMS_MESSAGE_TEXT" , nullable = false , length=250 )
   public String getSmsMessageText() {
      return smsMessageText;
   }

   /**
    * Sets the value of the <code>smsMessageText</code> property.
    *
    * @param smsMessageText the value for the <code>smsMessageText</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setSmsMessageText(String smsMessageText) {
      this.smsMessageText = smsMessageText;
   }

   /**
    * Returns the value of the <code>emailMessageText</code> property.
    *
    */
      @Column(name = "EMAIL_MESSAGE_TEXT" , nullable = false , length=250 )
   public String getEmailMessageText() {
      return emailMessageText;
   }

   /**
    * Sets the value of the <code>emailMessageText</code> property.
    *
    * @param emailMessageText the value for the <code>emailMessageText</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setEmailMessageText(String emailMessageText) {
      this.emailMessageText = emailMessageText;
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
    * Returns the value of the <code>messageTypeIdMessageTypeModel</code> relation property.
    *
    * @return the value of the <code>messageTypeIdMessageTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "MESSAGE_TYPE_ID")    
   public MessageTypeModel getRelationMessageTypeIdMessageTypeModel(){
      return messageTypeIdMessageTypeModel;
   }
    
   /**
    * Returns the value of the <code>messageTypeIdMessageTypeModel</code> relation property.
    *
    * @return the value of the <code>messageTypeIdMessageTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public MessageTypeModel getMessageTypeIdMessageTypeModel(){
      return getRelationMessageTypeIdMessageTypeModel();
   }

   /**
    * Sets the value of the <code>messageTypeIdMessageTypeModel</code> relation property.
    *
    * @param messageTypeModel a value for <code>messageTypeIdMessageTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationMessageTypeIdMessageTypeModel(MessageTypeModel messageTypeModel) {
      this.messageTypeIdMessageTypeModel = messageTypeModel;
   }
   
   /**
    * Sets the value of the <code>messageTypeIdMessageTypeModel</code> relation property.
    *
    * @param messageTypeModel a value for <code>messageTypeIdMessageTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setMessageTypeIdMessageTypeModel(MessageTypeModel messageTypeModel) {
      if(null != messageTypeModel)
      {
      	setRelationMessageTypeIdMessageTypeModel((MessageTypeModel)messageTypeModel.clone());
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
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */
    
   public void addInstructionIdProductModel(ProductModel productModel) {
      productModel.setRelationInstructionIdNotificationMessageModel(this);
      instructionIdProductModelList.add(productModel);
   }
   
   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */
   
   public void removeInstructionIdProductModel(ProductModel productModel) {      
      productModel.setRelationInstructionIdNotificationMessageModel(null);
      instructionIdProductModelList.remove(productModel);      
   }

   /**
    * Get a list of related ProductModel objects of the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the InstructionId member.
    *
    * @return Collection of ProductModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationInstructionIdNotificationMessageModel")
   @JoinColumn(name = "INSTRUCTION_ID")
   public Collection<ProductModel> getInstructionIdProductModelList() throws Exception {
   		return instructionIdProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the InstructionId member.
    *
    * @param productModelList the list of related objects.
    */
    public void setInstructionIdProductModelList(Collection<ProductModel> productModelList) throws Exception {
		this.instructionIdProductModelList = productModelList;
   }


   /**
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */
    
   public void addSuccessMessageIdProductModel(ProductModel productModel) {
      productModel.setRelationSuccessMessageIdNotificationMessageModel(this);
      successMessageIdProductModelList.add(productModel);
   }
   
   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */
   
   public void removeSuccessMessageIdProductModel(ProductModel productModel) {      
      productModel.setRelationSuccessMessageIdNotificationMessageModel(null);
      successMessageIdProductModelList.remove(productModel);      
   }

   /**
    * Get a list of related ProductModel objects of the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the SuccessMessageId member.
    *
    * @return Collection of ProductModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSuccessMessageIdNotificationMessageModel")
   @JoinColumn(name = "SUCCESS_MESSAGE_ID")
   public Collection<ProductModel> getSuccessMessageIdProductModelList() throws Exception {
   		return successMessageIdProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the SuccessMessageId member.
    *
    * @param productModelList the list of related objects.
    */
    public void setSuccessMessageIdProductModelList(Collection<ProductModel> productModelList) throws Exception {
		this.successMessageIdProductModelList = productModelList;
   }


   /**
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */
    
   public void addHelpLineProductModel(ProductModel productModel) {
      productModel.setRelationHelpLineNotificationMessageModel(this);
      helpLineProductModelList.add(productModel);
   }
   
   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */
   
   public void removeHelpLineProductModel(ProductModel productModel) {      
      productModel.setRelationHelpLineNotificationMessageModel(null);
      helpLineProductModelList.remove(productModel);      
   }

   /**
    * Get a list of related ProductModel objects of the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the HelpLine member.
    *
    * @return Collection of ProductModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationHelpLineNotificationMessageModel")
   @JoinColumn(name = "HELP_LINE")
   public Collection<ProductModel> getHelpLineProductModelList() throws Exception {
   		return helpLineProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the HelpLine member.
    *
    * @param productModelList the list of related objects.
    */
    public void setHelpLineProductModelList(Collection<ProductModel> productModelList) throws Exception {
		this.helpLineProductModelList = productModelList;
   }


   /**
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */
    
   public void addFailureMessageIdProductModel(ProductModel productModel) {
      productModel.setRelationFailureMessageIdNotificationMessageModel(this);
      failureMessageIdProductModelList.add(productModel);
   }
   
   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */
   
   public void removeFailureMessageIdProductModel(ProductModel productModel) {      
      productModel.setRelationFailureMessageIdNotificationMessageModel(null);
      failureMessageIdProductModelList.remove(productModel);      
   }

   /**
    * Get a list of related ProductModel objects of the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the FailureMessageId member.
    *
    * @return Collection of ProductModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFailureMessageIdNotificationMessageModel")
   @JoinColumn(name = "FAILURE_MESSAGE_ID")
   public Collection<ProductModel> getFailureMessageIdProductModelList() throws Exception {
   		return failureMessageIdProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the NotificationMessageModel object.
    * These objects are in a bidirectional one-to-many relation by the FailureMessageId member.
    *
    * @param productModelList the list of related objects.
    */
    public void setFailureMessageIdProductModelList(Collection<ProductModel> productModelList) throws Exception {
		this.failureMessageIdProductModelList = productModelList;
   }



   /**
    * Returns the value of the <code>messageTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getMessageTypeId() {
      if (messageTypeIdMessageTypeModel != null) {
         return messageTypeIdMessageTypeModel.getMessageTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>messageTypeId</code> property.
    *
    * @param messageTypeId the value for the <code>messageTypeId</code> property
					    * @spring.validator type="required"
																	    */
   
   @javax.persistence.Transient
   public void setMessageTypeId(Long messageTypeId) {
      if(messageTypeId == null)
      {      
      	messageTypeIdMessageTypeModel = null;
      }
      else
      {
        messageTypeIdMessageTypeModel = new MessageTypeModel();
      	messageTypeIdMessageTypeModel.setMessageTypeId(messageTypeId);
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
        checkBox += "_"+ getNotificationMessageId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&notificationMessageId=" + getNotificationMessageId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "notificationMessageId";
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
    	
    	associationModel.setClassName("MessageTypeModel");
    	associationModel.setPropertyName("relationMessageTypeIdMessageTypeModel");   		
   		associationModel.setValue(getRelationMessageTypeIdMessageTypeModel());
   		
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
