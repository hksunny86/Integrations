package com.inov8.microbank.common.model.portal.usermanagementmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The UserManagementListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UserManagementListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "USER_MANAGEMENT_LIST_VIEW")
public class UserManagementListViewModel extends BasePersistableModel implements Serializable {
  
   private static final long serialVersionUID = 813272814974493318L;
   private Long appUserId;
   private String firstName;
   private String lastName;
   private Long createdBy;
   private String username;
   private Long appUserTypeId;
   private Boolean accountEnabled;
   private Boolean accountExpired;
   private Boolean accountLocked;
   private Boolean credentialsExpired;
   private Long bankUserId;
   private Long bankId;
   private Long employeeId;
   private String partnerGroupId;
   private String partnerGroupName;

   //transient added by atif hussain
   private String accountStatus;
   
   /**
    * Default constructor.
    */
   public UserManagementListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAppUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAppUserId(primaryKey);
    }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID" , nullable = false )
   @Id 
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *    
		    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
   }

   /**
    * Returns the value of the <code>firstName</code> property.
    *
    */
      @Column(name = "FIRST_NAME" , nullable = false , length=50 )
   public String getFirstName() {
      return firstName;
   }

   /**
    * Sets the value of the <code>firstName</code> property.
    *
    * @param firstName the value for the <code>firstName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   /**
    * Returns the value of the <code>lastName</code> property.
    *
    */
      @Column(name = "LAST_NAME" , nullable = false , length=50 )
   public String getLastName() {
      return lastName;
   }

   /**
    * Sets the value of the <code>lastName</code> property.
    *
    * @param lastName the value for the <code>lastName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   /**
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY"  )
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
    * Returns the value of the <code>username</code> property.
    *
    */
      @Column(name = "USERNAME" , nullable = false , length=50 )
   public String getUsername() {
      return username;
   }

   /**
    * Sets the value of the <code>username</code> property.
    *
    * @param username the value for the <code>username</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUsername(String username) {
      this.username = username;
   }

   /**
    * Returns the value of the <code>appUserTypeId</code> property.
    *
    */
      @Column(name = "APP_USER_TYPE_ID" , nullable = false )
   public Long getAppUserTypeId() {
      return appUserTypeId;
   }

   /**
    * Sets the value of the <code>appUserTypeId</code> property.
    *
    * @param appUserTypeId the value for the <code>appUserTypeId</code> property
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
    * Returns the value of the <code>accountEnabled</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_ENABLED" , nullable = false )
   public Boolean getAccountEnabled() {
      return accountEnabled;
   }

   /**
    * Sets the value of the <code>accountEnabled</code> property.
    *
    * @param accountEnabled the value for the <code>accountEnabled</code> property
    *    
		    */

   public void setAccountEnabled(Boolean accountEnabled) {
      this.accountEnabled = accountEnabled;
   }

   /**
    * Returns the value of the <code>accountExpired</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_EXPIRED" , nullable = false )
   public Boolean getAccountExpired() {
      return accountExpired;
   }

   /**
    * Sets the value of the <code>accountExpired</code> property.
    *
    * @param accountExpired the value for the <code>accountExpired</code> property
    *    
		    */

   public void setAccountExpired(Boolean accountExpired) {
      this.accountExpired = accountExpired;
   }

   /**
    * Returns the value of the <code>accountLocked</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_LOCKED" , nullable = false )
   public Boolean getAccountLocked() {
      return accountLocked;
   }

   /**
    * Sets the value of the <code>accountLocked</code> property.
    *
    * @param accountLocked the value for the <code>accountLocked</code> property
    *    
		    */

   public void setAccountLocked(Boolean accountLocked) {
      this.accountLocked = accountLocked;
   }

   /**
    * Returns the value of the <code>credentialsExpired</code> property.
    *
    */
      @Column(name = "IS_CREDENTIALS_EXPIRED" , nullable = false )
   public Boolean getCredentialsExpired() {
      return credentialsExpired;
   }

   /**
    * Sets the value of the <code>credentialsExpired</code> property.
    *
    * @param credentialsExpired the value for the <code>credentialsExpired</code> property
    *    
		    */

   public void setCredentialsExpired(Boolean credentialsExpired) {
      this.credentialsExpired = credentialsExpired;
   }

   /**
    * Returns the value of the <code>bankUserId</code> property.
    *
    */
      @Column(name = "BANK_USER_ID"  )
   public Long getBankUserId() {
      return bankUserId;
   }

   /**
    * Sets the value of the <code>bankUserId</code> property.
    *
    * @param bankUserId the value for the <code>bankUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBankUserId(Long bankUserId) {
      this.bankUserId = bankUserId;
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
      @Column(name = "BANK_ID"  )
   public Long getBankId() {
      return bankId;
   }
      @Column(name = "EMPLOYEE_ID"  )
   public Long getEmployeeId() {
      return employeeId;
   }

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

/**
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBankId(Long bankId) {
      this.bankId = bankId;
   }

   /**
    * Returns the value of the <code>partnerGroupId</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_ID"  )
   public String getPartnerGroupId() {
      return partnerGroupId;
   }

   /**
    * Sets the value of the <code>partnerGroupId</code> property.
    *
    * @param partnerGroupId the value for the <code>partnerGroupId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    */

   public void setPartnerGroupId(String partnerGroupId) {
      this.partnerGroupId = partnerGroupId;
   }

   /**
    * Returns the value of the <code>partnerGroupName</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_NAME"  )
   public String getPartnerGroupName() {
      return partnerGroupName;
   }

   /**
    * Sets the value of the <code>partnerGroupName</code> property.
    *
    * @param partnerGroupName the value for the <code>partnerGroupName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setPartnerGroupName(String partnerGroupName) {
      this.partnerGroupName = partnerGroupName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAppUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&appUserId=" + getAppUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "appUserId";
			return primaryKeyFieldName;				
    }
    
    @javax.persistence.Transient
    public String getAccountStatus()
    {
    	if(accountEnabled)
			accountStatus = "Active";
    	else
    		accountStatus = "Inactive";
    	return accountStatus;
    }       

}