package com.inov8.microbank.common.model;

import java.util.Date;

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

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * ComplaintCategoryModel entity.
 * @author Muhammad Omar Butt
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMPLAINT_CATEGORY_SEQ",sequenceName = "COMPLAINT_CATEGORY_SEQ", allocationSize=1)
@Table(name = "COMPLAINT_CATEGORY")
public class ComplaintCategoryModel  extends BasePersistableModel{
	 
	 private static final long serialVersionUID = -2781139067346125123L;
	 private Long complaintCategoryId;
     private AppUserModel createdByAppUserModel;
     private AppUserModel updatedByAppUserModel;
     private String name;
     private String description;
     private Boolean isActive;
     private Boolean isAuto;
     private Date createdOn;
     private Date updatedOn;
     private Integer versionNo;


    public ComplaintCategoryModel() {
    }

    @Column(name = "COMPLAINT_CATEGORY_ID")
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPLAINT_CATEGORY_SEQ")
    public Long getComplaintCategoryId() {
        return this.complaintCategoryId;
    }
    
    public void setComplaintCategoryId(Long complaintCategoryId) {
        this.complaintCategoryId = complaintCategoryId;
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

    
    @Column(name="NAME")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="DESCRIPTION", length=100)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="IS_ACTIVE")
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Column(name="CREATED_ON", length=7)
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

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getComplaintCategoryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setComplaintCategoryId(primaryKey);
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&complaintCategoryId=" + getComplaintCategoryId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName(){ 
			String primaryKeyFieldName = "complaintCategoryId";
			return primaryKeyFieldName;				
    }

    @Column(name="IS_AUTO")
    public Boolean getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(Boolean isAuto) {
		this.isAuto = isAuto;
	}

}