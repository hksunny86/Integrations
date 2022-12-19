package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "FAILED_SMS")
@javax.persistence.SequenceGenerator(name = "FAILED_SMS_SEQ",sequenceName = "FAILED_SMS_SEQ", allocationSize=1)
public class FailedSmsModel extends BasePersistableModel implements Serializable {
	
	private static final long serialVersionUID = -1L;
	private Long failedSmsId;
	private String mobileNumber;
	private String smsText;
	private Date createdOn;

   public FailedSmsModel() {
   }
   
   public FailedSmsModel(Long id, String mobile, String text, Date createdOn) {
	   this.failedSmsId = id;
	   this.mobileNumber = mobile;
	   this.smsText = text;
	   this.createdOn = createdOn;
   }   

   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getFailedSmsId();
    }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setFailedSmsId(primaryKey);
    }

   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FAILED_SMS_SEQ")
   @Column(name = "FAILED_SMS_ID" , nullable = false )
	public Long getFailedSmsId() {
		return failedSmsId;
	}

	public void setFailedSmsId(Long failedSmsId) {
		this.failedSmsId = failedSmsId;
	}   

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getFailedSmsId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&failedSmsId=" + getFailedSmsId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "failedSmsId";
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
    	
    	    	
    	return associationModelList;
    }
	
    @Column(name = "MOBILE_NUMBER")
    public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	@Column(name = "SMS_TEXT")
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
    
	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
    
}
