package com.inov8.microbank.demographics.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The CustomerDemographicsModel entity bean.
 *
 * @author Atieq Rehman
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DEMOGRAPHICS_SEQ", sequenceName = "DEMOGRAPHICS_SEQ", allocationSize = 1)
@Table(name = "DEMOGRAPHICS")
public class DemographicsModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 1854659766832597319L;

    private Long demographicsId;
    private String udid;
    private String os;
    private String osVersion;
    private String model;
    private String network;
    private String vendor;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private String deviceKey;
    private Boolean locked;
    private AppUserModel appUserIdAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private AppUserModel createdByAppUserModel;

    public DemographicsModel() {
    }

    public DemographicsModel(Long appUserId) {
        if(appUserIdAppUserModel == null){
            appUserIdAppUserModel = new AppUserModel();
        }

        appUserIdAppUserModel.setAppUserId(appUserId);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getDemographicsId();
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setDemographicsId(primaryKey);
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&demographicsId=" + getDemographicsId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        return "demographicsId";
    }


    @Column(name = "DEMOGRAPHICS_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEMOGRAPHICS_SEQ")
    public Long getDemographicsId() {
        return demographicsId;
    }

    public void setDemographicsId(Long demographicsId) {
        this.demographicsId = demographicsId;
    }

    @Column(name = "UDID")
    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    @Column(name = "OS", length = 50)
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Column(name = "OS_VERSION", length = 50)
    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Column(name = "MODEL", length = 50)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "NETWORK", length = 50)
    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Column(name = "VENDOR", length = 50)
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
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

    @Version
    @Column(name = "VERSION_NO", nullable = false)
    public Integer getVersionNo() {
        return versionNo;
    }

    @javax.persistence.Transient
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

    @javax.persistence.Transient
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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_ID")
    public AppUserModel getAppUserIdAppUserModel() {
        return appUserIdAppUserModel;
    }

    public void setAppUserIdAppUserModel(AppUserModel appUserModel) {
        this.appUserIdAppUserModel = appUserModel;
    }

    @javax.persistence.Transient
    public Long getAppUserId() {
        if (appUserIdAppUserModel != null) {
            return appUserIdAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    @Column(name = "DEVICE_KEY")
    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    @Column(name="IS_LOCKED")
    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @javax.persistence.Transient
    public void setAppUserId(Long appUserId) {
        if (appUserId == null) {
            appUserIdAppUserModel = null;
        } else {
            appUserIdAppUserModel = new AppUserModel();
            appUserIdAppUserModel.setAppUserId(appUserId);
        }
    }

    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {
        List<AssociationModel> associationModelList = new ArrayList<>();
        AssociationModel associationModel = new AssociationModel();
        associationModel.setClassName("AppUserModel");
        associationModel.setPropertyName("appUserIdAppUserModel");
        associationModel.setValue(getAppUserIdAppUserModel());

        associationModelList.add(associationModel);

        return associationModelList;
    }
}
