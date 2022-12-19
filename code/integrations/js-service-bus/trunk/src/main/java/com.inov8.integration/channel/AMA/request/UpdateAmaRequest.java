package com.inov8.integration.channel.AMA.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;

import javax.xml.bind.annotation.XmlRootElement;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateAmaRequest extends Request {
    @JsonProperty("processingCode")
    private String processingCode;
    @JsonProperty("traceNo")
    private String traceNo;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("merchantType")
    private String merchantType;
    @JsonProperty("bankIMD")
    private String bankIMD;
    @JsonProperty("accountNumber")
    private String accountNumber;
    @JsonProperty("accountTitle")
    private String accountTitle;
    @JsonProperty("accountType")
    private String accountType;
    @JsonProperty("cnic")
    private String cnic;
    @JsonProperty("isClose")
    private String isClose;
    @JsonProperty("isEnabled")
    private String isEnabled;
    @JsonProperty("isPINSet")
    private String isPinSet;
    @JsonProperty("isRegistered")
    private String isRegistered;
    @JsonProperty("msisdn")
    private String msisdn;
    @JsonProperty("reserved1")
    private String reserved1;
    @JsonProperty("reserved2")
    private String reserved2;
    @JsonProperty("reserved3")
    private String reserved3;
    @JsonProperty("reserved4")
    private String reserved4;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setProcessingCode(PropertyReader.getProperty("update.ama.processing"));
        this.setTraceNo(i8SBSwitchControllerRequestVO.getTraceNo());
        this.setAccountNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setMerchantType(PropertyReader.getProperty("update.ama.merchanctType"));
        this.setBankIMD(PropertyReader.getProperty("update.ama.bank.imd"));
        this.setDateTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setAccountTitle(i8SBSwitchControllerRequestVO.getAccountTitle());
        this.setAccountType(i8SBSwitchControllerRequestVO.getAccountType());
        this.setIsClose(i8SBSwitchControllerRequestVO.getIsClose());
        this.setIsPinSet(i8SBSwitchControllerRequestVO.getIsPinSet());
        this.setIsEnabled(i8SBSwitchControllerRequestVO.getIsEnabled());
        this.setIsRegistered(i8SBSwitchControllerRequestVO.getIsRegistered());
        this.setMsisdn(i8SBSwitchControllerRequestVO.getMsisdn());
        this.setReserved1(i8SBSwitchControllerRequestVO.getReserved1());
        this.setReserved2(i8SBSwitchControllerRequestVO.getReserved2());
        this.setReserved3(i8SBSwitchControllerRequestVO.getReserved3());
        this.setReserved4(i8SBSwitchControllerRequestVO.getReserved4());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getIsClose() {
        return isClose;
    }

    public void setIsClose(String isClose) {
        this.isClose = isClose;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getIsPinSet() {
        return isPinSet;
    }

    public void setIsPinSet(String isPinSet) {
        this.isPinSet = isPinSet;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
