package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AllpayDeReLinkListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AllpayDeReLinkListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ALLPAY_DE_RE_LINK_LIST_VIEW")
public class AllpayDeReLinkListViewModel extends BasePersistableModel implements Serializable {
  



   private Long pk;
   private String accountNo;
   private String fullAddress;
   private String fullName;
   private Long appUserId;
   private String firstName;
   private String lastName;
   private String nic;
   private String mobileNo;
   private String address1;
   private String address2;
   private String city;
   private String country;
   private String state;
   private String zip;
   private Boolean auVerified;
   private Date dob;
   private Boolean auAccountEnabled;
   private Boolean auAccountExpired;
   private Boolean auAccountLocked;
   private Boolean auCredentialsExpired;
   private String paymentModeName;
   private Long paymentModeId;
   private String accountNick;
   private Long bankId;
   private Boolean smaActive;
   private Long smartMoneyAccountId;
   private String mobileTypeName;
   private Long mobileTypeId;
   private String mfsId;
   private Long userDeviceAccountsId;
   private Boolean udaAccountEnabled;
   private Boolean udaAccountExpired;
   private Boolean udaAccountLocked;
   private Boolean udaCredentialsExpired;
   private Boolean pinChangeRequired;
   private Long customerId;
   private Long retailerContactId;
   private Long distributorContactId;
   private String username;
   private String areaName;
   private Date accountOpeningDate;
   
   /**
    * Default constructor.
    */
   public AllpayDeReLinkListViewModel() {
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

    @Column(name="ACCOUNT_NO", length=50)
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

   /**
    * Returns the value of the <code>fullAddress</code> property.
    *
    */
      @Column(name = "FULL_ADDRESS"  , length=658 )
   public String getFullAddress() {
      return fullAddress;
   }

   /**
    * Sets the value of the <code>fullAddress</code> property.
    *
    * @param fullAddress the value for the <code>fullAddress</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="658"
    */

   public void setFullAddress(String fullAddress) {
      this.fullAddress = fullAddress;
   }

   /**
    * Returns the value of the <code>fullName</code> property.
    *
    */
      @Column(name = "FULL_NAME"  , length=101 )
   public String getFullName() {
      return fullName;
   }

   /**
    * Sets the value of the <code>fullName</code> property.
    *
    * @param fullName the value for the <code>fullName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="101"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFullName(String fullName) {
      this.fullName = fullName;
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
    * Returns the value of the <code>auVerified</code> property.
    *
    */
      @Column(name = "IS_AU_VERIFIED" , nullable = false )
   public Boolean getAuVerified() {
      return auVerified;
   }

   /**
    * Sets the value of the <code>auVerified</code> property.
    *
    * @param auVerified the value for the <code>auVerified</code> property
    *    
		    */

   public void setAuVerified(Boolean auVerified) {
      this.auVerified = auVerified;
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
    * Returns the value of the <code>auAccountEnabled</code> property.
    *
    */
      @Column(name = "IS_AU_ACCOUNT_ENABLED" , nullable = false )
   public Boolean getAuAccountEnabled() {
      return auAccountEnabled;
   }

   /**
    * Sets the value of the <code>auAccountEnabled</code> property.
    *
    * @param auAccountEnabled the value for the <code>auAccountEnabled</code> property
    *    
		    */

   public void setAuAccountEnabled(Boolean auAccountEnabled) {
      this.auAccountEnabled = auAccountEnabled;
   }

   /**
    * Returns the value of the <code>auAccountExpired</code> property.
    *
    */
      @Column(name = "IS_AU_ACCOUNT_EXPIRED" , nullable = false )
   public Boolean getAuAccountExpired() {
      return auAccountExpired;
   }

   /**
    * Sets the value of the <code>auAccountExpired</code> property.
    *
    * @param auAccountExpired the value for the <code>auAccountExpired</code> property
    *    
		    */

   public void setAuAccountExpired(Boolean auAccountExpired) {
      this.auAccountExpired = auAccountExpired;
   }

   /**
    * Returns the value of the <code>auAccountLocked</code> property.
    *
    */
      @Column(name = "IS_AU_ACCOUNT_LOCKED" , nullable = false )
   public Boolean getAuAccountLocked() {
      return auAccountLocked;
   }

   /**
    * Sets the value of the <code>auAccountLocked</code> property.
    *
    * @param auAccountLocked the value for the <code>auAccountLocked</code> property
    *    
		    */

   public void setAuAccountLocked(Boolean auAccountLocked) {
      this.auAccountLocked = auAccountLocked;
   }

   /**
    * Returns the value of the <code>auCredentialsExpired</code> property.
    *
    */
      @Column(name = "IS_AU_CREDENTIALS_EXPIRED" , nullable = false )
   public Boolean getAuCredentialsExpired() {
      return auCredentialsExpired;
   }

   /**
    * Sets the value of the <code>auCredentialsExpired</code> property.
    *
    * @param auCredentialsExpired the value for the <code>auCredentialsExpired</code> property
    *    
		    */

   public void setAuCredentialsExpired(Boolean auCredentialsExpired) {
      this.auCredentialsExpired = auCredentialsExpired;
   }

   /**
    * Returns the value of the <code>paymentModeName</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE_NAME" , nullable = false , length=50 )
   public String getPaymentModeName() {
      return paymentModeName;
   }

   /**
    * Sets the value of the <code>paymentModeName</code> property.
    *
    * @param paymentModeName the value for the <code>paymentModeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setPaymentModeName(String paymentModeName) {
      this.paymentModeName = paymentModeName;
   }

   /**
    * Returns the value of the <code>paymentModeId</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE_ID" , nullable = false )
   public Long getPaymentModeId() {
      return paymentModeId;
   }

   /**
    * Sets the value of the <code>paymentModeId</code> property.
    *
    * @param paymentModeId the value for the <code>paymentModeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setPaymentModeId(Long paymentModeId) {
      this.paymentModeId = paymentModeId;
   }

   /**
    * Returns the value of the <code>accountNick</code> property.
    *
    */
      @Column(name = "ACCOUNT_NICK" , nullable = false , length=50 )
   public String getAccountNick() {
      return accountNick;
   }

   /**
    * Sets the value of the <code>accountNick</code> property.
    *
    * @param accountNick the value for the <code>accountNick</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountNick(String accountNick) {
      this.accountNick = accountNick;
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
      @Column(name = "BANK_ID" , nullable = false )
   public Long getBankId() {
      return bankId;
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
    * Returns the value of the <code>smaActive</code> property.
    *
    */
      @Column(name = "IS_SMA_ACTIVE" , nullable = false )
   public Boolean getSmaActive() {
      return smaActive;
   }

   /**
    * Sets the value of the <code>smaActive</code> property.
    *
    * @param smaActive the value for the <code>smaActive</code> property
    *    
		    */

   public void setSmaActive(Boolean smaActive) {
      this.smaActive = smaActive;
   }

   /**
    * Returns the value of the <code>smartMoneyAccountId</code> property.
    *
    */
      @Column(name = "SMART_MONEY_ACCOUNT_ID" , nullable = false )
   public Long getSmartMoneyAccountId() {
      return smartMoneyAccountId;
   }

   /**
    * Sets the value of the <code>smartMoneyAccountId</code> property.
    *
    * @param smartMoneyAccountId the value for the <code>smartMoneyAccountId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
      this.smartMoneyAccountId = smartMoneyAccountId;
   }

   /**
    * Returns the value of the <code>mobileTypeName</code> property.
    *
    */
      @Column(name = "MOBILE_TYPE_NAME" , nullable = false , length=50 )
   public String getMobileTypeName() {
      return mobileTypeName;
   }

   /**
    * Sets the value of the <code>mobileTypeName</code> property.
    *
    * @param mobileTypeName the value for the <code>mobileTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setMobileTypeName(String mobileTypeName) {
      this.mobileTypeName = mobileTypeName;
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
    * Returns the value of the <code>mfsId</code> property.
    *
    */
      @Column(name = "MFS_ID"  , length=50 )
   public String getMfsId() {
      return mfsId;
   }

   /**
    * Sets the value of the <code>mfsId</code> property.
    *
    * @param mfsId the value for the <code>mfsId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMfsId(String mfsId) {
      this.mfsId = mfsId;
   }

   /**
    * Returns the value of the <code>userDeviceAccountsId</code> property.
    *
    */
      @Column(name = "USER_DEVICE_ACCOUNTS_ID" , nullable = false )
   public Long getUserDeviceAccountsId() {
      return userDeviceAccountsId;
   }

   /**
    * Sets the value of the <code>userDeviceAccountsId</code> property.
    *
    * @param userDeviceAccountsId the value for the <code>userDeviceAccountsId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setUserDeviceAccountsId(Long userDeviceAccountsId) {
      this.userDeviceAccountsId = userDeviceAccountsId;
   }

   /**
    * Returns the value of the <code>udaAccountEnabled</code> property.
    *
    */
      @Column(name = "IS_UDA_ACCOUNT_ENABLED" , nullable = false )
   public Boolean getUdaAccountEnabled() {
      return udaAccountEnabled;
   }

   /**
    * Sets the value of the <code>udaAccountEnabled</code> property.
    *
    * @param udaAccountEnabled the value for the <code>udaAccountEnabled</code> property
    *    
		    */

   public void setUdaAccountEnabled(Boolean udaAccountEnabled) {
      this.udaAccountEnabled = udaAccountEnabled;
   }

   /**
    * Returns the value of the <code>udaAccountExpired</code> property.
    *
    */
      @Column(name = "IS_UDA_ACCOUNT_EXPIRED" , nullable = false )
   public Boolean getUdaAccountExpired() {
      return udaAccountExpired;
   }

   /**
    * Sets the value of the <code>udaAccountExpired</code> property.
    *
    * @param udaAccountExpired the value for the <code>udaAccountExpired</code> property
    *    
		    */

   public void setUdaAccountExpired(Boolean udaAccountExpired) {
      this.udaAccountExpired = udaAccountExpired;
   }

   /**
    * Returns the value of the <code>udaAccountLocked</code> property.
    *
    */
      @Column(name = "IS_UDA_ACCOUNT_LOCKED" , nullable = false )
   public Boolean getUdaAccountLocked() {
      return udaAccountLocked;
   }

   /**
    * Sets the value of the <code>udaAccountLocked</code> property.
    *
    * @param udaAccountLocked the value for the <code>udaAccountLocked</code> property
    *    
		    */

   public void setUdaAccountLocked(Boolean udaAccountLocked) {
      this.udaAccountLocked = udaAccountLocked;
   }

   /**
    * Returns the value of the <code>udaCredentialsExpired</code> property.
    *
    */
      @Column(name = "IS_UDA_CREDENTIALS_EXPIRED" , nullable = false )
   public Boolean getUdaCredentialsExpired() {
      return udaCredentialsExpired;
   }

   /**
    * Sets the value of the <code>udaCredentialsExpired</code> property.
    *
    * @param udaCredentialsExpired the value for the <code>udaCredentialsExpired</code> property
    *    
		    */

   public void setUdaCredentialsExpired(Boolean udaCredentialsExpired) {
      this.udaCredentialsExpired = udaCredentialsExpired;
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
    @Column(name = "RETAILER_CONTACT_ID"  )
    public Long getRetailerContactId() {
       return retailerContactId;
    }


    public void setRetailerContactId(Long retailerContactId) {
       this.retailerContactId = retailerContactId;
    }

    @Column(name = "DISTRIBUTOR_CONTACT_ID"  )
    public Long getDistributorContactId() {
       return distributorContactId;
    }


    public void setDistributorContactId(Long distributorContactId) {
       this.distributorContactId = distributorContactId;
    }
    @Column(name = "USER_NAME" ,length=50 )
    public String getUsername() {
       return username;
    }


    public void setUsername(String username) {
       this.username = username;
    }
    @Column(name = "ACCOUNT_OPENING_DATE", length=50 )	
	public Date getAccountOpeningDate() {
		return accountOpeningDate;
	}
	
	public void setAccountOpeningDate(Date accountOpeningDate) {
		this.accountOpeningDate = accountOpeningDate;
	}
	@Column(name = "AREA" , nullable = false , length=50 )
   public String getAreaName() {
      return areaName;
   }

  
   public void setAreaName(String areaName) {
      this.areaName = areaName;
   }


}
