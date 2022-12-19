package com.inov8.microbank.common.model.portal.complaint;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMPLAINT_HISTORY_VIEW")
public class ComplaintHistoryViewModel extends BasePersistableModel {
	 private Long complaintId;
     private String complaintCode;
     private Long complaintCategoryId;
     private String complaintCategoryName;
     private Long complaintSubcategoryId;
     private String complaintSubcategoryName;
     private Date expectedTat;
     private Short actualTat;
     private String transactionId;
     private Long initiatorAppUserId;
     private String initiatorType;
     private String initiatorId;
	 private Long assigneeAppUserId;
     private String assigneeName;
     private String cnic;
     private String mobileNo;
     private Long createdBy;
     private Date createdOn;
     private Date closedOn;
     private Long closedBy;
     private String status;
     private String escalationStatus;
     private String historyStatus;
     private Date loggedFrom;
     private Date loggedTo;
     private Boolean isLatest;
     
   public ComplaintHistoryViewModel() {
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

   @Id 
   @Column(name = "COMPLAINT_ID" , nullable = false )
   public Long getComplaintId() {
      return complaintId;
   }

   public void setComplaintId(Long complaintId) {
      this.complaintId = complaintId;
   }


   @Column(name = "CREATED_BY")
   public Long getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(Long createdBy) {
      this.createdBy = createdBy;
   }

   @Column(name = "CREATED_ON")
   public Date getCreatedOn() {
      return createdOn;
   }

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getComplaintId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&complaintId=" + getComplaintId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "complaintId";
			return primaryKeyFieldName;				
    }

    @Column(name = "COMPLAINT_CODE")
	public String getComplaintCode() {
		return complaintCode;
	}

	public void setComplaintCode(String complaintCode) {
		this.complaintCode = complaintCode;
	}

	@Column(name = "COMPLAINT_CATEGORY_ID")
	public Long getComplaintCategoryId() {
		return complaintCategoryId;
	}

	public void setComplaintCategoryId(Long complaintCategoryId) {
		this.complaintCategoryId = complaintCategoryId;
	}

	@Column(name = "Complaint_Category")
	public String getComplaintCategoryName() {
		return complaintCategoryName;
	}

	public void setComplaintCategoryName(String complaintCategoryName) {
		this.complaintCategoryName = complaintCategoryName;
	}

	@Column(name = "COMPLAINT_SUBCATEGORY_ID")
	public Long getComplaintSubcategoryId() {
		return complaintSubcategoryId;
	}

	public void setComplaintSubcategoryId(Long complaintSubcategoryId) {
		this.complaintSubcategoryId = complaintSubcategoryId;
	}

	@Column(name = "complaint_subcategory")
	public String getComplaintSubcategoryName() {
		return complaintSubcategoryName;
	}

	public void setComplaintSubcategoryName(String complaintSubcategoryName) {
		this.complaintSubcategoryName = complaintSubcategoryName;
	}

	@Column(name = "EXPECTED_TAT")
	public Date getExpectedTat() {
		return expectedTat;
	}

	public void setExpectedTat(Date expectedTat) {
		this.expectedTat = expectedTat;
	}

	@Column(name = "ACTUAL_TAT")
	public Short getActualTat() {
		return actualTat;
	}

	public void setActualTat(Short actualTat) {
		this.actualTat = actualTat;
	}

//	@Column(name = "REMARKS")
//	public String getRemarks() {
//		return remarks;
//	}
//
//	public void setRemarks(String remarks) {
//		this.remarks = remarks;
//	}

//	@Column(name = "TRANSACTION_ID")
//	public Long getTransactionId() {
//		return transactionId;
//	}
//
//	public void setTransactionId(Long transactionId) {
//		this.transactionId = transactionId;
//	}

	@Column(name = "INIT_APP_USER_ID")
	public Long getInitiatorAppUserId() {
		return initiatorAppUserId;
	}

	public void setInitiatorAppUserId(Long initiatorAppUserId) {
		this.initiatorAppUserId = initiatorAppUserId;
	}

//	@Column(name = "INITIATOR_NAME")
//	public String getInitiatorName() {
//		return initiatorName;
//	}
//
//	public void setInitiatorName(String initiatorName) {
//		this.initiatorName = initiatorName;
//	}

	@Column(name = "INITIATOR_ID")
	public String getInitiatorId() {
		return initiatorId;
	}

	public void setInitiatorId(String initiatorId) {
		this.initiatorId = initiatorId;
	}

	@Column(name = "Assigned_To_id")
	public Long getAssigneeAppUserId() {
		return assigneeAppUserId;
	}

	public void setAssigneeAppUserId(Long assigneeAppUserId) {
		this.assigneeAppUserId = assigneeAppUserId;
	}

	@Column(name = "Assigned_To_Name")
	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	@Column(name = "INIT_CNIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	@Column(name = "INIT_Mobile_No")
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name = "CLOSED_ON")
	public Date getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(Date closedOn) {
		this.closedOn = closedOn;
	}

	@Column(name = "CLOSED_BY")
	public Long getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(Long closedBy) {
		this.closedBy = closedBy;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "HISTORY_STATUS")
	public String getHistoryStatus() {
		return historyStatus;
	}

	public void setHistoryStatus(String historyStatus) {
		this.historyStatus = historyStatus;
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
	
	@Column(name = "ESCALATION_STATUS")
	public String getEscalationStatus() {
		return escalationStatus;
	}

	public void setEscalationStatus(String escalationStatus) {
		this.escalationStatus = escalationStatus;
	}

	@Column(name = "IS_LATEST")
	public Boolean getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(Boolean isLatest) {
		this.isLatest = isLatest;
	}       
	
	@Column(name="initiator_type")
	public String getInitiatorType() {
		return initiatorType;
	}

	public void setInitiatorType(String initiatorType) {
		this.initiatorType = initiatorType;
	}

    @Column(name="TRANSACTION_ID")
    public String getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}