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
public class WasaBillClearanceResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(WasaBillClearanceResponse.class.getSimpleName());

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
                i8SBSwitchControllerResponseVO.setDescription("SUCCESSFUL");
            } else if (this.getStatus().equalsIgnoreCase("1")) {
                i8SBSwitchControllerResponseVO.setDescription("TRANSACTION ID NOT FOUND/TRANSACTION EXPIRED");
            } else if (this.getStatus().equalsIgnoreCase("2")) {
                i8SBSwitchControllerResponseVO.setDescription("TRANSACTION PAYMODE IS NOT CHEQUE");
            }  else if (this.getStatus().equalsIgnoreCase("6")) {
                i8SBSwitchControllerResponseVO.setDescription("INVALID CONSUMER NUMBER");
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