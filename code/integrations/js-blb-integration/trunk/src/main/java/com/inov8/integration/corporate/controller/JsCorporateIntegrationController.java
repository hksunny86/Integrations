package com.inov8.integration.corporate.controller;

import com.inov8.integration.webservice.controller.CorporatePortalSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller("JsCorporateIntegrationController")
public class JsCorporateIntegrationController implements CorporatePortalSwitchController {
    private static Logger logger = LoggerFactory.getLogger(JsCorporateIntegrationController.class.getSimpleName());

    @Override
    public WebServiceVO login(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO accountBlockUnblockInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO accountBlockUnblock(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO mpinResetInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO mpinReset(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO customerAccountStatementInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO customerAccountStatement(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO deviceVerificationInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO deviceVerification(WebServiceVO webServiceVO) {
        return null;
    }
}
