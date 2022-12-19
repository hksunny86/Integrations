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
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;

@Entity
@Table(name="COMPLAINT_HISTORY")
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMPLAINT_HISTORY_SEQ",sequenceName = "COMPLAINT_HISTORY_SEQ", allocationSize=1)
public class ComplaintHistoryModel extends BasePersistableModel {

	private static final long serialVersionUID = 1L;
	private Long complaintHistoryId;
	private Long complaintId;
	private Long appUserId;
    private Date assignedOn;
    private String status;
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Integer displayOrder;
    private Integer versionNo;
    private Date tatEndTime;
    private String remarks;
    
	@Transient
    private ComplaintHistoryVO complaintHistoryVO = new ComplaintHistoryVO();

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPLAINT_HISTORY_SEQ")
    @Column(name="COMPLAINT_HISTORY_ID", unique=true, nullable=false, precision=10, scale=0)
    public Long getComplaintHistoryId() {
    	return this.complaintHistoryId;
    }
    
    public void setComplaintHistoryId(Long complaintHistoryId) {
        this.complaintHistoryId = complaintHistoryId;
    }
    
    @Column(name="COMPLAINT_ID")
    public Long getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(Long complaintId) {
		this.complaintId = complaintId;
	}    
    
    @Column(name="APP_USER_ID", nullable=false, precision=10, scale=0)
    public Long getAppUserId() {
        return this.appUserId;
    }
    
    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }
    
    @Column(name="ASSIGNED_ON", length=7)

    public Date getAssignedOn() {
        return this.assignedOn;
    }
    
    public void setAssignedOn(Date assignedOn) {
        this.assignedOn = assignedOn;
    }
    
    @Column(name="STATUS", length=10)

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name="CREATED_BY", precision=10, scale=0)

    public Long getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    @Column(name="UPDATED_BY", precision=10, scale=0)

    public Long getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @Column(name="CREATED_ON", length=7)

    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON", length=7)

    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Column(name="DISPLAY_ORDER", nullable=false)
    public Integer getDisplayOrder() {
        return this.displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Version
    @Column(name="VERSION_NO", nullable=false)
    public Integer getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
    
    @Column(name="TAT_END_TIME", length=7)
    public Date getTatEndTime() {
        return this.tatEndTime;
    }
    
    public void setTatEndTime(Date tatEndTime) {
        this.tatEndTime = tatEndTime;
    }

	@Override
	public void setPrimaryKey(Long primaryKey) {
		setComplaintHistoryId(primaryKey);
		
	}

	@Override
    @javax.persistence.Transient
	public Long getPrimaryKey() {
		return getComplaintHistoryId();
	}

	@Override
    @javax.persistence.Transient
	public String getPrimaryKeyParameter() {

		return (complaintHistoryId != null ? String.valueOf(complaintHistoryId) : null);
	}

	@Override
    @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {

		return "complaintHistoryId";
	}

	@Transient
	public ComplaintHistoryVO getComplaintHistoryVO() {
		return complaintHistoryVO;
	}

	public void setComplaintHistoryVO(ComplaintHistoryVO complaintHistoryVO) {
		this.complaintHistoryVO = complaintHistoryVO;
	}
   
    @Override
    public String toString() {
       return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}