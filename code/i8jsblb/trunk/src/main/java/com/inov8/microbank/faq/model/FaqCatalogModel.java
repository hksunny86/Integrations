package com.inov8.microbank.faq.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@javax.persistence.SequenceGenerator(name = "FAQ_CATALOG_SEQ", sequenceName = "FAQ_CATALOG_SEQ", allocationSize = 1)
@Table(name = "FAQ_CATALOG")
public class FaqCatalogModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long faqCatalogId;
    private String faqCatalogName;
    private String description;
    private Boolean active;
    private String comments;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;

    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;


    @Column(name = "FAQ_CATALOG_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAQ_CATALOG_SEQ")
    public Long getFaqCatalogId() {
        return faqCatalogId;
    }

    public void setFaqCatalogId(Long faqCatalogId) {
        this.faqCatalogId = faqCatalogId;
    }

    @Column(name = "NAME")
    public String getFaqCatalogName() {
        return faqCatalogName;
    }

    public void setFaqCatalogName(String faqCatalogName) {
        this.faqCatalogName = faqCatalogName;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "IS_ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Version
    @Column(name = "VERSION_NO")
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
        if (updatedByAppUserModel != null) {
            return updatedByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }


    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
        if (appUserId == null) {
            updatedByAppUserModel = null;
        } else {
            updatedByAppUserModel = new AppUserModel();
            updatedByAppUserModel.setAppUserId(appUserId);
        }
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel() {
        return createdByAppUserModel;
    }

    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        this.createdByAppUserModel = appUserModel;
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
        if (createdByAppUserModel != null) {
            return createdByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
        if (appUserId == null) {
            createdByAppUserModel = null;
        } else {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&faqCatalogId=" + getFaqCatalogId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "faqCatalogId";
        return primaryKeyFieldName;
    }

    @Override
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getFaqCatalogId();
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setFaqCatalogId(primaryKey);
    }

}
