package com.inov8.integration.channel.T24Api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
@JsonIgnoreProperties(ignoreUnknown = true)
public class IbftTitleFetchResponse extends Response {

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
        i8SBSwitchControllerResponseVO.setResponseCode(this.getISOMessage().ResponseCode_039);
        i8SBSwitchControllerResponseVO.setAccountTitle(this.getISOMessage().getToAccountTitle());
//        i8SBSwitchControllerResponseVO.setAccountTitle("Title-1");
        i8SBSwitchControllerResponseVO.setBranchName(this.getISOMessage().getBranchName());
        i8SBSwitchControllerResponseVO.setAccountId1(this.getISOMessage().getAccountIdentification1_102());
        i8SBSwitchControllerResponseVO.setTransactionDate(this.getISOMessage().getTransmissionDatetime_007());
        i8SBSwitchControllerResponseVO.setTimeLocalTransaction(this.getISOMessage().getTimeLocalTransaction_012());
        i8SBSwitchControllerResponseVO.setDateLocalTransaction(this.getISOMessage().getDateLocalTransaction_013());
        i8SBSwitchControllerResponseVO.setBranchName(this.getISOMessage().getBranchName());
        i8SBSwitchControllerResponseVO.setAccountTitle(this.getISOMessage().reservedPrivate_120.trim());
        return i8SBSwitchControllerResponseVO;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ISOMessage {
        @JsonProperty("MTI")
        private String MTI;
        @JsonProperty("PrimaryAccountNumber_002")
        private String PrimaryAccountNumber_002;
        @JsonProperty("ProcessingCode_003")
        private String ProcessingCode_003;
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
        @JsonProperty("AcquiringInstitutionIdentificationCode_032")
        private String AcquiringInstitutionIdentificationCode_032;
        @JsonProperty("UserId")
        private String UserId;
        @JsonProperty("CNIC")
        private String CNIC;
        @JsonProperty("ResponseCode_039")
        private String ResponseCode_039;
        @JsonProperty("AccountIdentification1_102")
        private String AccountIdentification1_102;
        @JsonProperty("ToAccountTitle")
        private String ToAccountTitle;
        @JsonProperty("BranchName")
        private String BranchName;
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
            return PrimaryAccountNumber_002;
        }

        public void setPrimaryAccountNumber_002(String PrimaryAccountNumber_002) {
            this.PrimaryAccountNumber_002 = PrimaryAccountNumber_002;
        }

        public String getProcessingCode_003() {
            return ProcessingCode_003;
        }

        public void setProcessingCode_003(String ProcessingCode_003) {
            this.ProcessingCode_003 = ProcessingCode_003;
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

        public String getMerchantType_018() {
            return MerchantType_018;
        }

        // Setter Methods

        public void setMerchantType_018(String MerchantType_018) {
            this.MerchantType_018 = MerchantType_018;
        }

        public String getAcquiringInstitutionIdentificationCode_032() {
            return AcquiringInstitutionIdentificationCode_032;
        }

        public void setAcquiringInstitutionIdentificationCode_032(String AcquiringInstitutionIdentificationCode_032) {
            this.AcquiringInstitutionIdentificationCode_032 = AcquiringInstitutionIdentificationCode_032;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getCNIC() {
            return CNIC;
        }

        public void setCNIC(String CNIC) {
            this.CNIC = CNIC;
        }

        public String getResponseCode_039() {
            return ResponseCode_039;
        }

        public void setResponseCode_039(String ResponseCode_039) {
            this.ResponseCode_039 = ResponseCode_039;
        }

        public String getAccountIdentification1_102() {
            return AccountIdentification1_102;
        }

        public void setAccountIdentification1_102(String AccountIdentification1_102) {
            this.AccountIdentification1_102 = AccountIdentification1_102;
        }

        public String getToAccountTitle() {
            return ToAccountTitle;
        }

        public void setToAccountTitle(String ToAccountTitle) {
            this.ToAccountTitle = ToAccountTitle;
        }

        public String getBranchName() {
            return BranchName;
        }

        public void setBranchName(String BranchName) {
            this.BranchName = BranchName;
        }

        public String getReservedPrivate_120() {
            return reservedPrivate_120;
        }

        public void setReservedPrivate_120(String reservedPrivate_120) {
            this.reservedPrivate_120 = reservedPrivate_120;
        }
    }
}
