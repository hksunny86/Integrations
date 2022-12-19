package com.inov8.integration.channel.microbank.request.bopBlb;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.*;

/**
 * Created by Administrator on 2/4/2020.
 */
import javax.xml.bind.annotation.*;

@XmlType(name = "")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "GetBalanceInquiryServiceRequest")
public class BOPBalanceInquiryRequest extends Request {

    @XmlElement(name = "msisdn")
    private String msisdn;
    @XmlElement(name = "accountNo")
    private String accountNo;
    @XmlElement(name = "mPin")
    private String mPin;
    @XmlElement(name = "segmentID")
    private String segmentID;
    @XmlElement(name = "dateTime")
    private String dateTime;
    @XmlElement(name = "RRN")
    private String rrn;
    @XmlElement(name = "reserved1")
    private String reserved1;
    @XmlElement(name = "reserved2")
    private String reserved2;
    @XmlElement(name = "reserved3")
    private String reserved3;
    @XmlElement(name = "reserved4")
    private String reserved4;


    public String getSegmentID() {
        return segmentID;
    }

    public void setSegmentID(String segmentID) {
        this.segmentID = segmentID;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getmPin() {
        return mPin;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
