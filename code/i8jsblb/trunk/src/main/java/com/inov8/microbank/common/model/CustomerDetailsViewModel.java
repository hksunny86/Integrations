package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Inov8 on 4/11/2018.
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "CUSTOMER_DETAILS_VIEW")
public class CustomerDetailsViewModel extends BasePersistableModel {

    private Long pk;
    private String customerName;
    private String fatherHusbandName;
    private String motherMaidenName;
    private String nic;
    private String nicExpiryDate;
    private String gender;
    private String cityName;
    private String mobileNo;
    private String birthPlace;
    private String dob;
    private String accountType;
    private String accountRegistrationState;
    private String accountState;
    private String segment;
    private String taxRegionName;
    private String accountRegState;
    private Double fed;
    private Double dailyDebitLimit;
    private Double dailyCreditLimit;
    private Double monthlyDebitLimit;
    private Double monthlyCreditLimit;
    private Double yearlyDebitLimit;
    private Double yearlyCreditLimit;

    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setPk(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Column(name = "CUSTOMER_NAME")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "FATHER_HUSBAND_NAME")
    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    @Column(name = "GENDER")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name = "CITY_NAME")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Column(name = "BIRTH_PLACE")
    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Column(name = "ACCOUNT_TYPE")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "SEGMENT")
    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Column(name = "TAX_REGIME")
    public String getTaxRegionName() {
        return taxRegionName;
    }

    public void setTaxRegionName(String taxRegionName) {
        this.taxRegionName = taxRegionName;
    }

    @Column(name = "FED")
    public Double getFed() {
        return fed;
    }

    public void setFed(Double fed) {
        this.fed = fed;
    }

    @Column(name = "USED_DAILY_DEBIT_LIMIT")
    public Double getDailyDebitLimit() {
        return dailyDebitLimit;
    }

    public void setDailyDebitLimit(Double dailyDebitLimit) {
        this.dailyDebitLimit = dailyDebitLimit;
    }

    @Column(name = "USED_DAILY_CREDIT_LIMIT")
    public Double getDailyCreditLimit() {
        return dailyCreditLimit;
    }

    public void setDailyCreditLimit(Double dailyCreditLimit) {
        this.dailyCreditLimit = dailyCreditLimit;
    }

    @Column(name = "USED_MONTHLY_DEBIT_LIMIT")
    public Double getMonthlyDebitLimit() {
        return monthlyDebitLimit;
    }

    public void setMonthlyDebitLimit(Double monthlyDebitLimit) {
        this.monthlyDebitLimit = monthlyDebitLimit;
    }

    @Column(name = "USED_MONTHLY_CREDIT_LIMIT")
    public Double getMonthlyCreditLimit() {
        return monthlyCreditLimit;
    }

    public void setMonthlyCreditLimit(Double monthlyCreditLimit) {
        this.monthlyCreditLimit = monthlyCreditLimit;
    }

    @Column(name = "USED_YEARLY_DEBIT_LIMIT")
    public Double getYearlyDebitLimit() {
        return yearlyDebitLimit;
    }

    public void setYearlyDebitLimit(Double yearlyDebitLimit) {
        this.yearlyDebitLimit = yearlyDebitLimit;
    }

    @Column(name = "USED_YEARLY_CREDIT_LIMIT")
    public Double getYearlyCreditLimit() {
        return yearlyCreditLimit;
    }

    public void setYearlyCreditLimit(Double yearlyCreditLimit) {
        this.yearlyCreditLimit = yearlyCreditLimit;
    }

    @Column(name="PK")
    @Id
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo()
    {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "REGISTRATION_STATE")
    public String getAccountRegistrationState() {
        return accountRegistrationState;
    }

    public void setAccountRegistrationState(String accountRegistrationState) {
        this.accountRegistrationState = accountRegistrationState;
    }

    @Column(name = "ACCOUNT_STATE")
    public String getAccountState() {
        return accountState;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    @Column(name = "MOTHER_MAIDEN_NAME")
    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    @Column(name = "NIC")
    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    @Column(name = "NIC_EXPIRY_DATE")
    public String getNicExpiryDate() {
        return nicExpiryDate;
    }

    public void setNicExpiryDate(String nicExpiryDate) {
        this.nicExpiryDate = nicExpiryDate;
    }

    @Column(name = "DOB")
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}
