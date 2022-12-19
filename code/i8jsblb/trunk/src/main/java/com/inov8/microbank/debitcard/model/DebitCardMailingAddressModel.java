package com.inov8.microbank.debitcard.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DEBIT_CARD_MAILING_ADDRESS_SEQ", sequenceName = "DEBIT_CARD_MAILING_ADDRESS_SEQ", allocationSize = 1)
@Table(name = "DEBIT_CARD_MAILING_ADDRESS")
public class DebitCardMailingAddressModel extends BasePersistableModel implements Serializable, Cloneable {

    private Long mailingAddressId;
    private String mailingAddress;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;

    public DebitCardMailingAddressModel() {
    }
    public DebitCardMailingAddressModel(Long mailingAddressId) {
        setPrimaryKey(mailingAddressId);
    }
    @Override
    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setMailingAddressId(aLong);
    }

    @Override
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getMailingAddressId();
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&mailingAddressId=" + getMailingAddressId();
        return parameters;
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "mailingAddressId";
        return primaryKeyFieldName;
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
    /////Account UpdatedBy///////


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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_CARD_MAILING_ADDRESS_SEQ")
    @Column(name = "DEBIT_CARD_MAILING_ADDRESS_ID", nullable = false)
    public Long getMailingAddressId() {
        return mailingAddressId;
    }

    public void setMailingAddressId(Long mailingAddressId) {
        this.mailingAddressId = mailingAddressId;
    }

    @Column(name = "ADDRESS")
    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
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
