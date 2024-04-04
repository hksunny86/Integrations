package com.inov8.integration.channel.islamicSaving.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.lendingVO.GetActiveLoanData;
import com.inov8.integration.webservice.lendingVO.GetActiveLoanRequestPayload;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "id",
        "mobile",
        "amount",
})
public class IslamicSavingWithdrawalRequest extends Request {

    @JsonProperty("id")
    private String id;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("amount")
    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setId(i8SBSwitchControllerRequestVO.getId());
        this.setMobile(i8SBSwitchControllerRequestVO.getMobileNumber());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


