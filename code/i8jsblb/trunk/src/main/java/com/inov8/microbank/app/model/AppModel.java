package com.inov8.microbank.app.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserTypeModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * APP entity @author Hamza
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_SEQ", sequenceName = "APP_SEQ", allocationSize = 1)
@Table(name = "APP")
public class AppModel extends BasePersistableModel implements Serializable {

    private static final long serialVersionUID = -6087130388899352922L;
    private Long appId;
    private String appName;
    private Boolean active;
    private AppUserTypeModel appUserTypeModel;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Long versionNo;

    public AppModel() {
    }

    @Transient
    public Long getPrimaryKey() {
        return getAppId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        return "appId";
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameter = "&appId=" + getAppId();
        return parameter;
    }

    public void setPrimaryKey(Long primaryKey) {
        setAppId(primaryKey);
    }

    @Transient
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>(3);
        AssociationModel associationModel;

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("createdByAppUserModel");
        associationModel.setValue(getCreatedByAppUserModel());
        associationModelList.add(associationModel);

        associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("updatedByAppUserModel");
        associationModel.setValue(getUpdatedByAppUserModel());
        associationModelList.add(associationModel);

        return associationModelList;
    }

    @Column(name = "APP_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_SEQ")
    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel() {
        return createdByAppUserModel;
    }


    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.createdByAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    public void setCreatedBy(Long id) {
        if (createdByAppUserModel == null)
            createdByAppUserModel = new AppUserModel();

        createdByAppUserModel.setAppUserId(id);
    }

    @Transient
    public Long getCreatedBy() {
        Long createdByAppUserId = null;
        if (createdByAppUserModel != null) {
            createdByAppUserId = createdByAppUserModel.getAppUserId();
        }
        return createdByAppUserId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }


    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if (null != appUserModel) {
            this.updatedByAppUserModel = (AppUserModel) appUserModel.clone();
        }
    }

    public void setUpdatedBy(Long id) {
        if (updatedByAppUserModel == null)
            updatedByAppUserModel = new AppUserModel();

        updatedByAppUserModel.setAppUserId(id);
    }

    @Transient
    public Long getUpdatedBy() {
        Long updatedByAppUserId = null;
        if (updatedByAppUserModel != null) {
            updatedByAppUserId = updatedByAppUserModel.getAppUserId();
        }
        return updatedByAppUserId;
    }

    /**
     * @return the createdOn
     */
    @Column(name = "CREATED_ON", nullable = false)
    public Date getCreatedOn() {
        return createdOn;
    }


    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }


    /**
     * @return the updatedOn
     */
    @Column(name = "UPDATED_ON", nullable = false)
    public Date getUpdatedOn() {
        return updatedOn;
    }


    /**
     * @param updatedOn the updatedOn to set
     */
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }


    /**
     * @return the isActive
     */
    @Column(name = "IS_ACTIVE", nullable = false)
    public Boolean getActive() {
        return active;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setActive(Boolean isActive) {
        this.active = isActive;
    }

    /**
     * @return the versionNo
     */
    @Version
    @Column(name = "VERSION_NO")
    public Long getVersionNo() {
        return versionNo;
    }


    /**
     * @param versionNo the versionNo to set
     */
    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    /**
     * @return the appName
     */
    @Column(name = "NAME")
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_TYPE_ID")
    public AppUserTypeModel getAppUserTypeModel() {
        return appUserTypeModel;
    }

    public void setAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
        this.appUserTypeModel = appUserTypeModel;
    }

    @Transient
    public void setAppUserTypeId(Long appUserTypeId) {
        if(appUserTypeModel == null)
            appUserTypeModel = new AppUserTypeModel();

        appUserTypeModel.setAppUserTypeId(appUserTypeId);
    }
}
