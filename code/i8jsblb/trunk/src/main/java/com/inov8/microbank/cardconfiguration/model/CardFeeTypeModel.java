package com.inov8.microbank.cardconfiguration.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CARD_FEE_TYPE_SEQ", sequenceName = "CARD_FEE_TYPE_SEQ", allocationSize = 1)
@Table(name = "CARD_FEE_TYPE")
public class CardFeeTypeModel extends BasePersistableModel {

    private Long cardFeeTypeId;
    private String name;
    private String description;
    private String comments;
    private Date createdOn;
    private Date updatedOn;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setCardFeeTypeId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getCardFeeTypeId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&cardFeeTypeId=" + getCardFeeTypeId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "cardFeeTypeId";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "CARD_FEE_TYPE_ID")
    public Long getCardFeeTypeId() {
        return cardFeeTypeId;
    }

    public void setCardFeeTypeId(Long cardFeeTypeId) {
        this.cardFeeTypeId = cardFeeTypeId;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
}
