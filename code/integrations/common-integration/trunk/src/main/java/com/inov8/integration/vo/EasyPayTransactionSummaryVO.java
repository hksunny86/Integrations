package com.inov8.integration.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EasyPayTransactionSummaryVO implements Serializable{

    private static final long serialVersionUID = 5499800042530465399L;

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("responseMessage")
    private String responseMessage;
    @JsonProperty("summaries")
    private List<EasyPayTransactionDetailVO> easyPayTransactionDetailVOList;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<EasyPayTransactionDetailVO> getEasyPayTransactionDetailVOList() {
        return easyPayTransactionDetailVOList;
    }

    public void setEasyPayTransactionDetailVOList(List<EasyPayTransactionDetailVO> easyPayTransactionDetailVOList) {
        this.easyPayTransactionDetailVOList = easyPayTransactionDetailVOList;
    }
}
