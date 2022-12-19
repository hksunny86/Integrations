package com.inov8.integration.channel.APIGEE.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

/**
 * Created by inov8 on 5/28/2018.
 */
public abstract class Response {

    public abstract I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException;

}
