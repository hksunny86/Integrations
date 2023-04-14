package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.middleware.enums.ResponseCodeEnum;
import com.inov8.integration.webservice.vo.WebServiceVO;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "Reserved1",
        "Reserved2",
        "Reserved3",
        "Reserved4",
        "Reserved5",
        "HashData"
})
public class MerchantAccountUpgradeResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("Reserved3")
    private String reserved3;
    @JsonProperty("Reserved4")
    private String reserved4;
    @JsonProperty("Reserved5")
    private String reserved5;
    @JsonProperty("HashData")
    private String hashData;

    @JsonProperty("Rrn")
    public String getRrn() {
        return rrn;
    }

    @JsonProperty("Rrn")
    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    @JsonProperty("ResponseCode")
    public String getResponseCode() {
        return responseCode;
    }

    @JsonProperty("ResponseCode")
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty("ResponseDescription")
    public String getResponseDescription() {
        return responseDescription;
    }

    @JsonProperty("ResponseDescription")
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @JsonProperty("ResponseDateTime")
    public String getResponseDateTime() {
        return responseDateTime;
    }

    @JsonProperty("ResponseDateTime")
    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    @JsonProperty("Reserved1")
    public String getReserved1() {
        return reserved1;
    }

    @JsonProperty("Reserved1")
    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    @JsonProperty("Reserved2")
    public String getReserved2() {
        return reserved2;
    }

    @JsonProperty("Reserved2")
    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    @JsonProperty("Reserved3")
    public String getReserved3() {
        return reserved3;
    }

    @JsonProperty("Reserved3")
    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    @JsonProperty("Reserved4")
    public String getReserved4() {
        return reserved4;
    }

    @JsonProperty("Reserved4")
    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    @JsonProperty("Reserved5")
    public String getReserved5() {
        return reserved5;
    }

    @JsonProperty("Reserved5")
    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    @JsonProperty("HashData")
    public String getHashData() {
        return hashData;
    }

    @JsonProperty("HashData")
    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public WebServiceVO merchantAccountUpgradeResponse(WebServiceVO messageVO) {
        WebServiceVO webServiceVO = new WebServiceVO();

        webServiceVO.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        webServiceVO.setResponseCode(ResponseCodeEnum.PROCESSED_OK.getValue());
        webServiceVO.setResponseCodeDescription("Success");
        webServiceVO.setDateTime(messageVO.getDateTime());
        webServiceVO.setReserved1(messageVO.getReserved1());
        webServiceVO.setReserved2(messageVO.getReserved2());
        webServiceVO.setReserved3(messageVO.getReserved3());
        webServiceVO.setReserved4(messageVO.getReserved4());
        webServiceVO.setReserved5(messageVO.getReserved5());

        return webServiceVO;
    }
}