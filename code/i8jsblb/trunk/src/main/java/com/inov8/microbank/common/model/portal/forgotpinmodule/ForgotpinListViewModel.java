package com.inov8.microbank.common.model.portal.forgotpinmodule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ForgotpinListViewModel entity bean.
 *
 * @author  Muzzamil Idrees  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ForgotpinListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "FORGOTPIN_LIST_VIEW_seq",sequenceName = "FORGOTPIN_LIST_VIEW_seq", allocationSize=1)
@Table(name = "FORGOTPIN_LIST_VIEW")
public class ForgotpinListViewModel extends BasePersistableModel implements Serializable {
  



   private String userId;
   private String password;
   private String mobileNo;
   private String firstName;
   private String lastName;
   private String address1;
   private String address2;
   private String nic;
   private String pin;
   private Long appUserId;
   private Long mobileTypeId;
   private String city;
   private String state;
   private String country;
   private Date dob;
   private String name;
   private String fullAddress;
   private String fullName;
   private Boolean accountLocked;
   private Long deviceTypeId;

   /**
    * Default constructor.
    */
   public ForgotpinListViewModel() {
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
    * Returns the value of the <code>password</code> property.
    *
    */
      @Column(name = "PASSWORD"  , length=250 )
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
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
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
		    * @spring.validator type="required"
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
		    * @spring.validator type="required"
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
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FORGOTPIN_LIST_VIEW_seq")
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
		    * @spring.validator type="required"
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
		    * @spring.validator type="required"
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
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_ID" , nullable = false )
   public Long getDeviceTypeId() {
      return deviceTypeId;
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
    *    
		    * @spring.validator type="required"
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
    
    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	    	
    	return associationModelList;
    }    
          
}
