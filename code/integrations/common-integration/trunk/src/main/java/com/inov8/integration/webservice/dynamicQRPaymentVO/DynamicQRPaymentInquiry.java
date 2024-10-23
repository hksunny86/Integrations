package com.inov8.integration.webservice.dynamicQRPaymentVO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicQRPaymentInquiry implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String transactionId;
    private String transactionCode;
    private String createdOn;
    private String productId;
    private String productName;
    private String transactionCodeId;
    private String transactionDetailId;
    private String transactionAmount;
    private String processingStatusName;
    private String recipientMobileNo;
    private String fonepayTransactionCode;
    private String stan;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String reserved5;
    private String reserved6;
    private String reserved7;
    private String reserved8;
    private String reserved9;
    private String reserved10;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTransactionCodeId() {
        return transactionCodeId;
    }

    public void setTransactionCodeId(String transactionCodeId) {
        this.transactionCodeId = transactionCodeId;
    }

    public String getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(String transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getProcessingStatusName() {
        return processingStatusName;
    }

    public void setProcessingStatusName(String processingStatusName) {
        this.processingStatusName = processingStatusName;
    }

    public String getRecipientMobileNo() {
        return recipientMobileNo;
    }

    public void setRecipientMobileNo(String recipientMobileNo) {
        this.recipientMobileNo = recipientMobileNo;
    }

    public String getFonepayTransactionCode() {
        return fonepayTransactionCode;
    }

    public void setFonepayTransactionCode(String fonepayTransactionCode) {
        this.fonepayTransactionCode = fonepayTransactionCode;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    public String getReserved6() {
        return reserved6;
    }

    public void setReserved6(String reserved6) {
        this.reserved6 = reserved6;
    }

    public String getReserved7() {
        return reserved7;
    }

    public void setReserved7(String reserved7) {
        this.reserved7 = reserved7;
    }

    public String getReserved8() {
        return reserved8;
    }

    public void setReserved8(String reserved8) {
        this.reserved8 = reserved8;
    }

    public String getReserved9() {
        return reserved9;
    }

    public void setReserved9(String reserved9) {
        this.reserved9 = reserved9;
    }

    public String getReserved10() {
        return reserved10;
    }

    public void setReserved10(String reserved10) {
        this.reserved10 = reserved10;
    }
}
