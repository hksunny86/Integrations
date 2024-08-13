package com.inov8.integration.channel.crp.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.webservice.crpVO.*;

import java.util.ArrayList;
import java.util.List;

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
        Security security = new Security();
        security.setUserName("");
        security.setPassword("");
        security.setSecurityToken("");
        data.setSecurity(security);
        account.setMsidn(i8SBSwitchControllerRequestVO.getMobileNumber());
        account.setIban("");
        account.setBban("");
        account.setPan("");
        account.setCurrency("");
        data.setAccount(account);
        data.setChannel(i8SBSwitchControllerRequestVO.getChannelID());
        data.setTerminal(i8SBSwitchControllerRequestVO.getTerminalID());
        data.setReterivalReferenceNumber(i8SBSwitchControllerRequestVO.getRRN());
        data.setPayLoad(i8SBSwitchControllerRequestVO.getPayload());
        AdditionalInformation additionalInformation = new AdditionalInformation();
        additionalInformation.setInfoKey("");
        additionalInformation.setInfoValue("");
        List<AdditionalInformation> list = new ArrayList<>();
        list.add(additionalInformation);
        data.setAdditionalInformation(list);
        data.setCheckSum("");
        this.setData(data);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


