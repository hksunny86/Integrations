package com.inov8.microbank.common.vo.product;

import com.inov8.framework.common.model.BasePersistableModel;

import java.util.Date;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class CommissionRateDefaultVO extends BasePersistableModel {

    private Long productId;
    private Long createdByAppUserId;
    private Long updatedByAppUserId;
    private Long commissionRateDefaultId;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private Double exclusiveFixAmount;
    private Double exclusivePercentAmount;
    private Double inclusiveFixAmount;
    private Double inclusivePercentAmount;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Long getCommissionRateDefaultId() {
        return commissionRateDefaultId;
    }

    public void setCommissionRateDefaultId(Long commissionRateDefaultId) {
        this.commissionRateDefaultId = commissionRateDefaultId;
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
