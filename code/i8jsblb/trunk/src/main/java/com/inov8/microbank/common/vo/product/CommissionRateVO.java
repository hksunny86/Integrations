package com.inov8.microbank.common.vo.product;

import com.inov8.framework.common.model.BasePersistableModel;

import java.util.Date;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class CommissionRateVO extends BasePersistableModel {

    private Long transactionTypeId;
    private Long stakeholderBankInfoId;
    private Long segmentId;
    private Long productId;
    private Long paymentModeId;
    private Long commissionTypeId;
    private Long commissionStakeholderId;
    private Long commissionReasonId;
    private Long createdByAppUserId;
    private Long updatedByAppUserId;
    private Long deviceTypeId;
    private Long distributorId;
    private Long mnoId;
    private Long commissionRateId;
    private Double rate;
    private Date fromDate;
    private Date toDate;
    private Long active;
    private String description;
    private String comments;
    private Long extra;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private Double rangeStarts;
    private Double rangeEnds;
    private Double exclusiveFixAmount = 0D;
    private Double exclusivePercentAmount = 0D;
    private Double inclusiveFixAmount = 0D;
    private Double inclusivePercentAmount = 0D;
    private Long isDeleted;

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

    public Long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public Long getStakeholderBankInfoId() {
        return stakeholderBankInfoId;
    }

    public void setStakeholderBankInfoId(Long stakeholderBankInfoId) {
        this.stakeholderBankInfoId = stakeholderBankInfoId;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Long paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public Long getCommissionTypeId() {
        return commissionTypeId;
    }

    public void setCommissionTypeId(Long commissionTypeId) {
        this.commissionTypeId = commissionTypeId;
    }

    public Long getCommissionStakeholderId() {
        return commissionStakeholderId;
    }

    public void setCommissionStakeholderId(Long commissionStakeholderId) {
        this.commissionStakeholderId = commissionStakeholderId;
    }

    public Long getCommissionReasonId() {
        return commissionReasonId;
    }

    public void setCommissionReasonId(Long commissionReasonId) {
        this.commissionReasonId = commissionReasonId;
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



    public Long getCommissionRateId() {
        return commissionRateId;
    }

    public void setCommissionRateId(Long commissionRateId) {
        this.commissionRateId = commissionRateId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getExtra() {
        return extra;
    }

    public void setExtra(Long extra) {
        this.extra = extra;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public Double getRangeStarts() {
        return rangeStarts;
    }

    public void setRangeStarts(Double rangeStarts) {
        this.rangeStarts = rangeStarts;
    }

    public Double getRangeEnds() {
        return rangeEnds;
    }

    public void setRangeEnds(Double rangeEnds) {
        this.rangeEnds = rangeEnds;
    }

    public Double getExclusiveFixAmount() {
        return exclusiveFixAmount;
    }

    public void setExclusiveFixAmount(Double exclusiveFixAmount) {
        this.exclusiveFixAmount = exclusiveFixAmount;
    }

    public Double getExclusivePercentAmount() {
        return exclusivePercentAmount;
    }

    public void setExclusivePercentAmount(Double exclusivePercentAmount) {
        this.exclusivePercentAmount = exclusivePercentAmount;
    }

    public Double getInclusiveFixAmount() {
        return inclusiveFixAmount;
    }

    public void setInclusiveFixAmount(Double inclusiveFixAmount) {
        this.inclusiveFixAmount = inclusiveFixAmount;
    }

    public Double getInclusivePercentAmount() {
        return inclusivePercentAmount;
    }

    public void setInclusivePercentAmount(Double inclusivePercentAmount) {
        this.inclusivePercentAmount = inclusivePercentAmount;
    }

    public Long getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Long deleted) {
        isDeleted = deleted;
    }

    @Override
    public void setPrimaryKey(Long aLong) {

    }

    @Override
    public Long getPrimaryKey() {
        return null;
    }

    @Override
    public String getPrimaryKeyParameter() {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return null;
    }
}
