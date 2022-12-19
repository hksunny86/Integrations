package com.inov8.microbank.server.service.integration.vo;

import java.util.ArrayList;
import java.util.List;

import com.inov8.microbank.common.model.CommissionShSharesRuleModel;

public class ProductShSharesRuleVO {

	private Long	productId;
	private String productName;
	private String supplierName;
	private Long supplierId;
	private Boolean fedApply;
	private Boolean whApply;
	private Double	FedShare;
	private Double	WhShare;
	private Long deviceTypeId;
	private Long segmentId;
	private Long stakeHolderId;
	private Long distributorId;
	private Boolean isEdit;
	Long oldSegmentId ;
	Long oldDeviceTypeId ;
	Long oldDistributorId ;
	private Long oldMnoId;
	private List<CommissionShSharesRuleModel>	commissionShSharesRuleModel = new ArrayList<CommissionShSharesRuleModel>(0);
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public Double getFedShare() {
		return FedShare;
	}
	public void setFedShare(Double fedShare) {
		FedShare = fedShare;
	}
	public Double getWhShare() {
		return WhShare;
	}
	public void setWhShare(Double whShare) {
		WhShare = whShare;
	}
	public List<CommissionShSharesRuleModel> getCommissionShSharesRuleModel() {
		return commissionShSharesRuleModel;
	}
	public void setCommissionShSharesRuleModel(
			List<CommissionShSharesRuleModel> commissionShSharesRuleModel) {
		this.commissionShSharesRuleModel = commissionShSharesRuleModel;
	}
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public Long getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(Long segmentId) {
		this.segmentId = segmentId;
	}
	public Long getStakeHolderId() {
		return stakeHolderId;
	}
	public void setStakeHolderId(Long stakeHolderId) {
		this.stakeHolderId = stakeHolderId;
	}
	public Boolean getFedApply() {
		return fedApply;
	}
	public void setFedApply(Boolean fedApply) {
		this.fedApply = fedApply;
	}
	public Boolean getWhApply() {
		return whApply;
	}
	public void setWhApply(Boolean whApply) {
		this.whApply = whApply;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public Long getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}
	public Boolean getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}
	public Long getOldSegmentId() {
		return oldSegmentId;
	}
	public void setOldSegmentId(Long oldSegmentId) {
		this.oldSegmentId = oldSegmentId;
	}
	public Long getOldDeviceTypeId() {
		return oldDeviceTypeId;
	}
	public void setOldDeviceTypeId(Long oldDeviceTypeId) {
		this.oldDeviceTypeId = oldDeviceTypeId;
	}
	public Long getOldDistributorId() {
		return oldDistributorId;
	}
	public void setOldDistributorId(Long oldDistributorId) {
		this.oldDistributorId = oldDistributorId;
	}

	public Long getOldMnoId() {
		return oldMnoId;
	}

	public void setOldMnoId(Long oldMnoId) {
		this.oldMnoId = oldMnoId;
	}
}
