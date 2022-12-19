package com.inov8.microbank.server.service.integration.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.list.LazyList;

import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareDetailModel;

public class DistributorCommShareVO implements Serializable {

	private Long distributorCommShareId;
	private Long distributorId;
	private Long regionId;
	private Long productId;
	private Long currentLevelId;
	private Double commissionShare;

	
	private Collection<DistributorCommissionShareDetailModel> distCommShareDtlModelList = LazyList
			.decorate(new ArrayList<DistributorCommissionShareDetailModel>(), new Factory() {
				public Object create() {
					return new DistributorCommissionShareDetailModel();
				}
			});

	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCurrentLevelId() {
		return currentLevelId;
	}

	public void setCurrentLevelId(Long currentLevelId) {
		this.currentLevelId = currentLevelId;
	}

	public Collection<DistributorCommissionShareDetailModel> getDistCommShareDtlModelList() {
		return distCommShareDtlModelList;
	}

	public void setDistCommShareDtlModelList(
			Collection<DistributorCommissionShareDetailModel> distCommShareDtlModelList) {
		this.distCommShareDtlModelList = distCommShareDtlModelList;
	}

	public Double getCommissionShare() {
		return commissionShare;
	}

	public void setCommissionShare(Double commissionShare) {
		this.commissionShare = commissionShare;
	}

	public Long getDistributorCommShareId() {
		return distributorCommShareId;
	}

	public void setDistributorCommShareId(Long distributorCommShareId) {
		this.distributorCommShareId = distributorCommShareId;
	}
}
