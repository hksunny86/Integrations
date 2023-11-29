package com.inov8.integration.channel.wasa.bo;

import com.inov8.integration.channel.wasa.request.*;
import com.inov8.integration.channel.wasa.response.*;
import com.inov8.integration.channel.wasa.service.WasaService;
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

import java.util.Objects;

@Component
public class WasaBo implements I8SBChannelInterface {

    @Autowired
    WasaService wasaService;
    private static Logger logger = LoggerFactory.getLogger(WasaBo.class.getSimpleName());

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
        Objects.requireNonNull(request).populateRequest(i8SBSwitchControllerRequestVO);
        if (request.validateRequest()) {
            logger.info("Request Validate For RRN " + i8SBSwitchControllerRequestVO.getRRN());
            String requestJSON = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestJSON);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            wasaService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_LOGIN)) {
                response = wasaService.wasaLoginResponse((WasaLoginRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_GET_BILL)) {
                response = wasaService.wasaGetBillResponse((WasaGetBillRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_POST_BILL)) {
                response = wasaService.wasaPostBillResponse((WasaPostBillRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_REVERSAL_BILL)) {
                response = wasaService.wasaBillReversalResponse((WasaBillReversalRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_CLEARANCE_BILL)) {
                response = wasaService.wasaBillClearanceResponse((WasaBillClearanceRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_LOGOUT)) {
                response = wasaService.wasaLogoutResponse((WasaLogoutRequest) request);
            }


            if (Objects.requireNonNull(response).populateI8SBSwitchControllerResponseVO() != null)
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
        if (i8SBSwitchControllerResponseVO != null) {
            i8SBSwitchControllerRequestVO.setAccessToken(i8SBSwitchControllerResponseVO.getI8SBSwitchControllerResponseVOList().get(0).getAccessToken());
        }
        return i8SBSwitchControllerRequestVO;
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
            i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getSTAN());
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
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_LOGIN)) {
            request = new WasaLoginRequest();
            response = new WasaLoginResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_GET_BILL)) {
            request = new WasaGetBillRequest();
            response = new WasaGetBillResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_POST_BILL)) {
            request = new WasaPostBillRequest();
            response = new WasaPostBillResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_REVERSAL_BILL)) {
            request = new WasaBillReversalRequest();
            response = new WasaBillReversalResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_CLEARANCE_BILL)) {
            request = new WasaBillClearanceRequest();
            response = new WasaBillClearanceResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_WASA_LOGOUT)) {
            request = new WasaLogoutRequest();
            response = new WasaLogoutResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
