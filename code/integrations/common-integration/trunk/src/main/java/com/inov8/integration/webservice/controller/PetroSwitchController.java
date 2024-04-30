package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface PetroSwitchController {

    WebServiceVO petroInquiry(WebServiceVO webServiceVO);
    WebServiceVO petroPayment(WebServiceVO webServiceVO);
    WebServiceVO petroWalletToWalletInquiry(WebServiceVO webServiceVO);
    WebServiceVO petroWalletToWalletPayment(WebServiceVO webServiceVO);
    WebServiceVO petroBalanceInquiry(WebServiceVO webServiceVO);

}
