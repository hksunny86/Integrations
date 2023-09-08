package com.inov8.integration.l2Account.controller;

import com.inov8.integration.webservice.controller.CorporatePortalSwitchController;
import com.inov8.integration.webservice.controller.L2AccountSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller("JsL2AccountIntegrationController")
public class JsL2AccountIntegrationController implements L2AccountSwitchController {
    private static Logger logger = LoggerFactory.getLogger(JsL2AccountIntegrationController.class.getSimpleName());

    @Override
    public WebServiceVO l2Account(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO l2AccountFields(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO updatePmd(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO rateConversion(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO freelanceToWalletInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO freelanceToWallet(WebServiceVO webServiceVO) {
        return null;
    }
}
