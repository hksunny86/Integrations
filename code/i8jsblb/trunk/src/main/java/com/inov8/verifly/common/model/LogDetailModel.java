package com.inov8.verifly.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The LogDetailModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LogDetailModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="LOG_DETAIL_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="LOG_DETAIL_seq") } )
//@javax.persistence.SequenceGenerator(name = "LOG_DETAIL_seq",sequenceName = "LOG_DETAIL_seq")
@Table(name = "LOG_DETAIL")
public class LogDetailModel extends BasePersistableModel {


   private StatusModel statusIdStatusModel;
   private LogModel logIdLogModel;
   private VfFailureReasonModel failureReasonIdFailureReasonModel;
   private VfActionModel actionIdActionModel;


   private Long logDetailId;

   /**
    * Default constructor.
    */
   public LogDetailModel() {
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getLogDetailId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setLogDetailId(primaryKey);
    }

   /**
    * Returns the value of the <code>logDetailId</code> property.
    *
    */
      @Column(name = "LOG_DETAIL_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_DETAIL_seq")
   public Long getLogDetailId() {
      return logDetailId;
   }

   /**
    * Sets the value of the <code>logDetailId</code> property.
    *
    * @param logDetailId the value for the <code>logDetailId</code> property
    *
		    */

   public void setLogDetailId(Long logDetailId) {
      this.logDetailId = logDetailId;
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
    * Returns the value of the <code>logIdLogModel</code> relation property.
    *
    * @return the value of the <code>logIdLogModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "LOG_ID")
   public LogModel getRelationLogIdLogModel(){
      return logIdLogModel;
   }

   /**
    * Returns the value of the <code>logIdLogModel</code> relation property.
    *
    * @return the value of the <code>logIdLogModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public LogModel getLogIdLogModel(){
      return getRelationLogIdLogModel();
   }

   /**
    * Sets the value of the <code>logIdLogModel</code> relation property.
    *
    * @param logModel a value for <code>logIdLogModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationLogIdLogModel(LogModel logModel) {
      this.logIdLogModel = logModel;
   }

   /**
    * Sets the value of the <code>logIdLogModel</code> relation property.
    *
    * @param logModel a value for <code>logIdLogModel</code>.
    */
   @javax.persistence.Transient
   public void setLogIdLogModel(LogModel logModel) {
      if(null != logModel)
      {
      	setRelationLogIdLogModel((LogModel)logModel.clone());
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
    * Returns the value of the <code>logId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getLogId() {
      if (logIdLogModel != null) {
         return logIdLogModel.getLogId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>logId</code> property.
    *
    * @param logId the value for the <code>logId</code> property
					    * @spring.validator type="required"
									    */

   @javax.persistence.Transient
   public void setLogId(Long logId) {
      if(null != logId)
      {
      	if (logIdLogModel == null) {
        	logIdLogModel = new LogModel();
      	}
      	logIdLogModel.setLogId(logId);
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getLogDetailId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&logDetailId=" + getLogDetailId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
			String primaryKeyFieldName = "logDetailId";
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

    	associationModel.setClassName("LogModel");
    	associationModel.setPropertyName("relationLogIdLogModel");
   		associationModel.setValue(getRelationLogIdLogModel());

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


    	return associationModelList;
    }

}
