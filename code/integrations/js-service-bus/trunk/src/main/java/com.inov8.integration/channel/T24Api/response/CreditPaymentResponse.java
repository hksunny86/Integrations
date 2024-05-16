package com.inov8.integration.channel.T24Api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditPaymentResponse extends Response {
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
        i8SBSwitchControllerResponseVO.setPrimaryAccountNumber_002(this.getISOMessage().getPrimaryAccountNumber_002());
        i8SBSwitchControllerResponseVO.setTrack1Data_045(this.getISOMessage().getTrack1Data_045());
        i8SBSwitchControllerResponseVO.setPersonalIdentificationNumberData_052(this.getISOMessage().getPersonalIdentificationNumberData_052());
        i8SBSwitchControllerResponseVO.setReplacementAmounts_095(this.getISOMessage().getReplacementAmounts_095());
        i8SBSwitchControllerResponseVO.setTransactionDescription_104(this.getISOMessage().getTransactionDescription_104());
        i8SBSwitchControllerResponseVO.setReservedPrivate_120(this.getISOMessage().getReservedPrivate_120());

        return i8SBSwitchControllerResponseVO;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ISOMessage {
        @JsonProperty("MTI")
        private String MTI;
        @JsonProperty("PrimaryAccountNumber_002")
        private String primaryAccountNumber_002;
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
        @JsonProperty("Track1Data_045")
        private String track1Data_045;
        @JsonProperty("CurrencyCodeTransaction_049")
        private String CurrencyCodeTransaction_049;
        @JsonProperty("CurrencyCodeSettlement_050")
        private String CurrencyCodeSettlement_050;
        @JsonProperty("PersonalIdentificationNumberData_052")
        private String personalIdentificationNumberData_052;
        @JsonProperty("OriginalDataElements_090")
        private String originalDataElements_090;
        @JsonProperty("ReplacementAmounts_095")
        private String replacementAmounts_095;
        @JsonProperty("AccountIdentification1_102")
        private String AccountIdentification1_102;
        @JsonProperty("AccountIdentification2_103")
        private String AccountIdentification2_103;
        @JsonProperty("TransactionDescription_104")
        private String TransactionDescription_104;
        @JsonProperty("ReservedPrivate_120")
        private String reservedPrivate_120;

        // Getter Methods


        public String getMTI() {
            return MTI;
        }

        public void setMTI(String MTI) {
            this.MTI = MTI;
        }

        public String getPrimaryAccountNumber_002() {
            return primaryAccountNumber_002;
        }

        public void setPrimaryAccountNumber_002(String primaryAccountNumber_002) {
            this.primaryAccountNumber_002 = primaryAccountNumber_002;
        }

        public String getProcessingCode_003() {
            return ProcessingCode_003;
        }

        public void setProcessingCode_003(String processingCode_003) {
            ProcessingCode_003 = processingCode_003;
        }

        public String getAmountTransaction_004() {
            return AmountTransaction_004;
        }

        public void setAmountTransaction_004(String amountTransaction_004) {
            AmountTransaction_004 = amountTransaction_004;
        }

        public String getTransmissionDatetime_007() {
            return TransmissionDatetime_007;
        }

        public void setTransmissionDatetime_007(String transmissionDatetime_007) {
            TransmissionDatetime_007 = transmissionDatetime_007;
        }

        public String getSystemsTraceAuditNumber_011() {
            return SystemsTraceAuditNumber_011;
        }

        public void setSystemsTraceAuditNumber_011(String systemsTraceAuditNumber_011) {
            SystemsTraceAuditNumber_011 = systemsTraceAuditNumber_011;
        }

        public String getTimeLocalTransaction_012() {
            return TimeLocalTransaction_012;
        }

        public void setTimeLocalTransaction_012(String timeLocalTransaction_012) {
            TimeLocalTransaction_012 = timeLocalTransaction_012;
        }

        public String getDateLocalTransaction_013() {
            return DateLocalTransaction_013;
        }

        public void setDateLocalTransaction_013(String dateLocalTransaction_013) {
            DateLocalTransaction_013 = dateLocalTransaction_013;
        }

        public String getMerchantType_018() {
            return MerchantType_018;
        }

        public void setMerchantType_018(String merchantType_018) {
            MerchantType_018 = merchantType_018;
        }

        public String getResponseCode_039() {
            return ResponseCode_039;
        }

        public void setResponseCode_039(String responseCode_039) {
            ResponseCode_039 = responseCode_039;
        }

        public String getAdditionalResponseData_044() {
            return AdditionalResponseData_044;
        }

        public void setAdditionalResponseData_044(String additionalResponseData_044) {
            AdditionalResponseData_044 = additionalResponseData_044;
        }

        public String getTrack1Data_045() {
            return track1Data_045;
        }

        public void setTrack1Data_045(String track1Data_045) {
            this.track1Data_045 = track1Data_045;
        }

        public String getCurrencyCodeTransaction_049() {
            return CurrencyCodeTransaction_049;
        }

        public void setCurrencyCodeTransaction_049(String currencyCodeTransaction_049) {
            CurrencyCodeTransaction_049 = currencyCodeTransaction_049;
        }

        public String getCurrencyCodeSettlement_050() {
            return CurrencyCodeSettlement_050;
        }

        public void setCurrencyCodeSettlement_050(String currencyCodeSettlement_050) {
            CurrencyCodeSettlement_050 = currencyCodeSettlement_050;
        }

        public String getPersonalIdentificationNumberData_052() {
            return personalIdentificationNumberData_052;
        }

        public void setPersonalIdentificationNumberData_052(String personalIdentificationNumberData_052) {
            this.personalIdentificationNumberData_052 = personalIdentificationNumberData_052;
        }

        public String getOriginalDataElements_090() {
            return originalDataElements_090;
        }

        public void setOriginalDataElements_090(String originalDataElements_090) {
            this.originalDataElements_090 = originalDataElements_090;
        }

        public String getReplacementAmounts_095() {
            return replacementAmounts_095;
        }

        public void setReplacementAmounts_095(String replacementAmounts_095) {
            this.replacementAmounts_095 = replacementAmounts_095;
        }

        public String getAccountIdentification1_102() {
            return AccountIdentification1_102;
        }

        public void setAccountIdentification1_102(String accountIdentification1_102) {
            AccountIdentification1_102 = accountIdentification1_102;
        }

        public String getAccountIdentification2_103() {
            return AccountIdentification2_103;
        }

        public void setAccountIdentification2_103(String accountIdentification2_103) {
            AccountIdentification2_103 = accountIdentification2_103;
        }

        public String getTransactionDescription_104() {
            return TransactionDescription_104;
        }

        public void setTransactionDescription_104(String transactionDescription_104) {
            TransactionDescription_104 = transactionDescription_104;
        }

        public String getReservedPrivate_120() {
            return reservedPrivate_120;
        }

        public void setReservedPrivate_120(String reservedPrivate_120) {
            this.reservedPrivate_120 = reservedPrivate_120;
        }
    }
}