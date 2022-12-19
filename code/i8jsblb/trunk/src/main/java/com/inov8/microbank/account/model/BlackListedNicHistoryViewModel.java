package com.inov8.microbank.account.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.CommonUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "BLACKLISTED_CNICS_HISTORY_VIEW")
public class BlackListedNicHistoryViewModel extends BasePersistableModel implements Serializable {

    private Long blacklistedCnicsId;
    private Long userId;
    private Long appUserTypeId;
    private String appUserTypeName;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String businessName;
    private String mobileNo;
    private String cNic;
    private Date accountOpeningDate;
    private Long regStateId;
    private String regStateName;
    private String accountStatus;
    private Boolean isBlacklisted;
    private String comments;
    private Date lastActivityDate;
    private String action;
    private Date actionDate;
    private Date actionDateEnd;
    private String actionPerformedBy;
    private Long userAccountTypeId;
    private String userAccountTypeName;

    private Long segmentId;
    private String segmentName;
    private Long taxRegimeId;
    private String taxRegimeName;
    private String cardNo;


    @Column(name = "CARD_NO")
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Column(name = "TAX_REGIME_ID")
    public Long getTaxRegimeId() {
        return taxRegimeId;
    }

    public void setTaxRegimeId(Long taxRegimeId) {
        this.taxRegimeId = taxRegimeId;
    }






    @Column(name = "TAX_REGIME_NAME")
    public String getTaxRegimeName() {
        return taxRegimeName;
    }

    public void setTaxRegimeName(String taxRegimeName) {
        this.taxRegimeName = taxRegimeName;
    }

    @Column(name = "SEGMENT_NAME")
    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }
    @Column(name = "SEGMENT_ID")
    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }



    @Id
    @Column(name = "PK", nullable = false)
    public Long getBlacklistedCnicsId() {
        return blacklistedCnicsId;
    }

    public void setBlacklistedCnicsId(Long blacklistedCnicsId) {
        this.blacklistedCnicsId = blacklistedCnicsId;
    }

    @Transient
    public void setPrimaryKey(Long primaryKey) {
        this.setBlacklistedCnicsId(primaryKey);
    }

    @Transient
    public Long getPrimaryKey() {
        return getBlacklistedCnicsId();
    }

    @Transient
    public String getPrimaryKeyParameter() {
        String parameters = "";
        parameters += "&blacklistedCnicsId=" + getBlacklistedCnicsId();
        return parameters;
    }

    @Transient
    public String getPrimaryKeyFieldName() {
        String primaryKeyFieldName = "blacklistedCnicsId";
        return primaryKeyFieldName;
    }

    @Transient
    public String getStatus() {
        return CommonUtils.getDefaultIfNull(getBlacklisted(), false) ? "Yes" : "No";
    }

    @Column(name = "USER_ID")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long appUserId) {
        this.userId = appUserId;
    }

    @Column(name = "APP_USER_TYPE_ID")
    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }

    @Column(name = "APP_USER_TYPE_NAME")
    public String getAppUserTypeName() {
        return appUserTypeName;
    }

    public void setAppUserTypeName(String appUserTypeName) {
        this.appUserTypeName = appUserTypeName;
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

    @Column(name = "CNIC_NO")
    public String getcNic() {
        return cNic;
    }

    public void setcNic(String cNic) {
        this.cNic = cNic;
    }

    @Column(name = "AC_OPENING_DATE")
    public Date getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(Date accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    @Column(name = "REGISTRATION_STATE_ID")
    public Long getRegStateId() {
        return regStateId;
    }

    public void setRegStateId(Long regStateId) {
        this.regStateId = regStateId;
    }

    @Column(name = "REGISTRATION_STATE")
    public String getRegStateName() {
        return regStateName;
    }

    public void setRegStateName(String regStateName) {
        this.regStateName = regStateName;
    }

    @Column(name = "ACCOUNT_STATUS")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    @Column(name = "IS_BLACKLISTED")
    public Boolean getBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(Boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "LAST_ACTIVITY_DATE")
    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @Column(name = "ACTION")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Column(name = "ACTION_DATE")
    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    @Column(name = "ACTION_PERFORMED_BY")
    public String getActionPerformedBy() {
        return actionPerformedBy;
    }

    public void setActionPerformedBy(String actionPerformedBy) {
        this.actionPerformedBy = actionPerformedBy;
    }

    @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID")
    public Long getUserAccountTypeId() {
        return userAccountTypeId;
    }

    public void setUserAccountTypeId(Long userAccountTypeId) {
        this.userAccountTypeId = userAccountTypeId;
    }

    @Column(name = "ACCOUNT_TYPE")
    public String getUserAccountTypeName() {
        return userAccountTypeName;
    }

    public void setUserAccountTypeName(String userAccountTypeName) {
        this.userAccountTypeName = userAccountTypeName;
    }

    @Transient
    public Date getActionDateEnd() {
        return actionDateEnd;
    }

    public void setActionDateEnd(Date actionDateEnd) {
        this.actionDateEnd = actionDateEnd;
    }

}
