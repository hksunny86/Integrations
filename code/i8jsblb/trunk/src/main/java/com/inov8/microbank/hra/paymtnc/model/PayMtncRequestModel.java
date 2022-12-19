package com.inov8.microbank.hra.paymtnc.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PAY_MTNC_REQ_INFO_SEQ", sequenceName = "PAY_MTNC_REQ_INFO_SEQ", allocationSize = 1)
@Table(name = "PAY_MTNC_REQUEST_INFO")
public class PayMtncRequestModel extends BasePersistableModel {

    private Long payMtncReqId;
    private String mobileNo;
    private String cnic;
    private String thirdPartyResponseCode;
    private String rrn;
    private AppUserModel createdByAppUserModel;
    private Date createdOn;
    private AppUserModel updatedByAppUserModel;
    private Date updatedOn;
    private Long isValid;
    private Long isAccountOpening;

    private Date createdOnStartDate;
    private Date createdOnEndDate;
    private Date updatedOnStartDate;
    private Date updatedOnEndDate;

    @javax.persistence.Transient
    public Date getCreatedOnStartDate() {
        return createdOnStartDate;
    }

    public void setCreatedOnStartDate(Date createdOnStartDate) {
        this.createdOnStartDate = createdOnStartDate;
    }

    @javax.persistence.Transient
    public Date getCreatedOnEndDate() {
        return createdOnEndDate;
    }

    public void setCreatedOnEndDate(Date createdOnEndDate) {
        this.createdOnEndDate = createdOnEndDate;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAY_MTNC_REQ_INFO_SEQ")
    @Column(name = "PAY_MTNC_REQ_ID", nullable = false)
    public Long getPayMtncReqId() {
        return payMtncReqId;
    }

    public void setPayMtncReqId(Long customerRemmitanceId) {
        this.payMtncReqId = customerRemmitanceId;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "CNIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "THIRD_PARTY_RESPONSE_CODE")
    public String getThirdPartyResponseCode() {
        return thirdPartyResponseCode;
    }

    public void setThirdPartyResponseCode(String thirdPartyResponseCode) {
        this.thirdPartyResponseCode = thirdPartyResponseCode;
    }

    @Column(name = "RRN")
    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    /////Mobile Network CreatedBy///////

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.createdByAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
        Long createdByAppUserId = null;
        if (createdByAppUserModel != null) {
            createdByAppUserId = createdByAppUserModel.getAppUserId();
        }
        return createdByAppUserId;
    }

    public void setCreatedBy(Long createdBy) {
        if (createdBy == null) {
            createdByAppUserModel = null;
        } else {
            createdByAppUserModel = new AppUserModel(createdBy);
        }
    }
    /////Mobile Network UpdatedBy///////


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.updatedByAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
        Long updatedByAppUserId = null;
        if (updatedByAppUserModel != null) {
            updatedByAppUserId = updatedByAppUserModel.getAppUserId();
        }
        return updatedByAppUserId;
    }

    public void setUpdatedBy(Long updatedBy) {
        if (updatedBy == null) {
            updatedByAppUserModel = null;
        } else {
            updatedByAppUserModel = new AppUserModel(updatedBy);
        }
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setPayMtncReqId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPayMtncReqId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&payMtncReqId=" + getPayMtncReqId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "payMtncReqId";
        return primaryKeyFieldName;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @javax.persistence.Transient
    public Date getUpdatedOnStartDate() {
        return updatedOnStartDate;
    }

    public void setUpdatedOnStartDate(Date updatedOnStartDate) {
        this.updatedOnStartDate = updatedOnStartDate;
    }

    @javax.persistence.Transient
    public Date getUpdatedOnEndDate() {
        return updatedOnEndDate;
    }

    public void setUpdatedOnEndDate(Date updatedOnEndDate) {
        this.updatedOnEndDate = updatedOnEndDate;
    }

    @Column(name = "IS_ACTIVE")
    public Long getIsValid() {
        return isValid;
    }

    public void setIsValid(Long isValid) {
        this.isValid = isValid;
    }

    @Column(name = "IS_ACCOUNT_OPENING")
    public Long getIsAccountOpening() {
        return isAccountOpening;
    }

    public void setIsAccountOpening(Long isAccountOpening) {
        this.isAccountOpening = isAccountOpening;
    }
}
