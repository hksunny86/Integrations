package com.inov8.integration.client.socket;

/**
 * Created by inov8 on 9/7/2017.
 */
public class I8SBSocketConnectionPropertiesVO {
    private String channelID;
    private String ip;
    private int port;
    private int retryTimeInSeconds;
    private int responseTimeOutInSeconds;

    public int getResponseTimeOutInSeconds() {
        return responseTimeOutInSeconds;
    }

    public void setResponseTimeOutInSeconds(int responseTimeOutInSeconds) {
        this.responseTimeOutInSeconds = responseTimeOutInSeconds;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRetryTimeInSeconds() {
        return retryTimeInSeconds;
    }

    public void setRetryTimeInSeconds(int retryTimeInSeconds) {
        this.retryTimeInSeconds = retryTimeInSeconds;
    }
}
