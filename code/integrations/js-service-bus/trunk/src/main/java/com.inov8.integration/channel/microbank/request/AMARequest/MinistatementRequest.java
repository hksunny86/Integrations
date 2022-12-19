package com.inov8.integration.channel.microbank.request.AMARequest;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Inov8 on 1/2/2020.
 */
public class MinistatementRequest extends Request {

    private String msisdn;
    private String accountNo;
    private String mPin;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String dateTime;
    private String bankImd;
    private String traceNo;


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBankImd() {
        return bankImd;
    }

    public void setBankImd(String bankImd) {
        this.bankImd = bankImd;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getmPin() {
        return mPin;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
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
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMsisdn(i8SBSwitchControllerRequestVO.getMsisdn());
        this.setAccountNo(i8SBSwitchControllerRequestVO.getAccountNumber());
        this.setReserved1(i8SBSwitchControllerRequestVO.getReserved1());
        this.setReserved2(i8SBSwitchControllerRequestVO.getReserved2());
        this.setReserved3(i8SBSwitchControllerRequestVO.getReserved3());
        this.setReserved4(i8SBSwitchControllerRequestVO.getReserved4());
        this.setDateTime(i8SBSwitchControllerRequestVO.getDateTime());
        this.setTraceNo(i8SBSwitchControllerRequestVO.getTraceNo());
        this.setBankImd(i8SBSwitchControllerRequestVO.getBankImd());
    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
