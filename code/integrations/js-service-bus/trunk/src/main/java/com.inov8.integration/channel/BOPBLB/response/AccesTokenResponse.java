package com.inov8.integration.channel.BOPBLB.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccesTokenResponse extends Response {

    @JsonProperty("Access_Token")
    private String access_token;
    @JsonProperty("Token_Type")
    private String Token_Type;
    @JsonProperty("Expires_In")
    private String Expires_In;
    @JsonProperty("api_product_list_json")
    private String api_product_list_json;
    @JsonProperty("Issued_At")
    private String Issued_At;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_Type() {
        return Token_Type;
    }

    public void setToken_Type(String token_Type) {
        Token_Type = token_Type;
    }

    public String getExpires_In() {
        return Expires_In;
    }

    public void setExpires_In(String expires_In) {
        Expires_In = expires_In;
    }

    public String getApi_product_list_json() {
        return api_product_list_json;
    }

    public void setApi_product_list_json(String api_product_list_json) {
        this.api_product_list_json = api_product_list_json;
    }

    public String getIssued_At() {
        return Issued_At;
    }

    public void setIssued_At(String issued_At) {
        Issued_At = issued_At;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO=new I8SBSwitchControllerResponseVO();


        return i8SBSwitchControllerResponseVO;
    }
}
