package com.inov8.integration.channel.wasa.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Transaction",
        "BillAmount",
        "BillAmountAfterDueDate",
        "DueDate",
        "ReceivedAmount",
        "Period",
        "ConsumerNumber",
        "ConsumerName",
        "status"
})
public class WasaGetBillResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(WasaGetBillResponse.class.getSimpleName());

    @JsonProperty("Transaction")
    private String transaction;
    @JsonProperty("BillAmount")
    private String billAmount;
    @JsonProperty("BillAmountAfterDueDate")
    private String billAmountAfterDueDate;
    @JsonProperty("DueDate")
    private String dueDate;
    @JsonProperty("ReceivedAmount")
    private String receivedAmount;
    @JsonProperty("Period")
    private String period;
    @JsonProperty("ConsumerNumber")
    private String consumerNumber;
    @JsonProperty("ConsumerName")
    private String consumerName;
    @JsonProperty("status")
    private String status;
    private String responseCode;
    private String description;

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillAmountAfterDueDate() {
        return billAmountAfterDueDate;
    }

    public void setBillAmountAfterDueDate(String billAmountAfterDueDate) {
        this.billAmountAfterDueDate = billAmountAfterDueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
        i8SBSwitchControllerResponseVO.setnTransaction(this.getTransaction());
        i8SBSwitchControllerResponseVO.setBillAmount(this.getBillAmount());
        i8SBSwitchControllerResponseVO.setBillAmountAfterDueDate(this.getBillAmountAfterDueDate());
        i8SBSwitchControllerResponseVO.setDueDate(this.getDueDate());
        i8SBSwitchControllerResponseVO.setAmount(this.getReceivedAmount());
        i8SBSwitchControllerResponseVO.setPeriod(this.getPeriod());
        i8SBSwitchControllerResponseVO.setConsumerNumber(this.getConsumerNumber());
        i8SBSwitchControllerResponseVO.setConsumerTitle(this.getConsumerName());
        i8SBSwitchControllerResponseVO.setStatus(this.getStatus());
        return i8SBSwitchControllerResponseVO;
    }
}