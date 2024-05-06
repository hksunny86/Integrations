package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface L2AccountSwitchController {

    WebServiceVO l2Account(WebServiceVO webServiceVO);
    WebServiceVO l2AccountFields(WebServiceVO webServiceVO);
    WebServiceVO updatePmd(WebServiceVO webServiceVO);
    WebServiceVO rateConversion(WebServiceVO webServiceVO);
    WebServiceVO freelanceToWalletInquiry(WebServiceVO webServiceVO);
    WebServiceVO freelanceToWallet(WebServiceVO webServiceVO);
    WebServiceVO freelancerFCYFunds(WebServiceVO webServiceVO);
    WebServiceVO freelancerBalanceInquiry(WebServiceVO webServiceVO);
    WebServiceVO l2AccountStatus(WebServiceVO webServiceVO);
    WebServiceVO motherNames(WebServiceVO webServiceVO);

}
