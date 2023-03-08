package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement
@JsonIgnoreProperties(
        ignoreUnknown = true
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "UserName",
        "Password",
        "CustomerId",
        "DateTime",
        "Rrn",
        "ChannelId",
        "TerminalId",
        "LoanEvent",
        "LoanEventStatus",
        "OrigSource",
        "InternalLoanId",
        "ThirdPartyTransactionId",
        "Reserved1",
        "Reserved2",
        "HashData",
})
public class LoanCallBackRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("CustomerId")
    private String customerId;
    @JsonProperty("DateTime")
    private String dateTime;
    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ChannelId")
    private String channelId;
    @JsonProperty("TerminalId")
    private String terminalId;
    @JsonProperty("LoanEvent")
    private String loanEvent;
    @JsonProperty("LoanEventStatus")
    private String loanEventStatus;
    @JsonProperty("OrigSource")
    private String origSource;
    @JsonProperty("InternalLoanId")
    private String internalLoanId;
    @JsonProperty("ThirdPartyTransactionId")
    private String thirdPartyTransactionId;
    @JsonProperty("Reserved1")
    private String reserved1;
    @JsonProperty("Reserved2")
    private String reserved2;
    @JsonProperty("HashData")
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getLoanEvent() {
        return loanEvent;
    }

    public void setLoanEvent(String loanEvent) {
        this.loanEvent = loanEvent;
    }

    public String getLoanEventStatus() {
        return loanEventStatus;
    }

    public void setLoanEventStatus(String loanEventStatus) {
        this.loanEventStatus = loanEventStatus;
    }

    public String getOrigSource() {
        return origSource;
    }

    public void setOrigSource(String origSource) {
        this.origSource = origSource;
    }

    public String getInternalLoanId() {
        return internalLoanId;
    }

    public void setInternalLoanId(String internalLoanId) {
        this.internalLoanId = internalLoanId;
    }

    public String getThirdPartyTransactionId() {
        return thirdPartyTransactionId;
    }

    public void setThirdPartyTransactionId(String thirdPartyTransactionId) {
        this.thirdPartyTransactionId = thirdPartyTransactionId;
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

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
