package com.inov8.microbank.common.vo.product;

import java.io.Serializable;
import java.util.Date;

public class CommissionShSharesDefaultVO implements Serializable {

    private Long productId;
    private Long commissionStakeHolderId;
    private Long commissionShSharesDefaultId;
    private Long createdByAppUserId;
    private Long updatedByAppUserId;
    private Double commissionShare;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;
    private Long isWhtApplicable;
    private Long isFedApplicable;

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

    public Long getCommissionShSharesDefaultId() {
        return commissionShSharesDefaultId;
    }

    public void setCommissionShSharesDefaultId(Long commissionShSharesDefaultId) {
        this.commissionShSharesDefaultId = commissionShSharesDefaultId;
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

    public Long getFedApplicable() {
        return isFedApplicable;
    }

    public void setFedApplicable(Long fedApplicable) {
        isFedApplicable = fedApplicable;
    }
}
