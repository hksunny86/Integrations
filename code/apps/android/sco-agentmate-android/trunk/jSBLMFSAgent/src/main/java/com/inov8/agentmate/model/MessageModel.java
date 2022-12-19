package com.inov8.agentmate.model;

public class MessageModel {

    public String code = null;
    public String level = null;
    public String descr = null;
    public String nadraSessionId = null;
    public String thirdPartyTransactionId = null;

    public MessageModel(){}

    public MessageModel(String code, String level, String descr) {
        super();
        this.code = code;
        this.level = level;
        this.descr = descr;
    }

    public MessageModel(String code, String level, String descr, String nadraSessionId, String thirdPartyTransactionId) {
        super();
        this.code = code;
        this.level = level;
        this.descr = descr;
        this.nadraSessionId = nadraSessionId;
        this.thirdPartyTransactionId = thirdPartyTransactionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNadraSessionId() {
        return nadraSessionId;
    }

    public void setNadraSessionId(String nadraSessionId) {
        this.nadraSessionId = nadraSessionId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getThirdPartyTransactionId() { return thirdPartyTransactionId; }

    public void setThirdPartyTransactionId(String thirdPartyTransactionId) {
        this.thirdPartyTransactionId = thirdPartyTransactionId;
    }
}