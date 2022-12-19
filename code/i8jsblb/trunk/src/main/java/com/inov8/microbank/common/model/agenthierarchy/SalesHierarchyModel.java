package com.inov8.microbank.common.model.agenthierarchy;

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
@javax.persistence.SequenceGenerator(name = "SALES_HIERARCHY_SEQ",sequenceName = "SALES_HIERARCHY_SEQ", allocationSize=1)
@Table(name = "SALES_HIERARCHY")
public class SalesHierarchyModel extends BasePersistableModel implements Serializable {
  
	
	private static final long serialVersionUID = -3571110109965363149L;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private AppUserModel bankUserAppUserModel;
	private AppUserModel parentBankUserAppUserModel;
	private AppUserModel ultimateParentBankUserAppUserModel;

	private Long salesHierarchyId;
	private String parentBankUserName;
	private String roleTitle;
   	private String description;
   	private String comments;
   	private Date createdOn;
   	private Date updatedOn;
   	private Integer versionNo;
   	
   	private boolean editMode;
   	private Integer index;
   	   	
//   	private Collection<SalesHierarchyHistoryModel> salesHierarchyIdSalesHierarchyHistoryModelList = new ArrayList<SalesHierarchyHistoryModel>();
   	
   	/**
    * Default constructor.
    */
   	public SalesHierarchyModel() 
   	{
   	}   
   	    
    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSalesHierarchyId();
        
        
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSalesHierarchyId(primaryKey);
    }

   /**
    * Returns the value of the <code>regionId</code> property.
    *
    */
   @Column(name = "SALES_HIERARCHY_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SALES_HIERARCHY_SEQ")
   public Long getSalesHierarchyId() {
      return salesHierarchyId;
   }

   /**
    * Sets the value of the <code>regionalHirarchyId</code> property.
    *
    * @param regionId the value for the <code>regionalHirarchyId</code> property
    *    
		    */

   public void setSalesHierarchyId(Long salesHierarchyId) {
      this.salesHierarchyId = salesHierarchyId;
   }

   
   /**
    * Returns the value of the <code>regionalHirarchyName</code> property.
    *
    */
   @Column(name = "ROLE_TITLE" , length=50 )
   public String getRoleTitle() 
   {
	   return roleTitle;
   }

   public void setRoleTitle(String roleTitle) 
   {
	   this.roleTitle = roleTitle;
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
      parameters += "&salesHierarchyId=" + getSalesHierarchyId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
		String primaryKeyFieldName = "salesHierarchyId";
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
    	associationModel.setPropertyName("relationBankUserAppUserModel");   		
   		associationModel.setValue(getRelationBankUserAppUserModel());
   		
   		associationModelList.add(associationModel);
			    	
    	return associationModelList;
    }

    @javax.persistence.Transient
	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

    @javax.persistence.Transient
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_BANK_USER_ID")    
	public AppUserModel getRelationParentBankUserAppUserModel(){
		return parentBankUserAppUserModel;
	}
	   
	@javax.persistence.Transient
	public AppUserModel getParentBankUserAppUserModel(){
		return getRelationParentBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationParentBankUserAppUserModel(AppUserModel parentBankUserAppUserModel) {
		this.parentBankUserAppUserModel = parentBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setParentBankUserAppUserModel(AppUserModel parentBankUserAppUserModel) {
		if(null != parentBankUserAppUserModel)
		{
			setRelationParentBankUserAppUserModel((AppUserModel)parentBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getParentBankUserId() 
	{
		if (parentBankUserAppUserModel != null) {
			return parentBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setParentBankUserId(Long parentBankUserId) 
	{
		if(parentBankUserId == null)
	    {      
			parentBankUserAppUserModel = null;
	    }
	    else
	    {
	    	parentBankUserAppUserModel = new AppUserModel();
	    	parentBankUserAppUserModel.setAppUserId(parentBankUserId);
	    }      
	}
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "BANK_USER_ID")    
	public AppUserModel getRelationBankUserAppUserModel(){
		return bankUserAppUserModel;
	}
	   
	@javax.persistence.Transient
	public AppUserModel getBankUserAppUserModel(){
		return getRelationBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationBankUserAppUserModel(AppUserModel bankUserAppUserModel) {
		this.bankUserAppUserModel = bankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setBankUserAppUserModel(AppUserModel bankUserAppUserModel) {
		if(null != bankUserAppUserModel)
		{
			setRelationBankUserAppUserModel((AppUserModel)bankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getBankUserId() 
	{
		if (bankUserAppUserModel != null) {
			return bankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setBankUserId(Long bankUserId) 
	{
		if(bankUserId == null)
	    {      
			bankUserAppUserModel = null;
	    }
	    else
	    {
	    	bankUserAppUserModel = new AppUserModel();
	    	bankUserAppUserModel.setAppUserId(bankUserId);
	    }      
	}
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ULTIMATE_PARENT_BANK_USER_ID")    
	public AppUserModel getRelationUltimateParentBankUserAppUserModel(){
		return ultimateParentBankUserAppUserModel;
	}
	   
	@javax.persistence.Transient
	public AppUserModel getUltimateParentBankUserAppUserModel(){
		return getRelationUltimateParentBankUserAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationUltimateParentBankUserAppUserModel(AppUserModel ultimateParentBankUserAppUserModel) {
		this.ultimateParentBankUserAppUserModel = ultimateParentBankUserAppUserModel;
	}
   
	@javax.persistence.Transient
	public void setUltimateParentBankUserAppUserModel(AppUserModel ultimateParentBankUserAppUserModel) {
		if(null != ultimateParentBankUserAppUserModel)
		{
			setRelationUltimateParentBankUserAppUserModel((AppUserModel)ultimateParentBankUserAppUserModel.clone());
		}      
	}
	@javax.persistence.Transient
	public Long getUltimateParentBankUserId() 
	{
		if (ultimateParentBankUserAppUserModel != null) {
			return ultimateParentBankUserAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}
	   
	@javax.persistence.Transient
	public void setUltimateParentBankUserId(Long ultimateParentBankUserId) 
	{
		if(ultimateParentBankUserId == null)
	    {      
			ultimateParentBankUserAppUserModel = null;
	    }
	    else
	    {
	    	ultimateParentBankUserAppUserModel = new AppUserModel();
	    	ultimateParentBankUserAppUserModel.setAppUserId(ultimateParentBankUserId);
	    }      
	}

	@javax.persistence.Transient
	public String getParentBankUserName() {
		return parentBankUserName;
	}

	public void setParentBankUserName(String parentBankUserName) {
		this.parentBankUserName = parentBankUserName;
	}
	
//	public void addSalesHierarchyIdSalesHierarchyHistoryModel(SalesHierarchyHistoryModel salesHierarchyHistoryModel) {
//	     salesHierarchyHistoryModel.setRelationSalesHierarchyIdSalesHierarchyModel(this);
//	      salesHierarchyIdSalesHierarchyHistoryModelList.add(salesHierarchyHistoryModel);
//	}
//	public void removeSalesHierarchyIdSalesHierarchyHistoryModel(SalesHierarchyHistoryModel salesHierarchyHistoryModel) {      
//		salesHierarchyHistoryModel.setRelationSalesHierarchyIdSalesHierarchyModel(null);;
//		salesHierarchyIdSalesHierarchyHistoryModelList.remove(salesHierarchyHistoryModel);      
//	}
//	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSalesHierarchyIdSalesHierarchyModel")
//	@JoinColumn(name = "SALES_HIERARCHY_ID")
//	public Collection<SalesHierarchyHistoryModel> getSalesHierarchyIdSalesHierarchyHistoryModelList() throws Exception {
//		return salesHierarchyIdSalesHierarchyHistoryModelList;
//	}
//
//	public void setSalesHierarchyIdSalesHierarchyHistoryModelList(Collection<SalesHierarchyHistoryModel> salesHierarchyIdSalesHierarchyHistoryModelList) throws Exception {
//		this.salesHierarchyIdSalesHierarchyHistoryModelList = salesHierarchyIdSalesHierarchyHistoryModelList;
//	}
	
}
