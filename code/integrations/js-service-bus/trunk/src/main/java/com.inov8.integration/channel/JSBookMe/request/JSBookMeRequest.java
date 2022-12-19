package com.inov8.integration.channel.JSBookMe.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JSBookMeRequest extends Request{
   @JsonProperty("Type")
    private String type;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("OrderRefId")
    private String orderRefId;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("partnerId")
    private String partnerId;
    @JsonProperty("api_key")
    private String apiKey = PropertyReader.getProperty("jsbookme.API_KEY");

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getAmount() { return amount; }

    public void setAmount(String amount) { this.amount = amount; }

    public String getOrderRefId() { return orderRefId; }

    public void setOrderRefId(String orderRefId) { this.orderRefId = orderRefId; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getPartnerId() { return partnerId; }

    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }

    public String getApiKey() { return apiKey; }

    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setApiKey(apiKey);
        this.setOrderRefId(i8SBSwitchControllerRequestVO.getOrderRefId());
        this.setType(i8SBSwitchControllerRequestVO.getType());
        this.setStatus(i8SBSwitchControllerRequestVO.getStatus());
        this.setAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setPartnerId(i8SBSwitchControllerRequestVO.getPartnerId());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getApiKey())) {
            throw new I8SBValidationException("[Failed] API KEY:" + this.getApiKey());
        }
        if (StringUtils.isEmpty(this.getOrderRefId())) {
            throw new I8SBValidationException("[Failed] Order Reference ID:" + this.getOrderRefId());
        }
        if (StringUtils.isEmpty(this.getType())) {
            throw new I8SBValidationException("[Failed] Type:" + this.getType());
        }
        if (StringUtils.isEmpty(this.getStatus())) {
            throw new I8SBValidationException("[Failed] Status:" + this.getStatus());
        }
        return true;
    }
}
