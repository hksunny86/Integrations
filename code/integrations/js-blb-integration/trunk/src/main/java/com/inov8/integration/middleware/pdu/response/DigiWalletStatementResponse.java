package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.webservice.digiStatmentVO.DigiWalletStatementVo;
import com.inov8.integration.webservice.vo.CustomerValidationVO;
import com.inov8.integration.webservice.vo.EndDayStatementVo;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "DigiWalletStatement",
        "ClosingBalanceStatement",
        "HashData",
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigiWalletStatementResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("DigiWalletStatement")
    private List<DigiWalletStatementVo> digiWalletStatementVoList ;
    @JsonProperty("ClosingBalanceStatement")
    private List<EndDayStatementVo> endDayStatementVoList ;
    @JsonProperty("HashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public List<DigiWalletStatementVo> getDigiWalletStatementVoList() {
        return digiWalletStatementVoList;
    }

    public void setDigiWalletStatementVoList(List<DigiWalletStatementVo> digiWalletStatementVoList) {
        this.digiWalletStatementVoList = digiWalletStatementVoList;
    }

    public List<EndDayStatementVo> getEndDayStatementVoList() {
        return endDayStatementVoList;
    }

    public void setEndDayStatementVoList(List<EndDayStatementVo> endDayStatementVoList) {
        this.endDayStatementVoList = endDayStatementVoList;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}