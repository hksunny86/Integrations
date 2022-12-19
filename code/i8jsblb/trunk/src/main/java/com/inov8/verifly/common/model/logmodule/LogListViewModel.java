package com.inov8.verifly.common.model.logmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The LogListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LogListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "LOG_LIST_VIEW")
public class LogListViewModel extends BasePersistableModel {
  



   private Long pk;
   private Long logId;
   private Long accountInfoId;
   private Long actionId;
   private Long failureReasonId;
   private Integer statusId;
   private Long transactionCodeId;
   private Date endTime;
   private String inputParam;
   private Long creatdByUserId;
   private String createdBy;
   private Long logStatusId;
   private String logStatusName;
   private String logActionName;
   private Long logActionActionId;
   private Long logDetailLogId;
   private Long logDetailId;
   private Long logDetailFailureReasonId;
   private Long logDetailStatusStatusId;
   private Long logDetailActionActionId;

   /**
    * Default constructor.
    */
   public LogListViewModel() {
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

   /**
    * Returns the value of the <code>logId</code> property.
    *
    */
      @Column(name = "LOG_ID" , nullable = false )
   public Long getLogId() {
      return logId;
   }

   /**
    * Sets the value of the <code>logId</code> property.
    *
    * @param logId the value for the <code>logId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogId(Long logId) {
      this.logId = logId;
   }

   /**
    * Returns the value of the <code>accountInfoId</code> property.
    *
    */
      @Column(name = "ACCOUNT_INFO_ID"  )
   public Long getAccountInfoId() {
      return accountInfoId;
   }

   /**
    * Sets the value of the <code>accountInfoId</code> property.
    *
    * @param accountInfoId the value for the <code>accountInfoId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAccountInfoId(Long accountInfoId) {
      this.accountInfoId = accountInfoId;
   }

   /**
    * Returns the value of the <code>actionId</code> property.
    *
    */
      @Column(name = "ACTION_ID" , nullable = false )
   public Long getActionId() {
      return actionId;
   }

   /**
    * Sets the value of the <code>actionId</code> property.
    *
    * @param actionId the value for the <code>actionId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setActionId(Long actionId) {
      this.actionId = actionId;
   }

   /**
    * Returns the value of the <code>failureReasonId</code> property.
    *
    */
      @Column(name = "FAILURE_REASON_ID"  )
   public Long getFailureReasonId() {
      return failureReasonId;
   }

   /**
    * Sets the value of the <code>failureReasonId</code> property.
    *
    * @param failureReasonId the value for the <code>failureReasonId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFailureReasonId(Long failureReasonId) {
      this.failureReasonId = failureReasonId;
   }

   /**
    * Returns the value of the <code>statusId</code> property.
    *
    */
      @Column(name = "STATUS_ID" , nullable = false )
   public Integer getStatusId() {
      return statusId;
   }

   /**
    * Sets the value of the <code>statusId</code> property.
    *
    * @param statusId the value for the <code>statusId</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setStatusId(Integer statusId) {
      this.statusId = statusId;
   }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE_ID"  )
   public Long getTransactionCodeId() {
      return transactionCodeId;
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setTransactionCodeId(Long transactionCodeId) {
      this.transactionCodeId = transactionCodeId;
   }

   /**
    * Returns the value of the <code>endTime</code> property.
    *
    */
      @Column(name = "END_TIME"  )
   public Date getEndTime() {
      return endTime;
   }

   /**
    * Sets the value of the <code>endTime</code> property.
    *
    * @param endTime the value for the <code>endTime</code> property
    *    
		    * @spring.validator type="date"
    */

   public void setEndTime(Date endTime) {
      this.endTime = endTime;
   }

   /**
    * Returns the value of the <code>inputParam</code> property.
    *
    */
      @Column(name = "INPUT_PARAM"  )
   public String getInputParam() {
      return inputParam;
   }

   /**
    * Sets the value of the <code>inputParam</code> property.
    *
    * @param inputParam the value for the <code>inputParam</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    */

   public void setInputParam(String inputParam) {
      this.inputParam = inputParam;
   }

   /**
    * Returns the value of the <code>creatdByUserId</code> property.
    *
    */
      @Column(name = "CREATD_BY_USER_ID"  )
   public Long getCreatdByUserId() {
      return creatdByUserId;
   }

   /**
    * Sets the value of the <code>creatdByUserId</code> property.
    *
    * @param creatdByUserId the value for the <code>creatdByUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCreatdByUserId(Long creatdByUserId) {
      this.creatdByUserId = creatdByUserId;
   }

   /**
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY"  , length=50 )
   public String getCreatedBy() {
      return createdBy;
   }

   /**
    * Sets the value of the <code>createdBy</code> property.
    *
    * @param createdBy the value for the <code>createdBy</code> property
    *    
		    */

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   /**
    * Returns the value of the <code>logStatusId</code> property.
    *
    */
      @Column(name = "LOG_STATUS_ID" , nullable = false )
   public Long getLogStatusId() {
      return logStatusId;
   }

   /**
    * Sets the value of the <code>logStatusId</code> property.
    *
    * @param logStatusId the value for the <code>logStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogStatusId(Long logStatusId) {
      this.logStatusId = logStatusId;
   }

   /**
    * Returns the value of the <code>logStatusName</code> property.
    *
    */
      @Column(name = "LOG_STATUS_NAME" , nullable = false , length=50 )
   public String getLogStatusName() {
      return logStatusName;
   }

   /**
    * Sets the value of the <code>logStatusName</code> property.
    *
    * @param logStatusName the value for the <code>logStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setLogStatusName(String logStatusName) {
      this.logStatusName = logStatusName;
   }

   /**
    * Returns the value of the <code>logActionName</code> property.
    *
    */
      @Column(name = "LOG_ACTION_NAME" , nullable = false , length=50 )
   public String getLogActionName() {
      return logActionName;
   }

   /**
    * Sets the value of the <code>logActionName</code> property.
    *
    * @param logActionName the value for the <code>logActionName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setLogActionName(String logActionName) {
      this.logActionName = logActionName;
   }

   /**
    * Returns the value of the <code>logActionActionId</code> property.
    *
    */
      @Column(name = "LOG_ACTION_ACTION_ID" , nullable = false )
   public Long getLogActionActionId() {
      return logActionActionId;
   }

   /**
    * Sets the value of the <code>logActionActionId</code> property.
    *
    * @param logActionActionId the value for the <code>logActionActionId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogActionActionId(Long logActionActionId) {
      this.logActionActionId = logActionActionId;
   }

   /**
    * Returns the value of the <code>logDetailLogId</code> property.
    *
    */
      @Column(name = "LOG_DETAIL_LOG_ID"  )
   public Long getLogDetailLogId() {
      return logDetailLogId;
   }

   /**
    * Sets the value of the <code>logDetailLogId</code> property.
    *
    * @param logDetailLogId the value for the <code>logDetailLogId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogDetailLogId(Long logDetailLogId) {
      this.logDetailLogId = logDetailLogId;
   }

   /**
    * Returns the value of the <code>logDetailId</code> property.
    *
    */
      @Column(name = "LOG_DETAIL_ID"  )
   public Long getLogDetailId() {
      return logDetailId;
   }

   /**
    * Sets the value of the <code>logDetailId</code> property.
    *
    * @param logDetailId the value for the <code>logDetailId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogDetailId(Long logDetailId) {
      this.logDetailId = logDetailId;
   }

   /**
    * Returns the value of the <code>logDetailFailureReasonId</code> property.
    *
    */
      @Column(name = "LOG_DETAIL_FAILURE_REASON_ID"  )
   public Long getLogDetailFailureReasonId() {
      return logDetailFailureReasonId;
   }

   /**
    * Sets the value of the <code>logDetailFailureReasonId</code> property.
    *
    * @param logDetailFailureReasonId the value for the <code>logDetailFailureReasonId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogDetailFailureReasonId(Long logDetailFailureReasonId) {
      this.logDetailFailureReasonId = logDetailFailureReasonId;
   }

   /**
    * Returns the value of the <code>logDetailStatusStatusId</code> property.
    *
    */
      @Column(name = "LOG_DETAIL_STATUS_STATUS_ID"  )
   public Long getLogDetailStatusStatusId() {
      return logDetailStatusStatusId;
   }

   /**
    * Sets the value of the <code>logDetailStatusStatusId</code> property.
    *
    * @param logDetailStatusStatusId the value for the <code>logDetailStatusStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogDetailStatusStatusId(Long logDetailStatusStatusId) {
      this.logDetailStatusStatusId = logDetailStatusStatusId;
   }

   /**
    * Returns the value of the <code>logDetailActionActionId</code> property.
    *
    */
      @Column(name = "LOG_DETAIL_ACTION_ACTION_ID"  )
   public Long getLogDetailActionActionId() {
      return logDetailActionActionId;
   }

   /**
    * Sets the value of the <code>logDetailActionActionId</code> property.
    *
    * @param logDetailActionActionId the value for the <code>logDetailActionActionId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="intRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setLogDetailActionActionId(Long logDetailActionActionId) {
      this.logDetailActionActionId = logDetailActionActionId;
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
}
