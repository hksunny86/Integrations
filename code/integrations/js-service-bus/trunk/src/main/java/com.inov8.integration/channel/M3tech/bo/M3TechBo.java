package com.inov8.integration.channel.M3tech.bo;

import com.inov8.integration.channel.CLSJS.bo.CLSJSBo;
import com.inov8.integration.channel.CLSJS.request.ImportScreeningRequest;
import com.inov8.integration.channel.CLSJS.response.ScreeningResponse;
import com.inov8.integration.channel.CLSJS.service.CLSJSService;
import com.inov8.integration.channel.M3tech.Request.Request;
import com.inov8.integration.channel.M3tech.Request.SendSmsRequest;
import com.inov8.integration.channel.M3tech.Response.Response;
import com.inov8.integration.channel.M3tech.Response.SendSms;
import com.inov8.integration.channel.M3tech.service.M3TechService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
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

import static com.inov8.integration.enums.DateFormatEnum.TIME_LOCAL_TRANSACTION;
import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;
@Component
public class M3TechBo implements I8SBChannelInterface {

    private static Logger logger = LoggerFactory.getLogger(M3TechBo.class.getSimpleName());

    @Autowired
    M3TechService m3TechService;

    @Override
    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType());
        com.inov8.integration.channel.M3tech.Request.Request request = null;
        com.inov8.integration.channel.M3tech.Response.Response response = null;
        if (objects[0] != null) {
            request = (Request) objects[0];
        }
        if (objects[1] != null) {
            response = (Response) objects[1];
        }
        request.populateRequest(i8SBSwitchControllerRequestVO);
        logger.info("Valiadate Request For RRN" + i8SBSwitchControllerRequestVO.getRRN());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = null;
        if (request.validateRequest()) {
            logger.info("Request Validate For RRN" + i8SBSwitchControllerRequestVO.getRRN());

            String requestType = i8SBSwitchControllerRequestVO.getRequestType();

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_SendSMS)) {
                response = m3TechService.sendSms((SendSmsRequest) request);
            }

            if (response.populateI8SBSwitchControllerResponseVO() != null)
                i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseXML = JSONUtil.getJSON(response);
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
            i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
            i8SBSwitchControllerRequestVO.setSTAN(DateUtil.formatCurrentDate(TIME_LOCAL_TRANSACTION.getValue()));
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
        com.inov8.integration.channel.M3tech.Request.Request request = null;
        com.inov8.integration.channel.M3tech.Response.Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_SendSMS)) {
            request = new SendSmsRequest();
            response = new SendSms();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }

        objects[0] = request;
        objects[1] = response;

        return objects;
    }
}
