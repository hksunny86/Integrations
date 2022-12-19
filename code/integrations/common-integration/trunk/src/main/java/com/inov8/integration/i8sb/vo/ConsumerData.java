package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("ConsumerData")
public class ConsumerData implements Serializable {

    private final static long serialVersionUID = 1L;

    private String UBPSCompanyCode;
    private String UBPS1LinkCompanyCode;
    private String UBPSCompanyName;
    private String UBPSCategoryCode;
    private String UBPSCategoryName;
    private String UBPSConsumerNo;
    private String UBPSConsumerNickName;
    private String UBPSPaymentMinLength;
    private String UBPSPaymentMaxLength;
    private String UBPSAmountMultipleOf;
    private String UBPSLastAmount;
    private String UBPSLastDate;
    private String UBPSMinLength;
    private String UBPSMaxLength;
    private String UBPSDueDate;
    private String UBPSAllowAfterDueDate;
    private String UBPSPartiallyPayment;
    private String UBPSFlag;
    private String UBPSFFlag;
    private String UBPSDueAmount;
    private String UBPSBillStatus;
    private String UBPSConsumerName;

    public String getUBPSBillStatus() {
        return UBPSBillStatus;
    }

    public void setUBPSBillStatus(String UBPSBillStatus) {
        this.UBPSBillStatus = UBPSBillStatus;
    }

    public String getUBPSFFlag() {
        return UBPSFFlag;
    }

    public void setUBPSFFlag(String UBPSFFlag) {
        this.UBPSFFlag = UBPSFFlag;
    }

    public String getUBPSDueAmount() {
        return UBPSDueAmount;
    }

    public void setUBPSDueAmount(String UBPSDueAmount) {
        this.UBPSDueAmount = UBPSDueAmount;
    }

    public String getUBPSMinLength() {
        return UBPSMinLength;
    }

    public void setUBPSMinLength(String UBPSMinLength) {
        this.UBPSMinLength = UBPSMinLength;
    }

    public String getUBPSMaxLength() {
        return UBPSMaxLength;
    }

    public void setUBPSMaxLength(String UBPSMaxLength) {
        this.UBPSMaxLength = UBPSMaxLength;
    }

    public String getUBPSDueDate() {
        return UBPSDueDate;
    }

    public void setUBPSDueDate(String UBPSDueDate) {
        this.UBPSDueDate = UBPSDueDate;
    }

    public String getUBPSAllowAfterDueDate() {
        return UBPSAllowAfterDueDate;
    }

    public void setUBPSAllowAfterDueDate(String UBPSAllowAfterDueDate) {
        this.UBPSAllowAfterDueDate = UBPSAllowAfterDueDate;
    }

    public String getUBPSPartiallyPayment() {
        return UBPSPartiallyPayment;
    }

    public void setUBPSPartiallyPayment(String UBPSPartiallyPayment) {
        this.UBPSPartiallyPayment = UBPSPartiallyPayment;
    }

    public String getUBPSFlag() {
        return UBPSFlag;
    }

    public void setUBPSFlag(String UBPSFlag) {
        this.UBPSFlag = UBPSFlag;
    }

    public String getUBPS1LinkCompanyCode() {
        return UBPS1LinkCompanyCode;
    }

    public void setUBPS1LinkCompanyCode(String UBPS1LinkCompanyCode) {
        this.UBPS1LinkCompanyCode = UBPS1LinkCompanyCode;
    }

    public String getUBPSCompanyCode() {
        return UBPSCompanyCode;
    }

    public void setUBPSCompanyCode(String UBPSCompanyCode) {
        this.UBPSCompanyCode = UBPSCompanyCode;
    }

    public String getUBPSCompanyName() {
        return UBPSCompanyName;
    }

    public void setUBPSCompanyName(String UBPSCompanyName) {
        this.UBPSCompanyName = UBPSCompanyName;
    }

    public String getUBPSCategoryCode() {
        return UBPSCategoryCode;
    }

    public void setUBPSCategoryCode(String UBPSCategoryCode) {
        this.UBPSCategoryCode = UBPSCategoryCode;
    }

    public String getUBPSCategoryName() {
        return UBPSCategoryName;
    }

    public void setUBPSCategoryName(String UBPSCategoryName) {
        this.UBPSCategoryName = UBPSCategoryName;
    }

    public String getUBPSConsumerNo() {
        return UBPSConsumerNo;
    }

    public void setUBPSConsumerNo(String UBPSConsumerNo) {
        this.UBPSConsumerNo = UBPSConsumerNo;
    }

    public String getUBPSConsumerNickName() {
        return UBPSConsumerNickName;
    }

    public void setUBPSConsumerNickName(String UBPSConsumerNickName) {
        this.UBPSConsumerNickName = UBPSConsumerNickName;
    }

    public String getUBPSPaymentMinLength() {
        return UBPSPaymentMinLength;
    }

    public void setUBPSPaymentMinLength(String UBPSPaymentMinLength) {
        this.UBPSPaymentMinLength = UBPSPaymentMinLength;
    }

    public String getUBPSPaymentMaxLength() {
        return UBPSPaymentMaxLength;
    }

    public void setUBPSPaymentMaxLength(String UBPSPaymentMaxLength) {
        this.UBPSPaymentMaxLength = UBPSPaymentMaxLength;
    }

    public String getUBPSAmountMultipleOf() {
        return UBPSAmountMultipleOf;
    }

    public void setUBPSAmountMultipleOf(String UBPSAmountMultipleOf) {
        this.UBPSAmountMultipleOf = UBPSAmountMultipleOf;
    }

    public String getUBPSLastAmount() {
        return UBPSLastAmount;
    }

    public void setUBPSLastAmount(String UBPSLastAmount) {
        this.UBPSLastAmount = UBPSLastAmount;
    }

    public String getUBPSLastDate() {
        return UBPSLastDate;
    }

    public void setUBPSLastDate(String UBPSLastDate) {
        this.UBPSLastDate = UBPSLastDate;
    }

    public String getUBPSConsumerName() {
        return UBPSConsumerName;
    }

    public void setUBPSConsumerName(String UBPSConsumerName) {
        this.UBPSConsumerName = UBPSConsumerName;
    }
}
