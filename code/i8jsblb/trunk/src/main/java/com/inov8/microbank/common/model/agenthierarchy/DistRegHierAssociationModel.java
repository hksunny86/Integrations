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
import com.inov8.microbank.common.model.DistributorModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DIST_REG_HIER_ASSOCIATION_SEQ",sequenceName = "DIST_REG_HIER_ASSOCIATION_SEQ", allocationSize=1)
@Table(name = "DIST_REG_HIER_ASSOCIATION")
public class DistRegHierAssociationModel extends BasePersistableModel implements Serializable 
{
	private static final long serialVersionUID = -3571110109965363149L;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private RegionalHierarchyModel regionalHierarchyModel;
	private DistributorModel distributor;
	
	private Long distRegHierId;
   	private String description;
   	private String comments;
   	private Date createdOn;
   	private Date updatedOn;
   	private Integer versionNo;
   	
   	private Integer index;
   	private boolean editMode;
   	
   	/**
    * Default constructor.
    */
   	public DistRegHierAssociationModel() {
		// TODO Auto-generated constructor stub
	}   
   	    
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistRegHierId();
        
        
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistRegHierId(primaryKey);
    }

   /**
    * Returns the value of the <code>regionId</code> property.
    *
    */
   @Column(name = "DIST_REG_HIER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIST_REG_HIER_ASSOCIATION_SEQ")
   public Long getDistRegHierId() 
   {
      return this.distRegHierId;
   }

   public void setDistRegHierId(Long distRegHierId) {
      this.distRegHierId = distRegHierId;
   }

   
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn( name = "REGIONAL_HIERARCHY_ID" )
   public RegionalHierarchyModel getRegionalHierarchyModel()
   {
       return this.regionalHierarchyModel;
   }

   
   public void setRegionalHierarchyModel( RegionalHierarchyModel regionalHierarchyModel)
   {
       this.regionalHierarchyModel = regionalHierarchyModel;
   }
  	
   @javax.persistence.Transient
   public Long getRegionalHierarchyId() {
      if (regionalHierarchyModel != null) {
         return regionalHierarchyModel.getRegionalHierarchyId();
      } else {
         return null;
      }
   }
   
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
   
   @javax.persistence.Transient
   public String getRegionalHierarchyName() {
      if (regionalHierarchyModel != null) {
         return regionalHierarchyModel.getRegionalHierarchyName();
      } else {
         return null;
      }
   }
   
   @javax.persistence.Transient
	public void setRegionalHierarchyName(String regionalHierarchyName) 
	{
		if(regionalHierarchyName == null)
		{      
			this.regionalHierarchyModel = null;
		}
		else
		{
			regionalHierarchyModel = new RegionalHierarchyModel();
			regionalHierarchyModel.setRegionalHierarchyName(regionalHierarchyName);
		}      
  }
   
   
   
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn( name = "DISTRIBUTOR_ID" )
   public DistributorModel getDistributor()
   {
       return this.distributor;
   }

   
   public void setDistributor( DistributorModel distributor )
   {
       this.distributor = distributor;
   }
  	
  	
   /**
    * Returns the value of the <code>distributorId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDistributorId() {
      if (distributor != null) {
         return distributor.getDistributorId();
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
	public void setDistributorId(Long distributorId) 
	{
		if(distributorId == null)
		{      
			this.distributor = null;
		}
		else
		{
			distributor = new DistributorModel();
			distributor.setDistributorId(distributorId);
		}      
  }
   
   @javax.persistence.Transient
   public String getDistributorName() {
      if (distributor != null) {
         return distributor.getName();
      } else {
         return null;
      }
   }
   
   @javax.persistence.Transient
	public void setDistributorName(String distributorName) 
	{
		if(distributorName == null)
		{      
			this.distributor = null;
		}
		else
		{
			distributor = new DistributorModel();
			distributor.setName(distributorName);
		}      
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
      parameters += "&distRegHierId=" + getDistRegHierId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "distRegHierId";
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
			    	
    	return associationModelList;
    }

    @javax.persistence.Transient
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@javax.persistence.Transient
	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	
	
}
