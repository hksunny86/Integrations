package com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class MoneyTransferRequest extends Request implements Serializable {
    private static final long serialVersionUID = 2134956891427267824L;
    @JsonProperty("KEY_TRANSACTION_REF")
    private String keyTransferRefrence;
    @JsonProperty("KEY_AMOUNT")
    private String keyAmount;
    @JsonProperty("KEY_FROM_ACCOUNT")
    private String keyFromAccount;
    @JsonProperty("KEY_CHANNEL_MOBILE")
    private String keyChannelMobile;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("STAN")
    private String stan;

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getAccessToken() {

        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getKeyTransferRefrence() {
        return keyTransferRefrence;
    }

    public void setKeyTransferRefrence(String keyTransferRefrence) {
        this.keyTransferRefrence = keyTransferRefrence;
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

    public String getKeyChannelMobile() {
        return keyChannelMobile;
    }

    public void setKeyChannelMobile(String keyChannelMobile) {
        this.keyChannelMobile = keyChannelMobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @JsonProperty("OTP")
    private String otp;


    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setKeyAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.setKeyFromAccount(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setKeyChannelMobile(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setAccessToken(i8SBSwitchControllerRequestVO.getAccessToken());
        this.setKeyTransferRefrence(i8SBSwitchControllerRequestVO.getSessionId());
        this.setStan(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setOtp(i8SBSwitchControllerRequestVO.getOtp());

    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getKeyAmount())) {
            throw new I8SBValidationException("[Failed] KEY_AMOUNT:" + this.getKeyAmount());
        }
        if (StringUtils.isEmpty(this.getKeyTransferRefrence())) {
            throw new I8SBValidationException("[Failed] TRANSFER_REFRENCE:" + this.getKeyTransferRefrence());
        }
        if (StringUtils.isEmpty(this.getKeyChannelMobile())) {
            throw new I8SBValidationException("[Failed] KEY_CHANNEL_AMOUNT:" + this.getKeyChannelMobile());
        }
        if (StringUtils.isEmpty(this.getKeyFromAccount())) {
            throw new I8SBValidationException("[Failed] Key_FROM_ACCOUNT" + this.getKeyFromAccount());
        }
        if (StringUtils.isEmpty(this.getOtp())) {
            throw new I8SBValidationException("[Failed] OTP" + this.getOtp());
        }
        if (StringUtils.isEmpty(this.getStan())) {
            throw new I8SBValidationException("[Failed] STAN" + this.getStan());
        }
        return true;
    }
}
