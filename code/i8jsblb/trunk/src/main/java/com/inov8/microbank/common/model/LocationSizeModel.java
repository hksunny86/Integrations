package com.inov8.microbank.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author Rizwan Munir
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "LOCATION_SIZE")
public class LocationSizeModel extends BasePersistableModel {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3337626260301903622L;
	private Long				locationSizeId;
	private String				name;
	private AppUserModel		createdByAppUserModel;
	private AppUserModel		updatedByAppUserModel;
	private Date				createdOn;
	private Date				updatedOn;
	private Integer				versionNo;
	private Boolean				isActive;

	public LocationSizeModel() {

	}

	@Column(name = "LOCATION_SIZE_ID", nullable = false)
	@Id
	public Long getLocationSizeId() {
		return locationSizeId;
	}

	@javax.persistence.Transient
	public void setLocationSizeId(Long locationSizeId) {
		if (locationSizeId != null) {
			this.locationSizeId = locationSizeId;
		}
	}

	/**
	 * Return the primary key.
	 * @return Long with the primary key.
	 */
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return this.getLocationSizeId();
	}

	/**
	 * Set the primary key.
	 * @param primaryKey the primary key
	 */
	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		this.setLocationSizeId(primaryKey);
	}

	@Column(name = "NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel() {
		return this.updatedByAppUserModel;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 */
	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel() {
		return this.getRelationUpdatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			this.setRelationUpdatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return this.createdByAppUserModel;
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 */
	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return this.getRelationCreatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * @param appUserModel a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			this.setRelationCreatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getUpdatedBy() {
		if (this.updatedByAppUserModel != null) {
			return this.updatedByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if (appUserId == null) {
			this.updatedByAppUserModel = null;
		} else {
			this.updatedByAppUserModel = new AppUserModel();
			this.updatedByAppUserModel.setAppUserId(appUserId);
		}
	}

	@javax.persistence.Transient
	public Long getCreatedBy() {
		if (this.createdByAppUserModel != null) {
			return this.createdByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			this.createdByAppUserModel = null;
		} else {
			this.createdByAppUserModel = new AppUserModel();
			this.createdByAppUserModel.setAppUserId(appUserId);
		}
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO")
	public Integer getVersionNo() {
		return this.versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@Column(name = "IS_ACTIVE")
	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&locationSizeId=" + this.getLocationSizeId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "locationSizeId";
		return primaryKeyFieldName;
	}

	/**
	 * Helper method for Complex Example Queries
	 */
	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(this.getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(this.getRelationUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

}