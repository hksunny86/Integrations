package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlElement;

public class AccountRegistrationRequest extends Request{

    private String cnic;
    private String mobileNo;
    private String tokenID;
    private String debitCardNumber;
    private String sessionId;
    private String otp;
    private String fingerIndex;
    private String fingerTemplate;
    private String segmentID;
    private String templateType;
    private String agentLocation;
    private String agentCity;
    private String terminalId;
    private String rrn;
    private String transactionType;


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getFingerTemplate() {
        return fingerTemplate;
    }

    public void setFingerTemplate(String fingerTemplate) {
        this.fingerTemplate = fingerTemplate;
    }

    public String getSegmentID() {
        return segmentID;
    }

    public void setSegmentID(String segmentID) {
        this.segmentID = segmentID;
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
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setAgentCity(i8SBSwitchControllerRequestVO.getAgenCity());
        this.setDebitCardNumber(i8SBSwitchControllerRequestVO.getCardId());
        this.setTerminalId(i8SBSwitchControllerRequestVO.getTerminalID());
        this.setSegmentID(i8SBSwitchControllerRequestVO.getSegmentId());
        this.setAgentLocation(i8SBSwitchControllerRequestVO.getAgentLocation());
        this.setFingerIndex(i8SBSwitchControllerRequestVO.getFingerIndex());
        this.setFingerTemplate(i8SBSwitchControllerRequestVO.getFingerTemplete());
        this.setTemplateType(i8SBSwitchControllerRequestVO.getTempeleteType());
        this.setOtp(i8SBSwitchControllerRequestVO.getOtp());
        this.setSessionId(i8SBSwitchControllerRequestVO.getSessionId());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setTransactionType(i8SBSwitchControllerRequestVO.getTransactionType());


    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
