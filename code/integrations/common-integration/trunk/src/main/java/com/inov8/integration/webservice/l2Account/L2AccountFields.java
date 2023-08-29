package com.inov8.integration.webservice.l2Account;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class L2AccountFields implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String id;
    private String fieldId;
    private boolean isDiscrepant;
    private String title;
    private String dataEntryMethod;
    private boolean isVisible;
    private boolean isOptional;
    private boolean isReadonly;
    private String ultra;
    private String freelancer;
    private String ultraSignature;
    private String kycFieldRanking;
    private String dataType;
    private String minLength;
    private String maxLength;
    private String amount;
    private List<String> dataList;
    private String discrepantData;

    public String getDiscrepantData() {
        return discrepantData;
    }

    public void setDiscrepantData(String discrepantData) {
        this.discrepantData = discrepantData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public boolean isDiscrepant() {
        return isDiscrepant;
    }

    public void setDiscrepant(boolean discrepant) {
        isDiscrepant = discrepant;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataEntryMethod() {
        return dataEntryMethod;
    }

    public void setDataEntryMethod(String dataEntryMethod) {
        this.dataEntryMethod = dataEntryMethod;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public void setReadonly(boolean readonly) {
        isReadonly = readonly;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }
}
