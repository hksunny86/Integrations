package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Attique on 9/8/2018.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="THIRD_PARTY_RETRY_ADVICE_SEQ", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="THIRD_PARTY_RETRY_ADVICE_SEQ") } )
//@javax.persistence.SequenceGenerator(name = "MIDDLEWARE_RETRY_ADVICE_SEQ",sequenceName = "MIDDLEWARE_RETRY_ADVICE_SEQ")
@Table(name = "THIRD_PARTY_RETRY_ADVICE")
public class ThirdPartyRetryAdviceModel extends BasePersistableModel implements Serializable {

    private static final long serialVersionUID = 1640433713544167742L;

    private Long thirdPartyRetryAdviceId;
    private String transactionCode;
    private String transactionStatus;
    private Double transactionAmount;
    private String stan;
    private String status;
    private String sessionId;
    private String terminalId;
    private String customerAccountNumber;
    private String sellerCode;
    private String responseCode;
    private String transmissionTime;
    private Date requestTime;

    private String customerCnic;
    private String agentAccountNumber;
    private String projectCode;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date updatedOn;
    private Date createdOn;
    private Integer versionNo;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;

    @Column(name = "THIRD_PARTY_RETRY_ADVICE_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="THIRD_PARTY_RETRY_ADVICE_SEQ")
    public Long getThirdPartyRetryAdviceId() {
        return thirdPartyRetryAdviceId;
    }

    public void setThirdPartyRetryAdviceId(Long thirdPartyRetryAdviceId) {
        this.thirdPartyRetryAdviceId = thirdPartyRetryAdviceId;
    }

    @Column(name = "TRANSACTION_CODE")
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Column(name = "TRANSACTION_STATUS")
    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }



    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "SESSION_ID")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(name = "TERMINAL_ID")
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Column(name = "CUSTOMER_ACC_NUMBER")
    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    @Column(name = "SELLER_CODE")
    public String getSellerCode() {
        return sellerCode;
    }

    public void setSellerCode(String sellerCode) {
        this.sellerCode = sellerCode;
    }

    @Column(name = "RESPONSE_CODE")
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Column(name = "TRANSMISSION_TIME")
    public String getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(String transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

    @Column(name = "CUSTOMER_CNIC")
    public String getCustomerCnic() {
        return customerCnic;
    }

    public void setCustomerCnic(String customerCnic) {
        this.customerCnic = customerCnic;
    }

    @Column(name = "AGENT_ACC_NUMBER")
    public String getAgentAccountNumber() {
        return agentAccountNumber;
    }

    public void setAgentAccountNumber(String agentAccountNumber) {
        this.agentAccountNumber = agentAccountNumber;
    }

    @Column(name = "PROJECT_CODE")
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "VERSION_NO")
    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")
    public AppUserModel getRelationUpdatedByAppUserModel(){
        return updatedByAppUserModel;
    }

    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
        return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
        this.updatedByAppUserModel = appUserModel;
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
        if(null != appUserModel)
        {
            setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
        }
    }



    /**
     * Returns the value of the <code>appUserId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getCreatedBy() {
        if (createdByAppUserModel != null) {
            return createdByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     */

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
        if(appUserId == null)
        {
            createdByAppUserModel = null;
        }
        else
        {
            createdByAppUserModel = new AppUserModel();
            createdByAppUserModel.setAppUserId(appUserId);
        }
    }

    /**
     * Returns the value of the <code>appUserId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getUpdatedBy() {
        if (updatedByAppUserModel != null) {
            return updatedByAppUserModel.getAppUserId();
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
     */

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
        if(appUserId == null)
        {
            updatedByAppUserModel = null;
        }
        else
        {
            updatedByAppUserModel = new AppUserModel();
            updatedByAppUserModel.setAppUserId(appUserId);
        }
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getThirdPartyRetryAdviceId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setThirdPartyRetryAdviceId(primaryKey);
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&thirdPartyRetryAdviceId=" + getThirdPartyRetryAdviceId();
        return parameters;
    }
    /**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "thirdPartyRetryAdviceId";
        return primaryKeyFieldName;
    }

    @Column(name = "RESERVED_FILED_1")
    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    @Column(name = "RESERVED_FILED_2")
    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    @Column(name = "RESERVED_FILED_3")
    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    @Column(name = "RESERVED_FILED_4")
    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    @Column(name = "RESERVED_FILED_5")
    public String getReserve5() {
        return reserve5;
    }

    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }
}
