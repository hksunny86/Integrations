package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("UBPSCompany")
public class UBPSCompany implements Serializable {
    private String companyName;
    private String companyId;
    private String companyCode;
    private String categoryCode;
    private String companyPrefixCode;
    private String companyMinPayment;
    private String companyMaxPayment;
    private String companyPartialPaymentAllow;
    private String companyMinLength;
    private String companyMaxLength;
    private String companyBillFetchFlag;
    private String consumerNumberLabel;

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyId() { return companyId; }

    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCompanyCode() { return companyCode; }

    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }

    public String getCategoryCode() { return categoryCode; }

    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getCompanyPrefixCode() { return companyPrefixCode; }

    public void setCompanyPrefixCode(String companyPrefixCode) { this.companyPrefixCode = companyPrefixCode; }

    public String getCompanyMinPayment() { return companyMinPayment; }

    public void setCompanyMinPayment(String companyMinPayment) { this.companyMinPayment = companyMinPayment; }

    public String getCompanyMaxPayment() { return companyMaxPayment; }

    public void setCompanyMaxPayment(String companyMaxPayment) { this.companyMaxPayment = companyMaxPayment; }

    public String getCompanyPartialPaymentAllow() { return companyPartialPaymentAllow; }

    public void setCompanyPartialPaymentAllow(String companyPartialPaymentAllow) { this.companyPartialPaymentAllow = companyPartialPaymentAllow; }

    public String getCompanyMinLength() { return companyMinLength; }

    public void setCompanyMinLength(String companyMinLength) { this.companyMinLength = companyMinLength; }

    public String getCompanyMaxLength() { return companyMaxLength; }

    public void setCompanyMaxLength(String companyMaxLength) { this.companyMaxLength = companyMaxLength; }

    public String getCompanyBillFetchFlag() { return companyBillFetchFlag; }

    public void setCompanyBillFetchFlag(String companyBillFetchFlag) { this.companyBillFetchFlag = companyBillFetchFlag; }

    public String getConsumerNumberLabel() {
        return consumerNumberLabel;
    }

    public void setConsumerNumberLabel(String consumerNumberLabel) {
        this.consumerNumberLabel = consumerNumberLabel;
    }
}
