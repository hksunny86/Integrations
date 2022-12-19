package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACCOUNT_PENDING_SAF_REPO_SEQ", sequenceName = "ACCOUNT_PENDING_SAF_REPO_SEQ", allocationSize = 1)
@Table(name = "ACCOUNT_PENDING_SAF_REPO")
public class AccountOpeningPendingSafRepoModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {
    private Long accountOpeningPendingSafRepoId;
    private String mobileNo;
    private String cnic;
    private Long accountStateId;
    private Long registrationStateId;
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


    @Id
    @Column(name = "ACCOUNT_PENDING_SAF_REPO_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_PENDING_SAF_REPO_SEQ")
    public Long getAccountOpeningPendingSafRepoId() {
        return accountOpeningPendingSafRepoId;
    }

    public void setAccountOpeningPendingSafRepoId(Long accountOpeningPendingSafRepoId) {
        this.accountOpeningPendingSafRepoId = accountOpeningPendingSafRepoId;
    }


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

    @Column(name = "APP_ID")
    public Long getAppId() { return appId; }

    public void setAppId(Long appId) { this.appId = appId; }

    @Column(name = "INITIAL_DEPOSIT")
    public String getInitialDeposit() { return initialDeposit; }

    public void setInitialDeposit(String initialDeposit) { this.initialDeposit = initialDeposit; }

    @Column(name = "AGENT_MOBILE_NUMBER")
    public String getAgentMobileNo() { return agentMobileNo; }

    public void setAgentMobileNo(String agentMobileNo) { this.agentMobileNo = agentMobileNo; }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getAccountOpeningPendingSafRepoId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "accountOpeningPendingSafRepoId";
        return primaryKeyFieldName;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&accountOpeningPendingSafRepoId=" + getAccountOpeningPendingSafRepoId();
        return parameters;
    }

    @javax.persistence.Transient
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
        AccountOpeningPendingSafRepoModel model = new AccountOpeningPendingSafRepoModel();
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
