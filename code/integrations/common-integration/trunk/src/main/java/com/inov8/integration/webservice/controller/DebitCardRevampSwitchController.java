package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface DebitCardRevampSwitchController {

    WebServiceVO debitCardFee(WebServiceVO webServiceVO);
    WebServiceVO debitCardDiscrepant(WebServiceVO webServiceVO);

}
