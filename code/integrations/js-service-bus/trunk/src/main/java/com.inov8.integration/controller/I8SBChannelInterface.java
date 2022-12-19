package com.inov8.integration.controller;

import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
public interface I8SBChannelInterface {
    I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception;

    I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO);

    I8SBSwitchControllerRequestVO generateSystemTraceableInfo (I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception;

}
