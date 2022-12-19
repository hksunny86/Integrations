package com.inov8.microbank.common.model.distributormodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The DistributorContactListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistributorContactListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DISTRIBUTOR_CONTACT_LIST_VIEW")
public class DistributorContactListViewModel extends BasePersistableModel implements Serializable {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -4513764062252985670L;
private String distributorName;
   private String distributorLevelName;
   private Long distributorContactId;
   private Long distributorId;
   private Long managingContactId;
   private Long distributorLevelId;
   private Long areaId;
   private Boolean active;
   private Integer versionNo;
   private Double balance;
   private String email;
   private Long appUserId;
   private String firstName;
   private String lastName;
   private String mobileNo;
   private String areaName;
   private Boolean accountEnabled;
   private String username;
   private String managingContactName;
   private String partnerGroupId;
   private String partnerGroupName;
   private Long deviceTypeId;
  private Boolean head;
  private String allpayId;
   
  
  public void setHeadString (String set){
	  
  }
  @javax.persistence.Transient
  public String getHeadString(){
	   if (head == null || head== false){
		   return "No";
	   }else if (head == true){
		   return "Yes";
	   }
	   return null;
  }

  
   public void setDeviceTypeId(Long deviceTypeId) {
	      this.deviceTypeId = deviceTypeId;
	   }

	   /**
	    * Returns the value of the <code>distributorId</code> property.
	    *
	    */
	      @Column(name = "DEVICE_TYPE_ID"  )
	   public Long getDeviceTypeId() {
	      return deviceTypeId ;
	   }
   
   
   /**
    * Default constructor.
    */
   public DistributorContactListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistributorContactId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistributorContactId(primaryKey);
    }

   /**
    * Returns the value of the <code>distributorName</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_NAME"  , length=50 )
   public String getDistributorName() {
      return distributorName;
   }

   /**
    * Sets the value of the <code>distributorName</code> property.
    *
    * @param distributorName the value for the <code>distributorName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setDistributorName(String distributorName) {
      this.distributorName = distributorName;
   }

   /**
    * Returns the value of the <code>distributorLevelName</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_LEVEL_NAME"  , length=50 )
   public String getDistributorLevelName() {
      return distributorLevelName;
   }

   /**
    * Sets the value of the <code>distributorLevelName</code> property.
    *
    * @param distributorLevelName the value for the <code>distributorLevelName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setDistributorLevelName(String distributorLevelName) {
      this.distributorLevelName = distributorLevelName;
   }

   /**
    * Returns the value of the <code>distributorContactId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_CONTACT_ID"  )
   @Id 
   public Long getDistributorContactId() {
      return distributorContactId;
   }

   /**
    * Sets the value of the <code>distributorContactId</code> property.
    *
    * @param distributorContactId the value for the <code>distributorContactId</code> property
    *    
		    */

   public void setDistributorContactId(Long distributorContactId) {
      this.distributorContactId = distributorContactId;
   }

   /**
    * Returns the value of the <code>distributorId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_ID"  )
   public Long getDistributorId() {
      return distributorId;
   }

   /**
    * Sets the value of the <code>distributorId</code> property.
    *
    * @param distributorId the value for the <code>distributorId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDistributorId(Long distributorId) {
      this.distributorId = distributorId;
   }

   /**
    * Returns the value of the <code>managingContactId</code> property.
    *
    */
      @Column(name = "MANAGING_CONTACT_ID"  )
   public Long getManagingContactId() {
      return managingContactId;
   }

   /**
    * Sets the value of the <code>managingContactId</code> property.
    *
    * @param managingContactId the value for the <code>managingContactId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setManagingContactId(Long managingContactId) {
      this.managingContactId = managingContactId;
   }

   /**
    * Returns the value of the <code>distributorLevelId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_LEVEL_ID"  )
   public Long getDistributorLevelId() {
      return distributorLevelId;
   }

   /**
    * Sets the value of the <code>distributorLevelId</code> property.
    *
    * @param distributorLevelId the value for the <code>distributorLevelId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDistributorLevelId(Long distributorLevelId) {
      this.distributorLevelId = distributorLevelId;
   }

   /**
    * Returns the value of the <code>areaId</code> property.
    *
    */
      @Column(name = "AREA_ID"  )
   public Long getAreaId() {
      return areaId;
   }

   /**
    * Sets the value of the <code>areaId</code> property.
    *
    * @param areaId the value for the <code>areaId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAreaId(Long areaId) {
      this.areaId = areaId;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE"  )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setHead(Boolean head) {
      this.head = head;
   }

   @Column(name = "IS_HEAD"  )
   public Boolean getHead() {
      return head;
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
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO"  )
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
    * Returns the value of the <code>balance</code> property.
    *
    */
      @Column(name = "BALANCE"  )
   public Double getBalance() {
      return balance;
   }

   /**
    * Sets the value of the <code>balance</code> property.
    *
    * @param balance the value for the <code>balance</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBalance(Double balance) {
      this.balance = balance;
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
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID"  )
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
      @Column(name = "FIRST_NAME"  , length=50 )
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
   @Column(name = "ALLPAY_ID"  , length=50 )
   public String getAllpayId() {
      return allpayId;
   }

   

   public void setAllpayId(String allpayId) {
      this.allpayId= allpayId;
   }

   
   
   /**
    * Returns the value of the <code>lastName</code> property.
    *
    */
      @Column(name = "LAST_NAME"  , length=50 )
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
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
      @Column(name = "MOBILE_NO"  , length=50 )
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
    * Returns the value of the <code>areaName</code> property.
    *
    */
      @Column(name = "AREA_NAME"  , length=50 )
   public String getAreaName() {
      return areaName;
   }

   /**
    * Sets the value of the <code>areaName</code> property.
    *
    * @param areaName the value for the <code>areaName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setAreaName(String areaName) {
      this.areaName = areaName;
   }

   /**
    * Returns the value of the <code>accountEnabled</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_ENABLED"  )
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
    * Returns the value of the <code>username</code> property.
    *
    */
      @Column(name = "USERNAME"  , length=50 )
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
    * Returns the value of the <code>managingContactName</code> property.
    *
    */
      @Column(name = "MANAGING_CONTACT_NAME"  , length=101 )
   public String getManagingContactName() {
      return managingContactName;
   }

   /**
    * Sets the value of the <code>managingContactName</code> property.
    *
    * @param managingContactName the value for the <code>managingContactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="101"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setManagingContactName(String managingContactName) {
      this.managingContactName = managingContactName;
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
        checkBox += "_"+ getDistributorContactId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&distributorContactId=" + getDistributorContactId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "distributorContactId";
			return primaryKeyFieldName;				
    }       
}
