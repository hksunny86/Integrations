package com.inov8.microbank.app.vo;

import java.io.Serializable;

/**
 * Created by Atieq Rehman on 8/25/2016.
 */
public class AppInfoVO implements Serializable{

    private Long appId;
    private String appName;
    private String url;
    private String osType;
    private Long appUserTypeId;

    public AppInfoVO() {
    }


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long userTypeId) {
        this.appUserTypeId = userTypeId;
    }
}
