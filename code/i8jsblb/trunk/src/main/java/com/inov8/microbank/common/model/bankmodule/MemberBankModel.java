package com.inov8.microbank.common.model.bankmodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "MEMBER_BANK")
public class MemberBankModel extends BasePersistableModel {

    private Long memberBankId;
    private String bankName;
    private String bankImd;
    private String switchName;
    private String minAccountNoLength;
    private String maxAccountNoLength;
    private Boolean isActive;
    private Long sequenceNo;
    private String bankShortName;
    private Long createdBy;
    private Long updatedBy;
    private Date createdOn;
    private Date updatedOn;

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setMemberBankId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getMemberBankId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&memberBankId=" + getMemberBankId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "memberBankId";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "MEMBER_BANK_ID", nullable = false)
    public Long getMemberBankId() {
        return memberBankId;
    }

    public void setMemberBankId(Long memberBankId) {
        this.memberBankId = memberBankId;
    }

    @Column(name = "NAME")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "DEBIT_CARD_IMD")
    public String getBankImd() {
        return bankImd;
    }

    public void setBankImd(String bankImd) {
        this.bankImd = bankImd;
    }

    @Column(name = "SWITCH_NAME")
    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    @Column(name = "MIN_ACC_LENGTH")
    public String getMinAccountNoLength() {
        return minAccountNoLength;
    }

    public void setMinAccountNoLength(String minAccountNoLength) {
        this.minAccountNoLength = minAccountNoLength;
    }

    @Column(name = "MAX_ACC_LENGTH")
    public String getMaxAccountNoLength() {
        return maxAccountNoLength;
    }

    public void setMaxAccountNoLength(String maxAccountNoLength) {
        this.maxAccountNoLength = maxAccountNoLength;
    }

    @Column(name = "IS_ACTIVE")
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Column(name = "SEQUENCE_NO")
    public Long getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Long sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    @Column(name = "SHORT_NAME")
    public String getBankShortName() {
        return bankShortName;
    }

    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
    }

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED_BY")
    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
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
}
