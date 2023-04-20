package com.inov8.integration.channel.optasia.bo;

import com.inov8.integration.channel.optasia.request.*;
import com.inov8.integration.channel.optasia.response.*;
import com.inov8.integration.channel.optasia.service.OptasiaService;
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
public class OptasiaBo implements I8SBChannelInterface {

    @Autowired
    OptasiaService optasiaService;
    private static Logger logger = LoggerFactory.getLogger(OptasiaBo.class.getSimpleName());

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

            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_ECIB_DATA)) {
                response = optasiaService.sendEcibDataResponse((ECIBDataRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_OfferListForCommodity)) {
                response = optasiaService.sendOfferListForCommodityResponse((OfferListForCommodityRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_LOANOFFER)) {
                response = optasiaService.sendLoanOfferResponse((LoanOfferRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_CALLBACK)) {
                response = optasiaService.sendCallBackResponse((CallBackRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_LOANS)) {
                response = optasiaService.sendLoansResponse((LoansRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_PROJECTION)) {
                response = optasiaService.sendInitiateLoanResponse((InitiateLoanRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_OUTSTANDING)) {
                response = optasiaService.sendOutstandingResponse((OutstandingRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_TRANSACTIONS)) {
                response = optasiaService.sendTransactionStatusResponse((TransactionStatusRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_LOANSUMMARY)) {
                response = optasiaService.sendLoanStatusResponse((LoanStatusRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_PAYLOAN)) {
                response = optasiaService.sendLoanPaymentResponse((LoanPaymentRequest) request);
            }

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
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_ECIB_DATA)) {

            request = new ECIBDataRequest();
            response = new ECIBDataResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_OfferListForCommodity)) {

            request = new OfferListForCommodityRequest();
            response = new OfferListForCommodityResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_LOANOFFER)) {

            request = new LoanOfferRequest();
            response = new LoanOfferResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_CALLBACK)) {

            request = new CallBackRequest();
            response = new CallBackResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_LOANS)) {

            request = new LoansRequest();
            response = new LoansResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_PROJECTION)) {

            request = new InitiateLoanRequest();
            response = new InitiateLoanResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_OUTSTANDING)) {

            request = new OutstandingRequest();
            response = new OutstandingResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_TRANSACTIONS)) {

            request = new TransactionStatusRequest();
            response = new TransactionStatusResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_LOANSUMMARY)) {

            request = new LoanStatusRequest();
            response = new LoanStatusResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_OPTASIA_PAYLOAN)) {

            request = new LoanPaymentRequest();
            response = new LoanPaymentResponse();

        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
