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
        "status"
})
public class WasaBillReversalResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(WasaBillReversalResponse.class.getSimpleName());

    @JsonProperty("Transaction")
    private String transaction;
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
            if (this.getStatus().equalsIgnoreCase("0")) {
                i8SBSwitchControllerResponseVO.setDescription("REVERSAL SUCCESSFUL");
            } else if (this.getStatus().equalsIgnoreCase("1")) {
                i8SBSwitchControllerResponseVO.setDescription("TRANSACTION ID NOT FOUND/TRANSACTION EXPIRED");
            } else if (this.getStatus().equalsIgnoreCase("2")) {
                i8SBSwitchControllerResponseVO.setDescription("REVERSAL FAILED! MISMATCH OF OLD TRANSACTION ID");
            } else if (this.getStatus().equalsIgnoreCase("3")) {
                i8SBSwitchControllerResponseVO.setDescription("SAME OLD AND NEW TRANSACTION ID");
            } else if (this.getStatus().equalsIgnoreCase("4")) {
                i8SBSwitchControllerResponseVO.setDescription("DUPLICATE TRANSACTION ID");
            } else if (this.getStatus().equalsIgnoreCase("5")) {
                i8SBSwitchControllerResponseVO.setDescription("REVERSAL ALREADY MARKED");
            } else if (this.getStatus().equalsIgnoreCase("6")) {
                i8SBSwitchControllerResponseVO.setDescription("INVALID CONSUMER ID");
            } else if (this.getStatus().equalsIgnoreCase("7")) {
                i8SBSwitchControllerResponseVO.setDescription("REVERSAL NOT ALLOWED");
            } else if (this.getStatus().equalsIgnoreCase("8")) {
                i8SBSwitchControllerResponseVO.setDescription("VALUES OVERFLOW FOR A COLUMN CONTACT ADMINISTRATOR FOR HELP");
            }
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getDescription());
        }
        i8SBSwitchControllerResponseVO.setTransactionDecs(this.getTransaction());
        i8SBSwitchControllerResponseVO.setStatus(this.getStatus());
        return i8SBSwitchControllerResponseVO;
    }
}