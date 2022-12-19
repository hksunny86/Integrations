package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BLINK_LIMIT_SEQ",sequenceName = "BLINK_LIMIT_SEQ", allocationSize=1)
@Table(name = "BLINK_CUSTOMER_LIMIT")
public class BlinkCustomerLimitModel extends BasePersistableModel implements Serializable {
    private Long limitId;
    private Long customerId;
    private Long minimum;
    private Long maximum;
    private Long customerAccTypeId;
    private Long transactionType;
    private Long isApplicable;
    private String createdBy;
    private String updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Long limitType;
    private Long accountId;

    @Transient
    @Override
    public void setPrimaryKey(Long aLong) {
        setLimitId(aLong);
    }
    @Transient
    @Override
    public Long getPrimaryKey() {

        return getLimitId();
    }
    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&limitId=" + getLimitId();
        return parameters;
    }
    @Transient
    @Override
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "limitId";
        return primaryKeyFieldName;
    }

    @Column(name = "LIMIT_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BLINK_LIMIT_SEQ")
    public Long getLimitId() {
        return limitId;
    }

    public void setLimitId(Long limitId) {
        this.limitId = limitId;
    }


    @Column(name = "MINIMUM" )
    public Long getMinimum() {
        return minimum;
    }

    public void setMinimum(Long minimum) {
        this.minimum = minimum;
    }

    @Column(name = "TRANSACTION_TYPE" )
    public Long getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Long transactionType) {
        this.transactionType = transactionType;
    }
    @Column(name = "ACCOUNT_ID" )
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Column(name = "MAXIMUM" )

    public Long getMaximum() {
        return maximum;
    }

    public void setMaximum(Long maximum) {
        this.maximum = maximum;
    }

    @Column(name = "CUSTOMER_ACC_TYPE_ID" )
    public Long getCustomerAccTypeId() {
        return customerAccTypeId;
    }

    public void setCustomerAccTypeId(Long customerAccTypeId) {
        this.customerAccTypeId = customerAccTypeId;
    }
    @Column(name = "IS_APPLICABLE" )
    public Long getIsApplicable() {
        return isApplicable;
    }

    public void setIsApplicable(Long isApplicable) {
        this.isApplicable = isApplicable;
    }
    @Column(name = "CREATED_BY" )
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    @Column(name = "UPDATED_BY" )
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    @Column(name = "CREATED_ON" )
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    @Column(name = "UPDATED_ON" )
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    @Column(name = "LIMIT_TYPE" )
    public Long getLimitType() {
        return limitType;
    }

    public void setLimitType(Long limitType) {
        this.limitType = limitType;
    }
    @Column(name = "CUSTOMER_ID" )
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
