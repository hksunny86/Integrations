package com.inov8.microbank.common.model.agenthierarchy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorLevelModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DISTRIBUTOR_COMM_SHARE_DTL_seq", sequenceName = "DISTRIBUTOR_COMM_SHARE_DTL_seq", allocationSize = 1)
@Table(name = "DISTRIBUTOR_COMM_SHARE_DTL")
public class DistributorCommissionShareDetailModel extends BasePersistableModel
		implements Serializable {

	private Long distributorCommShareDetailId;

	private DistributorCommissionShareModel distributorCommissionShareIdModel;
	private DistributorLevelModel parentDistributorLevelIdModel;
	private Double commissionShare;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

	@Column(name = "DISTRIBUTOR_COMM_SHARE_DTL_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISTRIBUTOR_COMM_SHARE_DTL_seq")
	public Long getDistributorCommShareDetailId() {
		return distributorCommShareDetailId;
	}

	public void setDistributorCommShareDetailId(
			Long distributorCommShareDetailId) {
		this.distributorCommShareDetailId = distributorCommShareDetailId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_DISTRIBUTOR_LEVEL_ID")
	public DistributorLevelModel getParentDistributorLevelIdModel() {
		return parentDistributorLevelIdModel;
	}

	public void setParentDistributorLevelIdModel(
			DistributorLevelModel parentDistributorLevelIdModel) {
		this.parentDistributorLevelIdModel = parentDistributorLevelIdModel;
	}

	@Column(name = "COMMISSION_SHARE", nullable = false)
	public Double getCommissionShare() {
		return commissionShare;
	}

	public void setCommissionShare(Double commissionShare) {
		this.commissionShare = commissionShare;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
		this.updatedByAppUserModel = updatedByAppUserModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@Column(name = "CREATED_ON", nullable = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON", nullable = false)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_COMM_SHARE_ID")
	public DistributorCommissionShareModel getDistributorCommissionShareIdModel() {
		return distributorCommissionShareIdModel;
	}

	public void setDistributorCommissionShareIdModel(
			DistributorCommissionShareModel distributorCommissionShareIdModel) {
		this.distributorCommissionShareIdModel = distributorCommissionShareIdModel;
	}

	@Transient
	public Long getPrimaryKey() {
		return getDistributorCommShareDetailId();
	}

	@Transient
	public void setPrimaryKey(Long arg0) {
		setDistributorCommShareDetailId(arg0);
	}

	@Transient
	public String getPrimaryKeyFieldName() {
		return "distributorCommShareDetailId";
	}

	@Transient
	public String getPrimaryKeyParameter() {
		return "&distributorCommShareDetailId=" + distributorCommShareDetailId;
	}

	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();
		associationModel.setClassName("DistributorCommissionShareModel");
		associationModel.setPropertyName("distributorCommissionShareIdModel");
		associationModel.setValue(getDistributorCommissionShareIdModel());
		associationModelList.add(associationModel);

    	associationModel = new AssociationModel();
    	associationModel.setClassName("DistributorLevelModel");
    	associationModel.setPropertyName("parentDistributorLevelId");   		
   		associationModel.setValue(getParentDistributorLevelIdModel());
   		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Transient
	public Long getDistributorLevelId() {
		if (parentDistributorLevelIdModel == null) {
			return null;
		} else {
			return parentDistributorLevelIdModel .getDistributorLevelId();
		}
	}

	@Transient
	public void setDistributorLevelId(Long distributorLevelId) {
		if (parentDistributorLevelIdModel  == null) {
			parentDistributorLevelIdModel = null;
		} else {
			parentDistributorLevelIdModel  = new DistributorLevelModel();
			parentDistributorLevelIdModel.setDistributorLevelId(distributorLevelId);
		}
	}

}
