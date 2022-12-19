package com.inov8.microbank.mobilenetworks.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "MOBILE_NETWORK_SEQ", sequenceName = "MOBILE_NETWORK_SEQ", allocationSize = 1)
@Table(name = "MOBILE_NETWORKS")
public class MobileNetworkModel extends BasePersistableModel implements Serializable,Cloneable{

    private Long mobileNetworkId;
    private String networkName;
    private AppUserModel createdByAppUserModel;
    private Date createdOn;
    private AppUserModel updatedByAppUserModel;
    private Date updatedOn;

    public MobileNetworkModel()
    {

    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setMobileNetworkId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getMobileNetworkId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&mobileNetworkId=" + getMobileNetworkId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "mobileNetworkId";
        return primaryKeyFieldName;
    }

    /////Mobile Network CreatedBy///////

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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOBILE_NETWORK_SEQ")
    @Column(name = "MOBILE_NETWORK_ID", nullable = false)
    public Long getMobileNetworkId() {
        return mobileNetworkId;
    }

    public void setMobileNetworkId(Long mobileNetworkId) {
        this.mobileNetworkId = mobileNetworkId;
    }

    @Column(name = "NAME")
    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
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
