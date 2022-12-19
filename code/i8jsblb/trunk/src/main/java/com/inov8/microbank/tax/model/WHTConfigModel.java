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
import com.inov8.microbank.tax.vo.WHTConfigVo;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "WHT_CONFIG_seq", sequenceName = "WHT_CONFIG_seq", allocationSize = 1)
@Table(name = "WHT_CONFIG")
public class WHTConfigModel extends BasePersistableModel {

	private static final long serialVersionUID = 1L;
	private Long whtConfigId;
	private String title;
	private Double filerRate;
	private Double nonFilerRate;
	private Double thresholdLimit;
	private Boolean active;

	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;

	@Id
	@Column(name = "WHT_CONFIG_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHT_CONFIG_seq")
	public Long getWhtConfigId() {
		return whtConfigId;
	}

	public void setWhtConfigId(Long whtConfigId) {
		this.whtConfigId = whtConfigId;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "FILER_RATE")
	public Double getFilerRate() {
		return filerRate;
	}

	public void setFilerRate(Double filerRate) {
		this.filerRate = filerRate;
	}

	@Column(name = "THRESHOLD_LIMIT")
	public Double getThresholdLimit() {
		return thresholdLimit;
	}

	public void setThresholdLimit(Double thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	@Column(name = "NON_FILER_RATE")
	public Double getNonFilerRate() {
		return nonFilerRate;
	}

	public void setNonFilerRate(Double nonFilerRate) {
		this.nonFilerRate = nonFilerRate;
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

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getWhtConfigId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName() {
		return "whtConfigId";
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter() {
		return "&whtConfigId=" + getPrimaryKey();
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		setWhtConfigId(arg0);
	}

}
