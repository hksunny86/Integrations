package com.inov8.microbank.common.model.postedrransactionreportmodule;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Oct 22, 2012 6:55:40 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "POSTED_TRANSACTION_VIEW")
public class PostedTransactionViewModel extends BasePersistableModel implements Serializable, RowMapper {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 351878670364114461L;

    // Fields
    private Long postedTransRptId;

    private Long intgTransactionTypeId;

    private String transactionType;

    private String transactionCode;

    private Long productId;

    private String productName;

    private Double amount;

    private String responseCode;

    private String responseCodeNarration;

    private String stan;

    private String fromAccount;

    private String toAccount;

    private String refCode;

    private String consumerNo;

    private Long createdBy;

    private Timestamp createdOn;

    /**
     * default constructor
     */
    public PostedTransactionViewModel() {
    }

    @Override
    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return postedTransRptId;
    }

    @Override
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
        setPostedTransRptId(primaryKey);
    }

    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "&postedTransRptId=" + getPostedTransRptId();
        return parameters;
    }

    /**
     * Helper method for default Sorting on Primary Keys
     */
    @Override
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        return "postedTransRptId";
    }

    // Property accessors
    @Id
    @Column(name = "POSTED_TRANS_RPT_ID", nullable = false, precision = 10, scale = 0)
    public Long getPostedTransRptId() {
        return this.postedTransRptId;
    }

    public void setPostedTransRptId(Long postedTransRptId) {
        this.postedTransRptId = postedTransRptId;
    }

    @Column(name = "INTG_TRANSACTION_TYPE_ID", nullable = false, precision = 10, scale = 0)
    public Long getIntgTransactionTypeId() {
        return this.intgTransactionTypeId;
    }

    public void setIntgTransactionTypeId(Long intgTransactionTypeId) {
        this.intgTransactionTypeId = intgTransactionTypeId;
    }

    @Column(name = "TRANSACTION_TYPE", length = 50)
    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name = "TRANSACTION_CODE", nullable = false, length = 50)
    public String getTransactionCode() {
        return this.transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Column(name = "PRODUCT_ID", nullable = false, precision = 10, scale = 0)
    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "PRODUCT_NAME", nullable = false, length = 50)
    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "AMOUNT", precision = 10, scale = 0)
    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "RESPONSE_CODE", length = 50)
    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Column(name = "RESPONSE_CODE_NARRATION")
    public String getResponseCodeNarration() {
        return responseCodeNarration;
    }

    public void setResponseCodeNarration(String responseCodeNarration) {
        this.responseCodeNarration = responseCodeNarration;
    }

    @Column(name = "STAN")
    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    @Column(name = "FROM_ACCOUNT", length = 50)
    public String getFromAccount() {
        return this.fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    @Column(name = "TO_ACCOUNT", length = 50)
    public String getToAccount() {
        return this.toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    @Column(name = "REF_CODE", length = 50)
    public String getRefCode() {
        return this.refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    @Column(name = "CONSUMER_NO", length = 30)
    public String getConsumerNo() {
        return this.consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }

    @Column(name = "CREATED_BY", precision = 10, scale = 0)
    public Long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "CREATED_ON", length = 7)
    public Timestamp getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        PostedTransactionViewModel vo = new PostedTransactionViewModel();

        vo.setPostedTransRptId(resultSet.getLong("POSTED_TRANS_RPT_ID"));
        vo.setIntgTransactionTypeId(resultSet.getLong("INTG_TRANSACTION_TYPE_ID"));
        vo.setTransactionCode(resultSet.getString("TRANSACTION_CODE"));
        vo.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setProductName(resultSet.getString("PRODUCT_NAME"));
        vo.setAmount(resultSet.getDouble("AMOUNT"));
        vo.setResponseCode(resultSet.getString("RESPONSE_CODE"));
        vo.setResponseCodeNarration(resultSet.getString("RESPONSE_CODE_NARRATION"));
        vo.setStan(resultSet.getString("STAN"));
        vo.setFromAccount(resultSet.getString("FROM_ACCOUNT"));
        vo.setToAccount(resultSet.getString("TO_ACCOUNT"));
        vo.setRefCode(resultSet.getString("REF_CODE"));
        vo.setConsumerNo(resultSet.getString("CONSUMER_NO"));
        vo.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
        return vo;
    }
}