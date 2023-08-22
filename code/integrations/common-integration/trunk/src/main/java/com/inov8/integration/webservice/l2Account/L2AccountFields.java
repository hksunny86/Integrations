package com.inov8.integration.webservice.l2Account;

import java.io.Serializable;
import java.util.List;

public class L2AccountFields implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String id;
    private String fieldId;
    private String isDiscrepant;
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
    private List<DataList> dataList;
//    private boolean emailOtp;
//    private String cnicFront;
//    private String cnicBack;
//    private String nadraVerisysCharges;
//    private String biometricCharges;
//    private String pmdCharges;

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

    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }

//    public boolean isEmailOtp() {
//        return emailOtp;
//    }
//
//    public void setEmailOtp(boolean emailOtp) {
//        this.emailOtp = emailOtp;
//    }
//
//    public String getCnicFront() {
//        return cnicFront;
//    }
//
//    public void setCnicFront(String cnicFront) {
//        this.cnicFront = cnicFront;
//    }
//
//    public String getCnicBack() {
//        return cnicBack;
//    }
//
//    public void setCnicBack(String cnicBack) {
//        this.cnicBack = cnicBack;
//    }
//
//    public String getNadraVerisysCharges() {
//        return nadraVerisysCharges;
//    }
//
//    public void setNadraVerisysCharges(String nadraVerisysCharges) {
//        this.nadraVerisysCharges = nadraVerisysCharges;
//    }
//
//    public String getBiometricCharges() {
//        return biometricCharges;
//    }
//
//    public void setBiometricCharges(String biometricCharges) {
//        this.biometricCharges = biometricCharges;
//    }
//
//    public String getPmdCharges() {
//        return pmdCharges;
//    }
//
//    public void setPmdCharges(String pmdCharges) {
//        this.pmdCharges = pmdCharges;
//    }
}
