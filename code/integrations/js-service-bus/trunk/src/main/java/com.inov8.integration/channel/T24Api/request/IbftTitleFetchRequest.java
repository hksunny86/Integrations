package com.inov8.integration.channel.T24Api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.CommonUtils;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;
import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE_TIME;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IbftTitleFetchRequest extends Request {

    @JsonProperty("MTI")
    private String mti = PropertyReader.getProperty("MTI");
    @JsonProperty("PrimaryAccountNumber_002")
    private String primaryAccountNumber_002;
    @JsonProperty("ProcessingCode_003")
    private String processingCode_003;
    @JsonProperty("TransactionDatetime_007")
    private String transactionDatetime_007;
    @JsonProperty("SystemTraceAuditNumber_011")
    private String systemTraceAuditNumber_011;
    @JsonProperty("TimeLocalTransaction_012")
    private String timeLocalTransaction_012;
    @JsonProperty("DateLocalTransaction_013")
    private String dateLocalTransaction_013;
    @JsonProperty("MerchantType_018")
    private String merchantType_018 = PropertyReader.getProperty("IBFT_TITLEFETCH_MERCHANT_TYPE");
    @JsonProperty("CurrencyCodeTransaction_049")
    private String currencyCodeTransaction_049=PropertyReader.getProperty("PKR_CURRENCY");
    @JsonProperty("CurrencyCodeSettlement_50")
    private String currencyCodeSettlement_50=PropertyReader.getProperty("PKR_CURRENCY");
    @JsonProperty("AccountIdentification1_102")
    private String accountIdentification1_102;
    @JsonProperty("AccountIdentification1_103")
    private String accountIdentification2_103;

    @JsonProperty("ReservedPrivate_120")
    private String reservedPrivate_120;


    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMti(mti);

        this.setPrimaryAccountNumber_002(StringUtils.rightPad(i8SBSwitchControllerRequestVO.getAccountId2(),16,"0"));
        this.setProcessingCode_003("650000");
        this.setTransactionDatetime_007(DateUtil.formatCurrentDate(TRANSACTION_DATE_TIME.getValue()));
        this.setSystemTraceAuditNumber_011(i8SBSwitchControllerRequestVO.getSTAN());
        this.setTimeLocalTransaction_012(i8SBSwitchControllerRequestVO.getTimeLocalTransaction());
        this.setDateLocalTransaction_013(i8SBSwitchControllerRequestVO.getDateLocalTransaction());
        this.setMerchantType_018(merchantType_018);
        this.setCurrencyCodeTransaction_049(currencyCodeTransaction_049);
        this.setCurrencyCodeSettlement_50(currencyCodeSettlement_50);
        this.setAccountIdentification1_102(i8SBSwitchControllerRequestVO.getAccountId1());
        this.setAccountIdentification2_103(i8SBSwitchControllerRequestVO.getAccountId2());
       this.setReservedPrivate_120("");

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

    public String getTransactionDatetime_007() {
        return transactionDatetime_007;
    }

    public void setTransactionDatetime_007(String transactionDatetime_007) {
        this.transactionDatetime_007 = transactionDatetime_007;
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

    public String getCurrencyCodeSettlement_50() {
        return currencyCodeSettlement_50;
    }

    public void setCurrencyCodeSettlement_50(String currencyCodeSettlement_50) {
        this.currencyCodeSettlement_50 = currencyCodeSettlement_50;
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
    public String getReservedPrivate_120() {
        return reservedPrivate_120;
    }

    public void setReservedPrivate_120(String reservedPrivate_120) {
        this.reservedPrivate_120 = reservedPrivate_120;
    }
}
