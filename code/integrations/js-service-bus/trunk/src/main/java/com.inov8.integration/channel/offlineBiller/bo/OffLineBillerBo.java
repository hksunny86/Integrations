package com.inov8.integration.channel.offlineBiller.bo;


import com.inov8.integration.channel.merchantDiscountCamping.request.TransactionUpdateRequest;

import com.inov8.integration.channel.merchantDiscountCamping.response.TransactionUpdateResponse;
import com.inov8.integration.channel.merchantDiscountCamping.response.TransactionValidationResponse;
import com.inov8.integration.channel.offlineBiller.response.BillInquiryResponse;
import com.inov8.integration.channel.offlineBiller.response.BillPaymentResponse;
import com.inov8.integration.channel.offlineBiller.response.Response;
import com.inov8.integration.channel.offlineBiller.resquest.BillInquiryRequest;
import com.inov8.integration.channel.offlineBiller.resquest.BillPaymentRequest;
import com.inov8.integration.channel.offlineBiller.resquest.Request;
import com.inov8.integration.channel.offlineBiller.service.OffLineBillerService;
import com.inov8.integration.controller.I8SBChannelInterface;
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
public class OffLineBillerBo implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(OffLineBillerBo.class.getSimpleName());

    @Autowired
    OffLineBillerService offLineBillerService;

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
        request.populateRequest(i8SBSwitchControllerRequestVO);
        logger.info("Valiadate Request For RRN" + i8SBSwitchControllerRequestVO.getRRN());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = null;
        if (request.validateRequest()) {
            logger.info("Request Validate For RRN" + i8SBSwitchControllerRequestVO.getRRN());

            String requestType = i8SBSwitchControllerRequestVO.getRequestType();

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BillInquiry)) {
                response = offLineBillerService.billInquiryResponse((BillInquiryRequest) request);
            }
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BillPayment)) {
                response = offLineBillerService.billPaymentResponse((BillPaymentRequest) request);

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
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BillInquiry)) {
            request = new BillInquiryRequest();
            response = new BillInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BillPayment)) {
            request = new BillPaymentRequest();
            response = new BillPaymentResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }

        objects[0] = request;
        objects[1] = response;

        return objects;
    }
}
