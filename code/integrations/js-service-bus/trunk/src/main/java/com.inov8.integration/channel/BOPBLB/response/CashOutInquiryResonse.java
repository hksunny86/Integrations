package com.inov8.integration.channel.BOPBLB.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CashOutInquiryResonse extends Response {
    @JsonProperty("ResponseCode")
    private String ResponseCode;
    @JsonProperty("ResponseDescription")
    private String ResponseDescription;
    @JsonProperty("Bio_Status_Flag")
    private String Bio_Status_Flag;
    @JsonProperty("Amount")
    private String Amount;
    @JsonProperty("Transaction_Fee")
    private String Transaction_Fee;
    @JsonProperty("Total_Amount")
    private String Total_Amount;
    @JsonProperty("RRN")
    private String RRN;
    @JsonProperty("StatusCode")
    private String StatusCode;
    @JsonProperty("Status")
    private String Status;
    @JsonProperty("Transaction_ID")
    private String Transaction_ID;


    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseDescription() {
        return ResponseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        ResponseDescription = responseDescription;
    }

    public String getBio_Status_Flag() {
        return Bio_Status_Flag;
    }

    public void setBio_Status_Flag(String bio_Status_Flag) {
        Bio_Status_Flag = bio_Status_Flag;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getTransaction_Fee() {
        return Transaction_Fee;
    }

    public void setTransaction_Fee(String transaction_Fee) {
        Transaction_Fee = transaction_Fee;
    }

    public String getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        Total_Amount = total_Amount;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String statusCode) {
        StatusCode = statusCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTransaction_ID() {
        return Transaction_ID;
    }

    public void setTransaction_ID(String transaction_ID) {
        Transaction_ID = transaction_ID;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getBio_Status_Flag().equals("1")) {
            i8SBSwitchControllerResponseVO.setResponseCode("9999");
            i8SBSwitchControllerResponseVO.setDescription("Transaction Not Allowed Please Contact Bank Of Punjab");
        } else {

            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDescription());
            i8SBSwitchControllerResponseVO.setStatusFlag(this.getBio_Status_Flag());
            i8SBSwitchControllerResponseVO.setAmount(this.getAmount());
            i8SBSwitchControllerResponseVO.setTransactionFees(this.getTransaction_Fee());
            i8SBSwitchControllerResponseVO.setTotalAmount(new BigDecimal(this.getTotal_Amount()));
            i8SBSwitchControllerResponseVO.setTransactionId(this.getTransaction_ID());
        }
        return i8SBSwitchControllerResponseVO;

    }
}
