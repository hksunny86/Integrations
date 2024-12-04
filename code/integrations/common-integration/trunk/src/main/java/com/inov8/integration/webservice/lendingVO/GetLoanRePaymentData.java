package com.inov8.integration.webservice.lendingVO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLoanRePaymentData implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private Security security;
    private Account account;
    private String channel;
    private String terminal;
    private String reterivalReferenceNumber;
    private GetLoanRepaymentRequestPayload payLoad;
    private AdditionalInformation additionalInformation;
    private String checkSum;

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getReterivalReferenceNumber() {
        return reterivalReferenceNumber;
    }

    public void setReterivalReferenceNumber(String reterivalReferenceNumber) {
        this.reterivalReferenceNumber = reterivalReferenceNumber;
    }

    public GetLoanRepaymentRequestPayload getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(GetLoanRepaymentRequestPayload payLoad) {
        this.payLoad = payLoad;
    }

    public AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(AdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }
}
