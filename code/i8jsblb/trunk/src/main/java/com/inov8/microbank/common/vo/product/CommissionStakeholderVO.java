package com.inov8.microbank.common.vo.product;

import com.inov8.framework.common.model.BasePersistableModel;

import java.util.Date;

/**
 * Created by Inov8 on 9/21/2019.
 */
public class CommissionStakeholderVO extends BasePersistableModel {
    private Long stakeHolderTypeId;
    private Long retailerId;
    private Long operatorId;
    private Long distributorId;
    private Long bankId;
    private Long createdByAppUserId;
    private Long updatedByAppUserId;
    private Long CommissionShAcctsTypeId;
    private Long commissionStakeholderId;
    private String name;
    private String  contactName;
    private String description;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private String comments;
    private Boolean displayOnProductScreen;
    private Boolean filer;


    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getUpdatedByAppUserId() {
        return updatedByAppUserId;
    }

    public void setUpdatedByAppUserId(Long updatedByAppUserId) {
        this.updatedByAppUserId = updatedByAppUserId;
    }

    public Long getStakeHolderTypeId() {
        return stakeHolderTypeId;
    }

    public void setStakeHolderTypeId(Long stakeHolderTypeId) {
        this.stakeHolderTypeId = stakeHolderTypeId;
    }

    public Long getCreatedByAppUserId() {
        return createdByAppUserId;
    }

    public void setCreatedByAppUserId(Long createdByAppUserId) {
        this.createdByAppUserId = createdByAppUserId;
    }


    public Long getCommissionShAcctsTypeId() {
        return CommissionShAcctsTypeId;
    }

    public void setCommissionShAcctsTypeId(Long commissionShAcctsTypeId) {
        CommissionShAcctsTypeId = commissionShAcctsTypeId;
    }

    public Long getCommissionStakeholderId() {
        return commissionStakeholderId;
    }

    public void setCommissionStakeholderId(Long commissionStakeholderId) {
        this.commissionStakeholderId = commissionStakeholderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getDisplayOnProductScreen() {
        return displayOnProductScreen;
    }

    public void setDisplayOnProductScreen(Boolean displayOnProductScreen) {
        this.displayOnProductScreen = displayOnProductScreen;
    }

    public Boolean getFiler() {
        return filer;
    }

    public void setFiler(Boolean filer) {
        this.filer = filer;
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

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }
}
