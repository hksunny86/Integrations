package com.inov8.microbank.common.model.portal.concernmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernInActPartnersViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernInActPartnersViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CONCERN_IN_ACT_PARTNER_VIEW")
public class ConcernInActPartnerViewModel extends BasePersistableModel {
  



   private Long concernId;
   private Long recipientPartnerId;
   private String recipientPartnerName;
   private Long concernCategoryId;
   private String concernCategoryName;
   private Long concernStatusId;
   private String concernStatusName;
   private Long concernPriorityId;
   private String concernPriorityName;
   private String comments;
   private String concernCode;
   private Long updatedBy;
   private Date updatedOn;
   private String title;
   private Long parentConcernId;
   private String description;
   private Long concernPartnerId;
   private Long concernPartnerTypeId;
   private String partnerName;
   private Boolean active;

   /**
    * Default constructor.
    */
   public ConcernInActPartnerViewModel() {
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
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_ID" , nullable = false )
   public Long getConcernPartnerId() {
      return concernPartnerId;
   }

   /**
    * Sets the value of the <code>concernPartnerId</code> property.
    *
    * @param concernPartnerId the value for the <code>concernPartnerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernPartnerId(Long concernPartnerId) {
      this.concernPartnerId = concernPartnerId;
   }

   /**
    * Returns the value of the <code>concernPartnerTypeId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_TYPE_ID" , nullable = false )
   public Long getConcernPartnerTypeId() {
      return concernPartnerTypeId;
   }

   /**
    * Sets the value of the <code>concernPartnerTypeId</code> property.
    *
    * @param concernPartnerTypeId the value for the <code>concernPartnerTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernPartnerTypeId(Long concernPartnerTypeId) {
      this.concernPartnerTypeId = concernPartnerTypeId;
   }

   /**
    * Returns the value of the <code>partnerName</code> property.
    *
    */
      @Column(name = "PARTNER_NAME" , nullable = false , length=50 )
   public String getPartnerName() {
      return partnerName;
   }

   /**
    * Sets the value of the <code>partnerName</code> property.
    *
    * @param partnerName the value for the <code>partnerName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setPartnerName(String partnerName) {
      this.partnerName = partnerName;
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
