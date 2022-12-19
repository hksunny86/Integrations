package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.MoneySendIntegrationVO;

public interface IMoneySendSwitchController {

    MoneySendIntegrationVO paymentRequest(MoneySendIntegrationVO moneySendIntegrationVO);

    MoneySendIntegrationVO transferReversalRequest(MoneySendIntegrationVO moneySendIntegrationVO);

}
