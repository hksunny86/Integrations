package com.inov8.microbank.common.model.appversionmodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AppVersionListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AppVersionListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "APP_VERSION_LIST_VIEW")
public class AppVersionListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 7243699143651703404L;
	private Long appVersionId;
	private Long deviceTypeId;
   private String appVersionNumber;
   private Date releaseDate;
   private String toCompatibleVersion;
   private String fromCompatibleVersion;
   private Boolean active;
   private Boolean blackListed;
   private Long createdBy;
   private Long updatedBy;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public AppVersionListViewModel() {
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
   @Id 
   public Long getAppVersionId() {
      return appVersionId;
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
    *    
		    */

   public void setDeviceTypeId(Long deviceTypeId) {
      this.deviceTypeId = deviceTypeId;
   }

   
   /**
    * Returns the value of the <code>appVersionId</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_ID" , nullable = false )
      public Long getDeviceTypeId() {
      return deviceTypeId;
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
      @Column(name = "APP_VERSION_NUMBER" , nullable = false , length=10 )
   public String getAppVersionNumber() {
      return appVersionNumber;
   }

   /**
    * Sets the value of the <code>appVersionNumber</code> property.
    *
    * @param appVersionNumber the value for the <code>appVersionNumber</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="10"
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
      @Column(name = "TO_COMPATIBLE_VERSION"  , length=10 )
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
    * @spring.validator-var name="maxlength" value="10"
    */

   public void setToCompatibleVersion(String toCompatibleVersion) {
      this.toCompatibleVersion = toCompatibleVersion;
   }

   /**
    * Returns the value of the <code>fromCompatibleVersion</code> property.
    *
    */
      @Column(name = "FROM_COMPATIBLE_VERSION"  , length=10 )
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
    * @spring.validator-var name="maxlength" value="10"
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
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY" , nullable = false )
   public Long getCreatedBy() {
      return createdBy;
   }

   /**
    * Sets the value of the <code>createdBy</code> property.
    *
    * @param createdBy the value for the <code>createdBy</code> property
    *    
		    */

   public void setCreatedBy(Long createdBy) {
      this.createdBy = createdBy;
   }

   /**
    * Returns the value of the <code>updatedBy</code> property.
    *
    */
      @Column(name = "UPDATED_BY" , nullable = false )
   public Long getUpdatedBy() {
      return updatedBy;
   }

   /**
    * Sets the value of the <code>updatedBy</code> property.
    *
    * @param updatedBy the value for the <code>updatedBy</code> property
    *    
		    */

   public void setUpdatedBy(Long updatedBy) {
      this.updatedBy = updatedBy;
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
}
