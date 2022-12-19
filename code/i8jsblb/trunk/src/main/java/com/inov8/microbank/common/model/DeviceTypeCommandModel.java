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

/**
 * The DeviceTypeCommandModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DeviceTypeCommandModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DEVICE_TYPE_COMMAND_seq",sequenceName = "DEVICE_TYPE_COMMAND_seq", allocationSize=1)
@Table(name = "DEVICE_TYPE_COMMAND")
public class DeviceTypeCommandModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -5365109055317211469L;
private DeviceTypeModel deviceTypeIdDeviceTypeModel;
   private CommandModel commandIdCommandModel;
   private AppUserModel createdByAppUserModel;
   private AppUserModel updatedByAppUserModel;


   private Long deviceTypeCommandId;
   private Boolean loginRequired;
   private Integer versionNo;
   private Date updatedOn;
   private Date createdOn;

   /**
    * Default constructor.
    */
   public DeviceTypeCommandModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDeviceTypeCommandId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDeviceTypeCommandId(primaryKey);
    }

   /**
    * Returns the value of the <code>deviceTypeCommandId</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_COMMAND_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEVICE_TYPE_COMMAND_seq")
   public Long getDeviceTypeCommandId() {
      return deviceTypeCommandId;
   }

   /**
    * Sets the value of the <code>deviceTypeCommandId</code> property.
    *
    * @param deviceTypeCommandId the value for the <code>deviceTypeCommandId</code> property
    *    
		    */

   public void setDeviceTypeCommandId(Long deviceTypeCommandId) {
      this.deviceTypeCommandId = deviceTypeCommandId;
   }

   /**
    * Returns the value of the <code>loginRequired</code> property.
    *
    */
      @Column(name = "IS_LOGIN_REQUIRED" , nullable = false )
   public Boolean getLoginRequired() {
      return loginRequired;
   }

   /**
    * Sets the value of the <code>loginRequired</code> property.
    *
    * @param loginRequired the value for the <code>loginRequired</code> property
    *    
		    */

   public void setLoginRequired(Boolean loginRequired) {
      this.loginRequired = loginRequired;
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
							    * @spring.validator type="required"
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getDeviceTypeCommandId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&deviceTypeCommandId=" + getDeviceTypeCommandId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "deviceTypeCommandId";
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
    	
    	associationModel.setClassName("CommandModel");
    	associationModel.setPropertyName("relationCommandIdCommandModel");   		
   		associationModel.setValue(getRelationCommandIdCommandModel());
   		
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
