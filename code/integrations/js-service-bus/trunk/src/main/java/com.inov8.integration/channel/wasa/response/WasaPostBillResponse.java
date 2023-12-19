package com.inov8.integration.channel.wasa.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "Transaction",
        "status"
})
public class WasaPostBillResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(WasaPostBillResponse.class.getSimpleName());

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
            if (this.getStatus() != null && this.getStatus().equalsIgnoreCase("1")) {
                i8SBSwitchControllerResponseVO.setBillStatus("P");
            } else {
                i8SBSwitchControllerResponseVO.setBillStatus("U");
            }
            if (this.getStatus() != null && this.getStatus().equalsIgnoreCase("2")) {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getStatus());
                i8SBSwitchControllerResponseVO.setBillStatus("INVALID AMOUNT ENTERED PLEASE TRY AGAIN");
                i8SBSwitchControllerResponseVO.setDescription("INVALID AMOUNT ENTERED PLEASE TRY AGAIN");
            } else if (this.getStatus() != null && this.getStatus().equalsIgnoreCase("3")) {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getStatus());
                i8SBSwitchControllerResponseVO.setBillStatus("BILL IS ALREADY PAID");
                i8SBSwitchControllerResponseVO.setDescription("BILL IS ALREADY PAID");
            } else if (this.getStatus() != null && this.getStatus().equalsIgnoreCase("4")) {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getStatus());
                i8SBSwitchControllerResponseVO.setBillStatus("DUPLICATE TRANSACTION ID");
                i8SBSwitchControllerResponseVO.setDescription("DUPLICATE TRANSACTION ID");
            } else if (this.getStatus() != null && this.getStatus().equalsIgnoreCase("0")) {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getStatus());
                i8SBSwitchControllerResponseVO.setBillStatus("BILL INFO NOT FOUND");
                i8SBSwitchControllerResponseVO.setDescription("BILL INFO NOT FOUND");

            }
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getStatus());
        }
        i8SBSwitchControllerResponseVO.setTransactionDecs(this.getTransaction());
        i8SBSwitchControllerResponseVO.setStatus(this.getStatus());
        return i8SBSwitchControllerResponseVO;
    }
}