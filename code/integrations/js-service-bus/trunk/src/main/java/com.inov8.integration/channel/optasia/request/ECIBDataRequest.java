package com.inov8.integration.channel.optasia.request;

;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "customerId",
        "dateTime",
        "rrn",
        "activeAccounts",
        "outstandingAmount",
        "dpd_30",
        "dpd_60",
        "dpd_90",
        "dpd_120",
        "dpd_150",
        "dpd_180",
        "writeOff",
        "hashData"
})
public class ECIBDataRequest extends Request implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;

    @JsonProperty("customerId")
    private String customerId;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("activeAccounts")
    private String activeAccounts;
    @JsonProperty("outstandingAmount")
    private String outstandingAmount;
    @JsonProperty("dpd_30")
    private String dpd30;
    @JsonProperty("dpd_60")
    private String dpd60;
    @JsonProperty("dpd_90")
    private String dpd90;
    @JsonProperty("dpd_120")
    private String dpd120;
    @JsonProperty("dpd_150")
    private String dpd150;
    @JsonProperty("dpd_180")
    private String dpd180;
    @JsonProperty("writeOff")
    private String writeOff;
    @JsonProperty("hashData")
    private String hashData;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getActiveAccounts() {
        return activeAccounts;
    }

    public void setActiveAccounts(String activeAccounts) {
        this.activeAccounts = activeAccounts;
    }

    public String getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getDpd30() {
        return dpd30;
    }

    public void setDpd30(String dpd30) {
        this.dpd30 = dpd30;
    }

    public String getDpd60() {
        return dpd60;
    }

    public void setDpd60(String dpd60) {
        this.dpd60 = dpd60;
    }

    public String getDpd90() {
        return dpd90;
    }

    public void setDpd90(String dpd90) {
        this.dpd90 = dpd90;
    }

    public String getDpd120() {
        return dpd120;
    }

    public void setDpd120(String dpd120) {
        this.dpd120 = dpd120;
    }

    public String getDpd150() {
        return dpd150;
    }

    public void setDpd150(String dpd150) {
        this.dpd150 = dpd150;
    }

    public String getDpd180() {
        return dpd180;
    }

    public void setDpd180(String dpd180) {
        this.dpd180 = dpd180;
    }

    public String getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(String writeOff) {
        this.writeOff = writeOff;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setCustomerId(i8SBSwitchControllerRequestVO.getCustomerId());
        this.setDateTime(i8SBSwitchControllerRequestVO.getDateTime());
        this.setRrn(i8SBSwitchControllerRequestVO.getRRN());
        this.setActiveAccounts(i8SBSwitchControllerRequestVO.getActiveAccounts());
        this.setOutstandingAmount(i8SBSwitchControllerRequestVO.getOutstandingAmount());
        this.setDpd30(i8SBSwitchControllerRequestVO.getDpd30());
        this.setDpd60(i8SBSwitchControllerRequestVO.getDpd60());
        this.setDpd90(i8SBSwitchControllerRequestVO.getDpd90());
        this.setDpd120(i8SBSwitchControllerRequestVO.getDpd120());
        this.setDpd150(i8SBSwitchControllerRequestVO.getDpd150());
        this.setDpd180(i8SBSwitchControllerRequestVO.getDpd180());
        this.setWriteOff(i8SBSwitchControllerRequestVO.getWriteOff());
//        this.setTelcoData(i8SBSwitchControllerRequestVO.getTelcoData());

        StringBuilder stringText = new StringBuilder()
                .append(i8SBSwitchControllerRequestVO.getCustomerId())
                .append(i8SBSwitchControllerRequestVO.getDateTime())
                .append(i8SBSwitchControllerRequestVO.getRRN())
                .append(i8SBSwitchControllerRequestVO.getActiveAccounts())
                .append(i8SBSwitchControllerRequestVO.getOutstandingAmount())
                .append(i8SBSwitchControllerRequestVO.getDpd30())
                .append(i8SBSwitchControllerRequestVO.getDpd60())
                .append(i8SBSwitchControllerRequestVO.getDpd90())
                .append(i8SBSwitchControllerRequestVO.getDpd120())
                .append(i8SBSwitchControllerRequestVO.getDpd150())
                .append(i8SBSwitchControllerRequestVO.getDpd180())
                .append(i8SBSwitchControllerRequestVO.getWriteOff());
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        this.setHashData(sha256hex);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getRrn())) {
            throw new I8SBValidationException("[Failed] Rrn:" + this.getRrn());
        }
        if (StringUtils.isEmpty(this.getCustomerId())) {
            throw new I8SBValidationException("[Failed] Customer Id:" + this.getCustomerId());
        }
        if (StringUtils.isEmpty(this.getDateTime())) {
            throw new I8SBValidationException("[Failed] Date Time:" + this.getDateTime());
        }
        if (StringUtils.isEmpty(this.getActiveAccounts())) {
            throw new I8SBValidationException("[Failed] Active Accounts:" + this.getActiveAccounts());
        }
        if (StringUtils.isEmpty(this.getOutstandingAmount())) {
            throw new I8SBValidationException("[Failed] Outstanding Amount:" + this.getOutstandingAmount());
        }
        if (StringUtils.isEmpty(this.getDpd30())) {
            throw new I8SBValidationException("[Failed] DPD 30 Plus:" + this.getDpd30());
        }
        if (StringUtils.isEmpty(this.getDpd60())) {
            throw new I8SBValidationException("[Failed] DPD 60 Plus:" + this.getDpd60());
        }
        if (StringUtils.isEmpty(this.getDpd120())) {
            throw new I8SBValidationException("[Failed] DPD 120 Plus:" + this.getDpd120());
        }
        if (StringUtils.isEmpty(this.getDpd150())) {
            throw new I8SBValidationException("[Failed] DPD 150 Plus:" + this.getDpd150());
        }
        if (StringUtils.isEmpty(this.getDpd180())) {
            throw new I8SBValidationException("[Failed] DPD 180 Plus:" + this.getDpd180());
        }
        if (StringUtils.isEmpty(this.getWriteOff())) {
            throw new I8SBValidationException("[Failed] Write Off:" + this.getWriteOff());
        }
        return true;
    }
}