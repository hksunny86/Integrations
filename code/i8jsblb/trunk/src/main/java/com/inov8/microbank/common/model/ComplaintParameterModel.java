package com.inov8.microbank.common.model;

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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * HolidayModel entity.
 * @author Muhammad Omar Butt
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMPLAINT_PARAMETER_SEQ",sequenceName = "COMPLAINT_PARAMETER_SEQ", allocationSize=1)
@Table(name = "COMPLAINT_PARAMETER")
public class ComplaintParameterModel  extends BasePersistableModel {

	 private static final long serialVersionUID = 8085631766182075543L;
	 
	 private Long complaintParameterId;
     private String parameterName;
     private ComplaintCategoryModel complaintCategoryIdModel;
     private Boolean isActive;

    public ComplaintParameterModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getComplaintParameterId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setComplaintParameterId(primaryKey);
    }
    
    
    @Column(name = "COMPLAINT_PARAMETER_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPLAINT_PARAMETER_SEQ")
    public Long getComplaintParameterId() {
        return this.complaintParameterId;
    }
    
    public void setComplaintParameterId(Long complaintParameterId) {
        this.complaintParameterId = complaintParameterId;
    }
    
    @Column(name="PARAMETER_NAME")
    public String getParameterName() {
        return this.parameterName;
    }
    
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
    
    
    @Column(name="IS_ACTIVE")
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
       String parameters = "";
       parameters += "&complaintParameterId=" + getComplaintParameterId();
       return parameters;
    }
 	
    /**
      * Helper method for default Sorting on Primary Keys
      */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName(){ 
    	String primaryKeyFieldName = "complaintParameterId";
 		return primaryKeyFieldName;				
    }


    
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPLAINT_CATEGORY_ID")    
    public ComplaintCategoryModel getRelationComplaintCategoryIdModel(){
       return complaintCategoryIdModel;
    }
     
    @javax.persistence.Transient
    public ComplaintCategoryModel getComplaintCategoryIdModel(){
       return getComplaintCategoryIdModel();
    }

    @javax.persistence.Transient
    public void setRelationComplaintCategoryIdModel(ComplaintCategoryModel complaintCategoryIdModel) {
       this.complaintCategoryIdModel = complaintCategoryIdModel;
    }
    
    @javax.persistence.Transient
    public void setComplaintCategoryIdModel(ComplaintCategoryModel complaintCategoryIdModel) {
       if(null != complaintCategoryIdModel)
       {
       	setRelationComplaintCategoryIdModel((ComplaintCategoryModel)complaintCategoryIdModel.clone());
       }      
    }

    @javax.persistence.Transient
    public Long getComplaintCategoryId() {
       if (complaintCategoryIdModel != null) {
          return complaintCategoryIdModel.getComplaintCategoryId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setcomplaintCategoryId(Long complaintCategoryId) {
       if(complaintCategoryId == null)
       {      
    	   complaintCategoryIdModel = null;
       }
       else
       {
    	   complaintCategoryIdModel = new ComplaintCategoryModel();
    	   complaintCategoryIdModel.setComplaintCategoryId(complaintCategoryId);
       }      
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList(){
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	

    	AssociationModel associationModel = new AssociationModel();
    	associationModel.setClassName("ComplaintCategoryModel");
    	associationModel.setPropertyName("relationComplaintCategoryIdModel");   		
   		associationModel.setValue(getRelationComplaintCategoryIdModel());
   		associationModelList.add(associationModel);
   		
    	return associationModelList;
    }    
}