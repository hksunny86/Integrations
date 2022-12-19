package com.inov8.microbank.account.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "EXPIRED_NIC_LIST_VIEW")
public class ExpiredNicViewModel extends BasePersistableModel implements Serializable {

    private Long pk;
    private Long userId;
    private String accountType;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String businessName;
    private String mobileNo;
    private String cNic;
    private Date accountOpeningDate;
    private Date lastActivityDate;
    private Date cNicExpiryDate;
    private Date cNicEpiryDateEnd;
    private String regStatus;
    private String accountStatus;
    private Long customerAccountTypeId;
    private Long segmentId;
    private String segment;
    private String regime;
    private String cardNo;
    private String city;

    @Id
    @Column(name = "PK")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        this.setPk(primaryKey);
    }

    @Transient
    public Long getPrimaryKey() {
        return getPk();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&pk=" + getPk();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "pk";
        return primaryKeyFieldName;
    }

    @Column(name = "USER_ID")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "ACCOUNT_TYPE")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Column(name = "ACCOUNT_NUMBER")
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "BUSINESS_NAME")
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Column(name = "MOBILE_NO")
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Column(name = "NIC")
    public String getcNic() {
        return cNic;
    }

    public void setcNic(String cNic) {
        this.cNic = cNic;
    }

    @Column(name = "ACCOUNT_OPENING_DATE")
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name = "LAST_ACTIVITY_DATE")
    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @Column(name = "NIC_EXPIRY_DATE")
    public Date getcNicExpiryDate() {
        return cNicExpiryDate;
    }

    public void setcNicExpiryDate(Date cNicExpiryDate) {
        this.cNicExpiryDate = cNicExpiryDate;
    }

    @Column(name = "REGISTRATION_STATE")
    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    @Column(name = "ACCOUNT_STATUS")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID")
    public Long getCustomerAccountTypeId() {
        return customerAccountTypeId;
    }

    public void setCustomerAccountTypeId(Long customerAccountTypeId) {
        this.customerAccountTypeId = customerAccountTypeId;
    }

    @Column(name = "SEGMENT_ID")
    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    @Column(name = "SEGMENT")
    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    @Column(name = "TAX_REGIME")
    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    @Column(name = "CARD_NO")
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Transient
    public Date getcNicEpiryDateEnd() {
        return cNicEpiryDateEnd;
    }

    public void setcNicEpiryDateEnd(Date cNicEpiryDateEnd) {
        this.cNicEpiryDateEnd = cNicEpiryDateEnd;
    }

}
