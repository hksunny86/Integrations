package com.inov8.integration.channel.BOPBLB.bo;


import com.inov8.integration.channel.BOPBLB.mock.BopCashOutMock;
import com.inov8.integration.channel.BOPBLB.request.*;
import com.inov8.integration.channel.BOPBLB.response.*;
import com.inov8.integration.channel.BOPBLB.service.BopCashOutService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BopBlbCashOutBo implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(BopBlbCashOutBo.class.getSimpleName());

    @Autowired
    BopCashOutService bopCashOutService;


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
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        request.populateRequest(i8SBSwitchControllerRequestVO);
        if (request.validateRequest()) {

            logger.info("Request validated for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            String requestXML = "";
            requestXML = JSONUtil.getJSON(request);
            i8SBSwitchControllerRequestVO.setRequestXML(requestXML);
            bopCashOutService.setI8SBSwitchControllerRequestVO(i8SBSwitchControllerRequestVO);
            String requestType = i8SBSwitchControllerRequestVO.getRequestType();
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashOutInquiry)) {
                response = bopCashOutService.cashOutInquiryResonse((CashOutInquiryRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashOut)) {
                response = bopCashOutService.cashOutResponse((CashOutRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashWithdrawalReversal)) {
                response = bopCashOutService.cashWithdrawlReversalResponse((CashWithdrawlReversalRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_AccountRegistrationInquiry)) {
                response = bopCashOutService.accountRegistrationInquiryResponse((AccountRegistrationInquiryRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_AccountRegistration)) {
                response = bopCashOutService.accountRegistrationResponse((AccountRegistrationRequest) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CardIssuanceReIssuanceInquiry)) {
                response = bopCashOutService.cardissuaneInquiry((CardIssuanceReissuanceInquiry) request);
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CardIssuanceReIssuance)) {
                response = bopCashOutService.cardIssuaneReissuance((CardIssuanceReissuanceRequest) request);
            }
            else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_ProofOFLifeVerficationInquiry)) {
                response = bopCashOutService.proofOfLifeVerificationInquiry((ProofOfLifeVerificationInquiryRequest) request);
            }else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_ProofOfLifeVerification)) {
                response = bopCashOutService.proofOfLifeVerification((ProofOfLifeVerificationRequest) request);
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

        if (i8SBSwitchControllerResponseVO != null) {
            i8SBSwitchControllerRequestVO.setAccessToken(i8SBSwitchControllerResponseVO.getI8SBSwitchControllerResponseVOList().get(0).getAccessToken());

        }

        return i8SBSwitchControllerRequestVO;
    }

    @Override
    public I8SBSwitchControllerRequestVO generateSystemTraceableInfo(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        return null;
    }

    private Object[] initializeRequestAndResponseObjects(String requestType) throws I8SBValidationException {

        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashOutInquiry)) {
            request = new CashOutInquiryRequest();
            response = new CashOutInquiryResonse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashOut)) {
            request = new CashOutRequest();
            response = new CashOutResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashWithdrawalReversal)) {
            request = new CashWithdrawlReversalRequest();
            response = new CashWithdrawlReversalResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_AccountRegistrationInquiry)) {
            request = new AccountRegistrationInquiryRequest();
            response = new AccountRegistrationInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_AccountRegistration)) {
            request = new AccountRegistrationRequest();
            response = new AccountRegistrationResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CardIssuanceReIssuanceInquiry)) {
            request = new CardIssuanceReissuanceInquiry();
            response = new AccountRegistrationInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CardIssuanceReIssuance)) {
            request = new CardIssuanceReissuanceRequest();
            response = new AccountRegistrationResponse();
        }
        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_ProofOFLifeVerficationInquiry)) {
            request = new ProofOfLifeVerificationInquiryRequest();
            response = new ProofOfLifeVerificationInquiryResponse();
        }
        else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_ProofOfLifeVerification)) {
            request = new ProofOfLifeVerificationRequest();
            response = new ProofOfLifeVerificationResponse();
        }
        //
        else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;

    }
}
