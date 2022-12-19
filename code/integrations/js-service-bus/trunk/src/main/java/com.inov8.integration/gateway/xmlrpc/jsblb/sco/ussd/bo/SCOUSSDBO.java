package com.inov8.integration.gateway.xmlrpc.jsblb.sco.ussd.bo;

import com.inov8.integration.controller.I8SBFacade;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by inov8 on 4/4/2018.
 */

@Component
public class SCOUSSDBO {

    private static Logger logger = LoggerFactory.getLogger(SCOUSSDBO.class.getSimpleName());
    @Autowired
    I8SBFacade i8SBFacade;


    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerRequestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_I8SB);
        i8SBSwitchControllerRequestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
        i8SBSwitchControllerRequestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_MICROBANK);
        i8SBSwitchControllerRequestVO.setI8sbGateway(I8SBConstants.I8SB_Gateway_XMLRPC);
        i8SBSwitchControllerRequestVO.setRequestType(I8SBConstants.RequestType_JSBLB_SEO_USSD);

        i8SBSwitchControllerResponseVO = i8SBFacade.process(i8SBSwitchControllerRequestVO);

        return i8SBSwitchControllerResponseVO;
    }


}
