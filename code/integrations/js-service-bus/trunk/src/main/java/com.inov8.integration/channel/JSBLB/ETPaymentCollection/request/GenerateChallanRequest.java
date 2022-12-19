package com.inov8.integration.channel.JSBLB.ETPaymentCollection.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.inov8.integration.enums.DateFormatEnum.DATE_LOCAL_TRANSACTION;
import static com.inov8.integration.enums.DateFormatEnum.TIME_LOCAL_TRANSACTION;

/**
 * Created by Inov8 on 10/17/2019.
 */
@XmlRootElement
public class GenerateChallanRequest extends Request {
    @javax.xml.bind.annotation.XmlElement(name = "AssesementNumber")
    private String assesmentNumber;
    @javax.xml.bind.annotation.XmlElement(name = "VehicleRegistrationNumber")
    private String vehicleRegistrationNumber;
    @javax.xml.bind.annotation.XmlElement(name = "AssesementTotalAmount")
    private String assesmentTotalAmount;
    @javax.xml.bind.annotation.XmlElement(name = "ChallanStatus")
    private String challanStatus;
    @XmlElement(name = "bankMnemonic")
    private String bankMnemonic;
    @XmlElement(name = "authId")
    private String authId;
    @XmlElement(name = "TranDate")
    private String TranDate;
    @XmlElement(name = "TranTime")
    private String TranTime;
    private String customerMobileNumeber;

    public String getAssesmentNumber() {
        return assesmentNumber;
    }

    public void setAssesmentNumber(String assesmentNumber) {
        this.assesmentNumber = assesmentNumber;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getAssesmentTotalAmount() {
        return assesmentTotalAmount;
    }

    public void setAssesmentTotalAmount(String assesmentTotalAmount) {
        this.assesmentTotalAmount = assesmentTotalAmount;
    }

    public String getBankMnemonic() {
        return bankMnemonic;
    }

    public void setBankMnemonic(String bankMnemonic) {
        this.bankMnemonic = bankMnemonic;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getTranDate() {
        return TranDate;
    }

    public void setTranDate(String tranDate) {
        TranDate = tranDate;
    }

    public String getTranTime() {
        return TranTime;
    }

    public void setTranTime(String tranTime) {
        TranTime = tranTime;
    }

    public String getChallanStatus() {
        return challanStatus;
    }

    public void setChallanStatus(String challanStatus) {
        this.challanStatus = challanStatus;
    }

    public String getCustomerMobileNumeber() {
        return customerMobileNumeber;
    }

    public void setCustomerMobileNumeber(String customerMobileNumeber) {
        this.customerMobileNumeber = customerMobileNumeber;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setAssesmentNumber(i8SBSwitchControllerRequestVO.getAssessmentNumber());
        this.setAssesmentTotalAmount(i8SBSwitchControllerRequestVO.getAssesmentTotalAmount());
        this.setVehicleRegistrationNumber(i8SBSwitchControllerRequestVO.getVehicleRegistrationNumber());
        this.setChallanStatus(i8SBSwitchControllerRequestVO.getChallanStatus());
        this.setBankMnemonic(i8SBSwitchControllerRequestVO.getBankMnemonic());
        this.setAuthId(i8SBSwitchControllerRequestVO.getTransactionId());
        this.setTranDate(DateUtil.formatCurrentDate(DATE_LOCAL_TRANSACTION.getValue()));
        this.setTranTime(DateUtil.formatCurrentDate(TIME_LOCAL_TRANSACTION.getValue()));
        this.setCustomerMobileNumeber(i8SBSwitchControllerRequestVO.getMobileNumber());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getAssesmentNumber())) {
            throw new I8SBValidationException("[Failed] AssesmentNumber:" + this.getAssesmentNumber());
        }
        if (StringUtils.isEmpty(this.getVehicleRegistrationNumber())) {
            throw new I8SBValidationException("[Failed] VehicleRegistrationNumber:" + this.getVehicleRegistrationNumber());
        }
        if (StringUtils.isEmpty(this.getAssesmentTotalAmount())) {
            throw new I8SBValidationException("[Failed] AssesmentTotalAmount:" + this.getAssesmentTotalAmount());
        }
        if (StringUtils.isEmpty(this.getChallanStatus())) {
            throw new I8SBValidationException("[Failed] ChallanStatus:" + this.getChallanStatus());
        }
        if (StringUtils.isEmpty(this.getBankMnemonic())){
            throw new I8SBValidationException(("[Failed] Bank Mnemonics"+this.getBankMnemonic()));

        }
        if (StringUtils.isEmpty(this.getTranDate())){
            throw new I8SBValidationException(("[Failed] TransactionDate"+this.getTranDate()));
        }
        if (StringUtils.isEmpty(this.getAuthId())){
            throw new I8SBValidationException(("[Failed] AuthId"+this.getAuthId()));
        }
        if(StringUtils.isEmpty(this.getTranTime())){
            throw new I8SBValidationException(("[Failed] TransactionTime"+this.getTranTime()));
        }
        if(StringUtils.isEmpty(this.getCustomerMobileNumeber())){
            throw new I8SBValidationException("[Failed] CustomerMobileNumber"+this.getCustomerMobileNumeber());

        }
        return true;
    }
}
