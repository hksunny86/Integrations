package com.inov8.integration.channel.eocean.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "password",
        "message",
        "receiver",
        "network",
})
public class EoceanRequest extends Request implements Serializable {

    private String userName = PropertyReader.getProperty("username");
    private String pass = PropertyReader.getProperty("password");

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("message")
    private String message;
    @JsonProperty("receiver")
    private String receiver;
    @JsonProperty("network")
    private String network;

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("receiver")
    public String getReceiver() {
        return receiver;
    }

    @JsonProperty("receiver")
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    @JsonProperty("network")
    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setUsername(userName);
        this.setPassword(pass);
        this.setMessage(i8SBSwitchControllerRequestVO.getSmsText());
        this.setNetwork(i8SBSwitchControllerRequestVO.getMobileNetwork());
        this.setReceiver(i8SBSwitchControllerRequestVO.getRecieverMobileNo());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getMessage())) {
            throw new I8SBValidationException("[Failed] Message:" + this.getMessage());
        }
        if (StringUtils.isEmpty(this.getNetwork())) {
            throw new I8SBValidationException("[Failed] Network:" + this.getNetwork());
        }
        if (StringUtils.isEmpty(this.getReceiver())) {
            throw new I8SBValidationException("[Failed] Receiver Number:" + this.getReceiver());
        }
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
