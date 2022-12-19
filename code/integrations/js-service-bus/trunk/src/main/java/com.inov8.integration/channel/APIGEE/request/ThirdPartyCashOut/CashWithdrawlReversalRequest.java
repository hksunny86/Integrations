package com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class CashWithdrawlReversalRequest extends Request implements Serializable {
    private static final long serialVersionUID = 2134956891427267824L;


    @JsonProperty("BISPWithdarawalReversal")
    private BISPWithdarawalReversal bispWithdarawalReversal=new BISPWithdarawalReversal();

    public BISPWithdarawalReversal getBispWithdarawalReversal() {
        return bispWithdarawalReversal;
    }

    public void setBispWithdarawalReversal(BISPWithdarawalReversal bispWithdarawalReversal) {
        this.bispWithdarawalReversal = bispWithdarawalReversal;
    }

    public static class BISPWithdarawalReversal {

        @JsonProperty("CustomerAccNum")
        private String customerAccountNumber;
        @JsonProperty("TransactionAmount")
        private String transactionAmount;
        @JsonProperty("TransactionID")
        private String transactionId;
        @JsonProperty("transmissionDateTime")
        private String transmissionDateTime;
        @JsonProperty("AgentAccNum")
        private String agentAccountNumber;
        @JsonProperty("SellerCode")
        private String sellerCode;
        @JsonProperty("SessionIDBAFL")
        private String sessionId;
        @JsonProperty("CNIC")
        private String cnic;
        @JsonProperty("ProjectCode")
        private String projectCode;
        @JsonProperty("TerminalID")
        private String terminalId;
        @JsonProperty("Reserved1")
        private String reserved1;
        @JsonProperty("Reserved2")
        private String reserved2;
        @JsonProperty("Reserved3")
        private String reserved3;
        @JsonProperty("Reserved4")
        private String reserved4;
        @JsonProperty("Reserved5")
        private String reserved5;
        @JsonProperty("accessToken")
        private String accessToken;
        @JsonProperty("AreaName")
        private String areaName;


        public String getCustomerAccountNumber() {
            return customerAccountNumber;
        }

        public void setCustomerAccountNumber(String customerAccountNumber) {
            this.customerAccountNumber = customerAccountNumber;
        }

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(String transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getTransmissionDateTime() {
            return transmissionDateTime;
        }

        public void setTransmissionDateTime(String transmissionDateTime) {
            this.transmissionDateTime = transmissionDateTime;
        }

        public String getAgentAccountNumber() {
            return agentAccountNumber;
        }

        public void setAgentAccountNumber(String agentAccountNumber) {
            this.agentAccountNumber = agentAccountNumber;
        }

        public String getSellerCode() {
            return sellerCode;
        }

        public void setSellerCode(String sellerCode) {
            this.sellerCode = sellerCode;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getCnic() {
            return cnic;
        }

        public void setCnic(String cnic) {
            this.cnic = cnic;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public String getReserved1() {
            return reserved1;
        }

        public void setReserved1(String reserved1) {
            this.reserved1 = reserved1;
        }

        public String getReserved2() {
            return reserved2;
        }

        public void setReserved2(String reserved2) {
            this.reserved2 = reserved2;
        }

        public String getReserved3() {
            return reserved3;
        }

        public void setReserved3(String reserved3) {
            this.reserved3 = reserved3;
        }

        public String getReserved4() {
            return reserved4;
        }

        public void setReserved4(String reserved4) {
            this.reserved4 = reserved4;
        }

        public String getReserved5() {
            return reserved5;
        }

        public void setReserved5(String reserved5) {
            this.reserved5 = reserved5;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.getBispWithdarawalReversal().setCustomerAccountNumber(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.getBispWithdarawalReversal().setTransactionAmount(i8SBSwitchControllerRequestVO.getTransactionAmount());
        this.getBispWithdarawalReversal().setTransactionId("0" + i8SBSwitchControllerRequestVO.getTransactionId());
        this.getBispWithdarawalReversal().setTransmissionDateTime(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        this.getBispWithdarawalReversal().setAgentAccountNumber(i8SBSwitchControllerRequestVO.getSenderMobile());
        this.getBispWithdarawalReversal().setSellerCode(i8SBSwitchControllerRequestVO.getAgentId());
        this.getBispWithdarawalReversal().setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.getBispWithdarawalReversal().setSessionId(i8SBSwitchControllerRequestVO.getSessionId());
        this.getBispWithdarawalReversal().setProjectCode(i8SBSwitchControllerRequestVO.getProductCode());
        this.getBispWithdarawalReversal().setTerminalId(i8SBSwitchControllerRequestVO.getAgentId());
        this.getBispWithdarawalReversal().setAreaName(i8SBSwitchControllerRequestVO.getAreaName());
        this.getBispWithdarawalReversal().setReserved1("");
        this.getBispWithdarawalReversal().setReserved2("");
        this.getBispWithdarawalReversal().setReserved3("");
        this.getBispWithdarawalReversal().setReserved4("");
        this.getBispWithdarawalReversal().setReserved5("");



    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getCustomerAccountNumber())) {
            throw new I8SBValidationException("[Failed] CustomerAccountNumber: " + this.getBispWithdarawalReversal().getCustomerAccountNumber());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getTransactionAmount())) {
            throw new I8SBValidationException("[Failed] TransactionAmount: " + this.getBispWithdarawalReversal().getTransactionAmount());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getTransactionId())) {
            throw new I8SBValidationException("[Failed] TransactionId: " + this.getBispWithdarawalReversal().getTransactionId());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getTransmissionDateTime())) {
            throw new I8SBValidationException("[Failed] TransmissionDateTime: " + this.getBispWithdarawalReversal().getTransmissionDateTime());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getAgentAccountNumber())) {
            throw new I8SBValidationException("[Failed] AgentAccountNumber: " + this.getBispWithdarawalReversal().getAgentAccountNumber());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getSellerCode())) {
            throw new I8SBValidationException("[Failed] SellerCode: " + this.getBispWithdarawalReversal().getSellerCode());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getSessionId())) {
            throw new I8SBValidationException("[Failed] SessionId: " + this.getBispWithdarawalReversal().getSessionId());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getCnic())) {
            throw new I8SBValidationException("[Failed] Cnic: " + this.getBispWithdarawalReversal().getCnic());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getProjectCode())) {
            throw new I8SBValidationException("[Failed] projectCode: " + this.getBispWithdarawalReversal().getProjectCode());
        }
        if (StringUtils.isEmpty(this.getBispWithdarawalReversal().getTerminalId())) {
            throw new I8SBValidationException("[Failed] TerminalId: " + this.getBispWithdarawalReversal().getTerminalId());
        }

        return true;
    }


}
