package com.inov8.integration.controller;

import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.server.webservice.USSDRequestHandlerDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Logger;

public class I8SBSwitchControllerImpl implements I8SBSwitchController {

    USSDRequestHandlerDelegate ussdRequestHandlerDelegate;
    protected final Log logger = LogFactory.getLog(getClass());
    public Object invoke(Object obj) throws RuntimeException {

        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        logger.info("[I8SBSwitchControllerImpl] Request Received at Microbank");

        if (obj != null && obj instanceof I8SBSwitchControllerRequestVO) {
            i8SBSwitchControllerRequestVO = (I8SBSwitchControllerRequestVO) obj;
            i8SBSwitchControllerRequestVO.setI8SBSwitchControllerResponseVO(null);
            logger.info("USSDRequestString"+ i8SBSwitchControllerRequestVO.getuSSDRequestString());
            logger.info("USSD Mobile No"+ i8SBSwitchControllerRequestVO.getMobileNumber());
            logger.info("USSD Service Code" + i8SBSwitchControllerRequestVO.getuSSDServiceCode());
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            if(requestType.equals(I8SBConstants.RequestType_JSBLB_SEO_USSD))
            {
                try {
                    i8SBSwitchControllerResponseVO = (I8SBSwitchControllerResponseVO) ussdRequestHandlerDelegate.handlerUSSDRequest(i8SBSwitchControllerRequestVO);

                    logger.info("USSD Response String" + i8SBSwitchControllerResponseVO.getuSSDResponseString());
                    logger.info("USSD Action" + i8SBSwitchControllerResponseVO.getuSSDAction());

                }
                catch (Exception e) {
                    logger.error("Exception at [I8SBSwitchControllerImpl]" +e.getMessage());
                }

            }

        }
        else {
            i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
            i8SBSwitchControllerResponseVO.setError("Switch controller request VO object is null or object type not supported");
            logger.error("Switch controller request VO object is null or object type not supported");
        }

        i8SBSwitchControllerRequestVO.setI8SBSwitchControllerResponseVO(i8SBSwitchControllerResponseVO);


        return i8SBSwitchControllerRequestVO;

    }

    public USSDRequestHandlerDelegate getUssdRequestHandlerDelegate() {
        return ussdRequestHandlerDelegate;
    }

    public void setUssdRequestHandlerDelegate(USSDRequestHandlerDelegate ussdRequestHandlerDelegate) {
        this.ussdRequestHandlerDelegate = ussdRequestHandlerDelegate;
    }
}
