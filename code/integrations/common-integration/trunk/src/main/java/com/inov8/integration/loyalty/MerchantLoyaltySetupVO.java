package com.inov8.integration.loyalty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by bkr on 8/2/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantLoyaltySetupVO {

    private String merchantId;
    private String merchantName;
    private Integer amountSpent;
    private Integer pointEarned;
    private Integer pointMaturity;
    private Integer pointTopupMaturity;
    private Integer pointWorth;
    private String description;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;
    private Date validFrom;
    private Date validUntil;
    private Boolean isActive;
    private Long merchantLoyaltySetupId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(Integer amountSpent) {
        this.amountSpent = amountSpent;
    }

    public Integer getPointEarned() {
        return pointEarned;
    }

    public void setPointEarned(Integer pointEarned) {
        this.pointEarned = pointEarned;
    }

    public Integer getPointMaturity() {
        return pointMaturity;
    }

    public void setPointMaturity(Integer pointMaturity) {
        this.pointMaturity = pointMaturity;
    }

    public Integer getPointTopupMaturity() {
        return pointTopupMaturity;
    }

    public void setPointTopupMaturity(Integer pointTopupMaturity) {
        this.pointTopupMaturity = pointTopupMaturity;
    }

    public Integer getPointWorth() {
        return pointWorth;
    }

    public void setPointWorth(Integer pointWorth) {
        this.pointWorth = pointWorth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Long getMerchantLoyaltySetupId() {
        return merchantLoyaltySetupId;
    }

    public void setMerchantLoyaltySetupId(Long merchantLoyaltySetupId) {
        this.merchantLoyaltySetupId = merchantLoyaltySetupId;
    }

}
