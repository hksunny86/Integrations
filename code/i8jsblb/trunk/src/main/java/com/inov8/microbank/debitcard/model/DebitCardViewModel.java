package com.inov8.microbank.debitcard.model;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DEBIT_CARD_REQUESTS_INFO_VIEW")
public class DebitCardViewModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long debitCardId;
    private String cardNo;
    private String customerName;
    private String embossingName;
    private String mobileNo;
    private String nic;
    private String cardStatus;
    private String cardState;
    private String channel;
    private String channelId;
    private String createdBy;
    private java.sql.Timestamp createdOn;
    private String updatedBy;
    private java.sql.Timestamp updatedOn;
    private Date createdOnStartDate;
    private Date createdOnEndDate;
    private Date updatedOnStartDate;
    private Date updatedOnEndDate;
    private String mailingAddress;
    private String motherName;
    private String nicExpiry;
    private Long cardStateId;
    private Long cardStatusId;
    private Long createdByAppUserId;
    private Long updatedByAppUserId;

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setDebitCardId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getDebitCardId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&debitCardId=" + getDebitCardId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "debitCardId";
        return primaryKeyFieldName;
    }

    @Column(name = "CUSTOMER_NAME")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "EMBOSING_NAME")
    public String getEmbossingName() {
        return embossingName;
    }

    public void setEmbossingName(String embossingName) {
        this.embossingName = embossingName;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "NIC")
    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    @Column(name = "CARD_STATUS")
    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Column(name = "CARD_STATE")
    public String getCardState() {
        return cardState;
    }

    public void setCardState(String cardState) {
        this.cardState = cardState;
    }

    @Column(name = "CHANNEL_NAME")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Column(name = "CREATED_BY_NAME")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "CREATED_ON")
    public java.sql.Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(java.sql.Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_BY_NAME")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "UPDATED_ON")
    public java.sql.Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(java.sql.Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "DEBIT_CARD_ID", nullable = false)
    @Id
    public Long getDebitCardId() {
        return debitCardId;
    }

    public void setDebitCardId(Long debitCardId) {
        this.debitCardId = debitCardId;
    }

    @javax.persistence.Transient
    public Date getCreatedOnStartDate() {
        return createdOnStartDate;
    }

    public void setCreatedOnStartDate(Date createdOnStartDate) {
        this.createdOnStartDate = createdOnStartDate;
    }

    @javax.persistence.Transient
    public Date getCreatedOnEndDate() {
        return createdOnEndDate;
    }

    public void setCreatedOnEndDate(Date createdOnEndDate) {
        this.createdOnEndDate = createdOnEndDate;
    }

    @javax.persistence.Transient
    public Date getUpdatedOnStartDate() {
        return updatedOnStartDate;
    }

    public void setUpdatedOnStartDate(Date updatedOnStartDate) {
        this.updatedOnStartDate = updatedOnStartDate;
    }

    @javax.persistence.Transient
    public Date getUpdatedOnEndDate() {
        return updatedOnEndDate;
    }

    public void setUpdatedOnEndDate(Date updatedOnEndDate) {
        this.updatedOnEndDate = updatedOnEndDate;
    }

    @Column(name = "ADDRESS")
    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    @Column(name = "MOTHER_NAME")
    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    @Column(name = "NIC_EXPIRY")
    public String getNicExpiry() {
        return nicExpiry;
    }

    public void setNicExpiry(String nicExpiry) {
        this.nicExpiry = nicExpiry;
    }

    @Column(name = "CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Column(name = "CARD_STATE_ID")
    public Long getCardStateId() {
        return cardStateId;
    }

    public void setCardStateId(Long cardStateId) {
        this.cardStateId = cardStateId;
    }

    @Column(name = "CARD_STATUS_ID")
    public Long getCardStatusId() {
        return cardStatusId;
    }

    public void setCardStatusId(Long cardStatusId) {
        this.cardStatusId = cardStatusId;
    }

    @Column(name = "CREATED_BY_ID")
    public Long getCreatedByAppUserId() {
        return createdByAppUserId;
    }

    public void setCreatedByAppUserId(Long createdByAppUserId) {
        this.createdByAppUserId = createdByAppUserId;
    }

    @Column(name = "UPDATED_BY_ID")
    public Long getUpdatedByAppUserId() {
        return updatedByAppUserId;
    }

    public void setUpdatedByAppUserId(Long updatedByAppUserId) {
        this.updatedByAppUserId = updatedByAppUserId;
    }

    @Column(name = "CARD_NO")
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DebitCardViewModel vo = new DebitCardViewModel();

        vo.setDebitCardId(resultSet.getLong("DEBIT_CARD_ID"));
        vo.setCardNo(resultSet.getString("CARD_NO"));
        vo.setCustomerName(resultSet.getString("CUSTOMER_NAME"));
        vo.setEmbossingName(resultSet.getString("EMBOSING_NAME"));
        vo.setMobileNo(resultSet.getString("MOBILE_NO"));
        vo.setNic(resultSet.getString("NIC"));
        vo.setCardStatus(resultSet.getString("CARD_STATUS"));
        vo.setCardState(resultSet.getString("CARD_STATE"));
        vo.setChannel(resultSet.getString("CHANNEL_NAME"));
        vo.setChannelId(resultSet.getString("CHANNEL_ID"));
        vo.setCreatedBy(resultSet.getString("CREATED_BY_NAME"));
        vo.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        vo.setUpdatedBy(resultSet.getString("UPDATED_BY_NAME"));
        vo.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        vo.setMailingAddress(resultSet.getString("ADDRESS"));
        vo.setMotherName(resultSet.getString("MOTHER_NAME"));
        vo.setNicExpiry(resultSet.getString("NIC_EXPIRY"));
        vo.setCardStateId(resultSet.getLong("CARD_STATE_ID"));
        vo.setCardStatusId(resultSet.getLong("CARD_STATUS_ID"));
        vo.setCreatedByAppUserId(resultSet.getLong("CREATED_BY_ID"));
        vo.setUpdatedByAppUserId(resultSet.getLong("UPDATED_BY_ID"));

        return null;
    }
}
