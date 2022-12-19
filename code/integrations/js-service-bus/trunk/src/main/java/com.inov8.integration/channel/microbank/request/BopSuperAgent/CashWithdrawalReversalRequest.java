package com.inov8.integration.channel.microbank.request.BopSuperAgent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public class CashWithdrawalReversalRequest extends Request {
    @JsonProperty("Mobile_No")
    private String mobileNo;
    @JsonProperty("Transaction_Code")
    private String transactionCode;
    @JsonProperty("Date_Time")
    private String dateTime;
    @JsonProperty("RRN")
    private String rrn;
    @JsonProperty("Orignal_RRN")
    private String orignalRrn;
    @JsonProperty(" Orignal_Transaction_Date_Time")
    private String orignalTransactiondateTime;
    @JsonProperty("Terminal_Id")
    private String terminalId;


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getOrignalRrn() {
        return orignalRrn;
    }

    public void setOrignalRrn(String orignalRrn) {
        this.orignalRrn = orignalRrn;
    }

    public String getOrignalTransactiondateTime() {
        return orignalTransactiondateTime;
    }

    public void setOrignalTransactiondateTime(String orignalTransactiondateTime) {
        this.orignalTransactiondateTime = orignalTransactiondateTime;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
