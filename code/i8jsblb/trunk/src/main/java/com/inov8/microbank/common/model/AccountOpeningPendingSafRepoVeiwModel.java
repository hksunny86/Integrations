package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "ACCOUNT_PENDING_SAF_REPO_VIEW")
public class AccountOpeningPendingSafRepoVeiwModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {
    private Long accountOpeningPendingSafRepoId;
    private String mobileNo;
    private String cnic;
    private Long accountStateId;
    private String customerName;
    private Long registrationStateId;
    private String accountState;
    private String registrationstate;
    private String product;
    private String isCompleted;
    private String clsResponseCode;
    private Long productId;
    private Date createdOn;
    private Long createdBy;
    private Date updatedOn;
    private Long updatedBy;
    private Long appId;
    private String initialDeposit;
    private String agentMobileNo;
    private Date createdOnToDate;

    @Id
    @Column(name = "ACCOUNT_PENDING_SAF_REPO_ID", nullable = false)
    public Long getAccountOpeningPendingSafRepoId() {
        return accountOpeningPendingSafRepoId;
    }

    public void setAccountOpeningPendingSafRepoId(Long accountOpeningPendingSafRepoId) {
        this.accountOpeningPendingSafRepoId = accountOpeningPendingSafRepoId;
    }



    @javax.persistence.Transient
    public Date getCreatedOnToDate() { return createdOnToDate; }

    public void setCreatedOnToDate(Date createdOnToDate) { this.createdOnToDate = createdOnToDate; }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "CNIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
    @Column(name = "ACCOUNT_STATE_ID")
    public Long getAccountStateId() {
        return accountStateId;
    }

    public void setAccountStateId(Long accountStateId) {
        this.accountStateId = accountStateId;
    }

    @Column(name = "REGISTRATION_STATE_ID")
    public Long getRegistrationStateId() {
        return registrationStateId;
    }

    public void setRegistrationStateId(Long registrationStateId) {
        this.registrationStateId = registrationStateId;
    }
    @Column(name = "IS_COMPLETE")
    public String getCompleted() {
        return isCompleted;
    }

    public void setCompleted(String completed) {
        isCompleted = completed;
    }
    @Column(name = "CLS_RESPONSE_CODE")
    public String getClsResponseCode() {
        return clsResponseCode;
    }

    public void setClsResponseCode(String clsResponseCode) {
        this.clsResponseCode = clsResponseCode;
    }
    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "CREATED_BY")
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "UPDATED_BY")
    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "ACCOUNT_STATE")

    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    @Column(name = "REGISTRATION_STATE")

    public String getRegistrationstate() {
        return registrationstate;
    }

    public void setRegistrationstate(String registrationstate) {
        this.registrationstate = registrationstate;
    }

    @Column(name = "PRODUCT")

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Column(name = "CUSTOMER_NAME")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "APP_ID")
    public Long getAppId() { return appId; }

    public void setAppId(Long appId) { this.appId = appId; }

    @Column(name = "INITIAL_DEPOSIT")
    public String getInitialDeposit() { return initialDeposit; }

    public void setInitialDeposit(String initialDeposit) { this.initialDeposit = initialDeposit; }

    @Column(name = "AGENT_MOBILE_NUMBER")
    public String getAgentMobileNo() { return agentMobileNo; }

    public void setAgentMobileNo(String agentMobileNo) { this.agentMobileNo = agentMobileNo; }

    @Transient
    public Long getPrimaryKey() {
        return getAccountOpeningPendingSafRepoId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "accountOpeningPendingSafRepoId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&accountOpeningPendingSafRepoId=" + getAccountOpeningPendingSafRepoId();
        return parameters;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setAccountOpeningPendingSafRepoId(primaryKey);

    }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getAccountOpeningPendingSafRepoId();
        checkBox += "\"/>";
        return checkBox;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AccountOpeningPendingSafRepoVeiwModel model = new AccountOpeningPendingSafRepoVeiwModel();
        model.setAccountOpeningPendingSafRepoId(resultSet.getLong("ACCOUNT_PENDING_SAF_REPO_ID"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setAccountStateId(resultSet.getLong("ACCOUNT_STATE_ID"));
        model.setRegistrationStateId(resultSet.getLong("REGISTRATION_STATE_ID"));
        model.setProductId(resultSet.getLong("PRODUCT_ID"));
        model.setClsResponseCode(resultSet.getString("CLS_RESPONSE_CODE"));
        model.setCompleted(resultSet.getString("IS_COMPLETE"));
        model.setAppId(resultSet.getLong("APP_ID"));
        model.setAgentMobileNo(resultSet.getString("AGENT_MOBILE_NUMBER"));
        model.setInitialDeposit(resultSet.getString("INITIAL_DEPOSIT"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        return model;
    }
}
