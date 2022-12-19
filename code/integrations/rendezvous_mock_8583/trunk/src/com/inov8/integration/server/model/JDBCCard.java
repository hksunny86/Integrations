package com.inov8.integration.server.model;

import java.io.Serializable;

/**
 * Created by ZeeshanAh1 on 12/15/2015.
 */
public class JDBCCard implements Serializable{

    private static final long serialVersionUID = 4974733313355856582L;

    private String PAN;
    private String cardExpiry;
    private String cardStatus;
    private String PIN;
    private String password;
    private String cardType;
    private String channelStatus;

    private String responseCode;
    private int ttl;

    public String getPAN() {
        return PAN;
    }

    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }
}
