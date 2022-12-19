package com.inov8.verifly.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;


/**
 * The LogModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LogModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="LOG_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="LOG_seq") } )
//@javax.persistence.SequenceGenerator(name = "LOG_seq",sequenceName = "LOG_seq")
@Table(name = "LOG")
public class LogModel extends BasePersistableModel {


   private StatusModel statusIdStatusModel;
   private VfFailureReasonModel failureReasonIdFailureReasonModel;
   private VfActionModel actionIdActionModel;
   private AccountInfoModel accountInfoIdAccountInfoModel;

   private transient Collection<LogDetailModel> logIdLogDetailModelList = new ArrayList<LogDetailModel>();

   private Long logId;
   private Long transactionCodeId;
   private Date startTime;
   private Date endTime;
   private String inputParam;
   private String outputParam;
   private String createdBy;
   private Long creatdByUserId;

   /**
    * Default constructor.
    */
   public LogModel() {
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getLogId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setLogId(primaryKey);
    }

   /**
    * Returns the value of the <code>logId</code> property.
    *
    */
      @Column(name = "LOG_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_seq")
   public Long getLogId() {
      return logId;
   }

   /**
    * Sets the value of the <code>logId</code> property.
    *
    * @param logId the value for the <code>logId</code> property
    *
                    */

   public void setLogId(Long logId) {
      this.logId = logId;
   }

   /**
    * Returns the value of the <code>transactionCodeId</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE_ID")
   public Long getTransactionCodeId() {
      return transactionCodeId;
   }

   /**
    * Sets the value of the <code>transactionCodeId</code> property.
    *
    * @param transactionCodeId the value for the <code>transactionCodeId</code> property
    *
                    * @spring.validator type="required"
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
    * Returns the value of the <code>startTime</code> property.
    *
    */
      @Column(name = "START_TIME"  )
   public Date getStartTime() {
      return startTime;
   }

   /**
    * Sets the value of the <code>startTime</code> property.
    *
    * @param startTime the value for the <code>startTime</code> property
    *
                    * @spring.validator type="date"
    */

   public void setStartTime(Date startTime) {
      this.startTime = startTime;
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
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY" , length=50 )
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
    * Returns the value of the <code>creatdByUserId</code> property.
    *
    */
      @Column(name = "CREATD_BY_USER_ID" )
   public Long getCreatdByUserId() {
      return creatdByUserId;
   }

   /**
    * Sets the value of the <code>creatdByUserId</code> property.
    *
    * @param creatdByUserId the value for the <code>creatdByUserId</code> property
    *
                    * @spring.validator type="required"
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
    * Returns the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @return the value of the <code>statusIdStatusModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "STATUS_ID")
   public StatusModel getRelationStatusIdStatusModel(){
      return statusIdStatusModel;
   }

   /**
    * Returns the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @return the value of the <code>statusIdStatusModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public StatusModel getStatusIdStatusModel(){
      return getRelationStatusIdStatusModel();
   }

   /**
    * Sets the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @param statusModel a value for <code>statusIdStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationStatusIdStatusModel(StatusModel statusModel) {
      this.statusIdStatusModel = statusModel;
   }

   /**
    * Sets the value of the <code>statusIdStatusModel</code> relation property.
    *
    * @param statusModel a value for <code>statusIdStatusModel</code>.
    */
   @javax.persistence.Transient
   public void setStatusIdStatusModel(StatusModel statusModel) {
      if(null != statusModel)
      {
        setRelationStatusIdStatusModel((StatusModel)statusModel.clone());
      }
   }


   /**
    * Returns the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @return the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "FAILURE_REASON_ID")
   public VfFailureReasonModel getRelationFailureReasonIdFailureReasonModel(){
      return failureReasonIdFailureReasonModel;
   }

   /**
    * Returns the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @return the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public VfFailureReasonModel getFailureReasonIdFailureReasonModel(){
      return getRelationFailureReasonIdFailureReasonModel();
   }

   /**
    * Sets the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @param failureReasonModel a value for <code>failureReasonIdFailureReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationFailureReasonIdFailureReasonModel(VfFailureReasonModel failureReasonModel) {
      this.failureReasonIdFailureReasonModel = failureReasonModel;
   }

   /**
    * Sets the value of the <code>failureReasonIdFailureReasonModel</code> relation property.
    *
    * @param failureReasonModel a value for <code>failureReasonIdFailureReasonModel</code>.
    */
   @javax.persistence.Transient
   public void setFailureReasonIdFailureReasonModel(VfFailureReasonModel failureReasonModel) {
      if(null != failureReasonModel)
      {
        setRelationFailureReasonIdFailureReasonModel((VfFailureReasonModel)failureReasonModel.clone());
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
   public VfActionModel getRelationActionIdActionModel(){
      return actionIdActionModel;
   }

   /**
    * Returns the value of the <code>actionIdActionModel</code> relation property.
    *
    * @return the value of the <code>actionIdActionModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public VfActionModel getActionIdActionModel(){
      return getRelationActionIdActionModel();
   }

   /**
    * Sets the value of the <code>actionIdActionModel</code> relation property.
    *
    * @param actionModel a value for <code>actionIdActionModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationActionIdActionModel(VfActionModel actionModel) {
      this.actionIdActionModel = actionModel;
   }

   /**
    * Sets the value of the <code>actionIdActionModel</code> relation property.
    *
    * @param actionModel a value for <code>actionIdActionModel</code>.
    */
   @javax.persistence.Transient
   public void setActionIdActionModel(VfActionModel actionModel) {
      if(null != actionModel)
      {
        setRelationActionIdActionModel((VfActionModel)actionModel.clone());
      }
   }


   /**
    * Returns the value of the <code>accountInfoIdAccountInfoModel</code> relation property.
    *
    * @return the value of the <code>accountInfoIdAccountInfoModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACCOUNT_INFO_ID")
   public AccountInfoModel getRelationAccountInfoIdAccountInfoModel(){
      return accountInfoIdAccountInfoModel;
   }

   /**
    * Returns the value of the <code>accountInfoIdAccountInfoModel</code> relation property.
    *
    * @return the value of the <code>accountInfoIdAccountInfoModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AccountInfoModel getAccountInfoIdAccountInfoModel(){
      return getRelationAccountInfoIdAccountInfoModel();
   }

   /**
    * Sets the value of the <code>accountInfoIdAccountInfoModel</code> relation property.
    *
    * @param accountInfoModel a value for <code>accountInfoIdAccountInfoModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAccountInfoIdAccountInfoModel(AccountInfoModel accountInfoModel) {
      this.accountInfoIdAccountInfoModel = accountInfoModel;
   }

   /**
    * Sets the value of the <code>accountInfoIdAccountInfoModel</code> relation property.
    *
    * @param accountInfoModel a value for <code>accountInfoIdAccountInfoModel</code>.
    */
   @javax.persistence.Transient
   public void setAccountInfoIdAccountInfoModel(AccountInfoModel accountInfoModel) {
      if(null != accountInfoModel)
      {
        setRelationAccountInfoIdAccountInfoModel((AccountInfoModel)accountInfoModel.clone());
      }
   }



   /**
    * Add the related LogDetailModel to this one-to-many relation.
    *
    * @param logDetailModel object to be added.
    */

   public void addLogIdLogDetailModel(LogDetailModel logDetailModel) {
      logDetailModel.setRelationLogIdLogModel(this);
      logIdLogDetailModelList.add(logDetailModel);
   }

   /**
    * Remove the related LogDetailModel to this one-to-many relation.
    *
    * @param logDetailModel object to be removed.
    */

   public void removeLogIdLogDetailModel(LogDetailModel logDetailModel) {
      logDetailModel.setRelationLogIdLogModel(null);
      logIdLogDetailModelList.remove(logDetailModel);
   }

   /**
    * Get a list of related LogDetailModel objects of the LogModel object.
    * These objects are in a bidirectional one-to-many relation by the LogId member.
    *
    * @return Collection of LogDetailModel objects.
    *
    */

   @OneToMany(cascade =
              {
              CascadeType.ALL},
              fetch = FetchType.LAZY,
              mappedBy = "relationLogIdLogModel")
   @JoinColumn(name = "LOG_ID")
   @org.hibernate.annotations.Cascade(
      {org.hibernate.annotations.CascadeType.ALL})
   public Collection<LogDetailModel> getLogIdLogDetailModelList() throws Exception {
                return logIdLogDetailModelList;
   }


   /**
    * Set a list of LogDetailModel related objects to the LogModel object.
    * These objects are in a bidirectional one-to-many relation by the LogId member.
    *
    * @param logDetailModelList the list of related objects.
    */
    public void setLogIdLogDetailModelList(Collection<LogDetailModel> logDetailModelList) throws Exception {
                this.logIdLogDetailModelList = logDetailModelList;
   }



   /**
    * Returns the value of the <code>statusId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getStatusId() {
      if (statusIdStatusModel != null) {
         return statusIdStatusModel.getStatusId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>statusId</code> property.
    *
    * @param statusId the value for the <code>statusId</code> property
                                                                                            * @spring.validator type="required"
                                                                                                                                            */

   @javax.persistence.Transient
   public void setStatusId(Long statusId) {
      if(null != statusId)
      {
        if (statusIdStatusModel == null) {
                statusIdStatusModel = new StatusModel();
        }
        statusIdStatusModel.setStatusId(statusId);
      }
   }

   /**
    * Returns the value of the <code>failureReasonId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getFailureReasonId() {
      if (failureReasonIdFailureReasonModel != null) {
         return failureReasonIdFailureReasonModel.getFailureReasonId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>failureReasonId</code> property.
    *
    * @param failureReasonId the value for the <code>failureReasonId</code> property
                                                                                                                                                                                                                            */

   @javax.persistence.Transient
   public void setFailureReasonId(Long failureReasonId) {
      if(null != failureReasonId)
      {
        if (failureReasonIdFailureReasonModel == null) {
                failureReasonIdFailureReasonModel = new VfFailureReasonModel();
        }
        failureReasonIdFailureReasonModel.setFailureReasonId(failureReasonId);
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
                                            * @spring.validator type="required"
                                                                                                                                                                                            */

   @javax.persistence.Transient
   public void setActionId(Long actionId) {
      if(null != actionId)
      {
        if (actionIdActionModel == null) {
                actionIdActionModel = new VfActionModel();
        }
        actionIdActionModel.setActionId(actionId);
      }
   }

   /**
    * Returns the value of the <code>accountInfoId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAccountInfoId() {
      if (accountInfoIdAccountInfoModel != null) {
         return accountInfoIdAccountInfoModel.getAccountInfoId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>accountInfoId</code> property.
    *
    * @param accountInfoId the value for the <code>accountInfoId</code> property
                                                                                                                                                                                                                            */

   @javax.persistence.Transient
   public void setAccountInfoId(Long accountInfoId) {
      if(null != accountInfoId)
      {
        if (accountInfoIdAccountInfoModel == null) {
                accountInfoIdAccountInfoModel = new AccountInfoModel();
        }
        accountInfoIdAccountInfoModel.setAccountInfoId(accountInfoId);
      }
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getLogId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&logId=" + getLogId();
      return parameters;
   }
        /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
                        String primaryKeyFieldName = "logId";
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

        associationModel.setClassName("StatusModel");
        associationModel.setPropertyName("relationStatusIdStatusModel");
                associationModel.setValue(getRelationStatusIdStatusModel());

                associationModelList.add(associationModel);

                              associationModel = new AssociationModel();

        associationModel.setClassName("FailureReasonModel");
        associationModel.setPropertyName("relationFailureReasonIdFailureReasonModel");
                associationModel.setValue(getRelationFailureReasonIdFailureReasonModel());

                associationModelList.add(associationModel);

                              associationModel = new AssociationModel();

        associationModel.setClassName("ActionModel");
        associationModel.setPropertyName("relationActionIdActionModel");
                associationModel.setValue(getRelationActionIdActionModel());

                associationModelList.add(associationModel);

                              associationModel = new AssociationModel();

        associationModel.setClassName("AccountInfoModel");
        associationModel.setPropertyName("relationAccountInfoIdAccountInfoModel");
                associationModel.setValue(getRelationAccountInfoIdAccountInfoModel());

                associationModelList.add(associationModel);


        return associationModelList;
    }

}
