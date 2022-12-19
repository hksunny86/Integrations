package com.inov8.integration.channel.microbank.request.BopSuperAgent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public class AccountRegistrationRequest extends Request {

    @JsonProperty("Debit_Card_Number")
    private String debitCardNumber;
    @JsonProperty("Mobile_No")
    private String mobileNo;
    @JsonProperty("Segment_ID")
    private String segmentId;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("Date_Time")
    private String dateTime;
    @JsonProperty("RRN")
    private String rrn;
    @JsonProperty("OTP")
    private String otp;
    @JsonProperty("Finger_Index")
    private String fingeIndex;
    @JsonProperty("Finger_Template")
    private String fingerTemplate;
    @JsonProperty("Template_Type")
    private String templateType;
    @JsonProperty("Agent_Location")
    private String agentLocation;
    @JsonProperty("Agent_City")
    private String agentCity;
    @JsonProperty("Terminal_Id")
    private String terminalId;
    @JsonProperty("SessionID")
    private String sessionID;


    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getFingeIndex() {
        return fingeIndex;
    }

    public void setFingeIndex(String fingeIndex) {
        this.fingeIndex = fingeIndex;
    }

    public String getFingerTemplate() {
        return fingerTemplate;
    }

    public void setFingerTemplate(String fingerTemplate) {
        this.fingerTemplate = fingerTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    public String getAgentCity() {
        return agentCity;
    }

    public void setAgentCity(String agentCity) {
        this.agentCity = agentCity;
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

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
