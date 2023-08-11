package com.inov8.integration.webservice.l2Account;

import java.io.Serializable;

public class L2AccountFields implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String id;
    private String kycField;
    private String dataEntryMethod;
    private String isVisible;
    private String ultra;
    private String freelancer;
    private String ultraSignature;
    private String kycFieldRanking;
    private String dataType;
    private String minLength;
    private String maxLength;
    private String createdOn;
    private String createdBy;
    private String updateOn;
    private String updatedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKycField() {
        return kycField;
    }

    public void setKycField(String kycField) {
        this.kycField = kycField;
    }

    public String getDataEntryMethod() {
        return dataEntryMethod;
    }

    public void setDataEntryMethod(String dataEntryMethod) {
        this.dataEntryMethod = dataEntryMethod;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getUltra() {
        return ultra;
    }

    public void setUltra(String ultra) {
        this.ultra = ultra;
    }

    public String getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(String freelancer) {
        this.freelancer = freelancer;
    }

    public String getUltraSignature() {
        return ultraSignature;
    }

    public void setUltraSignature(String ultraSignature) {
        this.ultraSignature = ultraSignature;
    }

    public String getKycFieldRanking() {
        return kycFieldRanking;
    }

    public void setKycFieldRanking(String kycFieldRanking) {
        this.kycFieldRanking = kycFieldRanking;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMinLength() {
        return minLength;
    }

    public void setMinLength(String minLength) {
        this.minLength = minLength;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(String updateOn) {
        this.updateOn = updateOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
