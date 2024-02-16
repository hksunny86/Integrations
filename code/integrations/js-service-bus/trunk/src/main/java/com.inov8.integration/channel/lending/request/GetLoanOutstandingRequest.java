package com.inov8.integration.channel.lending.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.lending.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.lendingVO.LendingData;
import com.inov8.integration.webservice.lendingVO.Payload;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "data",
})
public class GetLoanOutstandingRequest extends Request {

    @JsonProperty("data")
    private LendingData data;

    public LendingData getData() {
        return data;
    }

    public void setData(LendingData data) {
        this.data = data;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        Payload payload = new Payload();
        payload.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        payload.setMobileNumber(i8SBSwitchControllerRequestVO.getTransactionId());
        payload.setMobileNumber(i8SBSwitchControllerRequestVO.getAmount());
        LendingData lendingData = new LendingData();
        lendingData.setPayLoad(payload);
        this.setData(lendingData);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


