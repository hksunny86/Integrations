package com.inov8.microbank.disbursement.vo;


import java.sql.Date;

/**
 * Created by AtieqRe on 2/16/2017.
 */
public class BDWalkInVO {
    private Long loggedInUser;
    private Long disbursementId;
    private String cnic;
    private String mobile;
    private Date date;
    private Long sequenceId;

    public BDWalkInVO(Long disbursementId, String cnic, String mobile, Long loggedInUser, Long sequenceId, Date date) {
        this.disbursementId = disbursementId;
        this.cnic = cnic;
        this.mobile = mobile;
        this.loggedInUser = loggedInUser;
        this.sequenceId = sequenceId;
        this.date = date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getDisbursementId() {
        return disbursementId;
    }

    public void setDisbursementId(Long disbursementId) {
        this.disbursementId = disbursementId;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public Long getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Long loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }
}