package com.inov8.integration.middleware.pdu.response;

import javax.xml.bind.annotation.*;


@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IbftAdviceResponse")
public class IbftAdviceResponse {


    @XmlElement(name = "ResponseCode")
    private String responseCode;
    @XmlElement(name = "ResponseDescription")
    private String responseDescription;
    @XmlElement(name = "DestinationIMD")
    private String destinationBankImd;
    @XmlElement(name = "DestinationAccountNumber")
    private String destinationAccountNumber;
    @XmlElement(name = "Rrn")
    private String rrn;
    @XmlElement(name = "HashData")
    private String hashData;


    public String getDestinationBankImd() {

        return destinationBankImd;
    }

    public void setDestinationBankImd(String destinationBankImd) {
        this.destinationBankImd = destinationBankImd;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}
