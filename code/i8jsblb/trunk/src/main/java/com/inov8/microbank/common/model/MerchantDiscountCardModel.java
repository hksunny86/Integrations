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
@Table(name = "MERCHANT_DISCOUNT_CARD_VIEW")
public class MerchantDiscountCardModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private long id;
    private String mobileNo;
    private String cardNo;
    private String productName;
    private Date transactionDate;
    private String transactionId;
    private double transactionAmount;
    private String merchanName;
    private double balanceAfterTransaction;
    private String segmentName;
    private String status;

    private Date createdOnEndDate;
    private Date createdOnStartDate;
    private Date start;
    private Date end;
    public MerchantDiscountCardModel() {
    }

    @Transient
    public Long getPrimaryKey() {
        return getId();
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setId(primaryKey);

    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "id";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&id=" + getId();
        return parameters;
    }

    @Transient
    public Date getCreatedOnEndDate() {
        return createdOnEndDate;
    }

    public void setCreatedOnEndDate(Date createdOnEndDate) {
        this.createdOnEndDate = createdOnEndDate;
    }

    @Transient
    public Date getCreatedOnStartDate() {
        return createdOnStartDate;
    }

    public void setCreatedOnStartDate(Date createdOnStartDate) {
        this.createdOnStartDate = createdOnStartDate;
    }

    @Transient
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Transient
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Id
    @Column(name = "TRANSACTION_ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


    @Column(name = "CARD_NO")

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "MICROBANK_TRANSACTION_CODE")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Column(name = "MERCHANT_NAME")

    public String getMerchanName() {
        return merchanName;
    }

    public void setMerchanName(String merchanName) {
        this.merchanName = merchanName;
    }
    @Column(name = "BALANCE_AFTER_TRANSACTION")

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(double balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }
    @Column(name = "SEGMENT_NAME")

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }
    @Column(name = "STATUS")

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "TRANSACTION_AMOUNT")
    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Column(name = "TRANSACTION_DATE")

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }








    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        MerchantDiscountCardModel model = new MerchantDiscountCardModel();
        model.setId(resultSet.getLong("TRANSACTION_ID"));
        model.setMobileNo(resultSet.getString("MOBILE_NO"));
        model.setMerchanName(resultSet.getString("MERCHANT_NAME"));
        model.setCardNo(resultSet.getString("CARD_NO"));
        model.setProductName(resultSet.getString("PRODUCT_NAME"));
        model.setSegmentName(resultSet.getString("SEGMENT_NAME"));
        model.setTransactionId(resultSet.getString("MICROBANK_TRANSACTION_CODE"));
        model.setStatus(resultSet.getString("STATUS"));
        model.setTransactionAmount(resultSet.getDouble("TRANSACTION_AMOUNT"));
        model.setTransactionDate(resultSet.getDate("TRANSACTION_DATE"));
        model.setBalanceAfterTransaction(resultSet.getDouble("BALANCE_AFTER_TRANSACTION"));
        return model;
    }
}
