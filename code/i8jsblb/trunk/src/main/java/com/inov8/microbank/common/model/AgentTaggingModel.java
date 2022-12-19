package com.inov8.microbank.common.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@javax.persistence.SequenceGenerator(name = "AGENT_GROUP_TAGGING_SEQ", sequenceName = "AGENT_GROUP_TAGGING_SEQ", allocationSize=1)
@Table(name = "AGENT_GROUP_TAGGING")
public class AgentTaggingModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -4949320924559852968L;

	private Long agentTaggingId;
	private String groupTitle;
	private String parrentId;
	private Boolean status;
	private Long appUserId;
//	private String agentName;
//	private String businessName;
//	private String mobileNumber;
//	private String cnic;

	private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
	private Integer versionNo;


	public AgentTaggingModel() {}

	public AgentTaggingModel(Long agentTaggingId) {
		this.agentTaggingId = agentTaggingId;
	}
	
	public AgentTaggingModel(String parrentId) {
		this.parrentId = parrentId;
	}

	@Column(name = "GROUP_TITLE")
	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	@Column(name = "PARENT_ID")
	public String getParrentId() {
		return parrentId;
	}

	public void setParrentId(String parrentId) {
		this.parrentId = parrentId;
	}
	
	/*
	@Column(name = "AGENT_NAME")
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Column(name = "BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	@Column(name = "MOBILE_NUMBER")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Column(name = "CNIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	*/

	@Column(name = "STATUS")
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getAgentTaggingId();
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setAgentTaggingId(primaryKey);
	}

	@Column(name = "AGENT_GROUP_TAGGING_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENT_GROUP_TAGGING_SEQ")
	public Long getAgentTaggingId() {
		return agentTaggingId;
	}

	public void setAgentTaggingId(Long agentTaggingId) {
		this.agentTaggingId = agentTaggingId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return createdByAppUserModel;
	}
	
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")    
    public AppUserModel getRelationUpdatedByAppUserModel(){
       return updatedByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
       return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
       this.updatedByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }
	
    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	updatedByAppUserModel = null;
       }
       else
       {
         updatedByAppUserModel = new AppUserModel();
       	updatedByAppUserModel.setAppUserId(appUserId);
       }      
    }
	
    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name="APP_USER_ID")
	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return getRelationCreatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationCreatedByAppUserModel((AppUserModel) appUserModel.clone());
		}
	}

	/**
	 * Returns the value of the <code>appUserId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getCreatedBy() {
		if (createdByAppUserModel != null) {
			return createdByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>appUserId</code> property.
	 * 
	 * @param appUserId
	 *            the value for the <code>appUserId</code> property
	 */

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			createdByAppUserModel = null;
		} else {
			createdByAppUserModel = new AppUserModel();
			createdByAppUserModel.setAppUserId(appUserId);
		}
	}
	
	

	/**
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&agentTaggingId=" + getAgentTaggingId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "agentTaggingId";
		return primaryKeyFieldName;
	}

	/**
	 * Helper method for Complex Example Queries
	 */
	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(getRelationUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}


	@Version 
	@Column(name = "VERSION_NO" , nullable = false )
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
}
