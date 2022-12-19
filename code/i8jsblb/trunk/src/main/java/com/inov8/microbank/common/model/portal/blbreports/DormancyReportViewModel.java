package com.inov8.microbank.common.model.portal.blbreports;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity( mutable=false )
@Table(name = "DORMANCY_REPORT_VIEW")
public class DormancyReportViewModel extends BasePersistableModel {
    private Long pk;
    private Long agentId;
    private String mobileNo;
    private String cnic;
    private String mbAccountNo;
    private String debitCardNumber;
    private Long customerAccountTypeId;
    private String accountType;
    private String customerName;
    private Long taxRegimeId;
    private String taxRegime;
    private Long segmentId;
    private String segmentName;
    private String accountBalance;
    private String accountStatus;
    private String city;
    private Date lastActivityDate;
    private Date dormancyDueOn;
    private Date accountOpeningDate;
    private String lastTransactionType;
    private String accountAging;
    private Date startDate;
    private Date endDate;
    private String dormantStatus;

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

    @Column(name="AGENT_CUSTOMER_ID")
    public Long getAgentId() {return agentId;}

    public void setAgentId(Long agentId) {this.agentId = agentId;}

    @Column(name="MOBILE_NO")
    public String getMobileNo() {return mobileNo;}

    public void setMobileNo(String mobileNo) {this.mobileNo = mobileNo;}

    @Column(name="CNIC")
    public String getCnic() {return cnic;}

    public void setCnic(String cnic) {this.cnic = cnic;}

    @Column(name="MB_ACCOUNT_NO")
    public String getMbAccountNo() {return mbAccountNo;}

    public void setMbAccountNo(String mbAccountNo) {this.mbAccountNo = mbAccountNo;}

    @Column(name="DEBIT_CARD_NUMBER")
    public String getDebitCardNumber() {return debitCardNumber;}

    public void setDebitCardNumber(String debitCardNumber) {this.debitCardNumber = debitCardNumber;}

    @Column(name="ACCOUNT_TYPE")
    public String getAccountType() {return accountType;}

    public void setAccountType(String accountType) {this.accountType = accountType;}

    @Column(name="CUSTOMER_NAME")
    public String getCustomerName() {return customerName;}

    public void setCustomerName(String customerName) {this.customerName = customerName;}

    @Column(name="TAX_REGIME_ID")
    public Long getTaxRegimeId() {return taxRegimeId;}

    public void setTaxRegimeId(Long taxRegimeId) {this.taxRegimeId = taxRegimeId;}

    @Column(name="TAX_REGIME")
    public String getTaxRegime() {return taxRegime;}

    public void setTaxRegime(String taxRegime) {this.taxRegime = taxRegime;}

    @Column(name="SEGMENT_ID")
    public Long getSegmentId() {return segmentId;}

    public void setSegmentId(Long segmentId) {this.segmentId = segmentId;}

    @Column(name="SEGMENT_NAME")
    public String getSegmentName() {return segmentName;}

    public void setSegmentName(String segmentName) {this.segmentName = segmentName;}

    @Column(name="ACCOUNT_BALANCE")
    public String getAccountBalance() {return accountBalance;}

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Column(name="ACCOUNT_STATUS")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Column(name="CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name="LAST_ACTIVITY_DATE")
    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @Column(name="DORMANCY_DUE_ON")
    public Date getDormancyDueOn() {
        return dormancyDueOn;
    }

    public void setDormancyDueOn(Date dormancyDueOn) {
        this.dormancyDueOn = dormancyDueOn;
    }

    @Column(name="ACCOUNT_CREATED_ON")
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name="LAST_TRANSACTION_TYPE")
    public String getLastTransactionType() {
        return lastTransactionType;
    }

    public void setLastTransactionType(String lastTransactionType) {
        this.lastTransactionType = lastTransactionType;
    }

    @Column(name="ACCOUNT_AGING")
    public String getAccountAging() {
        return accountAging;
    }

    public void setAccountAging(String accountAging) {
        this.accountAging = accountAging;
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

    @Column(name="DORMANT_STATUS")
    public String getDormantStatus() {
        return dormantStatus;
    }

    public void setDormantStatus(String dormantStatus) {
        this.dormantStatus = dormantStatus;
    }

    @Column(name="ACCOUNT_TYPE_ID")
    public Long getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }
}
