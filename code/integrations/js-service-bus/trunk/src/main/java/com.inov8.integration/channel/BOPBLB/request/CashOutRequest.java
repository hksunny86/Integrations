package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CashOutRequest extends Request {

    private String debitCardNumber;
    private String mobileNo;
    private String segmentID;
    private String amount;
    private String rrn;
    private String trnsactionID;
    private String otp;
    private String fingerIndex;
    private String fingerTemplate;
    private String templateType;
    private String agentLocation;
    private String agentCity;
    private String terminalId;
    private String cnic;




    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setDebitCardNumber(i8SBSwitchControllerRequestVO.getAccountNumber());
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setSegmentID(i8SBSwitchControllerRequestVO.getSegmentId());
        this.setTerminalId(i8SBSwitchControllerRequestVO.getTerminalID());
        this.setAgentCity(i8SBSwitchControllerRequestVO.getAgenCity());
        this.setAgentLocation(i8SBSwitchControllerRequestVO.getAgentLocation());
        this.setAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setTrnsactionID(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setOtp(i8SBSwitchControllerRequestVO.getOtp());
        this.setFingerIndex(i8SBSwitchControllerRequestVO.getFingerIndex());
        this.setFingerTemplate(i8SBSwitchControllerRequestVO.getFingerTemplete());
        this.setTemplateType(i8SBSwitchControllerRequestVO.getTempeleteType());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
       if (StringUtils.isEmpty(this.getAmount())) {
            throw new I8SBValidationException("[Failed] Amount" + this.getAmount());
        }
        return true;
    }


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

    public String getSegmentID() {
        return segmentID;
    }

    public void setSegmentID(String segmentID) {
        this.segmentID = segmentID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTrnsactionID() {
        return trnsactionID;
    }

    public void setTrnsactionID(String trnsactionID) {
        this.trnsactionID = trnsactionID;
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

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
}
