package com.inov8.integration.channel.T24Api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IbftReversalRequest extends Request {
    @JsonProperty("MTI")
    private String mti = PropertyReader.getProperty("MTI");
    ;
    @JsonProperty("PrimaryAccountNumber_002")
    private String primaryAccountNumber_002;
    @JsonProperty("ProcessingCode_003")
    private String processingCode_003;
    private String amountTransaction_004;
    @JsonProperty("TransactionDatetime_007")
    private String transmissionDateTime_007;
    @JsonProperty("SystemTraceAuditNumber_011")
    private String systemTraceAuditNumber_011;
    @JsonProperty("TimeLocalTransaction_012")
    private String timeLocalTransaction_012;
    @JsonProperty("DateLocalTransaction_013")
    private String dateLocalTransaction_013;
    @JsonProperty("MerchantType_018")
    private String merchantType_018;
    @JsonProperty("CurrencyCodeTransaction_049")
    private String currencyCodeTransaction_049;
    @JsonProperty("CurrencyCodeSettlement_50")
    private String currencyCodeSettlement_050;
    private String orignalDataElement_090;
    @JsonProperty("AccountIdentification1_102")
    private String accountIdentification1_102;
    @JsonProperty("AccountIdentification2_103")
    private String accountIdentification2_103;
    private String reservedPrivate_127;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMti(mti);
        this.setPrimaryAccountNumber_002(i8SBSwitchControllerRequestVO.getPAN());
        this.setDateLocalTransaction_013(i8SBSwitchControllerRequestVO.getDateLocalTransaction());
        this.setTimeLocalTransaction_012(i8SBSwitchControllerRequestVO.getTimeLocalTransaction());
        this.setTransmissionDateTime_007(i8SBSwitchControllerRequestVO.getTransactionDateTime());
        this.setSystemTraceAuditNumber_011(i8SBSwitchControllerRequestVO.getSTAN());
        this.setAccountIdentification1_102(i8SBSwitchControllerRequestVO.getAccountId1());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }


    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getPrimaryAccountNumber_002() {
        return primaryAccountNumber_002;
    }

    public void setPrimaryAccountNumber_002(String primaryAccountNumber_002) {
        this.primaryAccountNumber_002 = primaryAccountNumber_002;
    }

    public String getProcessingCode_003() {
        return processingCode_003;
    }

    public void setProcessingCode_003(String processingCode_003) {
        this.processingCode_003 = processingCode_003;
    }

    public String getAmountTransaction_004() {
        return amountTransaction_004;
    }

    public void setAmountTransaction_004(String amountTransaction_004) {
        this.amountTransaction_004 = amountTransaction_004;
    }

    public String getTransmissionDateTime_007() {
        return transmissionDateTime_007;
    }

    public void setTransmissionDateTime_007(String transmissionDateTime_007) {
        this.transmissionDateTime_007 = transmissionDateTime_007;
    }

    public String getSystemTraceAuditNumber_011() {
        return systemTraceAuditNumber_011;
    }

    public void setSystemTraceAuditNumber_011(String systemTraceAuditNumber_011) {
        this.systemTraceAuditNumber_011 = systemTraceAuditNumber_011;
    }

    public String getTimeLocalTransaction_012() {
        return timeLocalTransaction_012;
    }

    public void setTimeLocalTransaction_012(String timeLocalTransaction_012) {
        this.timeLocalTransaction_012 = timeLocalTransaction_012;
    }

    public String getDateLocalTransaction_013() {
        return dateLocalTransaction_013;
    }

    public void setDateLocalTransaction_013(String dateLocalTransaction_013) {
        this.dateLocalTransaction_013 = dateLocalTransaction_013;
    }

    public String getMerchantType_018() {
        return merchantType_018;
    }

    public void setMerchantType_018(String merchantType_018) {
        this.merchantType_018 = merchantType_018;
    }

    public String getCurrencyCodeTransaction_049() {
        return currencyCodeTransaction_049;
    }

    public void setCurrencyCodeTransaction_049(String currencyCodeTransaction_049) {
        this.currencyCodeTransaction_049 = currencyCodeTransaction_049;
    }

    public String getCurrencyCodeSettlement_050() {
        return currencyCodeSettlement_050;
    }

    public void setCurrencyCodeSettlement_050(String currencyCodeSettlement_050) {
        this.currencyCodeSettlement_050 = currencyCodeSettlement_050;
    }

    public String getOrignalDataElement_090() {
        return orignalDataElement_090;
    }

    public void setOrignalDataElement_090(String orignalDataElement_090) {
        this.orignalDataElement_090 = orignalDataElement_090;
    }

    public String getAccountIdentification1_102() {
        return accountIdentification1_102;
    }

    public void setAccountIdentification1_102(String accountIdentification1_102) {
        this.accountIdentification1_102 = accountIdentification1_102;
    }

    public String getAccountIdentification2_103() {
        return accountIdentification2_103;
    }

    public void setAccountIdentification2_103(String accountIdentification2_103) {
        this.accountIdentification2_103 = accountIdentification2_103;
    }

    public String getReservedPrivate_127() {
        return reservedPrivate_127;
    }

    public void setReservedPrivate_127(String reservedPrivate_127) {
        this.reservedPrivate_127 = reservedPrivate_127;
    }
}
