package com.inov8.microbank.tax.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Malik on 8/10/2016.
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "WHT_TRX_VIEW")
public class WHTTrxViewModel extends BasePersistableModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long pk;
    private Double amount;
    private Long whtConfigId;
    private String whtConfig;
    private Long status;
    private String statusName;
    private String desc;
    private Long appUserTypeId;
    private String appUserType;
    private String mobNo;
    private String name;
    private Long regStateId;
    private String regState;
    private Date createdOn;
    
    private Date transactionDate;

    @Column(name = "PK")
    @Id
    public Long getPk()
    {
        return pk;
    }

    public void setPk(Long pk)
    {
        this.pk = pk;
    }

    @Column(name = "AMOUNT",nullable = false)
    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    @Column(name = "WHT_CONFIG_ID",nullable = false)
    public Long getWhtConfigId()
    {
        return whtConfigId;
    }

    public void setWhtConfigId(Long whtConfigId)
    {
        this.whtConfigId = whtConfigId;
    }

    @Column(name = "WHT_CONFIG_NAME",nullable = false)
    public String getWhtConfig()
    {
        return whtConfig;
    }

    public void setWhtConfig(String whtConfig)
    {
        this.whtConfig = whtConfig;
    }

    @Column(name = "STATUS",nullable = false)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Column(name = "STATUS_NAME",nullable = false)
    public String getStatusName()
    {
        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    @Column(name = "DESCRIPTION")
    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    @Column(name = "APP_USER_TYPE_ID",nullable = false)
    public Long getAppUserTypeId()
    {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId)
    {
        this.appUserTypeId = appUserTypeId;
    }

    @Column(name = "APP_USER_TYPE_NAME",nullable = false)
    public String getAppUserType()
    {
        return appUserType;
    }

    public void setAppUserType(String appUserType)
    {
        this.appUserType = appUserType;
    }


    @Column(name = "MOBILE_NO",nullable = false)
    public String getMobNo()
    {
        return mobNo;
    }

    public void setMobNo(String mobNo)
    {
        this.mobNo = mobNo;
    }

    @Column(name = "APP_USER_NAME")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "REGISTRATION_STATE_NAME",nullable = false)
    public String getRegState()
    {
        return regState;
    }

    public void setRegState(String regState)
    {
        this.regState = regState;
    }

    @Column(name = "REGISTRATION_STATE_ID")
    public Long getRegStateId()
    {
        return regStateId;
    }

    public void setRegStateId(Long regStateId)
    {
        this.regStateId = regStateId;
    }

    @Column(name = "CREATED_ON",nullable = false)
    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey()
    {
        return this.pk;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Column(name = "TRANSACTION_DATE")
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
}

