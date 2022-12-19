package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;

public class AccountRegistrationInquiryRequest extends Request {

    private String cnic;
    private String mobileNo;
    private String tokenID;
    private String debitCardNumber;
    private String transactionType;
    private String segmentID;
    private String agentLocation;
    private String agentCity;
    private String terminalId;
    private String fingerIndex;
    private String fingerTemplate;
    private String templateType;

    private String rrn;


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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getSegmentID() {
        return segmentID;
    }

    public void setSegmentID(String segmentID) {
        this.segmentID = segmentID;
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
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getAgenCity())) {
            this.setAgentCity("");
        } else {
            this.setAgentCity(i8SBSwitchControllerRequestVO.getAgenCity());
        }

        this.setDebitCardNumber(i8SBSwitchControllerRequestVO.getCardId());
        this.setTransactionType(i8SBSwitchControllerRequestVO.getTransactionType());
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTerminalID())) {
            this.setTerminalId("");
        } else {
            this.setTerminalId(i8SBSwitchControllerRequestVO.getTerminalID());
        }
        this.setSegmentID(i8SBSwitchControllerRequestVO.getSegmentId());
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getAgentLocation())) {
            this.setAgentLocation("");
        } else {
            this.setAgentLocation(i8SBSwitchControllerRequestVO.getAgentLocation());
        }
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
