package com.inov8.integration.channel.brandverse.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.channel.tasdeeq.request.Request;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "username",
        "password"
})
public class AccessTokenRequest extends Request {

    @JsonProperty("username")
    private String userName;
    @JsonProperty("password")
    private String password;

    private String userId = PropertyReader.getProperty("brandverse.username");
    private String pass = PropertyReader.getProperty("brandverse.password");

    @JsonProperty("username")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("username")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setUserName(userId);
        this.setPassword(pass);
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

//        if (StringUtils.isEmpty(this.getUserName())) {
//            throw new I8SBValidationException("[Failed] Username:" + this.getUserName());
//        }
//        if (StringUtils.isEmpty(this.getPassword())) {
//            throw new I8SBValidationException("[Failed] Password:" + this.getPassword());
//        }
        return true;
    }
}
