package com.inov8.microbank.common.model.portal.authorizationmodule;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AppUserModel;

/**
* ActionAuthorizationHistory entity bean.
*
* @author  Hassan javaid  Inov8 Limited
* @version $Revision: 1.00 $, $Date: 18/06/2014
*
*
* @spring.bean name="ActionAuthorizationModel"
*/
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACTION_AUTH_HISTORY_SEQ",sequenceName = "ACTION_AUTH_HISTORY_SEQ", allocationSize=1)
@Table(name = "ACTION_AUTH_HISTORY")
public class ActionAuthorizationHistoryModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = 7057141495196011490L;

	private ActionStatusModel actionStatusIdActionStatusModel;
	private AppUserModel checkedByAppUserModel;
	private ActionAuthorizationModel actionAthorizationIdActionAuthorizationModel;

	private Long actionAuthHistoryId;
	private Long escalationLevel;
	private Date checkedOn;
	private String checkerComments;
	private Date intimatedOn;
	private String intimatedTo;
	private java.sql.Timestamp startTime;

	// Constructors

	/** default constructor */
	public ActionAuthorizationHistoryModel() {
	}

	public ActionAuthorizationHistoryModel(
			ActionStatusModel actionStatusIdActionStatusModel,
			AppUserModel checkedByAppUserModel,
			ActionAuthorizationModel actionAthorizationIdActionAuthorizationModel,
			Long escalationLevel, Date checkedOn, String checkerComments,
			Date intimatedOn, String intimatedTo) {
		super();
		this.actionStatusIdActionStatusModel = actionStatusIdActionStatusModel;
		this.checkedByAppUserModel = checkedByAppUserModel;
		this.actionAthorizationIdActionAuthorizationModel = actionAthorizationIdActionAuthorizationModel;
		this.escalationLevel = escalationLevel;
		this.checkedOn = checkedOn;
		this.checkerComments = checkerComments;
		this.intimatedOn = intimatedOn;
		this.intimatedTo = intimatedTo;
	}
	
	
  @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getActionAuthHistoryId();
    }

    /**
 * Set the primary key.
 *
 * @param primaryKey the primary key
 */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setActionAuthHistoryId(primaryKey);
    }

	
	@Column(name = "ACTION_AUTH_HISTORY_ID")
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACTION_AUTH_HISTORY_SEQ")
	public Long getActionAuthHistoryId() {
		return this.actionAuthHistoryId;
	}

	public void setActionAuthHistoryId(Long actionAuthHistoryId) {
		this.actionAuthHistoryId = actionAuthHistoryId;
	}

	
	

	@Column(name = "ESCALATION_LEVEL")
	public Long getEscalationLevel() {
		return this.escalationLevel;
	}

	public void setEscalationLevel(Long escalationLevel) {
		this.escalationLevel = escalationLevel;
	}


	@Column(name = "CHECKED_ON")
	public Date getCheckedOn() {
		return this.checkedOn;
	}

	public void setCheckedOn(Date checkedOn) {
		this.checkedOn = checkedOn;
	}

	@Column(name = "CHECKER_COMMENTS")
	public String getCheckerComments() {
		return this.checkerComments;
	}
	@Column(name = "INTIMATED_ON")
	public Date getIntimatedOn() {
		return intimatedOn;
	}

	
	public void setIntimatedOn(Date intimatedOn) {
		this.intimatedOn = intimatedOn;
	}

	@Column(name = "INTIMATED_TO")
	public String getIntimatedTo() {
		return intimatedTo;
	}

	public void setIntimatedTo(String intimatedTo) {
		this.intimatedTo = intimatedTo;
	}


	public void setCheckerComments(String checkerComments) {
		this.checkerComments = checkerComments;
	}
	  /** Helper method for Struts with displaytag
	    */
	   @javax.persistence.Transient
	   public String getPrimaryKeyParameter() {
	      String parameters = "";
	      parameters += "&actionAuthHistoryId=" + getActionAuthHistoryId();
	      return parameters;
	   }
		/**
	     * Helper method for default Sorting on Primary Keys
	     */
	    @javax.persistence.Transient
	    public String getPrimaryKeyFieldName()
	    { 
				String primaryKeyFieldName = "actionAuthHistoryId";
				return primaryKeyFieldName;				
	    }
	    
	    /**
		    * Returns the value of the <code>ActionStatusIdActionStatusModel</code> relation property.
		    *
		    * @return the value of the <code>ActionStatusIdActionStatusModel</code> relation property.
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
		   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		   @JoinColumn(name = "ACTION_STATUS_ID")    
		   public ActionStatusModel getRelationActionStatusIdActionStatusModel(){
		      return actionStatusIdActionStatusModel ;
		   }
		    
		   @javax.persistence.Transient
		   public ActionStatusModel getActionStatusIdActionStatusModel(){
		      return getRelationActionStatusIdActionStatusModel();
		   }

		   @javax.persistence.Transient
		   public void setRelationActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
		      this.actionStatusIdActionStatusModel = actionStatusModel;
		   }
		   
		   @javax.persistence.Transient
		   public void setActionStatusIdActionStatusModel(ActionStatusModel actionStatusModel) {
		      if(null != actionStatusModel)
		      {
		    	  setRelationActionStatusIdActionStatusModel((ActionStatusModel)actionStatusModel.clone());
		      }      
		   }
		   
		   
		  /////////////////////////////////  
		   /**
		    * Returns the value of the <code>ActionAthorizationIdActionAuthorizationModel</code> relation property.
		    *
		    * @return the value of the <code>ActionAthorizationIdActionAuthorizationModel</code> relation property.
		    *
		    */
		   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		   @JoinColumn(name = "ACTION_ATHORIZATION_ID")    
		   public ActionAuthorizationModel getRelationActionAthorizationIdActionAuthorizationModel(){
		      return actionAthorizationIdActionAuthorizationModel;
		   }
		    
		   @javax.persistence.Transient
		   public ActionAuthorizationModel getActionAthorizationIdActionAuthorizationModel(){
		      return getRelationActionAthorizationIdActionAuthorizationModel();
		   }

		   @javax.persistence.Transient
		   public void setRelationActionAthorizationIdActionAuthorizationModel(ActionAuthorizationModel actionAuthorizationModel) {
		      this.actionAthorizationIdActionAuthorizationModel = actionAuthorizationModel;
		   }
		   
		   @javax.persistence.Transient
		   public void setActionAthorizationIdActionAuthorizationModel(ActionAuthorizationModel actionAuthorizationModel) {
		      if(null != actionAuthorizationModel)
		      {
		    	  setRelationActionAthorizationIdActionAuthorizationModel((ActionAuthorizationModel)actionAuthorizationModel.clone());
		      }      
		   }
		   /**
		    * Returns the value of the <code>CheckedByAppUserModel</code> relation property.
		    *
		    * @return the value of the <code>CheckedByAppUserModel</code> relation property.
		    *
		    */
		   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		   @JoinColumn(name = "CHECKED_BY")    
		   public AppUserModel getRelationCheckedByAppUserModel(){
		      return checkedByAppUserModel;
		   }
		    
		   @javax.persistence.Transient
		   public AppUserModel getCheckedByAppUserModel(){
		      return getRelationCheckedByAppUserModel();
		   }

		 
		   @javax.persistence.Transient
		   public void setRelationCheckedByAppUserModel(AppUserModel appUserModel) {
		      this.checkedByAppUserModel = appUserModel;
		   }
		   
		  
		   @javax.persistence.Transient
		   public void setCheckedByAppUserModel(AppUserModel appUserModel) {
		      if(null != appUserModel)
		      {
		    	  setRelationCheckedByAppUserModel((AppUserModel)appUserModel.clone());
		      }      
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
			    	
		    	associationModel.setClassName("AppUserModel");
		    	associationModel.setPropertyName("relationCheckedByAppUserModel");   		
		   		associationModel.setValue(getRelationCheckedByAppUserModel());
		   		
		   		associationModelList.add(associationModel);
		    	
		    	associationModel = new AssociationModel();
		    	associationModel.setClassName("ActionAuthorizationModel");
		    	associationModel.setPropertyName("relationActionAthorizationIdActionAuthorizationModel");   		
		   		associationModel.setValue(getRelationActionAthorizationIdActionAuthorizationModel());
		   		
		   		associationModelList.add(associationModel);
		   		
		   		associationModel = new AssociationModel();
		    	
		    	associationModel.setClassName("ActionStatusModel");
		    	associationModel.setPropertyName("relationActionStatusIdActionStatusModel");   		
		   		associationModel.setValue(getRelationActionStatusIdActionStatusModel());
		   		
		   		associationModelList.add(associationModel);
		   		
					    	
		    	return associationModelList;
		    }

	@Column (name="START_TIME")
	public java.sql.Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(java.sql.Timestamp startTime) {
		this.startTime = startTime;
	}
}