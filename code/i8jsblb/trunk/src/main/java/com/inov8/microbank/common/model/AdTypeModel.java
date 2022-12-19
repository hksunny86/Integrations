package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by AtifHu on 18-Feb-16.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "AD_TYPE_seq", sequenceName = "AD_TYPE_seq", allocationSize = 1)
@Table(name = "AD_TYPE")
public class AdTypeModel extends BasePersistableModel implements Serializable {

	private Long adTypeId;
	private String adTypeName;
	private AppUserModel createdByModel;
	private AppUserModel updatedByModel;
	private Date createdOn;
	private Date updatedOn;
	private Integer version;

	@Column(name = "AD_TYPE_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AD_TYPE_seq")
	public Long getAdTypeId() {
		return adTypeId;
	}

	public void setAdTypeId(Long TypeId) {
		this.adTypeId = TypeId;
	}

	@Override
	public void setPrimaryKey(Long aLong) {
		setAdTypeId(aLong);
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getAdTypeId();
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&adTypeId=" + getAdTypeId();
		return parameters;
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		return "adTypeId";
	}

	@Column(name = "NAME")
	public String getAdTypeName() {
		return adTypeName;
	}

	public void setAdTypeName(String adTypeName) {
		this.adTypeName = adTypeName;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByModel() {
		return createdByModel;
	}

	public void setCreatedByModel(AppUserModel createdByModel) {
		this.createdByModel = createdByModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByModel() {
		return updatedByModel;
	}

	public void setUpdatedByModel(AppUserModel updatedByModel) {
		this.updatedByModel = updatedByModel;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO" , nullable = false )
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer versionNo) {
		this.version = versionNo;
	}

	@Transient
	public Long getCreatedBy() {
		Long createdByValue = null;
		if (createdByModel != null) {
			createdByValue = createdByModel.getAppUserId();
		}
		return createdByValue;
	}

	@Transient
	public Long getUpdatedBy() {
		Long updatedByValue = null;
		if (updatedByModel != null) {
			updatedByValue = updatedByModel.getAppUserId();
		}
		return updatedByValue;
	}
}