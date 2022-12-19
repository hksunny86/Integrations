package com.inov8.microbank.common.model.portal.digidormancyaccountmodule;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DIGI_DORMANCY_ACCOUNTS")
public class DigiDormancyAccountViewModel extends BasePersistableModel {

    private Long accountId;
    private String accountNumber;
    private Long balance;
    private String registrationState;
    private String name;
    private String cnic;
    private Date lastTransactionDate;
    private String currentDate;
    private String days;
    private String isDormantRequired;
    private String areaName;
    private String mobileNo;
    private String dob;
    private String customerAccountType;
    private String isAccountClosed;
    private String taxRegime;
    private String segment;
    private Long segmentId;
    private Long userId;
    private String cardNo;
    private String city;
    private Long customerAccountTypeId;
    private Date accountOpeningDate;
    private Date dormancyRemovedOn;
    private Date dormancyMarkedOn;






    private Date startDate;
    private Date endDate;

    @Column(name = "ACCOUNT_ID", nullable = false)
    @Id
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Column(name = "BALANCE")
    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Column(name = "REGISTRATION_STATE", length = 21)
    public String getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }

    @Column(name = "NAME", length = 156)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CNIC", nullable = false)
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @Column(name = "LAST_TRANSACTION_DATE")
    public Date getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(Date lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    @Column(name = "CURRENT_DATE", length = 36)
    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    @Column(name = "DAYS", length = 20)
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Column(name = "IS_DORMANT_REQUIRED", length = 3)
    public String getIsDormantRequired() {
        return isDormantRequired;
    }

    public void setIsDormantRequired(String isDormantRequired) {
        this.isDormantRequired = isDormantRequired;
    }

    @Column(name = "AREA_NAME", length = 50)
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Column(name = "MOBILE_NUMBER", length = 50)
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "DOB")
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE", length = 50)
    public String getCustomerAccountType() {
        return customerAccountType;
    }

    public void setCustomerAccountType(String customerAccountType) {
        this.customerAccountType = customerAccountType;
    }

    @Column(name = "IS_ACCOUNT_CLOSED", length = 16)
    public String getIsAccountClosed() {
        return isAccountClosed;
    }

    public void setIsAccountClosed(String isAccountClosed) {
        this.isAccountClosed = isAccountClosed;
    }

    @Column(name = "TAX_REGIME", length = 50)
    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(String taxRegime) {
        this.taxRegime = taxRegime;
    }

    @Column(name = "SEGMENT", length = 50)
    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Column(name = "SEGMENT_ID", length = 50)
    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    @Column(name = "USER_ID", length = 50)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "CARD_NO", length = 50)
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Column(name = "CITY", length = 50)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID", length = 50)
    public Long getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }
    @Column(name = "ACCOUNT_OPENING_DATE", length = 50)
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name = "DORMANCY_REMOVED_ON", length = 50)
    public Date getDormancyRemovedOn() {
        return dormancyRemovedOn;
    }

    public void setDormancyRemovedOn(Date dormancyRemovedOn) {
        this.dormancyRemovedOn = dormancyRemovedOn;
    }

    @Column(name = "DORMANT_MARKED_ON", length = 50)
    public Date getDormancyMarkedOn() {
        return dormancyMarkedOn;
    }

    public void setDormancyMarkedOn(Date dormancyMarkedOn) {
        this.dormancyMarkedOn = dormancyMarkedOn;
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setAccountId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return accountId;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&accountId=" + getAccountId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "accountId";
        return primaryKeyFieldName;
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
}
