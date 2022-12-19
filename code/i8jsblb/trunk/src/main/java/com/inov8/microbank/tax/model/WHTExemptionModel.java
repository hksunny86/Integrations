package com.inov8.microbank.tax.model;

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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "WHT_EXEMPTION_seq", sequenceName = "WHT_EXEMPTION_seq", allocationSize = 1)
@Table(name = "WHT_EXEMPTION")
public class WHTExemptionModel extends BasePersistableModel {

	private static final long serialVersionUID = 1L;
	private Long whtExemptionId;
	private Date startDate;
	private Date endDate;
	private AppUserModel appUserModel;
	private Boolean active;

	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;

	private Long appUserId;
	private Long userId;
	private String agentName;
	private String agentCnic;

	
	@Id
	@Column(name = "WHT_EXEMPTION_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHT_EXEMPTION_seq")
	public Long getWhtExemptionId() {
		return whtExemptionId;
	}

	public void setWhtExemptionId(Long whtExemptionId) {
		this.whtExemptionId = whtExemptionId;
	}

	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "APP_USER_ID")
	public AppUserModel getAppUserModel() {
		return appUserModel;
	}

	public void setAppUserModel(AppUserModel appUserModel) {
		this.appUserModel = appUserModel;
	}

	@Column(name = "IS_ACTIVE", nullable = false)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
		this.updatedByAppUserModel = updatedByAppUserModel;
	}

	@Column(name = "UPDATED_ON", nullable = false)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "CREATED_ON", nullable = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getWhtExemptionId();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		return "whtExemptionId";
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		return "&whtExemptionId=" + getPrimaryKey();
	}

	@javax.persistence.Transient
	public void setPrimaryKey(Long arg0) {
		setWhtExemptionId(arg0);
	}

	@javax.persistence.Transient
	public Long getAppUserId()
	{
		if(appUserModel!=null)
			return appUserModel.getAppUserId();
		else
		{
			return null;
		}
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
		if(appUserModel==null)
		{
			appUserModel = new AppUserModel();
			appUserModel.setAppUserId(appUserId);
		}
	}

	@javax.persistence.Transient
	public String getAgentName() {
		if(appUserModel!=null)
		{
			return appUserModel.getFullName();
		}
		else
			return null;
	}


	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	@javax.persistence.Transient
	public String getAgentCnic() {
		if(appUserModel!=null)
		{
			return appUserModel.getNic();
		}
		else
			return null;
	}

	public void setAgentCnic(String agentCnic) {
		this.agentCnic = agentCnic;
	}

	@javax.persistence.Transient
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
