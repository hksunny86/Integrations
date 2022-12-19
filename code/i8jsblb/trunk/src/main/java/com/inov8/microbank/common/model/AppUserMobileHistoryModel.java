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
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.mobilenetworks.model.MobileNetworkModel;

/**
 * The AppUserMobileHistoryModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AppUserMobileHistoryModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_USER_MOBILE_HISTORY_seq",sequenceName = "APP_USER_MOBILE_HISTORY_seq", allocationSize=1)
@Table(name = "APP_USER_MOBILE_HISTORY")
public class AppUserMobileHistoryModel extends BasePersistableModel implements Serializable{
  
	private static final long serialVersionUID = -6005544744586351376L;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel appUserIdAppUserModel;
	private MobileNetworkModel mobileNetworkIdmobileNetworkModel;

	private Long appUserMobileHistoryId;
	private String mobileNo;
	private	String	nic;
	private Date fromDate;
	private Date toDate;
	private String description;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

	private Long	usecaseId;
   /**
    * Default constructor.
    */
   public AppUserMobileHistoryModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAppUserMobileHistoryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAppUserMobileHistoryId(primaryKey);
    }

   /**
    * Returns the value of the <code>appUserMobileHistoryId</code> property.
    *
    */
   @Column(name = "APP_USER_MOBILE_HISTORY_ID" , nullable = false )
   @Id 
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_USER_MOBILE_HISTORY_seq")
   public Long getAppUserMobileHistoryId() {
      return appUserMobileHistoryId;
   }

   /**
    * Sets the value of the <code>appUserMobileHistoryId</code> property.
    *
    * @param appUserMobileHistoryId the value for the <code>appUserMobileHistoryId</code> property
    *    
	*/

   public void setAppUserMobileHistoryId(Long appUserMobileHistoryId) {
      this.appUserMobileHistoryId = appUserMobileHistoryId;
   }

   /**
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
   @Column(name = "MOBILE_NO" )
   public String getMobileNo() {
      return mobileNo;
   }

   /**
    * Sets the value of the <code>mobileNo</code> property.
    *
    * @param mobileNo the value for the <code>mobileNo</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>fromDate</code> property.
    *
    */
   @Column(name = "FROM_DATE" , nullable = false )
   public Date getFromDate() {
      return fromDate;
   }

   /**
    * Sets the value of the <code>fromDate</code> property.
    *
    * @param fromDate the value for the <code>fromDate</code> property
    *    
    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setFromDate(Date fromDate) {
      this.fromDate = fromDate;
   }

   /**
    * Returns the value of the <code>toDate</code> property.
    *
    */
   @Column(name = "TO_DATE"  )
   public Date getToDate() {
      return toDate;
   }

   /**
    * Sets the value of the <code>toDate</code> property.
    *
    * @param toDate the value for the <code>toDate</code> property
    *    
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setToDate(Date toDate) {
      this.toDate = toDate;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
   @Column(name = "DESCRIPTION"  , length=250 )
   public String getDescription() {
      return description;
   }

   /**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
	* @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription(String description) {
      this.description = description;
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
   
   @Column(name = "NIC")
	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
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
        checkBox += "_"+ getAppUserMobileHistoryId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&appUserMobileHistoryId=" + getAppUserMobileHistoryId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
    	String primaryKeyFieldName = "appUserMobileHistoryId";
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
			    	
    	return associationModelList;
    }

    @Transient
	public Long getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "MOBILE_NETWORK_ID")
    public MobileNetworkModel getRelationMobileNetworIdMobileNetworkModel(){
        return mobileNetworkIdmobileNetworkModel;
    }

    @javax.persistence.Transient
    public MobileNetworkModel getMobileNetworkModel(){
        return getRelationMobileNetworIdMobileNetworkModel();
    }

    @javax.persistence.Transient
    public void setRelationMobileNetworIdMobileNetworkModel(MobileNetworkModel mobileNetworkModel) {
        this.mobileNetworkIdmobileNetworkModel = mobileNetworkModel;
    }

    @javax.persistence.Transient
    public void setMobileNetworkModel(MobileNetworkModel mobileNetworkModel) {
        if(null != mobileNetworkModel)
        {
            setRelationMobileNetworIdMobileNetworkModel((MobileNetworkModel)mobileNetworkModel.clone());
        }
    }

    @javax.persistence.Transient
    public void setMobileNetworkId(Long mobileNetworkId) {
        if(mobileNetworkId == null)
        {
            mobileNetworkIdmobileNetworkModel = null;
        }
        else
        {
            mobileNetworkIdmobileNetworkModel = new MobileNetworkModel();
            mobileNetworkIdmobileNetworkModel.setMobileNetworkId(mobileNetworkId);
        }
    }

    @javax.persistence.Transient
    public Long getMobileNetworkId()
    {
        Long mobileNetworkId = null;
        if(mobileNetworkIdmobileNetworkModel != null)
            mobileNetworkId = mobileNetworkIdmobileNetworkModel.getMobileNetworkId();
        return mobileNetworkId;
    }

}
