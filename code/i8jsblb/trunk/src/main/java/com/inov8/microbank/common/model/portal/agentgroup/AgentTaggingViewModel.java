package com.inov8.microbank.common.model.portal.agentgroup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "AGENT_GROUP_TAGGING_VIEW")
public class AgentTaggingViewModel extends BasePersistableModel {

    private static final long serialVersionUID = -2914631301844610140L;
	private Long pk;
	private String mobileNumber;
	private String groupTitle;
	private String parrentId;
	private String businessName;
	private Long appUserId;
	private String agentName;
	private String cnic;
	private Boolean status;	
	
	private Date startDate;
	private Date endDate;

	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}

	@javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	@Column(name="APP_USER_ID")
	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	@Column(name="FULL_NAME")
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@Column(name="NIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	@Column(name="STATUS")
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	@Column(name="MOBILE_NO")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	@Column(name="GROUP_TITLE")
	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	@Column(name="PARENT_ID")
	public String getParrentId() {
		return parrentId;
	}

	public void setParrentId(String parrentId) {
		this.parrentId = parrentId;
	}
	@Column(name="BUSINESS_NAME")
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
    	setPk(primaryKey);
    }

    @Id
    @Column(name="AGENT_GROUP_TAGGING_ID")
    public Long getPk()
    {
        return pk;
    }

    public void setPk( Long pk )
    {
        this.pk = pk;
    }

	@Transient
	public String getPrimaryKeyFieldName() {
		return "pk";
	}

	@Transient
	public String getPrimaryKeyParameter() {
	    return "&pk=" + getPk();
	}

 }