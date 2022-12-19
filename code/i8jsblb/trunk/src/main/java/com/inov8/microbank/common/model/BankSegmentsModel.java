package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "BANK_SEGMENTS")
public class BankSegmentsModel extends BasePersistableModel {

    private Long pk;
    private Long sourceBankId;
    private Long sourceSegmentId;
    private String sourceSegmentName;
    private Long destinationBankId;
    private Long destinationSegmentId;
    private String destinationSegmentName;
    private Boolean isActive;
    private Date createdOn;
    private Date updatedOn;
    private String imd;

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setPk(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "PK")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Column(name = "SOURCE_BANK_ID")
    public Long getSourceBankId() {
        return sourceBankId;
    }

    public void setSourceBankId(Long sourceBankId) {
        this.sourceBankId = sourceBankId;
    }

    @Column(name = "SOURCE_SEGMENT_ID")
    public Long getSourceSegmentId() {
        return sourceSegmentId;
    }

    public void setSourceSegmentId(Long sourceSegmentId) {
        this.sourceSegmentId = sourceSegmentId;
    }

    @Column(name = "DESTINATION_BANK_ID")
    public Long getDestinationBankId() {
        return destinationBankId;
    }

    public void setDestinationBankId(Long destinationBankId) {
        this.destinationBankId = destinationBankId;
    }

    @Column(name = "DESTINATION_SEGMENT_ID")
    public Long getDestinationSegmentId() {
        return destinationSegmentId;
    }

    public void setDestinationSegmentId(Long destinationSegmentId) {
        this.destinationSegmentId = destinationSegmentId;
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

    @Column(name = "SOURCE_SEGMENT_NAME")
    public String getSourceSegmentName() {
        return sourceSegmentName;
    }

    public void setSourceSegmentName(String sourceSegmentName) {
        this.sourceSegmentName = sourceSegmentName;
    }

    @Column(name = "DESTINATION_SEGMENT_NAME")
    public String getDestinationSegmentName() {
        return destinationSegmentName;
    }

    public void setDestinationSegmentName(String destinationSegmentName) {
        this.destinationSegmentName = destinationSegmentName;
    }
    @Column(name = "IMD")
    public String getImd() { return imd; }

    public void setImd(String imd) { this.imd = imd; }
}
