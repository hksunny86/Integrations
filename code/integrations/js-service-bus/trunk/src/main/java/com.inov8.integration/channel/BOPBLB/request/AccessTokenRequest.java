package com.inov8.integration.channel.BOPBLB.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.inov8.integration.channel.BOP.BOPMB.request.Request;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Inov8 on 10/28/2019.
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccessTokenRequest extends com.inov8.integration.channel.BOPBLB.request.Request {
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("username")
    private String userName = PropertyReader.getProperty("bop.mb.username");
    ;
    @JsonProperty("password")
    private String password = PropertyReader.getProperty("bop.mb.password");

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setGrantType("client_credentials");
        this.setUserName(userName);
        this.setPassword(password);


    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }
}
