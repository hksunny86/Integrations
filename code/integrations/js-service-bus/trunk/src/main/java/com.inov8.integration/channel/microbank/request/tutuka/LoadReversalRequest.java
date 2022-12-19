package com.inov8.integration.channel.microbank.request.tutuka;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

/**
 * Created by inov8 on 4/23/2018.
 */
public class LoadReversalRequest extends Request {

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTerminalID())) {
            throw new I8SBValidationException("[FAILED] Terminal Id validation: " + i8SBSwitchControllerRequestVO.getTerminalID());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getMobilePhone())) {
            throw new I8SBValidationException("[FAILED] Mobile number validation: " + i8SBSwitchControllerRequestVO.getMobilePhone());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionAmount())) {
            throw new I8SBValidationException("[FAILED] Transaction amount validation: " + i8SBSwitchControllerRequestVO.getTransactionAmount());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getNarrative())) {
            throw new I8SBValidationException("[FAILED] narrative validation: " + i8SBSwitchControllerRequestVO.getNarrative());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionData())) {
            throw new I8SBValidationException("[FAILED] TransactionData validation: " + i8SBSwitchControllerRequestVO.getTransactionData());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getOriginalTxnID())) {
            throw new I8SBValidationException("[FAILED] Reference Id validation: " + i8SBSwitchControllerRequestVO.getOriginalTxnID());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getOriginalTxnDateTime())) {
            throw new I8SBValidationException("[FAILED] Reference date validation: " + i8SBSwitchControllerRequestVO.getOriginalTxnDateTime());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionId())) {
            throw new I8SBValidationException("[FAILED] Transaction Id validation: " + i8SBSwitchControllerRequestVO.getTransactionId());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionDateTime())) {
            throw new I8SBValidationException("[FAILED] Transaction Date validation: " + i8SBSwitchControllerRequestVO.getTransactionDateTime());
        }
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }


}
