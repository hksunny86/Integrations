package com.inov8.integration.channel.JSBLB.ETPaymentCollection.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Inov8 on 10/17/2019.
 */
public class GetchallanStatusRequest extends Request {
    private String challanNumber;
    private String vehicleRegistrationNumber;
    @XmlElement(name = "bankMnemonic")
    private String bankMnemonic;

    public String getBankMnemonic() {
        return bankMnemonic;
    }

    public void setBankMnemonic(String bankMnemonic) {
        this.bankMnemonic = bankMnemonic;
    }

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setVehicleRegistrationNumber(i8SBSwitchControllerRequestVO.getVehicleRegistrationNumber());
        this.setChallanNumber(i8SBSwitchControllerRequestVO.getChallanNumber());
        this.setBankMnemonic(i8SBSwitchControllerRequestVO.getBankMnemonic());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getVehicleRegistrationNumber())) {
            throw new I8SBValidationException("[Failed] VehicleRegistrationNumber:" + this.getVehicleRegistrationNumber());
        }
        if (StringUtils.isEmpty(this.getChallanNumber())) {
            throw new I8SBValidationException("[Failed] ChallanNumber:" + this.getChallanNumber());
        }
        if (StringUtils.isEmpty(this.getBankMnemonic())) {
            throw new I8SBValidationException("[Failed] Bank Mnemonic:" + this.getBankMnemonic());
        }
        return true;
    }
}

