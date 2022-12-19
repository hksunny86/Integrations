package com.inov8.microbank.common.model.billreminder;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The UserSerbillMaxduedateViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UserSerbillMaxduedateViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "USER_SERBILL_MAXDUEDATE_VIEW")
public class UserSerbillMaxduedateViewModel extends BasePersistableModel implements Serializable {
  



   private Date maxDueDate;
   private Long userServiceBillId;
   private Long userServiceId;
   private Integer billStatus;
   private Long billAmount;
   private String billMonth;
   private Long createdBy;
   private Long updatedBy;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;
   private Integer billFetchInterval;
   private Date maxBillFetchInterval;

   /**
    * Default constructor.
    */
   public UserSerbillMaxduedateViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUserServiceBillId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUserServiceBillId(primaryKey);
    }

   /**
    * Returns the value of the <code>maxDueDate</code> property.
    *
    */
      @Column(name = "MAX_DUE_DATE"  )
   public Date getMaxDueDate() {
      return maxDueDate;
   }

   /**
    * Sets the value of the <code>maxDueDate</code> property.
    *
    * @param maxDueDate the value for the <code>maxDueDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setMaxDueDate(Date maxDueDate) {
      this.maxDueDate = maxDueDate;
   }

   /**
    * Returns the value of the <code>userServiceBillId</code> property.
    *
    */
      @Column(name = "USER_SERVICE_BILL_ID" , nullable = false )
   @Id 
   public Long getUserServiceBillId() {
      return userServiceBillId;
   }

   /**
    * Sets the value of the <code>userServiceBillId</code> property.
    *
    * @param userServiceBillId the value for the <code>userServiceBillId</code> property
    *    
		    */

   public void setUserServiceBillId(Long userServiceBillId) {
      this.userServiceBillId = userServiceBillId;
   }

   /**
    * Returns the value of the <code>userServiceId</code> property.
    *
    */
      @Column(name = "USER_SERVICE_ID" , nullable = false )
   public Long getUserServiceId() {
      return userServiceId;
   }

   /**
    * Sets the value of the <code>userServiceId</code> property.
    *
    * @param userServiceId the value for the <code>userServiceId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setUserServiceId(Long userServiceId) {
      this.userServiceId = userServiceId;
   }

   /**
    * Returns the value of the <code>billStatus</code> property.
    *
    */
      @Column(name = "BILL_STATUS" , nullable = false )
   public Integer getBillStatus() {
      return billStatus;
   }

   /**
    * Sets the value of the <code>billStatus</code> property.
    *
    * @param billStatus the value for the <code>billStatus</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setBillStatus(Integer billStatus) {
      this.billStatus = billStatus;
   }

   /**
    * Returns the value of the <code>billAmount</code> property.
    *
    */
      @Column(name = "BILL_AMOUNT" , nullable = false )
   public Long getBillAmount() {
      return billAmount;
   }

   /**
    * Sets the value of the <code>billAmount</code> property.
    *
    * @param billAmount the value for the <code>billAmount</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBillAmount(Long billAmount) {
      this.billAmount = billAmount;
   }

   /**
    * Returns the value of the <code>billMonth</code> property.
    *
    */
      @Column(name = "BILL_MONTH" , nullable = false , length=50 )
   public String getBillMonth() {
      return billMonth;
   }

   /**
    * Sets the value of the <code>billMonth</code> property.
    *
    * @param billMonth the value for the <code>billMonth</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setBillMonth(String billMonth) {
      this.billMonth = billMonth;
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
    * Returns the value of the <code>billFetchInterval</code> property.
    *
    */
      @Column(name = "BILL_FETCH_INTERVAL"  )
   public Integer getBillFetchInterval() {
      return billFetchInterval;
   }

   /**
    * Sets the value of the <code>billFetchInterval</code> property.
    *
    * @param billFetchInterval the value for the <code>billFetchInterval</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setBillFetchInterval(Integer billFetchInterval) {
      this.billFetchInterval = billFetchInterval;
   }

   /**
    * Returns the value of the <code>maxBillFetchInterval</code> property.
    *
    */
      @Column(name = "MAX_BILL_FETCH_INTERVAL"  )
   public Date getMaxBillFetchInterval() {
      return maxBillFetchInterval;
   }

   /**
    * Sets the value of the <code>maxBillFetchInterval</code> property.
    *
    * @param maxBillFetchInterval the value for the <code>maxBillFetchInterval</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setMaxBillFetchInterval(Date maxBillFetchInterval) {
      this.maxBillFetchInterval = maxBillFetchInterval;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUserServiceBillId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&userServiceBillId=" + getUserServiceBillId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "userServiceBillId";
			return primaryKeyFieldName;				
    }       
}
