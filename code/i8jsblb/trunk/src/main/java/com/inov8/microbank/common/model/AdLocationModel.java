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
@SequenceGenerator(name = "AD_LOCATION_seq", sequenceName = "AD_LOCATION_seq", allocationSize = 1)
@Table(name = "AD_LOCATION")
public class AdLocationModel extends BasePersistableModel implements Serializable {

	private Long adLocationId;
	private String adLocationName;
	private AppUserModel createdByModel;
	private AppUserModel updatedByModel;
	private Date createdOn;
	private Date updatedOn;
	private Integer version;

	@Column(name = "AD_LOCATION_ID")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AD_LOCATION_seq")
	public Long getAdLocationId() {
		return adLocationId;
	}

	public void setAdLocationId(Long LocationId) {
		this.adLocationId = LocationId;
	}

	@Override
	public void setPrimaryKey(Long aLong) {
		setAdLocationId(aLong);
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getAdLocationId();
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&adLocationId=" + getAdLocationId();
		return parameters;
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		return "adLocationId";
	}

	@Column(name = "NAME")
	public String getAdLocationName() {
		return adLocationName;
	}

	public void setAdLocationName(String adLocationName) {
		this.adLocationName = adLocationName;
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
