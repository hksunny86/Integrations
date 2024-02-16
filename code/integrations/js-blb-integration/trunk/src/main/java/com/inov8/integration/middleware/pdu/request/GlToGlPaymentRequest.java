package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userName",
        "password",
        "dateTime",
        "rrn",
        "channelId",
        "terminalId",
        "senderGlAccountNo",
        "receiverGlAccountNo",
        "amount",
        "productId",
        "reserved1",
        "reserved2",
        "reserved3",
        "reserved4",
        "reserved5",
        "hashData"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlToGlPaymentRequest implements Serializable {

    private static final long serialVersionUID = 6848934910669383177L;

    @JsonProperty("userName")
    private String userName;
    @JsonProperty("password")
    private String password;
    @JsonProperty("senderGlAccountNo")
    private String senderGlAccountNo;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("rrn")
    private String rrn;
    @JsonProperty("channelId")
    private String channelId;
    @JsonProperty("terminalId")
    private String terminalId;
    @JsonProperty("receiverGlAccountNo")
    private String receiverGlAccountNo;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("reserved1")
    private String reserved1;
    @JsonProperty("reserved2")
    private String reserved2;
    @JsonProperty("reserved3")
    private String reserved3;
    @JsonProperty("reserved4")
    private String reserved4;
    @JsonProperty("reserved5")
    private String reserved5;
    @JsonProperty("hashData")
    private String hashData;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getSenderGlAccountNo() {
        return senderGlAccountNo;
    }

    public void setSenderGlAccountNo(String senderGlAccountNo) {
        this.senderGlAccountNo = senderGlAccountNo;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    public String getReceiverGlAccountNo() {
        return receiverGlAccountNo;
    }

    public void setReceiverGlAccountNo(String receiverGlAccountNo) {
        this.receiverGlAccountNo = receiverGlAccountNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
