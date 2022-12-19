package com.inov8.microbank.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import com.inov8.framework.common.model.BasePersistableModel;




/**
 * HolidayModel entity.
 * @author Muhammad Omar Butt
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMPLAINT_SEQ",sequenceName = "COMPLAINT_SEQ", allocationSize=1)
@Table(name = "COMPLAINT")
public class ComplaintModel extends BasePersistableModel {

	 private static final long serialVersionUID = -3682219819924977403L;
	 
	 private Long complaintId;
     private String complaintCode;
     private ComplaintCategoryModel catIdComplaintCategoryModel;
     private ComplaintSubcategoryModel subcatIdComplaintSubcategoryModel;
     private Date expectedTat;
     private Short actualTat;
     private String remarks;
     private String transactionId;
     private AppUserModel initiatorAppUserModel;
     private String initiatorType;
     private String initiatorId;
     private String otherContactNo;
     private String complaintDescription;
     private AppUserModel updatedByAppUserModel;
     private AppUserModel createdByAppUserModel;
     private Date createdOn;
     private Date updatedOn;
     private Short versionNo;
     private Date closedOn;
     private Long closedBy;
     private String status;
     private String escalationStatus;
     private String initiatorCity;
     private String mobileNo; 
     private String initiatorName;
     private String initiatorCNIC; 

     private Collection<ComplaintParamValueModel> complaintParamValues = new ArrayList<ComplaintParamValueModel>();
     
	public ComplaintModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getComplaintId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setComplaintId(primaryKey);
    }
    
    
   	@Column(name = "COMPLAINT_ID" , nullable = false )
   	@Id
   	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPLAINT_SEQ")
    public Long getComplaintId() {
        return this.complaintId;
    }
    
    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }
    
    @Column(name="COMPLAINT_CODE")
    public String getComplaintCode() {
        return this.complaintCode;
    }
    
    public void setComplaintCode(String complaintCode) {
        this.complaintCode = complaintCode;
    }
    

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPLAINT_CATEGORY_ID")    
    public ComplaintCategoryModel getRelationCatIdComplaintCategoryModel(){
       return catIdComplaintCategoryModel;
    }
     
    @javax.persistence.Transient
    public void setRelationCatIdComplaintCategoryModel(ComplaintCategoryModel catIdComplaintCategoryModel) {
       this.catIdComplaintCategoryModel = catIdComplaintCategoryModel;
    }

    @javax.persistence.Transient
    public ComplaintCategoryModel getCatIdComplaintCategoryModel(){
       return getRelationCatIdComplaintCategoryModel();
    }
    
    @javax.persistence.Transient
    public void setCatIdComplaintCategoryModel(ComplaintCategoryModel catIdComplaintCategoryModel) {
       if(null != catIdComplaintCategoryModel)
       {
       	setRelationCatIdComplaintCategoryModel((ComplaintCategoryModel)catIdComplaintCategoryModel.clone());
       }      
    }
    
    @javax.persistence.Transient
    public Long getComplaintCategoryId() {
       if (catIdComplaintCategoryModel != null) {
          return catIdComplaintCategoryModel.getComplaintCategoryId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setComplaintCategoryId(Long complaintCategoryId) {
       if(complaintCategoryId == null)
       {      
    	   catIdComplaintCategoryModel = null;
       }
       else
       {
    	   catIdComplaintCategoryModel = new ComplaintCategoryModel();
    	   catIdComplaintCategoryModel.setComplaintCategoryId(complaintCategoryId);
       }      
    }
    
    
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPLAINT_SUBCATEGORY_ID")    
    public ComplaintSubcategoryModel getRelationSubcatIdComplaintSubcategoryModel(){
       return subcatIdComplaintSubcategoryModel;
    }
     
    @javax.persistence.Transient
    public void setRelationSubcatIdComplaintSubcategoryModel(ComplaintSubcategoryModel subcatIdComplaintSubcategoryModel) {
       this.subcatIdComplaintSubcategoryModel = subcatIdComplaintSubcategoryModel;
    }

    @javax.persistence.Transient
    public ComplaintSubcategoryModel getSubcatIdComplaintSubcategoryModel(){
       return getRelationSubcatIdComplaintSubcategoryModel();
    }
    
    @javax.persistence.Transient
    public void setSubcatIdComplaintSubcategoryModel(ComplaintSubcategoryModel subcatIdComplaintSubcategoryModel) {
       if(null != subcatIdComplaintSubcategoryModel)
       {
       	setRelationSubcatIdComplaintSubcategoryModel((ComplaintSubcategoryModel)subcatIdComplaintSubcategoryModel.clone());
       }      
    }
    
    @javax.persistence.Transient
    public Long getComplaintSubcategoryId() {
       if (subcatIdComplaintSubcategoryModel != null) {
          return subcatIdComplaintSubcategoryModel.getComplaintSubcategoryId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setComplaintSubcategoryId(Long complaintSubcategoryId) {
       if(complaintSubcategoryId == null)
       {      
    	   subcatIdComplaintSubcategoryModel = null;
       }
       else
       {
    	   subcatIdComplaintSubcategoryModel = new ComplaintSubcategoryModel();
    	   subcatIdComplaintSubcategoryModel.setComplaintSubcategoryId(complaintSubcategoryId);
       }      
    }
    
    
    @Column(name="EXPECTED_TAT")
    public Date getExpectedTat() {
        return this.expectedTat;
    }
    
    public void setExpectedTat(Date expectedTat) {
        this.expectedTat = expectedTat;
    }
    
    @Column(name="ACTUAL_TAT")
    public Short getActualTat() {
        return this.actualTat;
    }
    
    public void setActualTat(Short actualTat) {
        this.actualTat = actualTat;
    }
    
    @Column(name="REMARKS")
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    @Column(name="TRANSACTION_ID")
    public String getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    @Column(name="OTHER_CONTACT_NO")
    public String getOtherContactNo() {
        return this.otherContactNo;
    }
    
    public void setOtherContactNo(String otherContactNo) {
        this.otherContactNo = otherContactNo;
    }
    
    @Column(name="COMPLAINT_DESCRIPTION")
    public String getComplaintDescription() {
        return this.complaintDescription;
    }
    
    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
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

    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

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

    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (createdByAppUserModel != null) {
          return createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

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

    
    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Version
    @Column(name="VERSION_NO")
    public Short getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Short versionNo) {
        this.versionNo = versionNo;
    }
    
    @Column(name="CLOSED_ON")
    public Date getClosedOn() {
        return this.closedOn;
    }
    
    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }
    
    @Column(name="CLOSED_BY")
    public Long getClosedBy() {
        return this.closedBy;
    }
    
    public void setClosedBy(Long closedBy) {
        this.closedBy = closedBy;
    }
    
    @Column(name="STATUS")
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

	@Transient
	public String getPrimaryKeyFieldName() {
		return "complaintId";
	}

	@Transient
	public String getPrimaryKeyParameter() {
		return String.valueOf(complaintId);
	}

    
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "INIT_APP_USER_ID")    
    public AppUserModel getRelationInitiatorAppUserModel(){
       return initiatorAppUserModel;
    }

    @javax.persistence.Transient
    public AppUserModel getInitiatorAppUserModel(){
       return getRelationInitiatorAppUserModel();
    }

    @javax.persistence.Transient
    public void setRelationInitiatorAppUserModel(AppUserModel appUserModel) {
       this.initiatorAppUserModel = appUserModel;
    }
    
    @javax.persistence.Transient
    public void setInitiatorAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationInitiatorAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }

    @javax.persistence.Transient
    public Long getInitAppUserId() {
       if (initiatorAppUserModel != null) {
          return initiatorAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setInitAppUserId(Long appUserId) {
       if(appUserId == null)
       {      
       	initiatorAppUserModel = null;
       }
       else
       {
         initiatorAppUserModel = new AppUserModel();
       	initiatorAppUserModel.setAppUserId(appUserId);
       }      
    }
    
    public void addcomplaintParamValues(ComplaintParamValueModel complaintParamValueModel) {
   	 complaintParamValueModel.setRelationComplaintIdComplaintModel(this);
        complaintParamValues.add(complaintParamValueModel);
    }
     
    public void removeComplaintParamValues(ComplaintParamValueModel complaintParamValueModel) {      
   	 complaintParamValueModel.setRelationComplaintIdComplaintModel(null);
   	 complaintParamValues.remove(complaintParamValueModel);      
    }
     
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relationComplaintIdComplaintModel")
    @JoinColumn(name = "COMPLAINT_ID")
    public Collection<ComplaintParamValueModel> getComplaintParamValues() throws Exception {
   	 return complaintParamValues;
    }

    public void setComplaintParamValues(Collection<ComplaintParamValueModel> complaintParamValues) throws Exception {
   	 this.complaintParamValues = complaintParamValues;
    }
    
    @Column(name="INITIATOR_ID")
    public String getInitiatorId() {
        return this.initiatorId;
    }
    
    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    @Column(name="INIT_MOBILE_NO")
    public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

    @Column(name="ESCALATION_STATUS")
	public String getEscalationStatus() {
		return escalationStatus;
	}

	public void setEscalationStatus(String escalationStatus) {
		this.escalationStatus = escalationStatus;
	}

    @Column(name="INITIATOR_CITY")
	public String getInitiatorCity() {
		return initiatorCity;
	}

	public void setInitiatorCity(String initiatorCity) {
		this.initiatorCity = initiatorCity;
	}

    @Column(name="INIT_NAME")
	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

    @Column(name="INIT_CNIC")
	public String getInitiatorCNIC() {
		return initiatorCNIC;
	}

	public void setInitiatorCNIC(String initiatorCNIC) {
		this.initiatorCNIC = initiatorCNIC;
	}
    
	@Column(name="initiator_type")
	public String getInitiatorType() {
		return initiatorType;
	}

	public void setInitiatorType(String initiatorType) {
		this.initiatorType = initiatorType;
	}
}