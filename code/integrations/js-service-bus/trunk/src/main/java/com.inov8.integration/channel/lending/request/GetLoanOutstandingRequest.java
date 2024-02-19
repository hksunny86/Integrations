package com.inov8.integration.channel.lending.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.lendingVO.GetOutstandingData;
import com.inov8.integration.webservice.lendingVO.GetOutstandingRequestPayload;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "data",
})
public class GetLoanOutstandingRequest extends Request {

    @JsonProperty("data")
    private GetOutstandingData data;

    public GetOutstandingData getData() {
        return data;
    }

    public void setData(GetOutstandingData data) {
        this.data = data;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        GetOutstandingRequestPayload payload = new GetOutstandingRequestPayload();
        payload.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        GetOutstandingData lendingData = new GetOutstandingData();
        lendingData.setPayLoad(payload);
        this.setData(lendingData);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


