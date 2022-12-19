package com.inov8.integration.channel.vrg.echallan.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Request {

    @XmlElement(name = "userName")
    private String userID;
    @XmlElement(name = "password")
    private String password;
    @XmlElement(name = "consumerNumber")
    private String consumerNumber;
    @XmlElement(name = "bankMnemonic")
    private String bankMnemonic;
    @XmlElement(name = "reserved")
    private String reserved;
    @XmlTransient
    private I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    private static final int USERID_AND_PASSWORD_MAX_LENGTH = 60;
    private static final int CONSUMER_NUMBER_MAX_LENGTH = 24;
    private static final int BANK_MNEMONIC_MAX_LENGTH = 8;


    public Request() {

    }

    public Request(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
        this.setUserID(i8SBSwitchControllerRequestVO.getUserId());
        this.setPassword(i8SBSwitchControllerRequestVO.getPassword());
        this.setBankMnemonic(i8SBSwitchControllerRequestVO.getBankMnemonic());
        this.setConsumerNo(i8SBSwitchControllerRequestVO.getConsumerNumber());

    }


    // abstract methods needs to be implemented by all requests
    public abstract void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO);

    public abstract boolean validateRequest() throws I8SBValidationException;


    public boolean validateCommonRequestAttributes() throws I8SBValidationException {

        if (StringUtils.isEmpty(this.getUserID()) ||
                this.getUserID().length() > USERID_AND_PASSWORD_MAX_LENGTH) {
            throw new I8SBValidationException("[FAILED] Validation Failed Username: "
                    + this.getUserID());
        }

        if (StringUtils.isEmpty(this.getPassword()) ||
                this.getPassword().length() > USERID_AND_PASSWORD_MAX_LENGTH) {
            throw new I8SBValidationException("[FAILED] Validation Failed Password: "
                    + this.getPassword());
        }

        if (StringUtils.isEmpty(this.getConsumerNo()) ||
                this.getConsumerNo().length() > CONSUMER_NUMBER_MAX_LENGTH) {
            throw new I8SBValidationException("[FAILED] Validation Failed Consumer Number: "
                    + this.getConsumerNo());
        }

        if (StringUtils.isEmpty(this.getBankMnemonic()) ||
                this.getBankMnemonic().length() > BANK_MNEMONIC_MAX_LENGTH) {
            throw new I8SBValidationException("[FAILED] Validation Failed Bank Mnemonic: " +
                    this.getBankMnemonic());
        }

        return true;
    }


    public abstract String toString();


    /**
     * Gets the value of the userID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    public String getConsumerNo() {
        return consumerNumber;
    }

    public void setConsumerNo(String consumerNo) {
        this.consumerNumber = consumerNo;
    }

    public String getBankMnemonic() {
        return bankMnemonic;
    }

    public void setBankMnemonic(String bankMnemonic) {
        this.bankMnemonic = bankMnemonic;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }


    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }

}
