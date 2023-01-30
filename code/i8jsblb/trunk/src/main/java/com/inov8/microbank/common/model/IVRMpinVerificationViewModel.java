package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// @Created On 1/27/2023 : Friday
// @Created By muhammad.aqeel

@Entity
@Table(name = "FONEPAY_INTG_LOG_REPORT_VIEW")
public class IVRMpinVerificationViewModel extends BasePersistableModel implements Serializable {
    @Id
    @Column(name = "PK")
    Long ID;
    @Column(name = "MOBILE_NO")
    String mobileNo;
    @Column(name = "LOG_DATE_AND_TIME")
    Date logDateAndTime;
    @Column(name = "RESPONSE_DESCRIPTION")
    String responseDescription;
    @Transient
    String verificationDate;
    @Transient
    String verificationTime;
    @Transient
    Date logEndDate;
    @Transient
    Date logStartDate;

    public IVRMpinVerificationViewModel() {
    }

    public Date getLogStartDate() {
        return this.logStartDate;
    }

    public void setLogStartDate(Date logStartDate) {
        this.logStartDate = logStartDate;
    }

    public Date getLogEndDate() {
        return this.logEndDate;
    }

    public void setLogEndDate(Date logEndDate) {
        this.logEndDate = logEndDate;
    }

    public String getVerificationDate() {
        return this.verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerificationTime() {
        return this.verificationTime;
    }

    public void setVerificationTime(String verificationTime) {
        this.verificationTime = verificationTime;
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getLogDateAndTime() {
        return this.logDateAndTime;
    }

    public void setLogDateAndTime(Date logDateAndTime) {
        this.logDateAndTime = logDateAndTime;
    }

    public String getResponseDescription() {
        return this.responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @Transient
    public void setPrimaryKey(Long aLong) {
        this.setID(aLong);
    }

    @Transient
    public Long getPrimaryKey() {
        return this.getID();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        return "&id=" + this.getID();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        return "id";
    }
}
