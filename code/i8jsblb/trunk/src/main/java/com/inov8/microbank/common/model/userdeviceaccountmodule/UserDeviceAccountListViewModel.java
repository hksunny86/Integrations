package com.inov8.microbank.common.model.userdeviceaccountmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The UserDeviceAccountListViewModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UserDeviceAccountListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "USER_DEVICE_ACCOUNT_LIST_VIEW")
public class UserDeviceAccountListViewModel extends BasePersistableModel {
  



   private Long userDeviceAccountsId;
   private String mobileNo;
   private Long appUserId;
   private String userId;
   private String pin;
   private Boolean accountExpired;
   private Boolean accountEnabled;
   private Boolean accountLocked;
   private Boolean credentialsExpired;
   private Long createdBy;
   private Long updatedBy;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Boolean pinChangeRequired;
   private Date expiryDate;
   private java.sql.Timestamp lastLoginAttemptTime;
   private Integer loginAttemptCount;
   private String firstName;
   private String lastName;
   private Boolean appuserAccountEnabled;
   private Boolean commissioned;
   private Long deviceTypeId;
   private Long appUserTypeId;

/**
    * Default constructor.
    */
   public UserDeviceAccountListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUserDeviceAccountsId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUserDeviceAccountsId(primaryKey);
    }

   /**
    * Returns the value of the <code>userDeviceAccountsId</code> property.
    *
    */
      @Column(name = "USER_DEVICE_ACCOUNTS_ID" , nullable = false )
   @Id 
   public Long getUserDeviceAccountsId() {
      return userDeviceAccountsId;
   }

   /**
    * Sets the value of the <code>userDeviceAccountsId</code> property.
    *
    * @param userDeviceAccountsId the value for the <code>userDeviceAccountsId</code> property
    *    
		    */

   public void setUserDeviceAccountsId(Long userDeviceAccountsId) {
      this.userDeviceAccountsId = userDeviceAccountsId;
   }
   
   /**
    * Returns the value of the <code>commissioned</code> property.
    *
    */
      @Column(name = "IS_COMMISSIONED" , nullable = false )
   public Boolean getCommissioned() {
      return commissioned;
   }

   /**
    * Sets the value of the <code>commissioned</code> property.
    *
    * @param commissioned the value for the <code>commissioned</code> property
    *    
		    */

   public void setCommissioned(Boolean commissioned) {
      this.commissioned = commissioned;
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
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID" , nullable = false )
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
   }

   /**
    * Returns the value of the <code>userId</code> property.
    *
    */
      @Column(name = "USER_ID"  , length=50 )
   public String getUserId() {
      return userId;
   }

   /**
    * Sets the value of the <code>userId</code> property.
    *
    * @param userId the value for the <code>userId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Returns the value of the <code>pin</code> property.
    *
    */
      @Column(name = "PIN"  , length=250 )
   public String getPin() {
      return pin;
   }

   /**
    * Sets the value of the <code>pin</code> property.
    *
    * @param pin the value for the <code>pin</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setPin(String pin) {
      this.pin = pin;
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
    * Returns the value of the <code>pinChangeRequired</code> property.
    *
    */
      @Column(name = "IS_PIN_CHANGE_REQUIRED" , nullable = false )
   public Boolean getPinChangeRequired() {
      return pinChangeRequired;
   }

   /**
    * Sets the value of the <code>pinChangeRequired</code> property.
    *
    * @param pinChangeRequired the value for the <code>pinChangeRequired</code> property
    *    
		    */

   public void setPinChangeRequired(Boolean pinChangeRequired) {
      this.pinChangeRequired = pinChangeRequired;
   }

   /**
    * Returns the value of the <code>expiryDate</code> property.
    *
    */
      @Column(name = "EXPIRY_DATE"  )
   public Date getExpiryDate() {
      return expiryDate;
   }

   /**
    * Sets the value of the <code>expiryDate</code> property.
    *
    * @param expiryDate the value for the <code>expiryDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setExpiryDate(Date expiryDate) {
      this.expiryDate = expiryDate;
   }

   /**
    * Returns the value of the <code>lastLoginAttemptTime</code> property.
    *
    */
      @Column(name = "LAST_LOGIN_ATTEMPT_TIME"  )
   public java.sql.Timestamp getLastLoginAttemptTime() {
      return lastLoginAttemptTime;
   }

   /**
    * Sets the value of the <code>lastLoginAttemptTime</code> property.
    *
    * @param lastLoginAttemptTime the value for the <code>lastLoginAttemptTime</code> property
    *    
		    */

   public void setLastLoginAttemptTime(java.sql.Timestamp lastLoginAttemptTime) {
      this.lastLoginAttemptTime = lastLoginAttemptTime;
   }

   /**
    * Returns the value of the <code>loginAttemptCount</code> property.
    *
    */
      @Column(name = "LOGIN_ATTEMPT_COUNT"  )
   public Integer getLoginAttemptCount() {
      return loginAttemptCount;
   }

   /**
    * Sets the value of the <code>loginAttemptCount</code> property.
    *
    * @param loginAttemptCount the value for the <code>loginAttemptCount</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setLoginAttemptCount(Integer loginAttemptCount) {
      this.loginAttemptCount = loginAttemptCount;
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
    * Returns the value of the <code>appuserAccountEnabled</code> property.
    *
    */
      @Column(name = "IS_APPUSER_ACCOUNT_ENABLED" , nullable = false )
   public Boolean getAppuserAccountEnabled() {
      return appuserAccountEnabled;
   }

   /**
    * Sets the value of the <code>appuserAccountEnabled</code> property.
    *
    * @param appuserAccountEnabled the value for the <code>appuserAccountEnabled</code> property
    *    
		    */

   public void setAppuserAccountEnabled(Boolean appuserAccountEnabled) {
      this.appuserAccountEnabled = appuserAccountEnabled;
   }

   /**
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
   @Column(name = "DEVICE_TYPE_ID" , nullable = false )
   public Long getDeviceTypeId() {
      return deviceTypeId;
   }

   @Column(name = "APP_USER_TYPE_ID" , nullable = false )
    public Long getAppUserTypeId() {
    	return appUserTypeId;
   	}
   	
   	public void setAppUserTypeId(Long appUserTypeId) {
   		this.appUserTypeId = appUserTypeId;
   	}

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDeviceTypeId(Long deviceTypeId) {
      this.deviceTypeId = deviceTypeId;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUserDeviceAccountsId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&userDeviceAccountsId=" + getUserDeviceAccountsId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "userDeviceAccountsId";
			return primaryKeyFieldName;				
    }       
}
