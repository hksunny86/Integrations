package com.inov8.integration.channel.microbank.bo;

import com.inov8.integration.channel.microbank.request.AMARequest.*;
import com.inov8.integration.channel.microbank.request.AMARequest.AccountRegistrationRequest;
import com.inov8.integration.channel.microbank.request.BillingRequest.BillPaymentRequest;
import com.inov8.integration.channel.microbank.request.BopSuperAgent.*;
import com.inov8.integration.channel.microbank.request.BillingRequest.*;
import com.inov8.integration.channel.microbank.request.scorequest.ScoUssdRequest;
import com.inov8.integration.channel.microbank.request.tutuka.*;
import com.inov8.integration.channel.microbank.response.AMAResponse.*;
import com.inov8.integration.channel.microbank.response.AMAResponse.AccountRegistrationResponse;
import com.inov8.integration.channel.microbank.response.BillingResponse.BillPaymentResponse;
import com.inov8.integration.channel.microbank.response.BopSuperAgent.*;
import com.inov8.integration.channel.microbank.response.BillingResponse.*;
import com.inov8.integration.channel.microbank.response.scoresponse.ScoUssdResponse;
import com.inov8.integration.channel.microbank.response.tutuka.*;
import com.inov8.integration.channel.microbank.response.*;
import com.inov8.integration.channel.microbank.request.*;
import com.inov8.integration.channel.microbank.service.MicrobankService;
import com.inov8.integration.controller.I8SBChannelInterface;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.util.CommonUtils;
import com.inov8.integration.util.DateUtil;
import com.inov8.integration.util.JSONUtil;
import com.inov8.integration.util.XMLUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.inov8.integration.enums.DateFormatEnum.TRANSACTION_DATE;

/**
 * Created by inov8 on 11/8/2017.
 */
@Component
public class MicrobankBO implements I8SBChannelInterface {
    private static Logger logger = LoggerFactory.getLogger(MicrobankBO.class.getSimpleName());

    @Autowired(required = false)
    MicrobankService microbankService;


    public I8SBSwitchControllerResponseVO execute(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws Exception {
        Object[] objects = this.initializeRequestAndResponseObjects(i8SBSwitchControllerRequestVO.getRequestType(), i8SBSwitchControllerRequestVO);
        Request request = null;
        Response response = null;
        if (objects[0] != null) {
            request = (Request) objects[0];
        }
        if (objects[1] != null) {
            response = (Response) objects[1];
        }
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        logger.info("Populating Request for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        request.populateRequest(i8SBSwitchControllerRequestVO);
        logger.info("Validating Request for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
        if (request.validateRequest(i8SBSwitchControllerRequestVO)) {
            logger.info("Request validated for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
            if (i8SBSwitchControllerRequestVO.getRequestType().equals("CashOut")||i8SBSwitchControllerRequestVO.getRequestType().equals("AccountRegistrationBop")) {

                String requestXML = JSONUtil.getJSON(i8SBSwitchControllerRequestVO);
                i8SBSwitchControllerRequestVO.setRequestXML(requestXML);
            }
            i8SBSwitchControllerResponseVO = microbankService.sendRequest(i8SBSwitchControllerRequestVO);

            logger.info("Parsing Response for RRN: " + i8SBSwitchControllerRequestVO.getRRN());
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
        // generate RRN and other info here based of channel id
        i8SBSwitchControllerRequestVO.setTransmissionDateAndTime(DateUtil.formatCurrentDate(TRANSACTION_DATE.getValue()));
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getSTAN())) {
            i8SBSwitchControllerRequestVO.setSTAN(CommonUtils.generateSTAN());
        }
        // 20 digit number
        if (StringUtils.isEmpty(i8SBSwitchControllerRequestVO.getRRN())) {
            i8SBSwitchControllerRequestVO.setRRN(i8SBSwitchControllerRequestVO.getSTAN() + i8SBSwitchControllerRequestVO.getTransmissionDateAndTime());
        }
        return i8SBSwitchControllerRequestVO;
    }


    private Object[] initializeRequestAndResponseObjects(String requestType, I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        Object[] objects = new Object[2];
        Request request = null;
        Response response = null;
        logger.info("Request type: " + requestType);
        if (i8SBSwitchControllerRequestVO.getI8sbGateway().equals(I8SBConstants.I8SB_Gateway_XMLRPC_Billing)) {
            if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Billing_BillCategory)) {
                request = new BillCategoryRequest();
                response = new BillCategoryResponse();
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Billing_BillCategoryProducts)) {
                request = new BillCategoryProductRequest();
                response = new BillCategoryProductResponse();
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Billing_BillInquiry)) {
                request = new BillinquiryRequest();
                response = new BillinquiryResponse();
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Billing_BillPayment)) {
                request = new BillPaymentRequest();
                response = new BillPaymentResponse();
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Billing_BillStatus)) {
                request = new BillStatusRequest();
                response = new BillStatusResponse();
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Billing_PaidBillSummary)) {
                request = new PaidBillStatusRequest();
                response = new PaidBillStatusResponse();
            } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Billing_FailedBillSummary)) {
                request = new FailedBillSummaryRequest();
                response = new FailedBillSummaryResponse();
            }
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LoadAdjustment)) {
            request = new LoadAdjustmentRequest();

            response = new LoadAdjustmentResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_LoadReversal)) {
            request = new LoadReversalRequest();
            response = new LoadReversalResponse();
        } else if (requestType.equals(I8SBConstants.RequestType_JSBLB_SEO_USSD)) {
            request = new ScoUssdRequest();
            response = new ScoUssdResponse();
        } else if (requestType.equals(I8SBConstants.RequestType_AccountRegistration)) {
            request = new AccountRegistrationRequest();
            response = new AccountRegistrationResponse();
        } else if (requestType.equals(I8SBConstants.RequestType_SetMPin)) {
            request = new SetMPinRequest();
            response = new SetMPinResponse();
        } else if (requestType.equals(I8SBConstants.RequestType_ChangeMPin)) {
            request = new ChangeMpinRequest();
            response = new ChangeMPinResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AccountBalanceInquiry)) {
            request = new BalanceInquiryRequest();
            response = new BalanceInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_Ministatement)) {
            request = new MinistatementRequest();
            response = new MinistatementResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BillInquiry)) {
            request = new BillInquiryRequest();
            response = new BillInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BillPayment)) {
            request = new com.inov8.integration.channel.microbank.request.AMARequest.BillPaymentRequest();
            response = new com.inov8.integration.channel.microbank.response.AMAResponse.BillPaymentResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_AccountClose)) {
            request = new CloseAccountRequest();
            response = new CloseAccountResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_TitleFetch)) {
            request = new LocalFundTitleFetchRequest();
            response = new LocalFundTitleFetchResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_FundsTransfer)) {
            request = new LocalFundTransferRequest();
            response = new LocalFundTransferResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_InterBankFundTransfer)) {
            request = new InterBankFundTransferRequest();
            response = new InterBankFundTransferResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_IBFTTitleFetch)) {
            request = new InterBankFundTransferTitleFetchRequest();
            response = new InterBankFundTransferTitleFetchResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_BalanceInquiry)) {
            request = new BalanceInquiryRequest();
            response = new BalanceInquiryResponse();
        } else if (requestType.equals(I8SBConstants.RequestType_BOP_MPinReset)) {
            request = new ChangeMpinRequest();
            response = new ChangeMPinResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashOutInquiry)) {
            request = new CashOutInquiry();
            response = new CashOutInquiryResponse();

        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashOut)) {
            request = new CashOut();
            response = new CashOutResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_AccountRegistrationInquiry)) {
            request = new AccountRegistrationInquiryRequest();
            response = new AccountRegistrationInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_AccountRegistration)) {
            request = new com.inov8.integration.channel.microbank.request.BopSuperAgent.AccountRegistrationRequest();
            response = new AccountRegistrationInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CashWithdrawalReversal)) {
            request = new CashWithdrawalReversalRequest();
            response = new CashWithdrawalReversalResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_ProofOFLifeVerficationInquiry)) {
            request = new ProofOfLifeVerificationInquiryRequest();
            response = new ProofOfLifeVerificationInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_ProofOfLifeVerification)) {
            request = new ProofOfLifeVerificationRequest();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CardIssuanceReIssuanceInquiry)) {
            request = new AccountRegistrationInquiryRequest();
            response = new AccountRegistrationInquiryResponse();
        } else if (requestType.equalsIgnoreCase(I8SBConstants.RequestType_BOP_CardIssuanceReIssuance)) {
            request = new AccountRegistrationRequest();
            response = new AccountRegistrationResponse();
        } else {
            logger.info("[FAILED] Request type not supported");
            throw new I8SBValidationException("Request type not supported");
        }
        objects[0] = request;
        objects[1] = response;
        return objects;
    }
}
