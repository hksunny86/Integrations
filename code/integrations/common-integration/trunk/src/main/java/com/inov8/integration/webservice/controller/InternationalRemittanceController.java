package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface InternationalRemittanceController {

    WebServiceVO titleFetchV2(WebServiceVO webServiceVO);
    WebServiceVO coreToWalletCredit(WebServiceVO webServiceVO);

}
