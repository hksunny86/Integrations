package com.inov8.integration.webservice.vo;

import java.io.Serializable;


public class TransactionChargesVO implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private String processingFee;
    private String processingFeeGl;
    private String serviceFee;
    private String serviceFeeGl;
    private String latePaymentFee;
    private String latePaymentFeeGl;
    private String earlyPaymentFee;
    private String earlyPaymentFeeGl;
    private String eStampCharges;
    private String eStampChargesGl;

    public String getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(String processingFee) {
        this.processingFee = processingFee;
    }

    public String getProcessingFeeGl() {
        return processingFeeGl;
    }

    public void setProcessingFeeGl(String processingFeeGl) {
        this.processingFeeGl = processingFeeGl;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getServiceFeeGl() {
        return serviceFeeGl;
    }

    public void setServiceFeeGl(String serviceFeeGl) {
        this.serviceFeeGl = serviceFeeGl;
    }

    public String getLatePaymentFee() {
        return latePaymentFee;
    }

    public void setLatePaymentFee(String latePaymentFee) {
        this.latePaymentFee = latePaymentFee;
    }

    public String getLatePaymentFeeGl() {
        return latePaymentFeeGl;
    }

    public void setLatePaymentFeeGl(String latePaymentFeeGl) {
        this.latePaymentFeeGl = latePaymentFeeGl;
    }

    public String getEarlyPaymentFee() {
        return earlyPaymentFee;
    }

    public void setEarlyPaymentFee(String earlyPaymentFee) {
        this.earlyPaymentFee = earlyPaymentFee;
    }

    public String getEarlyPaymentFeeGl() {
        return earlyPaymentFeeGl;
    }

    public void setEarlyPaymentFeeGl(String earlyPaymentFeeGl) {
        this.earlyPaymentFeeGl = earlyPaymentFeeGl;
    }

    public String geteStampCharges() {
        return eStampCharges;
    }

    public void seteStampCharges(String eStampCharges) {
        this.eStampCharges = eStampCharges;
    }

    public String geteStampChargesGl() {
        return eStampChargesGl;
    }

    public void seteStampChargesGl(String eStampChargesGl) {
        this.eStampChargesGl = eStampChargesGl;
    }
}
