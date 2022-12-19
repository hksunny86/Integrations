package com.inov8.microbank.common.model.portal.blbreports;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity( mutable=false )
@Table(name = "DEBIT_CARD_CHARGES_REPORT_VIEW")
public class DebitCardChargesViewModel extends BasePersistableModel implements Serializable{

    private Long pk;
    private String cnic;
    private String mobileNo;
    private String mbAccountNo;
    private String debitCardNumber;
    private Long customerAccountTypeId;
    private String accountTypeName;
    private String chargesType;
    private String chargesStatus;
    private String chargesAmount;
    private Double fedRate;
    private String name;
    private String city;
    private String regime;
    private Long regimeRate;
    private Date accountOpeningDate;
    private Date lastActivityDate;
    private String lastTransactionType;
    private String transactionType;
    private Date transactionDate;
    private Long segmentId;
    private String segmentName;
    private String accountStatus;
    private String accountBalance;
    private Date startDate;
    private Date endDate;

    @Transient
    @Override
    public Long getPrimaryKey()
    {
        return getPk();
    }

    @Override
    public void setPrimaryKey( Long primaryKey )
    {
        setPk( primaryKey );
    }

    @Transient
    @Override
    public String getPrimaryKeyFieldName()
    {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Transient
    @Override
    public String getPrimaryKeyParameter()
    {
        return "&pk=" + pk;
    }


    @Id
    @Column(name="PK")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }


    @Column(name="NIC")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name="MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name="MB_ACCOUNT_NO")
    public String getMbAccountNo() {
        return mbAccountNo;
    }

    public void setMbAccountNo(String mbAccountNo) {
        this.mbAccountNo = mbAccountNo;
    }

    @Column(name="DEBIT_CARD_NUMBER")
    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    @Column(name="ACCOUNT_TYPE_ID")
    public Long getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    @Column(name="ACCOUNT_TYPE")
    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    @Column(name="CHARGES_TYPE")
    public String getChargesType() {
        return chargesType;
    }

    public void setChargesType(String chargesType) {
        this.chargesType = chargesType;
    }

    @Column(name="CHARGES_STATUS")
    public String getChargesStatus() {
        return chargesStatus;
    }

    public void setChargesStatus(String chargesStatus) {
        this.chargesStatus = chargesStatus;
    }

    @Column(name="CHARGES_AMOUNT")
    public String getChargesAmount() {
        return chargesAmount;
    }

    public void setChargesAmount(String chargesAmount) {
        this.chargesAmount = chargesAmount;
    }

    @Column(name="FED_RATE")
    public Double getFedRate() {
        return fedRate;
    }

    public void setFedRate(Double fedRate) {
        this.fedRate = fedRate;
    }

    @Column(name="NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name="REGIME")
    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    @Column(name="REGIME_RATE")
    public Long getRegimeRate() {
        return regimeRate;
    }

    public void setRegimeRate(Long regimeRate) {
        this.regimeRate = regimeRate;
    }

    @Column(name="ACCOUNT_OPENING_DATE")
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name="LAST_ACTIVITY_DATE")
    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @Column(name="TRANSACTION_TYPE")
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Column(name="TRANSACTION_DATE")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Column(name="SEGMENT_ID")
    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    @Column(name="SEGMENT")
    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    @Column(name="ACCOUNT_STATUS")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Column(name="ACCOUNT_BALANCE")
    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Transient
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name="LAST_TRANSACTION_TYPE")
    public String getLastTransactionType() {
        return lastTransactionType;
    }

    public void setLastTransactionType(String lastTransactionType) {
        this.lastTransactionType = lastTransactionType;
    }
}
