package com.inov8.integration.channel.T24Api.bo;

import com.inov8.integration.channel.T24Api.request.*;
import com.inov8.integration.channel.T24Api.response.CreditPaymentResponse;
import com.inov8.integration.channel.T24Api.response.IbftResponse;
import com.inov8.integration.channel.T24Api.response.IbftTitleFetchResponse;
import com.inov8.integration.channel.T24Api.response.Response;
import com.inov8.integration.channel.T24Api.service.T24ApiService;

import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.CommonUtils;
import com.inov8.integration.util.DateUtil;
import com.inov8.integration.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.inov8.integration.enums.DateFormatEnum.*;

@Component
public class T24ApiBo implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(T24ApiBo.class.getSimpleName());


    @Autowired
    T24ApiService t24ApiService;

    @Override
    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType());
        Request request = null;
        Response response = null;
        if (objects[0] != null) {
            request = (Request) objects[0];
        }
        if (objects[1] != null) {
            response = (Response) objects[1];
        }

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = null;
        request.populateRequest(i8SBSwitchControllerRequestVO);
        logger.info("Validating request for RRN: " + i8SBSwitchControllerRequestVO.getRRN());

        if (request.validateRequest()) {
            logger.info("Request Validate For RRN" + i8SBSwitchControllerRequestVO.getRRN());
            String requestJSON = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestJSON);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            t24ApiService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_IBFTTitleFetch)) {
                response = t24ApiService.ibftTitleFetchResponse((IbftTitleFetchRequest) request);

            }
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_InterBankFundTransfer)) {
                response = t24ApiService.ibftResponse((IbftRequest) request);

            }
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_CreditPayment)) {
                response = t24ApiService.creditPaymentResponse((CreditPaymentRequest) request);

            }

            if (response.populateI8SBSwitchControllerResponseVO() != null)
                i8SBSwitchControllerResponseVO = response.populateI8SBSwitchControllerResponseVO();
            String responseJSON = JSONUtil.getJSON(i8SBSwitchControllerResponseVO);
            i8SBSwitchControllerResponseVO.setResponseXML(responseJSON);
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
            i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTimeLocalTransaction())) {
            i8SBSwitchControllerRequestVO.setTimeLocalTransaction(DateUtil.formatCurrentDate(TIME_LOCAL_TRANSACTION.getValue()));
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getDateLocalTransaction())) {
            i8SBSwitchControllerRequestVO.setDateLocalTransaction(DateUtil.formatCurrentDate(T24_DATE_LOCAL_TRANSACTION.getValue()));
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
            i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionId())) {
            i8SBSwitchControllerRequestVO.setTransactionId(i8SBSwitchControllerRequestVO.getRRN());
        }
        return i8SBSwitchControllerRequestVO;
    }

    private Object[] initializeRequestAndResponseObjects(String requestType) throws Exception {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);

        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_IBFTTitleFetch)) {
            request = new IbftTitleFetchRequest();
            response = new IbftTitleFetchResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_InterBankFundTransfer)) {
            request = new IbftRequest();
            response = new IbftResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_FundsTransferReversal)) {
            request = new IbftReversalRequest();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_CreditPayment)) {
            request = new CreditPaymentRequest();
            response = new CreditPaymentResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }

        objects[0] = request;
        objects[1] = response;
        return objects;

    }
}
