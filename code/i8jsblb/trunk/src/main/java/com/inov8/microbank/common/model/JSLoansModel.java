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
@SequenceGenerator(name = "JS_LOANS_SEQ", sequenceName = "JS_LOANS_SEQ", allocationSize = 1)
@Table(name = "JS_LOANS")
public class JSLoansModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {

    private Long jsLoansId;
    private String mobileNo;
    private String cnic;
    private Long loanAmount;
    private Long remainingAmount;
    private Long serviceFees;
    private Long partialPayment;
    private Long totalServiceFees;
    private String loanDuration;
    private Long productId;
    private Date createdOn;
    private Long createdBy;
    private Date updatedOn;
    private Long updatedBy;
    private Date lastPaymentDate;
    private Date disbursementDate;
    private Date maturityDate;
    private Boolean isCompleted;

    @Transient
    public Long getPrimaryKey() {
        return getJsLoansId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "jsLoansId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&jsLoansId=" + getJsLoansId();
        return parameters;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setJsLoansId(primaryKey);

    }

    @Id
    @Column(name = "JS_LOANS_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JS_LOANS_SEQ")
    public Long getJsLoansId() {
        return jsLoansId;
    }

    public void setJsLoansId(Long jsLoansId) {
        this.jsLoansId = jsLoansId;
    }

    @Column(name = "MOBILE_NUMBER")
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

    @Column(name = "LOAN_AMOUNT")
    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    @Column(name = "SERVICE_FEES")
    public Long getServiceFees() {
        return serviceFees;
    }

    public void setServiceFees(Long serviceFees) {
        this.serviceFees = serviceFees;
    }

    @Column(name = "PARTIAL_PAYMENT")
    public Long getPartialPayment() {
        return partialPayment;
    }

    public void setPartialPayment(Long partialPayment) {
        this.partialPayment = partialPayment;
    }

    @Column(name = "TOTAL_SERVICE_FEES")
    public Long getTotalServiceFees() {
        return totalServiceFees;
    }

    public void setTotalServiceFees(Long totalServiceFees) {
        this.totalServiceFees = totalServiceFees;
    }

    @Column(name = "LOAN_DURATION")
    public String getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(String loanDuration) {
        this.loanDuration = loanDuration;
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

    @Column(name = "LAST_PAYMENT_DATE")
    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    @Column(name = "IS_COMPLETED")
    public Boolean getIsCompleted() { return isCompleted; }

    public void setIsCompleted(Boolean completed) { isCompleted = completed; }

    @Column(name = "REMAINING_AMOUNT")
    public Long getRemainingAmount() {return remainingAmount;}

    public void setRemainingAmount(Long remainingAmount) {this.remainingAmount = remainingAmount;}

    @Column(name = "DISBURSEMENT_DATE")
    public Date getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(Date disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    @Column(name = "MATURITY_DATE")
    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        JSLoansModel model = new JSLoansModel();
        model.setJsLoansId(resultSet.getLong("JS_LOANS_ID"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NUMBER"));
        model.setLoanAmount(resultSet.getLong("LOAN_AMOUNT"));
        model.setRemainingAmount(resultSet.getLong("REMAINING_AMOUNT"));
        model.setServiceFees(resultSet.getLong("SERVICE_FEES"));
        model.setTotalServiceFees(resultSet.getLong("TOTAL_SERVICE_FEES"));
        model.setPartialPayment(resultSet.getLong("PARTIAL_PAYMENT"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setCreatedOn(resultSet.getDate("CREATED_ON"));
        model.setLoanDuration(resultSet.getString("LOAN_DURATION"));
        model.setProductId(resultSet.getLong("PRODUCT_ID"));
        model.setIsCompleted(resultSet.getBoolean("IS_COMPLETED"));
        model.setLastPaymentDate(resultSet.getTimestamp("LAST_PAYMENT_DATE"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setDisbursementDate(resultSet.getTimestamp("DISBURSEMENT_DATE"));
        model.setMaturityDate(resultSet.getTimestamp("MATURITY_DATE"));

        return model;
    }
}
