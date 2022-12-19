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
import javax.persistence.Transient;
import javax.xml.bind.annotation.*;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.UsecaseModel;

/**
 * ActionAuthorization entity bean.
 *
 * @author  Hassan javaid  Inov8 Limited
 * @version $Revision: 1.00 $, $Date: 11/06/2014
 *
 *
 * @spring.bean name="ActionAuthorizationModel"
 */
@XmlRootElement(name="actionAuthorizationModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACTION_AUTHORIZATION_SEQ",sequenceName = "ACTION_AUTHORIZATION_SEQ", allocationSize=1)
@Table(name = "ACTION_AUTHORIZATION")
public class ActionAuthorizationModel extends BasePersistableModel implements Serializable {

	

	private static final long serialVersionUID = 6413985862438540761L;
	@XmlElement
	private AppUserModel createdByAppUserModel;
	@XmlElement
	private AppUserModel checkedByAppUserModel;
	@XmlElement
	private UsecaseModel usecaseIdUsecaseModel;

	private SegmentModel segmentIdSegmentModel;
	@XmlElement
	private ActionStatusModel actionStatusIdActionStatusModel;

	@XmlElement
	private Long actionAuthorizationId;
	@XmlElement
	private Date createdOn;
	@XmlElement
	private String comments;
	@XmlElement
	private Date checkedOn;
	@XmlElement
	private String checkerComments;
	@XmlElement
	private Long escalationLevel;
	@XmlElement
	private Date intimatedOn;
	@XmlElement
	private String intimatedTo;
	@XmlElement
	private String referenceData;
	@XmlElement
	private String oldReferenceData;
	@XmlElement
	private String referenceId;

	@XmlElement
	private String transactionCode;
	@XmlElement
	private String custPicCheckerComments;
	@XmlElement
	private String pNicPicCheckerComments;
	@XmlElement
	private String bFormPicCheckerComments;
	@XmlElement
	private String nicFrontPicCheckerComments;
	@XmlElement
	private String nicBackPicCheckerComments;
	@XmlElement
	private String pNicBackPicCheckerComments;

	@Transient
	private String debitCardPan;

	// Constructors

	/** default constructor */
	public ActionAuthorizationModel() {
	}
	
	 /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getActionAuthorizationId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setActionAuthorizationId(primaryKey);
    }
	@Column(name = "ACTION_ATHORIZATION_ID")
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACTION_AUTHORIZATION_SEQ")
	public Long getActionAuthorizationId() {
		return this.actionAuthorizationId;
	}

	public void setActionAuthorizationId(Long actionId) {
		this.actionAuthorizationId = actionId;
	}
		
	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "COMMENTS", length = 250)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "CHECKED_ON")
	public Date getCheckedOn() {
		return this.checkedOn;
	}

	public void setCheckedOn(Date checkedOn) {
		this.checkedOn = checkedOn;
	}

	@Column(name = "CHECKER_COMMENTS",length = 250)
	public String getCheckerComments() {
		return this.checkerComments;
	}

	public void setCheckerComments(String checkerComments) {
		this.checkerComments = checkerComments;
	}

	@Column(name = "ESCALATION_LEVEL")
	public Long getEscalationLevel() {
		return this.escalationLevel;
	}

	public void setEscalationLevel(Long escalationLevel) {
		this.escalationLevel = escalationLevel;
	}
	
	@Column(name = "REFERENCE_DATA")
	public String getReferenceData() {
		return referenceData;
	}

	public void setReferenceData(String referenceData) {
		this.referenceData = referenceData;
	} 
	
	@Column(name = "OLD_REFERENCE_DATA")
	public String getOldReferenceData() {
		return oldReferenceData;
	}

	public void setOldReferenceData(String oldReferenceData) {
		this.oldReferenceData = oldReferenceData;
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
	@Column(name = "REFERENCE_ID")
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	/**
	    * Returns the value of the <code>createdByAppUserModel</code> relation property.
	    *
	    * @return the value of the <code>createdByAppUserModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "CREATED_BY")    
	   public AppUserModel getRelationCreatedByAppUserModel(){
	      return createdByAppUserModel;
	   }
	    
	   /**
	    * Returns the value of the <code>createdByAppUserModel</code> relation property.
	    *
	    * @return the value of the <code>createdByAppUserModel</code> relation property.
	    *
	    */
	   @javax.persistence.Transient
	   public AppUserModel getCreatedByAppUserModel(){
	      return getRelationCreatedByAppUserModel();
	   }

	   /**
	    * Sets the value of the <code>createdByAppUserModel</code> relation property.
	    *
	    * @param appUserModel a value for <code>createdByAppUserModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
	      this.createdByAppUserModel = appUserModel;
	   }
	   
	   /**
	    * Sets the value of the <code>createdByAppUserModel</code> relation property.
	    *
	    * @param appUserModel a value for <code>createdByAppUserModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setCreatedByAppUserModel(AppUserModel appUserModel) {
	      if(null != appUserModel)
	      {
	      	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
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
	    * Returns the value of the <code>UsecaseIdUsecaseModel</code> relation property.
	    *
	    * @return the value of the <code>UsecaseIdUsecaseModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "USECASE_ID")    
	   public UsecaseModel getRelationUsecaseIdUsecaseModel(){
	      return usecaseIdUsecaseModel ;
	   }
	    
	   @javax.persistence.Transient
	   public UsecaseModel getUsecaseIdUsecaseModel(){
	      return getRelationUsecaseIdUsecaseModel();
	   }

	   @javax.persistence.Transient
	   public void setRelationUsecaseIdUsecaseModel(UsecaseModel usecaseModel) {
	      this.usecaseIdUsecaseModel = usecaseModel;
	   }

	//SEGMENTMODEL
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEGMENT_ID")
	public SegmentModel getRelationSegmentIdSegmentModel(){
		return segmentIdSegmentModel ;
	}

	@javax.persistence.Transient
	public SegmentModel getSegmentIdSegmentModel(){
		return getRelationSegmentIdSegmentModel();
	}

	@javax.persistence.Transient
	public void setRelationSegmentIdSegmentModel(SegmentModel segmentModel) {
		this.segmentIdSegmentModel = segmentModel;
	}
	   @javax.persistence.Transient
	   public void setUsecaseIdUsecaseModel(UsecaseModel usecaseModel) {
	      if(null != usecaseModel)
	      {
	    	  setRelationUsecaseIdUsecaseModel((UsecaseModel)usecaseModel.clone());
	      }      
	   }

	//SEGMENTMODEL
	@javax.persistence.Transient
	public void setSegmentIdSegmentModel(SegmentModel segmentModel) {
		if(null != segmentModel)
		{
			setRelationSegmentIdSegmentModel((SegmentModel)segmentModel.clone());
		}
	}
	   /**
	    * Returns the value of the <code>ActionStatusIdActionStatusModel</code> relation property.
	    *
	    * @return the value of the <code>ActionStatusIdActionStatusModel</code> relation property.
	    *
	    */
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
	    * Returns the value of the <code>appUserId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getCreatedById() {
	      if (createdByAppUserModel != null) {
	         return createdByAppUserModel.getAppUserId();
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
	   public void setCreatedById(Long appUserId) {
	      if(appUserId == null)
	      {      
	      	createdByAppUserModel = null;
	      }
	      else
	      {
	        createdByAppUserModel = new AppUserModel();
	      	createdByAppUserModel.setAppUserId(appUserId);
	      }      
	   }
	   @javax.persistence.Transient
	   public String getCreatedByUsername() {
	      if (createdByAppUserModel != null) {
	         return createdByAppUserModel.getUsername();
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
	   public void setCreatedByUsername(String appUserName) {
	      if(appUserName == null)
	      {      
	      	createdByAppUserModel = null;
	      }
	      else
	      {
	        createdByAppUserModel = new AppUserModel();
	      	createdByAppUserModel.setUsername(appUserName);
	      }      
	   }
	   

	   /**
	    * Returns the value of the <code>appUserId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getCheckedById() {
	      if (checkedByAppUserModel != null) {
	         return checkedByAppUserModel.getAppUserId();
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
	   public void setCheckedById(Long appUserId) {
	      if(appUserId == null)
	      {      
	      	checkedByAppUserModel = null;
	      }
	      else
	      {
	        checkedByAppUserModel = new AppUserModel();
	      	checkedByAppUserModel.setAppUserId(appUserId);
	      }      
	   }
	   @javax.persistence.Transient
	   public String getCheckedByUsername() {
	      if (checkedByAppUserModel != null) {
	         return checkedByAppUserModel.getUsername();
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
	   public void setCheckedByUsername(String username) {
	      if(username == null)
	      {      
	      	checkedByAppUserModel = null;
	      }
	      else
	      {
	        checkedByAppUserModel = new AppUserModel();
	      	checkedByAppUserModel.setUsername(username);
	      }      
	   }
	   	
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
	   @javax.persistence.Transient
	   public Long getUsecaseId() {
	      if (usecaseIdUsecaseModel != null) {
	         return usecaseIdUsecaseModel.getUsecaseId();
	      } else {
	         return null;
	      }
	   }

	   
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
	//SEGMENTMODEL

	@javax.persistence.Transient
	public Long getSegmentId() {
		if (segmentIdSegmentModel != null) {
			return segmentIdSegmentModel.getSegmentId();
		} else {
			return null;
		}
	}



	@javax.persistence.Transient
	   public String getUsecaseName() {
	      if (usecaseIdUsecaseModel != null) {
	         return usecaseIdUsecaseModel.getName();
	      } else {
	         return null;
	      }
	   }

	//SEGMENTMODEL

	@javax.persistence.Transient
	public void setSegmentId(Long segmentId) {
		if(segmentId == null)
		{
			segmentIdSegmentModel = null;
		}
		else
		{
			segmentIdSegmentModel = new SegmentModel();
			segmentIdSegmentModel.setSegmentId(segmentId);
		}
	}

	//SegmentModel..
	@javax.persistence.Transient
	public String getSegmentName() {
		if (segmentIdSegmentModel != null) {
			return segmentIdSegmentModel.getName();
		} else {
			return null;
		}
	}
	//SEGMENTMODEL..

	@javax.persistence.Transient
	public void setSegmentName(String name) {
		if(name == null)
		{
			segmentIdSegmentModel = null;
		}
		else
		{
			segmentIdSegmentModel = new SegmentModel();
			segmentIdSegmentModel.setName(name);
		}
	}

	   
	   @javax.persistence.Transient
	   public void setUsecaseName(String name) {
	      if(name == null)
	      {      
	    	  usecaseIdUsecaseModel = null;
	      }
	      else
	      {
	    	  usecaseIdUsecaseModel = new UsecaseModel();
	    	  usecaseIdUsecaseModel.setName(name);
	      }      
	   }
	    /**
	     * Used by the display tag library for rendering a checkbox in the list.
	     * @return String with a HTML checkbox.
	     */
	    @Transient
	    public String getCheckbox() {
	        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
	        checkBox += "_"+ getActionAuthorizationId();
	        checkBox += "\"/>";
	        return checkBox;
	    }

	   /**
	    * Helper method for Struts with displaytag
	    */
	   @javax.persistence.Transient
	   public String getPrimaryKeyParameter() {
	      String parameters = "";
	      parameters += "&actionAuthorizationId=" + getActionAuthorizationId();
	      return parameters;
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
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCheckedByAppUserModel");
		associationModel.setValue(getRelationCheckedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("UsecaseModel");
		associationModel.setPropertyName("relationUsecaseIdUsecaseModel");
		associationModel.setValue(getRelationUsecaseIdUsecaseModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("ActionStatusModel");
		associationModel.setPropertyName("relationActionStatusIdActionStatusModel");
		associationModel.setValue(getRelationActionStatusIdActionStatusModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("SegmentModel");
		associationModel.setPropertyName("relationSegmentIdSegmentModel");
		associationModel.setValue(getRelationSegmentIdSegmentModel());

		associationModelList.add(associationModel);


		return associationModelList;
	}
	/**
	     * Helper method for default Sorting on Primary Keys
	     */
	    @javax.persistence.Transient
	    public String getPrimaryKeyFieldName()
	    {
				String primaryKeyFieldName = "actionAuthorizationId";
				return primaryKeyFieldName;
	    }

	    @Column(name =  "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Transient
	public String getDebitCardPan() {
		return debitCardPan;
	}

	public void setDebitCardPan(String debitCardPan) {
		this.debitCardPan = debitCardPan;
	}

	@Column(name = "CUST_PIC_CHECKER_COMMENTS")
	public String getCustPicCheckerComments() {return custPicCheckerComments;}

	public void setCustPicCheckerComments(String custPicCheckerComments) {this.custPicCheckerComments = custPicCheckerComments;}

	@Column(name = "P_NIC_PIC_CHECKER_COMMENTS")
	public String getpNicPicCheckerComments() {return pNicPicCheckerComments;}

	public void setpNicPicCheckerComments(String pNicPicCheckerComments) {this.pNicPicCheckerComments = pNicPicCheckerComments;}

	@Column(name = "B_FORM_PIC_CHECKER_COMMENTS")
	public String getbFormPicCheckerComments() {return bFormPicCheckerComments;}

	public void setbFormPicCheckerComments(String bFormPicCheckerComments) {this.bFormPicCheckerComments = bFormPicCheckerComments;}

	@Column(name = "NIC_FRONT_PIC_CHECKER_COMMENTS")
	public String getNicFrontPicCheckerComments() {return nicFrontPicCheckerComments;}

	public void setNicFrontPicCheckerComments(String nicFrontPicCheckerComments) {this.nicFrontPicCheckerComments = nicFrontPicCheckerComments;}

	@Column(name = "NIC_BACK_PIC_CHECKER_COMMENTS")
	public String getNicBackPicCheckerComments() {return nicBackPicCheckerComments;}

	public void setNicBackPicCheckerComments(String nicBackPicCheckerComments) {this.nicBackPicCheckerComments = nicBackPicCheckerComments;}

	@Column(name = "P_NIC_BACK_CHECKER_COMMENTS")
	public String getpNicBackPicCheckerComments() {return pNicBackPicCheckerComments;}

	public void setpNicBackPicCheckerComments(String pNicBackPicCheckerComments) {this.pNicBackPicCheckerComments = pNicBackPicCheckerComments;}
}