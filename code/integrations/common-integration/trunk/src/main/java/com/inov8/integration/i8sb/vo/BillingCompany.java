package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by Inov8 Limited on 10/18/2017.
 */
@XStreamAlias("BillingCompany")
public class BillingCompany implements Serializable {
    private final static long serialVersionUID = 1L;
    private String companyName;
    private String companyId;
    private String companyLabel;
    private String consumerLabel;
    private String billType;
    private String consumerMinLength;
    private String consumerMaxLength;
    private String minLimit;
    private String maxLimit;
    private String amountRequired;
    private String multiples;
    private String inputType;

    public BillingCompany() {
    }

    public BillingCompany(String companyName, String companyId, String companyLabel, String consumerLabel, String billType, String consumerMinLength, String consumerMaxLength, String minLimit, String maxLimit, String amountRequired, String multiples, String inputType) {
        this.companyName = companyName;
        this.companyId = companyId;
        this.companyLabel = companyLabel;
        this.consumerLabel = consumerLabel;
        this.billType = billType;
        this.consumerMinLength = consumerMinLength;
        this.consumerMaxLength = consumerMaxLength;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.amountRequired = amountRequired;
        this.multiples = multiples;
        this.inputType = inputType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyLabel() {
        return companyLabel;
    }

    public void setCompanyLabel(String companyLabel) {
        this.companyLabel = companyLabel;
    }

    public String getConsumerLabel() {
        return consumerLabel;
    }

    public void setConsumerLabel(String consumerLabel) {
        this.consumerLabel = consumerLabel;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getConsumerMinLength() {
        return consumerMinLength;
    }

    public void setConsumerMinLength(String consumerMinLength) {
        this.consumerMinLength = consumerMinLength;
    }

    public String getConsumerMaxLength() {
        return consumerMaxLength;
    }

    public void setConsumerMaxLength(String consumerMaxLength) {
        this.consumerMaxLength = consumerMaxLength;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(String minLimit) {
        this.minLimit = minLimit;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getAmountRequired() {
        return amountRequired;
    }

    public void setAmountRequired(String amountRequired) {
        this.amountRequired = amountRequired;
    }

    public String getMultiples() {
        return multiples;
    }

    public void setMultiples(String multiples) {
        this.multiples = multiples;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}
