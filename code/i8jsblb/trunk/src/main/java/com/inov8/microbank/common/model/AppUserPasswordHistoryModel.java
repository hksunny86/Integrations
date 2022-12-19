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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_USER_PASSWORD_HISTORY_seq",sequenceName = "APP_USER_PASSWORD_HISTORY_seq", allocationSize=2)
@Table(name = "APP_USER_PASSWORD_HISTORY")
public class AppUserPasswordHistoryModel extends BasePersistableModel implements Serializable {

	/**
	 * @author AbuTurab
	 */
	private static final long serialVersionUID = 8220325954056918321L;
	private AppUserModel appUserIdAppUserModel;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;

	private Long appUserPasswordHistoryId;
	private String password;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;
	
	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAppUserPasswordHistoryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setAppUserPasswordHistoryId(primaryKey);
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
   public void setAppUserIdAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationAppUserIdAppUserModel((AppUserModel)appUserModel.clone());
      }      
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
    * Sets the value of the <code>appUserIdAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>appUserIdAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAppUserIdAppUserModel(AppUserModel appUserModel) {
      this.appUserIdAppUserModel = appUserModel;
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
    * Sets the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>createdByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
      this.createdByAppUserModel = appUserModel;
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
    * Sets the value of the <code>updatedByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>updatedByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
      this.updatedByAppUserModel = appUserModel;
   }
    
	@Column(name = "APP_USER_PASSWORD_HISTORY_ID" , nullable = false )
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="APP_USER_PASSWORD_HISTORY_seq")
	public Long getAppUserPasswordHistoryId() {
		return appUserPasswordHistoryId;
	}
	
	
	public void setAppUserPasswordHistoryId(Long appUserPasswordHistoryId) {
		this.appUserPasswordHistoryId = appUserPasswordHistoryId;
	}
	
	@Column(name = "PASSWORD" , nullable = false , length=250 )
	public String getPassword() {
		return password;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	@Column(name = "CREATED_ON" , nullable = false )
	public Date getCreatedOn() {
		return createdOn;
	}
	
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Column(name = "UPDATED_ON" , nullable = false )
	public Date getUpdatedOn() {
		return updatedOn;
	}
	
	
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	@Version 
    @Column(name = "VERSION_NO" , nullable = false )
	public Integer getVersionNo() {
		return versionNo;
	}
	
	
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	/**
	    * Helper method for Struts with displaytag
	    */
	   @javax.persistence.Transient
	   public String getPrimaryKeyParameter() {
	      String parameters = "";
	      parameters += "&appUserPasswordHistoryId=" + getAppUserPasswordHistoryId();
	      return parameters;
	   }
		/**
	     * Helper method for default Sorting on Primary Keys
	     */
	    @javax.persistence.Transient
	    public String getPrimaryKeyFieldName()
	    { 
				String primaryKeyFieldName = "appUserPasswordHistoryId";
				return primaryKeyFieldName;				
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

}
