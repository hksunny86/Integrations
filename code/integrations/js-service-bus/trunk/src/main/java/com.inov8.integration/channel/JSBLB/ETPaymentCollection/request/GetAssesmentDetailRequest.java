package com.inov8.integration.channel.JSBLB.ETPaymentCollection.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Inov8 on 10/15/2019.
 */

@XmlRootElement(name = "GetAssesmentDetail")
public class GetAssesmentDetailRequest extends Request {

    @javax.xml.bind.annotation.XmlElement(name = "RegisterationNumber")
    private String registerationNumber;
    @javax.xml.bind.annotation.XmlElement(name = "ChesisNumber")
    private String chesisNumber;
    @XmlElement(name = "bankMnemonic")
    private String bankMnemonic;

    public String getRegisterationNumber() {
        return registerationNumber;
    }

    public void setRegisterationNumber(String registerationNumber) {
        this.registerationNumber = registerationNumber;
    }

    public String getChesisNumber() {
        return chesisNumber;
    }

    public void setChesisNumber(String chesisNumber) {
        this.chesisNumber = chesisNumber;
    }

    public String getBankMnemonic() {
        return bankMnemonic;
    }

    public void setBankMnemonic(String bankMnemonic) {
        this.bankMnemonic = bankMnemonic;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setRegisterationNumber(i8SBSwitchControllerRequestVO.getVehicleRegistrationNumber());
        this.setChesisNumber(i8SBSwitchControllerRequestVO.getChesisNumber());
        this.setBankMnemonic(i8SBSwitchControllerRequestVO.getBankMnemonic());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        if (StringUtils.isEmpty(this.getRegisterationNumber())) {
            throw new I8SBValidationException("[Failed] Registration Number:" + this.getRegisterationNumber());
        }
        if (StringUtils.isEmpty(this.getChesisNumber())) {
            throw new I8SBValidationException("[Failed] Chesis Number:" + this.getChesisNumber());
        }
        if (StringUtils.isEmpty(this.getBankMnemonic())){
            throw new I8SBValidationException("[Failed] Bank Mnemonic:" + this.getBankMnemonic());
        }
        return true;
    }
}
