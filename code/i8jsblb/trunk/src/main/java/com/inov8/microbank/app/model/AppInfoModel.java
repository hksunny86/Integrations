package com.inov8.microbank.app.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * APP entity @author Asad
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "APP_INFO_SEQ", sequenceName = "APP_INFO_SEQ", allocationSize = 1)
@Table(name = "APP_INFO")
public class AppInfoModel extends BasePersistableModel implements Serializable {

    private static final long serialVersionUID = -6087130388899352922L;
    private Long appInfoId;
    private AppModel appModel;
    private String url;
    private String osType;

    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Long versionNo;

    public AppInfoModel() {
        super();
    }

    @Transient
    public Long getPrimaryKey() {
        return getAppInfoId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        return "appInfoId";
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameter = "&appInfoId=" + getAppInfoId();
        return parameter;
    }

    public void setPrimaryKey(Long primaryKey) {
        setAppInfoId(primaryKey);
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

        associationModel = new AssociationModel();
        associationModel.setClassName("AppModel");
        associationModel.setPropertyName("appModel");
        associationModel.setValue(getAppModel());
        associationModelList.add(associationModel);

        return associationModelList;
    }


    /**
     * @return the appId
     */
    @Transient
    public Long getAppId() {
        Long appId = null;
        if (appModel != null) {
            appId = appModel.getAppId();
        }
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(Long appId) {
        if (appModel == null)
            appModel = new AppModel();

        appModel.setAppId(appId);
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_ID")
    public AppModel getAppModel() {
        return appModel;
    }

    public void setAppModel(AppModel app) {
        if (null != app) {
            appModel = (AppModel) app.clone();
        }
    }


    @Column(name = "APP_INFO_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_INFO_SEQ")
    public Long getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(Long appInfoId) {
        this.appInfoId = appInfoId;
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
     * @return the url
     */
    @Column(name = "URL")
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the osType
     */
    @Column(name = "OS_TYPE")
    public String getOsType() {
        return osType;
    }

    /**
     * @param osType the osType to set
     */
    public void setOsType(String osType) {
        this.osType = osType;
    }


}
