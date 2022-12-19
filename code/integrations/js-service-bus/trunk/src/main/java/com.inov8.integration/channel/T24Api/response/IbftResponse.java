package com.inov8.integration.channel.T24Api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

public class IbftResponse extends Response {
    @JsonProperty("ISOMessage")
    ISOMessage ISOMessageObject;


    // Getter Methods

    public ISOMessage getISOMessage() {
        return ISOMessageObject;
    }

    // Setter Methods

    public void setISOMessage(ISOMessage ISOMessageObject) {
        this.ISOMessageObject = ISOMessageObject;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setAmount(this.getISOMessage().getAmountTransaction_004());
        i8SBSwitchControllerResponseVO.setAccountId1(this.getISOMessage().getAccountIdentification1_102());
        i8SBSwitchControllerResponseVO.setAccountId2(this.getISOMessage().getAccountIdentification2_103());
        i8SBSwitchControllerResponseVO.setSTAN(this.getISOMessage().getSystemsTraceAuditNumber_011());
        i8SBSwitchControllerResponseVO.setResponseCode(this.getISOMessage().getResponseCode_039());
        i8SBSwitchControllerResponseVO.setTimeLocalTransaction(this.getISOMessage().getTimeLocalTransaction_012());
        i8SBSwitchControllerResponseVO.setDateLocalTransaction(this.getISOMessage().getDateLocalTransaction_013());
        i8SBSwitchControllerResponseVO.setMerchantType(this.getISOMessage().getMerchantType_018());

        return i8SBSwitchControllerResponseVO;
    }

    public class ISOMessage {
        @JsonProperty("MTI")
        private String MTI;
        @JsonProperty("ProcessingCode_003")
        private String ProcessingCode_003;
        @JsonProperty("AmountTransaction_004")
        private String AmountTransaction_004;
        @JsonProperty("TransmissionDatetime_007")
        private String TransmissionDatetime_007;
        @JsonProperty("SystemsTraceAuditNumber_011")
        private String SystemsTraceAuditNumber_011;
        @JsonProperty("TimeLocalTransaction_012")
        private String TimeLocalTransaction_012;
        @JsonProperty("DateLocalTransaction_013")
        private String DateLocalTransaction_013;
        @JsonProperty("MerchantType_018")
        private String MerchantType_018;
        @JsonProperty("ResponseCode_039")
        private String ResponseCode_039;
        @JsonProperty("AdditionalResponseData_044")
        private String AdditionalResponseData_044;
        @JsonProperty("CurrencyCodeTransaction_049")
        private String CurrencyCodeTransaction_049;
        @JsonProperty("CurrencyCodeSettlement_050")
        private String CurrencyCodeSettlement_050;
        @JsonProperty("AccountIdentification1_102")
        private String AccountIdentification1_102;
        @JsonProperty("AccountIdentification2_103")
        private String AccountIdentification2_103;


        // Getter Methods

        public String getMTI() {
            return MTI;
        }

        public void setMTI(String MTI) {
            this.MTI = MTI;
        }

        public String getProcessingCode_003() {
            return ProcessingCode_003;
        }

        public void setProcessingCode_003(String ProcessingCode_003) {
            this.ProcessingCode_003 = ProcessingCode_003;
        }

        public String getAmountTransaction_004() {
            return AmountTransaction_004;
        }

        public void setAmountTransaction_004(String AmountTransaction_004) {
            this.AmountTransaction_004 = AmountTransaction_004;
        }

        public String getTransmissionDatetime_007() {
            return TransmissionDatetime_007;
        }

        public void setTransmissionDatetime_007(String TransmissionDatetime_007) {
            this.TransmissionDatetime_007 = TransmissionDatetime_007;
        }

        public String getSystemsTraceAuditNumber_011() {
            return SystemsTraceAuditNumber_011;
        }

        public void setSystemsTraceAuditNumber_011(String SystemsTraceAuditNumber_011) {
            this.SystemsTraceAuditNumber_011 = SystemsTraceAuditNumber_011;
        }

        public String getTimeLocalTransaction_012() {
            return TimeLocalTransaction_012;
        }

        public void setTimeLocalTransaction_012(String TimeLocalTransaction_012) {
            this.TimeLocalTransaction_012 = TimeLocalTransaction_012;
        }

        public String getDateLocalTransaction_013() {
            return DateLocalTransaction_013;
        }

        public void setDateLocalTransaction_013(String DateLocalTransaction_013) {
            this.DateLocalTransaction_013 = DateLocalTransaction_013;
        }

        // Setter Methods

        public String getMerchantType_018() {
            return MerchantType_018;
        }

        public void setMerchantType_018(String MerchantType_018) {
            this.MerchantType_018 = MerchantType_018;
        }

        public String getResponseCode_039() {
            return ResponseCode_039;
        }

        public void setResponseCode_039(String ResponseCode_039) {
            this.ResponseCode_039 = ResponseCode_039;
        }

        public String getAdditionalResponseData_044() {
            return AdditionalResponseData_044;
        }

        public void setAdditionalResponseData_044(String AdditionalResponseData_044) {
            this.AdditionalResponseData_044 = AdditionalResponseData_044;
        }

        public String getCurrencyCodeTransaction_049() {
            return CurrencyCodeTransaction_049;
        }

        public void setCurrencyCodeTransaction_049(String CurrencyCodeTransaction_049) {
            this.CurrencyCodeTransaction_049 = CurrencyCodeTransaction_049;
        }

        public String getCurrencyCodeSettlement_050() {
            return CurrencyCodeSettlement_050;
        }

        public void setCurrencyCodeSettlement_050(String CurrencyCodeSettlement_050) {
            this.CurrencyCodeSettlement_050 = CurrencyCodeSettlement_050;
        }

        public String getAccountIdentification1_102() {
            return AccountIdentification1_102;
        }

        public void setAccountIdentification1_102(String AccountIdentification1_102) {
            this.AccountIdentification1_102 = AccountIdentification1_102;
        }

        public String getAccountIdentification2_103() {
            return AccountIdentification2_103;
        }

        public void setAccountIdentification2_103(String AccountIdentification2_103) {
            this.AccountIdentification2_103 = AccountIdentification2_103;
        }
    }
}
