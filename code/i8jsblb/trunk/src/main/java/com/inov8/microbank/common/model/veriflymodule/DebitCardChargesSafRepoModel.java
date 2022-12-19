package com.inov8.microbank.common.model.veriflymodule;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DEBIT_CARD_CHARGES_SREPO_SEQ", sequenceName = "DEBIT_CARD_CHARGES_SREPO_SEQ", allocationSize = 1)
@Table(name = "DEBIT_CARD_CHARGES_SAF_REPO")
public class DebitCardChargesSafRepoModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long debitCardChargesSafRepoId;
    private String debitCardNo;
    private String cnic;
    private String mobileNo;
    private Long cardStatusId;
    private Long CardStateId;
    private Long productId;
    private Long cardTypeConstant;
    private Long accountId;
    private Double transactionAmount;
    private Date transactionDate;
    private String transactionstatus;
    private String isCompleted;
    private Date createdOn;
    private Long createdBy;
    private Date updatedOn;
    private Long updatedBy;


    public DebitCardChargesSafRepoModel() {
    }

    @Id
    @Column(name = "DEBIT_CARD_CHARGES_SAF_REPO_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEBIT_CARD_CHARGES_SREPO_SEQ")
    public Long getDebitCardChargesSafRepoId() {
        return debitCardChargesSafRepoId;
    }

    public void setDebitCardChargesSafRepoId(Long debitCardChargesSafRepoId) {
        debitCardChargesSafRepoId = debitCardChargesSafRepoId;
    }

    @Column(name = "DEBIT_CARD_NO")
    public String getDebitCardNo() {
        return debitCardNo;
    }

    public void setDebitCardNo(String debitCardNo) {
        this.debitCardNo = debitCardNo;
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

    @Column(name = "CARD_STATUS_ID")
    public Long getCardStatusId() {
        return cardStatusId;
    }

    public void setCardStatusId(Long cardStatusId) {
        this.cardStatusId = cardStatusId;
    }


    @Column(name = "CARD_STATE_ID")
    public Long getCardStateId() {
        return CardStateId;
    }

    public void setCardStateId(Long cardStateId) {
        CardStateId = cardStateId;
    }

    @Column(name = "PRDUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "CARD_TYPE_CONSTANT")
    public Long getCardTypeConstant() {
        return cardTypeConstant;
    }

    public void setCardTypeConstant(Long cardTypeConstant) {
        this.cardTypeConstant = cardTypeConstant;
    }

    @Column(name = "DEBIT_CARD_ID")
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Column(name = "TRANSACTION_DATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


    @Column(name = "TRANSACTION_STATUS")
    public String getTransactionstatus() {
        return transactionstatus;
    }

    public void setTransactionstatus(String transactionstatus) {
        this.transactionstatus = transactionstatus;
    }


    @Column(name = "IS_COMPLETED")
    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
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


    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getDebitCardChargesSafRepoId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "debitCardChargesSafRepoId";
        return primaryKeyFieldName;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&debitCardChargesSafRepoId=" + getDebitCardChargesSafRepoId();
        return parameters;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setDebitCardChargesSafRepoId(primaryKey);

    }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getDebitCardChargesSafRepoId();
        checkBox += "\"/>";
        return checkBox;
    }


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DebitCardChargesSafRepoModel model = new DebitCardChargesSafRepoModel();
        model.setDebitCardChargesSafRepoId(resultSet.getLong("DEBIT_CARD_CHARGES_SAF_REPO_ID"));
        model.setDebitCardNo(resultSet.getString("DEBIT_CARD_NO"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setCardStatusId(resultSet.getLong("CARD_STATUS_ID"));
        model.setCardStateId(resultSet.getLong("CARD_STATE_ID"));
        model.setProductId(resultSet.getLong("PRDUCT_ID"));
        model.setCardTypeConstant(resultSet.getLong("CARD_TYPE_CONSTANT"));
        model.setAccountId(resultSet.getLong("DEBIT_CARD_ID"));
        model.setTransactionAmount(resultSet.getDouble("TRANSACTION_AMOUNT"));
        model.setTransactionDate(resultSet.getDate("TRANSACTION_DATE"));
        model.setTransactionstatus(resultSet.getString("TRANSACTION_STATUS"));
        model.setIsCompleted(resultSet.getString("IS_COMPLETED"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        return model;
    }
}
