package com.inov8.microbank.common.vo.product;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class DistributorCommShareViewVO extends BasePersistableModel {


    private Long pk;
    private Long appUserId;
    private Long productId;
    private Long distributorLevelId;
    private Long distributorId;
    private Long parentAppUserId;
    private Long parentRetailerContactId;
    private Double parentCommissionShare;
    private Double commissionShare;
    private Long head;


    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDistributorLevelId() {
        return distributorLevelId;
    }

    public void setDistributorLevelId(Long distributorLevelId) {
        this.distributorLevelId = distributorLevelId;
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    public Long getParentAppUserId() {
        return parentAppUserId;
    }

    public void setParentAppUserId(Long parentAppUserId) {
        this.parentAppUserId = parentAppUserId;
    }

    public Long getParentRetailerContactId() {
        return parentRetailerContactId;
    }

    public void setParentRetailerContactId(Long parentRetailerContactId) {
        this.parentRetailerContactId = parentRetailerContactId;
    }

    public Double getParentCommissionShare() {
        return parentCommissionShare;
    }

    public void setParentCommissionShare(Double parentCommissionShare) {
        this.parentCommissionShare = parentCommissionShare;
    }

    public Double getCommissionShare() {
        return commissionShare;
    }

    public void setCommissionShare(Double commissionShare) {
        this.commissionShare = commissionShare;
    }

    public Long getHead() {
        return head;
    }

    public void setHead(Long head) {
        this.head = head;
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
