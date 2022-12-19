package com.inov8.microbank.common.model.portal.partnergroupmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The PartnerGroupListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PartnerGroupListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PARTNER_GROUP_LIST_VIEW")
public class PartnerGroupListViewModel extends BasePersistableModel implements Serializable {
  



   private Long partnerGroupId;
   private String name;
   private String email;
   private String description;
   private Long partnerId;
   private String partnerName;
   private Boolean active;
   private Boolean editable;
   private Long appUserTypeId;
   private String appUserTypeName;

   /**
    * Default constructor.
    */
   public PartnerGroupListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPartnerGroupId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPartnerGroupId(primaryKey);
    }

   /**
    * Returns the value of the <code>partnerGroupId</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_ID" , nullable = false )
   @Id 
   public Long getPartnerGroupId() {
      return partnerGroupId;
   }

   /**
    * Sets the value of the <code>partnerGroupId</code> property.
    *
    * @param partnerGroupId the value for the <code>partnerGroupId</code> property
    *    
		    */

   public void setPartnerGroupId(Long partnerGroupId) {
      this.partnerGroupId = partnerGroupId;
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
    * Returns the value of the <code>email</code> property.
    *
    */
      @Column(name = "EMAIL"  , length=100 )
   public String getEmail() {
      return email;
   }

   /**
    * Sets the value of the <code>email</code> property.
    *
    * @param email the value for the <code>email</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="100"
    */

   public void setEmail(String email) {
      this.email = email;
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
    * Returns the value of the <code>partnerId</code> property.
    *
    */
      @Column(name = "PARTNER_ID" , nullable = false )
   public Long getPartnerId() {
      return partnerId;
   }

   /**
    * Sets the value of the <code>partnerId</code> property.
    *
    * @param partnerId the value for the <code>partnerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setPartnerId(Long partnerId) {
      this.partnerId = partnerId;
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
    * Returns the value of the <code>editable</code> property.
    *
    */
      @Column(name = "IS_EDITABLE" , nullable = false )
   public Boolean getEditable() {
      return editable;
   }

   /**
    * Sets the value of the <code>editable</code> property.
    *
    * @param editable the value for the <code>editable</code> property
    *    
		    */

   public void setEditable(Boolean editable) {
      this.editable = editable;
   }


   
   /**
    * Returns the value of the <code>AppUserTypeId</code> property.
    *
    */
      @Column(name = "APP_USER_TYPE_ID" , nullable = false )
   public Long getAppUserTypeId() {
      return appUserTypeId;
   }

   /**
    * Sets the value of the <code>AppUserTypeId</code> property.
    *
    * @param AppUserTypeId the value for the <code>AppUserTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAppUserTypeId(Long appUserTypeId) {
      this.appUserTypeId = appUserTypeId;
   }

   /**
    * Returns the value of the <code>AppUserTypeName</code> property.
    *
    */
      @Column(name = "APP_USER_TYPE_NAME" , nullable = false , length=50 )
   public String getAppUserTypeName() {
      return appUserTypeName;
   }

   /**
    * Sets the value of the <code>appUserTypeName</code> property.
    *
    * @param appUserTypeName the value for the <code>appUserTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setAppUserTypeName(String appUserTypeName) {
      this.appUserTypeName = appUserTypeName;
   }



    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPartnerGroupId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&partnerGroupId=" + getPartnerGroupId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "partnerGroupId";
			return primaryKeyFieldName;				
    }       
}
