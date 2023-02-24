package com.inov8.integration.channel.tasdeeq.request;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "UserName",
        "Password"
})
public class AuthenticateUpdatedRequest extends Request{

    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;

    @JsonProperty("UserName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("UserName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setUserName(i8SBSwitchControllerRequestVO.getUserName());
        this.setPassword(i8SBSwitchControllerRequestVO.getPassword());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getUserName())) {
            throw new I8SBValidationException("[Failed] Username:" + this.getUserName());
        }
        if (StringUtils.isEmpty(this.getPassword())) {
            throw new I8SBValidationException("[Failed] Password:" + this.getPassword());
        }
        return true;
    }
}
