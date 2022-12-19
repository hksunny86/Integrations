package com.inov8.microbank.common.model.usecasemodule;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

/**
 * LevelChecker entity. @author Hassan Javaid
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "LEVEL_CHECKERS_SEQ",sequenceName = "LEVEL_CHECKERS_SEQ",allocationSize=1)
@Table(name = "LEVEL_CHECKERS")
public class LevelCheckerModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -3249546037497019013L;
	private Long levelCheckerId;
	private UsecaseLevelModel usecaselevelIdUsecaseLevelModel;
	private AppUserModel checkerIdAppUserModel;
	private Long seqNo;
	// Constructors

	/** default constructor */
	public LevelCheckerModel() {
	}
	
	// Property accessors
	
	@Column(name = "LEVEL_CHECKER_ID")
	@Id@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LEVEL_CHECKERS_SEQ") 
	public Long getLevelCheckerId() {
		return this.levelCheckerId;
	}

	 @javax.persistence.Transient
		
	public Long getPrimaryKey() {
		
		return levelCheckerId ;
	}

	 @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName="levelcheckerId";	
		return primaryKeyFieldName;
	}

	 @javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
	    parameters += "&levelCheckerId=" + getLevelCheckerId();
		return parameters;
	}

	 @javax.persistence.Transient
	public void setPrimaryKey(Long pk) {
		
		 levelCheckerId=pk;
	}
	 @javax.persistence.Transient
	public void setLevelCheckerId(Long levelCheckerId) {
		this.levelCheckerId = levelCheckerId;
	}
	 
	@Column(name = "SEQ_NO")
	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
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
     	
	 	associationModel.setClassName("UsecaseLevelModel");
	 	associationModel.setPropertyName("relationUsecaselevelIdUsecaseLevelModel");   		
		associationModel.setValue(getRelationUsecaselevelIdUsecaseLevelModel());
		
		associationModelList.add(associationModel);
			
   		associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCheckerIdAppUserModel");   		
   		associationModel.setValue(getRelationCheckerIdAppUserModel());
   		
   		associationModelList.add(associationModel);
		
		return associationModelList;
    }  
    

////////CheckerIdAppUserModel Model///////
		 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
		   @JoinColumn(name = "CHECKER_APP_USER_ID")
		 @Fetch(FetchMode.JOIN)
		   public AppUserModel getRelationCheckerIdAppUserModel(){
		      return checkerIdAppUserModel;
		   }
		   
		   @javax.persistence.Transient
		   public AppUserModel getCheckerIdAppUserModel(){
		      return getRelationCheckerIdAppUserModel();
		   }

		   
		   @javax.persistence.Transient
		   public void setRelationCheckerIdAppUserModel(AppUserModel appUserModel) {
		      this.checkerIdAppUserModel = appUserModel;
		   }
		   
		   
		   @javax.persistence.Transient
		   public void setCheckerIdAppUserModel(AppUserModel appUserModel) {
		      if(null != appUserModel)
		      {
		    	  setRelationCheckerIdAppUserModel((AppUserModel)appUserModel.clone());
		      }      
		   }
		   
		   @javax.persistence.Transient
		   public Long getCheckerId() {
		      if (checkerIdAppUserModel != null) {
		         return checkerIdAppUserModel.getAppUserId();
		      } else {
		         return null;
		      }
		   }

		   
		   @javax.persistence.Transient
		   public void setCheckerId(Long appUserId) {
		      if(appUserId == null)
		      {      
		    	  checkerIdAppUserModel = null;
		      }
		      else
		      {
		    	  checkerIdAppUserModel = new AppUserModel();
		    	  checkerIdAppUserModel.setAppUserId(appUserId);;
		      }      
		   }

////////////////// UsecaseLevel Model
	   /**
	    * Returns the value of the <code>usecaselevelIdUsecaseLevelModel</code> relation property.
	    *
	    * @return the value of the <code>usecaselevelIdUsecaseLevelModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	   @JoinColumn(name = "USECASE_LEVEL_ID")  
	   public UsecaseLevelModel getRelationUsecaselevelIdUsecaseLevelModel(){
	      return usecaselevelIdUsecaseLevelModel;
	   }
	    
	   /**
	    * Returns the value of the <code>usecaselevelIdUsecaseLevelModel</code> relation property.
	    *
	    * @return the value of the <code>usecaselevelIdUsecaseLevelModel</code> relation property.
	    *
	    */
	   @javax.persistence.Transient
	   public UsecaseLevelModel getUsecaselevelIdUsecaseLevelModel(){
	      return getRelationUsecaselevelIdUsecaseLevelModel();
	   }

	   /**
	    * Sets the value of the <code>usecaselevelIdUsecaseLevelModel</code> relation property.
	    *
	    * @param usecaseModel a value for <code>usecaselevelIdUsecaseLevelModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setRelationUsecaselevelIdUsecaseLevelModel(UsecaseLevelModel usecaseLevelModel) {
	      this.usecaselevelIdUsecaseLevelModel = usecaseLevelModel;
	   }
	   
	   /**
	    * Sets the value of the <code>usecaselevelIdUsecaseLevelModel</code> relation property.
	    *
	    * @param usecaseModel a value for <code>usecaselevelIdUsecaseLevelModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setUsecaselevelIdUsecaseLevelModel(UsecaseLevelModel usecaseLevelModel) {
	      if(null != usecaseLevelModel)
	      {
	      	setRelationUsecaselevelIdUsecaseLevelModel((UsecaseLevelModel)usecaseLevelModel.clone());
	      }      
	   }
	   /**
	    * Returns the value of the <code>usecaseLevelId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getUsecaseLevelId() {
	      if (usecaselevelIdUsecaseLevelModel != null) {
	         return usecaselevelIdUsecaseLevelModel.getUsecaseLevelId();
	      } else {
	         return null;
	      }
	   }

	   /**
	    * Sets the value of the <code>usecaseLevelId</code> property.
	    *
	    * @param usecaseId the value for the <code>usecaseId</code> property
																																																						    */
	   
	   @javax.persistence.Transient
	   public void setUsecaseLevelId(Long usecaseLevelId) {
	      if(usecaseLevelId == null)
	      {      
	    	  usecaselevelIdUsecaseLevelModel = null;
	      }
	      else
	      {
	    	  usecaselevelIdUsecaseLevelModel = new UsecaseLevelModel();
	    	  usecaselevelIdUsecaseLevelModel.setUsecaseLevelId(usecaseLevelId);
	      }      
	   }
}