package com.inov8.microbank.common.model.notificationmessagemodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The NotificationMessageListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="NotificationMessageListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "NOTIFICATION_MESSAGE_LIST_VIEW")
public class NotificationMessageListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 3332800133768137085L;
private String name;
   private String smsMessageText;
   private String emailMessageText;
   private Long notificationMessageId;
   private Long messageTypeId;
   private Date createdOn;
   private Date updatedOn;
   private Long updatedBy;
   private Long createdBy;

   /**
    * Default constructor.
    */
   public NotificationMessageListViewModel() {
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
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setEmailMessageText(String emailMessageText) {
      this.emailMessageText = emailMessageText;
   }

   /**
    * Returns the value of the <code>notificationMessageId</code> property.
    *
    */
      @Column(name = "NOTIFICATION_MESSAGE_ID" , nullable = false )
   @Id 
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
    * Returns the value of the <code>messageTypeId</code> property.
    *
    */
      @Column(name = "MESSAGE_TYPE_ID" , nullable = false )
   public Long getMessageTypeId() {
      return messageTypeId;
   }

   /**
    * Sets the value of the <code>messageTypeId</code> property.
    *
    * @param messageTypeId the value for the <code>messageTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMessageTypeId(Long messageTypeId) {
      this.messageTypeId = messageTypeId;
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
    * Returns the value of the <code>updatedBy</code> property.
    *
    */
      @Column(name = "UPDATED_BY" , nullable = false )
   public Long getUpdatedBy() {
      return updatedBy;
   }

   /**
    * Sets the value of the <code>updatedBy</code> property.
    *
    * @param updatedBy the value for the <code>updatedBy</code> property
    *    
		    */

   public void setUpdatedBy(Long updatedBy) {
      this.updatedBy = updatedBy;
   }

   /**
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY" , nullable = false )
   public Long getCreatedBy() {
      return createdBy;
   }

   /**
    * Sets the value of the <code>createdBy</code> property.
    *
    * @param createdBy the value for the <code>createdBy</code> property
    *    
		    */

   public void setCreatedBy(Long createdBy) {
      this.createdBy = createdBy;
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
}
