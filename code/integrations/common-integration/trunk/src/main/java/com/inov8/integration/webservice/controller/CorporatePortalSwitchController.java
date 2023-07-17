package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface CorporatePortalSwitchController {

    WebServiceVO login(WebServiceVO webServiceVO);
    WebServiceVO accountBlockUnblockInquiry(WebServiceVO webServiceVO);
    WebServiceVO accountBlockUnblock(WebServiceVO webServiceVO);
    WebServiceVO mpinResetInquiry(WebServiceVO webServiceVO);
    WebServiceVO mpinReset(WebServiceVO webServiceVO);
    WebServiceVO customerAccountStatementInquiry(WebServiceVO webServiceVO);
    WebServiceVO customerAccountStatement(WebServiceVO webServiceVO);
    WebServiceVO deviceVerificationInquiry(WebServiceVO webServiceVO);
    WebServiceVO deviceVerification(WebServiceVO webServiceVO);

}
