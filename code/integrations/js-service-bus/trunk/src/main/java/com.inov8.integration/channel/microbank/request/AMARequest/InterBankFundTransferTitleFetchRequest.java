package com.inov8.integration.channel.microbank.request.AMARequest;


import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IBFTTitleFetchServiceRequest")
public class InterBankFundTransferTitleFetchRequest extends Request {

    @XmlElement(name = "msisdn")
    private String msisdn;
    @XmlElement(name = "sourceBankImd")
    private String sourceBankImd;
    @XmlElement(name = "sourceAccountNo")
    private String sourceAccountNo;
    @XmlElement(name = "destinationBankImd")
    private String destinationBankImd;
    @XmlElement(name = "destinationAccountNo")
    private String destinationAccountNo;
    @XmlElement(name = "amount")
    private String amount;
    @XmlElement(name = "reserved1")
    private String reserved1;
    @XmlElement(name = "reserved2")
    private String reserved2;
    @XmlElement(name = "reserved3")
    private String reserved3;
    @XmlElement(name = "reserved4")
    private String reserved4;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSourceBankImd() {
        return sourceBankImd;
    }

    public void setSourceBankImd(String sourceBankImd) {
        this.sourceBankImd = sourceBankImd;
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
    }

    public String getDestinationBankImd() {
        return destinationBankImd;
    }

    public void setDestinationBankImd(String destinationBankImd) {
        this.destinationBankImd = destinationBankImd;
    }

    public String getDestinationAccountNo() {
        return destinationAccountNo;
    }

    public void setDestinationAccountNo(String destinationAccountNo) {
        this.destinationAccountNo = destinationAccountNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
