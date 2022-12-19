package com.inov8.integration.channel.BOPBLB.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlElement;

public class CashOutResponse extends Response {

    @JsonProperty("ResponseCode")
    protected String responseCode;
    @JsonProperty("ResponseDescription")
    protected String responseDescription;
    @JsonProperty("RRN")
    protected String rrn;
    @JsonProperty("Finger_Index")
    protected String fingerIndex;
    @JsonProperty("Session_ID")
    protected String sessionID;
    @JsonProperty("StatusCode")
    protected String statusCode;
    @JsonProperty("Status")
    protected String status;
    @JsonProperty("Transaction_ID")
    protected String transactionID;


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


    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setFingerIndex(this.getFingerIndex());
        i8SBSwitchControllerResponseVO.setSessionId(this.getSessionID());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setTransactionId(this.getTransactionID());
        return i8SBSwitchControllerResponseVO;
    }
}
