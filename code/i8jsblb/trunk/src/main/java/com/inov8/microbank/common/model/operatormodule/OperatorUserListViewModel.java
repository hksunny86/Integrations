package com.inov8.microbank.common.model.operatormodule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The OperatorUserListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="OperatorUserListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "OPERATOR_USER_LIST_VIEW")
public class OperatorUserListViewModel extends BasePersistableModel implements Serializable {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -7961435920133067439L;
private Long operatorUserId;
   private String comments;
   private String description;
   private Long createdBy;
   private Long updatedBy;
   private Date createdOn;
   private Date updatedOn;
   private String firstName;
   private String lastName;
   private String passwordHint;
   private String address1;
   private String address2;
   private String city;
   private String state;
   private String country;
   private String zip;
   private String nic;
   private String email;
   private String username;
   private String password;
   private String mobileNo;
   private Date dob;
   private Long mobileTypeId;
   private Long appUserId;
   private Long operatorId;
   private String name;
   private String fax;
   private String motherMaidenName;
   private Boolean accountEnabled;
   private Boolean accountExpired;
   private Boolean accountLocked;
   private Boolean credentialsExpired;
   private Boolean verified;
   private Long partnerGroupId;
   private String partnerGroupName;

   /**
    * Default constructor.
    */
   public OperatorUserListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getOperatorUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setOperatorUserId(primaryKey);
    }

   /**
    * Returns the value of the <code>operatorUserId</code> property.
    *
    */
      @Column(name = "OPERATOR_USER_ID" , nullable = false )
   @Id 
   public Long getOperatorUserId() {
      return operatorUserId;
   }

   /**
    * Sets the value of the <code>operatorUserId</code> property.
    *
    * @param operatorUserId the value for the <code>operatorUserId</code> property
    *    
		    */

   public void setOperatorUserId(Long operatorUserId) {
      this.operatorUserId = operatorUserId;
   }

   /**
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS"  , length=250 )
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
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments(String comments) {
      this.comments = comments;
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
    * Returns the value of the <code>updatedBy</code> property.
    *
    */
      @Column(name = "UPDATED_BY"  )
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
      @Column(name = "PASSWORD" , nullable = false , length=250 )
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
    * @spring.validator-var name="maxlength" value="250"
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
    * Returns the value of the <code>operatorId</code> property.
    *
    */
      @Column(name = "OPERATOR_ID" , nullable = false )
   public Long getOperatorId() {
      return operatorId;
   }

   /**
    * Sets the value of the <code>operatorId</code> property.
    *
    * @param operatorId the value for the <code>operatorId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setOperatorId(Long operatorId) {
      this.operatorId = operatorId;
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
    * Returns the value of the <code>partnerGroupId</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_ID"  )
   public Long getPartnerGroupId() {
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

   public void setPartnerGroupId(Long partnerGroupId) {
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
        checkBox += "_"+ getOperatorUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&operatorUserId=" + getOperatorUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "operatorUserId";
			return primaryKeyFieldName;				
    }       
}
