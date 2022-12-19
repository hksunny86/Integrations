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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.app.model.AppModel;

/**
 * The AppVersionModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AppVersionModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_VERSION_seq",sequenceName = "APP_VERSION_seq", allocationSize=1)
@Table(name = "APP_VERSION")
public class AppVersionModel extends BasePersistableModel implements Serializable {
  

   private DeviceTypeModel deviceTypeIdDeviceTypeModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;

   private AppModel appModel;
   private Long appVersionId;
   private String appVersionNumber;
   private Date releaseDate;
   private String toCompatibleVersion;
   private String fromCompatibleVersion;
   private Boolean active;
   private Boolean blackListed;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public AppVersionModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAppVersionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAppVersionId(primaryKey);
    }

   /**
    * Returns the value of the <code>appVersionId</code> property.
    *
    */
      @Column(name = "APP_VERSION_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_VERSION_seq")
   public Long getAppVersionId() {
      return appVersionId;
   }

   /**
    * Sets the value of the <code>appVersionId</code> property.
    *
    * @param appVersionId the value for the <code>appVersionId</code> property
    *    
		    */

   public void setAppVersionId(Long appVersionId) {
      this.appVersionId = appVersionId;
   }

   /**
    * Returns the value of the <code>appVersionNumber</code> property.
    *
    */
      @Column(name = "APP_VERSION_NUMBER" , nullable = false , length=50 )
   public String getAppVersionNumber() {
      return appVersionNumber;
   }

   /**
    * Sets the value of the <code>appVersionNumber</code> property.
    *
    * @param appVersionNumber the value for the <code>appVersionNumber</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAppVersionNumber(String appVersionNumber) {
      this.appVersionNumber = appVersionNumber;
   }

   /**
    * Returns the value of the <code>releaseDate</code> property.
    *
    */
      @Column(name = "RELEASE_DATE" , nullable = false )
   public Date getReleaseDate() {
      return releaseDate;
   }

   /**
    * Sets the value of the <code>releaseDate</code> property.
    *
    * @param releaseDate the value for the <code>releaseDate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setReleaseDate(Date releaseDate) {
      this.releaseDate = releaseDate;
   }

   /**
    * Returns the value of the <code>toCompatibleVersion</code> property.
    *
    */
      @Column(name = "TO_COMPATIBLE_VERSION"  , length=50 )
   public String getToCompatibleVersion() {
      return toCompatibleVersion;
   }

   /**
    * Sets the value of the <code>toCompatibleVersion</code> property.
    *
    * @param toCompatibleVersion the value for the <code>toCompatibleVersion</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setToCompatibleVersion(String toCompatibleVersion) {
      this.toCompatibleVersion = toCompatibleVersion;
   }

   /**
    * Returns the value of the <code>fromCompatibleVersion</code> property.
    *
    */
      @Column(name = "FROM_COMPATIBLE_VERSION"  , length=50 )
   public String getFromCompatibleVersion() {
      return fromCompatibleVersion;
   }

   /**
    * Sets the value of the <code>fromCompatibleVersion</code> property.
    *
    * @param fromCompatibleVersion the value for the <code>fromCompatibleVersion</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFromCompatibleVersion(String fromCompatibleVersion) {
      this.fromCompatibleVersion = fromCompatibleVersion;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>blackListed</code> property.
    *
    */
      @Column(name = "IS_BLACK_LISTED" , nullable = false )
   public Boolean getBlackListed() {
      return blackListed;
   }

   /**
    * Sets the value of the <code>blackListed</code> property.
    *
    * @param blackListed the value for the <code>blackListed</code> property
    *    
		    */

   public void setBlackListed(Boolean blackListed) {
      this.blackListed = blackListed;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   /**
    * Returns the value of the <code>updatedOn</code> property.
    *
    */
      @Column(name = "UPDATED_ON" , nullable = false )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   /**
    * Sets the value of the <code>updatedOn</code> property.
    *
    * @param updatedOn the value for the <code>updatedOn</code> property
    *    
		    */

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
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
																											    * @spring.validator type="required"
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
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getUpdatedBy() {
      if (updatedByAppUserModel != null) {
         return updatedByAppUserModel.getAppUserId();
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
    * @param appUserId the value for the <code>appUserId</code> property
																											    */
   
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

   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_ID")
   public AppModel getAppModel() {
      return appModel;
   }

   public void setAppModel(AppModel appModel) {
      this.appModel = appModel;
   }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAppVersionId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&appVersionId=" + getAppVersionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "appVersionId";
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
    	
    	associationModel.setClassName("DeviceTypeModel");
    	associationModel.setPropertyName("relationDeviceTypeIdDeviceTypeModel");   		
   		associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
