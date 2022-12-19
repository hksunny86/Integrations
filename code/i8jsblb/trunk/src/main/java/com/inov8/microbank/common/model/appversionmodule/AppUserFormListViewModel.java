package com.inov8.microbank.common.model.appversionmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AppUserFormListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AppUserFormListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "APP_USER_FORM_LIST_VIEW")
public class AppUserFormListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 828282758257677619L;
private Long appUserId;
   private Long customerId;
   private Long appUserTypeId;
   private String appUserTypeName;
   private Long bankUserId;
   private Long supplierUserId;
   private Long retailerContactId;
   private Long distributorContactId;
   private Long mnoUserId;
   private Long operatorUserId;
   private String firstName;
   private String lastName;
   private String address1;
   private String address2;
   private String city;
   private String state;
   private String country;
   private String zip;
   private String nic;
   private String email;
   private String fax;
   private String motherMaidenName;
   private String username;
   private String password;
   private String mobileNo;
   private String passwordHint;
   private Boolean verified;
   private Boolean accountEnabled;
   private Boolean accountExpired;
   private Boolean accountLocked;
   private Boolean credentialsExpired;
   private Integer versionNo;
   private Date dob;
   private Long mobileTypeId;
   private java.sql.Timestamp lastLoginAttemptTime;
   private Integer loginAttemptCount;

   /**
    * Default constructor.
    */
   public AppUserFormListViewModel() {
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
    * Returns the value of the <code>customerId</code> property.
    *
    */
      @Column(name = "CUSTOMER_ID"  )
   public Long getCustomerId() {
      return customerId;
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
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
    * Returns the value of the <code>appUserTypeName</code> property.
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
    * Returns the value of the <code>supplierUserId</code> property.
    *
    */
      @Column(name = "SUPPLIER_USER_ID"  )
   public Long getSupplierUserId() {
      return supplierUserId;
   }

   /**
    * Sets the value of the <code>supplierUserId</code> property.
    *
    * @param supplierUserId the value for the <code>supplierUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setSupplierUserId(Long supplierUserId) {
      this.supplierUserId = supplierUserId;
   }

   /**
    * Returns the value of the <code>retailerContactId</code> property.
    *
    */
      @Column(name = "RETAILER_CONTACT_ID"  )
   public Long getRetailerContactId() {
      return retailerContactId;
   }

   /**
    * Sets the value of the <code>retailerContactId</code> property.
    *
    * @param retailerContactId the value for the <code>retailerContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setRetailerContactId(Long retailerContactId) {
      this.retailerContactId = retailerContactId;
   }

   /**
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_CONTACT_ID"  )
   public Long getDistributorContactId() {
      return distributorContactId;
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDistributorContactId(Long distributorContactId) {
      this.distributorContactId = distributorContactId;
   }

   /**
    * Returns the value of the <code>mnoUserId</code> property.
    *
    */
      @Column(name = "SERVICE_OP_USER_ID"  )
   public Long getMnoUserId() {
      return mnoUserId;
   }

   /**
    * Sets the value of the <code>mnoUserId</code> property.
    *
    * @param mnoUserId the value for the <code>mnoUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMnoUserId(Long mnoUserId) {
      this.mnoUserId = mnoUserId;
   }

   /**
    * Returns the value of the <code>operatorUserId</code> property.
    *
    */
      @Column(name = "OPERATOR_USER_ID"  )
   public Long getOperatorUserId() {
      return operatorUserId;
   }

   /**
    * Sets the value of the <code>operatorUserId</code> property.
    *
    * @param operatorUserId the value for the <code>operatorUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setOperatorUserId(Long operatorUserId) {
      this.operatorUserId = operatorUserId;
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
    * Returns the value of the <code>address1</code> property.
    *
    */
      @Column(name = "ADDRESS1"  , length=250 )
   public String getAddress1() {
      return address1;
   }

   /**
    * Sets the value of the <code>address1</code> property.
    *
    * @param address1 the value for the <code>address1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress1(String address1) {
      this.address1 = address1;
   }

   /**
    * Returns the value of the <code>address2</code> property.
    *
    */
      @Column(name = "ADDRESS2"  , length=250 )
   public String getAddress2() {
      return address2;
   }

   /**
    * Sets the value of the <code>address2</code> property.
    *
    * @param address2 the value for the <code>address2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress2(String address2) {
      this.address2 = address2;
   }

   /**
    * Returns the value of the <code>city</code> property.
    *
    */
      @Column(name = "CITY"  , length=50 )
   public String getCity() {
      return city;
   }

   /**
    * Sets the value of the <code>city</code> property.
    *
    * @param city the value for the <code>city</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCity(String city) {
      this.city = city;
   }

   /**
    * Returns the value of the <code>state</code> property.
    *
    */
      @Column(name = "STATE"  , length=50 )
   public String getState() {
      return state;
   }

   /**
    * Sets the value of the <code>state</code> property.
    *
    * @param state the value for the <code>state</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setState(String state) {
      this.state = state;
   }

   /**
    * Returns the value of the <code>country</code> property.
    *
    */
      @Column(name = "COUNTRY"  , length=50 )
   public String getCountry() {
      return country;
   }

   /**
    * Sets the value of the <code>country</code> property.
    *
    * @param country the value for the <code>country</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCountry(String country) {
      this.country = country;
   }

   /**
    * Returns the value of the <code>zip</code> property.
    *
    */
      @Column(name = "ZIP"  , length=50 )
   public String getZip() {
      return zip;
   }

   /**
    * Sets the value of the <code>zip</code> property.
    *
    * @param zip the value for the <code>zip</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setZip(String zip) {
      this.zip = zip;
   }

   /**
    * Returns the value of the <code>nic</code> property.
    *
    */
      @Column(name = "NIC"  , length=50 )
   public String getNic() {
      return nic;
   }

   /**
    * Sets the value of the <code>nic</code> property.
    *
    * @param nic the value for the <code>nic</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setNic(String nic) {
      this.nic = nic;
   }

   /**
    * Returns the value of the <code>email</code> property.
    *
    */
      @Column(name = "EMAIL"  , length=50 )
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
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setEmail(String email) {
      this.email = email;
   }

   /**
    * Returns the value of the <code>fax</code> property.
    *
    */
      @Column(name = "FAX"  , length=50 )
   public String getFax() {
      return fax;
   }

   /**
    * Sets the value of the <code>fax</code> property.
    *
    * @param fax the value for the <code>fax</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFax(String fax) {
      this.fax = fax;
   }

   /**
    * Returns the value of the <code>motherMaidenName</code> property.
    *
    */
      @Column(name = "MOTHER_MAIDEN_NAME"  , length=50 )
   public String getMotherMaidenName() {
      return motherMaidenName;
   }

   /**
    * Sets the value of the <code>motherMaidenName</code> property.
    *
    * @param motherMaidenName the value for the <code>motherMaidenName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setMotherMaidenName(String motherMaidenName) {
      this.motherMaidenName = motherMaidenName;
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
    * Returns the value of the <code>password</code> property.
    *
    */
      @Column(name = "PASSWORD" , nullable = false , length=4000 )
   public String getPassword() {
      return password;
   }

   /**
    * Sets the value of the <code>password</code> property.
    *
    * @param password the value for the <code>password</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="4000"
    */

   public void setPassword(String password) {
      this.password = password;
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
    * Returns the value of the <code>passwordHint</code> property.
    *
    */
      @Column(name = "PASSWORD_HINT"  , length=250 )
   public String getPasswordHint() {
      return passwordHint;
   }

   /**
    * Sets the value of the <code>passwordHint</code> property.
    *
    * @param passwordHint the value for the <code>passwordHint</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setPasswordHint(String passwordHint) {
      this.passwordHint = passwordHint;
   }

   /**
    * Returns the value of the <code>verified</code> property.
    *
    */
      @Column(name = "IS_VERIFIED" , nullable = false )
   public Boolean getVerified() {
      return verified;
   }

   /**
    * Sets the value of the <code>verified</code> property.
    *
    * @param verified the value for the <code>verified</code> property
    *    
		    */

   public void setVerified(Boolean verified) {
      this.verified = verified;
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
    * Returns the value of the <code>dob</code> property.
    *
    */
      @Column(name = "DOB"  )
   public Date getDob() {
      return dob;
   }

   /**
    * Sets the value of the <code>dob</code> property.
    *
    * @param dob the value for the <code>dob</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setDob(Date dob) {
      this.dob = dob;
   }

   /**
    * Returns the value of the <code>mobileTypeId</code> property.
    *
    */
      @Column(name = "MOBILE_TYPE_ID" , nullable = false )
   public Long getMobileTypeId() {
      return mobileTypeId;
   }

   /**
    * Sets the value of the <code>mobileTypeId</code> property.
    *
    * @param mobileTypeId the value for the <code>mobileTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMobileTypeId(Long mobileTypeId) {
      this.mobileTypeId = mobileTypeId;
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
}
