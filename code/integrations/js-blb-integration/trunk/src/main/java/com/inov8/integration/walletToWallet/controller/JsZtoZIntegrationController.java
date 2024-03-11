package com.inov8.integration.walletToWallet.controller;

import com.inov8.integration.webservice.controller.ZtoZSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller("JsZtoZIntegrationController")
public class JsZtoZIntegrationController implements ZtoZSwitchController {
    private static Logger logger = LoggerFactory.getLogger(JsZtoZIntegrationController.class.getSimpleName());


    @Override
    public WebServiceVO ztoZInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO ztoZPayment(WebServiceVO webServiceVO) {
        return null;
    }
}
