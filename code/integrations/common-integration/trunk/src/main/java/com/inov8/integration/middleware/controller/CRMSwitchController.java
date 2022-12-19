package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.CRMMessageVO;

public interface CRMSwitchController {
    CRMMessageVO queryBVSStatus(CRMMessageVO var1);

    CRMMessageVO checkSubscriber(CRMMessageVO var1);

    CRMMessageVO customerProfileQuery(CRMMessageVO var1);

    CRMMessageVO getCustomerDetail(CRMMessageVO var1);

    CRMMessageVO getLogin(CRMMessageVO var1);
}
