package com.inov8.integration.channel.merchant.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "staticQrResponses"
})
public class StaticQrResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(StaticQrResponse.class.getSimpleName());

    private List<StaticQrResponse__1> staticQrResponses;
    private String responseCode;
    private String description;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StaticQrResponse__1> getStaticQrResponses() {
        return staticQrResponses;
    }

    public void setStaticQrResponses(List<StaticQrResponse__1> staticQrResponses) {
        this.staticQrResponses = staticQrResponses;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("200")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        }
        if (this.getStaticQrResponses() != null) {
            List<StaticQrResponse__1> staticResponses = this.getStaticQrResponses();
            for (StaticQrResponse__1 staticQrResponse__1 : staticResponses) {
                i8SBSwitchControllerResponseVO.setQr(staticQrResponse__1.getQr());
                i8SBSwitchControllerResponseVO.setUuid(staticQrResponse__1.getUuid());
                i8SBSwitchControllerResponseVO.setUetr(staticQrResponse__1.getUetr());
                i8SBSwitchControllerResponseVO.setAmount(staticQrResponse__1.getAmount());
                i8SBSwitchControllerResponseVO.setExpiryDate(staticQrResponse__1.getExpiry());
                i8SBSwitchControllerResponseVO.setCreationDate(staticQrResponse__1.getCreatedDate());
            }
        }

        return i8SBSwitchControllerResponseVO;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "QR",
        "UUID",
        "UETR",
        "Amount",
        "Expiry",
        "CreatedDate"
})
class StaticQrResponse__1 {

    @JsonProperty("QR")
    private String qr;
    @JsonProperty("UUID")
    private String uuid;
    @JsonProperty("UETR")
    private String uetr;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("Expiry")
    private String expiry;
    @JsonProperty("CreatedDate")
    private String createdDate;

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUetr() {
        return uetr;
    }

    public void setUetr(String uetr) {
        this.uetr = uetr;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}