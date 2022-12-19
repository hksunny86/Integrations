package com.inov8.microbank.app.vo;

import java.io.Serializable;

/**
 * Created by Atieq Rehman on 8/25/2016.
 */
public class AppVO implements Serializable{

    private Long appId;
    private String appName;
    private String url;
    private String osType;
    private String appVersion;
    private String fromCompatibleVersion;
    private String toCompatibleVersion;
    private Long appUserTypeId;
    private String appUsageLevel;

    public AppVO() {
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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getFromCompatibleVersion() {
        return fromCompatibleVersion;
    }

    public void setFromCompatibleVersion(String fromCompatibleVersion) {
        this.fromCompatibleVersion = fromCompatibleVersion;
    }

    public String getToCompatibleVersion() {
        return toCompatibleVersion;
    }

    public void setToCompatibleVersion(String toCompatibleVersion) {
        this.toCompatibleVersion = toCompatibleVersion;
    }

    public Long getAppUserTypeId() {
        return appUserTypeId;
    }

    public void setAppUserTypeId(Long userTypeId) {
        this.appUserTypeId = userTypeId;
    }

    public String getAppUsageLevel() {
        return appUsageLevel;
    }

    public void setAppUsageLevel(String appUsageLevel) {
        this.appUsageLevel = appUsageLevel;
    }
}
