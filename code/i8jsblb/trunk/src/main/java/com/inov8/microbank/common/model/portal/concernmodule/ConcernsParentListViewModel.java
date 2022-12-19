package com.inov8.microbank.common.model.portal.concernmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernsParentListViewModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernsParentListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CONCERNS_PARENT_LIST_VIEW")
public class ConcernsParentListViewModel extends BasePersistableModel {
  



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
   private Long secondaryActiveStatusId;
   private String secondaryActiveStatusName;
   private String comments;
   private String concernCode;
   private Long createdBy;
   private Date createdOn;
   private Long updatedBy;
   private Date updatedOn;
   private String title;
   private Long parentConcernId;
   private String description;

   /**
    * Default constructor.
    */
   public ConcernsParentListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getConcernId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setConcernId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernId</code> property.
    *
    */
      @Column(name = "CONCERN_ID" , nullable = false )
   @Id 
   public Long getConcernId() {
      return concernId;
   }

   /**
    * Sets the value of the <code>concernId</code> property.
    *
    * @param concernId the value for the <code>concernId</code> property
    *    
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
    * Returns the value of the <code>secondaryActiveStatusId</code> property.
    *
    */
      @Column(name = "SECONDARY_ACTIVE_STATUS_ID"  )
   public Long getSecondaryActiveStatusId() {
      return secondaryActiveStatusId;
   }

   /**
    * Sets the value of the <code>secondaryActiveStatusId</code> property.
    *
    * @param secondaryActiveStatusId the value for the <code>secondaryActiveStatusId</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setSecondaryActiveStatusId(Long secondaryActiveStatusId) {
      this.secondaryActiveStatusId = secondaryActiveStatusId;
   }

   /**
    * Returns the value of the <code>secondaryActiveStatusName</code> property.
    *
    */
      @Column(name = "SECONDARY_ACTIVE_STATUS_NAME"  , length=50 )
   public String getSecondaryActiveStatusName() {
      return secondaryActiveStatusName;
   }

   /**
    * Sets the value of the <code>secondaryActiveStatusName</code> property.
    *
    * @param secondaryActiveStatusName the value for the <code>secondaryActiveStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setSecondaryActiveStatusName(String secondaryActiveStatusName) {
      this.secondaryActiveStatusName = secondaryActiveStatusName;
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getConcernId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&concernId=" + getConcernId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "concernId";
			return primaryKeyFieldName;				
    }       
}
