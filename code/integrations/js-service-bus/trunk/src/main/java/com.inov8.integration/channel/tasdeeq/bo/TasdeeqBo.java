package com.inov8.integration.channel.tasdeeq.bo;

import com.inov8.integration.channel.tasdeeq.request.*;
import com.inov8.integration.channel.tasdeeq.response.*;
import com.inov8.integration.channel.tasdeeq.service.TasdeeqService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.enums.DateFormatEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.DateUtil;
import com.inov8.integration.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TasdeeqBo implements I8SBChannelInterface {

    @Autowired
    TasdeeqService tasdeeqService;
    private static Logger logger = LoggerFactory.getLogger(TasdeeqBo.class.getSimpleName());

    @Override
    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType());
        Request request = null;
        Response response = null;
        if (objects[0] != null) {
            request = (Request) objects[0];
        }
        if (objects[1] != null) {
            response = (Response) objects[1];
        }

        request.populateRequest(i8SBSwitchControllerRequestVO);
        if (request.validateRequest()) {
            logger.info("Request Validate For RRN " + i8SBSwitchControllerRequestVO.getRRN());
            String requestJSON = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestJSON);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Tasdeeq_AuthenticateUpdated)) {
                response = tasdeeqService.authenticateUpdatedResponse((AuthenticateUpdatedRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Tasdeeq_CustomAnalytics)) {
                response = tasdeeqService.customAnalyticsResponse((CustomAnalyticsRequest) request);
            }
            logger.info("I8SB Response back to Microbank after 30s");
            if (response.populateI8SBSwitchControllerResponseVO() != null)
                i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseXML = JSONUtil.getJSON(i8SBSwitchControllerResponseVO);
            i8SBSwitchControllerResponseVO.setResponseXML(responseXML);
        } else {
            logger.info("[FAILED] Request validation failed for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        }


        return i8SBSwitchControllerResponseVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
        return null;
    }

    @Override
    public I8SBSwitchControllerRequestVO generateSystemTraceableInfo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransmissionDateAndTime())) {
            i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(DateFormatEnum.TRANSACTION_DATE.getValue()));
        }

        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
            i8SBSwitchControllerRequestVO.setSTAN(DateUtil.formatCurrentDate(DateFormatEnum.TIME_LOCAL_TRANSACTION.getValue()));
        }

        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
            i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        }

        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionId())) {
            i8SBSwitchControllerRequestVO.setTransactionId(i8SBSwitchControllerRequestVO.getRRN());
        }

        return i8SBSwitchControllerRequestVO;
    }

    private Object[] initializeRequestAndResponseObjects(String requestType) {
        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Tasdeeq_AuthenticateUpdated)) {

            request = new AuthenticateUpdatedRequest();
            response = new AuthenticateUpdatedResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Tasdeeq_CustomAnalytics)) {

            request = new CustomAnalyticsRequest();
            response = new CustomAnalyticsResponse();

        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
