package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlElement;

public class CashWithdrawlReversalRequest extends Request {

    private String mobileNo;
    private String rrn;
    private String terminalId;
    private String orignalRRN;
    private String orignalTransactionDateTime;
    private String transactionCode;
    private String transactiondateTime;


    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getOrignalRRN() {
        return orignalRRN;
    }

    public void setOrignalRRN(String orignalRRN) {
        this.orignalRRN = orignalRRN;
    }

    public String getOrignalTransactionDateTime() {
        return orignalTransactionDateTime;
    }

    public void setOrignalTransactionDateTime(String orignalTransactionDateTime) {
        this.orignalTransactionDateTime = orignalTransactionDateTime;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setMobileNo(i8SBSwitchControllerRequestVO.getSenderMobile());
        this.setOrignalRRN(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setOrignalTransactionDateTime(i8SBSwitchControllerRequestVO.getOriginalTxnDateTime());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setTransactionCode(i8SBSwitchControllerRequestVO.getTranCode());
        this.setTerminalId(i8SBSwitchControllerRequestVO.getAgentId());


    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
