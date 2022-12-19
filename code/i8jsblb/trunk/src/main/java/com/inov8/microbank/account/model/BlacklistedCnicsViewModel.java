package com.inov8.microbank.account.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.CommonUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Malik on 10/9/2016.
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BLACKLISTED_CNICS_VIEW")
public class BlacklistedCnicsViewModel extends BasePersistableModel implements Serializable {

    private Long blacklistedCnicsId;
    private String cnicNo;
    private Boolean isBlacklisted;
    private Date createdOn;
    private Date updatedOn;
    private Long createdBy;
    private Long updatedBy;
    private String createdByName;
    private String updatedByName;
    private String comments;

    private Date createdOnStart;
    private Date createdOnEnd;

    private Date updatedOnStart;
    private Date updatedOnEnd;

    @Id
    @Column(name = "BLACKLISTED_CNICS_ID", nullable = false)
    public Long getBlacklistedCnicsId() {
        return blacklistedCnicsId;
    }

    public void setBlacklistedCnicsId(Long blacklistedCnicsId) {
        this.blacklistedCnicsId = blacklistedCnicsId;
    }

    @Column(name = "CNIC_NO", nullable = false)
    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    @Column(name = "IS_BLACKLISTED")
    public Boolean getBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(Boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    @Column(name = "CREATED_ON", nullable = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON", nullable = false)
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name="CREATED_BY")
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name="UPDATED_BY")
    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name="CREATED_BY_NAME")
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Column(name="UPDATED_BY_NAME")
    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    @Column(name="COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        this.setBlacklistedCnicsId(primaryKey);
    }

    @Transient
    public Long getPrimaryKey() {
        return getBlacklistedCnicsId();
    }


    @Transient
    public Date getUpdatedOnEnd() {
        return updatedOnEnd;
    }

    public void setUpdatedOnEnd(Date updatedOnEnd) {
        this.updatedOnEnd = updatedOnEnd;
    }

    @Transient
    public Date getCreatedOnStart() {
        return createdOnStart;
    }

    public void setCreatedOnStart(Date createdOnStart) {
        this.createdOnStart = createdOnStart;
    }

    @Transient
    public Date getCreatedOnEnd() {
        return createdOnEnd;
    }

    public void setCreatedOnEnd(Date createdOnEnd) {
        this.createdOnEnd = createdOnEnd;
    }

    @Transient
    public Date getUpdatedOnStart() {
        return updatedOnStart;
    }

    public void setUpdatedOnStart(Date updatedOnStart) {
        this.updatedOnStart = updatedOnStart;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&blacklistedCnicsId=" + getBlacklistedCnicsId();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "blacklistedCnicsId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getStatus() {
        return CommonUtils.getDefaultIfNull(isBlacklisted, false) ? "Yes" : "No";
    }
}
