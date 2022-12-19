package com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TitleFetchRequest extends Request implements Serializable {
    private static final long serialVersionUID = 2134956891427267824L;
    @JsonProperty("KEY_AMOUNT")
    private String keyAmount;
    @JsonProperty("KEY_FROM_ACCOUNT")
    private String keyFromAccount;

    public String getKeyToAccount() {
        return keyToAccount;
    }

    public void setKeyToAccount(String keyToAccount) {
        this.keyToAccount = keyToAccount;
    }


    @JsonProperty("KEY_TO_ACCOUNT")
    private String keyToAccount;
    @JsonProperty("KEY_CHANNEL_MOBILE_NO")
    private String keyChannelMobileNo;
    @JsonProperty("accessToken")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getKeyAmount() {
        return keyAmount;
    }

    public void setKeyAmount(String keyAmount) {
        this.keyAmount = keyAmount;
    }

    public String getKeyFromAccount() {
        return keyFromAccount;
    }

    public void setKeyFromAccount(String keyFromAccount) {
        this.keyFromAccount = keyFromAccount;
    }

    public String getKeyChannelMobileNo() {
        return keyChannelMobileNo;
    }

    public void setKeyChannelMobileNo(String keyChannelMobileNo) {
        this.keyChannelMobileNo = keyChannelMobileNo;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setKeyAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setKeyFromAccount(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setKeyToAccount(i8SBSwitchControllerRequestVO.getSenderMobile());
        this.setKeyChannelMobileNo(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setAccessToken(i8SBSwitchControllerRequestVO.getAccessToken());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getKeyAmount())) {
            throw new I8SBValidationException("[Failed] KEY_AMOUNT: " + this.getKeyAmount());
        }
        if (StringUtils.isEmpty(this.getKeyChannelMobileNo())) {
            throw new I8SBValidationException("[Failed] KEY_CHANNEL_MOBILE_NUMBER: " + this.getKeyChannelMobileNo());
        }
        if (StringUtils.isEmpty(this.getKeyFromAccount())) {
            throw new I8SBValidationException("[Failed] KEY_FROM_ACCOUNT: " + this.getKeyFromAccount());
        }
        if(StringUtils.isEmpty(this.getKeyToAccount())){
            throw new I8SBValidationException("[Failed] KEY_TO_ACCOUNT"+this.keyToAccount);
        }
        return true;
    }
}
