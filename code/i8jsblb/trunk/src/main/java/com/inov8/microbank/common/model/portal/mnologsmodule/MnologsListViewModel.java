package com.inov8.microbank.common.model.portal.mnologsmodule;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.ActionStatusModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * The MnologsListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="MnologsListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SERVICE_OP_LOGS_LIST_VIEW")
public class MnologsListViewModel extends BasePersistableModel implements Serializable {
	
	private static final long serialVersionUID = 2256201184293394244L;
	private Long actionLogId;
	private Long usecaseId;
   private Long appUserId;
   private String userName;
   private String usecaseName;
   private String clientIp;
   private java.sql.Timestamp startTime;
//   private String mfsId;
   private String customField1;
   private String customField2;
   private String customField3;
   private String customField4;
   private String customField5;
   private String customField11;
   private String originalStatusId;
   private String statusChangedTo;
   private Long actionAuthorizationId;
   //Added by Sheheryaar Nawaz
   private String inputXML;
   private String outputXML;
   private String actionStatus;
   private String checkedBy;

   private String trxId;
   private String actionType1;
   private String oldSenderNIC;
   private String newSenderNIC;
   private String actionType2;
   private String oldSenderMobile;
   private String newSenderMobile;
   private String actionType3;
   private String oldRecipientNIC;
   private String newRecipientNIC;
   private String actionType4;
   private String oldRecipientMobile;
   private String newRecipientMobile;
   private String enquiryScreen;

   private String comments;
   /**
    * Default constructor.
    */
   public MnologsListViewModel() {
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
	 * @return the usecaseName
	 */
   	@Column(name = "USECASE_NAME"  )
	public String getUsecaseName() {
		return usecaseName;
	}
	
	/**
	 * @param usecaseName the usecaseName to set
	 */
	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
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
    * Returns the value of the <code>mfsId</code> property.
    *
    */
//      @Column(name = "MFS_ID"  , length=50 )
//   public String getMfsId() {
//      return mfsId;
//   }

   /**
    * Sets the value of the <code>mfsId</code> property.
    *
    * @param mfsId the value for the <code>mfsId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

//   public void setMfsId(String mfsId) {
//      this.mfsId = mfsId;
//   }

   /**
    * Returns the value of the <code>customField1</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_1"  , length=250 )
   public String getCustomField1() {
      return customField1;
   }

   /**
    * Sets the value of the <code>customField1</code> property.
    *
    * @param customField1 the value for the <code>customField1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField1(String customField1) {
      this.customField1 = customField1;
   }

   /**
    * Returns the value of the <code>customField2</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_2"  , length=250 )
   public String getCustomField2() {
      return customField2;
   }

   /**
    * Sets the value of the <code>customField2</code> property.
    *
    * @param customField2 the value for the <code>customField2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField2(String customField2) {
      this.customField2 = customField2;
   }

   /**
    * Returns the value of the <code>customField3</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_3"  , length=250 )
   public String getCustomField3() {
      return customField3;
   }

   /**
    * Sets the value of the <code>customField3</code> property.
    *
    * @param customField3 the value for the <code>customField3</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField3(String customField3) {
      this.customField3 = customField3;
   }

   /**
    * Returns the value of the <code>customField4</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_4"  , length=250 )
   public String getCustomField4() {
      return customField4;
   }

   /**
    * Sets the value of the <code>customField4</code> property.
    *
    * @param customField4 the value for the <code>customField4</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField4(String customField4) {
      this.customField4 = customField4;
   }

   /**
    * Returns the value of the <code>customField5</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_5"  , length=250 )
   public String getCustomField5() {
      return customField5;
   }

   /**
    * Sets the value of the <code>customField5</code> property.
    *
    * @param customField5 the value for the <code>customField5</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField5(String customField5) {
      this.customField5 = customField5;
   }

   @Column(name = "CUSTOM_FIELD_11"  , length=250 )
	public String getCustomField11() {
		return customField11;
	}

	public void setCustomField11(String customField11) {
		this.customField11 = customField11;
	} 
   
   /**
    * Returns the value of the <code>originalStatusId</code> property.
    *
    */
      @Column(name = "ORIGINAL_STATUS_ID"  , length=250 )
   public String getOriginalStatusId() {
      return originalStatusId;
   }

   /**
    * Sets the value of the <code>originalStatusId</code> property.
    *
    * @param originalStatusId the value for the <code>originalStatusId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setOriginalStatusId(String originalStatusId) {
      this.originalStatusId = originalStatusId;
   }

   /**
    * Returns the value of the <code>statusChangedTo</code> property.
    *
    */
      @Column(name = "STATUS_CHANGED_TO"  , length=250 )
   public String getStatusChangedTo() {
      return statusChangedTo;
   }

   @Column(name="CLIENT_IP")
    public String getClientIp()
    {
        return clientIp;
    }

    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp;
    }

    /**
    * Sets the value of the <code>statusChangedTo</code> property.
    *
    * @param statusChangedTo the value for the <code>statusChangedTo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setStatusChangedTo(String statusChangedTo) {
      this.statusChangedTo = statusChangedTo;
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

    @Column(name = "ACTION_AUTH_ID")
	public Long getActionAuthorizationId() {
		return actionAuthorizationId;
	}

	public void setActionAuthorizationId(Long actionAuthorizationId) {
		this.actionAuthorizationId = actionAuthorizationId;
	}

   @Column(name = "INPUT_XML")
   public String getInputXML() {
      return inputXML;
   }

   public void setInputXML(String inputXML) {
      this.inputXML = inputXML;
   }

   @Column(name = "OUTPUT_XML")
   public String getOutputXML() {
      return outputXML;
   }

   public void setOutputXML(String outputXML) {
      this.outputXML = outputXML;
   }

   @Column(name = "STATUS_NAME")
   public String getActionStatus() {
      return actionStatus;
   }

   public void setActionStatus(String actionStatus) {
      this.actionStatus = actionStatus;
   }

   @Column(name = "CHECKED_BY")
   public String getCheckedBy() {
      return checkedBy;
   }

   public void setCheckedBy(String checkedBy) {
      this.checkedBy = checkedBy;
   }

   @Column(name = "TRANSACTION_ID")
   public String getTrxId() {
      return trxId;
   }

   public void setTrxId(String trxId) {
      this.trxId = trxId;
   }

   @Column(name = "ACTION_TYPE_1")
   public String getActionType1() {
      return actionType1;
   }

   public void setActionType1(String actionType1) {
      this.actionType1 = actionType1;
   }

   @Column(name = "OLD_SENDER_CNIC")
   public String getOldSenderNIC() {
      return oldSenderNIC;
   }

   public void setOldSenderNIC(String oldSenderNIC) {
      this.oldSenderNIC = oldSenderNIC;
   }

   @Column(name = "NEW_SENDER_CNIC")
   public String getNewSenderNIC() {
      return newSenderNIC;
   }

   public void setNewSenderNIC(String newSenderNIC) {
      this.newSenderNIC = newSenderNIC;
   }

   @Column(name = "ACTION_TYPE_2")
   public String getActionType2() {
      return actionType2;
   }

   public void setActionType2(String actionType2) {
      this.actionType2 = actionType2;
   }

   @Column(name = "OLD_SENDER_MOBILE")
   public String getOldSenderMobile() {
      return oldSenderMobile;
   }

   public void setOldSenderMobile(String oldSenderMobile) {
      this.oldSenderMobile = oldSenderMobile;
   }

   @Column(name = "NEW_SENDER_MOBILE")
   public String getNewSenderMobile() {
      return newSenderMobile;
   }

   public void setNewSenderMobile(String newSenderMobile) {
      this.newSenderMobile = newSenderMobile;
   }

   @Column(name = "ACTION_TYPE_3")
   public String getActionType3() {
      return actionType3;
   }

   public void setActionType3(String actionType3) {
      this.actionType3 = actionType3;
   }

   @Column(name = "OLD_RECEIPIENT_CNIC")
   public String getOldRecipientNIC() {
      return oldRecipientNIC;
   }

   public void setOldRecipientNIC(String oldRecipientNIC) {
      this.oldRecipientNIC = oldRecipientNIC;
   }

   @Column(name = "NEW_RECEIPIENT_CNIC")
   public String getNewRecipientNIC() {
      return newRecipientNIC;
   }

   public void setNewRecipientNIC(String newRecipientNIC) {
      this.newRecipientNIC = newRecipientNIC;
   }

   @Column(name = "ACTION_TYPE_4")
   public String getActionType4() {
      return actionType4;
   }

   public void setActionType4(String actionType4) {
      this.actionType4 = actionType4;
   }

   @Column(name = "OLD_RECEIPIENT_MOBILE")
   public String getOldRecipientMobile() {
      return oldRecipientMobile;
   }

   public void setOldRecipientMobile(String oldRecipientMobile) {
      this.oldRecipientMobile = oldRecipientMobile;
   }

   @Column(name = "NEW_RECEIPIENT_MOBILE")
   public String getNewRecipientMobile() {
      return newRecipientMobile;
   }

   public void setNewRecipientMobile(String newRecipientMobile) {
      this.newRecipientMobile = newRecipientMobile;
   }

   @Column(name = "PARENT_SCREEN")
   public String getEnquiryScreen() {
      return enquiryScreen;
   }

   public void setEnquiryScreen(String enquiryScreen) {
      this.enquiryScreen = enquiryScreen;
   }

   @Column(name = "CHECKER_COMMENTS")
   public String getComments() {
      return comments;
   }

   public void setComments(String comments) {
      this.comments = comments;
   }
}
