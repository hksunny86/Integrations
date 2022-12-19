package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AuditLogModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AuditLogModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="AUDIT_LOG_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="AUDIT_LOG_seq") } )
//@javax.persistence.SequenceGenerator(name = "AUDIT_LOG_seq",sequenceName = "AUDIT_LOG_seq")
@Table(name = "AUDIT_LOG")
public class AuditLogModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -78726845561759117L;
private TransactionCodeModel transactionCodeIdTransactionCodeModel;
   private IntegrationModuleModel integrationModuleIdIntegrationModuleModel;
   private ActionLogModel actionLogIdActionLogModel;


   private Long auditLogId;
   private String inputParam;
   private String outputParam;
   private Integer versionNo;
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
   private java.sql.Timestamp transactionStartTime;
   private java.sql.Timestamp transactionEndTime;
   private String integrationPartnerIdentifier;

   /**
    * Default constructor.
    */
   public AuditLogModel() {
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
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUDIT_LOG_seq")
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
    * Returns the value of the <code>outputParam</code> property.
    *
    */
      @Column(name = "OUTPUT_PARAM"  )
   public String getOutputParam() {
      return outputParam;
   }

   /**
    * Sets the value of the <code>outputParam</code> property.
    *
    * @param outputParam the value for the <code>outputParam</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    */

   public void setOutputParam(String outputParam) {
      this.outputParam = outputParam;
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
    * Returns the value of the <code>customField1</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD1"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD2"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD3"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD4"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD5"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD6"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD7"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD8"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD9"  , length=250 )
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
      @Column(name = "CUSTOM_FIELD10"  , length=250 )
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
    * Returns the value of the <code>integrationPartnerIdentifier</code> property.
    *
    */
      @Column(name = "INTEGRATION_PARTNER_IDENTIFIER"  , length=250 )
   public String getIntegrationPartnerIdentifier() {
      return integrationPartnerIdentifier;
   }

   /**
    * Sets the value of the <code>integrationPartnerIdentifier</code> property.
    *
    * @param integrationPartnerIdentifier the value for the <code>integrationPartnerIdentifier</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setIntegrationPartnerIdentifier(String integrationPartnerIdentifier) {
      this.integrationPartnerIdentifier = integrationPartnerIdentifier;
   }

   /**
    * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "TRANSACTION_CODE_ID")    
   public TransactionCodeModel getRelationTransactionCodeIdTransactionCodeModel(){
      return transactionCodeIdTransactionCodeModel;
   }
    
   /**
    * Returns the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @return the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public TransactionCodeModel getTransactionCodeIdTransactionCodeModel(){
      return getRelationTransactionCodeIdTransactionCodeModel();
   }

   /**
    * Sets the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @param transactionCodeModel a value for <code>transactionCodeIdTransactionCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionCodeIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      this.transactionCodeIdTransactionCodeModel = transactionCodeModel;
   }
   
   /**
    * Sets the value of the <code>transactionCodeIdTransactionCodeModel</code> relation property.
    *
    * @param transactionCodeModel a value for <code>transactionCodeIdTransactionCodeModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionCodeIdTransactionCodeModel(TransactionCodeModel transactionCodeModel) {
      if(null != transactionCodeModel)
      {
      	setRelationTransactionCodeIdTransactionCodeModel((TransactionCodeModel)transactionCodeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>integrationModuleIdIntegrationModuleModel</code> relation property.
    *
    * @return the value of the <code>integrationModuleIdIntegrationModuleModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "INTEGRATION_MODULE_ID")    
   public IntegrationModuleModel getRelationIntegrationModuleIdIntegrationModuleModel(){
      return integrationModuleIdIntegrationModuleModel;
   }
    
   /**
    * Returns the value of the <code>integrationModuleIdIntegrationModuleModel</code> relation property.
    *
    * @return the value of the <code>integrationModuleIdIntegrationModuleModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public IntegrationModuleModel getIntegrationModuleIdIntegrationModuleModel(){
      return getRelationIntegrationModuleIdIntegrationModuleModel();
   }

   /**
    * Sets the value of the <code>integrationModuleIdIntegrationModuleModel</code> relation property.
    *
    * @param integrationModuleModel a value for <code>integrationModuleIdIntegrationModuleModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationIntegrationModuleIdIntegrationModuleModel(IntegrationModuleModel integrationModuleModel) {
      this.integrationModuleIdIntegrationModuleModel = integrationModuleModel;
   }
   
   /**
    * Sets the value of the <code>integrationModuleIdIntegrationModuleModel</code> relation property.
    *
    * @param integrationModuleModel a value for <code>integrationModuleIdIntegrationModuleModel</code>.
    */
   @javax.persistence.Transient
   public void setIntegrationModuleIdIntegrationModuleModel(IntegrationModuleModel integrationModuleModel) {
      if(null != integrationModuleModel)
      {
      	setRelationIntegrationModuleIdIntegrationModuleModel((IntegrationModuleModel)integrationModuleModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @return the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACTION_LOG_ID")    
   public ActionLogModel getRelationActionLogIdActionLogModel(){
      return actionLogIdActionLogModel;
   }
    
   /**
    * Returns the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @return the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ActionLogModel getActionLogIdActionLogModel(){
      return getRelationActionLogIdActionLogModel();
   }

   /**
    * Sets the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @param actionLogModel a value for <code>actionLogIdActionLogModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationActionLogIdActionLogModel(ActionLogModel actionLogModel) {
      this.actionLogIdActionLogModel = actionLogModel;
   }
   
   /**
    * Sets the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @param actionLogModel a value for <code>actionLogIdActionLogModel</code>.
    */
   @javax.persistence.Transient
   public void setActionLogIdActionLogModel(ActionLogModel actionLogModel) {
      if(null != actionLogModel)
      {
      	setRelationActionLogIdActionLogModel((ActionLogModel)actionLogModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionCodeId() {
      if (transactionCodeIdTransactionCodeModel != null) {
         return transactionCodeIdTransactionCodeModel.getTransactionCodeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
																																											    */
   
   @javax.persistence.Transient
   public void setTransactionCodeId(Long transactionCodeId) {
      if(transactionCodeId == null)
      {      
      	transactionCodeIdTransactionCodeModel = null;
      }
      else
      {
        transactionCodeIdTransactionCodeModel = new TransactionCodeModel();
      	transactionCodeIdTransactionCodeModel.setTransactionCodeId(transactionCodeId);
      }      
   }

   /**
    * Returns the value of the <code>integrationModuleId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getIntegrationModuleId() {
      if (integrationModuleIdIntegrationModuleModel != null) {
         return integrationModuleIdIntegrationModuleModel.getIntegrationModuleId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>integrationModuleId</code> property.
    *
    * @param integrationModuleId the value for the <code>integrationModuleId</code> property
																																			    * @spring.validator type="required"
									    */
   
   @javax.persistence.Transient
   public void setIntegrationModuleId(Long integrationModuleId) {
      if(integrationModuleId == null)
      {      
      	integrationModuleIdIntegrationModuleModel = null;
      }
      else
      {
        integrationModuleIdIntegrationModuleModel = new IntegrationModuleModel();
      	integrationModuleIdIntegrationModuleModel.setIntegrationModuleId(integrationModuleId);
      }      
   }

   /**
    * Returns the value of the <code>actionLogId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getActionLogId() {
      if (actionLogIdActionLogModel != null) {
         return actionLogIdActionLogModel.getActionLogId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>actionLogId</code> property.
    *
    * @param actionLogId the value for the <code>actionLogId</code> property
																																	    * @spring.validator type="required"
											    */
   
   @javax.persistence.Transient
   public void setActionLogId(Long actionLogId) {
      if(actionLogId == null)
      {      
      	actionLogIdActionLogModel = null;
      }
      else
      {
        actionLogIdActionLogModel = new ActionLogModel();
      	actionLogIdActionLogModel.setActionLogId(actionLogId);
      }      
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
    	
    	associationModel.setClassName("TransactionCodeModel");
    	associationModel.setPropertyName("relationTransactionCodeIdTransactionCodeModel");   		
   		associationModel.setValue(getRelationTransactionCodeIdTransactionCodeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("IntegrationModuleModel");
    	associationModel.setPropertyName("relationIntegrationModuleIdIntegrationModuleModel");   		
   		associationModel.setValue(getRelationIntegrationModuleIdIntegrationModuleModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ActionLogModel");
    	associationModel.setPropertyName("relationActionLogIdActionLogModel");   		
   		associationModel.setValue(getRelationActionLogIdActionLogModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
