package com.inov8.integration.channel.fonepay.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by inov8 on 8/28/2017.
 */
public abstract class Response {

    protected static final Logger logger = LoggerFactory.getLogger(Response.class.getSimpleName());


    // abstract methods needs to be implemented by all responses

    public abstract I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException;


    public abstract String toString();
}
