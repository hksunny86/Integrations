package com.inov8.export.vo;

import java.math.BigDecimal;

/**
 * Created by atieq.rehman on 3/29/2018.
 */
public class SharePlanDetailVO {

    private Long sharePlanId;
    private String title;
    private String planType;
    private String usageType;
    private String serviceName;
    private String productName;
    private String active;
    private Long sharePlanDetailId;
    private Double slabFrom;
    private Double slabTo;
    private Long sharePlanShId;
    private Long stakeholderId;
    private String stakeholderName;
    private Double share;
    private String whtApplicable;

    public SharePlanDetailVO(Long sharePlanId, String title, String planType, String usageType,
                             String serviceName, String productName, String active,
                             Long sharePlanDetailId, BigDecimal slabFrom, BigDecimal slabTo,
                             Long sharePlanShId, Long stakeholderId, String stakeholderName,
                             BigDecimal share, BigDecimal whtApplicable) {
        this.sharePlanId = sharePlanId;
        this.title = title;
        this.planType = planType;
        this.usageType = usageType;
        this.serviceName = serviceName;
        this.productName = productName;
        this.active = active;
        this.sharePlanDetailId = sharePlanDetailId;
        this.slabFrom = slabFrom != null ? slabFrom.doubleValue() : 0;
        this.slabTo = slabTo != null ? slabTo.doubleValue() : 0;
        this.sharePlanShId = sharePlanShId;
        this.stakeholderId = stakeholderId;
        this.stakeholderName = stakeholderName;
        this.share = share != null ? share.doubleValue() : 0;
        this.whtApplicable = whtApplicable != null && whtApplicable.toString().equals("0") ? "Yes" : "No";
    }

    public Long getSharePlanId() {
        return sharePlanId;
    }

    public void setSharePlanId(Long sharePlanId) {
        this.sharePlanId = sharePlanId;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getSharePlanDetailId() {
        return sharePlanDetailId;
    }

    public void setSharePlanDetailId(Long sharePlanDetailId) {
        this.sharePlanDetailId = sharePlanDetailId;
    }

    public Long getSharePlanShId() {
        return sharePlanShId;
    }

    public void setSharePlanShId(Long sharePlanShId) {
        this.sharePlanShId = sharePlanShId;
    }

    public Long getStakeholderId() {
        return stakeholderId;
    }

    public void setStakeholderId(Long stakeholderId) {
        this.stakeholderId = stakeholderId;
    }

    public String getStakeholderName() {
        return stakeholderName;
    }

    public void setStakeholderName(String stakeholderName) {
        this.stakeholderName = stakeholderName;
    }

    public Double getShare() {
        return share;
    }

    public void setShare(Double share) {
        this.share = share;
    }

    public String getWhtApplicable() {
        return whtApplicable;
    }

    public void setWhtApplicable(String whtApplicable) {
        this.whtApplicable = whtApplicable;
    }
}
