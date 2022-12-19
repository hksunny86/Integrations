package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

@XStreamAlias("AccountDebit")
public class AccountVO implements Serializable
{
    private String accountNo;
    private String accountTypeCode;
    private String accountStatusCode;
    private String branchCode;
    private String currencyCode;
    private String accountTitle;
    private String limitProfile1;
    private String accountTitleOther;
    private String officerCode;
    private String statementFrequency;
    private String introRelationShipNo;
    private String introAddress;
    private String introAccountNo;
    private String comments;
    private String closedRemark;
    private String jointDescription;
    private String address1;
    private String address2;
    private String city;
    private String countryCode;
    private String zipCode;
    private String T1;
    private String T2;
    private String T3;
    private String T4;
    private String T5;
    private String T6;
    private String T7;
    private String T8;
    private String reserved1;
    private String IBAN;

    private int joinType;

    private double availableBalance;
    private double ledgerBalance;
    private double holdAmount;

    private DateTime odaLimitReviewDate;
    private DateTime lisFinTranDate;
    private DateTime interestFromDate;
    private DateTime interestToDate;
    private DateTime openedDate;
    private DateTime lastStatementDate;
    private DateTime nextDateTime;
    private DateTime closedDate;
    private DateTime deletedOn;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountStatusCode() {
        return accountStatusCode;
    }

    public void setAccountStatusCode(String accountStatusCode) {
        this.accountStatusCode = accountStatusCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getLimitProfile1() {
        return limitProfile1;
    }

    public void setLimitProfile1(String limitProfile1) {
        this.limitProfile1 = limitProfile1;
    }

    public String getAccountTitleOther() {
        return accountTitleOther;
    }

    public void setAccountTitleOther(String accountTitleOther) {
        this.accountTitleOther = accountTitleOther;
    }

    public String getOfficerCode() {
        return officerCode;
    }

    public void setOfficerCode(String officerCode) {
        this.officerCode = officerCode;
    }

    public String getStatementFrequency() {
        return statementFrequency;
    }

    public void setStatementFrequency(String statementFrequency) {
        this.statementFrequency = statementFrequency;
    }

    public String getIntroRelationShipNo() {
        return introRelationShipNo;
    }

    public void setIntroRelationShipNo(String introRelationShipNo) {
        this.introRelationShipNo = introRelationShipNo;
    }

    public String getIntroAddress() {
        return introAddress;
    }

    public void setIntroAddress(String introAddress) {
        this.introAddress = introAddress;
    }

    public String getIntroAccountNo() {
        return introAccountNo;
    }

    public void setIntroAccountNo(String introAccountNo) {
        this.introAccountNo = introAccountNo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getClosedRemark() {
        return closedRemark;
    }

    public void setClosedRemark(String closedRemark) {
        this.closedRemark = closedRemark;
    }

    public String getJointDescription() {
        return jointDescription;
    }

    public void setJointDescription(String jointDescription) {
        this.jointDescription = jointDescription;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getT1() {
        return T1;
    }

    public void setT1(String t1) {
        T1 = t1;
    }

    public String getT2() {
        return T2;
    }

    public void setT2(String t2) {
        T2 = t2;
    }

    public String getT3() {
        return T3;
    }

    public void setT3(String t3) {
        T3 = t3;
    }

    public String getT4() {
        return T4;
    }

    public void setT4(String t4) {
        T4 = t4;
    }

    public String getT5() {
        return T5;
    }

    public void setT5(String t5) {
        T5 = t5;
    }

    public String getT6() {
        return T6;
    }

    public void setT6(String t6) {
        T6 = t6;
    }

    public String getT7() {
        return T7;
    }

    public void setT7(String t7) {
        T7 = t7;
    }

    public String getT8() {
        return T8;
    }

    public void setT8(String t8) {
        T8 = t8;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(double ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public double getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(double holdAmount) {
        this.holdAmount = holdAmount;
    }

    public DateTime getOdaLimitReviewDate() {
        return odaLimitReviewDate;
    }

    public void setOdaLimitReviewDate(DateTime odaLimitReviewDate) {
        this.odaLimitReviewDate = odaLimitReviewDate;
    }

    public DateTime getLisFinTranDate() {
        return lisFinTranDate;
    }

    public void setLisFinTranDate(DateTime lisFinTranDate) {
        this.lisFinTranDate = lisFinTranDate;
    }

    public DateTime getInterestFromDate() {
        return interestFromDate;
    }

    public void setInterestFromDate(DateTime interestFromDate) {
        this.interestFromDate = interestFromDate;
    }

    public DateTime getInterestToDate() {
        return interestToDate;
    }

    public void setInterestToDate(DateTime interestToDate) {
        this.interestToDate = interestToDate;
    }

    public DateTime getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(DateTime openedDate) {
        this.openedDate = openedDate;
    }

    public DateTime getLastStatementDate() {
        return lastStatementDate;
    }

    public void setLastStatementDate(DateTime lastStatementDate) {
        this.lastStatementDate = lastStatementDate;
    }

    public DateTime getNextDateTime() {
        return nextDateTime;
    }

    public void setNextDateTime(DateTime nextDateTime) {
        this.nextDateTime = nextDateTime;
    }

    public DateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(DateTime closedDate) {
        this.closedDate = closedDate;
    }

    public DateTime getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(DateTime deletedOn) {
        this.deletedOn = deletedOn;
    }


}
