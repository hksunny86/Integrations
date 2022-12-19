package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class ProofOfLifeVerificationInquiryRequest extends Request {

    private String mobileNumber;
    private String cnic;
    private String segmentID;
    private String rrn;
    private String agentLocation;
    private String agentCity;
    private String agentID;
    private String machineName;
    private String ipAddress;
    private String macAddress;
    private String udid;
    private String acknowledgedFlag ;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
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

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
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

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getAcknowledgedFlag() {
        return acknowledgedFlag;
    }

    public void setAcknowledgedFlag(String acknowledgedFlag) {
        this.acknowledgedFlag = acknowledgedFlag;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setSegmentID(i8SBSwitchControllerRequestVO.getSegmentId());
        this.setAgentCity(i8SBSwitchControllerRequestVO.getAgentCity());
        this.setAgentID(i8SBSwitchControllerRequestVO.getAgentId());
        this.setAgentLocation(i8SBSwitchControllerRequestVO.getAgentLocation());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setIpAddress(i8SBSwitchControllerRequestVO.getIpAddress());
        this.setMacAddress(i8SBSwitchControllerRequestVO.getMacAddress());
        this.setMachineName(i8SBSwitchControllerRequestVO.getMachineName());
        this.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setUdid(i8SBSwitchControllerRequestVO.getUdid());
        this.setAcknowledgedFlag(i8SBSwitchControllerRequestVO.getStatusFlag());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getRrn())) {
            throw new I8SBValidationException("[Failed] RRN" + this.getRrn());
        }
        if (StringUtils.isEmpty(this.getMobileNumber())) {
            throw new I8SBValidationException("[Failed] Mobile Number" + this.getMobileNumber());
        }
        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed] CNIC" + this.getCnic());
        }
        if (StringUtils.isEmpty(this.getSegmentID())) {
            throw new I8SBValidationException("[Failed] Segment Id" + this.getSegmentID());
        }
        if (StringUtils.isEmpty(this.getAgentID())) {
            throw new I8SBValidationException("[Failed] Agent Id" + this.getAgentID());
        }
        if (StringUtils.isEmpty(this.getMachineName())) {
            throw new I8SBValidationException("[Failed] Mashine Name" + this.getMachineName());
        }
        if (StringUtils.isEmpty(this.getIpAddress())) {
            throw new I8SBValidationException("[Failed] Ip Address" + this.getIpAddress());
        }
        if (StringUtils.isEmpty(this.getUdid())) {
            throw new I8SBValidationException("[Failed] UDID" + this.getUdid());
        }
//        if (StringUtils.isEmpty(this.getAcknowledgedFlag())) {
//            throw new I8SBValidationException("[Failed] Acknowledged Flag" + this.getAcknowledgedFlag());
//        }
        return true;
    }
}
