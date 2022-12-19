package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.webapp.action.appusermodule.RetagMobileCnicVo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author  Hassan  Inov8 Limited
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "RETAG_MOBILE_CNIC_HISTORY_SEQ",sequenceName = "RETAG_MOBILE_CNIC_HISTORY_SEQ", allocationSize=1)
@Table(name = "RETAG_MOBILE_CNIC_HISTORY")
public class ReTagMobileCnicHistoryModel extends BasePersistableModel implements Serializable{
  
	private static final long serialVersionUID = -3524263585105492103L;
	
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel appUserIdAppUserModel;

	private Long reTagMobileCnicHistoryId;
	private String 	currentMobileNo;
	private	String	currentNic;
	private String 	newMobileNo;
	private	String	newNic;
	private String 	description;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

   /**
    * Default constructor.
    */
   public ReTagMobileCnicHistoryModel() {
   } 
   
   public ReTagMobileCnicHistoryModel(RetagMobileCnicVo vo) {
	   currentMobileNo= vo.getOldMobileNo();
	   currentNic = vo.getOldCnic();
	   newMobileNo = vo.getNewMobileNo();
	   newNic = vo.getNewCnic();
	   description = vo.getComments();
	   createdOn = vo.getCreatedOn();
	   updatedOn = vo.getCreatedOn();
	   this.setCreatedBy(vo.getCreatedBy());
	   this.setUpdatedBy(vo.getCreatedBy());
	   this.setAppUserId(vo.getAppUserId());
   }

   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getReTagMobileCnicHistoryId();
    }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setReTagMobileCnicHistoryId(primaryKey);
    }


   @Column(name = "RETAG_MOBILE_CNIC_HISTORY_ID" , nullable = false )
   @Id
   @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="RETAG_MOBILE_CNIC_HISTORY_SEQ")
   	public Long getReTagMobileCnicHistoryId() {
		return reTagMobileCnicHistoryId;
	}

	public void setReTagMobileCnicHistoryId(Long reTagMobileCnicHistoryId) {
		this.reTagMobileCnicHistoryId = reTagMobileCnicHistoryId;
	}

   @Column(name="CURRENT_MOBILE")
	public String getCurrentMobileNo() {
		return currentMobileNo;
	}

	public void setCurrentMobileNo(String currentMobileNo) {
		this.currentMobileNo = currentMobileNo;
	}

	@Column(name="CURRENT_CNIC")
	public String getCurrentNic() {
		return currentNic;
	}

	public void setCurrentNic(String currentNic) {
		this.currentNic = currentNic;
	}

	@Column(name="NEW_MOBILE")
	public String getNewMobileNo() {
		return newMobileNo;
	}

	public void setNewMobileNo(String newMobileNo) {
		this.newMobileNo = newMobileNo;
	}

	@Column(name="NEW_CNIC")
	public String getNewNic() {
		return newNic;
	}

	public void setNewNic(String newNic) {
		this.newNic = newNic;
	}

	@Column(name = "COMMENTS"  , length=250 )
   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
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
        checkBox += "_"+ getReTagMobileCnicHistoryId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&reTagMobileCnicHistoryId=" + getReTagMobileCnicHistoryId();
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
}
