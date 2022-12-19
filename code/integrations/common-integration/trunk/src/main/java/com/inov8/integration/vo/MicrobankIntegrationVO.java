package com.inov8.integration.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Umar Aziz on 7/5/2017.
 */
public class MicrobankIntegrationVO implements Serializable {

    private String url;
    private String contentType;
    private Map<String, String> paramsMap = new HashMap<String, String>();
    private String requestType;
    private String responseData;
    private String responseCode;
    private String customField;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }
}
