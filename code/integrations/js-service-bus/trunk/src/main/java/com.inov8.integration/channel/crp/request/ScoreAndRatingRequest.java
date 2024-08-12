package com.inov8.integration.channel.crp.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.crpVO.Account;
import com.inov8.integration.webservice.crpVO.CrpDataRequestPayload;
import com.inov8.integration.webservice.crpVO.Payload;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "data",
})
public class ScoreAndRatingRequest extends Request {

    @JsonProperty("data")
    private CrpDataRequestPayload data;

    public CrpDataRequestPayload getData() {
        return data;
    }

    public void setData(CrpDataRequestPayload data) {
        this.data = data;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        CrpDataRequestPayload data = new CrpDataRequestPayload();
        Account account = new Account();
        account.setMsidn(i8SBSwitchControllerRequestVO.getMobileNumber());
        data.setAccount(account);
        data.setPayLoad(i8SBSwitchControllerRequestVO.getPayload());
        this.setData(data);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


