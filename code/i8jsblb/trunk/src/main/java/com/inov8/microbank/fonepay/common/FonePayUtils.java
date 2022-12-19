package com.inov8.microbank.fonepay.common;

import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.MessageUtil;

public class FonePayUtils {

    public static FonePayMessageVO prepareErrorResponse(FonePayMessageVO messageVO, String errorCode) {
        String errorMessage = getResponceCodeDescription(errorCode);
        CommonUtils.logger.info("prepareErrorResponse(FonePayMessageVO) => Resp Code:" + errorCode + " ,Message:" + errorMessage);

        messageVO.setResponseCode(errorCode);
        messageVO.setResponseCodeDescription(errorMessage);
        messageVO.setAccountStatus(FonePayConstants.CUSTOMER_STATUS_INVALID);

        return messageVO;
    }

    public static WebServiceVO prepareErrorResponse(WebServiceVO messageVO, String errorCode) {
        String errorMessage = getResponceCodeDescription(errorCode);
        CommonUtils.logger.info("prepareErrorResponse(WebServiceVO) => Resp Code:" + errorCode + " ,Message:" + errorMessage);

        messageVO.setResponseCode(errorCode);
        messageVO.setResponseCodeDescription(errorMessage);

        return messageVO;
    }

    public static String getResponceCodeDescription(String errorCode) {
        return MessageUtil.getMessage("fonepay.error." + errorCode);
    }

    public static MiddlewareMessageVO prepareErrorResponseForDebitCard(MiddlewareMessageVO messageVO, String errorCode) {
        String errorMessage = getResponceCodeDescription(errorCode);
        CommonUtils.logger.info("prepareErrorResponse(MiddlewareMessageVO) => Resp Code:" + errorCode + " ,Message:" + errorMessage);
        messageVO.setResponseCode(errorCode);
        messageVO.setResponseDescription(errorMessage);
        return messageVO;
    }
}