package com.inov8.integration.channel.esbT24.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import static com.inov8.integration.enums.DateFormatEnum.IBFT_DATE_TIME;
import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE_TIME;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest extends Request {

    private String MTI;
    private String ProcessingCode_003;
    private String AmountTransaction_004;
    private String TransmissionDatetime_007;
    private String SystemsTraceAuditNumber_011;
    private String TimeLocalTransaction_012;
    private String DateLocalTransaction_013;
    private String MerchantType_018;
    private String CurrencyCodeTransaction_049;
    private String CurrencyCodeSettlement_050;
    private String AccountIdentification1_102;
    private String AccountIdentification2_103;
    private String TransactionDescription_104;

    public String getMTI() {
        return MTI;
    }

    public void setMTI(String MTI) {
        this.MTI = MTI;
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

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setMTI(PropertyReader.getProperty("esbt24.mti"));
        this.setProcessingCode_003(PropertyReader.getProperty("esbt24.processingCode"));
        this.setAmountTransaction_004(StringUtils.leftPad(parseI8Amount(i8SBSwitchControllerRequestVO.getTransactionAmount()), 12, '0'));
        this.setTransmissionDatetime_007(DateUtil.formatCurrentDate(TRANSACTION_DATE_TIME.getValue()));
        this.setSystemsTraceAuditNumber_011(i8SBSwitchControllerRequestVO.getSTAN());
        this.setTimeLocalTransaction_012(i8SBSwitchControllerRequestVO.getTimeLocalTransaction());
        this.setDateLocalTransaction_013(i8SBSwitchControllerRequestVO.getDateLocalTransaction());
        this.setMerchantType_018(PropertyReader.getProperty("esbt24.merchantType"));
        this.setCurrencyCodeTransaction_049(PropertyReader.getProperty("esbt24.currencyCodeTransaction"));
        this.setCurrencyCodeSettlement_050(PropertyReader.getProperty("esbt24.currencyCodeSettlement"));
        this.setAccountIdentification1_102(i8SBSwitchControllerRequestVO.getAccountId1());
        if (i8SBSwitchControllerRequestVO.getReserved1() != null && i8SBSwitchControllerRequestVO.getReserved1().equals("1")) {
            this.setAccountIdentification2_103("PKR" + i8SBSwitchControllerRequestVO.getAccountId2());
            this.setTransactionDescription_104(i8SBSwitchControllerRequestVO.getReserved2() + DateUtil.formatCurrentDate(IBFT_DATE_TIME.getValue()) + i8SBSwitchControllerRequestVO.getSTAN());
        } else {
            this.setAccountIdentification2_103(i8SBSwitchControllerRequestVO.getAccountId2());
            this.setTransactionDescription_104(i8SBSwitchControllerRequestVO.getReserved2() + DateUtil.formatCurrentDate(IBFT_DATE_TIME.getValue()) + i8SBSwitchControllerRequestVO.getSTAN());

        }

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }

    public static String parseI8Amount(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (StringUtils.isNotEmpty(value)) {
                // CHECK IF AMOUNT HAS ALREADY DECIMAL POINT
                if (value.contains(".")) {
                    // SPILIT AMOUNT INTO TWO PARTS, ONE BEFORE DECIMAL & 2ND
                    // AFTER DECIMAL
                    String args[] = StringUtils.split(value, ".");
                    stringBuilder.append(args[0]);

                    // CHECK IF ONE DECIMAL PONT VALUE LIKE 200.5
                    if (args[1].length() == 1) {
                        stringBuilder.append(args[1]);
                        stringBuilder.append("0");

                        // CHECK IF ALREADY TWO DECIMAL PONT VALUE LIKE 200.50
                    } else if (args[1].length() == 2) {
                        stringBuilder.append(args[1]);
                    }

                    // IF THERE IS NO DECIMAL POINT THEN APPEND TWO 00 TO THE
                    // VALUE
                } else {
                    stringBuilder.append(value);
                    stringBuilder.append("00");
                }
            } else {
                throw new RuntimeException("Invalid Transaction Amount Provided. Please verify ");
            }
        } catch (Exception e) {
        }
        return stringBuilder.toString();
    }

}


