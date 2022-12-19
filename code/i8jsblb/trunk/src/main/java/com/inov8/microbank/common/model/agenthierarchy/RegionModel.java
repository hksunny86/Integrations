package com.inov8.microbank.common.model.agenthierarchy;

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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;


/**
 * The RegionModel entity bean.
 *
 * @author  Rashid Mahmood  Inov8 Limited
 * @version 1.0
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "REGION_SEQ",sequenceName = "REGION_SEQ", allocationSize=1)
@Table(name = "REGION")
public class RegionModel extends BasePersistableModel implements Serializable {
  
	
	private static final long serialVersionUID = -3571110109965363149L;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;

	private Long regionId;
	private RegionalHierarchyModel regionalHierarchyModel;
	private String regionName;
   	private Boolean active;
   	private String description;
   	private String comments;
   	private Date createdOn;
   	private Date updatedOn;
   	private Integer versionNo;
   	private Integer index;
   	boolean deleted;
	boolean activeDisabled;
	boolean editMode;
	boolean dirty;
	
	
   	
   	/**
    * Default constructor.
    */
   	public RegionModel() 
   	{
   	}   
   	
   	
   	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn( name = "REGIONAL_HIERARCHY_ID" )
   	public RegionalHierarchyModel getRegionalHierarchyModel() {
		return regionalHierarchyModel;
	}

	public void setRegionalHierarchyModel(
			RegionalHierarchyModel regionalHierarchyModel) {
		this.regionalHierarchyModel = regionalHierarchyModel;
	}
   	
   	
    /**
     * Returns the value of the <code>distributorId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getRegionalHierarchyId() {
       if (regionalHierarchyModel != null) {
          return regionalHierarchyModel.getRegionalHierarchyId();
       } else {
          return null;
       }
    }

	/**
     * Sets the value of the <code>distributorId</code> property.
     *
     * @param distributorId the value for the <code>distributorId</code> property
     * 
    */
     
    @javax.persistence.Transient
	public void setRegionalHierarchyId(Long regionalHierarchyId) 
	{
		if(regionalHierarchyId == null)
		{      
			this.regionalHierarchyModel = null;
		}
		else
		{
			regionalHierarchyModel = new RegionalHierarchyModel();
			regionalHierarchyModel.setRegionalHierarchyId(regionalHierarchyId);
		}      
   }
    
    
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getRegionId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setRegionId(primaryKey);
    }

   /**
    * Returns the value of the <code>regionId</code> property.
    *
    */
   @Column(name = "REGION_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REGION_SEQ")
   public Long getRegionId() {
      return regionId;
   }

   /**
    * Sets the value of the <code>regionId</code> property.
    *
    * @param regionId the value for the <code>regionId</code> property
    *    
		    */

   public void setRegionId(Long regionId) {
      this.regionId = regionId;
   }

   
   /**
    * Returns the value of the <code>regionName</code> property.
    *
    */
   @Column(name = "REGION_NAME" , nullable = false , length=50 )
   public String getRegionName() {
      return regionName;
   }

   /**
    * Sets the value of the <code>regionName</code> property.
    *
    * @param regionName the value for the <code>regionName</code> property
    *    
    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var regionName="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setRegionName(String regionName) {
	   this.regionName = regionName;
   }
   
   
   /**
    * Returns the value of the <code>active</code> property.
    *
    */
   @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() 
   {
	   return active;
   }

   
   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   @javax.persistence.Transient
	public void setActive(Boolean active) {
		this.active = active;
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
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS"  , length=250 )
   public String getComments() {
      return comments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments(String comments) {
      this.comments = comments;
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



   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&regionId=" + getRegionId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "regionId";
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
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
   		associationModel.setValue(getRelationUpdatedByAppUserModel());
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	associationModel.setClassName("RegionalHierarchyModel");
    	associationModel.setPropertyName("regionalHierarchyModel");   		
   		associationModel.setValue(getRegionalHierarchyModel());
   		associationModelList.add(associationModel);
			    	
    	return associationModelList;
    }

    @javax.persistence.Transient
	public boolean isDeleted() {
		return deleted;
	}

	@javax.persistence.Transient
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@javax.persistence.Transient
	public boolean isEditMode() {
		return editMode;
	}

	@javax.persistence.Transient
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	@javax.persistence.Transient
	public boolean isDirty() {
		return dirty;
	}

	@javax.persistence.Transient
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@javax.persistence.Transient
	public boolean isActiveDisabled() {
		return activeDisabled;
	}

	@javax.persistence.Transient
	public void setActiveDisabled(boolean activeDisabled) {
		this.activeDisabled = activeDisabled;
	}

	@javax.persistence.Transient
	public Integer getIndex() {
		return index;
	}

	@javax.persistence.Transient
	public void setIndex(Integer index) {
		this.index = index;
	}

	    
    
}
