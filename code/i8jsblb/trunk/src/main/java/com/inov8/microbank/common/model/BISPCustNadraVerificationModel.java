package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BISP_CUST_NADRA_SEQ",sequenceName = "BISP_CUST_NADRA_SEQ", allocationSize=1)
@Table(name = "BISP_CUST_NADRA_VERIFICATION")
public class BISPCustNadraVerificationModel extends BasePersistableModel implements Serializable {
    private Long pk;
    private String agentId;
    private Long customerId;
    private Boolean isBVSRequired;
    private Timestamp createdOn;
    private String businessDate;
    private String cNic;
    private String mobileNo;
    private String transactionCode;
    private Long retryCount;
    private String responseCode;
    private String baflTransactionNumber;
    private String baflSessionId;
    private String nadraSessionId;
    private String baflWalletId;
    private Long appUserTypeId;

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPk(primaryKey);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BISP_CUST_NADRA_SEQ")
    @Column(name = "PK"  )
    public Long getPk() { return pk; }
    public void setPk(Long pk) { this.pk = pk; }

    @Column(name="AGENT_ID")
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    @Column(name="CUSTOMER_ID")
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    @Column(name="IS_BVS_VERIFIED")
    public Boolean getBVSRequired() { return isBVSRequired; }
    public void setBVSRequired(Boolean BVSRequired) { isBVSRequired = BVSRequired; }

    @Column(name="CREATED_ON")
    public Timestamp getCreatedOn() { return createdOn; }
    public void setCreatedOn(Timestamp createdOn) { this.createdOn = createdOn; }

    @Column(name="BUSINESS_DATE")
    public String getBusinessDate() { return businessDate; }
    public void setBusinessDate(String businessDate) { this.businessDate = businessDate; }

    @Column(name="CNIC")
    public String getcNic() {
        return cNic;
    }

    public void setcNic(String cNic) {
        this.cNic = cNic;
    }

    @Column(name="MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name="TRANSACTION_CODE")
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Column(name="RETRY_COUNT")
    public Long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Long retryCount) {
        this.retryCount = retryCount;
    }

    @Column(name="RESPONSE_CODE")
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Column(name="BAFL_TRANSACTION_NUMBER")
    public String getBaflTransactionNumber() {
        return baflTransactionNumber;
    }

    public void setBaflTransactionNumber(String baflTransactionNumber) {
        this.baflTransactionNumber = baflTransactionNumber;
    }

    @Column(name="BAFL_SESSION_ID")
    public String getBaflSessionId() {
        return baflSessionId;
    }

    public void setBaflSessionId(String baflSessionId) {
        this.baflSessionId = baflSessionId;
    }

    @Column(name="NADRA_SESSION_ID")
    public String getNadraSessionId() {
        return nadraSessionId;
    }

    public void setNadraSessionId(String nadraSessionId) {
        this.nadraSessionId = nadraSessionId;
    }

    @Column(name="BAFL_WALLET_ID")
    public String getBaflWalletId() {
        return baflWalletId;
    }

    public void setBaflWalletId(String baflWalletId) {
        this.baflWalletId = baflWalletId;
    }

    @Column(name="APP_USER_TYPE_ID")
    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }
}
