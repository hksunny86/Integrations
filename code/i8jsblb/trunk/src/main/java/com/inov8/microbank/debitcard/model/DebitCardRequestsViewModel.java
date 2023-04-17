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
@Table(name = "DEBIT_CARD_REQUESTS_VIEW")
public class DebitCardRequestsViewModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper{

    private Long debitCardId;
    private Long appUserId;
    private String mobileNo;
    private String cnic;
    private String channelId;
    private String channelName;
    private Long segmentId;
    private String segmentName;
    private String embossingName;
    private String nadraName;
    private String mailingAddress;
    private String reason;
    private String cardStatus;
    private String cardState;
    private Double cardFee;
    private String cardNumber;
    private String createdByAppUser;
    private Long updatedByAppUserId;
    private Date createdOn;
    private Date updatedOn;
    private Date createdOnStartDate;
    private Date createdOnEndDate;
    private Date reIssuancecreatedOnStartDate;
    private Date reIssuancecreatedOnEndDate;
    private Long cardStateId;
    private Long cardStatusId;
    private Long cardProductCodeId;
    private Long mailingAddressId;
    private String cardProductType;
    private String cardTypeCode;
    private String reissuance;
    private String isApprovedDenied;
    private String isReIssuanceApprovedDenied;
    private String isApproved;
    private String isReIssuanceApproved;
    private Long checkedBy;
    private String checkedByName;
    private String debitCardFee;
    private Date reissuanceRequestDate;

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

    @Column(name = "DEBIT_CARD_ID", nullable = false)
    @Id
    public Long getDebitCardId() {return debitCardId;}

    public void setDebitCardId(Long debitCardId) {this.debitCardId = debitCardId;}

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {return mobileNo;}

    public void setMobileNo(String mobileNo) {this.mobileNo = mobileNo;}

    @Column(name = "NIC")
    public String getCnic() {return cnic;}

    public void setCnic(String cnic) {this.cnic = cnic;}

    @Column(name = "CHANNEL_ID")
    public String getChannelId() {return channelId;}

    public void setChannelId(String channelId) {this.channelId = channelId;}

    @Column(name = "CHANNEL_NAME")
    public String getChannelName() {return channelName;}

    public void setChannelName(String channelName) {this.channelName = channelName;}

    @Column(name = "SEGMENT_NAME")
    public String getSegmentName() {return segmentName;}

    public void setSegmentName(String segmentName) {this.segmentName = segmentName;}

    @Column(name = "SEGMENT_ID")
    public Long getSegmentId() {return segmentId;}

    public void setSegmentId(Long segmentId) {this.segmentId = segmentId;}

    @Column(name = "EMBOSING_NAME")
    public String getEmbossingName() {return embossingName;}

    public void setEmbossingName(String embossingName) {this.embossingName = embossingName;}

    @Column(name = "CUSTOMER_NAME")
    public String getNadraName() {return nadraName;}

    public void setNadraName(String nadraName) {this.nadraName = nadraName;}

    @Column(name = "ADDRESS")
    public String getMailingAddress() {return mailingAddress;}

    public void setMailingAddress(String mailingAddress) {this.mailingAddress = mailingAddress;}

    @Column(name = "CARD_STATUS")
    public String getCardStatus() {return cardStatus;}

    public void setCardStatus(String cardStatus) {this.cardStatus = cardStatus;}

    @Column(name = "CARD_STATE")
    public String getCardState() {return cardState;}

    public void setCardState(String cardState) {this.cardState = cardState;}
//
//    @Column(name = "CARD_FEE")
//    public Double getCardFee() {return cardFee;}
//
//    public void setCardFee(Double cardFee) {this.cardFee = cardFee;}

    @Column(name = "CARD_NO")
    public String getCardNumber() {return cardNumber;}

    public void setCardNumber(String cardNumber) {this.cardNumber = cardNumber;}

    @Column(name = "UPDATED_BY")
    public Long getUpdatedByAppUserId() {return updatedByAppUserId;}

    public void setUpdatedByAppUserId(Long updatedByAppUserId) {this.updatedByAppUserId = updatedByAppUserId;}

    @Column(name = "CREATED_ON")
    public Date getCreatedOn() {return createdOn;}

    public void setCreatedOn(Date createdOn) {this.createdOn = createdOn;}

    @Column(name = "UPDATED_ON")
    public Date getUpdatedOn() {return updatedOn;}

    public void setUpdatedOn(Date updatedOn) {this.updatedOn = updatedOn;}

    @Column(name = "CARD_STATE_ID")
    public Long getCardStateId() {return cardStateId;}

    public void setCardStateId(Long cardStateId) {this.cardStateId = cardStateId;}

    @Column(name = "CARD_STATUS_ID")
    public Long getCardStatusId() {return cardStatusId;}

    public void setCardStatusId(Long cardStatusId) {this.cardStatusId = cardStatusId;}

    @Column(name = "APP_USER_ID", nullable = false)
    public Long getAppUserId() {return appUserId;}

    public void setAppUserId(Long appUserId) {this.appUserId = appUserId;}

    @Column(name = "CARD_PRODUCT_TYPE", nullable = false)
    public String getCardProductType() {return cardProductType;}

    public void setCardProductType(String cardProductType) {this.cardProductType = cardProductType;
    }

    @Column(name = "CARD_TYPE_CODE", nullable = false)
    public String getCardTypeCode() {return cardTypeCode;}

    public void setCardTypeCode(String cardTypeCode) {this.cardTypeCode = cardTypeCode;}

    @Column(name = "MAILING_ADDRESS_ID")
    public Long getMailingAddressId() {return mailingAddressId;}

    public void setMailingAddressId(Long mailingAddressId) {this.mailingAddressId = mailingAddressId;}

    @Column(name = "CARD_PRODUCT_TYPE_ID")
    public Long getCardProductCodeId() {return cardProductCodeId;}

    public void setCardProductCodeId(Long cardProductCodeId) {this.cardProductCodeId = cardProductCodeId;}

    @Column(name = "IS_REISSUED")
    public String getReissuance() {return reissuance;}

    public void setReissuance(String reissuance) {this.reissuance = reissuance;}

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
    public Date getReIssuancecreatedOnStartDate() {
        return reIssuancecreatedOnStartDate;
    }

    public void setReIssuancecreatedOnStartDate(Date reIssuancecreatedOnStartDate) {
        this.reIssuancecreatedOnStartDate = reIssuancecreatedOnStartDate;
    }
    @javax.persistence.Transient
    public Date getReIssuancecreatedOnEndDate() {
        return reIssuancecreatedOnEndDate;
    }

    public void setReIssuancecreatedOnEndDate(Date reIssuancecreatedOnEndDate) {
        this.reIssuancecreatedOnEndDate = reIssuancecreatedOnEndDate;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DebitCardRequestsViewModel vo = new DebitCardRequestsViewModel();

        vo.setDebitCardId(resultSet.getLong("DEBIT_CARD_ID"));
        vo.setMobileNo(resultSet.getString("MOBILE_NO"));
        vo.setCnic(resultSet.getString("NIC"));
        vo.setChannelId(resultSet.getString("CHANNEL_ID"));
        vo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        vo.setSegmentName(resultSet.getString("SEGMENT_NAME"));
        vo.setSegmentId(resultSet.getLong("SEGMENT_ID"));
        vo.setEmbossingName(resultSet.getString("EMBOSING_NAME"));
        vo.setNadraName(resultSet.getString("CUSTOMER_NAME"));
        vo.setMailingAddress(resultSet.getString("ADDRESS"));
        vo.setCardStatus(resultSet.getString("CARD_STATUS"));
        vo.setCardState(resultSet.getString("CARD_STATE"));
//        vo.setCardFee(resultSet.getDouble("CARD_FEE"));
        vo.setCardNumber(resultSet.getString("CARD_NO"));
        vo.setCreatedByAppUser(resultSet.getString("CREATED_BY"));
        vo.setUpdatedByAppUserId(resultSet.getLong("UPDATED_BY"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCardStateId(resultSet.getLong("CARD_STATE_ID"));
        vo.setCardStatusId(resultSet.getLong("CARD_STATUS_ID"));
        vo.setCardProductCodeId(resultSet.getLong("CARD_PRODUCT_TYPE_ID"));
        vo.setCardProductType(resultSet.getString("CARD_PRODUCT_TYPE"));
        vo.setCardTypeCode(resultSet.getString("CARD_TYPE_CODE"));
        vo.setReissuance(resultSet.getString("IS_REISSUED"));

        return vo;
    }

    @Column(name = "IS_APPROVED_DENIED")
    public String getIsApprovedDenied() {
        return isApprovedDenied;
    }

    public void setIsApprovedDenied(String isApprovedDenied) {
        this.isApprovedDenied = isApprovedDenied;
    }

    @Column(name = "IS_REISSAUNCE_APPROVED_DENIED")
    public String getIsReIssuanceApprovedDenied() {
        return isReIssuanceApprovedDenied;
    }

    public void setIsReIssuanceApprovedDenied(String isReIssuanceApprovedDenied) {
        this.isReIssuanceApprovedDenied = isReIssuanceApprovedDenied;
    }

    @Column(name = "IS_APPROVED")
    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    @Column(name = "IS_REISSUANCE_APPROVED")
    public String getIsReIssuanceApproved() {
        return isReIssuanceApproved;
    }

    public void setIsReIssuanceApproved(String isReIssuanceApproved) {
        this.isReIssuanceApproved = isReIssuanceApproved;
    }

    @Column(name = "CHECKED_BY_NAME")
    public String getCheckedByName() {
        return checkedByName;
    }

    public void setCheckedByName(String checkedByName) {
        this.checkedByName = checkedByName;
    }

    @Column(name = "CHECKED_BY")
    public Long getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(Long checkedBy) {
        this.checkedBy = checkedBy;
    }

    @Column(name = "CREATED_BY")
    public String getCreatedByAppUser() {
        return createdByAppUser;
    }

    public void setCreatedByAppUser(String createdByAppUser) {
        this.createdByAppUser = createdByAppUser;
    }

    @Column(name = "DEBIT_CARD_FEE")
    public String getDebitCardFee() {
        return debitCardFee;
    }

    public void setDebitCardFee(String debitCardFee) {
        this.debitCardFee = debitCardFee;
    }

    @Column(name = "REISSUANCE_REQUEST_DATE")
    public Date getReissuanceRequestDate() {
        return reissuanceRequestDate;
    }

    public void setReissuanceRequestDate(Date reissuanceRequestDate) {
        this.reissuanceRequestDate = reissuanceRequestDate;
    }
}
