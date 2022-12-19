package com.inov8.integration.channel.microbank.response.AMAResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.*;


@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IBFTTitleFetchServiceResponse")
public class InterBankFundTransferTitleFetchResponse extends Response {
    @XmlElement(name = "responseCode")
    private String responseCode;
    @XmlElement(name = "accountTitle")
    private String accountTitle;
    @XmlElement(name = "destinationAccountNo")
    private String destinationAccount;
    @XmlElement(name = "desinatonBankIMD")
    private String destinationBankImd;
    @XmlElement(name = "feeCharged")
    private String feeCharged;
    @XmlElement(name = "reserved1")
    private String reserved1;
    @XmlElement(name = "reserved2")
    private String reserved2;
    @XmlElement(name = "reserved3")
    private String reserved3;
    @XmlElement(name = "reserved4")
    private String reserved4;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getDestinationBankImd() {
        return destinationBankImd;
    }

    public void setDestinationBankImd(String destinationBankImd) {
        this.destinationBankImd = destinationBankImd;
    }

    public String getFeeCharged() {
        return feeCharged;
    }

    public void setFeeCharged(String feeCharged) {
        this.feeCharged = feeCharged;
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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        return null;
    }
}
