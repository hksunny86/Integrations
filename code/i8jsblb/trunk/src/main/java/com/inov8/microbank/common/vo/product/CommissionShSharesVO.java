package com.inov8.microbank.common.vo.product;

import java.io.Serializable;
import java.util.Date;

public class CommissionShSharesVO implements Serializable {

    private Long productId;
    private Long commissionStakeHolderId;
    private Long segmentId;
    private Long deviceTypeId;
    private Long distributorId;
    private Long mnoId;
    private Long productShSharesId;
    private Long createdByAppUserId;
    private Long updatedByAppUserId;
    private Long commissionShSharesId;
    private Double commissionShare;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;
    private Long isWhtApplicable;
    private Long isDeleted;
    private CommissionStakeholderVO commissionStakeholderVO;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCommissionStakeHolderId() {
        return commissionStakeHolderId;
    }

    public void setCommissionStakeHolderId(Long commissionStakeHolderId) {
        this.commissionStakeHolderId = commissionStakeHolderId;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    public Long getMnoId() {
        return mnoId;
    }

    public void setMnoId(Long mnoId) {
        this.mnoId = mnoId;
    }

    public Long getCommissionShSharesId() {
        return commissionShSharesId;
    }

    public void setCommissionShSharesId(Long commissionShSharesId) {
        this.commissionShSharesId = commissionShSharesId;
    }

    public Double getCommissionShare() {
        return commissionShare;
    }

    public void setCommissionShare(Double commissionShare) {
        this.commissionShare = commissionShare;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public Long getWhtApplicable() {
        return isWhtApplicable;
    }

    public void setWhtApplicable(Long whtApplicable) {
        isWhtApplicable = whtApplicable;
    }

    public Long getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Long deleted) {
        isDeleted = deleted;
    }

    public Long getCreatedByAppUserId() {
        return createdByAppUserId;
    }

    public void setCreatedByAppUserId(Long createdByAppUserId) {
        this.createdByAppUserId = createdByAppUserId;
    }

    public Long getUpdatedByAppUserId() {
        return updatedByAppUserId;
    }

    public void setUpdatedByAppUserId(Long updatedByAppUserId) {
        this.updatedByAppUserId = updatedByAppUserId;
    }

    public Long getProductShSharesId() {
        return productShSharesId;
    }

    public void setProductShSharesId(Long productShSharesId) {
        this.productShSharesId = productShSharesId;
    }

    public CommissionStakeholderVO getCommissionStakeholderVO() {
        return commissionStakeholderVO;
    }

    public void setCommissionStakeholderVO(CommissionStakeholderVO commissionStakeholderVO) {
        this.commissionStakeholderVO = commissionStakeholderVO;
    }
}
