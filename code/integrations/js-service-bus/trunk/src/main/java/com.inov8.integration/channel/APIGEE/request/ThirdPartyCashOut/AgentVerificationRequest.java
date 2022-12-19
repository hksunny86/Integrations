package com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AgentVerificationRequest extends Request {

    private String accessToken;

    @JsonProperty("BISPAgentVerification")
    private BISPAgentVerification bispAgentVerification = new BISPAgentVerification();

    public BISPAgentVerification getBispAgentVerification() {
        return bispAgentVerification;
    }

    public void setBispAgentVerification(BISPAgentVerification bispAgentVerification) {
        this.bispAgentVerification = bispAgentVerification;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.getBispAgentVerification().setTransactionID("0" + i8SBSwitchControllerRequestVO.getTransactionId());
        this.getBispAgentVerification().setAgentAccNum(i8SBSwitchControllerRequestVO.getSenderMobile());
        this.getBispAgentVerification().setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.getBispAgentVerification().setSellerCode(i8SBSwitchControllerRequestVO.getAgentId());
        this.getBispAgentVerification().setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.getBispAgentVerification().setFingerIndex(i8SBSwitchControllerRequestVO.getFingerIndex());
        this.getBispAgentVerification().setFingerTemplate(i8SBSwitchControllerRequestVO.getFingerTemplete());
        this.getBispAgentVerification().setTemplateType(i8SBSwitchControllerRequestVO.getTempeleteType());
        this.getBispAgentVerification().setMinutiaeCount(i8SBSwitchControllerRequestVO.getMinutiaeCount());
        this.getBispAgentVerification().setNfiq(i8SBSwitchControllerRequestVO.getNfiq());
        this.getBispAgentVerification().setAreaName(i8SBSwitchControllerRequestVO.getAreaName());
        this.getBispAgentVerification().setImeiNumber(i8SBSwitchControllerRequestVO.getImeiNumber());
        this.getBispAgentVerification().setSessionIDNADRA(i8SBSwitchControllerRequestVO.getSessionIdNadra());
        this.getBispAgentVerification().setReserved(i8SBSwitchControllerRequestVO.getReserved1());
        this.setAccessToken(i8SBSwitchControllerRequestVO.getAccessToken());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getBispAgentVerification().getTransactionID())) {
            throw new I8SBValidationException("[Failed] TransactionId: " + this.getBispAgentVerification().getTransactionID());
        }

        if (StringUtils.isEmpty(this.getBispAgentVerification().getAgentAccNum())) {
            throw new I8SBValidationException("[Failed] AgentAccountNumber: " + this.getBispAgentVerification().getAgentAccNum());
        }
        if (StringUtils.isEmpty(this.getBispAgentVerification().getSellerCode())) {
            throw new I8SBValidationException("[Failed] SellerCode: " + this.getBispAgentVerification().getSellerCode());
        }
        if (StringUtils.isEmpty(this.getBispAgentVerification().getCnic())) {
            throw new I8SBValidationException("[Failed] Cnic: " + this.getBispAgentVerification().getCnic());
        }
        if (StringUtils.isEmpty(this.getBispAgentVerification().getFingerIndex())) {
            throw new I8SBValidationException("[Failed] finger Index: " + this.getBispAgentVerification().getFingerIndex());
        }

        if (StringUtils.isEmpty(this.getBispAgentVerification().getAreaName())) {
            throw new I8SBValidationException("[Failed] AreaName: " + this.getBispAgentVerification().getAreaName());
        }


        return true;
    }

    public static class BISPAgentVerification {

        @JsonProperty("TransactionID")
        private String transactionID;
        @JsonProperty("AgentAccNum")
        private String agentAccNum;
        @JsonProperty("SellerCode")
        private String sellerCode;
        @JsonProperty("SessionIDNADRA")
        private String sessionIDNADRA;
        @JsonProperty("CNIC")
        private String cnic;
        @JsonProperty("MobileNumber")
        private String mobileNumber;
        @JsonProperty("FingerTemplate")
        private String fingerTemplate;
        @JsonProperty("TemplateType")
        private String templateType;
        @JsonProperty("AreaName")
        private String areaName;
        @JsonProperty("NFIQ")
        private String nfiq;
        @JsonProperty("FingerIndex")
        private String fingerIndex;
        @JsonProperty("MinutiaeCount")
        private String minutiaeCount;
        @JsonProperty("IMEINumber")
        private String imeiNumber;
        @JsonProperty("Reserved")
        private String reserved;


        public String getTransactionID() {
            return transactionID;
        }

        public void setTransactionID(String transactionID) {
            this.transactionID = transactionID;
        }

        public String getAgentAccNum() {
            return agentAccNum;
        }

        public void setAgentAccNum(String agentAccNum) {
            this.agentAccNum = agentAccNum;
        }

        public String getSellerCode() {
            return sellerCode;
        }

        public void setSellerCode(String sellerCode) {
            this.sellerCode = sellerCode;
        }

        public String getSessionIDNADRA() {
            return sessionIDNADRA;
        }

        public void setSessionIDNADRA(String sessionIDNADRA) {
            this.sessionIDNADRA = sessionIDNADRA;
        }

        public String getCnic() {
            return cnic;
        }

        public void setCnic(String cnic) {
            this.cnic = cnic;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
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

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }


        public String getFingerIndex() {
            return fingerIndex;
        }

        public void setFingerIndex(String fingerIndex) {
            this.fingerIndex = fingerIndex;
        }

        public String getMinutiaeCount() {
            return minutiaeCount;
        }

        public void setMinutiaeCount(String minutiaeCount) {
            this.minutiaeCount = minutiaeCount;
        }

        public String getNfiq() {
            return nfiq;
        }

        public void setNfiq(String nfiq) {
            this.nfiq = nfiq;
        }

        public String getImeiNumber() {
            return imeiNumber;
        }

        public void setImeiNumber(String imeiNumber) {
            this.imeiNumber = imeiNumber;
        }

        public String getReserved() {
            return reserved;
        }

        public void setReserved(String reserved) {
            this.reserved = reserved;
        }
    }
}
