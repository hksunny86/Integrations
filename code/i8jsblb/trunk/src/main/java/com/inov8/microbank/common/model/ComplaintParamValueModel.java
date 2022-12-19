package com.inov8.microbank.common.model;

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

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * ComplaintParamValueModel entity.
 * @author Muhammad Omar Butt
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMPLAINT_PARAMETER_VALUE_SEQ",sequenceName = "COMPLAINT_PARAMETER_VALUE_SEQ", allocationSize=1)
@Table(name = "COMPLAINT_PARAMETER_VALUE")
public class ComplaintParamValueModel extends BasePersistableModel {

    private Long complaintParameterValueId;
    private ComplaintModel complaintIdComplaintModel;
    private Long complaintParameterId;
    private String value;

    public ComplaintParamValueModel() {
    }
    
    @Column(name = "COMPLAINT_PARAMETER_VALUE_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPLAINT_PARAMETER_VALUE_SEQ")
    public Long getComplaintParameterValueId() {
        return this.complaintParameterValueId;
    }
    
    public void setComplaintParameterValueId(Long complaintParameterValueId) {
        this.complaintParameterValueId = complaintParameterValueId;
    }

//    @Column(name="COMPLAINT_ID")
//    public Long getComplaintId() {
//        return this.complaintId;
//    }
//    
//    public void setComplaintId(Long complaintId) {
//        this.complaintId = complaintId;
//    }

    
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPLAINT_ID")    
    public ComplaintModel getRelationComplaintIdComplaintModel(){
       return complaintIdComplaintModel;
    }

    @javax.persistence.Transient
    public ComplaintModel getComplaintIdComplaintModel(){
       return getRelationComplaintIdComplaintModel();
    }

    @javax.persistence.Transient
    public void setRelationComplaintIdComplaintModel(ComplaintModel complaintModel) {
       this.complaintIdComplaintModel = complaintModel;
    }
    
    @javax.persistence.Transient
    public void setComplaintIdComplaintModel(ComplaintModel complaintModel) {
       if(null != complaintModel){
    	   setRelationComplaintIdComplaintModel((ComplaintModel)complaintModel.clone());
       }      
    }

    @javax.persistence.Transient
    public Long getComplaintId() {
       if (complaintIdComplaintModel != null) {
          return complaintIdComplaintModel.getComplaintId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setComplaintId(Long complaintId) {
       if(complaintId == null){
    	   complaintIdComplaintModel = null;
       }else{
    	   complaintIdComplaintModel = new ComplaintModel();
    	   complaintIdComplaintModel.setComplaintId(complaintId);
       }      
    }
    
    @Column(name="COMPLAINT_PARAMETER_ID")
    public Long getComplaintParameterId() {
        return this.complaintParameterId;
    }
    
    public void setComplaintParameterId(Long complaintParameterId) {
        this.complaintParameterId = complaintParameterId;
    }

    @Column(name="VALUE")
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    @javax.persistence.Transient
	public Long getPrimaryKey() {
		return complaintParameterValueId;
	}

    @javax.persistence.Transient
	public void setPrimaryKey(Long complaintParameterValueId) {
    	this.complaintParameterValueId = complaintParameterValueId;
	}

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
       String parameters = "";
       parameters += "&complaintParameterValueId=" + getComplaintParameterValueId();
       return parameters;
    }
 	/**
      * Helper method for default Sorting on Primary Keys
      */
     @javax.persistence.Transient
     public String getPrimaryKeyFieldName()
     { 
 			String primaryKeyFieldName = "complaintParameterValueId";
 			return primaryKeyFieldName;				
     }
    
}