package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class CardIssuanceReissuanceInquiry extends Request {

    private String cnic;
    private String mobileNo;
    private String debitCardNumber;
    private String transactionType;
    private String segmentID;
    private String agentLocation;
    private String agentCity;
    private String terminalId;
    private String rrn;

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

        if (StringUtils.isEmpty(this.getSegmentID())) {
            throw new I8SBValidationException("[Failed] SegmentId" + this.getSegmentID());
        }
        if (StringUtils.isEmpty(this.getDebitCardNumber())) {
            throw new I8SBValidationException("[Failed] Card Number" + this.getDebitCardNumber());
        }
        if (StringUtils.isEmpty(this.getCnic())) {
            throw new I8SBValidationException("[Failed] Cnic Number" + this.getCnic());
        }
        if (StringUtils.isEmpty(this.getMobileNo())) {
            throw new I8SBValidationException("[Failed] Mobile Number" + this.getMobileNo());
        }
        if (StringUtils.isEmpty(this.getTransactionType())) {

            throw new I8SBValidationException("[Failed] Transaction Type" + this.getTransactionType());

        }
        return true;
    }
}
