package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface CoolingOffHostSwitchController {
    WebServiceVO releaseIBFTAmount(WebServiceVO webServiceVO);
    WebServiceVO toggleNotification(WebServiceVO webServiceVO);
    WebServiceVO releaseInProcessBalance(WebServiceVO webServiceVO);

}
