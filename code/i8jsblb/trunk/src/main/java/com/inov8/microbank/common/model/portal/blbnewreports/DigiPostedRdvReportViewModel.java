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
@Table(name = "DIGI_RDV_POSTED")
public class DigiPostedRdvReportViewModel extends BasePersistableModel implements Serializable, RowMapper {

    private Long trnasactionId;
    private String transactionDate;
    private String transactionType;
    private String productName;
    private String rrn;
    private String consumerNo;
    private String fromAccount;
    private String fromMobile;
    private String toAccount;
    private String toMobile;
    private String amount;
    private String responseCode;
    private String responseCodeNarration;
    private String status;
    private String stan;


    @Transient
    @Override
    public Long getPrimaryKey() {
        return getTrnasactionId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        setTrnasactionId(primaryKey);
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "trnasactionId";
        return primaryKeyFieldName;
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter() {
        return "&trnasactionId=" + trnasactionId;
    }

    @Id
    @Column(name = "TRANSACTION_ID")
    public Long getTrnasactionId() {
        return trnasactionId;
    }

    public void setTrnasactionId(Long trnasactionId) {
        this.trnasactionId = trnasactionId;
    }

    @Column(name = "TRANSACTION_DATE")
    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Column(name = "TRANSACTION_TYPE")
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

@Column(name = "RRN")
    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @Column(name = "CONSUMER_NO")
    public String getConsumerNo() {
        return consumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNo = consumerNo;
    }



@Column(name = "FROM_ACCOUNT")
    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }


    @Column(name = "FROM_MOBILE")
    public String getFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(String fromMobile) {
        this.fromMobile = fromMobile;
    }

    @Column(name = "TO_ACCOUNT")
    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    @Column(name = "TO_MOBILE")
    public String getToMobile() {
        return toMobile;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }

    @Column(name = "AMOUNT")
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Column(name = "RESPONSE_CODE")
    public String getResponseCode() {
        return responseCode;
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

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "STAN")
    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        DigiPostedRdvReportViewModel model = new DigiPostedRdvReportViewModel();
        model.setTrnasactionId(resultSet.getLong("TRANSACTION_ID"));
        model.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
        model.setTransactionDate(resultSet.getString("TRANSACTION_DATE"));
        model.setProductName(resultSet.getString("PRODUCT_NAME"));
        model.setRrn(resultSet.getString("RRN"));
        model.setConsumerNo(resultSet.getString("CONSUMER_NO"));
        model.setFromAccount(resultSet.getString("FROM_ACCOUNT"));
        model.setFromMobile(resultSet.getString("FROM_MOBILE"));
        model.setToAccount(resultSet.getString("TO_ACCOUNT"));
        model.setToMobile(resultSet.getString("TO_MOBILE"));
        model.setAmount(resultSet.getString("AMOUNT"));
        model.setResponseCode(resultSet.getString("RESPONSE_CODE"));
        model.setResponseCodeNarration(resultSet.getString("RESPONSE_CODE_NARRATION"));
        model.setStan(resultSet.getString("STAN"));
        model.setStatus(resultSet.getString("STATUS"));

        return model;
    }


}
