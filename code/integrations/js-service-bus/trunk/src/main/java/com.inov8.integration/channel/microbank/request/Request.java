package com.inov8.integration.channel.microbank.request;

import autovalue.shaded.com.google$.common.base.$Strings;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by inov8 on 4/23/2018.
 */
public abstract class Request {

    public Map<String,Object> hashMap;
    private String ClientId;
    private String TerminalId;
    private String ChannelId;
    private String RRN;
    private String UserName;
    private String Password;
    private String CheckSum;
    private String RequestDateTime;
    private String RequestType;

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getTerminalId() {
        return TerminalId;
    }

    public void setTerminalId(String terminalId) {
        TerminalId = terminalId;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public void setChannelId(String channelId) {
        ChannelId = channelId;
    }

    public String getRRN() {
        return RRN;
    }

    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCheckSum() {
        return CheckSum;
    }

    public void setCheckSum(String checkSum) {
        CheckSum = checkSum;
    }

    public String getRequestDateTime() {
        return RequestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        RequestDateTime = requestDateTime;
    }
    public abstract void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO);
    public abstract boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException;
    public abstract void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException;
    public void BillingPopulateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO)
    {
        hashMap = i8SBSwitchControllerRequestVO.getCollectionOfList();
        if (hashMap.get("ClientId") != null && StringUtils.isNotEmpty(hashMap.get("ClientId").toString())) {
            this.setClientId(hashMap.get("ClientId").toString());
        }
        if (hashMap.get("TerminalId") != null && StringUtils.isNotEmpty(hashMap.get("TerminalId").toString())) {
            this.setTerminalId(hashMap.get("TerminalId").toString());
        }
        if (hashMap.get("RequestDateTime") != null && StringUtils.isNotEmpty(hashMap.get("RequestDateTime").toString())) {
            this.setRequestDateTime(hashMap.get("RequestDateTime").toString());
        }
        if (hashMap.get("RRN") != null && StringUtils.isNotEmpty(hashMap.get("RRN").toString())) {
            this.setRRN(hashMap.get("RRN").toString());
        }

        if (hashMap.get("UserName") != null && StringUtils.isNotEmpty(hashMap.get("UserName").toString())) {
            this.setUserName(hashMap.get("UserName").toString());
        }

        if (hashMap.get("Password") != null && StringUtils.isNotEmpty(hashMap.get("Password").toString())) {
            this.setPassword(hashMap.get("Password").toString());
        }

        if (hashMap.get("RequestType") != null && StringUtils.isNotEmpty(hashMap.get("RequestType").toString())) {
            this.setRequestType(hashMap.get("RequestType").toString());
        }
        if (hashMap.get("CheckSum") != null && StringUtils.isNotEmpty(hashMap.get("CheckSum").toString())) {
            this.setCheckSum(hashMap.get("CheckSum").toString());
        }
    }
    public boolean BillingValidateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO)
    {
        if (StringUtils.isEmpty(this.getClientId())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Client ID  is a mandatory parameter! ");
        }
        if(StringUtils.isEmpty(this.getTerminalId()))
        {
            throw new I8SBValidationException("[VALIDATION FAILED] Terminal ID  is a mandatory parameter! ");
        }
        if(StringUtils.isEmpty(this.getChannelId()))
        {
            throw new I8SBValidationException("[VALIDATION FAILED] Channel ID  is a mandatory parameter! ");
        }
        if(StringUtils.isEmpty(this.getRRN()))
        {
            throw new I8SBValidationException("[VALIDATION FAILED] RRN  is a mandatory parameter! ");
        }
        if(StringUtils.isEmpty(this.getRequestDateTime()))
        {
            throw new I8SBValidationException("[VALIDATION FAILED] RequestDateTime  is a mandatory parameter! ");
        }
        if(StringUtils.isEmpty(this.getUserName()))
        {
            throw new I8SBValidationException("[FAILED] UserName validation! ");
        }
        if(StringUtils.isEmpty(this.getPassword()))
        {
            throw new I8SBValidationException("[FAILED] Password validation! ");
        }
        if(StringUtils.isEmpty(this.getRequestType()))
        {
            throw new I8SBValidationException("[FAILED] RequestType is a mandatory parameter!");
        }
        if(StringUtils.isEmpty(this.getCheckSum()))
        {
            throw new I8SBValidationException("[FAILED] Checksum is a mandatory parameter! ");
        }
        return true;
    }
}
