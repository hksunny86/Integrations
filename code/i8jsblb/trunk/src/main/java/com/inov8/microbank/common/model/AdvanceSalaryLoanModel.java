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
@SequenceGenerator(name = "LOAN_SEQ", sequenceName = "LOAN_SEQ", allocationSize = 1)
@Table(name = "LOAN")
public class AdvanceSalaryLoanModel extends BasePersistableModel implements Serializable, Cloneable, RowMapper {
    private Long advaceSalaryLoanId;
    private String mobileNo;
    private String cnic;
    private Double loanAmount;
    private Long noOfInstallment;
    private Double installmentAmount;
    private Long gracePeriodDays;
    private Long earlyPaymentCharges;
    private Long latePaymentCharges;
    private Long productId;
    private Date createdOn;
    private Long createdBy;
    private Date updatedOn;
    private Long updatedBy;
    private Date lastPaymentDate;
    private Boolean isCompleted;
    private Long noOfInstallmentPaid;
    private Long gracePeriodConsumed;
    private Boolean isDebitBlock;
    private Boolean isIntimated;

    @Id
    @Column(name = "ADVANCE_SALARY_LOAN_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOAN_SEQ")
    public Long getAdvaceSalaryLoanId() {
        return advaceSalaryLoanId;
    }

    public void setAdvaceSalaryLoanId(Long advaceSalaryLoanId) {
        this.advaceSalaryLoanId = advaceSalaryLoanId;
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
    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    @Column(name = "TOTAL_NO_OF_INSTALLMENTS")
    public Long getNoOfInstallment() {
        return noOfInstallment;
    }

    public void setNoOfInstallment(Long noOfInstallment) {
        this.noOfInstallment = noOfInstallment;
    }

    @Column(name = "INSTALLMENT_AMOUNT")
    public Double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    @Column(name = "PRODUCT_ID")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Column(name = "TOTAL_GRACE_PERIOD_DAYS")
    public Long getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(Long gracePeriodDays) {
        this.gracePeriodDays = gracePeriodDays;
    }

    @Column(name = "EARLY_PAYMENT_CHARGES")
    public Long getEarlyPaymentCharges() {
        return earlyPaymentCharges;
    }

    public void setEarlyPaymentCharges(Long earlyPaymentCharges) {
        this.earlyPaymentCharges = earlyPaymentCharges;
    }

    @Column(name = "LATE_PAYMENT_CHARGES")
    public Long getLatePaymentCharges() {
        return latePaymentCharges;
    }

    public void setLatePaymentCharges(Long latePaymentCharges) {
        this.latePaymentCharges = latePaymentCharges;
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
    public Date getLastPaymentDate() { return lastPaymentDate; }

    public void setLastPaymentDate(Date lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }

    @Column(name = "IS_COMPLETED")
    public Boolean getIsCompleted() { return isCompleted; }

    public void setIsCompleted(Boolean completed) { isCompleted = completed; }

    @Column(name = "NO_OF_INSTALLMENT_PAID")
    public Long getNoOfInstallmentPaid() { return noOfInstallmentPaid; }

    public void setNoOfInstallmentPaid(Long noOfInstallmentPaid) { this.noOfInstallmentPaid = noOfInstallmentPaid; }

    @Column(name = "GRACE_PERIOD_CONSUMED")
    public Long getGracePeriodConsumed() { return gracePeriodConsumed; }

    public void setGracePeriodConsumed(Long gracePeriodConsumed) { this.gracePeriodConsumed = gracePeriodConsumed; }

    @Column(name = "DEBIT_BLOCK")
    public Boolean getIsDebitBlock() { return isDebitBlock; }

    public void setIsDebitBlock(Boolean debitBlock) { isDebitBlock = debitBlock; }

    @Column(name = "IS_INTIMATED")
    public Boolean getIsIntimated() {return isIntimated;}

    public void setIsIntimated(Boolean intimated) {isIntimated = intimated;}

    @Transient
    public Long getPrimaryKey() {
        return getAdvaceSalaryLoanId();
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "advaceSalaryLoanId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&advaceSalaryLoanId=" + getAdvaceSalaryLoanId();
        return parameters;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        setAdvaceSalaryLoanId(primaryKey);

    }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_" + this.getAdvaceSalaryLoanId();
        checkBox += "\"/>";
        return checkBox;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AdvanceSalaryLoanModel model = new AdvanceSalaryLoanModel();
        model.setAdvaceSalaryLoanId(resultSet.getLong("ADVANCE_SALARY_LOAN_ID"));
        model.setCnic(resultSet.getString("CNIC"));
        model.setMobileNo(resultSet.getString("MOBILE_NUMBER"));
        model.setInstallmentAmount(resultSet.getDouble("INSTALLMENT_AMOUNT"));
        model.setNoOfInstallment(resultSet.getLong("TOTAL_NO_OF_INSTALLMENTS"));
        model.setProductId(resultSet.getLong("PRODUCT_ID"));
        model.setLoanAmount(resultSet.getDouble("LOAN_AMOUNT"));
        model.setGracePeriodDays(resultSet.getLong("TOTAL_GRACE_PERIOD_DAYS"));
        model.setEarlyPaymentCharges(resultSet.getLong("EARLY_PAYMENT_CHARGES"));
        model.setLatePaymentCharges(resultSet.getLong("LATE_PAYMENT_CHARGES"));
        model.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        model.setCreatedBy(resultSet.getLong("CREATED_BY"));
        model.setUpdatedOn(resultSet.getTimestamp("UPDATED_ON"));
        model.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        model.setLastPaymentDate(resultSet.getTimestamp("LAST_PAYMENT_DATE"));
        model.setIsCompleted(resultSet.getBoolean("IS_COMPLETED"));
        model.setNoOfInstallmentPaid(resultSet.getLong("NO_OF_INSTALLMENT_PAID"));
        model.setGracePeriodConsumed(resultSet.getLong("GRACE_PERIOD_CONSUMED"));
        model.setIsDebitBlock(resultSet.getBoolean("DEBIT_BLOCK"));
        model.setIsIntimated(resultSet.getBoolean("IS_INTIMATED"));

        return model;
    }
}
