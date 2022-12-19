package com.inov8.microbank.disbursement.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by AtieqRe on 2/19/2017.
 */
public class DisbursementVO implements Serializable {
    private Long disbursementsFileInfoId;
    private Long disbursementId;
    private Long appUserTypeId;
    private String name;
    private String mobileNo;
    private String cnic;
    private Double amount;
    private Double charges;
    private Double fed;
    private Date paymentDate;
    private Boolean settled;
    private Boolean posted;
    private String description;
    private String batchNumber;
    private Boolean limitApplicable;
    private Boolean payCashViaCnic;
    private Boolean isCoreSumAccountNumber;
    private String sourceACNo;
    private Long productId;
    private String productName;
    private Long serviceId;
    private String serviceName;

    public Long getDisbursementsFileInfoId() {
        return disbursementsFileInfoId;
    }

    public void setDisbursementsFileInfoId(Long disbursementsFileInfoId) {
        this.disbursementsFileInfoId = disbursementsFileInfoId;
    }

    public Long getDisbursementId() {
        return disbursementId;
    }

    public void setDisbursementId(Long disbursementId) {
        this.disbursementId = disbursementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Double getFed() {
        return fed;
    }

    public void setFed(Double fed) {
        this.fed = fed;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Boolean getLimitApplicable() {
        return limitApplicable;
    }

    public void setLimitApplicable(Boolean limitApplicable) {
        this.limitApplicable = limitApplicable;
    }

    public Boolean getPayCashViaCnic() {
        return payCashViaCnic;
    }

    public void setPayCashViaCnic(Boolean payCashViaCnic) {
        this.payCashViaCnic = payCashViaCnic;
    }

    public String getSourceACNo() {
        return sourceACNo;
    }

    public void setSourceACNo(String sourceACNo) {
        this.sourceACNo = sourceACNo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Boolean isCoreSumAccountNumber() {
        return isCoreSumAccountNumber;
    }

    public void setCoreSumAccountNumber(Boolean coreSumAccountNumber) {
        isCoreSumAccountNumber = coreSumAccountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long appUserTypeId) {
        this.appUserTypeId = appUserTypeId;
    }
}
