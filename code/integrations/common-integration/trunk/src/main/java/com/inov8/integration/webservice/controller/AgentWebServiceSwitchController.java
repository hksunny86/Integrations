package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

public interface AgentWebServiceSwitchController {

    WebServiceVO agentAccountLogin(WebServiceVO webServiceVO);

    WebServiceVO agentLoginPinGeneration(WebServiceVO webServiceVO);

    WebServiceVO agentLoginPinReset(WebServiceVO webServiceVO);

    WebServiceVO agentMpinGeneration(WebServiceVO webServiceVO);

    WebServiceVO agentMpinReset(WebServiceVO webServiceVO);

    WebServiceVO agentProductCatalogs(WebServiceVO webServiceVO);
}
