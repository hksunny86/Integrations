package com.inov8.microbank.hra.airtimetopup.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "REMITANCE_INFO_SEQ", sequenceName = "REMITANCE_INFO_SEQ", allocationSize = 1)
@Table(name = "HRA_REMITANCE_INFO")
public class HRARemitanceInfoModel extends BasePersistableModel {

    private Long hraRemitanceInfoId;
    private String mobileNo;
    private Double amountCashedIn;
    private String rrn;
    private String terminalId;
    private String paymentMode;
    private Boolean isActive;
    private Timestamp reqDateTime;
    private Date createdOn;
    private Date updatedOn;

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setHraRemitanceInfoId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getHraRemitanceInfoId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&hraRemitanceInfoId=" + getHraRemitanceInfoId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "hraRemitanceInfoId";
        return primaryKeyFieldName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REMITANCE_INFO_SEQ")
    @Column(name = "HRA_REMITANCE_INFO_ID", nullable = false)
    public Long getHraRemitanceInfoId() {
        return hraRemitanceInfoId;
    }

    public void setHraRemitanceInfoId(Long hraRemitanceInfoId) {
        this.hraRemitanceInfoId = hraRemitanceInfoId;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "AMOUNT_CASHED_IN")
    public Double getAmountCashedIn() {
        return amountCashedIn;
    }

    public void setAmountCashedIn(Double amountCashedIn) {
        this.amountCashedIn = amountCashedIn;
    }

    @Column(name = "RRN")
    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @Column(name = "TERMINAL_ID")
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Column(name = "PAYMENT_MODE")
    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Column(name = "IS_ACTIVE")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    @Column(name = "REQ_DATE_TIME")
    public Timestamp getReqDateTime() {
        return reqDateTime;
    }

    public void setReqDateTime(Timestamp reqDateTime) {
        this.reqDateTime = reqDateTime;
    }
}
