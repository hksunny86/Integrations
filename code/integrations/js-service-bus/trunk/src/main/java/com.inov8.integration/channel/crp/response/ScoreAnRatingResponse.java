package com.inov8.integration.channel.crp.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.crpVO.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoreAnRatingResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(ScoreAnRatingResponse.class.getSimpleName());

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("channel")
    private String channel;
    @JsonProperty("terminal")
    private String terminal;
    @JsonProperty("transactionDate")
    private String transactionDate;
    @JsonProperty("reterivalReferenceNumber")
    private String reterivalReferenceNumber;
    @JsonProperty("payLoad")
    private Payload payload;
    @JsonProperty("errors")
    private String errors;
    @JsonProperty("checkSum")
    private String checkSum;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReterivalReferenceNumber() {
        return reterivalReferenceNumber;
    }

    public void setReterivalReferenceNumber(String reterivalReferenceNumber) {
        this.reterivalReferenceNumber = reterivalReferenceNumber;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode() != null && this.getResponseCode().equals("170000")) {
            if (this.getPayload() != null) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
                i8SBSwitchControllerResponseVO.setMessage(this.getMessage());
                i8SBSwitchControllerResponseVO.setPayload(this.getPayload());
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
                i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
            }
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
        }

        return i8SBSwitchControllerResponseVO;
    }
}