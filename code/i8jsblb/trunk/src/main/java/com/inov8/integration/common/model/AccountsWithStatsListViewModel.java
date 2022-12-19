package com.inov8.integration.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AccountsWithStatsListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AccountsWithStatsListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ACCOUNTS_WITH_STATS_LIST_VIEW")
public class AccountsWithStatsListViewModel extends BasePersistableModel implements Serializable {
  



   private Long accountId;
   private String accountNumber;
   private String balance;
   private Long statusId;
   private String firstName;
   private String middleName;
   private String lastName;
   private String address;
   private String cnic;
   private String fatherName;
   private Boolean active;
   private Boolean deleted;
   private String landlineNumber;
   private String mobileNumber;
   private String dob;
   private Long balanceDisbursed;
   private Long balanceReceived;
   private String endDayBalance;
   private String startDayBalance;
   private String statsDate;
   private Long dailyAccountStatsId;
   private Date dateOfBirth;
   

   /**
    * Default constructor.
    */
   public AccountsWithStatsListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDailyAccountStatsId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDailyAccountStatsId(primaryKey);
    }

   /**
    * Returns the value of the <code>accountId</code> property.
    *
    */
      @Column(name = "ACCOUNT_ID" , nullable = false )
   public Long getAccountId() {
      return accountId;
   }

   /**
    * Sets the value of the <code>accountId</code> property.
    *
    * @param accountId the value for the <code>accountId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAccountId(Long accountId) {
      this.accountId = accountId;
   }

   /**
    * Returns the value of the <code>accountNumber</code> property.
    *
    */
      @Column(name = "ACCOUNT_NUMBER" , nullable = false , length=255 )
   public String getAccountNumber() {
      return accountNumber;
   }

   /**
    * Sets the value of the <code>accountNumber</code> property.
    *
    * @param accountNumber the value for the <code>accountNumber</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setAccountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
   }

   /**
    * Returns the value of the <code>balance</code> property.
    *
    */
      @Column(name = "BALANCE" , nullable = false , length=255 )
   public String getBalance() {
      return balance;
   }

   /**
    * Sets the value of the <code>balance</code> property.
    *
    * @param balance the value for the <code>balance</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setBalance(String balance) {
      this.balance = balance;
   }

   /**
    * Returns the value of the <code>statusId</code> property.
    *
    */
      @Column(name = "STATUS_ID" , nullable = false )
   public Long getStatusId() {
      return statusId;
   }

   /**
    * Sets the value of the <code>statusId</code> property.
    *
    * @param statusId the value for the <code>statusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setStatusId(Long statusId) {
      this.statusId = statusId;
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
    * Returns the value of the <code>middleName</code> property.
    *
    */
      @Column(name = "MIDDLE_NAME" , nullable = false , length=50 )
   public String getMiddleName() {
      return middleName;
   }

   /**
    * Sets the value of the <code>middleName</code> property.
    *
    * @param middleName the value for the <code>middleName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setMiddleName(String middleName) {
      this.middleName = middleName;
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
    * Returns the value of the <code>address</code> property.
    *
    */
      @Column(name = "ADDRESS" , nullable = false , length=250 )
   public String getAddress() {
      return address;
   }

   /**
    * Sets the value of the <code>address</code> property.
    *
    * @param address the value for the <code>address</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress(String address) {
      this.address = address;
   }

   /**
    * Returns the value of the <code>cnic</code> property.
    *
    */
      @Column(name = "CNIC" , nullable = false , length=255 )
   public String getCnic() {
      return cnic;
   }

   /**
    * Sets the value of the <code>cnic</code> property.
    *
    * @param cnic the value for the <code>cnic</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setCnic(String cnic) {
      this.cnic = cnic;
   }

   /**
    * Returns the value of the <code>fatherName</code> property.
    *
    */
      @Column(name = "FATHER_NAME" , nullable = false , length=100 )
   public String getFatherName() {
      return fatherName;
   }

   /**
    * Sets the value of the <code>fatherName</code> property.
    *
    * @param fatherName the value for the <code>fatherName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="100"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFatherName(String fatherName) {
      this.fatherName = fatherName;
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

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>deleted</code> property.
    *
    */
      @Column(name = "IS_DELETED"  )
   public Boolean getDeleted() {
      return deleted;
   }

   /**
    * Sets the value of the <code>deleted</code> property.
    *
    * @param deleted the value for the <code>deleted</code> property
    *    
		    */

   public void setDeleted(Boolean deleted) {
      this.deleted = deleted;
   }

   /**
    * Returns the value of the <code>landlineNumber</code> property.
    *
    */
      @Column(name = "LANDLINE_NUMBER" , nullable = false , length=50 )
   public String getLandlineNumber() {
      return landlineNumber;
   }

   /**
    * Sets the value of the <code>landlineNumber</code> property.
    *
    * @param landlineNumber the value for the <code>landlineNumber</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setLandlineNumber(String landlineNumber) {
      this.landlineNumber = landlineNumber;
   }

   /**
    * Returns the value of the <code>mobileNumber</code> property.
    *
    */
      @Column(name = "MOBILE_NUMBER" , nullable = false , length=50 )
   public String getMobileNumber() {
      return mobileNumber;
   }

   /**
    * Sets the value of the <code>mobileNumber</code> property.
    *
    * @param mobileNumber the value for the <code>mobileNumber</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNumber(String mobileNumber) {
      this.mobileNumber = mobileNumber;
   }

   /**
    * Returns the value of the <code>dob</code> property.
    *
    */
      @Column(name = "DOB" , nullable = false , length=255 )
   public String getDob() {
      return dob;
   }

   /**
    * Sets the value of the <code>dob</code> property.
    *
    * @param dob the value for the <code>dob</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setDob(String dob) {
      this.dob = dob;
   }

   /**
    * Returns the value of the <code>balanceDisbursed</code> property.
    *
    */
      @Column(name = "BALANCE_DISBURSED"  )
   public Long getBalanceDisbursed() {
      return balanceDisbursed;
   }

   /**
    * Sets the value of the <code>balanceDisbursed</code> property.
    *
    * @param balanceDisbursed the value for the <code>balanceDisbursed</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBalanceDisbursed(Long balanceDisbursed) {
      this.balanceDisbursed = balanceDisbursed;
   }

   /**
    * Returns the value of the <code>balanceReceived</code> property.
    *
    */
      @Column(name = "BALANCE_RECEIVED"  )
   public Long getBalanceReceived() {
      return balanceReceived;
   }

   /**
    * Sets the value of the <code>balanceReceived</code> property.
    *
    * @param balanceReceived the value for the <code>balanceReceived</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBalanceReceived(Long balanceReceived) {
      this.balanceReceived = balanceReceived;
   }

   /**
    * Returns the value of the <code>endDayBalance</code> property.
    *
    */
      @Column(name = "END_DAY_BALANCE"  , length=255 )
   public String getEndDayBalance() {
      return endDayBalance;
   }

   /**
    * Sets the value of the <code>endDayBalance</code> property.
    *
    * @param endDayBalance the value for the <code>endDayBalance</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setEndDayBalance(String endDayBalance) {
      this.endDayBalance = endDayBalance;
   }

   /**
    * Returns the value of the <code>startDayBalance</code> property.
    *
    */
      @Column(name = "START_DAY_BALANCE"  , length=255 )
   public String getStartDayBalance() {
      return startDayBalance;
   }

   /**
    * Sets the value of the <code>startDayBalance</code> property.
    *
    * @param startDayBalance the value for the <code>startDayBalance</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setStartDayBalance(String startDayBalance) {
      this.startDayBalance = startDayBalance;
   }

   /**
    * Returns the value of the <code>statsDate</code> property.
    *
    */
      @Column(name = "STATS_DATE"  )
   public String getStatsDate() {
      return statsDate;
   }

   /**
    * Sets the value of the <code>statsDate</code> property.
    *
    * @param statsDate the value for the <code>statsDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setStatsDate(String statsDate) {
      this.statsDate = statsDate;
   }

   /**
    * Returns the value of the <code>dailyAccountStatsId</code> property.
    *
    */
      @Column(name = "DAILY_ACCOUNT_STATS_ID" , nullable = false )
   @Id 
   public Long getDailyAccountStatsId() {
      return dailyAccountStatsId;
   }

   /**
    * Sets the value of the <code>dailyAccountStatsId</code> property.
    *
    * @param dailyAccountStatsId the value for the <code>dailyAccountStatsId</code> property
    *    
		    */

   public void setDailyAccountStatsId(Long dailyAccountStatsId) {
      this.dailyAccountStatsId = dailyAccountStatsId;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getDailyAccountStatsId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&dailyAccountStatsId=" + getDailyAccountStatsId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "dailyAccountStatsId";
			return primaryKeyFieldName;				
    }
    
    @javax.persistence.Transient
	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}
    
    @javax.persistence.Transient
	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}       
}
