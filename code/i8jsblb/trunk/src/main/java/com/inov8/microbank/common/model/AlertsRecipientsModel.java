package com.inov8.microbank.common.model;

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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;


/**
 * @author Rizwan Munir
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ALERTS_RECIPIENTS_SEQ",sequenceName = "ALERTS_RECIPIENTS_SEQ", allocationSize=1)
@Table(name = "ALERTS_RECIPIENTS")
public class AlertsRecipientsModel extends BasePersistableModel {
	
	private static final long serialVersionUID = -842757234275240175L;
	private Long alertsRecipientsId;
    private String name;
    private String mobileNo;
    private String emailId;
    private AlertsConfigModel alertConfigIdAlertsConfigModel;
    private String description;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private Boolean isActive;

    public AlertsRecipientsModel() {
    	
    }
    
    @Column(name = "ALERTS_RECIPIENTS_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALERTS_RECIPIENTS_SEQ")
	public Long getAlertsRecipientsId() {
		return alertsRecipientsId;
	}

	public void setAlertsRecipientsId(Long alertsRecipientsId) {
		this.alertsRecipientsId = alertsRecipientsId;
	}


    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
    	return getAlertsRecipientsId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
    	setAlertsRecipientsId(primaryKey);
    }
    
    @Column(name="NAME")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="MOBILE_NO")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name="EMAIL_ID")
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
    
    /**
     * Returns the value of the <code>alertIdSMSAlertsConfigModel</code> relation property.
     *
     * @return the value of the <code>alertIdSMSAlertsConfigModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ALERTS_CONFIGURATION_ID")    
    public AlertsConfigModel getRelationAlertConfigIdAlertsConfigModel(){
       return alertConfigIdAlertsConfigModel;
    }
     
    /**
     * Returns the value of the <code>alertConfigIdAlertsConfigModel</code> relation property.
     *
     * @return the value of the <code>alertConfigIdAlertsConfigModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AlertsConfigModel getAlertConfigIdAlertsConfigModel(){
       return getRelationAlertConfigIdAlertsConfigModel();
    }

    /**
     * Sets the value of the <code>alertConfigIdAlertsConfigModel</code> relation property.
     *
     * @param appUserModel a value for <code>alertConfigIdAlertsConfigModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationAlertConfigIdAlertsConfigModel(AlertsConfigModel alertConfigIdAlertsConfigModel) {
       this.alertConfigIdAlertsConfigModel = alertConfigIdAlertsConfigModel;
    }
    
    /**
     * Sets the value of the <code>alertConfigIdAlertsConfigModel</code> relation property.
     *
     * @param appUserModel a value for <code>alertConfigIdAlertsConfigModel</code>.
     */
    @javax.persistence.Transient
    public void setAlertConfigIdAlertsConfigModel(AlertsConfigModel alertConfigIdAlertsConfigModel) {
       if(null != alertConfigIdAlertsConfigModel)
       {
    	   setRelationAlertConfigIdAlertsConfigModel((AlertsConfigModel)alertConfigIdAlertsConfigModel.clone());
       }      
    }
	
    
    @javax.persistence.Transient
    public Long getAlertConfigId() {
       if (alertConfigIdAlertsConfigModel != null) {
          return alertConfigIdAlertsConfigModel.getAlertsConfigId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setAlertConfigId(Long alertConfigId) {
       if(alertConfigId == null)
       {      
    	   alertConfigIdAlertsConfigModel = null;
       }
       else
       {
    	   alertConfigIdAlertsConfigModel = new AlertsConfigModel();
    	   alertConfigIdAlertsConfigModel.setAlertsConfigId(alertConfigId);
       }      
    }
    
    @Column(name = "DESCRIPTION")   
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
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

    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (createdByAppUserModel != null) {
          return createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
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
    
    @Version
    @Column(name="VERSION_NO")
    public Integer getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
    
    @Column(name="IS_ACTIVE")
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
       String parameters = "";
       parameters += "&alertsRecipientsId=" + getAlertsRecipientsId();
       return parameters;
    }
    
 	/**
      * Helper method for default Sorting on Primary Keys
      */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName(){ 
    	String primaryKeyFieldName = "alertsRecipientsId";
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
    	AssociationModel associationModel = new AssociationModel();
   
    	associationModel = new AssociationModel();
    	associationModel.setClassName("AlertsConfigModel");
    	associationModel.setPropertyName("relationAlertConfigIdAlertsConfigModel");   		
   		associationModel.setValue(getRelationAlertConfigIdAlertsConfigModel());
   		associationModelList.add(associationModel);
    	
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

}