package com.inov8.integration.petroPocket.controller;

import com.inov8.integration.webservice.controller.CorporatePortalSwitchController;
import com.inov8.integration.webservice.controller.PetroSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller("JsPetroIntegrationController")
public class JsPetroIntegrationController implements PetroSwitchController {
    private static Logger logger = LoggerFactory.getLogger(JsPetroIntegrationController.class.getSimpleName());

    @Override
    public WebServiceVO petroInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO petroPayment(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO petroWalletToWalletInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO petroWalletToWalletPayment(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO petroBalanceInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO walletToPetroInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO walletToPetroPayment(WebServiceVO webServiceVO) {
        return null;
    }
}
