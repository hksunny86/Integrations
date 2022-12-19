package com.inov8.microbank.debitcard.vo;

import com.inov8.framework.common.model.BasePersistableModel;

import java.util.Date;

public class DebitCardVO extends BasePersistableModel {

    private Long debitCardId;
    private String debitCardEmbosingName;
    private String customerNadraName;
    private Long cardStateId;
    private Long cardStatusId;
    private String mobileNo;
    private String cNic;
    private String cardNo;
    private String mailingAddress;
    private String appId;
    //
    private Long customerAppUserId;
    private Long createdByAppUserId;
    private Long smartMoneyAccountId;
    private Date createdOn;
    private Date cardIssuanceDate;

    private String transactionCode;
    private String fee;

    @Override
    public void setPrimaryKey(Long aLong) {

    }

    @Override
    public Long getPrimaryKey() {
        return null;
    }

    @Override
    public String getPrimaryKeyParameter() {
        return null;
    }

    @Override
    public String getPrimaryKeyFieldName() {
        return null;
    }

    public Long getDebitCardId() {
        return debitCardId;
    }

    public void setDebitCardId(Long debitCardId) {
        this.debitCardId = debitCardId;
    }

    public String getDebitCardEmbosingName() {
        return debitCardEmbosingName;
    }

    public void setDebitCardEmbosingName(String debitCardEmbosingName) {
        this.debitCardEmbosingName = debitCardEmbosingName;
    }

    public Long getCardStateId() {
        return cardStateId;
    }

    public void setCardStateId(Long cardStateId) {
        this.cardStateId = cardStateId;
    }

    public Long getCardStatusId() {
        return cardStatusId;
    }

    public void setCardStatusId(Long cardStatusId) {
        this.cardStatusId = cardStatusId;
    }

    public Long getCustomerAppUserId() {
        return customerAppUserId;
    }

    public void setCustomerAppUserId(Long customerAppUserId) {
        this.customerAppUserId = customerAppUserId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getcNic() {
        return cNic;
    }

    public void setcNic(String cNic) {
        this.cNic = cNic;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public Long getCreatedByAppUserId() {
        return createdByAppUserId;
    }

    public void setCreatedByAppUserId(Long createdByAppUserId) {
        this.createdByAppUserId = createdByAppUserId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getSmartMoneyAccountId() {
        return smartMoneyAccountId;
    }

    public void setSmartMoneyAccountId(Long smartMoneyAccountId) {
        this.smartMoneyAccountId = smartMoneyAccountId;
    }

    public String getCustomerNadraName() {
        return customerNadraName;
    }

    public void setCustomerNadraName(String customerNadraName) {
        this.customerNadraName = customerNadraName;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Date getCardIssuanceDate() {
        return cardIssuanceDate;
    }

    public void setCardIssuanceDate(Date cardIssuanceDate) {
        this.cardIssuanceDate = cardIssuanceDate;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
