package com.inov8.integration.middleware.controller.validation;

import com.inov8.integration.middleware.controller.NadraIntegrationControllerImpl;
import com.inov8.integration.middleware.service.IntegrationService;
import com.inov8.integration.vo.NadraIntegrationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by inov8 on 9/30/2016.
 */

@Controller("nadraWebController")
public class NadraWebController {

    private static Logger logger = LoggerFactory.getLogger(NadraWebController.class.getSimpleName());
    @Autowired
    private IntegrationService integrationService;
    @RequestMapping(value = "/fingerPrintVerification", method = RequestMethod.POST)
    public ModelAndView fingerPrintVerify(@RequestParam String citizenNumber, @RequestParam String fingerPrintTemplate) throws RuntimeException {
        long start = System.currentTimeMillis();
        NadraIntegrationVO messageVO = new NadraIntegrationVO();
        messageVO.setCitizenNumber(citizenNumber);
        logger.info("Start Processing Finger Print Verification for User :", messageVO.getUserName());
        try {
            logger.info("Validate Finger Print Verification Request");
//            RequestValidator.validateFingerPrintVerification(messageVO);
            messageVO = integrationService.fingerPrintVerification(messageVO);
        } catch (ValidationException e) {
            messageVO.setResponseCode("420");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("Validation ERROR: ", e);
        } catch (Exception e) {
            messageVO.setResponseCode("220");
            messageVO.setResponseDescription(e.getMessage());
            logger.error("ERROR: General Processing ", e);
        }
        logger.info("******* DEBUG Logs For Finger Print Verification *********");
        logger.info("ResponseCode: " + messageVO.getResponseCode());
        long end = System.currentTimeMillis() - start;
        logger.debug("Finger Print Verification Request  Processed in : {} ms ", end);
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/nadra-biometric-integration", method = RequestMethod.GET)
    public ModelAndView homePage(@ModelAttribute("command") NadraIntegrationVO messageVO) {
        return new ModelAndView("index");


    }

}
