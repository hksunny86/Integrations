package com.inov8.microbank.common.model.portal.blbnewreports;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DIGI_DEBIT_CARD_ANNUAL")
public class DigiDebitCardAnnualReportViewModel extends BasePersistableModel implements Serializable, RowMapper {

    private Long accountHolderId;
    private String embossingName;
    private String cardNo;
    private String cnic;
    private String mobileNo;
    private String issuanceDate;
    private String currentDate;
    private String days;
    private String isDeductionRequired;
    private String customerAccountType;
    private String fromAccount;
    private String fromAccountName;
    private String toAccount;
    private String toAccountName;
    private String microbankSegment;
    private Long deductionAmount;
    private Date deductionDate;
    private String comments;
    private Date startDate;
    private Date endDate;
    private Date cardRequestDate;

    @Transient
    @Override
    public Long getPrimaryKey() {
        return getAccountHolderId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setAccountHolderId(primaryKey);
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "accountHolderId";
        return primaryKeyFieldName;
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        return "&accountHolderId=" + accountHolderId;
    }


    @Id
    @Column(name = "ACCOUNT_HOLDER_ID")
    public Long getAccountHolderId() {
        return accountHolderId;
    }

    public void setAccountHolderId(Long accountHolderId) {
        this.accountHolderId = accountHolderId;
    }


    @Column(name = "EMBOSING_NAME")
    public String getEmbossingName() {
        return embossingName;
    }

    public void setEmbossingName(String embossingName) {
        this.embossingName = embossingName;
    }

    @Column(name = "CARD_NO")
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Column(name = "ISSUANCE_DATE")
    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    @Column(name = "CURRENT_DATE")
    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    @Column(name = "DAYS")
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Column(name = "IS_DEDUCTION_REQUIRED")

    public String getIsDeductionRequired() {
        return isDeductionRequired;
    }

    public void setIsDeductionRequired(String isDeductionRequired) {
        this.isDeductionRequired = isDeductionRequired;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE")
    public String getCustomerAccountType() {
        return customerAccountType;
    }

    public void setCustomerAccountType(String customerAccountType) {
        this.customerAccountType = customerAccountType;
    }

    @Column(name = "FROM_ACCOUNT")
    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }


    @Column(name = "FROM_ACCOUNT_NAME")
    public String getFromAccountName() {
        return fromAccountName;
    }

    public void setFromAccountName(String fromAccountName) {
        this.fromAccountName = fromAccountName;
    }


    @Column(name = "TO_ACCOUNT")
    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }


    @Column(name = "TO_ACCOUNT_NAME")
    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }


    @Column(name = "MICROBANK_SEGMENT")
    public String getMicrobankSegment() {
        return microbankSegment;
    }

    public void setMicrobankSegment(String microbankSegment) {
        this.microbankSegment = microbankSegment;
    }


    @Column(name = "DEDUCTION_AMOUNT")
    public Long getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(Long deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    @Column(name = "DEDUCTION_DATE")
    public Date getDeductionDate() {
        return deductionDate;
    }

    public void setDeductionDate(Date deductionDate) {
        this.deductionDate = deductionDate;
    }

    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "CNIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "CARD_REQUEST_DATE")
    public Date getCardRequestDate() {
        return cardRequestDate;
    }

    public void setCardRequestDate(Date cardRequestDate) {
        this.cardRequestDate = cardRequestDate;
    }

    @Transient
    @Temporal(TemporalType.DATE)
    public Date getStartDate() {
        return startDate;
    }

    @Temporal(TemporalType.DATE)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    @Temporal(TemporalType.DATE)
    public Date getEndDate() {
        return endDate;
    }

    @Temporal(TemporalType.DATE)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DigiDebitCardAnnualReportViewModel model = new DigiDebitCardAnnualReportViewModel();
        model.setAccountHolderId(resultSet.getLong("ACCOUNT_HOLDER_ID"));
        model.setEmbossingName(resultSet.getString("EMBOSING_NAME"));
        model.setCardNo(resultSet.getString("CARD_NO"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setIssuanceDate(resultSet.getString("ISSUANCE_DATE"));
        model.setCurrentDate(resultSet.getString("CURRENT_DATE"));
        model.setDays(resultSet.getString("DAYS"));
        model.setIsDeductionRequired(resultSet.getString("IS_DEDUCTION_REQUIRED"));
        model.setCustomerAccountType(resultSet.getString("CUSTOMER_ACCOUNT_TYPE"));
        model.setFromAccount(resultSet.getString("FROM_ACCOUNT"));
        model.setFromAccountName(resultSet.getString("FROM_ACCOUNT_NAME"));
        model.setToAccount(resultSet.getString("TO_ACCOUNT"));
        model.setToAccountName(resultSet.getString("TO_ACCOUNT_NAME"));
        model.setMicrobankSegment(resultSet.getString("MICROBANK_SEGMENT"));
        model.setDeductionAmount(resultSet.getLong("DEDUCTION_AMOUNT"));
        model.setDeductionDate(resultSet.getDate("DEDUCTION_DATE"));
        model.setComments(resultSet.getString("COMMENTS"));
        model.setCardRequestDate(resultSet.getTimestamp("CARD_REQUEST_DATE"));
        return model;
    }


}
