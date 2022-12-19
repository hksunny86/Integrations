package com.inov8.integration.channel.BOPBLB.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlElement;

public class ProofOfLifeVerificationResponse extends Response {

    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("RRN")
    private String rrn;
    @JsonProperty("Finger_Index")
    private String fingerIndex;
    @JsonProperty("Session_ID")
    private String sessionId;


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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO=new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
        i8SBSwitchControllerResponseVO.setFingerIndex(this.getFingerIndex());
        i8SBSwitchControllerResponseVO.setRrn(this.getRrn());
        i8SBSwitchControllerResponseVO.setSessionId(this.getSessionId());
        return i8SBSwitchControllerResponseVO;
    }
}
