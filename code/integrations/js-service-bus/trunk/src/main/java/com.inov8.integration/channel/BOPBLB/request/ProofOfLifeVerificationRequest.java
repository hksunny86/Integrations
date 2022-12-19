package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class ProofOfLifeVerificationRequest extends Request {

    private String mobileNo;
    private String cnic;
    private String segmentId;
    private String rrn;
    private String fingeIndex;
    private String fingerTemplate;
    private String templateType;
    private String sessionID;
    private String agentLocation;
    private String agentCity;
    private String agentID;
    private String machineName;
    private String ipAddress;
    private String macAddress;
    private String udid;
    private String acknowledgmentFlag;
    private String statusFlag;

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }


    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getAcknowledgmentFlag() {
        return acknowledgmentFlag;
    }

    public void setAcknowledgmentFlag(String acknowledgmentFlag) {
        this.acknowledgmentFlag = acknowledgmentFlag;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMobileNo(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setSegmentId(i8SBSwitchControllerRequestVO.getSegmentId());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setFingeIndex(i8SBSwitchControllerRequestVO.getFingerIndex());
        this.setFingerTemplate(i8SBSwitchControllerRequestVO.getFingerTemplete());
        this.setTemplateType(i8SBSwitchControllerRequestVO.getTempeleteType());
        this.setSessionID(i8SBSwitchControllerRequestVO.getSessionId());
        this.setAgentLocation(i8SBSwitchControllerRequestVO.getAgentLocation());
        this.setAgentID(i8SBSwitchControllerRequestVO.getAgentId());
        this.setAgentCity(i8SBSwitchControllerRequestVO.getAgentCity());
        this.setIpAddress(i8SBSwitchControllerRequestVO.getIpAddress());
        this.setMacAddress(i8SBSwitchControllerRequestVO.getMacAddress());
        this.setMachineName(i8SBSwitchControllerRequestVO.getMachineName());
        this.setAcknowledgmentFlag(i8SBSwitchControllerRequestVO.getStatusFlag());
        this.setUdid(i8SBSwitchControllerRequestVO.getUdid());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getMobileNo())) {
            throw new I8SBValidationException("[Failed] MobileNo " + this.getMobileNo());
        }
        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed] CNIC " + this.getCnic());
        }
        if (StringUtils.isEmpty(this.getSegmentId())) {
            throw new I8SBValidationException("[Failed] SegmentId " + this.getSegmentId());
        }

        if (StringUtils.isEmpty(this.getRrn())) {
            throw new I8SBValidationException("[Failed] RRN " + this.getRrn());
        }
        if (StringUtils.isEmpty(this.getFingeIndex())) {
            throw new I8SBValidationException("[Failed] FingeIndex " + this.getFingeIndex());
        }
        if (StringUtils.isEmpty(this.getFingerTemplate())) {
            throw new I8SBValidationException("[Failed] FingerTemplate " + this.getFingerTemplate());
        }
        if (StringUtils.isEmpty(this.getTemplateType())) {
            throw new I8SBValidationException("[Failed] TemplateType " + this.getTemplateType());
        }
        if (StringUtils.isEmpty(this.getAgentID())) {
            throw new I8SBValidationException("[Failed] AgentID " + this.getAgentID());
        }
        if (StringUtils.isEmpty(this.getMachineName())) {
            throw new I8SBValidationException("[Failed] MachineName " + this.getMachineName());
        }
        if (StringUtils.isEmpty(this.getIpAddress())) {
            throw new I8SBValidationException("[Failed] IpAddress " + this.getIpAddress());
        }
        return true;
    }
}
