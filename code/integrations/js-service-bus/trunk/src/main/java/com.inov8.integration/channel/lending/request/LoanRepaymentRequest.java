package com.inov8.integration.channel.lending.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.lending.request.Request;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.lendingVO.LoanPaymentData;
import com.inov8.integration.webservice.lendingVO.LoanPaymentRequestPayload;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "data",
})
public class LoanRepaymentRequest extends Request {

    @JsonProperty("data")
    private LoanPaymentData data;

    public LoanPaymentData getData() {
        return data;
    }

    public void setData(LoanPaymentData data) {
        this.data = data;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        LoanPaymentRequestPayload payload = new LoanPaymentRequestPayload();
        payload.setMobileNumber(i8SBSwitchControllerRequestVO.getMobileNumber());
        payload.setTransactionId(i8SBSwitchControllerRequestVO.getTransactionId());
        payload.setAmount(i8SBSwitchControllerRequestVO.getAmount());
        LoanPaymentData lendingData = new LoanPaymentData();
        lendingData.setPayLoad(payload);
        this.setData(lendingData);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


