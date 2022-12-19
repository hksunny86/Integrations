package com.inov8.integration.channel.fonepay.request;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by inov8 on 8/28/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Request {
    @XmlElement(name = "UserID")
    private String userID;
    @XmlElement(name = "Password")
    private String password;
    @XmlTransient
    private String terminalID;
    @XmlTransient
    private I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO;

    public Request() {
    }

    public Request(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
        this.setUserID(i8SBSwitchControllerRequestVO.getUserId());
        this.setPassword(i8SBSwitchControllerRequestVO.getPassword());
        this.setTerminalID(i8SBSwitchControllerRequestVO.getI8sbClientTerminalID());
    }


    // abstract methods needs to be implemented by all requests
    public abstract void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO);


    public abstract boolean validateRequest() throws I8SBValidationException;


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


    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public I8SBSwitchControllerRequestVO getI8SBSwitchControllerRequestVO() {
        return i8SBSwitchControllerRequestVO;
    }

    public void setI8SBSwitchControllerRequestVO(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.i8SBSwitchControllerRequestVO = i8SBSwitchControllerRequestVO;
    }
}
