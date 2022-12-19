package com.inov8.microbank.tax.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DATE_WISE_TX_AMOUNT_VIEW")
public class DateWiseUserWHTAmountViewModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long pk;
    private Long appUserId;
    private Long appUserTypeId;
    private String mobileNo;
    private String accountNick;
    private Double transactionAmount;
    private WHTConfigModel whtConfigIdWhtConfigModel;
    private Long smartMoneyAccountId;
    private Date transactionDate;


    @Column(name = "PK")
    @Id
    public Long getPk() {
        return pk;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "WHT_CONFIG_ID")
    public WHTConfigModel getRelationWhtConfigIdWhtConfigModel() {
        return this.whtConfigIdWhtConfigModel;
    }

    @javax.persistence.Transient
    public void setRelationWhtConfigIdWhtConfigModel(WHTConfigModel whtConfigIdWhtConfigModel) {
        this.whtConfigIdWhtConfigModel = whtConfigIdWhtConfigModel;
    }

    @javax.persistence.Transient
    public WHTConfigModel getWhtConfigIdWhtConfigModel() {
        return getRelationWhtConfigIdWhtConfigModel();
    }

    @javax.persistence.Transient
    public void setWhtConfigIdWhtConfigModel(WHTConfigModel whtConfigIdWhtConfigModel) {
        if (null != whtConfigIdWhtConfigModel) {
            setRelationWhtConfigIdWhtConfigModel((WHTConfigModel) whtConfigIdWhtConfigModel.clone());
        }
    }

    @javax.persistence.Transient
    public Long getWhtConfigId() {
        Long whtConfigId = null;
        if (whtConfigIdWhtConfigModel != null) {
            whtConfigId = whtConfigIdWhtConfigModel.getWhtConfigId();
        }
        return whtConfigId;
    }

    public void setWhtConfigId(Long whtConfigId) {
        if (null != whtConfigId) {
            this.whtConfigIdWhtConfigModel = new WHTConfigModel();
            whtConfigIdWhtConfigModel.setWhtConfigId(whtConfigId);
        } else {
            whtConfigIdWhtConfigModel = null;
        }

    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Column(name = "APP_USER_ID")
    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Column(name = "SENDER_ACCOUNT_NICK")
    public String getAccountNick() {
        return accountNick;
    }

    public void setAccountNick(String accountNick) {
        this.accountNick = accountNick;
    }

    @Column(name = "SMART_MONEY_ACCOUNT_ID")
    public Long getSmartMoneyAccountId() {
        return smartMoneyAccountId;
    }

    public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
        this.smartMoneyAccountId = smartMoneyAccountId;
    }


    @Column(name = "APP_USER_TYPE_ID")
    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "TRANSACTION_DATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    @Transient
    public Long getPrimaryKey() {
        return this.pk;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList() {

        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
        AssociationModel associationModel = new AssociationModel();

        associationModel.setClassName("WHTConfigModel");
        associationModel.setPropertyName("relationWhtConfigIdWhtConfigModel");
        associationModel.setValue(getRelationWhtConfigIdWhtConfigModel());

        return associationModelList;
    }
}

