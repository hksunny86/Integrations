package com.inov8.integration.channel.microbank.response.AMAResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

/**
 * Created by Inov8 on 1/2/2020.
 */
public class BalanceInquiryResponse extends Response {
    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        return null;
    }
}
