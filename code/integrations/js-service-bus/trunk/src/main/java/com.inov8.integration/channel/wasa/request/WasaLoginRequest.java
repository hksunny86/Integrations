package com.inov8.integration.channel.wasa.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "password",
        "ipaddress",
        "grant_type"
})
public class WasaLoginRequest extends Request {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("ipaddress")
    private String ipaddress;
    @JsonProperty("grant_type")
    private String grantType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setUsername(PropertyReader.getProperty("wasa.username"));
        this.setPassword(PropertyReader.getProperty("wasa.password"));
        this.setIpaddress(PropertyReader.getProperty("wasa.ip.address"));
        this.setGrantType(PropertyReader.getProperty("wasa.grant.type"));
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}


