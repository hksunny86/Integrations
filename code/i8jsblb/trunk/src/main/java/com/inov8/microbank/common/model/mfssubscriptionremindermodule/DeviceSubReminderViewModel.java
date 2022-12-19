package com.inov8.microbank.common.model.mfssubscriptionremindermodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The DeviceSubReminderViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DeviceSubReminderViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DEVICE_SUB_REMINDER_VIEW")
public class DeviceSubReminderViewModel extends BasePersistableModel {
  



   private Long userDeviceAccountsId;
   private String mobileNo;
   private Date expiryDate;

   /**
    * Default constructor.
    */
   public DeviceSubReminderViewModel() {
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
