package com.inov8.integration.dao.i8sb.model;

/**
 * Created by inov8 on 9/8/2017.
 */
public class LinkedRequest {

    private String clientID;
    private String channelID;
    private String RequestType;
    private String linkedRequest;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    public String getLinkedRequest() {
        return linkedRequest;
    }

    public void setLinkedRequest(String linkedRequest) {
        this.linkedRequest = linkedRequest;
    }
}
