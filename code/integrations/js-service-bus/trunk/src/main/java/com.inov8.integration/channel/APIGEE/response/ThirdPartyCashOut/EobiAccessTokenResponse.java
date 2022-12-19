package com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EobiAccessTokenResponse extends Response {


    private String refresh_token_expires_in;
    private String api_product_list;
    private List<String> api_product_list_json;
    private String organization_name;
    private String developeremail;
    private String token_type;
    private String issued_at;
    private String client_id;
    private String access_token;
    private String application_name;
    private String scope;
    private String expires_in;
    private String refresh_count;
    private String status;

    public String getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

    public void setRefresh_token_expires_in(String refresh_token_expires_in) {
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public String getApi_product_list() {
        return api_product_list;
    }

    public void setApi_product_list(String api_product_list) {
        this.api_product_list = api_product_list;
    }

    public List<String> getApi_product_list_json() {
        return api_product_list_json;
    }

    public void setApi_product_list_json(List<String> api_product_list_json) {
        this.api_product_list_json = api_product_list_json;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getDeveloperemail() {
        return developeremail;
    }

    public void setDeveloperemail(String developeremail) {
        this.developeremail = developeremail;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getIssued_at() {
        return issued_at;
    }

    public void setIssued_at(String issued_at) {
        this.issued_at = issued_at;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_count() {
        return refresh_count;
    }

    public void setRefresh_count(String refresh_count) {
        this.refresh_count = refresh_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setAccessToken(this.getAccess_token());
        if (this.getStatus().equals("approved")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        }
        return i8SBSwitchControllerResponseVO;
    }


}