package com.inov8.microbank.cardconfiguration.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CARD_STATE")
public class CardStateModel extends BasePersistableModel implements Serializable {

    private Long cardStateId;
    private String name;
    private String code;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;

    // Constructors

    /**
     * default constructor
     */
    public CardStateModel()
    {
    }

    /**
     * minimal constructor
     */
    public CardStateModel(Long cardStateId)
    {
        this.cardStateId = cardStateId;
    }

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getCardStateId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey)
    {
        setCardStateId(primaryKey);
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&cardStateId="+getCardStateId();
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        return "cardStateId";
    }

    @Id
    @Column(name = "CARD_STATE_ID")
    public Long getCardStateId()
    {
        return cardStateId;
    }

    public void setCardStateId(Long cardStateId)
    {
        this.cardStateId = cardStateId;
    }

    @Column(name = "NAME")
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name="CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")
    public AppUserModel getCreatedByAppUserModel()
    {
        return createdByAppUserModel;
    }

    public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel)
    {
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
    public AppUserModel getUpdatedByAppUserModel()
    {
        return updatedByAppUserModel;
    }

    public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel)
    {
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

    @Column(name = "CREATED_ON")
    public Date getCreatedOn()
    {
        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn()
    {
        return this.updatedOn;
    }

    public void setUpdatedOn(Date updatedOn)
    {
        this.updatedOn = updatedOn;
    }

    @Column(name = "VERSION_NO")
    public Integer getVersionNo()
    {
        return this.versionNo;
    }

    public void setVersionNo(Integer versionNo)
    {
        this.versionNo = versionNo;
    }
}
