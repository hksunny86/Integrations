package com.inov8.integration.internationalRemittance.controller;

import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.controller.InternationalRemittanceController;
import com.inov8.integration.webservice.vo.WebServiceVO;

public class JsInternationalRemittanceIntegrationController implements InternationalRemittanceController {


    @Override
    public WebServiceVO titleFetchV2(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO coreToWalletCredit(WebServiceVO webServiceVO) {
        return null;
    }
}
