package com.inov8.integration.channel.BOPBLB.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CashOutInquiryRequest extends Request {


    private String username;
    private String password;
    private String bankMnemonic;
    private String cnic;
    private String debitCardNumber;
    private String mobileNumber;
    private String segmentId;
    private String amount;
    private String dateTime;
    private String agentLocation;
    private String agentCity;
    private String terminalId;
    private String transactionId;


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankMnemonic() {
        return bankMnemonic;
    }

    public void setBankMnemonic(String bankMnemonic) {
        this.bankMnemonic = bankMnemonic;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
        this.setDebitCardNumber(i8SBSwitchControllerRequestVO.getAccountNumber());
        this.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setSegmentId(i8SBSwitchControllerRequestVO.getSegmentId());
        this.setTerminalId(i8SBSwitchControllerRequestVO.getTerminalID());
        this.setAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.setAgentCity(i8SBSwitchControllerRequestVO.getAgenCity());
        this.setAgentLocation(i8SBSwitchControllerRequestVO.getAgentLocation());
        this.setDateTime(i8SBSwitchControllerRequestVO.getDateTime());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
          if (StringUtils.isEmpty(this.getSegmentId())) {
            throw new I8SBValidationException("[Failed] SegmentId" + this.getSegmentId());
        } else if (StringUtils.isEmpty(this.getAmount())) {
            throw new I8SBValidationException("[Failed] Amount" + this.getAmount());
        }

        return true;
    }
}
