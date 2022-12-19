package com.inov8.export.vo;

import java.math.BigDecimal;

/**
 * Created by atieq.rehman on 3/29/2018.
 */
public class PricePlanDetailVO {

    private Long pricePlanId;
    private String title;
    private String planType;
    private String usageType;
    private Double activationFrom;
    private Double maxTxAmount;
    private String active;
    private Double slabFrom;
    private Double slabTo;
    private String fixed;
    private String inclusive;
    private Double fee;
    private Double minValue;
    private Double maxValue;

    public PricePlanDetailVO(Long pricePlanId, String title, String planType, String usageType, BigDecimal activationFrom,
                             BigDecimal maxTxAmount, String active, BigDecimal slabFrom, BigDecimal slabTo, BigDecimal minValue, BigDecimal maxValue,
                             String fixed, String inclusive, BigDecimal fee) {
        this.pricePlanId = pricePlanId;
        this.title = title;
        this.planType = planType;
        this.usageType = usageType;
        this.activationFrom = activationFrom != null ? activationFrom.doubleValue() : 0;
        this.maxTxAmount = maxTxAmount != null ? maxTxAmount.doubleValue() : 0;
        this.active = active;
        this.slabFrom = slabFrom != null ? slabFrom.doubleValue() : 0;
        this.slabTo = slabTo != null ? slabTo.doubleValue() : 0;
        this.minValue = minValue != null ? minValue.doubleValue() : 0;
        this.maxValue = maxValue != null ? maxValue.doubleValue() : 0;
        this.fixed = fixed;
        this.inclusive = inclusive;
        this.fee = fee != null ? fee.doubleValue() : 0;
    }

    public Long getPricePlanId() {
        return pricePlanId;
    }

    public void setPricePlanId(Long pricePlanId) {
        this.pricePlanId = pricePlanId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public Double getActivationFrom() {
        return activationFrom;
    }

    public void setActivationFrom(Double activationFrom) {
        this.activationFrom = activationFrom;
    }

    public Double getMaxTxAmount() {
        return maxTxAmount;
    }

    public void setMaxTxAmount(Double maxTxAmount) {
        this.maxTxAmount = maxTxAmount;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Double getSlabFrom() {
        return slabFrom;
    }

    public void setSlabFrom(Double slabFrom) {
        this.slabFrom = slabFrom;
    }

    public Double getSlabTo() {
        return slabTo;
    }

    public void setSlabTo(Double slabTo) {
        this.slabTo = slabTo;
    }

    public String getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getInclusive() {
        return inclusive;
    }

    public void setInclusive(String inclusive) {
        this.inclusive = inclusive;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }
}
