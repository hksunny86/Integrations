package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "THIRD_PARTY_ACCOUNT_OPENING_V")
public class ThirdPartyAccountOpeningViewModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = 7059945306873740626L;

    private Long pk;
    private String agentId;
    private String customerMobileNumber;
    private String customerNic;
    private Long bankId;
    private Date createdOn;
    private Timestamp updatedOn;
    private Long productId;
    private Boolean status;
    private String debitCardNumber;

    private Date createdOnToDate;

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
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

    @Id
    @Column(name = "PK"  )
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Column(name = "AGENT_ID")
    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Column(name = "MOBILE_NUMBER")
    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    @Column(name = "CUST_CNIC")
    public String getCustomerNic() {
        return customerNic;
    }

    public void setCustomerNic(String customerNic) {
        this.customerNic = customerNic;
    }

    @Column(name = "BANK_ID")
    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() { return createdOn; }

    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    @Column(name = "UPDATED_ON")
    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "STATUS")
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Column(name = "CARD_NUMBER")
    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    @javax.persistence.Transient
    public Date getCreatedOnToDate() { return createdOnToDate; }

    public void setCreatedOnToDate(Date createdOnToDate) { this.createdOnToDate = createdOnToDate; }
}
