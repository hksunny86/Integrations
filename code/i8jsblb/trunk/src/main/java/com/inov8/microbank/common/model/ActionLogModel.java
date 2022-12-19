package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The ActionLogModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ActionLogModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="ACTION_LOG_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="ACTION_LOG_seq") } )
//@javax.persistence.SequenceGenerator(name = "ACTION_LOG_seq",sequenceName = "ACTION_LOG_seq")
@Table(name = "ACTION_LOG")
public class ActionLogModel extends BasePersistableModel implements Serializable{
  

   
/**
	 * 
	 */
	private static final long serialVersionUID = -1242682311885245762L;
private UserDeviceAccountsModel userDeviceAccountsIdUserDeviceAccountsModel;
   private UsecaseModel usecaseIdUsecaseModel;
   private DeviceTypeModel deviceTypeIdDeviceTypeModel;
   private CommandModel commandIdCommandModel;
   private AppUserModel appUserIdAppUserModel;
   private ActionStatusModel actionStatusIdActionStatusModel;
   private ActionModel actionIdActionModel;

   private Collection<AuditLogModel> actionLogIdAuditLogModelList = new ArrayList<AuditLogModel>();
   private Collection<TransactionCodeModel> actionLogIdTransactionCodeModelList = new ArrayList<TransactionCodeModel>();

   private Long actionLogId;
   private String userName;
   private String deviceUserId;
   private String inputXml;
   private String outputXml;
   private String customField1;
   private String customField2;
   private String customField3;
   private String customField4;
   private String customField5;
   private String customField6;
   private String customField7;
   private String customField8;
   private String customField9;
   private String customField10;
   private String customField11;
   private java.sql.Timestamp startTime;
   private java.sql.Timestamp endTime;
   private Integer versionNo;
   private Long actionAuthorizationId;

   private String clientIpAddress;
   //
   private String trxData;


   /**
    * Default constructor.
    */
   public ActionLogModel() {
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
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACTION_LOG_seq")
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
      @Column(name = "DEVICE_USER_ID"  , length=50 )
   public String getDeviceUserId() {
      return deviceUserId;
   }

   /**
    * Sets the value of the <code>deviceUserId</code> property.
    *
    * @param deviceUserId the value for the <code>deviceUserId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setDeviceUserId(String deviceUserId) {
      this.deviceUserId = deviceUserId;
   }

   /**
    * Returns the value of the <code>inputXml</code> property.
    *
    */
      @Column(name = "INPUT_XML"  )
   public String getInputXml() {
      return inputXml;
   }

   /**
    * Sets the value of the <code>inputXml</code> property.
    *
    * @param inputXml the value for the <code>inputXml</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    */

   public void setInputXml(String inputXml) {
      this.inputXml = inputXml;
   }

   /**
    * Returns the value of the <code>outputXml</code> property.
    *
    */
      @Column(name = "OUTPUT_XML"  )
   public String getOutputXml() {
      return outputXml;
   }

   /**
    * Sets the value of the <code>outputXml</code> property.
    *
    * @param outputXml the value for the <code>outputXml</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    */

   public void setOutputXml(String outputXml) {
      this.outputXml = outputXml;
   }

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

   /**
    * Returns the value of the <code>customField6</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_6"  , length=250 )
   public String getCustomField6() {
      return customField6;
   }

   /**
    * Sets the value of the <code>customField6</code> property.
    *
    * @param customField6 the value for the <code>customField6</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField6(String customField6) {
      this.customField6 = customField6;
   }

   /**
    * Returns the value of the <code>customField7</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_7"  , length=250 )
   public String getCustomField7() {
      return customField7;
   }

   /**
    * Sets the value of the <code>customField7</code> property.
    *
    * @param customField7 the value for the <code>customField7</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField7(String customField7) {
      this.customField7 = customField7;
   }

   /**
    * Returns the value of the <code>customField8</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_8"  , length=250 )
   public String getCustomField8() {
      return customField8;
   }

   /**
    * Sets the value of the <code>customField8</code> property.
    *
    * @param customField8 the value for the <code>customField8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField8(String customField8) {
      this.customField8 = customField8;
   }

   /**
    * Returns the value of the <code>customField9</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_9"  , length=250 )
   public String getCustomField9() {
      return customField9;
   }

   /**
    * Sets the value of the <code>customField9</code> property.
    *
    * @param customField9 the value for the <code>customField9</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField9(String customField9) {
      this.customField9 = customField9;
   }

   /**
    * Returns the value of the <code>customField10</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_10"  , length=250 )
   public String getCustomField10() {
      return customField10;
   }

   /**
    * Sets the value of the <code>customField10</code> property.
    *
    * @param customField10 the value for the <code>customField10</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomField10(String customField10) {
      this.customField10 = customField10;
   }
   
   @Column(name = "CUSTOM_FIELD_11"  , length=250 )
	public String getCustomField11() {
		return customField11;
	}

	public void setCustomField11(String customField11) {
		this.customField11 = customField11;
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
    * Returns the value of the <code>userDeviceAccountsIdUserDeviceAccountsModel</code> relation property.
    *
    * @return the value of the <code>userDeviceAccountsIdUserDeviceAccountsModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "USER_DEVICE_ACCOUNTS_ID")    
   public UserDeviceAccountsModel getRelationUserDeviceAccountsIdUserDeviceAccountsModel(){
      return userDeviceAccountsIdUserDeviceAccountsModel;
   }
    
   /**
    * Returns the value of the <code>userDeviceAccountsIdUserDeviceAccountsModel</code> relation property.
    *
    * @return the value of the <code>userDeviceAccountsIdUserDeviceAccountsModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public UserDeviceAccountsModel getUserDeviceAccountsIdUserDeviceAccountsModel(){
      return getRelationUserDeviceAccountsIdUserDeviceAccountsModel();
   }

   /**
    * Sets the value of the <code>userDeviceAccountsIdUserDeviceAccountsModel</code> relation property.
    *
    * @param userDeviceAccountsModel a value for <code>userDeviceAccountsIdUserDeviceAccountsModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUserDeviceAccountsIdUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      this.userDeviceAccountsIdUserDeviceAccountsModel = userDeviceAccountsModel;
   }
   
   /**
    * Sets the value of the <code>userDeviceAccountsIdUserDeviceAccountsModel</code> relation property.
    *
    * @param userDeviceAccountsModel a value for <code>userDeviceAccountsIdUserDeviceAccountsModel</code>.
    */
   @javax.persistence.Transient
   public void setUserDeviceAccountsIdUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel) {
      if(null != userDeviceAccountsModel)
      {
      	setRelationUserDeviceAccountsIdUserDeviceAccountsModel((UserDeviceAccountsModel)userDeviceAccountsModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>usecaseIdUsecaseModel</code> relation property.
    *
    * @return the value of the <code>usecaseIdUsecaseModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "USECASE_ID")    
   public UsecaseModel getRelationUsecaseIdUsecaseModel(){
      return usecaseIdUsecaseModel;
   }
    
   /**
    * Returns the value of the <code>usecaseIdUsecaseModel</code> relation property.
    *
    * @return the value of the <code>usecaseIdUsecaseModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public UsecaseModel getUsecaseIdUsecaseModel(){
      return getRelationUsecaseIdUsecaseModel();
   }

   /**
    * Sets the value of the <code>usecaseIdUsecaseModel</code> relation property.
    *
    * @param usecaseModel a value for <code>usecaseIdUsecaseModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUsecaseIdUsecaseModel(UsecaseModel usecaseModel) {
      this.usecaseIdUsecaseModel = usecaseModel;
   }
   
   /**
    * Sets the value of the <code>usecaseIdUsecaseModel</code> relation property.
    *
    * @param usecaseModel a value for <code>usecaseIdUsecaseModel</code>.
    */
   @javax.persistence.Transient
   public void setUsecaseIdUsecaseModel(UsecaseModel usecaseModel) {
      if(null != usecaseModel)
      {
      	setRelationUsecaseIdUsecaseModel((UsecaseModel)usecaseModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @return the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DEVICE_TYPE_ID")    
   public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel(){
      return deviceTypeIdDeviceTypeModel;
   }
    
   /**
    * Returns the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @return the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DeviceTypeModel getDeviceTypeIdDeviceTypeModel(){
      return getRelationDeviceTypeIdDeviceTypeModel();
   }

   /**
    * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @param deviceTypeModel a value for <code>deviceTypeIdDeviceTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      this.deviceTypeIdDeviceTypeModel = deviceTypeModel;
   }
   
   /**
    * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @param deviceTypeModel a value for <code>deviceTypeIdDeviceTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      if(null != deviceTypeModel)
      {
      	setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel)deviceTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @return the value of the <code>commandIdCommandModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COMMAND_ID")    
   public CommandModel getRelationCommandIdCommandModel(){
      return commandIdCommandModel;
   }
    
   /**
    * Returns the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @return the value of the <code>commandIdCommandModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommandModel getCommandIdCommandModel(){
      return getRelationCommandIdCommandModel();
   }

   /**
    * Sets the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @param commandModel a value for <code>commandIdCommandModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommandIdCommandModel(CommandModel commandModel) {
      this.commandIdCommandModel = commandModel;
   }
   
   /**
    * Sets the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @param commandModel a value for <code>commandIdCommandModel</code>.
    */
   @javax.persistence.Transient
   public void setCommandIdCommandModel(CommandModel commandModel) {
      if(null != commandModel)
      {
      	setRelationCommandIdCommandModel((CommandModel)commandModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @return the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_USER_ID")    
   public AppUserModel getRelationAppUserIdAppUserModel(){
      return appUserIdAppUserModel;
   }
    
   /**
    * Returns the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @return the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getAppUserIdAppUserModel(){
      return getRelationAppUserIdAppUserModel();
   }

   /**
    * Sets the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>appUserIdAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAppUserIdAppUserModel(AppUserModel appUserModel) {
      this.appUserIdAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>appUserIdAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setAppUserIdAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationAppUserIdAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>actionStatusIdActionStatusModel</code> relation property.
    *
    * @return the value of the <code>actionStatusIdActionStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACTION_STATUS_ID")    
   public ActionStatusModel getRelationActionStatusIdActionStatusModel(){
      return actionStatusIdActionStatusModel;
   }
    
   /**
    * Returns the value of the <code>actionStatusIdActionStatusModel</code> relation property.
    *
    * @return the value of the <code>actionStatusIdActionStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ActionStatusModel getActionStatusIdActionStatusModel(){
      return getRelationActionStatusIdActionStatusModel();
   }

   /**
    * Sets the value of the <code>actionStatusIdActionStatusModel</code> relation property.
    *
    * @param actionStatusModel a value for <code>actionStatusIdActionStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
      this.actionStatusIdActionStatusModel = actionStatusModel;
   }
   
   /**
    * Sets the value of the <code>actionStatusIdActionStatusModel</code> relation property.
    *
    * @param actionStatusModel a value for <code>actionStatusIdActionStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
      if(null != actionStatusModel)
      {
      	setRelationActionStatusIdActionStatusModel((ActionStatusModel)actionStatusModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>actionIdActionModel</code> relation property.
    *
    * @return the value of the <code>actionIdActionModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACTION_ID")    
   public ActionModel getRelationActionIdActionModel(){
      return actionIdActionModel;
   }
    
   /**
    * Returns the value of the <code>actionIdActionModel</code> relation property.
    *
    * @return the value of the <code>actionIdActionModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ActionModel getActionIdActionModel(){
      return getRelationActionIdActionModel();
   }

   /**
    * Sets the value of the <code>actionIdActionModel</code> relation property.
    *
    * @param actionModel a value for <code>actionIdActionModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationActionIdActionModel(ActionModel actionModel) {
      this.actionIdActionModel = actionModel;
   }
   
   /**
    * Sets the value of the <code>actionIdActionModel</code> relation property.
    *
    * @param actionModel a value for <code>actionIdActionModel</code>.
    */
   @javax.persistence.Transient
   public void setActionIdActionModel(ActionModel actionModel) {
      if(null != actionModel)
      {
      	setRelationActionIdActionModel((ActionModel)actionModel.clone());
      }      
   }
   


   /**
    * Add the related AuditLogModel to this one-to-many relation.
    *
    * @param auditLogModel object to be added.
    */
    
   public void addActionLogIdAuditLogModel(AuditLogModel auditLogModel) {
      auditLogModel.setRelationActionLogIdActionLogModel(this);
      actionLogIdAuditLogModelList.add(auditLogModel);
   }
   
   /**
    * Remove the related AuditLogModel to this one-to-many relation.
    *
    * @param auditLogModel object to be removed.
    */
   
   public void removeActionLogIdAuditLogModel(AuditLogModel auditLogModel) {      
      auditLogModel.setRelationActionLogIdActionLogModel(null);
      actionLogIdAuditLogModelList.remove(auditLogModel);      
   }

   /**
    * Get a list of related AuditLogModel objects of the ActionLogModel object.
    * These objects are in a bidirectional one-to-many relation by the ActionLogId member.
    *
    * @return Collection of AuditLogModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationActionLogIdActionLogModel")
   @JoinColumn(name = "ACTION_LOG_ID")
   public Collection<AuditLogModel> getActionLogIdAuditLogModelList() throws Exception {
   		return actionLogIdAuditLogModelList;
   }


   /**
    * Set a list of AuditLogModel related objects to the ActionLogModel object.
    * These objects are in a bidirectional one-to-many relation by the ActionLogId member.
    *
    * @param auditLogModelList the list of related objects.
    */
    public void setActionLogIdAuditLogModelList(Collection<AuditLogModel> auditLogModelList) throws Exception {
		this.actionLogIdAuditLogModelList = auditLogModelList;
   }


   /**
    * Add the related TransactionCodeModel to this one-to-many relation.
    *
    * @param transactionCodeModel object to be added.
    */
    
   public void addActionLogIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      transactionCodeModel.setRelationActionLogIdActionLogModel(this);
      actionLogIdTransactionCodeModelList.add(transactionCodeModel);
   }
   
   /**
    * Remove the related TransactionCodeModel to this one-to-many relation.
    *
    * @param transactionCodeModel object to be removed.
    */
   
   public void removeActionLogIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {      
      transactionCodeModel.setRelationActionLogIdActionLogModel(null);
      actionLogIdTransactionCodeModelList.remove(transactionCodeModel);      
   }

   /**
    * Get a list of related TransactionCodeModel objects of the ActionLogModel object.
    * These objects are in a bidirectional one-to-many relation by the ActionLogId member.
    *
    * @return Collection of TransactionCodeModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationActionLogIdActionLogModel")
   @JoinColumn(name = "ACTION_LOG_ID")
   public Collection<TransactionCodeModel> getActionLogIdTransactionCodeModelList() throws Exception {
   		return actionLogIdTransactionCodeModelList;
   }


   /**
    * Set a list of TransactionCodeModel related objects to the ActionLogModel object.
    * These objects are in a bidirectional one-to-many relation by the ActionLogId member.
    *
    * @param transactionCodeModelList the list of related objects.
    */
    public void setActionLogIdTransactionCodeModelList(Collection<TransactionCodeModel> transactionCodeModelList) throws Exception {
		this.actionLogIdTransactionCodeModelList = transactionCodeModelList;
   }



   /**
    * Returns the value of the <code>userDeviceAccountsId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUserDeviceAccountsId() {
      if (userDeviceAccountsIdUserDeviceAccountsModel != null) {
         return userDeviceAccountsIdUserDeviceAccountsModel.getUserDeviceAccountsId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>userDeviceAccountsId</code> property.
    *
    * @param userDeviceAccountsId the value for the <code>userDeviceAccountsId</code> property
																																																					    */
   
   @javax.persistence.Transient
   public void setUserDeviceAccountsId(Long userDeviceAccountsId) {
      if(userDeviceAccountsId == null)
      {      
      	userDeviceAccountsIdUserDeviceAccountsModel = null;
      }
      else
      {
        userDeviceAccountsIdUserDeviceAccountsModel = new UserDeviceAccountsModel();
      	userDeviceAccountsIdUserDeviceAccountsModel.setUserDeviceAccountsId(userDeviceAccountsId);
      }      
   }

   /**
    * Returns the value of the <code>usecaseId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUsecaseId() {
      if (usecaseIdUsecaseModel != null) {
         return usecaseIdUsecaseModel.getUsecaseId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>usecaseId</code> property.
    *
    * @param usecaseId the value for the <code>usecaseId</code> property
																																																					    */
   
   @javax.persistence.Transient
   public void setUsecaseId(Long usecaseId) {
      if(usecaseId == null)
      {      
      	usecaseIdUsecaseModel = null;
      }
      else
      {
        usecaseIdUsecaseModel = new UsecaseModel();
      	usecaseIdUsecaseModel.setUsecaseId(usecaseId);
      }      
   }

   /**
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDeviceTypeId() {
      if (deviceTypeIdDeviceTypeModel != null) {
         return deviceTypeIdDeviceTypeModel.getDeviceTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
																																																					    */
   
   @javax.persistence.Transient
   public void setDeviceTypeId(Long deviceTypeId) {
      if(deviceTypeId == null)
      {      
      	deviceTypeIdDeviceTypeModel = null;
      }
      else
      {
        deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
      	deviceTypeIdDeviceTypeModel.setDeviceTypeId(deviceTypeId);
      }      
   }

   /**
    * Returns the value of the <code>commandId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommandId() {
      if (commandIdCommandModel != null) {
         return commandIdCommandModel.getCommandId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commandId</code> property.
    *
    * @param commandId the value for the <code>commandId</code> property
																																																					    */
   
   @javax.persistence.Transient
   public void setCommandId(Long commandId) {
      if(commandId == null)
      {      
      	commandIdCommandModel = null;
      }
      else
      {
        commandIdCommandModel = new CommandModel();
      	commandIdCommandModel.setCommandId(commandId);
      }      
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAppUserId() {
      if (appUserIdAppUserModel != null) {
         return appUserIdAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																																																					    */
   
   @javax.persistence.Transient
   public void setAppUserId(Long appUserId) {
      if(appUserId == null)
      {      
      	appUserIdAppUserModel = null;
      }
      else
      {
        appUserIdAppUserModel = new AppUserModel();
      	appUserIdAppUserModel.setAppUserId(appUserId);
      }      
   }

   /**
    * Returns the value of the <code>actionStatusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getActionStatusId() {
      if (actionStatusIdActionStatusModel != null) {
         return actionStatusIdActionStatusModel.getActionStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>actionStatusId</code> property.
    *
    * @param actionStatusId the value for the <code>actionStatusId</code> property
											    * @spring.validator type="required"
																																											    */
   
   @javax.persistence.Transient
   public void setActionStatusId(Long actionStatusId) {
      if(actionStatusId == null)
      {      
      	actionStatusIdActionStatusModel = null;
      }
      else
      {
        actionStatusIdActionStatusModel = new ActionStatusModel();
      	actionStatusIdActionStatusModel.setActionStatusId(actionStatusId);
      }      
   }

   /**
    * Returns the value of the <code>actionId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getActionId() {
      if (actionIdActionModel != null) {
         return actionIdActionModel.getActionId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>actionId</code> property.
    *
    * @param actionId the value for the <code>actionId</code> property
																																																					    */
   
   @javax.persistence.Transient
   public void setActionId(Long actionId) {
      if(actionId == null)
      {      
      	actionIdActionModel = null;
      }
      else
      {
        actionIdActionModel = new ActionModel();
      	actionIdActionModel.setActionId(actionId);
      }      
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
    
    @Column(name="ACTION_AUTH_ID")
	public Long getActionAuthorizationId() {
		return actionAuthorizationId;
	}

	public void setActionAuthorizationId(Long actionAuthorizationId) {
		this.actionAuthorizationId = actionAuthorizationId;
	}

   @Column(name="CLIENT_IP")
   public String getClientIpAddress() {
      return clientIpAddress;
   }

   public void setClientIpAddress(String clientIpAddress) {
      this.clientIpAddress = clientIpAddress;
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
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("UserDeviceAccountsModel");
    	associationModel.setPropertyName("relationUserDeviceAccountsIdUserDeviceAccountsModel");   		
   		associationModel.setValue(getRelationUserDeviceAccountsIdUserDeviceAccountsModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("UsecaseModel");
    	associationModel.setPropertyName("relationUsecaseIdUsecaseModel");   		
   		associationModel.setValue(getRelationUsecaseIdUsecaseModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DeviceTypeModel");
    	associationModel.setPropertyName("relationDeviceTypeIdDeviceTypeModel");   		
   		associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommandModel");
    	associationModel.setPropertyName("relationCommandIdCommandModel");   		
   		associationModel.setValue(getRelationCommandIdCommandModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationAppUserIdAppUserModel");   		
   		associationModel.setValue(getRelationAppUserIdAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ActionStatusModel");
    	associationModel.setPropertyName("relationActionStatusIdActionStatusModel");   		
   		associationModel.setValue(getRelationActionStatusIdActionStatusModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ActionModel");
    	associationModel.setPropertyName("relationActionIdActionModel");   		
   		associationModel.setValue(getRelationActionIdActionModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }

   @Column(name = "TRX_DATA"  )
   public String getTrxData() {
      return trxData;
   }

   public void setTrxData(String trxData) {
      this.trxData = trxData;
   }
}
