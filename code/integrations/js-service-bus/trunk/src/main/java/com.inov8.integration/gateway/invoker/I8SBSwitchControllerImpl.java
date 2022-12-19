package com.inov8.integration.gateway.invoker;

import com.inov8.integration.controller.I8SBFacade;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.enums.I8SBTransactionStatus;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * Created by inov8 on 8/28/2017.
 */
@Controller
public class I8SBSwitchControllerImpl implements I8SBSwitchController {

    private static Logger logger = LoggerFactory.getLogger(I8SBSwitchControllerImpl.class.getSimpleName());
    @Autowired
    I8SBFacade i8SBFacade;

    public Object invoke(Object obj) throws RuntimeException {
        logger.info("I8SB received request on Invoker gateway");
        logger.info("I8SB received request at "+ new Date());
        I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO = null;
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = null;
        if (obj != null && obj instanceof I8SBSwitchControllerRequestVO) {
            i8SBSwitchControllerRequestVO = (I8SBSwitchControllerRequestVO) obj;
            i8SBSwitchControllerRequestVO.setI8sbGateway(I8SBConstants.I8SB_Gateway_Invoker);
            i8SBSwitchControllerRequestVO.setI8SBSwitchControllerResponseVO(null);
            i8SBSwitchControllerResponseVO = i8SBFacade.process(i8SBSwitchControllerRequestVO);
        } else {
            logger.info("[FAILED] Switch controller request VO object is null or object type not supported");
            i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
            i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.NOT_PROCESSED.getValue());
            i8SBSwitchControllerResponseVO.setStatus(I8SBTransactionStatus.REJECTED.getValue());
            i8SBSwitchControllerResponseVO.setError("Switch controller request VO object is null or object type not supported");
        }
        i8SBSwitchControllerRequestVO.setI8SBSwitchControllerResponseVO(i8SBSwitchControllerResponseVO);
        logger.info("I8SB response sent back for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        logger.info("I8SB sending response at "+ new Date());
        return i8SBSwitchControllerRequestVO;
    }
}
