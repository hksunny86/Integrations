package com.inov8.integration.channel.lending.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.lendingVO.GetActiveLoanData;
import com.inov8.integration.webservice.lendingVO.GetActiveLoanRequestPayload;
import com.inov8.integration.webservice.lendingVO.GetOutstandingData;
import com.inov8.integration.webservice.lendingVO.GetOutstandingRequestPayload;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "data",
})
public class GetActiveLoanRequest extends Request {

    @JsonProperty("data")
    private GetActiveLoanData data;

    public GetActiveLoanData getData() {
        return data;
    }

    public void setData(GetActiveLoanData data) {
        this.data = data;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        GetActiveLoanRequestPayload payload = new GetActiveLoanRequestPayload();
        payload.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        GetActiveLoanData activeLoanData = new GetActiveLoanData();
        activeLoanData.setPayLoad(payload);
        this.setData(activeLoanData);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


