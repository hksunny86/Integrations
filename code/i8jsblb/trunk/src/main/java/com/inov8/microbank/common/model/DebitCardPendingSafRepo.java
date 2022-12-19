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
@javax.persistence.SequenceGenerator(name = "DEBIT_CARD_SAF_REPO_SEQ", sequenceName = "DEBIT_CARD_SAF_REPO_SEQ", allocationSize = 1)
@Table(name = "DEBIT_CARD_SAF_REPO")
public class DebitCardPendingSafRepo extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long debitCardSafRepoId;
    private String mobileNo;
    private String cnic;
    private Long registrationStateId;
    private String isCompleted;
    private String debitCardRegectionReason;
    private Long productId;
    private String cardDescription;
    private String appId;
    private String email;
    private String cardTypeId;
    private String segmentId;
    private String agentId;
    private String deviceTypeId;
    private Date createdOn;
    private Long createdBy;
    private Date updatedOn;
    private Long updatedBy;

    @Id
    @Column(name = "DEBIT_CARD_SAF_REPO_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_CARD_SAF_REPO_SEQ")
    public Long getDebitCardSafRepoId() {
        return debitCardSafRepoId;
    }

    public void setDebitCardSafRepoId(Long debitCardSafRepoId) {
        this.debitCardSafRepoId = debitCardSafRepoId;
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

    @Column(name = "CARD_TYPE_ID ")
    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    @Column(name = "REGISTRATION_STATE_ID")
    public Long getRegistrationStateId() {
        return registrationStateId;
    }

    public void setRegistrationStateId(Long registrationStateId) {
        this.registrationStateId = registrationStateId;
    }
    @Column(name = "IS_COMPLETE")
    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }


    @Column(name = "DEBIT_CARD_REJECTION_REASON")
    public String getDebitCardRegectionReason() {
        return debitCardRegectionReason;
    }

    public void setDebitCardRegectionReason(String debitCardRegectionReason) {
        this.debitCardRegectionReason = debitCardRegectionReason;
    }

    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    @Column(name = "CARD_DESCRIPTION")
    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    @Column(name = "APP_ID")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "MAILING_ADDRESS")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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



    @Column(name = "SEGMENT_ID")
    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }
    @Column(name = "AGENT_ID")
    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    @Column(name = "DEVICE_TYPE_ID")
    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getDebitCardSafRepoId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "debitCardSafRepoId";
        return primaryKeyFieldName;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&debitCardSafRepoId=" + getDebitCardSafRepoId();
        return parameters;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setDebitCardSafRepoId(primaryKey);

    }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getDebitCardSafRepoId();
        checkBox += "\"/>";
        return checkBox;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DebitCardPendingSafRepo model = new DebitCardPendingSafRepo();
        model.setDebitCardSafRepoId(resultSet.getLong("ACCOUNT_PENDING_SAF_REPO_ID"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setAppId(resultSet.getString("APP_ID"));
        model.setEmail(resultSet.getString("EMAIL"));
        model.setCardDescription(resultSet.getString("CARD_DESCRIPTION"));
        model.setRegistrationStateId(resultSet.getLong("REGISTRATION_STATE_ID"));
        model.setProductId(resultSet.getLong("PRODUCT_ID"));
        model.setDebitCardRegectionReason(resultSet.getString("DEBIT_CARD_REJECTION_REASON"));
        model.setSegmentId((resultSet.getString("SEGMENT_ID")));
        model.setCardTypeId(resultSet.getString("CARD_TYPE_ID"));
        model.setDeviceTypeId(resultSet.getString("DEVICE_TYPE_ID"));
        model.setAgentId(resultSet.getString("AGENT_ID"));
        model.setIsCompleted(resultSet.getString("IS_COMPLETE"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        return model;
    }
}
