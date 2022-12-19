package com.inov8.integration.channel.vrg.echallan.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Response {

    protected static final Logger logger = LoggerFactory.getLogger(com.inov8.integration.channel.vrg.
            echallan.response.Response.class.getSimpleName());

    @XmlElement(name = "return")
    private String returnedString;

    public String getReturnedString() { return returnedString; }

    public String setReturnedString(String returnedString) { return this.returnedString = returnedString; }


    // abstract methods needs to be implemented by all responses

    public abstract I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException;

    public abstract void parseResponse(I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO);

    public abstract String toString();
}
