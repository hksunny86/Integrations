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
import javax.xml.bind.annotation.*;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;

/**
 * The UsecaseModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 * @updaedBy HassanJavaid on 21-05-2014
 *
 * @spring.bean name="UsecaseModel"
 */
@XmlRootElement(name="usecaseModel")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "USECASE_seq",sequenceName = "USECASE_seq", allocationSize=1)
@Table(name = "USECASE")
public class UsecaseModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -5289550363702858503L;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;

   private Collection<ActionLogModel> usecaseIdActionLogModelList = new ArrayList<ActionLogModel>();
   
   private Collection<UsecaseLevelModel> usecaseIdLevelModelList = LazyList.decorate(new ArrayList<UsecaseLevelModel>(), new Factory() {
       public Object create() {
           return new UsecaseLevelModel();
       }
   });

   private Long usecaseId;
    @XmlElement
   private String name;
   private String description;
   private String comments;
   private Long escalationLevels;
   private Boolean isAuthorizationEnable;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;

   //added by atif hussain
   private String authorizationStatus;

   private String[] levelcheckers = new String[20];

   /**
    * Default constructor.
    */
   public UsecaseModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUsecaseId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUsecaseId(primaryKey);
    }

   /**
    * Returns the value of the <code>usecaseId</code> property.
    *
    */
      @Column(name = "USECASE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USECASE_seq")
   public Long getUsecaseId() {
      return usecaseId;
   }

   /**
    * Sets the value of the <code>usecaseId</code> property.
    *
    * @param usecaseId the value for the <code>usecaseId</code> property
    *    
		    */

   public void setUsecaseId(Long usecaseId) {
      this.usecaseId = usecaseId;
   }
   
   @Column(name = "MAX_ESCALATION_LEVELS")
   public Long getEscalationLevels() {
		return escalationLevels;
	}

	public void setEscalationLevels(Long escalationLevel) {
		this.escalationLevels = escalationLevel;
	}

	@Column(name = "IS_AUTHORIZATION_ENABLE" , nullable = false )
	public Boolean getIsAuthorizationEnable() {
		return isAuthorizationEnable;
	}

	public void setIsAuthorizationEnable(Boolean isCheckEnable) {
		this.isAuthorizationEnable = isCheckEnable;
	}    

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
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
    * Add the related UsecaseLevelModel to this one-to-many relation.
    *
    * @param UsecaseLevelModel object to be added.
    */
    
   public void addUsecaseIdUsecaseLevelModel(UsecaseLevelModel usecaseLevelModel) {
	   usecaseLevelModel.setRelationUsecaseIdUsecaseModel(this);
      usecaseIdLevelModelList.add(usecaseLevelModel);
   }
   
   /**
    * Remove the related UsecaseLevelModel to this one-to-many relation.
    *
    * @param UsecaseLevelModel object to be removed.
    */
   
   public void removeUsecaseIdUsecaseLevelModel(UsecaseLevelModel usecaseLevelModel) {      
	   usecaseLevelModel.setRelationUsecaseIdUsecaseModel(null);
	   usecaseIdLevelModelList.remove(usecaseLevelModel);     
   }

   /**
    * Get a list of related UsecaseLevelModel objects of the UsecaseModel object.
    * These objects are in a bidirectional one-to-many relation by the UsecaseId member.
    *
    * @return Collection of UsecaseLevelModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relationUsecaseIdUsecaseModel")
   @JoinColumn(name = "USECASE_ID")
   public Collection<UsecaseLevelModel> getUsecaseIdLevelModelList() throws Exception {
   		return usecaseIdLevelModelList;
   }


   /**
    * Set a list of UsecaseLevelModel related objects to the UsecaseModel object.
    * These objects are in a bidirectional one-to-many relation by the UsecaseId member.
    *
    * @param UsecaseLevelModelList the list of related objects.
    */
    public void setUsecaseIdLevelModelList(Collection<UsecaseLevelModel> usecaseIdLevelModelList) throws Exception {
		this.usecaseIdLevelModelList = usecaseIdLevelModelList;
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUsecaseId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&usecaseId=" + getUsecaseId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "usecaseId";
			return primaryKeyFieldName;				
    }
    
    /**
     * Add the related ActionLogModel to this one-to-many relation.
     *
     * @param actionLogModel object to be added.
     */
     
    public void addUsecaseIdActionLogModel(ActionLogModel actionLogModel) {
       actionLogModel.setRelationUsecaseIdUsecaseModel(this);
       usecaseIdActionLogModelList.add(actionLogModel);
    }
    
    /**
     * Remove the related ActionLogModel to this one-to-many relation.
     *
     * @param actionLogModel object to be removed.
     */
    
    public void removeUsecaseIdActionLogModel(ActionLogModel actionLogModel) {      
       actionLogModel.setRelationUsecaseIdUsecaseModel(null);
       usecaseIdActionLogModelList.remove(actionLogModel);      
    }

    /**
     * Get a list of related ActionLogModel objects of the UsecaseModel object.
     * These objects are in a bidirectional one-to-many relation by the UsecaseId member.
     *
     * @return Collection of ActionLogModel objects.
     *
     */
    
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUsecaseIdUsecaseModel")
    @JoinColumn(name = "USECASE_ID")
    public Collection<ActionLogModel> getUsecaseIdActionLogModelList() throws Exception {
    		return usecaseIdActionLogModelList;
    }


    /**
     * Set a list of ActionLogModel related objects to the UsecaseModel object.
     * These objects are in a bidirectional one-to-many relation by the UsecaseId member.
     *
     * @param actionLogModelList the list of related objects.
     */
     public void setUsecaseIdActionLogModelList(Collection<ActionLogModel> actionLogModelList) throws Exception {
 		this.usecaseIdActionLogModelList = actionLogModelList;
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
    public String[] getLevelcheckers() {
		return levelcheckers;
	}
    @javax.persistence.Transient
	public void setLevelcheckers(String[] levelcheckers) {
		this.levelcheckers = levelcheckers;
	}
    
    @javax.persistence.Transient
	public String getAuthorizationStatus() {
		if(isAuthorizationEnable)
			authorizationStatus="Enable";
		else
			authorizationStatus="Disable";
		return authorizationStatus;
	}
}
