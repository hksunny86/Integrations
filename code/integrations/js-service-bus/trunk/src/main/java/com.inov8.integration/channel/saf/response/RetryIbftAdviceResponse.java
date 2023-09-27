package com.inov8.integration.channel.saf.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.safVO.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseCode",
        "messages",
        "data"
})
public class RetryIbftAdviceResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(RetryIbftAdviceResponse.class.getSimpleName());

    @JsonProperty("responseCode")
    private String responseCode;
    @JsonProperty("messages")
    private String messages;
    @JsonProperty("data")
    private Data data;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getResponseCode().equals("1")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            i8SBSwitchControllerResponseVO.setDescription("Success");
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
            i8SBSwitchControllerResponseVO.setDescription(this.getMessages());
        }
        if (this.getData() != null) {
            Data data = new Data();
            data.setSaf1linkIntegrationId(this.getData().getSaf1linkIntegrationId());
            data.setAccountbankname(this.getData().getAccountbankname());
            data.setAccountbranchname(this.getData().getAccountbranchname());
            data.setAccountno1(this.getData().getAccountno1());
            data.setAccountno2(this.getData().getAccountno2());
            data.setAccounttitle(this.getData().getAccounttitle());
            data.setAuthidresp(this.getData().getAuthidresp());
            data.setBeneficiaryid(this.getData().getBeneficiaryid());
            data.setCardacceptoridentificationcode(this.getData().getCardacceptoridentificationcode());
            data.setCardacceptornameandlocation(this.getData().getCardacceptornameandlocation());
            data.setCardacceptorterminalid(this.getData().getCardacceptorterminalid());
            data.setCreatedate(this.getData().getCreatedate());
            data.setCreateuser(this.getData().getCreateuser());
            data.setCurrencycode(this.getData().getCurrencycode());
            data.setLastupdatedate(this.getData().getLastupdatedate());
            data.setLastupdateuser(this.getData().getLastupdateuser());
            data.setMerchanttype(this.getData().getMerchanttype());
            data.setNetworkidentifier(this.getData().getNetworkidentifier());
            data.setNoOfRetries(this.getData().getNoOfRetries());
            data.setPan(this.getData().getPan());
            data.setPointofentry(this.getData().getPointofentry());
            data.setPurposeofpayment(this.getData().getPurposeofpayment());
            data.setRequestMessage(this.getData().getRequestMessage());
            data.setRrn(this.getData().getRrn());
            data.setSenderid(this.getData().getSenderid());
            data.setSendername(this.getData().getSendername());
            data.setStan(this.getData().getStan());
            data.setStatusdescr(this.getData().getStatusdescr());
            data.setTobankimd(this.getData().getTobankimd());
            data.setTransType(this.getData().getTransType());
            data.setTransactionamount(this.getData().getTransactionamount());
            data.setTransactiondatetime(this.getData().getTransactiondatetime());
            data.setUpdateindex(this.getData().getUpdateindex());
            i8SBSwitchControllerResponseVO.setRetryIbftAdvice(data);
        }

        return i8SBSwitchControllerResponseVO;
    }
}

