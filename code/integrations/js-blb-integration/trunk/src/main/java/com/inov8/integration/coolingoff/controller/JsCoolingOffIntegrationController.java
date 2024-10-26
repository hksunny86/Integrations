package com.inov8.integration.coolingoff.controller;

import com.inov8.integration.webservice.controller.CoolingOffHostSwitchController;
import com.inov8.integration.webservice.controller.CorporatePortalSwitchController;
import com.inov8.integration.webservice.vo.WebServiceVO;

public class JsCoolingOffIntegrationController implements CoolingOffHostSwitchController {
    @Override
    public WebServiceVO releaseIBFTAmount(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO toggleNotification(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO releaseCoolingOffAmount(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO releaseInProcessBalance(WebServiceVO webServiceVO) {
        return null;
    }
}
