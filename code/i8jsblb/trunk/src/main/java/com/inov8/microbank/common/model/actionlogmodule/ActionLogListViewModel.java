package com.inov8.microbank.common.model.actionlogmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ActionLogListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ActionLogListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ACTION_LOG_LIST_VIEW")
public class ActionLogListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -749807589087258049L;
private Long actionLogId;
   private String userName;
   private String deviceUserId;
   private java.sql.Timestamp startTime;
   private java.sql.Timestamp endTime;
   private Long usecaseId;
   private String usecaseName;
   private Long actionId;
   private String actionName;
   private Long actionStatusId;
   private String actionStatusName;

   /**
    * Default constructor.
    */
   public ActionLogListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getActionLogId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setActionLogId(primaryKey);
    }

   /**
    * Returns the value of the <code>actionLogId</code> property.
    *
    */
      @Column(name = "ACTION_LOG_ID" , nullable = false )
   @Id 
   public Long getActionLogId() {
      return actionLogId;
   }

   /**
    * Sets the value of the <code>actionLogId</code> property.
    *
    * @param actionLogId the value for the <code>actionLogId</code> property
    *    
		    */

   public void setActionLogId(Long actionLogId) {
      this.actionLogId = actionLogId;
   }

   /**
    * Returns the value of the <code>userName</code> property.
    *
    */
      @Column(name = "USER_NAME"  , length=50 )
   public String getUserName() {
      return userName;
   }

   /**
    * Sets the value of the <code>userName</code> property.
    *
    * @param userName the value for the <code>userName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUserName(String userName) {
      this.userName = userName;
   }

   /**
    * Returns the value of the <code>deviceUserId</code> property.
    *
    */
      @Column(name = "DEVICE_USER_ID"  )
   public String getDeviceUserId() {
      return deviceUserId;
   }

   /**
    * Sets the value of the <code>deviceUserId</code> property.
    *
    * @param deviceUserId the value for the <code>deviceUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDeviceUserId(String deviceUserId) {
      this.deviceUserId = deviceUserId;
   }

   /**
    * Returns the value of the <code>startTime</code> property.
    *
    */
      @Column(name = "START_TIME"  )
   public java.sql.Timestamp getStartTime() {
      return startTime;
   }

   /**
    * Sets the value of the <code>startTime</code> property.
    *
    * @param startTime the value for the <code>startTime</code> property
    *    
		    */

   public void setStartTime(java.sql.Timestamp startTime) {
      this.startTime = startTime;
   }

   /**
    * Returns the value of the <code>endTime</code> property.
    *
    */
      @Column(name = "END_TIME"  )
   public java.sql.Timestamp getEndTime() {
      return endTime;
   }

   /**
    * Sets the value of the <code>endTime</code> property.
    *
    * @param endTime the value for the <code>endTime</code> property
    *    
		    */

   public void setEndTime(java.sql.Timestamp endTime) {
      this.endTime = endTime;
   }

   /**
    * Returns the value of the <code>usecaseId</code> property.
    *
    */
      @Column(name = "USECASE_ID"  )
   public Long getUsecaseId() {
      return usecaseId;
   }

   /**
    * Sets the value of the <code>usecaseId</code> property.
    *
    * @param usecaseId the value for the <code>usecaseId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setUsecaseId(Long usecaseId) {
      this.usecaseId = usecaseId;
   }

   /**
    * Returns the value of the <code>usecaseName</code> property.
    *
    */
      @Column(name = "USECASE_NAME"  , length=50 )
   public String getUsecaseName() {
      return usecaseName;
   }

   /**
    * Sets the value of the <code>usecaseName</code> property.
    *
    * @param usecaseName the value for the <code>usecaseName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUsecaseName(String usecaseName) {
      this.usecaseName = usecaseName;
   }

   /**
    * Returns the value of the <code>actionId</code> property.
    *
    */
      @Column(name = "ACTION_ID"  )
   public Long getActionId() {
      return actionId;
   }

   /**
    * Sets the value of the <code>actionId</code> property.
    *
    * @param actionId the value for the <code>actionId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setActionId(Long actionId) {
      this.actionId = actionId;
   }

   /**
    * Returns the value of the <code>actionName</code> property.
    *
    */
      @Column(name = "ACTION_NAME"  , length=50 )
   public String getActionName() {
      return actionName;
   }

   /**
    * Sets the value of the <code>actionName</code> property.
    *
    * @param actionName the value for the <code>actionName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setActionName(String actionName) {
      this.actionName = actionName;
   }

   /**
    * Returns the value of the <code>actionStatusId</code> property.
    *
    */
      @Column(name = "ACTION_STATUS_ID" , nullable = false )
   public Long getActionStatusId() {
      return actionStatusId;
   }

   /**
    * Sets the value of the <code>actionStatusId</code> property.
    *
    * @param actionStatusId the value for the <code>actionStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setActionStatusId(Long actionStatusId) {
      this.actionStatusId = actionStatusId;
   }

   /**
    * Returns the value of the <code>actionStatusName</code> property.
    *
    */
      @Column(name = "ACTION_STATUS_NAME"  , length=50 )
   public String getActionStatusName() {
      return actionStatusName;
   }

   /**
    * Sets the value of the <code>actionStatusName</code> property.
    *
    * @param actionStatusName the value for the <code>actionStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setActionStatusName(String actionStatusName) {
      this.actionStatusName = actionStatusName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getActionLogId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&actionLogId=" + getActionLogId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "actionLogId";
			return primaryKeyFieldName;				
    }       
}
