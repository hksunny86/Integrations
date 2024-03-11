package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface ZtoZSwitchController {

    WebServiceVO ztoZInquiry(WebServiceVO webServiceVO);
    WebServiceVO ztoZPayment(WebServiceVO webServiceVO);
}
