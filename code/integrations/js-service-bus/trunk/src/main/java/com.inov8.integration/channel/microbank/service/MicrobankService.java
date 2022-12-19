package com.inov8.integration.channel.microbank.service;

//import com.inov8.integration.channel.microbank.response.scoresponse.ScoUssdResponse;
import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBServiceNotAvailableException;
import com.inov8.integration.gateway.invoker.I8SBSwitchControllerImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.controller.I8SBSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by inov8 on 4/20/2018.
 */

@Service
public class MicrobankService {
    private static Logger logger = LoggerFactory.getLogger(MicrobankService.class.getSimpleName());
    private String microbankUrl = PropertyReader.getProperty("microbank.url/");
    I8SBSwitchController controller;

    public I8SBSwitchControllerResponseVO sendRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        long startTime = new Date().getTime(); // start time
        String rrn = i8SBSwitchControllerRequestVO.getRRN();
        controller = CommonUtils.getFromProxy(microbankUrl); //microbank url

        logger.info("Sending request to Microbank server for RRN: " + rrn);
        try {
            i8SBSwitchControllerRequestVO = (I8SBSwitchControllerRequestVO) controller.invoke(i8SBSwitchControllerRequestVO);
        } catch (Exception e) {
            logger.debug("Microbank is not connected" + e.getMessage());
            throw new I8SBServiceNotAvailableException("Microbank is not connected");
        }

        logger.info("Request sent to Microbank server for RRN: " + rrn);
        long endTime = new Date().getTime(); // end time
        long difference = endTime - startTime; // check different
        logger.debug("Response time from Microbank is " + difference + " milliseconds");
        i8SBSwitchControllerResponseVO = i8SBSwitchControllerRequestVO.getI8SBSwitchControllerResponseVO();
        return i8SBSwitchControllerResponseVO;
    }

}
