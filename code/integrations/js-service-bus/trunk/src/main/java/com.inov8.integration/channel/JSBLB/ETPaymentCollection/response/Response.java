package com.inov8.integration.channel.JSBLB.ETPaymentCollection.response;

import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.*;
import com.inov8.integration.channel.JSBLB.ETPaymentCollection.client.GetAssesmentDetailResponse;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

/**
 * Created by Inov8 on 10/15/2019.
 */
public abstract class Response {

    public abstract I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException;

}

