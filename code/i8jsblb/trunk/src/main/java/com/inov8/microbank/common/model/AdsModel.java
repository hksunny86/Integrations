package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.RetailerModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AtifHu on 18-Feb-16.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@SequenceGenerator(name = "ADS_SEQ", sequenceName = "ADS_SEQ", allocationSize = 1)
@Table(name = "ADS")
public class AdsModel extends BasePersistableModel implements Serializable {

	private Long adId;
	private AdTypeModel adTypeModel;
	private AdLocationModel adLocationModel;
	private String imageName;
	private String locationPath;
	private AppUserModel createdByModel;
	private AppUserModel updatedByModel;
	private BankModel bankModel;
	private RetailerModel retailerModel;
	private Integer version;
	private Boolean active;
	private Date createdOn;
	private Date updatedOn;

	@Override
	public void setPrimaryKey(Long aLong) {
		setAdId(aLong);
	}

	@Override
	@Transient
	public Long getPrimaryKey() {
		return getAdId();
	}

	@Override
	@Transient
	public String getPrimaryKeyParameter() {
		return "&adId=" + getAdId();
	}

	@Override
	@Transient
	public String getPrimaryKeyFieldName() {
		return "adId";
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADS_SEQ")
	@Column(name = "ADS_ID")
	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	@Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();
		associationModel.setClassName("RetailerModel");
		associationModel.setPropertyName("retailerModel");
		associationModel.setValue(getRetailerModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("BankModel");
		associationModel.setPropertyName("bankModel");
		associationModel.setValue(getBankModel());
		associationModelList.add(associationModel);


		associationModel = new AssociationModel();
		associationModel.setClassName("AdLocationModel");
		associationModel.setPropertyName("adLocationModel");
		associationModel.setValue(getAdLocationModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AdTypeModel");
		associationModel.setPropertyName("adTypeModel");
		associationModel.setValue(getAdTypeModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}


	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "AD_LOCATION_ID")
	public AdLocationModel getAdLocationModel() {
		return adLocationModel;
	}

	public void setAdLocationModel(AdLocationModel adLocationModel) {
		this.adLocationModel = adLocationModel;
	}

	@Column(name = "IMAGE_NAME")
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Column(name = "LOCATION")
	public String getLocationPath() {
		return locationPath;
	}

	public void setLocationPath(String locationPath) {
		this.locationPath = locationPath;
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

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "IS_ACTIVE")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	@Transient
	public Long getAdLocationId() {
		if (adLocationModel != null)
			return adLocationModel.getAdLocationId();
		return null;
	}

	public void setAdLocationId(Long adTypeId) {
		if (adTypeId == null) {
			adLocationModel = null;
		} else {
			adLocationModel = new AdLocationModel();
			adLocationModel.setAdLocationId(adTypeId);
		}
	}

	@Transient
	public Long getCreatedBy() {
		if (createdByModel != null)
			return createdByModel.getAppUserId();
		return null;
	}

	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			createdByModel = null;
		} else {
			createdByModel = new AppUserModel();
			createdByModel.setAppUserId(appUserId);
		}
	}

	@Transient
	public Long getUpdatedBy() {
		if (updatedByModel != null)
			return updatedByModel.getAppUserId();
		return null;
	}

	public void setUpdateBy(Long appUserId) {
		if (appUserId == null) {
			updatedByModel = null;
		} else {
			updatedByModel = new AppUserModel();
			updatedByModel.setAppUserId(appUserId);
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "BANK_ID")
	public BankModel getBankModel() {
		return bankModel;
	}

	public void setBankModel(BankModel bankModel) {
		this.bankModel = bankModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "RETAILER_ID")
	public RetailerModel getRetailerModel() {
		return retailerModel;
	}

	public void setRetailerModel(RetailerModel retailerModel) {
		this.retailerModel = retailerModel;
	}

	@Transient
	public Long getBankId() {
		if (bankModel != null)
			return bankModel.getBankId();
		return null;
	}

	public void setBankId(Long bankId) {
		if (bankId == null) {
			bankModel = null;
		} else {
			bankModel = new BankModel();
			bankModel.setBankId(bankId);
		}
	}

	@Transient
	public Long getRetailerId() {
		if (retailerModel != null)
			return retailerModel.getRetailerId();
		return null;
	}

	public void setRetailerId(Long retailerId) {
		if (retailerId == null) {
			retailerModel = null;
		} else {
			retailerModel = new RetailerModel();
			retailerModel.setRetailerId(retailerId);
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "AD_TYPE_ID")
	public AdTypeModel getAdTypeModel() {
		return adTypeModel;
	}

	public void setAdTypeModel(AdTypeModel adTypeModel) {
		this.adTypeModel = adTypeModel;
	}

	@Transient
	public Long getAdTypeId() {
		if (adTypeModel != null)
			return adTypeModel.getAdTypeId();
		return null;
	}

	public void setAdTypeId(Long adTypeId) {
		if (adTypeId == null) {
			adTypeModel = null;
		} else {
			adTypeModel = new AdTypeModel();
			adTypeModel.setAdTypeId(adTypeId);
		}
	}

}