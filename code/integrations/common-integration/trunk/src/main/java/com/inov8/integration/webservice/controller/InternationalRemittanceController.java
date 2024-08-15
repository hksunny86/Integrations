package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface InternationalRemittanceController {

    WebServiceVO TitleFetchV2(WebServiceVO webServiceVO);
    WebServiceVO CoreToWalletCredit(WebServiceVO webServiceVO);

}
