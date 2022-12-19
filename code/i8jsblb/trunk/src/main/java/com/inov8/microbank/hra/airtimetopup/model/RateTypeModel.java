package com.inov8.microbank.hra.airtimetopup.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "RATE_TYPE")
public class RateTypeModel extends BasePersistableModel implements Serializable, Cloneable {

    private Long rateTypeId;
    private String name;
    private Date createdOn;
    private Date updatedOn;
    private Double version;

    /*private Long createdByAppUserId;
    private Long updatedByAppUserId;*/


    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setRateTypeId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getRateTypeId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&rateTypeId=" + getRateTypeId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "rateTypeId";
        return primaryKeyFieldName;
    }

    @Id
    @Column(name = "RATE_TYPE_ID")
    public Long getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(long rateTypeId) {
        this.rateTypeId = rateTypeId;
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

    /*@Column(name = "VERSION")
    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }*/

    /*@Column(name = "CREATED_BY")
    public Long getCreatedByAppUserId() {
        return createdByAppUserId;
    }

    public void setCreatedByAppUserId(Long createdByAppUserId) {
        this.createdByAppUserId = createdByAppUserId;
    }

    @Column(name = "UPDATED_BY")
    public Long getUpdatedByAppUserId() {
        return updatedByAppUserId;
    }

    public void setUpdatedByAppUserId(Long updatedByAppUserId) {
        this.updatedByAppUserId = updatedByAppUserId;
    }*/

}
