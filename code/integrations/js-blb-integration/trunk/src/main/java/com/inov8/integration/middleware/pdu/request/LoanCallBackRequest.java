package com.inov8.integration.middleware.pdu.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LoanCallBackRequest")
public class LoanCallBackRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    @XmlElement(name = "UserName")
    private String userName;
    @XmlElement(name = "Password")
    private String password;
    @XmlElement(name = "CustomerId")
    private String customerId;
    @XmlElement(name = "DateTime")
    private String dateTime;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "ChannelId")
    private String channelId;
    @XmlElement(name = "TerminalId")
    private String terminalId;
    @XmlElement(name = "LoanEvent")
    private String loanEvent ;
    @XmlElement(name = "LoanEventStatus")
    private String loanEventStatus;
    @XmlElement(name = "OrigSource")
    private String origSource;
    @XmlElement(name = "SourceRequestId")
    private String sourceRequestId;
    @XmlElement(name = "IdentityType")
    private String identityType;
    @XmlElement(name = "IdentityValue")
    private String identityValue;
    @XmlElement(name = "Reserved1")
    private String reserved1;
    @XmlElement(name = "Reserved2")
    private String reserved2;
    @XmlElement(name = "Reserved3")
    private String reserved3;
    @XmlElement(name = "Reserved4")
    private String reserved4;
    @XmlElement(name = "Reserved5")
    private String reserved5;
    @XmlElement(name = "Reserved6")
    private String reserved6;
    @XmlElement(name = "Reserved7")
    private String reserved7;
    @XmlElement(name = "Reserved8")
    private String reserved8;
    @XmlElement(name = "Reserved9")
    private String reserved9;
    @XmlElement(name = "Reserved10")
    private String reserved10;
    @XmlElement(name = "HashData")
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

    public String getSourceRequestId() {
        return sourceRequestId;
    }

    public void setSourceRequestId(String sourceRequestId) {
        this.sourceRequestId = sourceRequestId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentityValue() {
        return identityValue;
    }

    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
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

    public String getReserved6() {
        return reserved6;
    }

    public void setReserved6(String reserved6) {
        this.reserved6 = reserved6;
    }

    public String getReserved7() {
        return reserved7;
    }

    public void setReserved7(String reserved7) {
        this.reserved7 = reserved7;
    }

    public String getReserved8() {
        return reserved8;
    }

    public void setReserved8(String reserved8) {
        this.reserved8 = reserved8;
    }

    public String getReserved9() {
        return reserved9;
    }

    public void setReserved9(String reserved9) {
        this.reserved9 = reserved9;
    }

    public String getReserved10() {
        return reserved10;
    }

    public void setReserved10(String reserved10) {
        this.reserved10 = reserved10;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
