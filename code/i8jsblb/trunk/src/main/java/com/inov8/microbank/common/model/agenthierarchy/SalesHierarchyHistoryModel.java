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
 * The SalesHierarchyModel entity bean.
 *
 * @author  Rashid Mahmood  Inov8 Limited
 * @version 1.0
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SALES_HIERARCHY_HISTORY_SEQ",sequenceName = "SALES_HIERARCHY_HISTORY_SEQ", allocationSize=1)
@Table(name = "SALES_HIERARCHY_HISTORY")
public class SalesHierarchyHistoryModel extends BasePersistableModel implements Serializable {
  	
	private static final long serialVersionUID = -2587601485316980962L;
  	
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private AppUserModel oldBankUserAppUserModel;
	private AppUserModel oldParentBankUserAppUserModel;
	private AppUserModel oldUltimateParentBankUserAppUserModel;
	
	private AppUserModel newBankUserAppUserModel;
	private AppUserModel newParentBankUserAppUserModel;
	private AppUserModel newUltimateParentBankUserAppUserModel;
	
	private SalesHierarchyModel	salesHierarchyIdSalesHierarchyModel;

	
	private Long salesHierarchyHistoryId;
	private String oldParentBankUserName;
	private String newParentBankUserName;
	private String oldRoleTitle;
	private String newRoleTitle;
   	private String oldDescription;
   	private String newDescription;
   	private String oldComments;
   	private String newComments;
   	private Date createdOn;
   	private Date updatedOn;
   	private Integer versionNo;
   	
   	private Boolean oldActive;
   	private Boolean newActive;
   	
   	/**
    * Default constructor.
    */
   	public SalesHierarchyHistoryModel() 
   	{
   	}   
   	    
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSalesHierarchyHistoryId();
        
        
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSalesHierarchyHistoryId(primaryKey);
    }

   /**
    * Returns the value of the <code>regionId</code> property.
    *
    */
   @Column(name = "SALES_HIERARCHY_HISTORY_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SALES_HIERARCHY_HISTORY_SEQ")
   public Long getSalesHierarchyHistoryId() {
      return salesHierarchyHistoryId;
   }

   /**
    * Sets the value of the <code>regionalHirarchyId</code> property.
    *
    * @param regionId the value for the <code>regionalHirarchyId</code> property
    *    
		    */

   public void setSalesHierarchyHistoryId(Long salesHierarchyHistoryId) {
      this.salesHierarchyHistoryId = salesHierarchyHistoryId;
   }

   
   @Column(name = "OLD_ROLE_TITLE" , length=50 )
   public String getOldRoleTitle() 
   {
	   return oldRoleTitle;
   }

   public void setOldRoleTitle(String oldRoleTitle) 
   {
	   this.oldRoleTitle = oldRoleTitle;
   }
   
   @Column(name = "NEW_ROLE_TITLE" , length=50 )
   public String getNewRoleTitle() 
   {
	   return newRoleTitle;
   }

   public void setNewRoleTitle(String newRoleTitle) 
   {
	   this.newRoleTitle = newRoleTitle;
   }
      
	/**
	* Returns the value of the <code>description</code> property.
	*
	*/
   @Column(name = "OLD_DESCRIPTION"  , length=250 )
   public String getOldDescription() {
      return oldDescription;
   }

/**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
    */

   public void setOldDescription(String oldDescription) {
      this.oldDescription = oldDescription;
   }
   
   @Column(name = "NEW_DESCRIPTION"  , length=250 )
   public String getNewDescription() {
      return newDescription;
   }

/**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
    */

   public void setNewDescription(String newDescription) {
      this.newDescription = newDescription;
   }

   /**
    * Returns the value of the <code>comments</code> property.
    *
    */
   @Column(name = "OLD_COMMENTS"  , length=250 )
   public String getOldComments() {
      return oldComments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
    */

   public void setOldComments(String oldComments) {
      this.oldComments = oldComments;
   }
   
   @Column(name = "NEW_COMMENTS"  , length=250 )
   public String getNewComments() {
      return newComments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
    */

   public void setNewComments(String newComments) {
      this.newComments = newComments;
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
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
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
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
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
      parameters += "&salesHierarchyHistoryId=" + getSalesHierarchyHistoryId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
		String primaryKeyFieldName = "salesHierarchyHistoryId";
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
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationOldBankUserAppUserModel");   		
   		associationModel.setValue(getRelationOldBankUserAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationNewBankUserAppUserModel");   		
   		associationModel.setValue(getRelationNewBankUserAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationOldParentBankUserAppUserModel");   		
   		associationModel.setValue(getRelationOldParentBankUserAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationNewParentBankUserAppUserModel");   		
   		associationModel.setValue(getRelationNewParentBankUserAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationOldUltimateParentBankUserAppUserModel");   		
   		associationModel.setValue(getRelationOldUltimateParentBankUserAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationNewUltimateParentBankUserAppUserModel");   		
   		associationModel.setValue(getRelationNewUltimateParentBankUserAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("SalesHierarchyModel");
    	associationModel.setPropertyName("relationSalesHierarchyIdSalesHierarchyModel");   		
   		associationModel.setValue(getRelationSalesHierarchyIdSalesHierarchyModel());
   		
   		associationModelList.add(associationModel);
			    	
    	return associationModelList;
    }

    //oldParentBankUser	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "OLD_PARENT_BANK_USER_ID")    
	public AppUserModel getRelationOldParentBankUserAppUserModel(){
		return oldParentBankUserAppUserModel;
	}
	   
	@javax.persistence.Transient
	public AppUserModel getOldParentBankUserAppUserModel(){
		return getRelationOldParentBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationOldParentBankUserAppUserModel(AppUserModel oldParentBankUserAppUserModel) {
		this.oldParentBankUserAppUserModel = oldParentBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setOldParentBankUserAppUserModel(AppUserModel oldParentBankUserAppUserModel) {
		if(null != oldParentBankUserAppUserModel)
		{
			setRelationOldParentBankUserAppUserModel((AppUserModel)oldParentBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getOldParentBankUserId() 
	{
		if (oldParentBankUserAppUserModel != null) {
			return oldParentBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setOldParentBankUserId(Long oldParentBankUserId) 
	{
		if(oldParentBankUserId == null)
	    {      
			oldParentBankUserAppUserModel = null;
	    }
	    else
	    {
	    	oldParentBankUserAppUserModel = new AppUserModel();
	    	oldParentBankUserAppUserModel.setAppUserId(oldParentBankUserId);
	    }      
	}//end of oldParentBankUser

	//newParentBankUser
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "NEW_PARENT_BANK_USER_ID")    
	public AppUserModel getRelationNewParentBankUserAppUserModel(){
		return newParentBankUserAppUserModel;
	}
	   
	@javax.persistence.Transient
	public AppUserModel getNewParentBankUserAppUserModel(){
		return getRelationNewParentBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationNewParentBankUserAppUserModel(AppUserModel newParentBankUserAppUserModel) {
		this.newParentBankUserAppUserModel = newParentBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setNewParentBankUserAppUserModel(AppUserModel newParentBankUserAppUserModel) {
		if(null != newParentBankUserAppUserModel)
		{
			setRelationNewParentBankUserAppUserModel((AppUserModel)newParentBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getNewParentBankUserId() 
	{
		if (newParentBankUserAppUserModel != null) {
			return newParentBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setNewParentBankUserId(Long newParentBankUserId) 
	{
		if(newParentBankUserId == null)
	    {      
			newParentBankUserAppUserModel = null;
	    }
	    else
	    {
	    	newParentBankUserAppUserModel = new AppUserModel();
	    	newParentBankUserAppUserModel.setAppUserId(newParentBankUserId);
	    }      
	}//end of newParentBankUser
	
	//oldBankUser
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "OLD_BANK_USER_ID")    
	public AppUserModel getRelationOldBankUserAppUserModel(){
		return oldBankUserAppUserModel;
	}
	   
	@javax.persistence.Transient
	public AppUserModel getOldBankUserAppUserModel(){
		return getRelationOldBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationOldBankUserAppUserModel(AppUserModel oldBankUserAppUserModel) {
		this.oldBankUserAppUserModel = oldBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setOldBankUserAppUserModel(AppUserModel oldBankUserAppUserModel) {
		if(null != oldBankUserAppUserModel)
		{
			setRelationOldBankUserAppUserModel((AppUserModel)oldBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getOldBankUserId() 
	{
		if (oldBankUserAppUserModel != null) {
			return oldBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setOldBankUserId(Long oldBankUserId) 
	{
		if(oldBankUserId == null)
	    {      
			oldBankUserAppUserModel = null;
	    }
	    else
	    {
	    	oldBankUserAppUserModel = new AppUserModel();
	    	oldBankUserAppUserModel.setAppUserId(oldBankUserId);
	    }      
	}//end of oldBankUser
	
	//newBankUser
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "NEW_BANK_USER_ID")    
	public AppUserModel getRelationNewBankUserAppUserModel(){
		return newBankUserAppUserModel;
	}
	   
	@javax.persistence.Transient
	public AppUserModel getNewBankUserAppUserModel(){
		return getRelationNewBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationNewBankUserAppUserModel(AppUserModel newBankUserAppUserModel) {
		this.newBankUserAppUserModel = newBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setNewBankUserAppUserModel(AppUserModel newBankUserAppUserModel) {
		if(null != newBankUserAppUserModel)
		{
			setRelationNewBankUserAppUserModel((AppUserModel)newBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getNewBankUserId() 
	{
		if (newBankUserAppUserModel != null) {
			return newBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setNewBankUserId(Long newBankUserId) 
	{
		if(newBankUserId == null)
	    {      
			newBankUserAppUserModel = null;
	    }
	    else
	    {
	    	newBankUserAppUserModel = new AppUserModel();
	    	newBankUserAppUserModel.setAppUserId(newBankUserId);
	    }      
	}//end of newBankUser
	
	//oldUltimateParentBankUser
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "OLD_ULT_PARENT_BANK_USER_ID")    
	public AppUserModel getRelationOldUltimateParentBankUserAppUserModel(){
		return oldUltimateParentBankUserAppUserModel;
	}
	
	@javax.persistence.Transient
	public AppUserModel getOldUltimateParentBankUserAppUserModel(){
		return getRelationOldUltimateParentBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationOldUltimateParentBankUserAppUserModel(AppUserModel oldUltimateParentBankUserAppUserModel) {
		this.oldUltimateParentBankUserAppUserModel = oldUltimateParentBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setOldUltimateParentBankUserAppUserModel(AppUserModel oldUltimateParentBankUserAppUserModel) {
		if(null != oldUltimateParentBankUserAppUserModel)
		{
			setRelationOldUltimateParentBankUserAppUserModel((AppUserModel)oldUltimateParentBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getOldUltimateParentBankUserId() 
	{
		if (oldUltimateParentBankUserAppUserModel != null) {
			return oldUltimateParentBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setOldUltimateParentBankUserId(Long oldUltimateParentBankUserId) 
	{
		if(oldUltimateParentBankUserId == null)
	    {      
			oldUltimateParentBankUserAppUserModel = null;
	    }
	    else
	    {
	    	oldUltimateParentBankUserAppUserModel = new AppUserModel();
	    	oldUltimateParentBankUserAppUserModel.setAppUserId(oldUltimateParentBankUserId);
	    }      
	}//end oldUltimateParentBankUser
	
	//newUltimateParentBankUser
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "NEW_ULT_PARENT_BANK_USER_ID")    
	public AppUserModel getRelationNewUltimateParentBankUserAppUserModel(){
		return newUltimateParentBankUserAppUserModel;
	}
	
	@javax.persistence.Transient
	public AppUserModel getNewUltimateParentBankUserAppUserModel(){
		return getRelationNewUltimateParentBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationNewUltimateParentBankUserAppUserModel(AppUserModel newUltimateParentBankUserAppUserModel) {
		this.newUltimateParentBankUserAppUserModel = newUltimateParentBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setNewUltimateParentBankUserAppUserModel(AppUserModel newUltimateParentBankUserAppUserModel) {
		if(null != newUltimateParentBankUserAppUserModel)
		{
			setRelationNewUltimateParentBankUserAppUserModel((AppUserModel)newUltimateParentBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getNewUltimateParentBankUserId() 
	{
		if (newUltimateParentBankUserAppUserModel != null) {
			return newUltimateParentBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setNewUltimateParentBankUserId(Long newUltimateParentBankUserId) 
	{
		if(newUltimateParentBankUserId == null)
	    {      
			newUltimateParentBankUserAppUserModel = null;
	    }
	    else
	    {
	    	newUltimateParentBankUserAppUserModel = new AppUserModel();
	    	newUltimateParentBankUserAppUserModel.setAppUserId(newUltimateParentBankUserId);
	    }      
	}//end newUltimateParentBankUser
	
	//salesHierarchyIdSalesHierarchyModel
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "SALES_HIERARCHY_ID")    
	public SalesHierarchyModel getRelationSalesHierarchyIdSalesHierarchyModel(){
		return salesHierarchyIdSalesHierarchyModel;
	}
	
	@javax.persistence.Transient
	public SalesHierarchyModel getSalesHierarchyIdSalesHierarchyModel(){
		return getRelationSalesHierarchyIdSalesHierarchyModel();
	}

	@javax.persistence.Transient
	public void setRelationSalesHierarchyIdSalesHierarchyModel(SalesHierarchyModel salesHierarchyIdSalesHierarchyModel) {
		this.salesHierarchyIdSalesHierarchyModel = salesHierarchyIdSalesHierarchyModel;
	}
   
	@javax.persistence.Transient
	public void setSalesHierarchyIdSalesHierarchyModel(SalesHierarchyModel saleshierarchyIdSalesHierarchyModel) {
		if(null != saleshierarchyIdSalesHierarchyModel)
		{
			setRelationSalesHierarchyIdSalesHierarchyModel((SalesHierarchyModel)saleshierarchyIdSalesHierarchyModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getSalesHierarchyId() 
	{
		if (salesHierarchyIdSalesHierarchyModel != null) {
			return salesHierarchyIdSalesHierarchyModel.getSalesHierarchyId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setSalesHierarchyId(Long salesHierarchyId) 
	{
		if(salesHierarchyId == null)
	    {      
			salesHierarchyIdSalesHierarchyModel = null;
	    }
	    else
	    {
	    	salesHierarchyIdSalesHierarchyModel = new SalesHierarchyModel();
	    	salesHierarchyIdSalesHierarchyModel.setSalesHierarchyId(salesHierarchyId);
	    }      
	}//end salesHierarchyIdSalesHierarchyModel


	@javax.persistence.Transient
	public String getOldParentBankUserName() {
		return oldParentBankUserName;
	}

	public void setOldParentBankUserName(String oldParentBankUserName) {
		this.oldParentBankUserName = oldParentBankUserName;
	}
	
	@javax.persistence.Transient
	public String getNewParentBankUserName() {
		return newParentBankUserName;
	}

	public void setNewParentBankUserName(String newParentBankUserName) {
		this.newParentBankUserName = newParentBankUserName;
	}
	
	@Column(name = "OLD_IS_ACTIVE" , nullable = false )
    public Boolean getOldActive() {
     return oldActive;
    }

	public void setOldActive(Boolean oldActive) {
     this.oldActive = oldActive;
    }
	
	@Column(name = "NEW_IS_ACTIVE" , nullable = false )
    public Boolean getNewActive() {
     return newActive;
    }

	public void setNewActive(Boolean newActive) {
     this.newActive = newActive;
    }

}
