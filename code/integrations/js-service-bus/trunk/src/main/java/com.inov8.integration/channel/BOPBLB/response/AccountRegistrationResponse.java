package com.inov8.integration.channel.BOPBLB.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlElement;

public class AccountRegistrationResponse extends Response
{

    protected String responseCode;
    protected String responseDescription;
    protected String rrn;
    protected String fingerIndex;
    protected String sessionID;
    protected String transactionID;
    protected String statusCode;
    protected String status;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO=new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setFingerIndex(this.getFingerIndex());
        i8SBSwitchControllerResponseVO.setSessionId(this.getSessionID());
        return i8SBSwitchControllerResponseVO;
    }
}
