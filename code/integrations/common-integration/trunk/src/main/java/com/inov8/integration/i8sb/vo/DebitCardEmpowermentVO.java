package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("DebitCardEmpowerment")
public class DebitCardEmpowermentVO implements Serializable {
    private String cardTypeCode;
    private String cycleLengthMonthly;
    private String isDefault;
    private String maxLimit;
    private String maxLimitOffline;
    private String singleTranLimit;
    private String singleTranLimitOffline;
    private String allowedTranCount;
    private String maxLimitMonthly;
    private String maxLimitMonthlyOffline;
    private String allowedTranCountMonthly;
    private String tranCode;
    private String effectiveFrom;
    private String effectiveTo;
    private String channelId;
    private String channelName;
    private String transactionName;
    private String tempLimit;
    private String tempLimitMax;
    private String tempLimitExpiry;
    private String profileId;
    private String instrumentCode;
    private String createdOn;
    private String name;
    private String isAllowed;

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCycleLengthMonthly() {
        return cycleLengthMonthly;
    }

    public void setCycleLengthMonthly(String cycleLengthMonthly) {
        this.cycleLengthMonthly = cycleLengthMonthly;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getMaxLimitOffline() {
        return maxLimitOffline;
    }

    public void setMaxLimitOffline(String maxLimitOffline) {
        this.maxLimitOffline = maxLimitOffline;
    }

    public String getSingleTranLimit() {
        return singleTranLimit;
    }

    public void setSingleTranLimit(String singleTranLimit) {
        this.singleTranLimit = singleTranLimit;
    }

    public String getSingleTranLimitOffline() {
        return singleTranLimitOffline;
    }

    public void setSingleTranLimitOffline(String singleTranLimitOffline) {
        this.singleTranLimitOffline = singleTranLimitOffline;
    }

    public String getAllowedTranCount() {
        return allowedTranCount;
    }

    public void setAllowedTranCount(String allowedTranCount) {
        this.allowedTranCount = allowedTranCount;
    }

    public String getMaxLimitMonthly() {
        return maxLimitMonthly;
    }

    public void setMaxLimitMonthly(String maxLimitMonthly) {
        this.maxLimitMonthly = maxLimitMonthly;
    }

    public String getMaxLimitMonthlyOffline() {
        return maxLimitMonthlyOffline;
    }

    public void setMaxLimitMonthlyOffline(String maxLimitMonthlyOffline) {
        this.maxLimitMonthlyOffline = maxLimitMonthlyOffline;
    }

    public String getAllowedTranCountMonthly() {
        return allowedTranCountMonthly;
    }

    public void setAllowedTranCountMonthly(String allowedTranCountMonthly) {
        this.allowedTranCountMonthly = allowedTranCountMonthly;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(String effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTempLimit() {
        return tempLimit;
    }

    public void setTempLimit(String tempLimit) {
        this.tempLimit = tempLimit;
    }

    public String getTempLimitMax() {
        return tempLimitMax;
    }

    public void setTempLimitMax(String tempLimitMax) {
        this.tempLimitMax = tempLimitMax;
    }

    public String getTempLimitExpiry() {
        return tempLimitExpiry;
    }

    public void setTempLimitExpiry(String tempLimitExpiry) {
        this.tempLimitExpiry = tempLimitExpiry;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode) {
        this.instrumentCode = instrumentCode;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(String isAllowed) {
        this.isAllowed = isAllowed;
    }
}
