package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "TRANSACTION_PURPOSE")
public class TransactionPurposeModel extends BasePersistableModel {

    private Long transactionPurposeId;
    private String code;
    private String name;
    private Date createdOn;
    private Date updatedOn;
    private Long createdBy;
    private Long updatedBy;
    private Boolean isActive;

    @Id
    @Column(name = "TRANS_PURPOSE_ID", nullable = false)
    public Long getTransactionPurposeId() {
        return transactionPurposeId;
    }

    public void setTransactionPurposeId(Long transactionPurposeId) {
        this.transactionPurposeId = transactionPurposeId;
    }

    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Column(name = "IS_ACTIVE")
    public Boolean getIsActive() { return this.isActive;
    }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setTransactionPurposeId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getTransactionPurposeId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&transactionPurposeId=" + getTransactionPurposeId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "transactionPurposeId";
        return primaryKeyFieldName;
    }
}
