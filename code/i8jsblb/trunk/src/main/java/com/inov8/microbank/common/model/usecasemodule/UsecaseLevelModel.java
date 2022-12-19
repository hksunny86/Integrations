package com.inov8.microbank.common.model.usecasemodule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
/**
 * UsecaseCheckers entity. @author Hassan Javaid
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "USECASE_LEVEL_SEQ",sequenceName = "USECASE_LEVEL_SEQ",allocationSize=1)
@Table(name = "USECASE_LEVEL")
public class UsecaseLevelModel extends BasePersistableModel implements Serializable {
	private static final long serialVersionUID = 7132431963130286961L;
	
	private Collection<LevelCheckerModel> usecaseLevelIdLevelCheckerModelList = LazyList.decorate(new ArrayList<LevelCheckerModel>(), new Factory() {
	       public Object create() {
	           return new LevelCheckerModel();
	       }
	   });
	
	private Long usecaseLevelId;
	private Long levelNo;
	private UsecaseModel usecaseIdUsecaseModel;
	private Boolean intimateOnly;

	
	// Constructors

	/** default constructor */
	public UsecaseLevelModel() {
	}
	
	// Property accessors
	
	@Column(name = "USECASE_LEVEL_ID",nullable = false)
	@Id@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USECASE_LEVEL_SEQ") 
	public Long getUsecaseLevelId() {
		return this.usecaseLevelId;
	}
	

	 @javax.persistence.Transient
		
	public Long getPrimaryKey() {
		
		return usecaseLevelId ;
	}

	 @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName="usecaseLevelId";	
		return primaryKeyFieldName;
	}

	 @javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
	    parameters += "&usecaseLevelId=" + getUsecaseLevelId();
		return parameters;
	}

	 @javax.persistence.Transient
	public void setPrimaryKey(Long pk) {
		
		 usecaseLevelId=pk;
	}
	
	public void setUsecaseLevelId(Long usecaseCheckerId) {
		this.usecaseLevelId = usecaseCheckerId;
	}

	@Column(name = "LEVEL_NO", nullable = false)
	public Long getLevelNo() {
		return this.levelNo;
	}

	public void setLevelNo(Long levelNo) {
		this.levelNo = levelNo;
	}

	

	@Column(name = "INTIMATE_ONLY", nullable = false)
	public Boolean getIntimateOnly() {
		return this.intimateOnly;
	}

	public void setIntimateOnly(Boolean intimateOnly) {
		this.intimateOnly = intimateOnly;
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
	    	
    	associationModel.setClassName("UsecaseModel");
    	associationModel.setPropertyName("relationUsecaseIdUsecaseModel");   		
   		associationModel.setValue(getRelationUsecaseIdUsecaseModel());

   		associationModelList.add(associationModel);
		
   		return associationModelList;
    }  
    


////////////////// Usecase Model
	   /**
	    * Returns the value of the <code>usecaseIdUsecaseModel</code> relation property.
	    *
	    * @return the value of the <code>usecaseIdUsecaseModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	   @JoinColumn(name = "USECASE_ID")    
	   public UsecaseModel getRelationUsecaseIdUsecaseModel(){
	      return usecaseIdUsecaseModel;
	   }
	    
	   /**
	    * Returns the value of the <code>usecaseIdUsecaseModel</code> relation property.
	    *
	    * @return the value of the <code>usecaseIdUsecaseModel</code> relation property.
	    *
	    */
	   @javax.persistence.Transient
	   public UsecaseModel getUsecaseIdUsecaseModel(){
	      return getRelationUsecaseIdUsecaseModel();
	   }

	   /**
	    * Sets the value of the <code>usecaseIdUsecaseModel</code> relation property.
	    *
	    * @param usecaseModel a value for <code>usecaseIdUsecaseModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setRelationUsecaseIdUsecaseModel(UsecaseModel usecaseModel) {
	      this.usecaseIdUsecaseModel = usecaseModel;
	   }
	   
	   /**
	    * Sets the value of the <code>usecaseIdUsecaseModel</code> relation property.
	    *
	    * @param usecaseModel a value for <code>usecaseIdUsecaseModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setUsecaseIdUsecaseModel(UsecaseModel usecaseModel) {
	      if(null != usecaseModel)
	      {
	      	setRelationUsecaseIdUsecaseModel((UsecaseModel)usecaseModel.clone());
	      }      
	   }
	   /**
	    * Returns the value of the <code>usecaseId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getUsecaseId() {
	      if (usecaseIdUsecaseModel != null) {
	         return usecaseIdUsecaseModel.getUsecaseId();
	      } else {
	         return null;
	      }
	   }

	   /**
	    * Sets the value of the <code>usecaseId</code> property.
	    *
	    * @param usecaseId the value for the <code>usecaseId</code> property
																																																						    */
	   
	   @javax.persistence.Transient
	   public void setUsecaseId(Long usecaseId) {
	      if(usecaseId == null)
	      {      
	      	usecaseIdUsecaseModel = null;
	      }
	      else
	      {
	        usecaseIdUsecaseModel = new UsecaseModel();
	      	usecaseIdUsecaseModel.setUsecaseId(usecaseId);
	      }      
	   }
////////////// Level Checker Model List/////////////
	   /**
	     * Add the related ActionLogModel to this one-to-many relation.
	     *
	     * @param actionLogModel object to be added.
	     */
	     
	    public void addUsecaseLevelIdLevelCheckerModel(LevelCheckerModel levelCheckerModel) {
	    	levelCheckerModel.setRelationUsecaselevelIdUsecaseLevelModel(this);
	    	usecaseLevelIdLevelCheckerModelList.add(levelCheckerModel);
	    }
	    
	    /**
	     * Remove the related ActionLogModel to this one-to-many relation.
	     *
	     * @param actionLogModel object to be removed.
	     */
	    
	    public void removeUsecaseLevelIdLevelCheckerModel(LevelCheckerModel levelCheckerModel) {      
	       levelCheckerModel.setRelationUsecaselevelIdUsecaseLevelModel(null);
	       usecaseLevelIdLevelCheckerModelList.remove(levelCheckerModel);      
	    }

	    /**
	     * Get a list of related ActionLogModel objects of the UsecaseModel object.
	     * These objects are in a bidirectional one-to-many relation by the UsecaseId member.
	     *
	     * @return Collection of ActionLogModel objects.
	     *
	     */
	    
	    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationUsecaselevelIdUsecaseLevelModel")
	    @JoinColumn(name = "USECASE_LEVEL_ID")
	    public Collection<LevelCheckerModel> getUsecaseLevelIdLevelCheckerModelList() throws Exception {
	    		return usecaseLevelIdLevelCheckerModelList;
	    }


	    /**
	     * Set a list of ActionLogModel related objects to the UsecaseModel object.
	     * These objects are in a bidirectional one-to-many relation by the UsecaseId member.
	     *
	     * @param actionLogModelList the list of related objects.
	     */
	     public void setUsecaseLevelIdLevelCheckerModelList(Collection<LevelCheckerModel> levelCheckerModelList) throws Exception {
	 		this.usecaseLevelIdLevelCheckerModelList = levelCheckerModelList;
	    }

		/**
		 * @return the usecaseLevelIdLevelCheckerModelMap
		 */
	     @javax.persistence.Transient
	     public Map<String, String> getUsecaseLevelIdLevelCheckerModelMap() {
			
			Map<String,String> levelCheckerModelMap = new LinkedHashMap<>();
			for (LevelCheckerModel levelCheckerModel : usecaseLevelIdLevelCheckerModelList) {
			
				levelCheckerModelMap.put(levelCheckerModel.getCheckerIdAppUserModel().getAppUserId().toString(),levelCheckerModel.getCheckerIdAppUserModel().getUsername());
			}
			
			return levelCheckerModelMap;
		}
	     

		
}





	

	