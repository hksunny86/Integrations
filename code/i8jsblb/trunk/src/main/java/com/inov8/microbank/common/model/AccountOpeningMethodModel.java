package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Zeeshan on 10/26/2016.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACCOUNT_OPENING_METHOD_seq",sequenceName = "ACCOUNT_OPENING_METHOD_seq", allocationSize=1)
@Table(name = "ACCOUNT_OPENING_METHOD")
public class AccountOpeningMethodModel extends BasePersistableModel implements Serializable {

    private static final long serialVersionUID = 3832026162173359411L;

    // Fields
    private Long accountOpeningMethodId;
    private String name;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;




    @Column(name = "ACCOUNT_OPENING_METHOD_ID" , nullable = false )
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCOUNT_OPENING_METHOD_seq")
    public Long getAccountOpeningMethodId() {
        return accountOpeningMethodId;
    }

    public void setAccountOpeningMethodId(Long accountOpeningMethodId) {
        this.accountOpeningMethodId = accountOpeningMethodId;
    }

    @Column(name ="UPDATED_ON")
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

    public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
        this.createdByAppUserModel = createdByAppUserModel;
    }

    @javax.persistence.Transient
    public Long getCreatedBy()
    {
        Long createdByAppUserId = null;
        if(createdByAppUserModel != null)
        {
            createdByAppUserId = createdByAppUserModel.getAppUserId();
        }
        return createdByAppUserId;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getUpdatedByAppUserModel() {
        return updatedByAppUserModel;
    }

    public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
        this.updatedByAppUserModel = updatedByAppUserModel;
    }

    @javax.persistence.Transient
    public Long getUpdatedBy()
    {
        Long updatedByAppUserId = null;
        if(updatedByAppUserModel != null)
        {
            updatedByAppUserId = updatedByAppUserModel.getAppUserId();
        }
        return updatedByAppUserId;
    }

    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name ="NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setAccountOpeningMethodId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getAccountOpeningMethodId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        return "&accountOpeningMethodId="+getAccountOpeningMethodId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        return "accountOpeningMethodId";
    }
}
