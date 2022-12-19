package com.inov8.integration.channel.APIGEE.bo;

import com.inov8.integration.channel.APIGEE.request.CardDetail.CardDetailRequest;
import com.inov8.integration.channel.APIGEE.request.HRA.PayMTCNRequest;
import com.inov8.integration.channel.APIGEE.request.HRA.PaymtcnAccessToken;
import com.inov8.integration.channel.APIGEE.request.MPIN.AtmPinGenerationRequest;
import com.inov8.integration.channel.APIGEE.request.Request;
import com.inov8.integration.channel.APIGEE.request.ThirdPartyCashOut.*;
import com.inov8.integration.channel.APIGEE.response.CardDetail.CardDetailResponse;
import com.inov8.integration.channel.APIGEE.response.HRA.PayMTCNResponse;
import com.inov8.integration.channel.APIGEE.response.HRA.PaymtcnAccessTokenResponse;
import com.inov8.integration.channel.APIGEE.response.MPIN.AtmPinGenerationResponse;
import com.inov8.integration.channel.APIGEE.response.Response;
import com.inov8.integration.channel.APIGEE.response.ThirdPartyCashOut.*;
import com.inov8.integration.channel.APIGEE.service.APIGEEService;
import com.inov8.integration.controller.I8SBChannelInterface;
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

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

/**
 * Created by inov8 on 5/28/2018.
 */
@Component

public class APIGEEBO implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(APIGEEBO.class.getSimpleName());
    @Autowired
    APIGEEService apigeeService;

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

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        request.populateRequest(i8SBSwitchControllerRequestVO);
        if (request.validateRequest()) {

            logger.info("Request validated for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            String requestXML = "";
            if (!i8SBSwitchControllerRequestVO.getRequestType().equalsIgnoreCase(I8SBConstants.RequestType_EOBI_AccessToken))
                requestXML = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestXML);
            apigeeService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_PayMTCN)) {
                response = apigeeService.sendPayMTCNRequest((PayMTCNRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_PayMtcnAccessToken)) {
                response = apigeeService.sendPayMtcnAccessTokenRequest((PaymtcnAccessToken) request);

            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AccountBalanceInquiry)) {
                response = apigeeService.balanceInquiryRequest((BalanceInquiryRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_CashWithdrawal)) {
                response = apigeeService.sendCashWithdrawlRequest((CashWithdrawlRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_CashWithdrawalReversal)) {
                response = apigeeService.SendCashWithdrawlReversalRequest((CashWithdrawlReversalRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AccessToken)) {
                response = apigeeService.sendAccessTokenRequest((AccessTokenRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EOBI_TitleFetch)) {
                response = apigeeService.sendTitleFetchRequest((TitleFetchRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EOBI_AccessToken)) {
                response = apigeeService.sendEobiAccessTokenRequest((EobiAccessTokenRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EOBI_CashWithdrawal)) {
                response = apigeeService.sendMoneyTransferresponse((MoneyTransferRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AgentVerification)) {
                response = apigeeService.agentVerificaionResponse((AgentVerificationRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_ATM_PIN_GENERATION)) {
                response = apigeeService.atmPinGenerationResponse((AtmPinGenerationRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_GET_CARD_DETAILS)) {
                response = apigeeService.getCardDetailResponse((CardDetailRequest) request);
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
    public I8SBSwitchControllerRequestVO prepareRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO,
                                                        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) {
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
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
            i8SBSwitchControllerRequestVO.setSTAN(CommonUtils.generateSTAN());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
            i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        }
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getTransactionId())) {
            i8SBSwitchControllerRequestVO.setTransactionId(i8SBSwitchControllerRequestVO.getRRN());
        }
        return i8SBSwitchControllerRequestVO;
    }

    private Object[] initializeRequestAndResponseObjects(String requestType) throws I8SBValidationException {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_PayMTCN)) {
            request = new PayMTCNRequest();
            response = new PayMTCNResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_PayMtcnAccessToken)) {
            request = new PaymtcnAccessToken();
            response = new PaymtcnAccessTokenResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AccountBalanceInquiry)) {
            request = new BalanceInquiryRequest();
            response = new BalanceInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_CashWithdrawal)) {
            request = new CashWithdrawlRequest();
            response = new CashWithdrawlResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_CashWithdrawalReversal)) {
            request = new CashWithdrawlReversalRequest();
            response = new CashWithdrawlReversalResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AccessToken)) {
            request = new AccessTokenRequest();
            response = new AccesTokenResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EOBI_TitleFetch)) {
            request = new TitleFetchRequest();
            response = new TitlefetchResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EOBI_AccessToken)) {
            request = new EobiAccessTokenRequest();
            response = new EobiAccessTokenResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_EOBI_CashWithdrawal)) {
            request = new MoneyTransferRequest();
            response = new MoneyTransferResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AgentVerification)) {
            request = new AgentVerificationRequest();
            response = new AgentVerificaionResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_ATM_PIN_GENERATION)) {
            request = new AtmPinGenerationRequest();
            response = new AtmPinGenerationResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_GET_CARD_DETAILS)) {
            request = new CardDetailRequest();
            response = new CardDetailResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
