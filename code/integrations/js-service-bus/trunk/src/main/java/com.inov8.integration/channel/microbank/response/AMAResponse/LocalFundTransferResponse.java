package com.inov8.integration.channel.microbank.response.AMAResponse;


import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

public class LocalFundTransferResponse extends Response{
    private String responseCode;
    private String autherizedIDResponse;
    private String PaymentReciepientNumber;
    private String destinationAccountNo;
    private String feeCharged;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAutherizedIDResponse() {
        return autherizedIDResponse;
    }

    public void setAutherizedIDResponse(String autherizedIDResponse) {
        this.autherizedIDResponse = autherizedIDResponse;
    }

    public String getPaymentReciepientNumber() {
        return PaymentReciepientNumber;
    }

    public void setPaymentReciepientNumber(String paymentReciepientNumber) {
        PaymentReciepientNumber = paymentReciepientNumber;
    }

    public String getDestinationAccountNo() {
        return destinationAccountNo;
    }

    public void setDestinationAccountNo(String destinationAccountNo) {
        this.destinationAccountNo = destinationAccountNo;
    }

    public String getFeeCharged() {
        return feeCharged;
    }

    public void setFeeCharged(String feeCharged) {
        this.feeCharged = feeCharged;
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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        return null;
    }
}
