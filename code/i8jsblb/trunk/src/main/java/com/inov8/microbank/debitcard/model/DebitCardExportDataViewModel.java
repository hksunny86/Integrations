package com.inov8.microbank.debitcard.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DEBIT_CARD_EXPORT_DATA_VIEW")
public class DebitCardExportDataViewModel extends BasePersistableModel {

    private Long debitCardId;
    private String mobileNo;
    private String cNic;
    private String customerTypeCode;
    private String branchCode;
    private String segmentCode;
    private String customerStatusCode;
    private String csutomerName;
    private String genderCode;
    private String accountNumber;
    private String accountTitle;
    private String accountStatusCode;
    private String currencyCode;
    private String cardEmbossingName;
    private String cardTypeCode;
    private String cardProductCode;
    private String requestType;
    private String createdBy;
    private java.sql.Timestamp createdOn;
    private String updatedBy;
    private java.sql.Timestamp updatedOn;

    private String cNicExpiryDate;
    private String fatherHusbandName;
    private String motherMaidenName;
    private Date dob;
    private String birthPlace;
    private String debitCardMailingAddress;
    private String customerCity;
    private String customerCountryCode;
    private String iban;


    @javax.persistence.Transient
    public void setPrimaryKey(Long aLong) {
        setDebitCardId(aLong);
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
        return getDebitCardId();
    }

    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&debitCardId=" + getDebitCardId();
        return parameters;
    }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "debitCardId";
        return primaryKeyFieldName;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "RELATIONSHIP_NUMBER")
    public String getcNic() {
        return cNic;
    }

    public void setcNic(String cNic) {
        this.cNic = cNic;
    }

    @Column(name = "CUSTOMER_TYPE_CODE")
    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    @Column(name = "BRANCH_CODE")
    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    @Column(name = "SEGMENT_CODE")
    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    @Column(name = "CUSTOMER_STATUS_CODE")
    public String getCustomerStatusCode() {
        return customerStatusCode;
    }

    public void setCustomerStatusCode(String customerStatusCode) {
        this.customerStatusCode = customerStatusCode;
    }

    @Column(name = "CUSTOMER_NAME")
    public String getCsutomerName() {
        return csutomerName;
    }

    public void setCsutomerName(String csutomerName) {
        this.csutomerName = csutomerName;
    }

    @Column(name = "GENDER_CODE")
    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    @Column(name = "ACCOUNT_NUMBER")
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Column(name = "ACCOUNT_TITLE")
    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    @Column(name = "ACCOUNT_STATUS_CODE")
    public String getAccountStatusCode() {
        return accountStatusCode;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    @Column(name = "CURRENCY_CODE")
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Column(name = "CARD_EMBOSING_NAME")
    public String getCardEmbossingName() {
        return cardEmbossingName;
    }

    public void setCardEmbossingName(String cardEmbossingName) {
        this.cardEmbossingName = cardEmbossingName;
    }

    @Column(name = "CARD_TYPE_CODE")
    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    @Column(name = "CARD_PRODUCT_CODE")
    public String getCardProductCode() {
        return cardProductCode;
    }

    public void setCardProductCode(String cardProductCode) {
        this.cardProductCode = cardProductCode;
    }

    @Column(name = "REQUEST_TYPE")
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "CREATED_ON")
    public java.sql.Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(java.sql.Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "UPDATED_ON")
    public java.sql.Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(java.sql.Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Column(name = "DEBIT_CARD_ID",nullable = false)
    @Id
    public Long getDebitCardId() {
        return debitCardId;
    }

    public void setDebitCardId(Long debitCardId) {
        this.debitCardId = debitCardId;
    }

    @Column(name = "NIC_EXPIRY")
    public String getcNicExpiryDate() {
        return cNicExpiryDate;
    }

    public void setcNicExpiryDate(String cNicExpiryDate) {
        this.cNicExpiryDate = cNicExpiryDate;
    }

    @Column(name = "FATHER_HUSBAND_NAME")
    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

    @Column(name = "MOTHER_MAIDEN_NAME")
    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    @Column(name = "DOB")
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Column(name = "BIRTH_PLACE")
    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Column(name = "DEBIT_CARD_MAILING_ADDRESS")
    public String getDebitCardMailingAddress() {
        return debitCardMailingAddress;
    }

    public void setDebitCardMailingAddress(String debitCardMailingAddress) {
        this.debitCardMailingAddress = debitCardMailingAddress;
    }

    @Column(name = "CITY")
    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    @Column(name = "COUNTRY_CODE")
    public String getCustomerCountryCode() {
        return customerCountryCode;
    }

    public void setCustomerCountryCode(String customerCountryCode) {
        this.customerCountryCode = customerCountryCode;
    }

    @Column(name = "IBAN")
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
