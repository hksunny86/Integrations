package com.inov8.integration.channel.microbank.request.AMARequest;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.ValidationException;


/**
 * Created by Inov8 on 12/16/2019.
 */
public class AccountRegistrationRequest extends Request {

    private String accountType;
    private String cnic;
    private String cnicIssuanceDate;
    private String msisdn;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String type;
    private String name;
    private String dateTime;
    private String bankImd;
    private String traceNo;

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCnicIssuanceDate() {
        return cnicIssuanceDate;
    }

    public void setCnicIssuanceDate(String cnicIssuanceDate) {
        this.cnicIssuanceDate = cnicIssuanceDate;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBankImd() {
        return bankImd;
    }

    public void setBankImd(String bankImd) {
        this.bankImd = bankImd;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setAccountType(i8SBSwitchControllerRequestVO.getAccountType());
        this.setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.setCnicIssuanceDate(i8SBSwitchControllerRequestVO.getCnicIssuanceDate());
        this.setBankImd(i8SBSwitchControllerRequestVO.getBankImd());
        this.setMsisdn(i8SBSwitchControllerRequestVO.getMsisdn());
        this.setReserved1(i8SBSwitchControllerRequestVO.getReserved1());
        this.setReserved2(i8SBSwitchControllerRequestVO.getReserved2());
        this.setReserved3(i8SBSwitchControllerRequestVO.getReserved3());
        this.setReserved4(i8SBSwitchControllerRequestVO.getReserved4());
        this.setDateTime(i8SBSwitchControllerRequestVO.getDateTime());
        this.setTraceNo(i8SBSwitchControllerRequestVO.getTraceNo());
    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
//        if (StringUtils.isEmpty(this.getAccountType())) {
//            throw new I8SBValidationException("[Failed] accountType is null" + this.getAccountType());
//        }
//        if (StringUtils.isEmpty(this.getCnic())) {
//            throw new I8SBValidationException("[Failed] CNIC is null" + this.getCnic());
//        }
//        if (StringUtils.isEmpty(this.getCnicIssuanceDate())) {
//            throw new I8SBValidationException("[Failed] CnicIssuanceDate is null" + this.getCnicIssuanceDate());
//        }
//        if (StringUtils.isEmpty(this.getBankImd())){
//            throw new I8SBValidationException("[Failed] BankImb is null"+this.getBankImd());
//        }
//        if (StringUtils.isEmpty(this.getMsisdn())){
//            throw new I8SBValidationException("[Failed] Msisdn is null"+this.getMsisdn());
//        }
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
