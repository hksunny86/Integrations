package com.inov8.integration.debitCard.controller;

import com.inov8.integration.webservice.controller.DebitCardRevampSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller("JsDebitCardIntegrationController")
public class JsDebitCardIntegrationController implements DebitCardRevampSwitchController {
    private static Logger logger = LoggerFactory.getLogger(JsDebitCardIntegrationController.class.getSimpleName());

    @Override
    public WebServiceVO debitCardFee(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO debitCardDiscrepant(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO appRebrandDebitCardIssuanceInquiry(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO appRebrandDebitCardIssuance(WebServiceVO webServiceVO) {
        return null;
    }
}
