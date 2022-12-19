package com.inov8.microbank.common.model.portal.concernmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernHistoryListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernHistoryListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CONCERN_OP_HISTORY_LIST_VIEW")
public class ConcernOpHistoryListViewModel extends BasePersistableModel {
  



   private Long pk;
   private Long concernId;
   private Long initiatorPartnerId;
   private Long recipientPartnerId;
   private String initiatorPartnerName;
   private String recipientPartnerName;
   private Long concernCategoryId;
   private String concernCategoryName;
   private Long concernStatusId;
   private String concernStatusName;
   private Long concernPriorityId;
   private String concernPriorityName;
   private String comments;
   private String concernCode;
   private Long createdBy;
   private Date createdOn;
   private Long updatedBy;
   private Date updatedOn;
   private String title;
   private Long parentConcernId;
   private String description;
   private String historyComments;
   private Long concernHistoryId;
   private Long initiatorConcernStatusId;
   private String initiatorConcernStatusName;
   private Long recipientConcernStatusId;
   private String recipientConcernStatusName;
   private Long historyUpdatedBy;
   private Date historyUpdatedOn;
   private String commentBy;
   private String commentByRecipient;
   private Boolean active;

   /**
    * Default constructor.
    */
   public ConcernOpHistoryListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPk(primaryKey);
    }

   /**
    * Returns the value of the <code>pk</code> property.
    *
    */
      @Column(name = "PK"  )
   @Id 
   public Long getPk() {
      return pk;
   }

   /**
    * Sets the value of the <code>pk</code> property.
    *
    * @param pk the value for the <code>pk</code> property
    *    
		    */

   public void setPk(Long pk) {
      this.pk = pk;
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
    * Returns the value of the <code>concernId</code> property.
    *
    */
      @Column(name = "CONCERN_ID" , nullable = false )
   public Long getConcernId() {
      return concernId;
   }

   /**
    * Sets the value of the <code>concernId</code> property.
    *
    * @param concernId the value for the <code>concernId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernId(Long concernId) {
      this.concernId = concernId;
   }

   /**
    * Returns the value of the <code>initiatorPartnerId</code> property.
    *
    */
      @Column(name = "INITIATOR_PARTNER_ID" , nullable = false )
   public Long getInitiatorPartnerId() {
      return initiatorPartnerId;
   }

   /**
    * Sets the value of the <code>initiatorPartnerId</code> property.
    *
    * @param initiatorPartnerId the value for the <code>initiatorPartnerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setInitiatorPartnerId(Long initiatorPartnerId) {
      this.initiatorPartnerId = initiatorPartnerId;
   }

   /**
    * Returns the value of the <code>recipientPartnerId</code> property.
    *
    */
      @Column(name = "RECIPIENT_PARTNER_ID" , nullable = false )
   public Long getRecipientPartnerId() {
      return recipientPartnerId;
   }

   /**
    * Sets the value of the <code>recipientPartnerId</code> property.
    *
    * @param recipientPartnerId the value for the <code>recipientPartnerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setRecipientPartnerId(Long recipientPartnerId) {
      this.recipientPartnerId = recipientPartnerId;
   }

   /**
    * Returns the value of the <code>initiatorPartnerName</code> property.
    *
    */
      @Column(name = "INITIATOR_PARTNER_NAME"  , length=50 )
   public String getInitiatorPartnerName() {
      return initiatorPartnerName;
   }

   /**
    * Sets the value of the <code>initiatorPartnerName</code> property.
    *
    * @param initiatorPartnerName the value for the <code>initiatorPartnerName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setInitiatorPartnerName(String initiatorPartnerName) {
      this.initiatorPartnerName = initiatorPartnerName;
   }
   
   
   @Column(name = "COMMENT_BY"  , length=50 )
   public String getCommentBy() {
      return commentBy;
   }

   public void setCommentBy(String commentBy) {
      this.commentBy = commentBy;
   }   
   
   @Column(name = "COMMENT_BY_RECIPIENT"  , length=50 )
   public String getCommentByRecipient() {
      return commentByRecipient;
   }

   public void setCommentByRecipient(String commentByRecipient) {
      this.commentByRecipient = commentByRecipient;
   }   
   
   /**
    * Returns the value of the <code>recipientPartnerName</code> property.
    *
    */
      @Column(name = "RECIPIENT_PARTNER_NAME"  , length=50 )
   public String getRecipientPartnerName() {
      return recipientPartnerName;
   }

   /**
    * Sets the value of the <code>recipientPartnerName</code> property.
    *
    * @param recipientPartnerName the value for the <code>recipientPartnerName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setRecipientPartnerName(String recipientPartnerName) {
      this.recipientPartnerName = recipientPartnerName;
   }

   /**
    * Returns the value of the <code>concernCategoryId</code> property.
    *
    */
      @Column(name = "CONCERN_CATEGORY_ID" , nullable = false )
   public Long getConcernCategoryId() {
      return concernCategoryId;
   }

   /**
    * Sets the value of the <code>concernCategoryId</code> property.
    *
    * @param concernCategoryId the value for the <code>concernCategoryId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernCategoryId(Long concernCategoryId) {
      this.concernCategoryId = concernCategoryId;
   }

   /**
    * Returns the value of the <code>concernCategoryName</code> property.
    *
    */
      @Column(name = "CONCERN_CATEGORY_NAME"  , length=50 )
   public String getConcernCategoryName() {
      return concernCategoryName;
   }

   /**
    * Sets the value of the <code>concernCategoryName</code> property.
    *
    * @param concernCategoryName the value for the <code>concernCategoryName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setConcernCategoryName(String concernCategoryName) {
      this.concernCategoryName = concernCategoryName;
   }

   /**
    * Returns the value of the <code>concernStatusId</code> property.
    *
    */
      @Column(name = "CONCERN_STATUS_ID" , nullable = false )
   public Long getConcernStatusId() {
      return concernStatusId;
   }

   /**
    * Sets the value of the <code>concernStatusId</code> property.
    *
    * @param concernStatusId the value for the <code>concernStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernStatusId(Long concernStatusId) {
      this.concernStatusId = concernStatusId;
   }

   /**
    * Returns the value of the <code>concernStatusName</code> property.
    *
    */
      @Column(name = "CONCERN_STATUS_NAME"  , length=50 )
   public String getConcernStatusName() {
      return concernStatusName;
   }

   /**
    * Sets the value of the <code>concernStatusName</code> property.
    *
    * @param concernStatusName the value for the <code>concernStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setConcernStatusName(String concernStatusName) {
      this.concernStatusName = concernStatusName;
   }

   /**
    * Returns the value of the <code>concernPriorityId</code> property.
    *
    */
      @Column(name = "CONCERN_PRIORITY_ID" , nullable = false )
   public Long getConcernPriorityId() {
      return concernPriorityId;
   }

   /**
    * Sets the value of the <code>concernPriorityId</code> property.
    *
    * @param concernPriorityId the value for the <code>concernPriorityId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernPriorityId(Long concernPriorityId) {
      this.concernPriorityId = concernPriorityId;
   }

   /**
    * Returns the value of the <code>concernPriorityName</code> property.
    *
    */
      @Column(name = "CONCERN_PRIORITY_NAME"  , length=50 )
   public String getConcernPriorityName() {
      return concernPriorityName;
   }

   /**
    * Sets the value of the <code>concernPriorityName</code> property.
    *
    * @param concernPriorityName the value for the <code>concernPriorityName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setConcernPriorityName(String concernPriorityName) {
      this.concernPriorityName = concernPriorityName;
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
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="1000"
    */

   public void setComments(String comments) {
      this.comments = comments;
   }

   /**
    * Returns the value of the <code>concernCode</code> property.
    *
    */
      @Column(name = "CONCERN_CODE" , nullable = false , length=50 )
   public String getConcernCode() {
      return concernCode;
   }

   /**
    * Sets the value of the <code>concernCode</code> property.
    *
    * @param concernCode the value for the <code>concernCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setConcernCode(String concernCode) {
      this.concernCode = concernCode;
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
    * Returns the value of the <code>title</code> property.
    *
    */
      @Column(name = "TITLE" , nullable = false , length=250 )
   public String getTitle() {
      return title;
   }

   /**
    * Sets the value of the <code>title</code> property.
    *
    * @param title the value for the <code>title</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setTitle(String title) {
      this.title = title;
   }

   /**
    * Returns the value of the <code>parentConcernId</code> property.
    *
    */
      @Column(name = "PARENT_CONCERN_ID"  )
   public Long getParentConcernId() {
      return parentConcernId;
   }

   /**
    * Sets the value of the <code>parentConcernId</code> property.
    *
    * @param parentConcernId the value for the <code>parentConcernId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setParentConcernId(Long parentConcernId) {
      this.parentConcernId = parentConcernId;
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
    * Returns the value of the <code>historyComments</code> property.
    *
    */
      @Column(name = "HISTORY_COMMENTS" , nullable = false , length=1000 )
   public String getHistoryComments() {
      return historyComments;
   }

   /**
    * Sets the value of the <code>historyComments</code> property.
    *
    * @param historyComments the value for the <code>historyComments</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="1000"
    */

   public void setHistoryComments(String historyComments) {
      this.historyComments = historyComments;
   }

   /**
    * Returns the value of the <code>concernHistoryId</code> property.
    *
    */
      @Column(name = "CONCERN_HISTORY_ID" , nullable = false )
   public Long getConcernHistoryId() {
      return concernHistoryId;
   }

   /**
    * Sets the value of the <code>concernHistoryId</code> property.
    *
    * @param concernHistoryId the value for the <code>concernHistoryId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernHistoryId(Long concernHistoryId) {
      this.concernHistoryId = concernHistoryId;
   }

   /**
    * Returns the value of the <code>initiatorConcernStatusId</code> property.
    *
    */
      @Column(name = "INITIATOR_CONCERN_STATUS_ID" , nullable = false )
   public Long getInitiatorConcernStatusId() {
      return initiatorConcernStatusId;
   }

   /**
    * Sets the value of the <code>initiatorConcernStatusId</code> property.
    *
    * @param initiatorConcernStatusId the value for the <code>initiatorConcernStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setInitiatorConcernStatusId(Long initiatorConcernStatusId) {
      this.initiatorConcernStatusId = initiatorConcernStatusId;
   }

   /**
    * Returns the value of the <code>initiatorConcernStatusName</code> property.
    *
    */
      @Column(name = "INITIATOR_CONCERN_STATUS_NAME"  , length=50 )
   public String getInitiatorConcernStatusName() {
      return initiatorConcernStatusName;
   }

   /**
    * Sets the value of the <code>initiatorConcernStatusName</code> property.
    *
    * @param initiatorConcernStatusName the value for the <code>initiatorConcernStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setInitiatorConcernStatusName(String initiatorConcernStatusName) {
      this.initiatorConcernStatusName = initiatorConcernStatusName;
   }

   /**
    * Returns the value of the <code>recipientConcernStatusId</code> property.
    *
    */
      @Column(name = "RECIPIENT_CONCERN_STATUS_ID" , nullable = false )
   public Long getRecipientConcernStatusId() {
      return recipientConcernStatusId;
   }

   /**
    * Sets the value of the <code>recipientConcernStatusId</code> property.
    *
    * @param recipientConcernStatusId the value for the <code>recipientConcernStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setRecipientConcernStatusId(Long recipientConcernStatusId) {
      this.recipientConcernStatusId = recipientConcernStatusId;
   }

   /**
    * Returns the value of the <code>recipientConcernStatusName</code> property.
    *
    */
      @Column(name = "RECIPIENT_CONCERN_STATUS_NAME"  , length=50 )
   public String getRecipientConcernStatusName() {
      return recipientConcernStatusName;
   }

   /**
    * Sets the value of the <code>recipientConcernStatusName</code> property.
    *
    * @param recipientConcernStatusName the value for the <code>recipientConcernStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setRecipientConcernStatusName(String recipientConcernStatusName) {
      this.recipientConcernStatusName = recipientConcernStatusName;
   }

   /**
    * Returns the value of the <code>historyUpdatedBy</code> property.
    *
    */
      @Column(name = "HISTORY_UPDATED_BY" , nullable = false )
   public Long getHistoryUpdatedBy() {
      return historyUpdatedBy;
   }

   /**
    * Sets the value of the <code>historyUpdatedBy</code> property.
    *
    * @param historyCreatedBy the value for the <code>historyUpdatedBy</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setHistoryUpdatedBy(Long historyUpdatedBy) {
      this.historyUpdatedBy = historyUpdatedBy;
   }

   /**
    * Returns the value of the <code>historyUpdatedOn</code> property.
    *
    */
      @Column(name = "HISTORY_UPDATED_ON" , nullable = false )
   public Date getHistoryUpdatedOn() {
      return historyUpdatedOn;
   }

   /**
    * Sets the value of the <code>historyUpdatedOn</code> property.
    *
    * @param historyUpdatedOn the value for the <code>historyUpdatedOn</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setHistoryUpdatedOn(Date historyUpdatedOn) {
      this.historyUpdatedOn = historyUpdatedOn;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getPk();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }       
}
