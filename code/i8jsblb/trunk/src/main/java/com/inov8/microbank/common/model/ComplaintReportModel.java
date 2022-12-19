package com.inov8.microbank.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author Muhammad Omar Butt
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMPLAINT_REPORT_SEQ",sequenceName = "COMPLAINT_REPORT_SEQ", allocationSize=1)
@Table(name = "COMPLAINT_REPORT")
public class ComplaintReportModel extends BasePersistableModel {
	 private static final long serialVersionUID = 7255127853050697595L;
	 private Long complaintReportId;
	 private Long complaintId;
	 private String complaintCode;
	 private Long complaintCategoryId;
	 private Long complaintSubcategoryId;
	 private String complaintCategory;
	 private String complaintSubcategory;
	 private Date expectedTat;
	 private Short actualTat;
	 private String remarks;
     private String transactionId;
     private Long initAppUserId;
     private String initiatorType;
     private String initiatorId;
     private String initiatorName;
     private String mobileNo;
     private String initiatorCNIC;
     private String initiatorCity;
     private String otherContactNo;
     private String complaintDescription;
     private Long createdBy;
     private String createdByName;
     private Date createdOn;
     private Long updatedBy;
     private Date updatedOn;
     private Short versionNo;
     private Date closedOn;
     private Long closedBy;
     private String closedByName;
     private Long currentAssigneeId;
     private String currentAssigneeName;
 	 @Transient
     private Long levelAssigneeId;
	 @Transient
     private String levelAssigneeName;
	 @Transient
     private Date levelTATEndTime;
     private Long level0AssigneeId;
     private String level0AssigneeName;
     private Date level0TATEndTime;
     private Long level1AssigneeId;
     private String level1AssigneeName;
     private Date level1TATEndTime;
     private Long level2AssigneeId;
     private String level2AssigneeName;
     private Date level2TATEndTime;
     private Long level3AssigneeId;
     private String level3AssigneeName;
     private Date level3TATEndTime;
     private String status;
     private String escalationStatus;
     //transient
     private String consumerNo;
     
     private Date loggedFrom;
     private Date loggedTo;
     
     @Transient
     private Integer displayOrder;

     @Transient
     public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public ComplaintReportModel() {
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getComplaintReportId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setComplaintReportId(primaryKey);
    }
    
    
 	@Id
   	@Column(name = "COMPLAINT_REPORT_ID")
   	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPLAINT_REPORT_SEQ")
    public Long getComplaintReportId() {
        return this.complaintReportId;
    }
    
    public void setComplaintReportId(Long complaintReportId) {
        this.complaintReportId = complaintReportId;
    }

   	@Column(name = "COMPLAINT_ID")
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
    

    @Column(name="COMPLAINT_CATEGORY_ID")
    public Long getComplaintCategoryId() {
          return complaintCategoryId;
    }

    public void setComplaintCategoryId(Long complaintCategoryId) {
    	this.complaintCategoryId = complaintCategoryId;
    }
    
    
    @Column(name="COMPLAINT_SUBCATEGORY_ID")
    public Long getComplaintSubcategoryId() {
       return complaintSubcategoryId;
    }

    public void setComplaintSubcategoryId(Long complaintSubcategoryId) {
       this.complaintSubcategoryId = complaintSubcategoryId;
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
    
    @Column(name = "UPDATED_BY")    
    public Long getUpdatedBy() {
    	return updatedBy;
    }

    public void setUpdatedBy(Long appUserId) {
       	updatedBy = appUserId;
    }

    @Column(name = "CREATED_BY")    
    public Long getCreatedBy() {
    	return createdBy;
    }

    public void setCreatedBy(Long appUserId) {
       	createdBy = appUserId;
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
		return "complaintReportId";
	}

	@Transient
	public String getPrimaryKeyParameter() {
		return String.valueOf(complaintReportId);
	}

    
    @Column(name = "INIT_APP_USER_ID")    
    public Long getInitAppUserId() {
    	return initAppUserId;
    }

    public void setInitAppUserId(Long appUserId) {
    	this.initAppUserId = appUserId;
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

    @Column(name="COMPLAINT_CATEGORY")
	public String getComplaintCategory() {
		return complaintCategory;
	}

	public void setComplaintCategory(String complaintCategory) {
		this.complaintCategory = complaintCategory;
	}

    @Column(name="COMPLAINT_SUBCATEGORY")
	public String getComplaintSubcategory() {
		return complaintSubcategory;
	}

	public void setComplaintSubcategory(String complaintSubcategory) {
		this.complaintSubcategory = complaintSubcategory;
	}

    @Column(name="INIT_CNIC")
	public String getInitiatorCNIC() {
		return initiatorCNIC;
	}

	public void setInitiatorCNIC(String initiatorCNIC) {
		this.initiatorCNIC = initiatorCNIC;
	}

    @Column(name="INIT_NAME")
	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

    @Column(name="CREATED_BY_NAME")
	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	@Column(name="CLOSED_BY_NAME")
	public String getClosedByName() {
		return closedByName;
	}

	public void setClosedByName(String closedByName) {
		this.closedByName = closedByName;
	}

	@Column(name="CURRENT_ASSIGNEE_ID")
	public Long getCurrentAssigneeId() {
		return currentAssigneeId;
	}

	public void setCurrentAssigneeId(Long currentAssigneeId) {
		this.currentAssigneeId = currentAssigneeId;
	}

	@Column(name="CURRENT_ASSIGNEE_NAME")
	public String getCurrentAssigneeName() {
		return currentAssigneeName;
	}

	public void setCurrentAssigneeName(String currentAssigneeName) {
		this.currentAssigneeName = currentAssigneeName;
	}
	
	@Transient
    public Long getLevelAssigneeId() {
		return levelAssigneeId;
	}

	public void setLevelAssigneeId(Long levelAssigneeId) {
		this.levelAssigneeId = levelAssigneeId;
	}

	@Transient
	public String getLevelAssigneeName() {
		return levelAssigneeName;
	}

	public void setLevelAssigneeName(String levelAssigneeName) {
		this.levelAssigneeName = levelAssigneeName;
	}

	@Transient
	public Date getLevelTATEndTime() {
		return levelTATEndTime;
	}

	public void setLevelTATEndTime(Date levelTATEndTime) {
		this.levelTATEndTime = levelTATEndTime;
	}
	
	@Column(name="LEVEL0_ASSIGNEE_ID")
	public Long getLevel0AssigneeId() {
		return level0AssigneeId;
	}

	public void setLevel0AssigneeId(Long level0AssigneeId) {
		this.level0AssigneeId = level0AssigneeId;
	}

	@Column(name="LEVEL0_ASSIGNEE_NAME")
	public String getLevel0AssigneeName() {
		return level0AssigneeName;
	}

	public void setLevel0AssigneeName(String level0AssigneeName) {
		this.level0AssigneeName = level0AssigneeName;
	}

	@Column(name="LEVEL0_ESC_ON")
	public Date getLevel0TATEndTime() {
		return level0TATEndTime;
	}

	public void setLevel0TATEndTime(Date level0tatEndTime) {
		level0TATEndTime = level0tatEndTime;
	}

	@Column(name="LEVEL1_ASSIGNEE_ID")
	public Long getLevel1AssigneeId() {
		return level1AssigneeId;
	}

	public void setLevel1AssigneeId(Long level1AssigneeId) {
		this.level1AssigneeId = level1AssigneeId;
	}

	@Column(name="LEVEL1_ASSIGNEE_NAME")
	public String getLevel1AssigneeName() {
		return level1AssigneeName;
	}

	public void setLevel1AssigneeName(String level1AssigneeName) {
		this.level1AssigneeName = level1AssigneeName;
	}

	@Column(name="LEVEL1_ESC_ON")
	public Date getLevel1TATEndTime() {
		return level1TATEndTime;
	}

	public void setLevel1TATEndTime(Date level1tatEndTime) {
		level1TATEndTime = level1tatEndTime;
	}

	@Column(name="LEVEL2_ASSIGNEE_ID")
	public Long getLevel2AssigneeId() {
		return level2AssigneeId;
	}

	public void setLevel2AssigneeId(Long level2AssigneeId) {
		this.level2AssigneeId = level2AssigneeId;
	}

	@Column(name="LEVEL2_ASSIGNEE_NAME")
	public String getLevel2AssigneeName() {
		return level2AssigneeName;
	}

	public void setLevel2AssigneeName(String level2AssigneeName) {
		this.level2AssigneeName = level2AssigneeName;
	}

	@Column(name="LEVEL2_ESC_ON")
	public Date getLevel2TATEndTime() {
		return level2TATEndTime;
	}

	public void setLevel2TATEndTime(Date level2tatEndTime) {
		level2TATEndTime = level2tatEndTime;
	}

	@Column(name="LEVEL3_ASSIGNEE_ID")
	public Long getLevel3AssigneeId() {
		return level3AssigneeId;
	}

	public void setLevel3AssigneeId(Long level3AssigneeId) {
		this.level3AssigneeId = level3AssigneeId;
	}

	@Column(name="LEVEL3_ASSIGNEE_NAME")
	public String getLevel3AssigneeName() {
		return level3AssigneeName;
	}

	public void setLevel3AssigneeName(String level3AssigneeName) {
		this.level3AssigneeName = level3AssigneeName;
	}

	@Column(name="LEVEL3_ESC_ON")
	public Date getLevel3TATEndTime() {
		return level3TATEndTime;
	}

	public void setLevel3TATEndTime(Date level3tatEndTime) {
		level3TATEndTime = level3tatEndTime;
	}
	
	@Transient
	public Date getLoggedFrom() {
		return loggedFrom;
	}

	public void setLoggedFrom(Date loggedFrom) {
		this.loggedFrom = loggedFrom;
	}

	@Transient
	public Date getLoggedTo() {
		return loggedTo;
	}

	public void setLoggedTo(Date loggedTo) {
		this.loggedTo = loggedTo;
	}

	@Column(name="initiator_type")
	public String getInitiatorType() {
		return initiatorType;
	}

	public void setInitiatorType(String initiatorType) {
		this.initiatorType = initiatorType;
	}

	@Transient
	public String getConsumerNo() {
		return consumerNo;
	}

	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}

}