package com.inov8.microbank.common.model;

import java.io.Serializable;
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
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The UserDeviceAccountsModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UserDeviceAccountsModel"
 */
@XmlRootElement(name="userDeviceAccountsModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "USER_DEVICE_ACCOUNTS_seq",sequenceName = "USER_DEVICE_ACCOUNTS_seq", allocationSize=1)
@Table(name = "USER_DEVICE_ACCOUNTS")
public class UserDeviceAccountsModel extends BasePersistableModel implements Serializable {
  

   private DeviceTypeModel deviceTypeIdDeviceTypeModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;
   private AppUserModel appUserIdAppUserModel;
   private ProductCatalogModel prodCatalogModel;

   private Collection<ActionLogModel> userDeviceAccountsIdActionLogModelList = new ArrayList<ActionLogModel>();

   private Long userDeviceAccountsId;
   @XmlElement
   private String userId;
   @XmlElement
   private String pin;
   @XmlElement
   private Boolean accountExpired;
   @XmlElement
   private Boolean accountEnabled;
   @XmlElement
   private Boolean accountLocked;
   @XmlElement
   private Boolean credentialsExpired;
   @XmlElement
   private Date createdOn;
   @XmlElement
   private Date updatedOn;
   @XmlElement
   private Integer versionNo;
   @XmlElement
   private Boolean pinChangeRequired;
   private java.sql.Timestamp lastLoginAttemptTime;
   @XmlElement
   private Integer loginAttemptCount;
   @XmlElement
   private Date expiryDate;
   @XmlElement
   private Boolean commissioned;
   @XmlElement
   private String password;

   @XmlElement
   private String comments;
   @XmlElement
   private Boolean passwordChangeRequired;



   /**
    * Default constructor.
    */
   public UserDeviceAccountsModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUserDeviceAccountsId();
    }


   @Column(name = "Comments")
   public String getComments() {
      return comments;
   }

   public void setComments(String comments) {
      this.comments = comments;
   }







   /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUserDeviceAccountsId(primaryKey);
    }

   /**
    * Returns the value of the <code>userDeviceAccountsId</code> property.
    *
    */
      @Column(name = "USER_DEVICE_ACCOUNTS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_DEVICE_ACCOUNTS_seq")
   public Long getUserDeviceAccountsId() {
      return userDeviceAccountsId;
   }

   /**
    * Sets the value of the <code>userDeviceAccountsId</code> property.
    *
    * @param userDeviceAccountsId the value for the <code>userDeviceAccountsId</code> property
    *    
		    */

   public void setUserDeviceAccountsId(Long userDeviceAccountsId) {
      this.userDeviceAccountsId = userDeviceAccountsId;
   }

   /**
    * Returns the value of the <code>userId</code> property.
    *
    */
      @Column(name = "USER_ID"  , length=50 )
   public String getUserId() {
      return userId;
   }

   /**
    * Sets the value of the <code>userId</code> property.
    *
    * @param userId the value for the <code>userId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Returns the value of the <code>pin</code> property.
    *
    */
      @Column(name = "PIN"  , length=250 )
   public String getPin() {
      return pin;
   }

   /**
    * Sets the value of the <code>pin</code> property.
    *
    * @param pin the value for the <code>pin</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setPin(String pin) {
      this.pin = pin;
   }

   /**
    * Returns the value of the <code>accountExpired</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_EXPIRED" , nullable = false )
   public Boolean getAccountExpired() {
      return accountExpired;
   }

   /**
    * Sets the value of the <code>accountExpired</code> property.
    *
    * @param accountExpired the value for the <code>accountExpired</code> property
    *    
		    */

   public void setAccountExpired(Boolean accountExpired) {
      this.accountExpired = accountExpired;
   }

   /**
    * Returns the value of the <code>accountEnabled</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_ENABLED" , nullable = false )
   public Boolean getAccountEnabled() {
      return accountEnabled;
   }

   /**
    * Sets the value of the <code>accountEnabled</code> property.
    *
    * @param accountEnabled the value for the <code>accountEnabled</code> property
    *    
		    */

   public void setAccountEnabled(Boolean accountEnabled) {
      this.accountEnabled = accountEnabled;
   }

   /**
    * Returns the value of the <code>accountLocked</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_LOCKED" , nullable = false )
   public Boolean getAccountLocked() {
      return accountLocked;
   }

   /**
    * Sets the value of the <code>accountLocked</code> property.
    *
    * @param accountLocked the value for the <code>accountLocked</code> property
    *    
		    */

   public void setAccountLocked(Boolean accountLocked) {
      this.accountLocked = accountLocked;
   }

   /**
    * Returns the value of the <code>credentialsExpired</code> property.
    *
    */
      @Column(name = "IS_CREDENTIALS_EXPIRED" , nullable = false )
   public Boolean getCredentialsExpired() {
      return credentialsExpired;
   }

   /**
    * Sets the value of the <code>credentialsExpired</code> property.
    *
    * @param credentialsExpired the value for the <code>credentialsExpired</code> property
    *    
		    */

   public void setCredentialsExpired(Boolean credentialsExpired) {
      this.credentialsExpired = credentialsExpired;
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
    * Returns the value of the <code>pinChangeRequired</code> property.
    *
    */
      @Column(name = "IS_PIN_CHANGE_REQUIRED" , nullable = false )
   public Boolean getPinChangeRequired() {
      return pinChangeRequired;
   }

   /**
    * Sets the value of the <code>pinChangeRequired</code> property.
    *
    * @param pinChangeRequired the value for the <code>pinChangeRequired</code> property
    *    
		    */

   public void setPinChangeRequired(Boolean pinChangeRequired) {
      this.pinChangeRequired = pinChangeRequired;
   }

   /**
    * Returns the value of the <code>lastLoginAttemptTime</code> property.
    *
    */
      @Column(name = "LAST_LOGIN_ATTEMPT_TIME"  )
   public java.sql.Timestamp getLastLoginAttemptTime() {
      return lastLoginAttemptTime;
   }

   /**
    * Sets the value of the <code>lastLoginAttemptTime</code> property.
    *
    * @param lastLoginAttemptTime the value for the <code>lastLoginAttemptTime</code> property
    *    
		    */

   public void setLastLoginAttemptTime(java.sql.Timestamp lastLoginAttemptTime) {
      this.lastLoginAttemptTime = lastLoginAttemptTime;
   }

   /**
    * Returns the value of the <code>loginAttemptCount</code> property.
    *
    */
      @Column(name = "LOGIN_ATTEMPT_COUNT"  )
   public Integer getLoginAttemptCount() {
      return loginAttemptCount;
   }

   /**
    * Sets the value of the <code>loginAttemptCount</code> property.
    *
    * @param loginAttemptCount the value for the <code>loginAttemptCount</code> property
    *    
		    * @spring.validator type="integer"
    */

   public void setLoginAttemptCount(Integer loginAttemptCount) {
      this.loginAttemptCount = loginAttemptCount;
   }

   /**
    * Returns the value of the <code>expiryDate</code> property.
    *
    */
      @Column(name = "EXPIRY_DATE"  )
   public Date getExpiryDate() {
      return expiryDate;
   }

   /**
    * Sets the value of the <code>expiryDate</code> property.
    *
    * @param expiryDate the value for the <code>expiryDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setExpiryDate(Date expiryDate) {
      this.expiryDate = expiryDate;
   }

   /**
    * Returns the value of the <code>commissioned</code> property.
    *
    */
      @Column(name = "IS_COMMISSIONED" , nullable = false )
   public Boolean getCommissioned() {
      return commissioned;
   }

   /**
    * Sets the value of the <code>commissioned</code> property.
    *
    * @param commissioned the value for the <code>commissioned</code> property
    *    
		    */

   public void setCommissioned(Boolean commissioned) {
      this.commissioned = commissioned;
   }

   /**
    * Returns the value of the <code>password</code> property.
    *
    */
      @Column(name = "PASSWORD"  , length=250 )
   public String getPassword() {
      return password;
   }

   /**
    * Sets the value of the <code>password</code> property.
    *
    * @param password the value for the <code>password</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * Returns the value of the <code>passwordChangeRequired</code> property.
    *
    */
      @Column(name = "IS_PASSWORD_CHANGE_REQUIRED"  ,nullable = false)
   public Boolean getPasswordChangeRequired() {
      return passwordChangeRequired;
   }

   /**
    * Sets the value of the <code>passwordChangeRequired</code> property.
    *
    * @param passwordChangeRequired the value for the <code>passwordChangeRequired</code> property
    *    
		    */

   public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
      this.passwordChangeRequired = passwordChangeRequired;
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
    * Add the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be added.
    */
    
   public void addUserDeviceAccountsIdActionLogModel(ActionLogModel actionLogModel) {
      actionLogModel.setRelationUserDeviceAccountsIdUserDeviceAccountsModel(this);
      userDeviceAccountsIdActionLogModelList.add(actionLogModel);
   }
   
   /**
    * Remove the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be removed.
    */
   
   public void removeUserDeviceAccountsIdActionLogModel(ActionLogModel actionLogModel) {      
      actionLogModel.setRelationUserDeviceAccountsIdUserDeviceAccountsModel(null);
      userDeviceAccountsIdActionLogModelList.remove(actionLogModel);      
   }

   /**
    * Get a list of related ActionLogModel objects of the UserDeviceAccountsModel object.
    * These objects are in a bidirectional one-to-many relation by the UserDeviceAccountsId member.
    *
    * @return Collection of ActionLogModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUserDeviceAccountsIdUserDeviceAccountsModel")
   @JoinColumn(name = "USER_DEVICE_ACCOUNTS_ID")
   public Collection<ActionLogModel> getUserDeviceAccountsIdActionLogModelList() throws Exception {
   		return userDeviceAccountsIdActionLogModelList;
   }


   /**
    * Set a list of ActionLogModel related objects to the UserDeviceAccountsModel object.
    * These objects are in a bidirectional one-to-many relation by the UserDeviceAccountsId member.
    *
    * @param actionLogModelList the list of related objects.
    */
    public void setUserDeviceAccountsIdActionLogModelList(Collection<ActionLogModel> actionLogModelList) throws Exception {
		this.userDeviceAccountsIdActionLogModelList = actionLogModelList;
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
							    * @spring.validator type="required"
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUserDeviceAccountsId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&userDeviceAccountsId=" + getUserDeviceAccountsId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "userDeviceAccountsId";
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
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationAppUserIdAppUserModel");   		
   		associationModel.setValue(getRelationAppUserIdAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		//by atif hussain
		associationModel = new AssociationModel();
   		associationModel.setClassName("ProductCatalogModel");
   		associationModel.setPropertyName("prodCatalogModel");
		associationModel.setValue(getProdCatalogModel());
		associationModelList.add(associationModel);
			    	
    	return associationModelList;
    }    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATALOG_ID")
    public ProductCatalogModel getProdCatalogModel() {
		return prodCatalogModel;
	}

	public void setProdCatalogModel(ProductCatalogModel prodCatalogModel) {
		this.prodCatalogModel = prodCatalogModel;
	}	    
    
	@javax.persistence.Transient
	public Long getProdCatalogId() 
	{
		if (prodCatalogModel != null) {
	          return prodCatalogModel.getProductCatalogId();
	       } else {
	          return null;
	       }
    }

	@javax.persistence.Transient
	public void setProdCatalogId(Long prodCatalogId) {
		if(prodCatalogId == null)
		{
			this.prodCatalogModel = null;
		}
		else
		{
			prodCatalogModel = new ProductCatalogModel();
			prodCatalogModel.setProductCatalogId(prodCatalogId);
		}
    }          
}
