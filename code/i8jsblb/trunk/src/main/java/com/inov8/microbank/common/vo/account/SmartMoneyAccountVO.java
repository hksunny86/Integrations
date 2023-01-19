package com.inov8.microbank.common.vo.account;

import com.inov8.framework.common.model.BasePersistableModel;

import java.util.Date;

/**
 * Created by Inov8 on 5/31/2018.
 */
public class SmartMoneyAccountVO extends BasePersistableModel {
    private Long smartMoneyAccountId;
    private Long bankId;
    private Long paymentModeId;
    private Long customerId;
    private Long createdByAppUserId;
    private Long updatedByAppUserId;

    private String name;
    private Long statusId;
    private Long registrationStateId;
    private Long prevRegistrationStateId;
    private Long accountStateId;
    private Boolean isActive;
    private Boolean defAccount;
    private Boolean changePinRequired;
    private Date dormancyUpdatedOn;
    private Date CREATED_ON;
    private Date updatedOn;
    private Integer versionNo;
    private Boolean deleted;
    //
    private Long appUserId;
    private String comments;

    public Long getSmartMoneyAccountId() {
        return smartMoneyAccountId;
    }

    public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
        this.smartMoneyAccountId = smartMoneyAccountId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Date getDormancyUpdatedOn() {
        return dormancyUpdatedOn;
    }

    public void setDormancyUpdatedOn(Date dormancyUpdatedOn) {
        this.dormancyUpdatedOn = dormancyUpdatedOn;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDefAccount() {
        return defAccount;
    }

    public void setDefAccount(Boolean defAccount) {
        this.defAccount = defAccount;
    }

    public Boolean getChangePinRequired() {
        return changePinRequired;
    }

    public void setChangePinRequired(Boolean changePinRequired) {
        this.changePinRequired = changePinRequired;
    }

    public Date getCREATED_ON() {
        return CREATED_ON;
    }

    public void setCREATED_ON(Date CREATED_ON) {
        this.CREATED_ON = CREATED_ON;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Long paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public Long getRegistrationStateId() {
        return registrationStateId;
    }

    public void setRegistrationStateId(Long registrationStateId) {
        this.registrationStateId = registrationStateId;
    }

    public Long getAccountStateId() {
        return accountStateId;
    }

    public void setAccountStateId(Long accountStateId) {
        this.accountStateId = accountStateId;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Long getPrevRegistrationStateId() {
        return prevRegistrationStateId;
    }

    public void setPrevRegistrationStateId(Long prevRegistrationStateId) {
        this.prevRegistrationStateId = prevRegistrationStateId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
