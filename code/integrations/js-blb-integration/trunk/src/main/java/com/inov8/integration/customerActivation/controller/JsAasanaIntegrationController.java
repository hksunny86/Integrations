package com.inov8.integration.customerActivation.controller;

import com.inov8.integration.webservice.controller.AasanaSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller("JsAasanaIntegrationController")
public class JsAasanaIntegrationController implements AasanaSwitchController {
    private static Logger logger = LoggerFactory.getLogger(JsAasanaIntegrationController.class.getSimpleName());

    @Override
    public WebServiceVO customerActivation(WebServiceVO webServiceVO) {
        return null;
    }
}
