package com.inov8.microbank.common.model.auditlogmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AuditLogListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AuditLogListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AUDIT_LOG_LIST_VIEW")
public class AuditLogListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 1068310513549210681L;
private Long auditLogId;
   private Long actionLogId;
   private Long integrationModuleId;
   private java.sql.Timestamp transactionStartTime;
   private java.sql.Timestamp transactionEndTime;
   private Long transactionCodeId;
   private String code;
   private String name;

   /**
    * Default constructor.
    */
   public AuditLogListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAuditLogId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAuditLogId(primaryKey);
    }

   /**
    * Returns the value of the <code>auditLogId</code> property.
    *
    */
      @Column(name = "AUDIT_LOG_ID" , nullable = false )
   @Id 
   public Long getAuditLogId() {
      return auditLogId;
   }

   /**
    * Sets the value of the <code>auditLogId</code> property.
    *
    * @param auditLogId the value for the <code>auditLogId</code> property
    *    
		    */

   public void setAuditLogId(Long auditLogId) {
      this.auditLogId = auditLogId;
   }

   /**
    * Returns the value of the <code>actionLogId</code> property.
    *
    */
      @Column(name = "ACTION_LOG_ID" , nullable = false )
   public Long getActionLogId() {
      return actionLogId;
   }

   /**
    * Sets the value of the <code>actionLogId</code> property.
    *
    * @param actionLogId the value for the <code>actionLogId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setActionLogId(Long actionLogId) {
      this.actionLogId = actionLogId;
   }

   /**
    * Returns the value of the <code>integrationModuleId</code> property.
    *
    */
      @Column(name = "INTEGRATION_MODULE_ID" , nullable = false )
   public Long getIntegrationModuleId() {
      return integrationModuleId;
   }

   /**
    * Sets the value of the <code>integrationModuleId</code> property.
    *
    * @param integrationModuleId the value for the <code>integrationModuleId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setIntegrationModuleId(Long integrationModuleId) {
      this.integrationModuleId = integrationModuleId;
   }

   /**
    * Returns the value of the <code>transactionStartTime</code> property.
    *
    */
      @Column(name = "TRANSACTION_START_TIME"  )
   public java.sql.Timestamp getTransactionStartTime() {
      return transactionStartTime;
   }

   /**
    * Sets the value of the <code>transactionStartTime</code> property.
    *
    * @param transactionStartTime the value for the <code>transactionStartTime</code> property
    *    
		    */

   public void setTransactionStartTime(java.sql.Timestamp transactionStartTime) {
      this.transactionStartTime = transactionStartTime;
   }

   /**
    * Returns the value of the <code>transactionEndTime</code> property.
    *
    */
      @Column(name = "TRANSACTION_END_TIME"  )
   public java.sql.Timestamp getTransactionEndTime() {
      return transactionEndTime;
   }

   /**
    * Sets the value of the <code>transactionEndTime</code> property.
    *
    * @param transactionEndTime the value for the <code>transactionEndTime</code> property
    *    
		    */

   public void setTransactionEndTime(java.sql.Timestamp transactionEndTime) {
      this.transactionEndTime = transactionEndTime;
   }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE_ID" , nullable = false )
   public Long getTransactionCodeId() {
      return transactionCodeId;
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setTransactionCodeId(Long transactionCodeId) {
      this.transactionCodeId = transactionCodeId;
   }

   /**
    * Returns the value of the <code>code</code> property.
    *
    */
      @Column(name = "CODE" , nullable = false , length=50 )
   public String getCode() {
      return code;
   }

   /**
    * Sets the value of the <code>code</code> property.
    *
    * @param code the value for the <code>code</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCode(String code) {
      this.code = code;
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAuditLogId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&auditLogId=" + getAuditLogId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "auditLogId";
			return primaryKeyFieldName;				
    }       
}
